package com.in.fujitsu.pricing.scenario.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="SCENARIO_YEARLY")
public class ScenarioYearlyInfo implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long yearId;

	private Integer year;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "scenarioId", nullable = false)
	private ScenarioInfo scenarioInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal hosting;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal storage;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal endUser;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal network;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal serviceDesk;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal application;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal retail;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal towerSubtotal;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal additionalSubtotal;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal totalPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal serviceGov;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal transitionFees;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal migrationCost;

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public ScenarioInfo getScenarioInfo() {
		return scenarioInfo;
	}

	public void setScenarioInfo(ScenarioInfo scenarioInfo) {
		this.scenarioInfo = scenarioInfo;
	}

	public BigDecimal getHosting() {
		return hosting;
	}

	public void setHosting(BigDecimal hosting) {
		this.hosting = hosting;
	}

	public BigDecimal getStorage() {
		return storage;
	}

	public void setStorage(BigDecimal storage) {
		this.storage = storage;
	}

	public BigDecimal getEndUser() {
		return endUser;
	}

	public void setEndUser(BigDecimal endUser) {
		this.endUser = endUser;
	}

	public BigDecimal getNetwork() {
		return network;
	}

	public void setNetwork(BigDecimal network) {
		this.network = network;
	}

	public BigDecimal getServiceDesk() {
		return serviceDesk;
	}

	public void setServiceDesk(BigDecimal serviceDesk) {
		this.serviceDesk = serviceDesk;
	}

	public BigDecimal getApplication() {
		return application;
	}

	public void setApplication(BigDecimal application) {
		this.application = application;
	}

	public BigDecimal getRetail() {
		return retail;
	}

	public void setRetail(BigDecimal retail) {
		this.retail = retail;
	}

	public BigDecimal getTowerSubtotal() {
		return towerSubtotal;
	}

	public void setTowerSubtotal(BigDecimal towerSubtotal) {
		this.towerSubtotal = towerSubtotal;
	}

	public BigDecimal getAdditionalSubtotal() {
		return additionalSubtotal;
	}

	public void setAdditionalSubtotal(BigDecimal additionalSubtotal) {
		this.additionalSubtotal = additionalSubtotal;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getServiceGov() {
		return serviceGov;
	}

	public void setServiceGov(BigDecimal serviceGov) {
		this.serviceGov = serviceGov;
	}

	public BigDecimal getTransitionFees() {
		return transitionFees;
	}

	public void setTransitionFees(BigDecimal transitionFees) {
		this.transitionFees = transitionFees;
	}

	public BigDecimal getMigrationCost() {
		return migrationCost;
	}

	public void setMigrationCost(BigDecimal migrationCost) {
		this.migrationCost = migrationCost;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
