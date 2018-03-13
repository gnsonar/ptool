package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class PerformanceCalculateDto implements Serializable {

	private static final long serialVersionUID = -2966371497570269795L;

	// Past deal
	private BigDecimal pastDealAvgUnitPrice;

	// Competitive deal
	private BigDecimal compDealAvgUnitPrice;

	// Benchmark deal low
	private BigDecimal benchDealLowAvgUnitPrice;

	// Benchmark deal Target
	private BigDecimal benchDealTargetAvgUnitPrice;


	private List<YearlyCalculateDto> pastDealYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

	private List<YearlyCalculateDto> compYearlyCalcDtoList = new ArrayList<YearlyCalculateDto>();

}
