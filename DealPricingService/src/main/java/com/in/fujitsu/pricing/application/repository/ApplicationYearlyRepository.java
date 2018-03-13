package com.in.fujitsu.pricing.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.application.entity.ApplicationYearlyDataInfo;


@Repository
public interface ApplicationYearlyRepository extends JpaRepository<ApplicationYearlyDataInfo, Long>{

}
