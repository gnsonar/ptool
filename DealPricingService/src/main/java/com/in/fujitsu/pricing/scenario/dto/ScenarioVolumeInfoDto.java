package com.in.fujitsu.pricing.scenario.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ScenarioVolumeInfoDto implements Serializable {

	private static final long serialVersionUID = 6876291437010151869L;

	private Integer hosting;

	private Integer storageBackup;

	private Integer endUser;

	private Integer networkWan;
	private Integer networkLan;

	private Integer serviceDesk;

	private Integer application;

	private Integer retail;

}
