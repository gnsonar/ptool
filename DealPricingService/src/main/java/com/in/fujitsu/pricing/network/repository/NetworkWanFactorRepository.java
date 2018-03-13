package com.in.fujitsu.pricing.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.network.entity.NetworkWanFactorInfo;

@Repository
public interface NetworkWanFactorRepository extends JpaRepository<NetworkWanFactorInfo, Long> {

}