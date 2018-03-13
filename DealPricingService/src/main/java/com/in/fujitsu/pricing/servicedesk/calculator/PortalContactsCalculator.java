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
public class PortalContactsCalculator {

	/**
	 * @param pastDealResults
	 * @param serviceDeskInfoInfo
	 * @param serviceDeskCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto preparePortalContactsCalculateDtoForPastDeal(List<ServiceDeskInfo> pastDealResults,
			ServiceDeskInfo serviceDeskInfoInfo, ServiceDeskCalculateDto serviceDeskCalculateDto) {

		// Past Deal Portal Application Volume
		setPortalContactsVolumePrice(serviceDeskInfoInfo, serviceDeskCalculateDto, pastDealResults);
		return serviceDeskCalculateDto;

	}

	/**
	 * @param serviceDeskInfo
	 * @param portalContactsCalculateDto
	 * @param dealResults
	 */
	private void setPortalContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto portalContactsCalculateDto, List<ServiceDeskInfo> dealResults) {

		// Past Deal Portal Contacts Volume
		Map<Integer, BigDecimal> portalContactsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> portalContactsRevenueMap = getYearlyRevenue(portalContactsUnitPriceMap,
				serviceDeskInfo);
		BigDecimal portalContactsAvgUnitPrice = getConsolidatedAvgUnitPrice(portalContactsRevenueMap, serviceDeskInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : portalContactsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = portalContactsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			portalContactsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		portalContactsCalculateDto.setPastDealAvgUnitPrice(portalContactsAvgUnitPrice);

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
											.add(unitPriceInfo.getPortalContactsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									unitPriceInfo.getPortalContactsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getPortalContacts())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param portalAppsVolumeRevenueMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> portalAppsVolumeRevenueMap,
			ServiceDeskInfo serviceDeskInfo) {
		BigDecimal portalContactsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int portalContacts = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : portalAppsVolumeRevenueMap.entrySet()) {
			portalContactsRevenue = portalContactsRevenue.add(entry.getValue());
			portalContacts = portalContacts
					+ serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getPortalContacts();
			i++;
		}
		if(portalContacts != 0){
			consolidatedAvgUnitPrice = portalContactsRevenue
					.divide(new BigDecimal(portalContacts), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param serviceDeskInfo
	 * @param portalContactsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto preparePortalContactsCalculateDtoForBenchmark(
			List<ServiceDeskInfo> benchmarkDealResults, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto portalContactsCalculateDto) {

		BigDecimal portalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			BigDecimal portalContacts = new BigDecimal(serviceDeskYearlyDataInfo.getPortalContacts());
			portalYearlySum = portalYearlySum.add(portalContacts);
			yearlyVolumeMap.put(serviceDeskYearlyDataInfo.getYear(), portalContacts);
		}

		// Benchmark Portal Contacts Volume
		setBenchmarkPortalContactsVolumePrice(serviceDeskInfo, portalContactsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, portalYearlySum);
		return portalContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param portalContactsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param portalYearlySum
	 */
	private void setBenchmarkPortalContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto portalContactsCalculateDto, List<ServiceDeskInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal portalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal portalContactsUnitPrice = new BigDecimal(0);
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
						portalContactsUnitPrice = serviceDeskUnitPriceInfo.getPortalContactsUnitPrice();
						revenue = portalContactsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(portalContactsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(portalContactsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			portalContactsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			portalContactsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			portalContactsCalculateDto.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(portalYearlySum, 2, BigDecimal.ROUND_CEILING));
			portalContactsCalculateDto.setBenchDealTargetAvgUnitPrice(totalTargetRevenue.divide(portalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
