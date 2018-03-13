package com.in.fujitsu.pricing.application.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.in.fujitsu.pricing.application.dto.ApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.ApplicationInfoDto;
import com.in.fujitsu.pricing.application.dto.ApplicationYearlyCalculateDto;
import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.enums.DealTypeEnum;

/**
 * @author mishrasub
 *
 */
public class AppCalculator {

	public ApplicationCalculateDto prepareAppCalculateDto(List<ApplicationInfo> dealResults, List<CountryFactorInfo> countryFactors,
			ApplicationInfoDto appInfoDto) {

		ApplicationCalculateDto appCalculateDto = new ApplicationCalculateDto();
		List<ApplicationYearlyCalculateDto> dealYearlyCalculateDtos = new ArrayList<>();
		List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos = new ArrayList<>();
		List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos = new ArrayList<>();
		List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos = new ArrayList<>();
		List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos = new ArrayList<>();


		Map<String, List<ApplicationInfo>> dealTypeList = prepareDealTypeList(dealResults);
		List<ApplicationInfo> pastDeals = dealTypeList.get(DealTypeEnum.PAST_DEAL.getName());
		List<ApplicationInfo> compDeals = dealTypeList.get(DealTypeEnum.COMPETITOR_DEAL.getName());
		List<ApplicationInfo> benchMarkDeals = dealTypeList.get(DealTypeEnum.BENCHMARK_DEAL.getName());

		//Total Apps
		setTotalAppsCalculatedValues(appInfoDto, appCalculateDto, pastDealYearlyCalculateDtos,
				compDealYearlyCalculateDtos, benchMarkDealYearlyLowCalculateDtos,
				benchMarkDealYearlyTargetCalculateDtos, pastDeals, compDeals, benchMarkDeals);

		//Simple Apps
		setSimpleAppsCalculatedValues(appInfoDto, appCalculateDto, pastDealYearlyCalculateDtos,
				compDealYearlyCalculateDtos, benchMarkDealYearlyLowCalculateDtos,
				benchMarkDealYearlyTargetCalculateDtos, pastDeals, compDeals, benchMarkDeals);

		//Medium Apps
		setMediumAppsCalculatedValues(appInfoDto, appCalculateDto, pastDealYearlyCalculateDtos,
				compDealYearlyCalculateDtos, benchMarkDealYearlyLowCalculateDtos,
				benchMarkDealYearlyTargetCalculateDtos, pastDeals, compDeals, benchMarkDeals);

		//Complex Apps
		setComplexAppsCalculatedValues(appInfoDto, appCalculateDto, pastDealYearlyCalculateDtos,
				compDealYearlyCalculateDtos, benchMarkDealYearlyLowCalculateDtos,
				benchMarkDealYearlyTargetCalculateDtos, pastDeals, compDeals, benchMarkDeals);

		//Very Complex Apps
		setVeryComplexAppsCalculatedValues(appInfoDto, appCalculateDto, pastDealYearlyCalculateDtos,
				compDealYearlyCalculateDtos, benchMarkDealYearlyLowCalculateDtos,
				benchMarkDealYearlyTargetCalculateDtos, pastDeals, compDeals, benchMarkDeals);

		dealYearlyCalculateDtos.addAll(pastDealYearlyCalculateDtos);
		dealYearlyCalculateDtos.addAll(compDealYearlyCalculateDtos);
		dealYearlyCalculateDtos.addAll(benchMarkDealYearlyLowCalculateDtos);
		dealYearlyCalculateDtos.addAll(benchMarkDealYearlyTargetCalculateDtos);

		appCalculateDto.setYearlyCalculateDtos(dealYearlyCalculateDtos);
		return appCalculateDto;
	}

