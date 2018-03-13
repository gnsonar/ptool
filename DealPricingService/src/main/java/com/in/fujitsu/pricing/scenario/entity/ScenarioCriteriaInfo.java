package com.in.fujitsu.pricing.scenario.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="SCENARIO_CRITERIA")
public class ScenarioCriteriaInfo implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long criteriaId;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "scenarioId", nullable = false, unique = true)
	private ScenarioInfo scenarioInfo;

	private boolean hostingOffshore;
	private boolean hostingHardware;
	private String hostingLevelOfService;
	private boolean hostingTooling;
	private String hostingcoLocation;

	private boolean storageOffshore;
	private boolean storageHardware;
	private String storageLevelOfService;

	private boolean endUserOffshore;
	private boolean endUserHardware;
	private boolean endUserBreakFix;
	private String endUserResolutionTime;

	private boolean networkOffshore;
	private boolean networkHardware;
	private String networkLevelOfService;

	private boolean serviceDeskOffshore;
	private String serviceDeskLevelOfService;
	private boolean serviceDeskTooling;
	private boolean serviceDeskMultiLingual;

	private boolean applicationOffshore;
	private String applicationLevelOfService;

	private boolean retailOffshore;
	private boolean retailHardware;
	private String retailEquipmentAge;
	private String retailEquipmentSet;

	public Long getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
	}
	public boolean isHostingOffshore() {
		return hostingOffshore;
	}
	public void setHostingOffshore(boolean hostingOffshore) {
		this.hostingOffshore = hostingOffshore;
	}
	public boolean isHostingHardware() {
		return hostingHardware;
	}
	public void setHostingHardware(boolean hostingHardware) {
		this.hostingHardware = hostingHardware;
	}
	public String getHostingLevelOfService() {
		return hostingLevelOfService;
	}
	public void setHostingLevelOfService(String hostingLevelOfService) {
		this.hostingLevelOfService = hostingLevelOfService;
	}
	public boolean isHostingTooling() {
		return hostingTooling;
	}
	public void setHostingTooling(boolean hostingTooling) {
		this.hostingTooling = hostingTooling;
	}
	public String getHostingcoLocation() {
		return hostingcoLocation;
	}
	public void setHostingcoLocation(String hostingcoLocation) {
		this.hostingcoLocation = hostingcoLocation;
	}
	public boolean isStorageOffshore() {
		return storageOffshore;
	}
	public void setStorageOffshore(boolean storageOffshore) {
		this.storageOffshore = storageOffshore;
	}
	public boolean isStorageHardware() {
		return storageHardware;
	}
	public void setStorageHardware(boolean storageHardware) {
		this.storageHardware = storageHardware;
	}
	public String getStorageLevelOfService() {
		return storageLevelOfService;
	}
	public void setStorageLevelOfService(String storageLevelOfService) {
		this.storageLevelOfService = storageLevelOfService;
	}
	public boolean isEndUserOffshore() {
		return endUserOffshore;
	}
	public void setEndUserOffshore(boolean endUserOffshore) {
		this.endUserOffshore = endUserOffshore;
	}
	public boolean isEndUserHardware() {
		return endUserHardware;
	}
	public void setEndUserHardware(boolean endUserHardware) {
		this.endUserHardware = endUserHardware;
	}
	public boolean isEndUserBreakFix() {
		return endUserBreakFix;
	}
	public void setEndUserBreakFix(boolean endUserBreakFix) {
		this.endUserBreakFix = endUserBreakFix;
	}
	public String getEndUserResolutionTime() {
		return endUserResolutionTime;
	}
	public void setEndUserResolutionTime(String endUserResolutionTime) {
		this.endUserResolutionTime = endUserResolutionTime;
	}
	public boolean isNetworkOffshore() {
		return networkOffshore;
	}
	public void setNetworkOffshore(boolean networkOffshore) {
		this.networkOffshore = networkOffshore;
	}
	public boolean isNetworkHardware() {
		return networkHardware;
	}
	public void setNetworkHardware(boolean networkHardware) {
		this.networkHardware = networkHardware;
	}
	public String getNetworkLevelOfService() {
		return networkLevelOfService;
	}
	public void setNetworkLevelOfService(String networkLevelOfService) {
		this.networkLevelOfService = networkLevelOfService;
	}
	public boolean isServiceDeskOffshore() {
		return serviceDeskOffshore;
	}
	public void setServiceDeskOffshore(boolean serviceDeskOffshore) {
		this.serviceDeskOffshore = serviceDeskOffshore;
	}
	public String getServiceDeskLevelOfService() {
		return serviceDeskLevelOfService;
	}
	public void setServiceDeskLevelOfService(String serviceDeskLevelOfService) {
		this.serviceDeskLevelOfService = serviceDeskLevelOfService;
	}
	public boolean isServiceDeskTooling() {
		return serviceDeskTooling;
	}
	public void setServiceDeskTooling(boolean serviceDeskTooling) {
		this.serviceDeskTooling = serviceDeskTooling;
	}
	public boolean isServiceDeskMultiLingual() {
		return serviceDeskMultiLingual;
	}
	public void setServiceDeskMultiLingual(boolean serviceDeskMultiLingual) {
		this.serviceDeskMultiLingual = serviceDeskMultiLingual;
	}
	public boolean isApplicationOffshore() {
		return applicationOffshore;
	}
	public void setApplicationOffshore(boolean applicationOffshore) {
		this.applicationOffshore = applicationOffshore;
	}
	public String getApplicationLevelOfService() {
		return applicationLevelOfService;
	}
	public void setApplicationLevelOfService(String applicationLevelOfService) {
		this.applicationLevelOfService = applicationLevelOfService;
	}
	public boolean isRetailOffshore() {
		return retailOffshore;
	}
	public void setRetailOffshore(boolean retailOffshore) {
		this.retailOffshore = retailOffshore;
	}
	public boolean isRetailHardware() {
		return retailHardware;
	}
	public void setRetailHardware(boolean retailHardware) {
		this.retailHardware = retailHardware;
	}
	public String getRetailEquipmentAge() {
		return retailEquipmentAge;
	}
	public void setRetailEquipmentAge(String retailEquipmentAge) {
		this.retailEquipmentAge = retailEquipmentAge;
	}
	public String getRetailEquipmentSet() {
		return retailEquipmentSet;
	}
	public void setRetailEquipmentSet(String retailEquipmentSet) {
		this.retailEquipmentSet = retailEquipmentSet;
	}
	public ScenarioInfo getScenarioInfo() {
		return scenarioInfo;
	}
	public void setScenarioInfo(ScenarioInfo scenarioInfo) {
		this.scenarioInfo = scenarioInfo;
	}



}
