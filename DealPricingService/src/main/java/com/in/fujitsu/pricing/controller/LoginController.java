package com.in.fujitsu.pricing.controller;

import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.exception.UserNotFoundException;

/**
 * @author mishrasub
 *
 */
public interface LoginController {

	public UserInfoDto login(String authString) throws UserNotFoundException;

	public String logout(Long userId) throws UserNotFoundException;

	public UserInfoDto registerUser(UserInfoDto userInfoDto) throws UserNotFoundException;

}
