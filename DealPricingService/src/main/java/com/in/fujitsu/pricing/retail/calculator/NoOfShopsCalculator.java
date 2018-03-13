package com.in.fujitsu.pricing.retail.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;
import com.in.fujitsu.pricing.retail.dto.RetailCalculateDto;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailUnitPriceInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;

/**
 * @author MishraSub
 *
 */
public class NoOfShopsCalculator {

	/**
	 * @param pastDealResults
	 * @param retailInfo
	 * @param retailCalculateDto
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @return
	 */
	public RetailCalculateDto prepareNoOfShopsCalculateDtoForPastDeal(List<RetailInfo> pastDealResults,
			RetailInfo retailInfo, RetailCalculateDto retailCalculateDto) {
		// Past Deal No Of shops Volume
		setNoOfShopsPrice(retailInfo, retailCalculateDto, pastDealResults);
		return retailCalculateDto;

	}

	/**
	 * @param retailInfo
	 * @param retailCalculateDto
	 * @param dealResults
	 */
	private void setNoOfShopsPrice(RetailInfo retailInfo, RetailCalculateDto retailCalculateDto,
			List<RetailInfo> dealResults) {

		// Past Deal
		Map<Integer, BigDecimal> noOfShopsUnitPriceMap = getYearlyUnitPrice(dealResults);
		Map<Integer, BigDecimal> noOfShopsRevenueMap = getYearlyRevenue(noOfShopsUnitPriceMap, retailInfo);
		BigDecimal noOfShopsAvgUnitPrice = getConsolidatedAvgUnitPrice(noOfShopsRevenueMap, retailInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : noOfShopsUnitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = noOfShopsRevenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			retailCalculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		retailCalculateDto.setPastDealAvgUnitPrice(noOfShopsAvgUnitPrice);

	}

	/**
	 * @param dealResults
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyUnitPrice(List<RetailInfo> dealResults) {
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
	public void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap, List<RetailInfo> dealResults) {
		for (RetailInfo retailInfo : dealResults) {
			if (!CollectionUtils.isEmpty(retailInfo.getRetailYearlyDataInfoList())) {
				for (RetailYearlyDataInfo retailYearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailUnitPriceInfoList())
							&& retailYearlyDataInfo.getRetailUnitPriceInfoList().get(0) != null) {
						RetailUnitPriceInfo unitPriceInfo = retailYearlyDataInfo.getRetailUnitPriceInfoList().get(0);
						if (yearlyUnitPriceMap.containsKey(retailYearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(retailYearlyDataInfo.getYear(), yearlyUnitPriceMap
									.get(retailYearlyDataInfo.getYear()).add(unitPriceInfo.getNoOfShops()));
						} else {
							yearlyUnitPriceMap.put(retailYearlyDataInfo.getYear(), unitPriceInfo.getNoOfShops());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param retailInfo
	 * @return
	 */
	public Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			RetailInfo retailInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (RetailYearlyDataInfo yearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getNoOfShops())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param noOfShopsRevenueMap
	 * @param retailInfo
	 * @return
	 */
	public BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> noOfShopsRevenueMap, RetailInfo retailInfo) {
		BigDecimal noOfShopsRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);	
		int noOfShopsVolume = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : noOfShopsRevenueMap.entrySet()) {
			noOfShopsRevenue = noOfShopsRevenue.add(entry.getValue());
			noOfShopsVolume = noOfShopsVolume + retailInfo.getRetailYearlyDataInfoList().get(i).getNoOfShops();
			i++;
		}
		if(noOfShopsVolume != 0){
			consolidatedAvgUnitPrice = noOfShopsRevenue
					.divide(new BigDecimal(noOfShopsVolume), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}

	/**
	 * @param benchMarkDealResults
	 * @param retailInfo
	 * @param noOfShopsCalculateDto
	 * @return
	 */
	public RetailCalculateDto prepareNoOfShopsCalculateDtoForBenchmark(List<RetailInfo> benchMarkDealResults,
			RetailInfo retailInfo, RetailCalculateDto noOfShopsCalculateDto) {

		BigDecimal noOfShopsYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (RetailYearlyDataInfo retailYearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
			BigDecimal noOfShops = new BigDecimal(retailYearlyDataInfo.getNoOfShops());
			noOfShopsYearlySum = noOfShopsYearlySum.add(noOfShops);
			yearlyVolumeMap.put(retailYearlyDataInfo.getYear(), noOfShops);
		}

		// Benchmark No Of Shops
		setBenchmarkNoOfShopsPrice(retailInfo, noOfShopsCalculateDto, benchMarkDealResults, yearlyVolumeMap,
				noOfShopsYearlySum);
		return noOfShopsCalculateDto;
	}

	/**
	 * @param retailInfo
	 * @param noOfShopsCalculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param noOfShopsYearlySum
	 */
	private void setBenchmarkNoOfShopsPrice(RetailInfo retailInfo, RetailCalculateDto noOfShopsCalculateDto,
			List<RetailInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal noOfShopsYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal noOfShopsUnitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal noOfShopsLowRevenue = new BigDecimal(0);
		BigDecimal noOfShopsTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			RetailInfo benchmarkRetailInfo = benchMarkDealResults.get(0);
			for (RetailYearlyDataInfo retailYearlyDataInfo : benchmarkRetailInfo.getRetailYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailUnitPriceInfoList())) {
					for (RetailUnitPriceInfo retailUnitPriceInfo : retailYearlyDataInfo.getRetailUnitPriceInfoList()) {
						year = retailYearlyDataInfo.getYear();
						noOfShopsUnitPrice = retailUnitPriceInfo.getNoOfShops();
						revenue = noOfShopsUnitPrice.multiply(yearlyVolumeMap.get(year));
						if (retailUnitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(retailUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(noOfShopsUnitPrice.floatValue());
							noOfShopsLowRevenue = noOfShopsLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (retailUnitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(retailUnitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(noOfShopsUnitPrice.floatValue());

							noOfShopsTargetRevenue = noOfShopsTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}

					}
				}
			}

			noOfShopsCalculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			noOfShopsCalculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			noOfShopsCalculateDto.setBenchDealLowAvgUnitPrice(
					noOfShopsLowRevenue.divide(noOfShopsYearlySum, 2, BigDecimal.ROUND_CEILING));
			noOfShopsCalculateDto.setBenchDealTargetAvgUnitPrice(
					noOfShopsTargetRevenue.divide(noOfShopsYearlySum, 2, BigDecimal.ROUND_CEILING));
		}

	}

}
