package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.repository.CountryFactorRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.TowerSpecificBandRepository;
import com.in.fujitsu.pricing.servicedesk.calculator.ChatContactsCalculator;
import com.in.fujitsu.pricing.servicedesk.calculator.MailContactsCalculator;
import com.in.fujitsu.pricing.servicedesk.calculator.PortalContactsCalculator;
import com.in.fujitsu.pricing.servicedesk.calculator.TotalContactsCalculator;
import com.in.fujitsu.pricing.servicedesk.calculator.VoiceContactsCalculator;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskCalculateDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskDropdownDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskPriceDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueDto;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskContactRatioInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskSolutionsInfo;
import com.in.fujitsu.pricing.servicedesk.helper.ChatContactsDealResultsHelper;
import com.in.fujitsu.pricing.servicedesk.helper.MailContactsDealResultsHelper;
import com.in.fujitsu.pricing.servicedesk.helper.PortalContactsDealResultsHelper;
import com.in.fujitsu.pricing.servicedesk.helper.ServiceDeskBeanConvertor;
import com.in.fujitsu.pricing.servicedesk.helper.ServiceDeskCommonHelper;
import com.in.fujitsu.pricing.servicedesk.helper.TotalContactsDealResultsHelper;
import com.in.fujitsu.pricing.servicedesk.helper.VoiceContactsDealResultsHelper;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskContactRatioRepository;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskRepository;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskSolutionsRepository;
import com.in.fujitsu.pricing.specification.ServiceDeskSpecification;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mishrasub
 *
 */
@Slf4j
@Service
public class ServiceDeskService {

	@Autowired
	private ServiceDeskSolutionsRepository solutionsRepository;

	@Autowired
	private ServiceDeskContactRatioRepository contactRatioRepository;

	@Autowired
	private ServiceDeskRepository serviceDeskRepository;

	@Autowired
	private GenericService genericService;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private ServiceDeskBeanConvertor beanConvertor;

	@Autowired
	private ServiceDeskCommonHelper serviceDeskCommonHelper;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private DealRepository dealRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TotalContactsDealResultsHelper totalContactsDealResultsHelper;

	@Autowired
	private VoiceContactsDealResultsHelper voiceContactsDealResultsHelper;

	@Autowired
	private MailContactsDealResultsHelper mailContactsDealResultsHelper;

	@Autowired
	private ChatContactsDealResultsHelper chatContactsDealResultsHelper;

	@Autowired
	private PortalContactsDealResultsHelper portalContactsDealResultsHelper;

	private final String SERVICE_DESK_TOWER = "ServiceDesk";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";
	private final String TOTAL_CONTACTS_LEVEL = "1.1";
	private final String VOICE_CONTACTS_LEVEL = "1.1.1";
	private final String MAIL_CONTACTS_LEVEL = "1.1.2";
	private final String CHAT_CONTACTS_LEVEL = "1.1.3";
	private final String PORTAL_CONTACTS_LEVEL = "1.1.4";

	/**
	 * @param dealId
	 * @return
	 */
	public ServiceDeskInfoDto getServiceDeskDetails(long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
		if (serviceDeskInfo == null) {
			throw new ServiceException("DB doesn't have the Service desk Details for given dealId.");
		}
		return beanConvertor.prepareServiceDeskInfoDto(serviceDeskInfo);

	}

	/**
	 * @param dealId
	 * @param serviceDeskInfoDto
	 * @return
	 */
	@Transactional
	public ServiceDeskInfoDto saveServiceDeskDetails(Long dealId, ServiceDeskInfoDto serviceDeskInfoDto) {
		ServiceDeskInfo serviceDeskInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
		if (null != serviceDeskInfo) {
			serviceDeskInfo = beanConvertor.prepareServiceDeskInfo(serviceDeskInfo, serviceDeskInfoDto, false);
		} else {
			serviceDeskInfo = beanConvertor.prepareServiceDeskInfo(new ServiceDeskInfo(), serviceDeskInfoDto, true);
		}
		serviceDeskInfo = serviceDeskRepository.saveAndFlush(serviceDeskInfo);

		return beanConvertor.prepareServiceDeskInfoDto(serviceDeskInfo);
	}

