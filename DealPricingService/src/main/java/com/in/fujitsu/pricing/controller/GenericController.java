package com.in.fujitsu.pricing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.GenericDealInfoDto;

public interface GenericController {

	public GenericDealInfoDto getGenericDropDownDetails() throws Exception;

	public DealInfoDto saveGenericDealDetails(@RequestBody DealInfoDto dealInfoDto) throws Exception;

	public DealInfoDto getGenericDetailsByDealId(Long dealId) throws Exception;

	public ResponseEntity<Object> updateDealStatus(Long dealId, String dealStatus) throws Exception;

	public ResponseEntity<Object> updateTowerIndicators(Long dealId, String assessmentIndicator, String submissionIndicator)
			throws Exception;

}
