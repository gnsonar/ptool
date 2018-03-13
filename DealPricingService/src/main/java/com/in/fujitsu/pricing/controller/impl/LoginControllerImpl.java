package com.in.fujitsu.pricing.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.in.fujitsu.pricing.controller.LoginController;
import com.in.fujitsu.pricing.dto.UserCredential;
import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.exception.UserNotFoundException;
import com.in.fujitsu.pricing.service.LdapService;
import com.in.fujitsu.pricing.service.LoginService;

/**
 * @author mishrasub
 *
 */
@RestController
@CrossOrigin(allowCredentials = "true", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
public class LoginControllerImpl implements LoginController {

	@Autowired
	private LdapService ldapService;

	@Autowired
	private LoginService loginService;

	@Override
	@GetMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfoDto login(@RequestHeader(value="Authorization", required=true) String authString) throws UserNotFoundException {
		UserInfoDto userInfoDto = null;
		try {
			if (authString != null && !authString.isEmpty()) {
				UserCredential userCredential = loginService.extractUserCredentials(authString);
				if (userCredential != null && !userCredential.getUserName().isEmpty()
						&& !userCredential.getPassword().isEmpty()) {
					String userName = userCredential.getUserName();
					String password = userCredential.getPassword();
					boolean authenticated = ldapService.authenticate(userName, password);
					if (authenticated) {
						userInfoDto = loginService.login(userName);
						if (userInfoDto == null) {
							throw new UserNotFoundException("Unregistered user.");
						}
					} else {
						throw new UserNotFoundException("LDAP Authentication Failed");
					}
				} else {
					throw new UserNotFoundException("Missing required parameter.");
				}
			}
		} catch (Exception exception) {
			throw new UserNotFoundException(exception.getMessage());
		}

		return userInfoDto;
	}

	@Override
	@GetMapping(path = "resources/logout/{userId}", produces = MediaType.TEXT_HTML_VALUE)
	public String logout(@PathVariable(value = "userId") Long userId) throws UserNotFoundException {
		boolean logout = loginService.logout(userId);
		if (logout) {
			return "Logout done!";
		}
		throw new UserNotFoundException("Logout Failed.");
	}

	@Override
	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfoDto registerUser(@RequestBody UserInfoDto userInfoDto) throws UserNotFoundException {
		UserInfoDto registeredUser = null;
		try {
			if (userInfoDto != null && !userInfoDto.getEmailId().isEmpty()) {
				registeredUser = loginService.registerUser(userInfoDto);
			} else {
				throw new UserNotFoundException("Missing Required inputs.");
			}

		} catch (Exception exception) {
			throw new UserNotFoundException(exception.getMessage());
		}

		return registeredUser;
	}

}
