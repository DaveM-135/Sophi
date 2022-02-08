package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Concepto;
import com.sophi.app.models.entity.DetalleEvaluacionProyecto;
import com.sophi.app.models.entity.Etapa;
import com.sophi.app.models.entity.Evaluacion;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IConceptoService;
import com.sophi.app.models.service.IDetalleEvaluacionProyectoService;
import com.sophi.app.models.service.IEtapaService;
import com.sophi.app.models.service.IEvaluacionService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

@Controller
public class EvaluacionProyectoController {
	
	@Autowired
	private IEtapaService etapaService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IEvaluacionService evaluacionService;
	
	@Autowired
	private IConceptoService conceptoService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IDetalleEvaluacionProyectoService detalleEvaluacionProyectoService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@RequestMapping(value = "/evaluacionProyecto/{codProyecto}", method = RequestMethod.GET)
	public String EvaluacionProyecto(@PathVariable(value = "codProyecto") Long codProyecto, Model model) {
		List<Etapa> etapas = new ArrayList<Etapa>();
		Proyecto proyecto = proyectoService.findOne(codProyecto);
		etapas = etapaService.findAll();
		model.addAttribute("etapas", etapas);
		model.addAttribute("proyecto", proyecto);
		return "evaluacionProyecto";
	}
	
	@RequestMapping(value = "/conceptosActivos", method = RequestMethod.GET)
	@ResponseBody
	public Long[] ConceptosActivos(@RequestParam(value = "codProyecto") Long codProyecto, Model model) {
		
		List<DetalleEvaluacionProyecto> listaConceptosProyecto = new ArrayList<DetalleEvaluacionProyecto>();
		listaConceptosProyecto = detalleEvaluacionProyectoService.findByCodProyecto(codProyecto);
		
		List<Long> conceptosActivos = new ArrayList<Long>();
		
		if(listaConceptosProyecto.size()>0) {
			for (DetalleEvaluacionProyecto detalleEvaluacionProyecto : listaConceptosProyecto) {
				conceptosActivos.add(detalleEvaluacionProyecto.getConcepto().getCodConcepto());
			}
		}
		
		Long[] conceptosActivosArray =  new Long[conceptosActivos.size()];
		conceptosActivosArray=conceptosActivos.toArray(conceptosActivosArray);
		
		return conceptosActivosArray;
	}
	
	
	
	@RequestMapping(value = "/evaluacionProyectoValidacion/{codProyecto}", method = RequestMethod.GET)
	public String EvaluacionProyectoValidacion(@PathVariable(value = "codProyecto") Long codProyecto, Model model) {
		Proyecto proyecto = proyectoService.findOne(codProyecto);
		List<DetalleEvaluacionProyecto> listaConceptosProyecto = new ArrayList<DetalleEvaluacionProyecto>();
		listaConceptosProyecto = detalleEvaluacionProyectoService.findByCodProyecto(codProyecto);
		List<Evaluacion> listaEvaluacion = new ArrayList<Evaluacion>();
		listaEvaluacion = evaluacionService.listaEvaluacion();
		List<Recurso> listaRecursos = new ArrayList<Recurso>();
		List<Recurso> listaRecursosEvaluadores = new ArrayList<Recurso>();
		listaRecursos = recursoService.findRecursosActivos();
		List<ProyectoRecurso> listaProyectoRecurso = new ArrayList<ProyectoRecurso>();
		listaProyectoRecurso = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		
		for(Recurso recurso: listaRecursos) {
			switch(recurso.getPuesto().getDescPuesto()) {
				case "Consultor Junior BI": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Senior BI": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Junior MicroStrategy": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Senior MicroStrategy": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Junior ETL": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Senior ETL": listaRecursosEvaluadores.add(recurso);
				break;
				case "Developer Junior": listaRecursosEvaluadores.add(recurso);
				break;
				case "Developer Senior": listaRecursosEvaluadores.add(recurso);
				break;
				case "Consultor Principal": listaRecursosEvaluadores.add(recurso);
				break;
				default: 
				break;
			}
			for(ProyectoRecurso proyectorecurso: listaProyectoRecurso) {
				if(proyectorecurso.getProyectoRecursoId().getCodRecurso().equals(recurso.getCodRecurso())) {
					listaRecursosEvaluadores.remove(recurso);
				}
			}
		}
		
		model.addAttribute("listaDetalleEvaluacion", listaConceptosProyecto); 
		model.addAttribute("listaEvaluacion", listaEvaluacion);
		model.addAttribute("listaRecursos", listaRecursosEvaluadores);
		model.addAttribute("proyecto", proyecto); 
		return "evaluacionProyectoValidacion";
	}
	
