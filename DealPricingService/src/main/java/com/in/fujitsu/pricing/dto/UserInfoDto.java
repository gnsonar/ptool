package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoDto extends ErrorInfoDto {

	private String userName;

	private String reasonForAccess;

	private String region;

	private Short gender;

	private String emailId;

	private long userId;

	private String role;

	private String active;

	private String createdOn;

	private String modifiedDate;

	private String fullName;
	private String status;
	private String approvedBy;

	private String token;

}
