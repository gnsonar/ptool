package com.in.fujitsu.pricing.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.network.entity.NetworkInfo;
import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class NetworkSpecification {

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgWanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgWanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgWanDevices.subtract(band);
					BigDecimal higherRange = avgWanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("wanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgSmallWanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForSmallWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgSmallWanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgSmallWanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgSmallWanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgSmallWanDevices.subtract(band);
					BigDecimal higherRange = avgSmallWanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("smallWanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgMediumWanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForMediumWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgMediumWanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgMediumWanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgMediumWanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgMediumWanDevices.subtract(band);
					BigDecimal higherRange = avgMediumWanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("mediumWanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLargeWanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForLargeWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLargeWanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgLargeWanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgLargeWanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgLargeWanDevices.subtract(band);
					BigDecimal higherRange = avgLargeWanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("largeWanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgLanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgLanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgLanDevices.subtract(band);
					BigDecimal higherRange = avgLanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("lanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgSmallLanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForSmallLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgSmallLanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgSmallLanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgSmallLanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgSmallLanDevices.subtract(band);
					BigDecimal higherRange = avgSmallLanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("smallLanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgMediumLanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForMediumLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgMediumLanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgMediumLanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgMediumLanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgMediumLanDevices.subtract(band);
					BigDecimal higherRange = avgMediumLanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("mediumLanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLargeLanDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForLargeLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLargeLanDevices, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgLargeLanDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgLargeLanDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgLargeLanDevices.subtract(band);
					BigDecimal higherRange = avgLargeLanDevices.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("largeLanDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWlanControllers
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForWlanControllers(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWlanControllers, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgWlanControllers.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgWlanControllers.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgWlanControllers.subtract(band);
					BigDecimal higherRange = avgWlanControllers.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("wlanControllers"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWlanAccessPoint
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForWlanAccessPoint(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWlanAccessPoint, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgWlanAccessPoint.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgWlanAccessPoint.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgWlanAccessPoint.subtract(band);
					BigDecimal higherRange = avgWlanAccessPoint.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("wlanAccesspoint"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgVpnIpSec
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForVpnIpSec(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgVpnIpSec, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgVpnIpSec.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVpnIpSec.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVpnIpSec.subtract(band);
					BigDecimal higherRange = avgVpnIpSec.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("vpnIpSec"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLoadBalancers
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForLoadBalancers(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLoadBalancers, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgLoadBalancers.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgLoadBalancers.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgLoadBalancers.subtract(band);
					BigDecimal higherRange = avgLoadBalancers.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("loadBalancers"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgVpnIpSec
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForDnsDhcpService(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgVpnIpSec, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgVpnIpSec.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVpnIpSec.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVpnIpSec.subtract(band);
					BigDecimal higherRange = avgVpnIpSec.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("dnsDhcpService"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgFirewalls
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForFireWalls(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgFirewalls, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgFirewalls.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgFirewalls.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgFirewalls.subtract(band);
					BigDecimal higherRange = avgFirewalls.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("firewalls"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgProxies
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForProxies(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgProxies, BigDecimal bandPercentage) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgProxies.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgProxies.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgProxies.subtract(band);
					BigDecimal higherRange = avgProxies.add(band);
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root
							.join("networkYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("proxies"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgWanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("wanDevices"), avgWanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("wanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgSmallWanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkSmallWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgSmallWanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgSmallWanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("smallWanDevices"), avgSmallWanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("smallWanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgMediumWanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkMediumWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgMediumWanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgMediumWanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("mediumWanDevices"), avgMediumWanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("mediumWanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLargeWanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkLargeWanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLargeWanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgLargeWanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("largeWanDevices"), avgLargeWanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("largeWanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgLanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("lanDevices"), avgLanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("lanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgSmallLanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkSmallLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgSmallLanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgSmallLanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("smallLanDevices"), avgSmallLanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("smallLanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgMediumLanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkMediumLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgMediumLanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgMediumLanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("mediumLanDevices"), avgMediumLanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("mediumLanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLargeLanDevices
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkLargeLanDevices(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLargeLanDevices) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgLargeLanDevices.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("largeLanDevices"), avgLargeLanDevices));
					query.orderBy(cb.desc(yearlyRelation.get("largeLanDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWlanControllers
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkWlanControllers(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWlanControllers) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgWlanControllers.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("wlanControllers"), avgWlanControllers));
					query.orderBy(cb.desc(yearlyRelation.get("wlanControllers")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgWlanAccesspoint
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkWlanAccesspoint(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgWlanAccesspoint) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgWlanAccesspoint.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("wlanAccesspoint"), avgWlanAccesspoint));
					query.orderBy(cb.desc(yearlyRelation.get("wlanAccesspoint")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgVpnIpSec
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkVpnIpSec(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgVpnIpSec) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgVpnIpSec.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("vpnIpSec"), avgVpnIpSec));
					query.orderBy(cb.desc(yearlyRelation.get("vpnIpSec")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgLoadBalancers
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkLoadBalancers(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgLoadBalancers) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgLoadBalancers.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("loadBalancers"), avgLoadBalancers));
					query.orderBy(cb.desc(yearlyRelation.get("loadBalancers")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgDnsDhcp
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkDnsDhcpService(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgDnsDhcp) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgDnsDhcp.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("dnsDhcpService"), avgDnsDhcp));
					query.orderBy(cb.desc(yearlyRelation.get("dnsDhcpService")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgFirewalls
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkFirewalls(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgFirewalls) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgFirewalls.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("firewalls"), avgFirewalls));
					query.orderBy(cb.desc(yearlyRelation.get("firewalls")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeHardware
	 * @param levelOfService
	 * @param avgProxies
	 * @return
	 */
	public static Specification<NetworkInfo> specificationForBenchMarkProxies(boolean offshoreAllowed,
			boolean includeHardware, String levelOfService, BigDecimal avgProxies) {
		return new Specification<NetworkInfo>() {
			@Override
			public Predicate toPredicate(Root<NetworkInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NetworkInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgProxies.equals(0)) {
					Join<NetworkInfo, NetworkYearlyDataInfo> yearlyRelation = root.join("networkYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("proxies"), avgProxies));
					query.orderBy(cb.desc(yearlyRelation.get("proxies")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
