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

import com.in.fujitsu.pricing.controller.NetworkController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.network.dto.NetworkDropdownDto;
import com.in.fujitsu.pricing.network.dto.NetworkInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkPriceDto;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueDto;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.NetworkService;

/**
 * @author ChhabrMa
 *
 */
@RestController
@RequestMapping("resources/network")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class NetworkControllerImpl implements NetworkController {

	@Autowired
	private NetworkService networkService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkDropdownDto getDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return networkService.getDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkInfoDto saveDetails(@PathVariable("dealId") Long dealId,
			@RequestBody NetworkInfoDto networkInfoDto) throws Exception {
		if (networkInfoDto == null) {
			throw new ServiceException("Required object : networkInfoDto can't be null");
		}
		return networkService.saveDetails(dealId, networkInfoDto);
	}

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkInfoDto getDetails(@PathVariable("dealId") Long dealId) throws Exception {
		return networkService.getDetails(dealId);
	}

	@Override
	@PutMapping(path = "/revenues/{networkId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePrice(@PathVariable("networkId") Long networkId,
			@RequestBody List<NetworkPriceDto> networkPriceDtoList) throws Exception {
		if (CollectionUtils.isEmpty(networkPriceDtoList)) {
			throw new ServiceException("Required list : networkPriceDtoList can't be null");
		}
		return networkService.updateNetworkPrice(networkPriceDtoList, networkId);
	}

	@Override
	@PutMapping(path = "/solution/{networkId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("networkId") Long networkId)
			throws Exception {
		if (solutionCriteriaDto == null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return networkService.updateSolutionCriteria(solutionCriteriaDto, networkId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		return networkService.getYearlyRevenues(dealId);
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return networkService.getNearestDeals(dealId, levelName, dealType);
	}

}
