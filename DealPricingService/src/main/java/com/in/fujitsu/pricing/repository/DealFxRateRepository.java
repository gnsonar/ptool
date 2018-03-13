package com.in.fujitsu.pricing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface DealFxRateRepository extends JpaRepository<DealFXRatesInfo, Long> {

	public List<DealFXRatesInfo> findByDealInfo(DealInfo dealInfo);


}
