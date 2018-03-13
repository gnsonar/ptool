package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.hosting.dto.HostingDropdownDto;
import com.in.fujitsu.pricing.hosting.dto.HostingInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingPriceDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueDto;

/**
 * @author MishraSub
 *
 */
public interface HostingController {

	public HostingDropdownDto getDropDownDetails(Long dealId) throws Exception;

	public HostingInfoDto saveDetails(Long dealId, HostingInfoDto hostingInfoDto) throws Exception;

	public HostingInfoDto getDetails(Long dealId) throws Exception;

	public ResponseEntity<Object> updatePrice(Long hostingId, List<HostingPriceDto> hostingPriceDtoList) throws Exception;

	public HostingRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;

	public HostingRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long hostingId) throws Exception;

}