	@RequestMapping(value = "/evaluacionProyectoEvaluador/{codProyecto}", method = RequestMethod.GET)
	public String EvaluacionProyectoEvaluador(@PathVariable(value = "codProyecto") Long codProyecto, Model model) {
		Proyecto proyecto = proyectoService.findOne(codProyecto);
		List<DetalleEvaluacionProyecto> listaConceptosProyecto = new ArrayList<DetalleEvaluacionProyecto>();
		listaConceptosProyecto = detalleEvaluacionProyectoService.findByCodProyecto(codProyecto);
		List<Evaluacion> listaEvaluacion = new ArrayList<Evaluacion>();
		listaEvaluacion = evaluacionService.listaEvaluacion();
		
		
		model.addAttribute("listaDetalleEvaluacion", listaConceptosProyecto); 
		model.addAttribute("listaEvaluacion", listaEvaluacion);
		model.addAttribute("proyecto", proyecto); 
		return "evaluacionProyectoEvaluador";
	}
	
	@RequestMapping(value = "/guardaAsignacionConceptosProyecto", method = RequestMethod.GET)
	@ResponseBody
	public String EvaluacionProyectoValidacion(@RequestParam(value = "codProyecto") Long codProyecto, 
												@RequestParam(value = "conceptos") List<String> conceptos,
												@RequestParam(value = "conceptosNot") List<String> conceptosNot,
												Model model) {
		
		List<DetalleEvaluacionProyecto> listaConceptosProyecto = new ArrayList<DetalleEvaluacionProyecto>();
		if(conceptos.size()>0) {
			if(!conceptos.get(0).equals("[]")) {
				for (String codConcepto : conceptos) {
						Long idConcepto = null;
						idConcepto = Long.parseLong(codConcepto.replace("\"", "").replace("[", "").replace("]", ""));
						Concepto concepto = new Concepto();
						concepto = conceptoService.findByCodConcepto(idConcepto);
						DetalleEvaluacionProyecto depAux = null;
						depAux = detalleEvaluacionProyectoService.findByConceptoAndCodProyecto(concepto, codProyecto);
						
						if (depAux == null) {
							DetalleEvaluacionProyecto dep = new DetalleEvaluacionProyecto();
							dep.setCodProyecto(codProyecto);
							dep.setConcepto(conceptoService.findByCodConcepto(idConcepto));
							listaConceptosProyecto.add(dep);
						}
				}
			}
		}
		if(conceptosNot.size()>0 ) {
			if(!conceptosNot.get(0).equals("[]")) {
				for (String codConceptoNot : conceptosNot) {
					Long idConceptoNot = null;
					idConceptoNot = Long.parseLong(codConceptoNot.replace("\"", "").replace("[", "").replace("]", ""));
					Concepto concepto = new Concepto();
					concepto = conceptoService.findByCodConcepto(idConceptoNot);
					DetalleEvaluacionProyecto depAux = null;
					depAux = detalleEvaluacionProyectoService.findByConceptoAndCodProyecto(concepto, codProyecto);
					
					if(depAux!=null) {
						detalleEvaluacionProyectoService.eliminarDetalle(depAux.getCodEvaluacionProyecto());
					}
				}
			}
			
		}

		
		try {
			if(listaConceptosProyecto.size()>0) {
				detalleEvaluacionProyectoService.guardarTodosDetalle(listaConceptosProyecto);
			}
			return "1";
		} catch (Exception e) {
			return "0";
		}
		
	}
	
