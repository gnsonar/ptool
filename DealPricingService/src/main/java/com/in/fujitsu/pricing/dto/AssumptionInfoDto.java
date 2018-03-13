package com.in.fujitsu.pricing.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class AssumptionInfoDto extends TimestampDto {

	private int id;
	private String towerName;
	private String status;

	List<AssumptionDetailDto> assumptionDetailsList = new ArrayList<AssumptionDetailDto>();

}
