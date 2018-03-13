package com.in.fujitsu.pricing.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class AssumptionResponse {

	private Map<String, List<AssumptionDetailDto>> assumptions;

}
