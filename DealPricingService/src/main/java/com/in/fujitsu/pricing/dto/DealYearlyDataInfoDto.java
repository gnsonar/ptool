package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DealYearlyDataInfoDto {

	private Long id;
	private Integer noOfUsers;
	private Integer noOfSites;
	private Integer noOfDatacenters;
	private Integer year;

}
