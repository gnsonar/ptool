package com.in.fujitsu.pricing.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class LatestUpdatesDto implements Serializable{
	private static final long serialVersionUID = -3985726775714735156L;

	private String dealName;

	private String clientName;

	private String messageName;

	private String modificationDate;

	private String submissionDate;

	private String dealStatus;

}
