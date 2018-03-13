package com.in.fujitsu.pricing.scenario.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="SCENARIO_MASTER")
public class ScenarioInfo implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long scenarioId;

	private Long dealId;

	private String scenarioName;
	private String scenarioDesc;

	private Long transitionFees;
	private Long serviceGovernance;
	private boolean isMigrationCostApplicable;

	@OneToMany(mappedBy = "scenarioInfo", cascade = CascadeType.ALL)
	private List<ScenarioYearlyInfo> scenarioYearlyInfoList;

	@JsonIgnore
	@OneToOne(mappedBy ="scenarioInfo", cascade = CascadeType.ALL)
	private ScenarioCriteriaInfo scenarioCriteriaInfo;

	public Long getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(Long scenarioId) {
		this.scenarioId = scenarioId;
	}

	public Long getDealId() {
		return dealId;
	}

	public void setDealId(Long dealId) {
		this.dealId = dealId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getScenarioDesc() {
		return scenarioDesc;
	}

	public void setScenarioDesc(String scenarioDesc) {
		this.scenarioDesc = scenarioDesc;
	}

	public Long getTransitionFees() {
		return transitionFees;
	}

	public void setTransitionFees(Long transitionFees) {
		this.transitionFees = transitionFees;
	}

	public Long getServiceGovernance() {
		return serviceGovernance;
	}

	public void setServiceGovernance(Long serviceGovernance) {
		this.serviceGovernance = serviceGovernance;
	}

	public List<ScenarioYearlyInfo> getScenarioYearlyInfoList() {
		return scenarioYearlyInfoList;
	}

	public void setScenarioYearlyInfoList(List<ScenarioYearlyInfo> scenarioYearlyInfoList) {
		this.scenarioYearlyInfoList = scenarioYearlyInfoList;
	}

	public boolean isMigrationCostApplicable() {
		return isMigrationCostApplicable;
	}

	public void setMigrationCostApplicable(boolean isMigrationCostApplicable) {
		this.isMigrationCostApplicable = isMigrationCostApplicable;
	}

	public ScenarioCriteriaInfo getScenarioCriteriaInfo() {
		return scenarioCriteriaInfo;
	}

	public void setScenarioCriteriaInfo(ScenarioCriteriaInfo scenarioCriteriaInfo) {
		this.scenarioCriteriaInfo = scenarioCriteriaInfo;
	}

}
