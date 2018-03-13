package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RetailSolutionsDto implements Serializable {

	private static final long serialVersionUID = -870561791506689849L;

	private Integer solutionId;

	private String solutionName;

	private String solutionDesc;

	private BigDecimal shopPerc;

}
