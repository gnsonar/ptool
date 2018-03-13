package com.in.fujitsu.pricing.enduser.helper;

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
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserUnitPriceInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.enduser.repository.EndUserRepository;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.specification.EndUserSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author MishraSub
 *
 */
@Slf4j
@Service
public class MobileDealResultsHelper {

	@Autowired
	EndUserRepository endUserRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EndUserCommonHelper commonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";
	private static String MOBILE = "MOBILE";

	/**
	 * @param endUserInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDealsForMobiles(EndUserInfo endUserInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgMobiles = commonHelper.getMobilesAverageVolume(endUserInfo.getEndUserYearlyDataInfoList());
		Specification<EndUserInfo> mobilesSpecification = EndUserSpecification.specificationForMobileDevices(
				endUserInfo.isOffshoreAllowed(), endUserInfo.isIncludeBreakFix(), endUserInfo.isIncludeHardware(),
				endUserInfo.getResolutionTime(), avgMobiles, towerSpecificBandInfo.getBandPercentage() == null
						? new BigDecimal(100) : towerSpecificBandInfo.getBandPercentage());
		List<EndUserInfo> pastDealResults = endUserRepository.findAll(mobilesSpecification);
		log.info("Mobiles dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, MOBILE);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageMobiles(pastDealResults);
			absVolumeDiffPercMap = commonHelper.prepareDealAbsVolumeDiff(avgMobiles, dealAvgVolumeMap);
			int mobilesVolume = getMobileVolume(endUserInfo.getEndUserYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageMobilesUnitPrice(endUserInfo, pastDealResults, mobilesVolume);

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
	private void setNearestDealInResult(List<EndUserInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (EndUserInfo pastDealRetailInfo : pastDealResults) {
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
	private Map<Long, BigDecimal> prepareDealAverageMobiles(List<EndUserInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgMobilesVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (EndUserInfo endUserInfo : pastDealResults) {
				Long dealId = endUserInfo.getDealInfo().getDealId();
				BigDecimal avgMobilesVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int mobilesVolume = 0;
				int size = 0;
				for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
					if (yearlyDataInfo.getMobileDevices() != 0) {
						mobilesVolume += yearlyDataInfo.getMobileDevices();
						size++;
					}
				}
				if (size != 0) {
					avgMobilesVolume = new BigDecimal(mobilesVolume / size);
				}
				yearlyAvgMobilesVolumeMap.put(dealId, avgMobilesVolume);
			}
		}
		return yearlyAvgMobilesVolumeMap;

	}

	/**
	 * @param assessmentEndUserInfo
	 * @param pastDealResults
	 * @param mobilesVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageMobilesUnitPrice(EndUserInfo assessmentEndUserInfo,
			List<EndUserInfo> pastDealResults, int mobilesVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (EndUserInfo pastEndUserInfo : pastDealResults) {
			BigDecimal mobilesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (EndUserYearlyDataInfo pastYearlyDataInfo : pastEndUserInfo.getEndUserYearlyDataInfoList()) {
				for (EndUserUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getEndUserUnitPriceInfoList()) {
					for (EndUserYearlyDataInfo assessmentYearlyDataInfo : assessmentEndUserInfo
							.getEndUserYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getEndUserYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getEndUserYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								mobilesRevenue = mobilesRevenue.add(pastUnitPriceInfo.getMobileDevices()
										.multiply(new BigDecimal(assessmentYearlyDataInfo.getMobileDevices())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = mobilesRevenue.divide(new BigDecimal(mobilesVolume), 2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastEndUserInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param endUserYearlyDataInfoList
	 * @return
	 */
	public int getMobileVolume(List<EndUserYearlyDataInfo> endUserYearlyDataInfoList) {
		int mobilesVolume = 0;
		if (!CollectionUtils.isEmpty(endUserYearlyDataInfoList)) {
			for (EndUserYearlyDataInfo yearlyDataInfo : endUserYearlyDataInfoList) {
				mobilesVolume += yearlyDataInfo.getMobileDevices();
			}
		}
		return mobilesVolume;
	}

