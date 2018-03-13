package com.in.fujitsu.pricing.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.in.fujitsu.pricing.dto.DealCompetitorInfoDto;
import com.in.fujitsu.pricing.dto.DealCostParametersDto;
import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.DealPhaseDto;
import com.in.fujitsu.pricing.dto.DealTypeDto;
import com.in.fujitsu.pricing.dto.DealYearlyDataInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;
import com.in.fujitsu.pricing.entity.DealCompetitorInfo;
import com.in.fujitsu.pricing.entity.DealCostParameters;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.DealPhase;
import com.in.fujitsu.pricing.entity.DealType;
import com.in.fujitsu.pricing.entity.DealYearlyDataInfo;
import com.in.fujitsu.pricing.entity.ServiceWindow;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.repository.DealCompetitorRepository;
import com.in.fujitsu.pricing.repository.DealYearlyRepository;

@Service
public class GenericModelConvertor {

	@Autowired
	private DealCompetitorRepository dealCompetitorRepository;

	@Autowired
	private DealYearlyRepository dealYearlyRepository;

	private final static String YES = "Yes";

	private final static String NO = "No";

	private static ModelMapper modelMapper = new ModelMapper();


	/**
	 * @param dealInfo
	 * @return
	 */
	public DealInfoDto prepareDealInfoDto(DealInfo dealInfo) {

		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		final DealInfoDto dealInfoDto = modelMapper.map(dealInfo, DealInfoDto.class);

		dealInfoDto.setBidManager(dealInfo.getBidManager());

		dealInfoDto.setIncludeHardware(checkYesOrNo(dealInfo.isIncludeHardware()));
		dealInfoDto.setOffshoreAllowed(checkYesOrNo(dealInfo.isOffshoreAllowed()));
		dealInfoDto.setThirdPartyFinance(checkYesOrNo(dealInfo.isThirdPartyFinance()));
		dealInfoDto.setOpenBook(checkYesOrNo(dealInfo.isOpenBook()));
		dealInfoDto.setCrossBorderTax(checkYesOrNo(dealInfo.isCrossBorderTax()));
		dealInfoDto.setNonStandardVariablePricing(checkYesOrNo(dealInfo.isNonStandardVariablePricing()));
		dealInfoDto.setMigrationCost(checkYesOrNo(dealInfo.isMigrationCostApplicable()));
		dealInfoDto.setTransitionFees(dealInfo.getTransitionFees());
		dealInfoDto.setServiceGovernance(dealInfo.getServiceGovernance());
		dealInfoDto.setAssessmentIndicator(dealInfo.getAssessmentIndicator());
		dealInfoDto.setSubmissionIndicator(dealInfo.getSubmissionIndicator());

		if (null != dealInfo.getUserInfo()) {
			dealInfoDto.setUserId(dealInfo.getUserInfo().getUserId());
		}

		final List<DealYearlyDataInfoDto> dealYearlyDataInfoDtos = new ArrayList<>();
		for (DealYearlyDataInfo dealYearlyDataInfo : dealInfo.getDealYearlyDataInfo()) {
			DealYearlyDataInfoDto dealYearlyDataInfoDto = modelMapper.map(dealYearlyDataInfo,
					DealYearlyDataInfoDto.class);
			dealYearlyDataInfoDtos.add(dealYearlyDataInfoDto);
		}
		dealInfoDto.setDealYearlyDataInfoDtos(dealYearlyDataInfoDtos);

		final List<DealCompetitorInfoDto> dealCompetitorInfoDtos = new ArrayList<>();
		for (DealCompetitorInfo dealCompetitorInfo : dealInfo.getDealCompetitorInfo()) {
			DealCompetitorInfoDto competitorInfoDto = modelMapper.map(dealCompetitorInfo, DealCompetitorInfoDto.class);
			dealCompetitorInfoDtos.add(competitorInfoDto);
		}
		dealInfoDto.setDealCompetitorInfoDtos(dealCompetitorInfoDtos);

		return dealInfoDto;
	}

	public String checkYesOrNo(boolean value) {
		return value ? YES : NO;
	}

