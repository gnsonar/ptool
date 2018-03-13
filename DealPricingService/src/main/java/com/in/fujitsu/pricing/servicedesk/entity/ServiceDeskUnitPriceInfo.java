package com.in.fujitsu.pricing.servicedesk.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

/**
 * @author MishraSub
 *
 */
@Slf4j
@Entity
@Table(name="SERVICE_DESK_UNIT_PRICE")
@DynamicInsert
@DynamicUpdate
public class ServiceDeskUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 1868958092589377126L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal totalContactsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal voiceContactsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal mailContactsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal chatContactsUnitPrice;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal portalContactsUnitPrice;

	// Low or Target in case of Benchmark deal
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


	public ServiceDeskYearlyDataInfo getServiceDeskYearlyDataInfo() {
		return serviceDeskYearlyDataInfo;
	}


	public void setServiceDeskYearlyDataInfo(ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo) {
		this.serviceDeskYearlyDataInfo = serviceDeskYearlyDataInfo;
	}


	public BigDecimal getTotalContactsUnitPrice() {
		return totalContactsUnitPrice;
	}


	public void setTotalContactsUnitPrice(BigDecimal totalContactsUnitPrice) {
		this.totalContactsUnitPrice = totalContactsUnitPrice;
	}


	public BigDecimal getVoiceContactsUnitPrice() {
		return voiceContactsUnitPrice;
	}


	public void setVoiceContactsUnitPrice(BigDecimal voiceContactsUnitPrice) {
		this.voiceContactsUnitPrice = voiceContactsUnitPrice;
	}


	public BigDecimal getMailContactsUnitPrice() {
		return mailContactsUnitPrice;
	}


	public void setMailContactsUnitPrice(BigDecimal mailContactsUnitPrice) {
		this.mailContactsUnitPrice = mailContactsUnitPrice;
	}


	public BigDecimal getChatContactsUnitPrice() {
		return chatContactsUnitPrice;
	}


	public void setChatContactsUnitPrice(BigDecimal chatContactsUnitPrice) {
		this.chatContactsUnitPrice = chatContactsUnitPrice;
	}


	public BigDecimal getPortalContactsUnitPrice() {
		return portalContactsUnitPrice;
	}


	public void setPortalContactsUnitPrice(BigDecimal portalContactsUnitPrice) {
		this.portalContactsUnitPrice = portalContactsUnitPrice;
	}


	public String getBenchMarkType() {
		return benchMarkType;
	}


	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	@Override
	public ServiceDeskUnitPriceInfo clone() {
		ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo = null;
		try {
			serviceDeskUnitPriceInfo = (ServiceDeskUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return serviceDeskUnitPriceInfo;
	}

}
