package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HostingRevenueDto implements Serializable {

	private static final long serialVersionUID = 2366179450862049528L;

	private HostingCalculateDto servers;
	private HostingCalculateDto physical;
	private HostingCalculateDto physicalWin;
	private HostingCalculateDto physicalWinSmall;
	private HostingCalculateDto physicalWinMedium;
	private HostingCalculateDto physicalWinLarge;
	private HostingCalculateDto physicalUnix;
	private HostingCalculateDto physicalUnixSmall;
	private HostingCalculateDto physicalUnixMedium;
	private HostingCalculateDto physicalUnixLarge;
	private HostingCalculateDto virtual;
	private HostingCalculateDto virtualPublic;
	private HostingCalculateDto virtualPublicWin;
	private HostingCalculateDto virtualPublicWinSmall;
	private HostingCalculateDto virtualPublicWinMedium;
	private HostingCalculateDto virtualPublicWinLarge;
	private HostingCalculateDto virtualPublicUnix;
	private HostingCalculateDto virtualPublicUnixSmall;
	private HostingCalculateDto virtualPublicUnixMedium;
	private HostingCalculateDto virtualPublicUnixLarge;
	private HostingCalculateDto virtualPrivate;
	private HostingCalculateDto virtualPrivateWin;
	private HostingCalculateDto virtualPrivateWinSmall;
	private HostingCalculateDto virtualPrivateWinMedium;
	private HostingCalculateDto virtualPrivateWinLarge;
	private HostingCalculateDto virtualPrivateUnix;
	private HostingCalculateDto virtualPrivateUnixSmall;
	private HostingCalculateDto virtualPrivateUnixMedium;
	private HostingCalculateDto virtualPrivateUnixLarge;
	private HostingCalculateDto sqlInstances;
	private HostingCalculateDto cotsInstallations;

}
