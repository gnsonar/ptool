package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RecentDealsDto implements Serializable {

	private static final long serialVersionUID = 6264797972703394406L;

	private Long dealId;

	private String dealName;

	private String clientName;

	private Date startDate;

	private Date submissionDate;

	// status will contain either 'active', 'inactive' or any of the deal phase value
	private String dealStatus;

	private Date modificationDate;

}
