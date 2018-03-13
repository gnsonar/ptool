package com.in.fujitsu.pricing.enduser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.enduser.entity.EndUserMigrationCostInfo;

/**
 * @author pawarb
 *
 */
@Repository
public interface EndUserMigrationCostRepository extends JpaRepository<EndUserMigrationCostInfo, Long> {

}
