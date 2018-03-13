package com.in.fujitsu.pricing.network.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NETWORK_LAN_SOLUTION")
public class NetworkLanSolutionInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer solutionId;
	
	private String solutionName;
	
	private String solutionDesc;
	
	private float smallPerc;
	
	private float mediumPerc;
	
	private float largePerc;
	
	public Integer getSolutionId() {
		return solutionId;
	}
	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	public String getSolutionDesc() {
		return solutionDesc;
	}
	public void setSolutionDesc(String solutionDesc) {
		this.solutionDesc = solutionDesc;
	}
	public float getSmallPerc() {
		return smallPerc;
	}
	public void setSmallPerc(float smallPerc) {
		this.smallPerc = smallPerc;
	}
	public float getMediumPerc() {
		return mediumPerc;
	}
	public void setMediumPerc(float mediumPerc) {
		this.mediumPerc = mediumPerc;
	}
	public float getLargePerc() {
		return largePerc;
	}
	public void setLargePerc(float largePerc) {
		this.largePerc = largePerc;
	}
	
	
	
}
