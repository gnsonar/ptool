package com.in.fujitsu.pricing.network.dto;

import java.io.Serializable;

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
public class NetworkRevenueDto implements Serializable {

	private static final long serialVersionUID = 2366179450862049528L;

	private NetworkCalculateDto wanDevicesCalculateDto;
	private NetworkCalculateDto smallWanDevicesCalculateDto;
	private NetworkCalculateDto mediumWanDevicesCalculateDto;
	private NetworkCalculateDto largeWanDevicesCalculateDto;
	private NetworkCalculateDto lanDevicesCalculateDto;
	private NetworkCalculateDto smallLanDevicesCalculateDto;
	private NetworkCalculateDto mediumLanDevicesCalculateDto;
	private NetworkCalculateDto largeLanDevicesCalculateDto;
	private NetworkCalculateDto wlanControllersCalculateDto;
	private NetworkCalculateDto wlanAccesspointCalculateDto;
	private NetworkCalculateDto loadBalancersCalculateDto;
	private NetworkCalculateDto vpnIpSecCalculateDto;
	private NetworkCalculateDto dnsDhcpServiceCalculateDto;
	private NetworkCalculateDto firewallsCalculateDto;
	private NetworkCalculateDto proxiesCalculateDto;

}
