package com.in.fujitsu.pricing.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DealServiceLevelEnum {
	HIGH(1, "99.90% - 99.99% ; 24x7"), //
	MEDIUM(3, "99.00% - 99.89% ; 5x12-7x24"),
	LOW(3, "<99.00% ; 5x8-7x12");

	private static final Map<Integer, String> mapData = new LinkedHashMap<Integer, String>();
	static {
		for (DealServiceLevelEnum enm : values()) {
			mapData.put(enm.getValue(), enm.getName());
		}
	}

	private int value;
	private String name;

	DealServiceLevelEnum(int value, String name) {
		this.value = value;
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
