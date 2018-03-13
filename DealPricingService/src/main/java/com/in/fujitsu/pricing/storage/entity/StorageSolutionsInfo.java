package com.in.fujitsu.pricing.storage.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STORAGE_SOLUTION")
public class StorageSolutionsInfo implements Serializable {

	private static final long serialVersionUID = 4529141818309671831L;
	@Id
	@GeneratedValue
	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	private Float performanceValue;
	private Float nonPerformanceValue;

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
	public Float getPerformanceValue() {
		return performanceValue;
	}
	public void setPerformanceValue(Float performanceValue) {
		this.performanceValue = performanceValue;
	}
	public Float getNonPerformanceValue() {
		return nonPerformanceValue;
	}
	public void setNonPerformanceValue(Float nonPerformanceValue) {
		this.nonPerformanceValue = nonPerformanceValue;
	}
	public String getSolutionDesc() {
		return solutionDesc;
	}
	public void setSolutionDesc(String solutionDesc) {
		this.solutionDesc = solutionDesc;
	}



}
