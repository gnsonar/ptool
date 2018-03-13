package com.in.fujitsu.pricing.application.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.ApplicationInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationPriceDto;
import com.in.fujitsu.pricing.application.dto.ApplicationRevenueInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationSolutionsInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationUnitPriceInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationYearlyDataInfoDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationRevenueInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationSolutionsInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.application.repository.ApplicationYearlyRepository;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;

/**
 * @author mishrasub
 *
 */
@Component
public class ApplicationBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private ApplicationYearlyRepository applicationYearlyRepository;

	/**
	 * @param appSolutionsInfoList
	 * @return
	 */
	public List<ApplicationSolutionsInfoDto> prepareAppSolutionsDtoList(
			List<ApplicationSolutionsInfo> appSolutionsInfoList) {
		final List<ApplicationSolutionsInfoDto> solutionsDtoList = new ArrayList<>();
		for (ApplicationSolutionsInfo appSolutionsInfo : appSolutionsInfoList) {
			final ApplicationSolutionsInfoDto solutionsDto = modelMapper.map(appSolutionsInfo,
					ApplicationSolutionsInfoDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param applicationInfo
	 * @return
	 */
	public ApplicationInfoDto prepareApplicationInfoDto(ApplicationInfo applicationInfo) {
		ApplicationInfoDto applicationInfoDto = new ApplicationInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if (applicationInfo != null) {
			applicationInfoDto = modelMapper.map(applicationInfo, ApplicationInfoDto.class);
			applicationInfoDto.setDealId(applicationInfo.getDealInfo().getDealId());
			final List<ApplicationYearlyDataInfoDto> appYearlyDataInfoDtoList = new ArrayList<>();
			for (ApplicationYearlyDataInfo appYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
				final ApplicationYearlyDataInfoDto appYearlyDataInfoDto = modelMapper.map(appYearlyDataInfo,
						ApplicationYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(appYearlyDataInfo.getAppUnitPriceInfoList())) {
					final List<ApplicationUnitPriceInfoDto> appUnitPriceInfoDtoList = new ArrayList<>();
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : appYearlyDataInfo.getAppUnitPriceInfoList()) {
						final ApplicationUnitPriceInfoDto applicationUnitPriceInfoDto = modelMapper.map(applicationUnitPriceInfo,
								ApplicationUnitPriceInfoDto.class);
						appUnitPriceInfoDtoList.add(applicationUnitPriceInfoDto);
					}
					appYearlyDataInfoDto.setAppUnitPriceInfoDtoList(appUnitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(appYearlyDataInfo.getApplicationRevenueInfoList())) {
					final List<ApplicationRevenueInfoDto> applicationRevenueInfoDtoList = new ArrayList<>();
					for (ApplicationRevenueInfo applicationRevenueInfo : appYearlyDataInfo.getApplicationRevenueInfoList()) {
						final ApplicationRevenueInfoDto applicationRevenueInfoDto = modelMapper.map(applicationRevenueInfo,
								ApplicationRevenueInfoDto.class);
						applicationRevenueInfoDtoList.add(applicationRevenueInfoDto);
					}
					appYearlyDataInfoDto.setApplicationRevenueInfoDtoList(applicationRevenueInfoDtoList);
				}
				appYearlyDataInfoDtoList.add(appYearlyDataInfoDto);
			}

			applicationInfoDto.setAppYearlyDataInfoDtoList(appYearlyDataInfoDtoList);
		}
		return applicationInfoDto;
	}

	/**
	 * @param appInfo
	 * @param appInfoDto
	 * @param isSave
	 * @return
	 */
	public ApplicationInfo prepareApplicationInfo(ApplicationInfo appInfo, ApplicationInfoDto appInfoDto,
			boolean isSave) {

		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(appInfoDto.getDealId());
			appInfo.setDealInfo(dealInfo);
		}

		appInfo.setOffshoreAllowed(appInfoDto.isOffshoreAllowed());
		appInfo.setLevelOfService(appInfoDto.getLevelOfService());
		appInfo.setSelectedAppSolution(appInfoDto.getSelectedAppSolution());
		appInfo.setTowerArchitect(appInfoDto.getTowerArchitect() !=null ?  appInfoDto.getTowerArchitect() : appInfo.getTowerArchitect());
		appInfo.setLevelIndicator(appInfoDto.getLevelIndicator());

		List<ApplicationYearlyDataInfo> appYearlyDataInfoList = new ArrayList<>();
		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(appInfoDto.getAppYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = appInfoDto.getAppYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				ApplicationYearlyDataInfoDto applicationYearlyDataInfoDto = appInfoDto.getAppYearlyDataInfoDtoList().get(i);
				ApplicationYearlyDataInfo applicationYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(appInfo.getAppYearlyDataInfos())
						&& appInfo.getAppYearlyDataInfos().size() - 1 >= i)) {
					for (int j = 0; j < appInfo.getAppYearlyDataInfos().size(); j++) {
						ApplicationYearlyDataInfo existingApplicationYearlyDataInfo = appInfo.getAppYearlyDataInfos().get(j);
						if (existingApplicationYearlyDataInfo.getYear().equals(applicationYearlyDataInfoDto.getYear())) {
							applicationYearlyDataInfo = existingApplicationYearlyDataInfo;
							break;
						}
					}
				} else {
					applicationYearlyDataInfo = new ApplicationYearlyDataInfo();
				}
				appYearlyDataInfoList = setAppYearlyDetails(appYearlyDataInfoList, appInfo, applicationYearlyDataInfo,applicationYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(appInfo.getAppYearlyDataInfos())
					&& yearlyDtoListSize < appInfo.getAppYearlyDataInfos().size()) {
				for (int i = appInfo.getAppYearlyDataInfos().size() - 1; i >= yearlyDtoListSize; i--) {
					applicationYearlyRepository.delete(appInfo.getAppYearlyDataInfos().get(i).getId());
				}
			}
		}

		appInfo.setAppYearlyDataInfos(appYearlyDataInfoList);

		return appInfo;
	}

	/**
	 * @param appYearlyDataInfoList
	 * @param appInfo
	 * @param applicationYearlyDataInfo
	 * @param applicationYearlyDataInfoDto
	 * @return
	 */
	private List<ApplicationYearlyDataInfo> setAppYearlyDetails(List<ApplicationYearlyDataInfo> appYearlyDataInfoList,
			ApplicationInfo appInfo, ApplicationYearlyDataInfo applicationYearlyDataInfo,
			ApplicationYearlyDataInfoDto applicationYearlyDataInfoDto) {
		applicationYearlyDataInfo.setTotalAppsVolume(applicationYearlyDataInfoDto.getTotalAppsVolume() == null ? 0
				: applicationYearlyDataInfoDto.getTotalAppsVolume());
		applicationYearlyDataInfo.setSimpleAppsVolume(applicationYearlyDataInfoDto.getSimpleAppsVolume() == null ? 0
				: applicationYearlyDataInfoDto.getSimpleAppsVolume());
		applicationYearlyDataInfo.setMediumAppsVolume(applicationYearlyDataInfoDto.getMediumAppsVolume() == null ? 0
				: applicationYearlyDataInfoDto.getMediumAppsVolume());
		applicationYearlyDataInfo.setComplexAppsVolume(applicationYearlyDataInfoDto.getComplexAppsVolume() == null ? 0
				: applicationYearlyDataInfoDto.getComplexAppsVolume());
		applicationYearlyDataInfo
				.setVeryComplexAppsVolume(applicationYearlyDataInfoDto.getVeryComplexAppsVolume() == null ? 0
						: applicationYearlyDataInfoDto.getVeryComplexAppsVolume());
		applicationYearlyDataInfo.setYear(applicationYearlyDataInfoDto.getYear());

		applicationYearlyDataInfo.setAppInfo(appInfo);
		// set the Unit Price
		List<ApplicationUnitPriceInfo> appUnitPriceInfoDtoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoDto.getAppUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
				for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo.getAppUnitPriceInfoList()) {
					ApplicationUnitPriceInfoDto applicationUnitPriceInfoDto = applicationYearlyDataInfoDto.getAppUnitPriceInfoDtoList().get(0);
					if (applicationUnitPriceInfoDto != null) {
						setUnitPrices(applicationUnitPriceInfo, applicationUnitPriceInfoDto);
						appUnitPriceInfoDtoList.add(applicationUnitPriceInfo);
					}
				}
			} else {
				for (ApplicationUnitPriceInfoDto applicationUnitPriceInfoDto : applicationYearlyDataInfoDto
						.getAppUnitPriceInfoDtoList()) {
					ApplicationUnitPriceInfo applicationUnitPriceInfo = new ApplicationUnitPriceInfo();
					setUnitPrices(applicationUnitPriceInfo, applicationUnitPriceInfoDto);
					applicationUnitPriceInfo.setAppYearlyDataInfo(applicationYearlyDataInfo);
					appUnitPriceInfoDtoList.add(applicationUnitPriceInfo);
				}
			}
			applicationYearlyDataInfo.setAppUnitPriceInfoList(appUnitPriceInfoDtoList);
		}

		// set the Revenue
		List<ApplicationRevenueInfo> applicationRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoDto.getApplicationRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getApplicationRevenueInfoList())) {
				for (ApplicationRevenueInfo applicationRevenueInfo : applicationYearlyDataInfo.getApplicationRevenueInfoList()) {
					ApplicationRevenueInfoDto applicationRevenueInfoDto = applicationYearlyDataInfoDto.getApplicationRevenueInfoDtoList().get(0);
					applicationRevenueInfo.setTotalAppsRevenue(applicationRevenueInfoDto.getTotalAppsRevenue() == null
							? 0 : applicationRevenueInfoDto.getTotalAppsRevenue().intValue());
					applicationRevenueInfoList.add(applicationRevenueInfo);
				}
			} else {
				for (ApplicationRevenueInfoDto applicationRevenueInfoDto : applicationYearlyDataInfoDto.getApplicationRevenueInfoDtoList()) {
					ApplicationRevenueInfo applicationRevenueInfo = new ApplicationRevenueInfo();
					applicationRevenueInfo.setTotalAppsRevenue(applicationRevenueInfoDto.getTotalAppsRevenue() == null
							? 0 : applicationRevenueInfoDto.getTotalAppsRevenue().intValue());
					applicationRevenueInfo.setAppYearlyDataInfo(applicationYearlyDataInfo);
					applicationRevenueInfoList.add(applicationRevenueInfo);
				}
			}
			applicationYearlyDataInfo.setApplicationRevenueInfoList(applicationRevenueInfoList);
		}

		appYearlyDataInfoList.add(applicationYearlyDataInfo);

		return appYearlyDataInfoList;

	}

	private void setUnitPrices(ApplicationUnitPriceInfo applicationUnitPriceInfo,
			ApplicationUnitPriceInfoDto applicationUnitPriceInfoDto) {
		applicationUnitPriceInfo
				.setTotalAppsUnitPrice(applicationUnitPriceInfoDto.getTotalAppsUnitPrice() == null
						? BigDecimal.ZERO : applicationUnitPriceInfoDto.getTotalAppsUnitPrice());
		applicationUnitPriceInfo
				.setSimpleAppsUnitPrice(applicationUnitPriceInfoDto.getSimpleAppsUnitPrice() == null
						? BigDecimal.ZERO : applicationUnitPriceInfoDto.getSimpleAppsUnitPrice());
		applicationUnitPriceInfo
				.setMediumAppsUnitPrice(applicationUnitPriceInfoDto.getMediumAppsUnitPrice() == null
						? BigDecimal.ZERO : applicationUnitPriceInfoDto.getMediumAppsUnitPrice());
		applicationUnitPriceInfo
				.setComplexAppsUnitPrice(applicationUnitPriceInfoDto.getComplexAppsUnitPrice() == null
						? BigDecimal.ZERO : applicationUnitPriceInfoDto.getComplexAppsUnitPrice());
		applicationUnitPriceInfo.setVeryComplexAppsUnitPrice(
				applicationUnitPriceInfoDto.getVeryComplexAppsUnitPrice() == null ? BigDecimal.ZERO
						: applicationUnitPriceInfoDto.getVeryComplexAppsUnitPrice());
	}

	/**
	 * @param applicationInfo
	 * @param applicationPriceDtoList
	 * @return
	 */
	public ApplicationInfo prepareApplicationPrice(ApplicationInfo applicationInfo,
			List<ApplicationPriceDto> applicationPriceDtoList) {
		List<ApplicationYearlyDataInfo> appYearlyDataInfoList = new ArrayList<>();
		ApplicationPriceDto applicationPriceDto = new ApplicationPriceDto();
		for (ApplicationYearlyDataInfo yearlydataInfo : applicationInfo.getAppYearlyDataInfos()) {
			for (ApplicationPriceDto priceDto : applicationPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					applicationPriceDto = priceDto;
					break;
				}
			}
			appYearlyDataInfoList = updateApplicationYearlyDetails(appYearlyDataInfoList, yearlydataInfo, applicationPriceDto);
		}

		applicationInfo.setAppYearlyDataInfos(appYearlyDataInfoList);

		return applicationInfo;
	}

	/**
	 * @param applicationInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public ApplicationInfo prepareSolutionCriteria(ApplicationInfo applicationInfo, SolutionCriteriaDto solutionCriteriaDto) {

		applicationInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		applicationInfo.setLevelOfService(solutionCriteriaDto.getLevelOfService());

		return applicationInfo;

	}

	/**
	 * @param appYearlyDataInfoList
	 * @param yearlydataInfo
	 * @param applicationPriceDto
	 * @return
	 */
	private List<ApplicationYearlyDataInfo> updateApplicationYearlyDetails(
			List<ApplicationYearlyDataInfo> appYearlyDataInfoList, ApplicationYearlyDataInfo yearlydataInfo,
			ApplicationPriceDto applicationPriceDto) {
		if (!CollectionUtils.isEmpty(yearlydataInfo.getAppUnitPriceInfoList())) {
			// Update the existing unit prices
			for (ApplicationUnitPriceInfo applicationUnitPriceInfo : yearlydataInfo.getAppUnitPriceInfoList()) {
				applicationUnitPriceInfo.setTotalAppsUnitPrice(applicationPriceDto.getTotalAppsUnitPrice());
				applicationUnitPriceInfo.setSimpleAppsUnitPrice(applicationPriceDto.getSimpleAppsUnitPrice());
				applicationUnitPriceInfo.setMediumAppsUnitPrice(applicationPriceDto.getMediumAppsUnitPrice());
				applicationUnitPriceInfo.setComplexAppsUnitPrice(applicationPriceDto.getComplexAppsUnitPrice());
				applicationUnitPriceInfo.setVeryComplexAppsUnitPrice(applicationPriceDto.getVeryComplexAppsUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<ApplicationUnitPriceInfo> applicationUnitPriceInfoList = new ArrayList<>();
			ApplicationUnitPriceInfo applicationUnitPriceInfo = new ApplicationUnitPriceInfo();
			applicationUnitPriceInfo.setTotalAppsUnitPrice(applicationPriceDto.getTotalAppsUnitPrice());
			applicationUnitPriceInfo.setSimpleAppsUnitPrice(applicationPriceDto.getSimpleAppsUnitPrice());
			applicationUnitPriceInfo.setMediumAppsUnitPrice(applicationPriceDto.getMediumAppsUnitPrice());
			applicationUnitPriceInfo.setComplexAppsUnitPrice(applicationPriceDto.getComplexAppsUnitPrice());
			applicationUnitPriceInfo.setVeryComplexAppsUnitPrice(applicationPriceDto.getVeryComplexAppsUnitPrice());
			applicationUnitPriceInfo.setAppYearlyDataInfo(yearlydataInfo);

			applicationUnitPriceInfoList.add(applicationUnitPriceInfo);
			yearlydataInfo.setAppUnitPriceInfoList(applicationUnitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(yearlydataInfo.getApplicationRevenueInfoList())) {
			// Update the existing revenues
			for (ApplicationRevenueInfo applicationRevenueInfo : yearlydataInfo.getApplicationRevenueInfoList()) {
				applicationRevenueInfo.setTotalAppsRevenue(applicationPriceDto.getTotalAppsRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<ApplicationRevenueInfo> applicationRevenueInfoList = new ArrayList<>();
			ApplicationRevenueInfo applicationRevenueInfo = new ApplicationRevenueInfo();
			applicationRevenueInfo.setTotalAppsRevenue(applicationPriceDto.getTotalAppsRevenue());
			applicationRevenueInfo.setAppYearlyDataInfo(yearlydataInfo);

			applicationRevenueInfoList.add(applicationRevenueInfo);
			yearlydataInfo.setApplicationRevenueInfoList(applicationRevenueInfoList);
		}

		appYearlyDataInfoList.add(yearlydataInfo);

		return appYearlyDataInfoList;
	}

}
