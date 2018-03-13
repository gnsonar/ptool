package com.in.fujitsu.pricing.enduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.enduser.entity.EndUserSolutionInfo;

public interface EndUserSolutionsRepository extends JpaRepository<EndUserSolutionInfo, Long> {

}
