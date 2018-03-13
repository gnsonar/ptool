package com.in.fujitsu.pricing.network.dto;

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
public class NetworkInfoDto implements Serializable {

	private static final long serialVersionUID = -4776601130450889942L;

	private Long networkId;
	private boolean offshoreAllowed;
	private boolean includeHardware;
	private String levelOfService;
	private Integer selectedWanSolutionId;
	private Integer selectedLanSolutionId;
	private Long dealId;
	private String levelIndicator;
	private String towerArchitect;

	private List<NetworkYearlyDataInfoDto> networkYearlyDataInfoDtoList;

}
