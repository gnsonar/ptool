/**
 *
 */
package com.in.fujitsu.pricing.application.dto;

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
public class ApplicationDropdownDto implements Serializable {

	private static final long serialVersionUID = -8497749960959838628L;

	private List<ServiceWindowDto> standardWindowInfoList;
	private List<String> offshoreAllowedOptionList;
	private List<ApplicationSolutionsInfoDto> applicationSolutionsInfoDtoList;
	private DealInfoDto dealInfoDto;

}
