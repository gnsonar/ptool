package com.in.fujitsu.pricing.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.network.entity.NetworkLanFactorInfo;

@Repository
public interface NetworkLanFactorRepository extends JpaRepository<NetworkLanFactorInfo, Long> {

}