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
public class ClientIndustryDto {

	private Long id;
	private String industryName;
	private boolean isActive;

}
