/**
 *
 */
package com.in.fujitsu.pricing.servicedesk.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mishrasub
 *
 */
@Getter
@Setter
@ToString
public class ServiceDeskDropdownDto implements Serializable {

	private static final long serialVersionUID = -3795333425344424793L;

	private List<ServiceWindowDto> standardWindowInfoList;
	private List<String> yesNoOptionList;
	private List<ServiceDeskContactRatioInfoDto> serviceDeskContactRatioDtoList;
	private List<ServiceDeskSolutionsDto> serviceDeskSolutionsDtoList;
	private DealInfoDto dealInfoDto;

}
