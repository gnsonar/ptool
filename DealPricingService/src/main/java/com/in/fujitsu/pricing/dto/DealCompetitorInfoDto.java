package com.in.fujitsu.pricing.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class DealCompetitorInfoDto implements Serializable {
	private static final long serialVersionUID = 2192439884980073825L;

	private Long id;
	private String name;
	private String type;
	private String industryName;

}
