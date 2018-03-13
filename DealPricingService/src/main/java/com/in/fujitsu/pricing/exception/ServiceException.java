package com.in.fujitsu.pricing.exception;

public class ServiceException extends Exception{
	private static final long serialVersionUID = -4780257792570017394L;
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ServiceException(String errorMsg) {
		super(errorMsg);
		this.errorMsg=errorMsg;
	}

	public ServiceException(){
		super();

	}
}
