package com.in.fujitsu.pricing.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.retail.entity.RetailEquipmentAgeInfo;



/**
 * @author pawarbh
 *
 */
@Repository
public interface RetailEquipmentAgeRepository extends JpaRepository<RetailEquipmentAgeInfo, Long> {

}
