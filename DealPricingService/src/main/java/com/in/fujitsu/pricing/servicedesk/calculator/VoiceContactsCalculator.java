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
public class VoiceContactsCalculator {

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
	public ServiceDeskCalculateDto prepareVoiceContactsCalculateDtoForPastDeal(List<ServiceDeskInfo> pastDealResults,
			ServiceDeskInfo serviceDeskInfoInfo, ServiceDeskCalculateDto serviceDeskCalculateDto) {

		// Past Deal Voice Application Volume
		setVoiceContactsVolumePrice(serviceDeskInfoInfo, serviceDeskCalculateDto, pastDealResults);
		return serviceDeskCalculateDto;

	}

	/**
	 * @param serviceDeskInfo
	 * @param voiceContactsCalculateDto
	 * @param dealResults
	 */
	private void setVoiceContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto voiceContactsCalculateDto, List<ServiceDeskInfo> dealResults) {

		// Past Deal Voice Contacts Volume
		Map<Integer, BigDecimal> voiceContactsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> voiceContactsRevenueMap = getYearlyRevenue(voiceContactsUnitPriceMap, serviceDeskInfo);
		BigDecimal voiceContactsAvgUnitPrice = getConsolidatedAvgUnitPrice(voiceContactsRevenueMap, serviceDeskInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : voiceContactsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = voiceContactsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			voiceContactsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		voiceContactsCalculateDto.setPastDealAvgUnitPrice(voiceContactsAvgUnitPrice);

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
											.add(unitPriceInfo.getVoiceContactsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									unitPriceInfo.getVoiceContactsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getVoiceContacts())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param voiceAppsVolumeRevenueMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> voiceAppsVolumeRevenueMap,
			ServiceDeskInfo serviceDeskInfo) {
		BigDecimal voiceContactsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int voiceContacts = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : voiceAppsVolumeRevenueMap.entrySet()) {
			voiceContactsRevenue = voiceContactsRevenue.add(entry.getValue());
			voiceContacts = voiceContacts
					+ serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getVoiceContacts();
			i++;
		}
		if(voiceContacts != 0){
			consolidatedAvgUnitPrice = voiceContactsRevenue
					.divide(new BigDecimal(voiceContacts), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param banchmarkDealResults
	 * @param serviceDeskInfo
	 * @param voiceContactsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto prepareVoiceContactsCalculateDtoForBenchmark(
			List<ServiceDeskInfo> banchmarkDealResults, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto voiceContactsCalculateDto) {

		BigDecimal voiceYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			BigDecimal voiceContacts = new BigDecimal(serviceDeskYearlyDataInfo.getVoiceContacts());
			voiceYearlySum = voiceYearlySum.add(voiceContacts);
			yearlyVolumeMap.put(serviceDeskYearlyDataInfo.getYear(), voiceContacts);
		}

		// Benchmark Voice Contacts Volume
		setBenchmarkVoiceContactsVolumePrice(serviceDeskInfo, voiceContactsCalculateDto, banchmarkDealResults,
				yearlyVolumeMap, voiceYearlySum);
		return voiceContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param voiceContactsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param voiceYearlySum
	 */
	private void setBenchmarkVoiceContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto voiceContactsCalculateDto, List<ServiceDeskInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal voiceYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal voiceContactsUnitPrice = new BigDecimal(0);
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
						voiceContactsUnitPrice = serviceDeskUnitPriceInfo.getVoiceContactsUnitPrice();
						revenue = voiceContactsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(voiceContactsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(voiceContactsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			voiceContactsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			voiceContactsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			voiceContactsCalculateDto.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(voiceYearlySum, 2, BigDecimal.ROUND_CEILING));
			voiceContactsCalculateDto.setBenchDealTargetAvgUnitPrice(totalTargetRevenue.divide(voiceYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
