package com.in.fujitsu.pricing.scenario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.in.fujitsu.pricing.scenario.entity.ScenarioInfo;

public interface ScenarioRepository extends JpaRepository<ScenarioInfo, Long> {

	public List<ScenarioInfo> findByDealId(@Param("dealId") Long dealId);

	public ScenarioInfo findByDealIdAndScenarioName(@Param("dealId") Long dealId,@Param("scenarioName")String scenarioName);

}
