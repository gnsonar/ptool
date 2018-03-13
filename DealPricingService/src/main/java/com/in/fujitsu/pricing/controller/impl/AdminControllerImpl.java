package com.in.fujitsu.pricing.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.AdminController;
import com.in.fujitsu.pricing.dto.MessageDto;
import com.in.fujitsu.pricing.dto.RoleDto;
import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.service.AdminService;

@RestController
@RequestMapping("resources/admin")
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class AdminControllerImpl implements AdminController {

	@Autowired
	private AdminService adminService;

	@Override
	@RequestMapping(value = "/getRoles", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public List<RoleDto> getRoles() throws Exception {
		return adminService.fetchRoles();
	}

	@RequestMapping(value = "/getPendingAccessRequests", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public List<UserInfoDto> getPendingAccessRequests() throws Exception {
		return adminService.getPendingAccessRequests();
	}

	@RequestMapping(value = "/updateAccessRequest", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public UserInfoDto updateAccessRequest(@RequestBody UserInfoDto userInfoDto) throws Exception {
		return adminService.updateAccessRequest(userInfoDto);
	}

	@RequestMapping(value = "/saveAdminMessage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public MessageDto saveAdminMessage(@RequestBody MessageDto messageDto) throws Exception {
		return adminService.saveAdminMessage(messageDto);
	}

}
