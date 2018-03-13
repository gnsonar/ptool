package com.in.fujitsu.pricing.servicedesk.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;


@Repository
public interface ServiceDeskRepository extends JpaRepository<ServiceDeskInfo, Long>, JpaSpecificationExecutor<ServiceDeskInfo> {

	public ServiceDeskInfo findByDealInfo(DealInfo dealId);

	@Query("select s from ServiceDeskInfo s where s.dealInfo = :dealId")
	public ServiceDeskInfo findServiceDeskDetailsByDealInfo(@Param("dealId")DealInfo dealId);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.totalContacts <= :totalContacts group by SM.serviceDeskId order by SYM.totalContacts desc")
	public List<ServiceDeskInfo> findLowBenchMarkDealForTotalContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("totalContacts") Integer totalContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.totalContacts > :totalContacts group by SM.serviceDeskId order by SYM.totalContacts asc")
	public List<ServiceDeskInfo> findHighBenchMarkDealForTotalContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("totalContacts") Integer totalContacts, Pageable pageable);


	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.voiceContacts <= :voiceContacts group by SM.serviceDeskId order by SYM.voiceContacts desc")
	public List<ServiceDeskInfo> findLowBenchMarkDealForVoiceContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("voiceContacts") Integer voiceContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.voiceContacts > :voiceContacts group by SM.serviceDeskId order by SYM.voiceContacts asc")
	public List<ServiceDeskInfo> findHighBenchMarkDealForVoiceContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("voiceContacts") Integer voiceContacts, Pageable pageable);


	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.mailContacts <= :mailContacts group by SM.serviceDeskId order by SYM.mailContacts desc")
	public List<ServiceDeskInfo> findLowBenchMarkDealForMailContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("mailContacts") Integer mailContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.mailContacts > :mailContacts group by SM.serviceDeskId order by SYM.mailContacts asc")
	public List<ServiceDeskInfo> findHighBenchMarkDealForMailContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("mailContacts") Integer mailContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.chatContacts <= :chatContacts group by SM.serviceDeskId order by SYM.chatContacts desc")
	public List<ServiceDeskInfo> findLowBenchMarkDealForChatContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("chatContacts") Integer chatContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.chatContacts > :chatContacts group by SM.serviceDeskId order by SYM.chatContacts asc")
	public List<ServiceDeskInfo> findHighBenchMarkDealForChatContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("chatContacts") Integer chatContacts, Pageable pageable);


	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.portalContacts <= :portalContacts group by SM.serviceDeskId order by SYM.portalContacts desc")
	public List<ServiceDeskInfo> findLowBenchMarkDealForPortalContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("portalContacts") Integer portalContacts, Pageable pageable);

	@Query("select SM from ServiceDeskInfo SM JOIN SM.serviceDeskYearlyDataInfoList SYM JOIN SM.dealInfo DM where DM.dealType = :dealType  "
			+ "and SM.offshoreAllowed = :offshoreAllowed and SM.multiLingual = :multiLingual "
			+ "and SM.toolingIncluded = :toolingIncluded and LOWER(SM.levelOfService) =LOWER(:levelOfService) "
			+ "and SYM.portalContacts > :portalContacts group by SM.serviceDeskId order by SYM.portalContacts asc")
	public List<ServiceDeskInfo> findHighBenchMarkDealForPortalContacts(
			@Param("dealType") String dealType,
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("multiLingual") boolean multiLingual,
			@Param("toolingIncluded") boolean toolingIncluded,
			@Param("levelOfService") String levelOfService,
			@Param("portalContacts") Integer portalContacts, Pageable pageable);


}
