package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAuthDto extends ErrorInfoDto {
	private String username;

	private String password;

	private String isActive;

	private String isLocked;

	private String isExpired;

	/*private List<String> roles;*/
	private String role;

}
