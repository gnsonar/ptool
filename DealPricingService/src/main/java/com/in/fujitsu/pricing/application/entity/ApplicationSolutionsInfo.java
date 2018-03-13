package com.in.fujitsu.pricing.application.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APPLICATION_SOLUTION")
public class ApplicationSolutionsInfo implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	@Id
	@GeneratedValue
	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	@Column(scale = 2)
	private BigDecimal simplePerc;

	@Column(scale = 2)
	private BigDecimal mediumPerc;

	@Column(scale = 2)
	private BigDecimal complexPerc;

	@Column(scale = 2)
	private BigDecimal veryComplexPerc;

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

	public BigDecimal getSimplePerc() {
		return simplePerc;
	}

	public void setSimplePerc(BigDecimal simplePerc) {
		this.simplePerc = simplePerc;
	}

	public BigDecimal getMediumPerc() {
		return mediumPerc;
	}

	public void setMediumPerc(BigDecimal mediumPerc) {
		this.mediumPerc = mediumPerc;
	}

	public BigDecimal getComplexPerc() {
		return complexPerc;
	}

	public void setComplexPerc(BigDecimal complexPerc) {
		this.complexPerc = complexPerc;
	}

	public BigDecimal getVeryComplexPerc() {
		return veryComplexPerc;
	}

	public void setVeryComplexPerc(BigDecimal veryComplexPerc) {
		this.veryComplexPerc = veryComplexPerc;
	}

}
