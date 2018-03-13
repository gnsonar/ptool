package com.in.fujitsu.pricing.storage.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class StorageSolutionsInfoDto implements Serializable {

	private static final long serialVersionUID = 9106312644191162534L;

	private Integer solutionId;
	private String solutionName;
	private String solutionDesc;
	private Float performanceValue;
	private Float nonPerformanceValue;

}