	/**
	 * @param dealInfo
	 * @param dealInfoDto
	 * @param isSave
	 * @return
	 */
	public DealInfo prepareDealInfo(DealInfo dealInfo, DealInfoDto dealInfoDto, boolean isSave) {
		if (isSave) {
			final UserInfo info = new UserInfo();
			info.setUserId(dealInfoDto.getUserId());
			dealInfo.setUserInfo(info);
		}

		dealInfo.setModifiedById(dealInfoDto.getModifiedById());
		dealInfo.setDealName(dealInfoDto.getDealName());
		dealInfo.setClientName(dealInfoDto.getClientName());
		dealInfo.setDealPhase(dealInfoDto.getDealPhase());
		dealInfo.setDealStatus(dealInfoDto.getDealStatus() == null ? DealStatusEnum.OPEN.getName() : DealStatusEnum.valueOf(dealInfoDto.getDealStatus().toUpperCase()).getName());
		dealInfo.setDealTerm(dealInfoDto.getDealTerm());
		dealInfo.setDealType(DealTypeEnum.PAST_DEAL.getName());
		dealInfo.setTransitionFees(dealInfoDto.getTransitionFees());
		dealInfo.setServiceGovernance(dealInfoDto.getServiceGovernance());

		dealInfo.setIncludeHardware(checkTrueOrFalse(dealInfoDto.getIncludeHardware()));
		dealInfo.setOffshoreAllowed(checkTrueOrFalse(dealInfoDto.getOffshoreAllowed()));
		dealInfo.setThirdPartyFinance(checkTrueOrFalse(dealInfoDto.getThirdPartyFinance()));
		dealInfo.setOpenBook(checkTrueOrFalse(dealInfoDto.getOpenBook()));
		dealInfo.setCrossBorderTax(checkTrueOrFalse(dealInfoDto.getCrossBorderTax()));
		dealInfo.setNonStandardVariablePricing(checkTrueOrFalse(dealInfoDto.getNonStandardVariablePricing()));
		dealInfo.setMigrationCostApplicable(checkTrueOrFalse(dealInfoDto.getMigrationCost()));

		dealInfo.setStartDate(dealInfoDto.getStartDate());

		dealInfo.setClientIndustry(dealInfoDto.getClientIndustry());
		dealInfo.setCountry(dealInfoDto.getCountry());
		dealInfo.setCurrency(dealInfoDto.getCurrency());
		dealInfo.setServiceWindowSla(dealInfoDto.getServiceWindowSla());
		Calendar calendar = new GregorianCalendar();
		dealInfo.setModificationDate(new Date(calendar.getTimeInMillis()));

		dealInfo.setBidManager(dealInfoDto.getBidManager() !=null ?  dealInfoDto.getBidManager() : dealInfo.getBidManager());
		dealInfo.setFinancialEngineer(dealInfoDto.getFinancialEngineer() !=null ?  dealInfoDto.getFinancialEngineer() : dealInfo.getFinancialEngineer());
		dealInfo.setLeadSolutionArch(dealInfoDto.getLeadSolutionArch() !=null ?  dealInfoDto.getLeadSolutionArch() : dealInfo.getLeadSolutionArch());
		dealInfo.setSalesRepresentative(dealInfoDto.getSalesRepresentative() !=null ?  dealInfoDto.getSalesRepresentative() : dealInfo.getSalesRepresentative());

		dealInfo.setAssessmentIndicator(dealInfoDto.getAssessmentIndicator());
		dealInfo.setSubmissionIndicator(dealInfoDto.getSubmissionIndicator());

		List<DealYearlyDataInfo> dealYearlyDataInfos = new ArrayList<>();
		if (null != dealInfoDto.getDealYearlyDataInfoDtos()) {
			int size = dealInfoDto.getDealYearlyDataInfoDtos().size();
			for (int i = 0; i < size; i++) {
				if (isSave) {
					setDealYearlyDetails(dealYearlyDataInfos, dealInfo, new DealYearlyDataInfo(),
							dealInfoDto.getDealYearlyDataInfoDtos().get(i));
				} else {
					if (i <= dealInfo.getDealYearlyDataInfo().size() - 1) {
						setDealYearlyDetails(dealYearlyDataInfos, dealInfo, dealInfo.getDealYearlyDataInfo().get(i),
								dealInfoDto.getDealYearlyDataInfoDtos().get(i));
					} else {
						setDealYearlyDetails(dealYearlyDataInfos, dealInfo, new DealYearlyDataInfo(),
								dealInfoDto.getDealYearlyDataInfoDtos().get(i));
					}
				}
			}
			// Case when deal term is reduced
			if (!isSave && size < dealInfo.getDealYearlyDataInfo().size()) {
				for (int i = dealInfo.getDealYearlyDataInfo().size() - 1; i >= size; i--) {
					dealYearlyRepository.delete(dealInfo.getDealYearlyDataInfo().get(i));
				}
			}

			dealInfo.setDealYearlyDataInfo(dealYearlyDataInfos);
		}

		Set<DealCompetitorInfo> dealCompetitorInfos = new HashSet<>();
		if (null != dealInfoDto.getDealCompetitorInfoDtos()) {
			if (isSave) {
				for (DealCompetitorInfoDto competitorInfoDto : dealInfoDto.getDealCompetitorInfoDtos()) {
					setDealCompetitorDetails(dealCompetitorInfos, dealInfo, new DealCompetitorInfo(),
							competitorInfoDto);
				}
			} else {
				// Populate the comp DTO set
				Set<DealCompetitorInfo> updatedCompetitorInfos = new HashSet<>();
				for (DealCompetitorInfoDto competitorInfoDto : dealInfoDto.getDealCompetitorInfoDtos()) {
					DealCompetitorInfo competitorInfo = modelMapper.map(competitorInfoDto, DealCompetitorInfo.class);
					if(competitorInfo != null && !StringUtils.isEmpty(competitorInfo.getName())) {
						competitorInfo.setDealInfo(dealInfo);
						updatedCompetitorInfos.add(competitorInfo);
					}
				}

				// remove the irrelevant comp data
				dealCompetitorInfos = dealInfo.getDealCompetitorInfo();
				Iterator<DealCompetitorInfo> dealCompetitorInfoIterator = dealCompetitorInfos.iterator();
				while (dealCompetitorInfoIterator.hasNext()) {
					DealCompetitorInfo dealCompetitorInfo = dealCompetitorInfoIterator.next();
					if (!updatedCompetitorInfos.contains(dealCompetitorInfo)) {
						if (!updatedCompetitorInfos.isEmpty()) {
							updatedCompetitorInfos.remove(dealCompetitorInfo);
						}
						dealCompetitorInfoIterator.remove();
						dealCompetitorRepository.delete(dealCompetitorInfo);
					}
				}
				dealCompetitorInfos.addAll(updatedCompetitorInfos);
			}
		}

		dealInfo.setDealCompetitorInfo(dealCompetitorInfos);

		return dealInfo;

	}


