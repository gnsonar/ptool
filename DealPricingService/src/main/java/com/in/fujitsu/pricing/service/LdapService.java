package com.in.fujitsu.pricing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import com.in.fujitsu.pricing.dto.FJEmployeeDto;
import com.in.fujitsu.pricing.utility.CommonModelConvertor;
import com.in.fujitsu.pricing.utility.FJEmployee;

/**
 * @author Subhash
 *
 */
@Service
public class LdapService {
	@Autowired
	private LdapTemplate ldapTemplate;

	public FJEmployee findByEmail(String email) {
		final LdapQuery query = LdapQueryBuilder.query().where("mail").is(email);
		return ldapTemplate.findOne(query, FJEmployee.class);
	}

	public boolean authenticate(String username, String password) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("uid", username));
		boolean authenticated = ldapTemplate.authenticate("", filter.toString(), password);
		return authenticated;
	}

	public List<FJEmployeeDto> findByName(String name) {
		final LdapQuery query = LdapQueryBuilder.query().where("cn").whitespaceWildcardsLike(name).or("sn").whitespaceWildcardsLike(name);
		List<FJEmployee> fJEmployeeList = ldapTemplate.find(query, FJEmployee.class);
		List<FJEmployeeDto> fJEmployeeDtoList = new ArrayList<>();
		for(FJEmployee employee : fJEmployeeList) {
			FJEmployeeDto fJEmployeeDto = CommonModelConvertor.prepareFJEmployeeDto(employee);
			fJEmployeeDtoList.add(fJEmployeeDto);
		}
		return fJEmployeeDtoList;
	}

}
