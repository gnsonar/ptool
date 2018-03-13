package com.in.fujitsu.pricing.enduser.calculator;

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
import com.in.fujitsu.pricing.enduser.dto.EndUserCalculateDto;
import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserUnitPriceInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.enduser.helper.EndUserCommonHelper;
import com.in.fujitsu.pricing.enduser.repository.EndUserRepository;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.specification.EndUserSpecification;

import lombok.extern.slf4j.Slf4j;


/**
 * @author pawarbh
 *
 */
@Slf4j
@Component
public class HighEndLaptopsCalculator {

	@Autowired
	private EndUserCommonHelper endUserCommonHelper;

	@Autowired
	private EndUserRepository endUserRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String HIGH_END_LAPTOP = "HIGH_END_LAPTOP";



	/**
	 * @param endUserInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @return
	 */
	public EndUserCalculateDto calculateHighEndLaptopYearlyRevenue(EndUserInfo endUserInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgDevices = endUserCommonHelper.getHighEndLaptopsAverageVolume(endUserInfo.getEndUserYearlyDataInfoList());
		EndUserCalculateDto calculateDto = null;
		if (avgDevices != null && avgDevices.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			calculateDto = calculatePastDealRevenue(endUserInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgDevices, calculateDto);

			// Benchmark Calculation
			calculateDto = calculateBenchMarkDealRevenue(endUserInfo, dealInfo, assessmentDealTerm, referenceCountryFactor,
					countryFactors, avgDevices,
					calculateDto == null ? new EndUserCalculateDto() : calculateDto);
		}

		return calculateDto;
	}



	/**
	 * @param endUserInfo
	 * @param dealInfo
	 * @param towerSpecificBandInfo
	 * @param assessmentDealTerm
	 * @param countryFactors
	 * @param referenceCountryFactor
	 * @param avgDevices
	 * @param calculateDto
	 * @return
	 */
	@Transactional
	private EndUserCalculateDto calculatePastDealRevenue(EndUserInfo endUserInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgDevices, EndUserCalculateDto calculateDto) {

		Specification<EndUserInfo> specification = EndUserSpecification.specificationForHighEndLaptops(
				endUserInfo.isOffshoreAllowed(),endUserInfo.isIncludeBreakFix(), endUserInfo.isIncludeHardware(), endUserInfo.getResolutionTime(),
				avgDevices, towerSpecificBandInfo.getBandPercentage() == null ? new BigDecimal(100)
						: towerSpecificBandInfo.getBandPercentage());
		List<EndUserInfo> pastDealResults = endUserRepository.findAll(specification);
		log.info("High End Laptop devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			endUserCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			endUserCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, HIGH_END_LAPTOP);
			calculateDto = prepareCalculateDtoForPastDeal(pastDealResults, endUserInfo,
					new EndUserCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}



	/**
	 * @param pastDealResults
	 * @param endUserInfo
	 * @param calculateDto
	 * @return
	 */
	private EndUserCalculateDto prepareCalculateDtoForPastDeal(List<EndUserInfo> pastDealResults,
			EndUserInfo endUserInfo, EndUserCalculateDto calculateDto) {
		// Past Deal Devices Volume
		setPrice(endUserInfo, calculateDto, pastDealResults);
		return calculateDto;
	}

	/**
	 * @param endUserInfo
	 * @param calculateDto
	 * @param pastDealResults
	 */
	private void setPrice(EndUserInfo endUserInfo, EndUserCalculateDto calculateDto,
			List<EndUserInfo> pastDealResults) {

		// Past Deal devices Volume
		Map<Integer, BigDecimal> unitPriceMap = getYearlyUnitPrice(pastDealResults);
		Map<Integer, BigDecimal> revenueMap = getYearlyRevenue(unitPriceMap, endUserInfo);
		BigDecimal avgUnitPrice = getConsolidatedAvgUnitPrice(revenueMap, endUserInfo);

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
	private Map<Integer, BigDecimal> getYearlyUnitPrice(List<EndUserInfo> dealResults) {
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
	private void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap, List<EndUserInfo> dealResults) {
		for (EndUserInfo endUserInfo : dealResults) {
			if (!CollectionUtils.isEmpty(endUserInfo.getEndUserYearlyDataInfoList())) {
				for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserUnitPriceInfoList())
							&& yearlyDataInfo.getEndUserUnitPriceInfoList().get(0) != null) {
						EndUserUnitPriceInfo unitPriceInfo = yearlyDataInfo.getEndUserUnitPriceInfoList().get(0);
						if (yearlyUnitPriceMap.containsKey(yearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap
									.get(yearlyDataInfo.getYear()).add(unitPriceInfo.getHighEndLaptops()));
						} else {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), unitPriceInfo.getHighEndLaptops());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param endUserInfo
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			EndUserInfo endUserInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getHighEndLaptops())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param yearlyRevenueMap
	 * @param endUserInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> yearlyRevenueMap,
			EndUserInfo endUserInfo) {
		BigDecimal totalRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int totalDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : yearlyRevenueMap.entrySet()) {
			totalRevenue = totalRevenue.add(entry.getValue());
			totalDevices = totalDevices + endUserInfo.getEndUserYearlyDataInfoList().get(i).getHighEndLaptops();
			i++;
		}
		if(totalDevices != 0){
			consolidatedAvgUnitPrice = totalRevenue
					.divide(new BigDecimal(totalDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}


	@Transactional
	private EndUserCalculateDto calculateBenchMarkDealRevenue(EndUserInfo endUserInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgDevices, EndUserCalculateDto calculateDto) {

		Specification<EndUserInfo> specification = EndUserSpecification.specificationForBenchmarkHighEndLaptops(
				endUserInfo.isOffshoreAllowed(), endUserInfo.isIncludeBreakFix(), endUserInfo.isIncludeHardware(),
						endUserInfo.getResolutionTime(), avgDevices);
		List<EndUserInfo> benchMarkDealResults = endUserRepository.findAll(specification);
		log.info("High End Laptop devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			endUserCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			endUserCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, HIGH_END_LAPTOP);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (EndUserYearlyDataInfo yearlyDataInfo : endUserInfo.getEndUserYearlyDataInfoList()) {
			BigDecimal totalDevices = new BigDecimal(yearlyDataInfo.getHighEndLaptops());
			totalYearlySum = totalYearlySum.add(totalDevices);
			yearlyVolumeMap.put(yearlyDataInfo.getYear(), totalDevices);
		}

		// Benchmark Devices
		setBenchmarkPrice(endUserInfo, calculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}


	/**
	 * @param endUserInfo
	 * @param calculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkPrice(EndUserInfo endUserInfo, EndUserCalculateDto calculateDto,
			List<EndUserInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal unitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			EndUserInfo benchmarkEndUserInfo = benchMarkDealResults.get(0);
			for (EndUserYearlyDataInfo yearlyDataInfo : benchmarkEndUserInfo.getEndUserYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getEndUserUnitPriceInfoList())) {
					for (EndUserUnitPriceInfo unitPriceInfo : yearlyDataInfo.getEndUserUnitPriceInfoList()) {
						year = yearlyDataInfo.getYear();
						unitPrice = unitPriceInfo.getHighEndLaptops();
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
