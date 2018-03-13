package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class HostingCoLocationInfoDto implements Serializable {

	private static final long serialVersionUID = 3289810068232474965L;

	private Integer coLocationId;

	private String coLocationName;

}
