package com.in.fujitsu.pricing.service;

import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.HostInfoDto;

@Service
public class SystemService {

	private static ResourceBundle hostPropBundle = ResourceBundle.getBundle("host");

	/**
	 * @return
	 */
	public HostInfoDto gethostDetails() {
	 final HostInfoDto hostInfoDto = new HostInfoDto();
		hostInfoDto.setHostname(hostPropBundle.getString("hostname"));
		hostInfoDto.setPortNumber(hostPropBundle.getString("port"));
		return hostInfoDto;
	}

}
