package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.hosting.calculator.CotsInstallationsCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalUnixCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalUnixLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalUnixMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalUnixSmallCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalWinCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalWinLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalWinMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.PhysicalWinSmallCalculator;
import com.in.fujitsu.pricing.hosting.calculator.ServersCalculator;
import com.in.fujitsu.pricing.hosting.calculator.SqlInstancesCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateUnixCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateUnixLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateUnixMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateUnixSmallCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateWinCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateWinLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateWinMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPrivateWinSmallCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicUnixCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicUnixLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicUnixMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicUnixSmallCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicWinCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicWinLargeCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicWinMediumCalculator;
import com.in.fujitsu.pricing.hosting.calculator.VirtualPublicWinSmallCalculator;
import com.in.fujitsu.pricing.hosting.dto.HostingCalculateDto;
import com.in.fujitsu.pricing.hosting.dto.HostingDropdownDto;
import com.in.fujitsu.pricing.hosting.dto.HostingInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingPriceDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueDto;
import com.in.fujitsu.pricing.hosting.entity.HostingCoLocationInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingSolutionInfo;
import com.in.fujitsu.pricing.hosting.helper.CotsHelper;
import com.in.fujitsu.pricing.hosting.helper.HostingBeanConvertor;
import com.in.fujitsu.pricing.hosting.helper.PhysicalHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalUnixHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalUnixLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalUnixMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalUnixSmallHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalWinHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalWinLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalWinMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.PhysicalWinSmallHelper;
import com.in.fujitsu.pricing.hosting.helper.ServerHelper;
import com.in.fujitsu.pricing.hosting.helper.SqlInstancesHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivUnixHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivUnixLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivUnixMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivUnixSmallHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivWinHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivWinLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivWinMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPrivWinSmallHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubUnixHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubUnixLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubUnixMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubUnixSmallHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubWinHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubWinLargeHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubWinMediumHelper;
import com.in.fujitsu.pricing.hosting.helper.VirtualPubWinSmallHelper;
import com.in.fujitsu.pricing.hosting.repository.HostingCoLocationRepository;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.hosting.repository.HostingSolutionsRepository;
import com.in.fujitsu.pricing.repository.CountryFactorRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.TowerSpecificBandRepository;
import com.in.fujitsu.pricing.utility.CommonHelper;

@Service
public class HostingService {

	@Autowired
	private HostingRepository hostingRepository;

	@Autowired
	private HostingBeanConvertor beanConvertor;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private GenericService genericService;

	@Autowired
	private HostingCoLocationRepository coLocationRepository;

	@Autowired
	private HostingSolutionsRepository solutionRepository;

	@Autowired
	private ServersCalculator serversCalculator;

	@Autowired
	private PhysicalCalculator physicalCalculator;

	@Autowired
	private PhysicalWinCalculator physicalWinCalculator;

	@Autowired
	private PhysicalWinSmallCalculator physicalWinSmallCalculator;

	@Autowired
	private PhysicalWinMediumCalculator physicalWinMediumCalculator;

	@Autowired
	private PhysicalWinLargeCalculator physicalWinLargeCalculator;

	@Autowired
	private PhysicalUnixCalculator physicalUnixCalculator;

	@Autowired
	private PhysicalUnixSmallCalculator physicalUnixSmallCalculator;

	@Autowired
	private PhysicalUnixMediumCalculator physicalUnixMediumCalculator;

	@Autowired
	private PhysicalUnixLargeCalculator physicalUnixLargeCalculator;

	@Autowired
	private VirtualCalculator virtualCalculator;

	@Autowired
	private VirtualPublicCalculator virtualPublicCalculator;

	@Autowired
	private VirtualPublicWinCalculator virtualPublicWinCalculator;

	@Autowired
	private VirtualPublicWinSmallCalculator virtualPublicWinSmallCalculator;

	@Autowired
	private  VirtualPublicWinMediumCalculator virtualPublicWinMediumCalculator;

	@Autowired
	private VirtualPublicWinLargeCalculator virtualPublicWinLargeCalculator;

	@Autowired
	private VirtualPublicUnixCalculator virtualPublicUnixCalculator;

