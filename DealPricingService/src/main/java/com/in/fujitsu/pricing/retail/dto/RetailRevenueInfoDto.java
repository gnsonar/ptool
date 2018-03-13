package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RetailRevenueInfoDto implements Serializable{

	private static final long serialVersionUID = 4749566547085098145L;

	private BigDecimal noOfShops;

	private int year;

	private String benchMarkType;

}
