
package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name="STORAGE_UNIT_PRICE_MASTER")
@DynamicInsert
@DynamicUpdate
public class StorageUnitPriceInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -4657652568464901513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private StorageYearlyDataInfo storageYearlyDataInfo;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal storageVolumeUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal performanceUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal nonPerformanceUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal backupVolumeUnitPrice;

	// Low or Target in case of Benchmark deal
	private String benchMarkType;

	@Override
	public StorageUnitPriceInfo clone() {
		StorageUnitPriceInfo storageUnitPriceInfo = null;
		try {
			storageUnitPriceInfo = (StorageUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return storageUnitPriceInfo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public StorageYearlyDataInfo getStorageYearlyDataInfo() {
		return storageYearlyDataInfo;
	}

	public void setStorageYearlyDataInfo(StorageYearlyDataInfo storageYearlyDataInfo) {
		this.storageYearlyDataInfo = storageYearlyDataInfo;
	}

	public BigDecimal getStorageVolumeUnitPrice() {
		return storageVolumeUnitPrice;
	}

	public void setStorageVolumeUnitPrice(BigDecimal storageVolumeUnitPrice) {
		this.storageVolumeUnitPrice = storageVolumeUnitPrice;
	}

	public BigDecimal getPerformanceUnitPrice() {
		return performanceUnitPrice;
	}

	public void setPerformanceUnitPrice(BigDecimal performanceUnitPrice) {
		this.performanceUnitPrice = performanceUnitPrice;
	}

	public BigDecimal getNonPerformanceUnitPrice() {
		return nonPerformanceUnitPrice;
	}

	public void setNonPerformanceUnitPrice(BigDecimal nonPerformanceUnitPrice) {
		this.nonPerformanceUnitPrice = nonPerformanceUnitPrice;
	}

	public BigDecimal getBackupVolumeUnitPrice() {
		return backupVolumeUnitPrice;
	}

	public void setBackupVolumeUnitPrice(BigDecimal backupVolumeUnitPrice) {
		this.backupVolumeUnitPrice = backupVolumeUnitPrice;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}


}
