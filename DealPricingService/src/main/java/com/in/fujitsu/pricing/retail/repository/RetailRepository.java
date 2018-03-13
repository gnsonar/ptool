package com.in.fujitsu.pricing.retail.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.retail.entity.RetailInfo;

@Repository
public interface RetailRepository extends JpaRepository<RetailInfo, Long>, JpaSpecificationExecutor<RetailInfo> {

	public RetailInfo findByDealInfo(DealInfo dealId);

	@Query("select RM from RetailInfo RM JOIN RM.retailYearlyDataInfoList RYM JOIN RM.dealInfo DM where DM.dealType = :dealType  "
			+ "and RM.offshoreAllowed =:offshoreAllowed and RM.includeHardware =:includeHardware "
			+ "and LOWER(RM.equipmentAge) =LOWER(:equipmentAge) and LOWER(RM.equipmentSet) =LOWER(:equipmentSet)  "
			+ "and RYM.noOfShops <= :noOfShops group by RM.retailId order by RYM.noOfShops desc")
	public List<RetailInfo> findLowBenchMarkDealForNoOfShops(
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("equipmentAge") String equipmentAge,
			@Param("equipmentSet") String equipmentSet,
			@Param("dealType") String dealType,
			@Param("noOfShops") Integer noOfShops, Pageable pageable);

	@Query("select RM from RetailInfo RM JOIN RM.retailYearlyDataInfoList RYM JOIN RM.dealInfo DM where DM.dealType = :dealType  "
			+ "and RM.offshoreAllowed =:offshoreAllowed and RM.includeHardware =:includeHardware "
			+ "and LOWER(RM.equipmentAge) =LOWER(:equipmentAge) and LOWER(RM.equipmentSet) =LOWER(:equipmentSet)  "
			+ "and RYM.noOfShops > :noOfShops group by RM.retailId order by RYM.noOfShops asc")
	public List<RetailInfo> findHighBenchMarkDealForNoOfShops(
			@Param("offshoreAllowed") boolean offshoreAllowed,
			@Param("includeHardware") boolean includeHardware,
			@Param("equipmentAge") String equipmentAge,
			@Param("equipmentSet") String equipmentSet,
			@Param("dealType") String dealType,
			@Param("noOfShops") Integer noOfShops, Pageable pageable);

}
