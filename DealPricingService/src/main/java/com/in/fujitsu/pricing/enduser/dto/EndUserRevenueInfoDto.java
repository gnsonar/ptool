
package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EndUserRevenueInfoDto implements Serializable , Cloneable {

	private static final long serialVersionUID = -6865933222135862282L;

	private long id;

	private Integer totalEndUserDevices;

	private Integer totalImacDevices;

	private BigDecimal totalRevenue;

	private String benchMarkType;

	private int year;

}
