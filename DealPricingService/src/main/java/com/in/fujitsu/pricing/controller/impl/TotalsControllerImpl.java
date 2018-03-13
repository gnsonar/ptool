package com.in.fujitsu.pricing.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.TotalsController;
import com.in.fujitsu.pricing.dto.TotalsDto;
import com.in.fujitsu.pricing.exception.UnauthorizedAccessException;
import com.in.fujitsu.pricing.service.GenericService;
import com.in.fujitsu.pricing.service.TotalsService;

/**
 * @author ChhabrMa
 *
 */
@RestController
@RequestMapping("resources/totalRevenues")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class TotalsControllerImpl implements TotalsController {

	@Autowired
	private TotalsService totalsService;

	@Autowired
	private GenericService genericService;

	@Override
	@GetMapping(path = "/{dealId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TotalsDto getTotalDetails(@PathVariable("dealId") Long dealId) throws Exception {
		boolean authorizedUser = genericService.isAuthorized(dealId);
		if(authorizedUser) {
			return totalsService.getTotalsDetails(dealId);
		} else {
			throw new UnauthorizedAccessException("Unauthorized Access.");
		}
	}

}
