package com.in.fujitsu.pricing.storage.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.storage.dto.StorageBackupInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageDefaultInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageSolutionsInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageUnitPriceInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageYearlyDataInfoDto;
import com.in.fujitsu.pricing.storage.dto.UpdateStoragePriceDto;
import com.in.fujitsu.pricing.storage.entity.StorageBackupInfo;
import com.in.fujitsu.pricing.storage.entity.StorageDefaultInfo;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageRevenueInfo;
import com.in.fujitsu.pricing.storage.entity.StorageSolutionsInfo;
import com.in.fujitsu.pricing.storage.entity.StorageUnitPriceInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;
import com.in.fujitsu.pricing.storage.repository.StorageYearlyRepository;

/**
 * @author Maninder
 *
 */
@Component
public class StorageBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private StorageYearlyRepository storageYearlyRepository;

	/**
	 * @param backups
	 * @return
	 */
	public List<StorageBackupInfoDto> prepareStorageBackupDtoList(List<StorageBackupInfo> backups) {
		final List<StorageBackupInfoDto> backupDtoList = new ArrayList<>();
		for (StorageBackupInfo backupInfo : backups) {
			final StorageBackupInfoDto backupDto = modelMapper.map(backupInfo, StorageBackupInfoDto.class);
			backupDtoList.add(backupDto);
		}
		return backupDtoList;
	}

	public List<StorageSolutionsInfoDto> prepareStorageSolutionsDtoList(List<StorageSolutionsInfo> solutions) {
		final List<StorageSolutionsInfoDto> solutionsDtoList = new ArrayList<>();
		for (StorageSolutionsInfo solutionsInfo : solutions) {
			final StorageSolutionsInfoDto solutionsDto = modelMapper.map(solutionsInfo, StorageSolutionsInfoDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	public List<StorageDefaultInfoDto> prepareStorageDefaultsDtoList(List<StorageDefaultInfo> storageDefaults) {
		final List<StorageDefaultInfoDto> storageDefaultInfoDtoList = new ArrayList<>();
		for (StorageDefaultInfo storageDefaultInfo : storageDefaults) {
			final StorageDefaultInfoDto storageDefaultInfoDto = modelMapper.map(storageDefaultInfo,
					StorageDefaultInfoDto.class);
			storageDefaultInfoDtoList.add(storageDefaultInfoDto);
		}
		return storageDefaultInfoDtoList;
	}

	/**
	 * @param storageInfo
	 * @return
	 */
	public StorageInfoDto prepareStorageInfoDto(StorageInfo storageInfo) {
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		final StorageInfoDto storageInfoDto = modelMapper.map(storageInfo, StorageInfoDto.class);
		storageInfoDto.setDealId(storageInfo.getDealInfo().getDealId());

		final List<StorageYearlyDataInfoDto> yearlyDataInfoDtoList = new ArrayList<>();
		for (StorageYearlyDataInfo storageYearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
			final StorageYearlyDataInfoDto storageYearlyDataInfoDto = modelMapper.map(storageYearlyDataInfo,
					StorageYearlyDataInfoDto.class);
			/*if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageUnitPriceInfo())) {
				final List<StorageUnitPriceInfoDto> unitPriceInfoDtoList = new ArrayList<>();
				for (StorageUnitPriceInfo storageUnitPriceInfo : storageYearlyDataInfo.getStorageUnitPriceInfo()) {
					final StorageUnitPriceInfoDto storageUnitPriceDto = modelMapper.map(storageUnitPriceInfo,
							StorageUnitPriceInfoDto.class);
					unitPriceInfoDtoList.add(storageUnitPriceDto);
				}
				storageYearlyDataInfoDto.setUnitPrice(unitPriceInfoDtoList);
			}*/

			/*if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageRevenueInfoList())) {
				final List<StorageRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
				for (StorageRevenueInfo storageRevenueInfo : storageYearlyDataInfo.getStorageRevenueInfoList()) {
					final StorageRevenueInfoDto storageRevenueDto = modelMapper.map(storageRevenueInfo, StorageRevenueInfoDto.class);
					revenueInfoDtoList.add(storageRevenueDto);
				}
				storageYearlyDataInfoDto.setRevenue(revenueInfoDtoList);
			}*/
			yearlyDataInfoDtoList.add(storageYearlyDataInfoDto);
		}

		storageInfoDto.setStorageYearlyDataInfoDtos(yearlyDataInfoDtoList);

		return storageInfoDto;
	}

	/**
	 * @param storageInfo
	 * @param storageInfoDto
	 * @param isSave
	 * @return
	 */
	public StorageInfo prepareStorageInfo(StorageInfo storageInfo, StorageInfoDto storageInfoDto, boolean isSave) {

		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(storageInfoDto.getDealId());
			storageInfo.setDealInfo(dealInfo);
		}

		storageInfo.setBackupFrequency(storageInfoDto.getBackupFrequency());
		storageInfo.setIncludeHardware(storageInfoDto.isIncludeHardware());
		storageInfo.setOffshoreAllowed(storageInfoDto.isOffshoreAllowed());
		storageInfo.setServiceWindowSla(storageInfoDto.getServiceWindowSla());
		storageInfo.setSelectedSolution(storageInfoDto.getSelectedSolution());
		storageInfo.setTowerArchitect(storageInfoDto.getTowerArchitect() !=null ?  storageInfoDto.getTowerArchitect() : storageInfo.getTowerArchitect());
		storageInfo.setLevelIndicator(storageInfoDto.getLevelIndicator());

		List<StorageYearlyDataInfo> storageYearlyDataInfoList = new ArrayList<>();
		// Below code is to handle Past deal entries in yearly & unit
		if (!CollectionUtils.isEmpty(storageInfoDto.getStorageYearlyDataInfoDtos())) {
			int size = storageInfoDto.getStorageYearlyDataInfoDtos().size();
			for (int i = 0; i < size; i++) {
				StorageYearlyDataInfoDto storageYearlyDataInfoDto = storageInfoDto.getStorageYearlyDataInfoDtos()
						.get(i);
				StorageYearlyDataInfo storageYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(storageInfo.getStorageYearlyDataInfos())
						&& storageInfo.getStorageYearlyDataInfos().size() - 1 >= i)) {
					for (int j = 0; j < storageInfo.getStorageYearlyDataInfos().size(); j++) {
						StorageYearlyDataInfo existingStorageYearlyDataInfo = storageInfo.getStorageYearlyDataInfos()
								.get(j);
						if (existingStorageYearlyDataInfo.getYear().equals(storageYearlyDataInfoDto.getYear())) {
							storageYearlyDataInfo = existingStorageYearlyDataInfo;
							break;
						}
					}
				} else {
					storageYearlyDataInfo = new StorageYearlyDataInfo();
				}
				storageYearlyDataInfoList = setStorageYearlyDetails(storageYearlyDataInfoList, storageInfo,
						storageYearlyDataInfo, storageYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(storageInfo.getStorageYearlyDataInfos())
					&& size < storageInfo.getStorageYearlyDataInfos().size()) {
				for (int i = storageInfo.getStorageYearlyDataInfos().size() - 1; i >= size; i--) {
					storageYearlyRepository.delete(storageInfo.getStorageYearlyDataInfos().get(i).getId());
				}
			}
		}

		storageInfo.setStorageYearlyDataInfos(storageYearlyDataInfoList);

		return storageInfo;

	}

	/**
	 * @param storageYearlyDataInfos
	 * @param storageInfo
	 * @param storageYearlyDataInfo
	 * @param storageYearlyDataInfoDto
	 * @return
	 */
	private List<StorageYearlyDataInfo> setStorageYearlyDetails(List<StorageYearlyDataInfo> storageYearlyDataInfos,
			StorageInfo storageInfo, StorageYearlyDataInfo storageYearlyDataInfo,
			StorageYearlyDataInfoDto storageYearlyDataInfoDto) {
		storageYearlyDataInfo.setBackupVolume(
				storageYearlyDataInfoDto.getBackupVolume() == null ? 0 : storageYearlyDataInfoDto.getBackupVolume());
		storageYearlyDataInfo.setPerformanceStorage(storageYearlyDataInfoDto.getPerformanceStorage() == null ? 0
				: storageYearlyDataInfoDto.getPerformanceStorage());
		storageYearlyDataInfo.setNonPerformanceStorage(storageYearlyDataInfoDto.getNonPerformanceStorage() == null ? 0
				: storageYearlyDataInfoDto.getNonPerformanceStorage());
		storageYearlyDataInfo.setStorageVolume(
				storageYearlyDataInfoDto.getStorageVolume() == null ? 0 : storageYearlyDataInfoDto.getStorageVolume());
		storageYearlyDataInfo.setYear(storageYearlyDataInfoDto.getYear());

		storageYearlyDataInfo.setStorageInfo(storageInfo);
		// set the Unit Price
		List<StorageUnitPriceInfo> storageUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoDto.getUnitPrice())) {
			if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageUnitPriceInfo())) {
				for (StorageUnitPriceInfo storageUnitPrice : storageYearlyDataInfo.getStorageUnitPriceInfo()) {
					StorageUnitPriceInfoDto storageUnitPriceInfoDto = storageYearlyDataInfoDto.getUnitPrice().get(0);
					if (storageUnitPriceInfoDto != null) {
						setUnitPrices(storageUnitPriceInfoDto, storageUnitPrice);
						storageUnitPriceInfoList.add(storageUnitPrice);
					}
				}
				storageYearlyDataInfo.setStorageUnitPriceInfo(storageUnitPriceInfoList);
			} else {
				for (StorageUnitPriceInfoDto storageUnitPriceDto : storageYearlyDataInfoDto.getUnitPrice()) {
					StorageUnitPriceInfo storageUnitPriceInfo = new StorageUnitPriceInfo();
					setUnitPrices(storageUnitPriceDto, storageUnitPriceInfo);
			
					storageUnitPriceInfo.setStorageYearlyDataInfo(storageYearlyDataInfo);
					storageUnitPriceInfoList.add(storageUnitPriceInfo);
				}
				storageYearlyDataInfo.setStorageUnitPriceInfo(storageUnitPriceInfoList);
			}
		}

		// set the Revenue
		List<StorageRevenueInfo> storageRevenueInfoList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(storageYearlyDataInfoDto.getRevenue())) {
			if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageRevenueInfoList())) {
				if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageRevenueInfoList())) {
					for (StorageRevenueInfo storageRevenueInfo : storageYearlyDataInfo.getStorageRevenueInfoList()) {
						StorageRevenueInfoDto storageRevenueInfoDto = storageYearlyDataInfoDto.getRevenue().get(0);
						setRevenues(storageRevenueInfo, storageRevenueInfoDto);
						
						storageRevenueInfoList.add(storageRevenueInfo);
					}
				}
				storageYearlyDataInfo.setStorageRevenueInfoList(storageRevenueInfoList);
			} else {
				for (StorageRevenueInfoDto storageRevenueInfoDto : storageYearlyDataInfoDto.getRevenue()) {
					StorageRevenueInfo storageRevenueInfo = new StorageRevenueInfo();
					setRevenues(storageRevenueInfo, storageRevenueInfoDto);
					
					storageRevenueInfo.setStorageYearlyDataInfo(storageYearlyDataInfo);
					storageRevenueInfoList.add(storageRevenueInfo);
				}
				storageYearlyDataInfo.setStorageRevenueInfoList(storageRevenueInfoList);
			}
		}

		storageYearlyDataInfos.add(storageYearlyDataInfo);

		return storageYearlyDataInfos;

	}

	private void setRevenues(StorageRevenueInfo storageRevenueInfo, StorageRevenueInfoDto storageRevenueInfoDto) {
		storageRevenueInfo.setStorageRevenue(storageRevenueInfoDto.getStorageRevenue() == null
				?0: storageRevenueInfoDto.getStorageRevenue().intValue());
		storageRevenueInfo.setBackupRevenue(storageRevenueInfoDto.getBackupRevenue() == null
				?0: storageRevenueInfoDto.getBackupRevenue().intValue());
	}

	private void setUnitPrices(StorageUnitPriceInfoDto storageUnitPriceDto, StorageUnitPriceInfo storageUnitPriceInfo) {
		storageUnitPriceInfo
				.setStorageVolumeUnitPrice(storageUnitPriceDto.getStorageVolumeUnitPrice() == null
						? BigDecimal.ZERO : storageUnitPriceDto.getStorageVolumeUnitPrice());
		storageUnitPriceInfo.setPerformanceUnitPrice(storageUnitPriceDto.getPerformanceUnitPrice() == null
				? BigDecimal.ZERO : storageUnitPriceDto.getPerformanceUnitPrice());
		storageUnitPriceInfo.setBackupVolumeUnitPrice(storageUnitPriceDto.getBackupVolumeUnitPrice() == null
				? BigDecimal.ZERO : storageUnitPriceDto.getBackupVolumeUnitPrice());
		storageUnitPriceInfo
				.setNonPerformanceUnitPrice(storageUnitPriceDto.getNonPerformanceUnitPrice() == null
						? BigDecimal.ZERO : storageUnitPriceDto.getNonPerformanceUnitPrice());
	}

	/**
	 * @param storageInfo
	 * @param storageInfoDto
	 * @param isSave
	 * @return
	 */
	public StorageInfo prepareStoragePrice(StorageInfo storageInfo,
			List<UpdateStoragePriceDto> storagePriceDtoList) {

		List<StorageYearlyDataInfo> storageYearlyDataInfoList = new ArrayList<>();
		UpdateStoragePriceDto updateStoragePriceDto = new UpdateStoragePriceDto();
		for (StorageYearlyDataInfo yearlydataInfo : storageInfo.getStorageYearlyDataInfos()) {
			for (UpdateStoragePriceDto priceDto : storagePriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					updateStoragePriceDto = priceDto;
					break;
				}
			}
			storageYearlyDataInfoList = updateStorageYearlyDetails(storageYearlyDataInfoList, yearlydataInfo,
					updateStoragePriceDto);
		}

		storageInfo.setStorageYearlyDataInfos(storageYearlyDataInfoList);

		return storageInfo;

	}

	/**
	 * @param storageInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public StorageInfo prepareSolutionCriteria(StorageInfo storageInfo, SolutionCriteriaDto solutionCriteriaDto) {

		storageInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		storageInfo.setIncludeHardware(solutionCriteriaDto.isIncludeHardware());
		storageInfo.setServiceWindowSla(solutionCriteriaDto.getLevelOfService());

		return storageInfo;

	}

	/**
	 * @param storageYearlyDataInfoList
	 * @param storageInfo
	 * @param storageYearlyDataInfo
	 * @param updateStoragePriceDto
	 * @return
	 */
	private List<StorageYearlyDataInfo> updateStorageYearlyDetails(
			List<StorageYearlyDataInfo> storageYearlyDataInfoList, StorageYearlyDataInfo storageYearlyDataInfo,
			UpdateStoragePriceDto updateStoragePriceDto) {
		if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageUnitPriceInfo())) {
			// Update the existing unit prices
			for (StorageUnitPriceInfo storageUnitPriceInfo : storageYearlyDataInfo.getStorageUnitPriceInfo()) {
				storageUnitPriceInfo.setStorageVolumeUnitPrice(updateStoragePriceDto.getStorageUnitPrice());
				storageUnitPriceInfo.setPerformanceUnitPrice(updateStoragePriceDto.getPerformanceUnitPrice());
				storageUnitPriceInfo.setBackupVolumeUnitPrice(updateStoragePriceDto.getBackupUnitPrice());
				storageUnitPriceInfo.setNonPerformanceUnitPrice(updateStoragePriceDto.getNonPerformanceUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<StorageUnitPriceInfo> storageUnitPriceInfoList = new ArrayList<>();
			StorageUnitPriceInfo storageUnitPriceInfo = new StorageUnitPriceInfo();
			storageUnitPriceInfo.setStorageVolumeUnitPrice(updateStoragePriceDto.getStorageUnitPrice());
			storageUnitPriceInfo.setPerformanceUnitPrice(updateStoragePriceDto.getPerformanceUnitPrice());
			storageUnitPriceInfo.setBackupVolumeUnitPrice(updateStoragePriceDto.getBackupUnitPrice());
			storageUnitPriceInfo.setNonPerformanceUnitPrice(updateStoragePriceDto.getNonPerformanceUnitPrice());
			storageUnitPriceInfo.setStorageYearlyDataInfo(storageYearlyDataInfo);

			storageUnitPriceInfoList.add(storageUnitPriceInfo);
			storageYearlyDataInfo.setStorageUnitPriceInfo(storageUnitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageRevenueInfoList())) {
			// Update the existing revenues
			for (StorageRevenueInfo storageRevenueInfo : storageYearlyDataInfo.getStorageRevenueInfoList()) {
				storageRevenueInfo.setStorageRevenue(updateStoragePriceDto.getStorageRevenue());
				storageRevenueInfo.setBackupRevenue(updateStoragePriceDto.getBackupRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<StorageRevenueInfo> storageRevenueInfoList = new ArrayList<>();
			StorageRevenueInfo storageRevenueInfo = new StorageRevenueInfo();
			storageRevenueInfo.setStorageRevenue(updateStoragePriceDto.getStorageRevenue());
			storageRevenueInfo.setBackupRevenue(updateStoragePriceDto.getBackupRevenue());
			storageRevenueInfo.setStorageYearlyDataInfo(storageYearlyDataInfo);

			storageRevenueInfoList.add(storageRevenueInfo);
			storageYearlyDataInfo.setStorageRevenueInfoList(storageRevenueInfoList);
		}

		storageYearlyDataInfoList.add(storageYearlyDataInfo);

		return storageYearlyDataInfoList;
	}

}
