package com.in.fujitsu.pricing.servicedesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskContactRatioInfo;


/**
 * @author mishrasub
 *
 */
@Repository
public interface ServiceDeskContactRatioRepository extends JpaRepository<ServiceDeskContactRatioInfo, Long> {

}
