package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ServiceDeskRevenueInfoDto implements Serializable{

	private static final long serialVersionUID = 9129977584436239044L;

	private BigDecimal totalContactsRevenue;
	private int year;

}
