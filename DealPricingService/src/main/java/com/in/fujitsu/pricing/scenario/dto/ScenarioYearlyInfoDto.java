package com.in.fujitsu.pricing.scenario.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ScenarioYearlyInfoDto implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	private Integer year;
	private BigDecimal hosting;
	private BigDecimal storage;
	private BigDecimal endUser;
	private BigDecimal network;
	private BigDecimal serviceDesk;
	private BigDecimal application;
	private BigDecimal retail;
	private BigDecimal towerSubtotal;
	private BigDecimal additionalSubtotal;
	private BigDecimal totalPrice;
	private BigDecimal serviceGov;
	private BigDecimal transitionFees;
	private BigDecimal migrationCost;

}
