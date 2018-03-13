package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserDropdownDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserPriceDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueDto;

public interface EndUserController {

	public EndUserDropdownDto getDropDownDetails(Long dealId) throws Exception;

	public EndUserInfoDto saveDetails(Long dealId, EndUserInfoDto endUserInfoDto) throws Exception;

	public EndUserInfoDto getDetails(Long dealId) throws Exception;

	public ResponseEntity<Object> updatePrice(Long endUserId, List<EndUserPriceDto> endUserPriceDtoList) throws Exception;

	public EndUserRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;

	public EndUserRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long endUserId) throws Exception;

}
