package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.storage.dto.StorageDropdownDto;
import com.in.fujitsu.pricing.storage.dto.StorageInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueDto;
import com.in.fujitsu.pricing.storage.dto.UpdateStoragePriceDto;

public interface StorageController {

	public StorageInfoDto getStorageDetails(Long dealid) throws Exception;

	public StorageInfoDto saveStorageDetails(Long dealId, StorageInfoDto storageInfoDto) throws Exception;

	public StorageRevenueDto getYearlyRevenues(Long dealid) throws Exception;

	public StorageDropdownDto getStorageDropDownDetails(Long dealid) throws Exception;

	public ResponseEntity<Object> updateStoragePriceDetails(List<UpdateStoragePriceDto> updateStoragePriceDtoList,
			Long storageId) throws Exception;

	public StorageRevenueDto updateSolutionCriteria(SolutionCriteriaDto storageSolutionDto, Long storageId)
			throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;

}
