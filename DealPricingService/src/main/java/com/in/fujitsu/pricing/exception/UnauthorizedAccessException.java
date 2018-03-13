package com.in.fujitsu.pricing.exception;

/**
 * @author mishrasub
 *
 */
public class UnauthorizedAccessException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -1711093574495699785L;

	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public UnauthorizedAccessException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public UnauthorizedAccessException() {
		super();
	}
}