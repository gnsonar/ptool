/**
 *
 */
package com.in.fujitsu.pricing.enduser.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * @author pawarbh
 *
 */
@Getter
@Setter
@ToString
public class EndUserDropdownDto implements Serializable {

	private static final long serialVersionUID = -3795333425344424793L;

	private List<String> yesNoOptionList;
	private List<EndUserSolutionInfoDto> endUserSolutionsDtoList;
	private List<ImacFactorInfoDto> imacFactorDtoList;
	private List<EndUserContactRatioInfoDto> contactRatioDtoList;
	private List<EndUserResolutionTimeDto> resolutionTimeDtoList;
	private DealInfoDto dealInfoDto;

}
