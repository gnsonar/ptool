package com.in.fujitsu.pricing.dto;

import java.io.Serializable;

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
public class SolutionCriteriaDto implements Serializable {

	private static final long serialVersionUID = 3804741062355446413L;
	private boolean offshoreAllowed;
	private boolean includeHardware;
	private String levelOfService;
	private boolean multiLingual;
	private boolean toolingIncluded;
	private boolean includeBreakFix;
	private String resolutionTime;
	private String coLocation;
	private String equipmentAge;
	private String equipmentSet;

}
