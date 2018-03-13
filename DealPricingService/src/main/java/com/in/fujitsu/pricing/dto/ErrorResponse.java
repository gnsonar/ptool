package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErrorResponse {

	private String message;
	private Object stackTrace;
	private String path;
	private String user;

}
