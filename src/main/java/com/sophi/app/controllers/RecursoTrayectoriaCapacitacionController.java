package com.sophi.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sophi.app.models.entity.RecursoCapacitacion;
import com.sophi.app.models.service.ICapacitacionesService;
import com.sophi.app.models.service.IRecursoCapacitacionService;

@Controller
public class RecursoTrayectoriaCapacitacionController {
	
	@Autowired
	private IRecursoCapacitacionService recursoCapacitacionService;
	
	@Autowired
	private ICapacitacionesService capacitacionesService;
	
	@RequestMapping(value = "/verTrayectoriaCapacitacion")
	public String verTrayectoriaCapacitacion(@RequestParam Long codRecurso, Map<String, Object> model) {
		model.put("listaTrayectoriaCapacitaciones", recursoCapacitacionService.findByCodRecurso(codRecurso));
		return "layout/trayectoria:: fragmentTrayectoriaCapacitacion";
	}
	
	@RequestMapping(value="/formTrayectoriaCapacitacion", method = RequestMethod.POST)
	@ResponseBody
	public String guardarTrayectoriaCapacitacion(@ModelAttribute RecursoCapacitacion recursoCapacitacion, @RequestParam("fotoEvidencia") MultipartFile fotoEvidencia, Model model) {
		
		if(!fotoEvidencia.isEmpty()) {
			try {
				byte[] bytesFotoEvidencia = fotoEvidencia.getBytes();
				recursoCapacitacion.setDescDocumentoEvidencia(bytesFotoEvidencia);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		recursoCapacitacionService.save(recursoCapacitacion);
		return "OK";
	}
	
	@RequestMapping(value = "/formTrayectoriaCapacitacion")
	public String crearTrayectoriaCapacitacion(@RequestParam Long codRecurso, Map<String, Object> model) {
		
		RecursoCapacitacion recursoCapacitacion = new RecursoCapacitacion();
		recursoCapacitacion.setCodRecurso(codRecurso);
		model.put("recursoCapacitacion", recursoCapacitacion);
		model.put("listCapacitaciones", capacitacionesService.findAll());
		return "layout/trayectoria:: fragmentModalCapacitacion";
	}
	
	@RequestMapping(value = "/editTrayectoriaCapacitacion")
	public String editarTrayectoriaCapacitacion(@RequestParam Long rtc, Map<String, Object> model) {
		RecursoCapacitacion recursoCapacitacion = recursoCapacitacionService.findById(rtc);
		model.put("recursoCapacitacion", recursoCapacitacion);
		model.put("listCapacitaciones", capacitacionesService.findAll());
		return "layout/trayectoria:: fragmentModalCapacitacion";
	}
	
	
	@RequestMapping(value = "/borrarTrayectoriaCapacitacion")
	@ResponseBody
	public String borrarTrayectoriaCapacitacion(@RequestParam Long rtc, Map<String, Object> model) {
		recursoCapacitacionService.delete(rtc);
		return "ok";
	}
	
	@GetMapping(value = "/capacitacion/evidencia")
	public void verFotoEvidenciaCapacitacion(@RequestParam Long rtc,  HttpServletResponse response) throws IOException{
		response.setContentType("image/jpeg");
		RecursoCapacitacion recursoCapacitacion = recursoCapacitacionService.findById(rtc);
		InputStream is = new ByteArrayInputStream(recursoCapacitacion.getDescDocumentoEvidencia());
		IOUtils.copy(is, response.getOutputStream());
	}
	
}
