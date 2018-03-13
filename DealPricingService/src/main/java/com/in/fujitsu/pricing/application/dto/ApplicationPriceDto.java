package com.in.fujitsu.pricing.application.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class ApplicationPriceDto {

	private int year;
	private BigDecimal totalAppsUnitPrice;
	private BigDecimal simpleAppsUnitPrice;
	private BigDecimal mediumAppsUnitPrice;
	private BigDecimal complexAppsUnitPrice;
	private BigDecimal veryComplexAppsUnitPrice;

	private Integer totalAppsRevenue;

}
