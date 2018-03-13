package com.in.fujitsu.pricing.dto;

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
public class FxRatesDto implements Serializable {

	private static final long serialVersionUID = 3474860290697691390L;

	private String currencyTo;

	private BigDecimal rate;

}
