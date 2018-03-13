
package com.in.fujitsu.pricing.servicedesk.entity;

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
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name="SERVICE_DESK_YEARLY_VOLUME")
@DynamicInsert
@DynamicUpdate
public class ServiceDeskYearlyDataInfo implements Serializable, Cloneable {


	private static final long serialVersionUID = -2183711413234866870L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long yearId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "serviceDeskId")
	private ServiceDeskInfo serviceDeskInfo;

	@Column(columnDefinition="int default 0")
	private Integer totalContacts;

	@Column(columnDefinition="int default 0")
	private Integer voiceContacts;

	@Column(columnDefinition="int default 0")
	private Integer mailContacts;

	@Column(columnDefinition="int default 0")
	private Integer chatContacts;

	@Column(columnDefinition="int default 0")
	private Integer portalContacts;

	private Integer year;

	@JsonIgnore
	@OneToMany(mappedBy = "serviceDeskYearlyDataInfo", cascade = CascadeType.ALL)
	private List<ServiceDeskUnitPriceInfo> serviceDeskUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "serviceDeskYearlyDataInfo", cascade = CascadeType.ALL)
	private List<ServiceDeskRevenueInfo> serviceDeskRevenueInfoList;

	public Long getYearId() {
		return yearId;
	}

	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	public ServiceDeskInfo getServiceDeskInfo() {
		return serviceDeskInfo;
	}

	public void setServiceDeskInfo(ServiceDeskInfo serviceDeskInfo) {
		this.serviceDeskInfo = serviceDeskInfo;
	}

	public Integer getTotalContacts() {
		return totalContacts;
	}

	public void setTotalContacts(Integer totalContacts) {
		this.totalContacts = totalContacts;
	}

	public Integer getVoiceContacts() {
		return voiceContacts;
	}

	public void setVoiceContacts(Integer voiceContacts) {
		this.voiceContacts = voiceContacts;
	}

	public Integer getMailContacts() {
		return mailContacts;
	}

	public void setMailContacts(Integer mailContacts) {
		this.mailContacts = mailContacts;
	}

	public Integer getChatContacts() {
		return chatContacts;
	}

	public void setChatContacts(Integer chatContacts) {
		this.chatContacts = chatContacts;
	}

	public Integer getPortalContacts() {
		return portalContacts;
	}

	public void setPortalContacts(Integer portalContacts) {
		this.portalContacts = portalContacts;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<ServiceDeskUnitPriceInfo> getServiceDeskUnitPriceInfoList() {
		return serviceDeskUnitPriceInfoList;
	}

	public void setServiceDeskUnitPriceInfoList(List<ServiceDeskUnitPriceInfo> serviceDeskUnitPriceInfoList) {
		this.serviceDeskUnitPriceInfoList = serviceDeskUnitPriceInfoList;
	}

	public List<ServiceDeskRevenueInfo> getServiceDeskRevenueInfoList() {
		return serviceDeskRevenueInfoList;
	}

	public void setServiceDeskRevenueInfoList(List<ServiceDeskRevenueInfo> serviceDeskRevenueInfoList) {
		this.serviceDeskRevenueInfoList = serviceDeskRevenueInfoList;
	}

	@Override
	public ServiceDeskYearlyDataInfo clone() {
		ServiceDeskYearlyDataInfo cloneServiceDeskYearlyDataInfo = null;
		try {
			cloneServiceDeskYearlyDataInfo = (ServiceDeskYearlyDataInfo) super.clone();
			List<ServiceDeskUnitPriceInfo> serviceDeskUnitPriceInfoList = new ArrayList<>();
			for (ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : cloneServiceDeskYearlyDataInfo
					.getServiceDeskUnitPriceInfoList()) {
				ServiceDeskUnitPriceInfo cloneUnitPrice = serviceDeskUnitPriceInfo.clone();
				cloneUnitPrice.setServiceDeskYearlyDataInfo(cloneServiceDeskYearlyDataInfo);
				serviceDeskUnitPriceInfoList.add(cloneUnitPrice);
			}
			cloneServiceDeskYearlyDataInfo.setServiceDeskUnitPriceInfoList(serviceDeskUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneServiceDeskYearlyDataInfo;
	}


}
