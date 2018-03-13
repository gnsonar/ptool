package com.in.fujitsu.pricing.retail.helper;

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
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailUnitPriceInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;
import com.in.fujitsu.pricing.retail.repository.RetailRepository;
import com.in.fujitsu.pricing.specification.RetailSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author MishraSub
 *
 */
@Slf4j
@Service
public class NoOfShopsDealResultsHelper {

	@Autowired
	private RetailRepository retailRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RetailCommonHelper commonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";

	/**
	 * @param retailInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDealsForNoOfShops(RetailInfo retailInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgNoOfShops = commonHelper.getNoOfShopsAverageVolume(retailInfo.getRetailYearlyDataInfoList());
		Specification<RetailInfo> noOfShopsSpecification = RetailSpecification.specificationForNoOfShops(
				retailInfo.isOffshoreAllowed(), retailInfo.isIncludeHardware(), retailInfo.getEquipmentAge(),
				retailInfo.getEquipmentSet(), avgNoOfShops, towerSpecificBandInfo.getBandPercentage());
		List<RetailInfo> pastDealResults = retailRepository.findAll(noOfShopsSpecification);
		log.info("No Of Shops dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageNoOfShopsVolume(pastDealResults);
			absVolumeDiffPercMap = commonHelper.prepareDealAbsVolumeDiff(avgNoOfShops, dealAvgVolumeMap);
			int noOfShopsVolume = getNoOfShopsVolume(retailInfo.getRetailYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageNoOfShopsUnitPrice(retailInfo, pastDealResults, noOfShopsVolume);

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
	private void setNearestDealInResult(List<RetailInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (RetailInfo pastDealRetailInfo : pastDealResults) {
				DealInfo pastDeallInfo = pastDealRetailInfo.getDealInfo();
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
	private Map<Long, BigDecimal> prepareDealAverageNoOfShopsVolume(List<RetailInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgNoOfShopsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (RetailInfo retailInfo : pastDealResults) {
				Long dealId = retailInfo.getDealInfo().getDealId();
				BigDecimal avgNoOfShopsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int noOfShopsVolume = 0;
				int size = 0;
				for (RetailYearlyDataInfo yearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
					if (yearlyDataInfo.getNoOfShops() != 0) {
						noOfShopsVolume += yearlyDataInfo.getNoOfShops();
						size++;
					}
				}
				if (size != 0) {
					avgNoOfShopsVolume = new BigDecimal(noOfShopsVolume / size);
				}
				yearlyAvgNoOfShopsVolumeMap.put(dealId, avgNoOfShopsVolume);
			}
		}
		return yearlyAvgNoOfShopsVolumeMap;

	}

	/**
	 * @param assessmentRetailInfo
	 * @param pastDealResults
	 * @param noOfShopsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageNoOfShopsUnitPrice(RetailInfo assessmentRetailInfo,
			List<RetailInfo> pastDealResults, int noOfShopsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (RetailInfo pastRetailInfo : pastDealResults) {
			BigDecimal noOfShopsRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (RetailYearlyDataInfo pastYearlyDataInfo : pastRetailInfo.getRetailYearlyDataInfoList()) {
				for (RetailUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getRetailUnitPriceInfoList()) {
					for (RetailYearlyDataInfo assessmentYearlyDataInfo : assessmentRetailInfo
							.getRetailYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getRetailYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getRetailYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								noOfShopsRevenue = noOfShopsRevenue.add(pastUnitPriceInfo.getNoOfShops()
										.multiply(new BigDecimal(assessmentYearlyDataInfo.getNoOfShops())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = noOfShopsRevenue
					.divide(new BigDecimal(noOfShopsVolume), 2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastRetailInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param retailYearlyDataInfoList
	 * @return
	 */
	public int getNoOfShopsVolume(List<RetailYearlyDataInfo> retailYearlyDataInfoList) {
		int noOfShopsVolume = 0;
		if (!CollectionUtils.isEmpty(retailYearlyDataInfoList)) {
			for (RetailYearlyDataInfo retailYearlyDataInfo : retailYearlyDataInfoList) {
				noOfShopsVolume += retailYearlyDataInfo.getNoOfShops();
			}
		}
		return noOfShopsVolume;
	}

