package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "END_USER_CONTACT_RATIO")
public class EndUserContactRatioInfo implements Serializable {

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
