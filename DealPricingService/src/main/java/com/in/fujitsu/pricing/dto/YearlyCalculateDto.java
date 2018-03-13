package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class YearlyCalculateDto {

	private int year;

	private float unitPrice;

	private float revenue;

}
