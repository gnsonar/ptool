package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DealInfoDto implements Serializable{

	private static final long serialVersionUID = 1722745168254511889L;

	private Long dealId;
	private Long modifiedById;
	private String dealName;
	private String clientName;
	private Integer dealTerm;
	private String clientIndustry;
	private String dealPhase;
	private String currency;
	private String country;
	private Date startDate;
	private Date submissionDate;
	private String offshoreAllowed;
	private String serviceWindowSla;
	private String includeHardware;
	private String thirdPartyFinance;
	private String openBook;
	private String crossBorderTax;
	private String nonStandardVariablePricing;
	private String migrationCost;
	private String salesRepresentative;
	private String bidManager;
	private String financialEngineer;
	private String leadSolutionArch;
	private List<DealYearlyDataInfoDto> dealYearlyDataInfoDtos;
	private List<DealCompetitorInfoDto> dealCompetitorInfoDtos;
	// status will contain either 'active', 'inactive' or any of the deal phase value
	private String dealStatus;
	// either of 'Past', 'Benchmark', 'Competitor'
	private String dealType;
	private Long userId;
	private Date modificationDate;
	private Long transitionFees;
	private Long serviceGovernance;
	private String assessmentIndicator;
	private String submissionIndicator;

}
