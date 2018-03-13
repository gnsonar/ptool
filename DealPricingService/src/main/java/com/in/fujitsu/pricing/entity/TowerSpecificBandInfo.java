package com.in.fujitsu.pricing.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TOWER_SPECIFIC_BAND")
public class TowerSpecificBandInfo implements Serializable {

	private static final long serialVersionUID = 3063247357135560995L;

	@Id
	@GeneratedValue
	private Integer Id;
	private String towerName;
	private BigDecimal bandPercentage;

	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getTowerName() {
		return towerName;
	}
	public void setTowerName(String towerName) {
		this.towerName = towerName;
	}
	public BigDecimal getBandPercentage() {
		return bandPercentage;
	}
	public void setBandPercentage(BigDecimal bandPercentage) {
		this.bandPercentage = bandPercentage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}






}
