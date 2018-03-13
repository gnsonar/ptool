package com.in.fujitsu.pricing.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class MigrationCostDto {

	private int year;
	private BigDecimal cost;

}
