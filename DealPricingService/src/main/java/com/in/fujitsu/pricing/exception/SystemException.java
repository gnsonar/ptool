package com.in.fujitsu.pricing.exception;

public class SystemException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMsg;
	
	

	public String getErrorMsg() {
		return errorMsg;
	}



	public SystemException(String errorMsg) {
		super(errorMsg);
		this.errorMsg=errorMsg;
	}
	
	public SystemException(){
		super();

	}
}
