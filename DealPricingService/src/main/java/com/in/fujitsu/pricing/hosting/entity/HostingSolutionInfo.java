package com.in.fujitsu.pricing.hosting.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOSTING_SOLUTION")
public class HostingSolutionInfo implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	@Id
	@GeneratedValue
	private Integer solutionId;
	
	private String solutionName;
	
	private String solutionDesc;
	
	@Column(scale = 2)
	private BigDecimal physicalPerc;

	@Column(scale = 2)
	private BigDecimal virtualPerc;

	@Column(scale = 2)
	private BigDecimal publicPerc;

	@Column(scale = 2)
	private BigDecimal privatePerc;
	
	@Column(scale = 2)
	private BigDecimal winLinuxPerc;
	
	@Column(scale = 2)
	private BigDecimal unixPerc;
	
	@Column(scale = 2)
	private BigDecimal smallPerc;
	
	@Column(scale = 2)
	private BigDecimal mediumPerc;
	
	@Column(scale = 2)
	private BigDecimal largePerc;

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

	public BigDecimal getPhysicalPerc() {
		return physicalPerc;
	}

	public void setPhysicalPerc(BigDecimal physicalPerc) {
		this.physicalPerc = physicalPerc;
	}

	public BigDecimal getVirtualPerc() {
		return virtualPerc;
	}

	public void setVirtualPerc(BigDecimal virtualPerc) {
		this.virtualPerc = virtualPerc;
	}

	public BigDecimal getPublicPerc() {
		return publicPerc;
	}

	public void setPublicPerc(BigDecimal publicPerc) {
		this.publicPerc = publicPerc;
	}

	public BigDecimal getPrivatePerc() {
		return privatePerc;
	}

	public void setPrivatePerc(BigDecimal privatePerc) {
		this.privatePerc = privatePerc;
	}

	public BigDecimal getWinLinuxPerc() {
		return winLinuxPerc;
	}

	public void setWinLinuxPerc(BigDecimal winLinuxPerc) {
		this.winLinuxPerc = winLinuxPerc;
	}

	public BigDecimal getUnixPerc() {
		return unixPerc;
	}

	public void setUnixPerc(BigDecimal unixPerc) {
		this.unixPerc = unixPerc;
	}

	public BigDecimal getSmallPerc() {
		return smallPerc;
	}

	public void setSmallPerc(BigDecimal smallPerc) {
		this.smallPerc = smallPerc;
	}

	public BigDecimal getMediumPerc() {
		return mediumPerc;
	}

	public void setMediumPerc(BigDecimal mediumPerc) {
		this.mediumPerc = mediumPerc;
	}

	public BigDecimal getLargePerc() {
		return largePerc;
	}

	public void setLargePerc(BigDecimal largePerc) {
		this.largePerc = largePerc;
	}

}
