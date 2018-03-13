/**
 *
 */
package com.in.fujitsu.pricing.network.dto;

import java.io.Serializable;
import java.util.List;

import com.in.fujitsu.pricing.dto.DealInfoDto;
import com.in.fujitsu.pricing.dto.ServiceWindowDto;
import com.in.fujitsu.pricing.dto.VolumeDto;

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
public class NetworkDropdownDto implements Serializable {

	private static final long serialVersionUID = -3795333425344424793L;

	private List<ServiceWindowDto> standardWindowInfoList;
	private List<String> yesNoOptionList;
	private List<NetworkWanSolutionInfoDto> networkWanSolutionsDtoList;
	private List<NetworkLanSolutionInfoDto> networkLanSolutionsDtoList;
	private List<NetworkWanFactorInfoDto> networkWanFactorDtoList;
	private List<NetworkLanFactorInfoDto> networkLanFactorDtoList;
	private List<NetworkWlanControllerFactorInfoDto> networkWlanControllerFactorDtoList;
	private List<NetworkWlanAccessPointFactorInfoDto> networkWlanAccessPointFactorDtoList;
	private List<VolumeDto> physicalServerList;
	private DealInfoDto dealInfoDto;

}
