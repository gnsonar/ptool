package com.in.fujitsu.pricing.scenario.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ScenarioCriteriaInfoDto implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	private boolean hostingOffshore;
	private boolean hostingHardware;
	private String hostingLevelOfService;
	private boolean hostingTooling;
	private String hostingcoLocation;

	private boolean storageOffshore;
	private boolean storageHardware;
	private String storageLevelOfService;

	private boolean endUserOffshore;
	private boolean endUserHardware;
	private boolean endUserBreakFix;
	private String endUserResolutionTime;

	private boolean networkOffshore;
	private boolean networkHardware;
	private String networkLevelOfService;

	private boolean serviceDeskOffshore;
	private String serviceDeskLevelOfService;
	private boolean serviceDeskTooling;
	private boolean serviceDeskMultiLingual;

	private boolean applicationOffshore;
	private String applicationLevelOfService;

	private boolean retailOffshore;
	private boolean retailHardware;
	private String retailEquipmentAge;
	private String retailEquipmentSet;

}
