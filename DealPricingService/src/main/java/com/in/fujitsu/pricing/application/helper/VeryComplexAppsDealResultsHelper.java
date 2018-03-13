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
public class VeryComplexAppsDealResultsHelper {

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
	public DealResultsResponse getNearestPastDealsForVeryComplex(ApplicationInfo applicationInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgVeryComplexAppsVolume = appCommonHelper.getVeryComplexApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		// Past Deal Calculation
		Specification<ApplicationInfo> veryComplexAppsSpecification = ApplicationSpecification.specificationForVeryComplexApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgVeryComplexAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(veryComplexAppsSpecification);
		log.info("Very Complex Application dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageVeryComplexAppsVolume(pastDealResults);
			absVolumeDiffPercMap = appCommonHelper.prepareDealAbsVolumeDiff(avgVeryComplexAppsVolume, dealAvgVolumeMap);
			int veryComplexAppsVolume = getVeryComplexAppsVolume(applicationInfo.getAppYearlyDataInfos());
			averageUnitPriceMap = prepareDealAverageVeryComplexAppsUnitPrice(applicationInfo, pastDealResults,
					veryComplexAppsVolume);

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
	private Map<Long, BigDecimal> prepareDealAverageVeryComplexAppsVolume(List<ApplicationInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgVeryComplexAppsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ApplicationInfo applicationInfo : pastDealResults) {
				Long dealId = applicationInfo.getDealInfo().getDealId();
				BigDecimal avgVeryComplexAppsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int veryComplexAppsVolume = 0;
				int size = 0;
				for (ApplicationYearlyDataInfo yearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
					if (yearlyDataInfo.getVeryComplexAppsVolume() != 0) {
						veryComplexAppsVolume += yearlyDataInfo.getVeryComplexAppsVolume();
						size++;
					}
				}
				if (size != 0) {
					avgVeryComplexAppsVolume = new BigDecimal(veryComplexAppsVolume / size);
				}
				yearlyAvgVeryComplexAppsVolumeMap.put(dealId, avgVeryComplexAppsVolume);
			}
		}
		return yearlyAvgVeryComplexAppsVolumeMap;

	}

	/**
	 * @param assessmentApplicationInfo
	 * @param pastDealResults
	 * @param veryComplexAppsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageVeryComplexAppsUnitPrice(ApplicationInfo assessmentApplicationInfo,
			List<ApplicationInfo> pastDealResults, int veryComplexAppsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ApplicationInfo pastApplicationInfo : pastDealResults) {
			BigDecimal veryComplexRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ApplicationYearlyDataInfo pastYearlyDataInfo : pastApplicationInfo.getAppYearlyDataInfos()) {
				for (ApplicationUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getAppUnitPriceInfoList()) {
					for (ApplicationYearlyDataInfo assessmentYearlyDataInfo : assessmentApplicationInfo.getAppYearlyDataInfos()) {
						if (pastUnitPriceInfo.getAppYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getAppYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								veryComplexRevenue = veryComplexRevenue.add(pastUnitPriceInfo.getVeryComplexAppsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getVeryComplexAppsVolume())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = veryComplexRevenue.divide(new BigDecimal(veryComplexAppsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastApplicationInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public int getVeryComplexAppsVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		int veryComplexAppsVolume = 0;
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				veryComplexAppsVolume += applicationYearlyDataInfo.getVeryComplexAppsVolume();
			}
		}
		return veryComplexAppsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForVeryComplex(ApplicationInfo assessmentApplicationInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgVeryComplexAppsVolume = appCommonHelper.getVeryComplexApplicationAverageVolume(assessmentApplicationInfo.getAppYearlyDataInfos());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		prepareSelectedAndLowBenchmarkDealResult(assessmentApplicationInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgVeryComplexAppsVolume, dealResultsResponse);

		prepareHighBenchmarkDealResult(assessmentApplicationInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgVeryComplexAppsVolume, dealResultsResponse);
		return dealResultsResponse;
	}

	/**
	 * @param assessmentApplicationInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgVeryComplexAppsVolume
	 * @param dealResultsResponse
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ApplicationInfo assessmentApplicationInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgVeryComplexAppsVolume,
			DealResultsResponse dealResultsResponse) {
		List<ApplicationInfo> benchMarkLowAndSelectedDealResults = appRepository.findLowBenchMarkDealForVeryComplex(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentApplicationInfo.isOffshoreAllowed(),
				assessmentApplicationInfo.getLevelOfService(), avgVeryComplexAppsVolume.intValue(), new PageRequest(0, 2));
		log.info("LOW Very Complex benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageVeryComplexAppsVolume(benchMarkLowAndSelectedDealResults);

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
	 * @param avgVeryComplexAppsVolume
	 * @param dealResultsResponse
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ApplicationInfo assessmentApplicationInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgVeryComplexAppsVolume, DealResultsResponse dealResultsResponse) {
		List<ApplicationInfo> benchMarkHighDealResult = appRepository.findHighBenchMarkDealForVeryComplex(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentApplicationInfo.isOffshoreAllowed(),
				assessmentApplicationInfo.getLevelOfService(), avgVeryComplexAppsVolume.intValue(), new PageRequest(0, 1));
		log.info("HIGH Very Complex benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageVeryComplexAppsVolume(benchMarkHighDealResult);
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
		int veryComplexAppsVolume = getVeryComplexAppsVolume(assessmentApplicationInfo.getAppYearlyDataInfos());
		dealResultDto.setAveragePrice(getBenchmarkVeryComplexAverageUnitPrice(assessmentApplicationInfo,
				benchmarkApplicationInfo.getAppYearlyDataInfos(), veryComplexAppsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkVeryComplexAverageUnitPrice(assessmentApplicationInfo,
				benchmarkApplicationInfo.getAppYearlyDataInfos(), veryComplexAppsVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentApplicationInfo
	 * @param dealResultsYearlyList
	 * @param veryComplexAppsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkVeryComplexAverageUnitPrice(ApplicationInfo assessmentApplicationInfo,
			List<ApplicationYearlyDataInfo> dealResultsYearlyList, int veryComplexAppsVolume, String benchMarkType) {
		BigDecimal veryComplexRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ApplicationYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ApplicationUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getAppUnitPriceInfoList()) {
				for (ApplicationYearlyDataInfo assessmentYearlyDataInfo : assessmentApplicationInfo
						.getAppYearlyDataInfos()) {
					if (benchmarkUnitPriceInfo.getAppYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						veryComplexRevenue = veryComplexRevenue.add(benchmarkUnitPriceInfo.getVeryComplexAppsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getVeryComplexAppsVolume())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = veryComplexRevenue.divide(new BigDecimal(veryComplexAppsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}


}