	/**
	 * @param assessmentEndUserInfoInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDealsForMobiles(EndUserInfo assessmentEndUserInfoInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgMobiles = commonHelper.getMobilesAverageVolume(assessmentEndUserInfoInfo.getEndUserYearlyDataInfoList());
		int mobilesVolume = getMobileVolume(assessmentEndUserInfoInfo.getEndUserYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentEndUserInfoInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgMobiles, dealResultsResponse,
				mobilesVolume);

		prepareHighBenchmarkDealResult(assessmentEndUserInfoInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgMobiles, dealResultsResponse, mobilesVolume);
		return dealResultsResponse;
	}

	/**
	 * @param assessmentEndUserInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgMobiles
	 * @param dealResultsResponse
	 * @param mobilesVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(EndUserInfo assessmentEndUserInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgMobiles, DealResultsResponse dealResultsResponse,
			int mobilesVolume) {
		List<EndUserInfo> benchMarkLowAndSelectedDealResults = endUserRepository.findLowBenchMarkDealForMobiles(
				assessmentEndUserInfo.isOffshoreAllowed(), assessmentEndUserInfo.isIncludeHardware(),
				assessmentEndUserInfo.isIncludeBreakFix(), assessmentEndUserInfo.getResolutionTime(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgMobiles.intValue(), new PageRequest(0, 2));
		log.info("LOW Mobiles benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, MOBILE);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageMobiles(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, mobilesVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, mobilesVolume);
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
	 * @param assessmentEndUserInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgMobiles
	 * @param dealResultsResponse
	 * @param mobilesVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(EndUserInfo assessmentEndUserInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgMobiles, DealResultsResponse dealResultsResponse,
			int mobilesVolume) {
		List<EndUserInfo> benchMarkHighDealResult = endUserRepository.findHighBenchMarkDealForMobiles(
				assessmentEndUserInfo.isOffshoreAllowed(), assessmentEndUserInfo.isIncludeHardware(),
				assessmentEndUserInfo.isIncludeBreakFix(), assessmentEndUserInfo.getResolutionTime(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgMobiles.intValue(), new PageRequest(0, 1));
		log.info("HIGH Mobiles benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, MOBILE);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageMobiles(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, mobilesVolume);
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
	 * @param assessmentEndUserInfo
	 * @param benchmarkEndUserInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param mobilesVolume
	 */
	private void prepareBenchmarkDealResultDto(EndUserInfo assessmentEndUserInfo, EndUserInfo benchmarkEndUserInfo,
			Map<Long, BigDecimal> dealAvgVolumeMap, DealResultDto dealResultDto, int mobilesVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkEndUserInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkMobileAverageUnitPrice(assessmentEndUserInfo,
				benchmarkEndUserInfo.getEndUserYearlyDataInfoList(), mobilesVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkMobileAverageUnitPrice(assessmentEndUserInfo,
				benchmarkEndUserInfo.getEndUserYearlyDataInfoList(), mobilesVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentEndUserInfo
	 * @param dealResultsYearlyList
	 * @param mobilesVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkMobileAverageUnitPrice(EndUserInfo assessmentEndUserInfo,
			List<EndUserYearlyDataInfo> dealResultsYearlyList, int mobilesVolume, String benchMarkType) {
		BigDecimal mobilesRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (EndUserYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (EndUserUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getEndUserUnitPriceInfoList()) {
				for (EndUserYearlyDataInfo assessmentYearlyDataInfo : assessmentEndUserInfo
						.getEndUserYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getEndUserYearlyDataInfo().getYear() == assessmentYearlyDataInfo.getYear()
							&& benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						mobilesRevenue = mobilesRevenue.add(benchmarkUnitPriceInfo.getMobileDevices()
								.multiply(new BigDecimal(assessmentYearlyDataInfo.getMobileDevices())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = mobilesRevenue.divide(new BigDecimal(mobilesVolume), 2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
