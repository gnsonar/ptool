package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.TotalApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

/**
 * @author ChhabrMa
 *
 */
public class TotalApplicationCalculator {

	/**
	 * @param pastDealResults
	 * @param applicationInfo
	 * @param totalAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public TotalApplicationCalculateDto prepareTotalApplicationCalculateDtoForPastDeal(
			List<ApplicationInfo> pastDealResults, ApplicationInfo applicationInfo,
			TotalApplicationCalculateDto totalAppsCalculateDto) {

		// Past Deal Total Application Volume
		setTotalApplicationVolumePrice(applicationInfo, totalAppsCalculateDto, pastDealResults);
		return totalAppsCalculateDto;

	}

	/**
	 * @param applicationInfo
	 * @param totalApplicationCalculateDto
	 * @param dealResults
	 */
	private void setTotalApplicationVolumePrice(ApplicationInfo applicationInfo,
			TotalApplicationCalculateDto totalApplicationCalculateDto, List<ApplicationInfo> dealResults) {

		// Past Deal Total Application Volume
		Map<Integer, BigDecimal> totalAppsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> totalAppsRevenueMap = getYearlyRevenue(totalAppsUnitPriceMap, applicationInfo);
		BigDecimal totalAppsAvgUnitPrice = getConsolidatedAvgUnitPrice(totalAppsRevenueMap, applicationInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : totalAppsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = totalAppsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			totalApplicationCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		totalApplicationCalculateDto.setPastDealTotalAppsAvgUnitPrice(totalAppsAvgUnitPrice);

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
			yearlyUnitPriceMap.put(entry.getKey(),
					entry.getValue().divide(dealResultSize, 2, BigDecimal.ROUND_CEILING));
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
											.add(unitPriceInfo.getTotalAppsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									unitPriceInfo.getTotalAppsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getTotalAppsVolume())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param totalAppsVolumeRevenueMap
	 * @param applicationInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> totalAppsVolumeRevenueMap,
			ApplicationInfo applicationInfo) {
		BigDecimal totalAppsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int totalAppsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : totalAppsVolumeRevenueMap.entrySet()) {
			totalAppsRevenue = totalAppsRevenue.add(entry.getValue());
			totalAppsVolume = totalAppsVolume + applicationInfo.getAppYearlyDataInfos().get(i).getTotalAppsVolume();
			i++;
		}
		if(totalAppsVolume != 0){
			consolidatedAvgUnitPrice = totalAppsRevenue
					.divide(new BigDecimal(totalAppsVolume), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param banchmarkDealResults
	 * @param applicationInfo
	 * @param totalAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public TotalApplicationCalculateDto prepareTotalAppsCalculateDtoForBenchmark(
			List<ApplicationInfo> banchmarkDealResults, ApplicationInfo applicationInfo,
			TotalApplicationCalculateDto totalAppsCalculateDto) {

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			BigDecimal totalAppsVolume = new BigDecimal(applicationYearlyDataInfo.getTotalAppsVolume());
			totalYearlySum = totalYearlySum.add(totalAppsVolume);
			yearlyVolumeMap.put(applicationYearlyDataInfo.getYear(), totalAppsVolume);
		}

		// Benchmark Total Apps Volume
		setBenchmarkTotalAppsVolumePrice(applicationInfo, totalAppsCalculateDto, banchmarkDealResults, yearlyVolumeMap,
				totalYearlySum);
		return totalAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param totalAppsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkTotalAppsVolumePrice(ApplicationInfo applicationInfo,
			TotalApplicationCalculateDto totalAppsCalculateDto, List<ApplicationInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal totalAppsVolumeUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			ApplicationInfo benchmarkApplicationInfo = benchMarkDealResults.get(0);
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : benchmarkApplicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
							.getAppUnitPriceInfoList()) {
						year = applicationYearlyDataInfo.getYear();
						totalAppsVolumeUnitPrice = applicationUnitPriceInfo.getTotalAppsUnitPrice();
						revenue = totalAppsVolumeUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(totalAppsVolumeUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(totalAppsVolumeUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			totalAppsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			totalAppsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			totalAppsCalculateDto.setBenchDealLowTotalAppsAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			totalAppsCalculateDto.setBenchDealTargetTotalAppsAvgUnitPrice(totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
