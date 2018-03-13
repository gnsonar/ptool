package com.in.fujitsu.pricing.hosting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.hosting.entity.HostingMigrationCostInfo;

/**
 * @author pawarb
 *
 */
@Repository
public interface HostingMigrationCostRepository extends JpaRepository<HostingMigrationCostInfo, Long> {

}
