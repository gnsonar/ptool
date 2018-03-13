package com.in.fujitsu.pricing.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.retail.entity.RetailSolutionsInfo;


/**
 * @author mishrasub
 *
 */
@Repository
public interface RetailSolutionsRepository extends JpaRepository<RetailSolutionsInfo, Long> {

}
