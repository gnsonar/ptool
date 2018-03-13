/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.CountryFactorInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface CountryFactorRepository extends JpaRepository<CountryFactorInfo, Long> {

}
