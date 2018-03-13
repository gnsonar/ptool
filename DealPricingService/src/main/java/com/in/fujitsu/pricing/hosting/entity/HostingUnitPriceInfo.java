package com.in.fujitsu.pricing.hosting.entity;

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
 * @author pawarbh
 *
 */
@Slf4j
@Entity
@Table(name = "HOSTING_UNIT_PRICE")
@DynamicInsert
@DynamicUpdate
public class HostingUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -5821567931912235588L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private HostingYearlyDataInfo hostingYearlyDataInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal servers;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physical;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalWin;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalWinSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalWinMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalWinLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalUnix;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalUnixSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalUnixMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal physicalUnixLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtual;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublic;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicWin;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicWinSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicWinMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicWinLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicUnix;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicUnixSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicUnixMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPublicUnixLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivate;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateWin;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateWinSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateWinMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateWinLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateUnix;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateUnixSmall;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateUnixMedium;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal virtualPrivateUnixLarge;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal sqlInstances;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal cotsInstallations;

	private String benchMarkType;

	@Override
	protected HostingUnitPriceInfo clone() throws CloneNotSupportedException {
		HostingUnitPriceInfo hostingUnitPriceInfo = null;
		try {
			hostingUnitPriceInfo = (HostingUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return hostingUnitPriceInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HostingYearlyDataInfo getHostingYearlyDataInfo() {
		return hostingYearlyDataInfo;
	}

	public void setHostingYearlyDataInfo(HostingYearlyDataInfo hostingYearlyDataInfo) {
		this.hostingYearlyDataInfo = hostingYearlyDataInfo;
	}

	public BigDecimal getServers() {
		return servers;
	}

	public void setServers(BigDecimal servers) {
		this.servers = servers;
	}

	public BigDecimal getPhysical() {
		return physical;
	}

	public void setPhysical(BigDecimal physical) {
		this.physical = physical;
	}

	public BigDecimal getPhysicalWin() {
		return physicalWin;
	}

	public void setPhysicalWin(BigDecimal physicalWin) {
		this.physicalWin = physicalWin;
	}

	public BigDecimal getPhysicalWinSmall() {
		return physicalWinSmall;
	}

	public void setPhysicalWinSmall(BigDecimal physicalWinSmall) {
		this.physicalWinSmall = physicalWinSmall;
	}

	public BigDecimal getPhysicalWinMedium() {
		return physicalWinMedium;
	}

	public void setPhysicalWinMedium(BigDecimal physicalWinMedium) {
		this.physicalWinMedium = physicalWinMedium;
	}

	public BigDecimal getPhysicalWinLarge() {
		return physicalWinLarge;
	}

	public void setPhysicalWinLarge(BigDecimal physicalWinLarge) {
		this.physicalWinLarge = physicalWinLarge;
	}

	public BigDecimal getPhysicalUnix() {
		return physicalUnix;
	}

	public void setPhysicalUnix(BigDecimal physicalUnix) {
		this.physicalUnix = physicalUnix;
	}

	public BigDecimal getPhysicalUnixSmall() {
		return physicalUnixSmall;
	}

	public void setPhysicalUnixSmall(BigDecimal physicalUnixSmall) {
		this.physicalUnixSmall = physicalUnixSmall;
	}

	public BigDecimal getPhysicalUnixMedium() {
		return physicalUnixMedium;
	}

	public void setPhysicalUnixMedium(BigDecimal physicalUnixMedium) {
		this.physicalUnixMedium = physicalUnixMedium;
	}

	public BigDecimal getPhysicalUnixLarge() {
		return physicalUnixLarge;
	}

	public void setPhysicalUnixLarge(BigDecimal physicalUnixLarge) {
		this.physicalUnixLarge = physicalUnixLarge;
	}

	public BigDecimal getVirtual() {
		return virtual;
	}

	public void setVirtual(BigDecimal virtual) {
		this.virtual = virtual;
	}

	public BigDecimal getVirtualPublic() {
		return virtualPublic;
	}

	public void setVirtualPublic(BigDecimal virtualPublic) {
		this.virtualPublic = virtualPublic;
	}

	public BigDecimal getVirtualPublicWin() {
		return virtualPublicWin;
	}

	public void setVirtualPublicWin(BigDecimal virtualPublicWin) {
		this.virtualPublicWin = virtualPublicWin;
	}

	public BigDecimal getVirtualPublicWinSmall() {
		return virtualPublicWinSmall;
	}

	public void setVirtualPublicWinSmall(BigDecimal virtualPublicWinSmall) {
		this.virtualPublicWinSmall = virtualPublicWinSmall;
	}

	public BigDecimal getVirtualPublicWinMedium() {
		return virtualPublicWinMedium;
	}

	public void setVirtualPublicWinMedium(BigDecimal virtualPublicWinMedium) {
		this.virtualPublicWinMedium = virtualPublicWinMedium;
	}

	public BigDecimal getVirtualPublicWinLarge() {
		return virtualPublicWinLarge;
	}

	public void setVirtualPublicWinLarge(BigDecimal virtualPublicWinLarge) {
		this.virtualPublicWinLarge = virtualPublicWinLarge;
	}

	public BigDecimal getVirtualPublicUnix() {
		return virtualPublicUnix;
	}

	public void setVirtualPublicUnix(BigDecimal virtualPublicUnix) {
		this.virtualPublicUnix = virtualPublicUnix;
	}

	public BigDecimal getVirtualPublicUnixSmall() {
		return virtualPublicUnixSmall;
	}

	public void setVirtualPublicUnixSmall(BigDecimal virtualPublicUnixSmall) {
		this.virtualPublicUnixSmall = virtualPublicUnixSmall;
	}

	public BigDecimal getVirtualPublicUnixMedium() {
		return virtualPublicUnixMedium;
	}

	public void setVirtualPublicUnixMedium(BigDecimal virtualPublicUnixMedium) {
		this.virtualPublicUnixMedium = virtualPublicUnixMedium;
	}

	public BigDecimal getVirtualPublicUnixLarge() {
		return virtualPublicUnixLarge;
	}

	public void setVirtualPublicUnixLarge(BigDecimal virtualPublicUnixLarge) {
		this.virtualPublicUnixLarge = virtualPublicUnixLarge;
	}

	public BigDecimal getVirtualPrivate() {
		return virtualPrivate;
	}

	public void setVirtualPrivate(BigDecimal virtualPrivate) {
		this.virtualPrivate = virtualPrivate;
	}

	public BigDecimal getVirtualPrivateWin() {
		return virtualPrivateWin;
	}

	public void setVirtualPrivateWin(BigDecimal virtualPrivateWin) {
		this.virtualPrivateWin = virtualPrivateWin;
	}

	public BigDecimal getVirtualPrivateWinSmall() {
		return virtualPrivateWinSmall;
	}

	public void setVirtualPrivateWinSmall(BigDecimal virtualPrivateWinSmall) {
		this.virtualPrivateWinSmall = virtualPrivateWinSmall;
	}

	public BigDecimal getVirtualPrivateWinMedium() {
		return virtualPrivateWinMedium;
	}

	public void setVirtualPrivateWinMedium(BigDecimal virtualPrivateWinMedium) {
		this.virtualPrivateWinMedium = virtualPrivateWinMedium;
	}

	public BigDecimal getVirtualPrivateWinLarge() {
		return virtualPrivateWinLarge;
	}

	public void setVirtualPrivateWinLarge(BigDecimal virtualPrivateWinLarge) {
		this.virtualPrivateWinLarge = virtualPrivateWinLarge;
	}

	public BigDecimal getVirtualPrivateUnix() {
		return virtualPrivateUnix;
	}

	public void setVirtualPrivateUnix(BigDecimal virtualPrivateUnix) {
		this.virtualPrivateUnix = virtualPrivateUnix;
	}

	public BigDecimal getVirtualPrivateUnixSmall() {
		return virtualPrivateUnixSmall;
	}

	public void setVirtualPrivateUnixSmall(BigDecimal virtualPrivateUnixSmall) {
		this.virtualPrivateUnixSmall = virtualPrivateUnixSmall;
	}

	public BigDecimal getVirtualPrivateUnixMedium() {
		return virtualPrivateUnixMedium;
	}

	public void setVirtualPrivateUnixMedium(BigDecimal virtualPrivateUnixMedium) {
		this.virtualPrivateUnixMedium = virtualPrivateUnixMedium;
	}

	public BigDecimal getVirtualPrivateUnixLarge() {
		return virtualPrivateUnixLarge;
	}

	public void setVirtualPrivateUnixLarge(BigDecimal virtualPrivateUnixLarge) {
		this.virtualPrivateUnixLarge = virtualPrivateUnixLarge;
	}

	public BigDecimal getSqlInstances() {
		return sqlInstances;
	}

	public void setSqlInstances(BigDecimal sqlInstances) {
		this.sqlInstances = sqlInstances;
	}

	public BigDecimal getCotsInstallations() {
		return cotsInstallations;
	}

	public void setCotsInstallations(BigDecimal cotsInstallations) {
		this.cotsInstallations = cotsInstallations;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

}
