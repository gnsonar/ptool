
package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "STORAGE_YEARLY_MASTER")
@DynamicInsert
@DynamicUpdate
public class StorageYearlyDataInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 8392629066893021651L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "storageId")
	private StorageInfo storageInfo;

	@Column(columnDefinition="int default 0")
	private Integer storageVolume;

	@Column(columnDefinition="int default 0")
	private Integer performanceStorage;

	@Column(columnDefinition="int default 0")
	private Integer nonPerformanceStorage;

	@Column(columnDefinition="int default 0")
	private Integer backupVolume;
	private Integer year;

	@JsonIgnore
	@OneToMany(mappedBy = "storageYearlyDataInfo", cascade = CascadeType.ALL)
	private List<StorageUnitPriceInfo> storageUnitPriceInfo;

	@JsonIgnore
	@OneToMany(mappedBy = "storageYearlyDataInfo", cascade = CascadeType.ALL)
	private List<StorageRevenueInfo> storageRevenueInfoList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StorageInfo getStorageInfo() {
		return storageInfo;
	}

	public void setStorageInfo(StorageInfo storageInfo) {
		this.storageInfo = storageInfo;
	}

	public Integer getStorageVolume() {
		return storageVolume;
	}

	public void setStorageVolume(Integer storageVolume) {
		this.storageVolume = storageVolume;
	}

	public Integer getPerformanceStorage() {
		return performanceStorage;
	}

	public void setPerformanceStorage(Integer performanceStorage) {
		this.performanceStorage = performanceStorage;
	}

	public Integer getNonPerformanceStorage() {
		return nonPerformanceStorage;
	}

	public void setNonPerformanceStorage(Integer nonPerformanceStorage) {
		this.nonPerformanceStorage = nonPerformanceStorage;
	}

	public Integer getBackupVolume() {
		return backupVolume;
	}

	public void setBackupVolume(Integer backupVolume) {
		this.backupVolume = backupVolume;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<StorageUnitPriceInfo> getStorageUnitPriceInfo() {
		return storageUnitPriceInfo;
	}

	public void setStorageUnitPriceInfo(List<StorageUnitPriceInfo> storageUnitPriceInfo) {
		this.storageUnitPriceInfo = storageUnitPriceInfo;
	}

	@Override
	public StorageYearlyDataInfo clone() {
		StorageYearlyDataInfo cloneStorageYearlyDataInfo = null;
		try {
			cloneStorageYearlyDataInfo = (StorageYearlyDataInfo) super.clone();
			 List<StorageUnitPriceInfo> storageUnitPriceInfoList = new ArrayList<>();
			 for(StorageUnitPriceInfo storageUnitPriceInfo : cloneStorageYearlyDataInfo.getStorageUnitPriceInfo()){
				 StorageUnitPriceInfo cloneUnitPrice = storageUnitPriceInfo.clone();
				 cloneUnitPrice.setStorageYearlyDataInfo(cloneStorageYearlyDataInfo);
				 storageUnitPriceInfoList.add(cloneUnitPrice);
			 }
			 cloneStorageYearlyDataInfo.setStorageUnitPriceInfo(storageUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneStorageYearlyDataInfo;
	}

	public List<StorageRevenueInfo> getStorageRevenueInfoList() {
		return storageRevenueInfoList;
	}

	public void setStorageRevenueInfoList(List<StorageRevenueInfo> storageRevenueInfoList) {
		this.storageRevenueInfoList = storageRevenueInfoList;
	}

}
