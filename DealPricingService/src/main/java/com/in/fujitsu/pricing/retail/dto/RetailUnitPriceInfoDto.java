
package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RetailUnitPriceInfoDto implements Serializable {

	private static final long serialVersionUID = -6529762410036991739L;

	private long id;

	private BigDecimal noOfShops;

	// Low or Target in case of Benchmark deal
	private String benchMarkType;

}
