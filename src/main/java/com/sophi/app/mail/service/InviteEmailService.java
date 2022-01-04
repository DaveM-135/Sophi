package com.sophi.app.mail.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class InviteEmailService {

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private Configuration config;
	
	public MailResponse sendEmail(MailRequest request, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
			
			Template t = config.getTemplate("email-template-invitacion-webinar-link.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			
			helper.setTo(request.getTo());
			helper.setText(html,true);
			helper.setSubject(request.getSubject());
			helper.setFrom("rogelio.botello@sophitech.mx","Rogelio de Sophitech");
			sender.send(message);
			
			response.setMessage("Mail enviado a: " + request.getTo());
			response.setStatus(Boolean.TRUE);
		} catch (Exception e) {
			response.setMessage("Fallo el envio a: " + request.getTo() + " " + e.getMessage());
			response.setStatus(Boolean.FALSE);
			
		}
		
		return response;
		
	}
	
	public MailResponse sendEmailFarmacias(MailRequest request, Map<String, Object> model) {
		MailResponse response = new MailResponse();
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
			
			Template t = config.getTemplate("email-template-farmacias.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
			
			helper.setTo(request.getTo());
			helper.setText(html,true);
			helper.setSubject(request.getSubject());
			helper.setFrom("rogelio.botello@sophitech.mx","Rogelio de Sophitech");
			sender.send(message);
			
			response.setMessage("Mail enviado a: " + request.getTo());
			response.setStatus(Boolean.TRUE);
		} catch (Exception e) {
			response.setMessage("Fallo el envio a: " + request.getTo() + " " + e.getMessage());
			response.setStatus(Boolean.FALSE);
			
		}
		
		return response;
		
	}
	
}
