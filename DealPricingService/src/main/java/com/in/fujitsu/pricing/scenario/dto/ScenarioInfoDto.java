package com.in.fujitsu.pricing.scenario.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ScenarioInfoDto implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	private Long scenarioId;
	private Long dealId;
	private String scenarioName;
	private String scenarioDesc;

	private Long transitionFees;
	private Long serviceGovernance;
	private boolean isMigrationCostApplicable;

	private List<ScenarioYearlyInfoDto> scenarioYearlyInfoDtoList;

	private ScenarioCriteriaInfoDto scenarioCriteriaInfoDto;

	private ScenarioVolumeInfoDto scenarioVolumeInfoDto;

}