	@RequestMapping(value = "/guardaComplementoConcepto", method = RequestMethod.GET)
	@ResponseBody
	public String guardaComplementoConcepto(@RequestParam(value = "codProyecto") Long codProyecto, 
												@RequestParam(value = "codConcepto") Long codConcepto,
												@RequestParam(value = "comentario") String comentario,
												@RequestParam(value = "ruta") String ruta,
												Model model) {
		Concepto concepto = conceptoService.findByCodConcepto(codConcepto);
		DetalleEvaluacionProyecto dep = null;
		
		if(comentario.length()==0) {
			comentario= null;
		}
		
		if(ruta.length()==0) {
			ruta=null;
		}
		
		dep = detalleEvaluacionProyectoService.findByConceptoAndCodProyecto(concepto, codProyecto);
		
		if(dep != null) {
			dep.setDescComentario(comentario);
			dep.setRuta(ruta);
			detalleEvaluacionProyectoService.guardarDetalle(dep);
			return "1";
		} else {
			return "0";
		}
		
	}
	
	
	@RequestMapping(value = "/guardaEvaluacionConcepto", method = RequestMethod.GET)
	@ResponseBody
	public String guardaEvaluacionConcepto(@RequestParam(value = "codProyecto") Long codProyecto, 
												@RequestParam(value = "codConcepto") Long codConcepto,
												@RequestParam(value = "valEvaluador") String valEvaluador,
												@RequestParam(value = "codEvaluacion") Long codEvaluacion,
												Model model) {
		Concepto concepto = conceptoService.findByCodConcepto(codConcepto);
		DetalleEvaluacionProyecto dep = null;
		
		Recurso recurso = recursoService.findByDescCorreoElectronico(valEvaluador);
			
		dep = detalleEvaluacionProyectoService.findByConceptoAndCodProyecto(concepto, codProyecto);
		
		if(dep != null) {
			dep.setCodRecursoEvaluador(recurso.getCodRecurso());
			dep.setCodEvaluacion(codEvaluacion);
			detalleEvaluacionProyectoService.guardarDetalle(dep);
			return "1";
		} else {
			return "0";
		}
		
	}
	
	@RequestMapping(value = "/guardaEvaluacionEncuestaConcepto", method = RequestMethod.GET)
	@ResponseBody
	public String guardaEvaluacionEncuestaConcepto(@RequestParam(value = "codProyecto") Long codProyecto, 
												@RequestParam(value = "codConcepto") Long codConcepto,
												@RequestParam(value = "valEvaluador") String valEvaluador,
												@RequestParam(value = "valEvaluacion") float valEvaluacion,
												Model model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(valEvaluador);
		
		Concepto concepto = conceptoService.findByCodConcepto(codConcepto);
		DetalleEvaluacionProyecto dep = null;
			
		dep = detalleEvaluacionProyectoService.findByConceptoAndCodProyecto(concepto, codProyecto);
		
		if(dep != null) {
			dep.setCalificacion(valEvaluacion);
			dep.setCodRecursoEvaluador(recurso.getCodRecurso());
			detalleEvaluacionProyectoService.guardarDetalle(dep);
			return "1";
		} else {
			return "0";
		}
		
	}
	
