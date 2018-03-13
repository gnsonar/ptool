package com.in.fujitsu.pricing.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.in.fujitsu.pricing.dto.ClientIndustryDto;
import com.in.fujitsu.pricing.dto.CountryCurrencyInfoDto;
import com.in.fujitsu.pricing.dto.FindDealDto;
import com.in.fujitsu.pricing.dto.LatestUpdatesDto;
import com.in.fujitsu.pricing.dto.MessageDto;
import com.in.fujitsu.pricing.dto.RecentDealsDto;
import com.in.fujitsu.pricing.dto.RoleDto;
import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.entity.ClientIndustry;
import com.in.fujitsu.pricing.entity.CountryCurrencyInfo;
import com.in.fujitsu.pricing.entity.DealInfo;
import com.in.fujitsu.pricing.entity.Message;
import com.in.fujitsu.pricing.entity.Role;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.enums.RoleEnum;
import com.in.fujitsu.pricing.enums.UserAccountStatusEnum;

public class HomeModelConvertor/* extends PropertyMap<DealInfo, DealInfoDto>*/{

	private static ModelMapper modelMapper = new ModelMapper();

	public static UserInfoDto prepareUserInfoDto(UserInfo userInfo) {
		return modelMapper.map(userInfo, UserInfoDto.class);
	}

	public static UserInfo prepareUserInfo(FJEmployee employee, UserInfoDto userInfoDto) throws ParseException {
		final UserInfo userInfo = modelMapper.map(userInfoDto, UserInfo.class);
		userInfo.setEmailId(employee.getMail());
		userInfo.setFullName(employee.getFullName());
		userInfo.setRole(RoleEnum.REGULAR.getName());
		userInfo.setStatus(UserAccountStatusEnum.PENDING.getName());
		userInfo.setUserName(employee.getUid());
		Calendar calendar = new GregorianCalendar();
		userInfo.setCreatedOn(new Date(calendar.getTimeInMillis()));
		return userInfo;
	}

	public static MessageDto prepareMessageDto(Message message) {
		return modelMapper.map(message, MessageDto.class);
	}


	public static Message prepareMessage(MessageDto messageDto) {
		Message message = modelMapper.map(messageDto, Message.class);
		message.setCreationDate(new Date());
		message.setModifiedDate(new Date());
		return message;
	}

	public static LatestUpdatesDto prepareLatestUpdatesDto(DealInfo dealInfo, Message message) {
		LatestUpdatesDto latestUpdatesDto = new LatestUpdatesDto();
		latestUpdatesDto.setDealName(dealInfo.getDealName());
		latestUpdatesDto.setClientName(dealInfo.getClientName());
		latestUpdatesDto.setDealStatus(dealInfo.getDealStatus());
		if (dealInfo.getSubmissionDate() != null) {
			latestUpdatesDto.setSubmissionDate(new SimpleDateFormat("yyyy-MM-dd").format(dealInfo.getSubmissionDate()));
		}
		if (dealInfo.getModificationDate() != null) {
			latestUpdatesDto
					.setModificationDate(new SimpleDateFormat("yyyy-MM-dd").format(dealInfo.getModificationDate()));
		}

		latestUpdatesDto.setMessageName(message.getMessageName());
		if (message.getModifiedDate() != null) {
			latestUpdatesDto.setModificationDate(new SimpleDateFormat("yyyy-MM-dd").format(message.getModifiedDate()));
		}

		return latestUpdatesDto;
	}

	public static RecentDealsDto prepareRecentDealsDto(DealInfo dealInfo) {
		return modelMapper.map(dealInfo, RecentDealsDto.class);
	}

	public static FindDealDto prepareFindDealDto(DealInfo dealInfo) {
		FindDealDto findDealDto = modelMapper.map(dealInfo, FindDealDto.class);
		findDealDto.setFullName(dealInfo.getUserInfo().getFullName());
		return findDealDto;
	}

	public static RoleDto prepareRoleDto(Role role ) {
		return modelMapper.map(role, RoleDto.class);
	}

	public static List<ClientIndustryDto> clientIndustryDtoList(List<ClientIndustry> clientindustry) {
		final List<ClientIndustryDto> clientindustrydtolist = new ArrayList<>();
		for (ClientIndustry clientIndustryInfo : clientindustry ) {
			final ClientIndustryDto clientIndustryDto = modelMapper.map(clientIndustryInfo, ClientIndustryDto.class);
			clientindustrydtolist.add(clientIndustryDto);
		}
		return clientindustrydtolist;
	}

	public static List<CountryCurrencyInfoDto> countryCurrencyInfoDtoList(List<CountryCurrencyInfo> countryCurrencyInfoList) {
		final List<CountryCurrencyInfoDto> countryCurrencyInfoDtoList = new ArrayList<>();
		for (CountryCurrencyInfo countryCurrencyInfo : countryCurrencyInfoList ) {
			final CountryCurrencyInfoDto countryCurrencyInfoDto = modelMapper.map(countryCurrencyInfo, CountryCurrencyInfoDto.class);
			countryCurrencyInfoDtoList.add(countryCurrencyInfoDto);
		}
		return countryCurrencyInfoDtoList;
	}

}
