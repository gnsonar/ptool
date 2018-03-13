
package com.in.fujitsu.pricing.network.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class NetworkPriceDto implements Serializable {

	private static final long serialVersionUID = -8637323796759219463L;

	private int year;

	private BigDecimal totalWanUnitPrice;
	private BigDecimal totalSmallWanUnitPrice;
	private BigDecimal totalMediumWanUnitPrice;
	private BigDecimal totalLargeWanUnitPrice;
	private BigDecimal totalLanUnitPrice;
	private BigDecimal totalSmallLanUnitPrice;
	private BigDecimal totalMediumLanUnitPrice;
	private BigDecimal totalLargeLanUnitPrice;
	private BigDecimal totalWlanControllersUnitPrice;
	private BigDecimal totalWlanAccesspointUnitPrice;
	private BigDecimal totalLoadBalancersUnitPrice;
	private BigDecimal totalVpnIpSecUnitPrice;
	private BigDecimal totalDnsDhcpServiceUnitPrice;
	private BigDecimal totalFirewallsUnitPrice;
	private BigDecimal totalProxiesUnitPrice;

	private Integer totalWanRevenue;
	private Integer totalLanRevenue;
	private Integer totalWlanControllersRevenue;
	private Integer totalWlanAccesspointRevenue;
	private Integer totalLoadBalancersRevenue;
	private Integer totalVpnIpSecRevenue;
	private Integer totalDnsDhcpServiceRevenue;
	private Integer totalFirewallsRevenue;
	private Integer totalProxiesRevenue;

}
