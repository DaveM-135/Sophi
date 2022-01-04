package com.sophi.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.models.entity.Equipo;
import com.sophi.app.models.entity.Herramienta;
import com.sophi.app.models.service.IEquipoService;
import com.sophi.app.models.service.IHerramientaService;
import com.sophi.app.models.service.ITipoHerramientaService;

@Controller
public class HerramientaController {
	
	@Autowired
	private IHerramientaService herramientaService;
	
	@Autowired
	private IEquipoService equipoService;
	
	@Autowired
	private ITipoHerramientaService tipoHerramientaService;
	

	@RequestMapping(value="/formHerramienta", method = RequestMethod.POST)
	public String guardarHerramienta(@Valid Herramienta herramienta, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de herramientas");
			return "formHerramienta";
		}
		herramientaService.save(herramienta);
		status.setComplete();
		flash.addFlashAttribute("success", "Herramienta guardada con éxito");
		return "redirect:listarHerramientas/" + herramienta.getRecurso().getCodRecurso().toString();
	}
	
	@RequestMapping(value = "/formCrearEditarHerramienta")
	public String crearEditarRecursoHerramienta(@RequestParam Long codRecurso, Model model) {
		
		Herramienta recursoHerramienta = new Herramienta();
		recursoHerramienta.setCodRecurso(codRecurso);
		model.addAttribute("tipoHerramientaList", tipoHerramientaService.findAll());
		model.addAttribute("listaEquiposTodo", equipoService.findListEquiposDisponibles());
		model.addAttribute("recursoHerramienta",recursoHerramienta);
		
		return "layout/herramientas :: fragmentModalHerramientas";
	}
	
	@RequestMapping(value = "/formEditarHerramienta")
	public String editarRecursoHerramienta(@RequestParam Long chr, Model model) {
		
		Herramienta recursoHerramienta = herramientaService.findOne(chr);
		
		//retornamos el equipo actualmente asignado a estatus 1(Disponible)
		Equipo equipo = equipoService.findByCodHerramienta(recursoHerramienta.getCodHerramienta());
		equipo.setCodEstadoHerramienta(1L);
		
		herramientaService.save(recursoHerramienta);
		
		model.addAttribute("tipoHerramientaList", tipoHerramientaService.findAll());
		model.addAttribute("listaEquiposTodo", equipoService.findListEquiposDisponibles());
		model.addAttribute("recursoHerramienta",recursoHerramienta);
		
		return "layout/herramientas :: fragmentModalHerramientas";
	}
	
	
	@RequestMapping(value="/formCrearEditarHerramienta", method = RequestMethod.POST)
	@ResponseBody
	public String guardarHerramienta(@ModelAttribute Herramienta recursoHerramienta, @RequestParam("fotoResponsiva") MultipartFile fotoResponsiva, Model model) {
		
		Equipo equipo = equipoService.findByCodHerramienta(recursoHerramienta.getCodHerramienta());
		
		equipo.setCodEstadoHerramienta(2L);
		
		
		if(!fotoResponsiva.isEmpty()) {
			try {
				byte[] bytesFotoResponsiva = fotoResponsiva.getBytes();
				recursoHerramienta.setResponsiva(bytesFotoResponsiva);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		herramientaService.save(recursoHerramienta);
		return "OK";
	}
	
	
	
	
	
	@RequestMapping(value = "/borrarHerramienta/{id}")
	public String borrarHerramienta(@PathVariable(value = "id") Long codHerramienta, Map<String, Object> model, RedirectAttributes flash) {
		Herramienta herramienta= null;
		if (codHerramienta > 0) {
			herramienta = herramientaService.findOne(codHerramienta);
			if(herramienta == null) {
				flash.addFlashAttribute("error", "El codigo de la herramienta no existe en base de datos!");
				return "redirect:/listarRecursos";
			}
		} else {
			flash.addFlashAttribute("error", "El codigo de la herramienta no debe ser cero!");
			return "redirect:/listarRecursos";
		}
		
		herramientaService.delete(herramienta);
		flash.addFlashAttribute("success", "Herramienta eliminada con éxito");
		return "redirect:../listarHerramientas/" + herramienta.getRecurso().getCodRecurso().toString();
	}
	
	
	@RequestMapping(value = "/cargaHerramientas")
	@ResponseBody
	public String cargaHerramienta(@RequestParam Long cth, Map<String, Object> model) {
		List<Equipo> listaEquipos = equipoService.findListEquiposDisponiblesPorTipoHerramienta(cth);
		StringBuilder opciones = new StringBuilder();
		opciones.append("");
		if (listaEquipos.size() > 0) {
			opciones.append("<option value=\"-1\">--- Seleccione ---</option>");
			for (Equipo equipo : listaEquipos) {
				opciones.append("<option value=\"");
				opciones.append(equipo.getCodHerramienta());
				opciones.append("\">");
				opciones.append(equipo.getDescHerramienta() + " " + equipo.getDescModelo());
				opciones.append("</option>");
			}
		}
		
		return opciones.toString();
	}
	
	@GetMapping(value = "/herramienta/responsiva")
	public void verFotoResponsiva(@RequestParam Long rh,  HttpServletResponse response) throws IOException{
		response.setContentType("image/jpeg");
		Herramienta herramienta = herramientaService.findOne(rh);
		InputStream is = new ByteArrayInputStream(herramienta.getResponsiva());
		IOUtils.copy(is, response.getOutputStream());
	}
	

}
