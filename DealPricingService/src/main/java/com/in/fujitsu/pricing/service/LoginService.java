package com.in.fujitsu.pricing.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import com.in.fujitsu.pricing.dto.UserCredential;
import com.in.fujitsu.pricing.dto.UserInfoDto;
import com.in.fujitsu.pricing.entity.UserInfo;
import com.in.fujitsu.pricing.exception.SystemException;
import com.in.fujitsu.pricing.repository.UserRepository;
import com.in.fujitsu.pricing.security.util.JwtTokenGenerator;
import com.in.fujitsu.pricing.utility.FJEmployee;
import com.in.fujitsu.pricing.utility.ModelConvertor;

/**
 * @author Maninder
 *
 */
@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LdapService ldapService;

	@Autowired
	private EmailService emailService;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${email.requestBody}")
	private String body;

	@Value("${email.to}")
	private String to;

	/**
	 * @param authString
	 * @return
	 */
	public UserCredential extractUserCredentials(String authString) {
		UserCredential userCredential = new UserCredential();
		if (!StringUtils.isEmpty(authString)) {
			String credentials = authString.substring(6, authString.length());
			String[] decodedCredentialParts = new String(Base64Utils.decode(credentials.getBytes())).split(":");
			userCredential.setUserName(decodedCredentialParts[0]);
			userCredential.setPassword(decodedCredentialParts[1]);
		}
		return userCredential;
	}

	/**
	 * Creates an access token.
	 *
	 * @param userName
	 * @return
	 */
	public UserInfoDto login(String userName) {
		final UserInfo userInfo = userRepository.findByUserName(userName);
		if (userInfo != null && "Approved".equalsIgnoreCase(userInfo.getStatus())) {
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.HOUR, 8);
			String token = UUID.randomUUID().toString().replace("-", "");
			Date tokenExpiry = cal.getTime();
			userInfo.setToken(token);
			userInfo.setTokenExpiry(tokenExpiry);
			userRepository.saveAndFlush(userInfo);
			userInfo.setToken(JwtTokenGenerator.generateToken(userInfo, secret));
			return ModelConvertor.prepareUserInfoDto(userInfo);
		}
		return null;
	}

	/***
	 * Gets the user associated with a token.
	 *
	 * @param token
	 * @return userInfo
	 */
	public UserInfo getPricingUserByToken(String token) {
		UserInfo userInfo = userRepository.findByToken(token);
		if (userInfo != null) {
			Date tokenExpiry = userInfo.getTokenExpiry();
			if (tokenExpiry.after(new Date())) {
				return userInfo;
			}
		}
		return null;
	}

	/**
	 * Remove access token and token expire.
	 *
	 * @param userName
	 * @return
	 */
	public boolean logout(Long userId) {
		final UserInfo userInfo = userRepository.findOne(userId);
		if (userInfo != null && !userInfo.getToken().isEmpty()) {
			userInfo.setToken("");
			userInfo.setTokenExpiry(null);
			userRepository.saveAndFlush(userInfo);
			return true;
		}
		return false;
	}

	public UserInfoDto registerUser(UserInfoDto userInfoDto) throws Exception {
		UserInfoDto registeredUserInfoDto = null;
		try {
			String emailId = userInfoDto.getEmailId();
			boolean userExists = userRepository.findByEmailId(emailId) != null ? true : false;
			if (userExists) {
				throw new SystemException("User already Registered");
			}

			FJEmployee fJEmployee = ldapService.findByEmail(emailId);
			// TODO : Setting GENDER value
			UserInfo userInfo = ModelConvertor.prepareUserInfo(fJEmployee, userInfoDto);
			userInfo = userRepository.save(userInfo);
			if (userInfo.getUserId() != null) {
				registeredUserInfoDto = ModelConvertor.prepareUserInfoDto(userInfo);
				emailService.sendSimpleMessage(to, registeredUserInfoDto.getEmailId(), body);
			}

		} catch (Exception exception) {
			throw new SystemException(exception.getMessage());
		}
		return registeredUserInfoDto;
	}

}
