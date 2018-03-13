package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.ComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

/**
 * @author ChhabrMa
 *
 */
public class ComplexApplicationCalculator {

	/**
	 * @param pastDealResults
	 * @param applicationInfo
	 * @param complexAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ComplexApplicationCalculateDto prepareComplexApplicationCalculateDtoForPastDeal(
			List<ApplicationInfo> pastDealResults, ApplicationInfo applicationInfo,
			ComplexApplicationCalculateDto complexAppsCalculateDto) {

		// Past Deal Complex Application Volume
		setComplexApplicationVolumePrice(applicationInfo, complexAppsCalculateDto, pastDealResults);
		return complexAppsCalculateDto;

	}

	/**
	 * @param applicationInfo
	 * @param complexApplicationCalculateDto
	 * @param dealResults
	 */
	private void setComplexApplicationVolumePrice(ApplicationInfo applicationInfo,
			ComplexApplicationCalculateDto complexApplicationCalculateDto, List<ApplicationInfo> dealResults) {

		// Past Deal Complex Application Volume
		Map<Integer, BigDecimal> complexAppsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> complexAppsRevenueMap = getYearlyRevenue(complexAppsUnitPriceMap, applicationInfo);
		BigDecimal complexAppsAvgUnitPrice = getConsolidatedAvgUnitPrice(complexAppsRevenueMap, applicationInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : complexAppsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = complexAppsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			complexApplicationCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		complexApplicationCalculateDto.setPastDealComplexAppsAvgUnitPrice(complexAppsAvgUnitPrice);

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
											.add(unitPriceInfo.getComplexAppsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									unitPriceInfo.getComplexAppsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getComplexAppsVolume())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param complexAppsVolumeRevenueMap
	 * @param applicationInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> complexAppsVolumeRevenueMap,
			ApplicationInfo applicationInfo) {
		BigDecimal complexAppsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int complexAppsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : complexAppsVolumeRevenueMap.entrySet()) {
			complexAppsRevenue = complexAppsRevenue.add(entry.getValue());
			complexAppsVolume = complexAppsVolume
					+ applicationInfo.getAppYearlyDataInfos().get(i).getComplexAppsVolume();
			i++;
		}
		if(complexAppsVolume != 0){
			consolidatedAvgUnitPrice = complexAppsRevenue
					.divide(new BigDecimal(complexAppsVolume), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param applicationInfo
	 * @param complexAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ComplexApplicationCalculateDto prepareComplexAppsCalculateDtoForBenchmark(
			List<ApplicationInfo> benchmarkDealResults, ApplicationInfo applicationInfo,
			ComplexApplicationCalculateDto complexAppsCalculateDto) {

		BigDecimal complexYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			BigDecimal complexAppsVolume = new BigDecimal(applicationYearlyDataInfo.getComplexAppsVolume());
			complexYearlySum = complexYearlySum.add(complexAppsVolume);
			yearlyVolumeMap.put(applicationYearlyDataInfo.getYear(), complexAppsVolume);
		}

		// Benchmark Complex Apps Volume
		setBenchmarkComplexAppsVolumePrice(applicationInfo, complexAppsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, complexYearlySum);
		return complexAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param complexAppsCalculateDto
	 * @param benchmarkDealResults
	 * @param yearlyVolumeMap
	 * @param complexYearlySum
	 */
	private void setBenchmarkComplexAppsVolumePrice(ApplicationInfo applicationInfo,
			ComplexApplicationCalculateDto complexAppsCalculateDto, List<ApplicationInfo> benchmarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal complexYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal complexAppsVolumeUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal complexLowRevenue = new BigDecimal(0);
		BigDecimal complexTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchmarkDealResults)) {
			ApplicationInfo benchmarkApplicationInfo = benchmarkDealResults.get(0);
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : benchmarkApplicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
							.getAppUnitPriceInfoList()) {
						year = applicationYearlyDataInfo.getYear();
						complexAppsVolumeUnitPrice = applicationUnitPriceInfo.getComplexAppsUnitPrice();
						revenue = complexAppsVolumeUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(complexAppsVolumeUnitPrice.floatValue());
							complexLowRevenue = complexLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(complexAppsVolumeUnitPrice.floatValue());

							complexTargetRevenue = complexTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			complexAppsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			complexAppsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			complexAppsCalculateDto.setBenchDealLowComplexAppsAvgUnitPrice(complexLowRevenue.divide(complexYearlySum, 2, BigDecimal.ROUND_CEILING));
			complexAppsCalculateDto
					.setBenchDealTargetComplexAppsAvgUnitPrice(complexTargetRevenue.divide(complexYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
