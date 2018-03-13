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
public class MailContactsDealResultsHelper {

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
	public DealResultsResponse getNearestPastDealsForMailContacts(ServiceDeskInfo serviceDeskInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgMailContacts = serviceDeskCommonHelper
				.getMailContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		// Past Deal Calculation
		Specification<ServiceDeskInfo> mailContactsSpecification = ServiceDeskSpecification.specificationForMailContacts(
				serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(), serviceDeskInfo.isToolingIncluded(),
				serviceDeskInfo.getLevelOfService(), avgMailContacts, towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(mailContactsSpecification);
		log.info("Mail Contacts dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageMailContactsVolume(pastDealResults);
			absVolumeDiffPercMap = serviceDeskCommonHelper.prepareDealAbsVolumeDiff(avgMailContacts, dealAvgVolumeMap);
			int mailContactsVolume = getMailContactsVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageMailContactsUnitPrice(serviceDeskInfo, pastDealResults,
					mailContactsVolume);

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
	private Map<Long, BigDecimal> prepareDealAverageMailContactsVolume(List<ServiceDeskInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyavgMailContactsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo ServiceDeskInfo : pastDealResults) {
				Long dealId = ServiceDeskInfo.getDealInfo().getDealId();
				BigDecimal avgMailContactsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int mailContactsVolume = 0;
				int size = 0;
				for (ServiceDeskYearlyDataInfo yearlyDataInfo : ServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyDataInfo.getMailContacts() != 0) {
						mailContactsVolume += yearlyDataInfo.getMailContacts();
						size++;
					}
				}
				if (size != 0) {
					avgMailContactsVolume = new BigDecimal(mailContactsVolume / size);
				}
				yearlyavgMailContactsVolumeMap.put(dealId, avgMailContactsVolume);
			}
		}
		return yearlyavgMailContactsVolumeMap;

	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param pastDealResults
	 * @param mailContactsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageMailContactsUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskInfo> pastDealResults, int mailContactsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ServiceDeskInfo pastServiceDeskInfo : pastDealResults) {
			BigDecimal mailRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ServiceDeskYearlyDataInfo pastYearlyDataInfo : pastServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
				for (ServiceDeskUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
					for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								mailRevenue = mailRevenue.add(pastUnitPriceInfo.getMailContactsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getMailContacts())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = mailRevenue.divide(new BigDecimal(mailContactsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastServiceDeskInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public int getMailContactsVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		int mailContactsVolume = 0;
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				mailContactsVolume += serviceDeskYearlyDataInfo.getMailContacts();
			}
		}
		return mailContactsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForMailContacts(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgMailContacts = serviceDeskCommonHelper
				.getMailContactsAverageVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		int mailContactsVolume = getMailContactsVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgMailContacts, dealResultsResponse, mailContactsVolume);

		prepareHighBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgMailContacts, dealResultsResponse, mailContactsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgMailContacts
	 * @param dealResultsResponse
	 * @param mailContactsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgMailContacts,
			DealResultsResponse dealResultsResponse, int mailContactsVolume) {
		List<ServiceDeskInfo> benchMarkLowAndSelectedDealResults = serviceDeskRepository.findLowBenchMarkDealForMailContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgMailContacts.intValue(), new PageRequest(0, 2));
		log.info("LOW Mail Contacts benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageMailContactsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, mailContactsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, mailContactsVolume);
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
	 * @param avgMailContacts
	 * @param dealResultsResponse
	 * @param mailContactsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgMailContacts, DealResultsResponse dealResultsResponse, int mailContactsVolume) {
		List<ServiceDeskInfo> benchMarkHighDealResult = serviceDeskRepository.findHighBenchMarkDealForMailContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgMailContacts.intValue(), new PageRequest(0, 1));
		log.info("HIGH Mail Contacts benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageMailContactsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, mailContactsVolume);
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
	 * @param mailContactsVolume
	 */
	private void prepareBenchmarkDealResultDto(ServiceDeskInfo assessmentServiceDeskInfo,
			ServiceDeskInfo benchmarkServiceDeskInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int mailContactsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkServiceDeskInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkMailAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), mailContactsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkMailAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), mailContactsVolume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param dealResultsYearlyList
	 * @param mailContactsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkMailAverageUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskYearlyDataInfo> dealResultsYearlyList, int mailContactsVolume, String benchMarkType) {
		BigDecimal mailRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ServiceDeskYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ServiceDeskUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
				for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						mailRevenue = mailRevenue.add(benchmarkUnitPriceInfo.getMailContactsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getMailContacts())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = mailRevenue.divide(new BigDecimal(mailContactsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
