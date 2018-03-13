package com.in.fujitsu.pricing.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DealStatusEnum {
	OPEN("Open"),
	PUBLISHED("Published"),
	SUBMITTED("Submitted"),
    STORED("Stored");

	private static final Map<Integer, String> mapData = new LinkedHashMap<Integer, String>();
	static {
		for (DealStatusEnum enm : values()) {
			mapData.put(enm.getValue(), enm.getName());
		}
	}

	private int value;
	private String name;

	DealStatusEnum(String name) {
		this.name = name;
	}

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public static String getName(int value) {
		return mapData.get(value);
	}

	public static Map<Integer, String> getList() {
		return mapData;
	}

}
