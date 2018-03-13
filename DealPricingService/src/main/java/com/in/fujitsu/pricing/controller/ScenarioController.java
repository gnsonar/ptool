package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.scenario.dto.ScenarioInfoDto;


/**
 * @author PawarBh
 *
 */
public interface ScenarioController {

	public ScenarioInfoDto saveDetails(Long dealId, ScenarioInfoDto scenarioInfoDto) throws Exception;

	public List<ScenarioInfoDto> getAllScenarios(Long dealId) throws Exception;

	public ResponseEntity<Object> deleteScenario(Long scenarioId) throws ServiceException;

	public ScenarioInfoDto getScenario(Long dealId, Long scenarioId) throws Exception;


}
