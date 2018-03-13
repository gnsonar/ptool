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
public class PortalContactsDealResultsHelper {

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
	public DealResultsResponse getNearestPastDealsForPortalContacts(ServiceDeskInfo serviceDeskInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgPortalContacts = serviceDeskCommonHelper
				.getPortalContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		// Past Deal Calculation
		Specification<ServiceDeskInfo> portalContactsSpecification = ServiceDeskSpecification.specificationForPortalContacts(
				serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(), serviceDeskInfo.isToolingIncluded(),
				serviceDeskInfo.getLevelOfService(), avgPortalContacts, towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(portalContactsSpecification);
		log.info("Portal Contacts dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAveragePortalContactsVolume(pastDealResults);
			absVolumeDiffPercMap = serviceDeskCommonHelper.prepareDealAbsVolumeDiff(avgPortalContacts, dealAvgVolumeMap);
			int portalContactsVolume = getPortalContactsVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAveragePortalContactsUnitPrice(serviceDeskInfo, pastDealResults,
					portalContactsVolume);

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
	private Map<Long, BigDecimal> prepareDealAveragePortalContactsVolume(List<ServiceDeskInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyavgPortalContactsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo ServiceDeskInfo : pastDealResults) {
				Long dealId = ServiceDeskInfo.getDealInfo().getDealId();
				BigDecimal avgPortalContactsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int portalContactsVolume = 0;
				int size = 0;
				for (ServiceDeskYearlyDataInfo yearlyDataInfo : ServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyDataInfo.getPortalContacts() != 0) {
						portalContactsVolume += yearlyDataInfo.getPortalContacts();
						size++;
					}
				}
				if (size != 0) {
					avgPortalContactsVolume = new BigDecimal(portalContactsVolume / size);
				}
				yearlyavgPortalContactsVolumeMap.put(dealId, avgPortalContactsVolume);
			}
		}
		return yearlyavgPortalContactsVolumeMap;

	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param pastDealResults
	 * @param portalContactsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAveragePortalContactsUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskInfo> pastDealResults, int portalContactsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ServiceDeskInfo pastServiceDeskInfo : pastDealResults) {
			BigDecimal portalRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ServiceDeskYearlyDataInfo pastYearlyDataInfo : pastServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
				for (ServiceDeskUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
					for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								portalRevenue = portalRevenue.add(pastUnitPriceInfo.getPortalContactsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getPortalContacts())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = portalRevenue.divide(new BigDecimal(portalContactsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastServiceDeskInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public int getPortalContactsVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		int portalContactsVolume = 0;
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				portalContactsVolume += serviceDeskYearlyDataInfo.getPortalContacts();
			}
		}
		return portalContactsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForPortalContacts(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgPortalContacts = serviceDeskCommonHelper
				.getPortalContactsAverageVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		int portalContactsVolume = getPortalContactsVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgPortalContacts, dealResultsResponse, portalContactsVolume);

		prepareHighBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgPortalContacts, dealResultsResponse, portalContactsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgPortalContacts
	 * @param dealResultsResponse
	 * @param portalContactsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgPortalContacts,
			DealResultsResponse dealResultsResponse, int portalContactsVolume) {
		List<ServiceDeskInfo> benchMarkLowAndSelectedDealResults = serviceDeskRepository.findLowBenchMarkDealForPortalContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgPortalContacts.intValue(), new PageRequest(0, 2));
		log.info("LOW Portal Contacts benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAveragePortalContactsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, portalContactsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, portalContactsVolume);
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
	 * @param avgPortalContacts
	 * @param dealResultsResponse
	 * @param portalContactsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgPortalContacts, DealResultsResponse dealResultsResponse, int portalContactsVolume) {
		List<ServiceDeskInfo> benchMarkHighDealResult = serviceDeskRepository.findHighBenchMarkDealForPortalContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgPortalContacts.intValue(), new PageRequest(0, 1));
		log.info("HIGH Portal Contacts benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAveragePortalContactsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, portalContactsVolume);
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
	 * @param portalContactsVolume
	 */
	private void prepareBenchmarkDealResultDto(ServiceDeskInfo assessmentServiceDeskInfo,
			ServiceDeskInfo benchmarkServiceDeskInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int portalContactsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkServiceDeskInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkPortalAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), portalContactsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkPortalAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), portalContactsVolume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param dealResultsYearlyList
	 * @param portalContactsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkPortalAverageUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskYearlyDataInfo> dealResultsYearlyList, int portalContactsVolume, String benchMarkType) {
		BigDecimal portalRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ServiceDeskYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ServiceDeskUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
				for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						portalRevenue = portalRevenue.add(benchmarkUnitPriceInfo.getPortalContactsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getPortalContacts())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = portalRevenue.divide(new BigDecimal(portalContactsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
