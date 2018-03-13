package com.in.fujitsu.pricing.enduser.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EndUserSolutionInfoDto {

	private Integer solutionId;

	private String solutionName;

	private String solutionDesc;

	private float laptopPerc;

	private float highEndLaptopPerc;

	private float standardLaptopPerc;

	private float desktopPerc;

	private float thinClientPerc;

	private float mobilePerc;

}
