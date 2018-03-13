package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.SimpleApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

/**
 * @author ChhabrMa
 *
 */
public class SimpleApplicationCalculator {

	/**
	 * @param pastDealResults
	 * @param applicationInfo
	 * @param simpleAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public SimpleApplicationCalculateDto prepareSimpleApplicationCalculateDtoForPastDeal(
			List<ApplicationInfo> pastDealResults, ApplicationInfo applicationInfo,
			SimpleApplicationCalculateDto simpleAppsCalculateDto) {

		// Past Deal Simple Application Volume
		setSimpleApplicationVolumePrice(applicationInfo, simpleAppsCalculateDto, pastDealResults);
		return simpleAppsCalculateDto;

	}

	/**
	 * @param applicationInfo
	 * @param simpleApplicationCalculateDto
	 * @param dealResults
	 */
	private void setSimpleApplicationVolumePrice(ApplicationInfo applicationInfo,
			SimpleApplicationCalculateDto simpleApplicationCalculateDto, List<ApplicationInfo> dealResults) {

		// Past Deal Simple Application Volume
		Map<Integer, BigDecimal> simpleAppsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> simpleAppsRevenueMap = getYearlyRevenue(simpleAppsUnitPriceMap, applicationInfo);
		BigDecimal simpleAppsAvgUnitPrice = getConsolidatedAvgUnitPrice(simpleAppsRevenueMap, applicationInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : simpleAppsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = simpleAppsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			simpleApplicationCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		simpleApplicationCalculateDto.setPastDealSimpleAppsAvgUnitPrice(simpleAppsAvgUnitPrice);

	}

	/**
	 * @param dealResults
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyUnitPrice(List<ApplicationInfo> dealResults) {
		final Map<Integer, BigDecimal> yearlyUnitPriceMap = new HashMap<>();
		prepareYearlyUnitPriceSum(yearlyUnitPriceMap, dealResults);
		BigDecimal dealResultSize = new BigDecimal(dealResults.size());
		for (Map.Entry<Integer, BigDecimal> entry : yearlyUnitPriceMap.entrySet()) {
			yearlyUnitPriceMap.put(entry.getKey(), entry.getValue().divide(dealResultSize, 2, BigDecimal.ROUND_CEILING));
		}

		return yearlyUnitPriceMap;
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param dealData
	 */
	public void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			List<ApplicationInfo> dealResults) {
		for (ApplicationInfo applicationInfo : dealResults) {
			if (!CollectionUtils.isEmpty(applicationInfo.getAppYearlyDataInfos())) {
				for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
					if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())
							&& applicationYearlyDataInfo.getAppUnitPriceInfoList().get(0) != null) {
						ApplicationUnitPriceInfo unitPriceInfo = applicationYearlyDataInfo.getAppUnitPriceInfoList()
								.get(0);
						if (yearlyUnitPriceMap.containsKey(applicationYearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									yearlyUnitPriceMap.get(applicationYearlyDataInfo.getYear())
											.add(unitPriceInfo.getSimpleAppsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									unitPriceInfo.getSimpleAppsUnitPrice());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param applicationInfo
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			ApplicationInfo applicationInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (ApplicationYearlyDataInfo yearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getSimpleAppsVolume())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param simpleAppsVolumeRevenueMap
	 * @param applicationInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> simpleAppsVolumeRevenueMap,
			ApplicationInfo applicationInfo) {
		BigDecimal simpleAppsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int simpleAppsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : simpleAppsVolumeRevenueMap.entrySet()) {
			simpleAppsRevenue = simpleAppsRevenue.add(entry.getValue());
			simpleAppsVolume = simpleAppsVolume + applicationInfo.getAppYearlyDataInfos().get(i).getSimpleAppsVolume();
			i++;
		}
		if(simpleAppsVolume != 0){
			consolidatedAvgUnitPrice = simpleAppsRevenue
					.divide(new BigDecimal(simpleAppsVolume),2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param applicationInfo
	 * @param simpleAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public SimpleApplicationCalculateDto prepareSimpleAppsCalculateDtoForBenchmark(
			List<ApplicationInfo> benchmarkDealResults, ApplicationInfo applicationInfo,
			SimpleApplicationCalculateDto simpleAppsCalculateDto) {

		BigDecimal simpleYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			BigDecimal simpleAppsVolume = new BigDecimal(applicationYearlyDataInfo.getSimpleAppsVolume());
			simpleYearlySum = simpleYearlySum.add(simpleAppsVolume);
			yearlyVolumeMap.put(applicationYearlyDataInfo.getYear(), simpleAppsVolume);
		}

		// Benchmark Simple Apps Volume
		setBenchmarkSimpleAppsVolumePrice(applicationInfo, simpleAppsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, simpleYearlySum);
		return simpleAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param simpleAppsCalculateDto
	 * @param benchmarkDealResults
	 * @param yearlyVolumeMap
	 * @param simpleYearlySum
	 */
	private void setBenchmarkSimpleAppsVolumePrice(ApplicationInfo applicationInfo,
			SimpleApplicationCalculateDto simpleAppsCalculateDto, List<ApplicationInfo> benchmarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal simpleYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal simpleAppsVolumeUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal simpleLowRevenue = new BigDecimal(0);
		BigDecimal simpleTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchmarkDealResults)) {
			ApplicationInfo benchmarkApplicationInfo = benchmarkDealResults.get(0);
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : benchmarkApplicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
							.getAppUnitPriceInfoList()) {
						year = applicationYearlyDataInfo.getYear();
						simpleAppsVolumeUnitPrice = applicationUnitPriceInfo.getSimpleAppsUnitPrice();
						revenue = simpleAppsVolumeUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(simpleAppsVolumeUnitPrice.floatValue());
							simpleLowRevenue = simpleLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(simpleAppsVolumeUnitPrice.floatValue());

							simpleTargetRevenue = simpleTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			simpleAppsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			simpleAppsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			simpleAppsCalculateDto.setBenchDealLowSimpleAppsAvgUnitPrice(simpleLowRevenue.divide(simpleYearlySum,2, BigDecimal.ROUND_CEILING));
			simpleAppsCalculateDto.setBenchDealTargetSimpleAppsAvgUnitPrice(simpleTargetRevenue.divide(simpleYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
