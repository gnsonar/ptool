package com.in.fujitsu.pricing.retail.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.retail.calculator.RetailFxRateConvertor;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;

/**
 * @author MishraSub
 *
 */
@Component
public class RetailCommonHelper {

	/**
	 * @param retailYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getNoOfShopsAverageVolume(List<RetailYearlyDataInfo> retailYearlyDataInfoList) {
		BigDecimal avgNoOfShops = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(retailYearlyDataInfoList)) {
			int noOfShops = 0;
			int size = 0;
			for (RetailYearlyDataInfo retailYearlyDataInfo : retailYearlyDataInfoList) {
				if (retailYearlyDataInfo.getNoOfShops() != 0) {
					noOfShops += retailYearlyDataInfo.getNoOfShops();
					size++;
				}
			}
			if (size != 0) {
				avgNoOfShops = new BigDecimal(noOfShops / size);
			}
		}
		return avgNoOfShops;
	}

	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<RetailInfo> dealResults) {
		for (RetailInfo retailInfo : dealResults) {
			Integer dealTerm = retailInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = retailInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<RetailYearlyDataInfo> retailYearlyDataInfoList = retailInfo.getRetailYearlyDataInfoList();
				int size = retailYearlyDataInfoList.size();
				RetailYearlyDataInfo retailYearlyDataInfo = retailYearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					RetailYearlyDataInfo cloneRetailYearlyDataInfo = (RetailYearlyDataInfo) retailYearlyDataInfo
							.clone();
					cloneRetailYearlyDataInfo.setYear(size + i);
					retailYearlyDataInfoList.add(cloneRetailYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<RetailYearlyDataInfo> retailYearlyDataInfoList = retailInfo.getRetailYearlyDataInfoList();
				int size = retailYearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					retailYearlyDataInfoList.remove(i);
				}
			}
		}
	}

	/**
	 * @param dealResults
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void applyFxRatesAndCountryFactor(List<RetailInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor) {
		// currency conversion based on the FX Rates and country factor
		RetailFxRateConvertor fxRateConvertor = new RetailFxRateConvertor();

		fxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency, referenceCountry,
				referenceCountryFactor);

	}

	/**
	 * @param assessmentAvgVolume
	 * @param yearlyAvgUnitPriceMap
	 * @return
	 */
	public Map<Long, BigDecimal> prepareDealAbsVolumeDiff(BigDecimal assessmentAvgVolume,
			Map<Long, BigDecimal> yearlyAvgUnitPriceMap) {
		Map<Long, BigDecimal> differencePercentageMap = new HashMap<>();
		for (Map.Entry<Long, BigDecimal> entry : yearlyAvgUnitPriceMap.entrySet()) {
			BigDecimal difference = assessmentAvgVolume.subtract(entry.getValue());
			BigDecimal differencePercentage = difference.divide(assessmentAvgVolume, 2, BigDecimal.ROUND_CEILING)
					.multiply(new BigDecimal(100));
			BigDecimal absoluteDifference = differencePercentage.abs();
			differencePercentageMap.put(entry.getKey(), absoluteDifference);
		}
		return differencePercentageMap;
	}

}
