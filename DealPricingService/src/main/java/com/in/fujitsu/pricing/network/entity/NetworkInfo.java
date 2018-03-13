package com.in.fujitsu.pricing.network.entity;

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
 * @author ChhabrMa
 *
 */
@Entity
@Table(name = "NETWORK_MASTER")
public class NetworkInfo implements Serializable {

	private static final long serialVersionUID = 3838453584223800242L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long networkId;

	private boolean offshoreAllowed;
	private boolean includeHardware;
	private String levelOfService;
	private Integer selectedWanSolutionId;
	private Integer selectedLanSolutionId;
	private String levelIndicator;
	private String towerArchitect;

	@OneToMany(mappedBy = "networkInfo", cascade = CascadeType.ALL)
	private List<NetworkYearlyDataInfo> networkYearlyDataInfoList;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "dealId", nullable = false, unique = true)
	private DealInfo dealInfo;

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public boolean isOffshoreAllowed() {
		return offshoreAllowed;
	}

	public void setOffshoreAllowed(boolean offshoreAllowed) {
		this.offshoreAllowed = offshoreAllowed;
	}

	public boolean isIncludeHardware() {
		return includeHardware;
	}

	public void setIncludeHardware(boolean includeHardware) {
		this.includeHardware = includeHardware;
	}

	public String getLevelOfService() {
		return levelOfService;
	}

	public void setLevelOfService(String levelOfService) {
		this.levelOfService = levelOfService;
	}

	public Integer getSelectedWanSolutionId() {
		return selectedWanSolutionId;
	}

	public void setSelectedWanSolutionId(Integer selectedWanSolutionId) {
		this.selectedWanSolutionId = selectedWanSolutionId;
	}

	public Integer getSelectedLanSolutionId() {
		return selectedLanSolutionId;
	}

	public void setSelectedLanSolutionId(Integer selectedLanSolutionId) {
		this.selectedLanSolutionId = selectedLanSolutionId;
	}

	public List<NetworkYearlyDataInfo> getNetworkYearlyDataInfoList() {
		return networkYearlyDataInfoList;
	}

	public void setNetworkYearlyDataInfoList(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		this.networkYearlyDataInfoList = networkYearlyDataInfoList;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

	/**
	 * @return the levelIndicator
	 */
	public String getLevelIndicator() {
		return levelIndicator;
	}

	/**
	 * @param levelIndicator the levelIndicator to set
	 */
	public void setLevelIndicator(String levelIndicator) {
		this.levelIndicator = levelIndicator;
	}

	/**
	 * @return the towerArchitect
	 */
	public String getTowerArchitect() {
		return towerArchitect;
	}

	/**
	 * @param towerArchitect the towerArchitect to set
	 */
	public void setTowerArchitect(String towerArchitect) {
		this.towerArchitect = towerArchitect;
	}

}
