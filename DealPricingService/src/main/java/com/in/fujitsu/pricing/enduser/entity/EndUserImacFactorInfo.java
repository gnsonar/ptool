package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "END_USER_IMAC_FACTOR")
public class EndUserImacFactorInfo implements Serializable {

	private static final long serialVersionUID = 1403592081997301947L;

	@Id
	@GeneratedValue
	private Integer id;
	
	private String factorName;
	
	private Float factorSize;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFactorName() {
		return factorName;
	}
	public void setFactorName(String factorName) {
		this.factorName = factorName;
	}
	public Float getFactorSize() {
		return factorSize;
	}
	public void setFactorSize(Float factorSize) {
		this.factorSize = factorSize;
	}

}
