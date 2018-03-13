package com.in.fujitsu.pricing.network.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ChhabrMa
 *
 */
@Slf4j
@Entity
@Table(name = "NETWORK_YEARLY_VOLUME")
@DynamicInsert
@DynamicUpdate
public class NetworkYearlyDataInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -1414398922139428162L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long yearId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "networkId")
	private NetworkInfo networkInfo;

	@JsonIgnore
	@OneToMany(mappedBy = "networkYearlyDataInfo", cascade = CascadeType.ALL)
	private List<NetworkUnitPriceInfo> networkUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "networkYearlyDataInfo", cascade = CascadeType.ALL)
	private List<NetworkRevenueInfo> networkRevenueInfoList;

	@Column(columnDefinition="int default 0")
	private Integer wanDevices;

	@Column(columnDefinition="int default 0")
	private Integer smallWanDevices;

	@Column(columnDefinition="int default 0")
	private Integer mediumWanDevices;

	@Column(columnDefinition="int default 0")
	private Integer largeWanDevices;

	@Column(columnDefinition="int default 0")
	private Integer lanDevices;

	@Column(columnDefinition="int default 0")
	private Integer smallLanDevices;

	@Column(columnDefinition="int default 0")
	private Integer mediumLanDevices;

	@Column(columnDefinition="int default 0")
	private Integer largeLanDevices;

	@Column(columnDefinition="int default 0")
	private Integer wlanControllers;

	@Column(columnDefinition="int default 0")
	private Integer wlanAccesspoint;

	@Column(columnDefinition="int default 0")
	private Integer loadBalancers;

	@Column(columnDefinition="int default 0")
	private Integer vpnIpSec;

	@Column(columnDefinition="int default 0")
	private Integer dnsDhcpService;

	@Column(columnDefinition="int default 0")
	private Integer firewalls;

	@Column(columnDefinition="int default 0")
	private Integer proxies;

	private Integer year;

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public NetworkInfo getNetworkInfo() {
		return networkInfo;
	}

	public void setNetworkInfo(NetworkInfo networkInfo) {
		this.networkInfo = networkInfo;
	}

	public List<NetworkUnitPriceInfo> getNetworkUnitPriceInfoList() {
		return networkUnitPriceInfoList;
	}

	public void setNetworkUnitPriceInfoList(List<NetworkUnitPriceInfo> networkUnitPriceInfoList) {
		this.networkUnitPriceInfoList = networkUnitPriceInfoList;
	}

	public Integer getWanDevices() {
		return wanDevices;
	}

	public void setWanDevices(Integer wanDevices) {
		this.wanDevices = wanDevices;
	}

	public Integer getSmallWanDevices() {
		return smallWanDevices;
	}

	public void setSmallWanDevices(Integer smallWanDevices) {
		this.smallWanDevices = smallWanDevices;
	}

	public Integer getMediumWanDevices() {
		return mediumWanDevices;
	}

	public void setMediumWanDevices(Integer mediumWanDevices) {
		this.mediumWanDevices = mediumWanDevices;
	}

	public Integer getLargeWanDevices() {
		return largeWanDevices;
	}

	public void setLargeWanDevices(Integer largeWanDevices) {
		this.largeWanDevices = largeWanDevices;
	}

	public Integer getLanDevices() {
		return lanDevices;
	}

	public void setLanDevices(Integer lanDevices) {
		this.lanDevices = lanDevices;
	}

	public Integer getSmallLanDevices() {
		return smallLanDevices;
	}

	public void setSmallLanDevices(Integer smallLanDevices) {
		this.smallLanDevices = smallLanDevices;
	}

	public Integer getMediumLanDevices() {
		return mediumLanDevices;
	}

	public void setMediumLanDevices(Integer mediumLanDevices) {
		this.mediumLanDevices = mediumLanDevices;
	}

	public Integer getLargeLanDevices() {
		return largeLanDevices;
	}

	public void setLargeLanDevices(Integer largeLanDevices) {
		this.largeLanDevices = largeLanDevices;
	}

	public Integer getWlanControllers() {
		return wlanControllers;
	}

	public void setWlanControllers(Integer wlanControllers) {
		this.wlanControllers = wlanControllers;
	}

	public Integer getWlanAccesspoint() {
		return wlanAccesspoint;
	}

	public void setWlanAccesspoint(Integer wlanAccesspoint) {
		this.wlanAccesspoint = wlanAccesspoint;
	}

	public Integer getLoadBalancers() {
		return loadBalancers;
	}

	public void setLoadBalancers(Integer loadBalancers) {
		this.loadBalancers = loadBalancers;
	}

	public Integer getVpnIpSec() {
		return vpnIpSec;
	}

	public void setVpnIpSec(Integer vpnIpSec) {
		this.vpnIpSec = vpnIpSec;
	}

	public Integer getDnsDhcpService() {
		return dnsDhcpService;
	}

	public void setDnsDhcpService(Integer dnsDhcpService) {
		this.dnsDhcpService = dnsDhcpService;
	}

	public Integer getFirewalls() {
		return firewalls;
	}

	public void setFirewalls(Integer firewalls) {
		this.firewalls = firewalls;
	}

	public Integer getProxies() {
		return proxies;
	}

	public void setProxies(Integer proxies) {
		this.proxies = proxies;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public NetworkYearlyDataInfo clone() {
		NetworkYearlyDataInfo cloneNetworkYearlyDataInfo = null;
		try {
			cloneNetworkYearlyDataInfo = (NetworkYearlyDataInfo) super.clone();
			List<NetworkUnitPriceInfo> networkUnitPriceInfoList = new ArrayList<>();
			for (NetworkUnitPriceInfo networkUnitPriceInfo : cloneNetworkYearlyDataInfo
					.getNetworkUnitPriceInfoList()) {
				NetworkUnitPriceInfo cloneUnitPrice = networkUnitPriceInfo.clone();
				cloneUnitPrice.setNetworkYearlyDataInfo(cloneNetworkYearlyDataInfo);
				networkUnitPriceInfoList.add(cloneUnitPrice);
			}
			cloneNetworkYearlyDataInfo.setNetworkUnitPriceInfoList(networkUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneNetworkYearlyDataInfo;
	}

	public List<NetworkRevenueInfo> getNetworkRevenueInfoList() {
		return networkRevenueInfoList;
	}

	public void setNetworkRevenueInfoList(List<NetworkRevenueInfo> networkRevenueInfoList) {
		this.networkRevenueInfoList = networkRevenueInfoList;
	}

}
