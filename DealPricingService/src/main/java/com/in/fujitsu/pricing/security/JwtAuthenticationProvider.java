package com.in.fujitsu.pricing.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.security.exception.JwtTokenMalformedException;
import com.in.fujitsu.pricing.security.model.AuthenticatedUser;
import com.in.fujitsu.pricing.security.model.JwtAuthenticationToken;
import com.in.fujitsu.pricing.security.util.JwtTokenValidator;

/**
 * Used for checking the token from the request and supply the UserDetails if
 * the token is valid
 *
 * @author Subhash
 */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private JwtTokenValidator jwtTokenValidator;

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String token = jwtAuthenticationToken.getToken();

		UserInfo parsedUser = jwtTokenValidator.parseToken(token);

		if (parsedUser == null) {
			throw new JwtTokenMalformedException("JWT token is not valid");
		}

		List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());

		return new AuthenticatedUser(parsedUser.getUserId(), parsedUser.getUserName(), token, authorityList);
	}

}
