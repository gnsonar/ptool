package com.in.fujitsu.pricing.scenario.helper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.application.repository.ApplicationRepository;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.enduser.repository.EndUserRepository;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;
import com.in.fujitsu.pricing.network.repository.NetworkRepository;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;
import com.in.fujitsu.pricing.retail.repository.RetailRepository;
import com.in.fujitsu.pricing.scenario.dto.ScenarioCriteriaInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioVolumeInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioYearlyInfoDto;
import com.in.fujitsu.pricing.scenario.entity.ScenarioCriteriaInfo;
import com.in.fujitsu.pricing.scenario.entity.ScenarioInfo;
import com.in.fujitsu.pricing.scenario.entity.ScenarioYearlyInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskRepository;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;
import com.in.fujitsu.pricing.storage.repository.StorageRepository;

@Component
public class ScenarioBeanConvertor {

	@Autowired
	HostingRepository hostingRepository;

	@Autowired
	StorageRepository storageRepository;

	@Autowired
	EndUserRepository endUserRepository;

	@Autowired
	NetworkRepository networkRepository;

	@Autowired
	ServiceDeskRepository serviceDeskRepository;

	@Autowired
	ApplicationRepository applicationRepository;

	@Autowired
	RetailRepository retailRepository;

	private ModelMapper modelMapper = new ModelMapper();

	public ScenarioInfo prepareScenarioInfo(Long dealId, ScenarioInfoDto scenarioInfoDto) {
		ScenarioInfo scenarioInfo = new ScenarioInfo();
		scenarioInfo.setDealId(scenarioInfoDto.getDealId());
		scenarioInfo.setMigrationCostApplicable(scenarioInfoDto.isMigrationCostApplicable());
		scenarioInfo.setScenarioDesc(scenarioInfoDto.getScenarioDesc());
		scenarioInfo.setScenarioName(scenarioInfoDto.getScenarioName());
		scenarioInfo.setServiceGovernance(scenarioInfoDto.getServiceGovernance());
		scenarioInfo.setTransitionFees(scenarioInfoDto.getTransitionFees());

		List<ScenarioYearlyInfo> yearlyList = new ArrayList<>();
		for (ScenarioYearlyInfoDto yearlyDto : scenarioInfoDto.getScenarioYearlyInfoDtoList()) {
			final ScenarioYearlyInfo yearlyInfo = modelMapper.map(yearlyDto,ScenarioYearlyInfo.class);
			yearlyInfo.setScenarioInfo(scenarioInfo);
			yearlyList.add(yearlyInfo);
		}
		scenarioInfo.setScenarioYearlyInfoList(yearlyList);

		final ScenarioCriteriaInfo criteriaInfo = new ScenarioCriteriaInfo();
		setTowerSpecificCriteria(dealId, criteriaInfo);
		criteriaInfo.setScenarioInfo(scenarioInfo);
		scenarioInfo.setScenarioCriteriaInfo(criteriaInfo);

		return scenarioInfo;
	}

	public void setTowerSpecificCriteria(Long dealId, final ScenarioCriteriaInfo criteriaInfo) {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);

		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		if (null != hostingInfo) {
			setHostingCriteria(criteriaInfo,hostingInfo);
		}

		StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
		if (null != storageInfo) {
			setStorageCriteria(criteriaInfo,storageInfo);
		}

		NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
		if (null != networkInfo) {
			setNetworkCriteria(criteriaInfo,networkInfo);
		}

		EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
		if (null != endUserInfo) {
			setEndUserCriteria(criteriaInfo,endUserInfo);
		}

		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
		if (null != serviceDeskInfo) {
			setServiceDeskCriteria(criteriaInfo,serviceDeskInfo);
		}

		ApplicationInfo appInfo = applicationRepository.findByDealInfo(dealInfo);
		if (null != appInfo) {
			setApplicationCriteria(criteriaInfo,appInfo);
		}

