package com.in.fujitsu.pricing.application.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ApplicationCalculateDto {
	// Past deal
	private float pastDealTotalAppsAvgUnitPrice;
	private float pastDealSimpleAppsAvgUnitPrice;
	private float pastDealMediumAppsAvgUnitPrice;
	private float pastDealComplexAppsAvgUnitPrice;
	private float pastDealVeryComplexAppsAvgUnitPrice;

	// Competitive deal
	private float compDealTotalAppsAvgUnitPrice;
	private float compDealSimpleAppsAvgUnitPrice;
	private float compDealMediumAppsAvgUnitPrice;
	private float compDealComplexAppsAvgUnitPrice;
	private float compDealVeryComplexAppsAvgUnitPrice;

	// Benchmark deal low
	private float benchDealLowTotalAppsAvgUnitPrice;
	private float benchDealLowSimpleAppsAvgUnitPrice;
	private float benchDealLowMediumAppsAvgUnitPrice;
	private float benchDealLowComplexAppsAvgUnitPrice;
	private float benchDealLowVeryComplexAppsAvgUnitPrice;

	// Benchmark deal Target
	private float benchDealTargeTotalAppsAvgUnitPrice;
	private float benchDealTargetSimpleAppsAvgUnitPrice;
	private float benchDealTargetMediumAppsAvgUnitPrice;
	private float benchDealTargetComplexAppsAvgUnitPrice;
	private float benchDealTargetVeryComplexAppsAvgUnitPrice;

	private List<ApplicationYearlyCalculateDto> yearlyCalculateDtos;

}
