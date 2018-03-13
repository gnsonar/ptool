package com.in.fujitsu.pricing.servicedesk.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskUnitPriceInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class ServiceDeskFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<ServiceDeskInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		for (ServiceDeskInfo serviceDeskInfo : dealResults) {
			DealInfo dealInfo = serviceDeskInfo.getDealInfo();
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

				for (ServiceDeskYearlyDataInfo serviceDeskYearlyDataInfo : serviceDeskInfo
						.getServiceDeskYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(serviceDeskYearlyDataInfo.getServiceDeskUnitPriceInfoList())) {
						for ( ServiceDeskUnitPriceInfo serviceDeskUnitPriceInfo : serviceDeskYearlyDataInfo
								.getServiceDeskUnitPriceInfoList()) {
							BigDecimal totalContactsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									serviceDeskUnitPriceInfo.getTotalContactsUnitPrice());
							serviceDeskUnitPriceInfo.setTotalContactsUnitPrice(totalContactsUnitPrice);

							BigDecimal voiceContactsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									serviceDeskUnitPriceInfo.getTotalContactsUnitPrice());

							serviceDeskUnitPriceInfo.setVoiceContactsUnitPrice(voiceContactsUnitPrice);

							BigDecimal mailContactsUnitPrice = performCountryFactorFXRatesConversion(referenceCountryFactor,
									dealCountryFactor, fxRate, serviceDeskUnitPriceInfo.getMailContactsUnitPrice());

							serviceDeskUnitPriceInfo.setMailContactsUnitPrice(mailContactsUnitPrice);

							BigDecimal chatContactssUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									serviceDeskUnitPriceInfo.getChatContactsUnitPrice());

							serviceDeskUnitPriceInfo.setChatContactsUnitPrice(chatContactssUnitPrice);

							BigDecimal portalContactsUnitPrice = performCountryFactorFXRatesConversion(
									referenceCountryFactor, dealCountryFactor, fxRate,
									serviceDeskUnitPriceInfo.getPortalContactsUnitPrice());

							serviceDeskUnitPriceInfo.setPortalContactsUnitPrice(portalContactsUnitPrice);
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
