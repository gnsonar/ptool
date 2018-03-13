package com.in.fujitsu.pricing.servicedesk.entity;

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
 * @author mishrasub
 *
 */
@Entity
@Table(name = "SERVICE_DESK_MASTER")
public class ServiceDeskInfo implements Serializable {

	private static final long serialVersionUID = 343002579973146967L;

	@Id
	@GeneratedValue
	private Long serviceDeskId;

	private boolean offshoreAllowed;

	private String levelOfService;

	private boolean multiLingual;

	private boolean toolingIncluded;

	private Integer selectedContactSolution;

	private Integer selectedContactRatio;

	private String levelIndicator;

	private String towerArchitect;

	@OneToMany(mappedBy = "serviceDeskInfo", cascade = CascadeType.ALL)
	private List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "dealId", nullable = false, unique = true)
	private DealInfo dealInfo;

	public Long getServiceDeskId() {
		return serviceDeskId;
	}

	public void setServiceDeskId(Long serviceDeskId) {
		this.serviceDeskId = serviceDeskId;
	}

	public boolean isOffshoreAllowed() {
		return offshoreAllowed;
	}

	public void setOffshoreAllowed(boolean offshoreAllowed) {
		this.offshoreAllowed = offshoreAllowed;
	}

	public String getLevelOfService() {
		return levelOfService;
	}

	public void setLevelOfService(String levelOfService) {
		this.levelOfService = levelOfService;
	}

	public boolean isMultiLingual() {
		return multiLingual;
	}

	public void setMultiLingual(boolean multiLingual) {
		this.multiLingual = multiLingual;
	}

	public boolean isToolingIncluded() {
		return toolingIncluded;
	}

	public void setToolingIncluded(boolean toolingIncluded) {
		this.toolingIncluded = toolingIncluded;
	}

	public Integer getSelectedContactSolution() {
		return selectedContactSolution;
	}

	public void setSelectedContactSolution(Integer selectedContactSolution) {
		this.selectedContactSolution = selectedContactSolution;
	}

	public List<ServiceDeskYearlyDataInfo> getServiceDeskYearlyDataInfoList() {
		return serviceDeskYearlyDataInfoList;
	}

	public void setServiceDeskYearlyDataInfoList(List<ServiceDeskYearlyDataInfo> serviceDeskYearlyDataInfoList) {
		this.serviceDeskYearlyDataInfoList = serviceDeskYearlyDataInfoList;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

	public String getLevelIndicator() {
		return levelIndicator;
	}

	public void setLevelIndicator(String levelIndicator) {
		this.levelIndicator = levelIndicator;
	}

	public String getTowerArchitect() {
		return towerArchitect;
	}

	public void setTowerArchitect(String towerArchitect) {
		this.towerArchitect = towerArchitect;
	}

	public Integer getSelectedContactRatio() {
		return selectedContactRatio;
	}

	public void setSelectedContactRatio(Integer selectedContactRatio) {
		this.selectedContactRatio = selectedContactRatio;
	}

}
