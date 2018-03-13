package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.ApplicationRevenueInfoDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationRevenueInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.application.repository.ApplicationRepository;
import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.MigrationCostDto;
import com.in.fujitsu.pricing.dto.TotalsDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserMigrationCostInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueInfoDto;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserMigrationCostInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserRevenueInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.enduser.helper.EndUserBeanConvertor;
import com.in.fujitsu.pricing.enduser.repository.EndUserMigrationCostRepository;
import com.in.fujitsu.pricing.enduser.repository.EndUserRepository;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.hosting.dto.HostingMigrationCostInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueInfoDto;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingMigrationCostInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingRevenueInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.helper.HostingBeanConvertor;
import com.in.fujitsu.pricing.hosting.repository.HostingMigrationCostRepository;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueInfoDto;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkRevenueInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;
import com.in.fujitsu.pricing.network.repository.NetworkRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueInfoDto;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailRevenueInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;
import com.in.fujitsu.pricing.retail.repository.RetailRepository;
import com.in.fujitsu.pricing.scenario.dto.ScenarioVolumeInfoDto;
import com.in.fujitsu.pricing.scenario.entity.ScenarioCriteriaInfo;
import com.in.fujitsu.pricing.scenario.helper.ScenarioBeanConvertor;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueInfoDto;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskRevenueInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;
import com.in.fujitsu.pricing.servicedesk.repository.ServiceDeskRepository;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueInfoDto;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageRevenueInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;
import com.in.fujitsu.pricing.storage.repository.StorageRepository;
import com.in.fujitsu.pricing.utility.GenericModelConvertor;

/**
 * @author ChhabrMa
 *
 */
@Service
public class TotalsService {

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private StorageRepository storageRepository;

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private ServiceDeskRepository serviceDeskRepository;

	@Autowired
	private RetailRepository retailRepository;

	@Autowired
	private NetworkRepository networkRepository;

	@Autowired
	private EndUserRepository endUserRepository;

	@Autowired
	private HostingRepository hostingRepository;

	@Autowired
	private EndUserMigrationCostRepository endUserMigrationCostRepository;

	@Autowired
	private EndUserBeanConvertor endUserBeanConvertor;

	@Autowired
	private HostingMigrationCostRepository hostingMigrationCostRepository;

	@Autowired
	private HostingBeanConvertor hostingBeanConverter;

	@Autowired
	private ScenarioService scenarioService;

	@Autowired
	private ScenarioBeanConvertor scenarioBeanConverter;

	private static final BigDecimal thousand = new BigDecimal(1000);

	@Autowired
	private GenericModelConvertor genericModelConvertor;

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public TotalsDto getTotalsDetails(long dealId) throws Exception {
		final TotalsDto totalsDto = new TotalsDto();
		final DealInfo dealInfo = dealRepository.findByDealId(dealId);
		if (dealInfo != null) {
			final DealInfoDto dealInfoDto = genericModelConvertor.prepareDealInfoDto(dealInfo);
			totalsDto.setDealInfoDto(dealInfoDto);
			totalsDto.setScenarioList(scenarioService.getAllScenarios(dealId));

			final ScenarioCriteriaInfo criteriaInfo = new ScenarioCriteriaInfo();
			scenarioBeanConverter.setTowerSpecificCriteria(dealId, criteriaInfo);

			ScenarioVolumeInfoDto volumeDto = new ScenarioVolumeInfoDto();
			scenarioBeanConverter.setTowerSpecificVolume(dealId, volumeDto);

			totalsDto.setScenarioCriteriaInfoDto(scenarioBeanConverter.prepareScenarioCriteriaInfoDto(criteriaInfo));
			totalsDto.setScenarioVolumeInfoDto(volumeDto);

			setStorageRevenue(dealInfo, totalsDto);
			setApplicationRevenue(dealInfo, totalsDto);
			setServiceDeskRevenue(dealInfo, totalsDto);
			setRetailRevenue(dealInfo, totalsDto);
			setNetworkRevenue(dealInfo, totalsDto);
			setEndUserRevenue(dealInfo, totalsDto);
			setHostingRevenue(dealInfo, totalsDto);
			calculateYearlyMigrationCost(totalsDto);
		}
		return totalsDto;
	}

