
package com.in.fujitsu.pricing.application.entity;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name="APPLICATION_REVENUE")
@DynamicInsert
@DynamicUpdate
public class ApplicationRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -2454787007078344480L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private ApplicationYearlyDataInfo appYearlyDataInfo;

	@Column(columnDefinition="int default 0")
	private Integer totalAppsRevenue;

	private String benchMarkType;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ApplicationYearlyDataInfo getAppYearlyDataInfo() {
		return appYearlyDataInfo;
	}

	public void setAppYearlyDataInfo(ApplicationYearlyDataInfo appYearlyDataInfo) {
		this.appYearlyDataInfo = appYearlyDataInfo;
	}

	public Integer getTotalAppsRevenue() {
		return totalAppsRevenue;
	}

	public void setTotalAppsRevenue(Integer totalAppsRevenue) {
		this.totalAppsRevenue = totalAppsRevenue;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	public ApplicationRevenueInfo clone() {
		ApplicationRevenueInfo applicationRevenueInfo = null;
		try {
			applicationRevenueInfo = (ApplicationRevenueInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return applicationRevenueInfo;
	}
}
