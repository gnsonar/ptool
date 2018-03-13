package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class HostingSolutionInfoDto implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	private BigDecimal physicalPerc;
	private BigDecimal virtualPerc;
	private BigDecimal publicPerc;
	private BigDecimal privatePerc;
	private BigDecimal winLinuxPerc;
	private BigDecimal unixPerc;
	private BigDecimal smallPerc;
	private BigDecimal mediumPerc;
	private BigDecimal largePerc;

}
