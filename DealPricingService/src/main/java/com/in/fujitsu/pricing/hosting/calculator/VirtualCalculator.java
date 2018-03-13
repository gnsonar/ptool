package com.in.fujitsu.pricing.hosting.calculator;

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
import com.in.fujitsu.pricing.hosting.dto.HostingCalculateDto;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingUnitPriceInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.helper.HostingCommonHelper;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.specification.HostingSpecification;

import lombok.extern.slf4j.Slf4j;


/**
 * @author pawarbh
 *
 */
@Slf4j
@Component
public class VirtualCalculator {

	@Autowired
	private HostingCommonHelper hostingCommonHelper;

	@Autowired
	private HostingRepository hostingRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private static String VIRTUAL = "VIRTUAL";




	public HostingCalculateDto calculateYearlyRevenue(HostingInfo hostingInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		BigDecimal avgVolume = hostingCommonHelper.getVirtualAverageVolume(hostingInfo.getHostingYearlyDataInfoList());
		HostingCalculateDto calculateDto = null;
		if (avgVolume != null && avgVolume.compareTo(new BigDecimal(0)) != 0) {
			// Past Deal Calculation
			calculateDto = calculatePastDealRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
					assessmentDealTerm, countryFactors, referenceCountryFactor, avgVolume, calculateDto);

			// Benchmark Calculation
			calculateDto = calculateBenchMarkDealRevenue(hostingInfo, dealInfo, assessmentDealTerm, referenceCountryFactor,
					countryFactors, avgVolume,
					calculateDto == null ? new HostingCalculateDto() : calculateDto);
		}

