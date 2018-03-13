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
public class ChatContactsDealResultsHelper {

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
	public DealResultsResponse getNearestPastDealsForChatContacts(ServiceDeskInfo serviceDeskInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		BigDecimal avgChatContacts = serviceDeskCommonHelper
				.getChatContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		// Past Deal Calculation
		Specification<ServiceDeskInfo> chatContactsSpecification = ServiceDeskSpecification.specificationForChatContacts(
				serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(), serviceDeskInfo.isToolingIncluded(),
				serviceDeskInfo.getLevelOfService(), avgChatContacts, towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(chatContactsSpecification);
		log.info("Chat Contacts dealResults.size :" + pastDealResults.size());

		DealResultsResponse dealResultsResponse = new DealResultsResponse();

		Map<Long, BigDecimal> dealAvgVolumeMap = new HashMap<>();
		Map<Long, BigDecimal> absVolumeDiffPercMap = new HashMap<>();
		Map<Long, BigDecimal> averageUnitPriceMap = new HashMap<>();

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			dealResultsResponse.setNoOfUsedDeals(pastDealResults.size());
			dealAvgVolumeMap = prepareDealAverageChatContactsVolume(pastDealResults);
			absVolumeDiffPercMap = serviceDeskCommonHelper.prepareDealAbsVolumeDiff(avgChatContacts, dealAvgVolumeMap);
			int chatContactsVolume = getChatContactsVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
			averageUnitPriceMap = prepareDealAverageChatContactsUnitPrice(serviceDeskInfo, pastDealResults,
					chatContactsVolume);

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
	private Map<Long, BigDecimal> prepareDealAverageChatContactsVolume(List<ServiceDeskInfo> pastDealResults) {
		// key - value -- >dealId - avgvolume
		final Map<Long, BigDecimal> yearlyavgChatContactsVolumeMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(pastDealResults)) {
			for (ServiceDeskInfo ServiceDeskInfo : pastDealResults) {
				Long dealId = ServiceDeskInfo.getDealInfo().getDealId();
				BigDecimal avgChatContactsVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
				int chatContactsVolume = 0;
				int size = 0;
				for (ServiceDeskYearlyDataInfo yearlyDataInfo : ServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyDataInfo.getChatContacts() != 0) {
						chatContactsVolume += yearlyDataInfo.getChatContacts();
						size++;
					}
				}
				if(size != 0){
					avgChatContactsVolume = new BigDecimal(chatContactsVolume / size);
				}
				yearlyavgChatContactsVolumeMap.put(dealId, avgChatContactsVolume);
			}
		}
		return yearlyavgChatContactsVolumeMap;

	}

