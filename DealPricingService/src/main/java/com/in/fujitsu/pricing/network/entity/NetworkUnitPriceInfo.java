package com.in.fujitsu.pricing.network.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import lombok.extern.slf4j.Slf4j;

/**
 * @author ChhabrMa
 *
 */
@Slf4j
@Entity
@Table(name = "NETWORK_UNIT_PRICE")
@DynamicInsert
@DynamicUpdate
public class NetworkUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -5821567931912235588L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private NetworkYearlyDataInfo networkYearlyDataInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal wanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal smallWanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal mediumWanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal largeWanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal lanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal smallLanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal mediumLanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal largeLanDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal wlanControllers;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal wlanAccesspoint;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal loadBalancers;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal vpnIpSec;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal dnsDhcpService;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal firewalls;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal proxies;

	private String benchMarkType;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the networkYearlyDataInfo
	 */
	public NetworkYearlyDataInfo getNetworkYearlyDataInfo() {
		return networkYearlyDataInfo;
	}

	/**
	 * @param networkYearlyDataInfo the networkYearlyDataInfo to set
	 */
	public void setNetworkYearlyDataInfo(NetworkYearlyDataInfo networkYearlyDataInfo) {
		this.networkYearlyDataInfo = networkYearlyDataInfo;
	}

	/**
	 * @return the wanDevices
	 */
	public BigDecimal getWanDevices() {
		return wanDevices;
	}

	/**
	 * @param wanDevices the wanDevices to set
	 */
	public void setWanDevices(BigDecimal wanDevices) {
		this.wanDevices = wanDevices;
	}

	/**
	 * @return the smallWanDevices
	 */
	public BigDecimal getSmallWanDevices() {
		return smallWanDevices;
	}

	/**
	 * @param smallWanDevices the smallWanDevices to set
	 */
	public void setSmallWanDevices(BigDecimal smallWanDevices) {
		this.smallWanDevices = smallWanDevices;
	}

	/**
	 * @return the mediumWanDevices
	 */
	public BigDecimal getMediumWanDevices() {
		return mediumWanDevices;
	}

	/**
	 * @param mediumWanDevices the mediumWanDevices to set
	 */
	public void setMediumWanDevices(BigDecimal mediumWanDevices) {
		this.mediumWanDevices = mediumWanDevices;
	}

	/**
	 * @return the largeWanDevices
	 */
	public BigDecimal getLargeWanDevices() {
		return largeWanDevices;
	}

	/**
	 * @param largeWanDevices the largeWanDevices to set
	 */
	public void setLargeWanDevices(BigDecimal largeWanDevices) {
		this.largeWanDevices = largeWanDevices;
	}

	/**
	 * @return the lanDevices
	 */
	public BigDecimal getLanDevices() {
		return lanDevices;
	}

	/**
	 * @param lanDevices the lanDevices to set
	 */
	public void setLanDevices(BigDecimal lanDevices) {
		this.lanDevices = lanDevices;
	}

	/**
	 * @return the smallLanDevices
	 */
	public BigDecimal getSmallLanDevices() {
		return smallLanDevices;
	}

	/**
	 * @param smallLanDevices the smallLanDevices to set
	 */
	public void setSmallLanDevices(BigDecimal smallLanDevices) {
		this.smallLanDevices = smallLanDevices;
	}

	/**
	 * @return the mediumLanDevices
	 */
	public BigDecimal getMediumLanDevices() {
		return mediumLanDevices;
	}

	/**
	 * @param mediumLanDevices the mediumLanDevices to set
	 */
	public void setMediumLanDevices(BigDecimal mediumLanDevices) {
		this.mediumLanDevices = mediumLanDevices;
	}

	/**
	 * @return the largeLanDevices
	 */
	public BigDecimal getLargeLanDevices() {
		return largeLanDevices;
	}

	/**
	 * @param largeLanDevices the largeLanDevices to set
	 */
	public void setLargeLanDevices(BigDecimal largeLanDevices) {
		this.largeLanDevices = largeLanDevices;
	}

	/**
	 * @return the wlanControllers
	 */
	public BigDecimal getWlanControllers() {
		return wlanControllers;
	}

	/**
	 * @param wlanControllers the wlanControllers to set
	 */
	public void setWlanControllers(BigDecimal wlanControllers) {
		this.wlanControllers = wlanControllers;
	}

	/**
	 * @return the wlanAccesspoint
	 */
	public BigDecimal getWlanAccesspoint() {
		return wlanAccesspoint;
	}

	/**
	 * @param wlanAccesspoint the wlanAccesspoint to set
	 */
	public void setWlanAccesspoint(BigDecimal wlanAccesspoint) {
		this.wlanAccesspoint = wlanAccesspoint;
	}

	/**
	 * @return the loadBalancers
	 */
	public BigDecimal getLoadBalancers() {
		return loadBalancers;
	}

	/**
	 * @param loadBalancers the loadBalancers to set
	 */
	public void setLoadBalancers(BigDecimal loadBalancers) {
		this.loadBalancers = loadBalancers;
	}

	/**
	 * @return the vpnIpSec
	 */
	public BigDecimal getVpnIpSec() {
		return vpnIpSec;
	}

	/**
	 * @param vpnIpSec the vpnIpSec to set
	 */
	public void setVpnIpSec(BigDecimal vpnIpSec) {
		this.vpnIpSec = vpnIpSec;
	}

	/**
	 * @return the dnsDhcpService
	 */
	public BigDecimal getDnsDhcpService() {
		return dnsDhcpService;
	}

	/**
	 * @param dnsDhcpService the dnsDhcpService to set
	 */
	public void setDnsDhcpService(BigDecimal dnsDhcpService) {
		this.dnsDhcpService = dnsDhcpService;
	}

	/**
	 * @return the firewalls
	 */
	public BigDecimal getFirewalls() {
		return firewalls;
	}

	/**
	 * @param firewalls the firewalls to set
	 */
	public void setFirewalls(BigDecimal firewalls) {
		this.firewalls = firewalls;
	}

	/**
	 * @return the proxies
	 */
	public BigDecimal getProxies() {
		return proxies;
	}

	/**
	 * @param proxies the proxies to set
	 */
	public void setProxies(BigDecimal proxies) {
		this.proxies = proxies;
	}

	/**
	 * @return the benchMarkType
	 */
	public String getBenchMarkType() {
		return benchMarkType;
	}

	/**
	 * @param benchMarkType the benchMarkType to set
	 */
	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	protected NetworkUnitPriceInfo clone() throws CloneNotSupportedException {
		NetworkUnitPriceInfo networkUnitPriceInfo = null;
		try {
			networkUnitPriceInfo = (NetworkUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return networkUnitPriceInfo;
	}

}
