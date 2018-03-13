package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.network.dto.NetworkDropdownDto;
import com.in.fujitsu.pricing.network.dto.NetworkInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkPriceDto;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueDto;

/**
 * @author ChhabrMa
 *
 */
public interface NetworkController {

	public NetworkDropdownDto getDropDownDetails(Long dealId) throws Exception;

	public NetworkInfoDto saveDetails(Long dealId, NetworkInfoDto serviceDeskInfoDto) throws Exception;

	public NetworkInfoDto getDetails(Long dealid) throws Exception;

	public ResponseEntity<Object> updatePrice(Long networkId, List<NetworkPriceDto> networkPriceDtoList) throws Exception;

	public NetworkRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long networkId) throws Exception;

	public NetworkRevenueDto getYearlyRevenues(Long dealId) throws Exception;

	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception;

}
