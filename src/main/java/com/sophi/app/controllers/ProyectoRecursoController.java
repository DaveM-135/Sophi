package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.ProyectoRecursoDto;
import com.sophi.app.models.entity.ProyectoRecursoId;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

@Controller
@SessionAttributes("formRecursosProyecto")
public class ProyectoRecursoController {
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@GetMapping("/recursosProyecto/{codProyecto}")
	public String recursosProyecto(@PathVariable Long codProyecto, Model model) {
		ProyectoRecursoDto formRecursosProyecto = new ProyectoRecursoDto();
		List<ProyectoRecurso> listaRecursosProyecto = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		for (ProyectoRecurso proyectoRecurso : listaRecursosProyecto) {
			Recurso recurso = recursoService.findOne(proyectoRecurso.getProyectoRecursoId().getCodRecurso());
			proyectoRecurso.setNombreRecurso(recurso.getDescRecurso() + " " + recurso.getDescApellidoPaterno());
			formRecursosProyecto.addProyectoRecurso(proyectoRecurso);
		}
		String nombreProyecto = proyectoService.findOne(codProyecto).getDescProyecto();
		model.addAttribute("nombreProyecto", nombreProyecto);
		model.addAttribute("formRecursosProyecto", formRecursosProyecto);
		return "layout/costoRecursoProyecto :: costoRecursoProyecto";
	}

	
	@PostMapping("/formCostoRecursoProyecto")
	public String guardaCostoRecurso(@ModelAttribute("formRecursosProyecto") ProyectoRecursoDto formRecursosProyecto, @ModelAttribute("codProyecto") Long codProyecto ,  Model model, SessionStatus status) {

		proyectoRecursoService.saveAll(formRecursosProyecto.getListaRecursosProyecto());
		status.setComplete();
		return "redirect:/preventaProyectoContactoInfraestructura/"+ codProyecto; 
	}
	
	
	@GetMapping("/recursosDispProyecto/{codProyecto}")
	public String recursosDispProyecto(@PathVariable Long codProyecto, Model model) {
		String nombreProyecto = proyectoService.findOne(codProyecto).getDescProyecto();
		List<Recurso> recursoList = new ArrayList<Recurso>();
		recursoList = recursoService.findAll();
		List<Recurso> recursoListAsignados = new ArrayList<Recurso>();
		List<ProyectoRecurso> recursoListAsignadosTmp = new ArrayList<ProyectoRecurso>();
		recursoListAsignadosTmp = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		if(recursoListAsignadosTmp.size() > 0) {
			for (ProyectoRecurso proyectoRecurso : recursoListAsignadosTmp) {
				Recurso recurso = recursoService.findOne(proyectoRecurso.getProyectoRecursoId().getCodRecurso());
				recursoList.remove(recurso);
				recursoListAsignados.add(recurso);
			}
		}
		model.addAttribute("nombreProyecto", nombreProyecto);
		model.addAttribute("recursoList", recursoList);
		model.addAttribute("recursoListAsignados", recursoListAsignados);
		return "layout/asignacionRecursosProyecto :: asignacionRecursoProyecto";
	}
	
	
	@RequestMapping(value = "/guardaAsignacionRecursosProyecto")
	@ResponseBody
	public String guardaAsignacionRecursosProyecto(@RequestParam(value = "ids[]") Long[] ids,@RequestParam(value = "codProyecto") Long codProyecto,  Model model) {
		List<ProyectoRecurso> recursoListAsignadosTmp = new ArrayList<ProyectoRecurso>();
		recursoListAsignadosTmp = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		for (ProyectoRecurso proyectoRecurso : recursoListAsignadosTmp) {
			proyectoRecursoService.delete(proyectoRecurso);
		}
		Proyecto proyecto = proyectoService.findOne(codProyecto);
		for (Long id : ids) {
			ProyectoRecurso proyectoRecurso = new ProyectoRecurso();
			ProyectoRecursoId pryectoRecursoId = new ProyectoRecursoId();
			pryectoRecursoId.setCodCliente(proyecto.getCodCliente());
			pryectoRecursoId.setCodEstatusProyecto(proyecto.getCodEstatusProyecto());
			pryectoRecursoId.setCodRecurso(id);
			pryectoRecursoId.setCodProyecto(codProyecto);
			proyectoRecurso.setProyectoRecursoId(pryectoRecursoId);
			proyectoRecursoService.delete(proyectoRecurso);
			proyectoRecursoService.save(proyectoRecurso);
			System.out.println(id);
		}
		return "Hecho : "+ codProyecto; 
	}
}