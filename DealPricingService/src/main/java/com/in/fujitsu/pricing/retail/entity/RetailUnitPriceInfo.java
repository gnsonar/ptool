
package com.in.fujitsu.pricing.retail.entity;

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
@Table(name="RETAIL_UNIT_PRICE")
@DynamicInsert
@DynamicUpdate
public class RetailUnitPriceInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 6972442749199229699L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition = "decimal(20,2) default 0.0")
	private BigDecimal noOfShops;

	// Low or Target in case of Benchmark deal
	private String benchMarkType;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "yearId", nullable = false)
	private RetailYearlyDataInfo retailYearlyDataInfo;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the noOfShops
	 */
	public BigDecimal getNoOfShops() {
		return noOfShops;
	}

	/**
	 * @param noOfShops the noOfShops to set
	 */
	public void setNoOfShops(BigDecimal noOfShops) {
		this.noOfShops = noOfShops;
	}

	/**
	 * @return the benchMarkType
	 */
	public String getBenchMarkType() {
		return benchMarkType;
	}

	/**
	 * @param benchMarkType the benchMarkType to set
	 */
	public void setBenchMarkType(String benchMarkType) {
		this.benchMarkType = benchMarkType;
	}

	/**
	 * @return the retailYearlyDataInfo
	 */
	public RetailYearlyDataInfo getRetailYearlyDataInfo() {
		return retailYearlyDataInfo;
	}

	/**
	 * @param retailYearlyDataInfo the retailYearlyDataInfo to set
	 */
	public void setRetailYearlyDataInfo(RetailYearlyDataInfo retailYearlyDataInfo) {
		this.retailYearlyDataInfo = retailYearlyDataInfo;
	}

	@Override
	public RetailUnitPriceInfo clone() {
		RetailUnitPriceInfo retailUnitPriceInfo = null;
		try {
			retailUnitPriceInfo = (RetailUnitPriceInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return retailUnitPriceInfo;
	}

}
