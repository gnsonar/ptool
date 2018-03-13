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

import com.in.fujitsu.pricing.application.calculator.ComplexApplicationCalculator;
import com.in.fujitsu.pricing.application.calculator.MediumApplicationCalculator;
import com.in.fujitsu.pricing.application.calculator.SimpleApplicationCalculator;
import com.in.fujitsu.pricing.application.calculator.TotalApplicationCalculator;
import com.in.fujitsu.pricing.application.calculator.VeryComplexApplicationCalculator;
import com.in.fujitsu.pricing.application.dto.ApplicationDropdownDto;
import com.in.fujitsu.pricing.application.dto.ApplicationInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationPriceDto;
import com.in.fujitsu.pricing.application.dto.ApplicationRevenueDto;
import com.in.fujitsu.pricing.application.dto.ComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.MediumApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.SimpleApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.TotalApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.VeryComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationSolutionsInfo;
import com.in.fujitsu.pricing.application.helper.ApplicationBeanConvertor;
import com.in.fujitsu.pricing.application.helper.ApplicationCommonHelper;
import com.in.fujitsu.pricing.application.helper.ComplexAppsDealResultsHelper;
import com.in.fujitsu.pricing.application.helper.MediumAppsDealResultsHelper;
import com.in.fujitsu.pricing.application.helper.SimpleAppsDealResultsHelper;
import com.in.fujitsu.pricing.application.helper.TotalAppsDealResultsHelper;
import com.in.fujitsu.pricing.application.helper.VeryComplexAppsDealResultsHelper;
import com.in.fujitsu.pricing.application.repository.ApplicationRepository;
import com.in.fujitsu.pricing.application.repository.ApplicationSolutionRepository;
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
import com.in.fujitsu.pricing.specification.ApplicationSpecification;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mishrasub
 *
 */
@Slf4j
@Service
public class ApplicationService {

	@Autowired
	private ApplicationSolutionRepository appSolutionRepository;

	@Autowired
	private ApplicationRepository appRepository;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private ApplicationBeanConvertor appBeanConvertor;

	@Autowired
	private ApplicationCommonHelper appCommonHelper;

	@Autowired
	private GenericService genericService;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TotalAppsDealResultsHelper totalAppsDealResultsHelper;

	@Autowired
	private SimpleAppsDealResultsHelper simpleAppsDealResultsHelper;

	@Autowired
	private MediumAppsDealResultsHelper mediumAppsDealResultsHelper;

	@Autowired
	private ComplexAppsDealResultsHelper complexAppsDealResultsHelper;

	@Autowired
	private VeryComplexAppsDealResultsHelper veryComplexAppsDealResultsHelper;

	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";
	private final String APPLICATION_TOWER = "Application";
	private final String TOTAL_LEVEL = "1.1";
	private final String SIMPLE_LEVEL = "1.1.1";
	private final String MEDIUM_LEVEL = "1.1.2";
	private final String COMPLEX_LEVEL = "1.1.3";
	private final String VERY_COMPLEX_LEVEL = "1.1.4";

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public ApplicationInfoDto getAppDetails(Long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		ApplicationInfo applicationInfo = appRepository.findByDealInfo(dealInfo);
		if (applicationInfo == null) {
			throw new ServiceException("DB doesn't have the Application Details for given dealId.");
		}
		return appBeanConvertor.prepareApplicationInfoDto(applicationInfo);
	}

	/**
	 * @param dealId
	 * @param appInfoDto
	 * @return
	 */
	@Transactional
	public ApplicationInfoDto saveAppDetails(Long dealId, ApplicationInfoDto appInfoDto) {
		ApplicationInfo appInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		appInfo = appRepository.findByDealInfo(dealInfo);
		if (null != appInfo) {
			appInfo = appBeanConvertor.prepareApplicationInfo(appInfo, appInfoDto, false);
		} else {
			appInfo = appBeanConvertor.prepareApplicationInfo(new ApplicationInfo(), appInfoDto, true);
		}
		appInfo = appRepository.saveAndFlush(appInfo);

		return appBeanConvertor.prepareApplicationInfoDto(appInfo);
	}

