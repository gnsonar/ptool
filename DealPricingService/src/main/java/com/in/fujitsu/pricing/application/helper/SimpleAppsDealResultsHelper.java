package com.in.fujitsu.pricing.application.helper;

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

import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.application.repository.ApplicationRepository;
import com.in.fujitsu.pricing.dto.DealResultDto;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.specification.ApplicationSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SimpleAppsDealResultsHelper {

	@Autowired
	private ApplicationRepository appRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ApplicationCommonHelper appCommonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";

	/**
	 * @param applicationInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDealsForSimple(ApplicationInfo applicationInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgSimpleAppsVolume = appCommonHelper.getSimpleApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		// Past Deal Calculation
		Specification<ApplicationInfo> simpleAppsSpecification = ApplicationSpecification.specificationForSimpleApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgSimpleAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(simpleAppsSpecification);
		log.info("Simple Application dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageSimpleAppsVolume(pastDealResults);
			absVolumeDiffPercMap = appCommonHelper.prepareDealAbsVolumeDiff(avgSimpleAppsVolume, dealAvgVolumeMap);
			int simpleAppsVolume = getSimpleAppsVolume(applicationInfo.getAppYearlyDataInfos());
			averageUnitPriceMap = prepareDealAverageSimpleAppsUnitPrice(applicationInfo, pastDealResults,
					simpleAppsVolume);

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
	private void setNearestDealInResult(List<ApplicationInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ApplicationInfo pastDealApplicationInfo : pastDealResults) {
				DealInfo pastDeallInfo = pastDealApplicationInfo.getDealInfo();
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
	private Map<Long, BigDecimal> prepareDealAverageSimpleAppsVolume(List<ApplicationInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgSimpleAppsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ApplicationInfo applicationInfo : pastDealResults) {
				Long dealId = applicationInfo.getDealInfo().getDealId();
				BigDecimal avgSimpleAppsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int simpleAppsVolume = 0;
				int size = 0;
				for (ApplicationYearlyDataInfo yearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
					if (yearlyDataInfo.getSimpleAppsVolume() != 0) {
						simpleAppsVolume += yearlyDataInfo.getSimpleAppsVolume();
						size++;
					}
				}
				if (size != 0) {
					avgSimpleAppsVolume = new BigDecimal(simpleAppsVolume / size);
				}
				yearlyAvgSimpleAppsVolumeMap.put(dealId, avgSimpleAppsVolume);
			}
		}
		return yearlyAvgSimpleAppsVolumeMap;

	}

	/**
	 * @param assessmentApplicationInfo
	 * @param pastDealResults
	 * @param simpleAppsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageSimpleAppsUnitPrice(ApplicationInfo assessmentApplicationInfo,
			List<ApplicationInfo> pastDealResults, int simpleAppsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ApplicationInfo pastApplicationInfo : pastDealResults) {
			BigDecimal simpleRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ApplicationYearlyDataInfo pastYearlyDataInfo : pastApplicationInfo.getAppYearlyDataInfos()) {
				for (ApplicationUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getAppUnitPriceInfoList()) {
					for (ApplicationYearlyDataInfo assessmentYearlyDataInfo : assessmentApplicationInfo.getAppYearlyDataInfos()) {
						if (pastUnitPriceInfo.getAppYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getAppYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								simpleRevenue = simpleRevenue.add(pastUnitPriceInfo.getSimpleAppsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getSimpleAppsVolume())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = simpleRevenue.divide(new BigDecimal(simpleAppsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastApplicationInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public int getSimpleAppsVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		int simpleAppsVolume = 0;
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				simpleAppsVolume += applicationYearlyDataInfo.getSimpleAppsVolume();
			}
		}
		return simpleAppsVolume;
	}

	/**
	 * @param assessmentApplicationInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDealsForSimple(ApplicationInfo assessmentApplicationInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgSimpleAppsVolume = appCommonHelper.getSimpleApplicationAverageVolume(assessmentApplicationInfo.getAppYearlyDataInfos());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		prepareSelectedAndLowBenchmarkDealResult(assessmentApplicationInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgSimpleAppsVolume, dealResultsResponse);

		prepareHighBenchmarkDealResult(assessmentApplicationInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgSimpleAppsVolume, dealResultsResponse);

		return dealResultsResponse;
	}

	/**
	 * @param assessmentApplicationInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgSimpleAppsVolume
	 * @param dealResultsResponse
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ApplicationInfo assessmentApplicationInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgSimpleAppsVolume,
			DealResultsResponse dealResultsResponse) {
		List<ApplicationInfo> benchMarkLowAndSelectedDealResults = appRepository.findLowBenchMarkDealForSimple(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentApplicationInfo.isOffshoreAllowed(),
				assessmentApplicationInfo.getLevelOfService(), avgSimpleAppsVolume.intValue(), new PageRequest(0, 2));
		log.info("LOW Simple benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageSimpleAppsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentApplicationInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentApplicationInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal);
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
	 * @param assessmentApplicationInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgSimpleAppsVolume
	 * @param dealResultsResponse
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ApplicationInfo assessmentApplicationInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgSimpleAppsVolume, DealResultsResponse dealResultsResponse) {
		List<ApplicationInfo> benchMarkHighDealResult = appRepository.findHighBenchMarkDealForSimple(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentApplicationInfo.isOffshoreAllowed(),
				assessmentApplicationInfo.getLevelOfService(), avgSimpleAppsVolume.intValue(), new PageRequest(0, 1));
		log.info("HIGH Simple benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageSimpleAppsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentApplicationInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal);
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
	 * @param assessmentApplicationInfo
	 * @param benchmarkApplicationInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 */
	private void prepareBenchmarkDealResultDto(ApplicationInfo assessmentApplicationInfo,
			ApplicationInfo benchmarkApplicationInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkApplicationInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		int simpleAppsVolume = getSimpleAppsVolume(assessmentApplicationInfo.getAppYearlyDataInfos());
		dealResultDto.setAveragePrice(getBenchmarkSimpleAverageUnitPrice(assessmentApplicationInfo,
				benchmarkApplicationInfo.getAppYearlyDataInfos(), simpleAppsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkSimpleAverageUnitPrice(assessmentApplicationInfo,
				benchmarkApplicationInfo.getAppYearlyDataInfos(), simpleAppsVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentApplicationInfo
	 * @param dealResultsYearlyList
	 * @param simpleAppsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkSimpleAverageUnitPrice(ApplicationInfo assessmentApplicationInfo,
			List<ApplicationYearlyDataInfo> dealResultsYearlyList, int simpleAppsVolume, String benchMarkType) {
		BigDecimal simpleRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ApplicationYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ApplicationUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getAppUnitPriceInfoList()) {
				for (ApplicationYearlyDataInfo assessmentYearlyDataInfo : assessmentApplicationInfo
						.getAppYearlyDataInfos()) {
					if (benchmarkUnitPriceInfo.getAppYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						simpleRevenue = simpleRevenue.add(benchmarkUnitPriceInfo.getSimpleAppsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getSimpleAppsVolume())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = simpleRevenue.divide(new BigDecimal(simpleAppsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
