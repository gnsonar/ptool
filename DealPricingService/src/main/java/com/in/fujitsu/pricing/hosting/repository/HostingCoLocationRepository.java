package com.in.fujitsu.pricing.hosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.hosting.entity.HostingCoLocationInfo;

public interface HostingCoLocationRepository extends JpaRepository<HostingCoLocationInfo, Long> {

}
