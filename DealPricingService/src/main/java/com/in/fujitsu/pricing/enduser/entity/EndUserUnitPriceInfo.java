package com.in.fujitsu.pricing.enduser.entity;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "END_USER_UNIT_PRICE")
@DynamicInsert
public class EndUserUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 2933203746585193258L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private EndUserYearlyDataInfo endUserYearlyDataInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal endUserDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal laptops;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal highEndLaptops;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal standardLaptops;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal desktops;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal thinClients;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal mobileDevices;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal imacDevices;

	private String benchMarkType;



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public EndUserYearlyDataInfo getEndUserYearlyDataInfo() {
		return endUserYearlyDataInfo;
	}

	public void setEndUserYearlyDataInfo(EndUserYearlyDataInfo endUserYearlyDataInfo) {
		this.endUserYearlyDataInfo = endUserYearlyDataInfo;
	}

	public BigDecimal getEndUserDevices() {
		return endUserDevices;
	}

	public void setEndUserDevices(BigDecimal endUserDevices) {
		this.endUserDevices = endUserDevices;
	}

	public BigDecimal getLaptops() {
		return laptops;
	}

	public void setLaptops(BigDecimal laptops) {
		this.laptops = laptops;
	}

	public BigDecimal getHighEndLaptops() {
		return highEndLaptops;
	}

	public void setHighEndLaptops(BigDecimal highEndLaptops) {
		this.highEndLaptops = highEndLaptops;
	}

	public BigDecimal getStandardLaptops() {
		return standardLaptops;
	}

	public void setStandardLaptops(BigDecimal standardLaptops) {
		this.standardLaptops = standardLaptops;
	}

	public BigDecimal getDesktops() {
		return desktops;
	}

	public void setDesktops(BigDecimal desktops) {
		this.desktops = desktops;
	}

	public BigDecimal getThinClients() {
		return thinClients;
	}

	public void setThinClients(BigDecimal thinClients) {
		this.thinClients = thinClients;
	}

	public BigDecimal getMobileDevices() {
		return mobileDevices;
	}

	public void setMobileDevices(BigDecimal mobileDevices) {
		this.mobileDevices = mobileDevices;
	}

	public BigDecimal getImacDevices() {
		return imacDevices;
	}

	public void setImacDevices(BigDecimal imacDevices) {
		this.imacDevices = imacDevices;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	public EndUserUnitPriceInfo clone() {
		EndUserUnitPriceInfo endUserUnitPriceInfo = null;
		try {
			endUserUnitPriceInfo = (EndUserUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return endUserUnitPriceInfo;
	}

}
