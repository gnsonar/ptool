package com.in.fujitsu.pricing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.UserInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface DealRepository extends JpaRepository<DealInfo, Long>, JpaSpecificationExecutor<DealInfo> {

	public DealInfo findByDealNameAndStartDate(String name, Date startDate);

	public DealInfo findByDealId(Long id);

	@Query("select d from DealInfo d where d.userInfo = :userInfo and d.dealStatus = :dealStatus")
	public Page<DealInfo> findByUserInfo(@Param(value = "userInfo") UserInfo userInfo, @Param(value = "dealStatus") String dealStatus, Pageable pageable);

	public List<DealInfo> findTop15ByOrderByModificationDateDesc();

}
