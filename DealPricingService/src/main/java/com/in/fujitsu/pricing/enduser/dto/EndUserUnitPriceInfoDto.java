package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class EndUserUnitPriceInfoDto implements Serializable {

	private static final long serialVersionUID = 3005929296788774772L;

	private long id;
	private BigDecimal endUserDevices;
	private BigDecimal laptops;
	private BigDecimal highEndLaptops;
	private BigDecimal standardLaptops;
	private BigDecimal desktops;
	private BigDecimal thinClients;
	private BigDecimal mobileDevices;
	private BigDecimal imacDevices;
	private String benchmarkType;

}
