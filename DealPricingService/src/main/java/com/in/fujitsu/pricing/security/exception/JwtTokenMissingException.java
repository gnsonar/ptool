package com.in.fujitsu.pricing.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when token cannot be found in the request header
 * @author Subhash
 */

public class JwtTokenMissingException extends AuthenticationException {


    /**
	 *
	 */
	private static final long serialVersionUID = -1684760059885228471L;

	public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
