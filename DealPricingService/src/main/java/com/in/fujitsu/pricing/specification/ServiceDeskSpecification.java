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
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskInfo;
import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;

/**
 * @author ChhabrMa
 *
 */
public class ServiceDeskSpecification {


	/**
	 * @param offshoreAllowed
	 * @param multiLingual
	 * @param toolingIncluded
	 * @param levelOfService
	 * @param avgTotalContacts
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForTotalContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService, BigDecimal avgTotalContacts,
			BigDecimal bandPercentage) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgTotalContacts.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgTotalContacts.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgTotalContacts.subtract(band);
					BigDecimal higherRange = avgTotalContacts.add(band);
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root
							.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("totalContacts"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgVoiceContacts
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForVoiceContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService, BigDecimal avgVoiceContacts,
			BigDecimal bandPercentage) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgVoiceContacts.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgVoiceContacts.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgVoiceContacts.subtract(band);
					BigDecimal higherRange = avgVoiceContacts.add(band);
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root
							.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("voiceContacts"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgMailContacts
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForMailContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService, BigDecimal avgMailContacts,
			BigDecimal bandPercentage) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgMailContacts.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgMailContacts.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgMailContacts.subtract(band);
					BigDecimal higherRange = avgMailContacts.add(band);
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root
							.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("mailContacts"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgChatContacts
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForChatContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService, BigDecimal avgChatContacts,
			BigDecimal bandPercentage) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgChatContacts.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgChatContacts.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgChatContacts.subtract(band);
					BigDecimal higherRange = avgChatContacts.add(band);
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root
							.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("chatContacts"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param offshoreAllowed
	 * @param levelOfService
	 * @param avgPortaContacts
	 * @param bandPercentage
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForPortalContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService, BigDecimal avgPortaContacts,
			BigDecimal bandPercentage) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.PAST_DEAL.getName()));
				predicates.add(cb.equal(dealRelation.get("dealStatus"), DealStatusEnum.PUBLISHED.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				if (!avgPortaContacts.equals(0) && !bandPercentage.equals(100)) {
					BigDecimal band = avgPortaContacts.multiply(bandPercentage).divide(new BigDecimal(100));
					BigDecimal lowerRange = avgPortaContacts.subtract(band);
					BigDecimal higherRange = avgPortaContacts.add(band);
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root
							.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.between(yearlyRelation.get("portalContacts"), lowerRange, higherRange));
				}

				query.groupBy(dealRelation.get("dealId"));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param avgTotalContacts
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForBenchMarkTotalContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService,BigDecimal avgTotalContacts) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgTotalContacts.equals(0)) {
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("totalContacts"), avgTotalContacts));
					query.orderBy(cb.desc(yearlyRelation.get("totalContacts")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param avgVoiceContacts
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForBenchMarkVoiceContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService,BigDecimal avgVoiceContacts) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgVoiceContacts.equals(0)) {
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("voiceContacts"), avgVoiceContacts));
					query.orderBy(cb.desc(yearlyRelation.get("voiceContacts")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param avgMailContacts
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForBenchMarkMailContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService,BigDecimal avgMailContacts) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgMailContacts.equals(0)) {
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("mailContacts"), avgMailContacts));
					query.orderBy(cb.desc(yearlyRelation.get("mailContacts")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param avgChatContacts
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForBenchMarkChatContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService,BigDecimal avgChatContacts) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgChatContacts.equals(0)) {
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("chatContacts"), avgChatContacts));
					query.orderBy(cb.desc(yearlyRelation.get("chatContacts")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

	/**
	 * @param avgPortalContacts
	 * @return
	 */
	public static Specification<ServiceDeskInfo> specificationForBenchMarkPortalContacts(boolean offshoreAllowed,
			boolean multiLingual, boolean toolingIncluded, String levelOfService,BigDecimal avgPortalContacts) {
		return new Specification<ServiceDeskInfo>() {
			@Override
			public Predicate toPredicate(Root<ServiceDeskInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<ServiceDeskInfo, DealInfo> dealRelation = root.join("dealInfo");
				predicates.add(cb.equal(dealRelation.get("dealType"), DealTypeEnum.BENCHMARK_DEAL.getName()));

				predicates.add(cb.equal(root.get("offshoreAllowed"), offshoreAllowed));
				predicates.add(cb.equal(root.get("multiLingual"), multiLingual));
				predicates.add(cb.equal(root.get("toolingIncluded"), toolingIncluded));
				if (!levelOfService.isEmpty()) {
					predicates.add(cb.equal(cb.lower(root.get("levelOfService")), levelOfService));
				}
				
				if (!avgPortalContacts.equals(0)) {
					Join<ServiceDeskInfo, ServiceDeskYearlyDataInfo> yearlyRelation = root.join("serviceDeskYearlyDataInfoList");
					predicates.add(cb.lessThanOrEqualTo(yearlyRelation.get("portalContacts"), avgPortalContacts));
					query.orderBy(cb.desc(yearlyRelation.get("portalContacts")));
				}

				query.groupBy(dealRelation.get("dealId"));

				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}

}
