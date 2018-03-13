package com.in.fujitsu.pricing.exception;

/**
 * @author mishrasub
 *
 */
public class UserNotFoundException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1514396855649333202L;

	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public UserNotFoundException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public UserNotFoundException() {
		super();
	}
}