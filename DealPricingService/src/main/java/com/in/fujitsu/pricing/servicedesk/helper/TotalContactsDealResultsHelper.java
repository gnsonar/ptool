package com.in.fujitsu.pricing.servicedesk.helper;

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
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskUnitPriceInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskRepository;
import com.in.fujitsu.pricing.specification.ServiceDeskSpecification;
import com.in.fujitsu.pricing.utility.CommonMapUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author MishraSub
 *
 */
@Slf4j
@Service
public class TotalContactsDealResultsHelper {

	@Autowired
	private ServiceDeskRepository serviceDeskRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ServiceDeskCommonHelper serviceDeskCommonHelper;

	private final String BENCHMARK_LOW = "Low";
	private final String BENCHMARK_TARGET = "Target";

	/**
	 * @param serviceDeskInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	public DealResultsResponse getNearestPastDealsForTotalContacts(ServiceDeskInfo serviceDeskInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgTotalContacts = serviceDeskCommonHelper
				.getTotalContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		// Past Deal Calculation
		Specification<ServiceDeskInfo> totalContactsSpecification = ServiceDeskSpecification.specificationForTotalContacts(
				serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(), serviceDeskInfo.isToolingIncluded(),
				serviceDeskInfo.getLevelOfService(), avgTotalContacts, towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(totalContactsSpecification);
		log.info("Total Contacts dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageTotalContactsVolume(pastDealResults);
			absVolumeDiffPercMap = serviceDeskCommonHelper.prepareDealAbsVolumeDiff(avgTotalContacts, dealAvgVolumeMap);
			int totalContactsVolume = getTotalContactsVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageTotalContactsUnitPrice(serviceDeskInfo, pastDealResults,
					totalContactsVolume);

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
	private void setNearestDealInResult(List<ServiceDeskInfo> pastDealResults, DealResultsResponse dealResultsResponse,
			Map<Long, BigDecimal> dealAvgVolumeMap, Map<Long, BigDecimal> sortedAbsVolumeDiffPercMap,
			Map<Long, BigDecimal> sortedAverageUnitPriceMap) {
		Long expensiveDealId = CommonMapUtils.getFirstElement(sortedAverageUnitPriceMap.keySet());
		Long cheapestDealId = CommonMapUtils.getLastElement(sortedAverageUnitPriceMap.keySet());

		Map<Long, BigDecimal> nearestDealMapInVolume = CommonMapUtils.getFirstThreeEntries(3,
				sortedAbsVolumeDiffPercMap);
		List<DealResultDto> nearestDealsInVolume = new ArrayList<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo pastDealServiceDeskInfo : pastDealResults) {
				DealInfo pastDeallInfo = pastDealServiceDeskInfo.getDealInfo();
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
	private Map<Long, BigDecimal> prepareDealAverageTotalContactsVolume(List<ServiceDeskInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgTotalContactsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo ServiceDeskInfo : pastDealResults) {
				Long dealId = ServiceDeskInfo.getDealInfo().getDealId();
				BigDecimal avgTotalContactsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int totalContactsVolume = 0;
				int size = 0;
				for (ServiceDeskYearlyDataInfo yearlyDataInfo : ServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyDataInfo.getTotalContacts() != 0) {
						totalContactsVolume += yearlyDataInfo.getTotalContacts();
						size++;
					}
				}
				if (size != 0) {
					avgTotalContactsVolume = new BigDecimal(totalContactsVolume / size);
				}
				yearlyAvgTotalContactsVolumeMap.put(dealId, avgTotalContactsVolume);
			}
		}
		return yearlyAvgTotalContactsVolumeMap;

	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param pastDealResults
	 * @param totalContactsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageTotalContactsUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskInfo> pastDealResults, int totalContactsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ServiceDeskInfo pastServiceDeskInfo : pastDealResults) {
			BigDecimal totalRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ServiceDeskYearlyDataInfo pastYearlyDataInfo : pastServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
				for (ServiceDeskUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
					for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								totalRevenue = totalRevenue.add(pastUnitPriceInfo.getTotalContactsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getTotalContacts())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = totalRevenue.divide(new BigDecimal(totalContactsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastServiceDeskInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public int getTotalContactsVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		int totalContactsVolume = 0;
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				totalContactsVolume += serviceDeskYearlyDataInfo.getTotalContacts();
			}
		}
		return totalContactsVolume;
	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public DealResultsResponse getNearestBenchmarkDealsForTotalContacts(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgTotalContacts = serviceDeskCommonHelper
				.getTotalContactsAverageVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		int totalContactsVolume = getTotalContactsVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgTotalContacts, dealResultsResponse, totalContactsVolume);

		prepareHighBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgTotalContacts, dealResultsResponse, totalContactsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgTotalContacts
	 * @param dealResultsResponse
	 * @param totalContactsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgTotalContacts,
			DealResultsResponse dealResultsResponse, int totalContactsVolume) {
		List<ServiceDeskInfo> benchMarkLowAndSelectedDealResults = serviceDeskRepository.findLowBenchMarkDealForTotalContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgTotalContacts.intValue(), new PageRequest(0, 2));
		log.info("LOW Total Contacts benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageTotalContactsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, totalContactsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, totalContactsVolume);
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
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgTotalContacts
	 * @param dealResultsResponse
	 * @param totalContactsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgTotalContacts, DealResultsResponse dealResultsResponse, int totalContactsVolume) {
		List<ServiceDeskInfo> benchMarkHighDealResult = serviceDeskRepository.findHighBenchMarkDealForTotalContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgTotalContacts.intValue(), new PageRequest(0, 1));
		log.info("HIGH Total Contacts benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageTotalContactsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, totalContactsVolume);
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
	 * @param assessmentServiceDeskInfo
	 * @param benchmarkServiceDeskInfo
	 * @param dealAvgVolumeMap
	 * @param dealResultDto
	 * @param totalContactsVolume
	 */
	private void prepareBenchmarkDealResultDto(ServiceDeskInfo assessmentServiceDeskInfo,
			ServiceDeskInfo benchmarkServiceDeskInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int totalContactsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkServiceDeskInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkTotalAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), totalContactsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkTotalAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), totalContactsVolume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param dealResultsYearlyList
	 * @param avgTotalContacts
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkTotalAverageUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskYearlyDataInfo> dealResultsYearlyList, int avgTotalContacts, String benchMarkType) {
		BigDecimal totalRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ServiceDeskYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ServiceDeskUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
				for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						totalRevenue = totalRevenue.add(benchmarkUnitPriceInfo.getTotalContactsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getTotalContacts())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = totalRevenue.divide(new BigDecimal(avgTotalContacts),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
