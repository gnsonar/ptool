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
@Table(name = "Deal_Cost_parameters")
public class DealCostParameters implements Serializable {

	private static final long serialVersionUID = -5310340081243338675L;
	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	private String name;
	private long amount;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}

}
