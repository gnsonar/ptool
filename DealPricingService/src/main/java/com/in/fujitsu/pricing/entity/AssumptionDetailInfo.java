package com.in.fujitsu.pricing.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "assumption_details")
public class AssumptionDetailInfo extends TimestampInfo {

	@Id
	@GeneratedValue
	private int id;
	private String assumptionDesc;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "assumptionId")
	private AssumptionInfo assumptionMaster;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAssumptionDesc() {
		return assumptionDesc;
	}
	public void setAssumptionDesc(String assumptionDesc) {
		this.assumptionDesc = assumptionDesc;
	}
	public AssumptionInfo getAssumptionMaster() {
		return assumptionMaster;
	}
	public void setAssumptionMaster(AssumptionInfo assumptionMaster) {
		this.assumptionMaster = assumptionMaster;
	}

}