	@Autowired
	private VirtualPublicUnixSmallCalculator virtualPublicUnixSmallCalculator;

	@Autowired
	private VirtualPublicUnixMediumCalculator virtualPublicUnixMediumCalculator;

	@Autowired
	private VirtualPublicUnixLargeCalculator virtualPublicUnixLargeCalculator;

	@Autowired
	private VirtualPrivateCalculator virtualPrivateCalculator;

	@Autowired
	private VirtualPrivateWinCalculator virtualPrivateWinCalculator;

	@Autowired
	private VirtualPrivateWinSmallCalculator virtualPrivateWinSmallCalculator;

	@Autowired
	private  VirtualPrivateWinMediumCalculator virtualPrivateWinMediumCalculator;

	@Autowired
	private VirtualPrivateWinLargeCalculator virtualPrivateWinLargeCalculator;

	@Autowired
	private VirtualPrivateUnixCalculator virtualPrivateUnixCalculator;

	@Autowired
	private VirtualPrivateUnixSmallCalculator virtualPrivateUnixSmallCalculator;

	@Autowired
	private VirtualPrivateUnixMediumCalculator virtualPrivateUnixMediumCalculator;

	@Autowired
	private VirtualPrivateUnixLargeCalculator virtualPrivateUnixLargeCalculator;

	@Autowired
	private SqlInstancesCalculator sqlInstancesCalculator;

	@Autowired
	private CotsInstallationsCalculator cotsInstallationsCalculator;

	@Autowired
	private ServerHelper serverHelper;

	@Autowired
	private PhysicalHelper physicalHelper;

	@Autowired
	private PhysicalWinHelper physicalWinHelper;

	@Autowired
	private PhysicalWinSmallHelper physicalWinSmallHelper;

	@Autowired
	private PhysicalWinMediumHelper physicalWinMediumHelper;

	@Autowired
	private PhysicalWinLargeHelper physicalWinLargeHelper;

	@Autowired
	private PhysicalUnixHelper physicalUnixHelper;

	@Autowired
	private PhysicalUnixSmallHelper physicalUnixSmallHelper;

	@Autowired
	private PhysicalUnixMediumHelper physicalUnixMediumHelper;

	@Autowired
	private PhysicalUnixLargeHelper physicalUnixLargeHelper;

	@Autowired
	private VirtualHelper virtHelper;

	@Autowired
	private VirtualPubHelper virtPubHelper;

	@Autowired
	private VirtualPubWinHelper virtPubWinHelper;

	@Autowired
	private VirtualPubWinSmallHelper virtPubWinSmallHelper;

	@Autowired
	private VirtualPubWinMediumHelper virtPubWinMediumHelper;

	@Autowired
	private VirtualPubWinLargeHelper virtPubWinLargeHelper;

	@Autowired
	private VirtualPubUnixHelper virtPubUnixHelper;

	@Autowired
	private VirtualPubUnixSmallHelper virtPubUnixSmallHelper;

	@Autowired
	private VirtualPubUnixMediumHelper virtPubUnixMediumHelper;

	@Autowired
	private VirtualPubUnixLargeHelper virtPubUnixLargeHelper;

	@Autowired
	private VirtualPrivHelper virtPrivHelper;

	@Autowired
	private VirtualPrivWinHelper virtPrivWinHelper;

	@Autowired
	private VirtualPrivWinSmallHelper virtPrivWinSmallHelper;

	@Autowired
	private VirtualPrivWinMediumHelper virtPrivWinMediumHelper;

	@Autowired
	private VirtualPrivWinLargeHelper virtPrivWinLargeHelper;

	@Autowired
	private VirtualPrivUnixHelper virtPrivUnixHelper;

	@Autowired
	private VirtualPrivUnixSmallHelper virtPrivUnixSmallHelper;

	@Autowired
	private VirtualPrivUnixMediumHelper virtPrivUnixMediumHelper;

	@Autowired
	private VirtualPrivUnixLargeHelper virtPrivUnixLargeHelper;

	@Autowired
	private SqlInstancesHelper sqlHelper;

	@Autowired
	private CotsHelper cotsHelper;



	private final String HOSTING_TOWER = "Hosting";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";

	private final String SERVERS_LEVEL = "1.1";

