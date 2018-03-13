package com.in.fujitsu.pricing.enduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.in.fujitsu.pricing.enduser.entity.EndUserUnitPriceInfo;

public interface EndUserUnitPriceRepository extends JpaRepository<EndUserUnitPriceInfo, Long> {

}
