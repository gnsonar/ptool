package com.in.fujitsu.pricing.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="COUNTRY_FACTOR_MASTER")
public class CountryFactorInfo implements Serializable {

	private static final long serialVersionUID = -2507966266536840478L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String country;

	private BigDecimal countryFactor;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getCountryFactor() {
		return countryFactor;
	}

	public void setCountryFactor(BigDecimal countryFactor) {
		this.countryFactor = countryFactor;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


}
