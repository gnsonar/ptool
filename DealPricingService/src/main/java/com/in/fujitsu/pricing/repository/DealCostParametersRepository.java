/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealCostParameters;


/**
 * @author mishrasub
 *
 */
@Repository
public interface DealCostParametersRepository extends JpaRepository<DealCostParameters, Long> {

}
