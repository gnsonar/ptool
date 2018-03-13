package com.in.fujitsu.pricing.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.enums.DealTypeEnum;

public class DealSpecification {

	public static Specification<DealInfo> findByDealNmClientNmDealStatus(String dealName, String clientName,
			boolean allDeals) {
		return new Specification<DealInfo>() {
			@Override
			public Predicate toPredicate(Root<DealInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (!StringUtils.isEmpty(dealName) && !StringUtils.isEmpty(clientName)) {
					predicates.add(cb.or(cb.like(cb.lower(root.<String> get("dealName")), concatLike(dealName)),
							cb.like(cb.lower(root.<String> get("clientName")), concatLike(clientName))));
				} else if (!StringUtils.isEmpty(dealName)) {
					predicates.add(cb.like(cb.lower(root.get("dealName")), concatLike(dealName)));

				} else if (!StringUtils.isEmpty(clientName)) {
					predicates.add(cb.like(cb.lower(root.get("clientName")), concatLike(clientName)));
				}

				// Check for deal statuses
				final List<String> dealStatuses = new ArrayList<>();
				dealStatuses.add(DealStatusEnum.STORED.getName());
				dealStatuses.add(DealStatusEnum.SUBMITTED.getName());
				if (allDeals) {
					dealStatuses.add(DealStatusEnum.PUBLISHED.getName());

				}
				Expression<String> expression = root.get("dealStatus");
				Predicate predicate = expression.in(dealStatuses);
				predicates.add(predicate);
				predicates.add(cb.equal(cb.lower(root.get("dealType")), DealTypeEnum.PAST_DEAL.getName().toLowerCase()));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	private static String concatLike(String value) {
		return value.toLowerCase() + "%";
	}

}
