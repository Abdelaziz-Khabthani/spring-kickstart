package com.abdelaziz.service.impl;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.entity.User;
import com.abdelaziz.service.MailService;
import com.abdelaziz.util.LocalsUtil;

import lombok.Setter;

@Service
@Loggable(layer = ApplicationLayer.SERVICE_LAYER)
@ConfigurationProperties(prefix = "custom.mail")
public class MailServiceImpl implements MailService {

	private static final String USER = "user";

	private static final String BASE_URL = "baseUrl";

	@Setter
	private String from;

	@Setter
	private String baseUrl;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private LocalsUtil localsUtil;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Override
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws MessagingException {
		// Prepare message using a Spring helper
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
		message.setTo(to);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(content, isHtml);
		javaMailSender.send(mimeMessage);
	}

	@Override
	public void sendEmailFromTemplate(User user, String templateName, String titleKey) throws MessagingException {
		Context context = new Context(localsUtil.getCurrentLocal());
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, baseUrl);
		String content = templateEngine.process(templateName, context);
		String subject = localsUtil.getSimpleLocalizedMessage(titleKey);
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Override
	@Async
	public void sendActivationEmail(User user) throws MessagingException {
		sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
	}

	@Override
	@Async
	public void sendCreationEmail(User user) throws MessagingException {
		sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
	}

	@Override
	@Async
	public void sendPasswordResetMail(User user) throws MessagingException {
		sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");

	}

}
