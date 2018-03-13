
package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EndUserPriceDto implements Serializable {

	private static final long serialVersionUID = -8637323796759219463L;

	private int year;

	private BigDecimal totalEndUserUnitPrice;
	private BigDecimal totalLaptopUnitPrice;
	private BigDecimal totalHighEndLaptopUnitPrice;
	private BigDecimal totalStandardLaptopUnitPrice;
	private BigDecimal totalDesktopUnitPrice;
	private BigDecimal totalThinClientUnitPrice;
	private BigDecimal totalMobileUnitPrice;
	private BigDecimal totalImacUnitPrice;

	private Integer totalEndUserRevenue;
	private Integer totalImacRevenue;

}
