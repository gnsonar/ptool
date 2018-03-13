/**
 * 
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.CountryCurrencyInfo;

/**
 * @author Sovit
 *
 */
@Repository
public interface CountryCurrencyRepository extends JpaRepository<CountryCurrencyInfo, Long> {

}
