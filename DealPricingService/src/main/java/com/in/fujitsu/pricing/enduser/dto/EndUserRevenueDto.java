package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class EndUserRevenueDto implements Serializable {

	private static final long serialVersionUID = 8240559275372309682L;

	private EndUserCalculateDto endUserDevicesCalculateDto;
	private EndUserCalculateDto laptopsCalculateDto;
	private EndUserCalculateDto highEndLaptopsCalculateDto;
	private EndUserCalculateDto standardLaptopsCalculateDto;
	private EndUserCalculateDto desktopsCalculateDto;
	private EndUserCalculateDto thinClientsCalculateDto;
	private EndUserCalculateDto mobileDevicesCalculateDto;
	private EndUserCalculateDto imacDevicesCalculateDto;

}
