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
public class ProxiesCalculator {

	@Autowired
	private NetworkCommonHelper networkCommonHelper;

	@Autowired
	private NetworkRepository networkRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String PROXIES = "PROXIES";



	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */

	public NetworkCalculateDto calculateProxiesYearlyRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgProxies = networkCommonHelper
				.getProxiesAverageVolume(networkInfo.getNetworkYearlyDataInfoList());
		NetworkCalculateDto proxiesCalculateDto = null;
		// Past Deal Calculation
		if (avgProxies!= null && avgProxies.compareTo(new BigDecimal(0)) != 0) {
			proxiesCalculateDto = calculateProxiesPastDealRevenue(networkInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor, avgProxies, proxiesCalculateDto);

			// Benchmark Calculation
			proxiesCalculateDto = calculateProxiesBenchMarkDealRevenue(networkInfo, dealInfo, assessmentDealTerm,
					referenceCountryFactor, countryFactors, avgProxies,
					proxiesCalculateDto == null ? new NetworkCalculateDto() : proxiesCalculateDto);
		}

		return proxiesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgProxies
	 * @param proxiesCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateProxiesPastDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor, BigDecimal avgProxies,
			NetworkCalculateDto proxiesCalculateDto) {
		Specification<NetworkInfo> proxiesSpecification = NetworkSpecification.specificationForProxies(
				networkInfo.isOffshoreAllowed(), networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(),
				avgProxies, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<NetworkInfo> pastDealResults = networkRepository.findAll(proxiesSpecification);
		log.info("Proxies dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, PROXIES);
			proxiesCalculateDto = prepareProxiesCalculateDtoForPastDeal(pastDealResults, networkInfo,
					new NetworkCalculateDto());

		}

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return proxiesCalculateDto;
	}

	/**
	 * @param pastDealResults
	 * @param networkInfo
	 * @param proxiesCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	private NetworkCalculateDto prepareProxiesCalculateDtoForPastDeal(List<NetworkInfo> pastDealResults,
			NetworkInfo networkInfo, NetworkCalculateDto proxiesCalculateDto) {
		// Past Deal Proxies Volume
		setProxiesPrice(networkInfo, proxiesCalculateDto, pastDealResults);
		return proxiesCalculateDto;

	}

	/**
	 * @param networkInfo
	 * @param proxiesCalculateDto
	 * @param dealResults
	 */
	private void setProxiesPrice(NetworkInfo networkInfo, NetworkCalculateDto proxiesCalculateDto,
			List<NetworkInfo> dealResults) {

		// Past Deal Proxies Volume
		Map<Integer, BigDecimal> proxiesUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> proxiesRevenueMap = getYearlyRevenue(proxiesUnitPriceMap, networkInfo);
		BigDecimal proxiesAvgUnitPrice = getConsolidatedAvgUnitPrice(proxiesRevenueMap, networkInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : proxiesUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = proxiesRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			proxiesCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		proxiesCalculateDto.setPastDealAvgUnitPrice(proxiesAvgUnitPrice);

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
									.get(networkYearlyDataInfo.getYear()).add(unitPriceInfo.getProxies()));
						} else {
							yearlyUnitPriceMap.put(networkYearlyDataInfo.getYear(), unitPriceInfo.getProxies());
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
					.multiply(new BigDecimal(yearlyDataInfo.getProxies())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param proxiesRevenueMap
	 * @param networkInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> proxiesRevenueMap,
			NetworkInfo networkInfo) {
		BigDecimal proxiesRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int proxies = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : proxiesRevenueMap.entrySet()) {
			proxiesRevenue = proxiesRevenue.add(entry.getValue());
			proxies = proxies + networkInfo.getNetworkYearlyDataInfoList().get(i).getProxies();
			i++;
		}
		if(proxies != 0){
			consolidatedAvgUnitPrice = proxiesRevenue
					.divide(new BigDecimal(proxies), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param networkInfo
	 * @param dealInfo
	 * @param assessmentDealTerm
	 * @param referenceCountryFactor
	 * @param countryFactors
	 * @param avgProxies
	 * @param proxiesCalculateDto
	 * @return
	 */
	@Transactional
	private NetworkCalculateDto calculateProxiesBenchMarkDealRevenue(NetworkInfo networkInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgProxies, NetworkCalculateDto proxiesCalculateDto) {

		Specification<NetworkInfo> benchMarkProxiesSpecification = NetworkSpecification
				.specificationForBenchMarkProxies(networkInfo.isOffshoreAllowed(),
						networkInfo.isIncludeHardware(), networkInfo.getLevelOfService(), avgProxies);
		List<NetworkInfo> benchMarkDealResults = networkRepository.findAll(benchMarkProxiesSpecification);
		log.info("Proxies devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			networkCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			networkCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, PROXIES);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
			BigDecimal proxies = new BigDecimal(networkYearlyDataInfo.getProxies());
			totalYearlySum = totalYearlySum.add(proxies);
			yearlyVolumeMap.put(networkYearlyDataInfo.getYear(), proxies);
		}

		// Benchmark Proxies Devices
		setBenchmarkProxiesPrice(networkInfo, proxiesCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return proxiesCalculateDto;
	}

	/**
	 * @param networkInfo
	 * @param proxiesCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkProxiesPrice(NetworkInfo networkInfo, NetworkCalculateDto proxiesCalculateDto,
			List<NetworkInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal proxiesUnitPrice = new BigDecimal(0);
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
						proxiesUnitPrice = networkUnitPriceInfo.getProxies();
						revenue = proxiesUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(proxiesUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (networkUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(networkUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(proxiesUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			proxiesCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			proxiesCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			proxiesCalculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			proxiesCalculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
