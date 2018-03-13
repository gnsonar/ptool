package com.in.fujitsu.pricing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.in.fujitsu.pricing.entity.FXRatesInfo;

/**
 * @author Maninder
 *
 */
@Repository
public interface FxRateRepository extends JpaRepository<FXRatesInfo, Long> {


	@Query(value= "select * from FX_RATES_MASTER GROUP BY currency_from ", nativeQuery=true)
	public List<FXRatesInfo> getGroupedFxRates();

	public List<FXRatesInfo> findByCurrencyFrom(String currency);


}
