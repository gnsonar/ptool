package com.in.fujitsu.pricing.controller;

import com.in.fujitsu.pricing.dto.TotalsDto;


/**
 * @author ChhabrMa
 *
 */
public interface TotalsController {

	public TotalsDto getTotalDetails(Long dealId) throws Exception;

}