	/**
	 * @param dealId
	 * @return
	 */
	public ApplicationDropdownDto getAppDropDownDetails(Long dealId) {
		ApplicationDropdownDto applicationDropdownDto = new ApplicationDropdownDto();

		applicationDropdownDto.setStandardWindowInfoList(genericService.getServicedWindowInfo());
		applicationDropdownDto.setOffshoreAllowedOptionList(genericService.getOffshreAndHardwareInfo());

		final List<ApplicationSolutionsInfo> appSolutionsInfoList = appSolutionRepository.findAll();

		applicationDropdownDto
				.setApplicationSolutionsInfoDtoList(appBeanConvertor.prepareAppSolutionsDtoList(appSolutionsInfoList));

		if (dealId != null) {
			applicationDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return applicationDropdownDto;
	}

	/**
	 * @param applicationPriceDtoList
	 * @param appId
	 * @return
	 * @throws ServiceException
	 */
	public ResponseEntity<Object> updateAppPrice(List<ApplicationPriceDto> applicationPriceDtoList, Long appId)
			throws ServiceException {
		ApplicationInfo applicationInfo = appRepository.findOne(appId);
		if (null != applicationInfo) {
			applicationInfo = appBeanConvertor.prepareApplicationPrice(applicationInfo, applicationPriceDtoList);
			appRepository.saveAndFlush(applicationInfo);
		} else {
			throw new ServiceException("No ApplicationInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);
	}

	/**
	 * Method for updating the Solution Criteria
	 *
	 * @param storageUnitPriceDtoList
	 * @param appId
	 * @return
	 * @throws ServiceException
	 */
	public ApplicationRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionDto, Long appId)
			throws ServiceException {
		ApplicationInfo applicationInfo = appRepository.findOne(appId);
		if (null != applicationInfo) {
			applicationInfo = appBeanConvertor.prepareSolutionCriteria(applicationInfo, solutionDto);
			appRepository.saveAndFlush(applicationInfo);
		} else {
			throw new ServiceException("No ApplicationInfo data to update");
		}

		return getYearlyRevenues(applicationInfo.getDealInfo().getDealId());
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
			ApplicationInfo applicationInfo = appRepository.findByDealInfo(dealInfo);
			if (applicationInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(APPLICATION_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (TOTAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = totalAppsDealResultsHelper.getNearestPastDealsForTotal(applicationInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (SIMPLE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = simpleAppsDealResultsHelper.getNearestPastDealsForSimple(applicationInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumAppsDealResultsHelper.getNearestPastDealsForMedium(applicationInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (COMPLEX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = complexAppsDealResultsHelper.getNearestPastDealsForComplex(applicationInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VERY_COMPLEX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = veryComplexAppsDealResultsHelper.getNearestPastDealsForVeryComplex(
								applicationInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					}

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (TOTAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = totalAppsDealResultsHelper.getNearestBenchmarkDealsForTotal(applicationInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (SIMPLE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = simpleAppsDealResultsHelper.getNearestBenchmarkDealsForSimple(applicationInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumAppsDealResultsHelper.getNearestBenchmarkDealsForMedium(applicationInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (COMPLEX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = complexAppsDealResultsHelper.getNearestBenchmarkDealsForComplex(
								applicationInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VERY_COMPLEX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = veryComplexAppsDealResultsHelper.getNearestBenchmarkDealsForVeryComplex(
								applicationInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					}

				}

			} else {
				throw new ServiceException("Can't find the Application Detail for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;
	}

	/**
	 * @param dealId
	 * @return
	 * @throws ServiceException
	 */
	public ApplicationRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		ApplicationRevenueDto applicationRevenueDto = new ApplicationRevenueDto();
		if (dealInfo != null) {
			TotalApplicationCalculateDto totalApplicationCalculateDto = null;
			SimpleApplicationCalculateDto simpleApplicationCalculateDto = null;
			MediumApplicationCalculateDto mediumApplicationCalculateDto = null;
			ComplexApplicationCalculateDto complexApplicationCalculateDto = null;
			VeryComplexApplicationCalculateDto veryComplexApplicationCalculateDto = null;
			ApplicationInfo applicationInfo = appRepository.findByDealInfo(dealInfo);
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

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(APPLICATION_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			String levelInd = applicationInfo.getLevelIndicator();

			if (levelInd != null && levelInd.equals("0")) {

				log.info("Got correct position for Total Apps calculation.");
				// Total Applications calculation
				totalApplicationCalculateDto = calculateTotalAppsYearlyRevenue(applicationInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(totalApplicationCalculateDto)){
					applicationRevenueDto.setTotalApplicationCalculateDto(totalApplicationCalculateDto);
				}	

			} else if (levelInd != null && levelInd.equals("1")) {
				log.info("Got correct position for Simple/Medium/Complex/Very Complex Apps calculation.");
				// Simple Applications calculation
				simpleApplicationCalculateDto = calculateSimpleAppsYearlyRevenue(applicationInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(simpleApplicationCalculateDto)){
					applicationRevenueDto.setSimpleApplicationCalculateDto(simpleApplicationCalculateDto);
				}

				// Medium Applications calculation
				mediumApplicationCalculateDto = calculateMediumAppsYearlyRevenue(applicationInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mediumApplicationCalculateDto)){
					applicationRevenueDto.setMediumApplicationCalculateDto(mediumApplicationCalculateDto);
				}
					
				// Complex Applications calculation
				complexApplicationCalculateDto = calculateComplexAppsYearlyRevenue(applicationInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(complexApplicationCalculateDto)){
					applicationRevenueDto.setComplexApplicationCalculateDto(complexApplicationCalculateDto);
				}

				// Very Complex Applications calculation
				veryComplexApplicationCalculateDto = calculateVeryComplexAppsYearlyRevenue(applicationInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(veryComplexApplicationCalculateDto)){
					applicationRevenueDto.setVeryComplexApplicationCalculateDto(veryComplexApplicationCalculateDto);
				}
			}
		}
		return applicationRevenueDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private TotalApplicationCalculateDto calculateTotalAppsYearlyRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgTotalAppsVolume = appCommonHelper
				.getTotalApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		TotalApplicationCalculateDto totalAppsCalculateDto = null;
		TotalApplicationCalculator totalAppsCalculator = new TotalApplicationCalculator();
		if (avgTotalAppsVolume != null && avgTotalAppsVolume.compareTo(new BigDecimal(0)) != 0) {
			totalAppsCalculateDto = calculateTotalAppsPastDealRevenue(applicationInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgTotalAppsVolume, totalAppsCalculateDto, totalAppsCalculator);

			totalAppsCalculateDto = calculateTotalAppsBenchMarkDealRevenue(applicationInfo, dealInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgTotalAppsVolume, totalAppsCalculateDto, totalAppsCalculator);
		}
		return totalAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgTotalAppsVolume
	 * @param totalAppsCalculateDto
	 * @param totalAppsCalculator
	 * @return
	 */
	@Transactional
	private TotalApplicationCalculateDto calculateTotalAppsBenchMarkDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgTotalAppsVolume,
			TotalApplicationCalculateDto totalAppsCalculateDto, TotalApplicationCalculator totalAppsCalculator) {
		// Benchmark Calculation
		Specification<ApplicationInfo> totalAppsSpecificationBenchMark = ApplicationSpecification
				.specificationForBenchMarkTotalApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgTotalAppsVolume);
		List<ApplicationInfo> benchMarkDealResults = appRepository
				.findAll(totalAppsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Total Apps benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			totalAppsCalculateDto = totalAppsCalculator.prepareTotalAppsCalculateDtoForBenchmark(benchMarkDealResults,
					applicationInfo,
					totalAppsCalculateDto == null ? new TotalApplicationCalculateDto() : totalAppsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return totalAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgTotalAppsVolume
	 * @param totalAppsCalculateDto
	 * @param totalAppsCalculator
	 * @return
	 */
	@Transactional
	private TotalApplicationCalculateDto calculateTotalAppsPastDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgTotalAppsVolume,
			TotalApplicationCalculateDto totalAppsCalculateDto, TotalApplicationCalculator totalAppsCalculator) {
		// Past Deal Calculation
		Specification<ApplicationInfo> totalAppsSpecification = ApplicationSpecification.specificationForTotalApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgTotalAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(totalAppsSpecification);
		log.info("Total Application dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			totalAppsCalculateDto = totalAppsCalculator.prepareTotalApplicationCalculateDtoForPastDeal(pastDealResults,
					applicationInfo, new TotalApplicationCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return totalAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	private SimpleApplicationCalculateDto calculateSimpleAppsYearlyRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgSimpleAppsVolume = appCommonHelper
				.getSimpleApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		SimpleApplicationCalculator simpleAppsCalculator = new SimpleApplicationCalculator();
		SimpleApplicationCalculateDto simpleAppsCalculateDto = null;
		if (avgSimpleAppsVolume != null && avgSimpleAppsVolume.compareTo(new BigDecimal(0)) != 0) {
			simpleAppsCalculateDto = calculateSimpleAppsPastDealRevenue(applicationInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgSimpleAppsVolume, simpleAppsCalculator,
							simpleAppsCalculateDto);

			simpleAppsCalculateDto = calculateSimpleAppsBenchmarkDealRevenue(applicationInfo, dealInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgSimpleAppsVolume, simpleAppsCalculator,
					simpleAppsCalculateDto);
		}
		return simpleAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgSimpleAppsVolume
	 * @param simpleAppsCalculator
	 * @param simpleAppsCalculateDto
	 * @return
	 */
	@Transactional
	private SimpleApplicationCalculateDto calculateSimpleAppsBenchmarkDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgSimpleAppsVolume,
			SimpleApplicationCalculator simpleAppsCalculator, SimpleApplicationCalculateDto simpleAppsCalculateDto) {
		// Benchmark Calculation
		Specification<ApplicationInfo> simpleAppsSpecificationBenchMark = ApplicationSpecification
				.specificationForBenchMarkSimpleApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgSimpleAppsVolume);
		List<ApplicationInfo> benchMarkDealResults = appRepository
				.findAll(simpleAppsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Simple Apps benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			simpleAppsCalculateDto = simpleAppsCalculator.prepareSimpleAppsCalculateDtoForBenchmark(
					benchMarkDealResults, applicationInfo,
					simpleAppsCalculateDto == null ? new SimpleApplicationCalculateDto() : simpleAppsCalculateDto);

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return simpleAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgSimpleAppsVolume
	 * @param simpleAppsCalculator
	 * @param simpleAppsCalculateDto
	 * @return
	 */
	@Transactional
	private SimpleApplicationCalculateDto calculateSimpleAppsPastDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgSimpleAppsVolume,
			SimpleApplicationCalculator simpleAppsCalculator, SimpleApplicationCalculateDto simpleAppsCalculateDto) {
		// Past Deal Calculation for Simple Applications
		Specification<ApplicationInfo> simpleAppsSpecification = ApplicationSpecification.specificationForSimpleApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgSimpleAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(simpleAppsSpecification);
		log.info("Simple Application past DealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			simpleAppsCalculateDto = simpleAppsCalculator.prepareSimpleApplicationCalculateDtoForPastDeal(
					pastDealResults, applicationInfo, new SimpleApplicationCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return simpleAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private MediumApplicationCalculateDto calculateMediumAppsYearlyRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgMediumAppsVolume = appCommonHelper
				.getMediumApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		MediumApplicationCalculateDto mediumAppsCalculateDto = null;
		MediumApplicationCalculator mediumAppsCalculator = new MediumApplicationCalculator();
		if (avgMediumAppsVolume != null && avgMediumAppsVolume.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			mediumAppsCalculateDto = calculateMediumAppsPastDealRevenue(applicationInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgMediumAppsVolume, mediumAppsCalculateDto,
							mediumAppsCalculator);

			// Benchmark Calculation
			mediumAppsCalculateDto = calculateMediumAppsBenchMarkDealRevenue(applicationInfo, dealInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgMediumAppsVolume, mediumAppsCalculateDto,
					mediumAppsCalculator);
		}
		return mediumAppsCalculateDto;
	}

	@Transactional
	private MediumApplicationCalculateDto calculateMediumAppsBenchMarkDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgMediumAppsVolume,
			MediumApplicationCalculateDto mediumAppsCalculateDto, MediumApplicationCalculator mediumAppsCalculator) {
		Specification<ApplicationInfo> mediumAppsSpecificationBenchMark = ApplicationSpecification
				.specificationForBenchMarkMediumApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgMediumAppsVolume);
		List<ApplicationInfo> benchMarkDealResults = appRepository
				.findAll(mediumAppsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Medium Apps benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			mediumAppsCalculateDto = mediumAppsCalculator.prepareMediumAppsCalculateDtoForBenchmark(
					benchMarkDealResults, applicationInfo,
					mediumAppsCalculateDto == null ? new MediumApplicationCalculateDto() : mediumAppsCalculateDto);

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return mediumAppsCalculateDto;
	}

	@Transactional
	private MediumApplicationCalculateDto calculateMediumAppsPastDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgMediumAppsVolume,
			MediumApplicationCalculateDto mediumAppsCalculateDto, MediumApplicationCalculator mediumAppsCalculator) {
		Specification<ApplicationInfo> mediumAppsSpecification = ApplicationSpecification.specificationForMediumApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgMediumAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(mediumAppsSpecification);
		log.info("Medium Application dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			mediumAppsCalculateDto = mediumAppsCalculator.prepareMediumApplicationCalculateDtoForPastDeal(
					pastDealResults, applicationInfo, new MediumApplicationCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return mediumAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private ComplexApplicationCalculateDto calculateComplexAppsYearlyRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgComplexAppsVolume = appCommonHelper
				.getComplexApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		ComplexApplicationCalculateDto complexAppsCalculateDto = null;
		ComplexApplicationCalculator complexAppsCalculator = new ComplexApplicationCalculator();
		if (avgComplexAppsVolume != null && avgComplexAppsVolume.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			complexAppsCalculateDto = calculateComplexAppsPastDealRevenue(applicationInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgComplexAppsVolume, complexAppsCalculateDto,
							complexAppsCalculator);

			// Benchmark Calculation
			complexAppsCalculateDto = calculateComplexAppsBenchMarkDealRevenue(applicationInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgComplexAppsVolume,
					complexAppsCalculateDto, complexAppsCalculator);
		}
		return complexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgComplexAppsVolume
	 * @param complexAppsCalculateDto
	 * @param complexAppsCalculator
	 * @return
	 */
	@Transactional
	private ComplexApplicationCalculateDto calculateComplexAppsBenchMarkDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgComplexAppsVolume,
			ComplexApplicationCalculateDto complexAppsCalculateDto,
			ComplexApplicationCalculator complexAppsCalculator) {
		Specification<ApplicationInfo> complexAppsSpecificationBenchMark = ApplicationSpecification
				.specificationForBenchMarkComplexApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgComplexAppsVolume);
		List<ApplicationInfo> benchMarkDealResults = appRepository
				.findAll(complexAppsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Complex Apps benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			complexAppsCalculateDto = complexAppsCalculator.prepareComplexAppsCalculateDtoForBenchmark(
					benchMarkDealResults, applicationInfo,
					complexAppsCalculateDto == null ? new ComplexApplicationCalculateDto() : complexAppsCalculateDto);

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return complexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgComplexAppsVolume
	 * @param complexAppsCalculateDto
	 * @param complexAppsCalculator
	 * @return
	 */
	@Transactional
	private ComplexApplicationCalculateDto calculateComplexAppsPastDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgComplexAppsVolume,
			ComplexApplicationCalculateDto complexAppsCalculateDto,
			ComplexApplicationCalculator complexAppsCalculator) {
		Specification<ApplicationInfo> complexAppsSpecification = ApplicationSpecification.specificationForComplexApps(
				applicationInfo.isOffshoreAllowed(), applicationInfo.getLevelOfService(), avgComplexAppsVolume,
				towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(complexAppsSpecification);
		log.info("Complex Application dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			complexAppsCalculateDto = complexAppsCalculator.prepareComplexApplicationCalculateDtoForPastDeal(
					pastDealResults, applicationInfo, new ComplexApplicationCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return complexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private VeryComplexApplicationCalculateDto calculateVeryComplexAppsYearlyRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgVeryComplexAppsVolume = appCommonHelper
				.getVeryComplexApplicationAverageVolume(applicationInfo.getAppYearlyDataInfos());
		VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto = null;
		VeryComplexApplicationCalculator veryComplexAppsCalculator = new VeryComplexApplicationCalculator();
		if (avgVeryComplexAppsVolume != null && avgVeryComplexAppsVolume.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			veryComplexAppsCalculateDto = calculateVeryComplexAppsPastDealRevenue(applicationInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor, avgVeryComplexAppsVolume, veryComplexAppsCalculateDto,
							veryComplexAppsCalculator);

			// Benchmark Calculation
			veryComplexAppsCalculateDto = calculateVeryComplexAppsBenchMarkDealRevenue(applicationInfo, dealInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgVeryComplexAppsVolume,
					veryComplexAppsCalculateDto, veryComplexAppsCalculator);
		}
		return veryComplexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgVeryComplexAppsVolume
	 * @param veryComplexAppsCalculateDto
	 * @param veryComplexAppsCalculator
	 * @return
	 */
	@Transactional
	private VeryComplexApplicationCalculateDto calculateVeryComplexAppsBenchMarkDealRevenue(
			ApplicationInfo applicationInfo, DealInfo dealInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor,
			BigDecimal avgVeryComplexAppsVolume, VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto,
			VeryComplexApplicationCalculator veryComplexAppsCalculator) {
		Specification<ApplicationInfo> veryComplexAppsSpecificationBenchMark = ApplicationSpecification
				.specificationForBenchMarkVeryComplexApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgVeryComplexAppsVolume);
		List<ApplicationInfo> benchMarkDealResults = appRepository
				.findAll(veryComplexAppsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("Very Complex Apps benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			veryComplexAppsCalculateDto = veryComplexAppsCalculator.prepareVeryComplexAppsCalculateDtoForBenchmark(
					benchMarkDealResults, applicationInfo, veryComplexAppsCalculateDto == null
							? new VeryComplexApplicationCalculateDto() : veryComplexAppsCalculateDto);

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return veryComplexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgVeryComplexAppsVolume
	 * @param veryComplexAppsCalculateDto
	 * @param veryComplexAppsCalculator
	 * @return
	 */
	@Transactional
	private VeryComplexApplicationCalculateDto calculateVeryComplexAppsPastDealRevenue(ApplicationInfo applicationInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor,
			BigDecimal avgVeryComplexAppsVolume, VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto,
			VeryComplexApplicationCalculator veryComplexAppsCalculator) {
		Specification<ApplicationInfo> veryComplexAppsSpecification = ApplicationSpecification
				.specificationForVeryComplexApps(applicationInfo.isOffshoreAllowed(),
						applicationInfo.getLevelOfService(), avgVeryComplexAppsVolume,
						towerSpecificBandInfo.getBandPercentage());
		List<ApplicationInfo> pastDealResults = appRepository.findAll(veryComplexAppsSpecification);
		log.info("Very Complex Application dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			appCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			appCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			veryComplexAppsCalculateDto = veryComplexAppsCalculator
					.prepareVeryComplexApplicationCalculateDtoForPastDeal(pastDealResults, applicationInfo,
							new VeryComplexApplicationCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return veryComplexAppsCalculateDto;
	}

}
