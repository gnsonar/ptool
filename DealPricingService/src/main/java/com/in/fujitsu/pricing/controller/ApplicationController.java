package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.application.dto.ApplicationDropdownDto;
import com.in.fujitsu.pricing.application.dto.ApplicationInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationPriceDto;
import com.in.fujitsu.pricing.application.dto.ApplicationRevenueDto;
import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;

/**
 * @author mishrasub
 *
 */
public interface ApplicationController {

	public ApplicationDropdownDto getAppDropDownDetails(Long dealId) throws Exception;

	public ApplicationInfoDto saveAppDetails(Long dealId, ApplicationInfoDto applicationInfoDto) throws Exception;

	public ApplicationInfoDto getAppDetails(Long dealId) throws Exception;

	public ApplicationRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public ResponseEntity<Object> updateAppPrice(List<ApplicationPriceDto> applicationPriceDtoList, Long appId)
			throws Exception;

	public ApplicationRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long appId)
			throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;
}
