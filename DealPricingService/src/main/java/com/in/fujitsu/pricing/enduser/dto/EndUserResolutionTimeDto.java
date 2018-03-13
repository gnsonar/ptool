package com.in.fujitsu.pricing.enduser.dto;

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
public class EndUserResolutionTimeDto {

	private Long id;
	private String resolutionName;
	private String resolutionRange;
	private boolean isActive;

}
