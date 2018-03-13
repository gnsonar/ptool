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

import com.in.fujitsu.pricing.application.entity.ApplicationInfo;
import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;

/**
 * @author ChhabrMa
 *
 */
public class ApplicationSpecification {

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalApps
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForTotalApps(boolean offshoreAllowed, String levelOfService,
			BigDecimal avgTotalApps, BigDecimal bandPercentage) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgTotalApps.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgTotalApps.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgTotalApps.subtract(band);
					BigDecimal higherRange = avgTotalApps.add(band);
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("totalAppsVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgSimpleApps
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForSimpleApps(boolean offshoreAllowed, String levelOfService,
			BigDecimal avgSimpleApps, BigDecimal bandPercentage) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgSimpleApps.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgSimpleApps.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgSimpleApps.subtract(band);
					BigDecimal higherRange = avgSimpleApps.add(band);
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("simpleAppsVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgMediumApps
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForMediumApps(boolean offshoreAllowed, String levelOfService,
			BigDecimal avgMediumApps, BigDecimal bandPercentage) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgMediumApps.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgMediumApps.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgMediumApps.subtract(band);
					BigDecimal higherRange = avgMediumApps.add(band);
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("mediumAppsVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgComplexApps
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForComplexApps(boolean offshoreAllowed, String levelOfService,
			BigDecimal avgComplexApps, BigDecimal bandPercentage) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgComplexApps.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgComplexApps.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgComplexApps.subtract(band);
					BigDecimal higherRange = avgComplexApps.add(band);
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("complexAppsVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgVeryComplexApps
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForVeryComplexApps(boolean offshoreAllowed, String levelOfService,
			BigDecimal avgVeryComplexApps, BigDecimal bandPercentage) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgVeryComplexApps.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVeryComplexApps.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVeryComplexApps.subtract(band);
					BigDecimal higherRange = avgVeryComplexApps.add(band);
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("veryComplexAppsVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalAppsVolume
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForBenchMarkTotalApps(boolean offshoreAllowed,
			String levelOfService, BigDecimal avgTotalAppsVolume) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));
				
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}

				if (!avgTotalAppsVolume.equals(0)) {
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("totalAppsVolume"), avgTotalAppsVolume));
					query.orderBy(cb.desc(yearlyRelation.get("totalAppsVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalAppsVolume
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForBenchMarkSimpleApps(boolean offshoreAllowed,
			String levelOfService, BigDecimal avgTotalAppsVolume) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgTotalAppsVolume.equals(0)) {
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("simpleAppsVolume"), avgTotalAppsVolume));
					query.orderBy(cb.desc(yearlyRelation.get("simpleAppsVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalAppsVolume
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForBenchMarkMediumApps(boolean offshoreAllowed,
			String levelOfService, BigDecimal avgTotalAppsVolume) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgTotalAppsVolume.equals(0)) {
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("mediumAppsVolume"), avgTotalAppsVolume));
					query.orderBy(cb.desc(yearlyRelation.get("mediumAppsVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalAppsVolume
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForBenchMarkComplexApps(boolean offshoreAllowed,
			String levelOfService, BigDecimal avgTotalAppsVolume) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgTotalAppsVolume.equals(0)) {
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("complexAppsVolume"), avgTotalAppsVolume));
					query.orderBy(cb.desc(yearlyRelation.get("complexAppsVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	
	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgTotalAppsVolume
	 * @return
	 */
	public static Specification<ApplicationInfo> specificationForBenchMarkVeryComplexApps(boolean offshoreAllowed,
			String levelOfService, BigDecimal avgTotalAppsVolume) {
		return new Specification<ApplicationInfo>() {
			@Override
			public Predicate toPredicate(Root<ApplicationInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ApplicationInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgTotalAppsVolume.equals(0)) {
					Join<ApplicationInfo, ApplicationYearlyDataInfo> yearlyRelation = root.join("appYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("veryComplexAppsVolume"), avgTotalAppsVolume));
					query.orderBy(cb.desc(yearlyRelation.get("veryComplexAppsVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
