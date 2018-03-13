package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.application.dto.ApplicationDropdownDto;
import com.in.fujitsu.pricing.application.dto.ApplicationInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationPriceDto;
import com.in.fujitsu.pricing.application.dto.ApplicationRevenueDto;
import com.in.fujitsu.pricing.controller.ApplicationController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.ApplicationService;
import com.in.fujitsu.pricing.service.GenericService;

/**
 * @author mishrasub
 *
 */
@RestController
@RequestMapping("resources/apps")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class ApplicationControllerImpl implements ApplicationController {

	@Autowired
	private ApplicationService appService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationDropdownDto getAppDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return appService.getAppDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationInfoDto saveAppDetails(@PathVariable("dealId") Long dealId, @RequestBody ApplicationInfoDto applicationInfoDto) throws Exception {
		if (applicationInfoDto == null) {
			throw new ServiceException("Required object : applicationInfoDto can't be null");
		}
		return appService.saveAppDetails(dealId, applicationInfoDto);
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationInfoDto getAppDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return appService.getAppDetails(dealId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return appService.getYearlyRevenues(dealId);
	}

	@Override
	@PutMapping(path = "/revenues/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateAppPrice(
			@RequestBody List<ApplicationPriceDto> applicationPriceDtoList, @PathVariable("appId") Long appId)
			throws Exception {

		if (CollectionUtils.isEmpty(applicationPriceDtoList)) {
			throw new ServiceException("Required list : applicationPriceDtoList can't be null");
		}
		return appService.updateAppPrice(applicationPriceDtoList, appId);
	}

	@Override
	@PutMapping(path = "/solution/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ApplicationRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("appId") Long appId)
			throws Exception {

		if (solutionCriteriaDto == null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return appService.updateSolutionCriteria(solutionCriteriaDto, appId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return appService.getNearestDeals(dealId, levelName, dealType);
	}

}
