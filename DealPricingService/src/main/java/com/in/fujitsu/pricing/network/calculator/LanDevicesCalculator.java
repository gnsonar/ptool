package com.in.fujitsu.pricing.network.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.network.dto.NetworkCalculateDto;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkUnitPriceInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;
import com.in.fujitsu.pricing.network.helper.NetworkCommonHelper;
import com.in.fujitsu.pricing.network.repository.NetworkRepository;
import com.in.fujitsu.pricing.specification.NetworkSpecification;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ChhabrMa
 *
 */
@Slf4j
@Component
public class LanDevicesCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String LAN_DEVICE = "LAN_DEVICE";



	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateLanDevicesYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgLanDevices = networkCommonHelper
				.getLanDevicesAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto lanDevicesCalculateDto = null;

		if (avgLanDevices!= null && avgLanDevices.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			lanDevicesCalculateDto = calculateLanDevicesPastDealRevenue(networkInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgLanDevices, lanDevicesCalculateDto);

			// Benchmark Calculation
			lanDevicesCalculateDto = calculateLanDevicesBenchMarkDealRevenue(networkInfo, dealInfo, assessmentDealTerm,
					referenceCountryFactor, countryFactors, avgLanDevices,
					lanDevicesCalculateDto == null ? new NetworkCalculateDto() : lanDevicesCalculateDto);

		}

		return lanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgLanDevices
	 * @param lanDevicesCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateLanDevicesPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgLanDevices,
			NetworkCalculateDto lanDevicesCalculateDto) {
		Specification<NetworkInfo> lanDevicesSpecification = NetworkSpecification.specificationForLanDevices(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgLanDevices, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(lanDevicesSpecification);
		log.info("Lan devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, LAN_DEVICE);
			lanDevicesCalculateDto = prepareLanDevicesCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return lanDevicesCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param lanDevicesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareLanDevicesCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto lanDevicesCalculateDto) {
		// Past Deal Lan Devices Volume
		setLanDevicesPrice(networkInfo, lanDevicesCalculateDto, pastDealResults);
		return lanDevicesCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param lanDevicesCalculateDto
	 * @param dealResults
	 */
	private void setLanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto lanDevicesCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal Lan devices Volume
		Map<Integer, BigDecimal> lanDeviceUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> lanDeviceRevenueMap = getYearlyRevenue(lanDeviceUnitPriceMap, networkInfo);
		BigDecimal lanDeviceAvgUnitPrice = getConsolidatedAvgUnitPrice(lanDeviceRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : lanDeviceUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = lanDeviceRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			lanDevicesCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		lanDevicesCalculateDto.setPastDealAvgUnitPrice(lanDeviceAvgUnitPrice);

	}

	/**
	 * @param dealResults
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyUnitPrice(List<NetworkInfo> dealResults) {
		final Map<Integer, BigDecimal> yearlyUnitPriceMap = new HashMap<>();
		prepareYearlyUnitPriceSum(yearlyUnitPriceMap, dealResults);
		BigDecimal dealResultSize = new BigDecimal(dealResults.size());
		for (Map.Entry<Integer, BigDecimal> entry : yearlyUnitPriceMap.entrySet()) {
			yearlyUnitPriceMap.put(entry.getKey(),
					entry.getValue().divide(dealResultSize, 2, BigDecimal.ROUND_CEILING));
		}

		return yearlyUnitPriceMap;
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param dealData
	 */
	private void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap, List<NetworkInfo> dealResults) {
		for (NetworkInfo networkInfo : dealResults) {
			if (!CollectionUtils.isEmpty(networkInfo.getNetworkYearlyDataInfoList())) {
				for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())
							&& networkYearlyDataInfo.getNetworkUnitPriceInfoList().get(0) != null) {
						NetworkUnitPriceInfo unitPriceInfo = networkYearlyDataInfo.getNetworkUnitPriceInfoList().get(0);
						if (yearlyUnitPriceMap.containsKey(networkYearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), yearlyUnitPriceMap
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getLanDevices()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getLanDevices());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param networkInfo
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			NetworkInfo networkInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (NetworkYearlyDataInfo yearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getLanDevices())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param lanDevicesRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> lanDevicesRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal lanDevicesRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int lanDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : lanDevicesRevenueMap.entrySet()) {
			lanDevicesRevenue = lanDevicesRevenue.add(entry.getValue());
			lanDevices = lanDevices + networkInfo.getNetworkYearlyDataInfoList().get(i).getLanDevices();
			i++;
		}
		if(lanDevices != 0){
			consolidatedAvgUnitPrice = lanDevicesRevenue
					.divide(new BigDecimal(lanDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param referenceCountryFactor
	 * @param countryFactors
	 * @param avgLanDevices
	 * @param lanDevicesCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateLanDevicesBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgLanDevices, NetworkCalculateDto lanDevicesCalculateDto) {

		Specification<NetworkInfo> benchMarkLanDevicesSpecification = NetworkSpecification
				.specificationForBenchMarkLanDevices(networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(),
						networkInfo.getLevelOfService(), avgLanDevices);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkLanDevicesSpecification);
		log.info("Lan devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, LAN_DEVICE);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal lanDevices = new BigDecimal(networkYearlyDataInfo.getLanDevices());
			totalYearlySum = totalYearlySum.add(lanDevices);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), lanDevices);
		}

		// Benchmark Lan Devices
		setBenchmarkLanDevicesPrice(networkInfo, lanDevicesCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return lanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param lanDevicesCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkLanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto lanDevicesCalculateDto,
			List<NetworkInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal lanDevicesUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			NetworkInfo benchmarkNetworkInfo = benchMarkDealResults.get(0);
			for (NetworkYearlyDataInfo networkYearlyDataInfo : benchmarkNetworkInfo.getNetworkYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
					for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo
							.getNetworkUnitPriceInfoList()) {
						year = networkYearlyDataInfo.getYear();
						lanDevicesUnitPrice = networkUnitPriceInfo.getLanDevices();
						revenue = lanDevicesUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(lanDevicesUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(lanDevicesUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			lanDevicesCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			lanDevicesCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			lanDevicesCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			lanDevicesCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
