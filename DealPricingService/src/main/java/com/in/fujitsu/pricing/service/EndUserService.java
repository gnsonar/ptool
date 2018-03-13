package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.enduser.calculator.DesktopsCalculator;
import com.in.fujitsu.pricing.enduser.calculator.EndUserDevicesCalculator;
import com.in.fujitsu.pricing.enduser.calculator.HighEndLaptopsCalculator;
import com.in.fujitsu.pricing.enduser.calculator.ImacsCalculator;
import com.in.fujitsu.pricing.enduser.calculator.LaptopsCalculator;
import com.in.fujitsu.pricing.enduser.calculator.MobilesCalculator;
import com.in.fujitsu.pricing.enduser.calculator.StandardLaptopsCalculator;
import com.in.fujitsu.pricing.enduser.calculator.ThinClientsCalculator;
import com.in.fujitsu.pricing.enduser.dto.EndUserCalculateDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserDropdownDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserPriceDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueDto;
import com.in.fujitsu.pricing.enduser.entity.EndUserContactRatioInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserImacFactorInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserResolutionTimeInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserSolutionInfo;
import com.in.fujitsu.pricing.enduser.helper.DesktopDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.EndUserBeanConvertor;
import com.in.fujitsu.pricing.enduser.helper.EndUserDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.HighEndLaptopsDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.ImacDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.LaptopsDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.MobileDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.StandardLaptopsDealResultsHelper;
import com.in.fujitsu.pricing.enduser.helper.ThinClientsDealResultsHelper;
import com.in.fujitsu.pricing.enduser.repository.EndUserContactRatioRepository;
import com.in.fujitsu.pricing.enduser.repository.EndUserImacFactorRepository;
import com.in.fujitsu.pricing.enduser.repository.EndUserRepository;
import com.in.fujitsu.pricing.enduser.repository.EndUserResolutionTimeRepository;
import com.in.fujitsu.pricing.enduser.repository.EndUserSolutionsRepository;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.repository.CountryFactorRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.TowerSpecificBandRepository;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EndUserService {

	@Autowired
	private GenericService genericService;

	@Autowired
	EndUserSolutionsRepository endUserSolutionsRepository;

	@Autowired
	EndUserImacFactorRepository imacFactorRepository;

	@Autowired
	EndUserContactRatioRepository contactRatioRepository;

	@Autowired
	EndUserResolutionTimeRepository resolutionTimeRepository;

	@Autowired
	EndUserBeanConvertor beanConvertor;

	@Autowired
	EndUserRepository endUserRepository;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private EndUserDevicesCalculator endUserCalculator;

	@Autowired
	private LaptopsCalculator laptopsCalculator;

	@Autowired
	private HighEndLaptopsCalculator highEndLaptopsCalculator;

	@Autowired
	private StandardLaptopsCalculator standardLaptopsCalculator;

	@Autowired
	private DesktopsCalculator desktopsCalculator;

	@Autowired
	private ThinClientsCalculator thinClientsCalculator;

	@Autowired
	private MobilesCalculator mobilesCalculator;

	@Autowired
	private ImacsCalculator imacsCalculator;

	@Autowired
	private EndUserDealResultsHelper endUserDealResultsHelper;

	@Autowired
	private LaptopsDealResultsHelper laptopsDealResultsHelper;

	@Autowired
	private HighEndLaptopsDealResultsHelper highEndLaptopsDealResultsHelper;

	@Autowired
	private StandardLaptopsDealResultsHelper standardLaptopsDealResultsHelper;

	@Autowired
	private DesktopDealResultsHelper desktopDealResultsHelper;

	@Autowired
	private ThinClientsDealResultsHelper thinClientsDealResultsHelper;

	@Autowired
	private MobileDealResultsHelper mobileDealResultsHelper;

	@Autowired
	private ImacDealResultsHelper imacDealResultsHelper;

	private final String END_USER_TOWER = "EndUser";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";
	private final String END_USER_LEVEL = "1.1";
	private final String LAPTOP_LEVEL = "1.1.1";
	private final String HIGH_END_LAPTOP_LEVEL = "1.1.1.1";
	private final String STANDARD_LAPTOP_LEVEL = "1.1.1.2";
	private final String DESKTOP_LEVEL = "1.1.2";
	private final String THIN_CLIENT_LEVEL = "1.1.3";
	private final String MOBILE_LEVEL = "1.1.4";
	private final String IMAC_LEVEL = "2.1";

	/**
	 * @param dealId
	 * @return
	 */
	public EndUserDropdownDto getDropDownDetails(Long dealId) {
		EndUserDropdownDto endUserDropdownDto = new EndUserDropdownDto();

		endUserDropdownDto.setYesNoOptionList(genericService.getOffshreAndHardwareInfo());

		final List<EndUserSolutionInfo> endUserSolutions = endUserSolutionsRepository.findAll();
		endUserDropdownDto.setEndUserSolutionsDtoList(beanConvertor.prepareEndUserSolutionsDtoList(endUserSolutions));

		final List<EndUserImacFactorInfo> imacFactorInfoList = imacFactorRepository.findAll();
		endUserDropdownDto.setImacFactorDtoList(beanConvertor.prepareImacFactorDtoList(imacFactorInfoList));

		final List<EndUserContactRatioInfo> contactRatioList = contactRatioRepository.findAll();
		endUserDropdownDto.setContactRatioDtoList(beanConvertor.prepareContactRatioDtoList(contactRatioList));

		final List<EndUserResolutionTimeInfo> resTimeList = resolutionTimeRepository.findAll();
		endUserDropdownDto.setResolutionTimeDtoList(beanConvertor.prepareResolutionTimeDToList(resTimeList));

		if (dealId != null) {
			endUserDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return endUserDropdownDto;
	}

	/**
	 * @param dealId
	 * @param endUserInfoDto
	 * @return endUserInfoDto
	 */
	@Transactional
	public EndUserInfoDto saveDetails(Long dealId, EndUserInfoDto endUserInfoDto) {
		EndUserInfo endUserInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		endUserInfo = endUserRepository.findByDealInfo(dealInfo);
		if (null != endUserInfo) {
			endUserInfo = beanConvertor.prepareEndUserInfo(endUserInfo, endUserInfoDto, false);
		} else {
			endUserInfo = beanConvertor.prepareEndUserInfo(new EndUserInfo(), endUserInfoDto, true);
		}
		endUserInfo = endUserRepository.saveAndFlush(endUserInfo);
		return beanConvertor.prepareEndUserInfoDto(endUserInfo);
	}

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public EndUserInfoDto getDetails(long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
		if (endUserInfo == null) {
			throw new ServiceException("DB doesn't have the End User Details for given dealId.");
		}
		return beanConvertor.prepareEndUserInfoDto(endUserInfo);
	}

	/**
	 * @param endUserPriceDtoList
	 * @param endUserId
	 * @return
	 */
	public ResponseEntity<Object> updateEndUserPrice(List<EndUserPriceDto> endUserPriceDtoList, Long endUserId)
			throws ServiceException {
		EndUserInfo endUserInfo = endUserRepository.findOne(endUserId);
		if (null != endUserInfo) {
			endUserInfo = beanConvertor.prepareEndUserPrice(endUserInfo, endUserPriceDtoList);
			endUserRepository.saveAndFlush(endUserInfo);
		} else {
			throw new ServiceException("No EndUserInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);
	}

	/**
	 * @param dealId
	 * @return
	 * @throws ServiceException
	 */
	public EndUserRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		EndUserRevenueDto revenueDto = new EndUserRevenueDto();
		if (dealInfo != null) {
			EndUserCalculateDto endUserCalcDto = null;
			EndUserCalculateDto laptopCalcDto = null;
			EndUserCalculateDto highEndLaptopCalcDto = null;
			EndUserCalculateDto standardLaptopCalcDto = null;
			EndUserCalculateDto desktopCalcDto = null;
			EndUserCalculateDto thinClientCalcDto = null;
			EndUserCalculateDto mobileCalcDto = null;
			EndUserCalculateDto imacCalcDto = null;
			EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
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

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(END_USER_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			String[] levelInd = endUserInfo.getLevelIndicator().split(",");
			if (levelInd != null && (levelInd[0].equals("1")) && (levelInd[1].equals("0"))
					&& (levelInd[2].equals("0"))) {
				log.info("Got correct position for Only End user devices calculation.");
				// 100 -- End user only calculation
				endUserCalcDto = endUserCalculator.calculateEndUserDevicesYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(endUserCalcDto)){
					revenueDto.setEndUserDevicesCalculateDto(endUserCalcDto);
				}

			} else if (levelInd != null && (levelInd[0].equals("1")) && (levelInd[1].equals("1"))
					&& (levelInd[2].equals("0"))) {
				// 110 -- End user children calculation
				log.info("Got correct position for Laptop, Desktop, ThinClient, Mobile devices calculation.");
				laptopCalcDto = laptopsCalculator.calculateLaptopYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(laptopCalcDto)){
					revenueDto.setLaptopsCalculateDto(laptopCalcDto);
				}

				desktopCalcDto = desktopsCalculator.calculateDesktopYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(desktopCalcDto)){
					revenueDto.setDesktopsCalculateDto(desktopCalcDto);
				}

				thinClientCalcDto = thinClientsCalculator.calculateThinClientYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(thinClientCalcDto)){
					revenueDto.setThinClientsCalculateDto(thinClientCalcDto);
				}

				mobileCalcDto = mobilesCalculator.calculateMobileYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mobileCalcDto)){
					revenueDto.setMobileDevicesCalculateDto(mobileCalcDto);
				}

			} else if (levelInd != null && (levelInd[0].equals("1")) && (levelInd[1].equals("1"))
					&& (levelInd[2].equals("1"))) {
				// 111 -- End user children and Laptop children calculation
				log.info(
						"Got correct position for High end laptop, standard laptop, desktop, thinclient, mobile devices calculation.");
				highEndLaptopCalcDto = highEndLaptopsCalculator.calculateHighEndLaptopYearlyRevenue(endUserInfo,
						dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(highEndLaptopCalcDto)){
					revenueDto.setHighEndLaptopsCalculateDto(highEndLaptopCalcDto);
				}

				standardLaptopCalcDto = standardLaptopsCalculator.calculateStandardEndLaptopYearlyRevenue(endUserInfo,
						dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(standardLaptopCalcDto)){
					revenueDto.setStandardLaptopsCalculateDto(standardLaptopCalcDto);
				}

				desktopCalcDto = desktopsCalculator.calculateDesktopYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(desktopCalcDto)){
					revenueDto.setDesktopsCalculateDto(desktopCalcDto);
				}

				thinClientCalcDto = thinClientsCalculator.calculateThinClientYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(thinClientCalcDto)){
					revenueDto.setThinClientsCalculateDto(thinClientCalcDto);
				}

				mobileCalcDto = mobilesCalculator.calculateMobileYearlyRevenue(endUserInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mobileCalcDto)){
					revenueDto.setMobileDevicesCalculateDto(mobileCalcDto);
				}

			}

			if (levelInd != null && (levelInd[3].equals("1"))) {
				log.info("Got correct position for Imac devices calculation.");
				// ---1 -- Imac calculation
				imacCalcDto = imacsCalculator.calculateImacYearlyRevenue(endUserInfo, dealInfo, towerSpecificBandInfo,
						assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(imacCalcDto)){
					revenueDto.setImacDevicesCalculateDto(imacCalcDto);
				}
			}
		}

		return revenueDto;

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
			EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
			if (endUserInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(END_USER_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (END_USER_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = endUserDealResultsHelper.getNearestPastDealsForEndUser(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = laptopsDealResultsHelper.getNearestPastDealsForLaptops(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (HIGH_END_LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = highEndLaptopsDealResultsHelper
								.getNearestPastDealsForHighEndLaptops(endUserInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo(): towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (STANDARD_LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = standardLaptopsDealResultsHelper
								.getNearestPastDealsForStandardLaptops(endUserInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo(): towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (DESKTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = desktopDealResultsHelper.getNearestPastDealsForDesktop(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (THIN_CLIENT_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = thinClientsDealResultsHelper.getNearestPastDealsForThinClients(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (MOBILE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mobileDealResultsHelper.getNearestPastDealsForMobiles(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (IMAC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = imacDealResultsHelper.getNearestPastDealsForImac(endUserInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					}

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (END_USER_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = endUserDealResultsHelper.getNearestBenchmarkDealsForEndUser(endUserInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = laptopsDealResultsHelper.getNearestBenchmarkDealsForLaptops(endUserInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (HIGH_END_LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = highEndLaptopsDealResultsHelper.getNearestBenchmarkDealsForHighEndLaptops(
								endUserInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (STANDARD_LAPTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = standardLaptopsDealResultsHelper.getNearestBenchmarkDealsForStandardLaptops(
								endUserInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (DESKTOP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = desktopDealResultsHelper.getNearestBenchmarkDealsForDesktop(endUserInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (THIN_CLIENT_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = thinClientsDealResultsHelper.getNearestBenchmarkDealsForThinClients(
								endUserInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (MOBILE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mobileDealResultsHelper.getNearestBenchmarkDealsForMobiles(endUserInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (IMAC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = imacDealResultsHelper.getNearestBenchmarkDealsForImac(endUserInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					}

				}

			} else {
				throw new ServiceException("Can't find the EndUser Detail for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;

	}

	/**
	 * @param solutionDto
	 * @param endUserId
	 * @return
	 * @throws ServiceException
	 */
	public EndUserRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionDto, Long endUserId)
			throws ServiceException {
		EndUserInfo endUserInfo = endUserRepository.findOne(endUserId);
		if (null != endUserInfo) {
			endUserInfo = beanConvertor.prepareSolutionCriteria(endUserInfo, solutionDto);
			endUserRepository.saveAndFlush(endUserInfo);
		} else {
			throw new ServiceException("No EndUserInfo data to update");
		}

		return getYearlyRevenues(endUserInfo.getDealInfo().getDealId());
	}

}
