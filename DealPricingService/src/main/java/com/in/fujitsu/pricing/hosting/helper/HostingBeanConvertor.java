package com.in.fujitsu.pricing.hosting.helper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.hosting.dto.HostingCoLocationInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingMigrationCostInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingPriceDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingSolutionInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingUnitPriceInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingYearlyDataInfoDto;
import com.in.fujitsu.pricing.hosting.entity.HostingCoLocationInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingMigrationCostInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingRevenueInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingSolutionInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingUnitPriceInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.repository.HostingYearlyRepository;

@Component
public class HostingBeanConvertor {

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private HostingYearlyRepository hostingYearlyRepository;

	/**
	 * @param coLocations
	 * @return
	 */
	public List<HostingCoLocationInfoDto> prepareCoLocationDtoList(List<HostingCoLocationInfo> coLocations) {
		final List<HostingCoLocationInfoDto> coLocationsDtoList = new ArrayList<>();
		for (HostingCoLocationInfo coLocationInfo : coLocations) {
			final HostingCoLocationInfoDto coLocationDto = modelMapper.map(coLocationInfo,
					HostingCoLocationInfoDto.class);
			coLocationsDtoList.add(coLocationDto);
		}
		return coLocationsDtoList;
	}

	public List<HostingSolutionInfoDto> prepareSolutionDtoList(List<HostingSolutionInfo> solutions) {
		final List<HostingSolutionInfoDto> solutiontionDtoList = new ArrayList<>();
		for (HostingSolutionInfo solutionInfo : solutions) {
			final HostingSolutionInfoDto solutionDto = modelMapper.map(solutionInfo, HostingSolutionInfoDto.class);
			solutiontionDtoList.add(solutionDto);
		}
		return solutiontionDtoList;
	}

