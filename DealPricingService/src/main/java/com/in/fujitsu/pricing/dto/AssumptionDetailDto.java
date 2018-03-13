package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AssumptionDetailDto extends TimestampDto {

	private int id;
	private String assumptionDesc;
}
