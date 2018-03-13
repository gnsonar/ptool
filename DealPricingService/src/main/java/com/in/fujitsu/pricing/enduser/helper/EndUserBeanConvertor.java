package com.in.fujitsu.pricing.enduser.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserContactRatioInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserMigrationCostInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserPriceDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserResolutionTimeDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserSolutionInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserUnitPriceInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserYearlyDataInfoDto;
import com.in.fujitsu.pricing.enduser.dto.ImacFactorInfoDto;
import com.in.fujitsu.pricing.enduser.entity.EndUserContactRatioInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserImacFactorInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserMigrationCostInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserResolutionTimeInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserRevenueInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserSolutionInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserUnitPriceInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.enduser.repository.EndUserYearlyRepository;
import com.in.fujitsu.pricing.entity.DealInfo;

@Component
public class EndUserBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	EndUserYearlyRepository endUserYearlyRepository;

	/**
	 * @param solutions
	 * @return end user Solution list
	 */
	public List<EndUserSolutionInfoDto> prepareEndUserSolutionsDtoList(List<EndUserSolutionInfo> solutions) {
		final List<EndUserSolutionInfoDto> solutionsDtoList = new ArrayList<>();
		for (EndUserSolutionInfo solutionsInfo : solutions) {
			final EndUserSolutionInfoDto solutionsDto = modelMapper.map(solutionsInfo, EndUserSolutionInfoDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param imac
	 *            factors info
	 * @return imac factors
	 */
	public List<ImacFactorInfoDto> prepareImacFactorDtoList(List<EndUserImacFactorInfo> imacFactorInfoList) {
		final List<ImacFactorInfoDto> imacDtoList = new ArrayList<>();
		for (EndUserImacFactorInfo imacFactorInfo : imacFactorInfoList) {
			final ImacFactorInfoDto wanFactorDto = modelMapper.map(imacFactorInfo, ImacFactorInfoDto.class);
			imacDtoList.add(wanFactorDto);
		}
		return imacDtoList;
	}

	/**
	 * @param endUserContactRatioInfoList
	 * @return
	 */
	public List<EndUserContactRatioInfoDto> prepareContactRatioDtoList(
			List<EndUserContactRatioInfo> endUserContactRatioInfoList) {
		final List<EndUserContactRatioInfoDto> contactRatioDtoList = new ArrayList<>();
		for (EndUserContactRatioInfo contactRatioInfo : endUserContactRatioInfoList) {
			final EndUserContactRatioInfoDto contactRatioDto = modelMapper.map(contactRatioInfo,
					EndUserContactRatioInfoDto.class);
			contactRatioDtoList.add(contactRatioDto);
		}
		return contactRatioDtoList;
	}

	public List<EndUserResolutionTimeDto> prepareResolutionTimeDToList(List<EndUserResolutionTimeInfo> resTimeList) {
		List<EndUserResolutionTimeDto> resTimeDtoList = new ArrayList<>();
		for (EndUserResolutionTimeInfo resTimeInfo : resTimeList) {
			final EndUserResolutionTimeDto resTimeDto = modelMapper.map(resTimeInfo, EndUserResolutionTimeDto.class);
			resTimeDtoList.add(resTimeDto);
		}
		return resTimeDtoList;
	}

	public EndUserInfo prepareEndUserInfo(EndUserInfo endUserInfo, EndUserInfoDto endUserInfoDto, boolean isSave) {
		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(endUserInfoDto.getDealId());
			endUserInfo.setDealInfo(dealInfo);
		}

		endUserInfo.setOffshoreAllowed(endUserInfoDto.isOffshoreAllowed());
		endUserInfo.setIncludeHardware(endUserInfoDto.isIncludeHardware());
		endUserInfo.setIncludeBreakFix(endUserInfoDto.isIncludeBreakFix());
		endUserInfo.setResolutionTime(endUserInfoDto.getResolutionTime());
		endUserInfo.setImacType(endUserInfoDto.getImacType());
		endUserInfo.setSelectedSolution(endUserInfoDto.getSelectedSolution());
		endUserInfo.setLevelIndicator(endUserInfoDto.getLevelIndicator());
		endUserInfo.setTowerArchitect(endUserInfoDto.getTowerArchitect() !=null ?  endUserInfoDto.getTowerArchitect() : endUserInfo.getTowerArchitect());
		endUserInfo.setSelectedContactSolution(endUserInfoDto.getSelectedContactSolution());

		List<EndUserYearlyDataInfo> endUserYearlyDataInfoList = new ArrayList<>();

		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(endUserInfoDto.getEndUserYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = endUserInfoDto.getEndUserYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				EndUserYearlyDataInfoDto endUserYearlyInfoDto = endUserInfoDto.getEndUserYearlyDataInfoDtoList().get(i);
				EndUserYearlyDataInfo endUserYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(endUserInfo.getEndUserYearlyDataInfoList())
						&& endUserInfo.getEndUserYearlyDataInfoList().size() - 1 >= i)) {
					for (int j = 0; j < endUserInfo.getEndUserYearlyDataInfoList().size(); j++) {
						EndUserYearlyDataInfo existingEndUserYearlyDataInfo = endUserInfo.getEndUserYearlyDataInfoList()
								.get(j);
						if (existingEndUserYearlyDataInfo.getYear().equals(endUserYearlyInfoDto.getYear())) {
							endUserYearlyDataInfo = existingEndUserYearlyDataInfo;
							break;
						}
					}
				} else {
					endUserYearlyDataInfo = new EndUserYearlyDataInfo();
				}
				endUserYearlyDataInfoList = setEndUserYearlyDetails(endUserYearlyDataInfoList, endUserInfo,
						endUserYearlyDataInfo, endUserYearlyInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(endUserInfo.getEndUserYearlyDataInfoList())
					&& yearlyDtoListSize < endUserInfo.getEndUserYearlyDataInfoList().size()) {
				for (int i = endUserInfo.getEndUserYearlyDataInfoList().size() - 1; i >= yearlyDtoListSize; i--) {
					endUserYearlyRepository.delete(endUserInfo.getEndUserYearlyDataInfoList().get(i).getYearId());
				}
			}
		}

		endUserInfo.setEndUserYearlyDataInfoList(endUserYearlyDataInfoList);

		return endUserInfo;
	}

	private List<EndUserYearlyDataInfo> setEndUserYearlyDetails(List<EndUserYearlyDataInfo> endUserYearlyDataInfoList,
			EndUserInfo endUserInfo, EndUserYearlyDataInfo endUserYearlyDataInfo,
			EndUserYearlyDataInfoDto endUserYearlyInfoDto) {

		endUserYearlyDataInfo.setYear(endUserYearlyInfoDto.getYear());
		endUserYearlyDataInfo.setEndUserDevices(
				endUserYearlyInfoDto.getEndUserDevices() == null ? 0 : endUserYearlyInfoDto.getEndUserDevices());
		endUserYearlyDataInfo
				.setLaptops(endUserYearlyInfoDto.getLaptops() == null ? 0 : endUserYearlyInfoDto.getLaptops());
		endUserYearlyDataInfo.setHighEndLaptops(
				endUserYearlyInfoDto.getHighEndLaptops() == null ? 0 : endUserYearlyInfoDto.getHighEndLaptops());
		endUserYearlyDataInfo.setStandardLaptops(
				endUserYearlyInfoDto.getStandardLaptops() == null ? 0 : endUserYearlyInfoDto.getStandardLaptops());
		endUserYearlyDataInfo.setThinClients(
				endUserYearlyInfoDto.getThinClients() == null ? 0 : endUserYearlyInfoDto.getThinClients());
		endUserYearlyDataInfo
				.setDesktops(endUserYearlyInfoDto.getDesktops() == null ? 0 : endUserYearlyInfoDto.getDesktops());
		endUserYearlyDataInfo.setMobileDevices(
				endUserYearlyInfoDto.getMobileDevices() == null ? 0 : endUserYearlyInfoDto.getMobileDevices());
		endUserYearlyDataInfo.setImacDevices(
				endUserYearlyInfoDto.getImacDevices() == null ? 0 : endUserYearlyInfoDto.getImacDevices());

		endUserYearlyDataInfo.setEndUserInfo(endUserInfo);
		// set the Unit Price
		List<EndUserUnitPriceInfo> endUserUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(endUserYearlyInfoDto.getEndUserUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(endUserYearlyDataInfo.getEndUserUnitPriceInfoList())) {
				for (EndUserUnitPriceInfo endUserUnitPriceInfo : endUserYearlyDataInfo.getEndUserUnitPriceInfoList()) {
					EndUserUnitPriceInfoDto endUserUnitPriceInfoDto = endUserYearlyInfoDto
							.getEndUserUnitPriceInfoDtoList().get(0);
					if (endUserUnitPriceInfoDto != null) {
						setUnitPrices(endUserUnitPriceInfo, endUserUnitPriceInfoDto);
						endUserUnitPriceInfoList.add(endUserUnitPriceInfo);
					}
				}
			} else {
				for (EndUserUnitPriceInfoDto endUserUnitPriceInfoDto : endUserYearlyInfoDto
						.getEndUserUnitPriceInfoDtoList()) {
					EndUserUnitPriceInfo endUserUnitPriceInfo = new EndUserUnitPriceInfo();
					setUnitPrices(endUserUnitPriceInfo, endUserUnitPriceInfoDto);
					
					endUserUnitPriceInfo.setEndUserYearlyDataInfo(endUserYearlyDataInfo);
					endUserUnitPriceInfoList.add(endUserUnitPriceInfo);
				}
			}
			endUserYearlyDataInfo.setEndUserUnitPriceInfoList(endUserUnitPriceInfoList);
		}

		// set the Revenue
		List<EndUserRevenueInfo> endUserRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(endUserYearlyInfoDto.getEndUserRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(endUserYearlyDataInfo.getEndUserRevenueInfoList())) {
				for (EndUserRevenueInfo endUserRevenueInfo : endUserYearlyDataInfo.getEndUserRevenueInfoList()) {
					EndUserRevenueInfoDto endUserRevenueInfoDto = endUserYearlyInfoDto
							.getEndUserRevenueInfoDtoList().get(0);
					endUserRevenueInfo.setTotalEndUserDevices(endUserRevenueInfoDto.getTotalEndUserDevices() == null ? 0
							: endUserRevenueInfoDto.getTotalEndUserDevices());
					endUserRevenueInfo.setTotalImacDevices(endUserRevenueInfoDto.getTotalImacDevices() == null ? 0
							: endUserRevenueInfoDto.getTotalImacDevices());
					endUserRevenueInfo.setBenchMarkType(endUserRevenueInfoDto.getBenchMarkType());
					endUserRevenueInfoList.add(endUserRevenueInfo);
				}
			} else {
				for (EndUserRevenueInfoDto revenueInfoDto : endUserYearlyInfoDto
						.getEndUserRevenueInfoDtoList()) {
					EndUserRevenueInfo revenueInfo = new EndUserRevenueInfo();
					revenueInfo.setTotalEndUserDevices(revenueInfoDto.getTotalEndUserDevices() == null ? 0
							: revenueInfoDto.getTotalEndUserDevices());
					revenueInfo.setTotalImacDevices(revenueInfoDto.getTotalImacDevices() == null ? 0
							: revenueInfoDto.getTotalImacDevices());
					revenueInfo.setBenchMarkType(revenueInfoDto.getBenchMarkType());
					
					revenueInfo.setEndUserYearlyDataInfo(endUserYearlyDataInfo);
					endUserRevenueInfoList.add(revenueInfo);
				}
			}
			endUserYearlyDataInfo.setEndUserRevenueInfoList(endUserRevenueInfoList);
		}

		endUserYearlyDataInfoList.add(endUserYearlyDataInfo);

		return endUserYearlyDataInfoList;
	}

	private void setUnitPrices(EndUserUnitPriceInfo endUserUnitPriceInfo,
			EndUserUnitPriceInfoDto endUserUnitPriceInfoDto) {
		endUserUnitPriceInfo.setBenchMarkType(endUserUnitPriceInfoDto.getBenchmarkType());
		endUserUnitPriceInfo.setEndUserDevices(endUserUnitPriceInfoDto.getEndUserDevices() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getEndUserDevices());
		endUserUnitPriceInfo.setLaptops(endUserUnitPriceInfoDto.getLaptops() == null ? BigDecimal.ZERO
				: endUserUnitPriceInfoDto.getLaptops());
		endUserUnitPriceInfo.setHighEndLaptops(endUserUnitPriceInfoDto.getHighEndLaptops() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getHighEndLaptops());
		endUserUnitPriceInfo.setStandardLaptops(endUserUnitPriceInfoDto.getStandardLaptops() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getStandardLaptops());
		endUserUnitPriceInfo.setDesktops(endUserUnitPriceInfoDto.getDesktops() == null ? BigDecimal.ZERO
				: endUserUnitPriceInfoDto.getDesktops());
		endUserUnitPriceInfo.setThinClients(endUserUnitPriceInfoDto.getThinClients() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getThinClients());
		endUserUnitPriceInfo.setMobileDevices(endUserUnitPriceInfoDto.getMobileDevices() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getMobileDevices());
		endUserUnitPriceInfo.setImacDevices(endUserUnitPriceInfoDto.getImacDevices() == null
				? BigDecimal.ZERO : endUserUnitPriceInfoDto.getImacDevices());
	}

	public EndUserInfoDto prepareEndUserInfoDto(EndUserInfo endUserInfo) {
		EndUserInfoDto endUserInfoDto = new EndUserInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if (endUserInfo != null) {
			endUserInfoDto = modelMapper.map(endUserInfo, EndUserInfoDto.class);
			endUserInfoDto.setDealId(endUserInfo.getDealInfo().getDealId());

			final List<EndUserYearlyDataInfoDto> yearlyDataInfoDtoList = new ArrayList<>();
			for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
				final EndUserYearlyDataInfoDto yearlyDataInfoDto = modelMapper.map(yearlyDataInfo,
						EndUserYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserUnitPriceInfoList())) {
					final List<EndUserUnitPriceInfoDto> unitPriceInfoDtoList = new ArrayList<>();
					for (EndUserUnitPriceInfo unitPriceInfo : yearlyDataInfo.getEndUserUnitPriceInfoList()) {
						final EndUserUnitPriceInfoDto unitPriceInfoDto = modelMapper.map(unitPriceInfo,
								EndUserUnitPriceInfoDto.class);
						unitPriceInfoDtoList.add(unitPriceInfoDto);
					}
					yearlyDataInfoDto.setEndUserUnitPriceInfoDtoList(unitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserRevenueInfoList())) {
					final List<EndUserRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
					for (EndUserRevenueInfo revenueInfo : yearlyDataInfo.getEndUserRevenueInfoList()) {
						final EndUserRevenueInfoDto revenueInfoDto = modelMapper.map(revenueInfo,
								EndUserRevenueInfoDto.class);
						revenueInfoDtoList.add(revenueInfoDto);
					}
					yearlyDataInfoDto.setEndUserRevenueInfoDtoList(revenueInfoDtoList);
				}
				yearlyDataInfoDtoList.add(yearlyDataInfoDto);
			}

			endUserInfoDto.setEndUserYearlyDataInfoDtoList(yearlyDataInfoDtoList);
		}

		return endUserInfoDto;
	}

	public EndUserInfo prepareEndUserPrice(EndUserInfo endUserInfo, List<EndUserPriceDto> endUserPriceDtoList) {
		List<EndUserYearlyDataInfo> yearlyDataInfoList = new ArrayList<>();
		EndUserPriceDto endUserPriceDto = new EndUserPriceDto();
		for (EndUserYearlyDataInfo yearlydataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
			for (EndUserPriceDto priceDto : endUserPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					endUserPriceDto = priceDto;
					break;
				}
			}
			yearlyDataInfoList = updateEndUserYearlyDetails(yearlyDataInfoList, yearlydataInfo, endUserPriceDto);
		}

		endUserInfo.setEndUserYearlyDataInfoList(yearlyDataInfoList);

		return endUserInfo;
	}

	private List<EndUserYearlyDataInfo> updateEndUserYearlyDetails(List<EndUserYearlyDataInfo> yearlyDataInfoList,
			EndUserYearlyDataInfo yearlydataInfo, EndUserPriceDto endUserPriceDto) {
		if (!CollectionUtils.isEmpty(yearlydataInfo.getEndUserUnitPriceInfoList())) {
			// Update the existing unit prices
			for (EndUserUnitPriceInfo unitPriceInfo : yearlydataInfo.getEndUserUnitPriceInfoList()) {
				unitPriceInfo.setEndUserDevices(endUserPriceDto.getTotalEndUserUnitPrice());
				unitPriceInfo.setLaptops(endUserPriceDto.getTotalLaptopUnitPrice());
				unitPriceInfo.setHighEndLaptops(endUserPriceDto.getTotalHighEndLaptopUnitPrice());
				unitPriceInfo.setStandardLaptops(endUserPriceDto.getTotalStandardLaptopUnitPrice());
				unitPriceInfo.setDesktops(endUserPriceDto.getTotalDesktopUnitPrice());
				unitPriceInfo.setThinClients(endUserPriceDto.getTotalThinClientUnitPrice());
				unitPriceInfo.setMobileDevices(endUserPriceDto.getTotalMobileUnitPrice());
				unitPriceInfo.setImacDevices(endUserPriceDto.getTotalImacUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<EndUserUnitPriceInfo> unitPriceInfoList = new ArrayList<>();
			EndUserUnitPriceInfo unitPriceInfo = new EndUserUnitPriceInfo();
			unitPriceInfo.setEndUserDevices(endUserPriceDto.getTotalEndUserUnitPrice());
			unitPriceInfo.setLaptops(endUserPriceDto.getTotalLaptopUnitPrice());
			unitPriceInfo.setHighEndLaptops(endUserPriceDto.getTotalHighEndLaptopUnitPrice());
			unitPriceInfo.setStandardLaptops(endUserPriceDto.getTotalStandardLaptopUnitPrice());
			unitPriceInfo.setDesktops(endUserPriceDto.getTotalDesktopUnitPrice());
			unitPriceInfo.setThinClients(endUserPriceDto.getTotalThinClientUnitPrice());
			unitPriceInfo.setMobileDevices(endUserPriceDto.getTotalMobileUnitPrice());
			unitPriceInfo.setImacDevices(endUserPriceDto.getTotalImacUnitPrice());
			unitPriceInfo.setEndUserYearlyDataInfo(yearlydataInfo);

			unitPriceInfoList.add(unitPriceInfo);
			yearlydataInfo.setEndUserUnitPriceInfoList(unitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(yearlydataInfo.getEndUserRevenueInfoList())) {
			// Update the existing revenues
			for (EndUserRevenueInfo revenueInfo : yearlydataInfo.getEndUserRevenueInfoList()) {
				revenueInfo.setTotalEndUserDevices(endUserPriceDto.getTotalEndUserRevenue());
				revenueInfo.setTotalImacDevices(endUserPriceDto.getTotalImacRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<EndUserRevenueInfo> revenueInfoList = new ArrayList<>();
			EndUserRevenueInfo revenueInfo = new EndUserRevenueInfo();
			revenueInfo.setTotalEndUserDevices(endUserPriceDto.getTotalEndUserRevenue());
			revenueInfo.setTotalImacDevices(endUserPriceDto.getTotalImacRevenue());

			revenueInfo.setEndUserYearlyDataInfo(yearlydataInfo);

			revenueInfoList.add(revenueInfo);
			yearlydataInfo.setEndUserRevenueInfoList(revenueInfoList);
		}

		yearlyDataInfoList.add(yearlydataInfo);

		return yearlyDataInfoList;
	}

	public EndUserInfo prepareSolutionCriteria(EndUserInfo endUserInfo, SolutionCriteriaDto solutionDto) {
		endUserInfo.setOffshoreAllowed(solutionDto.isOffshoreAllowed());
		endUserInfo.setIncludeBreakFix(solutionDto.isIncludeBreakFix());
		endUserInfo.setIncludeHardware(solutionDto.isIncludeHardware());
		endUserInfo.setResolutionTime(solutionDto.getResolutionTime());
		return endUserInfo;
	}

	/**
	 * @param migrationCostList
	 * @return
	 */
	public List<EndUserMigrationCostInfoDto> convertMigrationCostInfoToDto(
			List<EndUserMigrationCostInfo> migrationCostList) {
		List<EndUserMigrationCostInfoDto> costDtoList = new ArrayList<>();
		EndUserMigrationCostInfoDto dto = null;
		for (int i = 0; i < migrationCostList.size(); i++) {
			dto = new EndUserMigrationCostInfoDto();
			dto.setLower(migrationCostList.get(i).getLower());
			dto.setUpper(migrationCostList.get(i).getUpper());
			dto.setCost(migrationCostList.get(i).getCost());
			if (i == 0) {
				dto.setDifference(migrationCostList.get(i).getUpper());
			} else {
				dto.setDifference(
						migrationCostList.get(i).getUpper().subtract(migrationCostList.get(i - 1).getUpper()));
			}
			costDtoList.add(dto);
		}
		return costDtoList;
	}

}
