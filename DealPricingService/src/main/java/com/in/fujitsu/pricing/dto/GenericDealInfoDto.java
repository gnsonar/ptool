/**
 *
 */
package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sovit
 *
 */
@Getter
@Setter
@ToString
public class GenericDealInfoDto implements Serializable {

	private static final long serialVersionUID = -5386575052765543199L;
	private List<ClientIndustryDto> clientIndustryDtoList;
	private List<CountryCurrencyInfoDto> countryCurrencyInfoDtoList;
	List<DealTypeDto> dealtypeList;
	List<DealPhaseDto> dealPhaseList;
	List<ServiceWindowDto> standardWindowInfoList;
	List<String> offshoreAndHardwareIncludedList;
	List<DealCostParametersDto> dealCostParametersDtoList;

}
