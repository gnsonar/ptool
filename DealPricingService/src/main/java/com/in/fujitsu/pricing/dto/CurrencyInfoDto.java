package com.in.fujitsu.pricing.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class CurrencyInfoDto {

		private Integer currencyCode;
		private String currencyName;
		private Float previousRate;
		private Float currentRate;
		private Date creationDate;
		private Date updatedOn;

}






