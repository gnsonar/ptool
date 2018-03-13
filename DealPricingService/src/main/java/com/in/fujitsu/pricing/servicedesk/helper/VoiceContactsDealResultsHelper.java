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
public class VoiceContactsDealResultsHelper {

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
	public DealResultsResponse getNearestPastDealsForVoiceContacts(ServiceDeskInfo serviceDeskInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgVoiceContacts = serviceDeskCommonHelper
				.getVoiceContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		// Past Deal Calculation
		Specification<ServiceDeskInfo> voiceContactsSpecification = ServiceDeskSpecification
				.specificationForVoiceContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgVoiceContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(voiceContactsSpecification);
		log.info("Voice Contacts dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageVoiceContactsVolume(pastDealResults);
			absVolumeDiffPercMap = serviceDeskCommonHelper.prepareDealAbsVolumeDiff(avgVoiceContacts, dealAvgVolumeMap);
			int voiceContactsVolume = getVoiceContactsVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageVoiceContactsUnitPrice(serviceDeskInfo, pastDealResults,
					voiceContactsVolume);

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
	private Map<Long, BigDecimal> prepareDealAverageVoiceContactsVolume(List<ServiceDeskInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyAvgVoiceContactsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo ServiceDeskInfo : pastDealResults) {
				Long dealId = ServiceDeskInfo.getDealInfo().getDealId();
				BigDecimal avgVoiceContactsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int voiceContactsVolume = 0;
				int size = 0;
				for (ServiceDeskYearlyDataInfo yearlyDataInfo : ServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyDataInfo.getVoiceContacts() != 0) {
						voiceContactsVolume += yearlyDataInfo.getVoiceContacts();
						size++;
					}
				}
				if (size != 0) {
					avgVoiceContactsVolume = new BigDecimal(voiceContactsVolume / size);
				}
				yearlyAvgVoiceContactsVolumeMap.put(dealId, avgVoiceContactsVolume);
			}
		}
		return yearlyAvgVoiceContactsVolumeMap;

	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param pastDealResults
	 * @param voiceContactsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageVoiceContactsUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskInfo> pastDealResults, int voiceContactsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ServiceDeskInfo pastServiceDeskInfo : pastDealResults) {
			BigDecimal voiceRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ServiceDeskYearlyDataInfo pastYearlyDataInfo : pastServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
				for (ServiceDeskUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
					for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								voiceRevenue = voiceRevenue.add(pastUnitPriceInfo.getVoiceContactsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getVoiceContacts())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = voiceRevenue.divide(new BigDecimal(voiceContactsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastServiceDeskInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public int getVoiceContactsVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		int voiceContactsVolume = 0;
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				voiceContactsVolume += serviceDeskYearlyDataInfo.getVoiceContacts();
			}
		}
		return voiceContactsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForVoiceContacts(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgVoiceContacts = serviceDeskCommonHelper
				.getVoiceContactsAverageVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		int voiceContactsVolume = getVoiceContactsVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgVoiceContacts, dealResultsResponse, voiceContactsVolume);

		prepareHighBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgVoiceContacts, dealResultsResponse, voiceContactsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgVoiceContacts
	 * @param dealResultsResponse
	 * @param voiceContactsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgVoiceContacts,
			DealResultsResponse dealResultsResponse, int voiceContactsVolume) {
		List<ServiceDeskInfo> benchMarkLowAndSelectedDealResults = serviceDeskRepository.findLowBenchMarkDealForVoiceContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgVoiceContacts.intValue(), new PageRequest(0, 2));
		log.info("LOW Voice Contacts benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageVoiceContactsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, voiceContactsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, voiceContactsVolume);
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
	 * @param avgVoiceContacts
	 * @param dealResultsResponse
	 * @param voiceContactsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgVoiceContacts, DealResultsResponse dealResultsResponse, int voiceContactsVolume) {
		List<ServiceDeskInfo> benchMarkHighDealResult = serviceDeskRepository.findHighBenchMarkDealForVoiceContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgVoiceContacts.intValue(), new PageRequest(0, 1));
		log.info("HIGH Voice Contacts benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageVoiceContactsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, voiceContactsVolume);
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
	 * @param voiceContactsVolume
	 */
	private void prepareBenchmarkDealResultDto(ServiceDeskInfo assessmentServiceDeskInfo,
			ServiceDeskInfo benchmarkServiceDeskInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int voiceContactsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkServiceDeskInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkVoiceAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), voiceContactsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkVoiceAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), voiceContactsVolume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param dealResultsYearlyList
	 * @param voiceContactsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkVoiceAverageUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskYearlyDataInfo> dealResultsYearlyList, int voiceContactsVolume, String benchMarkType) {
		BigDecimal voiceRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ServiceDeskYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ServiceDeskUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
				for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						voiceRevenue = voiceRevenue.add(benchmarkUnitPriceInfo.getVoiceContactsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getVoiceContacts())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = voiceRevenue.divide(new BigDecimal(voiceContactsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
