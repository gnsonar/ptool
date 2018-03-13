package com.in.fujitsu.pricing.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationYearlyCalculateDto {

	private int year;
	private String dealType;
	private String benchmarkType;

	private float totalAppsUnitPrice;
	private float simpleAppsUnitPrice;
	private float mediumAppsUnitPrice;
	private float complexAppsUnitPrice;
	private float veryComplexAppsUnitPrice;

	private float totalAppsRevenue;
	private float simpleAppsRevenue;
	private float mediumAppsRevenue;
	private float complexAppsRevenue;
	private float veryComplexAppsRevenue;

}
