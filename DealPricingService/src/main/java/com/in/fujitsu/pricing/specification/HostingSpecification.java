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
import com.in.fujitsu.pricing.hosting.entity.HostingInfo;
import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class HostingSpecification {

	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForServers(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("servers"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchServers(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("servers"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("servers")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysical(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physical"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysical(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physical"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physical")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalWin"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalWin"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalWin")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalWinSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalWinSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalWinSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalWinMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalWinMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalWinMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalWinLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalWinLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalWinLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalUnix"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalUnix"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalUnix")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalUnixSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalUnixSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalUnixSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalUnixMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalUnixMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalUnixMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForPhysUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("physicalUnixLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchPhysUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("physicalUnixLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("physicalUnixLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtual(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtual"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtual(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtual"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtual")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPub(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublic"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPub(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublic"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublic")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicWin"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicWin"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicWin")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicWinSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicWinSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicWinSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicWinMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicWinMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicWinMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicWinLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicWinLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicWinLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicUnix"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicUnix"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicUnix")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicUnixSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicUnixSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicUnixSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicUnixMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicUnixMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicUnixMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPubUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPublicUnixLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPubUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPublicUnixLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPublicUnixLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPriv(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivate"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPriv(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivate"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivate")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateWin"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivWin(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateWin"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateWin")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateWinSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivWinSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateWinSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateWinSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateWinMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivWinMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateWinMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateWinMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateWinLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivWinLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateWinLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateWinLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateUnix"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivUnix(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateUnix"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateUnix")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateUnixSmall"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivUnixSmall(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateUnixSmall"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateUnixSmall")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateUnixMedium"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivUnixMedium(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateUnixMedium"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateUnixMedium")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForVirtPrivUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("virtualPrivateUnixLarge"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchVirtPrivUnixLarge(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("virtualPrivateUnixLarge"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("virtualPrivateUnixLarge")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForSqlInstances(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("sqlInstances"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchSqlInstances(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("sqlInstances"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("sqlInstances")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<HostingInfo> specForCotsInstallations(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume, BigDecimal bandPercentage) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}
				if (!avgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVolume.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVolume.subtract(band);
					BigDecimal higherRange = avgVolume.add(band);
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root
							.join("hostingYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("cotsInstallations"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param includeHardware
	 * @param includeTooling
	 * @param coLocation
	 * @param avgVolume
	 * @return
	 */
	public static Specification<HostingInfo> specForBenchCotsInstallations(boolean offshoreAllowed,
			String levelOfService, boolean includeHardware, boolean includeTooling, String coLocation,
			BigDecimal avgVolume) {
		return new Specification<HostingInfo>() {
			@Override
			public Predicate toPredicate(Root<HostingInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<HostingInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				predicates.add(cb.equal(root.get("includeTooling"), includeTooling));
				
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!coLocation.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("coLocation")), coLocation));
				}

				if (!avgVolume.equals(0)) {
					Join<HostingInfo, HostingYearlyDataInfo> yearlyRelation = root.join("hostingYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("cotsInstallations"), avgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("cotsInstallations")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
