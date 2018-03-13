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
public class ChatContactsCalculator {

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
	public ServiceDeskCalculateDto prepareChatContactsCalculateDtoForPastDeal(List<ServiceDeskInfo> pastDealResults,
			ServiceDeskInfo serviceDeskInfoInfo, ServiceDeskCalculateDto serviceDeskCalculateDto) {

		// Past Deal Chat Application Volume
		setChatContactsVolumePrice(serviceDeskInfoInfo, serviceDeskCalculateDto, pastDealResults);
		return serviceDeskCalculateDto;

	}

	/**
	 * @param serviceDeskInfo
	 * @param chatContactsCalculateDto
	 * @param dealResults
	 */
	private void setChatContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto chatContactsCalculateDto, List<ServiceDeskInfo> dealResults) {

		// Past Deal Chat Contacts Volume
		Map<Integer, BigDecimal> chatContactsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> chatContactsRevenueMap = getYearlyRevenue(chatContactsUnitPriceMap, serviceDeskInfo);
		BigDecimal chatContactsAvgUnitPrice = getConsolidatedAvgUnitPrice(chatContactsRevenueMap, serviceDeskInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : chatContactsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = chatContactsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			chatContactsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		chatContactsCalculateDto.setPastDealAvgUnitPrice(chatContactsAvgUnitPrice);

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
											.add(unitPriceInfo.getChatContactsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									unitPriceInfo.getChatContactsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getChatContacts())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param chatAppsVolumeRevenueMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> chatAppsVolumeRevenueMap,
			ServiceDeskInfo serviceDeskInfo) {
		BigDecimal chatContactsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int chatContacts = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : chatAppsVolumeRevenueMap.entrySet()) {
			chatContactsRevenue = chatContactsRevenue.add(entry.getValue());
			chatContacts = chatContacts + serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getChatContacts();
			i++;
		}
		if(chatContacts != 0){
			consolidatedAvgUnitPrice = chatContactsRevenue
					.divide(new BigDecimal(chatContacts), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param serviceDeskInfo
	 * @param chatContactsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto prepareChatContactsCalculateDtoForBenchmark(
			List<ServiceDeskInfo> benchmarkDealResults, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto chatContactsCalculateDto) {
		BigDecimal chatYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			BigDecimal chatContacts = new BigDecimal(serviceDeskYearlyDataInfo.getChatContacts());
			chatYearlySum = chatYearlySum.add(chatContacts);
			yearlyVolumeMap.put(serviceDeskYearlyDataInfo.getYear(), chatContacts);
		}

		// Benchmark Chat Contacts Volume
		setBenchmarkChatContactsVolumePrice(serviceDeskInfo, chatContactsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, chatYearlySum);
		return chatContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param chatContactsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param chatYearlySum
	 */
	private void setBenchmarkChatContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto chatContactsCalculateDto, List<ServiceDeskInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal chatYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal chatContactsUnitPrice = new BigDecimal(0);
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
						chatContactsUnitPrice = serviceDeskUnitPriceInfo.getChatContactsUnitPrice();
						revenue = chatContactsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(chatContactsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(chatContactsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			chatContactsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			chatContactsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			chatContactsCalculateDto.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(chatYearlySum, 2, BigDecimal.ROUND_CEILING));
			chatContactsCalculateDto.setBenchDealTargetAvgUnitPrice(totalTargetRevenue.divide(chatYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
