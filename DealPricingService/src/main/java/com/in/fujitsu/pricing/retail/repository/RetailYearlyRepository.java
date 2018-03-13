package com.in.fujitsu.pricing.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.retail.entity.RetailYearlyDataInfo;


@Repository
public interface RetailYearlyRepository extends JpaRepository<RetailYearlyDataInfo, Long> {

}
