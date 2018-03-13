package com.in.fujitsu.pricing.enums;

/**
 * @author Maninder
 *
 */
public enum RoleEnum {
	ADMIN("Admin"),
	REGULAR("Regular"),
	FINANCE("Finance");

	private String name;
	private RoleEnum(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}

}
