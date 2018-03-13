
package com.in.fujitsu.pricing.retail.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

/**
 * @author MishraSub
 *
 */
@Slf4j
@Entity
@Table(name="RETAIL_YEARLY_VOLUME")
@DynamicInsert
@DynamicUpdate
public class RetailYearlyDataInfo implements Serializable, Cloneable {


	private static final long serialVersionUID = 534243126213291402L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long yearId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "retailId")
	private RetailInfo retailInfo;

	@Column(columnDefinition="int default 0")
	private Integer noOfShops;

	private Integer year;

	@JsonIgnore
	@OneToMany(mappedBy = "retailYearlyDataInfo", cascade = CascadeType.ALL)
	private List<RetailUnitPriceInfo> retailUnitPriceInfoList;

	@JsonIgnore
	@OneToMany(mappedBy = "retailYearlyDataInfo", cascade = CascadeType.ALL)
	private List<RetailRevenueInfo> retailRevenueInfoList;

	/**
	 * @return the yearId
	 */
	public Long getYearId() {
		return yearId;
	}

	/**
	 * @param yearId the yearId to set
	 */
	public void setYearId(Long yearId) {
		this.yearId = yearId;
	}

	/**
	 * @return the retailInfo
	 */
	public RetailInfo getRetailInfo() {
		return retailInfo;
	}

	/**
	 * @param retailInfo the retailInfo to set
	 */
	public void setRetailInfo(RetailInfo retailInfo) {
		this.retailInfo = retailInfo;
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
	 * @return the year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * @return the retailUnitPriceInfoList
	 */
	public List<RetailUnitPriceInfo> getRetailUnitPriceInfoList() {
		return retailUnitPriceInfoList;
	}

	/**
	 * @param retailUnitPriceInfoList the retailUnitPriceInfoList to set
	 */
	public void setRetailUnitPriceInfoList(List<RetailUnitPriceInfo> retailUnitPriceInfoList) {
		this.retailUnitPriceInfoList = retailUnitPriceInfoList;
	}

	/**
	 * @return the retailRevenueInfoList
	 */
	public List<RetailRevenueInfo> getRetailRevenueInfoList() {
		return retailRevenueInfoList;
	}

	/**
	 * @param retailRevenueInfoList the retailRevenueInfoList to set
	 */
	public void setRetailRevenueInfoList(List<RetailRevenueInfo> retailRevenueInfoList) {
		this.retailRevenueInfoList = retailRevenueInfoList;
	}

	@Override
	public RetailYearlyDataInfo clone() {
		RetailYearlyDataInfo cloneRetailYearlyDataInfo = null;
		try {
			cloneRetailYearlyDataInfo = (RetailYearlyDataInfo) super.clone();
			List<RetailUnitPriceInfo> retailUnitPriceInfoList = new ArrayList<>();
			for (RetailUnitPriceInfo retailUnitPriceInfo : cloneRetailYearlyDataInfo
					.getRetailUnitPriceInfoList()) {
				RetailUnitPriceInfo cloneUnitPrice = retailUnitPriceInfo.clone();
				cloneUnitPrice.setRetailYearlyDataInfo(cloneRetailYearlyDataInfo);
				retailUnitPriceInfoList.add(cloneUnitPrice);
			}
			cloneRetailYearlyDataInfo.setRetailUnitPriceInfoList(retailUnitPriceInfoList);

		} catch (CloneNotSupportedException e) {
			log.error(e.getMessage());
		}
		return cloneRetailYearlyDataInfo;
	}

}
