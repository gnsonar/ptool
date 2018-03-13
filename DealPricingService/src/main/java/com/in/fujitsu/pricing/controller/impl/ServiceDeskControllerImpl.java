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

import com.in.fujitsu.pricing.controller.ServiceDeskController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.ServiceDeskService;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskDropdownDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskPriceDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueDto;

/**
 * @author mishrasub
 *
 */
@RestController
@RequestMapping("resources/serviceDesk")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class ServiceDeskControllerImpl implements ServiceDeskController {

	@Autowired
	private ServiceDeskService serviceDeskService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceDeskDropdownDto getServiceDeskDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return serviceDeskService.getServiceDeskDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceDeskInfoDto getServiceDeskDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return serviceDeskService.getServiceDeskDetails(dealId);
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceDeskInfoDto saveServiceDeskDetails(@PathVariable("dealId") Long dealId,
			@RequestBody ServiceDeskInfoDto serviceDeskInfoDto) throws Exception {
		if (serviceDeskInfoDto == null) {
			throw new ServiceException("Required object : serviceDeskInfoDto can't be null");
		}
		return serviceDeskService.saveServiceDeskDetails(dealId, serviceDeskInfoDto);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceDeskRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return serviceDeskService.getYearlyRevenues(dealId);
	}

	@Override
	@PutMapping(path = "/revenues/{serviceDeskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateServiceDeskPrice(@PathVariable("serviceDeskId") Long serviceDeskId,
			@RequestBody List<ServiceDeskPriceDto> serviceDeskPriceDtoList) throws Exception {
		if (CollectionUtils.isEmpty(serviceDeskPriceDtoList)) {
			throw new ServiceException("Required list : serviceDeskPriceDtoList can't be null");
		}
		return serviceDeskService.updateServiceDeskPrice(serviceDeskPriceDtoList, serviceDeskId);
	}

	@Override
	@PutMapping(path = "/solution/{serviceDeskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceDeskRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("serviceDeskId") Long serviceDeskId)
			throws Exception {

		if (solutionCriteriaDto ==null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return serviceDeskService.updateSolutionCriteria(solutionCriteriaDto, serviceDeskId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return serviceDeskService.getNearestDeals(dealId, levelName, dealType);
	}

}
