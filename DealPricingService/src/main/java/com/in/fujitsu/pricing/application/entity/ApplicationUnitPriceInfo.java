
package com.in.fujitsu.pricing.application.entity;

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

@Slf4j
@Entity
@Table(name = "APPLICATION_UNIT_PRICE")
@DynamicInsert
@DynamicUpdate
public class ApplicationUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = -1160552172750323501L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private ApplicationYearlyDataInfo appYearlyDataInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal totalAppsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal simpleAppsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal mediumAppsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal complexAppsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal veryComplexAppsUnitPrice;

	// Low or Target in case of Benchmark deal
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

	public BigDecimal getTotalAppsUnitPrice() {
		return totalAppsUnitPrice;
	}

	public void setTotalAppsUnitPrice(BigDecimal totalAppsUnitPrice) {
		this.totalAppsUnitPrice = totalAppsUnitPrice;
	}

	public BigDecimal getSimpleAppsUnitPrice() {
		return simpleAppsUnitPrice;
	}

	public void setSimpleAppsUnitPrice(BigDecimal simpleAppsUnitPrice) {
		this.simpleAppsUnitPrice = simpleAppsUnitPrice;
	}

	public BigDecimal getMediumAppsUnitPrice() {
		return mediumAppsUnitPrice;
	}

	public void setMediumAppsUnitPrice(BigDecimal mediumAppsUnitPrice) {
		this.mediumAppsUnitPrice = mediumAppsUnitPrice;
	}

	public BigDecimal getComplexAppsUnitPrice() {
		return complexAppsUnitPrice;
	}

	public void setComplexAppsUnitPrice(BigDecimal complexAppsUnitPrice) {
		this.complexAppsUnitPrice = complexAppsUnitPrice;
	}

	public BigDecimal getVeryComplexAppsUnitPrice() {
		return veryComplexAppsUnitPrice;
	}

	public void setVeryComplexAppsUnitPrice(BigDecimal veryComplexAppsUnitPrice) {
		this.veryComplexAppsUnitPrice = veryComplexAppsUnitPrice;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	public ApplicationUnitPriceInfo clone() {
		ApplicationUnitPriceInfo applicationUnitPriceInfo = null;
		try {
			applicationUnitPriceInfo = (ApplicationUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return applicationUnitPriceInfo;
	}

}
