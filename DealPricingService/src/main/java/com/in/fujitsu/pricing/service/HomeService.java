package com.in.fujitsu.pricing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.FindDealDto;
import com.in.fujitsu.pricing.dto.LatestUpdatesDto;
import com.in.fujitsu.pricing.dto.RecentDealsDto;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.Message;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.enums.DealStatusEnum;
import com.in.fujitsu.pricing.repository.DealRepository;
import com.in.fujitsu.pricing.repository.MessageRepository;
import com.in.fujitsu.pricing.specification.DealSpecification;
import com.in.fujitsu.pricing.utility.ModelConvertor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maninder
 *
 */
@Slf4j
@Service
public class HomeService {

	@Autowired
	private DealRepository dealRepository;

	@Autowired
	private MessageRepository messageRepository;

	/**
	 * Get recent deals for a user
	 * @param userId
	 * @return
	 */
	public Page<RecentDealsDto> getRecentDeals(Long userId,Pageable pageable) {
		final UserInfo userInfo = new UserInfo();
		List<RecentDealsDto> recentDealsDtoList = new ArrayList<>();
		userInfo.setUserId(userId);
		Page<DealInfo> dealInfoList= dealRepository.findByUserInfo(userInfo, DealStatusEnum.OPEN.getName(), pageable);
		 for(DealInfo dealInfo: dealInfoList) {
			 RecentDealsDto recentDealsDto =  ModelConvertor.prepareRecentDealsDto(dealInfo);
			 recentDealsDtoList.add(recentDealsDto);
		 }
		 return new PageImpl<>(recentDealsDtoList, pageable, dealInfoList.getTotalElements());
	}

	/**
	 * Get latest updates
	 *
	 * @param pageable
	 * @return
	 */
	public List<LatestUpdatesDto> getLatestUpdates() {
		List<LatestUpdatesDto> latestUpdatesDtos = new ArrayList<LatestUpdatesDto>();
		List<Message> messagesList = messageRepository.findTop5ByOrderByCreationDateDesc();
		for (Message message : messagesList) {
			LatestUpdatesDto latestUpdatesDto = ModelConvertor.prepareLatestUpdatesDto(new DealInfo(), message);
			latestUpdatesDtos.add(latestUpdatesDto);
		}

		List<DealInfo> dealInfoList = dealRepository.findTop15ByOrderByModificationDateDesc();
		for (DealInfo dealInfo : dealInfoList) {
			LatestUpdatesDto latestUpdatesDto = ModelConvertor.prepareLatestUpdatesDto(dealInfo, new Message());
			latestUpdatesDtos.add(latestUpdatesDto);
		}

		return latestUpdatesDtos;
	}

	/**
	 * Find the deals based on the deal name or client name and all the deals who
	 * are either WON, LOSS, LOSS ON PRICE, DISCARDED
	 *
	 * @param clientName
	 * @param dealName
	 * @param allDeals
	 * @return
	 */
	public List<FindDealDto> findDeal(String clientName, String dealName, boolean allDeals) {
		List<FindDealDto> findDealDtos = new ArrayList<>();
		Specification<DealInfo> dealSpecification = DealSpecification.findByDealNmClientNmDealStatus(dealName, clientName, allDeals);
		Sort sort = new Sort(new Sort.Order(Direction.DESC, "submissionDate"));
		List<DealInfo> results = dealRepository.findAll(dealSpecification, sort);
		log.info("Find a deal results size :" + results.size());
		for (DealInfo deal : results) {
			FindDealDto findDealDto = ModelConvertor.prepareFindDealDto(deal);
			findDealDtos.add(findDealDto);
		}
		return findDealDtos;

	}

}