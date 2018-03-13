package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.TowerSpecificBandInfo;

@Repository
public interface TowerSpecificBandRepository extends JpaRepository<TowerSpecificBandInfo, Long> {

	public TowerSpecificBandInfo findByTowerName(String towerName);

}