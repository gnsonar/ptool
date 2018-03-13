package com.in.fujitsu.pricing.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.AssumptionDetailDto;
import com.in.fujitsu.pricing.dto.AssumptionInfoDto;
import com.in.fujitsu.pricing.dto.AssumptionResponse;
import com.in.fujitsu.pricing.entity.AssumptionInfo;
import com.in.fujitsu.pricing.repository.AssumptionRepository;
import com.in.fujitsu.pricing.utility.CommonModelConvertor;

@Service
public class AssumptionService {

	@Autowired
	private AssumptionRepository assumptionRepository;

	public AssumptionResponse getAssumptionDetails() {
		 List<AssumptionInfo> assumptionInfoList =  assumptionRepository.findAll();

		 List<AssumptionInfoDto> assumptionInfoDtoList = CommonModelConvertor.prepareAssumptionDTOList(assumptionInfoList);
		 AssumptionResponse assumptionResponse = new AssumptionResponse();
		 Map<String, List<AssumptionDetailDto>> assumptions = new HashMap<>();
		 for(AssumptionInfoDto dto : assumptionInfoDtoList) {
			 assumptions.put(dto.getTowerName(), dto.getAssumptionDetailsList());
		 }
		 assumptionResponse.setAssumptions(assumptions);
		 return assumptionResponse;
	}
}
