
package com.in.fujitsu.pricing.network.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class NetworkRevenueInfoDto {

	private long id;

	private Integer totalWanRevenue;

	private Integer totalLanRevenue;

	private Integer totalWlanControllersRevenue;

	private Integer totalWlanAccesspointRevenue;

	private Integer totalLoadBalancersRevenue;

	private Integer totalVpnIpSecRevenue;

	private Integer totalDnsDhcpServiceRevenue;

	private Integer totalFirewallsRevenue;

	private Integer totalProxiesRevenue;

	private BigDecimal totalRevenue;

	private String benchMarkType;

	private int year;

}
