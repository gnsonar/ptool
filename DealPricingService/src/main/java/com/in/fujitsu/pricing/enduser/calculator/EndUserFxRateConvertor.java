package com.in.fujitsu.pricing.enduser.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserUnitPriceInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;


/**
 * @author pawarbh
 *
 */
public class EndUserFxRateConvertor {
	

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<EndUserInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, String level) {
		for (EndUserInfo endUserInfo : dealResults) {
			DealInfo dealInfo = endUserInfo.getDealInfo();
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

				for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserUnitPriceInfoList())) {
						for (EndUserUnitPriceInfo unitPriceInfo : yearlyDataInfo.getEndUserUnitPriceInfoList()) {
							switch (level) {
								case "END_USER": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getEndUserDevices());
									unitPriceInfo.setEndUserDevices(unitPrice);;
									break;
								}
								case "LAPTOP": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getLaptops());
									unitPriceInfo.setLaptops(unitPrice);
									break;
								}
								case "HIGH_END_LAPTOP": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getHighEndLaptops());
									unitPriceInfo.setHighEndLaptops(unitPrice);
									break;
								}
								case "STANDARD_LAPTOP": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getStandardLaptops());
									unitPriceInfo.setStandardLaptops(unitPrice);
									break;
								}
								case "DESKTOP": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getDesktops());
									unitPriceInfo.setDesktops(unitPrice);
									break;
								}
								case "THIN_CLIENT": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getThinClients());
									unitPriceInfo.setThinClients(unitPrice);
									break;
								}
								case "MOBILE": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getMobileDevices());
									unitPriceInfo.setMobileDevices(unitPrice);
									break;
								}
								case "IMAC": {
									BigDecimal unitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getImacDevices());
									unitPriceInfo.setImacDevices(unitPrice);
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
