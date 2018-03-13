package com.in.fujitsu.pricing.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Maninder
 *
 */
@Entity
@Table(name = "ACCESS_MASTER")
public class UserInfo implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 2630977271667068169L;

	@Id
	@GeneratedValue
	private Long userId;

	@Column(unique=true, nullable=false)
	private String userName;
	private String emailId;
	private Short gender;
	private String region;
	private String reasonForAccess;
	private String fullName;
	private String status;
	private String approvedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@JsonIgnore
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<DealInfo> dealInfo;

	private String role;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	private Date tokenExpiry;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Short getGender() {
		return gender;
	}

	public void setGender(Short gender) {
		this.gender = gender;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getReasonForAccess() {
		return reasonForAccess;
	}

	public void setReasonForAccess(String reasonForAccess) {
		this.reasonForAccess = reasonForAccess;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<DealInfo> getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(List<DealInfo> dealInfo) {
		this.dealInfo = dealInfo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(Date tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}



}
