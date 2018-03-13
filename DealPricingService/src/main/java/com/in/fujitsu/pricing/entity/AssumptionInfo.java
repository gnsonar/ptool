package com.in.fujitsu.pricing.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "assumption_master")
public class AssumptionInfo extends TimestampInfo {

	@Id
	@GeneratedValue
	private int id;
	private String towerName;
	private String status;

	@OneToMany(mappedBy = "assumptionMaster" , fetch = FetchType.EAGER)
	List<AssumptionDetailInfo> assumptionDetailsList = new ArrayList<AssumptionDetailInfo>();


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTowerName() {
		return towerName;
	}

	public void setTowerName(String towerName) {
		this.towerName = towerName;
	}

	public List<AssumptionDetailInfo> getAssumptionDetailsList() {
		return assumptionDetailsList;
	}

	public void setAssumptionDetailsList(List<AssumptionDetailInfo> assumptionDetailsList) {
		this.assumptionDetailsList = assumptionDetailsList;
	}

}
