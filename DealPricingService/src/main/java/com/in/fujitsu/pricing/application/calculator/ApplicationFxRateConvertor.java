package com.in.fujitsu.pricing.application.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationUnitPriceInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;

/**
 * @author ChhabrMa
 *
 */
public class ApplicationFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<ApplicationInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		for (ApplicationInfo applicationInfo : dealResults) {
			DealInfo dealInfo = applicationInfo.getDealInfo();
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

				for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationInfo.getAppYearlyDataInfos()) {
					if (!CollectionUtils.isEmpty(applicationYearlyDataInfo.getAppUnitPriceInfoList())) {
						for (ApplicationUnitPriceInfo applicationUnitPriceInfo : applicationYearlyDataInfo
								.getAppUnitPriceInfoList()) {
							BigDecimal totalAppsVolumeUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									applicationUnitPriceInfo.getTotalAppsUnitPrice());
							applicationUnitPriceInfo.setTotalAppsUnitPrice(totalAppsVolumeUnitPrice);

							BigDecimal simpleAppsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									applicationUnitPriceInfo.getSimpleAppsUnitPrice());

							applicationUnitPriceInfo.setSimpleAppsUnitPrice(simpleAppsUnitPrice);

							BigDecimal mediumAppsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									applicationUnitPriceInfo.getMediumAppsUnitPrice());

							applicationUnitPriceInfo.setMediumAppsUnitPrice(mediumAppsUnitPrice);

							BigDecimal complexAppsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									applicationUnitPriceInfo.getComplexAppsUnitPrice());

							applicationUnitPriceInfo.setComplexAppsUnitPrice(complexAppsUnitPrice);

							BigDecimal veryComplexAppsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									applicationUnitPriceInfo.getVeryComplexAppsUnitPrice());

							applicationUnitPriceInfo.setVeryComplexAppsUnitPrice(veryComplexAppsUnitPrice);
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
