package com.in.fujitsu.pricing.utility;


import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class UserAttributeMapper implements AttributesMapper<FJEmployee>{

	public FJEmployee mapFromAttributes(Attributes attrs) throws NamingException {

		FJEmployee emp = new FJEmployee();
		emp.setEmpId(getValue("emp-id",attrs));
        emp.setFullName(getValue("cn",attrs));
        emp.setFirstName(getValue("givenName",attrs));
        emp.setLastName(getValue("sn",attrs));
        emp.setMail(getValue("mail",attrs));
        emp.setAddress(getValue("postOfficeBox",attrs));
        emp.setLocationDescr(getValue("locationdescription",attrs));
        emp.setLocationCode(getValue("l",attrs));
        emp.setOrg(getValue("o",attrs));
        emp.setOrgDescr(getValue("orgdescription",attrs));
        emp.setCostCenter(getValue("costcenter",attrs));
        emp.setCostCenterDescr(getValue("costcenterdescription",attrs));
        emp.setCountry(getValue("co",attrs));
        emp.setEmployeeNumber(getValue("employeeNumber",attrs));
        emp.setEmployeeType(getValue("employeetype",attrs));
        emp.setWorkingForCompany(getValue("workingforcompany",attrs));
        emp.setExtNumber(getValue("telephonenumber",attrs));
        emp.setHrLocation(getValue("hrlocation",attrs));
        emp.setJobFunctionDescr(getValue("jobfunctiondescription",attrs));
        emp.setMobilePhone(getValue("mobile",attrs));
        emp.setOrgUnit(getValue("ou",attrs));
        emp.setRegion(getValue("st-region",attrs));
        emp.setRegionDescr(getValue("regiondescription",attrs));
        emp.setUid(getValue("uid",attrs));

        return emp;

	}

	private String getValue(String attrID,Attributes attrs){

		Attribute value = attrs.get(attrID);
		String attrValue = null;

		if( value != null)
			try {
				attrValue = (String)value.get();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}

		return attrValue;
	}
}
