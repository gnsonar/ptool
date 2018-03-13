package com.in.fujitsu.pricing.storage.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageUnitPriceInfoDto {

	private BigDecimal storageVolumeUnitPrice;
	private BigDecimal performanceUnitPrice;
	private BigDecimal nonPerformanceUnitPrice;
	private BigDecimal backupVolumeUnitPrice;
	private String benchMarkType;

}
