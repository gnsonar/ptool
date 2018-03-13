package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationRevenueDto implements Serializable {

	private static final long serialVersionUID = 6225095681986892015L;

	private TotalApplicationCalculateDto totalApplicationCalculateDto;

	private SimpleApplicationCalculateDto simpleApplicationCalculateDto;

	private MediumApplicationCalculateDto mediumApplicationCalculateDto;

	private ComplexApplicationCalculateDto complexApplicationCalculateDto;

	private VeryComplexApplicationCalculateDto veryComplexApplicationCalculateDto;

}
