package com.in.fujitsu.pricing.network.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NetworkYearlyCalculateDto {

	private int year;
	private String dealType;
	private String benchmarkType;

	private float wanDetailAvgUnitPrice;
	private float smallWanDetailAvgUnitPrice;
	private float mediumWanDetailAvgUnitPrice;
	private float largewanDetailAvgUnitPrice;
	private float lanDetailAvgUnitPrice;
	private float smalllanDetailAvgUnitPrice;
	private float mediumlanDetailAvgUnitPrice;
	private float largelanDetailAvgUnitPrice;
	private float wlanSecurityDetailAvgUnitPrice;
	private float wlanAccessSecurityDetailAvgUnitPrice;
	private float loadBalancesSecurityDetailAvgUnitPrice;
	private float vpnSecurityDetailAvgUnitPrice;
	private float dnssecurityDetailAvgUnitPrice;
	private float firewallsSecurityDetailAvgUnitPrice;
	private float reverseProxiesSecurityDetailAvgUnitPrice;

	private float wanDetailVolumeRevenue;
	private float smallWanDetailRevenue;
	private float mediumwanDetailRevenue;
	private float largewanDetailRevenue;
	private float lanDetailVolumeRevenue;
	private float smalllanDetailRevenue;
	private float mediumlanDetailRevenue;
	private float largelanDetailRevenue;
	private float wlanSecurityDetailRevenue;
	private float wlanAccessSecuritylanDetailRevenue;
	private float loadBalancesSecurityDetailRevenue;
	private float vpnSecurityDetailRevenue;
	private float dnsSecurityDetailRevenue;
	private float firewallsSecurityDetailRevenue;
	private float reverseProxiesSecurityDetailRevenue;

}
