/**
 *
 */
package com.in.fujitsu.pricing.hosting.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;

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
public class HostingDropdownDto implements Serializable {

	private static final long serialVersionUID = -3795333425344424793L;

	private List<ServiceWindowDto> standardWindowInfoList;
	private List<String> yesNoOptionList;
	private List<HostingCoLocationInfoDto> coLocationDtoList;
	private List<HostingSolutionInfoDto> hostingSolutionsDtoList;
	private DealInfoDto dealInfoDto;

}
