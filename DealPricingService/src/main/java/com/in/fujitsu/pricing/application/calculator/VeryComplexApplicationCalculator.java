package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.VeryComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

/**
 * @author ChhabrMa
 *
 */
public class VeryComplexApplicationCalculator {

	/**
	 * @param pastDealResults
	 * @param applicationInfo
	 * @param veryComplexAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public VeryComplexApplicationCalculateDto prepareVeryComplexApplicationCalculateDtoForPastDeal(
			List<ApplicationInfo> pastDealResults, ApplicationInfo applicationInfo,
			VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto) {

		// Past Deal Very Complex Application Volume
		setVeryComplexApplicationVolumePrice(applicationInfo, veryComplexAppsCalculateDto, pastDealResults);
		return veryComplexAppsCalculateDto;

	}

	/**
	 * @param applicationInfo
	 * @param veryComplexApplicationCalculateDto
	 * @param dealResults
	 */
	private void setVeryComplexApplicationVolumePrice(ApplicationInfo applicationInfo,
			VeryComplexApplicationCalculateDto veryComplexApplicationCalculateDto, List<ApplicationInfo> dealResults) {

		// Past Deal Very Complex Application Volume
		Map<Integer, BigDecimal> veryComplexAppsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> veryComplexAppsRevenueMap = getYearlyRevenue(veryComplexAppsUnitPriceMap,
				applicationInfo);
		BigDecimal veryComplexAppsAvgUnitPrice = getConsolidatedAvgUnitPrice(veryComplexAppsRevenueMap,
				applicationInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : veryComplexAppsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = veryComplexAppsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			veryComplexApplicationCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		veryComplexApplicationCalculateDto.setPastDealVeryComplexAppsAvgUnitPrice(veryComplexAppsAvgUnitPrice);

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
											.add(unitPriceInfo.getVeryComplexAppsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									unitPriceInfo.getVeryComplexAppsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getVeryComplexAppsVolume())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param veryComplexAppsVolumeRevenueMap
	 * @param applicationInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> veryComplexAppsVolumeRevenueMap,
			ApplicationInfo applicationInfo) {
		BigDecimal veryComplexAppsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int veryComplexAppsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : veryComplexAppsVolumeRevenueMap.entrySet()) {
			veryComplexAppsRevenue = veryComplexAppsRevenue.add(entry.getValue());
			veryComplexAppsVolume = veryComplexAppsVolume
					+ applicationInfo.getAppYearlyDataInfos().get(i).getVeryComplexAppsVolume();
			i++;
		}
		if(veryComplexAppsVolume != 0){
			consolidatedAvgUnitPrice = veryComplexAppsRevenue
					.divide(new BigDecimal(veryComplexAppsVolume), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param applicationInfo
	 * @param veryComplexAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public VeryComplexApplicationCalculateDto prepareVeryComplexAppsCalculateDtoForBenchmark(
			List<ApplicationInfo> benchmarkDealResults, ApplicationInfo applicationInfo,
			VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto) {

		BigDecimal veryComplexYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			BigDecimal veryComplexAppsVolume = new BigDecimal(applicationYearlyDataInfo.getVeryComplexAppsVolume());
			veryComplexYearlySum = veryComplexYearlySum.add(veryComplexAppsVolume);
			yearlyVolumeMap.put(applicationYearlyDataInfo.getYear(), veryComplexAppsVolume);
		}

		// Benchmark Very Complex Apps Volume
		setBenchmarkVeryComplexAppsVolumePrice(applicationInfo, veryComplexAppsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, veryComplexYearlySum);
		return veryComplexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param veryComplexAppsCalculateDto
	 * @param benchmarkDealResults
	 * @param yearlyVolumeMap
	 * @param veryComplexYearlySum
	 */
	private void setBenchmarkVeryComplexAppsVolumePrice(ApplicationInfo applicationInfo,
			VeryComplexApplicationCalculateDto veryComplexAppsCalculateDto, List<ApplicationInfo> benchmarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal veryComplexYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal veryComplexAppsVolumeUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal veryComplexLowRevenue = new BigDecimal(0);
		BigDecimal veryComplexTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchmarkDealResults)) {
			ApplicationInfo benchmarkApplicationInfo = benchmarkDealResults.get(0);
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : benchmarkApplicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
							.getAppUnitPriceInfoList()) {
						year = applicationYearlyDataInfo.getYear();
						veryComplexAppsVolumeUnitPrice = applicationUnitPriceInfo.getVeryComplexAppsUnitPrice();
						revenue = veryComplexAppsVolumeUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(veryComplexAppsVolumeUnitPrice.floatValue());
							veryComplexLowRevenue = veryComplexLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(veryComplexAppsVolumeUnitPrice.floatValue());

							veryComplexTargetRevenue = veryComplexTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			veryComplexAppsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			veryComplexAppsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			veryComplexAppsCalculateDto
					.setBenchDealLowVeryComplexAppsAvgUnitPrice(veryComplexLowRevenue.divide(veryComplexYearlySum, 2, BigDecimal.ROUND_CEILING));
			veryComplexAppsCalculateDto
					.setBenchDealTargetVeryComplexAppsAvgUnitPrice(veryComplexTargetRevenue.divide(veryComplexYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