		RetailInfo retailInfo = retailRepository.findByDealInfo(dealInfo);
		if (null != retailInfo) {
			setRetailCriteria(criteriaInfo,retailInfo);
		}
	}

	private void setNetworkCriteria(ScenarioCriteriaInfo criteriaInfo, NetworkInfo networkInfo) {
		criteriaInfo.setNetworkOffshore(networkInfo.isOffshoreAllowed());
		criteriaInfo.setNetworkHardware(networkInfo.isIncludeHardware());
		criteriaInfo.setNetworkLevelOfService(networkInfo.getLevelOfService());
	}

	private void setHostingCriteria(ScenarioCriteriaInfo criteriaInfo, HostingInfo hostingInfo){
		criteriaInfo.setHostingcoLocation(hostingInfo.getCoLocation());
		criteriaInfo.setHostingHardware(hostingInfo.isIncludeHardware());
		criteriaInfo.setHostingLevelOfService(hostingInfo.getLevelOfService());
		criteriaInfo.setHostingOffshore(hostingInfo.isOffshoreAllowed());
		criteriaInfo.setHostingTooling(hostingInfo.isIncludeTooling());
	}

	private void setStorageCriteria(ScenarioCriteriaInfo criteriaInfo, StorageInfo storageInfo){
		criteriaInfo.setStorageHardware(storageInfo.isIncludeHardware());
		criteriaInfo.setStorageLevelOfService(storageInfo.getServiceWindowSla());
		criteriaInfo.setStorageOffshore(storageInfo.isOffshoreAllowed());
	}

	private void setEndUserCriteria(ScenarioCriteriaInfo criteriaInfo, EndUserInfo endUserInfo){
		criteriaInfo.setEndUserHardware(endUserInfo.isIncludeHardware());
		criteriaInfo.setEndUserOffshore(endUserInfo.isOffshoreAllowed());
		criteriaInfo.setEndUserBreakFix(endUserInfo.isIncludeBreakFix());
		criteriaInfo.setEndUserResolutionTime(endUserInfo.getResolutionTime());
	}

	private void setServiceDeskCriteria(ScenarioCriteriaInfo criteriaInfo, ServiceDeskInfo serviceDeskInfo){
		criteriaInfo.setServiceDeskLevelOfService(serviceDeskInfo.getLevelOfService());
		criteriaInfo.setServiceDeskOffshore(serviceDeskInfo.isOffshoreAllowed());
		criteriaInfo.setServiceDeskTooling(serviceDeskInfo.isToolingIncluded());
		criteriaInfo.setServiceDeskMultiLingual(serviceDeskInfo.isMultiLingual());
	}

	private void setRetailCriteria(ScenarioCriteriaInfo criteriaInfo, RetailInfo retailInfo){
		criteriaInfo.setRetailHardware(retailInfo.isIncludeHardware());
		criteriaInfo.setRetailOffshore(retailInfo.isOffshoreAllowed());
		criteriaInfo.setRetailEquipmentAge(retailInfo.getEquipmentAge());
		criteriaInfo.setRetailEquipmentSet(retailInfo.getEquipmentSet());
	}

	private void setApplicationCriteria(ScenarioCriteriaInfo criteriaInfo, ApplicationInfo appInfo){
		criteriaInfo.setApplicationLevelOfService(appInfo.getLevelOfService());
		criteriaInfo.setApplicationOffshore(appInfo.isOffshoreAllowed());
	}

	public ScenarioInfoDto prepareScenarioInfoDto(ScenarioInfo scenarioInfo) {
		ScenarioInfoDto scenarioInfoDto = modelMapper.map(scenarioInfo, ScenarioInfoDto.class);
		List<ScenarioYearlyInfoDto> yearlyList = new ArrayList<>();
		for (ScenarioYearlyInfo yearlyInfo : scenarioInfo.getScenarioYearlyInfoList()) {
			final ScenarioYearlyInfoDto yearlyInfoDto = modelMapper.map(yearlyInfo,ScenarioYearlyInfoDto.class);
			yearlyList.add(yearlyInfoDto);
		}
		scenarioInfoDto.setScenarioYearlyInfoDtoList(yearlyList);

		final ScenarioCriteriaInfoDto yearlyDto = modelMapper.map(scenarioInfo.getScenarioCriteriaInfo(),ScenarioCriteriaInfoDto.class);
		scenarioInfoDto.setScenarioCriteriaInfoDto(yearlyDto);

		ScenarioVolumeInfoDto volumeDto = new ScenarioVolumeInfoDto();
		setTowerSpecificVolume(scenarioInfo.getDealId(), volumeDto);
		scenarioInfoDto.setScenarioVolumeInfoDto(volumeDto);

		return scenarioInfoDto;
	}

	public void setTowerSpecificVolume(Long dealId, final ScenarioVolumeInfoDto volumeDto) {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);

		setHostingVolume(dealInfo,volumeDto);
		setStorageVolume(dealInfo, volumeDto);
		setEndUserVolume(dealInfo, volumeDto);
		setNetworkVolume(dealInfo, volumeDto);
		setServiceDeskVolume(dealInfo, volumeDto);
		setApplicationVolume(dealInfo, volumeDto);
		setRetailVolume(dealInfo, volumeDto);
	}

	private void setHostingVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != hostingInfo) {
			if (!CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())) {
				for (HostingYearlyDataInfo yearlyInfo : hostingInfo.getHostingYearlyDataInfoList()) {
					if (yearlyInfo.getServers()!=null && yearlyInfo.getServers()!= 0) {
						totalVolume += yearlyInfo.getServers();
					}
				}
			}
		}
		volumeDto.setHosting(totalVolume);
	}

	private void setStorageVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != storageInfo) {
			if (!CollectionUtils.isEmpty(storageInfo.getStorageYearlyDataInfos())) {
				for (StorageYearlyDataInfo yearlyInfo : storageInfo.getStorageYearlyDataInfos()) {
					if (yearlyInfo.getStorageVolume()!=null && yearlyInfo.getStorageVolume() != 0) {
						totalVolume += yearlyInfo.getStorageVolume();
					}
					if(yearlyInfo.getBackupVolume()!=null && yearlyInfo.getBackupVolume() != 0){
						totalVolume += yearlyInfo.getBackupVolume();
					}
				}
			}
		}
		volumeDto.setStorageBackup(totalVolume);
	}

	private void setEndUserVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != endUserInfo) {
			if (!CollectionUtils.isEmpty(endUserInfo.getEndUserYearlyDataInfoList())) {
				for (EndUserYearlyDataInfo yearlyInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
					if (yearlyInfo.getEndUserDevices()!=null && yearlyInfo.getEndUserDevices() != 0) {
						totalVolume += yearlyInfo.getEndUserDevices();
					}
				}
			}
		}
		volumeDto.setEndUser(totalVolume);
	}

	private void setNetworkVolume(final DealInfo dealInfo,ScenarioVolumeInfoDto volumeDto) {
		NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
		Integer totalWanVolume = 0;
		Integer totalLanVolume = 0;
		if (null != networkInfo) {
			if (!CollectionUtils.isEmpty(networkInfo.getNetworkYearlyDataInfoList())) {
				for (NetworkYearlyDataInfo yearlyInfo : networkInfo.getNetworkYearlyDataInfoList()) {
					if (yearlyInfo.getWanDevices()!=null && yearlyInfo.getWanDevices() != 0) {
						totalWanVolume += yearlyInfo.getWanDevices();
					}
					if (yearlyInfo.getLanDevices()!=null && yearlyInfo.getLanDevices() != 0) {
						totalLanVolume += yearlyInfo.getLanDevices();
					}
				}
			}
		}
		volumeDto.setNetworkWan(totalWanVolume);
		volumeDto.setNetworkLan(totalLanVolume);
	}

	private void setServiceDeskVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != serviceDeskInfo) {
			if (!CollectionUtils.isEmpty(serviceDeskInfo.getServiceDeskYearlyDataInfoList())) {
				for (ServiceDeskYearlyDataInfo yearlyInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
					if (yearlyInfo.getTotalContacts()!=null && yearlyInfo.getTotalContacts()!= 0) {
						totalVolume += yearlyInfo.getTotalContacts();
					}
				}
			}
		}
		volumeDto.setServiceDesk(totalVolume);
	}

	private void setApplicationVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		ApplicationInfo appInfo = applicationRepository.findByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != appInfo) {
			if (!CollectionUtils.isEmpty(appInfo.getAppYearlyDataInfos())) {
				for (ApplicationYearlyDataInfo yearlyInfo : appInfo.getAppYearlyDataInfos()) {
					if (yearlyInfo.getTotalAppsVolume()!=null && yearlyInfo.getTotalAppsVolume()!= 0) {
						totalVolume += yearlyInfo.getTotalAppsVolume();
					}
				}
			}
		}
		volumeDto.setApplication(totalVolume);
	}

	private void setRetailVolume(final DealInfo dealInfo, ScenarioVolumeInfoDto volumeDto) {
		RetailInfo appInfo = retailRepository.findByDealInfo(dealInfo);
		Integer totalVolume = 0;
		if (null != appInfo) {
			if (!CollectionUtils.isEmpty(appInfo.getRetailYearlyDataInfoList())) {
				for (RetailYearlyDataInfo yearlyInfo : appInfo.getRetailYearlyDataInfoList()) {
					if (yearlyInfo.getNoOfShops()!=null && yearlyInfo.getNoOfShops()!= 0) {
						totalVolume += yearlyInfo.getNoOfShops();
					}
				}
			}
		}
		volumeDto.setRetail(totalVolume);
	}


	public List<ScenarioInfoDto> prepareScenarioInfoDto(List<ScenarioInfo> scenarioList) {
		List<ScenarioInfoDto> scenarioDtoList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(scenarioList)){
			for(ScenarioInfo scenarioInfo : scenarioList){
				scenarioDtoList.add(prepareScenarioInfoDto(scenarioInfo));
			}
		}
		return scenarioDtoList;
	}

	public ScenarioCriteriaInfoDto prepareScenarioCriteriaInfoDto(ScenarioCriteriaInfo scenarioCriteriaInfo){
		return modelMapper.map(scenarioCriteriaInfo,ScenarioCriteriaInfoDto.class);
	}


}
