package com.in.fujitsu.pricing.controller;

import java.util.List;

import com.in.fujitsu.pricing.dto.CurrencyDto;

/**
 * @author mishrasub
 *
 */
public interface CurrencyController {

	public List<CurrencyDto> getCurrencyRates() throws Exception;

	public List<CurrencyDto> updateCurrencyRates(List<CurrencyDto> currencyDtoList) throws Exception;

}

