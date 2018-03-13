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
import com.in.fujitsu.pricing.retail.entity.RetailInfo;
import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;

/**
 * @author MishraSub
 *
 */
public class RetailSpecification {

	
	public static Specification<RetailInfo> specificationForNoOfShops(boolean offshoreAllowed,
			boolean includeHardware, String equipmentAge, String equipmentSet, BigDecimal avgNoOfShops,
			BigDecimal bandPercentage) {
		return new Specification<RetailInfo>() {
			@Override
			public Predicate toPredicate(Root<RetailInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<RetailInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!equipmentAge.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("equipmentAge")), equipmentAge));
				}
				if (!equipmentSet.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("equipmentSet")), equipmentSet));
				}
				
				if (!avgNoOfShops.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgNoOfShops.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgNoOfShops.subtract(band);
					BigDecimal higherRange = avgNoOfShops.add(band);
					Join<RetailInfo, RetailYearlyDataInfo> yearlyRelation = root
							.join("retailYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("noOfShops"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}


	public static Specification<RetailInfo> specificationForBenchMarkNoOfShops(boolean offshoreAllowed,
			boolean includeHardware, String equipmentAge, String equipmentSet, BigDecimal avgNoOfShops) {
		return new Specification<RetailInfo>() {
			@Override
			public Predicate toPredicate(Root<RetailInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<RetailInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("includeHardware"), includeHardware));
				if (!equipmentAge.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("equipmentAge")), equipmentAge));
				}
				if (!equipmentSet.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("equipmentSet")), equipmentSet));
				}

				if (!avgNoOfShops.equals(0)) {
					Join<RetailInfo, RetailYearlyDataInfo> yearlyRelation = root
							.join("retailYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("noOfShops"), avgNoOfShops));
					query.orderBy(cb.desc(yearlyRelation.get("noOfShops")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
