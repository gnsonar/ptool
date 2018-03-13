package com.in.fujitsu.pricing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.in.fujitsu.pricing.dto.DealResultsResponse;
import com.in.fujitsu.pricing.dto.SolutionCriteriaDto;
import com.in.fujitsu.pricing.dto.SuccessResponse;
import com.in.fujitsu.pricing.dto.VolumeDto;
import com.in.fujitsu.pricing.entity.CountryFactorInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;
import com.in.fujitsu.pricing.exception.ServiceException;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;
import com.in.fujitsu.pricing.hosting.repository.HostingRepository;
import com.in.fujitsu.pricing.repository.CountryFactorRepository;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.TowerSpecificBandRepository;
import com.in.fujitsu.pricing.storage.calculator.BackupCalculator;
import com.in.fujitsu.pricing.storage.calculator.NonPerformanceCalculator;
import com.in.fujitsu.pricing.storage.calculator.PerformanceCalculator;
import com.in.fujitsu.pricing.storage.calculator.StorageCalculator;
import com.in.fujitsu.pricing.storage.dto.BackupCalculateDto;
import com.in.fujitsu.pricing.storage.dto.NonPerformanceCalculateDto;
import com.in.fujitsu.pricing.storage.dto.PerformanceCalculateDto;
import com.in.fujitsu.pricing.storage.dto.StorageCalculateDto;
import com.in.fujitsu.pricing.storage.dto.StorageDropdownDto;
import com.in.fujitsu.pricing.storage.dto.StorageInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueDto;
import com.in.fujitsu.pricing.storage.dto.UpdateStoragePriceDto;
import com.in.fujitsu.pricing.storage.entity.StorageBackupInfo;
import com.in.fujitsu.pricing.storage.entity.StorageDefaultInfo;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageSolutionsInfo;
import com.in.fujitsu.pricing.storage.helper.BackupDealResultsHelper;
import com.in.fujitsu.pricing.storage.helper.NonPerformanceDealResultsHelper;
import com.in.fujitsu.pricing.storage.helper.PerformanceDealResultsHelper;
import com.in.fujitsu.pricing.storage.helper.StorageBeanConvertor;
import com.in.fujitsu.pricing.storage.helper.StorageDealResultsHelper;
import com.in.fujitsu.pricing.storage.repository.StorageBackupRepository;
import com.in.fujitsu.pricing.storage.repository.StorageDefaultRepository;
import com.in.fujitsu.pricing.storage.repository.StorageRepository;
import com.in.fujitsu.pricing.storage.repository.StorageSolutionsRepository;
import com.in.fujitsu.pricing.utility.CommonHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StorageService {

	@Autowired
	private StorageRepository storageRepository;

	@Autowired
	private StorageSolutionsRepository solutionRepository;

	@Autowired
	private StorageBackupRepository backupRepository;

	@Autowired
	private CountryFactorRepository countryFactorRepository;

	@Autowired
	private StorageDefaultRepository storageDefaultRepository;

	@Autowired
	private StorageBeanConvertor storageBeanConvertor;

	@Autowired
	private GenericService genericService;

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private TowerSpecificBandRepository bandRepository;

	@Autowired
	private StorageDealResultsHelper storageDealResultsHelper;

	@Autowired
	private BackupDealResultsHelper backupDealResultsHelper;

	@Autowired
	private PerformanceDealResultsHelper performanceDealResultsHelper;

	@Autowired
	private NonPerformanceDealResultsHelper nonPerformanceDealResultsHelper;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private HostingRepository hostingRepository;

	@Autowired
	private StorageCalculator storageCalculator;

	@Autowired
	private PerformanceCalculator performanceCalculator;

	@Autowired
	private NonPerformanceCalculator nonPerformanceCalculator;

	@Autowired
	private BackupCalculator backupCalculator;

	private final String STORAGE_TOWER = "Storage";
	private final String STORAGE_LEVEL = "1.1";
	private final String BACKUP_LEVEL = "2.1";
	private final String PERFORMANCE_LEVEL = "1.1.1";
	private final String NONPERFORMANCE_LEVEL = "1.1.2";
	private final String PAST_DEAL = "Past";
	private final String BENCHMARK_DEAL = "Benchmark";

	@Transactional
	public StorageRevenueDto getYearlyRevenues(Long dealId) throws ServiceException {

		DealInfo dealInfo = dealRepository.findOne(dealId);
		StorageRevenueDto storageRevenueDto = new StorageRevenueDto();
		StorageCalculateDto storageCalculateDto = null;
		PerformanceCalculateDto performanceCalculateDto = null;
		NonPerformanceCalculateDto nonPerformanceCalculateDto = null;
		BackupCalculateDto backupCalculateDto = null;
		if (dealInfo != null) {
			StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
			if (storageInfo != null && !storageInfo.getLevelIndicator().isEmpty()) {
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
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName("Storage");
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				String[] levelInd = storageInfo.getLevelIndicator().split(",");
				if (levelInd != null && (levelInd[0].equals("1") && levelInd[1].equals("0"))) {
					log.info("Got correct position for Storage calculation.");
					storageCalculateDto = storageCalculator.calculateYearlyRevenue(storageInfo, dealInfo,
							towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
					if(!CommonHelper.isAllEmpty(storageCalculateDto)){
						storageRevenueDto.setStorageCalculateDto(storageCalculateDto);
					}

				} else if (levelInd != null && (levelInd[0].equals("1") && levelInd[1].equals("1"))) {
					log.info("Got correct position for Performance calculation.");
					performanceCalculateDto = performanceCalculator.calculateYearlyRevenue(storageInfo, dealInfo,
							towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);

					nonPerformanceCalculateDto = nonPerformanceCalculator.calculateYearlyRevenue(storageInfo, dealInfo,
							towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
					if(!CommonHelper.isAllEmpty(performanceCalculateDto)){
						storageRevenueDto.setPerformanceCalculateDto(performanceCalculateDto);
					}
					if(!CommonHelper.isAllEmpty(nonPerformanceCalculateDto)){
						storageRevenueDto.setNonPerformanceCalculateDto(nonPerformanceCalculateDto);
					}

				}

				if (levelInd != null && levelInd[2].equals("1")) {
					log.info("Got correct position for Backup calculation.");
					backupCalculateDto = backupCalculator.calculateYearlyRevenue(storageInfo, dealInfo,
							towerSpecificBandInfo, assessmentDealTerm, countryFactors, referenceCountryFactor);
					if(!CommonHelper.isAllEmpty(backupCalculateDto)){
						storageRevenueDto.setBackupCalculateDto(backupCalculateDto);
					}
				}
			} else {
				throw new ServiceException("Can't find the Storage Detaild for given dealID.");
			}

		}

		return storageRevenueDto;
	}



	/**
	 * @param storageInfoDto
	 * @return
	 */
	@Transactional
	public StorageInfoDto saveStorageDetails(Long dealId, StorageInfoDto storageInfoDto) {
		StorageInfo storageInfo = null;
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
		if (null != storageInfo) {
			storageInfo = storageBeanConvertor.prepareStorageInfo(storageInfo, storageInfoDto, false);
		} else {
			storageInfo = storageBeanConvertor.prepareStorageInfo(new StorageInfo(), storageInfoDto, true);
		}
		storageInfo = storageRepository.saveAndFlush(storageInfo);

		return storageBeanConvertor.prepareStorageInfoDto(storageInfo);

	}

	/**
	 * Method for updating/saving the unit price details
	 *
	 * @param storageUnitPriceDtoList
	 * @param storageId
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public ResponseEntity<Object> updateStoragePriceDetails(List<UpdateStoragePriceDto> storageUnitPriceDtoList,
			Long storageId) throws ServiceException {
		StorageInfo storageInfo = storageRepository.findOne(storageId);
		if (null != storageInfo) {
			storageInfo = storageBeanConvertor.prepareStoragePrice(storageInfo, storageUnitPriceDtoList);
			storageRepository.saveAndFlush(storageInfo);
		} else {
			throw new ServiceException("No StorageInfo data to update");
		}

		return new ResponseEntity<Object>(new SuccessResponse("Prices Updated Successfully"), HttpStatus.OK);

	}

	/**
	 * @param dealId
	 * @return
	 */
	public StorageInfoDto getStorageDetails(long dealId) throws Exception {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
		if (storageInfo == null) {
			throw new ServiceException("DB doesn't have the storage Details for given dealId.");
		}
		return storageBeanConvertor.prepareStorageInfoDto(storageInfo);

	}

	/**
	 * @param dealId
	 * @return
	 */
	public StorageDropdownDto getStorageDropDownDetails(Long dealId) {
		StorageDropdownDto storageDropdownDto = new StorageDropdownDto();

		storageDropdownDto.setOffshoreAndHardwareIncludedList(genericService.getOffshreAndHardwareInfo());

		storageDropdownDto.setStandardWindowInfoList(genericService.getServicedWindowInfo());

		final List<StorageSolutionsInfo> solutions = solutionRepository.findAll();
		storageDropdownDto
				.setStorageSolutionsInfoDtoList(storageBeanConvertor.prepareStorageSolutionsDtoList(solutions));

		final List<StorageBackupInfo> backups = backupRepository.findAll();
		storageDropdownDto.setStorageBackupInfoDtoList(storageBeanConvertor.prepareStorageBackupDtoList(backups));

		final List<StorageDefaultInfo> storageDefaults = storageDefaultRepository.findAll();
		storageDropdownDto
				.setStorageDefaultInfoDtoList(storageBeanConvertor.prepareStorageDefaultsDtoList(storageDefaults));

		storageDropdownDto.setHostingServerList(getHostingServersList(dealId));



		if (dealId != null) {
			storageDropdownDto.setDealInfoDto(genericService.getGenericDetailsByDealId(dealId));
		}

		return storageDropdownDto;
	}

	/**
	 * @param dealId
	 * @return
	 */
	private List<VolumeDto> getHostingServersList(Long dealId) {
		List<VolumeDto> hostingServerList = new ArrayList<>();
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(dealId);
		HostingInfo hostingInfo = hostingRepository.findByDealInfo(dealInfo);
		if(hostingInfo != null){
			if(!CollectionUtils.isEmpty(hostingInfo.getHostingYearlyDataInfoList())){
				for(HostingYearlyDataInfo yearlyInfo : hostingInfo.getHostingYearlyDataInfoList()){
					VolumeDto dto = new VolumeDto();
					dto.setYear(yearlyInfo.getYear());
					dto.setVolume(yearlyInfo.getServers());
					hostingServerList.add(dto);
				}
			}
		}
		return hostingServerList;
	}

	/**
	 * Method for updating the Solution Criteria
	 *
	 * @param storageUnitPriceDtoList
	 * @param storageId
	 * @return
	 * @throws ServiceException
	 */
	public StorageRevenueDto updateSolutionCriteria(SolutionCriteriaDto solutionCriteriaDto, Long storageId)
			throws ServiceException {
		StorageInfo storageInfo = storageRepository.findOne(storageId);
		if (null != storageInfo) {
			storageInfo = storageBeanConvertor.prepareSolutionCriteria(storageInfo, solutionCriteriaDto);
			storageInfo = storageRepository.save(storageInfo);
		} else {
			throw new ServiceException("No StorageInfo data to update");
		}

		return getYearlyRevenues(storageInfo.getDealInfo().getDealId());
	}

	/**
	 * @param dealId
	 * @param levelName
	 * @param dealType
	 * @return
	 * @throws Exception
	 */
	public DealResultsResponse getNearestDeals(Long dealId, String levelName, String dealType) throws Exception {
		DealInfo dealInfo = dealRepository.findOne(dealId);
		DealResultsResponse dealResultsDto = new DealResultsResponse();
		if (dealInfo != null) {
			StorageInfo storageInfo = storageRepository.findStorageDetailsByDealInfo(dealInfo);
			if (storageInfo != null) {
				List<CountryFactorInfo> countryFactors = countryFactorRepository.findAll();
				String referenceCountry = dealInfo.getCountry();
				BigDecimal referenceCountryFactor = new BigDecimal(1);
				for (CountryFactorInfo countryFactorInfo : countryFactors) {
					if (referenceCountry != null && referenceCountry.equalsIgnoreCase(countryFactorInfo.getCountry())) {
						referenceCountryFactor = countryFactorInfo.getCountryFactor();
						break;
					}
				}
				TowerSpecificBandInfo towerSpecificBandInfo = bandRepository.findByTowerName(STORAGE_TOWER);
				Integer dealTerm = dealInfo.getDealTerm() / 12;
				Integer assessmentDealTerm = dealInfo.getDealTerm() % 12 == 0 ? dealTerm : dealTerm + 1;
				if (PAST_DEAL.equalsIgnoreCase(dealType)) {
					if (STORAGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = storageDealResultsHelper.getNearestPastDeals(storageInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (BACKUP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = backupDealResultsHelper.getNearestPastDeals(storageInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (PERFORMANCE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = performanceDealResultsHelper.getNearestPastDeals(storageInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					} else if (NONPERFORMANCE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = nonPerformanceDealResultsHelper.getNearestPastDeals(storageInfo,
								towerSpecificBandInfo, assessmentDealTerm, countryFactors, dealInfo.getCurrency(),
								referenceCountry, referenceCountryFactor);
					}
				} else if (BENCHMARK_DEAL.equalsIgnoreCase(dealType)) {
					if (STORAGE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = storageDealResultsHelper.getNearestBenchmarkDeals(storageInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (BACKUP_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = backupDealResultsHelper.getNearestBenchmarkDeals(storageInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					} else if (PERFORMANCE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = performanceDealResultsHelper.getNearestBenchmarkDeals(storageInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);

					} else if (NONPERFORMANCE_LEVEL.equalsIgnoreCase(levelName)) {
						dealResultsDto = nonPerformanceDealResultsHelper.getNearestBenchmarkDeals(storageInfo,
								assessmentDealTerm, countryFactors, dealInfo.getCurrency(), referenceCountry,
								referenceCountryFactor);
					}
				}

			} else {
				throw new ServiceException("Can't find the Storage Detaild for given dealID.");
			}

		} else {
			throw new ServiceException("Invalid dealID.");
		}
		return dealResultsDto;
	}

}
