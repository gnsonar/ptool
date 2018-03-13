package com.in.fujitsu.pricing.servicedesk.dto;

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
public class ServiceDeskInfoDto implements Serializable {

	private static final long serialVersionUID = 343002579973146967L;

	private Long serviceDeskId;

	private boolean offshoreAllowed;

	private String levelOfService;

	private boolean multiLingual;

	private boolean toolingIncluded;

	private Integer selectedContactSolution;

	private Integer selectedContactRatio;

	private List<ServiceDeskYearlyDataInfoDto> serviceDeskYearlyDataInfoDtoList;

	private Long dealId;

	private String levelIndicator;

	private String towerArchitect;

}
