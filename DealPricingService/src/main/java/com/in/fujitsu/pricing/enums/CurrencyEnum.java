package com.in.fujitsu.pricing.enums;

/**
 * @author Maninder
 *
 */
public enum CurrencyEnum {

	EURO("EUR"),
    GREAT_BRITAIN_POUD("GBP"),
    US_DOLLAR("USD"),
    CANADIAN_DOLLAR("CAD"),
    SWISS_FRANC("CHF"),
    SWEDISH_KRONA("SEK"),
    NORWEIGIAN_KRONE("NOK");

	private String name;

	CurrencyEnum( String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
