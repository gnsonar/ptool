package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.in.fujitsu.pricing.application.dto.ApplicationRevenueInfoDto;
import com.in.fujitsu.pricing.enduser.dto.EndUserRevenueInfoDto;
import com.in.fujitsu.pricing.hosting.dto.HostingRevenueInfoDto;
import com.in.fujitsu.pricing.network.dto.NetworkRevenueInfoDto;
import com.in.fujitsu.pricing.retail.dto.RetailRevenueInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioCriteriaInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioInfoDto;
import com.in.fujitsu.pricing.scenario.dto.ScenarioVolumeInfoDto;
import com.in.fujitsu.pricing.servicedesk.dto.ServiceDeskRevenueInfoDto;
import com.in.fujitsu.pricing.storage.dto.StorageRevenueInfoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class TotalsDto implements Serializable {

	private static final long serialVersionUID = -5967687472385941747L;

	private List<StorageRevenueInfoDto> storageYearlyRevenue = new ArrayList<StorageRevenueInfoDto>();
	private List<ApplicationRevenueInfoDto> appYearlyRevenue = new ArrayList<ApplicationRevenueInfoDto>();;
	private List<ServiceDeskRevenueInfoDto> serviceDeskYearlyRevenue = new ArrayList<ServiceDeskRevenueInfoDto>();;
	private List<NetworkRevenueInfoDto> networkYearlyRevenue = new ArrayList<NetworkRevenueInfoDto>();;
	private List<EndUserRevenueInfoDto> endUserYearlyRevenue = new ArrayList<EndUserRevenueInfoDto>();
	private List<RetailRevenueInfoDto> retailYearlyRevenue = new ArrayList<RetailRevenueInfoDto>();
	private List<HostingRevenueInfoDto> hostingYearlyRevenue = new ArrayList<HostingRevenueInfoDto>();
	private DealInfoDto dealInfoDto;

	private List<MigrationCostDto> totalMigrationCost = new ArrayList<>();
	private List<MigrationCostDto> endUserMigrationCost = new ArrayList<>();
	private List<MigrationCostDto> hostingMigrationCost = new ArrayList<>();
	private List<ScenarioInfoDto> scenarioList = new ArrayList<>();
	private ScenarioCriteriaInfoDto scenarioCriteriaInfoDto;
	private ScenarioVolumeInfoDto scenarioVolumeInfoDto;


}
