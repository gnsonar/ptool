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

import com.in.fujitsu.pricing.controller.HostingController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.hosting.dto.HostingDropdownDto;
import com.in.fujitsu.pricing.hosting.dto.HostingInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingPriceDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueDto;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.HostingService;

@RestController
@RequestMapping("resources/hosting")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class HostingControllerImpl implements HostingController {

	@Autowired
	private HostingService hostingService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public HostingDropdownDto getDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return hostingService.getDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HostingInfoDto saveDetails(@PathVariable("dealId") Long dealId, @RequestBody HostingInfoDto hostingInfoDto) throws Exception {
		if (hostingInfoDto == null) {
			throw new ServiceException("Required object : hostingInfoDto can't be null");
		}
		return hostingService.saveDetails(dealId, hostingInfoDto);
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public HostingInfoDto getDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return hostingService.getDetails(dealId);
	}

	@Override
	@PutMapping(path = "/revenues/{hostingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePrice(@PathVariable("hostingId") Long hostingId, @RequestBody List<HostingPriceDto> hostingPriceDtoList)
			throws Exception {
		if (CollectionUtils.isEmpty(hostingPriceDtoList)) {
			throw new ServiceException("Required list : hostingPriceDtoList can't be null");
		}
		return hostingService.updateHostingPrice(hostingPriceDtoList, hostingId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public HostingRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return hostingService.getYearlyRevenues(dealId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		return hostingService.getNearestDeals(dealId, levelName, dealType);
	}

	@Override
	@PutMapping(path = "/solution/{hostingId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HostingRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("hostingId") Long hostingId)
			throws Exception {
		if (solutionCriteriaDto == null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return hostingService.updateSolutionCriteria(solutionCriteriaDto, hostingId);
	}

}
