package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.HomeController;
import com.in.fujitsu.pricing.dto.FindDealDto;
import com.in.fujitsu.pricing.dto.LatestUpdatesDto;
import com.in.fujitsu.pricing.dto.RecentDealsDto;
import com.in.fujitsu.pricing.service.HomeService;

@RestController
@RequestMapping("resources/home")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class HomeControllerImpl implements HomeController {

	@Autowired
	private HomeService homeService;

	@RequestMapping(value = "/getRecentDeals", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<RecentDealsDto> getRecentDeals(Long userId,Pageable pageable) throws Exception {

		return homeService.getRecentDeals(userId,pageable);
	}

	@RequestMapping(value = "/getLatestUpdates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LatestUpdatesDto> getLatestUpdates() throws Exception {
		return homeService.getLatestUpdates();
	}

	@RequestMapping(value = "/findDeal", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FindDealDto> findDeal(String clientName, String dealName, boolean allDeals) {
		return homeService.findDeal(clientName, dealName, allDeals);
	}

}
