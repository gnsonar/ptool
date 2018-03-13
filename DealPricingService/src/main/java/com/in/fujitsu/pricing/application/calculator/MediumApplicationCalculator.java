package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.MediumApplicationCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;

/**
 * @author ChhabrMa
 *
 */
public class MediumApplicationCalculator {

	/**
	 * @param pastDealResults
	 * @param applicationInfo
	 * @param mediumAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public MediumApplicationCalculateDto prepareMediumApplicationCalculateDtoForPastDeal(
			List<ApplicationInfo> pastDealResults, ApplicationInfo applicationInfo,
			MediumApplicationCalculateDto mediumAppsCalculateDto) {

		// Past Deal Medium Application Volume
		setMediumApplicationVolumePrice(applicationInfo, mediumAppsCalculateDto, pastDealResults);
		return mediumAppsCalculateDto;

	}

	/**
	 * @param applicationInfo
	 * @param mediumApplicationCalculateDto
	 * @param dealResults
	 */
	private void setMediumApplicationVolumePrice(ApplicationInfo applicationInfo,
			MediumApplicationCalculateDto mediumApplicationCalculateDto, List<ApplicationInfo> dealResults) {

		// Past Deal Medium Application Volume
		Map<Integer, BigDecimal> mediumAppsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> mediumAppsRevenueMap = getYearlyRevenue(mediumAppsUnitPriceMap, applicationInfo);
		BigDecimal mediumAppsAvgUnitPrice = getConsolidatedAvgUnitPrice(mediumAppsRevenueMap, applicationInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : mediumAppsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = mediumAppsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			mediumApplicationCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		mediumApplicationCalculateDto.setPastDealMediumAppsAvgUnitPrice(mediumAppsAvgUnitPrice);

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
											.add(unitPriceInfo.getMediumAppsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(applicationYearlyDataInfo.getYear(),
									unitPriceInfo.getMediumAppsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getMediumAppsVolume())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param mediumAppsVolumeRevenueMap
	 * @param applicationInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> mediumAppsVolumeRevenueMap,
			ApplicationInfo applicationInfo) {
		BigDecimal mediumAppsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int mediumAppsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : mediumAppsVolumeRevenueMap.entrySet()) {
			mediumAppsRevenue = mediumAppsRevenue.add(entry.getValue());
			mediumAppsVolume = mediumAppsVolume + applicationInfo.getAppYearlyDataInfos().get(i).getMediumAppsVolume();
			i++;
		}
		if(mediumAppsVolume != 0){
			consolidatedAvgUnitPrice = mediumAppsRevenue
					.divide(new BigDecimal(mediumAppsVolume), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param applicationInfo
	 * @param mediumAppsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public MediumApplicationCalculateDto prepareMediumAppsCalculateDtoForBenchmark(
			List<ApplicationInfo> benchmarkDealResults, ApplicationInfo applicationInfo,
			MediumApplicationCalculateDto mediumAppsCalculateDto) {
		BigDecimal mediumYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
			BigDecimal mediumAppsVolume = new BigDecimal(applicationYearlyDataInfo.getMediumAppsVolume());
			mediumYearlySum = mediumYearlySum.add(mediumAppsVolume);
			yearlyVolumeMap.put(applicationYearlyDataInfo.getYear(), mediumAppsVolume);
		}

		// Benchmark Medium Apps Volume
		setBenchmarkMediumAppsVolumePrice(applicationInfo, mediumAppsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, mediumYearlySum);
		return mediumAppsCalculateDto;
	}

	/**
	 * @param applicationInfo
	 * @param mediumAppsCalculateDto
	 * @param benchmarkDealResults
	 * @param yearlyVolumeMap
	 * @param mediumYearlySum
	 */
	private void setBenchmarkMediumAppsVolumePrice(ApplicationInfo applicationInfo,
			MediumApplicationCalculateDto mediumAppsCalculateDto, List<ApplicationInfo> benchmarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal mediumYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal mediumAppsVolumeUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal mediumLowRevenue = new BigDecimal(0);
		BigDecimal mediumTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchmarkDealResults)) {
			ApplicationInfo benchmarkApplicationInfo = benchmarkDealResults.get(0);
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : benchmarkApplicationInfo
					.getAppYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
					for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
							.getAppUnitPriceInfoList()) {
						year = applicationYearlyDataInfo.getYear();
						mediumAppsVolumeUnitPrice = applicationUnitPriceInfo.getMediumAppsUnitPrice();
						revenue = mediumAppsVolumeUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(mediumAppsVolumeUnitPrice.floatValue());
							mediumLowRevenue = mediumLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (applicationUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(applicationUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(mediumAppsVolumeUnitPrice.floatValue());

							mediumTargetRevenue = mediumTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			mediumAppsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			mediumAppsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			mediumAppsCalculateDto.setBenchDealLowMediumAppsAvgUnitPrice(mediumLowRevenue.divide(mediumYearlySum, 2, BigDecimal.ROUND_CEILING));
			mediumAppsCalculateDto.setBenchDealTargetMediumAppsAvgUnitPrice(mediumTargetRevenue.divide(mediumYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
