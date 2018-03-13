package com.in.fujitsu.pricing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maninder
 *
 */
@Slf4j
@Component
public class EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Value("${email.sender}")
	private String sender;

	@Value("${email.subject}")
	private String subject;

	public void sendSimpleMessage(String to, String cc, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			// TODO Require an admin DL to send an email for approval request
			message.setTo(to);
			//TODO uncomment it before deployment
			// message.setCc(cc);
			message.setFrom(sender);
			message.setSubject(subject);
			message.setText(body);

			emailSender.send(message);
		} catch (MailException exception) {
			log.error(exception.getMessage());
		}
	}

}
