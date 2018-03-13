package com.in.fujitsu.pricing.servicedesk.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskCalculateDto;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskUnitPriceInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class TotalContactsCalculator {

	/**
	 * @param pastDealResults
	 * @param serviceDeskInfo
	 * @param serviceDeskCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto prepareTotalContactsCalculateDtoForPastDeal(List<ServiceDeskInfo> pastDealResults,
			ServiceDeskInfo serviceDeskInfo, ServiceDeskCalculateDto serviceDeskCalculateDto) {
		// Past Deal Total Application Volume
		setTotalContactsVolumePrice(serviceDeskInfo, serviceDeskCalculateDto, pastDealResults);
		return serviceDeskCalculateDto;

	}

	/**
	 * @param serviceDeskInfo
	 * @param totalContactsCalculateDto
	 * @param dealResults
	 */
	private void setTotalContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto totalContactsCalculateDto, List<ServiceDeskInfo> dealResults) {

		// Past Deal Total Contacts Volume
		Map<Integer, BigDecimal> totalContactsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> totalContactsRevenueMap = getYearlyRevenue(totalContactsUnitPriceMap, serviceDeskInfo);
		BigDecimal totalContactsAvgUnitPrice = getConsolidatedAvgUnitPrice(totalContactsRevenueMap, serviceDeskInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : totalContactsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = totalContactsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			totalContactsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		totalContactsCalculateDto.setPastDealAvgUnitPrice(totalContactsAvgUnitPrice);

	}

	/**
	 * @param dealResults
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyUnitPrice(List<ServiceDeskInfo> dealResults) {
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
			List<ServiceDeskInfo> dealResults) {
		for (ServiceDeskInfo serviceDeskInfo : dealResults) {
			if (!CollectionUtils.isEmpty(serviceDeskInfo.getServiceDeskYearlyDataInfoList())) {
				for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo
						.getServiceDeskYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList())
							&& serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList().get(0) != null) {
						ServiceDeskUnitPriceInfo unitPriceInfo = serviceDeskYearlyDataInfo
								.getServiceDeskUnitPriceInfoList().get(0);
						if (yearlyUnitPriceMap.containsKey(serviceDeskYearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									yearlyUnitPriceMap.get(serviceDeskYearlyDataInfo.getYear())
											.add(unitPriceInfo.getTotalContactsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									unitPriceInfo.getTotalContactsUnitPrice());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			ServiceDeskInfo serviceDeskInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo yearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getTotalContacts())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param totalAppsVolumeRevenueMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> totalAppsVolumeRevenueMap,
			ServiceDeskInfo serviceDeskInfo) {
		BigDecimal totalContactsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int totalContacts = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : totalAppsVolumeRevenueMap.entrySet()) {
			totalContactsRevenue = totalContactsRevenue.add(entry.getValue());
			totalContacts = totalContacts
					+ serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getTotalContacts();
			i++;
		}
		if(totalContacts != 0){
			consolidatedAvgUnitPrice = totalContactsRevenue
					.divide(new BigDecimal(totalContacts), 2, BigDecimal.ROUND_CEILING);
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
	/**
	 * @param benchMarkDealResults
	 * @param serviceDeskInfo
	 * @param totalContactsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto prepareTotalContactsCalculateDtoForBenchmark(
			List<ServiceDeskInfo> benchMarkDealResults, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto totalContactsCalculateDto) {

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			BigDecimal totalContacts = new BigDecimal(serviceDeskYearlyDataInfo.getTotalContacts());
			totalYearlySum = totalYearlySum.add(totalContacts);
			yearlyVolumeMap.put(serviceDeskYearlyDataInfo.getYear(), totalContacts);
		}

		// Benchmark Total Contacts Volume
		setBenchmarkTotalContactsVolumePrice(serviceDeskInfo, totalContactsCalculateDto, benchMarkDealResults,
				yearlyVolumeMap, totalYearlySum);
		return totalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param totalContactsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkTotalContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto totalContactsCalculateDto, List<ServiceDeskInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal totalContactsUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			ServiceDeskInfo benchmarkApplicationInfo = benchMarkDealResults.get(0);
			for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : benchmarkApplicationInfo
					.getServiceDeskYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList())) {
					for (ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : serviceDeskYearlyDataInfo
							.getServiceDeskUnitPriceInfoList()) {
						year = serviceDeskYearlyDataInfo.getYear();
						totalContactsUnitPrice = serviceDeskUnitPriceInfo.getTotalContactsUnitPrice();
						revenue = totalContactsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(totalContactsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(totalContactsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			totalContactsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			totalContactsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			totalContactsCalculateDto.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			totalContactsCalculateDto.setBenchDealTargetAvgUnitPrice(totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
