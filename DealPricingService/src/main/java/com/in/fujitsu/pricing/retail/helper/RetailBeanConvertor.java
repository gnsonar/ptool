package com.in.fujitsu.pricing.retail.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.retail.dto.RetailEquipmentAgeInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailEquipmentSetInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailPriceDto;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailSolutionsDto;
import com.in.fujitsu.pricing.retail.dto.RetailUnitPriceInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailYearlyDataInfoDto;
import com.in.fujitsu.pricing.retail.entity.RetailEquipmentAgeInfo;
import com.in.fujitsu.pricing.retail.entity.RetailEquipmentSetInfo;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailRevenueInfo;
import com.in.fujitsu.pricing.retail.entity.RetailSolutionsInfo;
import com.in.fujitsu.pricing.retail.entity.RetailUnitPriceInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;
import com.in.fujitsu.pricing.retail.repository.RetailYearlyRepository;

/**
 * @author mishrasub
 *
 */
@Component
public class RetailBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private RetailYearlyRepository retailYearlyRepository;

	/**
	 * @param retailInfo
	 * @return
	 */
	public RetailInfoDto prepareRetailInfoDto(RetailInfo retailInfo) {
		RetailInfoDto retailInfoDto = new RetailInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if (retailInfo != null) {
			retailInfoDto = modelMapper.map(retailInfo, RetailInfoDto.class);
			retailInfoDto.setDealId(retailInfo.getDealInfo().getDealId());

			final List<RetailYearlyDataInfoDto> retailYearlyDataInfoDtoList = new ArrayList<>();
			for (RetailYearlyDataInfo retailYearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
				final RetailYearlyDataInfoDto retailYearlyDataInfoDto = modelMapper.map(retailYearlyDataInfo,
						RetailYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailUnitPriceInfoList())) {
					final List<RetailUnitPriceInfoDto> retailUnitPriceInfoDtoList = new ArrayList<>();
					for (RetailUnitPriceInfo retailUnitPriceInfo : retailYearlyDataInfo.getRetailUnitPriceInfoList()) {
						final RetailUnitPriceInfoDto retailUnitPriceInfoDto = modelMapper.map(retailUnitPriceInfo,
								RetailUnitPriceInfoDto.class);
						retailUnitPriceInfoDtoList.add(retailUnitPriceInfoDto);
					}
					retailYearlyDataInfoDto.setRetailUnitPriceInfoDtoList(retailUnitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailRevenueInfoList())) {
					final List<RetailRevenueInfoDto> retailRevenueInfoDtoList = new ArrayList<>();
					for (RetailRevenueInfo retailRevenueInfo : retailYearlyDataInfo.getRetailRevenueInfoList()) {
						final RetailRevenueInfoDto retailRevenueInfoDto = modelMapper.map(retailRevenueInfo,
								RetailRevenueInfoDto.class);
						retailRevenueInfoDtoList.add(retailRevenueInfoDto);
					}
					retailYearlyDataInfoDto.setRetailRevenueInfoDtoList(retailRevenueInfoDtoList);
				}
				retailYearlyDataInfoDtoList.add(retailYearlyDataInfoDto);
			}

			retailInfoDto.setRetailYearlyDataInfoDtoList(retailYearlyDataInfoDtoList);
		}

		return retailInfoDto;
	}

	/**
	 * @param retailInfo
	 * @param retailInfoDto
	 * @param isSave
	 * @return
	 */
	public RetailInfo prepareRetailInfo(RetailInfo retailInfo, RetailInfoDto retailInfoDto, boolean isSave) {
		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(retailInfoDto.getDealId());
			retailInfo.setDealInfo(dealInfo);
		}

		retailInfo.setOffshoreAllowed(retailInfoDto.isOffshoreAllowed());
		retailInfo.setIncludeHardware(retailInfoDto.isIncludeHardware());
		retailInfo.setEquipmentAge(retailInfoDto.getEquipmentAge());
		retailInfo.setEquipmentSet(retailInfoDto.getEquipmentSet());
		retailInfo.setTowerArchitect(retailInfoDto.getTowerArchitect() !=null ?  retailInfoDto.getTowerArchitect() : retailInfo.getTowerArchitect());
		retailInfo.setLevelIndicator(retailInfoDto.getLevelIndicator());

		List<RetailYearlyDataInfo> retailYearlyDataInfoList = new ArrayList<>();

		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(retailInfoDto.getRetailYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = retailInfoDto.getRetailYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				RetailYearlyDataInfoDto retailYearlyDataInfoDto = retailInfoDto.getRetailYearlyDataInfoDtoList().get(i);
				RetailYearlyDataInfo retailYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(retailInfo.getRetailYearlyDataInfoList())
						&& retailInfo.getRetailYearlyDataInfoList().size() - 1 >= i)) {
					for (int j = 0; j < retailInfo.getRetailYearlyDataInfoList().size(); j++) {
						RetailYearlyDataInfo existingRetailYearlyDataInfo = retailInfo.getRetailYearlyDataInfoList()
								.get(j);
						if (existingRetailYearlyDataInfo.getYear().equals(retailYearlyDataInfoDto.getYear())) {
							retailYearlyDataInfo = existingRetailYearlyDataInfo;
							break;
						}
					}
				} else {
					retailYearlyDataInfo = new RetailYearlyDataInfo();
				}
				retailYearlyDataInfoList = setRetailYearlyDetails(retailYearlyDataInfoList, retailInfo,
						retailYearlyDataInfo, retailYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(retailInfo.getRetailYearlyDataInfoList())
					&& yearlyDtoListSize < retailInfo.getRetailYearlyDataInfoList().size()) {
				for (int i = retailInfo.getRetailYearlyDataInfoList().size() - 1; i >= yearlyDtoListSize; i--) {
					retailYearlyRepository.delete(retailInfo.getRetailYearlyDataInfoList().get(i).getYearId());
				}
			}
		}

		retailInfo.setRetailYearlyDataInfoList(retailYearlyDataInfoList);
		return retailInfo;
	}

	/**
	 * @param retailYearlyDataInfoList
	 * @param retailInfo
	 * @param retailYearlyDataInfo
	 * @param retailYearlyDataInfoDto
	 * @return
	 */
	private List<RetailYearlyDataInfo> setRetailYearlyDetails(List<RetailYearlyDataInfo> retailYearlyDataInfoList,
			RetailInfo retailInfo, RetailYearlyDataInfo retailYearlyDataInfo,
			RetailYearlyDataInfoDto retailYearlyDataInfoDto) {

		retailYearlyDataInfo.setNoOfShops(
				retailYearlyDataInfoDto.getNoOfShops() == null ? 0 : retailYearlyDataInfoDto.getNoOfShops());
		retailYearlyDataInfo.setYear(retailYearlyDataInfoDto.getYear());

		retailYearlyDataInfo.setRetailInfo(retailInfo);

		// set the Unit Price
		List<RetailUnitPriceInfo> retailUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(retailYearlyDataInfoDto.getRetailUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailUnitPriceInfoList())) {
				for (RetailUnitPriceInfo retailUnitPriceInfo : retailYearlyDataInfo.getRetailUnitPriceInfoList()) {
					RetailUnitPriceInfoDto retailUnitPriceInfoDto = retailYearlyDataInfoDto
							.getRetailUnitPriceInfoDtoList().get(0);
					if (retailUnitPriceInfoDto != null) {
						retailUnitPriceInfo.setNoOfShops(retailUnitPriceInfoDto.getNoOfShops() == null ? BigDecimal.ZERO
								: retailUnitPriceInfoDto.getNoOfShops());
						retailUnitPriceInfoList.add(retailUnitPriceInfo);
					}
				}
			} else {
				for (RetailUnitPriceInfoDto retailUnitPriceInfoDto : retailYearlyDataInfoDto
						.getRetailUnitPriceInfoDtoList()) {
					RetailUnitPriceInfo retailUnitPriceInfo = new RetailUnitPriceInfo();
					retailUnitPriceInfo.setNoOfShops(retailUnitPriceInfoDto.getNoOfShops() == null ? BigDecimal.ZERO
							: retailUnitPriceInfoDto.getNoOfShops());
					
					retailUnitPriceInfo.setRetailYearlyDataInfo(retailYearlyDataInfo);
					retailUnitPriceInfoList.add(retailUnitPriceInfo);
				}
			}
			retailYearlyDataInfo.setRetailUnitPriceInfoList(retailUnitPriceInfoList);
		}

		// set the Revenue
		List<RetailRevenueInfo> retailRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(retailYearlyDataInfoDto.getRetailRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailRevenueInfoList())) {
				for (RetailRevenueInfo retailRevenueInfo : retailYearlyDataInfo.getRetailRevenueInfoList()) {
					RetailRevenueInfoDto retailRevenueInfoDto = retailYearlyDataInfoDto.getRetailRevenueInfoDtoList()
							.get(0);
					retailRevenueInfo.setNoOfShops(retailRevenueInfoDto.getNoOfShops() == null ? 0
							: retailRevenueInfoDto.getNoOfShops().intValue());
					retailRevenueInfoList.add(retailRevenueInfo);
				}
			} else {
				for (RetailRevenueInfoDto retailRevenueInfoDto : retailYearlyDataInfoDto
						.getRetailRevenueInfoDtoList()) {
					RetailRevenueInfo retailRevenueInfo = new RetailRevenueInfo();
					retailRevenueInfo.setNoOfShops(retailRevenueInfoDto.getNoOfShops() == null ? 0
							: retailRevenueInfoDto.getNoOfShops().intValue());
					
					retailRevenueInfo.setRetailYearlyDataInfo(retailYearlyDataInfo);
					retailRevenueInfoList.add(retailRevenueInfo);
				}
			}
			retailYearlyDataInfo.setRetailRevenueInfoList(retailRevenueInfoList);
		}

		retailYearlyDataInfoList.add(retailYearlyDataInfo);

		return retailYearlyDataInfoList;

	}

	/**
	 * @param retailInfo
	 * @param retailPriceDtoList
	 * @return
	 */
	public RetailInfo prepareRetailPrice(RetailInfo retailInfo, List<RetailPriceDto> retailPriceDtoList) {
		List<RetailYearlyDataInfo> retailYearlyDataInfoList = new ArrayList<>();
		RetailPriceDto retailPriceDto = new RetailPriceDto();
		for (RetailYearlyDataInfo yearlydataInfo : retailInfo.getRetailYearlyDataInfoList()) {
			for (RetailPriceDto priceDto : retailPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					retailPriceDto = priceDto;
					break;
				}
			}
			retailYearlyDataInfoList = updateRetailYearlyDetails(retailYearlyDataInfoList, yearlydataInfo,
					retailPriceDto);
		}

		retailInfo.setRetailYearlyDataInfoList(retailYearlyDataInfoList);

		return retailInfo;
	}

	/**
	 * @param retailYearlyDataInfoList
	 * @param yearlydataInfo
	 * @param retailPriceDto
	 * @return
	 */
	private List<RetailYearlyDataInfo> updateRetailYearlyDetails(List<RetailYearlyDataInfo> retailYearlyDataInfoList,
			RetailYearlyDataInfo yearlydataInfo, RetailPriceDto retailPriceDto) {

		if (!CollectionUtils.isEmpty(yearlydataInfo.getRetailUnitPriceInfoList())) {
			// Update the existing unit prices
			for (RetailUnitPriceInfo retailUnitPriceInfo : yearlydataInfo.getRetailUnitPriceInfoList()) {
				retailUnitPriceInfo.setNoOfShops(retailPriceDto.getNoOfShopsUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<RetailUnitPriceInfo> retailUnitPriceInfoList = new ArrayList<>();
			RetailUnitPriceInfo retailUnitPriceInfo = new RetailUnitPriceInfo();
			retailUnitPriceInfo.setNoOfShops(retailPriceDto.getNoOfShopsUnitPrice());
			retailUnitPriceInfo.setRetailYearlyDataInfo(yearlydataInfo);

			retailUnitPriceInfoList.add(retailUnitPriceInfo);
			yearlydataInfo.setRetailUnitPriceInfoList(retailUnitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(yearlydataInfo.getRetailRevenueInfoList())) {
			// Update the existing revenues
			for (RetailRevenueInfo retailRevenueInfo : yearlydataInfo.getRetailRevenueInfoList()) {
				retailRevenueInfo.setNoOfShops(retailPriceDto.getNoOfShopsRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<RetailRevenueInfo> retailRevenueInfoList = new ArrayList<>();
			RetailRevenueInfo retailRevenueInfo = new RetailRevenueInfo();
			retailRevenueInfo.setNoOfShops(retailPriceDto.getNoOfShopsRevenue());
			retailRevenueInfo.setRetailYearlyDataInfo(yearlydataInfo);

			retailRevenueInfoList.add(retailRevenueInfo);
			yearlydataInfo.setRetailRevenueInfoList(retailRevenueInfoList);
		}

		retailYearlyDataInfoList.add(yearlydataInfo);

		return retailYearlyDataInfoList;
	}

	/**
	 * @param retailInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public RetailInfo prepareSolutionCriteria(RetailInfo retailInfo, SolutionCriteriaDto solutionCriteriaDto) {
		retailInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		retailInfo.setIncludeHardware(solutionCriteriaDto.isIncludeHardware());
		retailInfo.setEquipmentAge(solutionCriteriaDto.getEquipmentAge());
		retailInfo.setEquipmentSet(solutionCriteriaDto.getEquipmentSet());

		return retailInfo;
	}

	/**
	 * @param retailSolutionsInfoList
	 * @return
	 */
	public List<RetailSolutionsDto> prepareRetailSolutionsDtoList(List<RetailSolutionsInfo> retailSolutionsInfoList) {
		final List<RetailSolutionsDto> solutionsDtoList = new ArrayList<>();
		for (RetailSolutionsInfo solutionsInfo : retailSolutionsInfoList) {
			final RetailSolutionsDto solutionsDto = modelMapper.map(solutionsInfo, RetailSolutionsDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param equipAgeList
	 * @return
	 */
	public List<RetailEquipmentAgeInfoDto> prepareEquipmentAgeDtoList(List<RetailEquipmentAgeInfo> equipAgeList) {
		final List<RetailEquipmentAgeInfoDto> equipAgeDtoList = new ArrayList<>();
		for (RetailEquipmentAgeInfo equipAgeInfo : equipAgeList) {
			final RetailEquipmentAgeInfoDto equipAgeDto = modelMapper.map(equipAgeInfo, RetailEquipmentAgeInfoDto.class);
			equipAgeDtoList.add(equipAgeDto);
		}
		return equipAgeDtoList;
	}

	/**
	 * @param equipSetList
	 * @return
	 */
	public List<RetailEquipmentSetInfoDto> prepareEquipmentSetDtoList(List<RetailEquipmentSetInfo> equipSetList) {
		final List<RetailEquipmentSetInfoDto> equipSetDtoList = new ArrayList<>();
		for (RetailEquipmentSetInfo equipSetInfo : equipSetList) {
			final RetailEquipmentSetInfoDto equipSetDto = modelMapper.map(equipSetInfo, RetailEquipmentSetInfoDto.class);
			equipSetDtoList.add(equipSetDto);
		}
		return equipSetDtoList;
	}

}
