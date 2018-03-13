package com.in.fujitsu.pricing.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.in.fujitsu.pricing.dto.FindDealDto;
import com.in.fujitsu.pricing.dto.LatestUpdatesDto;
import com.in.fujitsu.pricing.dto.RecentDealsDto;

public interface HomeController {

	public Page<RecentDealsDto> getRecentDeals(Long userId, Pageable pageable) throws Exception;

	public List<LatestUpdatesDto> getLatestUpdates()throws Exception;

	public List<FindDealDto> findDeal(String clientName, String DealName, boolean allDeals);
}
