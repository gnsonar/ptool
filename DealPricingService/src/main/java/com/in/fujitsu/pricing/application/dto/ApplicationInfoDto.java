package com.in.fujitsu.pricing.application.dto;

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
public class ApplicationInfoDto implements Serializable {

	private static final long serialVersionUID = -7600427903018374314L;

	private Long id;
	private boolean offshoreAllowed;
	private String levelOfService;
	private Integer selectedAppSolution;
	private String towerArchitect;
	private String levelIndicator;

	private List<ApplicationYearlyDataInfoDto> appYearlyDataInfoDtoList;

	private Long dealId;

}
