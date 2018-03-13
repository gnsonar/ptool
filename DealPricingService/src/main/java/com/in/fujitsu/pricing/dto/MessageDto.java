package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageDto {

	private Long messageId;
	private String messageName;
	private String modifiedDate;

}
