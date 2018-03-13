package com.in.fujitsu.pricing.servicedesk.dto;

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
public class ServiceDeskCalculateDto implements Serializable {

	private static final long serialVersionUID = 6283776826880675630L;

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
