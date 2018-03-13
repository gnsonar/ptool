package com.in.fujitsu.pricing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author mishrasub
 *
 */
@Entity
@Table(name = "Deal_Type_Master")
public class DealType implements Serializable {

	private static final long serialVersionUID = -3332594424614607794L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	private String dealType;
	private boolean isActive;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
