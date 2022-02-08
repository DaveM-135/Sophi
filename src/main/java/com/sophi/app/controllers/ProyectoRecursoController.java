package com.sophi.app.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Actividad;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.ProyectoRecursoDto;
import com.sophi.app.models.entity.ProyectoRecursoId;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

@Controller
@SessionAttributes("formRecursosProyecto")
public class ProyectoRecursoController {
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IActividadService actividadService;
	
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
		
		List<Actividad> listaActividades =  actividadService.findByCodProyecto(codProyecto);
		int tienePlan = 0;
		if(listaActividades.size()>0) {
			tienePlan = 1;
		}
		model.addAttribute("nombreProyecto", nombreProyecto);
		model.addAttribute("formRecursosProyecto", formRecursosProyecto);
		model.addAttribute("plan", tienePlan);
		return "layout/costoRecursoProyecto :: costoRecursoProyecto";
	}

	
	@PostMapping("/formCostoRecursoProyecto")
	public String guardaCostoRecurso(@ModelAttribute("formRecursosProyecto") ProyectoRecursoDto formRecursosProyecto, @ModelAttribute("codProyecto") Long codProyecto ,  Model model, SessionStatus status) {
		List<ProyectoRecurso> listadoRecursos = new ArrayList<ProyectoRecurso>();
		listadoRecursos = formRecursosProyecto.getListaRecursosProyecto();
		
		for (ProyectoRecurso proyectoRecurso: listadoRecursos) {
			System.out.println(proyectoRecurso.getNombreRecurso()+": "+proyectoRecurso.getImpCostoRecurso()+": "+proyectoRecurso.getValHorasRecurso());
		}
		
		float costoProyecto = 0;
		float totalHoras = 0;
		
		if(listadoRecursos.size() > 0) {
			proyectoRecursoService.saveAll(listadoRecursos);
			status.setComplete();
			
//			List<Actividad> listaActividades =  actividadService.findByCodProyecto(codProyecto);
			//Si tiene plan
//			if(listaActividades.size()>0) {
//				for (ProyectoRecurso proyectoRecurso : listadoRecursos) {
//
//					Float horasRecurso = actividadService.sumTotalHorasProyecto(proyectoRecurso.getProyectoRecursoId().getCodRecurso(), codProyecto);
//					if (horasRecurso == null) {
//						horasRecurso = (float) 0;
//					}
//					Float costoRecurso = horasRecurso * proyectoRecurso.getImpCostoRecurso();
//					
//					totalHoras += horasRecurso;
//					costoProyecto += costoRecurso;
//				}
//			} else {
				for (ProyectoRecurso proyectoRecurso : listadoRecursos) {

					Float horasRecurso = proyectoRecurso.getValHorasRecurso();
					if (horasRecurso == null) {
						horasRecurso = (float) 0;
					}
					Float costoRecurso = horasRecurso * proyectoRecurso.getImpCostoRecurso();
					
					totalHoras += horasRecurso;
					costoProyecto += costoRecurso;
				}
//			}
			
			
		}
		
		//Set de costo y total de horas + guardado de proyecto
		Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
//		proyecto.setImpCostoProyecto(String.format("%.2f", costoProyecto));
		
		
        String pattern = "###,###,###.00";
        //Si no le paso ningun Locale, toma el del sistema, que en mi caso es Locale("es","MX");
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(costoProyecto);
//        System.out.println(costoProyecto + " " + pattern + " " + output);
        proyecto.setImpCostoProyecto(output);
		double d = totalHoras;
		String output2 =  myFormatter.format(d);
		
//		proyecto.setValTotalHorasProyecto(String.format("%.2f", totalHoras));
		proyecto.setValTotalHorasProyecto(output2);
		proyectoService.save(proyecto)
		;
		return "redirect:/preventaProyectoContactoInfraestructura/"+ codProyecto; 
	}
	
	
	@GetMapping("/recursosDispProyecto/{codProyecto}")
	public String recursosDispProyecto(@PathVariable Long codProyecto, Model model) {
		String nombreProyecto = proyectoService.findOne(codProyecto).getDescProyecto();
		List<Recurso> recursoList = new ArrayList<Recurso>();
		recursoList = recursoService.findListRecursosResponsables();
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
		
		//Lista temporal con los recursos actualmente asignados al proyecto
		List<ProyectoRecurso> recursoListAsignadosTmp = new ArrayList<ProyectoRecurso>();
		recursoListAsignadosTmp = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		
		//Datos del proyecto
		Proyecto proyecto = proyectoService.findOne(codProyecto);
		
		//convertir arreglo ids a lista para poder eliminar elementos
		List<Long> listaNuevosAsignados = new ArrayList<>(Arrays.asList(ids));
		
		//Definicion de listas para borrar y para agregar recursos
		List<ProyectoRecurso> listaParaBorrar = new ArrayList<>();
		List<ProyectoRecurso> listaParaAgregar = new ArrayList<>();
	
		//Iteracion para identificar los recursos a borrar y/o agregar
		for (ProyectoRecurso proyectoRecursoTmp : recursoListAsignadosTmp) {
			int existe = 0;
			long q = 0;
			for (Long id : listaNuevosAsignados) {
				if(proyectoRecursoTmp.getProyectoRecursoId().getCodRecurso().equals(id)) {
					existe = 1;
					q = id;
					break;
				}
			}
			if(existe == 0) {
				listaParaBorrar.add(proyectoRecursoTmp);
			} else if(existe == 1) {
				listaNuevosAsignados.remove(q);
			}
		}
		
		//Iterar lista actual de nuevos asignados para completar lista para agregar
		
		for (Long id : listaNuevosAsignados) {
			ProyectoRecurso proyectoRecurso = new ProyectoRecurso();
			ProyectoRecursoId pryectoRecursoId = new ProyectoRecursoId();
			pryectoRecursoId.setCodCliente(proyecto.getCodCliente());
			pryectoRecursoId.setCodEstatusProyecto(proyecto.getCodEstatusProyecto());
			pryectoRecursoId.setCodRecurso(id);
			pryectoRecursoId.setCodProyecto(codProyecto);
			proyectoRecurso.setProyectoRecursoId(pryectoRecursoId);
			
			proyectoRecurso.setImpCostoRecurso(recursoService.findOne(id).getValCostoMinimo());
			proyectoRecurso.setValHorasRecurso((float) 0);
			
			listaParaAgregar.add(proyectoRecurso);
		}
		
		
		for (ProyectoRecurso proyectoRec : listaParaBorrar) {
			proyectoRecursoService.delete(proyectoRec);
		}
		
		for (ProyectoRecurso proyectoRec : listaParaAgregar) {
			proyectoRecursoService.save(proyectoRec);
			try {
				enviaNotificacionAsignacionRecurso(proyecto, proyectoRec.getProyectoRecursoId().getCodRecurso());
			} catch (Exception e) {
				System.out.println("NOTIFICACIONES : <Error> No se puede enviar notificacion al recurso de nueva asignación sin plan");
			}
		}
		
		return "Hecho : "+ codProyecto; 
	}
	
	
	//Asignación de recurso a proyecto 
	public void enviaNotificacionAsignacionRecurso(Proyecto proy, Long codRecurso) {
		Recurso recurso = recursoService.findOne(codRecurso);
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(recurso.getDescRecurso());
		request.setSubject("Nueva asignación - Recurso");
		request.setTo(recurso.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Has sido asignado al proyecto \""+ proy.getDescProyecto() + "\" sin plan de actividades.</h3>");
		model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-status.png\">");
		model.put("pie", "");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
		//Aprobador FIN
	}
	
	
}