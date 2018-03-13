package com.in.fujitsu.pricing.enduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.enduser.entity.EndUserYearlyDataInfo;

public interface EndUserYearlyRepository extends JpaRepository<EndUserYearlyDataInfo, Long> {

}
