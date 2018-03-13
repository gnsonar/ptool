package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class RetailPriceDto implements Serializable {

	private static final long serialVersionUID = -7165846499149547205L;

	private int year;

	private BigDecimal noOfShopsUnitPrice;

	private Integer noOfShopsRevenue;

}