	/**
	 * @param assessmentRetailInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDealsForNoOfShops(RetailInfo assessmentRetailInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgNoOfShops = commonHelper
				.getNoOfShopsAverageVolume(assessmentRetailInfo.getRetailYearlyDataInfoList());
		int noOfShopsVolume = getNoOfShopsVolume(assessmentRetailInfo.getRetailYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentRetailInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgNoOfShops, dealResultsResponse,
				noOfShopsVolume);

		prepareHighBenchmarkDealResult(assessmentRetailInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgNoOfShops, dealResultsResponse, noOfShopsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentRetailInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgNoOfShops
	 * @param dealResultsResponse
	 * @param noOfShopsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(RetailInfo assessmentRetailInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgNoOfShops, DealResultsResponse dealResultsResponse,
			int noOfShopsVolume) {
		List<RetailInfo> benchMarkLowAndSelectedDealResults = retailRepository.findLowBenchMarkDealForNoOfShops(
				assessmentRetailInfo.isOffshoreAllowed(), assessmentRetailInfo.isIncludeHardware(),
				assessmentRetailInfo.getEquipmentAge(), assessmentRetailInfo.getEquipmentSet(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgNoOfShops.intValue(), new PageRequest(0, 2));
		log.info("LOW No Of shops benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageNoOfShopsVolume(
					benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentRetailInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, noOfShopsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentRetailInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, noOfShopsVolume);
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
	 * @param assessmentRetailInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgNoOfShops
	 * @param dealResultsResponse
	 * @param noOfShopsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(RetailInfo assessmentRetailInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgNoOfShops, DealResultsResponse dealResultsResponse,
			int noOfShopsVolume) {
		List<RetailInfo> benchMarkHighDealResult = retailRepository.findHighBenchMarkDealForNoOfShops(
				assessmentRetailInfo.isOffshoreAllowed(), assessmentRetailInfo.isIncludeHardware(),
				assessmentRetailInfo.getEquipmentAge(), assessmentRetailInfo.getEquipmentSet(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgNoOfShops.intValue(), new PageRequest(0, 1));
		log.info("HIGH No Of Shops benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageNoOfShopsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentRetailInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, noOfShopsVolume);
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
	 * @param assessmentRetailInfo
	 * @param benchmarkRetailInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param noOfShopsVolume
	 */
	private void prepareBenchmarkDealResultDto(RetailInfo assessmentRetailInfo,
			RetailInfo benchmarkRetailInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int noOfShopsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkRetailInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkNoOfShopsAverageUnitPrice(assessmentRetailInfo,
				benchmarkRetailInfo.getRetailYearlyDataInfoList(), noOfShopsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkNoOfShopsAverageUnitPrice(assessmentRetailInfo,
				benchmarkRetailInfo.getRetailYearlyDataInfoList(), noOfShopsVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentRetailInfo
	 * @param dealResultsYearlyList
	 * @param noOfShopsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkNoOfShopsAverageUnitPrice(RetailInfo assessmentRetailInfo,
			List<RetailYearlyDataInfo> dealResultsYearlyList, int noOfShopsVolume, String benchMarkType) {
		BigDecimal noOfShopsRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (RetailYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (RetailUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getRetailUnitPriceInfoList()) {
				for (RetailYearlyDataInfo assessmentYearlyDataInfo : assessmentRetailInfo
						.getRetailYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getRetailYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						noOfShopsRevenue = noOfShopsRevenue.add(benchmarkUnitPriceInfo.getNoOfShops()
								.multiply(new BigDecimal(assessmentYearlyDataInfo.getNoOfShops())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = noOfShopsRevenue.divide(new BigDecimal(noOfShopsVolume), 2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
