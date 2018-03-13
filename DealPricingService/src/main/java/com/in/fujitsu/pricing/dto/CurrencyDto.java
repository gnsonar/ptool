package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class CurrencyDto implements Serializable {

	private static final long serialVersionUID = 4625820654720969297L;

	private String currencyFrom;

	private List<FxRatesDto> fxRatesDtos = new ArrayList<FxRatesDto>();

}
