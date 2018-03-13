
package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationUnitPriceInfoDto implements Serializable {

	private static final long serialVersionUID = -1160552172750323501L;

	private long id;
	private BigDecimal totalAppsUnitPrice;
	private BigDecimal simpleAppsUnitPrice;
	private BigDecimal mediumAppsUnitPrice;
	private BigDecimal complexAppsUnitPrice;
	private BigDecimal veryComplexAppsUnitPrice;

	// Low or Target in case of Benchmark deal
	private String benchMarkType;

}
