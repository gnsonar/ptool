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

import com.in.fujitsu.pricing.controller.EndUserController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserDropdownDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserPriceDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.EndUserService;
import com.in.fujitsu.pricing.service.GenericService;

@RestController
@RequestMapping("resources/enduser")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class EndUserControllerImpl implements EndUserController {

	@Autowired
	private EndUserService endUserService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EndUserDropdownDto getDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return endUserService.getDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EndUserInfoDto saveDetails(@PathVariable("dealId") Long dealId, @RequestBody EndUserInfoDto endUserInfoDto)
			throws Exception {
		if (endUserInfoDto == null) {
			throw new ServiceException("Required object : endUserInfoDto can't be null");
		}
		return endUserService.saveDetails(dealId, endUserInfoDto);
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EndUserInfoDto getDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return endUserService.getDetails(dealId);
	}

	@Override
	@PutMapping(path = "/revenues/{endUserId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePrice(@PathVariable("endUserId") Long endUserId,
			@RequestBody List<EndUserPriceDto> endUserPriceDtoList) throws Exception {
		if (CollectionUtils.isEmpty(endUserPriceDtoList)) {
			throw new ServiceException("Required list : endUserPriceDtoList can't be null");
		}
		return endUserService.updateEndUserPrice(endUserPriceDtoList, endUserId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EndUserRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return endUserService.getYearlyRevenues(dealId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		return endUserService.getNearestDeals(dealId, levelName, dealType);
	}

	@Override
	@PutMapping(path = "/solution/{endUserId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EndUserRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("endUserId") Long endUserId)
			throws Exception {
		if (solutionCriteriaDto == null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return endUserService.updateSolutionCriteria(solutionCriteriaDto, endUserId);
	}

}