	private final String PHYSICAL_LEVEL = "1.1.1";

	private final String PHYSICAL_WIN_LEVEL = "1.1.1.1";
	private final String PHYSICAL_WIN_SMALL_LEVEL = "1.1.1.1.1";
	private final String PHYSICAL_WIN_MEDIUM_LEVEL = "1.1.1.1.2";
	private final String PHYSICAL_WIN_LARGE_LEVEL = "1.1.1.1.3";

	private final String PHYSICAL_UNIX_LEVEL = "1.1.1.2";
	private final String PHYSICAL_UNIX_SMALL_LEVEL = "1.1.1.2.1";
	private final String PHYSICAL_UNIX_MEDIUM_LEVEL = "1.1.1.2.2";
	private final String PHYSICAL_UNIX_LARGE_LEVEL = "1.1.1.2.3";

	private final String VIRTUAL_LEVEL = "1.1.2";

	private final String VIRTUAL_PUBLIC_LEVEL = "1.1.2.1";

	private final String VIRTUAL_PUBLIC_WIN_LEVEL = "1.1.2.1.1";
	private final String VIRTUAL_PUBLIC_WIN_SMALL_LEVEL = "1.1.2.1.1.1";
	private final String VIRTUAL_PUBLIC_WIN_MEDIUM_LEVEL = "1.1.2.1.1.2";
	private final String VIRTUAL_PUBLIC_WIN_LARGE_LEVEL = "1.1.2.1.1.3";

	private final String VIRTUAL_PUBLIC_UNIX_LEVEL = "1.1.2.1.2";
	private final String VIRTUAL_PUBLIC_UNIX_SMALL_LEVEL = "1.1.2.1.2.1";
	private final String VIRTUAL_PUBLIC_UNIX_MEDIUM_LEVEL = "1.1.2.1.2.2";
	private final String VIRTUAL_PUBLIC_UNIX_LARGE_LEVEL = "1.1.2.1.2.3";

	private final String VIRTUAL_PRIVATE_LEVEL = "1.1.2.2";
	private final String VIRTUAL_PRIVATE_WIN_LEVEL = "1.1.2.2.1";
	private final String VIRTUAL_PRIVATE_WIN_SMALL_LEVEL = "1.1.2.2.1.1";
	private final String VIRTUAL_PRIVATE_WIN_MEDIUM_LEVEL = "1.1.2.2.1.2";
	private final String VIRTUAL_PRIVATE_WIN_LARGE_LEVEL = "1.1.2.2.1.3";

	private final String VIRTUAL_PRIVATE_UNIX_LEVEL = "1.1.2.2.2";
	private final String VIRTUAL_PRIVATE_UNIX_SMALL_LEVEL = "1.1.2.2.2.1";
	private final String VIRTUAL_PRIVATE_UNIX_MEDIUM_LEVEL = "1.1.2.2.2.2";
	private final String VIRTUAL_PRIVATE_UNIX_LARGE_LEVEL = "1.1.2.2.2.3";

	private final String SQL_LEVEL = "2.1";
	private final String COTS_LEVEL = "2.2";

	/**
	 * @param dealId
	 * @return
	 */
	public HostingDropdownDto getDropDownDetails(Long dealId) {
		HostingDropdownDto dropdownDto = new HostingDropdownDto();
		dropdownDto.setStandardWindowInfoList(genericService.getServicedWindowInfo());
		dropdownDto.setYesNoOptionList(genericService.getOffshreAndHardwareInfo());

		final List<HostingCoLocationInfo> coLocationInfoList = coLocationRepository.findAll();
		dropdownDto.setCoLocationDtoList(beanConvertor.prepareCoLocationDtoList(coLocationInfoList));

		final List<HostingSolutionInfo> solutionInfoList = solutionRepository.findAll();
		dropdownDto.setHostingSolutionsDtoList(beanConvertor.prepareSolutionDtoList(solutionInfoList));

		if (dealId != null) {
			dropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}
		return dropdownDto;
	}

