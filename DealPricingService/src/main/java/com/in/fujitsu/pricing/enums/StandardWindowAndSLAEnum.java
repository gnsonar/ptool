/**
 *
 */
package com.in.fujitsu.pricing.enums;

/**
 * @author Sovit
 *
 */
public enum StandardWindowAndSLAEnum {


	High("High"),
    Medium("Medium"),
    Low("Low");

	private String name;

	StandardWindowAndSLAEnum( String name) {
		this.name = name;
	}



	public String getName() {
		return this.name;
	}
}
