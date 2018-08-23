package com.abdelaziz.service;

import javax.mail.MessagingException;

import com.abdelaziz.dto.UserDto;
import com.abdelaziz.entity.User;

public interface MailService {
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws MessagingException;

	public void sendEmailFromTemplate(User user, String templateName, String titleKey) throws MessagingException;

	public void sendActivationEmail(User user) throws MessagingException;

	public void sendCreationEmail(User user) throws MessagingException;

	public void sendPasswordResetMail(User user) throws MessagingException;

}
