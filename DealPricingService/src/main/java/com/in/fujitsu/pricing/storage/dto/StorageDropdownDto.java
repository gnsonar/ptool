/**
 *
 */
package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;
import com.in.fujitsu.pricing.dto.VolumeDto;

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
public class StorageDropdownDto implements Serializable {

	private static final long serialVersionUID = -1726706431299754248L;
	private List<ServiceWindowDto> standardWindowInfoList;
	private List<String> offshoreAndHardwareIncludedList;
	private List<StorageSolutionsInfoDto> storageSolutionsInfoDtoList;
	private List<StorageBackupInfoDto> storageBackupInfoDtoList;
	private List<StorageDefaultInfoDto> storageDefaultInfoDtoList;
	private List<VolumeDto> hostingServerList;
	private DealInfoDto dealInfoDto;

}
