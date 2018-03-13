package com.in.fujitsu.pricing.network.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.network.dto.NetworkInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkLanFactorInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkLanSolutionInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkPriceDto;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkUnitPriceInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkWanFactorInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkWanSolutionInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkWlanAccessPointFactorInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkWlanControllerFactorInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkYearlyDataInfoDto;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkLanFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkLanSolutionInfo;
import com.in.fujitsu.pricing.network.entity.NetworkRevenueInfo;
import com.in.fujitsu.pricing.network.entity.NetworkUnitPriceInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWanFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWanSolutionInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWlanAccessPointFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWlanControllerFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;
import com.in.fujitsu.pricing.network.repository.NetworkYearlyRepository;

@Component
public class NetworkBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private NetworkYearlyRepository networkYearlyRepository;

	/**
	 * @param solutions
	 * @return wanSolution list
	 */
	public List<NetworkWanSolutionInfoDto> prepareWanSolutionsDtoList(List<NetworkWanSolutionInfo> solutions) {
		final List<NetworkWanSolutionInfoDto> solutionsDtoList = new ArrayList<>();
		for (NetworkWanSolutionInfo solutionsInfo : solutions) {
			final NetworkWanSolutionInfoDto solutionsDto = modelMapper.map(solutionsInfo,
					NetworkWanSolutionInfoDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param solutions
	 * @return lanSolution list
	 */
	public List<NetworkLanSolutionInfoDto> prepareLanSolutionsDtoList(List<NetworkLanSolutionInfo> solutions) {
		final List<NetworkLanSolutionInfoDto> solutionsDtoList = new ArrayList<>();
		for (NetworkLanSolutionInfo solutionsInfo : solutions) {
			final NetworkLanSolutionInfoDto solutionsDto = modelMapper.map(solutionsInfo,
					NetworkLanSolutionInfoDto.class);
			solutionsDtoList.add(solutionsDto);
		}
		return solutionsDtoList;
	}

	/**
	 * @param wan
	 *            factors info
	 * @return wan factors
	 */
	public List<NetworkWanFactorInfoDto> prepareWanDtoList(List<NetworkWanFactorInfo> wanFactorInfoList) {
		final List<NetworkWanFactorInfoDto> wanDtoList = new ArrayList<>();
		for (NetworkWanFactorInfo wanFactorInfo : wanFactorInfoList) {
			final NetworkWanFactorInfoDto wanFactorDto = modelMapper.map(wanFactorInfo, NetworkWanFactorInfoDto.class);
			wanDtoList.add(wanFactorDto);
		}
		return wanDtoList;
	}

	/**
	 * @param lan
	 *            factors info
	 * @return lan factors
	 */
	public List<NetworkLanFactorInfoDto> prepareLanDtoList(List<NetworkLanFactorInfo> lanFactorInfoList) {
		final List<NetworkLanFactorInfoDto> lanDtoList = new ArrayList<>();
		for (NetworkLanFactorInfo lanFactorInfo : lanFactorInfoList) {
			final NetworkLanFactorInfoDto lanFactorDto = modelMapper.map(lanFactorInfo, NetworkLanFactorInfoDto.class);
			lanDtoList.add(lanFactorDto);
		}
		return lanDtoList;
	}

	/**
	 * @param wlan
	 *            controller factors info
	 * @return wlan controller factors
	 */
	public List<NetworkWlanControllerFactorInfoDto> prepareWlanControllerDtoList(
			List<NetworkWlanControllerFactorInfo> wlanControllerFactorInfoList) {
		final List<NetworkWlanControllerFactorInfoDto> wlanControllerDtoList = new ArrayList<>();
		for (NetworkWlanControllerFactorInfo wlanControllerFactorInfo : wlanControllerFactorInfoList) {
			final NetworkWlanControllerFactorInfoDto wlanControllerFactorDto = modelMapper.map(wlanControllerFactorInfo,
					NetworkWlanControllerFactorInfoDto.class);
			wlanControllerDtoList.add(wlanControllerFactorDto);
		}
		return wlanControllerDtoList;
	}

	/**
	 * @param wlan
	 *            accessPoint factors info
	 * @return wlan accessPoint factors
	 */
	public List<NetworkWlanAccessPointFactorInfoDto> prepareAccessPointDtoList(
			List<NetworkWlanAccessPointFactorInfo> wlanAccessPointFactorInfoList) {
		final List<NetworkWlanAccessPointFactorInfoDto> wlanAccessPointDtoList = new ArrayList<>();
		for (NetworkWlanAccessPointFactorInfo wlanAccessPointFactorInfo : wlanAccessPointFactorInfoList) {
			final NetworkWlanAccessPointFactorInfoDto wlanAccessPointFactorDto = modelMapper
					.map(wlanAccessPointFactorInfo, NetworkWlanAccessPointFactorInfoDto.class);
			wlanAccessPointDtoList.add(wlanAccessPointFactorDto);
		}
		return wlanAccessPointDtoList;
	}

	/**
	 * @param networkInfo
	 * @param networkInfoDto
	 * @param isSave
	 * @return
	 */
	public NetworkInfo prepareNetworkInfo(NetworkInfo networkInfo, NetworkInfoDto networkInfoDto, boolean isSave) {
		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(networkInfoDto.getDealId());
			networkInfo.setDealInfo(dealInfo);
		}

		networkInfo.setLevelOfService(networkInfoDto.getLevelOfService());
		networkInfo.setOffshoreAllowed(networkInfoDto.isOffshoreAllowed());
		networkInfo.setTowerArchitect(networkInfoDto.getTowerArchitect() !=null ?  networkInfoDto.getTowerArchitect() : networkInfo.getTowerArchitect());
		networkInfo.setLevelIndicator(networkInfoDto.getLevelIndicator());
		networkInfo.setIncludeHardware(networkInfoDto.isIncludeHardware());
		networkInfo.setSelectedLanSolutionId(networkInfoDto.getSelectedLanSolutionId());
		networkInfo.setSelectedWanSolutionId(networkInfoDto.getSelectedWanSolutionId());

		List<NetworkYearlyDataInfo> networkYearlyDataInfoList = new ArrayList<>();

		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(networkInfoDto.getNetworkYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = networkInfoDto.getNetworkYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				NetworkYearlyDataInfoDto networkYearlyDataInfoDto = networkInfoDto.getNetworkYearlyDataInfoDtoList().get(i);
				NetworkYearlyDataInfo networkYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(networkInfo.getNetworkYearlyDataInfoList())
						&& networkInfo.getNetworkYearlyDataInfoList().size() - 1 >= i)) {
					for (int j = 0; j < networkInfo.getNetworkYearlyDataInfoList().size(); j++) {
						NetworkYearlyDataInfo existingNetworkYearlyDataInfo = networkInfo.getNetworkYearlyDataInfoList().get(j);
						if (existingNetworkYearlyDataInfo.getYear().equals(networkYearlyDataInfoDto.getYear())) {
							networkYearlyDataInfo = existingNetworkYearlyDataInfo;
							break;
						}
					}
				} else {
					networkYearlyDataInfo = new NetworkYearlyDataInfo();
				}
				networkYearlyDataInfoList = setNetworkYearlyDetails(networkYearlyDataInfoList, networkInfo,
						networkYearlyDataInfo, networkYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(networkInfo.getNetworkYearlyDataInfoList())
					&& yearlyDtoListSize < networkInfo.getNetworkYearlyDataInfoList().size()) {
				for (int i = networkInfo.getNetworkYearlyDataInfoList().size() - 1; i >= yearlyDtoListSize; i--) {
					networkYearlyRepository.delete(networkInfo.getNetworkYearlyDataInfoList().get(i).getYearId());
				}
			}
		}

		networkInfo.setNetworkYearlyDataInfoList(networkYearlyDataInfoList);

		return networkInfo;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @param networkInfo
	 * @param networkYearlyDataInfo
	 * @param networkYearlyDataInfoDto
	 * @return
	 */
	private List<NetworkYearlyDataInfo> setNetworkYearlyDetails(List<NetworkYearlyDataInfo> networkYearlyDataInfoList,
			NetworkInfo networkInfo, NetworkYearlyDataInfo networkYearlyDataInfo,
			NetworkYearlyDataInfoDto networkYearlyDataInfoDto) {

		networkYearlyDataInfo.setYear(networkYearlyDataInfoDto.getYear());
		networkYearlyDataInfo.setWanDevices(
				networkYearlyDataInfoDto.getWanDevices() == null ? 0 : networkYearlyDataInfoDto.getWanDevices());
		networkYearlyDataInfo.setSmallWanDevices(networkYearlyDataInfoDto.getSmallWanDevices() == null ? 0
				: networkYearlyDataInfoDto.getSmallWanDevices());
		networkYearlyDataInfo.setMediumWanDevices(networkYearlyDataInfoDto.getMediumWanDevices() == null ? 0
				: networkYearlyDataInfoDto.getMediumWanDevices());
		networkYearlyDataInfo.setLargeWanDevices(networkYearlyDataInfoDto.getLargeWanDevices() == null ? 0
				: networkYearlyDataInfoDto.getLargeWanDevices());
		networkYearlyDataInfo.setLanDevices(
				networkYearlyDataInfoDto.getLanDevices() == null ? 0 : networkYearlyDataInfoDto.getLanDevices());
		networkYearlyDataInfo.setSmallLanDevices(networkYearlyDataInfoDto.getSmallLanDevices() == null ? 0
				: networkYearlyDataInfoDto.getSmallLanDevices());
		networkYearlyDataInfo.setMediumLanDevices(networkYearlyDataInfoDto.getMediumLanDevices() == null ? 0
				: networkYearlyDataInfoDto.getMediumLanDevices());
		networkYearlyDataInfo.setLargeLanDevices(networkYearlyDataInfoDto.getLargeLanDevices() == null ? 0
				: networkYearlyDataInfoDto.getLargeLanDevices());
		networkYearlyDataInfo.setWlanControllers(networkYearlyDataInfoDto.getWlanControllers() == null ? 0
				: networkYearlyDataInfoDto.getWlanControllers());
		networkYearlyDataInfo.setWlanAccesspoint(networkYearlyDataInfoDto.getWlanAccesspoint() == null ? 0
				: networkYearlyDataInfoDto.getWlanAccesspoint());
		networkYearlyDataInfo.setLoadBalancers(
				networkYearlyDataInfoDto.getLoadBalancers() == null ? 0 : networkYearlyDataInfoDto.getLoadBalancers());
		networkYearlyDataInfo.setDnsDhcpService(networkYearlyDataInfoDto.getDnsDhcpService() == null ? 0
				: networkYearlyDataInfoDto.getDnsDhcpService());
		networkYearlyDataInfo.setVpnIpSec(
				networkYearlyDataInfoDto.getVpnIpSec() == null ? 0 : networkYearlyDataInfoDto.getVpnIpSec());
		networkYearlyDataInfo.setFirewalls(
				networkYearlyDataInfoDto.getFirewalls() == null ? 0 : networkYearlyDataInfoDto.getFirewalls());
		networkYearlyDataInfo
				.setProxies(networkYearlyDataInfoDto.getProxies() == null ? 0 : networkYearlyDataInfoDto.getProxies());

		networkYearlyDataInfo.setNetworkInfo(networkInfo);

		// set the Unit Price
		List<NetworkUnitPriceInfo> networkUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoDto.getNetworkUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
				for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo.getNetworkUnitPriceInfoList()) {
					NetworkUnitPriceInfoDto networkUnitPriceInfoDto = networkYearlyDataInfoDto
							.getNetworkUnitPriceInfoDtoList().get(0);
					if (networkUnitPriceInfoDto != null) {
						setUnitPrices(networkUnitPriceInfoDto, networkUnitPriceInfo);
						networkUnitPriceInfoList.add(networkUnitPriceInfo);
					}
				}
			} else {
				for (NetworkUnitPriceInfoDto networkUnitPriceInfoDto : networkYearlyDataInfoDto
						.getNetworkUnitPriceInfoDtoList()) {
					NetworkUnitPriceInfo networkUnitPriceInfo = new NetworkUnitPriceInfo();
					setUnitPrices(networkUnitPriceInfoDto, networkUnitPriceInfo);
					
					networkUnitPriceInfo.setNetworkYearlyDataInfo(networkYearlyDataInfo);
					networkUnitPriceInfoList.add(networkUnitPriceInfo);
				}
			}
			networkYearlyDataInfo.setNetworkUnitPriceInfoList(networkUnitPriceInfoList);
		}

		// set the Revenue
		List<NetworkRevenueInfo> networkRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoDto.getNetworkRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkRevenueInfoList())) {
				for (NetworkRevenueInfo networkRevenueInfo : networkYearlyDataInfo.getNetworkRevenueInfoList()) {
					NetworkRevenueInfoDto networkRevenueInfoDto = networkYearlyDataInfoDto
							.getNetworkRevenueInfoDtoList().get(0);
					setRevenues(networkRevenueInfoDto, networkRevenueInfo);
					networkRevenueInfoList.add(networkRevenueInfo);
				}
			} else {
				for (NetworkRevenueInfoDto networkRevenueInfoDto : networkYearlyDataInfoDto
						.getNetworkRevenueInfoDtoList()) {
					NetworkRevenueInfo networkRevenueInfo = new NetworkRevenueInfo();
					setRevenues(networkRevenueInfoDto, networkRevenueInfo);
					
					networkRevenueInfo.setNetworkYearlyDataInfo(networkYearlyDataInfo);
					networkRevenueInfoList.add(networkRevenueInfo);
				}
			}
			networkYearlyDataInfo.setNetworkRevenueInfoList(networkRevenueInfoList);
		}

		networkYearlyDataInfoList.add(networkYearlyDataInfo);

		return networkYearlyDataInfoList;
	}

	private void setRevenues(NetworkRevenueInfoDto networkRevenueInfoDto, NetworkRevenueInfo networkRevenueInfo) {
		networkRevenueInfo.setTotalWanRevenue(networkRevenueInfoDto.getTotalWanRevenue() == null ? 0
				: networkRevenueInfoDto.getTotalWanRevenue());
		networkRevenueInfo.setTotalLanRevenue(networkRevenueInfoDto.getTotalLanRevenue() == null ? 0
				: networkRevenueInfoDto.getTotalLanRevenue());
		networkRevenueInfo.setTotalWlanControllersRevenue(
				networkRevenueInfoDto.getTotalWlanControllersRevenue() == null ? 0
						: networkRevenueInfoDto.getTotalWlanControllersRevenue());
		networkRevenueInfo.setTotalWlanAccesspointRevenue(
				networkRevenueInfoDto.getTotalWlanAccesspointRevenue() == null ? 0
						: networkRevenueInfoDto.getTotalWlanAccesspointRevenue());
		networkRevenueInfo
				.setTotalLoadBalancersRevenue(networkRevenueInfoDto.getTotalLoadBalancersRevenue() == null
						? 0 : networkRevenueInfoDto.getTotalLoadBalancersRevenue());
		networkRevenueInfo.setTotalVpnIpSecRevenue(networkRevenueInfoDto.getTotalVpnIpSecRevenue() == null
				? 0 : networkRevenueInfoDto.getTotalVpnIpSecRevenue());
		networkRevenueInfo
				.setTotalDnsDhcpServiceRevenue(networkRevenueInfoDto.getTotalDnsDhcpServiceRevenue() == null
						? 0 : networkRevenueInfoDto.getTotalDnsDhcpServiceRevenue());
		networkRevenueInfo.setTotalFirewallsRevenue(networkRevenueInfoDto.getTotalFirewallsRevenue() == null
				? 0 : networkRevenueInfoDto.getTotalFirewallsRevenue());
		networkRevenueInfo.setTotalProxiesRevenue(networkRevenueInfoDto.getTotalProxiesRevenue() == null ? 0
				: networkRevenueInfoDto.getTotalProxiesRevenue());
		networkRevenueInfo.setBenchMarkType(networkRevenueInfoDto.getBenchMarkType());
	}

	private void setUnitPrices(NetworkUnitPriceInfoDto networkUnitPriceInfoDto,
			NetworkUnitPriceInfo networkUnitPriceInfo) {
		networkUnitPriceInfo.setBenchMarkType(networkUnitPriceInfoDto.getBenchMarkType());
		networkUnitPriceInfo.setWanDevices(networkUnitPriceInfoDto.getWanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getWanDevices());
		networkUnitPriceInfo.setSmallWanDevices(networkUnitPriceInfoDto.getSmallWanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getSmallWanDevices());
		networkUnitPriceInfo.setMediumWanDevices(networkUnitPriceInfoDto.getMediumWanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getMediumWanDevices());
		networkUnitPriceInfo.setLargeWanDevices(networkUnitPriceInfoDto.getLargeWanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getLargeWanDevices());
		networkUnitPriceInfo.setLanDevices(networkUnitPriceInfoDto.getLanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getLanDevices());
		networkUnitPriceInfo.setSmallLanDevices(networkUnitPriceInfoDto.getSmallLanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getSmallLanDevices());
		networkUnitPriceInfo.setMediumLanDevices(networkUnitPriceInfoDto.getMediumLanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getMediumLanDevices());
		networkUnitPriceInfo.setLargeLanDevices(networkUnitPriceInfoDto.getLargeLanDevices()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getLargeLanDevices());
		networkUnitPriceInfo.setWlanControllers(networkUnitPriceInfoDto.getWlanControllers()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getWlanControllers());
		networkUnitPriceInfo.setWlanAccesspoint(networkUnitPriceInfoDto.getWlanAccesspoint()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getWlanAccesspoint());
		networkUnitPriceInfo.setLoadBalancers(networkUnitPriceInfoDto.getLoadBalancers()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getLoadBalancers());
		networkUnitPriceInfo.setDnsDhcpService(networkUnitPriceInfoDto.getDnsDhcpService()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getDnsDhcpService());
		networkUnitPriceInfo.setVpnIpSec(networkUnitPriceInfoDto.getVpnIpSec()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getVpnIpSec());
		networkUnitPriceInfo.setFirewalls(networkUnitPriceInfoDto.getFirewalls()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getFirewalls());
		networkUnitPriceInfo.setProxies(networkUnitPriceInfoDto.getProxies()== null ? BigDecimal.ZERO
				:networkUnitPriceInfoDto.getProxies());
	}

	/**
	 * @param networkInfo
	 * @return
	 */
	public NetworkInfoDto prepareNetworkInfoDto(NetworkInfo networkInfo) {
		NetworkInfoDto networkInfoDto = new NetworkInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		if (networkInfo != null) {
			networkInfoDto = modelMapper.map(networkInfo, NetworkInfoDto.class);
			networkInfoDto.setDealId(networkInfo.getDealInfo().getDealId());

			final List<NetworkYearlyDataInfoDto> networkYearlyDataInfoDtoList = new ArrayList<>();
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
				final NetworkYearlyDataInfoDto networkYearlyDataInfoDto = modelMapper.map(networkYearlyDataInfo,
						NetworkYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
					final List<NetworkUnitPriceInfoDto> networkUnitPriceInfoDtoList = new ArrayList<>();
					for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo
							.getNetworkUnitPriceInfoList()) {
						final NetworkUnitPriceInfoDto networkUnitPriceInfoDto = modelMapper.map(networkUnitPriceInfo,
								NetworkUnitPriceInfoDto.class);
						networkUnitPriceInfoDtoList.add(networkUnitPriceInfoDto);
					}
					networkYearlyDataInfoDto.setNetworkUnitPriceInfoDtoList(networkUnitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkRevenueInfoList())) {
					final List<NetworkRevenueInfoDto> networkRevenueInfoDtoList = new ArrayList<>();
					for (NetworkRevenueInfo networkRevenueInfo : networkYearlyDataInfo.getNetworkRevenueInfoList()) {
						final NetworkRevenueInfoDto networkRevenueInfoDto = modelMapper.map(networkRevenueInfo,
								NetworkRevenueInfoDto.class);
						networkRevenueInfoDtoList.add(networkRevenueInfoDto);
					}
					networkYearlyDataInfoDto.setNetworkRevenueInfoDtoList(networkRevenueInfoDtoList);
				}
				networkYearlyDataInfoDtoList.add(networkYearlyDataInfoDto);
			}

			networkInfoDto.setNetworkYearlyDataInfoDtoList(networkYearlyDataInfoDtoList);
		}

		return networkInfoDto;
	}


	/**
	 * @param networkInfo
	 * @param networkPriceDtoList
	 * @return
	 */
	public NetworkInfo prepareNetworkPrice(NetworkInfo networkInfo, List<NetworkPriceDto> networkPriceDtoList) {
		List<NetworkYearlyDataInfo> networkYearlyDataInfoList = new ArrayList<>();
		NetworkPriceDto networkPriceDto = new NetworkPriceDto();
		for (NetworkYearlyDataInfo yearlydataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			for (NetworkPriceDto priceDto : networkPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					networkPriceDto = priceDto;
					break;
				}
			}
			networkYearlyDataInfoList = updateNetworkYearlyDetails(networkYearlyDataInfoList,
					yearlydataInfo, networkPriceDto);
		}

		networkInfo.setNetworkYearlyDataInfoList(networkYearlyDataInfoList);

		return networkInfo;
	}


	/**
	 * @param networkYearlyDataInfoList
	 * @param yearlydataInfo
	 * @param networkPriceDto
	 * @return
	 */
	private List<NetworkYearlyDataInfo> updateNetworkYearlyDetails(
			List<NetworkYearlyDataInfo> networkYearlyDataInfoList, NetworkYearlyDataInfo yearlydataInfo,
			NetworkPriceDto networkPriceDto) {

		if (!CollectionUtils.isEmpty(yearlydataInfo.getNetworkUnitPriceInfoList())) {
			// Update the existing unit prices
			for (NetworkUnitPriceInfo networkUnitPriceInfo : yearlydataInfo.getNetworkUnitPriceInfoList()) {
				networkUnitPriceInfo.setWanDevices(networkPriceDto.getTotalWanUnitPrice());
				networkUnitPriceInfo.setSmallWanDevices(networkPriceDto.getTotalSmallWanUnitPrice());
				networkUnitPriceInfo.setMediumWanDevices(networkPriceDto.getTotalMediumWanUnitPrice());
				networkUnitPriceInfo.setLargeWanDevices(networkPriceDto.getTotalLargeWanUnitPrice());
				networkUnitPriceInfo.setLanDevices(networkPriceDto.getTotalLanUnitPrice());
				networkUnitPriceInfo.setSmallLanDevices(networkPriceDto.getTotalSmallLanUnitPrice());
				networkUnitPriceInfo.setMediumLanDevices(networkPriceDto.getTotalMediumLanUnitPrice());
				networkUnitPriceInfo.setLargeLanDevices(networkPriceDto.getTotalLargeLanUnitPrice());
				networkUnitPriceInfo.setWlanControllers(networkPriceDto.getTotalWlanControllersUnitPrice());
				networkUnitPriceInfo.setWlanAccesspoint(networkPriceDto.getTotalWlanAccesspointUnitPrice());
				networkUnitPriceInfo.setLoadBalancers(networkPriceDto.getTotalLoadBalancersUnitPrice());
				networkUnitPriceInfo.setDnsDhcpService(networkPriceDto.getTotalDnsDhcpServiceUnitPrice());
				networkUnitPriceInfo.setVpnIpSec(networkPriceDto.getTotalVpnIpSecUnitPrice());
				networkUnitPriceInfo.setFirewalls(networkPriceDto.getTotalFirewallsUnitPrice());
				networkUnitPriceInfo.setProxies(networkPriceDto.getTotalProxiesUnitPrice());
			}
		} else {
			// Case when first time assessment is done
			List<NetworkUnitPriceInfo> networkUnitPriceInfoList = new ArrayList<>();
			NetworkUnitPriceInfo networkUnitPriceInfo = new NetworkUnitPriceInfo();
			networkUnitPriceInfo.setWanDevices(networkPriceDto.getTotalWanUnitPrice());
			networkUnitPriceInfo.setSmallWanDevices(networkPriceDto.getTotalSmallWanUnitPrice());
			networkUnitPriceInfo.setMediumWanDevices(networkPriceDto.getTotalMediumWanUnitPrice());
			networkUnitPriceInfo.setLargeWanDevices(networkPriceDto.getTotalLargeWanUnitPrice());
			networkUnitPriceInfo.setLanDevices(networkPriceDto.getTotalLanUnitPrice());
			networkUnitPriceInfo.setSmallLanDevices(networkPriceDto.getTotalSmallLanUnitPrice());
			networkUnitPriceInfo.setMediumLanDevices(networkPriceDto.getTotalMediumLanUnitPrice());
			networkUnitPriceInfo.setLargeLanDevices(networkPriceDto.getTotalLargeLanUnitPrice());
			networkUnitPriceInfo.setWlanControllers(networkPriceDto.getTotalWlanControllersUnitPrice());
			networkUnitPriceInfo.setWlanAccesspoint(networkPriceDto.getTotalWlanAccesspointUnitPrice());
			networkUnitPriceInfo.setLoadBalancers(networkPriceDto.getTotalLoadBalancersUnitPrice());
			networkUnitPriceInfo.setDnsDhcpService(networkPriceDto.getTotalDnsDhcpServiceUnitPrice());
			networkUnitPriceInfo.setVpnIpSec(networkPriceDto.getTotalVpnIpSecUnitPrice());
			networkUnitPriceInfo.setFirewalls(networkPriceDto.getTotalFirewallsUnitPrice());
			networkUnitPriceInfo.setProxies(networkPriceDto.getTotalProxiesUnitPrice());
			networkUnitPriceInfo.setNetworkYearlyDataInfo(yearlydataInfo);

			networkUnitPriceInfoList.add(networkUnitPriceInfo);
			yearlydataInfo.setNetworkUnitPriceInfoList(networkUnitPriceInfoList);
		}

		if (!CollectionUtils.isEmpty(yearlydataInfo.getNetworkRevenueInfoList())) {
			// Update the existing revenues
			for (NetworkRevenueInfo networkRevenueInfo : yearlydataInfo.getNetworkRevenueInfoList()) {
				networkRevenueInfo.setTotalWanRevenue(networkPriceDto.getTotalWanRevenue());
				networkRevenueInfo.setTotalLanRevenue(networkPriceDto.getTotalLanRevenue());
				networkRevenueInfo.setTotalWlanControllersRevenue(networkPriceDto.getTotalWlanControllersRevenue());
				networkRevenueInfo.setTotalWlanAccesspointRevenue(networkPriceDto.getTotalWlanAccesspointRevenue());
				networkRevenueInfo.setTotalLoadBalancersRevenue(networkPriceDto.getTotalLoadBalancersRevenue());
				networkRevenueInfo.setTotalDnsDhcpServiceRevenue(networkPriceDto.getTotalDnsDhcpServiceRevenue());
				networkRevenueInfo.setTotalVpnIpSecRevenue(networkPriceDto.getTotalVpnIpSecRevenue());
				networkRevenueInfo.setTotalFirewallsRevenue(networkPriceDto.getTotalFirewallsRevenue());
				networkRevenueInfo.setTotalProxiesRevenue(networkPriceDto.getTotalProxiesRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<NetworkRevenueInfo> networkRevenueInfoList = new ArrayList<>();
			NetworkRevenueInfo networkRevenueInfo = new NetworkRevenueInfo();
			networkRevenueInfo.setTotalWanRevenue(networkPriceDto.getTotalWanRevenue());
			networkRevenueInfo.setTotalLanRevenue(networkPriceDto.getTotalLanRevenue());
			networkRevenueInfo.setTotalWlanControllersRevenue(networkPriceDto.getTotalWlanControllersRevenue());
			networkRevenueInfo.setTotalWlanAccesspointRevenue(networkPriceDto.getTotalWlanAccesspointRevenue());
			networkRevenueInfo.setTotalLoadBalancersRevenue(networkPriceDto.getTotalLoadBalancersRevenue());
			networkRevenueInfo.setTotalDnsDhcpServiceRevenue(networkPriceDto.getTotalDnsDhcpServiceRevenue());
			networkRevenueInfo.setTotalVpnIpSecRevenue(networkPriceDto.getTotalVpnIpSecRevenue());
			networkRevenueInfo.setTotalFirewallsRevenue(networkPriceDto.getTotalFirewallsRevenue());
			networkRevenueInfo.setTotalProxiesRevenue(networkPriceDto.getTotalProxiesRevenue());
			networkRevenueInfo.setNetworkYearlyDataInfo(yearlydataInfo);

			networkRevenueInfoList.add(networkRevenueInfo);
			yearlydataInfo.setNetworkRevenueInfoList(networkRevenueInfoList);
		}

		networkYearlyDataInfoList.add(yearlydataInfo);

		return networkYearlyDataInfoList;
	}

	/**
	 * @param networkInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public NetworkInfo prepareSolutionCriteria(NetworkInfo networkInfo, SolutionCriteriaDto solutionCriteriaDto) {
		networkInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		networkInfo.setLevelOfService(solutionCriteriaDto.getLevelOfService());
		networkInfo.setIncludeHardware(solutionCriteriaDto.isIncludeHardware());
		return networkInfo;

	}

}
