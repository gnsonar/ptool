package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.CurrencyController;
import com.in.fujitsu.pricing.dto.CurrencyDto;
import com.in.fujitsu.pricing.service.FXRateService;

/**
 * @author mishrasub
 *
 */
@RestController
@RequestMapping("resources/admin/currency")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class CurrencyControllerImpl implements CurrencyController {

	@Autowired
	FXRateService fXRateService;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CurrencyDto> getCurrencyRates() throws Exception {
		return fXRateService.getCurrencyRates();
	}

	@Override
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CurrencyDto> updateCurrencyRates(@RequestBody List<CurrencyDto> currencyDtoList) throws Exception {
		return fXRateService.updateCurrencyRates(currencyDtoList);
	}

}
