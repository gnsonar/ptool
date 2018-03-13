package com.in.fujitsu.pricing.storage.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.storage.calculator.StorageFxRateConvertor;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;

@Component
public class StorageCommonHelper {

	public BigDecimal getStorageAverageVolume(List<StorageYearlyDataInfo> storageYearlyDataInfoList) {
		BigDecimal avgStorageVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoList)) {
			int totalStorageVolume = 0;
			int size = 0;
			for (StorageYearlyDataInfo storageYearlyDataInfo : storageYearlyDataInfoList) {
				if (storageYearlyDataInfo.getStorageVolume() != 0) {
					totalStorageVolume += storageYearlyDataInfo.getStorageVolume();
					size++;
				}
			}
			if (size != 0) {
				avgStorageVolume = new BigDecimal(totalStorageVolume / size);
			}

		}
		return avgStorageVolume;
	}

	public BigDecimal getBackupAverageVolume(List<StorageYearlyDataInfo> storageYearlyDataInfoList) {
		BigDecimal avgBackupVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoList)) {
			int totalBackupVolume = 0;
			int size = 0;
			for (StorageYearlyDataInfo storageYearlyDataInfo : storageYearlyDataInfoList) {
				if (storageYearlyDataInfo.getBackupVolume() != 0) {
					totalBackupVolume += storageYearlyDataInfo.getBackupVolume();
					size++;
				}
			}
			if (size != 0) {
				avgBackupVolume = new BigDecimal(totalBackupVolume / size);
			}
		}
		return avgBackupVolume;
	}

	public BigDecimal getPerformanceAverageVolume(List<StorageYearlyDataInfo> storageYearlyDataInfoList) {
		BigDecimal performanceAvgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoList)) {
			int performaceTotalVolume = 0;
			int performaceSize = 0;
			for (StorageYearlyDataInfo storageYearlyDataInfo : storageYearlyDataInfoList) {
				if (storageYearlyDataInfo.getPerformanceStorage() != 0) {
					performaceTotalVolume += storageYearlyDataInfo.getPerformanceStorage();
					performaceSize++;
				}
			}
			if (performaceSize != 0) {
				performanceAvgVolume = new BigDecimal(performaceTotalVolume / performaceSize);
			}
		}
		return performanceAvgVolume;
	}

	public BigDecimal getNonPerformanceAverageVolume(List<StorageYearlyDataInfo> storageYearlyDataInfoList) {
		BigDecimal nonPerformanceAvgVolume = new BigDecimal("0").setScale(2, BigDecimal.ROUND_CEILING);
		if (!CollectionUtils.isEmpty(storageYearlyDataInfoList)) {
			int nonPerformaceTotalVolume = 0;
			int nonPerformaceSize = 0;
			for (StorageYearlyDataInfo storageYearlyDataInfo : storageYearlyDataInfoList) {
				if (storageYearlyDataInfo.getNonPerformanceStorage() != 0) {
					nonPerformaceTotalVolume += storageYearlyDataInfo.getNonPerformanceStorage();
					nonPerformaceSize++;
				}
			}
			if (nonPerformaceSize != 0) {
				nonPerformanceAvgVolume = new BigDecimal(nonPerformaceTotalVolume / nonPerformaceSize);
			}
		}

		return nonPerformanceAvgVolume;
	}

	/**
	 * @param assessmentDealTerm
	 * @param dealResults
	 */
	public void adjustYearlyDataBasedOnDealTerm(Integer assessmentDealTerm, List<StorageInfo> dealResults) {
		for (StorageInfo storageInfo : dealResults) {
			Integer dealTerm = storageInfo.getDealInfo().getDealTerm() / 12;
			Integer currentDealTerm = storageInfo.getDealInfo().getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;

			if (currentDealTerm < assessmentDealTerm) {
				int count = assessmentDealTerm - currentDealTerm;
				List<StorageYearlyDataInfo> yearlyDataInfoList = storageInfo
						.getStorageYearlyDataInfos();
				int size = yearlyDataInfoList.size();
				StorageYearlyDataInfo yearlyDataInfo = yearlyDataInfoList.get(size - 1);
				for (int i = 1; i <= count; i++) {
					StorageYearlyDataInfo cloneYearlyDataInfo = (StorageYearlyDataInfo) yearlyDataInfo
							.clone();
					cloneYearlyDataInfo.setYear(size + i);
					yearlyDataInfoList.add(cloneYearlyDataInfo);
				}

			}
			if (currentDealTerm > assessmentDealTerm) {
				List<StorageYearlyDataInfo> yearlyDataInfoList = storageInfo
						.getStorageYearlyDataInfos();
				int size = yearlyDataInfoList.size();
				for (int i = size - 1; i >= assessmentDealTerm; i--) {
					yearlyDataInfoList.remove(i);
				}
			}
		}
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
	
	/**
	 * @param dealResults
	 * @param countryFactors
	 * @param referenceCurrency
	 * @param referenceCountry
	 * @param referenceCountryFactor
	 * @param level
	 */
	public void applyFxRatesAndCountryFactor(List<StorageInfo> dealResults, List<CountryFactorInfo> countryFactors,
			String referenceCurrency, String referenceCountry, BigDecimal referenceCountryFactor, String level) {
		// currency conversion based on the FX Rates and country factor
		StorageFxRateConvertor fxRateConvertor = new StorageFxRateConvertor();
		fxRateConvertor.calculateUnitPriceFromFXRates(dealResults, countryFactors, referenceCurrency,
				referenceCountry, referenceCountryFactor, level);

	}

}
