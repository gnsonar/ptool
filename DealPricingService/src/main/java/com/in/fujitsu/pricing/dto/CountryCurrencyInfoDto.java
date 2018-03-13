/**
 *
 */
package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sovit
 *
 */
@Getter
@Setter
@ToString
public class CountryCurrencyInfoDto {

	private int id;
    private String countryName;
    private String countryCode;
    private String currencyName;
    private String currencyCode;

}
