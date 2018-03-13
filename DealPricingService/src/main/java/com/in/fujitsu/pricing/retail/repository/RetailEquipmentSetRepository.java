package com.in.fujitsu.pricing.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.retail.entity.RetailEquipmentSetInfo;



/**
 * @author pawarbh
 *
 */
@Repository
public interface RetailEquipmentSetRepository extends JpaRepository<RetailEquipmentSetInfo, Long> {

}
