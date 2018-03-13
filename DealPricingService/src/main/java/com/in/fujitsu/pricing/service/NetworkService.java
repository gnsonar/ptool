package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.dto.VolumeDto;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.network.calculator.DnsDhcpCalculator;
import com.in.fujitsu.pricing.network.calculator.FirewallsCalculator;
import com.in.fujitsu.pricing.network.calculator.LanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.LargeLanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.LargeWanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.LoadBalancersCalculator;
import com.in.fujitsu.pricing.network.calculator.MediumLanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.MediumWanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.ProxiesCalculator;
import com.in.fujitsu.pricing.network.calculator.SmallLanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.SmallWanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.VpnIpSecCalculator;
import com.in.fujitsu.pricing.network.calculator.WanDevicesCalculator;
import com.in.fujitsu.pricing.network.calculator.WlanAccessPointsCalculator;
import com.in.fujitsu.pricing.network.calculator.WlanControllerCalculator;
import com.in.fujitsu.pricing.network.dto.NetworkCalculateDto;
import com.in.fujitsu.pricing.network.dto.NetworkDropdownDto;
import com.in.fujitsu.pricing.network.dto.NetworkInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkPriceDto;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueDto;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkLanFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkLanSolutionInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWanFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWanSolutionInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWlanAccessPointFactorInfo;
import com.in.fujitsu.pricing.network.entity.NetworkWlanControllerFactorInfo;
import com.in.fujitsu.pricing.network.helper.DnsDhcpDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.FirewallsDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.LanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.LargeLanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.LargeWanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.LoadBalancersDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.MediumLanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.MediumWanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.NetworkBeanConvertor;
import com.in.fujitsu.pricing.network.helper.ProxiesDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.SmallLanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.SmallWanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.VpnIPSecDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.WanDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.WlanAccessPointsDealResultsHelper;
import com.in.fujitsu.pricing.network.helper.WlanControllerDealResultsHelper;
import com.in.fujitsu.pricing.network.repository.NetworkLanFactorRepository;
import com.in.fujitsu.pricing.network.repository.NetworkLanSolutionsRepository;
import com.in.fujitsu.pricing.network.repository.NetworkRepository;
import com.in.fujitsu.pricing.network.repository.NetworkWanFactorRepository;
import com.in.fujitsu.pricing.network.repository.NetworkWanSolutionsRepository;
import com.in.fujitsu.pricing.network.repository.NetworkWlanAccessPointFactorRepository;
import com.in.fujitsu.pricing.network.repository.NetworkWlanControllerFactorRepository;
import com.in.fujitsu.pricing.repository.CountryFactorRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.TowerSpecificBandRepository;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NetworkService {
	@Autowired
	private NetworkRepository networkRepository;

	@Autowired
	NetworkWanSolutionsRepository wanSolutionRepository;

	@Autowired
	NetworkLanSolutionsRepository lanSolutionRepository;

	@Autowired
	private NetworkWanFactorRepository networkWanFactorRepository;

	@Autowired
	private NetworkLanFactorRepository networkLanFactorRepository;

	@Autowired
	private NetworkWlanControllerFactorRepository networkWlanControllerFactorRepository;

	@Autowired
	private NetworkWlanAccessPointFactorRepository networkWlanAccessPointFactorRepository;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private GenericService genericService;

	@Autowired
	private NetworkBeanConvertor beanConvertor;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private WanDevicesCalculator wanDevicesCalculator;

	@Autowired
	private SmallWanDevicesCalculator smallWanDevicesCalculator;

	@Autowired
	private MediumWanDevicesCalculator mediumWanDevicesCalculator;

	@Autowired
	private LargeWanDevicesCalculator largeWanDevicesCalculator;

	@Autowired
	private LanDevicesCalculator lanDevicesCalculator;

	@Autowired
	private SmallLanDevicesCalculator smallLanDevicesCalculator;

	@Autowired
	private MediumLanDevicesCalculator mediumLanDevicesCalculator;

	@Autowired
	private LargeLanDevicesCalculator largeLanDevicesCalculator;

	@Autowired
	private WlanControllerCalculator wlanControllerCalculator;

	@Autowired
	private WlanAccessPointsCalculator wlanAccessPointsCalculator;

	@Autowired
	private VpnIpSecCalculator vpnIpSecCalculator;

	@Autowired
	private LoadBalancersCalculator loadBalancersCalculator;

	@Autowired
	private DnsDhcpCalculator dnsDhcpCalculator;

	@Autowired
	private FirewallsCalculator firewallsCalculator;

	@Autowired
	private ProxiesCalculator proxiesCalculator;

	@Autowired
	private WanDealResultsHelper wanDealResultsHelper;

	@Autowired
	private SmallWanDealResultsHelper smallWanDealResultsHelper;

	@Autowired
	private MediumWanDealResultsHelper mediumWanDealResultsHelper;

	@Autowired
	private LargeWanDealResultsHelper largeWanDealResultsHelper;

	@Autowired
	private LanDealResultsHelper lanDealResultsHelper;

	@Autowired
	private SmallLanDealResultsHelper smallLanDealResultsHelper;

	@Autowired
	private MediumLanDealResultsHelper mediumLanDealResultsHelper;

	@Autowired
	private LargeLanDealResultsHelper largeLanDealResultsHelper;

	@Autowired
	private WlanControllerDealResultsHelper wlanControllerDealResultsHelper;

	@Autowired
	private WlanAccessPointsDealResultsHelper wlanAccessPointsDealResultsHelper;

	@Autowired
	private VpnIPSecDealResultsHelper vpnIPSecDealResultsHelper;

	@Autowired
	private DnsDhcpDealResultsHelper dnsDhcpDealResultsHelper;

	@Autowired
	private LoadBalancersDealResultsHelper loadBalancersDealResultsHelper;

	@Autowired
	private FirewallsDealResultsHelper firewallsDealResultsHelper;

	@Autowired
	private ProxiesDealResultsHelper proxiesDealResultsHelper;

	@Autowired
	private HostingRepository hostingRepository;

	private final String NETWORK_TOWER = "Network";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";
	private final String WAN_LEVEL = "1.1";
	private final String SMALL_WAN_LEVEL = "1.1.1";
	private final String MEDIUM_WAN_LEVEL = "1.1.2";
	private final String LARGE_WAN_LEVEL = "1.1.3";
	private final String LAN_LEVEL = "2.1";
	private final String SMALL_LAN_LEVEL = "2.1.1";
	private final String MEDIUM_LAN_LEVEL = "2.1.2";
	private final String LARGE_LAN_LEVEL = "2.1.3";

	private final String CONTROLLERS_LEVEL = "3.1";
	private final String ACCESS_POINTS_LEVEL = "4.1";
	private final String LOAD_BALANCERS_LEVEL = "5.1";
	private final String VPN_IPSEC_LEVEL = "6.1";
	private final String DNS_DHCP_LEVEL = "7.1";
	private final String FIREWALLS_LEVEL = "8.1";
	private final String PROXIES_LEVEL = "9.1";

	/**
	 * @param dealId
	 * @param networkInfoDto
	 * @return networkInfoDto
	 */
	@Transactional
	public NetworkInfoDto saveDetails(Long dealId, NetworkInfoDto networkInfoDto) {
		NetworkInfo networkInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		networkInfo = networkRepository.findByDealInfo(dealInfo);
		if (null != networkInfo) {
			networkInfo = beanConvertor.prepareNetworkInfo(networkInfo, networkInfoDto, false);
		} else {
			networkInfo = beanConvertor.prepareNetworkInfo(new NetworkInfo(), networkInfoDto, true);
		}
		networkInfo = networkRepository.saveAndFlush(networkInfo);
		return beanConvertor.prepareNetworkInfoDto(networkInfo);
	}

	/**
	 * @param dealId
	 * @return
	 * @throws Exception
	 */
	public NetworkInfoDto getDetails(long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
		if (networkInfo == null) {
			throw new ServiceException("DB doesn't have the Network Details for given dealId.");
		}
		return beanConvertor.prepareNetworkInfoDto(networkInfo);
	}

	/**
	 * @param dealId
	 * @return
	 */
	public NetworkDropdownDto getDropDownDetails(Long dealId) {
		NetworkDropdownDto networkDropdownDto = new NetworkDropdownDto();
		networkDropdownDto.setStandardWindowInfoList(genericService.getServicedWindowInfo());
		networkDropdownDto.setYesNoOptionList(genericService.getOffshreAndHardwareInfo());

		final List<NetworkWanSolutionInfo> wanSolutions = wanSolutionRepository.findAll();
		final List<NetworkLanSolutionInfo> lanSolutions = lanSolutionRepository.findAll();

		networkDropdownDto.setNetworkWanSolutionsDtoList(beanConvertor.prepareWanSolutionsDtoList(wanSolutions));
		networkDropdownDto.setNetworkLanSolutionsDtoList(beanConvertor.prepareLanSolutionsDtoList(lanSolutions));

		final List<NetworkWanFactorInfo> wanFactorInfoList = networkWanFactorRepository.findAll();
		final List<NetworkLanFactorInfo> lanFactorInfoList = networkLanFactorRepository.findAll();
		final List<NetworkWlanControllerFactorInfo> wlanControllerFactorInfoList = networkWlanControllerFactorRepository
				.findAll();
		final List<NetworkWlanAccessPointFactorInfo> wlanAccessPointFactorInfoList = networkWlanAccessPointFactorRepository
				.findAll();

		networkDropdownDto.setNetworkWanFactorDtoList(beanConvertor.prepareWanDtoList(wanFactorInfoList));
		networkDropdownDto.setNetworkLanFactorDtoList(beanConvertor.prepareLanDtoList(lanFactorInfoList));
		networkDropdownDto.setNetworkWlanControllerFactorDtoList(
				beanConvertor.prepareWlanControllerDtoList(wlanControllerFactorInfoList));
		networkDropdownDto.setNetworkWlanAccessPointFactorDtoList(
				beanConvertor.prepareAccessPointDtoList(wlanAccessPointFactorInfoList));

		networkDropdownDto.setPhysicalServerList(getPhysicalServersList(dealId));

		if (dealId != null) {
			networkDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return networkDropdownDto;
	}

	/**
	 * @param dealId
	 * @return
	 */
	private List<VolumeDto> getPhysicalServersList(Long dealId) {
		List<VolumeDto> physicalServerList = new ArrayList<>();
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		if(hostingInfo != null){
			if(!CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())){
				for(HostingYearlyDataInfo yearlyInfo : hostingInfo.getHostingYearlyDataInfoList()){
					VolumeDto dto = new VolumeDto();
					dto.setYear(yearlyInfo.getYear());
					dto.setVolume(yearlyInfo.getPhysical());
					physicalServerList.add(dto);
				}
			}
		}
		return physicalServerList;
	}

	/**
	 * @param networkPriceDtoList
	 * @param networkId
	 * @return
	 */
	public ResponseEntity<Object> updateNetworkPrice(List<NetworkPriceDto> networkPriceDtoList, Long networkId) throws ServiceException {
		NetworkInfo networkInfo = networkRepository.findOne(networkId);
		if (null != networkInfo) {
			networkInfo = beanConvertor.prepareNetworkPrice(networkInfo, networkPriceDtoList);
			networkRepository.saveAndFlush(networkInfo);
		} else {
			throw new ServiceException("No NetworkInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);
	}

	/**
	 * Method for updating the Solution Criteria
	 *
	 * @param networkPriceDtoList
	 * @param networkId
	 * @return
	 * @throws ServiceException
	 */
	public NetworkRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionDto, Long networkId)
			throws ServiceException {
		NetworkInfo networkInfo = networkRepository.findOne(networkId);
		if (null != networkInfo) {
			networkInfo = beanConvertor.prepareSolutionCriteria(networkInfo, solutionDto);
			networkRepository.saveAndFlush(networkInfo);
		} else {
			throw new ServiceException("No NetworkInfo data to update");
		}

		return getYearlyRevenues(networkInfo.getDealInfo().getDealId());
	}

	/**
	 * @param dealId
	 * @return
	 * @throws ServiceException
	 */
	public NetworkRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		NetworkRevenueDto networkRevenueDto = new NetworkRevenueDto();
		if (dealInfo != null) {
			NetworkCalculateDto wanDevicesCalculateDto = null;
			NetworkCalculateDto smallWanDevicesCalculateDto = null;
			NetworkCalculateDto mediumWanDevicesCalculateDto = null;
			NetworkCalculateDto largeWanDevicesCalculateDto = null;

			NetworkCalculateDto lanDevicesCalculateDto = null;
			NetworkCalculateDto smallLanDevicesCalculateDto = null;
			NetworkCalculateDto mediumLanDevicesCalculateDto = null;
			NetworkCalculateDto largeLanDevicesCalculateDto = null;

			NetworkCalculateDto wlanControllersCalculateDto = null;

			NetworkCalculateDto wlanAccessPointsCalculateDto = null;

			NetworkCalculateDto vpnIpSecCalculateDto = null;

			NetworkCalculateDto loadBalancersCalculateDto = null;

			NetworkCalculateDto dnsDhcpCalculateDto = null;

			NetworkCalculateDto firewallsCalculateDto = null;

			NetworkCalculateDto proxiesCalculateDto = null;

			NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
			List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
			// currency conversion based on the FX Rates and country factor
			String referenceCountry = dealInfo.getCountry();
			BigDecimal referenceCountryFactor = new BigDecimal(1);
			for (CountryFactorInfo countryFactorInfo : countryFactors) {
				if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
					referenceCountryFactor = countryFactorInfo.getCountryFactor();
					break;
				}
			}

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(NETWORK_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			String[] levelInd = networkInfo.getLevelIndicator().split(",");
			if (levelInd != null && (levelInd[0].equals("1") && levelInd[1].equals("0"))) {
				log.info("Got correct position for Wan devices calculation.");
				// Wan Devices calculation
				wanDevicesCalculateDto = wanDevicesCalculator.calculateWanDevicesYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(wanDevicesCalculateDto)){
					networkRevenueDto.setWanDevicesCalculateDto(wanDevicesCalculateDto);
				}
				
			} else if (levelInd != null && (levelInd[0].equals("1") && levelInd[1].equals("1"))) {
				log.info("Got correct position for Small/Medium/Large Wan devices calculation.");
				// Small Wan Devices calculation
				smallWanDevicesCalculateDto = smallWanDevicesCalculator.calculateSmallWanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(smallWanDevicesCalculateDto)){
					networkRevenueDto.setSmallWanDevicesCalculateDto(smallWanDevicesCalculateDto);
				}

				// Medium Wan Devices calculation
				mediumWanDevicesCalculateDto = mediumWanDevicesCalculator.calculateMediumWanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mediumWanDevicesCalculateDto)){
					networkRevenueDto.setMediumWanDevicesCalculateDto(mediumWanDevicesCalculateDto);
				}

				// Large Wan Devices calculation
				largeWanDevicesCalculateDto = largeWanDevicesCalculator.calculateLargeWanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(largeWanDevicesCalculateDto)){
					networkRevenueDto.setLargeWanDevicesCalculateDto(largeWanDevicesCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[2].equals("1") && levelInd[3].equals("0"))) {
				log.info("Got correct position for Lan devices calculation.");
				// Lan Devices calculation
				lanDevicesCalculateDto = lanDevicesCalculator.calculateLanDevicesYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(lanDevicesCalculateDto)){
					networkRevenueDto.setLanDevicesCalculateDto(lanDevicesCalculateDto);
				}
				
			} else if (levelInd != null && (levelInd[2].equals("1") && levelInd[3].equals("1"))) {
				log.info("Got correct position for Small/Medium/Large Lan devices calculation.");
				// Small Lan Devices calculation
				smallLanDevicesCalculateDto = smallLanDevicesCalculator.calculateSmallLanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(smallLanDevicesCalculateDto)){
					networkRevenueDto.setSmallLanDevicesCalculateDto(smallLanDevicesCalculateDto);
				}

				// Medium Lan Devices calculation
				mediumLanDevicesCalculateDto = mediumLanDevicesCalculator.calculateMediumLanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(mediumLanDevicesCalculateDto)){
					networkRevenueDto.setMediumLanDevicesCalculateDto(mediumLanDevicesCalculateDto);
				}

				// Large Lan Devices calculation
				largeLanDevicesCalculateDto = largeLanDevicesCalculator.calculateLargeLanDevicesYearlyRevenue(
						networkInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
						referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(largeLanDevicesCalculateDto)){
					networkRevenueDto.setLargeLanDevicesCalculateDto(largeLanDevicesCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[4].equals("1"))) {
				log.info("Got correct position for Wlan Controller calculation.");
				// Wlan Controller Devices calculation
				wlanControllersCalculateDto = wlanControllerCalculator.calculateWlanControllerYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(wlanControllersCalculateDto)){
					networkRevenueDto.setWlanControllersCalculateDto(wlanControllersCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[5].equals("1"))) {
				log.info("Got correct position for Wlan Access Point calculation.");
				// Wlan Access Point calculation
				wlanAccessPointsCalculateDto = wlanAccessPointsCalculator.calculateWlanAccessPointsYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(wlanAccessPointsCalculateDto)){
					networkRevenueDto.setWlanAccesspointCalculateDto(wlanAccessPointsCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[7].equals("1"))) {
				log.info("Got correct position for Vpn IpSec calculation.");
				// Vpn IpSec calculation
				vpnIpSecCalculateDto = vpnIpSecCalculator.calculateVpnIpSecYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(vpnIpSecCalculateDto)){
					networkRevenueDto.setVpnIpSecCalculateDto(vpnIpSecCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[6].equals("1"))) {
				log.info("Got correct position for Load Balancers calculation.");
				// Load Balancers calculation
				loadBalancersCalculateDto = loadBalancersCalculator.calculateLoadBalancerYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(loadBalancersCalculateDto)){
					networkRevenueDto.setLoadBalancersCalculateDto(loadBalancersCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[8].equals("1"))) {
				log.info("Got correct position for DNS DHCP calculation.");
				// DNS DHCP calculation
				dnsDhcpCalculateDto = dnsDhcpCalculator.calculateDnsDhcpYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(dnsDhcpCalculateDto)){
					networkRevenueDto.setDnsDhcpServiceCalculateDto(dnsDhcpCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[9].equals("1"))) {
				log.info("Got correct position for Firewalls calculation.");
				// Firewalls calculation
				firewallsCalculateDto = firewallsCalculator.calculateFirewallsYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(firewallsCalculateDto)){
					networkRevenueDto.setFirewallsCalculateDto(firewallsCalculateDto);
				}
			}

			if (levelInd != null && (levelInd[10].equals("1"))) {
				log.info("Got correct position for Proxies calculation.");
				// Proxies calculation
				proxiesCalculateDto = proxiesCalculator.calculateProxiesYearlyRevenue(networkInfo, dealInfo,
						towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
				if(!CommonHelper.isAllEmpty(proxiesCalculateDto)){
					networkRevenueDto.setProxiesCalculateDto(proxiesCalculateDto);
				}
			}
		}

		return networkRevenueDto;

	}

	/**
	 * @param dealId
	 * @param levelName
	 * @param dealType
	 * @return
	 * @throws Exception
	 */
	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		DealResultsResponse dealResultsDto = new DealResultsResponse();
		if (dealInfo != null) {
			NetworkInfo networkInfo = networkRepository.findByDealInfo(dealInfo);
			if (networkInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(NETWORK_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wanDealResultsHelper.getNearestPastDealsForWanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (SMALL_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = smallWanDealResultsHelper.getNearestPastDealsForSmallWanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (MEDIUM_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumWanDealResultsHelper.getNearestPastDealsForMediumWanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (LARGE_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = largeWanDealResultsHelper.getNearestPastDealsForLargeWanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = lanDealResultsHelper.getNearestPastDealsForLanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (SMALL_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = smallLanDealResultsHelper.getNearestPastDealsForSmallLanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (MEDIUM_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumLanDealResultsHelper.getNearestPastDealsForMediumLanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (LARGE_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = largeLanDealResultsHelper.getNearestPastDealsForLargeLanDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (CONTROLLERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wlanControllerDealResultsHelper.getNearestPastDealsForWlanControllersDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (ACCESS_POINTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wlanAccessPointsDealResultsHelper.getNearestPastDealsForWlanAccessPointsDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (LOAD_BALANCERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = loadBalancersDealResultsHelper.getNearestPastDealsForLoadBalancersDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (VPN_IPSEC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = vpnIPSecDealResultsHelper.getNearestPastDealsForVpnIPSecDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (DNS_DHCP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = dnsDhcpDealResultsHelper.getNearestPastDealsForDnsDhcpDevices(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (FIREWALLS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = firewallsDealResultsHelper.getNearestPastDealsForFirewalls(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					} else if (PROXIES_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = proxiesDealResultsHelper.getNearestPastDealsForProxies(
								networkInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
								dealInfo.getCurrency(), referenceCountry, referenceCountryFactor);
					}

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wanDealResultsHelper.getNearestBenchmarkDealsForWan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (SMALL_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = smallWanDealResultsHelper.getNearestBenchmarkDealsForSmallWan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (MEDIUM_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumWanDealResultsHelper.getNearestBenchmarkDealsForMediumWan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (LARGE_WAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = largeWanDealResultsHelper.getNearestBenchmarkDealsForLargeWan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = lanDealResultsHelper.getNearestBenchmarkDealsForLan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (SMALL_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = smallLanDealResultsHelper.getNearestBenchmarkDealsForSmallLan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (MEDIUM_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = mediumLanDealResultsHelper.getNearestBenchmarkDealsForMediumLan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (LARGE_LAN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = largeLanDealResultsHelper.getNearestBenchmarkDealsForLargeLan(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (CONTROLLERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wlanControllerDealResultsHelper.getNearestBenchmarkDealsForWlanControllers(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (ACCESS_POINTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = wlanAccessPointsDealResultsHelper.getNearestBenchmarkDealsForWlanAccessPoints(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (LOAD_BALANCERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = loadBalancersDealResultsHelper.getNearestBenchmarkDealsForLoadBalancers(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VPN_IPSEC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = vpnIPSecDealResultsHelper.getNearestBenchmarkDealsForVpnIPSec(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (DNS_DHCP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = dnsDhcpDealResultsHelper.getNearestBenchmarkDealsForDnsDhcp(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (FIREWALLS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = firewallsDealResultsHelper.getNearestBenchmarkDealsForFirewalls(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PROXIES_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = proxiesDealResultsHelper.getNearestBenchmarkDealsForProxies(
								networkInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					}

				}

			} else {
				throw new ServiceException("Can't find the Network Detail for given dealID.");
			}
		} else {
			throw new ServiceException("Invalid dealID.");
		}

		return dealResultsDto;
	}
}
