package com.in.fujitsu.pricing.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DealPhaseEnum {
	WON("Won"),
	LOSS("Loss"),
	DISCARDED("Discarded"),
	LOSS_ON_PRICE("Loss on price");

	private static final Map<Integer, String> mapData = new LinkedHashMap<Integer, String>();
	static {
		for (DealPhaseEnum enm : values()) {
			mapData.put(enm.getValue(), enm.getName());
		}
	}

	private int value;
	private String name;

	DealPhaseEnum(String name) {
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
