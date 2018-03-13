package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ChhabrMa
 *
 */
@Getter
@Setter
@ToString
public class ServiceDeskContactRatioInfoDto implements Serializable {

	private static final long serialVersionUID = -1876219799256495639L;
	private Integer Id;
	private String contactName;
	private Float contactRatio;

}
