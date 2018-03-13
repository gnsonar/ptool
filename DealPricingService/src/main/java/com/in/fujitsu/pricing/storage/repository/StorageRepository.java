package com.in.fujitsu.pricing.storage.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;

public interface StorageRepository extends JpaRepository<StorageInfo, Long>, JpaSpecificationExecutor<StorageInfo> {

	@Query("select s from StorageInfo s where s.dealInfo = :dealId")
	public StorageInfo findStorageDetailsByDealInfo(@Param("dealId") DealInfo dealId);

	@Query("select SM from StorageInfo SM JOIN SM.dealInfo DM JOIN SM.storageYearlyDataInfos SYM where DM.dealType = :dealType "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "AND SYM.backupVolume <= :backupVolume group by SM.id order by SYM.backupVolume desc")
	public List<StorageInfo> findLowBenchmarkDealForBackup(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("backupVolume")Integer backupVolume, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.dealInfo DM JOIN SM.storageYearlyDataInfos SYM where DM.dealType = :dealType "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "AND SYM.backupVolume > :backupVolume group by SM.id order by SYM.backupVolume asc")
	public List<StorageInfo> findHighBenchmarkDealForBackup(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("backupVolume")Integer backupVolume, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.storageVolume <= :storageVolume group by SM.id order by SYM.storageVolume desc")
	public List<StorageInfo> findLowBenchMarkDealForStorage(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("storageVolume") Integer storageVolume, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.storageVolume > :storageVolume group by SM.id order by SYM.storageVolume asc")
	public List<StorageInfo> findHighBenchMarkDealForStorage(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("storageVolume") Integer storageVolume, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.performanceStorage <= :performanceStorage group by SM.id order by SYM.performanceStorage desc")
	public List<StorageInfo> findLowBenchMarkDealForPerformance(
			@Param("dealType") String dealType, 
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("performanceStorage") Integer performanceStorage, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.performanceStorage > :performanceStorage group by SM.id order by SYM.performanceStorage asc")
	public List<StorageInfo> findHighBenchMarkDealForPerformance(
			@Param("dealType") String dealType, 
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("performanceStorage") Integer performanceStorage, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.nonPerformanceStorage <= :nonPerformanceStorage group by SM.id order by SYM.nonPerformanceStorage desc")
	public List<StorageInfo> findLowBenchMarkDealForNonPerformance(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("nonPerformanceStorage") Integer nonPerformanceStorage, Pageable pageable);

	@Query("select SM from StorageInfo SM JOIN SM.storageYearlyDataInfos SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "AND SM.offshoreAllowed = :offshoreAllowed AND SM.includeHardware = :includeHardware AND LOWER(SM.serviceWindowSla) =LOWER(:serviceWindowSla) "
			+ "and SYM.nonPerformanceStorage > :nonPerformanceStorage group by SM.id order by SYM.nonPerformanceStorage asc")
	public List<StorageInfo> findHighBenchMarkDealForNonPerformance(
			@Param("dealType") String dealType, 
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("serviceWindowSla") String serviceWindowSla,
			@Param("nonPerformanceStorage") Integer nonPerformanceStorage, Pageable pageable);

}
