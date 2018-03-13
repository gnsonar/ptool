package com.in.fujitsu.pricing.enduser.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.entity.DealInfo;

public interface EndUserRepository extends JpaRepository<EndUserInfo, Long>, JpaSpecificationExecutor<EndUserInfo> {

	public EndUserInfo findByDealInfo(@Param("dealId") DealInfo dealId);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.endUserDevices <= :endUserDevices group by EUI.endUserId order by YDI.endUserDevices desc")
	public List<EndUserInfo> findLowBenchMarkDealForEndUser(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("endUserDevices") Integer endUserDevices, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.endUserDevices > :endUserDevices group by EUI.endUserId order by YDI.endUserDevices asc")
	public List<EndUserInfo> findHighBenchMarkDealForEndUser(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("endUserDevices") Integer endUserDevices, Pageable pageable);


	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.laptops <= :laptops group by EUI.endUserId order by YDI.laptops desc")
	public List<EndUserInfo> findLowBenchMarkDealForLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("laptops") Integer laptops, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.laptops > :laptops group by EUI.endUserId order by YDI.laptops asc")
	public List<EndUserInfo> findHighBenchMarkDealForLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("laptops") Integer laptops, Pageable pageable);


	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.highEndLaptops <= :highEndLaptops group by EUI.endUserId order by YDI.highEndLaptops desc")
	public List<EndUserInfo> findLowBenchMarkDealForHighEndLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("highEndLaptops") Integer highEndLaptops, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.highEndLaptops > :highEndLaptops group by EUI.endUserId order by YDI.highEndLaptops asc")
	public List<EndUserInfo> findHighBenchMarkDealForHighEndLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("highEndLaptops") Integer highEndLaptops, Pageable pageable);


	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.standardLaptops <= :standardLaptops group by EUI.endUserId order by YDI.standardLaptops desc")
	public List<EndUserInfo> findLowBenchMarkDealForStandardLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("standardLaptops") Integer standardLaptops, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.standardLaptops > :standardLaptops group by EUI.endUserId order by YDI.standardLaptops asc")
	public List<EndUserInfo> findHighBenchMarkDealForStandardLaptops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("standardLaptops") Integer standardLaptops, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.desktops <= :desktops group by EUI.endUserId order by YDI.desktops desc")
	public List<EndUserInfo> findLowBenchMarkDealForDesktops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("desktops") Integer desktops, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.desktops > :desktops group by EUI.endUserId order by YDI.desktops asc")
	public List<EndUserInfo> findHighBenchMarkDealForDesktops(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("desktops") Integer desktops, Pageable pageable);


	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.thinClients <= :thinClients group by EUI.endUserId order by YDI.thinClients desc")
	public List<EndUserInfo> findLowBenchMarkDealForThinClients(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("thinClients") Integer thinClients, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.thinClients > :thinClients group by EUI.endUserId order by YDI.thinClients asc")
	public List<EndUserInfo> findHighBenchMarkDealForThinClients(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("thinClients") Integer thinClients, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.mobileDevices <= :mobileDevices group by EUI.endUserId order by YDI.mobileDevices desc")
	public List<EndUserInfo> findLowBenchMarkDealForMobiles(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("mobileDevices") Integer mobileDevices, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.mobileDevices > :mobileDevices group by EUI.endUserId order by YDI.mobileDevices asc")
	public List<EndUserInfo> findHighBenchMarkDealForMobiles(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("mobileDevices") Integer mobileDevices, Pageable pageable);


	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.imacDevices <= :imacDevices group by EUI.endUserId order by YDI.imacDevices desc")
	public List<EndUserInfo> findLowBenchMarkDealForImac(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("imacDevices") Integer imacDevices, Pageable pageable);

	@Query("select EUI from EndUserInfo EUI JOIN EUI.endUserYearlyDataInfoList YDI JOIN EUI.dealInfo DI where DI.dealType = :dealType  "
			+ "and EUI.offshoreAllowed =:offshoreAllowed and EUI.includeHardware =:includeHardware "
			+ "and EUI.includeBreakFix =:includeBreakFix and LOWER(EUI.resolutionTime) =LOWER(:resolutionTime)  "
			+ "and YDI.imacDevices > :imacDevices group by EUI.endUserId order by YDI.imacDevices asc")
	public List<EndUserInfo> findHighBenchMarkDealForImac(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware, @Param("includeBreakFix") boolean includeBreakFix,
			@Param("resolutionTime") String resolutionTime, @Param("dealType") String dealType,
			@Param("imacDevices") Integer imacDevices, Pageable pageable);







}
