package com.in.fujitsu.pricing.hosting.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;

public interface HostingRepository extends JpaRepository<HostingInfo, Long>, JpaSpecificationExecutor<HostingInfo> {

	public HostingInfo findByDealInfo(@Param("dealId") DealInfo dealId);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.servers <= :servers group by HI.hostingId order by YDI.servers desc")
	public List<HostingInfo> findLowBenchMarkDealForServer(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("servers") Integer servers, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.servers > :servers group by HI.hostingId order by YDI.servers asc")
	public List<HostingInfo> findHighBenchMarkDealForServer(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("servers") Integer servers, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physical <= :physical group by HI.hostingId order by YDI.physical desc")
	public List<HostingInfo> findLowBenchMarkDealForPhysical(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physical") Integer physical, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physical > :physical group by HI.hostingId order by YDI.physical asc")
	public List<HostingInfo> findHighBenchMarkDealForPhysical(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physical") Integer physical, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWin <= :physicalWin group by HI.hostingId order by YDI.physicalWin desc")
	public List<HostingInfo> findLowBenchMarkDealForPhysicalWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWin") Integer physicalWin, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWin > :physicalWin group by HI.hostingId order by YDI.physicalWin asc")
	public List<HostingInfo> findHighBenchMarkDealForPhysicalWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWin") Integer physicalWin, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinSmall <= :physicalWinSmall group by HI.hostingId order by YDI.physicalWinSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinSmall") Integer physicalWinSmall, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinSmall > :physicalWinSmall group by HI.hostingId order by YDI.physicalWinSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinSmall") Integer physicalWinSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinMedium <= :physicalWinMedium group by HI.hostingId order by YDI.physicalWinMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinMedium") Integer physicalWinMedium, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinMedium > :physicalWinMedium group by HI.hostingId order by YDI.physicalWinMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinMedium") Integer physicalWinMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinLarge <= :physicalWinLarge group by HI.hostingId order by YDI.physicalWinLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinLarge") Integer physicalWinLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalWinLarge > :physicalWinLarge group by HI.hostingId order by YDI.physicalWinLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalWinLarge") Integer physicalWinLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnix <= :physicalUnix group by HI.hostingId order by YDI.physicalUnix desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnix") Integer physicalUnix, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnix > :physicalUnix group by HI.hostingId order by YDI.physicalUnix asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnix") Integer physicalUnix, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixSmall <= :physicalUnixSmall group by HI.hostingId order by YDI.physicalUnixSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixSmall") Integer physicalUnixSmall, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixSmall > :physicalUnixSmall group by HI.hostingId order by YDI.physicalUnixSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixSmall") Integer physicalUnixSmall, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixMedium <= :physicalUnixMedium group by HI.hostingId order by YDI.physicalUnixMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixMedium") Integer physicalUnixMedium, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixMedium > :physicalUnixMedium group by HI.hostingId order by YDI.physicalUnixMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixMedium") Integer physicalUnixMedium, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixLarge <= :physicalUnixLarge group by HI.hostingId order by YDI.physicalUnixLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForPhyUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixLarge") Integer physicalUnixLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.physicalUnixLarge > :physicalUnixLarge group by HI.hostingId order by YDI.physicalUnixLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForPhyUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("physicalUnixLarge") Integer physicalUnixLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtual <= :virtual group by HI.hostingId order by YDI.virtual desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtual(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtual") Integer virtual, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtual > :virtual group by HI.hostingId order by YDI.virtual asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtual(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtual") Integer virtual, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublic <= :virtualPublic group by HI.hostingId order by YDI.virtualPublic desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPub(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublic") Integer virtualPublic, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublic > :virtualPublic group by HI.hostingId order by YDI.virtualPublic asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPub(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublic") Integer virtualPublic, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWin <= :virtualPublicWin group by HI.hostingId order by YDI.virtualPublicWin desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWin") Integer virtualPublicWin, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWin > :virtualPublicWin group by HI.hostingId order by YDI.virtualPublicWin asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWin") Integer virtualPublicWin, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinSmall <= :virtualPublicWinSmall group by HI.hostingId order by YDI.virtualPublicWinSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinSmall") Integer virtualPublicWinSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinSmall > :virtualPublicWinSmall group by HI.hostingId order by YDI.virtualPublicWinSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinSmall") Integer virtualPublicWinSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinMedium <= :virtualPublicWinMedium group by HI.hostingId order by YDI.virtualPublicWinMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinMedium") Integer virtualPublicWinMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinMedium > :virtualPublicWinMedium group by HI.hostingId order by YDI.virtualPublicWinMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinMedium") Integer virtualPublicWinMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinLarge <= :virtualPublicWinLarge group by HI.hostingId order by YDI.virtualPublicWinLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinLarge") Integer virtualPublicWinLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicWinLarge > :virtualPublicWinLarge group by HI.hostingId order by YDI.virtualPublicWinLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicWinLarge") Integer virtualPublicWinLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnix <= :virtualPublicUnix group by HI.hostingId order by YDI.virtualPublicUnix desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnix") Integer virtualPublicUnix, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnix > :virtualPublicUnix group by HI.hostingId order by YDI.virtualPublicUnix asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnix") Integer virtualPublicUnix, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixSmall <= :virtualPublicUnixSmall group by HI.hostingId order by YDI.virtualPublicUnixSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixSmall") Integer virtualPublicUnixSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixSmall > :virtualPublicUnixSmall group by HI.hostingId order by YDI.virtualPublicUnixSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixSmall") Integer virtualPublicUnixSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixMedium <= :virtualPublicUnixMedium group by HI.hostingId order by YDI.virtualPublicUnixMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixMedium") Integer virtualPublicUnixMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixMedium > :virtualPublicUnixMedium group by HI.hostingId order by YDI.virtualPublicUnixMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixMedium") Integer virtualPublicUnixMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixLarge <= :virtualPublicUnixLarge group by HI.hostingId order by YDI.virtualPublicUnixLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPubUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixLarge") Integer virtualPublicUnixLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPublicUnixLarge > :virtualPublicUnixLarge group by HI.hostingId order by YDI.virtualPublicUnixLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPubUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPublicUnixLarge") Integer virtualPublicUnixLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivate <= :virtualPrivate group by HI.hostingId order by YDI.virtualPrivate desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPriv(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivate") Integer virtualPrivate, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivate > :virtualPrivate group by HI.hostingId order by YDI.virtualPrivate asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPriv(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivate") Integer virtualPrivate, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWin <= :virtualPrivateWin group by HI.hostingId order by YDI.virtualPrivateWin desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWin") Integer virtualPrivateWin, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWin > :virtualPrivateWin group by HI.hostingId order by YDI.virtualPrivateWin asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivWin(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWin") Integer virtualPrivateWin, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinSmall <= :virtualPrivateWinSmall group by HI.hostingId order by YDI.virtualPrivateWinSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinSmall") Integer virtualPrivateWinSmall, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinSmall > :virtualPrivateWinSmall group by HI.hostingId order by YDI.virtualPrivateWinSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivWinSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinSmall") Integer virtualPrivateWinSmall, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinMedium <= :virtualPrivateWinMedium group by HI.hostingId order by YDI.virtualPrivateWinMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinMedium") Integer virtualPrivateWinMedium, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinMedium > :virtualPrivateWinMedium group by HI.hostingId order by YDI.virtualPrivateWinMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivWinMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinMedium") Integer virtualPrivateWinMedium, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinLarge <= :virtualPrivateWinLarge group by HI.hostingId order by YDI.virtualPrivateWinLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinLarge") Integer virtualPrivateWinLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateWinLarge > :virtualPrivateWinLarge group by HI.hostingId order by YDI.virtualPrivateWinLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivWinLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateWinLarge") Integer virtualPrivateWinLarge, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnix <= :virtualPrivateUnix group by HI.hostingId order by YDI.virtualPrivateUnix desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnix") Integer virtualPrivateUnix, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnix > :virtualPrivateUnix group by HI.hostingId order by YDI.virtualPrivateUnix asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivUnix(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnix") Integer virtualPrivateUnix, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixSmall <= :virtualPrivateUnixSmall group by HI.hostingId order by YDI.virtualPrivateUnixSmall desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixSmall") Integer virtualPrivateUnixSmall, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixSmall > :virtualPrivateUnixSmall group by HI.hostingId order by YDI.virtualPrivateUnixSmall asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivUnixSmall(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixSmall") Integer virtualPrivateUnixSmall, Pageable pageable);
	
	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixMedium <= :virtualPrivateUnixMedium group by HI.hostingId order by YDI.virtualPrivateUnixMedium desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixMedium") Integer virtualPrivateUnixMedium, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixMedium > :virtualPrivateUnixMedium group by HI.hostingId order by YDI.virtualPrivateUnixMedium asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivUnixMedium(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixMedium") Integer virtualPrivateUnixMedium, Pageable pageable);
	
	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixLarge <= :virtualPrivateUnixLarge group by HI.hostingId order by YDI.virtualPrivateUnixLarge desc")
	public List<HostingInfo> findLowBenchMarkDealForVirtPrivUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixLarge") Integer virtualPrivateUnixLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.virtualPrivateUnixLarge > :virtualPrivateUnixLarge group by HI.hostingId order by YDI.virtualPrivateUnixLarge asc")
	public List<HostingInfo> findHighBenchMarkDealForVirtPrivUnixLarge(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("virtualPrivateUnixLarge") Integer virtualPrivateUnixLarge, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.sqlInstances <= :sqlInstances group by HI.hostingId order by YDI.sqlInstances desc")
	public List<HostingInfo> findLowBenchMarkDealForSqlInstances(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("sqlInstances") Integer sqlInstances, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.sqlInstances > :sqlInstances group by HI.hostingId order by YDI.sqlInstances asc")
	public List<HostingInfo> findHighBenchMarkDealForSqlInstances(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("sqlInstances") Integer sqlInstances, Pageable pageable);


	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.cotsInstallations <= :cotsInstallations group by HI.hostingId order by YDI.cotsInstallations desc")
	public List<HostingInfo> findLowBenchMarkDealForCots(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("cotsInstallations") Integer cotsInstallations, Pageable pageable);

	@Query("select HI from HostingInfo HI JOIN HI.hostingYearlyDataInfoList YDI JOIN HI.dealInfo DI where DI.dealType = :dealType  "
			+ "and HI.offshoreAllowed =:offshoreAllowed and LOWER(HI.levelOfService) =LOWER(:levelOfService) and HI.includeHardware =:includeHardware "
			+ "and HI.includeTooling =:includeTooling and LOWER(HI.coLocation) =LOWER(:coLocation)  "
			+ "and YDI.cotsInstallations > :cotsInstallations group by HI.hostingId order by YDI.cotsInstallations asc")
	public List<HostingInfo> findHighBenchMarkDealForCots(@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService, @Param("includeHardware") boolean includeHardware,
			@Param("includeTooling") boolean includeTooling, @Param("coLocation") String coLocation,
			@Param("dealType") String dealType, @Param("cotsInstallations") Integer cotsInstallations, Pageable pageable);

}
