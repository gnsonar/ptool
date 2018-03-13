package com.in.fujitsu.pricing.network.dto;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class NetworkUnitPriceInfoDto implements Serializable {

	private static final long serialVersionUID = 2989770679116932011L;

	private BigDecimal wanDevices;

	private BigDecimal smallWanDevices;

	private BigDecimal mediumWanDevices;

	private BigDecimal largeWanDevices;

	private BigDecimal lanDevices;

	private BigDecimal smallLanDevices;

	private BigDecimal mediumLanDevices;

	private BigDecimal largeLanDevices;

	private BigDecimal wlanControllers;

	private BigDecimal wlanAccesspoint;

	private BigDecimal loadBalancers;

	private BigDecimal vpnIpSec;

	private BigDecimal dnsDhcpService;

	private BigDecimal firewalls;

	private BigDecimal proxies;

	private String benchMarkType;

}
