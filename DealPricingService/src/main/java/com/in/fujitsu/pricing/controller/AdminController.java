package com.in.fujitsu.pricing.controller;

import java.util.List;

import com.in.fujitsu.pricing.dto.MessageDto;
import com.in.fujitsu.pricing.dto.RoleDto;
import com.in.fujitsu.pricing.dto.UserInfoDto;

public interface AdminController {

	public List<RoleDto> getRoles() throws Exception;

	public List<UserInfoDto> getPendingAccessRequests() throws Exception;

	public UserInfoDto updateAccessRequest(UserInfoDto userInfoDto) throws Exception;

	public MessageDto saveAdminMessage(MessageDto messageDto) throws Exception;

}
