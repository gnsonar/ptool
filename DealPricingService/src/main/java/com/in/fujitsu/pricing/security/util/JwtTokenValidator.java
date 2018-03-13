package com.in.fujitsu.pricing.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.service.LoginService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * Class validates a given token by using the secret configured in the
 * application
 *
 * @author Subhash
 */
@Slf4j
@Component
public class JwtTokenValidator {

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	LoginService loginService;

	/**
	 * Tries to parse specified String as a JWT token. If successful, returns
	 * User object with username, id, role and token prefilled (extracted from
	 * token). If unsuccessful (token is invalid or not containing all required
	 * user properties), simply returns null.
	 *
	 * @param token
	 *            the JWT token to parse
	 * @return the User object extracted from specified token or null if a token
	 *         is invalid.
	 */
	public UserInfo parseToken(String token) {
		UserInfo userInfo = null;

		try {
			Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

			String randomToken = (String) body.get("token");
			String userName = body.getSubject();
			UserInfo pricingUserByToken = loginService.getPricingUserByToken(randomToken);
			if (pricingUserByToken != null && !userName.isEmpty()
					&& userName.equalsIgnoreCase(pricingUserByToken.getUserName())
					&& "Approved".equalsIgnoreCase(pricingUserByToken.getStatus())) {
				userInfo = new UserInfo();
				userInfo.setUserName(userName);
				userInfo.setUserId(Long.parseLong((String) body.get("userId")));
				userInfo.setRole((String) body.get("role"));
				userInfo.setToken(randomToken);
			}

		} catch (JwtException e) {
			// Simply print the exception and null will be returned for the userInfo
			log.error(e.getMessage());
		}
		return userInfo;
	}
}
