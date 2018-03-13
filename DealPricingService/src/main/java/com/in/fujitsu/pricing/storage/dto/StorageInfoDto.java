package com.in.fujitsu.pricing.storage.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageInfoDto {
	private Long id;
	private boolean offshoreAllowed;
	private boolean includeHardware;
	private String serviceWindowSla;
	private Integer selectedSolution;
	private Long dealId;
	private String dealType;
	private String benchMarkType;
	private String backupFrequency;
	private String towerArchitect;
	private String levelIndicator;

	private List<StorageYearlyDataInfoDto> storageYearlyDataInfoDtos;

}