	/**
	 * @param assessmentServiceDeskInfo
	 * @param pastDealResults
	 * @param chatContactsVolume
	 * @return
	 */
	private Map<Long, BigDecimal> prepareDealAverageChatContactsUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskInfo> pastDealResults, int chatContactsVolume) {
		Map<Long, BigDecimal> avergaeRevenueMap = new HashMap<>();
		for (ServiceDeskInfo pastServiceDeskInfo : pastDealResults) {
			BigDecimal chatRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
			for (ServiceDeskYearlyDataInfo pastYearlyDataInfo : pastServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
				for (ServiceDeskUnitPriceInfo pastUnitPriceInfo : pastYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
					for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
						if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo() != null) {
							if (pastUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
									.getYear()) {
								chatRevenue = chatRevenue.add(pastUnitPriceInfo.getChatContactsUnitPrice().multiply(
										new BigDecimal(assessmentYearlyDataInfo.getChatContacts())));
								break;
							}

						}

					}

				}
			}
			BigDecimal avgUnitPrice = chatRevenue.divide(new BigDecimal(chatContactsVolume),2, BigDecimal.ROUND_CEILING);
			avergaeRevenueMap.put(pastServiceDeskInfo.getDealInfo().getDealId(), avgUnitPrice);
		}
		return avergaeRevenueMap;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @return
	 */
	public int getChatContactsVolume(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		int chatContactsVolume = 0;
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoList)) {
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskYearlyDataInfoList) {
				chatContactsVolume += serviceDeskYearlyDataInfo.getChatContacts();
			}
		}
		return chatContactsVolume;
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
	public DealResultsResponse getNearestBenchmarkDealsForChatContacts(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor) {
		BigDecimal avgChatContacts = serviceDeskCommonHelper
				.getChatContactsAverageVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		int chatContactsVolume = getChatContactsVolume(assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList());
		DealResultsResponse dealResultsResponse = new DealResultsResponse();
		prepareSelectedAndLowBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors,
				referenceCurrency, referenceCountry, referenceCountryFactor, avgChatContacts, dealResultsResponse, chatContactsVolume);

		prepareHighBenchmarkDealResult(assessmentServiceDeskInfo, assessmentDealTerm, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, avgChatContacts, dealResultsResponse, chatContactsVolume);
		return dealResultsResponse;
	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param avgChatContacts
	 * @param dealResultsResponse
	 * @param chatContactsVolume
	 */
	@Transactional
	private void prepareSelectedAndLowBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, String referenceCurrency,
			String referenceCountry, BigDecimal referenceCountryFactor, BigDecimal avgChatContacts,
			DealResultsResponse dealResultsResponse, int chatContactsVolume) {
		List<ServiceDeskInfo> benchMarkLowAndSelectedDealResults = serviceDeskRepository
				.findLowBenchMarkDealForChatContacts(DealTypeEnum.BENCHMARK_DEAL.getName(),
						assessmentServiceDeskInfo.isOffshoreAllowed(), assessmentServiceDeskInfo.isMultiLingual(),
						assessmentServiceDeskInfo.isToolingIncluded(), assessmentServiceDeskInfo.getLevelOfService(),
						avgChatContacts.intValue(), new PageRequest(0, 2));
		log.info("LOW Chat Contacts benchmark dealResults.size :" + benchMarkLowAndSelectedDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkLowAndSelectedDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkLowAndSelectedDealResults, countryFactors,
					referenceCurrency, referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageChatContactsVolume(benchMarkLowAndSelectedDealResults);

			if (!CollectionUtils.isEmpty(benchMarkLowAndSelectedDealResults)) {

				if (benchMarkLowAndSelectedDealResults.size() > 0
						&& benchMarkLowAndSelectedDealResults.get(0) != null) {
					DealResultDto selectedBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(0),
							dealAvgVolumeMap, selectedBenchMarkDeal, chatContactsVolume);
					dealResultsResponse.setSelectedBenchMarkDeal(selectedBenchMarkDeal);
				}

				if (benchMarkLowAndSelectedDealResults.size() > 1
						&& benchMarkLowAndSelectedDealResults.get(1) != null) {
					DealResultDto lowBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkLowAndSelectedDealResults.get(1),
							dealAvgVolumeMap, lowBenchMarkDeal, chatContactsVolume);
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
	 * @param avgChatContacts
	 * @param dealResultsResponse
	 * @param chatContactsVolume
	 */
	@Transactional
	private void prepareHighBenchmarkDealResult(ServiceDeskInfo assessmentServiceDeskInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, BigDecimal avgChatContacts, DealResultsResponse dealResultsResponse, int chatContactsVolume) {
		List<ServiceDeskInfo> benchMarkHighDealResult = serviceDeskRepository.findHighBenchMarkDealForChatContacts(
				DealTypeEnum.BENCHMARK_DEAL.getName(), assessmentServiceDeskInfo.isOffshoreAllowed(),
				assessmentServiceDeskInfo.isMultiLingual(), assessmentServiceDeskInfo.isToolingIncluded(),
				assessmentServiceDeskInfo.getLevelOfService(), avgChatContacts.intValue(), new PageRequest(0, 1));
		log.info("HIGH Chat Contacts benchmark dealResults.size :" + benchMarkHighDealResult.size());

		if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkHighDealResult);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkHighDealResult, countryFactors, referenceCurrency,
					referenceCountry, referenceCountryFactor);

			Map<Long, BigDecimal> dealAvgVolumeMap = prepareDealAverageChatContactsVolume(benchMarkHighDealResult);
			if (!CollectionUtils.isEmpty(benchMarkHighDealResult)) {
				if (benchMarkHighDealResult.size() > 0 && benchMarkHighDealResult.get(0) != null) {
					DealResultDto highBenchMarkDeal = new DealResultDto();
					prepareBenchmarkDealResultDto(assessmentServiceDeskInfo, benchMarkHighDealResult.get(0),
							dealAvgVolumeMap, highBenchMarkDeal, chatContactsVolume);
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
	 * @param chatContactsVolume
	 */
	private void prepareBenchmarkDealResultDto(ServiceDeskInfo assessmentServiceDeskInfo,
			ServiceDeskInfo benchmarkServiceDeskInfo, Map<Long, BigDecimal> dealAvgVolumeMap,
			DealResultDto dealResultDto, int chatContactsVolume) {
		for (Map.Entry<Long, BigDecimal> entry : dealAvgVolumeMap.entrySet()) {
			if (entry.getKey() == benchmarkServiceDeskInfo.getDealInfo().getDealId()) {
				dealResultDto.setAverageVolume(entry.getValue());
			}
		}
		dealResultDto.setAveragePrice(getBenchmarkChatAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), chatContactsVolume, BENCHMARK_LOW));
		dealResultDto.setAverageTargetPrice(getBenchmarkChatAverageUnitPrice(assessmentServiceDeskInfo,
				benchmarkServiceDeskInfo.getServiceDeskYearlyDataInfoList(), chatContactsVolume, BENCHMARK_TARGET));

	}


	/**
	 * @param assessmentServiceDeskInfo
	 * @param dealResultsYearlyList
	 * @param chatContactsVolume
	 * @param benchMarkType
	 * @return
	 */
	private BigDecimal getBenchmarkChatAverageUnitPrice(ServiceDeskInfo assessmentServiceDeskInfo,
			List<ServiceDeskYearlyDataInfo> dealResultsYearlyList, int chatContactsVolume, String benchMarkType) {
		BigDecimal chatRevenue = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		for (ServiceDeskYearlyDataInfo benchmarkYearlyDataInfo : dealResultsYearlyList) {
			unitPriceLoop: for (ServiceDeskUnitPriceInfo benchmarkUnitPriceInfo : benchmarkYearlyDataInfo.getServiceDeskUnitPriceInfoList()) {
				for (ServiceDeskYearlyDataInfo assessmentYearlyDataInfo : assessmentServiceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (benchmarkUnitPriceInfo.getServiceDeskYearlyDataInfo().getYear() == assessmentYearlyDataInfo
							.getYear() && benchMarkType.equalsIgnoreCase(benchmarkUnitPriceInfo.getBenchMarkType())) {
						chatRevenue = chatRevenue.add(benchmarkUnitPriceInfo.getChatContactsUnitPrice().multiply(
								new BigDecimal(assessmentYearlyDataInfo.getChatContacts())));
						break unitPriceLoop;
					}

				}
			}
		}
		BigDecimal avgUnitPrice = chatRevenue.divide(new BigDecimal(chatContactsVolume),2, BigDecimal.ROUND_CEILING);
		return avgUnitPrice;
	}

}
