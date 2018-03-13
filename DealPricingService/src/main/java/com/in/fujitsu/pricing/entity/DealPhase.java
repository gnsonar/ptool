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
@Table(name = "Deal_Phase_Master")
public class DealPhase implements Serializable {

	private static final long serialVersionUID = 5220265098483217664L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	private String dealPhase;
	private boolean isActive;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDealPhase() {
		return dealPhase;
	}
	public void setDealPhase(String dealPhase) {
		this.dealPhase = dealPhase;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
