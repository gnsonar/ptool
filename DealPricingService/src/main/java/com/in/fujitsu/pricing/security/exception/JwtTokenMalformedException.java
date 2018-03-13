package com.in.fujitsu.pricing.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when token cannot be parsed
 * @author Subhash
 */
public class JwtTokenMalformedException extends AuthenticationException {


    /**
	 *
	 */
	private static final long serialVersionUID = 1826427366166371150L;

	public JwtTokenMalformedException(String msg) {
        super(msg);
    }
}
