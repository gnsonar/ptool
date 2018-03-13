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
public class ServiceDeskRevenueDto implements Serializable {

	private static final long serialVersionUID = 2366179450862049528L;

	private ServiceDeskCalculateDto totalContactsCalculateDto;

	private ServiceDeskCalculateDto voiceContactsCalculateDto;

	private ServiceDeskCalculateDto mailContactsCalculateDto;

	private ServiceDeskCalculateDto chatContactsCalculateDto;

	private ServiceDeskCalculateDto portalContactsCalculateDto;

}
