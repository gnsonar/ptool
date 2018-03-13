package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageBackupInfoDto implements Serializable {

	private static final long serialVersionUID = 4689515985084923557L;

	private Integer backupId;
	private String backupFrequencyName;
	private Float backupSize;

}
