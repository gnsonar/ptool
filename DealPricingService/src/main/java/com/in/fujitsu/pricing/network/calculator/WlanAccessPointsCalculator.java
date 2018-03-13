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
public class WlanAccessPointsCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String WLAN_ACCESS_POINTS = "WLAN_ACCESS_POINTS";



	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateWlanAccessPointsYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgWlanAccessPoints = networkCommonHelper
				.getWlanAccessPointsAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto wlanAccessPointCalculateDto = null;
		if (avgWlanAccessPoints!= null && avgWlanAccessPoints.compareTo(new BigDecimal(0)) != 0) {

		}// Past Deal Calculation
		wlanAccessPointCalculateDto = calculateWlanAccessPointPastDealRevenue(networkInfo, dealInfo,
				towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
				countryFactors, referenceCountryFactor, avgWlanAccessPoints, wlanAccessPointCalculateDto);

		// Benchmark Calculation
		wlanAccessPointCalculateDto = calculateWlanAccessPointBenchMarkDealRevenue(networkInfo, dealInfo,
				assessmentDealTerm, referenceCountryFactor, countryFactors, avgWlanAccessPoints,
				wlanAccessPointCalculateDto == null ? new NetworkCalculateDto() : wlanAccessPointCalculateDto);

		return wlanAccessPointCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgWanDevices
	 * @param wlanAccessPointCalculateDto
	 * @param wanDevicesCalculator
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateWlanAccessPointPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgWanDevices,
			NetworkCalculateDto wlanAccessPointCalculateDto) {
		Specification<NetworkInfo> wlanAccessPointSpecification = NetworkSpecification.specificationForWlanAccessPoint(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgWanDevices, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(wlanAccessPointSpecification);
		log.info("Wlan Access Point devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, WLAN_ACCESS_POINTS);
			wlanAccessPointCalculateDto = prepareWlanAccessPointCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return wlanAccessPointCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param wlanAccessPointCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareWlanAccessPointCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto wlanAccessPointCalculateDto) {
		// Past Deal Wlan AccessPoint Volume
		setWlanAccessPointPrice(networkInfo, wlanAccessPointCalculateDto, pastDealResults);
		return wlanAccessPointCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param wanDevicesCalculateDto
	 * @param dealResults
	 */
	private void setWlanAccessPointPrice(NetworkInfo networkInfo, NetworkCalculateDto wanDevicesCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal Wlan AccessPoint Volume
		Map<Integer, BigDecimal> wlanAccessPointUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> wlanAccessPointRevenueMap = getYearlyRevenue(wlanAccessPointUnitPriceMap, networkInfo);
		BigDecimal wlanAccessPointAvgUnitPrice = getConsolidatedAvgUnitPrice(wlanAccessPointRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : wlanAccessPointUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = wlanAccessPointRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			wanDevicesCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		wanDevicesCalculateDto.setPastDealAvgUnitPrice(wlanAccessPointAvgUnitPrice);

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
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getWlanAccesspoint()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getWlanAccesspoint());
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
					.multiply(new BigDecimal(yearlyDataInfo.getWlanAccesspoint())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param wlanAccessPointRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> wlanAccessPointRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal wlanAccessPointRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int wlanAccessPoints = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : wlanAccessPointRevenueMap.entrySet()) {
			wlanAccessPointRevenue = wlanAccessPointRevenue.add(entry.getValue());
			wlanAccessPoints = wlanAccessPoints + networkInfo.getNetworkYearlyDataInfoList().get(i).getWlanAccesspoint();
			i++;
		}
		if(wlanAccessPoints != 0){
			consolidatedAvgUnitPrice = wlanAccessPointRevenue
					.divide(new BigDecimal(wlanAccessPoints), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param referenceCountryFactor
	 * @param countryFactors
	 * @param avgWlanAccessPoint
	 * @param wlanAccessPointCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateWlanAccessPointBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgWlanAccessPoint, NetworkCalculateDto wlanAccessPointCalculateDto) {

		Specification<NetworkInfo> benchMarkWlanAccessPointSpecification = NetworkSpecification
				.specificationForBenchMarkWlanAccesspoint(networkInfo.isOffshoreAllowed(),
						networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(), avgWlanAccessPoint);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkWlanAccessPointSpecification);
		log.info("WlanAccessPoint devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, WLAN_ACCESS_POINTS);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal wlanAccesspoint = new BigDecimal(networkYearlyDataInfo.getWlanAccesspoint());
			totalYearlySum = totalYearlySum.add(wlanAccesspoint);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), wlanAccesspoint);
		}

		// Benchmark Wlan AccessPoint Devices
		setBenchmarkWlanAccessPointPrice(networkInfo, wlanAccessPointCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return wlanAccessPointCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param wlanAccessPointsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkWlanAccessPointPrice(NetworkInfo networkInfo, NetworkCalculateDto wlanAccessPointsCalculateDto,
			List<NetworkInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal wlanAccessPointUnitPrice = new BigDecimal(0);
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
						wlanAccessPointUnitPrice = networkUnitPriceInfo.getWlanAccesspoint();
						revenue = wlanAccessPointUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(wlanAccessPointUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(wlanAccessPointUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			wlanAccessPointsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			wlanAccessPointsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			wlanAccessPointsCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			wlanAccessPointsCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
