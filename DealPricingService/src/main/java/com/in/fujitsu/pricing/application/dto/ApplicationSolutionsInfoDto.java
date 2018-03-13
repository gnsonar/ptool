package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationSolutionsInfoDto implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	private BigDecimal simplePerc;
	private BigDecimal mediumPerc;
	private BigDecimal complexPerc;
	private BigDecimal veryComplexPerc;

}
