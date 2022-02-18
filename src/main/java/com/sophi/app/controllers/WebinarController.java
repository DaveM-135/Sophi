package com.sophi.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.RegistroWebinar;
import com.sophi.app.models.service.IRegistroWebinarService;

@Controller
public class WebinarController {
	
	@Autowired
	private IRegistroWebinarService registroWebinarService;
	
	@Autowired
	private EmailService service;
	
	@GetMapping(value="/registroWebinar")
	public String registroWebinar(Model model){
		RegistroWebinar registroWebinar = new RegistroWebinar();
		model.addAttribute("registroWebinar", registroWebinar);
		return "registerWebinar";
	}
	
	@PostMapping(value="/registroWebinar")
	public String guardarRegistroWebinar(@ModelAttribute RegistroWebinar registroWebinar, Model model2){
		registroWebinarService.guardar(registroWebinar);
		
		MailRequest request = new MailRequest();
		request.setName(registroWebinar.getDescNombre());
		request.setSubject("Confirmación de registro");
		request.setTo(registroWebinar.getDescEmail());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Gracias por registrarte a nuestro webinar.<br><br>Posteriormente recibir&aacute;s un correo electr&oacute;nico<br>con el link de acceso al evento.</h3>");
		model.put("pie", "Sophitech | MicroStrategy");
		model.put("imagen","<img data-cfsrc=\"images/webinar.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-webinar.png\">");
		
		MailResponse response = service.sendEmailConfirmacionWebinar(request, model);
		System.out.println(response.getMessage());
		
		
		model2.addAttribute("mensaje", "Gracias por registrarte");
		return "registerWebinar";
	}

}
