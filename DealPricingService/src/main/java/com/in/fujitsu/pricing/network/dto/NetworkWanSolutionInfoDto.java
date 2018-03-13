package com.in.fujitsu.pricing.network.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NetworkWanSolutionInfoDto {

	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	private float smallperc;
	private float mediumPerc;
	private float largePerc;

}
