package com.in.fujitsu.pricing.enduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.enduser.entity.EndUserResolutionTimeInfo;

public interface EndUserResolutionTimeRepository extends JpaRepository<EndUserResolutionTimeInfo, Long> {

}
