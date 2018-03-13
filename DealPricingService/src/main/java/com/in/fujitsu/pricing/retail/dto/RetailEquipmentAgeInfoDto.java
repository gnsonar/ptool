package com.in.fujitsu.pricing.retail.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author pawarbh
 *
 */
@Getter
@Setter
@ToString
public class RetailEquipmentAgeInfoDto {

	private String age;
	private String ageDesc;
	private boolean isActive;

}
