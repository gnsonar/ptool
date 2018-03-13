package com.in.fujitsu.pricing.network.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.network.calculator.NetworkFxRateConvertor;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
@Component
public class NetworkCommonHelper {

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getWanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgWanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int wanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getWanDevices() != 0) {
					wanDevices += networkYearlyDataInfo.getWanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgWanDevices = new BigDecimal(wanDevices / size);
			}

		}
		return avgWanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getSmallWanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgSmallWanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int smallWanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getSmallWanDevices() != 0) {
					smallWanDevices += networkYearlyDataInfo.getSmallWanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgSmallWanDevices = new BigDecimal(smallWanDevices / size);
			}

		}
		return avgSmallWanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getMediumWanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgMediumWanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int mediumWanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getMediumWanDevices() != 0) {
					mediumWanDevices += networkYearlyDataInfo.getMediumWanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgMediumWanDevices = new BigDecimal(mediumWanDevices / size);
			}

		}
		return avgMediumWanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getLargeWanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgLargeWanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int largeWanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getLargeWanDevices() != 0) {
					largeWanDevices += networkYearlyDataInfo.getLargeWanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgLargeWanDevices = new BigDecimal(largeWanDevices / size);
			}

		}
		return avgLargeWanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getLanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgLanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int lanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getLanDevices() != 0) {
					lanDevices += networkYearlyDataInfo.getLanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgLanDevices = new BigDecimal(lanDevices / size);
			}

		}
		return avgLanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getSmallLanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgSmallLanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int smallLanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getSmallLanDevices() != 0) {
					smallLanDevices += networkYearlyDataInfo.getSmallLanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgSmallLanDevices = new BigDecimal(smallLanDevices / size);
			}

		}
		return avgSmallLanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getMediumLanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgMediumLanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int mediumLanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getMediumLanDevices() != 0) {
					mediumLanDevices += networkYearlyDataInfo.getMediumLanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgMediumLanDevices = new BigDecimal(mediumLanDevices / size);
			}

		}
		return avgMediumLanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getLargeLanDevicesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgLargeLanDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int largeLanDevices = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getLargeLanDevices() != 0) {
					largeLanDevices += networkYearlyDataInfo.getLargeLanDevices();
					size++;
				}
			}

			if (size != 0) {
				avgLargeLanDevices = new BigDecimal(largeLanDevices / size);
			}

		}
		return avgLargeLanDevices;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getWlanControllersAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgWlanControllers = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int wlanControllers = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getWlanControllers() != 0) {
					wlanControllers += networkYearlyDataInfo.getWlanControllers();
					size++;
				}
			}

			if (size != 0) {
				avgWlanControllers = new BigDecimal(wlanControllers / size);
			}

		}
		return avgWlanControllers;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getWlanAccessPointsAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgWlanAccessPoints = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int wlanAccessPoints = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getWlanAccesspoint() != 0) {
					wlanAccessPoints += networkYearlyDataInfo.getWlanAccesspoint();
					size++;
				}
			}

			if (size != 0) {
				avgWlanAccessPoints = new BigDecimal(wlanAccessPoints / size);
			}

		}
		return avgWlanAccessPoints;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getVpnIpSecAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgVpnIpSec = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int vpnIpSec = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getVpnIpSec() != 0) {
					vpnIpSec += networkYearlyDataInfo.getVpnIpSec();
					size++;
				}
			}
			if (size != 0) {
				avgVpnIpSec = new BigDecimal(vpnIpSec / size);
			}

		}
		return avgVpnIpSec;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getLoadBalancerAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgLoadBalancer = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int loadBalancer = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getLoadBalancers() != 0) {
					loadBalancer += networkYearlyDataInfo.getLoadBalancers();
					size++;
				}
			}
			if (size != 0) {
				avgLoadBalancer = new BigDecimal(loadBalancer / size);
			}

		}
		return avgLoadBalancer;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getDnsDhcpAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgDnsDhcp = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int dnsDhcp = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getDnsDhcpService() != 0) {
					dnsDhcp += networkYearlyDataInfo.getDnsDhcpService();
					size++;
				}
			}
			if (size != 0) {
				avgDnsDhcp = new BigDecimal(dnsDhcp / size);
			}

		}
		return avgDnsDhcp;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getFirewallsAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgFirewalls = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int firewalls = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getFirewalls() != 0) {
					firewalls += networkYearlyDataInfo.getFirewalls();
					size++;
				}
			}
			if (size != 0) {
				avgFirewalls = new BigDecimal(firewalls / size);
			}

		}
		return avgFirewalls;
	}

	/**
	 * @param networkYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getProxiesAverageVolume(List<NetworkYearlyDataInfo> networkYearlyDataInfoList) {
		BigDecimal avgProxies = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(networkYearlyDataInfoList)) {
			int proxies = 0;
			int size = 0;
			for (NetworkYearlyDataInfo networkYearlyDataInfo : networkYearlyDataInfoList) {
				if (networkYearlyDataInfo.getProxies() != 0) {
					proxies += networkYearlyDataInfo.getProxies();
					size++;
				}
			}
			if (size != 0) {
				avgProxies = new BigDecimal(proxies / size);
			}

		}
		return avgProxies;
	}


	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<NetworkInfo> dealResults) {
		for (NetworkInfo networkInfo : dealResults) {
			Integer dealTerm = networkInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = networkInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<NetworkYearlyDataInfo> networkYearlyDataInfoList = networkInfo
						.getNetworkYearlyDataInfoList();
				int size = networkYearlyDataInfoList.size();
				NetworkYearlyDataInfo networkYearlyDataInfo = networkYearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					NetworkYearlyDataInfo cloneNetworkYearlyDataInfo = (NetworkYearlyDataInfo) networkYearlyDataInfo
							.clone();
					cloneNetworkYearlyDataInfo.setYear(size + i);
					networkYearlyDataInfoList.add(cloneNetworkYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<NetworkYearlyDataInfo> networkYearlyDataInfoList = networkInfo
						.getNetworkYearlyDataInfoList();
				int size = networkYearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					networkYearlyDataInfoList.remove(i);
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
	public void applyFxRatesAndCountryFactor(List<NetworkInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor, String level) {
		// currency conversion based on the FX Rates and country factor
		NetworkFxRateConvertor networkFxRateConvertor = new NetworkFxRateConvertor();
		networkFxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, level);

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
