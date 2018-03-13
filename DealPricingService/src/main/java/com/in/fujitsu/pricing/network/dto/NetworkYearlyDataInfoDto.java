package com.in.fujitsu.pricing.network.dto;

import java.io.Serializable;
import java.util.List;

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
public class NetworkYearlyDataInfoDto implements Serializable {

	private static final long serialVersionUID = 2561282103109882579L;

	private Long yearId;
	private Integer wanDevices;
	private Integer smallWanDevices;
	private Integer mediumWanDevices;
	private Integer largeWanDevices;
	private Integer lanDevices;
	private Integer smallLanDevices;
	private Integer mediumLanDevices;
	private Integer largeLanDevices;
	private Integer wlanControllers;
	private Integer wlanAccesspoint;
	private Integer loadBalancers;
	private Integer vpnIpSec;
	private Integer dnsDhcpService;
	private Integer firewalls;
	private Integer proxies;
	private Integer year;
	private List<NetworkUnitPriceInfoDto> networkUnitPriceInfoDtoList;
	private List<NetworkRevenueInfoDto> networkRevenueInfoDtoList;

}
