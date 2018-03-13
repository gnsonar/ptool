package com.in.fujitsu.pricing.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.AssumptionController;
import com.in.fujitsu.pricing.dto.AssumptionResponse;
import com.in.fujitsu.pricing.service.AssumptionService;

@RestController
@RequestMapping("resources/assumptions")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE })
public class AssumptionControllerImpl implements AssumptionController{

	@Autowired
	private AssumptionService assumptionService;

	@Override
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public AssumptionResponse getAssumptionDeatails() throws Exception {
		return assumptionService.getAssumptionDetails();
	}

}
