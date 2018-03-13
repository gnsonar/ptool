package com.in.fujitsu.pricing.storage.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.DealResultDto;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.specification.StorageSpecification;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageUnitPriceInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;
import com.in.fujitsu.pricing.storage.repository.StorageRepository;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PerformanceDealResultsHelper {

	@Autowired
	private StorageRepository storageRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	StorageCommonHelper commonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";
	private static String PERFORMANCE = "PERFORMANCE";

	/**
	 * @param storageInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDeals(StorageInfo storageInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgDevices = commonHelper
				.getPerformanceAverageVolume(storageInfo.getStorageYearlyDataInfos());
		// Past Deal Calculation
		Specification<StorageInfo> specification = StorageSpecification.specificationForPerformancePastDeal(
				storageInfo.isOffshoreAllowed(), storageInfo.getServiceWindowSla(), storageInfo.isIncludeHardware(),
				avgDevices, towerSpecificBandInfo.getBandPercentage());
		List<StorageInfo> pastDealResults = storageRepository.findAll(specification);
		log.info("Performance dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, PERFORMANCE);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageDevicesVolume(pastDealResults);
			absVolumeDiffPercMap = commonHelper.prepareDealAbsVolumeDiff(avgDevices, dealAvgVolumeMap);
			int devicesVolume = getDevicesVolume(storageInfo.getStorageYearlyDataInfos());
			averageUnitPriceMap = prepareDealAverageDevicesUnitPrice(storageInfo, pastDealResults,
					devicesVolume);

			Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap = CommonMapUtils.sortByValueAsc(absVolumeDiffPercMap);
			Map<Long, BigDecimal> sortedAverageUnitPriceMap = CommonMapUtils.sortByValueDesc(averageUnitPriceMap);

			setNearestDealInResult(pastDealResults, dealResultsResponse, dealAvgVolumeMap, sortedAbsVolumeDiffPercMap,
					sortedAverageUnitPriceMap);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return dealResultsResponse;
	}

	/**
	 * @param pastDealResults
	 * @param dealResultsResponse
	 * @param dealAvgVolumeMap
	 * @param sortedAbsVolumeDiffPercMap
	 * @param sortedAverageUnitPriceMap
	 */
	private void setNearestDealInResult(List<StorageInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (StorageInfo pastDealStorageInfo : pastDealResults) {
				DealInfo pastDeallInfo = pastDealStorageInfo.getDealInfo();
				if (expensiveDealId != null && expensiveDealId == pastDeallInfo.getDealId()) {
					DealResultDto expensiveDealDto = prepareDealResultDto(dealAvgVolumeMap, sortedAverageUnitPriceMap,
							expensiveDealId, pastDeallInfo);
					dealResultsResponse.setExpensiveDeal(expensiveDealDto);
				}

				if (cheapestDealId != null && cheapestDealId == pastDeallInfo.getDealId()) {
					DealResultDto cheapestDealDto = prepareDealResultDto(dealAvgVolumeMap, sortedAverageUnitPriceMap,
							cheapestDealId, pastDeallInfo);
					dealResultsResponse.setCheapestDeal(cheapestDealDto);
				}

				for (Long nearestDealKey : nearestDealMapInVolume.keySet()) {
					if (nearestDealKey != null && nearestDealKey == pastDeallInfo.getDealId()) {
						DealResultDto nearestDealDto = prepareDealResultDto(dealAvgVolumeMap, sortedAverageUnitPriceMap,
								nearestDealKey, pastDeallInfo);
						nearestDealsInVolume.add(nearestDealDto);

					}
				}

			}
			dealResultsResponse.setNearestInVolumeDeals(nearestDealsInVolume);
		}
	}

	/**
	 * @param dealAvgVolumeMap
	 * @param sortedAverageUnitPriceMap
	 * @param dealId
	 * @param pastDeallInfo
	 * @return
	 */
	private DealResultDto prepareDealResultDto(Map<Long, BigDecimal> dealAvgVolumeMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap, Long dealId, DealInfo pastDeallInfo) {
		DealResultDto resultDto = new DealResultDto();
		resultDto.setClientName(pastDeallInfo.getClientName());
		resultDto.setDealName(pastDeallInfo.getDealName());
		resultDto.setDealId(pastDeallInfo.getDealId());
		resultDto.setDealStatus(pastDeallInfo.getDealStatus());
		resultDto.setDealTerm(pastDeallInfo.getDealTerm());
		resultDto.setFinancialEngineer(pastDeallInfo.getFinancialEngineer());
		resultDto.setAveragePrice(sortedAverageUnitPriceMap.get(dealId));
		resultDto.setAverageVolume(dealAvgVolumeMap.get(dealId));
		return resultDto;
	}

	/**
	 * @param pastDealResults
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageDevicesVolume(List<StorageInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (StorageInfo StorageInfo : pastDealResults) {
				Long dealId = StorageInfo.getDealInfo().getDealId();
				BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int devicesVolume = 0;
				int size = 0;
				for (StorageYearlyDataInfo yearlyDataInfo : StorageInfo.getStorageYearlyDataInfos()) {
					if (yearlyDataInfo.getPerformanceStorage() != 0) {
						devicesVolume += yearlyDataInfo.getPerformanceStorage();
						size++;
					}
				}
				if (size != 0) {
					avgVolume = new BigDecimal(devicesVolume / size);
				}
				yearlyAvgVolumeMap.put(dealId, avgVolume);
			}
		}
		return yearlyAvgVolumeMap;

	}

	/**
	 * @param assessmentStorageInfo
	 * @param pastDealResults
	 * @param devicesVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageDevicesUnitPrice(StorageInfo assessmentStorageInfo,
			List<StorageInfo> pastDealResults, int devicesVolume) {
		Map<Long, BigDecimal> averageRevenueMap = new HashMap<>();
		for (StorageInfo pastStorageInfo : pastDealResults) {
			BigDecimal devicesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (StorageYearlyDataInfo pastYearlyDataInfo : pastStorageInfo.getStorageYearlyDataInfos()) {
				for (StorageUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getStorageUnitPriceInfo()) {
					for (StorageYearlyDataInfo assessmentYearlyDataInfo : assessmentStorageInfo.getStorageYearlyDataInfos()) {
						if (pastUnitPriceInfo.getStorageYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getStorageYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								devicesRevenue = devicesRevenue.add(pastUnitPriceInfo.getPerformanceUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getPerformanceStorage())));
								break;
							}
						}
					}
				}
			}
			BigDecimal avgUnitPrice = devicesRevenue.divide(new BigDecimal(devicesVolume),2,BigDecimal.ROUND_CEILING);
			averageRevenueMap.put(pastStorageInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return averageRevenueMap;
	}

	/**
	 * @param storageYearlyDataInfoList
	 * @return
	 */
	public int getDevicesVolume(List<StorageYearlyDataInfo> storageYearlyDataInfoList) {
		int devicesVolume = 0;
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoList)) {
			for (StorageYearlyDataInfo storageYearlyDataInfo : storageYearlyDataInfoList) {
				devicesVolume += storageYearlyDataInfo.getPerformanceStorage();
			}
		}
		return devicesVolume;
	}

	/**
	 * @param assessmentStorageInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDeals(StorageInfo assessmentStorageInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgDevices = commonHelper
				.getPerformanceAverageVolume(assessmentStorageInfo.getStorageYearlyDataInfos());
		int devicesVolume = getDevicesVolume(assessmentStorageInfo.getStorageYearlyDataInfos());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		if(devicesVolume != 0){
			prepareSelectedAndLowBenchmarkDealResult(assessmentStorageInfo, assessmentDealTerm, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, avgDevices, dealResultsResponse, devicesVolume);

			prepareHighBenchmarkDealResult(assessmentStorageInfo, assessmentDealTerm, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, avgDevices, dealResultsResponse, devicesVolume);
		}
		return dealResultsResponse;
	}


	/**
	 * @param assessmentStorageInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgDevices
	 * @param dealResultsResponse
	 * @param volume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(StorageInfo assessmentStorageInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgDevices,
			DealResultsResponse dealResultsResponse, int volume) {
		List<StorageInfo> benchMarkLowAndSelectedDealResults = storageRepository.findLowBenchMarkDealForPerformance(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentStorageInfo.isOffshoreAllowed(),
				assessmentStorageInfo.isIncludeHardware(), assessmentStorageInfo.getServiceWindowSla(),
				avgDevices.intValue(), new PageRequest(0, 2));
		log.info("Performance benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, PERFORMANCE);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageDevicesVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentStorageInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, volume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentStorageInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, volume);
					dealResultsResponse.setLowBenchMarkDeal(lowBenchMarkDeal);
				}
			}
		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

	}


	/**
	 * @param assessmentStorageInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgDevices
	 * @param dealResultsResponse
	 * @param devicesVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(StorageInfo assessmentStorageInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgDevices, DealResultsResponse dealResultsResponse, int devicesVolume) {
		List<StorageInfo> benchMarkHighDealResult = storageRepository.findHighBenchMarkDealForPerformance(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentStorageInfo.isOffshoreAllowed(),
				assessmentStorageInfo.isIncludeHardware(), assessmentStorageInfo.getServiceWindowSla(),
				avgDevices.intValue(), new PageRequest(0, 1));
		log.info("HIGH Performance benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, PERFORMANCE);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageDevicesVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentStorageInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, devicesVolume);
					dealResultsResponse.setHighBenchMarkDeal(highBenchMarkDeal);
				}
			}
		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

	}


	/**
	 * @param assessmentStorageInfo
	 * @param benchmarkStorageInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param volume
	 */
	private void prepareBenchmarkDealResultDto(StorageInfo assessmentStorageInfo,
			StorageInfo benchmarkStorageInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int volume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkStorageInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkTotalAverageUnitPrice(assessmentStorageInfo,
				benchmarkStorageInfo.getStorageYearlyDataInfos(), volume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkTotalAverageUnitPrice(assessmentStorageInfo,
				benchmarkStorageInfo.getStorageYearlyDataInfos(), volume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentStorageInfo
	 * @param dealResultsYearlyList
	 * @param avgDevices
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkTotalAverageUnitPrice(StorageInfo assessmentStorageInfo,
			List<StorageYearlyDataInfo> dealResultsYearlyList, int avgDevices, String benchMarkType) {
		BigDecimal devicesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (StorageYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (StorageUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getStorageUnitPriceInfo()) {
				for (StorageYearlyDataInfo assessmentYearlyDataInfo : assessmentStorageInfo.getStorageYearlyDataInfos()) {
					if (benchmarkUnitPriceInfo.getStorageYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						devicesRevenue = devicesRevenue.add(benchmarkUnitPriceInfo.getPerformanceUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getPerformanceStorage())));
						break unitPriceLoop;
					}
				}
			}
		}
		BigDecimal avgUnitPrice = devicesRevenue.divide(new BigDecimal(avgDevices),2,BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}


}