	public void calculateUnitPriceFromFXRates(List<ApplicationInfo> dealDetails, List<CountryFactorInfo> countrySpecificFactors,
			String referenceCurrency) {/*
		for (AppInfo appInfo : dealDetails) {
			DealInfo dealInfo = appInfo.getDealInfo();
			BigDecimal rate = new BigDecimal(0);
			BigDecimal countryFactorValue = new BigDecimal(0);
			String currency = dealInfo.getCurrency();
			for (DealFXRatesInfo dealFXRatesInfo : dealInfo.getDealFxRates()) {
				if (referenceCurrency.equals(dealFXRatesInfo.getCurrencyFrom())
						&& currency.equals(dealFXRatesInfo.getCurrencyTo())) {
					rate = dealFXRatesInfo.getRate();
					break;
				}
			}

			for (CountryFactorInfo countryFactorInfo : countrySpecificFactors) {
				if (dealInfo.getCountry().equals(countryFactorInfo.getCountry())) {
					countryFactorValue = countryFactorInfo.getCountryFactor();
					break;
				}
			}

			for (AppYearlyDataInfo appYearlyDataInfo : appInfo.getAppYearlyDataInfos()) {
				/*if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
					AppUnitPriceInfo appUnitPriceInfo = null;
					Double totalContactsUnitPrice = appUnitPriceInfo.getTotalAppsUnitPrice() * rate.doubleValue()
							* countryFactorValue.doubleValue();
					appUnitPriceInfo.setTotalAppsUnitPrice(totalContactsUnitPrice);

					Double voiceUnitPrice = appUnitPriceInfo.getSimpleAppsUnitPrice() * rate.doubleValue()
							* countryFactorValue.doubleValue();
					appUnitPriceInfo.setSimpleAppsUnitPrice(voiceUnitPrice);

					Double mailUnitPrice = appUnitPriceInfo.getMediumAppsUnitPrice() * rate.doubleValue()
							* countryFactorValue.doubleValue();
					appUnitPriceInfo.setMediumAppsUnitPrice(mailUnitPrice);

					Double chatUnitPrice = appUnitPriceInfo.getComplexAppsUnitPrice() * rate.doubleValue()
							* countryFactorValue.doubleValue();
					appUnitPriceInfo.setComplexAppsUnitPrice(chatUnitPrice);

					Double portalUnitPrice = appUnitPriceInfo.getVeryComplexAppsUnitPrice() * rate.doubleValue()
							* countryFactorValue.doubleValue();
					appUnitPriceInfo.setVeryComplexAppsUnitPrice(portalUnitPrice);
				}*/
			}

	public Map<String, List<ApplicationInfo>> prepareDealTypeList(List<ApplicationInfo> data) {
		List<ApplicationInfo> pastDealList = new ArrayList<>();
		List<ApplicationInfo> compDealList = new ArrayList<>();
		List<ApplicationInfo> benchMarkDealList = new ArrayList<>();
		final Map<String, List<ApplicationInfo>> dealTypeMap = new HashMap<>();

		for (ApplicationInfo appInfo : data) {
			if (appInfo.getDealInfo() != null
					&& DealTypeEnum.PAST_DEAL.getName().equals(appInfo.getDealInfo().getDealType())) {
				pastDealList.add(appInfo);
			} else if (appInfo.getDealInfo() != null
					&& DealTypeEnum.COMPETITOR_DEAL.getName().equals(appInfo.getDealInfo().getDealType())) {
				compDealList.add(appInfo);
			} else if (appInfo.getDealInfo() != null
					&& DealTypeEnum.BENCHMARK_DEAL.getName().equals(appInfo.getDealInfo().getDealType())) {
				benchMarkDealList.add(appInfo);
			}
		}

		dealTypeMap.put(DealTypeEnum.PAST_DEAL.getName(), pastDealList);
		dealTypeMap.put(DealTypeEnum.COMPETITOR_DEAL.getName(), compDealList);
		dealTypeMap.put(DealTypeEnum.BENCHMARK_DEAL.getName(), benchMarkDealList);
		return dealTypeMap;
	}

	private void setTotalAppsCalculatedValues(ApplicationInfoDto appInfoDto,
			ApplicationCalculateDto appCalculateDto,
			List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos,
			List<ApplicationInfo> pastDeals,
			List<ApplicationInfo> compDeals, List<ApplicationInfo> benchMarkDeals) {

		// Past Deal Total Apps
		Map<Integer, Double> pastDealsTotalAppsUnitAvgMap = getUnitPriceAvgValue(pastDeals, DealTypeEnum.PAST_DEAL.getName(), "TOTAL APPS", null);
		Map<Integer, Double> pastDealsTotalAppsRevenueMap = getRevenueValue(pastDealsTotalAppsUnitAvgMap, appInfoDto, "TOTAL APPS");
		Double totalAppsPastDealAvgUnitPrice = getCalculatedValue(pastDealsTotalAppsRevenueMap, appInfoDto, "TOTAL APPS");
		// yearly Avg Unit Price
		for (Map.Entry<Integer, Double> entry : pastDealsTotalAppsUnitAvgMap.entrySet()) {
			ApplicationYearlyCalculateDto yearlyCalculateDto = new ApplicationYearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());
			yearlyCalculateDto.setTotalAppsUnitPrice(entry.getValue().floatValue());
			yearlyCalculateDto.setDealType( DealTypeEnum.PAST_DEAL.getName());
			pastDealYearlyCalculateDtos.add(yearlyCalculateDto);
		}

		//yearly Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : pastDealYearlyCalculateDtos) {
			Double revenue = pastDealsTotalAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setTotalAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setPastDealTotalAppsAvgUnitPrice(totalAppsPastDealAvgUnitPrice.floatValue());

