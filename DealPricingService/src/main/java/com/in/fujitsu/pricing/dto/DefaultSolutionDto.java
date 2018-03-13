package com.in.fujitsu.pricing.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DefaultSolutionDto {
	private long id;
	private int perDataCenterWanDevce;
	private int perSiteWanDevice;
	private int perSiteLanDevice;
	private int lanDevicesUsers;
	private int lanDevicesPhysicalServers;
	private int perSiteWlanControllerevice;
	private int wlanAccessDevicesUsers;

}
