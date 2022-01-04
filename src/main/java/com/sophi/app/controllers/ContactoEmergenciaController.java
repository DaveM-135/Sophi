package com.sophi.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IRecursoService;

@Controller
@SessionAttributes("contactoEmergencia")
public class ContactoEmergenciaController {
	
	@Autowired
	private IRecursoService recursoService;
	
//	@Autowired
//	private IContactoEmergenciaService contactoEmergenciaService;
	
	@RequestMapping(value = "/listarContactosEmergencia/{id}")
	public String listarContactosEmergencia(@PathVariable(value = "id") Long codRecurso, Map<String, Object> model, RedirectAttributes flash) {
		Recurso recurso = null;
		if (codRecurso > 0) {
			recurso = recursoService.findOne(codRecurso);
			if(recurso == null) {
				flash.addFlashAttribute("error", "El codigo del contacto de emergencia no existe en base de datos!");
				return "redirect:/listarRecursos";
			}
		} else {
			flash.addFlashAttribute("error", "El codigo del contacto de emergencia no debe ser cero!");
			return "redirect:/listarRecursos";
		}
		model.put("recurso", recurso);
		model.put("titulo", "Contactos de emergencia de " + recurso.getDescRecurso());
		return "listaContactosEmergencia";
	}
	
//	@RequestMapping(value = "/formContactoEmergencia/{id}")
//	public String editarContactoEmergencia(@PathVariable(value = "id") Long codContactoEmergencia, Map<String, Object> model, RedirectAttributes flash) {
//		ContactoEmergencia contactoEmergencia= null;
//		if (codContactoEmergencia > 0) {
//			contactoEmergencia = contactoEmergenciaService.findOne(codContactoEmergencia);
//			if(contactoEmergencia == null) {
//				flash.addFlashAttribute("error", "El codigo del contacto de emergencia no existe en base de datos!");
//				return "redirect:/listarRecursos";
//			}
//		} else {
//			flash.addFlashAttribute("error", "El codigo del dependiente no debe ser cero!");
//			return "redirect:/listarRecursos";
//		}
//		model.put("contactoEmergencia", contactoEmergencia);
//		model.put("titulo", "Formulario de contacto de emergencia");
//		
//		return "formContactoEmergencia";
//	}
	
//	@RequestMapping(value = "/asignarContactoEmergencia/{id}")
//	public String crearContactoEmergencia(@PathVariable(value = "id") Long codRecurso, Map<String, Object> model, RedirectAttributes flash) {
//		Recurso recurso = null;
//		if (codRecurso > 0) {
//			recurso = recursoService.findOne(codRecurso);
//			if(recurso == null) {
//				flash.addFlashAttribute("error", "El codigo del recurso no existe en base de datos!");
//				return "redirect:/listarRecursos";
//			}
//		} else {
//			flash.addFlashAttribute("error", "El codigo del recurso no debe ser cero!");
//			return "redirect:/listarRecursos";
//		}
//		ContactoEmergencia contactoEmergencia= new ContactoEmergencia();
//		contactoEmergencia.setRecurso(recurso);
//		model.put("contactoEmergencia", contactoEmergencia);
//		model.put("titulo", "Formulario de contacto de emergencia");
//		
//		return "formContactoEmergencia";
//	}
	
	
//	@RequestMapping(value="/formContactoEmergencia", method = RequestMethod.POST)
//	public String guardarContactoEmergencia(@Valid ContactoEmergencia contactoEmergencia, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
//		if(result.hasErrors()) {
//			model.addAttribute("titulo", "Formulario de contacto de emergencia");
//			return "formContactoEmergencia";
//		}
//		contactoEmergenciaService.save(contactoEmergencia);
//		status.setComplete();
//		flash.addFlashAttribute("success", "Contacto de emergencia guardado con éxito");
//		return "redirect:listarContactosEmergencia/" + contactoEmergencia.getRecurso().getCodRecurso().toString();
//	}
	
//	@RequestMapping(value = "/borrarContactoEmergencia/{id}")
//	public String borrarContactoEmergencia(@PathVariable(value = "id") Long codContactoEmergencia, Map<String, Object> model, RedirectAttributes flash) {
//		ContactoEmergencia contactoEmergencia= null;
//		if (codContactoEmergencia > 0) {
//			contactoEmergencia = contactoEmergenciaService.findOne(codContactoEmergencia);
//			if(contactoEmergencia == null) {
//				flash.addFlashAttribute("error", "El codigo del contacto de emergencia no existe en base de datos!");
//				return "redirect:/listarRecursos";
//			}
//		} else {
//			flash.addFlashAttribute("error", "El codigo del contacto de emergencia no debe ser cero!");
//			return "redirect:/listarRecursos";
//		}
//		
//		contactoEmergenciaService.delete(contactoEmergencia);
//		flash.addFlashAttribute("success", "Contacto de emergencia eliminado con éxito");
//		return "redirect:../listarContactosEmergencia/" + contactoEmergencia.getRecurso().getCodRecurso().toString();
//	}
	

}
