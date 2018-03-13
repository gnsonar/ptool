package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class ServiceDeskPriceDto implements Serializable {

	private static final long serialVersionUID = -8637323796759219463L;

	private int year;

	private BigDecimal totalContactsUnitPrice;

	private BigDecimal voiceContactsUnitPrice;

	private BigDecimal mailContactsUnitPrice;

	private BigDecimal chatContactsUnitPrice;

	private BigDecimal portalContactsUnitPrice;

	private Integer totalContactsRevenue;

}
