package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author MishraSub
 *
 */
@Getter
@Setter
@ToString
public class RetailRevenueDto implements Serializable {

	private static final long serialVersionUID = 5974298366202867867L;

	private RetailCalculateDto noOfShopsCalculateDto;

}