	/**
	 * @param dealId
	 * @param hostingInfoDto
	 * @return
	 */
	@Transactional
	public HostingInfoDto saveDetails(Long dealId, HostingInfoDto hostingInfoDto) {
		HostingInfo hostingInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		if (null != hostingInfo) {
			hostingInfo = beanConvertor.prepareHostingInfo(hostingInfo, hostingInfoDto, false);
		} else {
			hostingInfo = beanConvertor.prepareHostingInfo(new HostingInfo(), hostingInfoDto, true);
		}
		hostingInfo = hostingRepository.saveAndFlush(hostingInfo);
		return beanConvertor.prepareHostingInfoDto(hostingInfo);
	}

	/**
	 * @param dealId
	 * @return
	 */
	public HostingInfoDto getDetails(Long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		if (hostingInfo == null) {
			throw new ServiceException("DB doesn't have the Hosting Details for given dealId.");
		}
		return beanConvertor.prepareHostingInfoDto(hostingInfo);
	}

	/**
	 * @param hostingPriceDtoList
	 * @param hostingId
	 * @return
	 */
	public ResponseEntity<Object> updateHostingPrice(List<HostingPriceDto> hostingPriceDtoList, Long hostingId)
			throws ServiceException {
		HostingInfo hostingInfo = hostingRepository.findOne(hostingId);
		if (null != hostingInfo) {
			hostingInfo = beanConvertor.prepareHostingPrice(hostingInfo, hostingPriceDtoList);
			hostingRepository.saveAndFlush(hostingInfo);
		} else {
			throw new ServiceException("No HostingInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully."), HttpStatus.OK);
	}

	/*Indices indicating level indicator
	 * 0 : Server hosting
	 * 1 : Server volume
	 * 2 : Physical
	 * 3 : Physical Win/Linux
	 * 4 : Physical Unix
	 * 5 : Virtual
	 * 6 : Public
	 * 7 : Public Win/Linux
	 * 8 : Public Unix
	 * 9 : Private
	 * 10: Private Win/Linux
	 * 11: Private Unix
	 * 12: Platform hosting
	 *
	 * */
	/**
	 * @param dealId
	 * @return
	 * @throws ServiceException
	 */
	public HostingRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		HostingRevenueDto revenueDto = new HostingRevenueDto();
		if (dealInfo != null) {

			HostingCalculateDto serversDto = null;

			HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
			List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
			// currency conversion based on the FX Rates and country factor
			String referenceCountry = dealInfo.getCountry();
			BigDecimal referenceCountryFactor = new BigDecimal(1);
			for (CountryFactorInfo countryFactorInfo : countryFactors) {
				if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
					referenceCountryFactor = countryFactorInfo.getCountryFactor();
					break;
				}
			}

			TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(HOSTING_TOWER);
			Integer dealTerm = dealInfo.getDealTerm() / 12;
			Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
			String[] levelInd = hostingInfo.getLevelIndicator().split(",");

