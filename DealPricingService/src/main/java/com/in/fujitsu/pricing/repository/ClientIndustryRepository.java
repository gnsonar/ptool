/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.ClientIndustry;

/**
 * @author Sovit
 *
 */

@Repository
public interface ClientIndustryRepository extends JpaRepository<ClientIndustry, Long> {

}
