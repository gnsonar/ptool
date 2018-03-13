package com.in.fujitsu.pricing.servicedesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.servicedesk.entity.ServiceDeskYearlyDataInfo;


@Repository
public interface ServiceDeskYearlyRepository extends JpaRepository<ServiceDeskYearlyDataInfo, Long> {

}
