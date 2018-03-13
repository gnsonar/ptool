package com.in.fujitsu.pricing.storage.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.YearlyCalculateDto;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.specification.StorageSpecification;
import com.in.fujitsu.pricing.storage.dto.NonPerformanceCalculateDto;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageUnitPriceInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;
import com.in.fujitsu.pricing.storage.helper.StorageCommonHelper;
import com.in.fujitsu.pricing.storage.repository.StorageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NonPerformanceCalculator {

	@Autowired
	private StorageCommonHelper commonHelper;

	@Autowired
	private StorageRepository storageRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String NON_PERFORMANCE = "NON_PERFORMANCE";

	public NonPerformanceCalculateDto calculateYearlyRevenue(StorageInfo storageInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgVolume = commonHelper.getNonPerformanceAverageVolume(storageInfo.getStorageYearlyDataInfos());
		NonPerformanceCalculateDto calculateDto = null;
		if (avgVolume != null && avgVolume.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			calculateDto = calculatePastDealRevenue(storageInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgVolume, calculateDto);

			// Benchmark Calculation
			calculateDto = calculateBenchMarkDealRevenue(storageInfo, dealInfo, assessmentDealTerm, referenceCountryFactor,
					countryFactors, avgVolume,
					calculateDto == null ? new NonPerformanceCalculateDto() : calculateDto);
		}

		return calculateDto;
	}



	/**
	 * @param storageInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgVolume
	 * @param calculateDto
	 * @return
	 */
	@Transactional
	private NonPerformanceCalculateDto calculatePastDealRevenue(StorageInfo storageInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgVolume, NonPerformanceCalculateDto calculateDto) {

		Specification<StorageInfo> specification = StorageSpecification.specificationForNonPerformancePastDeal(
				storageInfo.isOffshoreAllowed(), storageInfo.getServiceWindowSla(), storageInfo.isIncludeHardware(),
				avgVolume, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<StorageInfo> pastDealResults = storageRepository.findAll(specification);
		log.info("Non Performance dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			commonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, NON_PERFORMANCE);
			calculateDto = prepareCalculateDtoForPastDeal(pastDealResults, storageInfo,
					new NonPerformanceCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}



	/**
	 * @param pastDealResults
	 * @param storageInfo
	 * @param calculateDto
	 * @return
	 */
	private NonPerformanceCalculateDto prepareCalculateDtoForPastDeal(List<StorageInfo> pastDealResults,
			StorageInfo storageInfo, NonPerformanceCalculateDto calculateDto) {
		// Past Deal Devices Volume
		setPrice(storageInfo, calculateDto, pastDealResults);
		return calculateDto;
	}

	/**
	 * @param storageInfo
	 * @param calculateDto
	 * @param pastDealResults
	 */
	private void setPrice(StorageInfo storageInfo, NonPerformanceCalculateDto calculateDto,
			List<StorageInfo> pastDealResults) {

		// Past Deal devices Volume
		Map<Integer, BigDecimal> unitPriceMap = getYearlyUnitPrice(pastDealResults);
		Map<Integer, BigDecimal> revenueMap = getYearlyRevenue(unitPriceMap, storageInfo);
		BigDecimal avgUnitPrice = getConsolidatedAvgUnitPrice(revenueMap, storageInfo);

		// yearly Unit Price & Revenue
		for (Map.Entry<Integer, BigDecimal> entry : unitPriceMap.entrySet()) {
			YearlyCalculateDto yearlyCalculateDto = new YearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());

			// Unit Price
			yearlyCalculateDto.setUnitPrice(entry.getValue().floatValue());

			// Revenue
			BigDecimal revenue = revenueMap.get(entry.getKey());
			yearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
			calculateDto.getPastDealYearlyCalcDtoList().add(yearlyCalculateDto);
		}

		// Consolidated Average Unit Price
		calculateDto.setPastDealAvgUnitPrice(avgUnitPrice);

	}

	/**
	 * @param dealResults
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyUnitPrice(List<StorageInfo> dealResults) {
		final Map<Integer, BigDecimal> yearlyUnitPriceMap = new HashMap<>();
		prepareYearlyUnitPriceSum(yearlyUnitPriceMap, dealResults);
		BigDecimal dealResultSize = new BigDecimal(dealResults.size());
		for (Map.Entry<Integer, BigDecimal> entry : yearlyUnitPriceMap.entrySet()) {
			yearlyUnitPriceMap.put(entry.getKey(),
					entry.getValue().divide(dealResultSize, 2, BigDecimal.ROUND_CEILING));
		}

		return yearlyUnitPriceMap;
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param dealData
	 */
	private void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap, List<StorageInfo> dealResults) {
		for (StorageInfo storageInfo : dealResults) {
			if (!CollectionUtils.isEmpty(storageInfo.getStorageYearlyDataInfos())) {
				for (StorageYearlyDataInfo yearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getStorageUnitPriceInfo())
							&& yearlyDataInfo.getStorageUnitPriceInfo().get(0) != null) {
						StorageUnitPriceInfo unitPriceInfo = yearlyDataInfo.getStorageUnitPriceInfo().get(0);
						if (yearlyUnitPriceMap.containsKey(yearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap
									.get(yearlyDataInfo.getYear()).add(unitPriceInfo.getNonPerformanceUnitPrice()));
						} else {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), unitPriceInfo.getNonPerformanceUnitPrice());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param storageInfo
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			StorageInfo storageInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (StorageYearlyDataInfo yearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getNonPerformanceStorage())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param yearlyRevenueMap
	 * @param storageInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> yearlyRevenueMap,
			StorageInfo storageInfo) {
		BigDecimal totalRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);	
		int totalDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : yearlyRevenueMap.entrySet()) {
			totalRevenue = totalRevenue.add(entry.getValue());
			totalDevices = totalDevices + storageInfo.getStorageYearlyDataInfos().get(i).getNonPerformanceStorage();
			i++;
		}
		if(totalDevices != 0){
			consolidatedAvgUnitPrice = totalRevenue
					.divide(new BigDecimal(totalDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}


	@Transactional
	private NonPerformanceCalculateDto calculateBenchMarkDealRevenue(StorageInfo storageInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgVolume, NonPerformanceCalculateDto calculateDto) {

		Specification<StorageInfo> specification = StorageSpecification.specificationForNonPerormanceBenchmark(
				storageInfo.isOffshoreAllowed(), storageInfo.getServiceWindowSla(), storageInfo.isIncludeHardware(),
				avgVolume);
		List<StorageInfo> benchMarkDealResults = storageRepository.findAll(specification);
		log.info("Non Performance Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			commonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			commonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, NON_PERFORMANCE);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (StorageYearlyDataInfo yearlyDataInfo : storageInfo.getStorageYearlyDataInfos()) {
			BigDecimal totalDevices = new BigDecimal(yearlyDataInfo.getNonPerformanceStorage());
			totalYearlySum = totalYearlySum.add(totalDevices);
			yearlyVolumeMap.put(yearlyDataInfo.getYear(), totalDevices);
		}

		// Benchmark Devices
		setBenchmarkPrice(storageInfo, calculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}


	/**
	 * @param storageInfo
	 * @param calculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkPrice(StorageInfo storageInfo, NonPerformanceCalculateDto calculateDto,
			List<StorageInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal unitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			StorageInfo benchmarkStorageInfo = benchMarkDealResults.get(0);
			for (StorageYearlyDataInfo yearlyDataInfo : benchmarkStorageInfo.getStorageYearlyDataInfos()) {
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getStorageUnitPriceInfo())) {
					for (StorageUnitPriceInfo unitPriceInfo : yearlyDataInfo.getStorageUnitPriceInfo()) {
						year = yearlyDataInfo.getYear();
						unitPrice = unitPriceInfo.getNonPerformanceUnitPrice();
						revenue = unitPrice.multiply(yearlyVolumeMap.get(year));
						if (unitPriceInfo.getBenchMarkType() != null
								&& "Low".equalsIgnoreCase(unitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto lowYearlyCalculateDto = new YearlyCalculateDto();
							lowYearlyCalculateDto.setYear(year);
							// Unit price
							lowYearlyCalculateDto.setUnitPrice(unitPrice.floatValue());
							totalLowRevenue = totalLowRevenue.add(revenue);
							// Revenue
							lowYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkLowYearlyCalcDtoList.add(lowYearlyCalculateDto);
						} else if (unitPriceInfo.getBenchMarkType() != null
								&& "Target".equalsIgnoreCase(unitPriceInfo.getBenchMarkType())) {
							YearlyCalculateDto targetYearlyCalculateDto = new YearlyCalculateDto();
							targetYearlyCalculateDto.setYear(year);
							// Unit price
							targetYearlyCalculateDto.setUnitPrice(unitPrice.floatValue());

							totalTargetRevenue = totalTargetRevenue.add(revenue);
							// Revenue
							targetYearlyCalculateDto.setRevenue(Math.round(revenue.floatValue()));
							benchmarkTargetYearlyCalcDtoList.add(targetYearlyCalculateDto);
						}
					}
				}
			}

			calculateDto.setBenchmarkLowYearlyCalcDtoList(benchmarkLowYearlyCalcDtoList);
			calculateDto.setBenchmarkTargetYearlyCalcDtoList(benchmarkTargetYearlyCalcDtoList);
			calculateDto
					.setBenchDealLowAvgUnitPrice(totalLowRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
			calculateDto.setBenchDealTargetAvgUnitPrice(
					totalTargetRevenue.divide(totalYearlySum, 2, BigDecimal.ROUND_CEILING));
		}
	}

}
