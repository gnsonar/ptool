package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.CommonServiceController;
import com.in.fujitsu.pricing.dto.FJEmployeeDto;
import com.in.fujitsu.pricing.service.LdapService;

/**
 * @author mishrasub
 *
 */
@RestController
@RequestMapping("resources/common")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class CommonServiceControllerImpl implements CommonServiceController {

	@Autowired
	private LdapService ldapService;

	@Override
	@GetMapping(path="ldapUsers",produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FJEmployeeDto> getUserDetails(String name) throws Exception {
		// TODO Auto-generated method stub
		return ldapService.findByName(name);
	}

}