	public boolean checkTrueOrFalse(String value) {
		return value != null && value.equalsIgnoreCase(YES) ? true : false;
	}

	/**
	 * @param dealInfo
	 * @param dealYearlyDataInfo
	 * @param dealYearlyDataInfoDto
	 * @return
	 */
	public void setDealYearlyDetails(List<DealYearlyDataInfo> dealYearlyDataInfos, DealInfo dealInfo,
			DealYearlyDataInfo dealYearlyDataInfo, DealYearlyDataInfoDto dealYearlyDataInfoDto) {
		dealYearlyDataInfo.setDealInfo(dealInfo);
		dealYearlyDataInfo.setNoOfDatacenters(dealYearlyDataInfoDto.getNoOfDatacenters());
		dealYearlyDataInfo.setNoOfSites(dealYearlyDataInfoDto.getNoOfSites());
		dealYearlyDataInfo.setNoOfUsers(dealYearlyDataInfoDto.getNoOfUsers());
		dealYearlyDataInfo.setYear(dealYearlyDataInfoDto.getYear());

		dealYearlyDataInfos.add(dealYearlyDataInfo);

	}

	/**
	 * @param dealInfo
	 * @param dealYearlyDataInfo
	 * @param dealYearlyDataInfoDto
	 * @return
	 */
	public void setDealCompetitorDetails(Set<DealCompetitorInfo> dealCompetitorInfos, DealInfo dealInfo,
			DealCompetitorInfo dealCompetitorInfo, DealCompetitorInfoDto dealCompetitorInfoDto) {
		dealCompetitorInfo.setDealInfo(dealInfo);
		dealCompetitorInfo.setIndustryName(dealCompetitorInfoDto.getIndustryName());
		dealCompetitorInfo.setName(dealCompetitorInfoDto.getName());
		dealCompetitorInfo.setType(dealCompetitorInfoDto.getType());

		dealCompetitorInfos.add(dealCompetitorInfo);

	}

	public static List<DealTypeDto> prepareDealTypeDtoList(List<DealType> dealTypeList) {
		final List<DealTypeDto> dealTypeDtoList = new ArrayList<>();
		for (DealType dealType : dealTypeList) {
			final DealTypeDto dealTypeDto = modelMapper.map(dealType, DealTypeDto.class);
			dealTypeDtoList.add(dealTypeDto);
		}
		return dealTypeDtoList;
	}

	public static List<DealPhaseDto> prepareDealPhaseDtoList(List<DealPhase> dealPhaseList) {
		final List<DealPhaseDto> dealPhaseDtoList = new ArrayList<>();
		for (DealPhase dealPhase : dealPhaseList) {
			final DealPhaseDto dealPhaseDto = modelMapper.map(dealPhase, DealPhaseDto.class);
			dealPhaseDtoList.add(dealPhaseDto);
		}
		return dealPhaseDtoList;
	}

	public static List<ServiceWindowDto> prepareServiceWindowDtoList(List<ServiceWindow> servicedWindowInfoList) {
		final List<ServiceWindowDto> serviceWindowDtoList = new ArrayList<>();
		for (ServiceWindow serviceWindow : servicedWindowInfoList) {
			final ServiceWindowDto serviceWindowDto = modelMapper.map(serviceWindow, ServiceWindowDto.class);
			serviceWindowDtoList.add(serviceWindowDto);
		}
		return serviceWindowDtoList;
	}

	public static List<DealCostParametersDto> prepareDealCostParametersDtoList(
			List<DealCostParameters> dealCostParametersList) {
		final List<DealCostParametersDto> dealCostParametersDtoList = new ArrayList<>();
		for (DealCostParameters dealCostParameters : dealCostParametersList) {
			final DealCostParametersDto dealCostParametersDto = modelMapper.map(dealCostParameters, DealCostParametersDto.class);
			dealCostParametersDtoList.add(dealCostParametersDto);
		}
		return dealCostParametersDtoList;
	}

}
