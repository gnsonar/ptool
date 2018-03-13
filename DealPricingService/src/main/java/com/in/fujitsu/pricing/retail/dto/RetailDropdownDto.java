/**
 *
 */
package com.in.fujitsu.pricing.retail.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;

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
public class RetailDropdownDto implements Serializable {

	private static final long serialVersionUID = 8726144133823250255L;

	private List<String> yesNoOptionList;
	private List<RetailEquipmentAgeInfoDto> equipmentAgeList;
	private List<RetailEquipmentSetInfoDto> equipmentSetList;
	private DealInfoDto dealInfoDto;

	private List<RetailSolutionsDto> retailSolutionsDtoList;

}
