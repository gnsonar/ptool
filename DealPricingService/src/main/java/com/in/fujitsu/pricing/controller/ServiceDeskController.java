package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskDropdownDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskPriceDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueDto;

public interface ServiceDeskController {

	public ServiceDeskInfoDto getServiceDeskDetails(Long dealId) throws Exception;

	public ServiceDeskInfoDto saveServiceDeskDetails(Long dealId, ServiceDeskInfoDto serviceDeskInfoDto) throws Exception;

	public ServiceDeskRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public ServiceDeskDropdownDto getServiceDeskDropDownDetails(Long dealId) throws Exception;

	public ResponseEntity<Object> updateServiceDeskPrice(Long serviceDeskId, List<ServiceDeskPriceDto> serviceDeskPriceDtoList) throws Exception;

	public ServiceDeskRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long serviceDeskId)
			throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;
}
