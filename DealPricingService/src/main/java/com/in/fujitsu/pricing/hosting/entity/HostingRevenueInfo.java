
package com.in.fujitsu.pricing.hosting.entity;

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
@Table(name="HOSTING_REVENUE")
@DynamicInsert
@DynamicUpdate
public class HostingRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition="int default 0")
	private Integer servers;
	
	@Column(columnDefinition="int default 0")
	private Integer sqlInstances;
	
	@Column(columnDefinition="int default 0")
	private Integer cotsInstallations;
	
	private String benchMarkType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private HostingYearlyDataInfo hostingYearlyDataInfo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getServers() {
		return servers;
	}

	public void setServers(Integer servers) {
		this.servers = servers;
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

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	public HostingYearlyDataInfo getHostingYearlyDataInfo() {
		return hostingYearlyDataInfo;
	}

	public void setHostingYearlyDataInfo(HostingYearlyDataInfo hostingYearlyDataInfo) {
		this.hostingYearlyDataInfo = hostingYearlyDataInfo;
	}

}
