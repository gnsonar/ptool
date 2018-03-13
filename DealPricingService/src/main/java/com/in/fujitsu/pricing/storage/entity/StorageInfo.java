package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.in.fujitsu.pricing.entity.DealInfo;

/**
 * @author Maninder
 *
 */
@Entity
@Table(name="STORAGE_MASTER")
public class StorageInfo implements Serializable {


	private static final long serialVersionUID = 7475473176542235770L;

	@Id
	@GeneratedValue
	private Long id;
	private boolean offshoreAllowed;
	private String serviceWindowSla;
	private boolean includeHardware;
	private String backupFrequency;
	private Integer selectedSolution;
	private String towerArchitect;
	private String levelIndicator;


	@OneToMany(mappedBy = "storageInfo", cascade = CascadeType.ALL)
	private List<StorageYearlyDataInfo> storageYearlyDataInfos;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name="dealId",nullable = false, unique=true)
	private DealInfo dealInfo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isOffshoreAllowed() {
		return offshoreAllowed;
	}

	public void setOffshoreAllowed(boolean offshoreAllowed) {
		this.offshoreAllowed = offshoreAllowed;
	}

	public String getServiceWindowSla() {
		return serviceWindowSla;
	}

	public void setServiceWindowSla(String serviceWindowSla) {
		this.serviceWindowSla = serviceWindowSla;
	}

	public boolean isIncludeHardware() {
		return includeHardware;
	}

	public void setIncludeHardware(boolean includeHardware) {
		this.includeHardware = includeHardware;
	}

	public String getBackupFrequency() {
		return backupFrequency;
	}

	public void setBackupFrequency(String backupFrequency) {
		this.backupFrequency = backupFrequency;
	}

	public List<StorageYearlyDataInfo> getStorageYearlyDataInfos() {
		return storageYearlyDataInfos;
	}

	public void setStorageYearlyDataInfos(List<StorageYearlyDataInfo> storageYearlyDataInfos) {
		this.storageYearlyDataInfos = storageYearlyDataInfos;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

	public Integer getSelectedSolution() {
		return selectedSolution;
	}

	public void setSelectedSolution(Integer selectedSolution) {
		this.selectedSolution = selectedSolution;
	}

	public String getTowerArchitect() {
		return towerArchitect;
	}

	public void setTowerArchitect(String towerArchitect) {
		this.towerArchitect = towerArchitect;
	}

	public String getLevelIndicator() {
		return levelIndicator;
	}

	public void setLevelIndicator(String levelIndicator) {
		this.levelIndicator = levelIndicator;
	}

}
