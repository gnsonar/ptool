package com.in.fujitsu.pricing.retail.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RETAIL_SOLUTION")
public class RetailSolutionsInfo implements Serializable {

	private static final long serialVersionUID = 1465696311941693009L;

	@Id
	@GeneratedValue
	private Integer solutionId;

	private String solutionName;

	private String solutionDesc;

	@Column(scale = 2)
	private BigDecimal shopPerc;

	/**
	 * @return the solutionId
	 */
	public Integer getSolutionId() {
		return solutionId;
	}

	/**
	 * @param solutionId the solutionId to set
	 */
	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	/**
	 * @return the solutionName
	 */
	public String getSolutionName() {
		return solutionName;
	}

	/**
	 * @param solutionName the solutionName to set
	 */
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	/**
	 * @return the solutionDesc
	 */
	public String getSolutionDesc() {
		return solutionDesc;
	}

	/**
	 * @param solutionDesc the solutionDesc to set
	 */
	public void setSolutionDesc(String solutionDesc) {
		this.solutionDesc = solutionDesc;
	}

	/**
	 * @return the shopPerc
	 */
	public BigDecimal getShopPerc() {
		return shopPerc;
	}

	/**
	 * @param shopPerc the shopPerc to set
	 */
	public void setShopPerc(BigDecimal shopPerc) {
		this.shopPerc = shopPerc;
	}


}
