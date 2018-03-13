package com.in.fujitsu.pricing.network.helper;

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
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkUnitPriceInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;
import com.in.fujitsu.pricing.network.repository.NetworkRepository;
import com.in.fujitsu.pricing.specification.NetworkSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pawarbh
 *
 */
@Slf4j
@Service
public class ProxiesDealResultsHelper {

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";
	private static String PROXIES = "PROXIES";

	/**
	 * @param networkInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDealsForProxies(NetworkInfo networkInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgDevices = networkCommonHelper
				.getProxiesAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		// Past Deal Calculation
		Specification<NetworkInfo> specification = NetworkSpecification.specificationForProxies(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgDevices, towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(specification);
		log.info("Proxies devices dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, PROXIES);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageDevicesVolume(pastDealResults);
			absVolumeDiffPercMap = networkCommonHelper.prepareDealAbsVolumeDiff(avgDevices, dealAvgVolumeMap);
			int devicesVolume = getDevicesVolume(networkInfo.getNetworkYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageDevicesUnitPrice(networkInfo, pastDealResults,
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
	private void setNearestDealInResult(List<NetworkInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (NetworkInfo pastDealNetworkInfo : pastDealResults) {
				DealInfo pastDeallInfo = pastDealNetworkInfo.getDealInfo();
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
	private Map<Long, BigDecimal> prepareDealAverageDevicesVolume(List<NetworkInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (NetworkInfo NetworkInfo : pastDealResults) {
				Long dealId = NetworkInfo.getDealInfo().getDealId();
				BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int devicesVolume = 0;
				int size = 0;
				for (NetworkYearlyDataInfo yearlyDataInfo : NetworkInfo.getNetworkYearlyDataInfoList()) {
					if (yearlyDataInfo.getProxies() != 0) {
						devicesVolume += yearlyDataInfo.getProxies();
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
	 * @param assessmentNetworkInfo
	 * @param pastDealResults
	 * @param devicesVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageDevicesUnitPrice(NetworkInfo assessmentNetworkInfo,
			List<NetworkInfo> pastDealResults, int devicesVolume) {
		Map<Long, BigDecimal> averageRevenueMap = new HashMap<>();
		for (NetworkInfo pastNetworkInfo : pastDealResults) {
			BigDecimal devicesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (NetworkYearlyDataInfo pastYearlyDataInfo : pastNetworkInfo.getNetworkYearlyDataInfoList()) {
				for (NetworkUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getNetworkUnitPriceInfoList()) {
					for (NetworkYearlyDataInfo assessmentYearlyDataInfo : assessmentNetworkInfo.getNetworkYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getNetworkYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getNetworkYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								devicesRevenue = devicesRevenue.add(pastUnitPriceInfo.getProxies().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getProxies())));
								break;
							}
						}
					}
				}
			}
			BigDecimal avgUnitPrice = devicesRevenue.divide(new BigDecimal(devicesVolume), 2,BigDecimal.ROUND_CEILING);
			averageRevenueMap.put(pastNetworkInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return averageRevenueMap;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public int getDevicesVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		int devicesVolume = 0;
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				devicesVolume += networkYearlyDataInfo.getProxies();
			}
		}
		return devicesVolume;
	}

	/**
	 * @param assessmentNetworkInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDealsForProxies(NetworkInfo assessmentNetworkInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgDevices = networkCommonHelper
				.getProxiesAverageVolume(assessmentNetworkInfo.getNetworkYearlyDataInfoList());
		int devicesVolume = getDevicesVolume(assessmentNetworkInfo.getNetworkYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		if (devicesVolume != 0) {
			prepareSelectedAndLowBenchmarkDealResult(assessmentNetworkInfo, assessmentDealTerm, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, avgDevices, dealResultsResponse, devicesVolume);

			prepareHighBenchmarkDealResult(assessmentNetworkInfo, assessmentDealTerm, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, avgDevices, dealResultsResponse, devicesVolume);
		}
		return dealResultsResponse;
	}


	/**
	 * @param assessmentNetworkInfo
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
	private void prepareSelectedAndLowBenchmarkDealResult(NetworkInfo assessmentNetworkInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgDevices,
			DealResultsResponse dealResultsResponse, int volume) {
		List<NetworkInfo> benchMarkLowAndSelectedDealResults = networkRepository.findLowBenchMarkDealForProxies(
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgDevices.intValue(),
				assessmentNetworkInfo.isOffshoreAllowed(), assessmentNetworkInfo.isIncludeHardware(), assessmentNetworkInfo.getLevelOfService(), new PageRequest(0, 2));
		log.info("LOW Proxies benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, PROXIES);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageDevicesVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentNetworkInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, volume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentNetworkInfo, benchMarkLowAndSelectedDealResults.get(1),
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
	 * @param assessmentNetworkInfo
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
	private void prepareHighBenchmarkDealResult(NetworkInfo assessmentNetworkInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgDevices, DealResultsResponse dealResultsResponse, int devicesVolume) {
		List<NetworkInfo> benchMarkHighDealResult = networkRepository.findHighBenchMarkDealForProxies(
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgDevices.intValue(),
				assessmentNetworkInfo.isOffshoreAllowed(), assessmentNetworkInfo.isIncludeHardware(), assessmentNetworkInfo.getLevelOfService(),new PageRequest(0, 1));
		log.info("HIGH Proxies benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, PROXIES);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageDevicesVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentNetworkInfo, benchMarkHighDealResult.get(0),
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
	 * @param assessmentNetworkInfo
	 * @param benchmarkNetworkInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param volume
	 */
	private void prepareBenchmarkDealResultDto(NetworkInfo assessmentNetworkInfo,
			NetworkInfo benchmarkNetworkInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int volume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkNetworkInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkTotalAverageUnitPrice(assessmentNetworkInfo,
				benchmarkNetworkInfo.getNetworkYearlyDataInfoList(), volume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkTotalAverageUnitPrice(assessmentNetworkInfo,
				benchmarkNetworkInfo.getNetworkYearlyDataInfoList(), volume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentNetworkInfo
	 * @param dealResultsYearlyList
	 * @param avgDevices
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkTotalAverageUnitPrice(NetworkInfo assessmentNetworkInfo,
			List<NetworkYearlyDataInfo> dealResultsYearlyList, int avgDevices, String benchMarkType) {
		BigDecimal devicesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (NetworkYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (NetworkUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getNetworkUnitPriceInfoList()) {
				for (NetworkYearlyDataInfo assessmentYearlyDataInfo : assessmentNetworkInfo.getNetworkYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getNetworkYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						devicesRevenue = devicesRevenue.add(benchmarkUnitPriceInfo.getProxies().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getProxies())));
						break unitPriceLoop;
					}
				}
			}
		}
		BigDecimal avgUnitPrice = devicesRevenue.divide(new BigDecimal(avgDevices), 2,BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
