package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealYearlyDataInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface DealYearlyRepository extends JpaRepository<DealYearlyDataInfo, Long> {

}
