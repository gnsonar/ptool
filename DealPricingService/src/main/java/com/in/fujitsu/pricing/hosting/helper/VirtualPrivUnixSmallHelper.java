package com.in.fujitsu.pricing.hosting.helper;

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
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingUnitPriceInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.specification.HostingSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @author pawarbh
 *
 */
@Slf4j
@Service
public class VirtualPrivUnixSmallHelper {

	@Autowired
	private HostingRepository hostingRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private HostingCommonHelper commonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";
	private static String VIRTUAL_PRIVATE_UNIX_SMALL = "VIRTUAL_PRIVATE_UNIX_SMALL";

	/**
	 * @param hostingInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDeals(HostingInfo hostingInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgVolume = commonHelper
				.getVirtualPrivateUnixSmallAverageVolume(hostingInfo.getHostingYearlyDataInfoList());
		Specification<HostingInfo> specification = HostingSpecification.specForVirtPrivUnixSmall(hostingInfo.isOffshoreAllowed(),
				hostingInfo.getLevelOfService(), hostingInfo.isIncludeHardware(), hostingInfo.isIncludeTooling(),
				hostingInfo.getCoLocation(), avgVolume, towerSpecificBandInfo.getBandPercentage() == null
						? new BigDecimal(100) : towerSpecificBandInfo.getBandPercentage());
		List<HostingInfo> pastDealResults = hostingRepository.findAll(specification);
		log.info("Virtual Private Unix Small dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, VIRTUAL_PRIVATE_UNIX_SMALL);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverage(pastDealResults);
			absVolumeDiffPercMap = commonHelper.prepareDealAbsVolumeDiff(avgVolume, dealAvgVolumeMap);
			int totalVolume = getTotalVolume(hostingInfo.getHostingYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageUnitPrice(hostingInfo, pastDealResults, totalVolume);

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
	private void setNearestDealInResult(List<HostingInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (HostingInfo pastDealRetailInfo : pastDealResults) {
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
	private Map<Long, BigDecimal> prepareDealAverage(List<HostingInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (HostingInfo hostingInfo : pastDealResults) {
				Long dealId = hostingInfo.getDealInfo().getDealId();
				BigDecimal avgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int totalVolume = 0;
				int size = 0;
				for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
					if (yearlyDataInfo.getVirtualPrivateUnixSmall() != 0) {
						totalVolume += yearlyDataInfo.getVirtualPrivateUnixSmall();
						size++;
					}
				}
				if (size != 0) {
					avgVolume = new BigDecimal(totalVolume / size);
				}
				yearlyAvgVolumeMap.put(dealId, avgVolume);
			}
		}
		return yearlyAvgVolumeMap;

	}

	/**
	 * @param assessmentHostingInfo
	 * @param pastDealResults
	 * @param totalVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageUnitPrice(HostingInfo assessmentHostingInfo,
			List<HostingInfo> pastDealResults, int totalVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (HostingInfo pastHostingInfo : pastDealResults) {
			BigDecimal revenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (HostingYearlyDataInfo pastYearlyDataInfo : pastHostingInfo.getHostingYearlyDataInfoList()) {
				for (HostingUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getHostingUnitPriceInfoList()) {
					for (HostingYearlyDataInfo assessmentYearlyDataInfo : assessmentHostingInfo
							.getHostingYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getHostingYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getHostingYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								revenue = revenue.add(pastUnitPriceInfo.getVirtualPrivateUnixSmall()
										.multiply(new BigDecimal(assessmentYearlyDataInfo.getVirtualPrivateUnixSmall())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = revenue.divide(new BigDecimal(totalVolume), 2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastHostingInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param hostingUserYearlyDataInfoList
	 * @return
	 */
	public int getTotalVolume(List<HostingYearlyDataInfo> hostingUserYearlyDataInfoList) {
		int totalVolume = 0;
		if (!CollectionUtils.isEmpty(hostingUserYearlyDataInfoList)) {
			for (HostingYearlyDataInfo yearlyDataInfo : hostingUserYearlyDataInfoList) {
				totalVolume += yearlyDataInfo.getVirtualPrivateUnixSmall();
			}
		}
		return totalVolume;
	}

