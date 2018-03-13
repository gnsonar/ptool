package com.in.fujitsu.pricing.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum GenderEnum {
	MALE(1, "Male"), //
	FEMALE(2, "Female"), //
	CLIENT(3, "Client");

	private static final Map<Integer, String> mapData = new LinkedHashMap<Integer, String>();
	static {
		for (GenderEnum enm : values()) {
			mapData.put(enm.getValue(), enm.getName());
		}
	}

	private int value;
	private String name;

	GenderEnum(int value, String name) {
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