	public HostingInfo prepareHostingInfo(HostingInfo hostingInfo, HostingInfoDto hostingInfoDto, boolean isSave) {
		if (isSave) {
			final DealInfo dealInfo = new DealInfo();
			dealInfo.setDealId(hostingInfoDto.getDealId());
			hostingInfo.setDealInfo(dealInfo);
		}

		hostingInfo.setOffshoreAllowed(hostingInfoDto.isOffshoreAllowed());
		hostingInfo.setLevelOfService(hostingInfoDto.getLevelOfService());
		hostingInfo.setIncludeHardware(hostingInfoDto.isIncludeHardware());
		hostingInfo.setIncludeTooling(hostingInfoDto.isIncludeTooling());
		hostingInfo.setCoLocation(hostingInfoDto.getCoLocation());
		hostingInfo.setSelectedSolutionId(hostingInfoDto.getSelectedSolutionId());
		hostingInfo.setLevelIndicator(hostingInfoDto.getLevelIndicator());
		hostingInfo.setTowerArchitect(hostingInfoDto.getTowerArchitect() !=null ?  hostingInfoDto.getTowerArchitect() : hostingInfo.getTowerArchitect());

		List<HostingYearlyDataInfo> hostingYearlyDataInfoList = new ArrayList<>();

		// To do Past deal entries in yearly , unit & Revenue tables
		if (!CollectionUtils.isEmpty(hostingInfoDto.getHostingYearlyDataInfoDtoList())) {
			int yearlyDtoListSize = hostingInfoDto.getHostingYearlyDataInfoDtoList().size();
			for (int i = 0; i < yearlyDtoListSize; i++) {
				HostingYearlyDataInfoDto hostingYearlyDataInfoDto = hostingInfoDto.getHostingYearlyDataInfoDtoList()
						.get(i);
				HostingYearlyDataInfo hostingYearlyDataInfo = null;
				if (!isSave && (!CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())
						&& hostingInfo.getHostingYearlyDataInfoList().size() - 1 >= i)) {
					for (int j = 0; j < hostingInfo.getHostingYearlyDataInfoList().size(); j++) {
						HostingYearlyDataInfo existingHostingYearlyDataInfo = hostingInfo.getHostingYearlyDataInfoList()
								.get(j);
						if (existingHostingYearlyDataInfo.getYear().equals(hostingYearlyDataInfoDto.getYear())) {
							hostingYearlyDataInfo = existingHostingYearlyDataInfo;
							break;
						}
					}
				} else {
					hostingYearlyDataInfo = new HostingYearlyDataInfo();
				}
				hostingYearlyDataInfoList = setHostingYearlyDetails(hostingYearlyDataInfoList, hostingInfo,
						hostingYearlyDataInfo, hostingYearlyDataInfoDto);
			}

			// In case deal term is reduced
			if (!isSave && !CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())
					&& yearlyDtoListSize < hostingInfo.getHostingYearlyDataInfoList().size()) {
				for (int i = hostingInfo.getHostingYearlyDataInfoList().size() - 1; i >= yearlyDtoListSize; i--) {
					hostingYearlyRepository.delete(hostingInfo.getHostingYearlyDataInfoList().get(i).getYearId());
				}
			}
		}

		hostingInfo.setHostingYearlyDataInfoList(hostingYearlyDataInfoList);

		return hostingInfo;
	}

	private List<HostingYearlyDataInfo> setHostingYearlyDetails(List<HostingYearlyDataInfo> hostingYearlyDataInfoList,
			HostingInfo hostingInfo, HostingYearlyDataInfo hostingYearlyDataInfo,
			HostingYearlyDataInfoDto hostingYearlyDataInfoDto) {

		hostingYearlyDataInfo.setYear(hostingYearlyDataInfoDto.getYear());
		hostingYearlyDataInfo
				.setServers(hostingYearlyDataInfoDto.getServers() == null ? 0 : hostingYearlyDataInfoDto.getServers());
		hostingYearlyDataInfo.setPhysical(
				hostingYearlyDataInfoDto.getPhysical() == null ? 0 : hostingYearlyDataInfoDto.getPhysical());

		hostingYearlyDataInfo.setPhysicalWin(
				hostingYearlyDataInfoDto.getPhysicalWin() == null ? 0 : hostingYearlyDataInfoDto.getPhysicalWin());
		hostingYearlyDataInfo.setPhysicalWinSmall(hostingYearlyDataInfoDto.getPhysicalWinSmall() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalWinSmall());
		hostingYearlyDataInfo.setPhysicalWinMedium(hostingYearlyDataInfoDto.getPhysicalWinMedium() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalWinMedium());
		hostingYearlyDataInfo.setPhysicalWinLarge(hostingYearlyDataInfoDto.getPhysicalWinLarge() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalWinLarge());

		hostingYearlyDataInfo.setPhysicalUnix(
				hostingYearlyDataInfoDto.getPhysicalUnix() == null ? 0 : hostingYearlyDataInfoDto.getPhysicalUnix());
		hostingYearlyDataInfo.setPhysicalUnixSmall(hostingYearlyDataInfoDto.getPhysicalUnixSmall() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalUnixSmall());
		hostingYearlyDataInfo.setPhysicalUnixMedium(hostingYearlyDataInfoDto.getPhysicalUnixMedium() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalUnixMedium());
		hostingYearlyDataInfo.setPhysicalUnixLarge(hostingYearlyDataInfoDto.getPhysicalUnixLarge() == null ? 0
				: hostingYearlyDataInfoDto.getPhysicalUnixLarge());

		hostingYearlyDataInfo
				.setVirtual(hostingYearlyDataInfoDto.getVirtual() == null ? 0 : hostingYearlyDataInfoDto.getVirtual());
		hostingYearlyDataInfo.setVirtualPublic(
				hostingYearlyDataInfoDto.getVirtualPublic() == null ? 0 : hostingYearlyDataInfoDto.getVirtualPublic());

		hostingYearlyDataInfo.setVirtualPublicWin(hostingYearlyDataInfoDto.getVirtualPublicWin() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicWin());
		hostingYearlyDataInfo.setVirtualPublicWinSmall(hostingYearlyDataInfoDto.getVirtualPublicWinSmall() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicWinSmall());
		hostingYearlyDataInfo.setVirtualPublicWinMedium(hostingYearlyDataInfoDto.getVirtualPublicWinMedium() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicWinMedium());
		hostingYearlyDataInfo.setVirtualPublicWinLarge(hostingYearlyDataInfoDto.getVirtualPublicWinLarge() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicWinLarge());

		hostingYearlyDataInfo.setVirtualPublicUnix(hostingYearlyDataInfoDto.getVirtualPublicUnix() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicUnix());
		hostingYearlyDataInfo.setVirtualPublicUnixSmall(hostingYearlyDataInfoDto.getVirtualPublicUnixSmall() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicUnixSmall());
		hostingYearlyDataInfo.setVirtualPublicUnixMedium(hostingYearlyDataInfoDto.getVirtualPublicUnixMedium() == null
				? 0 : hostingYearlyDataInfoDto.getVirtualPublicUnixMedium());
		hostingYearlyDataInfo.setVirtualPublicUnixLarge(hostingYearlyDataInfoDto.getVirtualPublicUnixLarge() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPublicUnixLarge());

		hostingYearlyDataInfo.setVirtualPrivate(hostingYearlyDataInfoDto.getVirtualPrivate() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPrivate());

		hostingYearlyDataInfo.setVirtualPrivateWin(hostingYearlyDataInfoDto.getVirtualPrivateWin() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPrivateWin());
		hostingYearlyDataInfo.setVirtualPrivateWinSmall(hostingYearlyDataInfoDto.getVirtualPrivateWinSmall() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPrivateWinSmall());
		hostingYearlyDataInfo.setVirtualPrivateWinMedium(hostingYearlyDataInfoDto.getVirtualPrivateWinMedium() == null
				? 0 : hostingYearlyDataInfoDto.getVirtualPrivateWinMedium());
		hostingYearlyDataInfo.setVirtualPrivateWinLarge(hostingYearlyDataInfoDto.getVirtualPrivateWinLarge() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPrivateWinLarge());

		hostingYearlyDataInfo.setVirtualPrivateUnix(hostingYearlyDataInfoDto.getVirtualPrivateUnix() == null ? 0
				: hostingYearlyDataInfoDto.getVirtualPrivateUnix());
		hostingYearlyDataInfo.setVirtualPrivateUnixSmall(hostingYearlyDataInfoDto.getVirtualPrivateUnixSmall() == null
				? 0 : hostingYearlyDataInfoDto.getVirtualPrivateUnixSmall());
		hostingYearlyDataInfo.setVirtualPrivateUnixMedium(hostingYearlyDataInfoDto.getVirtualPrivateUnixMedium() == null
				? 0 : hostingYearlyDataInfoDto.getVirtualPrivateUnixMedium());
		hostingYearlyDataInfo.setVirtualPrivateUnixLarge(hostingYearlyDataInfoDto.getVirtualPrivateUnixLarge() == null
				? 0 : hostingYearlyDataInfoDto.getVirtualPrivateUnixLarge());

		hostingYearlyDataInfo.setSqlInstances(
				hostingYearlyDataInfoDto.getSqlInstances() == null ? 0 : hostingYearlyDataInfoDto.getSqlInstances());
		hostingYearlyDataInfo.setCotsInstallations(hostingYearlyDataInfoDto.getCotsInstallations() == null ? 0
				: hostingYearlyDataInfoDto.getCotsInstallations());

		hostingYearlyDataInfo.setHostingInfo(hostingInfo);

		// set the Unit Price
		List<HostingUnitPriceInfo> hostingUnitPriceInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hostingYearlyDataInfoDto.getHostingUnitPriceInfoDtoList())) {
			if (!CollectionUtils.isEmpty(hostingYearlyDataInfo.getHostingUnitPriceInfoList())) {
				for (HostingUnitPriceInfo hostingUnitPriceInfo : hostingYearlyDataInfo.getHostingUnitPriceInfoList()) {
					HostingUnitPriceInfoDto hostingUnitPriceInfoDto = hostingYearlyDataInfoDto
							.getHostingUnitPriceInfoDtoList().get(0);
					if (hostingUnitPriceInfoDto != null) {
						setUnitPrices(hostingUnitPriceInfo, hostingUnitPriceInfoDto);
						hostingUnitPriceInfoList.add(hostingUnitPriceInfo);
					}
				}
			} else {
				for (HostingUnitPriceInfoDto hostingUnitPriceInfoDto : hostingYearlyDataInfoDto
						.getHostingUnitPriceInfoDtoList()) {
					HostingUnitPriceInfo hostingUnitPriceInfo = new HostingUnitPriceInfo();
					setUnitPrices(hostingUnitPriceInfo, hostingUnitPriceInfoDto);
					
					hostingUnitPriceInfo.setHostingYearlyDataInfo(hostingYearlyDataInfo);
					hostingUnitPriceInfoList.add(hostingUnitPriceInfo);
				}
			}
			hostingYearlyDataInfo.setHostingUnitPriceInfoList(hostingUnitPriceInfoList);
		}


		// set the Revenue
		List<HostingRevenueInfo> hostingRevenueInfoList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hostingYearlyDataInfoDto.getHostingRevenueInfoDtoList())) {
			if (!CollectionUtils.isEmpty(hostingYearlyDataInfo.getHostingRevenueInfoList())) {
				for (HostingRevenueInfo hostingRevenueInfo : hostingYearlyDataInfo.getHostingRevenueInfoList()) {
					HostingRevenueInfoDto hostingRevenueInfoDto = hostingYearlyDataInfoDto
							.getHostingRevenueInfoDtoList().get(0);
					if (hostingRevenueInfoDto != null) {
						setRevenues(hostingRevenueInfo, hostingRevenueInfoDto);
						hostingRevenueInfoList.add(hostingRevenueInfo);
					}
				}
			} else {
				for (HostingRevenueInfoDto hostingRevenueInfoDto : hostingYearlyDataInfoDto
						.getHostingRevenueInfoDtoList()) {
					HostingRevenueInfo hostingRevenueInfo = new HostingRevenueInfo();
					setRevenues(hostingRevenueInfo, hostingRevenueInfoDto);
					
					hostingRevenueInfo.setHostingYearlyDataInfo(hostingYearlyDataInfo);
					hostingRevenueInfoList.add(hostingRevenueInfo);
				}
			}
			hostingYearlyDataInfo.setHostingRevenueInfoList(hostingRevenueInfoList);
		}

		hostingYearlyDataInfoList.add(hostingYearlyDataInfo);

		return hostingYearlyDataInfoList;
	}

	private void setRevenues(HostingRevenueInfo hostingRevenueInfo, HostingRevenueInfoDto hostingRevenueInfoDto) {
		hostingRevenueInfo.setBenchMarkType(hostingRevenueInfoDto.getBenchMarkType());
		hostingRevenueInfo.setServers(
				hostingRevenueInfoDto.getServers() == null ? 0 : hostingRevenueInfoDto.getServers());
		hostingRevenueInfo.setSqlInstances(hostingRevenueInfoDto.getSqlInstances() == null ? 0
				: hostingRevenueInfoDto.getSqlInstances());
		hostingRevenueInfo.setCotsInstallations(hostingRevenueInfoDto.getCotsInstallations() == null ? 0
				: hostingRevenueInfoDto.getCotsInstallations());
	}

	/**
	 * @param hostingUnitPriceInfo
	 * @param hostingUnitPriceInfoDto
	 */
	private void setUnitPrices(HostingUnitPriceInfo hostingUnitPriceInfo,
			HostingUnitPriceInfoDto hostingUnitPriceInfoDto) {
		hostingUnitPriceInfo.setBenchMarkType(hostingUnitPriceInfoDto.getBenchMarkType());

		hostingUnitPriceInfo.setServers(
				hostingUnitPriceInfoDto.getServers() == null ? BigDecimal.ZERO : hostingUnitPriceInfoDto.getServers());
		hostingUnitPriceInfo.setPhysical(hostingUnitPriceInfoDto.getPhysical() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getPhysical());

		hostingUnitPriceInfo.setPhysicalWin(hostingUnitPriceInfoDto.getPhysicalWin() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getPhysicalWin());
		hostingUnitPriceInfo.setPhysicalWinSmall(hostingUnitPriceInfoDto.getPhysicalWinSmall() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getPhysicalWinSmall());
		hostingUnitPriceInfo.setPhysicalWinMedium(hostingUnitPriceInfoDto.getPhysicalWinMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getPhysicalWinMedium());
		hostingUnitPriceInfo.setPhysicalWinLarge(hostingUnitPriceInfoDto.getPhysicalWinLarge() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getPhysicalWinLarge());

		hostingUnitPriceInfo.setPhysicalUnix(hostingUnitPriceInfoDto.getPhysicalUnix() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getPhysicalUnix());
		hostingUnitPriceInfo.setPhysicalUnixSmall(hostingUnitPriceInfoDto.getPhysicalUnixSmall() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getPhysicalUnixSmall());
		hostingUnitPriceInfo.setPhysicalUnixMedium(hostingUnitPriceInfoDto.getPhysicalUnixMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getPhysicalUnixMedium());
		hostingUnitPriceInfo.setPhysicalUnixLarge(hostingUnitPriceInfoDto.getPhysicalUnixLarge() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getPhysicalUnixLarge());

		hostingUnitPriceInfo.setVirtual(
				hostingUnitPriceInfoDto.getVirtual() == null ? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtual());
		hostingUnitPriceInfo.setVirtualPublic(hostingUnitPriceInfoDto.getVirtualPublic() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getVirtualPublic());

		hostingUnitPriceInfo.setVirtualPublicWin(hostingUnitPriceInfoDto.getVirtualPublicWin() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getVirtualPublicWin());
		hostingUnitPriceInfo.setVirtualPublicWinSmall(hostingUnitPriceInfoDto.getVirtualPublicWinSmall() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicWinSmall());
		hostingUnitPriceInfo.setVirtualPublicWinMedium(hostingUnitPriceInfoDto.getVirtualPublicWinMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicWinMedium());
		hostingUnitPriceInfo.setVirtualPublicWinLarge(hostingUnitPriceInfoDto.getVirtualPublicWinLarge() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicWinLarge());

		hostingUnitPriceInfo.setVirtualPublicUnix(hostingUnitPriceInfoDto.getVirtualPublicUnix() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicUnix());
		hostingUnitPriceInfo.setVirtualPublicUnixSmall(hostingUnitPriceInfoDto.getVirtualPublicUnixSmall() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicUnixSmall());
		hostingUnitPriceInfo.setVirtualPublicUnixMedium(hostingUnitPriceInfoDto.getVirtualPublicUnixMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicUnixMedium());
		hostingUnitPriceInfo.setVirtualPublicUnixLarge(hostingUnitPriceInfoDto.getVirtualPublicUnixLarge() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPublicUnixLarge());

		hostingUnitPriceInfo.setVirtualPrivate(hostingUnitPriceInfoDto.getVirtualPrivate() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getVirtualPrivate());

		hostingUnitPriceInfo.setVirtualPrivateWin(hostingUnitPriceInfoDto.getVirtualPrivateWin() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateWin());
		hostingUnitPriceInfo.setVirtualPrivateWinSmall(hostingUnitPriceInfoDto.getVirtualPrivateWinSmall() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateWinSmall());
		hostingUnitPriceInfo.setVirtualPrivateWinMedium(hostingUnitPriceInfoDto.getVirtualPrivateWinMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateWinMedium());
		hostingUnitPriceInfo.setVirtualPrivateWinLarge(hostingUnitPriceInfoDto.getVirtualPrivateWinLarge() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateWinLarge());

		hostingUnitPriceInfo.setVirtualPrivateUnix(hostingUnitPriceInfoDto.getVirtualPrivateUnix() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateUnix());
		hostingUnitPriceInfo.setVirtualPrivateUnixSmall(hostingUnitPriceInfoDto.getVirtualPrivateUnixSmall() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateUnixSmall());
		hostingUnitPriceInfo.setVirtualPrivateUnixMedium(hostingUnitPriceInfoDto.getVirtualPrivateUnixMedium() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateUnixMedium());
		hostingUnitPriceInfo.setVirtualPrivateUnixLarge(hostingUnitPriceInfoDto.getVirtualPrivateUnixLarge() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getVirtualPrivateUnixLarge());

		hostingUnitPriceInfo.setSqlInstances(hostingUnitPriceInfoDto.getSqlInstances() == null ? BigDecimal.ZERO
				: hostingUnitPriceInfoDto.getSqlInstances());
		hostingUnitPriceInfo.setCotsInstallations(hostingUnitPriceInfoDto.getCotsInstallations() == null
				? BigDecimal.ZERO : hostingUnitPriceInfoDto.getCotsInstallations());
	}

	public HostingInfoDto prepareHostingInfoDto(HostingInfo hostingInfo) {
		HostingInfoDto hostingInfoDto = new HostingInfoDto();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		if (hostingInfo != null) {
			hostingInfoDto = modelMapper.map(hostingInfo, HostingInfoDto.class);
			hostingInfoDto.setDealId(hostingInfo.getDealInfo().getDealId());

			final List<HostingYearlyDataInfoDto> hostingYearlyDataInfoDtoList = new ArrayList<>();
			for (HostingYearlyDataInfo hostingYearlyDataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
				final HostingYearlyDataInfoDto hostingYearlyDataInfoDto = modelMapper.map(hostingYearlyDataInfo,
						HostingYearlyDataInfoDto.class);
				if (!CollectionUtils.isEmpty(hostingYearlyDataInfo.getHostingUnitPriceInfoList())) {
					final List<HostingUnitPriceInfoDto> hostingUnitPriceInfoDtoList = new ArrayList<>();
					for (HostingUnitPriceInfo hostingUnitPriceInfo : hostingYearlyDataInfo
							.getHostingUnitPriceInfoList()) {
						final HostingUnitPriceInfoDto hostingUnitPriceInfoDto = modelMapper.map(hostingUnitPriceInfo,
								HostingUnitPriceInfoDto.class);
						hostingUnitPriceInfoDtoList.add(hostingUnitPriceInfoDto);
					}
					hostingYearlyDataInfoDto.setHostingUnitPriceInfoDtoList(hostingUnitPriceInfoDtoList);
				}

				if (!CollectionUtils.isEmpty(hostingYearlyDataInfo.getHostingRevenueInfoList())) {
					final List<HostingRevenueInfoDto> hostingRevenueInfoDtoList = new ArrayList<>();
					for (HostingRevenueInfo hostingRevenueInfo : hostingYearlyDataInfo.getHostingRevenueInfoList()) {
						final HostingRevenueInfoDto hostingRevenueInfoDto = modelMapper.map(hostingRevenueInfo,
								HostingRevenueInfoDto.class);
						hostingRevenueInfoDtoList.add(hostingRevenueInfoDto);
					}
					hostingYearlyDataInfoDto.setHostingRevenueInfoDtoList(hostingRevenueInfoDtoList);
				}
				hostingYearlyDataInfoDtoList.add(hostingYearlyDataInfoDto);
			}

			hostingInfoDto.setHostingYearlyDataInfoDtoList(hostingYearlyDataInfoDtoList);
		}
		return hostingInfoDto;
	}

	/**
	 * @param hostingInfo
	 * @param solutionCriteriaDto
	 * @return
	 */
	public HostingInfo prepareSolutionCriteria(HostingInfo hostingInfo, SolutionCriteriaDto solutionCriteriaDto) {
		hostingInfo.setOffshoreAllowed(solutionCriteriaDto.isOffshoreAllowed());
		hostingInfo.setLevelOfService(solutionCriteriaDto.getLevelOfService());
		hostingInfo.setIncludeHardware(solutionCriteriaDto.isIncludeHardware());
		hostingInfo.setIncludeTooling(solutionCriteriaDto.isToolingIncluded());
		hostingInfo.setCoLocation(solutionCriteriaDto.getCoLocation());
		return hostingInfo;
	}

	/**
	 * @param hostingInfo
	 * @param hostingPriceDtoList
	 * @return
	 */
	public HostingInfo prepareHostingPrice(HostingInfo hostingInfo, List<HostingPriceDto> hostingPriceDtoList) {
		List<HostingYearlyDataInfo> hostingYearlyDataInfoList = new ArrayList<>();
		HostingPriceDto hostingPriceDto = new HostingPriceDto();
		for (HostingYearlyDataInfo yearlydataInfo : hostingInfo.getHostingYearlyDataInfoList()) {
			for (HostingPriceDto priceDto : hostingPriceDtoList) {
				if (yearlydataInfo.getYear().equals(priceDto.getYear())) {
					hostingPriceDto = priceDto;
					break;
				}
			}
			hostingYearlyDataInfoList = updateHostingYearlyDetails(hostingYearlyDataInfoList,
					yearlydataInfo, hostingPriceDto);
		}

		hostingInfo.setHostingYearlyDataInfoList(hostingYearlyDataInfoList);

		return hostingInfo;
	}

	/**
	 * @param yearlyDataInfoList
	 * @param yearlydataInfo
	 * @param hostingPriceDto
	 * @return
	 */
	private List<HostingYearlyDataInfo> updateHostingYearlyDetails(
			List<HostingYearlyDataInfo> yearlyDataInfoList, HostingYearlyDataInfo yearlydataInfo,
			HostingPriceDto hostingPriceDto) {

		if (!CollectionUtils.isEmpty(yearlydataInfo.getHostingUnitPriceInfoList())) {
			// Update the existing unit prices
			for (HostingUnitPriceInfo unitPriceInfo : yearlydataInfo.getHostingUnitPriceInfoList()) {
				mapUnitPriceFromPriceDtoToInfo(hostingPriceDto, unitPriceInfo);
			}
		} else {
			// Case when first time assessment is done
			List<HostingUnitPriceInfo> unitPriceInfoList = new ArrayList<>();
			HostingUnitPriceInfo unitPriceInfo = new HostingUnitPriceInfo();
			mapUnitPriceFromPriceDtoToInfo(hostingPriceDto, unitPriceInfo);
			unitPriceInfo.setHostingYearlyDataInfo(yearlydataInfo);
			unitPriceInfoList.add(unitPriceInfo);
			yearlydataInfo.setHostingUnitPriceInfoList(unitPriceInfoList);
		}


		if (!CollectionUtils.isEmpty(yearlydataInfo.getHostingRevenueInfoList())) {
			// Update the existing revenues
			for (HostingRevenueInfo revenueInfo : yearlydataInfo.getHostingRevenueInfoList()) {
				revenueInfo.setServers(hostingPriceDto.getServersRevenue());
				revenueInfo.setSqlInstances(hostingPriceDto.getSqlInstancesRevenue());
				revenueInfo.setCotsInstallations(hostingPriceDto.getCotsInstallationsRevenue());
			}
		} else {
			// Case when first time assessment is done
			List<HostingRevenueInfo> revenueInfoList = new ArrayList<>();
			HostingRevenueInfo revenueInfo = new HostingRevenueInfo();
			revenueInfo.setServers(hostingPriceDto.getServersRevenue());
			revenueInfo.setSqlInstances(hostingPriceDto.getSqlInstancesRevenue());
			revenueInfo.setCotsInstallations(hostingPriceDto.getCotsInstallationsRevenue());

			revenueInfo.setHostingYearlyDataInfo(yearlydataInfo);

			revenueInfoList.add(revenueInfo);
			yearlydataInfo.setHostingRevenueInfoList(revenueInfoList);
		}

		yearlyDataInfoList.add(yearlydataInfo);

		return yearlyDataInfoList;
	}

	/**
	 * @param hostingPriceDto
	 * @param unitPriceInfo
	 */
	private void mapUnitPriceFromPriceDtoToInfo(HostingPriceDto hostingPriceDto, HostingUnitPriceInfo unitPriceInfo) {
		unitPriceInfo.setServers(hostingPriceDto.getServers());
		unitPriceInfo.setPhysical(hostingPriceDto.getPhysical());

		unitPriceInfo.setPhysicalWin(hostingPriceDto.getPhysicalWin());
		unitPriceInfo.setPhysicalWinSmall(hostingPriceDto.getPhysicalWinSmall());
		unitPriceInfo.setPhysicalWinMedium(hostingPriceDto.getPhysicalWinMedium());
		unitPriceInfo.setPhysicalWinLarge(hostingPriceDto.getPhysicalWinLarge());

		unitPriceInfo.setPhysicalUnix(hostingPriceDto.getPhysicalUnix());
		unitPriceInfo.setPhysicalUnixSmall(hostingPriceDto.getPhysicalUnixSmall());
		unitPriceInfo.setPhysicalUnixMedium(hostingPriceDto.getPhysicalUnixMedium());
		unitPriceInfo.setPhysicalUnixLarge(hostingPriceDto.getPhysicalUnixLarge());

		unitPriceInfo.setVirtual(hostingPriceDto.getVirtual());
		unitPriceInfo.setVirtualPublic(hostingPriceDto.getVirtualPublic());

		unitPriceInfo.setVirtualPublicWin(hostingPriceDto.getVirtualPublicWin());
		unitPriceInfo.setVirtualPublicWinSmall(hostingPriceDto.getVirtualPublicWinSmall());
		unitPriceInfo.setVirtualPublicWinMedium(hostingPriceDto.getVirtualPublicWinMedium());
		unitPriceInfo.setVirtualPublicWinLarge(hostingPriceDto.getVirtualPublicWinLarge());

		unitPriceInfo.setVirtualPublicUnix(hostingPriceDto.getVirtualPublicUnix());
		unitPriceInfo.setVirtualPublicUnixSmall(hostingPriceDto.getVirtualPublicUnixSmall());
		unitPriceInfo.setVirtualPublicUnixMedium(hostingPriceDto.getVirtualPublicUnixMedium());
		unitPriceInfo.setVirtualPublicUnixLarge(hostingPriceDto.getVirtualPublicUnixLarge());

		unitPriceInfo.setVirtualPrivate(hostingPriceDto.getVirtualPrivate());

		unitPriceInfo.setVirtualPrivateWin(hostingPriceDto.getVirtualPrivateWin());
		unitPriceInfo.setVirtualPrivateWinSmall(hostingPriceDto.getVirtualPrivateWinSmall());
		unitPriceInfo.setVirtualPrivateWinMedium(hostingPriceDto.getVirtualPrivateWinMedium());
		unitPriceInfo.setVirtualPrivateWinLarge(hostingPriceDto.getVirtualPrivateWinLarge());

		unitPriceInfo.setVirtualPrivateUnix(hostingPriceDto.getVirtualPrivateUnix());
		unitPriceInfo.setVirtualPrivateUnixSmall(hostingPriceDto.getVirtualPrivateUnixSmall());
		unitPriceInfo.setVirtualPrivateUnixMedium(hostingPriceDto.getVirtualPrivateUnixMedium());
		unitPriceInfo.setVirtualPrivateUnixLarge(hostingPriceDto.getVirtualPrivateUnixLarge());

		unitPriceInfo.setSqlInstances(hostingPriceDto.getSqlInstances());
		unitPriceInfo.setCotsInstallations(hostingPriceDto.getCotsInstallations());
	}

	/**
	 * @param migrationCostList
	 * @return
	 */
	public List<HostingMigrationCostInfoDto> convertMigrationCostInfoToDto(
			List<HostingMigrationCostInfo> migrationCostList) {
		List<HostingMigrationCostInfoDto> costDtoList = new ArrayList<>();
		HostingMigrationCostInfoDto dto = null;
		for(int i=0; i < migrationCostList.size() ; i++){
			dto = new HostingMigrationCostInfoDto();
			dto.setLower(migrationCostList.get(i).getLower());
			dto.setUpper(migrationCostList.get(i).getUpper());
			dto.setCost(migrationCostList.get(i).getCost());
			if(i==0){
				dto.setDifference(migrationCostList.get(i).getUpper());
			} else{
				dto.setDifference(migrationCostList.get(i).getUpper().subtract(migrationCostList.get(i-1).getUpper()));
			}
			costDtoList.add(dto);
		}
		return costDtoList;
	}

}
