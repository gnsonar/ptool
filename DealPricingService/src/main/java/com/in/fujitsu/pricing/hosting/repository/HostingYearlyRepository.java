package com.in.fujitsu.pricing.hosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.hosting.entity.HostingYearlyDataInfo;


@Repository
public interface HostingYearlyRepository extends JpaRepository<HostingYearlyDataInfo, Long> {

}
