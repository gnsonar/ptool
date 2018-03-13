/**
 *
 */
package com.in.fujitsu.pricing.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.ClientIndustryDto;
import com.in.fujitsu.pricing.dto.CountryCurrencyInfoDto;
import com.in.fujitsu.pricing.dto.DealCostParametersDto;
import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.DealPhaseDto;
import com.in.fujitsu.pricing.dto.DealTypeDto;
import com.in.fujitsu.pricing.dto.GenericDealInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.entity.ClientIndustry;
import com.in.fujitsu.pricing.entity.CountryCurrencyInfo;
import com.in.fujitsu.pricing.entity.DealCostParameters;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.DealPhase;
import com.in.fujitsu.pricing.entity.DealType;
import com.in.fujitsu.pricing.entity.FXRatesInfo;
import com.in.fujitsu.pricing.entity.ServiceWindow;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.OffshoreAndHardwareinfoEnum;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.repository.ClientIndustryRepository;
import com.in.fujitsu.pricing.repository.CountryCurrencyRepository;
import com.in.fujitsu.pricing.repository.DealCostParametersRepository;
import com.in.fujitsu.pricing.repository.DealPhaseRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.DealTypeRepository;
import com.in.fujitsu.pricing.repository.FxRateRepository;
import com.in.fujitsu.pricing.repository.ServiceWindowRepository;
import com.in.fujitsu.pricing.repository.UserRepository;
import com.in.fujitsu.pricing.security.model.AuthenticatedUser;
import com.in.fujitsu.pricing.utility.GenericModelConvertor;
import com.in.fujitsu.pricing.utility.HomeModelConvertor;

/**
 * @author MishraSub
 *
 */
@Service
public class GenericService {

	@Autowired
	private ClientIndustryRepository clientIndustryRepository;

	@Autowired
	private CountryCurrencyRepository countryCurrencyRepository;

	@Autowired
	private DealPhaseRepository dealPhaseRepository;

	@Autowired
	private GenericModelConvertor genericModelConvertor;

	@Autowired
	private DealTypeRepository dealTypeRepository;

	@Autowired
	private ServiceWindowRepository serviceWindowRepository;

	@Autowired
	private DealCostParametersRepository dealCostParametersRepository;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private FxRateRepository fxRateRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * @param dealId
	 * @return
	 */
	public boolean isAuthorized(Long dealId) {
		AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		boolean isAuthorizedUser = false;
		if (user != null) {
			final UserInfo userInfo = userRepository.findByUserName(user.getUsername());
			if (userInfo != null) {
				final DealInfo dealInfo = dealRepository.findByDealId(dealId);
				if (dealInfo != null) {
					if (userInfo.getUserId() == dealInfo.getUserInfo().getUserId()
							|| hasRole("Admin", user.getAuthorities())) {
						isAuthorizedUser = true;
					}
				}
			}
		}
		return isAuthorizedUser;
	}

	/**
	 * @param role
	 * @param authorities
	 * @return
	 */
	private boolean hasRole(String role, Collection<? extends GrantedAuthority> authorities) {
		boolean hasRole = false;
		for (GrantedAuthority authority : authorities) {
			hasRole = authority.getAuthority().equalsIgnoreCase(role);
			if (hasRole) {
				break;
			}
		}
		return hasRole;
	}

	public GenericDealInfoDto getGenericDropDownDetails() {
		final GenericDealInfoDto genericDealInfoDto = new GenericDealInfoDto();
		genericDealInfoDto.setClientIndustryDtoList(getClientIndustry());
		genericDealInfoDto.setCountryCurrencyInfoDtoList(getCountryCurrencyInfo());
		genericDealInfoDto.setDealtypeList(getDealType());
		genericDealInfoDto.setDealPhaseList(getDealPhase());
		genericDealInfoDto.setStandardWindowInfoList(getServicedWindowInfo());
		genericDealInfoDto.setOffshoreAndHardwareIncludedList(getOffshreAndHardwareInfo());
		genericDealInfoDto.setDealCostParametersDtoList(getDealCostParametersDto());
		return genericDealInfoDto;
	}

	public List<ClientIndustryDto> getClientIndustry() {
		final List<ClientIndustry> clientIndustry = clientIndustryRepository.findAll();
		return HomeModelConvertor.clientIndustryDtoList(clientIndustry);
	}

	public List<CountryCurrencyInfoDto> getCountryCurrencyInfo() {
		final List<CountryCurrencyInfo> countryCurrencyInfo = countryCurrencyRepository.findAll();
		return HomeModelConvertor.countryCurrencyInfoDtoList(countryCurrencyInfo);
	}

	public List<DealTypeDto> getDealType() {
		List<DealType> dealTypeList = dealTypeRepository.findAll();
		return GenericModelConvertor.prepareDealTypeDtoList(dealTypeList);
	}

	public List<DealPhaseDto> getDealPhase() {
		List<DealPhase> dealPhaseList = dealPhaseRepository.findAll();
		return GenericModelConvertor.prepareDealPhaseDtoList(dealPhaseList);
	}

