
package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class ServiceDeskYearlyDataInfoDto implements Serializable {

	private static final long serialVersionUID = -2183711413234866870L;

	private Long yearId;

	private Integer totalContacts;

	private Integer voiceContacts;

	private Integer mailContacts;

	private Integer chatContacts;

	private Integer portalContacts;

	private Integer year;

	private List<ServiceDeskUnitPriceInfoDto> serviceDeskUnitPriceInfoDtoList;

	private List<ServiceDeskRevenueInfoDto> serviceDeskRevenueInfoDtoList;

}
