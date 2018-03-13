package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="END_USER_SOLUTION")
public class EndUserSolutionInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer solutionId;
	
	private String solutionName;
	
	private String solutionDesc;
	
	@Column(scale = 2)
	private BigDecimal laptopPerc;
	
	@Column(scale = 2)
	private BigDecimal highEndLaptopPerc;
	
	@Column(scale = 2)
	private BigDecimal standardLaptopPerc;
	
	@Column(scale = 2)
	private BigDecimal desktopPerc;
	
	@Column(scale = 2)
	private BigDecimal thinClientPerc;
	
	@Column(scale = 2)
	private BigDecimal mobilePerc;

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

	public BigDecimal getLaptopPerc() {
		return laptopPerc;
	}

	public void setLaptopPerc(BigDecimal laptopPerc) {
		this.laptopPerc = laptopPerc;
	}

	public BigDecimal getHighEndLaptopPerc() {
		return highEndLaptopPerc;
	}

	public void setHighEndLaptopPerc(BigDecimal highEndLaptopPerc) {
		this.highEndLaptopPerc = highEndLaptopPerc;
	}

	public BigDecimal getStandardLaptopPerc() {
		return standardLaptopPerc;
	}

	public void setStandardLaptopPerc(BigDecimal standardLaptopPerc) {
		this.standardLaptopPerc = standardLaptopPerc;
	}

	public BigDecimal getDesktopPerc() {
		return desktopPerc;
	}

	public void setDesktopPerc(BigDecimal desktopPerc) {
		this.desktopPerc = desktopPerc;
	}

	public BigDecimal getThinClientPerc() {
		return thinClientPerc;
	}

	public void setThinClientPerc(BigDecimal thinClientPerc) {
		this.thinClientPerc = thinClientPerc;
	}

	public BigDecimal getMobilePerc() {
		return mobilePerc;
	}

	public void setMobilePerc(BigDecimal mobilePerc) {
		this.mobilePerc = mobilePerc;
	}

	
	
}
