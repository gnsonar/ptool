package com.in.fujitsu.pricing.servicedesk.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskContactRatioInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskPriceDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskSolutionsDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskUnitPriceInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskYearlyDataInfoDto;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskContactRatioInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskRevenueInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskSolutionsInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskUnitPriceInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskYearlyRepository;

/**
 * @author mishrasub
 *
 */
@Component
public class ServiceDeskBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private ServiceDeskYearlyRepository serviceDeskYearlyRepository;

	/**
	 * @param solutions
	 * @return
	 */
	public List<ServiceDeskSolutionsDto> prepareContactSolutionsDtoList(List<ServiceDeskSolutionsInfo> solutions) {
		final List<ServiceDeskSolutionsDto> solutionsDtoList = new ArrayList<>();
		for (ServiceDeskSolutionsInfo solutionsInfo : solutions) {
			final ServiceDeskSolutionsDto solutionsDto = modelMapper.map(solutionsInfo, ServiceDeskSolutionsDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param serviceDeskContactRatioInfoList
	 * @return
	 */
	public List<ServiceDeskContactRatioInfoDto> prepareContactRatioDtoList(
			List<ServiceDeskContactRatioInfo> serviceDeskContactRatioInfoList) {
		final List<ServiceDeskContactRatioInfoDto> contactRatioDtoList = new ArrayList<>();
		for (ServiceDeskContactRatioInfo contactRatioInfo : serviceDeskContactRatioInfoList) {
			final ServiceDeskContactRatioInfoDto contactRatioDto = modelMapper.map(contactRatioInfo,
					ServiceDeskContactRatioInfoDto.class);
			contactRatioDtoList.add(contactRatioDto);
		}
		return contactRatioDtoList;
	}


	/**
	 * @param serviceDeskInfo
	 * @return
	 */
	public ServiceDeskInfoDto prepareServiceDeskInfoDto(ServiceDeskInfo serviceDeskInfo) {
		ServiceDeskInfoDto serviceDeskInfoDto = new ServiceDeskInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if (serviceDeskInfo != null) {
			serviceDeskInfoDto = modelMapper.map(serviceDeskInfo, ServiceDeskInfoDto.class);
			serviceDeskInfoDto.setDealId(serviceDeskInfo.getDealInfo().getDealId());

			final List<ServiceDeskYearlyDataInfoDto> serviceDeskYearlyDataInfoDtoList = new ArrayList<>();
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo
					.getServiceDeskYearlyDataInfoList()) {
				final ServiceDeskYearlyDataInfoDto serviceDeskYearlyDataInfoDto = modelMapper
						.map(serviceDeskYearlyDataInfo, ServiceDeskYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList())) {
					final List<ServiceDeskUnitPriceInfoDto> serviceDeskUnitPriceInfoDtoList = new ArrayList<>();
					for (ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : serviceDeskYearlyDataInfo
							.getServiceDeskUnitPriceInfoList()) {
						final ServiceDeskUnitPriceInfoDto serviceDeskUnitPriceInfoDto = modelMapper
								.map(serviceDeskUnitPriceInfo, ServiceDeskUnitPriceInfoDto.class);
						serviceDeskUnitPriceInfoDtoList.add(serviceDeskUnitPriceInfoDto);
					}
					serviceDeskYearlyDataInfoDto.setServiceDeskUnitPriceInfoDtoList(serviceDeskUnitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskRevenueInfoList())) {
					final List<ServiceDeskRevenueInfoDto> serviceDeskRevenueInfoDtoList = new ArrayList<>();
					for (ServiceDeskRevenueInfo serviceDeskRevenueInfo : serviceDeskYearlyDataInfo
							.getServiceDeskRevenueInfoList()) {
						final ServiceDeskRevenueInfoDto serviceDeskRevenueInfoDto = modelMapper
								.map(serviceDeskRevenueInfo, ServiceDeskRevenueInfoDto.class);
						serviceDeskRevenueInfoDtoList.add(serviceDeskRevenueInfoDto);
					}
					serviceDeskYearlyDataInfoDto.setServiceDeskRevenueInfoDtoList(serviceDeskRevenueInfoDtoList);
				}
				serviceDeskYearlyDataInfoDtoList.add(serviceDeskYearlyDataInfoDto);
			}

			serviceDeskInfoDto.setServiceDeskYearlyDataInfoDtoList(serviceDeskYearlyDataInfoDtoList);
		}

		return serviceDeskInfoDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param serviceDeskInfoDto
	 * @param isSave
	 * @return
	 */
	public ServiceDeskInfo prepareServiceDeskInfo(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskInfoDto serviceDeskInfoDto, boolean isSave) {
		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(serviceDeskInfoDto.getDealId());
			serviceDeskInfo.setDealInfo(dealInfo);
		}

		serviceDeskInfo.setLevelOfService(serviceDeskInfoDto.getLevelOfService());
		serviceDeskInfo.setMultiLingual(serviceDeskInfoDto.isMultiLingual());
		serviceDeskInfo.setOffshoreAllowed(serviceDeskInfoDto.isOffshoreAllowed());
		serviceDeskInfo.setToolingIncluded(serviceDeskInfoDto.isToolingIncluded());
		serviceDeskInfo.setSelectedContactSolution(serviceDeskInfoDto.getSelectedContactSolution());
		serviceDeskInfo.setTowerArchitect(serviceDeskInfoDto.getTowerArchitect() !=null ?  serviceDeskInfoDto.getTowerArchitect() : serviceDeskInfo.getTowerArchitect());
		serviceDeskInfo.setLevelIndicator(serviceDeskInfoDto.getLevelIndicator());
		serviceDeskInfo.setSelectedContactRatio(serviceDeskInfoDto.getSelectedContactRatio());

		List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList = new ArrayList<>();

		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(serviceDeskInfoDto.getServiceDeskYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = serviceDeskInfoDto.getServiceDeskYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				ServiceDeskYearlyDataInfoDto serviceDeskYearlyDataInfoDto = serviceDeskInfoDto
						.getServiceDeskYearlyDataInfoDtoList().get(i);
				ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(serviceDeskInfo.getServiceDeskYearlyDataInfoList())
						&& serviceDeskInfo.getServiceDeskYearlyDataInfoList().size() - 1 >= i)) {
					for (int j = 0; j < serviceDeskInfo.getServiceDeskYearlyDataInfoList().size(); j++) {
						ServiceDeskYearlyDataInfo existingServiceDeskYearlyDataInfo = serviceDeskInfo
								.getServiceDeskYearlyDataInfoList().get(j);
						if (existingServiceDeskYearlyDataInfo.getYear()
								.equals(serviceDeskYearlyDataInfoDto.getYear())) {
							serviceDeskYearlyDataInfo = existingServiceDeskYearlyDataInfo;
							break;
						}
					}
				} else {
					serviceDeskYearlyDataInfo = new ServiceDeskYearlyDataInfo();
				}
				serviceDeskYearlyDataInfoList = setServiceDeskYearlyDetails(serviceDeskYearlyDataInfoList,
						serviceDeskInfo, serviceDeskYearlyDataInfo, serviceDeskYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(serviceDeskInfo.getServiceDeskYearlyDataInfoList())
					&& yearlyDtoListSize < serviceDeskInfo.getServiceDeskYearlyDataInfoList().size()) {
				for (int i = serviceDeskInfo.getServiceDeskYearlyDataInfoList().size()
						- 1; i >= yearlyDtoListSize; i--) {
					serviceDeskYearlyRepository
							.delete(serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getYearId());
				}
			}
		}

		serviceDeskInfo.setServiceDeskYearlyDataInfoList(serviceDeskYearlyDataInfoList);

		return serviceDeskInfo;
	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @param serviceDeskInfo
	 * @param serviceDeskYearlyDataInfo
	 * @param serviceDeskYearlyDataInfoDto
	 * @return
	 */
	private List<ServiceDeskYearlyDataInfo> setServiceDeskYearlyDetails(
			List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo,
			ServiceDeskYearlyDataInfoDto serviceDeskYearlyDataInfoDto) {

		serviceDeskYearlyDataInfo.setTotalContacts(serviceDeskYearlyDataInfoDto.getTotalContacts() == null ? 0
				: serviceDeskYearlyDataInfoDto.getTotalContacts());
		serviceDeskYearlyDataInfo.setChatContacts(serviceDeskYearlyDataInfoDto.getChatContacts() == null ? 0
				: serviceDeskYearlyDataInfoDto.getChatContacts());
		serviceDeskYearlyDataInfo.setMailContacts(serviceDeskYearlyDataInfoDto.getMailContacts() == null ? 0
				: serviceDeskYearlyDataInfoDto.getMailContacts());
		serviceDeskYearlyDataInfo.setPortalContacts(serviceDeskYearlyDataInfoDto.getPortalContacts() == null ? 0
				: serviceDeskYearlyDataInfoDto.getPortalContacts());
		serviceDeskYearlyDataInfo.setVoiceContacts(serviceDeskYearlyDataInfoDto.getVoiceContacts() == null ? 0
				: serviceDeskYearlyDataInfoDto.getVoiceContacts());
		serviceDeskYearlyDataInfo.setYear(serviceDeskYearlyDataInfoDto.getYear());

		serviceDeskYearlyDataInfo.setServiceDeskInfo(serviceDeskInfo);

		// set the Unit Price
		List<ServiceDeskUnitPriceInfo> serviceDeskUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoDto.getServiceDeskUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList())) {
				for (ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : serviceDeskYearlyDataInfo
						.getServiceDeskUnitPriceInfoList()) {
					ServiceDeskUnitPriceInfoDto serviceDeskUnitPriceInfoDto = serviceDeskYearlyDataInfoDto
							.getServiceDeskUnitPriceInfoDtoList().get(0);
					if (serviceDeskUnitPriceInfoDto != null) {
						setUnitPrices(serviceDeskUnitPriceInfoDto, serviceDeskUnitPriceInfo);
						serviceDeskUnitPriceInfoList.add(serviceDeskUnitPriceInfo);
					}
				}
			} else {
				for (ServiceDeskUnitPriceInfoDto serviceDeskUnitPriceInfoDto : serviceDeskYearlyDataInfoDto
						.getServiceDeskUnitPriceInfoDtoList()) {
					ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo = new ServiceDeskUnitPriceInfo();
					setUnitPrices(serviceDeskUnitPriceInfoDto, serviceDeskUnitPriceInfo);
					
					serviceDeskUnitPriceInfo.setServiceDeskYearlyDataInfo(serviceDeskYearlyDataInfo);
					serviceDeskUnitPriceInfoList.add(serviceDeskUnitPriceInfo);
				}
			}
			serviceDeskYearlyDataInfo.setServiceDeskUnitPriceInfoList(serviceDeskUnitPriceInfoList);
		}

		// set the Revenue
		List<ServiceDeskRevenueInfo> serviceDeskRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfoDto.getServiceDeskRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskRevenueInfoList())) {
				for (ServiceDeskRevenueInfo serviceDeskRevenueInfo : serviceDeskYearlyDataInfo
						.getServiceDeskRevenueInfoList()) {
					ServiceDeskRevenueInfoDto serviceDeskRevenueInfoDto = serviceDeskYearlyDataInfoDto
							.getServiceDeskRevenueInfoDtoList().get(0);
					serviceDeskRevenueInfo
							.setTotalContactsRevenue(serviceDeskRevenueInfoDto.getTotalContactsRevenue() == null ? 0
									: serviceDeskRevenueInfoDto.getTotalContactsRevenue().intValue());
					serviceDeskRevenueInfoList.add(serviceDeskRevenueInfo);
				}
			} else {
				for (ServiceDeskRevenueInfoDto serviceDeskRevenueInfoDto : serviceDeskYearlyDataInfoDto
						.getServiceDeskRevenueInfoDtoList()) {
					ServiceDeskRevenueInfo serviceDeskRevenueInfo = new ServiceDeskRevenueInfo();
					serviceDeskRevenueInfo
					.setTotalContactsRevenue(serviceDeskRevenueInfoDto.getTotalContactsRevenue() == null ? 0
							: serviceDeskRevenueInfoDto.getTotalContactsRevenue().intValue());
					
					serviceDeskRevenueInfo.setServiceDeskYearlyDataInfo(serviceDeskYearlyDataInfo);
					serviceDeskRevenueInfoList.add(serviceDeskRevenueInfo);
				}
			}
			serviceDeskYearlyDataInfo.setServiceDeskRevenueInfoList(serviceDeskRevenueInfoList);
		}

		serviceDeskYearlyDataInfoList.add(serviceDeskYearlyDataInfo);

		return serviceDeskYearlyDataInfoList;

	}

	private void setUnitPrices(ServiceDeskUnitPriceInfoDto serviceDeskUnitPriceInfoDto,
			ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo) {
		serviceDeskUnitPriceInfo.setTotalContactsUnitPrice(
				serviceDeskUnitPriceInfoDto.getTotalContactsUnitPrice() == null ? BigDecimal.ZERO
						: serviceDeskUnitPriceInfoDto.getTotalContactsUnitPrice());
		serviceDeskUnitPriceInfo
				.setChatContactsUnitPrice(serviceDeskUnitPriceInfoDto.getChatContactsUnitPrice() == null
						? BigDecimal.ZERO : serviceDeskUnitPriceInfoDto.getChatContactsUnitPrice());
		serviceDeskUnitPriceInfo
				.setMailContactsUnitPrice(serviceDeskUnitPriceInfoDto.getMailContactsUnitPrice() == null
						? BigDecimal.ZERO : serviceDeskUnitPriceInfoDto.getMailContactsUnitPrice());
		serviceDeskUnitPriceInfo.setPortalContactsUnitPrice(
				serviceDeskUnitPriceInfoDto.getPortalContactsUnitPrice() == null ? BigDecimal.ZERO
						: serviceDeskUnitPriceInfoDto.getPortalContactsUnitPrice());
		serviceDeskUnitPriceInfo.setVoiceContactsUnitPrice(
				serviceDeskUnitPriceInfoDto.getVoiceContactsUnitPrice() == null ? BigDecimal.ZERO
						: serviceDeskUnitPriceInfoDto.getVoiceContactsUnitPrice());
	}

	/**
	 * @param serviceDeskInfo
	 * @param serviceDeskPriceDtoList
	 * @return
	 */
	public ServiceDeskInfo prepareServiceDeskPrice(ServiceDeskInfo serviceDeskInfo,
			List<ServiceDeskPriceDto> serviceDeskPriceDtoList) {
		List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList = new ArrayList<>();
		ServiceDeskPriceDto serviceDeskPriceDto = new ServiceDeskPriceDto();
		for (ServiceDeskYearlyDataInfo yearlydataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			for (ServiceDeskPriceDto priceDto : serviceDeskPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					serviceDeskPriceDto = priceDto;
					break;
				}
			}
			serviceDeskYearlyDataInfoList = updateServiceDeskYearlyDetails(serviceDeskYearlyDataInfoList,
					yearlydataInfo, serviceDeskPriceDto);
		}

		serviceDeskInfo.setServiceDeskYearlyDataInfoList(serviceDeskYearlyDataInfoList);

		return serviceDeskInfo;
	}

	/**
	 * @param serviceDeskInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public ServiceDeskInfo prepareSolutionCriteria(ServiceDeskInfo serviceDeskInfo, SolutionCriteriaDto solutionCriteriaDto) {

		serviceDeskInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		serviceDeskInfo.setLevelOfService(solutionCriteriaDto.getLevelOfService());
		serviceDeskInfo.setMultiLingual(solutionCriteriaDto.isMultiLingual());
		serviceDeskInfo.setToolingIncluded(solutionCriteriaDto.isToolingIncluded());

		return serviceDeskInfo;

	}

	/**
	 * @param serviceDeskYearlyDataInfoList
	 * @param yearlydataInfo
	 * @param serviceDeskPriceDto
	 * @return
	 */
	private List<ServiceDeskYearlyDataInfo> updateServiceDeskYearlyDetails(
			List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList, ServiceDeskYearlyDataInfo yearlydataInfo,
			ServiceDeskPriceDto serviceDeskPriceDto) {

		if (!CollectionUtils.isEmpty(yearlydataInfo.getServiceDeskUnitPriceInfoList())) {
			// Update the existing unit prices
			for (ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : yearlydataInfo.getServiceDeskUnitPriceInfoList()) {
				serviceDeskUnitPriceInfo.setTotalContactsUnitPrice(serviceDeskPriceDto.getTotalContactsUnitPrice());
				serviceDeskUnitPriceInfo.setChatContactsUnitPrice(serviceDeskPriceDto.getChatContactsUnitPrice());
				serviceDeskUnitPriceInfo.setMailContactsUnitPrice(serviceDeskPriceDto.getMailContactsUnitPrice());
				serviceDeskUnitPriceInfo.setPortalContactsUnitPrice(serviceDeskPriceDto.getPortalContactsUnitPrice());
				serviceDeskUnitPriceInfo.setVoiceContactsUnitPrice(serviceDeskPriceDto.getVoiceContactsUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<ServiceDeskUnitPriceInfo> serviceDeskUnitPriceInfoList = new ArrayList<>();
			ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo = new ServiceDeskUnitPriceInfo();
			serviceDeskUnitPriceInfo.setTotalContactsUnitPrice(serviceDeskPriceDto.getTotalContactsUnitPrice());
			serviceDeskUnitPriceInfo.setChatContactsUnitPrice(serviceDeskPriceDto.getChatContactsUnitPrice());
			serviceDeskUnitPriceInfo.setMailContactsUnitPrice(serviceDeskPriceDto.getMailContactsUnitPrice());
			serviceDeskUnitPriceInfo.setPortalContactsUnitPrice(serviceDeskPriceDto.getPortalContactsUnitPrice());
			serviceDeskUnitPriceInfo.setVoiceContactsUnitPrice(serviceDeskPriceDto.getVoiceContactsUnitPrice());
			serviceDeskUnitPriceInfo.setServiceDeskYearlyDataInfo(yearlydataInfo);

			serviceDeskUnitPriceInfoList.add(serviceDeskUnitPriceInfo);
			yearlydataInfo.setServiceDeskUnitPriceInfoList(serviceDeskUnitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(yearlydataInfo.getServiceDeskRevenueInfoList())) {
			// Update the existing revenues
			for (ServiceDeskRevenueInfo serviceDeskRevenueInfo : yearlydataInfo.getServiceDeskRevenueInfoList()) {
				serviceDeskRevenueInfo.setTotalContactsRevenue(serviceDeskPriceDto.getTotalContactsRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<ServiceDeskRevenueInfo> serviceDeskRevenueInfoList = new ArrayList<>();
			ServiceDeskRevenueInfo serviceDeskRevenueInfo = new ServiceDeskRevenueInfo();
			serviceDeskRevenueInfo.setTotalContactsRevenue(serviceDeskPriceDto.getTotalContactsRevenue());
			serviceDeskRevenueInfo.setServiceDeskYearlyDataInfo(yearlydataInfo);

			serviceDeskRevenueInfoList.add(serviceDeskRevenueInfo);
			yearlydataInfo.setServiceDeskRevenueInfoList(serviceDeskRevenueInfoList);
		}

		serviceDeskYearlyDataInfoList.add(yearlydataInfo);

		return serviceDeskYearlyDataInfoList;
	}

}
