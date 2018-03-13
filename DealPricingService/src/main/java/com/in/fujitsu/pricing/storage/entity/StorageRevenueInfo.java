
package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;

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
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name="STORAGE_REVENUE")
public class StorageRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -4657652568464901513L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private StorageYearlyDataInfo storageYearlyDataInfo;

	@Column(columnDefinition="int default 0")
	private Integer storageRevenue;

	@Column(columnDefinition="int default 0")
	private Integer backupRevenue;

	private String benchMarkType;

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

	public Integer getStorageRevenue() {
		return storageRevenue;
	}

	public void setStorageRevenue(Integer storageRevenue) {
		this.storageRevenue = storageRevenue;
	}

	public Integer getBackupRevenue() {
		return backupRevenue;
	}

	public void setBackupRevenue(Integer backupRevenue) {
		this.backupRevenue = backupRevenue;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	public StorageRevenueInfo clone() {
		StorageRevenueInfo storageRevenueInfo = null;
		try {
			storageRevenueInfo = (StorageRevenueInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return storageRevenueInfo;
	}

}
