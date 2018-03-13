package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.ScenarioController;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.scenario.dto.ScenarioInfoDto;
import com.in.fujitsu.pricing.service.ScenarioService;


/**
 * @author PawarBh
 *
 */
@RestController
@RequestMapping("resources/scenario")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class ScenarioControllerImpl implements ScenarioController {

	@Autowired
	private ScenarioService scenarioService;


	@Override
	@PostMapping(path = "/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ScenarioInfoDto saveDetails(@PathVariable("dealId") Long dealId,
			@RequestBody ScenarioInfoDto scenarioInfoDto) throws Exception {
		if (scenarioInfoDto == null) {
			throw new ServiceException("Required object : scenarioInfoDto can't be null");
		}
		return scenarioService.saveDetails(dealId, scenarioInfoDto);
	}

	@Override
	@GetMapping(path = "/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScenarioInfoDto> getAllScenarios(@PathVariable("dealId") Long dealId) throws Exception {
		return scenarioService.getAllScenarios(dealId);
	}
	
	@Override
	@GetMapping(path = "/{dealId}/{scenarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ScenarioInfoDto getScenario(@PathVariable("dealId") Long dealId,@PathVariable("scenarioId") Long scenarioId) throws Exception {
		return scenarioService.getScenario(dealId,scenarioId);
	}

	@Override
	@DeleteMapping(path = "/{scenarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteScenario(@PathVariable("scenarioId") Long scenarioId) throws ServiceException{
		return scenarioService.deleteScenario(scenarioId);
	}
}
