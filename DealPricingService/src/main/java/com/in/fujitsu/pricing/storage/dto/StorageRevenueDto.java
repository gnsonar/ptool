package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageRevenueDto implements Serializable {

	private static final long serialVersionUID = -7527205079502518946L;

	private StorageCalculateDto storageCalculateDto;

	private PerformanceCalculateDto performanceCalculateDto;

	private NonPerformanceCalculateDto nonPerformanceCalculateDto;

	private BackupCalculateDto backupCalculateDto;

}
