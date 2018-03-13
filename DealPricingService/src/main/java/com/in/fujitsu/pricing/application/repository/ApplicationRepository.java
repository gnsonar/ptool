package com.in.fujitsu.pricing.application.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.entity.DealInfo;

@Repository
public interface ApplicationRepository
		extends JpaRepository<ApplicationInfo, Long>, JpaSpecificationExecutor<ApplicationInfo> {

	public ApplicationInfo findByDealInfo(DealInfo dealId);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.totalAppsVolume <= :totalAppsVolume group by AM.id order by AYM.totalAppsVolume desc")
	public List<ApplicationInfo> findLowBenchMarkDealForTotal(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("totalAppsVolume") Integer totalAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.totalAppsVolume > :totalAppsVolume group by AM.id order by AYM.totalAppsVolume asc")
	public List<ApplicationInfo> findHighBenchMarkDealForTotal(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("totalAppsVolume") Integer totalAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.simpleAppsVolume <= :simpleAppsVolume group by AM.id order by AYM.simpleAppsVolume desc")
	public List<ApplicationInfo> findLowBenchMarkDealForSimple(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("simpleAppsVolume") Integer simpleAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.simpleAppsVolume > :simpleAppsVolume group by AM.id order by AYM.simpleAppsVolume asc")
	public List<ApplicationInfo> findHighBenchMarkDealForSimple(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("simpleAppsVolume") Integer simpleAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.mediumAppsVolume <= :mediumAppsVolume group by AM.id order by AYM.mediumAppsVolume desc")
	public List<ApplicationInfo> findLowBenchMarkDealForMedium(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("mediumAppsVolume") Integer mediumAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.mediumAppsVolume > :mediumAppsVolume group by AM.id order by AYM.mediumAppsVolume asc")
	public List<ApplicationInfo> findHighBenchMarkDealForMedium(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("mediumAppsVolume") Integer mediumAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.complexAppsVolume <= :complexAppsVolume group by AM.id order by AYM.complexAppsVolume desc")
	public List<ApplicationInfo> findLowBenchMarkDealForComplex(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("complexAppsVolume") Integer complexAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.complexAppsVolume > :complexAppsVolume group by AM.id order by AYM.complexAppsVolume asc")
	public List<ApplicationInfo> findHighBenchMarkDealForComplex(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("complexAppsVolume") Integer complexAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.veryComplexAppsVolume <= :veryComplexAppsVolume group by AM.id order by AYM.veryComplexAppsVolume desc")
	public List<ApplicationInfo> findLowBenchMarkDealForVeryComplex(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("veryComplexAppsVolume") Integer veryComplexAppsVolume, Pageable pageable);

	@Query("select AM from ApplicationInfo AM JOIN AM.appYearlyDataInfos AYM JOIN AM.dealInfo DM where DM.dealType = :dealType  "
			+ "and AM.offshoreAllowed = :offshoreAllowed and LOWER(AM.levelOfService) =LOWER(:levelOfService) "
			+ "and AYM.veryComplexAppsVolume > :veryComplexAppsVolume group by AM.id order by AYM.veryComplexAppsVolume asc")
	public List<ApplicationInfo> findHighBenchMarkDealForVeryComplex(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("levelOfService") String levelOfService,
			@Param("veryComplexAppsVolume") Integer veryComplexAppsVolume, Pageable pageable);

}
