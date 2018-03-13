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
public class MediumLanDevicesCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String MEDIUM_LAN_DEVICE = "MEDIUM_LAN_DEVICE";

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateMediumLanDevicesYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgMediumLanDevices = networkCommonHelper
				.getMediumLanDevicesAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto mediumLanDevicesCalculateDto = null;
		if (avgMediumLanDevices!= null && avgMediumLanDevices.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			mediumLanDevicesCalculateDto = calculateMediumLanDevicesPastDealRevenue(networkInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgMediumLanDevices, mediumLanDevicesCalculateDto);

			// Benchmark Calculation
			mediumLanDevicesCalculateDto = calculateMediumLanDevicesBenchMarkDealRevenue(networkInfo, dealInfo,
					assessmentDealTerm, referenceCountryFactor, countryFactors, avgMediumLanDevices,
					mediumLanDevicesCalculateDto == null ? new NetworkCalculateDto() : mediumLanDevicesCalculateDto);
		}

		return mediumLanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgMediumLanDevices
	 * @param mediumLanDevicesCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateMediumLanDevicesPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgMediumLanDevices,
			NetworkCalculateDto mediumLanDevicesCalculateDto) {
		Specification<NetworkInfo> mediumLanDevicesSpecification = NetworkSpecification.specificationForMediumLanDevices(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgMediumLanDevices, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(mediumLanDevicesSpecification);
		log.info("Medium Lan devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, MEDIUM_LAN_DEVICE);
			mediumLanDevicesCalculateDto = prepareMediumLanDevicesCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return mediumLanDevicesCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param mediumLanDevicesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareMediumLanDevicesCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto mediumLanDevicesCalculateDto) {
		// Past Deal Medium Lan Devices Volume
		setMediumLanDevicesPrice(networkInfo, mediumLanDevicesCalculateDto, pastDealResults);
		return mediumLanDevicesCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param mediumLanDevicesCalculateDto
	 * @param dealResults
	 */
	private void setMediumLanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto mediumLanDevicesCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal Medium Lan devices
		Map<Integer, BigDecimal> mediumLanDeviceUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> mediumLanDeviceRevenueMap = getYearlyRevenue(mediumLanDeviceUnitPriceMap, networkInfo);
		BigDecimal mediumLanDeviceAvgUnitPrice = getConsolidatedAvgUnitPrice(mediumLanDeviceRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : mediumLanDeviceUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = mediumLanDeviceRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			mediumLanDevicesCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		mediumLanDevicesCalculateDto.setPastDealAvgUnitPrice(mediumLanDeviceAvgUnitPrice);

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
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getMediumLanDevices()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getMediumLanDevices());
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
					.multiply(new BigDecimal(yearlyDataInfo.getMediumLanDevices())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param mediumLanDevicesRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> mediumLanDevicesRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal mediumLanDevicesRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int mediumLanDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : mediumLanDevicesRevenueMap.entrySet()) {
			mediumLanDevicesRevenue = mediumLanDevicesRevenue.add(entry.getValue());
			mediumLanDevices = mediumLanDevices + networkInfo.getNetworkYearlyDataInfoList().get(i).getMediumLanDevices();
			i++;
		}
		if(mediumLanDevices != 0){
			consolidatedAvgUnitPrice = mediumLanDevicesRevenue
					.divide(new BigDecimal(mediumLanDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchMarkDealResults
	 * @param networkInfo
	 * @param mediumLanDevicesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateMediumLanDevicesBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgMediumLanDevices, NetworkCalculateDto mediumLanDevicesCalculateDto) {

		Specification<NetworkInfo> benchMarkLanDevicesSpecification = NetworkSpecification
				.specificationForBenchMarkMediumLanDevices(networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(),
						networkInfo.getLevelOfService(), avgMediumLanDevices);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkLanDevicesSpecification);
		log.info("Medium Lan devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, MEDIUM_LAN_DEVICE);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal mediumLanDevices = new BigDecimal(networkYearlyDataInfo.getMediumLanDevices());
			totalYearlySum = totalYearlySum.add(mediumLanDevices);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), mediumLanDevices);
		}

		// Benchmark Medium Lan Devices
		setBenchmarkMediumLanDevicesPrice(networkInfo, mediumLanDevicesCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return mediumLanDevicesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param mediumLanDevicesCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkMediumLanDevicesPrice(NetworkInfo networkInfo, NetworkCalculateDto mediumLanDevicesCalculateDto,
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
			NetworkInfo benchmarkApplicationInfo = benchMarkDealResults.get(0);
			for (NetworkYearlyDataInfo networkYearlyDataInfo : benchmarkApplicationInfo
					.getNetworkYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
					for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo
							.getNetworkUnitPriceInfoList()) {
						year = networkYearlyDataInfo.getYear();
						lanDevicesUnitPrice = networkUnitPriceInfo.getMediumLanDevices();
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

			mediumLanDevicesCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			mediumLanDevicesCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			mediumLanDevicesCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			mediumLanDevicesCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
