/**
 *
 */
package com.in.fujitsu.pricing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.ServiceWindow;


/**
 * @author mishrasub
 *
 */
@Repository
public interface ServiceWindowRepository extends JpaRepository<ServiceWindow, Long> {

}
