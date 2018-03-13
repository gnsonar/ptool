package com.in.fujitsu.pricing.utility;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.in.fujitsu.pricing.dto.AssumptionInfoDto;
import com.in.fujitsu.pricing.dto.FJEmployeeDto;
import com.in.fujitsu.pricing.entity.AssumptionInfo;

public class CommonModelConvertor {

	private static ModelMapper modelMapper = new ModelMapper();

	public static FJEmployeeDto prepareFJEmployeeDto(FJEmployee employee) {
		return modelMapper.map(employee, FJEmployeeDto.class);
	}

	public static List<AssumptionInfoDto> prepareAssumptionDTOList(List<AssumptionInfo> assumptionInfoList) {
		List<AssumptionInfoDto> assumptionInfoDtoList = new ArrayList<>();

		for (AssumptionInfo assumptionInfo : assumptionInfoList) {
			AssumptionInfoDto assumptionInfoDto = modelMapper.map(assumptionInfo, AssumptionInfoDto.class);
			assumptionInfoDtoList.add(assumptionInfoDto);
		}
		return assumptionInfoDtoList;
	}

}
