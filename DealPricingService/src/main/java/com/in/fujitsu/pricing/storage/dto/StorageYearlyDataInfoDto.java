package com.in.fujitsu.pricing.storage.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageYearlyDataInfoDto {
	private Integer year;
	private List<StorageUnitPriceInfoDto> unitPrice;
	private List<StorageRevenueInfoDto> revenue;
	private Integer storageVolume;
	private Integer performanceStorage;
	private Integer nonPerformanceStorage;
	private Integer backupVolume;
	private String backupType;

}
