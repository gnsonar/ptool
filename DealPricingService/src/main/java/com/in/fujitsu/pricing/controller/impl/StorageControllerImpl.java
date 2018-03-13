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

import com.in.fujitsu.pricing.controller.StorageController;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.StorageService;
import com.in.fujitsu.pricing.storage.dto.StorageDropdownDto;
import com.in.fujitsu.pricing.storage.dto.StorageInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueDto;
import com.in.fujitsu.pricing.storage.dto.UpdateStoragePriceDto;

@RestController
@RequestMapping("resources/storage")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class StorageControllerImpl implements StorageController {

	@Autowired
	private StorageService storageService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/volumes/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StorageInfoDto getStorageDetails(@PathVariable("dealId") Long dealId) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return storageService.getStorageDetails(dealId);
	}

	@Override
	@PostMapping(path = "/volumes/{dealId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public StorageInfoDto saveStorageDetails(@PathVariable("dealId") Long dealId, @RequestBody StorageInfoDto storageInfoDto) throws Exception {
		if (storageInfoDto == null) {
			throw new ServiceException("Required object : storageInfoDto can't be null");
		}
		return storageService.saveStorageDetails(dealId, storageInfoDto);
	}

	@Override
	@PutMapping(path = "/revenues/{storageId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateStoragePriceDetails(
			@RequestBody List<UpdateStoragePriceDto> storagePriceDtoList, @PathVariable("storageId") Long storageId)
			throws Exception {

		if (CollectionUtils.isEmpty(storagePriceDtoList)) {
			throw new ServiceException("Required list : storageUnitPriceDtoList can't be null");
		}
		return storageService.updateStoragePriceDetails(storagePriceDtoList, storageId);
	}

	@Override
	@PutMapping(path = "/solution/{storageId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public StorageRevenueDto updateSolutionCriteria(@RequestBody SolutionCriteriaDto solutionCriteriaDto, @PathVariable("storageId") Long storageId)
			throws Exception {

		if (solutionCriteriaDto ==null) {
			throw new ServiceException("Required : solutionCriteriaDto can't be null");
		}
		return storageService.updateSolutionCriteria(solutionCriteriaDto, storageId);
	}

	@Override
	@GetMapping(path = "/revenues/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StorageRevenueDto getYearlyRevenues(@PathVariable("dealId") Long dealId) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return storageService.getYearlyRevenues(dealId);
	}

	@Override
	@GetMapping(path = "/dropdowns/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public StorageDropdownDto getStorageDropDownDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return storageService.getStorageDropDownDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

	@Override
	@GetMapping(path = "/results/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public DealResultsResponse getNearestDeals(@PathVariable("dealId") Long dealId,
			@RequestParam(required = true, value = "levelName") String levelName,
			@RequestParam(required = true, value = "dealType") String dealType) throws Exception {
		if (dealId == null) {
			throw new ServiceException("Required property : dealId can't be null");
		}
		return storageService.getNearestDeals(dealId, levelName, dealType);
	}
}
