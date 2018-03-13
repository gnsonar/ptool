package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationRevenueInfoDto implements Serializable {

	private static final long serialVersionUID = 9129977584436239044L;

	private BigDecimal totalAppsRevenue;
	private int year;

}