		return calculateDto;
	}



	/**
	 * @param hostingInfo
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
	private HostingCalculateDto calculatePastDealRevenue(HostingInfo hostingInfo, DealInfo dealInfo,
			TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm, List<CountryFactorInfo> countryFactors,
			BigDecimal referenceCountryFactor, BigDecimal avgVolume, HostingCalculateDto calculateDto) {

		Specification<HostingInfo> specification = HostingSpecification.specForVirtual(hostingInfo.isOffshoreAllowed(),
				hostingInfo.getLevelOfService(), hostingInfo.isIncludeHardware(), hostingInfo.isIncludeTooling(),
				hostingInfo.getCoLocation(), avgVolume, towerSpecificBandInfo.getBandPercentage() == null
						? new BigDecimal(100) : towerSpecificBandInfo.getBandPercentage());
		List<HostingInfo> pastDealResults = hostingRepository.findAll(specification);
		log.info("Virtual devices dealResults.size :" + pastDealResults.size());

		if (!CollectionUtils.isEmpty(pastDealResults)) {
			hostingCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, pastDealResults);
			hostingCommonHelper.applyFxRatesAndCountryFactor(pastDealResults, countryFactors, dealInfo.getCurrency(),
					dealInfo.getCountry(), referenceCountryFactor, VIRTUAL);
			calculateDto = prepareCalculateDtoForPastDeal(pastDealResults, hostingInfo,
					new HostingCalculateDto());

		}
		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}



	/**
	 * @param pastDealResults
	 * @param hostingInfo
	 * @param calculateDto
	 * @return
	 */
	private HostingCalculateDto prepareCalculateDtoForPastDeal(List<HostingInfo> pastDealResults,
			HostingInfo hostingInfo, HostingCalculateDto calculateDto) {
		// Past Deal Devices Volume
		setPrice(hostingInfo, calculateDto, pastDealResults);
		return calculateDto;
	}

	/**
	 * @param hostingInfo
	 * @param calculateDto
	 * @param pastDealResults
	 */
	private void setPrice(HostingInfo hostingInfo, HostingCalculateDto calculateDto,
			List<HostingInfo> pastDealResults) {

		// Past Deal devices Volume
		Map<Integer, BigDecimal> unitPriceMap = getYearlyUnitPrice(pastDealResults);
		Map<Integer, BigDecimal> revenueMap = getYearlyRevenue(unitPriceMap, hostingInfo);
		BigDecimal avgUnitPrice = getConsolidatedAvgUnitPrice(revenueMap, hostingInfo);

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
	private Map<Integer, BigDecimal> getYearlyUnitPrice(List<HostingInfo> dealResults) {
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
	private void prepareYearlyUnitPriceSum(Map<Integer, BigDecimal> yearlyUnitPriceMap, List<HostingInfo> dealResults) {
		for (HostingInfo hostingInfo : dealResults) {
			if (!CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())) {
				for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
					if (!CollectionUtils.isEmpty(yearlyDataInfo.getHostingUnitPriceInfoList())
							&& yearlyDataInfo.getHostingUnitPriceInfoList().get(0) != null) {
						HostingUnitPriceInfo unitPriceInfo = yearlyDataInfo.getHostingUnitPriceInfoList().get(0);
						if (yearlyUnitPriceMap.containsKey(yearlyDataInfo.getYear())) {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap
									.get(yearlyDataInfo.getYear()).add(unitPriceInfo.getVirtual()));
						} else {
							yearlyUnitPriceMap.put(yearlyDataInfo.getYear(), unitPriceInfo.getVirtual());
						}
					}
				}
			}
		}
	}

	/**
	 * @param yearlyUnitPriceMap
	 * @param hostingInfo
	 * @return
	 */
	private Map<Integer, BigDecimal> getYearlyRevenue(Map<Integer, BigDecimal> yearlyUnitPriceMap,
			HostingInfo hostingInfo) {
		final Map<Integer, BigDecimal> yearlyRevenueMap = new HashMap<>();
		for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
			yearlyRevenueMap.put(yearlyDataInfo.getYear(), yearlyUnitPriceMap.get(yearlyDataInfo.getYear())
					.multiply(new BigDecimal(yearlyDataInfo.getVirtual())));
		}

		return yearlyRevenueMap;
	}

	/**
	 * @param yearlyRevenueMap
	 * @param hostingInfo
	 * @return
	 */
	private BigDecimal getConsolidatedAvgUnitPrice(Map<Integer, BigDecimal> yearlyRevenueMap,
			HostingInfo hostingInfo) {
		BigDecimal totalRevenue = new BigDecimal(0);
		BigDecimal consolidatedAvgUnitPrice = new BigDecimal(0);
		int totalDevices = 0;
		int i = 0;
		for (Map.Entry<Integer, BigDecimal> entry : yearlyRevenueMap.entrySet()) {
			totalRevenue = totalRevenue.add(entry.getValue());
			totalDevices = totalDevices + hostingInfo.getHostingYearlyDataInfoList().get(i).getVirtual();
			i++;
		}
		if(totalDevices != 0){
			consolidatedAvgUnitPrice = totalRevenue
					.divide(new BigDecimal(totalDevices), 2, BigDecimal.ROUND_CEILING);
		}
		return consolidatedAvgUnitPrice;
	}


	@Transactional
	private HostingCalculateDto calculateBenchMarkDealRevenue(HostingInfo hostingInfo, DealInfo dealInfo,
			Integer assessmentDealTerm, BigDecimal referenceCountryFactor, List<CountryFactorInfo> countryFactors,
			BigDecimal avgVolume, HostingCalculateDto calculateDto) {

		Specification<HostingInfo> specification = HostingSpecification.specForBenchVirtual(
				hostingInfo.isOffshoreAllowed(), hostingInfo.getLevelOfService(), hostingInfo.isIncludeHardware(),
				hostingInfo.isIncludeTooling(), hostingInfo.getCoLocation(), avgVolume);
		List<HostingInfo> benchMarkDealResults = hostingRepository.findAll(specification);
		log.info("Virtual devices Benchmark dealResults.size :" + benchMarkDealResults.size());

		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			hostingCommonHelper.adjustYearlyDataBasedOnDealTerm(assessmentDealTerm, benchMarkDealResults);
			hostingCommonHelper.applyFxRatesAndCountryFactor(benchMarkDealResults, countryFactors,
					dealInfo.getCurrency(), dealInfo.getCountry(), referenceCountryFactor, VIRTUAL);
		}

		BigDecimal totalYearlySum = new BigDecimal(0);
		Map<Integer, BigDecimal> yearlyVolumeMap = new HashMap<>();
		for (HostingYearlyDataInfo yearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
			BigDecimal totalDevices = new BigDecimal(yearlyDataInfo.getVirtual());
			totalYearlySum = totalYearlySum.add(totalDevices);
			yearlyVolumeMap.put(yearlyDataInfo.getYear(), totalDevices);
		}

		// Benchmark Devices
		setBenchmarkPrice(hostingInfo, calculateDto, benchMarkDealResults, yearlyVolumeMap,
				totalYearlySum);

		Session session = entityManager.unwrap(Session.class);
		if (session != null) {
			session.clear();
		}

		return calculateDto;
	}


	/**
	 * @param hostingInfo
	 * @param calculateDto
	 * @param benchMarkDealResults
	 * @param yearlyVolumeMap
	 * @param totalYearlySum
	 */
	private void setBenchmarkPrice(HostingInfo hostingInfo, HostingCalculateDto calculateDto,
			List<HostingInfo> benchMarkDealResults, Map<Integer, BigDecimal> yearlyVolumeMap,
			BigDecimal totalYearlySum) {
		List<YearlyCalculateDto> benchmarkLowYearlyCalcDtoList = new ArrayList<>();
		List<YearlyCalculateDto> benchmarkTargetYearlyCalcDtoList = new ArrayList<>();
		Integer year = 0;
		BigDecimal unitPrice = new BigDecimal(0);
		BigDecimal revenue = new BigDecimal(0);
		BigDecimal totalLowRevenue = new BigDecimal(0);
		BigDecimal totalTargetRevenue = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(benchMarkDealResults)) {
			HostingInfo benchmarkHostingInfo = benchMarkDealResults.get(0);
			for (HostingYearlyDataInfo yearlyDataInfo : benchmarkHostingInfo.getHostingYearlyDataInfoList()) {
				if (!CollectionUtils.isEmpty(yearlyDataInfo.getHostingUnitPriceInfoList())) {
					for (HostingUnitPriceInfo unitPriceInfo : yearlyDataInfo.getHostingUnitPriceInfoList()) {
						year = yearlyDataInfo.getYear();
						unitPrice = unitPriceInfo.getVirtual();
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
