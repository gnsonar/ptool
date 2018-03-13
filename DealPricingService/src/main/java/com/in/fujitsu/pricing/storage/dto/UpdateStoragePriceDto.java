package com.in.fujitsu.pricing.storage.dto;

import java.math.BigDecimal;

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
public class UpdateStoragePriceDto {
	private int year;

	private BigDecimal storageUnitPrice;
	private BigDecimal performanceUnitPrice;
	private BigDecimal nonPerformanceUnitPrice;
	private BigDecimal backupUnitPrice;

	private Integer storageRevenue;
	private Integer backupRevenue;

}
