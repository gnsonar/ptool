package com.in.fujitsu.pricing.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class DealResultsResponse implements Serializable {

	private static final long serialVersionUID = -5500845116160681026L;

	private int noOfUsedDeals;

	private DealResultDto expensiveDeal;

	private DealResultDto cheapestDeal;

	private List<DealResultDto> nearestInVolumeDeals;

	private DealResultDto selectedBenchMarkDeal;

    private DealResultDto highBenchMarkDeal;

    private DealResultDto lowBenchMarkDeal;

}
