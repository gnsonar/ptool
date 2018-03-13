
package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name="END_USER_REVENUE")
@DynamicInsert
public class EndUserRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition="int default 0")
	private Integer totalEndUserDevices;

	@Column(columnDefinition="int default 0")
	private Integer totalImacDevices;

	private String benchMarkType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private EndUserYearlyDataInfo endUserYearlyDataInfo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getTotalEndUserDevices() {
		return totalEndUserDevices;
	}

	public void setTotalEndUserDevices(Integer totalEndUserDevices) {
		this.totalEndUserDevices = totalEndUserDevices;
	}

	public Integer getTotalImacDevices() {
		return totalImacDevices;
	}

	public void setTotalImacDevices(Integer totalImacDevices) {
		this.totalImacDevices = totalImacDevices;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	public EndUserYearlyDataInfo getEndUserYearlyDataInfo() {
		return endUserYearlyDataInfo;
	}

	public void setEndUserYearlyDataInfo(EndUserYearlyDataInfo endUserYearlyDataInfo) {
		this.endUserYearlyDataInfo = endUserYearlyDataInfo;
	}

}
