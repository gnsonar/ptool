package com.in.fujitsu.pricing.servicedesk.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_DESK_CONTACT_RATIO")
public class ServiceDeskContactRatioInfo implements Serializable {

	private static final long serialVersionUID = 2772862341422080409L;

	@Id
	@GeneratedValue
	private Integer Id;
	private String contactName;
	private Float contactRatio;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Float getContactRatio() {
		return contactRatio;
	}

	public void setContactRatio(Float contactRatio) {
		this.contactRatio = contactRatio;
	}

}
