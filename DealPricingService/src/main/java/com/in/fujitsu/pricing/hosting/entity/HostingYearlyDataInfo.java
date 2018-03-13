package com.in.fujitsu.pricing.hosting.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "HOSTING_YEARLY_VOLUME")
@DynamicInsert
public class HostingYearlyDataInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -1414398922139428162L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long yearId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "hostingId")
	private HostingInfo hostingInfo;

	@JsonIgnore
	@OneToMany(mappedBy = "hostingYearlyDataInfo", cascade = CascadeType.ALL)
	private List<HostingUnitPriceInfo> hostingUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "hostingYearlyDataInfo", cascade = CascadeType.ALL)
	private List<HostingRevenueInfo> hostingRevenueInfoList;

	@Column(columnDefinition="int default 0")
	private Integer servers;

	@Column(columnDefinition="int default 0")
	private Integer physical;

	@Column(columnDefinition="int default 0")
	private Integer physicalWin;

	@Column(columnDefinition="int default 0")
	private Integer physicalWinSmall;

	@Column(columnDefinition="int default 0")
	private Integer physicalWinMedium;

	@Column(columnDefinition="int default 0")
	private Integer physicalWinLarge;

	@Column(columnDefinition="int default 0")
	private Integer physicalUnix;

	@Column(columnDefinition="int default 0")
	private Integer physicalUnixSmall;

	@Column(columnDefinition="int default 0")
	private Integer physicalUnixMedium;

	@Column(columnDefinition="int default 0")
	private Integer physicalUnixLarge;

	@Column(columnDefinition="int default 0")
	private Integer virtual;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublic;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicWin;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicWinSmall;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicWinMedium;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicWinLarge;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicUnix;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicUnixSmall;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicUnixMedium;

	@Column(columnDefinition="int default 0")
	private Integer virtualPublicUnixLarge;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivate;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateWin;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateWinSmall;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateWinMedium;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateWinLarge;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateUnix;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateUnixSmall;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateUnixMedium;

	@Column(columnDefinition="int default 0")
	private Integer virtualPrivateUnixLarge;

	@Column(columnDefinition="int default 0")
	private Integer sqlInstances;

	@Column(columnDefinition="int default 0")
	private Integer cotsInstallations;

	private Integer year;

	@Override
	public HostingYearlyDataInfo clone() {
		HostingYearlyDataInfo cloneHostingYearlyDataInfo = null;
		try {
			cloneHostingYearlyDataInfo = (HostingYearlyDataInfo) super.clone();
			List<HostingUnitPriceInfo> unitPriceInfoList = new ArrayList<>();
			for (HostingUnitPriceInfo endUserUnitPriceInfo : cloneHostingYearlyDataInfo.getHostingUnitPriceInfoList()) {
				HostingUnitPriceInfo cloneUnitPrice = endUserUnitPriceInfo.clone();
				cloneUnitPrice.setHostingYearlyDataInfo(cloneHostingYearlyDataInfo);
				unitPriceInfoList.add(cloneUnitPrice);
			}
			cloneHostingYearlyDataInfo.setHostingUnitPriceInfoList(unitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneHostingYearlyDataInfo;
	}

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public HostingInfo getHostingInfo() {
		return hostingInfo;
	}

	public void setHostingInfo(HostingInfo hostingInfo) {
		this.hostingInfo = hostingInfo;
	}

	public List<HostingUnitPriceInfo> getHostingUnitPriceInfoList() {
		return hostingUnitPriceInfoList;
	}

	public void setHostingUnitPriceInfoList(List<HostingUnitPriceInfo> hostingUnitPriceInfoList) {
		this.hostingUnitPriceInfoList = hostingUnitPriceInfoList;
	}

	public List<HostingRevenueInfo> getHostingRevenueInfoList() {
		return hostingRevenueInfoList;
	}

	public void setHostingRevenueInfoList(List<HostingRevenueInfo> hostingRevenueInfoList) {
		this.hostingRevenueInfoList = hostingRevenueInfoList;
	}

	public Integer getServers() {
		return servers;
	}

	public void setServers(Integer servers) {
		this.servers = servers;
	}

	public Integer getPhysical() {
		return physical;
	}

	public void setPhysical(Integer physical) {
		this.physical = physical;
	}

	public Integer getPhysicalWin() {
		return physicalWin;
	}

	public void setPhysicalWin(Integer physicalWin) {
		this.physicalWin = physicalWin;
	}

	public Integer getPhysicalWinSmall() {
		return physicalWinSmall;
	}

	public void setPhysicalWinSmall(Integer physicalWinSmall) {
		this.physicalWinSmall = physicalWinSmall;
	}

	public Integer getPhysicalWinMedium() {
		return physicalWinMedium;
	}

	public void setPhysicalWinMedium(Integer physicalWinMedium) {
		this.physicalWinMedium = physicalWinMedium;
	}

	public Integer getPhysicalWinLarge() {
		return physicalWinLarge;
	}

	public void setPhysicalWinLarge(Integer physicalWinLarge) {
		this.physicalWinLarge = physicalWinLarge;
	}

	public Integer getPhysicalUnix() {
		return physicalUnix;
	}

	public void setPhysicalUnix(Integer physicalUnix) {
		this.physicalUnix = physicalUnix;
	}

	public Integer getPhysicalUnixSmall() {
		return physicalUnixSmall;
	}

	public void setPhysicalUnixSmall(Integer physicalUnixSmall) {
		this.physicalUnixSmall = physicalUnixSmall;
	}

	public Integer getPhysicalUnixMedium() {
		return physicalUnixMedium;
	}

	public void setPhysicalUnixMedium(Integer physicalUnixMedium) {
		this.physicalUnixMedium = physicalUnixMedium;
	}

	public Integer getPhysicalUnixLarge() {
		return physicalUnixLarge;
	}

	public void setPhysicalUnixLarge(Integer physicalUnixLarge) {
		this.physicalUnixLarge = physicalUnixLarge;
	}

	public Integer getVirtual() {
		return virtual;
	}

	public void setVirtual(Integer virtual) {
		this.virtual = virtual;
	}

	public Integer getVirtualPublic() {
		return virtualPublic;
	}

	public void setVirtualPublic(Integer virtualPublic) {
		this.virtualPublic = virtualPublic;
	}

	public Integer getVirtualPublicWin() {
		return virtualPublicWin;
	}

	public void setVirtualPublicWin(Integer virtualPublicWin) {
		this.virtualPublicWin = virtualPublicWin;
	}

	public Integer getVirtualPublicWinSmall() {
		return virtualPublicWinSmall;
	}

	public void setVirtualPublicWinSmall(Integer virtualPublicWinSmall) {
		this.virtualPublicWinSmall = virtualPublicWinSmall;
	}

	public Integer getVirtualPublicWinMedium() {
		return virtualPublicWinMedium;
	}

	public void setVirtualPublicWinMedium(Integer virtualPublicWinMedium) {
		this.virtualPublicWinMedium = virtualPublicWinMedium;
	}

	public Integer getVirtualPublicWinLarge() {
		return virtualPublicWinLarge;
	}

	public void setVirtualPublicWinLarge(Integer virtualPublicWinLarge) {
		this.virtualPublicWinLarge = virtualPublicWinLarge;
	}

	public Integer getVirtualPublicUnix() {
		return virtualPublicUnix;
	}

	public void setVirtualPublicUnix(Integer virtualPublicUnix) {
		this.virtualPublicUnix = virtualPublicUnix;
	}

	public Integer getVirtualPublicUnixSmall() {
		return virtualPublicUnixSmall;
	}

	public void setVirtualPublicUnixSmall(Integer virtualPublicUnixSmall) {
		this.virtualPublicUnixSmall = virtualPublicUnixSmall;
	}

	public Integer getVirtualPublicUnixMedium() {
		return virtualPublicUnixMedium;
	}

	public void setVirtualPublicUnixMedium(Integer virtualPublicUnixMedium) {
		this.virtualPublicUnixMedium = virtualPublicUnixMedium;
	}

	public Integer getVirtualPublicUnixLarge() {
		return virtualPublicUnixLarge;
	}

	public void setVirtualPublicUnixLarge(Integer virtualPublicUnixLarge) {
		this.virtualPublicUnixLarge = virtualPublicUnixLarge;
	}

	public Integer getVirtualPrivate() {
		return virtualPrivate;
	}

	public void setVirtualPrivate(Integer virtualPrivate) {
		this.virtualPrivate = virtualPrivate;
	}

	public Integer getVirtualPrivateWin() {
		return virtualPrivateWin;
	}

	public void setVirtualPrivateWin(Integer virtualPrivateWin) {
		this.virtualPrivateWin = virtualPrivateWin;
	}

	public Integer getVirtualPrivateWinSmall() {
		return virtualPrivateWinSmall;
	}

	public void setVirtualPrivateWinSmall(Integer virtualPrivateWinSmall) {
		this.virtualPrivateWinSmall = virtualPrivateWinSmall;
	}

	public Integer getVirtualPrivateWinMedium() {
		return virtualPrivateWinMedium;
	}

	public void setVirtualPrivateWinMedium(Integer virtualPrivateWinMedium) {
		this.virtualPrivateWinMedium = virtualPrivateWinMedium;
	}

	public Integer getVirtualPrivateWinLarge() {
		return virtualPrivateWinLarge;
	}

	public void setVirtualPrivateWinLarge(Integer virtualPrivateWinLarge) {
		this.virtualPrivateWinLarge = virtualPrivateWinLarge;
	}

	public Integer getVirtualPrivateUnix() {
		return virtualPrivateUnix;
	}

	public void setVirtualPrivateUnix(Integer virtualPrivateUnix) {
		this.virtualPrivateUnix = virtualPrivateUnix;
	}

	public Integer getVirtualPrivateUnixSmall() {
		return virtualPrivateUnixSmall;
	}

	public void setVirtualPrivateUnixSmall(Integer virtualPrivateUnixSmall) {
		this.virtualPrivateUnixSmall = virtualPrivateUnixSmall;
	}

	public Integer getVirtualPrivateUnixMedium() {
		return virtualPrivateUnixMedium;
	}

	public void setVirtualPrivateUnixMedium(Integer virtualPrivateUnixMedium) {
		this.virtualPrivateUnixMedium = virtualPrivateUnixMedium;
	}

	public Integer getVirtualPrivateUnixLarge() {
		return virtualPrivateUnixLarge;
	}

	public void setVirtualPrivateUnixLarge(Integer virtualPrivateUnixLarge) {
		this.virtualPrivateUnixLarge = virtualPrivateUnixLarge;
	}

	public Integer getSqlInstances() {
		return sqlInstances;
	}

	public void setSqlInstances(Integer sqlInstances) {
		this.sqlInstances = sqlInstances;
	}

	public Integer getCotsInstallations() {
		return cotsInstallations;
	}

	public void setCotsInstallations(Integer cotsInstallations) {
		this.cotsInstallations = cotsInstallations;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
