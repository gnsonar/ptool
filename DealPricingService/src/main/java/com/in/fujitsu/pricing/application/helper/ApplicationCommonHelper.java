package com.in.fujitsu.pricing.application.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.calculator.ApplicationFxRateConvertor;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;

/**
 * @author ChhabrMa
 *
 */
@Component
public class ApplicationCommonHelper {

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getTotalApplicationAverageVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		BigDecimal avgTotalApplicationVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			int totalApplicationVolume = 0;
			int size = 0;
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				if (applicationYearlyDataInfo.getTotalAppsVolume() != 0) {
					totalApplicationVolume += applicationYearlyDataInfo.getTotalAppsVolume();
					size++;
				}
			}
			if (size != 0) {
				avgTotalApplicationVolume = new BigDecimal(totalApplicationVolume / size);
			}

		}
		return avgTotalApplicationVolume;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getSimpleApplicationAverageVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		BigDecimal avgSimpleApplicationVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			int simpleApplicationVolume = 0;
			int size = 0;
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				if (applicationYearlyDataInfo.getSimpleAppsVolume() != 0) {
					simpleApplicationVolume += applicationYearlyDataInfo.getSimpleAppsVolume();
					size++;
				}
			}
			if (size != 0) {
				avgSimpleApplicationVolume = new BigDecimal(simpleApplicationVolume / size);
			}

		}
		return avgSimpleApplicationVolume;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getMediumApplicationAverageVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		BigDecimal avgMediumApplicationVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			int mediumApplicationVolume = 0;
			int size = 0;
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				if (applicationYearlyDataInfo.getMediumAppsVolume() != 0) {
					mediumApplicationVolume += applicationYearlyDataInfo.getMediumAppsVolume();
					size++;
				}
			}
			if (size != 0) {
				avgMediumApplicationVolume = new BigDecimal(mediumApplicationVolume / size);
			}

		}
		return avgMediumApplicationVolume;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getComplexApplicationAverageVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		BigDecimal avgComplexApplicationVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			int complexApplicationVolume = 0;
			int size = 0;
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				if (applicationYearlyDataInfo.getComplexAppsVolume() != 0) {
					complexApplicationVolume += applicationYearlyDataInfo.getComplexAppsVolume();
					size++;
				}
			}
			if (size != 0) {
				avgComplexApplicationVolume = new BigDecimal(complexApplicationVolume / size);
			}

		}
		return avgComplexApplicationVolume;
	}

	/**
	 * @param applicationYearlyDataInfoList
	 * @return
	 */
	public BigDecimal getVeryComplexApplicationAverageVolume(List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList) {
		BigDecimal avgVeryComplexApplicationVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(applicationYearlyDataInfoList)) {
			int veryComplexApplicationVolume = 0;
			int size = 0;
			for (ApplicationYearlyDataInfo applicationYearlyDataInfo : applicationYearlyDataInfoList) {
				if (applicationYearlyDataInfo.getMediumAppsVolume() != 0) {
					veryComplexApplicationVolume += applicationYearlyDataInfo.getVeryComplexAppsVolume();
					size++;
				}
			}
			if (size != 0) {
				avgVeryComplexApplicationVolume = new BigDecimal(veryComplexApplicationVolume / size);
			}

		}
		return avgVeryComplexApplicationVolume;
	}

	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<ApplicationInfo> dealResults) {
		for (ApplicationInfo applicationInfo : dealResults) {
			Integer dealTerm = applicationInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = applicationInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList = applicationInfo.getAppYearlyDataInfos();
				int size = applicationYearlyDataInfoList.size();
				ApplicationYearlyDataInfo applicationYearlyDataInfo = applicationYearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					ApplicationYearlyDataInfo cloneApplicationYearlyDataInfo = (ApplicationYearlyDataInfo) applicationYearlyDataInfo
							.clone();
					cloneApplicationYearlyDataInfo.setYear(size + i);
					applicationYearlyDataInfoList.add(cloneApplicationYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<ApplicationYearlyDataInfo> applicationYearlyDataInfoList = applicationInfo.getAppYearlyDataInfos();
				int size = applicationYearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					applicationYearlyDataInfoList.remove(i);
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
	public void applyFxRatesAndCountryFactor(List<ApplicationInfo> dealResults,
			List<CountryFactorInfo> countryFactors, String referenceCurrency, String referenceCountry,
			BigDecimal referenceCountryFactor) {
		// currency conversion based on the FX Rates and country factor
		ApplicationFxRateConvertor applicationFxRateConvertor = new ApplicationFxRateConvertor();
		applicationFxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor);
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
			BigDecimal difference = assessmentAvgVolume.subtract( entry.getValue());
			BigDecimal differencePercentage = difference.divide(assessmentAvgVolume, 2, BigDecimal.ROUND_CEILING).multiply(new BigDecimal(100));
			BigDecimal absoluteDifference = differencePercentage.abs();
			differencePercentageMap.put(entry.getKey(), absoluteDifference);
		}
		return differencePercentageMap;
	}

}
