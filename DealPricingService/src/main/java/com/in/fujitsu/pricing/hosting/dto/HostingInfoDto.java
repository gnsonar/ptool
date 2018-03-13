package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.util.List;

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
public class HostingInfoDto implements Serializable {

	private static final long serialVersionUID = 3838453584223800242L;

	private Long dealId;
	private Long hostingId;
	private boolean offshoreAllowed;
	private String levelOfService;
	private boolean includeHardware;
	private boolean includeTooling;
	private String coLocation;
	private Integer selectedSolutionId;
	private String levelIndicator;
	private String towerArchitect;

	private List<HostingYearlyDataInfoDto> hostingYearlyDataInfoDtoList;

}
