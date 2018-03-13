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
import org.springframework.util.StringUtils;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;
import com.in.fujitsu.pricing.storage.entity.StorageInfo;
import com.in.fujitsu.pricing.storage.entity.StorageYearlyDataInfo;

public class StorageSpecification {

	public static Specification<StorageInfo> specificationForStorage(boolean offshoreAllowed, String serviceWindowSla,
			boolean includeHardware, BigDecimal avgStorageVolume, BigDecimal bandPercentage) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!avgStorageVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal lowerRange = avgStorageVolume.subtract(avgStorageVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					BigDecimal higherRange = avgStorageVolume.add(avgStorageVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("storageVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForPerformancePastDeal(boolean offshoreAllowed,
			String serviceWindowSla, boolean includeHardware, BigDecimal performanceAvgVolume, BigDecimal bandPercentage) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!performanceAvgVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal performaceLowerRange = performanceAvgVolume.subtract(performanceAvgVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					BigDecimal performanceHigherRange = performanceAvgVolume.add(performanceAvgVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("performanceStorage"), performaceLowerRange,
							performanceHigherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForNonPerformancePastDeal(boolean offshoreAllowed,
			String serviceWindowSla, boolean includeHardware, BigDecimal nonPerformanceAvgVolume, BigDecimal bandPercentage) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!nonPerformanceAvgVolume.equals(0) && !bandPercentage.equals(100)) {

					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					BigDecimal nonPerformaceLowerRange = nonPerformanceAvgVolume.subtract(nonPerformanceAvgVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					BigDecimal nonPerformanceHigherRange = nonPerformanceAvgVolume.add(nonPerformanceAvgVolume.multiply(bandPercentage).divide(new BigDecimal(100)));

					predicates.add(cb.between(yearlyRelation.get("nonPerformanceStorage"), nonPerformaceLowerRange,
							nonPerformanceHigherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForBackup(boolean offshore, String serviceWin,
			boolean includeHardware, BigDecimal avgBackupVolume, BigDecimal bandPercentage) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshore));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));

				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				if (!StringUtils.isEmpty(serviceWin)) {
					predicates.add(cb.equal(cb.lower(root.<String> get("serviceWindowSla")), serviceWin.toLowerCase()));
				}

				if (!avgBackupVolume.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal lowerRange = avgBackupVolume.subtract(avgBackupVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					BigDecimal higherRange = avgBackupVolume.add(avgBackupVolume.multiply(bandPercentage).divide(new BigDecimal(100)));
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(cb.between(yearlyRelation.get("backupVolume"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForStorageBenchmark(boolean offshoreAllowed,
			String serviceWindowSla,boolean includeHardware, BigDecimal avgStorageVolume) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));
				
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}

				if (!avgStorageVolume.equals(0)) {
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("storageVolume"), avgStorageVolume));
					query.orderBy(cb.desc(yearlyRelation.get("storageVolume")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForBenchMarkBackupVolume(boolean offshoreAllowed,
			String serviceWindowSla, boolean includeHardware, BigDecimal avgBackupVolume) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));
				
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				
				if (!avgBackupVolume.equals(0)) {
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("backupVolume"), avgBackupVolume));
					query.orderBy(cb.desc(yearlyRelation.get("backupVolume")));
					query.groupBy(dealRelation.get("dealId"));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForPerformanceBenchmark(boolean offshoreAllowed,
			String serviceWindowSla, boolean includeHardware, BigDecimal performanceAvgVolume) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));
				
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				
				if (!performanceAvgVolume.equals(0)) {
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates
							.add(cb.lessThanOrEqualTo(yearlyRelation.get("performanceStorage"), performanceAvgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("performanceStorage")));
					query.groupBy(dealRelation.get("dealId"));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	public static Specification<StorageInfo> specificationForNonPerormanceBenchmark(boolean offshoreAllowed,
			String serviceWindowSla, boolean includeHardware, BigDecimal nonPerformanceAvgVolume) {
		return new Specification<StorageInfo>() {
			@Override
			public Predicate toPredicate(Root<StorageInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<StorageInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));
				
				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!serviceWindowSla.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("serviceWindowSla")), serviceWindowSla));
				}
				
				if (!nonPerformanceAvgVolume.equals(0)) {
					Join<StorageInfo, StorageYearlyDataInfo> yearlyRelation = root.join("storageYearlyDataInfos");
					predicates.add(
							cb.lessThanOrEqualTo(yearlyRelation.get("nonPerformanceStorage"), nonPerformanceAvgVolume));
					query.orderBy(cb.desc(yearlyRelation.get("nonPerformanceStorage")));
					query.groupBy(dealRelation.get("dealId"));
				}

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
