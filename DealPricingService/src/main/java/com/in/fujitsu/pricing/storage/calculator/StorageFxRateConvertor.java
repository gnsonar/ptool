package com.in.fujitsu.pricing.storage.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageUnitPriceInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;

public class StorageFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<StorageInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, String level) {
		for (StorageInfo storageInfo : dealResults) {
			DealInfo dealInfo = storageInfo.getDealInfo();
			String dealCountry = dealInfo.getCountry();
			if (referenceCountry != null && dealCountry != null && !referenceCountry.equals(dealCountry)) {
				BigDecimal fxRate = new BigDecimal(1);
				BigDecimal dealCountryFactor = new BigDecimal(1);

				String dealCurrency = dealInfo.getCurrency();
				for (DealFXRatesInfo dealFXRatesInfo : dealInfo.getDealFxRates()) {
					if (dealCurrency.equalsIgnoreCase(dealFXRatesInfo.getCurrencyFrom())
							&& referenceCurrency.equals(dealFXRatesInfo.getCurrencyTo())) {
						fxRate = dealFXRatesInfo.getRate();
						break;
					}
				}

				for (CountryFactorInfo countryFactorInfo : countrySpecificFactors) {
					if (dealCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						dealCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}

				for (StorageYearlyDataInfo yearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getStorageUnitPriceInfo())) {
						for (StorageUnitPriceInfo unitPriceInfo : yearlyDataInfo
								.getStorageUnitPriceInfo()) {
							switch (level) {
								case "STORAGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getStorageVolumeUnitPrice());
									unitPriceInfo.setStorageVolumeUnitPrice(fxUnitPrice);
									break;
								}

								case "PERFORMANCE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPerformanceUnitPrice());
									unitPriceInfo.setPerformanceUnitPrice(fxUnitPrice);
									break;
								}

								case "NON_PERFORMANCE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getNonPerformanceUnitPrice());
									unitPriceInfo.setNonPerformanceUnitPrice(fxUnitPrice);
									break;
								}

								case "BACKUP": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getBackupVolumeUnitPrice());
									unitPriceInfo.setBackupVolumeUnitPrice(fxUnitPrice);
									break;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @param referenceCountryFactor
	 * @param dealCountryFactor
	 * @param fxRate
	 * @param unitPrice
	 * @return
	 */
	private BigDecimal performCountryFactorFXRatesConversion(BigDecimal referenceCountryFactor,
			BigDecimal dealCountryFactor, BigDecimal fxRate, BigDecimal unitPrice) {
		unitPrice = unitPrice.divide(dealCountryFactor, 2, RoundingMode.HALF_UP);
		unitPrice = unitPrice.multiply(referenceCountryFactor);
		return unitPrice.multiply(fxRate);
	}


}
