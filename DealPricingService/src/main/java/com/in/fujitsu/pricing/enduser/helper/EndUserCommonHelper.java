package com.in.fujitsu.pricing.enduser.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.enduser.calculator.EndUserFxRateConvertor;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;


/**
 * @author pawarbh
 *
 */
@Component
public class EndUserCommonHelper {

	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getEndUserDevicesAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getEndUserDevices() != 0) {
					totalDevices += yearlyDataInfo.getEndUserDevices();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getLaptopsAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getLaptops() != 0) {
					totalDevices += yearlyDataInfo.getLaptops();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getHighEndLaptopsAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getHighEndLaptops() != 0) {
					totalDevices += yearlyDataInfo.getHighEndLaptops();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getStandardLaptopsAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getStandardLaptops() != 0) {
					totalDevices += yearlyDataInfo.getStandardLaptops();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getDesktopsAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getDesktops() != 0) {
					totalDevices += yearlyDataInfo.getDesktops();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getThinClientsAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getThinClients() != 0) {
					totalDevices += yearlyDataInfo.getThinClients();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getMobilesAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getMobileDevices() != 0) {
					totalDevices += yearlyDataInfo.getMobileDevices();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}
	
	/**
	 * @param yearlyDataInfoList
	 * @return
	 */
	public BigDecimal getImacDevicesAverageVolume(List<EndUserYearlyDataInfo> yearlyDataInfoList) {
		BigDecimal avgDevices = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(yearlyDataInfoList)) {
			int totalDevices = 0;
			int size = 0;
			for (EndUserYearlyDataInfo yearlyDataInfo : yearlyDataInfoList) {
				if (yearlyDataInfo.getImacDevices() != 0) {
					totalDevices += yearlyDataInfo.getImacDevices();
					size++;
				}
			}

			if (size != 0) {
				avgDevices = new BigDecimal(totalDevices / size);
			}

		}
		return avgDevices;
	}

	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<EndUserInfo> pastDealResults) {
		for (EndUserInfo endUserInfo : pastDealResults) {
			Integer dealTerm = endUserInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = endUserInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<EndUserYearlyDataInfo> yearlyDataInfoList = endUserInfo
						.getEndUserYearlyDataInfoList();
				int size = yearlyDataInfoList.size();
				EndUserYearlyDataInfo yearlyDataInfo = yearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					EndUserYearlyDataInfo cloneYearlyDataInfo = (EndUserYearlyDataInfo) yearlyDataInfo
							.clone();
					cloneYearlyDataInfo.setYear(size + i);
					yearlyDataInfoList.add(cloneYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<EndUserYearlyDataInfo> ydInfoList = endUserInfo.getEndUserYearlyDataInfoList();
				int size = ydInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					ydInfoList.remove(i);
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
	 * @param level
	 */
	public void applyFxRatesAndCountryFactor(List<EndUserInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor, String level) {
		// currency conversion based on the FX Rates and country factor
		EndUserFxRateConvertor fxRateConvertor = new EndUserFxRateConvertor();
		fxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
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