	public List<ServiceWindowDto> getServicedWindowInfo() {
		List<ServiceWindow> servicedWindowInfoList = serviceWindowRepository.findAll();
		return GenericModelConvertor.prepareServiceWindowDtoList(servicedWindowInfoList);
	}

	public List<String> getOffshreAndHardwareInfo() {
		List<String> offshoreAndhardwareinfoList = new ArrayList<String>();
		for (OffshoreAndHardwareinfoEnum offshreandHardwareinfoEnum : OffshoreAndHardwareinfoEnum.values()) {
			offshoreAndhardwareinfoList.add(offshreandHardwareinfoEnum.getName());
		}
		return offshoreAndhardwareinfoList;

	}

	public List<DealCostParametersDto> getDealCostParametersDto() {
		List<DealCostParameters> dealCostParametersList = dealCostParametersRepository.findAll();
		return GenericModelConvertor.prepareDealCostParametersDtoList(dealCostParametersList);
	}

	/**
	 * @param dealId
	 * @return
	 */
	public DealInfoDto getGenericDetailsByDealId(Long dealId) {
		final DealInfo fetchedDealInfo = dealRepository.findByDealId(dealId);
		DealInfoDto dealInfoDto = null;
		if(fetchedDealInfo != null){
			dealInfoDto = genericModelConvertor.prepareDealInfoDto(fetchedDealInfo);
		}
		return dealInfoDto;
	}

	/**
	 * @param dealInfoDto
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public DealInfoDto saveGenericDealDetails(DealInfoDto dealInfoDto) throws Exception {
		DealInfo dealInfo = null;
		if (dealInfoDto.getDealId() != null) {
			dealInfo = dealRepository.findOne(dealInfoDto.getDealId());
			if (null != dealInfo) {
				dealInfo = genericModelConvertor.prepareDealInfo(dealInfo, dealInfoDto, false);
			}
		} else {
			dealInfo = genericModelConvertor.prepareDealInfo(new DealInfo(), dealInfoDto, true);
		}
		dealInfo = dealRepository.saveAndFlush(dealInfo);

		return genericModelConvertor.prepareDealInfoDto(dealInfo);
	}

	/**
	 * @param dealId
	 * @param dealStatus
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<Object> updateDealStatus(Long dealId, String dealStatus) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		if (dealInfo != null) {
			dealInfo.setDealStatus(DealStatusEnum.valueOf(dealStatus.toUpperCase()).getName());
			if (DealStatusEnum.SUBMITTED.getName().equalsIgnoreCase(dealStatus)
					|| CollectionUtils.isEmpty(dealInfo.getDealFxRates())) {
				Calendar calendar = new GregorianCalendar();
				Date submissionDate = new Date(calendar.getTimeInMillis());
				dealInfo.setSubmissionDate(submissionDate);
				List<FXRatesInfo> fxRatesInfoList = fxRateRepository.findByCurrencyFrom(dealInfo.getCurrency());
				if (CollectionUtils.isEmpty(dealInfo.getDealFxRates())) {
					Set<DealFXRatesInfo> dealFXRatesInfoList = new HashSet<>();
					for (FXRatesInfo fXRatesInfo : fxRatesInfoList) {
						DealFXRatesInfo dealFXRatesInfo = new DealFXRatesInfo();
						dealFXRatesInfo.setCurrencyFrom(fXRatesInfo.getCurrencyFrom());
						dealFXRatesInfo.setCurrencyTo(fXRatesInfo.getCurrencyTo());
						dealFXRatesInfo.setRate(fXRatesInfo.getRate());
						dealFXRatesInfo.setDealInfo(dealInfo);
						dealFXRatesInfo.setCreatedDate(submissionDate);
						dealFXRatesInfoList.add(dealFXRatesInfo);
					}
					dealInfo.setDealFxRates(dealFXRatesInfoList);
				} else {
					for (DealFXRatesInfo dealFXRatesInfo : dealInfo.getDealFxRates()) {
						for (FXRatesInfo fXRatesInfo : fxRatesInfoList) {
							if (dealFXRatesInfo.getCurrencyTo().equalsIgnoreCase(fXRatesInfo.getCurrencyTo())) {
								dealFXRatesInfo.setRate(fXRatesInfo.getRate());
							}
						}
					}

				}
			}
			dealRepository.save(dealInfo);
		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return new ResponseEntity<Object>(new SuccessResponse("Deal Status Updated Successfully."), HttpStatus.OK);
	}

	public ResponseEntity<Object> updateTowerIndicators(Long dealId, String assessmentIndicator,
			String submissionIndicator) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		if (null != dealInfo) {
			dealInfo.setAssessmentIndicator(assessmentIndicator);
			dealInfo.setSubmissionIndicator(submissionIndicator);
			dealRepository.saveAndFlush(dealInfo);
		} else {
			throw new ServiceException("No dealInfo data found to update");
		}
		return new ResponseEntity<Object>(new SuccessResponse("Tower Indicators Updated Successfully"), HttpStatus.OK);
	}


}
