package com.in.fujitsu.pricing.network.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;

public interface NetworkRepository extends JpaRepository<NetworkInfo, Long>, JpaSpecificationExecutor<NetworkInfo> {

	public NetworkInfo findByDealInfo(@Param("dealId") DealInfo dealId);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.wanDevices <= :wanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForWan(
			@Param("dealType") String dealType,
			@Param("wanDevices") Integer wanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.wanDevices > :wanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForWan(
			@Param("dealType") String dealType,
			@Param("wanDevices") Integer wanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.smallWanDevices <= :smallWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.smallWanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForSmallWan(
			@Param("dealType") String dealType,
			@Param("smallWanDevices") Integer smallWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.smallWanDevices > :smallWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.smallWanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForSmallWan(
			@Param("dealType") String dealType,
			@Param("smallWanDevices") Integer smallWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.mediumWanDevices <= :mediumWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.mediumWanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForMediumWan(
			@Param("dealType") String dealType,
			@Param("mediumWanDevices") Integer mediumWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.mediumWanDevices > :mediumWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.mediumWanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForMediumWan(
			@Param("dealType") String dealType,
			@Param("mediumWanDevices") Integer mediumWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.largeWanDevices <= :largeWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.largeWanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForLargeWan(
			@Param("dealType") String dealType,
			@Param("largeWanDevices") Integer largeWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.largeWanDevices > :largeWanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.largeWanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForLargeWan(
			@Param("dealType") String dealType,
			@Param("largeWanDevices") Integer largeWanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.lanDevices <= :lanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.lanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForLan(
			@Param("dealType") String dealType,
			@Param("lanDevices") Integer lanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.lanDevices > :lanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.lanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForLan(
			@Param("dealType") String dealType,
			@Param("lanDevices") Integer lanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.smallLanDevices <= :smallLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.smallLanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForSmallLan(
			@Param("dealType") String dealType,
			@Param("smallLanDevices") Integer smallLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.smallLanDevices > :smallLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.smallLanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForSmallLan(
			@Param("dealType") String dealType,
			@Param("smallLanDevices") Integer smallLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.mediumLanDevices <= :mediumLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.mediumLanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForMediumLan(
			@Param("dealType") String dealType,
			@Param("mediumLanDevices") Integer mediumLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.mediumLanDevices > :mediumLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.mediumLanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForMediumLan(
			@Param("dealType") String dealType,
			@Param("mediumLanDevices") Integer mediumLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.largeLanDevices <= :largeLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.largeLanDevices desc")
	public List<NetworkInfo> findLowBenchMarkDealForLargeLan(
			@Param("dealType") String dealType,
			@Param("largeLanDevices") Integer largeLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.largeLanDevices > :largeLanDevices "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.largeLanDevices asc")
	public List<NetworkInfo> findHighBenchMarkDealForLargeLan(
			@Param("dealType") String dealType,
			@Param("largeLanDevices") Integer largeLanDevices,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.wlanControllers <= :wlanControllers "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wlanControllers desc")
	public List<NetworkInfo> findLowBenchMarkDealForWlanControllers(
			@Param("dealType") String dealType,
			@Param("wlanControllers") Integer wlanControllers,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.wlanControllers > :wlanControllers "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wlanControllers asc")
	public List<NetworkInfo> findHighBenchMarkDealForWlanControllers(
			@Param("dealType") String dealType,
			@Param("wlanControllers") Integer wlanControllers,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);


	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.wlanAccesspoint <= :wlanAccesspoint "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wlanAccesspoint desc")
	public List<NetworkInfo> findLowBenchMarkDealForWlanAccesspoint(
			@Param("dealType") String dealType,
			@Param("wlanAccesspoint") Integer wlanAccesspoint,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.wlanAccesspoint > :wlanAccesspoint "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.wlanAccesspoint asc")
	public List<NetworkInfo> findHighBenchMarkDealForWlanAccesspoint(
			@Param("dealType") String dealType,
			@Param("wlanAccesspoint") Integer wlanAccesspoint,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.loadBalancers <= :loadBalancers "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.loadBalancers desc")
	public List<NetworkInfo> findLowBenchMarkDealForLoadBalancers(
			@Param("dealType") String dealType,
			@Param("loadBalancers") Integer loadBalancers,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.loadBalancers > :loadBalancers "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.loadBalancers asc")
	public List<NetworkInfo> findHighBenchMarkDealForLoadBalancers(
			@Param("dealType") String dealType,
			@Param("loadBalancers") Integer loadBalancers,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);


	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.vpnIpSec <= :vpnIpSec "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.vpnIpSec desc")
	public List<NetworkInfo> findLowBenchMarkDealForVpnIpSec(
			@Param("dealType") String dealType,
			@Param("vpnIpSec") Integer vpnIpSec,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.vpnIpSec > :vpnIpSec "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.vpnIpSec asc")
	public List<NetworkInfo> findHighBenchMarkDealForVpnIpSec(
			@Param("dealType") String dealType,
			@Param("vpnIpSec") Integer vpnIpSec,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);


	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.dnsDhcpService <= :dnsDhcpService "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.dnsDhcpService desc")
	public List<NetworkInfo> findLowBenchMarkDealForDnsDhcpService(
			@Param("dealType") String dealType,
			@Param("dnsDhcpService") Integer dnsDhcpService,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.dnsDhcpService > :dnsDhcpService "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.dnsDhcpService asc")
	public List<NetworkInfo> findHighBenchMarkDealForDnsDhcpService(
			@Param("dealType") String dealType,
			@Param("dnsDhcpService") Integer dnsDhcpService,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.firewalls <= :firewalls "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.firewalls desc")
	public List<NetworkInfo> findLowBenchMarkDealForFirewalls(
			@Param("dealType") String dealType,
			@Param("firewalls") Integer firewalls,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.firewalls > :firewalls "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.firewalls asc")
	public List<NetworkInfo> findHighBenchMarkDealForFirewalls(
			@Param("dealType") String dealType,
			@Param("firewalls") Integer firewalls,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM "
			+ "where DM.dealType = :dealType and NYI.proxies <= :proxies "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.proxies desc")
	public List<NetworkInfo> findLowBenchMarkDealForProxies(
			@Param("dealType") String dealType,
			@Param("proxies") Integer proxies,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

	@Query("select NI from NetworkInfo NI JOIN NI.networkYearlyDataInfoList NYI JOIN NI.dealInfo DM where "
			+ "DM.dealType = :dealType and NYI.proxies > :proxies "
			+ "and NI.offshoreAllowed = :offshoreAllowed and NI.includeHardware = :includeHardware and LOWER(NI.levelOfService) = LOWER(:levelOfService) "
			+ "group by NI.networkId order by NYI.proxies asc")
	public List<NetworkInfo> findHighBenchMarkDealForProxies(
			@Param("dealType") String dealType,
			@Param("proxies") Integer proxies,
			@Param("offshoreAllowed") Boolean offshoreAllowed,
			@Param("includeHardware") Boolean includeHardware,
			@Param("levelOfService") String levelOfService,
			Pageable pageable);

}
