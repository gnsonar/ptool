package com.in.fujitsu.pricing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.scenario.dto.ScenarioInfoDto;
import com.in.fujitsu.pricing.scenario.entity.ScenarioInfo;
import com.in.fujitsu.pricing.scenario.helper.ScenarioBeanConvertor;
import com.in.fujitsu.pricing.scenario.repository.ScenarioRepository;

@Service
public class ScenarioService {

	@Autowired
	private ScenarioRepository scenarioRepository;

	@Autowired
	private ScenarioBeanConvertor beanConvertor;


	/**
	 * @param dealId
	 * @param scenarioInfoDto
	 * @return
	 */
	@Transactional
	public ScenarioInfoDto saveDetails(Long dealId, ScenarioInfoDto scenarioInfoDto) {
		ScenarioInfo scenarioInfo = beanConvertor.prepareScenarioInfo(dealId, scenarioInfoDto);
		scenarioInfo = scenarioRepository.saveAndFlush(scenarioInfo);
		return beanConvertor.prepareScenarioInfoDto(scenarioInfo);
	}

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public List<ScenarioInfoDto> getAllScenarios(Long dealId) throws Exception {
		List<ScenarioInfo> scenarioList = scenarioRepository.findByDealId(dealId);
		return beanConvertor.prepareScenarioInfoDto(scenarioList);
	}

	/**
	 * @param dealId
	 * @param scenarioName
	 * @return
	 * @throws ServiceException
	 */
	public ResponseEntity<Object> deleteScenario(Long scenarioId) throws ServiceException {
		ScenarioInfo scenarioInfo = scenarioRepository.findOne(scenarioId);
		if (null != scenarioInfo) {
			scenarioRepository.delete(scenarioInfo);
		} else {
			throw new ServiceException("No scenario found to delete");
		}
		return new ResponseEntity<Object>(new SuccessResponse("Scenario deleted successfully"), HttpStatus.OK);
	}

	public ScenarioInfoDto getScenario(Long dealId,Long scenarioId) throws ServiceException {
		ScenarioInfoDto scenarioInfoDto = new ScenarioInfoDto();
		ScenarioInfo scenarioInfo = scenarioRepository.findOne(scenarioId);
		if (null != scenarioInfo) {
			scenarioInfoDto = beanConvertor.prepareScenarioInfoDto(scenarioInfo);
		} else {
			throw new ServiceException("Scenario by this does not exists");
		}
		return scenarioInfoDto;
	}
}
