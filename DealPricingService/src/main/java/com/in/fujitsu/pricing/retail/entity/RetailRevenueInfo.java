
package com.in.fujitsu.pricing.retail.entity;

import java.io.Serializable;

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
@Table(name="RETAIL_REVENUE")
@DynamicInsert
@DynamicUpdate
public class RetailRevenueInfo implements Serializable , Cloneable {

	private static final long serialVersionUID = -8344709963248576445L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(columnDefinition="int default 0")
	private Integer noOfShops;

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
	public Integer getNoOfShops() {
		return noOfShops;
	}

	/**
	 * @param noOfShops the noOfShops to set
	 */
	public void setNoOfShops(Integer noOfShops) {
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
	public RetailRevenueInfo clone() {
		RetailRevenueInfo retailRevenueInfo = null;
		try {
			retailRevenueInfo = (RetailRevenueInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return retailRevenueInfo;
	}


}
