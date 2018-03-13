
package com.in.fujitsu.pricing.servicedesk.entity;

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
import org.hibernate.annotations.DynamicUpdate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name="SERVICE_DESK_REVENUE")
@DynamicInsert
@DynamicUpdate
public class ServiceDeskRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition="int default 0")
	private Integer totalContactsRevenue;

	private String benchMarkType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getTotalContactsRevenue() {
		return totalContactsRevenue;
	}

	public void setTotalContactsRevenue(Integer totalContactsRevenue) {
		this.totalContactsRevenue = totalContactsRevenue;
	}

	public String getBenchMarkType() {
		return benchMarkType;
	}

	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	public ServiceDeskYearlyDataInfo getServiceDeskYearlyDataInfo() {
		return serviceDeskYearlyDataInfo;
	}

	public void setServiceDeskYearlyDataInfo(ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo) {
		this.serviceDeskYearlyDataInfo = serviceDeskYearlyDataInfo;
	}

	@Override
	public ServiceDeskRevenueInfo clone() {
		ServiceDeskRevenueInfo serviceDeskRevenueInfo = null;
		try {
			serviceDeskRevenueInfo = (ServiceDeskRevenueInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return serviceDeskRevenueInfo;
	}

}
