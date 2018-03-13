package com.in.fujitsu.pricing.utility;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.application.dto.ComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.MediumApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.SimpleApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.TotalApplicationCalculateDto;
import com.in.fujitsu.pricing.application.dto.VeryComplexApplicationCalculateDto;
import com.in.fujitsu.pricing.dto.YearlyCalculateDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserCalculateDto;
import com.in.fujitsu.pricing.hosting.dto.HostingCalculateDto;
import com.in.fujitsu.pricing.network.dto.NetworkCalculateDto;
import com.in.fujitsu.pricing.retail.dto.RetailCalculateDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskCalculateDto;
import com.in.fujitsu.pricing.storage.dto.BackupCalculateDto;
import com.in.fujitsu.pricing.storage.dto.NonPerformanceCalculateDto;
import com.in.fujitsu.pricing.storage.dto.PerformanceCalculateDto;
import com.in.fujitsu.pricing.storage.dto.StorageCalculateDto;

public class CommonHelper {
	
	public static boolean isAllEmpty(TotalApplicationCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealTotalAppsAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowTotalAppsAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetTotalAppsAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(SimpleApplicationCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealSimpleAppsAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowSimpleAppsAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetSimpleAppsAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(MediumApplicationCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealMediumAppsAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowMediumAppsAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetMediumAppsAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(ComplexApplicationCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealComplexAppsAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowComplexAppsAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetComplexAppsAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(VeryComplexApplicationCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealVeryComplexAppsAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowVeryComplexAppsAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetVeryComplexAppsAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(StorageCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(PerformanceCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(NonPerformanceCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(BackupCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(EndUserCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(NetworkCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(ServiceDeskCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(RetailCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}
	
	public static boolean isAllEmpty(HostingCalculateDto calculateDto) {
		boolean empty = false;
		if(null != calculateDto){
			if (isEmptyPastPrice(calculateDto.getPastDealAvgUnitPrice())
				&& isEmptyBenchLowPrice(calculateDto.getBenchDealLowAvgUnitPrice())
				&& isEmptyBenchTargetPrice(calculateDto.getBenchDealTargetAvgUnitPrice())
				&& isEmptyPastCalcList(calculateDto.getPastDealYearlyCalcDtoList())
				&& isEmptyBenchLowCalcList(calculateDto.getBenchmarkLowYearlyCalcDtoList())
				&& isEmptyBenchTargetCalcList(calculateDto.getBenchmarkTargetYearlyCalcDtoList())) {
				empty = true;
			}
		}
		return empty;
	}

	private static boolean isEmptyBenchTargetPrice(BigDecimal price) {
		return price == null? true:(price.intValue() == 0 ? true:false);
	}

	private static boolean isEmptyBenchLowPrice(BigDecimal price) {
		return price == null? true:(price.intValue() == 0 ? true:false);
	}

	private static boolean isEmptyPastPrice(BigDecimal price) {
		return price == null? true:(price.intValue() == 0 ? true:false);
	}

	private static boolean isEmptyBenchTargetCalcList(List<YearlyCalculateDto> calcList) {
		return CollectionUtils.isEmpty(calcList)? true:false;
	}

	private static boolean isEmptyBenchLowCalcList(List<YearlyCalculateDto> calcList) {
		return CollectionUtils.isEmpty(calcList)? true:false;
	}

	private static boolean isEmptyPastCalcList(List<YearlyCalculateDto> calcList) {
		return CollectionUtils.isEmpty(calcList)? true:false;
	}


}