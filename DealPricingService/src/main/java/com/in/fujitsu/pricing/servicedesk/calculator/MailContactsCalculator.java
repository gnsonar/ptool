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
public class MailContactsCalculator {

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
	public ServiceDeskCalculateDto prepareMailContactsCalculateDtoForPastDeal(List<ServiceDeskInfo> pastDealResults,
			ServiceDeskInfo serviceDeskInfoInfo, ServiceDeskCalculateDto serviceDeskCalculateDto) {

		// Past Deal Mail Application Volume
		setMailContactsVolumePrice(serviceDeskInfoInfo, serviceDeskCalculateDto, pastDealResults);
		return serviceDeskCalculateDto;

	}

	/**
	 * @param serviceDeskInfo
	 * @param mailContactsCalculateDto
	 * @param dealResults
	 */
	private void setMailContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto mailContactsCalculateDto, List<ServiceDeskInfo> dealResults) {

		// Past Deal Mail Contacts Volume
		Map<Integer, BigDecimal> mailContactsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> mailContactsRevenueMap = getYearlyRevenue(mailContactsUnitPriceMap, serviceDeskInfo);
		BigDecimal mailContactsAvgUnitPrice = getConsolidatedAvgUnitPrice(mailContactsRevenueMap, serviceDeskInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : mailContactsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = mailContactsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			mailContactsCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		mailContactsCalculateDto.setPastDealAvgUnitPrice(mailContactsAvgUnitPrice);

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
											.add(unitPriceInfo.getMailContactsUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(serviceDeskYearlyDataInfo.getYear(),
									unitPriceInfo.getMailContactsUnitPrice());
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
					.multiply(new BigDecimal(yearlyDataInfo.getMailContacts())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param mailAppsVolumeRevenueMap
	 * @param serviceDeskInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> mailAppsVolumeRevenueMap,
			ServiceDeskInfo serviceDeskInfo) {
		BigDecimal mailContactsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int mailContacts = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : mailAppsVolumeRevenueMap.entrySet()) {
			mailContactsRevenue = mailContactsRevenue.add(entry.getValue());
			mailContacts = mailContacts + serviceDeskInfo.getServiceDeskYearlyDataInfoList().get(i).getMailContacts();
			i++;
		}
		if(mailContacts != 0){
			consolidatedAvgUnitPrice = mailContactsRevenue
					.divide(new BigDecimal(mailContacts), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchmarkDealResults
	 * @param serviceDeskInfo
	 * @param mailContactsCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public ServiceDeskCalculateDto prepareMailContactsCalculateDtoForBenchmark(
			List<ServiceDeskInfo> benchmarkDealResults, ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto mailContactsCalculateDto) {

		BigDecimal mailYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo.getServiceDeskYearlyDataInfoList()) {
			BigDecimal mailContacts = new BigDecimal(serviceDeskYearlyDataInfo.getMailContacts());
			mailYearlySum = mailYearlySum.add(mailContacts);
			yearlyVolumeMap.put(serviceDeskYearlyDataInfo.getYear(), mailContacts);
		}

		// Benchmark Mail Contacts Volume
		setBenchmarkMailContactsVolumePrice(serviceDeskInfo, mailContactsCalculateDto, benchmarkDealResults,
				yearlyVolumeMap, mailYearlySum);
		return mailContactsCalculateDto;
	}

	/**
	 * @param serviceDeskInfo
	 * @param mailContactsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param mailYearlySum
	 */
	private void setBenchmarkMailContactsVolumePrice(ServiceDeskInfo serviceDeskInfo,
			ServiceDeskCalculateDto mailContactsCalculateDto, List<ServiceDeskInfo> benchMarkDealResults,
			Map<Integer, BigDecimal> yearlyVolumeMap, BigDecimal mailYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal mailContactsUnitPrice = new BigDecimal(0);
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
						mailContactsUnitPrice = serviceDeskUnitPriceInfo.getMailContactsUnitPrice();
						revenue = mailContactsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(mailContactsUnitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (serviceDeskUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(serviceDeskUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(mailContactsUnitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			mailContactsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			mailContactsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			mailContactsCalculateDto.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(mailYearlySum, 2, BigDecimal.ROUND_CEILING));
			mailContactsCalculateDto.setBenchDealTargetAvgUnitPrice(totalTargetRevenue.divide(mailYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
