package com.in.fujitsu.pricing.enduser.entity;

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

@Entity
@Table(name="END_USER_MASTER")
public class EndUserInfo implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	@Id
	@GeneratedValue
	private Long endUserId;

	private boolean offshoreAllowed;
	private boolean includeBreakFix;
	private boolean includeHardware;
	private String resolutionTime;
	private String imacType;
	private Integer selectedSolution;
	private String levelIndicator;
	private String towerArchitect;
	private Integer selectedContactSolution;

	@OneToMany(mappedBy = "endUserInfo", cascade = CascadeType.ALL)
	private List<EndUserYearlyDataInfo> endUserYearlyDataInfoList;

	@OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name="dealId", nullable = false, unique = true)
	private DealInfo dealInfo;

	public Long getEndUserId() {
		return endUserId;
	}

	public void setEndUserId(Long endUserId) {
		this.endUserId = endUserId;
	}

	public boolean isOffshoreAllowed() {
		return offshoreAllowed;
	}

	public void setOffshoreAllowed(boolean offshoreAllowed) {
		this.offshoreAllowed = offshoreAllowed;
	}

	public boolean isIncludeBreakFix() {
		return includeBreakFix;
	}

	public void setIncludeBreakFix(boolean includeBreakFix) {
		this.includeBreakFix = includeBreakFix;
	}

	public boolean isIncludeHardware() {
		return includeHardware;
	}

	public void setIncludeHardware(boolean includeHardware) {
		this.includeHardware = includeHardware;
	}

	public String getResolutionTime() {
		return resolutionTime;
	}

	public void setResolutionTime(String resolutionTime) {
		this.resolutionTime = resolutionTime;
	}

	public String getImacType() {
		return imacType;
	}

	public void setImacType(String imacType) {
		this.imacType = imacType;
	}

	public Integer getSelectedSolution() {
		return selectedSolution;
	}

	public void setSelectedSolution(Integer selectedSolution) {
		this.selectedSolution = selectedSolution;
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

	public List<EndUserYearlyDataInfo> getEndUserYearlyDataInfoList() {
		return endUserYearlyDataInfoList;
	}

	public void setEndUserYearlyDataInfoList(List<EndUserYearlyDataInfo> endUserYearlyDataInfoList) {
		this.endUserYearlyDataInfoList = endUserYearlyDataInfoList;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

	public Integer getSelectedContactSolution() {
		return selectedContactSolution;
	}

	public void setSelectedContactSolution(Integer selectedContactSolution) {
		this.selectedContactSolution = selectedContactSolution;
	}

}
