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
public class SmallWanDevicesCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String SMALL_WAN_DEVICE = "SMALL_WAN_DEVICE";

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateSmallWanDevicesYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgSmallWanDevices = networkCommonHelper
				.getSmallWanDevicesAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto smallWanDevicesCalculateDto = null;
		if (avgSmallWanDevices!= null && avgSmallWanDevices.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			smallWanDevicesCalculateDto = calculateSmallWanDevicesPastDealRevenue(networkInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgSmallWanDevices, smallWanDevicesCalculateDto);


			// Benchmark Calculation
			smallWanDevicesCalculateDto = calculateSmallWanDevicesBenchMarkDealRevenue(networkInfo, dealInfo, assessmentDealTerm, referenceCountryFactor,
					countryFactors, avgSmallWanDevices,
					smallWanDevicesCalculateDto == null ? new NetworkCalculateDto() : smallWanDevicesCalculateDto);

		}

		return smallWanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgSmallLanDevices
	 * @param smallWanDevicesCalculateDto
	 * @param wanDevicesCalculator
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateSmallWanDevicesPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgSmallLanDevices,
			NetworkCalculateDto smallWanDevicesCalculateDto) {
		Specification<NetworkInfo> smallWanDevicesSpecification = NetworkSpecification.specificationForSmallWanDevices(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgSmallLanDevices, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(smallWanDevicesSpecification);
		log.info("Small Wan devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, SMALL_WAN_DEVICE);
			smallWanDevicesCalculateDto = prepareSmallWanDevicesCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return smallWanDevicesCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param smallWanDevicesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareSmallWanDevicesCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto smallWanDevicesCalculateDto) {
		// Past Deal Small Wan Devices Volume
		setSmallWanDevicesPrice(networkInfo, smallWanDevicesCalculateDto, pastDealResults);
		return smallWanDevicesCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param smallWanDevicesCalculateDto
	 * @param dealResults
	 */
	private void setSmallWanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto smallWanDevicesCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal Small Wan devices
		Map<Integer, BigDecimal> smallWanDeviceUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> smallWanDeviceRevenueMap = getYearlyRevenue(smallWanDeviceUnitPriceMap, networkInfo);
		BigDecimal smalWanDeviceAvgUnitPrice = getConsolidatedAvgUnitPrice(smallWanDeviceRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : smallWanDeviceUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = smallWanDeviceRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			smallWanDevicesCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		smallWanDevicesCalculateDto.setPastDealAvgUnitPrice(smalWanDeviceAvgUnitPrice);

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
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getSmallWanDevices()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getSmallWanDevices());
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
					.multiply(new BigDecimal(yearlyDataInfo.getSmallWanDevices())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param smallWanDevicesRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> smallWanDevicesRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal smallWanDevicesRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int smallWanDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : smallWanDevicesRevenueMap.entrySet()) {
			smallWanDevicesRevenue = smallWanDevicesRevenue.add(entry.getValue());
			smallWanDevices = smallWanDevices + networkInfo.getNetworkYearlyDataInfoList().get(i).getSmallWanDevices();
			i++;
		}
		if(smallWanDevices != 0){
			consolidatedAvgUnitPrice = smallWanDevicesRevenue
					.divide(new BigDecimal(smallWanDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchMarkDealResults
	 * @param networkInfo
	 * @param smallWanDevicesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateSmallWanDevicesBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgSmallWanDevices, NetworkCalculateDto smallWanDevicesCalculateDto) {

		Specification<NetworkInfo> benchMarkWanDevicesSpecification = NetworkSpecification
				.specificationForBenchMarkSmallWanDevices(networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(),
						networkInfo.getLevelOfService(), avgSmallWanDevices);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkWanDevicesSpecification);
		log.info("Small Wan devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, SMALL_WAN_DEVICE);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal smallWanDevices = new BigDecimal(networkYearlyDataInfo.getSmallWanDevices());
			totalYearlySum = totalYearlySum.add(smallWanDevices);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), smallWanDevices);
		}

		// Benchmark Small Wan Devices
		setBenchmarkSmallWanDevicesPrice(networkInfo, smallWanDevicesCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return smallWanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param smallWanDevicesCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkSmallWanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto smallWanDevicesCalculateDto,
			List<NetworkInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal smallWanDevicesUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			NetworkInfo benchmarkApplicationInfo = benchMarkDealResults.get(0);
			for (NetworkYearlyDataInfo networkYearlyDataInfo : benchmarkApplicationInfo
					.getNetworkYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
					for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo
							.getNetworkUnitPriceInfoList()) {
						year = networkYearlyDataInfo.getYear();
						smallWanDevicesUnitPrice = networkUnitPriceInfo.getSmallWanDevices();
						revenue = smallWanDevicesUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(smallWanDevicesUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(smallWanDevicesUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			smallWanDevicesCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			smallWanDevicesCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			smallWanDevicesCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			smallWanDevicesCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