	/**
	 * @param assessmentHostingInfoInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDeals(HostingInfo assessmentHostingInfoInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgVolume = commonHelper
				.getVirtualPrivateUnixSmallAverageVolume(assessmentHostingInfoInfo.getHostingYearlyDataInfoList());
		int totalVolume = getTotalVolume(assessmentHostingInfoInfo.getHostingYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentHostingInfoInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgVolume, dealResultsResponse,
				totalVolume);

		prepareHighBenchmarkDealResult(assessmentHostingInfoInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgVolume, dealResultsResponse, totalVolume);
		return dealResultsResponse;
	}

	/**
	 * @param assessmentHostingInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgVolume
	 * @param dealResultsResponse
	 * @param totalVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(HostingInfo assessmentHostingInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgVolume, DealResultsResponse dealResultsResponse,
			int totalVolume) {
		List<HostingInfo> benchMarkLowAndSelectedDealResults = hostingRepository.findLowBenchMarkDealForVirtPrivUnixSmall(
				assessmentHostingInfo.isOffshoreAllowed(), assessmentHostingInfo.getLevelOfService(),
				assessmentHostingInfo.isIncludeHardware(), assessmentHostingInfo.isIncludeTooling(),
				assessmentHostingInfo.getCoLocation(), DealTypeEnum.BENCHMARK_DEAL.getName(), avgVolume.intValue(),
				new PageRequest(0, 2));
		log.info("LOW Virtual Private Unix Small benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor, VIRTUAL_PRIVATE_UNIX_SMALL);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverage(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentHostingInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, totalVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentHostingInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, totalVolume);
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
	 * @param assessmentHostingInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgVolume
	 * @param dealResultsResponse
	 * @param totalVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(HostingInfo assessmentHostingInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgVolume, DealResultsResponse dealResultsResponse,
			int totalVolume) {
		List<HostingInfo> benchMarkHighDealResult = hostingRepository.findHighBenchMarkDealForVirtPrivUnixSmall(
				assessmentHostingInfo.isOffshoreAllowed(), assessmentHostingInfo.getLevelOfService(),
				assessmentHostingInfo.isIncludeHardware(), assessmentHostingInfo.isIncludeTooling(),
				assessmentHostingInfo.getCoLocation(), DealTypeEnum.BENCHMARK_DEAL.getName(), avgVolume.intValue(),
				new PageRequest(0, 1));
		log.info("HIGH Virtual Private Unix Small benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor, VIRTUAL_PRIVATE_UNIX_SMALL);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverage(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentHostingInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, totalVolume);
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
	 * @param assessmentHostingInfo
	 * @param benchmarkHostingInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param totalVolume
	 */
	private void prepareBenchmarkDealResultDto(HostingInfo assessmentHostingInfo, HostingInfo benchmarkHostingInfo,
			Map<Long, BigDecimal> dealAvgVolumeMap, DealResultDto dealResultDto, int totalVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkHostingInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkAverageUnitPrice(assessmentHostingInfo,
				benchmarkHostingInfo.getHostingYearlyDataInfoList(), totalVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkAverageUnitPrice(assessmentHostingInfo,
				benchmarkHostingInfo.getHostingYearlyDataInfoList(), totalVolume, BENCHMARK_TARGET));

	}

	/**
	 * @param assessmentHostingInfo
	 * @param dealResultsYearlyList
	 * @param totalVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkAverageUnitPrice(HostingInfo assessmentHostingInfo,
			List<HostingYearlyDataInfo> dealResultsYearlyList, int totalVolume, String benchMarkType) {
		BigDecimal revenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (HostingYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (HostingUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo
					.getHostingUnitPriceInfoList()) {
				for (HostingYearlyDataInfo assessmentYearlyDataInfo : assessmentHostingInfo
						.getHostingYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getHostingYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						revenue = revenue.add(benchmarkUnitPriceInfo.getVirtualPrivateUnixSmall()
								.multiply(new BigDecimal(assessmentYearlyDataInfo.getVirtualPrivateUnixSmall())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = revenue.divide(new BigDecimal(totalVolume), 2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
