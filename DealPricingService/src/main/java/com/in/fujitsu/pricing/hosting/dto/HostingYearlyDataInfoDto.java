package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HostingYearlyDataInfoDto implements Serializable, Cloneable {

	private static final long serialVersionUID = -1414398922139428162L;

	private Integer year;
	private Integer servers;
	private Integer physical;
	private Integer physicalWin;
	private Integer physicalWinSmall;
	private Integer physicalWinMedium;
	private Integer physicalWinLarge;
	private Integer physicalUnix;
	private Integer physicalUnixSmall;
	private Integer physicalUnixMedium;
	private Integer physicalUnixLarge;
	private Integer virtual;
	private Integer virtualPublic;
	private Integer virtualPublicWin;
	private Integer virtualPublicWinSmall;
	private Integer virtualPublicWinMedium;
	private Integer virtualPublicWinLarge;
	private Integer virtualPublicUnix;
	private Integer virtualPublicUnixSmall;
	private Integer virtualPublicUnixMedium;
	private Integer virtualPublicUnixLarge;
	private Integer virtualPrivate;
	private Integer virtualPrivateWin;
	private Integer virtualPrivateWinSmall;
	private Integer virtualPrivateWinMedium;
	private Integer virtualPrivateWinLarge;
	private Integer virtualPrivateUnix;
	private Integer virtualPrivateUnixSmall;
	private Integer virtualPrivateUnixMedium;
	private Integer virtualPrivateUnixLarge;
	private Integer sqlInstances;
	private Integer cotsInstallations;
	private List<HostingUnitPriceInfoDto> hostingUnitPriceInfoDtoList;
	private List<HostingRevenueInfoDto> hostingRevenueInfoDtoList;

}
