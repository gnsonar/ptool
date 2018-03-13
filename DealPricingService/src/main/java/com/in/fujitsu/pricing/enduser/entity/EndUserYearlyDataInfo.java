package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "END_USER_YEARLY_VOLUME")
@DynamicInsert
public class EndUserYearlyDataInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 7156725323525534401L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long yearId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "endUserId")
	private EndUserInfo endUserInfo;

	@Column(columnDefinition = "int default 0")
	private Integer endUserDevices;

	@Column(columnDefinition = "int default 0")
	private Integer laptops;

	@Column(columnDefinition = "int default 0")
	private Integer highEndLaptops;

	@Column(columnDefinition = "int default 0")
	private Integer standardLaptops;

	@Column(columnDefinition = "int default 0")
	private Integer desktops;

	@Column(columnDefinition = "int default 0")
	private Integer thinClients;

	@Column(columnDefinition = "int default 0")
	private Integer mobileDevices;

	@Column(columnDefinition = "int default 0")
	private Integer imacDevices;

	private Integer year;

	@JsonIgnore
	@OneToMany(mappedBy = "endUserYearlyDataInfo", cascade = CascadeType.ALL)
	private List<EndUserUnitPriceInfo> endUserUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "endUserYearlyDataInfo", cascade = CascadeType.ALL)
	private List<EndUserRevenueInfo> endUserRevenueInfoList;


	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public EndUserInfo getEndUserInfo() {
		return endUserInfo;
	}

	public void setEndUserInfo(EndUserInfo endUserInfo) {
		this.endUserInfo = endUserInfo;
	}

	public Integer getEndUserDevices() {
		return endUserDevices;
	}

	public void setEndUserDevices(Integer endUserDevices) {
		this.endUserDevices = endUserDevices;
	}

	public Integer getLaptops() {
		return laptops;
	}

	public void setLaptops(Integer laptops) {
		this.laptops = laptops;
	}

	public Integer getHighEndLaptops() {
		return highEndLaptops;
	}

	public void setHighEndLaptops(Integer highEndLaptops) {
		this.highEndLaptops = highEndLaptops;
	}

	public Integer getStandardLaptops() {
		return standardLaptops;
	}

	public void setStandardLaptops(Integer standardLaptops) {
		this.standardLaptops = standardLaptops;
	}

	public Integer getDesktops() {
		return desktops;
	}

	public void setDesktops(Integer desktops) {
		this.desktops = desktops;
	}

	public Integer getThinClients() {
		return thinClients;
	}

	public void setThinClients(Integer thinClients) {
		this.thinClients = thinClients;
	}

	public Integer getMobileDevices() {
		return mobileDevices;
	}

	public void setMobileDevices(Integer mobileDevices) {
		this.mobileDevices = mobileDevices;
	}

	public Integer getImacDevices() {
		return imacDevices;
	}

	public void setImacDevices(Integer imacDevices) {
		this.imacDevices = imacDevices;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<EndUserUnitPriceInfo> getEndUserUnitPriceInfoList() {
		return endUserUnitPriceInfoList;
	}

	public void setEndUserUnitPriceInfoList(List<EndUserUnitPriceInfo> endUserUnitPriceInfoList) {
		this.endUserUnitPriceInfoList = endUserUnitPriceInfoList;
	}

	@Override
	public EndUserYearlyDataInfo clone() {
		EndUserYearlyDataInfo cloneEndUserYearlyDataInfo = null;
		try {
			cloneEndUserYearlyDataInfo = (EndUserYearlyDataInfo) super.clone();
			List<EndUserUnitPriceInfo> endUserUnitPriceInfoList = new ArrayList<>();
			for (EndUserUnitPriceInfo endUserUnitPriceInfo : cloneEndUserYearlyDataInfo.getEndUserUnitPriceInfoList()) {
				EndUserUnitPriceInfo cloneUnitPrice = endUserUnitPriceInfo.clone();
				cloneUnitPrice.setEndUserYearlyDataInfo(cloneEndUserYearlyDataInfo);
				endUserUnitPriceInfoList.add(cloneUnitPrice);
			}
			cloneEndUserYearlyDataInfo.setEndUserUnitPriceInfoList(endUserUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneEndUserYearlyDataInfo;
	}



	public List<EndUserRevenueInfo> getEndUserRevenueInfoList() {
		return endUserRevenueInfoList;
	}



	public void setEndUserRevenueInfoList(List<EndUserRevenueInfo> endUserRevenueInfoList) {
		this.endUserRevenueInfoList = endUserRevenueInfoList;
	}

}
