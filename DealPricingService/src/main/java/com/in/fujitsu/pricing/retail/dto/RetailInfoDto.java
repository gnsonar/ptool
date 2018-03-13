package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class RetailInfoDto implements Serializable {

	private static final long serialVersionUID = 902290171937813251L;

	private Long retailId;

	private boolean offshoreAllowed;

	private boolean includeHardware;

	private String equipmentAge;

	private String equipmentSet;

	private String levelIndicator;

	private String towerArchitect;

	private List<RetailYearlyDataInfoDto> retailYearlyDataInfoDtoList;

	private Long dealId;

}
