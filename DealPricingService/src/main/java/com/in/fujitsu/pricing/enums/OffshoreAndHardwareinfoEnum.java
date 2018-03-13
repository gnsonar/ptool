/**
 *
 */
package com.in.fujitsu.pricing.enums;

/**
 * @author Sovit
 *
 */
public enum OffshoreAndHardwareinfoEnum {

	YES("Yes"),
	No("No");

	private String name;

	OffshoreAndHardwareinfoEnum( String name) {
		this.name = name;
	}



	public String getName() {
		return this.name;
	}

}
