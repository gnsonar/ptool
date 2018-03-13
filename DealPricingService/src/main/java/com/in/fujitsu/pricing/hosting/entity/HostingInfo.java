package com.in.fujitsu.pricing.hosting.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.in.fujitsu.pricing.entity.DealInfo;


/**
 * @author pawarbh
 *
 */
@Entity
@Table(name = "HOSTING_MASTER")
public class HostingInfo implements Serializable {

	private static final long serialVersionUID = 3838453584223800242L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long hostingId;

	private boolean offshoreAllowed;
	private String levelOfService;
	private boolean includeHardware;
	private boolean includeTooling;
	private String coLocation;
	private Integer selectedSolutionId;
	private String levelIndicator;
	private String towerArchitect;

	@OneToMany(mappedBy = "hostingInfo", cascade = CascadeType.ALL)
	private List<HostingYearlyDataInfo> hostingYearlyDataInfoList;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "dealId", nullable = false, unique = true)
	private DealInfo dealInfo;

	public Long getHostingId() {
		return hostingId;
	}

	public void setHostingId(Long hostingId) {
		this.hostingId = hostingId;
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

	public boolean isIncludeHardware() {
		return includeHardware;
	}

	public void setIncludeHardware(boolean includeHardware) {
		this.includeHardware = includeHardware;
	}

	public boolean isIncludeTooling() {
		return includeTooling;
	}

	public void setIncludeTooling(boolean includeTooling) {
		this.includeTooling = includeTooling;
	}

	public String getCoLocation() {
		return coLocation;
	}

	public void setCoLocation(String coLocation) {
		this.coLocation = coLocation;
	}

	public Integer getSelectedSolutionId() {
		return selectedSolutionId;
	}

	public void setSelectedSolutionId(Integer selectedSolutionId) {
		this.selectedSolutionId = selectedSolutionId;
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

	public List<HostingYearlyDataInfo> getHostingYearlyDataInfoList() {
		return hostingYearlyDataInfoList;
	}

	public void setHostingYearlyDataInfoList(List<HostingYearlyDataInfo> hostingYearlyDataInfoList) {
		this.hostingYearlyDataInfoList = hostingYearlyDataInfoList;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

	
}
