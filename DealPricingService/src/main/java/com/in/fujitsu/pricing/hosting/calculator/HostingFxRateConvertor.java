package com.in.fujitsu.pricing.hosting.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingUnitPriceInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;


/**
 * @author pawarbh
 *
 */
public class HostingFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<HostingInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, String level) {
		for (HostingInfo hostingInfo : dealResults) {
			DealInfo dealInfo = hostingInfo.getDealInfo();
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

				for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getHostingUnitPriceInfoList())) {
						for (HostingUnitPriceInfo unitPriceInfo : yearlyDataInfo
								.getHostingUnitPriceInfoList()) {
							switch (level) {
								case "SERVERS": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getServers());
									unitPriceInfo.setServers(fxUnitPrice);
									break;
								}

								case "PHYSICAL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysical());
									unitPriceInfo.setPhysical(fxUnitPrice);
									break;
								}

								case "PHYSICAL_WIN": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalWin());
									unitPriceInfo.setPhysicalWin(fxUnitPrice);
									break;
								}

								case "PHYSICAL_WIN_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalWinSmall());
									unitPriceInfo.setPhysicalWinSmall(fxUnitPrice);
									break;
								}

								case "PHYSICAL_WIN_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalWinMedium());
									unitPriceInfo.setPhysicalWinMedium(fxUnitPrice);
									break;
								}

								case "PHYSICAL_WIN_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalWinLarge());
									unitPriceInfo.setPhysicalWinLarge(fxUnitPrice);
									break;
								}
								case "PHYSICAL_UNIX": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalUnix());
									unitPriceInfo.setPhysicalUnix(fxUnitPrice);
									break;
								}
								case "PHYSICAL_UNIX_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalUnixSmall());
									unitPriceInfo.setPhysicalUnixSmall(fxUnitPrice);
									break;
								}
								case "PHYSICAL_UNIX_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalUnixMedium());
									unitPriceInfo.setPhysicalUnixMedium(fxUnitPrice);
									break;
								}
								case "PHYSICAL_UNIX_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getPhysicalUnixLarge());
									unitPriceInfo.setPhysicalUnixLarge(fxUnitPrice);
									break;
								}
								case "VIRTUAL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtual());
									unitPriceInfo.setVirtual(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublic());
									unitPriceInfo.setVirtualPublic(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_WIN": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicWin());
									unitPriceInfo.setVirtualPublicWin(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_WIN_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicWinSmall());
									unitPriceInfo.setVirtualPublicWinSmall(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_WIN_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicWinMedium());
									unitPriceInfo.setVirtualPublicWinMedium(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_WIN_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicWinLarge());
									unitPriceInfo.setVirtualPublicWinLarge(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_UNIX": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicUnix());
									unitPriceInfo.setVirtualPublicUnix(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_UNIX_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicUnixSmall());
									unitPriceInfo.setVirtualPublicUnixSmall(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_UNIX_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicUnixMedium());
									unitPriceInfo.setVirtualPublicUnixMedium(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PUBLIC_UNIX_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPublicUnixLarge());
									unitPriceInfo.setVirtualPublicUnixLarge(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivate());
									unitPriceInfo.setVirtualPrivate(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_WIN": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateWin());
									unitPriceInfo.setVirtualPrivateWin(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_WIN_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateWinSmall());
									unitPriceInfo.setVirtualPrivateWinSmall(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_WIN_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateWinMedium());
									unitPriceInfo.setVirtualPrivateWinMedium(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_WIN_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateWinLarge());
									unitPriceInfo.setVirtualPrivateWinLarge(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_UNIX": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateUnix());
									unitPriceInfo.setVirtualPrivateUnix(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_UNIX_SMALL": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateUnixSmall());
									unitPriceInfo.setVirtualPrivateUnixSmall(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_UNIX_MEDIUM": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateUnixMedium());
									unitPriceInfo.setVirtualPrivateUnixMedium(fxUnitPrice);
									break;
								}
								case "VIRTUAL_PRIVATE_UNIX_LARGE": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getVirtualPrivateUnixLarge());
									unitPriceInfo.setVirtualPrivateUnixLarge(fxUnitPrice);
									break;
								}
								case "SQL_INSTANCES": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getSqlInstances());
									unitPriceInfo.setSqlInstances(fxUnitPrice);
									break;
								}
								case "COTS_INSTALLATIONS": {
									BigDecimal fxUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											unitPriceInfo.getCotsInstallations());
									unitPriceInfo.setCotsInstallations(fxUnitPrice);
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
