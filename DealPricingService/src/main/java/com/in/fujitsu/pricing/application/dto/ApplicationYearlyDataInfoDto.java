
package com.in.fujitsu.pricing.application.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationYearlyDataInfoDto implements Serializable {

	private static final long serialVersionUID = -2183711413234866870L;

	private Long id;

	private Integer totalAppsVolume;
	private Integer simpleAppsVolume;
	private Integer mediumAppsVolume;
	private Integer complexAppsVolume;
	private Integer veryComplexAppsVolume;
	private Integer year;

	private List<ApplicationUnitPriceInfoDto> appUnitPriceInfoDtoList;

	private List<ApplicationRevenueInfoDto> applicationRevenueInfoDtoList;

}
