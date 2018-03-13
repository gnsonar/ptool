package com.in.fujitsu.pricing.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.network.entity.NetworkWlanAccessPointFactorInfo;

@Repository
public interface NetworkWlanAccessPointFactorRepository extends JpaRepository<NetworkWlanAccessPointFactorInfo, Long> {

}