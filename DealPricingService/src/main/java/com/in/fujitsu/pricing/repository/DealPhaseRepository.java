/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealPhase;


/**
 * @author mishrasub
 *
 */
@Repository
public interface DealPhaseRepository extends JpaRepository<DealPhase, Long> {

}
