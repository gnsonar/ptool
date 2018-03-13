package com.in.fujitsu.pricing.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Maninder
 *
 */
@Entity
@Table(name="DEAL_YEARLY_MASTER")
public class DealYearlyDataInfo implements Serializable {

	private static final long serialVersionUID = 2569115933868864051L;

	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name="dealId")
	private DealInfo dealInfo;

	private Integer noOfUsers;
	private Integer noOfSites;
	private Integer noOfDatacenters;
	private Integer year;
	private Float serviceGovernance;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DealInfo getDealInfo() {
		return dealInfo;
	}
	public void setDealInfo(DealInfo dealInfo) {
		this.dealInfo = dealInfo;
	}
	public Integer getNoOfUsers() {
		return noOfUsers;
	}
	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}
	public Integer getNoOfSites() {
		return noOfSites;
	}
	public void setNoOfSites(Integer noOfSites) {
		this.noOfSites = noOfSites;
	}
	public Integer getNoOfDatacenters() {
		return noOfDatacenters;
	}
	public void setNoOfDatacenters(Integer noOfDatacenters) {
		this.noOfDatacenters = noOfDatacenters;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Float getServiceGovernance() {
		return serviceGovernance;
	}
	public void setServiceGovernance(Float serviceGovernance) {
		this.serviceGovernance = serviceGovernance;
	}


}
