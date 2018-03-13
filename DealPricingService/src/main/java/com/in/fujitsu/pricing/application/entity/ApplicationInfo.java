package com.in.fujitsu.pricing.application.entity;

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
@Table(name="APPLICATION_MASTER")
public class ApplicationInfo implements Serializable {

	private static final long serialVersionUID = -7600427903018374314L;

	@Id
	@GeneratedValue
	private Long id;
	private boolean offshoreAllowed;
	private String levelOfService;
	private Integer selectedAppSolution;
	private String towerArchitect;
	private String levelIndicator;

	@OneToMany(mappedBy = "appInfo", cascade = CascadeType.ALL)
	private List<ApplicationYearlyDataInfo> appYearlyDataInfos;

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

	public String getLevelOfService() {
		return levelOfService;
	}

	public void setLevelOfService(String levelOfService) {
		this.levelOfService = levelOfService;
	}

	public Integer getSelectedAppSolution() {
		return selectedAppSolution;
	}

	public void setSelectedAppSolution(Integer selectedAppSolution) {
		this.selectedAppSolution = selectedAppSolution;
	}

	public List<ApplicationYearlyDataInfo> getAppYearlyDataInfos() {
		return appYearlyDataInfos;
	}

	public void setAppYearlyDataInfos(List<ApplicationYearlyDataInfo> appYearlyDataInfos) {
		this.appYearlyDataInfos = appYearlyDataInfos;
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

}
