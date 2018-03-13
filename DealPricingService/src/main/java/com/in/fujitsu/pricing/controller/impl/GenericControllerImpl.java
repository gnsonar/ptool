/**
 *
 */
package com.in.fujitsu.pricing.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.GenericController;
import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.GenericDealInfoDto;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.GenericService;

/**
 * @author Maninder
 *
 */

@RestController
@RequestMapping("resources/generic")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class GenericControllerImpl implements GenericController {

	@Autowired
	private GenericService genericService;

	@RequestMapping(value = "/getGenericDropDownDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericDealInfoDto getGenericDropDownDetails() throws Exception {
		return genericService.getGenericDropDownDetails();
	}

	@RequestMapping(value = "/saveGenericDealDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DealInfoDto saveGenericDealDetails(@RequestBody DealInfoDto dealInfoDto) throws Exception {
		return genericService.saveGenericDealDetails(dealInfoDto);
	}

	@RequestMapping(value = "/getGenericDetailsByDealId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public DealInfoDto getGenericDetailsByDealId(Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return genericService.getGenericDetailsByDealId(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@PutMapping(path = "/workflow/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateDealStatus(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "status") String dealStatus) throws Exception {
		return genericService.updateDealStatus(dealId, dealStatus);
	}

	@Override
	@PutMapping(path = "/towerIndicators/{dealId}")
	public ResponseEntity<Object> updateTowerIndicators(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "assessmentIndicator") String assessmentIndicator,
			@RequestParam(required = true, value = "submissionIndicator") String submissionIndicator)
			throws Exception {
		return genericService.updateTowerIndicators(dealId, assessmentIndicator, submissionIndicator);
	}

}