	/**
	 * @param dealId
	 * @return
	 */
	public ServiceDeskDropdownDto getServiceDeskDropDownDetails(Long dealId) {
		ServiceDeskDropdownDto serviceDeskDropdownDto = new ServiceDeskDropdownDto();
		serviceDeskDropdownDto.setStandardWindowInfoList(genericService.getServicedWindowInfo());
		serviceDeskDropdownDto.setYesNoOptionList(genericService.getOffshreAndHardwareInfo());

		final List<ServiceDeskSolutionsInfo> serviceDeskSolutionsInfoList = solutionsRepository.findAll();

		serviceDeskDropdownDto.setServiceDeskSolutionsDtoList(
				beanConvertor.prepareContactSolutionsDtoList(serviceDeskSolutionsInfoList));

		final List<ServiceDeskContactRatioInfo> serviceDeskContactRatioInfoList = contactRatioRepository.findAll();

		serviceDeskDropdownDto.setServiceDeskContactRatioDtoList(
				beanConvertor.prepareContactRatioDtoList(serviceDeskContactRatioInfoList));

		if (dealId != null) {
			serviceDeskDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return serviceDeskDropdownDto;
	}

	/**
	 * @param serviceDeskPriceDtoList
	 * @param serviceDeskId
	 * @return
	 */
	public ResponseEntity<Object> updateServiceDeskPrice(List<ServiceDeskPriceDto> serviceDeskPriceDtoList,
			Long serviceDeskId) throws ServiceException {
		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findOne(serviceDeskId);
		if (null != serviceDeskInfo) {
			serviceDeskInfo = beanConvertor.prepareServiceDeskPrice(serviceDeskInfo, serviceDeskPriceDtoList);
			serviceDeskRepository.saveAndFlush(serviceDeskInfo);
		} else {
			throw new ServiceException("No ServiceDeskInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);
	}

	/**
	 * Method for updating the Solution Criteria
	 *
	 * @param storageUnitPriceDtoList
	 * @param serviceDeskId
	 * @return
	 * @throws ServiceException
	 */
	public ServiceDeskRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionDto, Long serviceDeskId)
			throws ServiceException {
		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findOne(serviceDeskId);
		if (null != serviceDeskInfo) {
			serviceDeskInfo = beanConvertor.prepareSolutionCriteria(serviceDeskInfo, solutionDto);
			serviceDeskRepository.saveAndFlush(serviceDeskInfo);
		} else {
			throw new ServiceException("No ServiceDeskInfo data to update");
		}

		return getYearlyRevenues(serviceDeskInfo.getDealInfo().getDealId());
	}

	/**
	 * @param dealId
	 * @param levelName
	 * @param dealType
	 * @return
	 * @throws Exception
	 */
	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		DealResultsResponse dealResultsDto = new DealResultsResponse();
		if (dealInfo != null) {
			ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
			if (serviceDeskInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(SERVICE_DESK_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (TOTAL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = totalContactsDealResultsHelper.getNearestPastDealsForTotalContacts(
								serviceDeskInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (VOICE_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = voiceContactsDealResultsHelper.getNearestPastDealsForVoiceContacts(
								serviceDeskInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (MAIL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mailContactsDealResultsHelper.getNearestPastDealsForMailContacts(
								serviceDeskInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (CHAT_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = chatContactsDealResultsHelper.getNearestPastDealsForChatContacts(
								serviceDeskInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (PORTAL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = portalContactsDealResultsHelper.getNearestPastDealsForPortalContacts(
								serviceDeskInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					}

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (TOTAL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = totalContactsDealResultsHelper.getNearestBenchmarkDealsForTotalContacts(
								serviceDeskInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VOICE_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = voiceContactsDealResultsHelper.getNearestBenchmarkDealsForVoiceContacts(
								serviceDeskInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (MAIL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mailContactsDealResultsHelper.getNearestBenchmarkDealsForMailContacts(
								serviceDeskInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (CHAT_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = chatContactsDealResultsHelper.getNearestBenchmarkDealsForChatContacts(
								serviceDeskInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PORTAL_CONTACTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = portalContactsDealResultsHelper.getNearestBenchmarkDealsForPortalContacts(
								serviceDeskInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					}

				}

			} else {
				throw new ServiceException("Can't find the Service Desk Detail for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;
	}

	/**
	 * @param dealId
	 * @return
	 */
	public ServiceDeskRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		ServiceDeskRevenueDto serviceDeskRevenueDto = new ServiceDeskRevenueDto();
		if (dealInfo != null) {
			ServiceDeskCalculateDto totalContactsCalculateDto = null;
			ServiceDeskCalculateDto voiceContactsCalculateDto = null;
			ServiceDeskCalculateDto mailContactsCalculateDto = null;
			ServiceDeskCalculateDto chatContactsCalculateDto = null;
			ServiceDeskCalculateDto portalContactsCalculateDto = null;

			ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
			List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
			// currency conversion based on the FX Rates and country factor
			String referenceCountry = dealInfo.getCountry();
			BigDecimal referenceCountryFactor = new BigDecimal(1);
			for (CountryFactorInfo countryFactorInfo : countryFactors) {
				if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
					referenceCountryFactor = countryFactorInfo.getCountryFactor();
					break;
				}
			}

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(SERVICE_DESK_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			String levelInd = serviceDeskInfo.getLevelIndicator();

			if (levelInd != null && levelInd.equals("0")) {

				log.info("Got correct position for Total Contacts calculation.");
				// Total Contacts calculation
				totalContactsCalculateDto = calculateTotalContactsYearlyRevenue(serviceDeskInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(totalContactsCalculateDto)){
					serviceDeskRevenueDto.setTotalContactsCalculateDto(totalContactsCalculateDto);
				}

			} else if (levelInd != null && levelInd.equals("1")) {
				// Voice Contacts calculation
				voiceContactsCalculateDto = calculateVoiceContactsYearlyRevenue(serviceDeskInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(voiceContactsCalculateDto)){
					serviceDeskRevenueDto.setVoiceContactsCalculateDto(voiceContactsCalculateDto);
				}

				// Mail Contacts calculation
				mailContactsCalculateDto = calculateMailContactsYearlyRevenue(serviceDeskInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mailContactsCalculateDto)){
					serviceDeskRevenueDto.setMailContactsCalculateDto(mailContactsCalculateDto);
				}

				// Chat Contacts calculation
				chatContactsCalculateDto = calculateChatContactsYearlyRevenue(serviceDeskInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(chatContactsCalculateDto)){
					serviceDeskRevenueDto.setChatContactsCalculateDto(chatContactsCalculateDto);
				}

				// Portal Contacts calculation
				portalContactsCalculateDto = calculatePortalContactsYearlyRevenue(serviceDeskInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(portalContactsCalculateDto)){
					serviceDeskRevenueDto.setPortalContactsCalculateDto(portalContactsCalculateDto);
				}
			}
		}

		return serviceDeskRevenueDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	private ServiceDeskCalculateDto calculateTotalContactsYearlyRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgTotalContacts = serviceDeskCommonHelper
				.getTotalContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		ServiceDeskCalculateDto totalContactsCalculateDto = null;
		TotalContactsCalculator totalContactsCalculator = new TotalContactsCalculator();
		if (avgTotalContacts != null && avgTotalContacts.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			totalContactsCalculateDto = calculateTotalContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgTotalContacts, totalContactsCalculateDto,
							totalContactsCalculator);

			// Benchmark Calculation
			totalContactsCalculateDto = calculateTotalContactsBenchMarkDealRevenue(serviceDeskInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgTotalContacts, totalContactsCalculateDto,
					totalContactsCalculator);
		}
		return totalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgTotalContacts
	 * @param totalContactsCalculateDto
	 * @param totalContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateTotalContactsBenchMarkDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgTotalContacts,
			ServiceDeskCalculateDto totalContactsCalculateDto, TotalContactsCalculator totalContactsCalculator) {
		Specification<ServiceDeskInfo> totalContactsSpecificationBenchMark = ServiceDeskSpecification
				.specificationForBenchMarkTotalContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgTotalContacts);
		List<ServiceDeskInfo> benchMarkDealResults = serviceDeskRepository
				.findAll(totalContactsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Total Contacts benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			totalContactsCalculateDto = totalContactsCalculator.prepareTotalContactsCalculateDtoForBenchmark(
					benchMarkDealResults, serviceDeskInfo,
					totalContactsCalculateDto == null ? new ServiceDeskCalculateDto() : totalContactsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return totalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgTotalContacts
	 * @param totalContactsCalculateDto
	 * @param totalContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateTotalContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgTotalContacts,
			ServiceDeskCalculateDto totalContactsCalculateDto, TotalContactsCalculator totalContactsCalculator) {
		Specification<ServiceDeskInfo> totalContactsSpecification = ServiceDeskSpecification
				.specificationForTotalContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgTotalContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(totalContactsSpecification);
		log.info("Total Contacts dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			totalContactsCalculateDto = totalContactsCalculator.prepareTotalContactsCalculateDtoForPastDeal(
					pastDealResults, serviceDeskInfo, new ServiceDeskCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return totalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	private ServiceDeskCalculateDto calculateVoiceContactsYearlyRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgVoiceContacts = serviceDeskCommonHelper
				.getVoiceContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		ServiceDeskCalculateDto voiceContactsCalculateDto = null;
		VoiceContactsCalculator voiceContactsCalculator = new VoiceContactsCalculator();
		if (avgVoiceContacts != null && avgVoiceContacts.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			voiceContactsCalculateDto = calculateVoiceContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgVoiceContacts, voiceContactsCalculateDto,
							voiceContactsCalculator);

			// Benchmark Calculation
			voiceContactsCalculateDto = calculateVoiceContactsBenchMarkDealRevenue(serviceDeskInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgVoiceContacts, voiceContactsCalculateDto,
					voiceContactsCalculator);
		}

		return voiceContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgVoiceContacts
	 * @param voiceContactsCalculateDto
	 * @param voiceContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateVoiceContactsBenchMarkDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgVoiceContacts,
			ServiceDeskCalculateDto voiceContactsCalculateDto, VoiceContactsCalculator voiceContactsCalculator) {
		Specification<ServiceDeskInfo> voiceContactsSpecificationBenchMark = ServiceDeskSpecification
				.specificationForBenchMarkVoiceContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgVoiceContacts);
		List<ServiceDeskInfo> benchMarkDealResults = serviceDeskRepository
				.findAll(voiceContactsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Voice Contacts benchmark dealResults.size" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			voiceContactsCalculateDto = voiceContactsCalculator.prepareVoiceContactsCalculateDtoForBenchmark(
					benchMarkDealResults, serviceDeskInfo,
					voiceContactsCalculateDto == null ? new ServiceDeskCalculateDto() : voiceContactsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return voiceContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgVoiceContacts
	 * @param voiceContactsCalculateDto
	 * @param voiceContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateVoiceContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgVoiceContacts,
			ServiceDeskCalculateDto voiceContactsCalculateDto, VoiceContactsCalculator voiceContactsCalculator) {
		Specification<ServiceDeskInfo> voiceContactsSpecification = ServiceDeskSpecification
				.specificationForVoiceContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgVoiceContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(voiceContactsSpecification);
		log.info("Voice Contacts dealResults.size" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			voiceContactsCalculateDto = voiceContactsCalculator.prepareVoiceContactsCalculateDtoForPastDeal(
					pastDealResults, serviceDeskInfo, new ServiceDeskCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return voiceContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private ServiceDeskCalculateDto calculateMailContactsYearlyRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgMailContacts = serviceDeskCommonHelper
				.getMailContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		ServiceDeskCalculateDto mailContactsCalculateDto = null;
		MailContactsCalculator mailContactsCalculator = new MailContactsCalculator();
		if (avgMailContacts != null && avgMailContacts.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			mailContactsCalculateDto = calculateMailContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgMailContacts, mailContactsCalculateDto,
							mailContactsCalculator);

			// Benchmark Calculation
			mailContactsCalculateDto = calculateMailContactsBenchMarkDealRevenue(serviceDeskInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgMailContacts, mailContactsCalculateDto,
					mailContactsCalculator);
		}

		return mailContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgMailContacts
	 * @param mailContactsCalculateDto
	 * @param mailContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateMailContactsBenchMarkDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgMailContacts,
			ServiceDeskCalculateDto mailContactsCalculateDto, MailContactsCalculator mailContactsCalculator) {
		Specification<ServiceDeskInfo> mailContactsSpecificationBenchMark = ServiceDeskSpecification
				.specificationForBenchMarkMailContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgMailContacts);
		List<ServiceDeskInfo> benchMarkDealResults = serviceDeskRepository
				.findAll(mailContactsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Mail Contacts benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			mailContactsCalculateDto = mailContactsCalculator.prepareMailContactsCalculateDtoForBenchmark(
					benchMarkDealResults, serviceDeskInfo,
					mailContactsCalculateDto == null ? new ServiceDeskCalculateDto() : mailContactsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return mailContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgMailContacts
	 * @param mailContactsCalculateDto
	 * @param mailContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateMailContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgMailContacts,
			ServiceDeskCalculateDto mailContactsCalculateDto, MailContactsCalculator mailContactsCalculator) {
		Specification<ServiceDeskInfo> mailContactsSpecification = ServiceDeskSpecification
				.specificationForMailContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgMailContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(mailContactsSpecification);
		log.info("Mail Contacts dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			mailContactsCalculateDto = mailContactsCalculator.prepareMailContactsCalculateDtoForPastDeal(
					pastDealResults, serviceDeskInfo, new ServiceDeskCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return mailContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private ServiceDeskCalculateDto calculateChatContactsYearlyRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgChatContacts = serviceDeskCommonHelper
				.getChatContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		ServiceDeskCalculateDto chatContactsCalculateDto = null;
		ChatContactsCalculator chatContactsCalculator = new ChatContactsCalculator();
		if (avgChatContacts != null && avgChatContacts.compareTo(new BigDecimal(0)) != 0) {
			chatContactsCalculateDto = calculateChatContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgChatContacts, chatContactsCalculateDto,
							chatContactsCalculator);

			// Benchmark Calculation
			chatContactsCalculateDto = calculateChatContactsBenchMarkDealRevenue(serviceDeskInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgChatContacts, chatContactsCalculateDto,
					chatContactsCalculator);
		}
		return chatContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgChatContacts
	 * @param chatContactsCalculateDto
	 * @param chatContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateChatContactsBenchMarkDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgChatContacts,
			ServiceDeskCalculateDto chatContactsCalculateDto, ChatContactsCalculator chatContactsCalculator) {
		Specification<ServiceDeskInfo> chatContactsSpecificationBenchMark = ServiceDeskSpecification
				.specificationForBenchMarkChatContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgChatContacts);
		List<ServiceDeskInfo> benchMarkDealResults = serviceDeskRepository
				.findAll(chatContactsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Chat Contacts benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			chatContactsCalculateDto = chatContactsCalculator.prepareChatContactsCalculateDtoForBenchmark(
					benchMarkDealResults, serviceDeskInfo,
					chatContactsCalculateDto == null ? new ServiceDeskCalculateDto() : chatContactsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return chatContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgChatContacts
	 * @param chatContactsCalculateDto
	 * @param chatContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculateChatContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgChatContacts,
			ServiceDeskCalculateDto chatContactsCalculateDto, ChatContactsCalculator chatContactsCalculator) {
		// Past Deal Calculation
		Specification<ServiceDeskInfo> chatContactsSpecification = ServiceDeskSpecification
				.specificationForChatContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgChatContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(chatContactsSpecification);
		log.info("Chat Contacts dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			chatContactsCalculateDto = chatContactsCalculator.prepareChatContactsCalculateDtoForPastDeal(
					pastDealResults, serviceDeskInfo, new ServiceDeskCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return chatContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private ServiceDeskCalculateDto calculatePortalContactsYearlyRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgPortalContacts = serviceDeskCommonHelper
				.getPortalContactsAverageVolume(serviceDeskInfo.getServiceDeskYearlyDataInfoList());
		ServiceDeskCalculateDto portalContactsCalculateDto = null;
		PortalContactsCalculator portalContactsCalculator = new PortalContactsCalculator();
		if (avgPortalContacts != null && avgPortalContacts.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			portalContactsCalculateDto = calculatePortalContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgPortalContacts, portalContactsCalculateDto,
							portalContactsCalculator);

			// Benchmark Calculation
			portalContactsCalculateDto = calculatePortalContactsPastDealRevenue(serviceDeskInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgPortalContacts,
					portalContactsCalculateDto, portalContactsCalculator);
		}

		return portalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgPortalContacts
	 * @param portalContactsCalculateDto
	 * @param portalContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculatePortalContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgPortalContacts,
			ServiceDeskCalculateDto portalContactsCalculateDto, PortalContactsCalculator portalContactsCalculator) {
		Specification<ServiceDeskInfo> portalContactsSpecificationBenchMark = ServiceDeskSpecification
				.specificationForBenchMarkPortalContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgPortalContacts);
		List<ServiceDeskInfo> benchMarkDealResults = serviceDeskRepository
				.findAll(portalContactsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Portal Contacts benchmark dealResults.size" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			portalContactsCalculateDto = portalContactsCalculator.preparePortalContactsCalculateDtoForBenchmark(
					benchMarkDealResults, serviceDeskInfo,
					portalContactsCalculateDto == null ? new ServiceDeskCalculateDto() : portalContactsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return portalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgPortalContacts
	 * @param portalContactsCalculateDto
	 * @param portalContactsCalculator
	 * @return
	 */
	@Transactional
	private ServiceDeskCalculateDto calculatePortalContactsPastDealRevenue(ServiceDeskInfo serviceDeskInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgPortalContacts,
			ServiceDeskCalculateDto portalContactsCalculateDto, PortalContactsCalculator portalContactsCalculator) {
		Specification<ServiceDeskInfo> portalContactsSpecification = ServiceDeskSpecification
				.specificationForPortalContacts(serviceDeskInfo.isOffshoreAllowed(), serviceDeskInfo.isMultiLingual(),
						serviceDeskInfo.isToolingIncluded(), serviceDeskInfo.getLevelOfService(), avgPortalContacts,
						towerSpecificBandInfo.getBandPercentage());
		List<ServiceDeskInfo> pastDealResults = serviceDeskRepository.findAll(portalContactsSpecification);
		log.info("Portal Contacts dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			serviceDeskCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			serviceDeskCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor);
			portalContactsCalculateDto = portalContactsCalculator.preparePortalContactsCalculateDtoForPastDeal(
					pastDealResults, serviceDeskInfo, new ServiceDeskCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return portalContactsCalculateDto;
	}

}
