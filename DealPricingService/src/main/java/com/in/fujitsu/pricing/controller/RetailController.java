package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.retail.dto.RetailDropdownDto;
import com.in.fujitsu.pricing.retail.dto.RetailInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailPriceDto;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueDto;

public interface RetailController {

	public RetailDropdownDto getDropDownDetails(Long dealId) throws Exception;

	public RetailInfoDto getDetails(Long dealId) throws Exception;

	public RetailInfoDto saveDetails(Long dealId, RetailInfoDto retailInfoDto) throws Exception;

	public ResponseEntity<Object> updatePrice(Long retailId, List<RetailPriceDto> retailPriceDtoList) throws Exception;

	public RetailRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long retailId)
			throws Exception;

	public RetailRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;

}
