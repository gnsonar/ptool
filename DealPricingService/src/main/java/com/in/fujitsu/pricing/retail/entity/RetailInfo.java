package com.in.fujitsu.pricing.retail.entity;

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
@Table(name = "RETAIL_MASTER")
public class RetailInfo implements Serializable {

	private static final long serialVersionUID = -1744769739057786937L;

	@Id
	@GeneratedValue
	private Long retailId;

	private boolean offshoreAllowed;
	private boolean includeHardware;
	private String equipmentAge;
	private String equipmentSet;
	private String levelIndicator;
	private String towerArchitect;

	@OneToMany(mappedBy = "retailInfo", cascade = CascadeType.ALL)
	private List<RetailYearlyDataInfo> retailYearlyDataInfoList;

	@OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "dealId", nullable = false, unique = true)
	private DealInfo dealInfo;

	public Long getRetailId() {
		return retailId;
	}

	public void setRetailId(Long retailId) {
		this.retailId = retailId;
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

	public String getEquipmentAge() {
		return equipmentAge;
	}

	public void setEquipmentAge(String equipmentAge) {
		this.equipmentAge = equipmentAge;
	}

	public String getEquipmentSet() {
		return equipmentSet;
	}

	public void setEquipmentSet(String equipmentSet) {
		this.equipmentSet = equipmentSet;
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

	public List<RetailYearlyDataInfo> getRetailYearlyDataInfoList() {
		return retailYearlyDataInfoList;
	}

	public void setRetailYearlyDataInfoList(List<RetailYearlyDataInfo> retailYearlyDataInfoList) {
		this.retailYearlyDataInfoList = retailYearlyDataInfoList;
	}

	public DealInfo getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}

}
