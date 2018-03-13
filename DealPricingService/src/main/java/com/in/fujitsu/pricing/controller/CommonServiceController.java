package com.in.fujitsu.pricing.controller;

import java.util.List;

import com.in.fujitsu.pricing.dto.FJEmployeeDto;

/**
 * @author mishrasub
 *
 */
public interface CommonServiceController {

	public List<FJEmployeeDto> getUserDetails(String name) throws Exception;


}