	@RequestMapping(value = "/validaEncuestaEvaluador", method = RequestMethod.GET)
	@ResponseBody
	public String validaEncuestaEvaluador(@RequestParam(value = "codProyecto") Long codProyecto, Model model) {
		Integer conceptosNoEvaluados = null;
		conceptosNoEvaluados = detalleEvaluacionProyectoService.conceptosNoEvaluadosByCodProyecto(codProyecto);
		
		if(conceptosNoEvaluados != null) {
			if(conceptosNoEvaluados == 0) {
				return "0";
			} else {
				return "1";
			}
		} else {
			return "1";
		}
	}
	
	
	
	
	@RequestMapping(value = "/enviaMailEvaluador", method = RequestMethod.GET)
	@ResponseBody
	public String enviaMailEvaluador(@RequestParam(value = "idRecurso") Long idRecurso,
			@RequestParam(value = "codProyecto") Long codProyecto,
			 Model model) {
		
		Recurso recurso = recursoService.findOne(idRecurso);
		Proyecto proy = proyectoService.findOne(codProyecto);
		
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(recurso.getDescRecurso());
		request.setSubject("Nueva asignación - Evaluador de proyecto");
		request.setTo(recurso.getDescCorreoElectronico());
		
		Map<String, Object> modelM = new HashMap<String, Object>();
		modelM.put("nombreRecurso", request.getName());
		modelM.put("mensaje", "<h3>Has sido asignado como evaluador del proyecto \""+ proy.getDescProyecto() + "\"</h3>.");
		modelM.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-status.png\">");
		modelM.put("btnLink", "<a href=\"https://"+new Utiles().getHostName()+".com/evaluacionProyectoEvaluador/" +codProyecto+" \" style=\"text-align: center; border-radius: 5px; font-weight: bold; background-color: #C02C57; color: white; padding: 14px 25px; text-decoration: none; display: inline-block; \">Ir a la evaluación</a>");
		modelM.put("pie", "");
		
		MailResponse response = service.sendEmailEvaluador(request, modelM);
		System.out.println(response.getMessage());
		//Aprobador FIN
		
			return "0";
		
	}
	
	
	@RequestMapping(value = "/enviaMailCalidadCierre", method = RequestMethod.GET)
	@ResponseBody
	public String enviaMailCalidadCierre(@RequestParam(value = "idRecurso") Long idRecurso,
			@RequestParam(value = "codProyecto") Long codProyecto,
			 Model model) {
		
		Recurso recurso = recursoService.findOne(idRecurso);
		Proyecto proy = proyectoService.findOne(codProyecto);
		
		//Actualiza proyecto a bandera 1
		proy.setValEvaluacion(1L);
		proy.setFecCierreEvaluacion(new Utiles().getFechaActual());
		proyectoService.save(proy);
		
		
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(recurso.getDescRecurso());
		request.setSubject("Cierre de evaluación de proyecto - Calidad");
		request.setTo(recurso.getDescCorreoElectronico());
		
		Map<String, Object> modelM = new HashMap<String, Object>();
		modelM.put("nombreRecurso", request.getName());
		modelM.put("mensaje", "<h3>La etapa de evaluación del proyecto \""+ proy.getDescProyecto() + "\" ha terminado, requerimos tu validación final para el cierre del proyecto.<br> En el siguiente enlace encontraras la evaluación actual: </h3>.");
		modelM.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-status.png\">");
		modelM.put("btnLink", "<a href=\"https://"+new Utiles().getHostName()+".com/evaluacionProyectoEvaluador/" +codProyecto+" \" style=\"text-align: center; border-radius: 5px; font-weight: bold; background-color: #C02C57; color: white; padding: 14px 25px; text-decoration: none; display: inline-block; \">Cerrar evaluación</a>");
		modelM.put("pie", "");
		
		MailResponse response = service.sendEmailEvaluador(request, modelM);
		System.out.println(response.getMessage());
		//Aprobador FIN
		
			return "0";
		
	}
	
	@RequestMapping(value = "/cierreEvaluacion", method = RequestMethod.GET)
	@ResponseBody
	public String cierreEvaluacion(@RequestParam(value = "codProyecto") Long codProyecto,
			 Model model) {
		
		Proyecto proy = proyectoService.findOne(codProyecto);
		
		//Actualiza proyecto a bandera 2 (cierre de evaluacion)
		proy.setValEvaluacion(2L);
		proy.setFecCierreEvaluacion(new Utiles().getFechaActual());
		proyectoService.save(proy);
		
		return "0";
		
	}
	
	

}
