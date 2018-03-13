package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.util.List;

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
public class EndUserYearlyDataInfoDto implements Serializable {

	private static final long serialVersionUID = 5371553307602589889L;

	private Long yearId;
	private Integer endUserDevices;
	private Integer laptops;
	private Integer highEndLaptops;
	private Integer standardLaptops;
	private Integer desktops;
	private Integer thinClients;
	private Integer mobileDevices;
	private Integer imacDevices;
	private Integer year;

	private List<EndUserUnitPriceInfoDto> endUserUnitPriceInfoDtoList;
	private List<EndUserRevenueInfoDto> endUserRevenueInfoDtoList;


}
