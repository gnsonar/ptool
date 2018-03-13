package com.in.fujitsu.pricing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.entity.DealFXRatesInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.repository.DealFxRateRepository;

@Service
public class DealFXRateService {
	@Autowired
	private DealFxRateRepository dealFxRateRepository;

	public List<DealFXRatesInfo> getFxRates(Long id) {
		final DealInfo dealInfo = new DealInfo();
		dealInfo.setDealId(id);
		return dealFxRateRepository.findByDealInfo(dealInfo);
	}

}
