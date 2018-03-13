package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class ComplexApplicationCalculateDto implements Serializable {

	private static final long serialVersionUID = 7149950660215826418L;

	// Past deal
	private BigDecimal pastDealComplexAppsAvgUnitPrice;

	// Competitive deal
	private BigDecimal compDealComplexAppsAvgUnitPrice;

	// Benchmark deal low
	private BigDecimal benchDealLowComplexAppsAvgUnitPrice;

	// Benchmark deal Target
	private BigDecimal benchDealTargetComplexAppsAvgUnitPrice;

	private List<YearlyCalculateDto> pastDealYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> compYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

}
