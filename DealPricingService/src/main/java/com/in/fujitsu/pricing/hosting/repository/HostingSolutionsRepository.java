package com.in.fujitsu.pricing.hosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.hosting.entity.HostingSolutionInfo;

public interface HostingSolutionsRepository extends JpaRepository<HostingSolutionInfo, Long> {

}
