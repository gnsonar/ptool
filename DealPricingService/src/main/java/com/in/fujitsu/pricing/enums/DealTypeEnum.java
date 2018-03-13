package com.in.fujitsu.pricing.enums;

public enum DealTypeEnum {

	BENCHMARK_DEAL("Benchmark deal"),
    PAST_DEAL("Past deal"),
    COMPETITOR_DEAL("Competitor deal");

//	PAST(1, "Past"), //
//	BENCHMARK(3, "Benchmark"),
//	COMPETITIVE(3, "Competitive");
//
//	private static final Map<Integer, String> mapData = new LinkedHashMap<Integer, String>();
//	static {
//		for (DealTypeEnum enm : values()) {
//			mapData.put(enm.getValue(), enm.getName());
//		}
//	}
//
	private String name;

	DealTypeEnum( String name) {
		this.name = name;
	}



	public String getName() {
		return this.name;
	}



}
