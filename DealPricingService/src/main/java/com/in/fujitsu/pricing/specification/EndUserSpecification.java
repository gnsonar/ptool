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

import com.in.fujitsu.pricing.enduser.entity.EndUserInfo;
import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;


/**
 * @author pawarbh
 *
 */
public class EndUserSpecification {

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForEndUserDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("endUserDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkEndUserDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("endUserDevices"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("endUserDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}


	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("laptops"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("laptops"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("laptops")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForHighEndLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("highEndLaptops"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkHighEndLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("highEndLaptops"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("highEndLaptops")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForStandardLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("standardLaptops"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkStandardLaptops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("standardLaptops"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("standardLaptops")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForDesktops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("desktops"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkDesktops(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("desktops"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("desktops")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForThinClients(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("thinClients"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkThinClients(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("thinClients"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("thinClients")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForMobileDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("mobileDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkMobileDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("mobileDevices"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("mobileDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForImacDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices, BigDecimal bandPercentage) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}
				if (!avgDevices.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgDevices.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgDevices.subtract(band);
					BigDecimal higherRange = avgDevices.add(band);
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("imacDevices"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}



	/**
	 * @param offshoreAllowed
	 * @param includeBreakFix
	 * @param includeHardware
	 * @param resolutionTime
	 * @param avgDevices
	 * @return
	 */
	public static Specification<EndUserInfo> specificationForBenchmarkImacDevices(boolean offshoreAllowed,
			boolean includeBreakFix, boolean includeHardware, String resolutionTime, BigDecimal avgDevices) {
		return new Specification<EndUserInfo>() {
			@Override
			public Predicate toPredicate(Root<EndUserInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<EndUserInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeBreakFix"), includeBreakFix));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!resolutionTime.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("resolutionTime")), resolutionTime));
				}

				if (!avgDevices.equals(0)) {
					Join<EndUserInfo, EndUserYearlyDataInfo> yearlyRelation = root.join("endUserYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("imacDevices"), avgDevices));
					query.orderBy(cb.desc(yearlyRelation.get("imacDevices")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
