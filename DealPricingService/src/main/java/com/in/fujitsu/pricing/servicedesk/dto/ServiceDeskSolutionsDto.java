package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ServiceDeskSolutionsDto implements Serializable {

	private static final long serialVersionUID = 2375532168110832965L;

	private Integer solutionId;

	private String solutionName;

	private String solutionDesc;

	private BigDecimal voicePerc;

	private BigDecimal mailPerc;

	private BigDecimal chatPerc;

	private BigDecimal portalPerc;

}
