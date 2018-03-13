/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealType;


/**
 * @author mishrasub
 *
 */
@Repository
public interface DealTypeRepository extends JpaRepository<DealType, Long> {

}
