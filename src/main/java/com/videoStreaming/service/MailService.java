package com.videoStreaming.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.videoStreaming.model.User;

import org.thymeleaf.context.Context;

@Service
public class MailService {
	private static final String USER = "user";
	
	private static final String BASE_URL = "baseUrl";
	
	@Value("${baseUrl}")
	private String baseUrl;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SpringTemplateEngine springTemplateEngine;
	
	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = 
					new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
			messageHelper.setTo(to);
			messageHelper.setFrom("hanyelrebby0122@yahoo.com");
			messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);
            javaMailSender.send(mimeMessage);    
		} catch (MailException | MessagingException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Async
	public void sendEmailFromTemplate(User user, String templateName) {
		if (user.getEmail() == null) {
			return;
		}
		Context context = new Context();
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, baseUrl);
		
		String content = springTemplateEngine.process(templateName, context);
		
        sendEmail(user.getEmail().toLowerCase(),"Registration Confirmation", content, false, true);
	}
	
	public void sendActivationEmail(User user) {
        sendEmailFromTemplate(user, "activationEmail");
	}
	
	public void sendCreationEmail(User user) {
        sendEmailFromTemplate(user, "creationEmail");
	}
	
	public void sendPasswordResetMail(User user) {
		
	}
}
