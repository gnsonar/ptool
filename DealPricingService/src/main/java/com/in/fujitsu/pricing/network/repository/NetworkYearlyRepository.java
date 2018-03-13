package com.in.fujitsu.pricing.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.network.entity.NetworkYearlyDataInfo;


@Repository
public interface NetworkYearlyRepository extends JpaRepository<NetworkYearlyDataInfo, Long> {

}
