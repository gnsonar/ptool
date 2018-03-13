package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SuccessResponse {

	public SuccessResponse(String message) {
		this.message = message;
	}

	private String message;

}
