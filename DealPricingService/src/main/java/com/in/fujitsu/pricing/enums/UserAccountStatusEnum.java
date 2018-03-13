package com.in.fujitsu.pricing.enums;

/**
 * @author Maninder
 *
 */
public enum UserAccountStatusEnum {
	PENDING("Pending"),
	REJECTED("Rejected"),
	APPROVED("Approved"),
	INACTIVE("Inactive"),
	LOCKED("Locked");

	private String name;
	private UserAccountStatusEnum(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}

}
