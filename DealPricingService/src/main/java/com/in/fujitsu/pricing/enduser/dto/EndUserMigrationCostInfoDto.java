package com.in.fujitsu.pricing.enduser.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class EndUserMigrationCostInfoDto {

	private BigDecimal lower;
	private BigDecimal upper;
	private BigDecimal cost;
	private BigDecimal difference;

}
