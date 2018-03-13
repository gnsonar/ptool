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

import com.in.fujitsu.pricing.controller.RetailController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.retail.dto.RetailDropdownDto;
import com.in.fujitsu.pricing.retail.dto.RetailInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailPriceDto;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueDto;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.RetailService;

/**
 * @author mishrasub
 *
 */
@RestController
@RequestMapping("resources/retail")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class RetailControllerImpl implements RetailController {

	@Autowired
	private RetailService retailService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RetailDropdownDto getDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return retailService.getDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RetailInfoDto getDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return retailService.getDetails(dealId);
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RetailInfoDto saveDetails(@PathVariable("dealId") Long dealId, @RequestBody RetailInfoDto retailInfoDto)
			throws Exception {
		if (retailInfoDto == null) {
			throw new ServiceException("Required object : retailInfoDto can't be null");
		}
		return retailService.saveDetails(dealId, retailInfoDto);
	}

	@Override
	@PutMapping(path = "/revenues/{retailId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePrice(@PathVariable("retailId") Long retailId,
			@RequestBody List<RetailPriceDto> retailPriceDtoList) throws Exception {
		if (CollectionUtils.isEmpty(retailPriceDtoList)) {
			throw new ServiceException("Required list : retailPriceDtoList can't be null");
		}
		return retailService.updatePrice(retailPriceDtoList, retailId);
	}

	@Override
	@PutMapping(path = "/solution/{retailId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RetailRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto,
			@PathVariable("retailId") Long retailId) throws Exception {
		if (solutionCriteriaDto == null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return retailService.updateSolutionCriteria(solutionCriteriaDto, retailId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RetailRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return retailService.getYearlyRevenues(dealId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {

		return retailService.getNearestDeals(dealId, levelName, dealType);
	}

}
