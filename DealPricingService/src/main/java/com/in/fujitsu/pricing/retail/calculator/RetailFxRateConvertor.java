package com.in.fujitsu.pricing.retail.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailUnitPriceInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;

/**
 * @author MishraSub
 *
 */
public class RetailFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<RetailInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		for (RetailInfo retailInfo : dealResults) {
			DealInfo dealInfo = retailInfo.getDealInfo();
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

				for (RetailYearlyDataInfo retailYearlyDataInfo : retailInfo.getRetailYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(retailYearlyDataInfo.getRetailUnitPriceInfoList())) {
						for (RetailUnitPriceInfo retailUnitPriceInfo : retailYearlyDataInfo.getRetailUnitPriceInfoList()) {
							BigDecimal noOfShopsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate, retailUnitPriceInfo.getNoOfShops());
							retailUnitPriceInfo.setNoOfShops(noOfShopsUnitPrice);
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
