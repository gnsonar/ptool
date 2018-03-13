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
import com.in.fujitsu.pricing.retail.calculator.NoOfShopsCalculator;
import com.in.fujitsu.pricing.retail.dto.RetailCalculateDto;
import com.in.fujitsu.pricing.retail.dto.RetailDropdownDto;
import com.in.fujitsu.pricing.retail.dto.RetailInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailPriceDto;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueDto;
import com.in.fujitsu.pricing.retail.entity.RetailEquipmentAgeInfo;
import com.in.fujitsu.pricing.retail.entity.RetailEquipmentSetInfo;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailSolutionsInfo;
import com.in.fujitsu.pricing.retail.helper.NoOfShopsDealResultsHelper;
import com.in.fujitsu.pricing.retail.helper.RetailBeanConvertor;
import com.in.fujitsu.pricing.retail.helper.RetailCommonHelper;
import com.in.fujitsu.pricing.retail.repository.RetailEquipmentAgeRepository;
import com.in.fujitsu.pricing.retail.repository.RetailEquipmentSetRepository;
import com.in.fujitsu.pricing.retail.repository.RetailRepository;
import com.in.fujitsu.pricing.retail.repository.RetailSolutionsRepository;
import com.in.fujitsu.pricing.specification.RetailSpecification;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mishrasub
 *
 */
@Slf4j
@Service
public class RetailService {

	@Autowired
	private RetailRepository retailRepository;

	@Autowired
	private RetailSolutionsRepository retailSolutionsRepository;

	@Autowired
	private GenericService genericService;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private RetailBeanConvertor beanConvertor;

	@Autowired
	private RetailCommonHelper commonHelper;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private DealRepository dealRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NoOfShopsDealResultsHelper noOfShopsDealResultsHelper;

	@Autowired
	private RetailEquipmentAgeRepository equipmentAgeRepository;

	@Autowired
	private RetailEquipmentSetRepository equipmentSetRepository;

	private final String RETAIL_TOWER = "Retail";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";

