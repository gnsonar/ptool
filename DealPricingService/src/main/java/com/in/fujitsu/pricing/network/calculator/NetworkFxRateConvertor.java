package com.in.fujitsu.pricing.network.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkUnitPriceInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class NetworkFxRateConvertor {

	/**
	 * @param dealResults
	 * @param countrySpecificFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 */
	public void calculateUnitPriceFromFXRates(List<NetworkInfo> dealResults,
			List<CountryFactorInfo> countrySpecificFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor, String level) {
		for (NetworkInfo networkInfo : dealResults) {
			DealInfo dealInfo = networkInfo.getDealInfo();
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

				for (NetworkYearlyDataInfo networkYearlyDataInfo : networkInfo.getNetworkYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(networkYearlyDataInfo.getNetworkUnitPriceInfoList())) {
						for (NetworkUnitPriceInfo networkUnitPriceInfo : networkYearlyDataInfo
								.getNetworkUnitPriceInfoList()) {
							switch (level) {
								case "WAN_DEVICE": {
									BigDecimal wanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getWanDevices());
									networkUnitPriceInfo.setWanDevices(wanDevicesUnitPrice);
									break;
								}

								case "SMALL_WAN_DEVICE": {
									BigDecimal smallWanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getSmallWanDevices());
									networkUnitPriceInfo.setSmallWanDevices(smallWanDevicesUnitPrice);
									break;
								}

								case "MEDIUM_WAN_DEVICE": {
									BigDecimal mediumWanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getMediumWanDevices());
									networkUnitPriceInfo.setMediumWanDevices(mediumWanDevicesUnitPrice);
									break;
								}

								case "LARGE_WAN_DEVICE": {
									BigDecimal largeWanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getLargeWanDevices());
									networkUnitPriceInfo.setLargeWanDevices(largeWanDevicesUnitPrice);
									break;
								}

								case "LAN_DEVICE": {
									BigDecimal lanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getLanDevices());
									networkUnitPriceInfo.setLanDevices(lanDevicesUnitPrice);
									break;
								}

								case "SMALL_LAN_DEVICE": {
									BigDecimal smallLanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getSmallLanDevices());

									networkUnitPriceInfo.setSmallLanDevices(smallLanDevicesUnitPrice);
									break;
								}

								case "MEDIUM_LAN_DEVICE": {
									BigDecimal mediumLanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getMediumLanDevices());

									networkUnitPriceInfo.setMediumLanDevices(mediumLanDevicesUnitPrice);
									break;
								}

								case "LARGE_LAN_DEVICE": {
									BigDecimal largeLanDevicesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getLargeLanDevices());

									networkUnitPriceInfo.setLargeLanDevices(largeLanDevicesUnitPrice);
									break;
								}

								case "WLAN_CONTROLLER": {
									BigDecimal wlanControllerUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getWlanControllers());

									networkUnitPriceInfo.setWlanControllers(wlanControllerUnitPrice);
									break;
								}

								case "WLAN_ACCESS_POINTS": {
									BigDecimal wlanAccessPointsUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getWlanAccesspoint());

									networkUnitPriceInfo.setWlanAccesspoint(wlanAccessPointsUnitPrice);
									break;
								}

								case "LOAD_BALANCER": {
									BigDecimal loadBalancersUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getLoadBalancers());

									networkUnitPriceInfo.setLoadBalancers(loadBalancersUnitPrice);
									break;
								}

								case "VPN_IPSEC": {
									BigDecimal vpnIpSecUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getVpnIpSec());
									networkUnitPriceInfo.setVpnIpSec(vpnIpSecUnitPrice);
									break;
								}

								case "DNS_DHCP": {
									BigDecimal dnsDhcpUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getDnsDhcpService());
									networkUnitPriceInfo.setDnsDhcpService(dnsDhcpUnitPrice);
									break;
								}

								case "FIREWALLS": {
									BigDecimal firewallsUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getFirewalls());

									networkUnitPriceInfo.setFirewalls(firewallsUnitPrice);
									break;
								}

								case "PROXIES": {
									BigDecimal proxiesUnitPrice = performCountryFactorFXRatesConversion(
											referenceCountryFactor, dealCountryFactor, fxRate,
											networkUnitPriceInfo.getProxies());

									networkUnitPriceInfo.setProxies(proxiesUnitPrice);
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
