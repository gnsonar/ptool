package com.in.fujitsu.pricing.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.MessageDto;
import com.in.fujitsu.pricing.dto.RoleDto;
import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.entity.Message;
import com.in.fujitsu.pricing.entity.Role;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.enums.UserAccountStatusEnum;
import com.in.fujitsu.pricing.exception.UserNotFoundException;
import com.in.fujitsu.pricing.repository.MessageRepository;
import com.in.fujitsu.pricing.repository.RoleRepository;
import com.in.fujitsu.pricing.repository.UserRepository;
import com.in.fujitsu.pricing.utility.ModelConvertor;

@Service
public class AdminService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private MessageRepository messageRepository;
	@Autowired
	private EmailService emailService;

	@Value("${email.approvedBody}")
	private String approvedBody;

	@Value("${email.rejectBody}")
	private String rejectBody;

	@Value("${email.to}")
	private String cc;

	/**
	 * @return
	 */
	public List<UserInfoDto> getPendingAccessRequests() {
		final List<UserInfoDto> userInfoDtoList = new ArrayList<UserInfoDto>();
		final List<UserInfo> userInfoList = userRepository.findByStatus(UserAccountStatusEnum.PENDING.getName());
		for (UserInfo userInfo : userInfoList) {
			UserInfoDto userInfoDto = ModelConvertor.prepareUserInfoDto(userInfo);
			userInfoDtoList.add(userInfoDto);
		}

		return userInfoDtoList;
	}

	/**
	 * @return
	 */
	public List<RoleDto> fetchRoles() {
		final List<Role> roles = roleRepository.findAll();
		final List<RoleDto> roleDtoList = new ArrayList<>();
		for (Role role : roles) {
			final RoleDto roleDto = ModelConvertor.prepareRoleDto(role);
			roleDtoList.add(roleDto);
		}

		return roleDtoList;
	}

	/**
	 * @param userInfoDto
	 * @throws UserNotFoundException
	 */
	public UserInfoDto updateAccessRequest(UserInfoDto userInfoDto) throws UserNotFoundException {
		final UserInfo existingUserInfo = userRepository.findOne(userInfoDto.getUserId());
		if (existingUserInfo != null) {
			existingUserInfo.setStatus(UserAccountStatusEnum.valueOf(userInfoDto.getStatus().toUpperCase()).getName());
			existingUserInfo.setApprovedBy(userInfoDto.getApprovedBy());
			existingUserInfo.setRole(userInfoDto.getRole());
			Calendar calendar = new GregorianCalendar();
			existingUserInfo.setModifiedDate(new Date(calendar.getTimeInMillis()));
			final UserInfo savedUser = userRepository.save(existingUserInfo);
			String body;
			if (UserAccountStatusEnum.REJECTED.getName().equalsIgnoreCase(userInfoDto.getStatus())) {
				body = rejectBody;
			} else {
				body = approvedBody;
			}
			emailService.sendSimpleMessage(savedUser.getEmailId(), cc, body);
			return ModelConvertor.prepareUserInfoDto(savedUser);
		} else {
			throw new UserNotFoundException("User does not exist");
		}
	}

	/**
	 * @param messageDto
	 * @return
	 */
	public MessageDto saveAdminMessage(MessageDto messageDto) {
		Message message = ModelConvertor.prepareMessage(messageDto);
		message = messageRepository.save(message);
		return ModelConvertor.prepareMessageDto(message);
	}

}
