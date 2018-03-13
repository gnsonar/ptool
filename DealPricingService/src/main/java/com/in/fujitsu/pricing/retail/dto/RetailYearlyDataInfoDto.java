
package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class RetailYearlyDataInfoDto implements Serializable {

	private static final long serialVersionUID = 7617757043224633093L;

	private Long yearId;

	private Integer noOfShops;

	private Integer year;

	private List<RetailUnitPriceInfoDto> retailUnitPriceInfoDtoList;

	private List<RetailRevenueInfoDto> retailRevenueInfoDtoList;

}
