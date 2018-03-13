package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TimestampDto {

	private String createdBy;
	private String modifiedBy;
	private String createdTimestamp;
	private String modifiedTimestamp;
}
