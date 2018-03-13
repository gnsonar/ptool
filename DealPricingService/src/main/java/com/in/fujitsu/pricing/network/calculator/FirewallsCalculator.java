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
public class FirewallsCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String FIREWALLS = "FIREWALLS";



	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateFirewallsYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgFireWalls = networkCommonHelper
				.getFirewallsAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto fireWallsCalculateDto = null;
		// Past Deal Calculation
		if (avgFireWalls!= null && avgFireWalls.compareTo(new BigDecimal(0)) != 0) {
			fireWallsCalculateDto = calculateFireWallsPastDealRevenue(networkInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgFireWalls, fireWallsCalculateDto);

			// Benchmark Calculation
			fireWallsCalculateDto = calculateFirewallsBenchMarkDealRevenue(networkInfo, dealInfo, assessmentDealTerm,
					referenceCountryFactor, countryFactors, avgFireWalls,
					fireWallsCalculateDto == null ? new NetworkCalculateDto() : fireWallsCalculateDto);
		}

		return fireWallsCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgFirewalls
	 * @param fireWallsCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateFireWallsPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgFirewalls,
			NetworkCalculateDto fireWallsCalculateDto) {
		Specification<NetworkInfo> fireWallsSpecification = NetworkSpecification.specificationForFireWalls(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgFirewalls, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(fireWallsSpecification);
		log.info("FireWalls dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, FIREWALLS);
			fireWallsCalculateDto = prepareFireWallsCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return fireWallsCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param fireWallsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareFireWallsCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto fireWallsCalculateDto) {
		// Past Deal FireWalls Volume
		setFireWallsPrice(networkInfo, fireWallsCalculateDto, pastDealResults);
		return fireWallsCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param fireWallsCalculateDto
	 * @param dealResults
	 */
	private void setFireWallsPrice(NetworkInfo networkInfo, NetworkCalculateDto fireWallsCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal FireWalls Volume
		Map<Integer, BigDecimal> fireWallsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> fireWallsRevenueMap = getYearlyRevenue(fireWallsUnitPriceMap, networkInfo);
		BigDecimal fireWallsAvgUnitPrice = getConsolidatedAvgUnitPrice(fireWallsRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : fireWallsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = fireWallsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			fireWallsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		fireWallsCalculateDto.setPastDealAvgUnitPrice(fireWallsAvgUnitPrice);

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
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getFirewalls()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getFirewalls());
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
					.multiply(new BigDecimal(yearlyDataInfo.getFirewalls())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param fireWallsRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> fireWallsRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal fireWallsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int fireWalls = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : fireWallsRevenueMap.entrySet()) {
			fireWallsRevenue = fireWallsRevenue.add(entry.getValue());
			fireWalls = fireWalls + networkInfo.getNetworkYearlyDataInfoList().get(i).getFirewalls();
			i++;
		}
		if(fireWalls != 0){
			consolidatedAvgUnitPrice = fireWallsRevenue
					.divide(new BigDecimal(fireWalls), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param referenceCountryFactor
	 * @param countryFactors
	 * @param avgFirewalls
	 * @param firewallsCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateFirewallsBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgFirewalls, NetworkCalculateDto firewallsCalculateDto) {

		Specification<NetworkInfo> benchMarkFirewallsSpecification = NetworkSpecification
				.specificationForBenchMarkFirewalls(networkInfo.isOffshoreAllowed(),
						networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(), avgFirewalls);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkFirewallsSpecification);
		log.info("Firewalls devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, FIREWALLS);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal firewalls = new BigDecimal(networkYearlyDataInfo.getFirewalls());
			totalYearlySum = totalYearlySum.add(firewalls);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), firewalls);
		}

		// Benchmark Firewalls Devices
		setBenchmarkFirewallsPrice(networkInfo, firewallsCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return firewallsCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param firewallsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkFirewallsPrice(NetworkInfo networkInfo, NetworkCalculateDto firewallsCalculateDto,
			List<NetworkInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal firewallsUnitPrice = new BigDecimal(0);
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
						firewallsUnitPrice = networkUnitPriceInfo.getFirewalls();
						revenue = firewallsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(firewallsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(firewallsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			firewallsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			firewallsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			firewallsCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			firewallsCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
