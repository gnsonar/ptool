package com.in.fujitsu.pricing.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
public class FJEmployeeDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7341851994754114946L;

	private String empId ;

	private String uid;

	String employeeNumber;

	private String firstName;

	private String lastName;

	private String fullName;

	private String mail;

	private String employeeType;

	private String Org;

	private String orgUnit;

	private  String orgDescr;

	private String locationCode;

	private String locationDescr;

	private String address;

	private String region;

	private String regionDescr;

	private String mobilePhone;

	private String extNumber;

	private String costCenter;

	private String costCenterDescr;

	private String country;

	private String hrLocation;

	private String workingForCompany;

	private String jobFunctionDescr;

}
