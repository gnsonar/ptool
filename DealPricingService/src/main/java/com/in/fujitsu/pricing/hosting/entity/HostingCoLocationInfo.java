package com.in.fujitsu.pricing.hosting.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOSTING_COLOCATION")
public class HostingCoLocationInfo implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	@Id
	@GeneratedValue
	private Integer coLocationId;
	
	private String coLocationName;

	public Integer getCoLocationId() {
		return coLocationId;
	}

	public void setCoLocationId(Integer coLocationId) {
		this.coLocationId = coLocationId;
	}

	public String getCoLocationName() {
		return coLocationName;
	}

	public void setCoLocationName(String coLocationName) {
		this.coLocationName = coLocationName;
	}
	
	
}
