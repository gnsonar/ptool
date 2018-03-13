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
public class StandardLaptopsDealResultsHelper {

	@Autowired
	EndUserRepository endUserRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private EndUserCommonHelper commonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";
	private static String STANDARD_LAPTOP = "STANDARD_LAPTOP";

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
	public DealResultsResponse getNearestPastDealsForStandardLaptops(EndUserInfo endUserInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgStandardLaptops = commonHelper.getStandardLaptopsAverageVolume(endUserInfo.getEndUserYearlyDataInfoList());
		Specification<EndUserInfo> standardLaptopsSpecification = EndUserSpecification.specificationForStandardLaptops(
				endUserInfo.isOffshoreAllowed(), endUserInfo.isIncludeBreakFix(), endUserInfo.isIncludeHardware(),
				endUserInfo.getResolutionTime(), avgStandardLaptops, towerSpecificBandInfo.getBandPercentage() == null
						? new BigDecimal(100) : towerSpecificBandInfo.getBandPercentage());
		List<EndUserInfo> pastDealResults = endUserRepository.findAll(standardLaptopsSpecification);
		log.info("Standard LAPTOPS dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, STANDARD_LAPTOP);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageStandardLaptops(pastDealResults);
			absVolumeDiffPercMap = commonHelper.prepareDealAbsVolumeDiff(avgStandardLaptops, dealAvgVolumeMap);
			int standardLaptopsVolume = getStandardLaptopsVolume(endUserInfo.getEndUserYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageStandardLaptopsUnitPrice(endUserInfo, pastDealResults, standardLaptopsVolume);

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
	private Map<Long, BigDecimal> prepareDealAverageStandardLaptops(List<EndUserInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgStandardLaptopsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (EndUserInfo endUserInfo : pastDealResults) {
				Long dealId = endUserInfo.getDealInfo().getDealId();
				BigDecimal avgStandardLaptopsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int standardLaptopsVolume = 0;
				int size = 0;
				for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
					if (yearlyDataInfo.getStandardLaptops() != 0) {
						standardLaptopsVolume += yearlyDataInfo.getStandardLaptops();
						size++;
					}
				}
				if (size != 0) {
					avgStandardLaptopsVolume = new BigDecimal(standardLaptopsVolume / size);
				}
				yearlyAvgStandardLaptopsVolumeMap.put(dealId, avgStandardLaptopsVolume);
			}
		}
		return yearlyAvgStandardLaptopsVolumeMap;

	}

	/**
	 * @param assessmentEndUserInfo
	 * @param pastDealResults
	 * @param standardLaptopsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageStandardLaptopsUnitPrice(EndUserInfo assessmentEndUserInfo,
			List<EndUserInfo> pastDealResults, int standardLaptopsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (EndUserInfo pastEndUserInfo : pastDealResults) {
			BigDecimal stnadradLaptopsRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (EndUserYearlyDataInfo pastYearlyDataInfo : pastEndUserInfo.getEndUserYearlyDataInfoList()) {
				for (EndUserUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getEndUserUnitPriceInfoList()) {
					for (EndUserYearlyDataInfo assessmentYearlyDataInfo : assessmentEndUserInfo
							.getEndUserYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getEndUserYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getEndUserYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								stnadradLaptopsRevenue = stnadradLaptopsRevenue.add(pastUnitPriceInfo.getStandardLaptops()
										.multiply(new BigDecimal(assessmentYearlyDataInfo.getStandardLaptops())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = stnadradLaptopsRevenue.divide(new BigDecimal(standardLaptopsVolume), 2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastEndUserInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param endUserYearlyDataInfoList
	 * @return
	 */
	public int getStandardLaptopsVolume(List<EndUserYearlyDataInfo> endUserYearlyDataInfoList) {
		int standardLaptopsVolume = 0;
		if (!CollectionUtils.isEmpty(endUserYearlyDataInfoList)) {
			for (EndUserYearlyDataInfo yearlyDataInfo : endUserYearlyDataInfoList) {
				standardLaptopsVolume += yearlyDataInfo.getStandardLaptops();
			}
		}
		return standardLaptopsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForStandardLaptops(EndUserInfo assessmentEndUserInfoInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgStandardLaptops = commonHelper.getStandardLaptopsAverageVolume(assessmentEndUserInfoInfo.getEndUserYearlyDataInfoList());
		int standardLaptopsVolume = getStandardLaptopsVolume(assessmentEndUserInfoInfo.getEndUserYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentEndUserInfoInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgStandardLaptops, dealResultsResponse,
				standardLaptopsVolume);

		prepareHighBenchmarkDealResult(assessmentEndUserInfoInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgStandardLaptops, dealResultsResponse, standardLaptopsVolume);
		return dealResultsResponse;
	}

	/**
	 * @param assessmentEndUserInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgStandardLaptops
	 * @param dealResultsResponse
	 * @param standardLaptopsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(EndUserInfo assessmentEndUserInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgStandardLaptops, DealResultsResponse dealResultsResponse,
			int standardLaptopsVolume) {
		List<EndUserInfo> benchMarkLowAndSelectedDealResults = endUserRepository.findLowBenchMarkDealForStandardLaptops(
				assessmentEndUserInfo.isOffshoreAllowed(), assessmentEndUserInfo.isIncludeHardware(),
				assessmentEndUserInfo.isIncludeBreakFix(), assessmentEndUserInfo.getResolutionTime(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgStandardLaptops.intValue(), new PageRequest(0, 2));
		log.info("LOW : Standard LAPTOPS benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, STANDARD_LAPTOP);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageStandardLaptops(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, standardLaptopsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, standardLaptopsVolume);
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
	 * @param avgStandardLaptops
	 * @param dealResultsResponse
	 * @param standardLaptopsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(EndUserInfo assessmentEndUserInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgStandardLaptops, DealResultsResponse dealResultsResponse,
			int standardLaptopsVolume) {
		List<EndUserInfo> benchMarkHighDealResult = endUserRepository.findHighBenchMarkDealForStandardLaptops(
				assessmentEndUserInfo.isOffshoreAllowed(), assessmentEndUserInfo.isIncludeHardware(),
				assessmentEndUserInfo.isIncludeBreakFix(), assessmentEndUserInfo.getResolutionTime(),
				DealTypeEnum.BENCHMARK_DEAL.getName(), avgStandardLaptops.intValue(), new PageRequest(0, 1));
		log.info("HIGH : Standard LAPTOPS benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, STANDARD_LAPTOP);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageStandardLaptops(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentEndUserInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, standardLaptopsVolume);
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
	 * @param standardLaptopsVolume
	 */
	private void prepareBenchmarkDealResultDto(EndUserInfo assessmentEndUserInfo, EndUserInfo benchmarkEndUserInfo,
			Map<Long, BigDecimal> dealAvgVolumeMap, DealResultDto dealResultDto, int standardLaptopsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkEndUserInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkStandardLaptopAverageUnitPrice(assessmentEndUserInfo,
				benchmarkEndUserInfo.getEndUserYearlyDataInfoList(), standardLaptopsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkStandardLaptopAverageUnitPrice(assessmentEndUserInfo,
				benchmarkEndUserInfo.getEndUserYearlyDataInfoList(), standardLaptopsVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentEndUserInfo
	 * @param dealResultsYearlyList
	 * @param standardLaptopsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkStandardLaptopAverageUnitPrice(EndUserInfo assessmentEndUserInfo,
			List<EndUserYearlyDataInfo> dealResultsYearlyList, int standardLaptopsVolume, String benchMarkType) {
		BigDecimal standardLaptopsRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (EndUserYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (EndUserUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getEndUserUnitPriceInfoList()) {
				for (EndUserYearlyDataInfo assessmentYearlyDataInfo : assessmentEndUserInfo
						.getEndUserYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getEndUserYearlyDataInfo().getYear() == assessmentYearlyDataInfo.getYear()
							&& benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						standardLaptopsRevenue = standardLaptopsRevenue.add(benchmarkUnitPriceInfo.getStandardLaptops()
								.multiply(new BigDecimal(assessmentYearlyDataInfo.getStandardLaptops())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = standardLaptopsRevenue.divide(new BigDecimal(standardLaptopsVolume), 2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