	/**
	 * @param totalsDto
	 */
	private void calculateYearlyMigrationCost(TotalsDto totalsDto) {
		addCost(totalsDto.getEndUserMigrationCost(), totalsDto);
		addCost(totalsDto.getHostingMigrationCost(), totalsDto);
	}

	/**
	 * @param costList
	 * @param totalsDto
	 */
	private void addCost(List<MigrationCostDto> costList, TotalsDto totalsDto) {
		List<MigrationCostDto> totalMigrationCostList = new ArrayList<>();
		if(CollectionUtils.isEmpty(totalsDto.getTotalMigrationCost())){
			for(MigrationCostDto costDto : costList){
				MigrationCostDto totalYearlyCost = new MigrationCostDto();
				totalYearlyCost.setYear(costDto.getYear());
				totalYearlyCost.setCost(costDto.getCost());
				totalMigrationCostList.add(totalYearlyCost);
			}
			totalsDto.setTotalMigrationCost(totalMigrationCostList);

		} else{
			for(MigrationCostDto costDto : costList){
				for(MigrationCostDto existingCost : totalsDto.getTotalMigrationCost()){
					if(existingCost.getYear() == costDto.getYear()){
						existingCost.setCost(existingCost.getCost().add(costDto.getCost()));
						break;
					}
				}
			}
		}
	}


	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setStorageRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
		List<StorageRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		if (storageInfo != null) {
			BigDecimal backupRevenue = new BigDecimal(0);
			BigDecimal storageRevenue = new BigDecimal(0);
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

			storageYearly: for (StorageYearlyDataInfo storageYearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(storageYearlyDataInfo.getStorageRevenueInfoList())) {
					for (StorageRevenueInfo revenueInfo : storageYearlyDataInfo.getStorageRevenueInfoList()) {
						StorageRevenueInfoDto storageRevenueInfoDto = new StorageRevenueInfoDto();
						backupRevenue = new BigDecimal(revenueInfo.getBackupRevenue()!=null?revenueInfo.getBackupRevenue():0);
						storageRevenue = new BigDecimal(revenueInfo.getStorageRevenue()!=null?revenueInfo.getStorageRevenue():0);
						totalRevenue = backupRevenue.add(storageRevenue).divide(thousand, 2, RoundingMode.HALF_UP);
						storageRevenueInfoDto.setTotalRevenue(totalRevenue);
						storageRevenueInfoDto.setBackupRevenue(backupRevenue);
						storageRevenueInfoDto.setStorageRevenue(storageRevenue);
						storageRevenueInfoDto.setYear(storageYearlyDataInfo.getYear());

						revenueInfoDtoList.add(storageRevenueInfoDto);
					}
				} else {
					break storageYearly;
				}
			}
		}

		totalsDto.setStorageYearlyRevenue(revenueInfoDtoList);
	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setApplicationRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		ApplicationInfo applicationInfo = applicationRepository.findByDealInfo(dealInfo);
		List<ApplicationRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		if (applicationInfo != null) {
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

			applicationYearly: for (ApplicationYearlyDataInfo appYearlyDataInfo : applicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(appYearlyDataInfo.getApplicationRevenueInfoList())) {
					for (ApplicationRevenueInfo revenueInfo : appYearlyDataInfo.getApplicationRevenueInfoList()) {
						ApplicationRevenueInfoDto appRevenueInfoDto = new ApplicationRevenueInfoDto();
						totalRevenue = new BigDecimal(revenueInfo.getTotalAppsRevenue()!=null?revenueInfo.getTotalAppsRevenue():0).divide(thousand, 2, RoundingMode.HALF_UP);
						appRevenueInfoDto.setTotalAppsRevenue(totalRevenue);
						appRevenueInfoDto.setYear(appYearlyDataInfo.getYear());

						revenueInfoDtoList.add(appRevenueInfoDto);
					}
				} else {
					break applicationYearly;
				}
			}
		}

		totalsDto.setAppYearlyRevenue(revenueInfoDtoList);
	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setServiceDeskRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		ServiceDeskInfo serviceDeskInfo = serviceDeskRepository.findByDealInfo(dealInfo);
		List<ServiceDeskRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		if (serviceDeskInfo != null) {
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

			serviceDeskYearly: for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo
					.getServiceDeskYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskRevenueInfoList())) {
					for (ServiceDeskRevenueInfo revenueInfo : serviceDeskYearlyDataInfo
							.getServiceDeskRevenueInfoList()) {
						ServiceDeskRevenueInfoDto serviceDeskRevenueInfoDto = new ServiceDeskRevenueInfoDto();
						totalRevenue = new BigDecimal(revenueInfo.getTotalContactsRevenue()!=null?revenueInfo.getTotalContactsRevenue():0).divide(thousand, 2, RoundingMode.HALF_UP);
						serviceDeskRevenueInfoDto.setTotalContactsRevenue(totalRevenue);
						serviceDeskRevenueInfoDto.setYear(serviceDeskYearlyDataInfo.getYear());

						revenueInfoDtoList.add(serviceDeskRevenueInfoDto);
					}
				} else {
					break serviceDeskYearly;
				}
			}
		}
		totalsDto.setServiceDeskYearlyRevenue(revenueInfoDtoList);

	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setRetailRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		RetailInfo retailInfo = retailRepository.findByDealInfo(dealInfo);
		List<RetailRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		if (retailInfo != null) {
			BigDecimal noOfShopsRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

			retailYearly: for (RetailYearlyDataInfo retailYearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailRevenueInfoList())) {
					for (RetailRevenueInfo revenueInfo : retailYearlyDataInfo.getRetailRevenueInfoList()) {
						RetailRevenueInfoDto retailRevenueInfoDto = new RetailRevenueInfoDto();
						noOfShopsRevenue = new BigDecimal(revenueInfo.getNoOfShops()!=null?revenueInfo.getNoOfShops():0).divide(thousand, 2, RoundingMode.HALF_UP);
						retailRevenueInfoDto.setNoOfShops(noOfShopsRevenue);
						retailRevenueInfoDto.setYear(retailYearlyDataInfo.getYear());

						revenueInfoDtoList.add(retailRevenueInfoDto);
					}
				} else {
					break retailYearly;
				}
			}
		}

		totalsDto.setRetailYearlyRevenue(revenueInfoDtoList);

	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setNetworkRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
		List<NetworkRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		if (networkInfo != null) {
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

			networkYearly: for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo
					.getNetworkYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkRevenueInfoList())) {
					for (NetworkRevenueInfo revenueInfo : networkYearlyDataInfo.getNetworkRevenueInfoList()) {
						NetworkRevenueInfoDto networkRevenueInfoDto = new NetworkRevenueInfoDto();

						networkRevenueInfoDto.setTotalDnsDhcpServiceRevenue(revenueInfo.getTotalDnsDhcpServiceRevenue());
						networkRevenueInfoDto.setTotalFirewallsRevenue(revenueInfo.getTotalFirewallsRevenue());
						networkRevenueInfoDto.setTotalLanRevenue(revenueInfo.getTotalLanRevenue());
						networkRevenueInfoDto.setTotalLoadBalancersRevenue(revenueInfo.getTotalLoadBalancersRevenue());
						networkRevenueInfoDto.setTotalProxiesRevenue(revenueInfo.getTotalProxiesRevenue());
						networkRevenueInfoDto.setTotalVpnIpSecRevenue(revenueInfo.getTotalVpnIpSecRevenue());
						networkRevenueInfoDto.setTotalWanRevenue(revenueInfo.getTotalWanRevenue());
						networkRevenueInfoDto.setTotalWlanAccesspointRevenue(revenueInfo.getTotalWlanAccesspointRevenue());
						networkRevenueInfoDto.setTotalWlanControllersRevenue(revenueInfo.getTotalWlanControllersRevenue());

						totalRevenue = new BigDecimal(revenueInfo.getTotalDnsDhcpServiceRevenue()!=null?revenueInfo.getTotalDnsDhcpServiceRevenue():0)
								.add(new BigDecimal(revenueInfo.getTotalFirewallsRevenue()!=null?revenueInfo.getTotalFirewallsRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalLanRevenue()!=null?revenueInfo.getTotalLanRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalLoadBalancersRevenue()!=null?revenueInfo.getTotalLoadBalancersRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalProxiesRevenue()!=null?revenueInfo.getTotalProxiesRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalVpnIpSecRevenue()!=null?revenueInfo.getTotalVpnIpSecRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalWanRevenue()!=null?revenueInfo.getTotalWanRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalWlanAccesspointRevenue()!=null?revenueInfo.getTotalWlanAccesspointRevenue():0))
								.add(new BigDecimal(revenueInfo.getTotalWlanControllersRevenue()!=null?revenueInfo.getTotalWlanControllersRevenue():0));

						totalRevenue = totalRevenue.divide(thousand, 2, RoundingMode.HALF_UP);
						networkRevenueInfoDto.setTotalRevenue(totalRevenue);
						networkRevenueInfoDto.setYear(networkYearlyDataInfo.getYear());

						revenueInfoDtoList.add(networkRevenueInfoDto);
					}
				} else {
					break networkYearly;
				}
			}
		}
		totalsDto.setNetworkYearlyRevenue(revenueInfoDtoList);

	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setEndUserRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		EndUserInfo endUserInfo = endUserRepository.findByDealInfo(dealInfo);
		List<EndUserRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		BigDecimal migrationCost = new BigDecimal(0);
		List<MigrationCostDto> yearlyMigrationCostList = new ArrayList<>();
		List<EndUserMigrationCostInfo> migrationCostList = new ArrayList<>();
		String migrationCostApplicable = totalsDto.getDealInfoDto().getMigrationCost();
		if(migrationCostApplicable != null && migrationCostApplicable.equalsIgnoreCase("YES") ? true : false){
			migrationCostList = endUserMigrationCostRepository.findAll();
		}
		if (endUserInfo != null) {
			BigDecimal endUserRevenue = new BigDecimal(0);
			BigDecimal imacRevenue = new BigDecimal(0);
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
			for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
				if(migrationCostApplicable != null && migrationCostApplicable.equalsIgnoreCase("YES") ? true : false){
					migrationCost = calculateMigrationCostForEndUser(totalsDto, yearlyDataInfo,migrationCostList);
					MigrationCostDto costDto = new MigrationCostDto();
					costDto.setYear(yearlyDataInfo.getYear());
					costDto.setCost(migrationCost);
					yearlyMigrationCostList.add(costDto);
				}
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserRevenueInfoList())) {
					for (EndUserRevenueInfo revenueInfo : yearlyDataInfo.getEndUserRevenueInfoList()) {
						EndUserRevenueInfoDto endUserRevenueInfoDto = new EndUserRevenueInfoDto();
						endUserRevenue = new BigDecimal(revenueInfo.getTotalEndUserDevices()!=null?revenueInfo.getTotalEndUserDevices():0);
						imacRevenue = new BigDecimal(revenueInfo.getTotalImacDevices()!=null?revenueInfo.getTotalImacDevices():0);
						totalRevenue = endUserRevenue.add(imacRevenue).divide(thousand, 2, RoundingMode.HALF_UP);
						endUserRevenueInfoDto.setTotalRevenue(totalRevenue);
						endUserRevenueInfoDto.setTotalEndUserDevices(endUserRevenue.intValue());
						endUserRevenueInfoDto.setTotalImacDevices(imacRevenue.intValue());
						endUserRevenueInfoDto.setYear(yearlyDataInfo.getYear());

						revenueInfoDtoList.add(endUserRevenueInfoDto);
					}
				}
			}
		}

		totalsDto.setEndUserYearlyRevenue(revenueInfoDtoList);
		totalsDto.setEndUserMigrationCost(yearlyMigrationCostList);
	}

	/**
	 * @param dealInfo
	 * @param totalsDto
	 */
	private void setHostingRevenue(DealInfo dealInfo, TotalsDto totalsDto) {
		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		List<HostingRevenueInfoDto> revenueInfoDtoList = new ArrayList<>();
		BigDecimal migrationCost = new BigDecimal(0);
		List<MigrationCostDto> yearlyMigrationCostList = new ArrayList<>();
		List<HostingMigrationCostInfo> migrationCostList = new ArrayList<>();
		String migrationCostApplicable = totalsDto.getDealInfoDto().getMigrationCost();
		if(migrationCostApplicable != null && migrationCostApplicable.equalsIgnoreCase("YES") ? true : false){
			migrationCostList = hostingMigrationCostRepository.findAll();
		}
		if (hostingInfo != null) {
			BigDecimal serversRev = new BigDecimal(0);
			BigDecimal sqlInstancesRev = new BigDecimal(0);
			BigDecimal cotsInstallationsRev = new BigDecimal(0);
			BigDecimal totalRevenue = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
			for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
				if(migrationCostApplicable != null && migrationCostApplicable.equalsIgnoreCase("YES") ? true : false){
					migrationCost = calculateMigrationCostForHosting(totalsDto, yearlyDataInfo, migrationCostList);
					MigrationCostDto costDto = new MigrationCostDto();
					costDto.setYear(yearlyDataInfo.getYear());
					costDto.setCost(migrationCost);
					yearlyMigrationCostList.add(costDto);
				}
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getHostingRevenueInfoList())) {
					for (HostingRevenueInfo revenueInfo : yearlyDataInfo.getHostingRevenueInfoList()) {
						HostingRevenueInfoDto revenueInfoDto = new HostingRevenueInfoDto();
						serversRev = new BigDecimal(revenueInfo.getServers()!=null?revenueInfo.getServers():0);
						sqlInstancesRev = new BigDecimal(revenueInfo.getSqlInstances()!=null?revenueInfo.getSqlInstances():0);
						cotsInstallationsRev = new BigDecimal(revenueInfo.getCotsInstallations()!=null?revenueInfo.getCotsInstallations():0);
						totalRevenue = serversRev.add(sqlInstancesRev).add(cotsInstallationsRev).divide(thousand, 2, RoundingMode.HALF_UP);
						revenueInfoDto.setTotalRevenue(totalRevenue);
						revenueInfoDto.setServers(serversRev.intValue());
						revenueInfoDto.setSqlInstances(sqlInstancesRev.intValue());
						revenueInfoDto.setCotsInstallations(cotsInstallationsRev.intValue());
						revenueInfoDto.setYear(yearlyDataInfo.getYear());

						revenueInfoDtoList.add(revenueInfoDto);
					}
				}
			}
		}

		totalsDto.setHostingYearlyRevenue(revenueInfoDtoList);
		totalsDto.setHostingMigrationCost(yearlyMigrationCostList);
	}

	/**
	 * @param totalsDto
	 * @param yearlyDataInfo
	 * @return
	 */
	private BigDecimal calculateMigrationCostForEndUser(TotalsDto totalsDto, EndUserYearlyDataInfo yearlyDataInfo,
			List<EndUserMigrationCostInfo> migrationCostList) {
		List<EndUserMigrationCostInfoDto> costDtoList = new ArrayList<>();
		BigDecimal endUserDevices = new BigDecimal(yearlyDataInfo.getEndUserDevices() !=null ? yearlyDataInfo.getEndUserDevices() :0);
		BigDecimal migrationCost = new BigDecimal(0);
		Collections.sort(migrationCostList);
		costDtoList = endUserBeanConvertor.convertMigrationCostInfoToDto(migrationCostList);
		for(int i=0; i< costDtoList.size() && endUserDevices.compareTo(BigDecimal.ZERO)> 0; i++){
			if(endUserDevices.compareTo(costDtoList.get(i).getDifference()) > 0){
				migrationCost = migrationCost.add(costDtoList.get(i).getDifference().multiply(costDtoList.get(i).getCost()));
				endUserDevices = endUserDevices.subtract(costDtoList.get(i).getDifference());
			}else{
				migrationCost = migrationCost.add(endUserDevices.multiply(costDtoList.get(i).getCost()));
				break;
			}
		}
		return migrationCost.compareTo(BigDecimal.ZERO)==0 ? migrationCost : migrationCost.divide(thousand, 2, RoundingMode.HALF_UP);
	}

	private BigDecimal calculateMigrationCostForHosting(TotalsDto totalsDto, HostingYearlyDataInfo yearlyDataInfo,
			List<HostingMigrationCostInfo> migrationCostList) {
		List<HostingMigrationCostInfoDto> costDtoList = new ArrayList<>();
		BigDecimal servers = new BigDecimal(yearlyDataInfo.getServers() !=null ? yearlyDataInfo.getServers() :0);
		BigDecimal migrationCost = new BigDecimal(0);
		Collections.sort(migrationCostList);
		costDtoList = hostingBeanConverter.convertMigrationCostInfoToDto(migrationCostList);
		for(int i=0; i< costDtoList.size() && servers.compareTo(BigDecimal.ZERO)> 0; i++){
			if(servers.compareTo(costDtoList.get(i).getDifference()) > 0){
				migrationCost = migrationCost.add(costDtoList.get(i).getDifference().multiply(costDtoList.get(i).getCost()));
				servers = servers.subtract(costDtoList.get(i).getDifference());
			}else{
				migrationCost = migrationCost.add(servers.multiply(costDtoList.get(i).getCost()));
				break;
			}
		}
		return migrationCost.compareTo(BigDecimal.ZERO)==0 ? migrationCost : migrationCost.divide(thousand, 2, RoundingMode.HALF_UP);
	}

}
