package com.in.fujitsu.pricing.security.util;

import com.in.fujitsu.pricing.entity.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * convenience class to generate a token for login.
 * Make sure the used secret here matches the on in your application.properties
 *
 * @author Subhash
 */
public class JwtTokenGenerator {

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param user for which the token will be generated
     * @return the JWT token
     */
    public static String generateToken(UserInfo userInfo, String secret) {
    	Claims claims = Jwts.claims().setSubject(userInfo.getUserName());
		claims.put("userId", userInfo.getUserId() + "");
		claims.put("role", userInfo.getRole());
		claims.put("token", userInfo.getToken());

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }


}
