package com.in.fujitsu.pricing.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class DealResultDto {

	private Long dealId;
	private String dealName;
	private String clientName;
	private Integer dealTerm;
	private String financialEngineer;
	private String dealStatus;
	private BigDecimal averageVolume;
	private BigDecimal averagePrice;
	private BigDecimal averageTargetPrice;

}