	/**
	 * @param dealId
	 * @return
	 */
	public RetailDropdownDto getDropDownDetails(Long dealId) {
		RetailDropdownDto retailDropdownDto = new RetailDropdownDto();
		retailDropdownDto.setYesNoOptionList(genericService.getOffshreAndHardwareInfo());

		List<RetailEquipmentAgeInfo> equipAgeList = equipmentAgeRepository.findAll();
		retailDropdownDto.setEquipmentAgeList(beanConvertor.prepareEquipmentAgeDtoList(equipAgeList));

		List<RetailEquipmentSetInfo> equipSetList = equipmentSetRepository.findAll();
		retailDropdownDto.setEquipmentSetList(beanConvertor.prepareEquipmentSetDtoList(equipSetList));

		List<RetailSolutionsInfo> retailSolutionsInfoList = retailSolutionsRepository.findAll();
		retailDropdownDto.setRetailSolutionsDtoList(beanConvertor.prepareRetailSolutionsDtoList(retailSolutionsInfoList));

		if (dealId != null) {
			retailDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return retailDropdownDto;
	}

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public RetailInfoDto getDetails(Long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		RetailInfo retailInfo = retailRepository.findByDealInfo(dealInfo);
		if (retailInfo == null) {
			throw new ServiceException("DB doesn't have the Reatil Details for given dealId.");
		}
		return beanConvertor.prepareRetailInfoDto(retailInfo);
	}

	/**
	 * @param dealId
	 * @param retailInfoDto
	 * @return
	 */
	@Transactional
	public RetailInfoDto saveDetails(Long dealId, RetailInfoDto retailInfoDto) {
		RetailInfo retailInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		retailInfo = retailRepository.findByDealInfo(dealInfo);
		if (null != retailInfo) {
			retailInfo = beanConvertor.prepareRetailInfo(retailInfo, retailInfoDto, false);
		} else {
			retailInfo = beanConvertor.prepareRetailInfo(new RetailInfo(), retailInfoDto, true);
		}
		retailInfo = retailRepository.saveAndFlush(retailInfo);

		return beanConvertor.prepareRetailInfoDto(retailInfo);
	}

	/**
	 * @param retailPriceDtoList
	 * @param retailId
	 * @return
	 * @throws ServiceException
	 */
	public ResponseEntity<Object> updatePrice(List<RetailPriceDto> retailPriceDtoList, Long retailId)
			throws ServiceException {
		RetailInfo retailInfo = retailRepository.findOne(retailId);
		if (null != retailInfo) {
			retailInfo = beanConvertor.prepareRetailPrice(retailInfo, retailPriceDtoList);
			retailRepository.saveAndFlush(retailInfo);
		} else {
			throw new ServiceException("No RetailInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);
	}

	/**
	 * @param solutionCriteriaDto
	 * @param retailId
	 * @return
	 */
	public RetailRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long retailId)
			throws ServiceException {
		RetailInfo retailInfo = retailRepository.findOne(retailId);
		if (null != retailInfo) {
			retailInfo = beanConvertor.prepareSolutionCriteria(retailInfo, solutionCriteriaDto);
			retailRepository.saveAndFlush(retailInfo);
		} else {
			throw new ServiceException("No RetailInfo data to update");
		}

		return getYearlyRevenues(retailInfo.getDealInfo().getDealId());
	}

	/**
	 * @param dealId
	 * @return
	 */
	public RetailRevenueDto getYearlyRevenues(Long dealId) {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		RetailRevenueDto retailRevenueDto = new RetailRevenueDto();
		if (dealInfo != null) {
			RetailCalculateDto noOfShopsCalculateDto = null;

			RetailInfo retailInfo = retailRepository.findByDealInfo(dealInfo);
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

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(RETAIL_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			// No Of Shops calculation
			noOfShopsCalculateDto = calculateNoOfShopsYearlyRevenue(retailInfo, dealInfo, towerSpecificBandInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(noOfShopsCalculateDto)){
				retailRevenueDto.setNoOfShopsCalculateDto(noOfShopsCalculateDto);
			}

		}

		return retailRevenueDto;
	}

	/**
	 * @param retailInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	private RetailCalculateDto calculateNoOfShopsYearlyRevenue(RetailInfo retailInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgNoOfShops = commonHelper.getNoOfShopsAverageVolume(retailInfo.getRetailYearlyDataInfoList());
		RetailCalculateDto noOfShopsCalculateDto = null;
		NoOfShopsCalculator noOfShopsCalculator = new NoOfShopsCalculator();

		if (avgNoOfShops != null && avgNoOfShops.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			noOfShopsCalculateDto = calculateNoOfShopsPastDealRevenue(retailInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgNoOfShops, noOfShopsCalculateDto,
					noOfShopsCalculator);

			// Benchmark Calculation
			noOfShopsCalculateDto = calculateNoOfShopsBenchMarkDealRevenue(retailInfo, dealInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgNoOfShops, noOfShopsCalculateDto, noOfShopsCalculator);
		}
		return noOfShopsCalculateDto;
	}

	/**
	 * @param retailInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgNoOfShops
	 * @param noOfShopsCalculateDto
	 * @param noOfShopsCalculator
	 * @return
	 */
	@Transactional
	private RetailCalculateDto calculateNoOfShopsBenchMarkDealRevenue(RetailInfo retailInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor,
			BigDecimal avgNoOfShops, RetailCalculateDto noOfShopsCalculateDto,
			NoOfShopsCalculator noOfShopsCalculator) {

		Specification<RetailInfo> noOfShopsSpecificationBenchMark = RetailSpecification
				.specificationForBenchMarkNoOfShops(retailInfo.isOffshoreAllowed(), retailInfo.isIncludeHardware(),
						retailInfo.getEquipmentAge(), retailInfo.getEquipmentSet(), avgNoOfShops);
		List<RetailInfo> benchMarkDealResults = retailRepository
				.findAll(noOfShopsSpecificationBenchMark, new PageRequest(0, 1)).getContent();
		log.info("No Of shops benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			noOfShopsCalculateDto = noOfShopsCalculator.prepareNoOfShopsCalculateDtoForBenchmark(benchMarkDealResults,
					retailInfo, noOfShopsCalculateDto == null ? new RetailCalculateDto() : noOfShopsCalculateDto);

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return noOfShopsCalculateDto;
	}

	/**
	 * @param retailInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgNoOfShops
	 * @param noOfShopsCalculateDto
	 * @param noOfShopsCalculator
	 * @return
	 */
	@Transactional
	private RetailCalculateDto calculateNoOfShopsPastDealRevenue(RetailInfo retailInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgNoOfShops,
			RetailCalculateDto noOfShopsCalculateDto, NoOfShopsCalculator noOfShopsCalculator) {

		Specification<RetailInfo> noOfShopsSpecification = RetailSpecification.specificationForNoOfShops(
				retailInfo.isOffshoreAllowed(), retailInfo.isIncludeHardware(), retailInfo.getEquipmentAge(),
				retailInfo.getEquipmentSet(), avgNoOfShops, towerSpecificBandInfo.getBandPercentage());
		List<RetailInfo> pastDealResults = retailRepository.findAll(noOfShopsSpecification);
		log.info("No Of Shops dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor);
			noOfShopsCalculateDto = noOfShopsCalculator.prepareNoOfShopsCalculateDtoForPastDeal(pastDealResults,
					retailInfo, new RetailCalculateDto());
		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}
		return noOfShopsCalculateDto;
	}

	/**
	 * @param dealId
	 * @param levelName
	 * @param dealType
	 * @return
	 */
	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		DealResultsResponse dealResultsDto = new DealResultsResponse();
		if (dealInfo != null) {
			RetailInfo retailInfo = retailRepository.findByDealInfo(dealInfo);
			if (retailInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(RETAIL_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					dealResultsDto = noOfShopsDealResultsHelper.getNearestPastDealsForNoOfShops(retailInfo,
							towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
							referenceCountry, referenceCountryFactor);

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					dealResultsDto = noOfShopsDealResultsHelper.getNearestBenchmarkDealsForNoOfShops(
							retailInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
							referenceCountry, referenceCountryFactor);

				}

			} else {
				throw new ServiceException("Can't find the Retail Detail for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;
	}

}
