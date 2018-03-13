
package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ServiceDeskUnitPriceInfoDto implements Serializable {

	private static final long serialVersionUID = 1868958092589377126L;

	private long id;

	private BigDecimal totalContactsUnitPrice;

	private BigDecimal voiceContactsUnitPrice;

	private BigDecimal mailContactsUnitPrice;

	private BigDecimal chatContactsUnitPrice;

	private BigDecimal portalContactsUnitPrice;

	// Low or Target in case of Benchmark deal
	private String benchMarkType;

}
