package com.in.fujitsu.pricing.utility;

import java.io.Serializable;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses = { "top" }, base = "ou=people")
public final class FJEmployee implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -123436689557229703L;

	@Id
	private Name distinguisedName;

	@Attribute(name = "emp-id")
	private String empId ;

	@Attribute(name = "uid")
	private String uid;

	@Attribute(name = "employeeNumber")
	String employeeNumber;

	@Attribute(name = "givenName")
	private String firstName;

	@Attribute(name = "sn")
	private String lastName;

	@Attribute(name = "cn")
	private String fullName;

	@Attribute(name = "mail")
	private String mail;

	@Attribute(name = "employeetype")
	private String employeeType;

	@Attribute(name = "o")
	private String Org;

	@Attribute(name = "ou")
	private String orgUnit;

	@Attribute(name = "orgdescription")
	private  String orgDescr;

	@Attribute(name = "l")
	private String locationCode;

	@Attribute(name = "locationdescription")
	private String locationDescr;

	@Attribute(name = "postOfficeBox")
	private String address;

	@Attribute(name = "st-region")
	private String region;

	@Attribute(name = "regiondescription")
	private String regionDescr;

	@Attribute(name = "mobile")
	private String mobilePhone;

	@Attribute(name = "telephonenumber")
	private String extNumber;

	@Attribute(name = "costcenter")
	private String costCenter;

	@Attribute(name = "costcenterdescription")
	private String costCenterDescr;

	@Attribute(name = "co")
	private String country;

	@Attribute(name = "hrlocation")
	private String hrLocation;

	@Attribute(name = "workingforcompany")
	private String workingForCompany;

	@Attribute(name = "jobfunctiondescription")
	private String jobFunctionDescr;

	public Name getDistinguisedName() {
		return distinguisedName;
	}

	public void setDistinguisedName(Name distinguisedName) {
		this.distinguisedName = distinguisedName;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getOrg() {
		return Org;
	}

	public void setOrg(String org) {
		Org = org;
	}

	public String getOrgUnit() {
		return orgUnit;
	}

	public void setOrgUnit(String orgUnit) {
		this.orgUnit = orgUnit;
	}

	public String getOrgDescr() {
		return orgDescr;
	}

	public void setOrgDescr(String orgDescr) {
		this.orgDescr = orgDescr;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationDescr() {
		return locationDescr;
	}

	public void setLocationDescr(String locationDescr) {
		this.locationDescr = locationDescr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegionDescr() {
		return regionDescr;
	}

	public void setRegionDescr(String regionDescr) {
		this.regionDescr = regionDescr;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getExtNumber() {
		return extNumber;
	}

	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getCostCenterDescr() {
		return costCenterDescr;
	}

	public void setCostCenterDescr(String costCenterDescr) {
		this.costCenterDescr = costCenterDescr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHrLocation() {
		return hrLocation;
	}

	public void setHrLocation(String hrLocation) {
		this.hrLocation = hrLocation;
	}

	public String getWorkingForCompany() {
		return workingForCompany;
	}

	public void setWorkingForCompany(String workingForCompany) {
		this.workingForCompany = workingForCompany;
	}

	public String getJobFunctionDescr() {
		return jobFunctionDescr;
	}

	public void setJobFunctionDescr(String jobFunctionDescr) {
		this.jobFunctionDescr = jobFunctionDescr;
	}

}
