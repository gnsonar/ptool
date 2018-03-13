package com.in.fujitsu.pricing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.dto.HostInfoDto;
import com.in.fujitsu.pricing.service.SystemService;

@RestController
public class SystemController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(value = "/getHostDetails")
	public HostInfoDto getHostDetails() {
		return systemService.gethostDetails();
	}

}
