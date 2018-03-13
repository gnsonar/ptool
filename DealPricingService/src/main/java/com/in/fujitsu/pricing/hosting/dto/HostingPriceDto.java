
package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class HostingPriceDto implements Serializable {

	private static final long serialVersionUID = 5804567475602170919L;

	private int year;

	private BigDecimal servers;
	private BigDecimal physical;
	private BigDecimal physicalWin;
	private BigDecimal physicalWinSmall;
	private BigDecimal physicalWinMedium;
	private BigDecimal physicalWinLarge;
	private BigDecimal physicalUnix;
	private BigDecimal physicalUnixSmall;
	private BigDecimal physicalUnixMedium;
	private BigDecimal physicalUnixLarge;
	private BigDecimal virtual;
	private BigDecimal virtualPublic;
	private BigDecimal virtualPublicWin;
	private BigDecimal virtualPublicWinSmall;
	private BigDecimal virtualPublicWinMedium;
	private BigDecimal virtualPublicWinLarge;
	private BigDecimal virtualPublicUnix;
	private BigDecimal virtualPublicUnixSmall;
	private BigDecimal virtualPublicUnixMedium;
	private BigDecimal virtualPublicUnixLarge;
	private BigDecimal virtualPrivate;
	private BigDecimal virtualPrivateWin;
	private BigDecimal virtualPrivateWinSmall;
	private BigDecimal virtualPrivateWinMedium;
	private BigDecimal virtualPrivateWinLarge;
	private BigDecimal virtualPrivateUnix;
	private BigDecimal virtualPrivateUnixSmall;
	private BigDecimal virtualPrivateUnixMedium;
	private BigDecimal virtualPrivateUnixLarge;
	private BigDecimal sqlInstances;
	private BigDecimal cotsInstallations;

	private Integer serversRevenue;
	private Integer sqlInstancesRevenue;
	private Integer cotsInstallationsRevenue;

}