		// Comp Deal Total Apps
		Map<Integer, Double> compDealsTotalAppsUnitAvgMap = getUnitPriceAvgValue(compDeals, DealTypeEnum.COMPETITOR_DEAL.getName(), "TOTAL APPS", null);
		Map<Integer, Double> compDealsTotalAppsRevenueMap = getRevenueValue(compDealsTotalAppsUnitAvgMap, appInfoDto, "TOTAL APPS");
		Double totalAppsCompDealAvgUnitPrice = getCalculatedValue(compDealsTotalAppsRevenueMap, appInfoDto, "TOTAL APPS");
		// yearly Avg Unit Price
		for (Map.Entry<Integer, Double> entry : compDealsTotalAppsUnitAvgMap.entrySet()) {
			ApplicationYearlyCalculateDto yearlyCalculateDto = new ApplicationYearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());
			yearlyCalculateDto.setTotalAppsUnitPrice(entry.getValue().floatValue());
			yearlyCalculateDto.setDealType( DealTypeEnum.COMPETITOR_DEAL.getName());
			compDealYearlyCalculateDtos.add(yearlyCalculateDto);
		}

		//yearly Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : compDealYearlyCalculateDtos) {
			Double revenue = compDealsTotalAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setTotalAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setCompDealTotalAppsAvgUnitPrice(totalAppsCompDealAvgUnitPrice.floatValue());

		// BenchMark Low Total Apps
		Map<Integer, Double> benchMarkDealsLowTotalAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "TOTAL APPS", "Low");
		Map<Integer, Double> benchMarkDealsLowTotalAppsRevenueMap = getRevenueValue(benchMarkDealsLowTotalAppsUnitAvgMap, appInfoDto, "TOTAL APPS");
		Double totalAppsBenchMarkDealLowAvgUnitPrice = getCalculatedValue(benchMarkDealsLowTotalAppsRevenueMap, appInfoDto, "TOTAL APPS");

		// yearly Avg Unit Price
		for (Map.Entry<Integer, Double> entry : benchMarkDealsLowTotalAppsUnitAvgMap.entrySet()) {
			ApplicationYearlyCalculateDto yearlyCalculateDto = new ApplicationYearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());
			yearlyCalculateDto.setTotalAppsUnitPrice(entry.getValue().floatValue());
			yearlyCalculateDto.setDealType( DealTypeEnum.BENCHMARK_DEAL.getName());
			yearlyCalculateDto.setBenchmarkType("Low");
			benchMarkDealYearlyLowCalculateDtos.add(yearlyCalculateDto);
		}

		//yearly Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyLowCalculateDtos) {
			Double revenue = benchMarkDealsLowTotalAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setTotalAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealLowTotalAppsAvgUnitPrice(totalAppsBenchMarkDealLowAvgUnitPrice.floatValue());

		// BenchMark Target Total Apps
		Map<Integer, Double> benchMarkDealsTargetTotalAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "TOTAL APPS", "Target");
		Map<Integer, Double> benchMarkDealsTargetTotalAppsRevenueMap = getRevenueValue(benchMarkDealsTargetTotalAppsUnitAvgMap, appInfoDto, "TOTAL APPS");
		Double totalAppsBenchMarkDealTargetAvgUnitPrice = getCalculatedValue(benchMarkDealsTargetTotalAppsRevenueMap, appInfoDto, "TOTAL APPS");

		// yearly Avg Unit Price
		for (Map.Entry<Integer, Double> entry : benchMarkDealsTargetTotalAppsUnitAvgMap.entrySet()) {
			ApplicationYearlyCalculateDto yearlyCalculateDto = new ApplicationYearlyCalculateDto();
			yearlyCalculateDto.setYear(entry.getKey());
			yearlyCalculateDto.setTotalAppsUnitPrice(entry.getValue().floatValue());
			yearlyCalculateDto.setDealType( DealTypeEnum.BENCHMARK_DEAL.getName());
			yearlyCalculateDto.setBenchmarkType("Target");
			benchMarkDealYearlyTargetCalculateDtos.add(yearlyCalculateDto);
		}

		//yearly Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyTargetCalculateDtos) {
			Double revenue = benchMarkDealsTargetTotalAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setTotalAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealTargeTotalAppsAvgUnitPrice(totalAppsBenchMarkDealTargetAvgUnitPrice.floatValue());

	}

	private void setSimpleAppsCalculatedValues(ApplicationInfoDto appInfoDto, ApplicationCalculateDto appCalculateDto,
			List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos, List<ApplicationInfo> pastDeals,
			List<ApplicationInfo> compDeals, List<ApplicationInfo> benchMarkDeals) {

		// Past Deal Simple Apps
		Map<Integer, Double> pastDealsSimpleAppsUnitAvgMap = getUnitPriceAvgValue(pastDeals, DealTypeEnum.PAST_DEAL.getName(), "SIMPLE APPS", null);
		Map<Integer, Double> pastDealsSimpleAppsRevenueMap = getRevenueValue(pastDealsSimpleAppsUnitAvgMap, appInfoDto, "SIMPLE APPS");
		Double simpleAppsPastDealAvgUnitPrice = getCalculatedValue(pastDealsSimpleAppsRevenueMap, appInfoDto, "SIMPLE APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : pastDealYearlyCalculateDtos) {
			Double unit = pastDealsSimpleAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsUnitPrice(unit.floatValue());

			Double revenue = pastDealsSimpleAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setPastDealSimpleAppsAvgUnitPrice(simpleAppsPastDealAvgUnitPrice.floatValue());

		// Comp Deal Simple Apps
		Map<Integer, Double> compDealsSimpleAppsUnitAvgMap = getUnitPriceAvgValue(compDeals, DealTypeEnum.COMPETITOR_DEAL.getName(), "SIMPLE APPS", null);
		Map<Integer, Double> compDealsSimpleAppsRevenueMap = getRevenueValue(compDealsSimpleAppsUnitAvgMap, appInfoDto, "SIMPLE APPS");
		Double simpleAppsCompDealAvgUnitPrice = getCalculatedValue(compDealsSimpleAppsRevenueMap, appInfoDto, "SIMPLE APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : compDealYearlyCalculateDtos) {
			Double unit = compDealsSimpleAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsUnitPrice(unit.floatValue());

			Double revenue = compDealsSimpleAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setCompDealSimpleAppsAvgUnitPrice(simpleAppsCompDealAvgUnitPrice.floatValue());

		// BenchMark Low Simple Apps
		Map<Integer, Double> benchMarkDealsLowSimpleAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "SIMPLE APPS", "Low");
		Map<Integer, Double> benchMarkDealsLowSimpleAppsRevenueMap = getRevenueValue(benchMarkDealsLowSimpleAppsUnitAvgMap, appInfoDto, "SIMPLE APPS");
		Double simpleAppsBenchMarkDealLowAvgUnitPrice = getCalculatedValue(benchMarkDealsLowSimpleAppsRevenueMap, appInfoDto, "SIMPLE APPS");

		///yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyLowCalculateDtos) {
			Double unit = benchMarkDealsLowSimpleAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsLowSimpleAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealLowSimpleAppsAvgUnitPrice(simpleAppsBenchMarkDealLowAvgUnitPrice.floatValue());

		// BenchMark Target Simple Apps
		Map<Integer, Double> benchMarkDealsTargetSimpleAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "SIMPLE APPS", "Target");
		Map<Integer, Double> benchMarkDealsTargetSimpleAppsRevenueMap = getRevenueValue(benchMarkDealsTargetSimpleAppsUnitAvgMap, appInfoDto, "SIMPLE APPS");
		Double simpleAppsBenchMarkDealTargetAvgUnitPrice = getCalculatedValue(benchMarkDealsTargetSimpleAppsRevenueMap, appInfoDto, "SIMPLE APPS");


		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyTargetCalculateDtos) {
			Double unit = benchMarkDealsTargetSimpleAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsTargetSimpleAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setSimpleAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealTargetSimpleAppsAvgUnitPrice(simpleAppsBenchMarkDealTargetAvgUnitPrice.floatValue());

	}

	private void setMediumAppsCalculatedValues(ApplicationInfoDto appInfoDto, ApplicationCalculateDto appCalculateDto,
			List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos, List<ApplicationInfo> pastDeals,
			List<ApplicationInfo> compDeals, List<ApplicationInfo> benchMarkDeals) {

		// Past Deal Medium Apps
		Map<Integer, Double> pastDealsMediumAppsUnitAvgMap = getUnitPriceAvgValue(pastDeals, DealTypeEnum.PAST_DEAL.getName(), "MEDIUM APPS", null);
		Map<Integer, Double> pastDealsMediumAppsRevenueMap = getRevenueValue(pastDealsMediumAppsUnitAvgMap, appInfoDto, "MEDIUM APPS");
		Double mediumAppsPastDealAvgUnitPrice = getCalculatedValue(pastDealsMediumAppsRevenueMap, appInfoDto, "MEDIUM APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : pastDealYearlyCalculateDtos) {
			Double unit = pastDealsMediumAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsUnitPrice(unit.floatValue());

			Double revenue = pastDealsMediumAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setPastDealMediumAppsAvgUnitPrice(mediumAppsPastDealAvgUnitPrice.floatValue());

		// Comp Deal Medium Apps
		Map<Integer, Double> compDealsMediumAppsUnitAvgMap = getUnitPriceAvgValue(compDeals, DealTypeEnum.COMPETITOR_DEAL.getName(), "MEDIUM APPS", null);
		Map<Integer, Double> compDealsMediumAppsRevenueMap = getRevenueValue(compDealsMediumAppsUnitAvgMap, appInfoDto, "MEDIUM APPS");
		Double mediumAppsCompDealAvgUnitPrice = getCalculatedValue(compDealsMediumAppsRevenueMap, appInfoDto, "MEDIUM APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : compDealYearlyCalculateDtos) {
			Double unit = compDealsMediumAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsUnitPrice(unit.floatValue());

			Double revenue = compDealsMediumAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setCompDealMediumAppsAvgUnitPrice(mediumAppsCompDealAvgUnitPrice.floatValue());

		// BenchMark Low Medium Apps
		Map<Integer, Double> benchMarkDealsLowMediumAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "MEDIUM APPS", "Low");
		Map<Integer, Double> benchMarkDealsLowMediumAppsRevenueMap = getRevenueValue(benchMarkDealsLowMediumAppsUnitAvgMap, appInfoDto, "MEDIUM APPS");
		Double mediumAppsBenchMarkDealLowAvgUnitPrice = getCalculatedValue(benchMarkDealsLowMediumAppsRevenueMap, appInfoDto, "MEDIUM APPS");

		///yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyLowCalculateDtos) {
			Double unit = benchMarkDealsLowMediumAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsLowMediumAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealLowMediumAppsAvgUnitPrice(mediumAppsBenchMarkDealLowAvgUnitPrice.floatValue());

		// BenchMark Target Medium Apps
		Map<Integer, Double> benchMarkDealsTargetMediumAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "MEDIUM APPS", "Target");
		Map<Integer, Double> benchMarkDealsTargetMediumAppsRevenueMap = getRevenueValue(benchMarkDealsTargetMediumAppsUnitAvgMap, appInfoDto, "MEDIUM APPS");
		Double mediumAppsBenchMarkDealTargetAvgUnitPrice = getCalculatedValue(benchMarkDealsTargetMediumAppsRevenueMap, appInfoDto, "MEDIUM APPS");


		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyTargetCalculateDtos) {
			Double unit = benchMarkDealsTargetMediumAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsTargetMediumAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setMediumAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealTargetMediumAppsAvgUnitPrice(mediumAppsBenchMarkDealTargetAvgUnitPrice.floatValue());

	}

	private void setComplexAppsCalculatedValues(ApplicationInfoDto appInfoDto, ApplicationCalculateDto appCalculateDto,
			List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos, List<ApplicationInfo> pastDeals,
			List<ApplicationInfo> compDeals, List<ApplicationInfo> benchMarkDeals) {

		// Past Deal Complex Apps
		Map<Integer, Double> pastDealsComplexAppsUnitAvgMap = getUnitPriceAvgValue(pastDeals, DealTypeEnum.PAST_DEAL.getName(), "COMPLEX APPS", null);
		Map<Integer, Double> pastDealsComplexAppsRevenueMap = getRevenueValue(pastDealsComplexAppsUnitAvgMap, appInfoDto, "COMPLEX APPS");
		Double complexAppsPastDealAvgUnitPrice = getCalculatedValue(pastDealsComplexAppsRevenueMap, appInfoDto, "COMPLEX APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : pastDealYearlyCalculateDtos) {
			Double unit = pastDealsComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsUnitPrice(unit.floatValue());

			Double revenue = pastDealsComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setPastDealComplexAppsAvgUnitPrice(complexAppsPastDealAvgUnitPrice.floatValue());

		// Comp Deal Complex Apps
		Map<Integer, Double> compDealsComplexAppsUnitAvgMap = getUnitPriceAvgValue(compDeals, DealTypeEnum.COMPETITOR_DEAL.getName(), "COMPLEX APPS", null);
		Map<Integer, Double> compDealsComplexAppsRevenueMap = getRevenueValue(compDealsComplexAppsUnitAvgMap, appInfoDto, "COMPLEX APPS");
		Double complexAppsCompDealAvgUnitPrice = getCalculatedValue(compDealsComplexAppsRevenueMap, appInfoDto, "COMPLEX APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : compDealYearlyCalculateDtos) {
			Double unit = compDealsComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsUnitPrice(unit.floatValue());

			Double revenue = compDealsComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setCompDealComplexAppsAvgUnitPrice(complexAppsCompDealAvgUnitPrice.floatValue());

		// BenchMark Low Complex Apps
		Map<Integer, Double> benchMarkDealsLowComplexAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "COMPLEX APPS", "Low");
		Map<Integer, Double> benchMarkDealsLowComplexAppsRevenueMap = getRevenueValue(benchMarkDealsLowComplexAppsUnitAvgMap, appInfoDto, "COMPLEX APPS");
		Double complexAppsBenchMarkDealLowAvgUnitPrice = getCalculatedValue(benchMarkDealsLowComplexAppsRevenueMap, appInfoDto, "COMPLEX APPS");

		///yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyLowCalculateDtos) {
			Double unit = benchMarkDealsLowComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsLowComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealLowComplexAppsAvgUnitPrice(complexAppsBenchMarkDealLowAvgUnitPrice.floatValue());

		// BenchMark Target Complex Apps
		Map<Integer, Double> benchMarkDealsTargetComplexAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "COMPLEX APPS", "Target");
		Map<Integer, Double> benchMarkDealsTargetComplexAppsRevenueMap = getRevenueValue(benchMarkDealsTargetComplexAppsUnitAvgMap, appInfoDto, "COMPLEX APPS");
		Double complexAppsBenchMarkDealTargetAvgUnitPrice = getCalculatedValue(benchMarkDealsTargetComplexAppsRevenueMap, appInfoDto, "COMPLEX APPS");


		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyTargetCalculateDtos) {
			Double unit = benchMarkDealsTargetComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsTargetComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setComplexAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealTargetComplexAppsAvgUnitPrice(complexAppsBenchMarkDealTargetAvgUnitPrice.floatValue());
	}

	private void setVeryComplexAppsCalculatedValues(ApplicationInfoDto appInfoDto, ApplicationCalculateDto appCalculateDto,
			List<ApplicationYearlyCalculateDto> pastDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> compDealYearlyCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyLowCalculateDtos,
			List<ApplicationYearlyCalculateDto> benchMarkDealYearlyTargetCalculateDtos, List<ApplicationInfo> pastDeals,
			List<ApplicationInfo> compDeals, List<ApplicationInfo> benchMarkDeals) {

		// Past Deal Very Complex Apps
		Map<Integer, Double> pastDealsVeryComplexAppsUnitAvgMap = getUnitPriceAvgValue(pastDeals, DealTypeEnum.PAST_DEAL.getName(), "VERY COMPLEX APPS", null);
		Map<Integer, Double> pastDealsVeryComplexAppsRevenueMap = getRevenueValue(pastDealsVeryComplexAppsUnitAvgMap, appInfoDto, "VERY COMPLEX APPS");
		Double veryComplexAppsPastDealAvgUnitPrice = getCalculatedValue(pastDealsVeryComplexAppsRevenueMap, appInfoDto, "VERY COMPLEX APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : pastDealYearlyCalculateDtos) {
			Double unit = pastDealsVeryComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsUnitPrice(unit.floatValue());

			Double revenue = pastDealsVeryComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setPastDealVeryComplexAppsAvgUnitPrice(veryComplexAppsPastDealAvgUnitPrice.floatValue());

		// Comp Deal Very Complex Apps
		Map<Integer, Double> compDealsVeryComplexAppsUnitAvgMap = getUnitPriceAvgValue(compDeals, DealTypeEnum.COMPETITOR_DEAL.getName(), "VERY COMPLEX APPS", null);
		Map<Integer, Double> compDealsVeryComplexAppsRevenueMap = getRevenueValue(compDealsVeryComplexAppsUnitAvgMap, appInfoDto, "VERY COMPLEX APPS");
		Double veryComplexAppsCompDealAvgUnitPrice = getCalculatedValue(compDealsVeryComplexAppsRevenueMap, appInfoDto, "VERY COMPLEX APPS");

		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : compDealYearlyCalculateDtos) {
			Double unit = compDealsVeryComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsUnitPrice(unit.floatValue());

			Double revenue = compDealsVeryComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsRevenue(revenue.floatValue());
		}
		// Average Unit Price
		appCalculateDto.setCompDealVeryComplexAppsAvgUnitPrice(veryComplexAppsCompDealAvgUnitPrice.floatValue());

		// BenchMark Low Very Complex Apps
		Map<Integer, Double> benchMarkDealsLowVeryComplexAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "VERY COMPLEX APPS", "Low");
		Map<Integer, Double> benchMarkDealsLowVeryComplexAppsRevenueMap = getRevenueValue(benchMarkDealsLowVeryComplexAppsUnitAvgMap, appInfoDto, "VERY COMPLEX APPS");
		Double veryComplexAppsBenchMarkDealLowAvgUnitPrice = getCalculatedValue(benchMarkDealsLowVeryComplexAppsRevenueMap, appInfoDto, "VERY COMPLEX APPS");

		///yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyLowCalculateDtos) {
			Double unit = benchMarkDealsLowVeryComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsLowVeryComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealLowVeryComplexAppsAvgUnitPrice(veryComplexAppsBenchMarkDealLowAvgUnitPrice.floatValue());

		// BenchMark Target Very Complex Apps
		Map<Integer, Double> benchMarkDealsTargetVeryComplexAppsUnitAvgMap = getUnitPriceAvgValue(benchMarkDeals, DealTypeEnum.BENCHMARK_DEAL.getName(), "VERY COMPLEX APPS", "Target");
		Map<Integer, Double> benchMarkDealsTargetVeryComplexAppsRevenueMap = getRevenueValue(benchMarkDealsTargetVeryComplexAppsUnitAvgMap, appInfoDto, "VERY COMPLEX APPS");
		Double veryComplexAppsBenchMarkDealTargetAvgUnitPrice = getCalculatedValue(benchMarkDealsTargetVeryComplexAppsRevenueMap, appInfoDto, "VERY COMPLEX APPS");


		//yearly Avg Unit Price & Revenue
		for(ApplicationYearlyCalculateDto appYearlyCalculateDto : benchMarkDealYearlyTargetCalculateDtos) {
			Double unit = benchMarkDealsTargetVeryComplexAppsUnitAvgMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsUnitPrice(unit.floatValue());

			Double revenue = benchMarkDealsTargetVeryComplexAppsRevenueMap.get(appYearlyCalculateDto.getYear());
			appYearlyCalculateDto.setVeryComplexAppsRevenue(revenue.floatValue());
		}

		// Average Unit Price
		appCalculateDto.setBenchDealTargetVeryComplexAppsAvgUnitPrice(veryComplexAppsBenchMarkDealTargetAvgUnitPrice.floatValue());
	}



	public Map<Integer, Double> getUnitPriceAvgValue(List<ApplicationInfo> dealData, String dealType, String level, String benchMarkType) {
		final Map<Integer, Double> unitPriceAvg  = new HashMap<>();
		if (DealTypeEnum.PAST_DEAL.getName().equals(dealType)
				|| DealTypeEnum.COMPETITOR_DEAL.getName().equals(dealType)) {
			prepareUnitPriceAvgMap(unitPriceAvg, dealData, level);

		} else if (DealTypeEnum.BENCHMARK_DEAL.getName().equals(dealType)) {

			prepareBenchMarkUnitPriceAvgMap(unitPriceAvg, dealData, level, benchMarkType);
		}

		for (Map.Entry<Integer, Double>  entry : unitPriceAvg.entrySet()) {
			unitPriceAvg.put(entry.getKey(), entry.getValue() / dealData.size());
		}
		return unitPriceAvg;
	}

	public Map<Integer, Double> getRevenueValue(Map<Integer, Double> unitAvgMap, ApplicationInfoDto appInfoDto, String level) {
		final Map<Integer, Double> revenuePrice  = new HashMap<>();
		/*for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
			switch (level) {
				case "TOTAL APPS" : {
					revenuePrice.put(appYearlyDataInfoDto.getYear(), unitAvgMap.get(appYearlyDataInfoDto.getYear()) * appYearlyDataInfoDto.getTotalApps());
					break;
				}
				case "SIMPLE APPS" : {
					revenuePrice.put(appYearlyDataInfoDto.getYear(), unitAvgMap.get(appYearlyDataInfoDto.getYear()) * appYearlyDataInfoDto.getSimpleApps());
					break;
				}
				case "MEDIUM APPS" : {
					revenuePrice.put(appYearlyDataInfoDto.getYear(), unitAvgMap.get(appYearlyDataInfoDto.getYear()) * appYearlyDataInfoDto.getMediumApps());
					break;
				}
				case "COMPLEX APPS" : {
					revenuePrice.put(appYearlyDataInfoDto.getYear(), unitAvgMap.get(appYearlyDataInfoDto.getYear()) * appYearlyDataInfoDto.getComplexApps());
					break;
				}
				case "VERY COMPLEX APPS" : {
					revenuePrice.put(appYearlyDataInfoDto.getYear(), unitAvgMap.get(appYearlyDataInfoDto.getYear()) * appYearlyDataInfoDto.getVeryComplexApps());
					break;
				}
			}
		}*/

		return revenuePrice;

	}

	public static Double getCalculatedValue(Map<Integer, Double> revenueMap, ApplicationInfoDto appInfoDto, String level) {
		switch (level) {/*
			case "TOTAL APPS" : {
				double totalAppsRevenue = 0;
				int totalApps = 0;
				for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
					totalApps = totalApps + appYearlyDataInfoDto.getTotalApps();
				}

				for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
					totalAppsRevenue = totalAppsRevenue + entry.getValue();
				}

				return totalAppsRevenue/totalApps;
			}
			case "SIMPLE APPS" : {
				double simpleRevenue = 0;
				int simple = 0;
				for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
					simple = simple + appYearlyDataInfoDto.getSimpleApps();
				}

				for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
					simpleRevenue = simpleRevenue + entry.getValue();
				}

				return simpleRevenue/simple;
			}
			case "MEDIUM APPS" : {
				double mediumRevenue = 0;
				int medium = 0;
				for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
					medium = medium + appYearlyDataInfoDto.getMediumApps();
				}

				for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
					mediumRevenue = mediumRevenue + entry.getValue();
				}

				return mediumRevenue/medium;
			}
			case "COMPLEX APPS" : {
				double complexRevenue = 0;
				int complex = 0;
				for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
					complex = complex + appYearlyDataInfoDto.getComplexApps();
				}

				for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
					complexRevenue = complexRevenue + entry.getValue();
				}

				return complexRevenue/complex;
			}
			case "VERY COMPLEX APPS" : {
				double veryComplexRevenue = 0;
				int veryComplex = 0;
				for (AppYearlyDataInfoDto appYearlyDataInfoDto : appInfoDto.getAppYearlyDataInfoDtos()) {
					veryComplex = veryComplex + appYearlyDataInfoDto.getVeryComplexApps();
				}

				for (Map.Entry<Integer, Double> entry : revenueMap.entrySet()) {
					veryComplexRevenue = veryComplexRevenue + entry.getValue();
				}

				return veryComplexRevenue/veryComplex;
			}
		*/}
		return 0.0;
	}

	public void prepareUnitPriceAvgMap(Map<Integer, Double> unitPriceAvg, List<ApplicationInfo> dealData, String level) {/*
		for (AppInfo appInfo : dealData) {/*
			if (appInfo.getAppYearlyDataInfos() != null) {
				for (AppYearlyDataInfo appYearlyDataInfo : appInfo.getAppYearlyDataInfos()) {
					ServiceDeskYearlyCalculateDto yearlyCalculateDto = new ServiceDeskYearlyCalculateDto();
					yearlyCalculateDto.setYear(appYearlyDataInfo.getYear());
					switch (level) {
						case "TOTAL APPS" : {
							if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
								unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getTotalAppsUnitPrice());
							} else {
								if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getTotalAppsUnitPrice());
								}
							}
							break;
						}
						case "SIMPLE APPS" : {
							if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
								unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getSimpleAppsUnitPrice());
							} else {
								if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getSimpleAppsUnitPrice());
								}
							}
							break;
						}
						case "MEDIUM APPS" : {
							if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
								unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getMediumAppsUnitPrice());
							} else {
								if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getMediumAppsUnitPrice());
								}
							}
							break;
						}
						case "COMPLEX APPS" : {
							if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
								unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getComplexAppsUnitPrice());
							} else {
								if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getComplexAppsUnitPrice());
								}
							}
							break;
						}
						case "VERY COMPLEX APPS" : {
							if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
								unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getVeryComplexAppsUnitPrice());
							} else {
								if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getVeryComplexAppsUnitPrice());
								}
							}
							break;
						}
					}
				}
			}
		*/}


	public void prepareBenchMarkUnitPriceAvgMap(Map<Integer, Double> unitPriceAvg, List<ApplicationInfo> dealData, String level, String benchMarkType) {/*
		for (AppInfo appInfo : dealData) {/*
			if (appInfo.getAppYearlyDataInfos() != null) {
				for (AppYearlyDataInfo appYearlyDataInfo : appInfo.getAppYearlyDataInfos()) {
					ServiceDeskYearlyCalculateDto yearlyCalculateDto = new ServiceDeskYearlyCalculateDto();
					yearlyCalculateDto.setYear(appYearlyDataInfo.getYear());
					switch (level) {
						case "TOTAL APPS" : {
							if (benchMarkType.equals(appYearlyDataInfo.getBenchMarkType())) {
								if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getTotalAppsUnitPrice());
								} else {
									if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
										unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getTotalAppsUnitPrice());
									}
								}
							}

							break;
						}
						case "SIMPLE APPS" : {
							if (benchMarkType.equals(appYearlyDataInfo.getBenchMarkType())) {
								if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getSimpleAppsUnitPrice());
								} else {
									if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
										unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getSimpleAppsUnitPrice());
									}
								}
							}

							break;
						}
						case "MEDIUM APPS" : {
							if (benchMarkType.equals(appYearlyDataInfo.getBenchMarkType())) {
								if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getMediumAppsUnitPrice());
								} else {
									if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
										unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getMediumAppsUnitPrice());
									}
								}
							}

							break;
						}
						case "COMPLEX APPS" : {
							if (benchMarkType.equals(appYearlyDataInfo.getBenchMarkType())) {
								if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getComplexAppsUnitPrice());
								} else {
									if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
										unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getComplexAppsUnitPrice());
									}
								}
							}

							break;
						}
						case "VERY COMPLEX APPS" : {
							if (benchMarkType.equals(appYearlyDataInfo.getBenchMarkType())) {
								if (unitPriceAvg.containsKey(appYearlyDataInfo.getYear()) && appYearlyDataInfo.getAppUnitPriceInfo() != null) {
									unitPriceAvg.put(appYearlyDataInfo.getYear(), unitPriceAvg.get(appYearlyDataInfo.getYear()) + appYearlyDataInfo.getAppUnitPriceInfo().getVeryComplexAppsUnitPrice());
								} else {
									if (appYearlyDataInfo.getAppUnitPriceInfo() != null) {
										unitPriceAvg.put(appYearlyDataInfo.getYear(), appYearlyDataInfo.getAppUnitPriceInfo().getVeryComplexAppsUnitPrice());
									}
								}
							}

							break;
						}
					}
				}
			}
		*/}

}