			// Server hosting-> Server volume
			if (levelInd != null) {
				if (levelInd[0].equals("1") && levelInd[1].equals("0")) {
					// Server volume only
					serversDto = serversCalculator.calculateYearlyRevenue(hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
									countryFactors, referenceCountryFactor);
					if(!CommonHelper.isAllEmpty(serversDto)){
						revenueDto.setServers(serversDto);
					}

				} else if (levelInd[0].equals("1") && levelInd[1].equals("1")) {
					handleServers(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
							countryFactors, referenceCountryFactor);

				}

				// Platform hosting
				if (levelInd[12].equals("1")) {
					HostingCalculateDto sqlInstancesDto = sqlInstancesCalculator.calculateYearlyRevenue(hostingInfo,
							dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

					HostingCalculateDto cotsInstallationsDto = cotsInstallationsCalculator.calculateYearlyRevenue(
							hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
							referenceCountryFactor);
					if(!CommonHelper.isAllEmpty(sqlInstancesDto)){
						revenueDto.setSqlInstances(sqlInstancesDto);
					}
					if(!CommonHelper.isAllEmpty(cotsInstallationsDto)){
						revenueDto.setCotsInstallations(cotsInstallationsDto);
					}
				}
			}
		}
		return revenueDto;
	}

	private void handleServers(HostingRevenueDto revenueDto, String[] levelInd, HostingInfo hostingInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		// Server volume:open
		if (levelInd[2].equals("0") && levelInd[5].equals("0")) {
			// Physical,Virtual:closed
			HostingCalculateDto physicalDto = physicalCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualDto = virtualCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(physicalDto)){
				revenueDto.setPhysical(physicalDto);
			}
			if(!CommonHelper.isAllEmpty(virtualDto)){
				revenueDto.setVirtual(virtualDto);
			}

		} else if (levelInd[2].equals("1") && levelInd[5].equals("0")) {
			// Physical:open, Virtual:closed
			handlePhysical(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualDto = virtualCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(virtualDto)){
				revenueDto.setVirtual(virtualDto);
			}

		} else if (levelInd[2].equals("0") && levelInd[5].equals("1")) {
			// Virtual:open, Physical:closed
			handleVirtual(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalDto = physicalCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(physicalDto)){
				revenueDto.setPhysical(physicalDto);
			}

		} else if (levelInd[2].equals("1") && levelInd[5].equals("1")) {
			// Virtual:open, Physical:open
			handlePhysical(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);
			handleVirtual(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);
		}
	}

	private void handlePhysical(HostingRevenueDto revenueDto, String[] levelInd, HostingInfo hostingInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		// Physical:open
		if (levelInd[3].equals("0") && levelInd[4].equals("0")) {
			// Win:closed, Unix:closed
			HostingCalculateDto physicalWinDto = physicalWinCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixDto = physicalUnixCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(physicalWinDto)){
				revenueDto.setPhysicalWin(physicalWinDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixDto)){
				revenueDto.setPhysicalUnix(physicalUnixDto);
			}

		} else if (levelInd[3].equals("1") && levelInd[4].equals("0")) {
			// Win:open, Unix:closed
			HostingCalculateDto physicalWinSmallDto = physicalWinSmallCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalWinMediumDto = physicalWinMediumCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalWinLargeDto = physicalWinLargeCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixDto = physicalUnixCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(physicalWinSmallDto)){
				revenueDto.setPhysicalWinSmall(physicalWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(physicalWinMediumDto)){
				revenueDto.setPhysicalWinMedium(physicalWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(physicalWinLargeDto)){
				revenueDto.setPhysicalWinLarge(physicalWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixDto)){
				revenueDto.setPhysicalUnix(physicalUnixDto);
			}

		} else if (levelInd[3].equals("0") && levelInd[4].equals("1")) {
			// Win:closed, Unix:open
			HostingCalculateDto physicalUnixSmallDto = physicalUnixSmallCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixMediumDto = physicalUnixMediumCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixLargeDto = physicalUnixLargeCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalWinDto = physicalWinCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(physicalUnixSmallDto)){
				revenueDto.setPhysicalUnixSmall(physicalUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixMediumDto)){
				revenueDto.setPhysicalUnixMedium(physicalUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixLargeDto)){
				revenueDto.setPhysicalUnixLarge(physicalUnixLargeDto);
			}
			if(!CommonHelper.isAllEmpty(physicalWinDto)){
				revenueDto.setPhysicalWin(physicalWinDto);
			}

		} else if (levelInd[3].equals("1") && levelInd[4].equals("1")) {
			// Win:closed, Unix:open

			HostingCalculateDto physicalWinSmallDto = physicalWinSmallCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalWinMediumDto = physicalWinMediumCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalWinLargeDto = physicalWinLargeCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixSmallDto = physicalUnixSmallCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixMediumDto = physicalUnixMediumCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto physicalUnixLargeDto = physicalUnixLargeCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(physicalWinSmallDto)){
				revenueDto.setPhysicalWinSmall(physicalWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(physicalWinMediumDto)){
				revenueDto.setPhysicalWinMedium(physicalWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(physicalWinLargeDto)){
				revenueDto.setPhysicalWinLarge(physicalWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixSmallDto)){
				revenueDto.setPhysicalUnixSmall(physicalUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixMediumDto)){
				revenueDto.setPhysicalUnixMedium(physicalUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(physicalUnixLargeDto)){
				revenueDto.setPhysicalUnixLarge(physicalUnixLargeDto);
			}
		}
	}

	private void handleVirtual(HostingRevenueDto revenueDto, String[] levelInd, HostingInfo hostingInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		// Virtual:open
		if (levelInd[6].equals("0") && levelInd[9].equals("0")) {
			// Public,Private:closed
			HostingCalculateDto virtualPubDto = virtualPublicCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			HostingCalculateDto virtualPrivDto = virtualPrivateCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(virtualPubDto)){
				revenueDto.setVirtualPublic(virtualPubDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivDto)){
				revenueDto.setVirtualPrivate(virtualPrivDto);
			}

		} else if (levelInd[6].equals("1") && levelInd[9].equals("0")) {
			// Public:open, Private:closed
			handlePublic(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualPrivDto = virtualPrivateCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(virtualPrivDto)){
				revenueDto.setVirtualPrivate(virtualPrivDto);
			}

		} else if (levelInd[6].equals("0") && levelInd[9].equals("1")) {
			// Public:closed, Private:open
			handlePrivate(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualPubDto = virtualPublicCalculator.calculateYearlyRevenue(hostingInfo, dealInfo,
					towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(virtualPubDto)){
				revenueDto.setVirtualPublic(virtualPubDto);
			}

		} else if (levelInd[6].equals("1") && levelInd[9].equals("1")) {
			// Public:open, Private:open
			handlePublic(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);
			handlePrivate(revenueDto, levelInd, hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm,
					countryFactors, referenceCountryFactor);
		}

	}

	private void handlePrivate(HostingRevenueDto revenueDto, String[] levelInd, HostingInfo hostingInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		// Private:open
		if (levelInd[10].equals("0") && levelInd[11].equals("0")) {
			// Win:closed, Unix:closed
			HostingCalculateDto virtualPrivWinDto = virtualPrivateWinCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixDto = virtualPrivateUnixCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			if(!CommonHelper.isAllEmpty(virtualPrivWinDto)){
				revenueDto.setVirtualPrivateWin(virtualPrivWinDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixDto)){
				revenueDto.setVirtualPrivateUnix(virtualPrivUnixDto);
			}

		} else if (levelInd[10].equals("1") && levelInd[11].equals("0")) {
			// Win:open, Unix:closed
			HostingCalculateDto virtualPrivWinSmallDto = virtualPrivateWinSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivWinMediumDto = virtualPrivateWinMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivWinLargeDto = virtualPrivateWinLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixDto = virtualPrivateUnixCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
			
			if(!CommonHelper.isAllEmpty(virtualPrivWinSmallDto)){
				revenueDto.setVirtualPrivateWinSmall(virtualPrivWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivWinMediumDto)){
				revenueDto.setVirtualPrivateWinMedium(virtualPrivWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivWinLargeDto)){
				revenueDto.setVirtualPrivateWinLarge(virtualPrivWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixDto)){
				revenueDto.setVirtualPrivateUnix(virtualPrivUnixDto);
			}

		} else if (levelInd[10].equals("0") && levelInd[11].equals("1")) {
			// Win:closed, Unix:open
			HostingCalculateDto virtualPrivUnixSmallDto = virtualPrivateUnixSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixMediumDto = virtualPrivateUnixMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixLargeDto = virtualPrivateUnixLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivWinDto = virtualPrivateWinCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPrivUnixSmallDto)){
				revenueDto.setVirtualPrivateUnixSmall(virtualPrivUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixMediumDto)){
				revenueDto.setVirtualPrivateUnixMedium(virtualPrivUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixLargeDto)){
				revenueDto.setVirtualPrivateUnixLarge(virtualPrivUnixLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivWinDto)){
				revenueDto.setVirtualPrivateWin(virtualPrivWinDto);
			}

		} else if (levelInd[10].equals("1") && levelInd[11].equals("1")) {
			// Win:closed, Unix:open
			HostingCalculateDto virtualPrivWinSmallDto = virtualPrivateWinSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivWinMediumDto = virtualPrivateWinMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivWinLargeDto = virtualPrivateWinLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixSmallDto = virtualPrivateUnixSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixMediumDto = virtualPrivateUnixMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPrivUnixLargeDto = virtualPrivateUnixLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPrivWinSmallDto)){
				revenueDto.setVirtualPrivateWinSmall(virtualPrivWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivWinMediumDto)){
				revenueDto.setVirtualPrivateWinMedium(virtualPrivWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivWinLargeDto)){
				revenueDto.setVirtualPrivateWinLarge(virtualPrivWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixSmallDto)){
				revenueDto.setVirtualPrivateUnixSmall(virtualPrivUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixMediumDto)){
				revenueDto.setVirtualPrivateUnixMedium(virtualPrivUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPrivUnixLargeDto)){
				revenueDto.setVirtualPrivateUnixLarge(virtualPrivUnixLargeDto);
			}
		}

	}

	private void handlePublic(HostingRevenueDto revenueDto, String[] levelInd, HostingInfo hostingInfo,
			DealInfo dealInfo, TowerSpecificBandInfo towerSpecificBandInfo, Integer assessmentDealTerm,
			List<CountryFactorInfo> countryFactors, BigDecimal referenceCountryFactor) {
		// Public:open
		if (levelInd[7].equals("0") && levelInd[8].equals("0")) {
			// Win:closed, Unix:closed
			HostingCalculateDto virtualPubWinDto = virtualPublicWinCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			HostingCalculateDto virtualPubUnixDto = virtualPublicUnixCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPubWinDto)){
				revenueDto.setVirtualPublicWin(virtualPubWinDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixDto)){
				revenueDto.setVirtualPublicUnix(virtualPubUnixDto);
			}

		} else if (levelInd[7].equals("1") && levelInd[8].equals("0")) {
			// Win:open, Unix:closed
			HostingCalculateDto virtualPubWinSmallDto = virtualPublicWinSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubWinMediumDto = virtualPublicWinMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubWinLargeDto = virtualPublicWinLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixDto = virtualPublicUnixCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPubWinSmallDto)){
				revenueDto.setVirtualPublicWinSmall(virtualPubWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubWinMediumDto)){
				revenueDto.setVirtualPublicWinMedium(virtualPubWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubWinLargeDto)){
				revenueDto.setVirtualPublicWinLarge(virtualPubWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixDto)){
				revenueDto.setVirtualPublicUnix(virtualPubUnixDto);
			}

		} else if (levelInd[7].equals("0") && levelInd[8].equals("1")) {
			// Win:closed, Unix:open
			HostingCalculateDto virtualPubUnixSmallDto = virtualPublicUnixSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixMediumDto = virtualPublicUnixMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixLargeDto = virtualPublicUnixLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubWinDto = virtualPublicWinCalculator.calculateYearlyRevenue(hostingInfo,
					dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPubUnixSmallDto)){
				revenueDto.setVirtualPublicUnixSmall(virtualPubUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixMediumDto)){
				revenueDto.setVirtualPublicUnixMedium(virtualPubUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixLargeDto)){
				revenueDto.setVirtualPublicUnixLarge(virtualPubUnixLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubWinDto)){
				revenueDto.setVirtualPublicWin(virtualPubWinDto);
			}

		} else if (levelInd[7].equals("1") && levelInd[8].equals("1")) {
			// Win:closed, Unix:open
			HostingCalculateDto virtualPubWinSmallDto = virtualPublicWinSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubWinMediumDto = virtualPublicWinMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubWinLargeDto = virtualPublicWinLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixSmallDto = virtualPublicUnixSmallCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixMediumDto = virtualPublicUnixMediumCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			HostingCalculateDto virtualPubUnixLargeDto = virtualPublicUnixLargeCalculator.calculateYearlyRevenue(
					hostingInfo, dealInfo, towerSpecificBandInfo, assessmentDealTerm, countryFactors,
					referenceCountryFactor);

			if(!CommonHelper.isAllEmpty(virtualPubWinSmallDto)){
				revenueDto.setVirtualPublicWinSmall(virtualPubWinSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubWinMediumDto)){
				revenueDto.setVirtualPublicWinMedium(virtualPubWinMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubWinLargeDto)){
				revenueDto.setVirtualPublicWinLarge(virtualPubWinLargeDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixSmallDto)){
				revenueDto.setVirtualPublicUnixSmall(virtualPubUnixSmallDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixMediumDto)){
				revenueDto.setVirtualPublicUnixMedium(virtualPubUnixMediumDto);
			}
			if(!CommonHelper.isAllEmpty(virtualPubUnixLargeDto)){
				revenueDto.setVirtualPublicUnixLarge(virtualPubUnixLargeDto);
			}
		}

	}

	/**
	 * @param dealId
	 * @param levelName
	 * @param dealType
	 * @return
	 */
	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		DealResultsResponse dealResultsDto = new DealResultsResponse();
		if (dealInfo != null) {
			HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
			if (hostingInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(HOSTING_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (SERVERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = serverHelper.getNearestPastDealsForServer(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PHYSICAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalHelper.getNearestPastDealsForPhysical(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PHYSICAL_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinHelper.getNearestPastDealsForPhysicalWin(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PHYSICAL_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinSmallHelper
								.getNearestPastDealsForPhysicalWinSmall(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinMediumHelper
								.getNearestPastDealsForPhysicalWinMedium(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinLargeHelper
								.getNearestPastDealsForPhysicalWinLarge(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixHelper
								.getNearestPastDealsForPhysicalUnix(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixSmallHelper
								.getNearestPastDealsForPhysicalUnixSmall(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixMediumHelper
								.getNearestPastDealsForPhysicalUnixMedium(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (PHYSICAL_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixLargeHelper
								.getNearestPastDealsForPhysicalUnixLarge(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtHelper
								.getNearestPastDealsForVirtual(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinSmallHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinMediumHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinLargeHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixHelper
								.getNearestPastDeals(hostingInfo,
										towerSpecificBandInfo == null ? new TowerSpecificBandInfo()
												: towerSpecificBandInfo,
										assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
										referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixSmallHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixMediumHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixLargeHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinSmallHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinMediumHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinLargeHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixSmallHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixMediumHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixLargeHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (SQL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = sqlHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (COTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = cotsHelper.getNearestPastDeals(hostingInfo,
								towerSpecificBandInfo == null ? new TowerSpecificBandInfo() : towerSpecificBandInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					}

				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (SERVERS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = serverHelper.getNearestBenchmarkDealsForServer(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PHYSICAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalHelper.getNearestBenchmarkDealsForPhysical(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PHYSICAL_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinHelper.getNearestBenchmarkDealsForPhysicalWin(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinSmallHelper.getNearestBenchmarkDealsForPhysicalWinSmall(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinMediumHelper.getNearestBenchmarkDealsForPhysicalWinMedium(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalWinLargeHelper.getNearestBenchmarkDealsForPhysicalWinLarge(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixHelper.getNearestBenchmarkDealsForPhysicalUnix(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixSmallHelper.getNearestBenchmarkDealsForPhysicalUnixSmall(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixMediumHelper.getNearestBenchmarkDealsForPhysicalUnixMedium(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PHYSICAL_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = physicalUnixLargeHelper.getNearestBenchmarkDealsForPhysicalUnixLarge(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtHelper.getNearestBenchmarkDealsForVirtual(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinSmallHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinMediumHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubWinLargeHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixHelper.getNearestBenchmarkDeals(
								hostingInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixSmallHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixMediumHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PUBLIC_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPubUnixLargeHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinSmallHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinMediumHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_WIN_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivWinLargeHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_SMALL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixSmallHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_MEDIUM_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixMediumHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (VIRTUAL_PRIVATE_UNIX_LARGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = virtPrivUnixLargeHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (SQL_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = sqlHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (COTS_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = cotsHelper.getNearestBenchmarkDeals(hostingInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					}
				}

			} else {
				throw new ServiceException("Can't find the Hosting Detail for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;

	}

	/**
	 * @param solutionCriteriaDto
	 * @param hostingId
	 * @return
	 */
	public HostingRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long hostingId)
			throws ServiceException {
		HostingInfo hostingInfo = hostingRepository.findOne(hostingId);
		if (null != hostingInfo) {
			hostingInfo = beanConvertor.prepareSolutionCriteria(hostingInfo, solutionCriteriaDto);
			hostingRepository.saveAndFlush(hostingInfo);
		} else {
			throw new ServiceException("No HostingInfo data to update");
		}

		return getYearlyRevenues(hostingInfo.getDealInfo().getDealId());
	}

}
