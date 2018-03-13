package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class EndUserInfoDto implements Serializable {

	private static final long serialVersionUID = 6467244133160638033L;

	private Long endUserId;
	private boolean offshoreAllowed;
	private boolean includeBreakFix;
	private boolean includeHardware;
	private String resolutionTime;
	private String imacType;
	private Integer selectedSolution;
	private Long dealId;
	private String levelIndicator;
	private String towerArchitect;
	private Integer selectedContactSolution;

	private List<EndUserYearlyDataInfoDto> endUserYearlyDataInfoDtoList;

}
