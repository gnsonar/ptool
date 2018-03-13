package com.in.fujitsu.pricing.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ErrorInfoDto {
	public boolean error;
	public String errorFocus;
	List<String> lstErrors;

}