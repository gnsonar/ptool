
package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class HostingRevenueInfoDto implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	private Integer servers;
	private Integer sqlInstances;
	private Integer cotsInstallations;
	private String benchMarkType;
	private BigDecimal totalRevenue;
	private int year;

}
