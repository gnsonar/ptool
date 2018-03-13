package com.in.fujitsu.pricing.enduser.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "END_USER_MIGRATION_COST")
public class EndUserMigrationCostInfo implements Serializable, Comparable<EndUserMigrationCostInfo> {

	private static final long serialVersionUID = 3063247357135560995L;

	@Id
	@GeneratedValue
	private Integer id;
	private BigDecimal lower;
	private BigDecimal upper;
	private BigDecimal cost;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getLower() {
		return lower;
	}

	public void setLower(BigDecimal lower) {
		this.lower = lower;
	}

	public BigDecimal getUpper() {
		return upper;
	}

	public void setUpper(BigDecimal upper) {
		this.upper = upper;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	
	
	@Override
	public int compareTo(EndUserMigrationCostInfo o) {
		return this.getLower().compareTo(o.getLower());
	}


}
