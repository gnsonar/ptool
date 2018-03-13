
package com.in.fujitsu.pricing.network.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="NETWORK_REVENUE")
@DynamicInsert
@DynamicUpdate
public class NetworkRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition="int default 0")
	private Integer totalWanRevenue;

	@Column(columnDefinition="int default 0")
	private Integer totalLanRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalWlanControllersRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalWlanAccesspointRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalLoadBalancersRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalVpnIpSecRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalDnsDhcpServiceRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalFirewallsRevenue;
	
	@Column(columnDefinition="int default 0")
	private Integer totalProxiesRevenue;
	
	private String benchMarkType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private NetworkYearlyDataInfo networkYearlyDataInfo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getTotalWanRevenue() {
		return totalWanRevenue;
	}

	public void setTotalWanRevenue(Integer totalWanRevenue) {
		this.totalWanRevenue = totalWanRevenue;
	}

	public Integer getTotalLanRevenue() {
		return totalLanRevenue;
	}

	public void setTotalLanRevenue(Integer totalLanRevenue) {
		this.totalLanRevenue = totalLanRevenue;
	}

	public Integer getTotalWlanControllersRevenue() {
		return totalWlanControllersRevenue;
	}

	public void setTotalWlanControllersRevenue(Integer totalWlanControllersRevenue) {
		this.totalWlanControllersRevenue = totalWlanControllersRevenue;
	}

	public Integer getTotalWlanAccesspointRevenue() {
		return totalWlanAccesspointRevenue;
	}

	public void setTotalWlanAccesspointRevenue(Integer totalWlanAccesspointRevenue) {
		this.totalWlanAccesspointRevenue = totalWlanAccesspointRevenue;
	}

	public Integer getTotalLoadBalancersRevenue() {
		return totalLoadBalancersRevenue;
	}

	public void setTotalLoadBalancersRevenue(Integer totalLoadBalancersRevenue) {
		this.totalLoadBalancersRevenue = totalLoadBalancersRevenue;
	}

	public Integer getTotalVpnIpSecRevenue() {
		return totalVpnIpSecRevenue;
	}

	public void setTotalVpnIpSecRevenue(Integer totalVpnIpSecRevenue) {
		this.totalVpnIpSecRevenue = totalVpnIpSecRevenue;
	}

	public Integer getTotalDnsDhcpServiceRevenue() {
		return totalDnsDhcpServiceRevenue;
	}

	public void setTotalDnsDhcpServiceRevenue(Integer totalDnsDhcpServiceRevenue) {
		this.totalDnsDhcpServiceRevenue = totalDnsDhcpServiceRevenue;
	}

	public Integer getTotalFirewallsRevenue() {
		return totalFirewallsRevenue;
	}

	public void setTotalFirewallsRevenue(Integer totalFirewallsRevenue) {
		this.totalFirewallsRevenue = totalFirewallsRevenue;
	}

	public Integer getTotalProxiesRevenue() {
		return totalProxiesRevenue;
	}

	public void setTotalProxiesRevenue(Integer totalProxiesRevenue) {
		this.totalProxiesRevenue = totalProxiesRevenue;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	public NetworkYearlyDataInfo getNetworkYearlyDataInfo() {
		return networkYearlyDataInfo;
	}

	public void setNetworkYearlyDataInfo(NetworkYearlyDataInfo networkYearlyDataInfo) {
		this.networkYearlyDataInfo = networkYearlyDataInfo;
	}

}
