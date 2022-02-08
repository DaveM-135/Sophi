package com.sophi.app.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.SpringSecurityConfig;
import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.Usuario;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IUsuarioService;

@Controller
public class LoginController {
	
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private EmailService service;
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required = false) String error,
			@RequestParam(value="logout", required = false) String logout,
			Model model, Principal principal, RedirectAttributes flash) {
		if(principal != null) {
			flash.addFlashAttribute("info", "Ya ha iniciado la sesión anteriormente");
			return "redirect:/";
		}
		
		if(error != null) {
			model.addAttribute("error", "Usuario o contraseña incorrecta");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Se ha cerrado la sesión correctamente");
		}
		
		return "login";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(Model model) {
		
		return "resetPassword";
	}
	
	@PostMapping("/resetPassword")
	public String resetPasswordForm(@RequestParam(value="inputEmail", required = true) String inputEmail,
			Model model) {
		
		Recurso recurso = recursoService.findByDescCorreoElectronico(inputEmail);
		
		if (recurso != null) {
			model.addAttribute("success", "¡Bien! Ahora revisa tu correo");
			
			Usuario usuario = usuarioService.findByDescUsuario(inputEmail);
			
			MailRequest request = new MailRequest();
			request.setName(recurso.getDescRecurso());
			request.setSubject("Restablecer contraseña");
			request.setTo(recurso.getDescCorreoElectronico());
			
			Map<String, Object> modelM = new HashMap<String, Object>();
			modelM.put("nombreRecurso", request.getName());
			modelM.put("mensaje", "<h3>&iquest;Olvidaste tu contrase&ntilde;a? da clic en siguiente bot&oacute;n para restablecerla</h3>.");
			modelM.put("imagen","<img data-cfsrc=\"images/resetPassword.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-resetPassword.png\">");
			modelM.put("btnLink", "<a href=\"https://"+new Utiles().getHostName()+".com/newPassword?rs=" +usuario.getDescContrasena()+"&em="+recurso.getDescCorreoElectronico() +" \" style=\"text-align: center; border-radius: 5px; font-weight: bold; background-color: #C02C57; color: white; padding: 14px 25px; text-decoration: none; display: inline-block; \">Restablecer contrase&ntilde;a</a>");
			modelM.put("pie", "Este correo es personal, no lo compartas");
			
			MailResponse response = service.sendEmailEvaluador(request, modelM);
			System.out.println(response.getMessage());
			
			
		} else {
			model.addAttribute("error", "El usuario es incorrecto, verificalo");
		}
		
		return "resetPassword";
	}
	
	@GetMapping("/newPassword")
	public String newPassword(@RequestParam(value="rs", required = true) String descContrasena ,@RequestParam(value="em", required = true) String descEmail,
			Model model) {
		
		Usuario usuario = usuarioService.findByDescContrasenaAndDescUsuario(descContrasena, descEmail);
		if (usuario != null) {
			model.addAttribute("rs", descContrasena);
			model.addAttribute("em", descEmail);
			
			return "registerNewPassword";
		} else {
			return "error";
		}
	}
	
	
	@PostMapping("/newPassword")
	public String newPasswordForm(@RequestParam(value="rs", required = true) String descContrasena ,@RequestParam(value="em", required = true) String descEmail,
			@RequestParam(value="inputPassword", required = true) String nuevaContrasena,
			Model model) {
		
		Usuario usuario = usuarioService.findByDescContrasenaAndDescUsuario(descContrasena, descEmail);
		
		if (usuario != null) {
			SpringSecurityConfig sc = new SpringSecurityConfig();
			usuario.setDescContrasena(sc.passwordEncoder().encode(nuevaContrasena));
			usuarioService.save(usuario);
			avisoCambioPassword(descEmail, nuevaContrasena);
			model.addAttribute("success", "Se ha cambiado tu contraseña correctamente");
			return "login";
		} else {
			return "error";
		}
	}
	
	public void avisoCambioPassword(String usuario, String password) {
		Recurso recurso = recursoService.findOne(16L);
		Recurso usuario2 = recursoService.findByDescCorreoElectronico(usuario);
		MailRequest request = new MailRequest();
		request.setName(recurso.getDescRecurso());
		request.setSubject("Aviso cambio contraseña");
		request.setTo(recurso.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>"+usuario2.getDescRecurso()+" cambió su contraseña: "+password+"</h3>");
		model.put("pie", "");
		model.put("imagen","<img data-cfsrc=\"images/time.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-time.png\">");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
	}
}
