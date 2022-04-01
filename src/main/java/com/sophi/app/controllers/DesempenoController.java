package com.sophi.app.controllers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.Utiles;
import com.sophi.app.models.dao.RespuestaMeta;
import com.sophi.app.models.entity.CompetenciasPerfiles;
import com.sophi.app.models.entity.EstatusGeneral;
import com.sophi.app.models.entity.EvaluacionDesempeno;
import com.sophi.app.models.entity.MetasPerfiles;
import com.sophi.app.models.entity.PerfilRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoEvaluacion;
import com.sophi.app.models.entity.RecursoEvaluador;
import com.sophi.app.models.entity.RespuestaCompetencia;
import com.sophi.app.models.service.IEvaluacionDesempenoService;
import com.sophi.app.models.service.IPerfilRecursoService;
import com.sophi.app.models.service.IRecursoEvaluacionService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRespuestaCompetenciaService;
import com.sophi.app.models.service.IRespuestaMetasService;
import com.sophi.app.models.service.RecursosEvaluadoresServiceImpl;

@Controller
public class DesempenoController {
	
	@Autowired
	private IPerfilRecursoService perfilRecursoService;
	
	@Autowired
	private IRecursoEvaluacionService recursoEvaluacionService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IEvaluacionDesempenoService evaluacionDesempenoService;
	
	@Autowired
	private IRespuestaCompetenciaService respuestaCompetenciaService;
	
	@Autowired
	private RecursosEvaluadoresServiceImpl recursosEvaluadoresService;
	
	@Autowired
	private IRespuestaMetasService respuestaMetaService;
	
	@RequestMapping(value = "/misEvaluaciones")
	public String misEvaluaciones(Map<String, Object> model) {
		return "listaMisEvaluaciones";
	} 
	
	@RequestMapping(value = "/verMisEvaluaciones")
	public String verMisEvaluaciones(@RequestParam("s") String mail, Map<String, Object> model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
		List<RecursoEvaluacion> listaRecursoEvaluacion = recursoEvaluacionService.findByCodRecursoAndValTipoEvaluador(recurso.getCodRecurso(), 1L);
		
		
		for (RecursoEvaluacion recursoEvaluacion : listaRecursoEvaluacion) {
			if(recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(0L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("Registrada");
			} else if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(1L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("En ejecución");
			} else if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(2L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("Cerrada");
			}
			DecimalFormat df = new DecimalFormat("#.#");
			
			if(recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(1L)) {
				recursoEvaluacion.setPromedioCompetencias(Float.valueOf(df.format(recursoEvaluacionService.getPromedioCompetenciasByCodRecursoAndCodEvaluacionAndTipo(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno(), 1L))));
			} else {
				recursoEvaluacion.setPromedioCompetencias(Float.valueOf(df.format(recursoEvaluacionService.getPromedioCompetenciasByCodRecursoAndCodEvaluacion(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno()))));
			}
			
			
			Float metas = respuestaMetaService.getPromedioMetasByCodRecursoAndCodEvaluacion(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno());
			recursoEvaluacion.setValResultadoMetas(metas == null ? 0 : Float.valueOf(df.format(metas)));
			
			EstatusGeneral eg = getEstatusGeneral(recursoEvaluacion.getCodEvaluacionDesempeno(), recursoEvaluacion.getCodRecurso());
			recursoEvaluacion.setEstatusGeneral(eg.getDescEstatusGeneral());
			recursoEvaluacion.setValEstatusGeneral(eg.getCodEstatusGeneral());
			
			recursoEvaluacion.setValResultadoGeneral(Float.valueOf(df.format((recursoEvaluacion.getPromedioCompetencias() + recursoEvaluacion.getValResultadoMetas())/2)));
			
			
			if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(2L)) {
				Float resultadoFinal = (recursoEvaluacion.getEvaluacionDesempeno().getValPromedioEmpresa() + recursoEvaluacion.getValResultadoGeneral())/2;
				
				recursoEvaluacion.setValResultadoFinal(resultadoFinal == null ? 0 : Float.valueOf(df.format(resultadoFinal)));
			}
			
			
			
		}
		
		model.put("listaRecursoEvaluacion", listaRecursoEvaluacion);
		return "layout/plantilla-mis-evaluaciones :: listMisEvaluaciones";
	} 
	
	public EstatusGeneral getEstatusGeneral(Long codEvaluacion, Long codRecurso){
		List<RecursoEvaluacion> listaRecursoEvaluacion = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempeno(codRecurso, codEvaluacion);
		
		String estatusGeneral = "";
		Long valEstatusGeneral = 0L;
		int total = listaRecursoEvaluacion.size();
		if(total == 1) {
			for (RecursoEvaluacion re : listaRecursoEvaluacion) {
				estatusGeneral = re.getDescEstatus();
				valEstatusGeneral = re.getValEstatus();
			}
		} else {
			int completos = 0;
			for (RecursoEvaluacion re : listaRecursoEvaluacion) {
				if(re.getValEstatus().equals(3L)) {
					completos++;
				}
			}
			if(completos == total) {
				estatusGeneral="evaluación completa";
				valEstatusGeneral = 5L;
			} else {
				estatusGeneral="en evaluación";
				valEstatusGeneral = 4L;
			}
		}
		
		return new EstatusGeneral(valEstatusGeneral, estatusGeneral);
	}
	
	@RequestMapping(value = "/misEvaluados")
	public String misEvaluados(Map<String, Object> model) {
		return "listaMisEvaluados";
	} 
	
	@RequestMapping(value = "/verMisEvaluados")
	public String verMisEvaluados(@RequestParam("s") String mail, Map<String, Object> model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
		List<RecursoEvaluacion> listaRecursoEvaluacion = recursoEvaluacionService.findByCodRecursoEvaluadorAndValTipoEvaluador(recurso.getCodRecurso(),2L);
		
		for (RecursoEvaluacion recursoEvaluacion : listaRecursoEvaluacion) {
			if(recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(0L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("Registrada");
			} else if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(1L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("En ejecución");
			} else if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(2L)) {
				recursoEvaluacion.getEvaluacionDesempeno().setDescEstatus("Cerrada");
			}
			DecimalFormat df = new DecimalFormat("#.#");
			
			Float metas = respuestaMetaService.getPromedioMetasByCodRecursoAndCodEvaluacion(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno());
			recursoEvaluacion.setValResultadoMetas(metas == null ? 0 : Float.valueOf(df.format(metas)));
			
			recursoEvaluacion.setPromedioCompetencias(Float.valueOf(df.format(recursoEvaluacionService.getPromedioCompetenciasByCodRecursoAndCodEvaluacion(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno()))));
			
			EstatusGeneral eg = getEstatusGeneral(recursoEvaluacion.getCodEvaluacionDesempeno(), recursoEvaluacion.getCodRecurso());
			recursoEvaluacion.setEstatusGeneral(eg.getDescEstatusGeneral());
			recursoEvaluacion.setValEstatusGeneral(eg.getCodEstatusGeneral());
			
			recursoEvaluacion.setValResultadoGeneral(Float.valueOf(df.format((recursoEvaluacion.getPromedioCompetencias() + recursoEvaluacion.getValResultadoMetas())/2)));
			
			
			if (recursoEvaluacion.getEvaluacionDesempeno().getValEstatus().equals(2L)) {
				Float resultadoFinal = (recursoEvaluacion.getEvaluacionDesempeno().getValPromedioEmpresa() + recursoEvaluacion.getValResultadoGeneral())/2;
				
				recursoEvaluacion.setValResultadoFinal(resultadoFinal == null ? 0 : Float.valueOf(df.format(resultadoFinal)));
			}
			
			
			
		}
		
		model.put("listaRecursoEvaluacion", listaRecursoEvaluacion);
		model.put("codEvaluador", recurso.getCodRecurso());
		return "layout/plantilla-mis-evaluaciones :: listMisEvaluados";
	} 
	
	@RequestMapping(value = "/autoevaluacion")
	public String ejecutarEvaluacion(@RequestParam("re") Long codEvaluacionRecurso, 
			@RequestParam("r") Long codRecurso, @RequestParam("e") Long codEvaluacion, Map<String, Object> model) {
		RecursoEvaluacion re = recursoEvaluacionService.findById(codEvaluacionRecurso);
		if(re != null) {
			if(re.getCodRecurso().equals(codRecurso) && re.getCodEvaluacionDesempeno().equals(codEvaluacion)){
				
				List<RespuestaCompetencia> listaRespuestasCompetencia = new ArrayList<RespuestaCompetencia>();
				List<RespuestaMeta> listaRespuestasMetas = new ArrayList<RespuestaMeta>();
				
				if (respuestaCompetenciaService.findByRecursoEvaluacionTipo(codRecurso, codEvaluacion, 1L).size() == 0) {
					PerfilRecurso perfil = perfilRecursoService.findByCodPerfil(recursoService.findOne(codRecurso).getCodPerfil());
					List<CompetenciasPerfiles> listaCompetencias = perfil.getCompetenciasPerfiles();
					List<MetasPerfiles> ListaMetas = perfil.getMetasPerfiles();
					for (CompetenciasPerfiles competenciasPerfiles : listaCompetencias) {
						RespuestaCompetencia rc = new RespuestaCompetencia(codEvaluacion, 
								codRecurso,
								competenciasPerfiles.getCompetencia(),
								competenciasPerfiles.getDescDefinicion(), null, codRecurso, 1L);
						rc.setCodCompetencias(competenciasPerfiles.getCompetencia().getCodCompetencias());
						listaRespuestasCompetencia.add(rc);
					}
					for (MetasPerfiles metasPerfiles : ListaMetas) {
						RespuestaMeta rm = new RespuestaMeta();
						rm.setCodEvaluacionDesempeno(codEvaluacion);
						rm.setCodMeta(metasPerfiles.getMeta().getCodMeta());
						rm.setMeta(metasPerfiles.getMeta());
						rm.setCodRecurso(codRecurso);
						rm.setValPorcentaje(metasPerfiles.getValPorcentaje());
						//calculo Captura de horas
						if(rm.getCodMeta().equals(3L)) {
							EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
							SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
							Long mesIdInicio = Long.valueOf(formatter.format(ed.getFecPeriodoInicio()));
							Long mesIdFin = Long.valueOf(formatter.format(ed.getFecPeriodoFin()));
							
							
							Float horasHabiles = evaluacionDesempenoService.getHorasHabilesByMes(mesIdInicio,mesIdFin);
							Float horasValidadas = evaluacionDesempenoService.getHorasHabilesValidadasByCodRecursoAndMes(codRecurso, ed.getFecPeriodoInicio(), ed.getFecPeriodoFin());
							if (horasHabiles != null && horasValidadas != null) {
								if (horasValidadas >= horasHabiles) {
									rm.setValResultado(100L);
									DecimalFormat df = new DecimalFormat("#.#");
									rm.setValResultadoPorcentaje(Float.valueOf(df.format(rm.getValPorcentaje())));
								} else {
									rm.setValResultado((long)((horasValidadas*100)/horasHabiles));
									DecimalFormat df = new DecimalFormat("#.#");
									rm.setValResultadoPorcentaje(Float.valueOf(df.format((rm.getValResultado() * rm.getValPorcentaje()) / 100)));
								}
							}
						}
						//calculo CSI
						if(rm.getCodMeta().equals(6L)) {
							EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
							Float csi = evaluacionDesempenoService.getAvgCsiByCodRecursoAndMes(codRecurso, ed.getFecPeriodoInicio(), ed.getFecPeriodoFin());
							if (csi != null) {
								rm.setValResultado((long) (csi*10));
								DecimalFormat df = new DecimalFormat("#.#");
								rm.setValResultadoPorcentaje(Float.valueOf(df.format((rm.getValResultado() * rm.getValPorcentaje()) / 100)));
							} 
						}
						listaRespuestasMetas.add(rm);
					}
					
					respuestaCompetenciaService.guardaAll(listaRespuestasCompetencia);
					respuestaMetaService.guardarAll(listaRespuestasMetas);
					
					
					
				} else {
					listaRespuestasCompetencia = respuestaCompetenciaService.findByRecursoEvaluacionTipo(codRecurso, codEvaluacion, 1L);
				}
				
				EvaluacionDesempeno evaluacion = evaluacionDesempenoService.findById(codEvaluacion);
				
				model.put("evaluacion", evaluacion);
				model.put("listaRespuestasCompetencia", listaRespuestasCompetencia);
				model.put("recursoEvaluacion", re);
				model.put("titulo", "Autoevaluación");
				model.put("tituloColumna", "Autoevaluación");
				return "evaluacionDesempeno";
			} else {
				return "error";
			}
		} else {
			return "error";
		}
		
	} 
	
	
	@RequestMapping(value = "/evaluacion")
	public String ejecutarEvaluacionAprobador(@RequestParam("re") Long codEvaluacionRecurso, 
			@RequestParam("r") Long codRecurso, @RequestParam("e") Long codEvaluacion, @RequestParam("v") Long codRecursoEvaluador, Map<String, Object> model) {
		RecursoEvaluacion re = recursoEvaluacionService.findById(codEvaluacionRecurso);
		if(re != null) {
			if(re.getCodRecurso().equals(codRecurso) && re.getCodEvaluacionDesempeno().equals(codEvaluacion) && re.getCodRecursoEvaluador().equals(codRecursoEvaluador)){
				
				List<RespuestaCompetencia> listaRespuestasCompetencia = new ArrayList<RespuestaCompetencia>();
				
				if (respuestaCompetenciaService.findByRecursoEvaluacionTipoEvaluador(codRecurso, codEvaluacion, 2L, codRecursoEvaluador).size() == 0) {
					PerfilRecurso perfil = perfilRecursoService.findByCodPerfil(recursoService.findOne(codRecurso).getCodPerfil());
					List<CompetenciasPerfiles> listaCompetencias = perfil.getCompetenciasPerfiles();
					for (CompetenciasPerfiles competenciasPerfiles : listaCompetencias) {
						RespuestaCompetencia rc = new RespuestaCompetencia(codEvaluacion, 
								codRecurso,
								competenciasPerfiles.getCompetencia(),
								competenciasPerfiles.getDescDefinicion(), null, re.getCodRecursoEvaluador(), 2L);
						rc.setCodCompetencias(competenciasPerfiles.getCompetencia().getCodCompetencias());
						listaRespuestasCompetencia.add(rc);
					}
					
					respuestaCompetenciaService.guardaAll(listaRespuestasCompetencia);
				} else {
					listaRespuestasCompetencia = respuestaCompetenciaService.findByRecursoEvaluacionTipoEvaluador(codRecurso, codEvaluacion, 2L, codRecursoEvaluador);
				}
				
				EvaluacionDesempeno evaluacion = evaluacionDesempenoService.findById(codEvaluacion);
				
				model.put("evaluacion", evaluacion);
				model.put("listaRespuestasCompetencia", listaRespuestasCompetencia);
				model.put("recursoEvaluacion", re);
				model.put("titulo", "Evaluación de " + re.getDescRecurso() +" en ");
				model.put("tituloColumna", "Evaluación");
				
				return "evaluacionDesempeno";
			} else {
				return "error";
			}
		} else {
			return "error";
		}
		
	} 
	
	
	
	//return pagina con listado de evaluaciones hechas por RH
	@RequestMapping(value = "/evaluaciones")
	public String evaluaciones(Map<String, Object> model) {
		List<EvaluacionDesempeno> listaEvaluaciones = evaluacionDesempenoService.findAll();
		for (EvaluacionDesempeno evaluacionDesempeno : listaEvaluaciones) {
			if(evaluacionDesempeno.getValEstatus().equals(0L)) {
				evaluacionDesempeno.setDescEstatus("Registrada");
			} else if (evaluacionDesempeno.getValEstatus().equals(1L)) {
				evaluacionDesempeno.setDescEstatus("En ejecución");
			} else if (evaluacionDesempeno.getValEstatus().equals(2L)) {
				evaluacionDesempeno.setDescEstatus("Cerrada");
			}
			DecimalFormat df = new DecimalFormat("#.#");
			Float competencias = recursoEvaluacionService.getPromedioCompetenciasByCodEvaluacion(evaluacionDesempeno.getCodEvaluacionDesempeno());
			evaluacionDesempeno.setPromedioCompetencias(competencias== null ? 0 : Float.valueOf(df.format(competencias)));
			
			Float metas = respuestaMetaService.getPromedioMetasByCodEvaluacion(evaluacionDesempeno.getCodEvaluacionDesempeno());
			evaluacionDesempeno.setPromedioMetas(metas== null ? 0 : Float.valueOf(df.format(metas)));
			
			evaluacionDesempeno.setValPromedioEmpresa(Float.valueOf(df.format((evaluacionDesempeno.getPromedioMetas() + evaluacionDesempeno.getPromedioCompetencias())/2)));
		}
		model.put("listaEvaluaciones", listaEvaluaciones);
		return "evaluaciones";
	} 
	
	
	@RequestMapping(value = "/verDetalleEvaluacion")
	public String verDetalleEvaluacion(@RequestParam("e") Long codEvaluacion, Map<String, Object> model) {
		EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
		
		for (RecursoEvaluacion re : ed.getRecursosEvaluacion()) {
			String evaluadores ="";
			List<RecursoEvaluador> listaRecursoEvaluadores = recursosEvaluadoresService.findByCodEvaluacionDesempenoAndCodRecurso(re.getCodEvaluacionDesempeno(), re.getCodRecurso());
			for (RecursoEvaluador recursoEvaluador : listaRecursoEvaluadores) {
				evaluadores+=recursoEvaluador.getDescEvaluador()+", ";
			}
			re.setEvaluadoresConcat(evaluadores.trim());
			
			DecimalFormat df = new DecimalFormat("#.#");
			re.setPromedioCompetencias(Float.valueOf(df.format(recursoEvaluacionService.getPromedioCompetenciasByCodRecursoAndCodEvaluacion(re.getCodRecurso(), re.getCodEvaluacionDesempeno()))));
			
			Float metas = respuestaMetaService.getPromedioMetasByCodRecursoAndCodEvaluacion(re.getCodRecurso(), re.getCodEvaluacionDesempeno());
			re.setValResultadoMetas(metas == null ? 0 : Float.valueOf(df.format(metas)));
			
			String estatusGeneral = "";
			Long valEstatusGeneral = 0L;
			Long totalEvRecurso = respuestaCompetenciaService.getTotalEvaluadosByCodRecursoAndCodEvaluacion(re.getCodRecurso(), re.getCodEvaluacionDesempeno());
			if(totalEvRecurso.equals(0L) || totalEvRecurso.equals(1L)) {
					estatusGeneral = re.getDescEstatus();
					valEstatusGeneral = re.getValEstatus();
			} else {
				int completos = 0;
				List<RecursoEvaluacion> listaEvaluado =	recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempeno(re.getCodRecurso(), re.getCodEvaluacionDesempeno());
				int total = listaEvaluado.size();
				for (RecursoEvaluacion red : recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempeno(re.getCodRecurso(), re.getCodEvaluacionDesempeno())) {
					if(red.getValEstatus().equals(3L)) {
						completos++;
					}
				}
				if(completos == total) {
					estatusGeneral="evaluación completa";
					valEstatusGeneral = 5L;
				} else {
					estatusGeneral="en evaluación";
					valEstatusGeneral = 4L;
				}
			}
			
			re.setEstatusGeneral(estatusGeneral);
			re.setValEstatusGeneral(valEstatusGeneral);
			
			re.setValResultadoGeneral(Float.valueOf(df.format((re.getPromedioCompetencias() + re.getValResultadoMetas())/2)));
			
			
			if (re.getEvaluacionDesempeno().getValEstatus().equals(2L)) {
				Float resultadoFinal = (re.getEvaluacionDesempeno().getValPromedioEmpresa() + re.getValResultadoGeneral())/2;
				
				re.setValResultadoFinal(resultadoFinal == null ? 0 : Float.valueOf(df.format(resultadoFinal)));
			}
			
			
			
			
			
		}
		model.put("evaluacionDesempeno", ed);
		return "layout/plantilla-evaluaciones :: detalleEvaluacion";
	}
	
	@RequestMapping(value = "/descargarResultadosEvalDesempeno")
	@ResponseBody
	public String descargarResultadosEvalDesempeno(@RequestParam Long codEvalDesc, @RequestParam Long codRecurso) {
		StringBuilder link = new StringBuilder();
		
		link.append("http://34.132.85.42:8080/MicroStrategy/servlet/mstrWeb?");
		link.append("Server=34.132.85.42&Project=Plataforma+Sophitech&Port=0&evt=3069&");
		link.append("src=mstrWeb.3069&executionMode=3&documentID=4711493948652505FD92A3B0827135CD&");
		link.append("hiddensections=header,path,dockTop,dockLeft,footer&");
		link.append("valuePromptAnswers=");
		link.append(codEvalDesc);
		link.append("%5e");
		link.append(codRecurso);
		link.append("%5e");
		link.append(codRecurso);
		link.append("%5e");
		link.append(codEvalDesc);
		link.append("&uid=desarrollo&pwd=plataforma2020");
		
		return link.toString();
	}
	
	@RequestMapping(value = "/verEvaluadores")
	public String verEvaluadores(@RequestParam("e") Long codEvaluacion, @RequestParam("r") Long codRecurso, Map<String, Object> model) {
		List<RecursoEvaluador> listaEvaluadores = recursosEvaluadoresService.findByCodEvaluacionDesempenoAndCodRecurso(codEvaluacion, codRecurso);
		for (RecursoEvaluador recursoEvaluador : listaEvaluadores) {
			recursoEvaluador.setDescEvaluador(recursoService.getNombreApellidoPuestoById(recursoEvaluador.getCodEvaluador()));
		}
		
		
		List<Recurso> listaRecursosActivosInicial = recursoService.findRecursosActivos();
		List<Recurso> listaRecursosActivos = new ArrayList<>();
		for (Recurso recurso : listaRecursosActivosInicial) {
			if(!recurso.getCodRecurso().equals(codRecurso)) {
				listaRecursosActivos.add(recurso);
			}
		}
		model.put("listaRecursosActivos", listaRecursosActivos);
		model.put("listaEvaluadores", listaEvaluadores);
		model.put("re", codRecurso);
		model.put("ev", codEvaluacion);
		model.put("titulo", "Evaluadores");
		return "layout/plantilla-evaluaciones :: fragmentModalEvaluadoresRecurso";
	} 
	
	@RequestMapping(value = "/modificarEvaluadores")
	@ResponseBody
	public String modificarEvaluadores(@RequestParam(value="evaluadores[]") Long[] evaluadores,
			@RequestParam("re") Long codRecurso,
			@RequestParam("ev") Long codEvaluacion, Map<String, Object> model) {
		recursosEvaluadoresService.deleteRecursoEvaluadorByCodRecursoAndCodEvaluacion(codRecurso, codEvaluacion);
		List<RecursoEvaluador> listaEvaluadores = new ArrayList<>();
		for (Long codEvaluador : evaluadores) {
			RecursoEvaluador re = new RecursoEvaluador();
			re.setCodEvaluacionDesempeno(codEvaluacion);
			re.setEvaluacionDesempeno(evaluacionDesempenoService.findById(codEvaluacion));
			re.setCodRecurso(codRecurso);
			re.setDescRecurso(recursoService.getNombreApellidoById(codRecurso));
			re.setCodEvaluador(codEvaluador);
			re.setDescEvaluador(recursoService.getNombreApellidoById(codEvaluador));
			re.setDescMailEvaluador(recursoService.getEmailRecursoById(codEvaluador));
			listaEvaluadores.add(re);
		}
		recursosEvaluadoresService.guardarAll(listaEvaluadores);
		return "ok";
	}
	
	
	public String evaluadores(List<RecursoEvaluador> listaEvaluadoresAgrupado) {
		String evaluadores = "";
		for (RecursoEvaluador recursoEvaluador : listaEvaluadoresAgrupado) {
			evaluadores += recursoEvaluador.getDescEvaluador() + ",";
		}
		return evaluadores;
	}
	
	
	@RequestMapping(value = "/nuevaEvaluacion")
	public String nuevaEvaluacion(Map<String, Object> model) {
		EvaluacionDesempeno ed = new EvaluacionDesempeno();
		model.put("evaluacionDesempeno", ed);
		model.put("titulo", "Nueva evaluación");
		model.put("listaRecursos", recursoService.findActivosOnlyIdNombre());
		return "layout/plantilla-evaluaciones :: nuevaEvaluacion";
	} 
	
	@PostMapping(value = "/formEvaluacion")
	public String guardarEvaluacion(@RequestParam(name="inicioEvaluacion",required = false) String inicioEvaluacion,
			@RequestParam(name="cierreEvaluacion",required = false) String cierreEvaluacion,
			@RequestParam(name="chkRecurso",required = false) List<Long> listRecursos,
			EvaluacionDesempeno evaluacionDesempeno, Map<String, Object> model) {
		
		evaluacionDesempeno.setFecPeriodoFin(getLastDayOfDate(evaluacionDesempeno.getFecPeriodoFin()));
		
		
		if(evaluacionDesempeno.getValEstatus() == null || evaluacionDesempeno.getValEstatus().equals(0L)) {
			if(inicioEvaluacion == null) {
				evaluacionDesempeno.setValEstatus(0L);
				evaluacionDesempenoService.guardar(evaluacionDesempeno);
			} else {
				evaluacionDesempeno.setValEstatus(1L);
				List<RecursoEvaluacion> listaRecursosEvaluacion = new ArrayList<>();
				for (Long id : listRecursos) {
					RecursoEvaluacion re = new RecursoEvaluacion();
					re.setCodRecurso(id);
					re.setCodEvaluacionDesempeno(evaluacionDesempeno.getCodEvaluacionDesempeno());
					re.setEvaluacionDesempeno(evaluacionDesempeno);
					re.setDescRecurso(recursoService.getNombreApellidoById(id));
					re.setCodRecursoEvaluador(id);
					re.setValTipoEvaluador(1L);
					re.setValEstatus(1L);
					re.setDescEstatus("no iniciada");
					re.setValResultadoMetas(0);
					re.setValResultadoCompetencias(0);
					listaRecursosEvaluacion.add(re);
				}
				
				evaluacionDesempeno.setRecursosEvaluacion(listaRecursosEvaluacion);
				evaluacionDesempeno.setFecArranque(new Utiles().getFechaActual());
				evaluacionDesempenoService.guardar(evaluacionDesempeno);
				recursosEvaluadoresService.notificacionInicioEvaluacion(listaRecursosEvaluacion);
				recursosEvaluadoresService.evaluadoresRecurso(listaRecursosEvaluacion);
			}
		} else if(evaluacionDesempeno.getValEstatus().equals(1L)) {
					if(cierreEvaluacion != null) {	
						
						DecimalFormat df = new DecimalFormat("#.#");
						
						Float desempeno = recursoEvaluacionService.getPromedioCompetenciasByCodEvaluacion(evaluacionDesempeno.getCodEvaluacionDesempeno());
						evaluacionDesempeno.setPromedioCompetencias(desempeno== null ? 0 : Float.valueOf(df.format(desempeno)));
						
						Float metas = respuestaMetaService.getPromedioMetasByCodEvaluacion(evaluacionDesempeno.getCodEvaluacionDesempeno());
						evaluacionDesempeno.setPromedioMetas(metas== null ? 0 : Float.valueOf(df.format(metas)));
						
						Float promedio = ((evaluacionDesempeno.getPromedioMetas() + evaluacionDesempeno.getPromedioCompetencias())/2);
						Float promedioEmpresa = promedio== null ? 0 : Float.valueOf(df.format(promedio));
						
						evaluacionDesempenoService.updateCierreEvaluacionDesempeno(evaluacionDesempeno.getCodEvaluacionDesempeno(), 2L, new Utiles().getFechaActual(), promedioEmpresa);
						
						//Actualizacion de resultados por evaluado
						List<RecursoEvaluacion> listaRecursosEvaluacion = recursoEvaluacionService.findByCodEvaluacionDesempeno(evaluacionDesempeno.getCodEvaluacionDesempeno());
						List<RecursoEvaluacion> listaNotificaciones = new ArrayList<>();
						for (RecursoEvaluacion re : listaRecursosEvaluacion) {
							
							Float metasRE = respuestaMetaService.getPromedioMetasByCodRecursoAndCodEvaluacion(re.getCodRecurso(), re.getCodEvaluacionDesempeno());
							re.setValResultadoMetas(metasRE == null ? 0 : Float.valueOf(df.format(metasRE)));
							
							re.setValResultadoGeneral(Float.valueOf(df.format((re.getValResultadoCompetencias() + re.getValResultadoMetas())/2)));
							
							if(re.getValTipoEvaluador().equals(1L)) {
								listaNotificaciones.add(re);
							}
						}
						
						recursoEvaluacionService.guardarAll(listaRecursosEvaluacion);
						recursosEvaluadoresService.notificacionFinEvaluacion(listaNotificaciones);
						
					} 
		}
		
		return "redirect:/evaluaciones";
	} 
	
	public Date getLastDayOfDate(Date fecha) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fecha);
	    int ultimoDia = cal.getActualMaximum(Calendar.DATE);
	    int mes = cal.get(Calendar.MONTH);
	    int anio = cal.get(Calendar.YEAR);
		cal.set(anio, mes, ultimoDia);
		return cal.getTime();
	}
	
	@RequestMapping(value = "/editarDetalleEvaluacion")
	public String editarDetalleEvaluacion(@RequestParam("e") Long codEvaluacion, Map<String, Object> model) {
		EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
		model.put("evaluacionDesempeno", ed);
		model.put("titulo", "Editar evaluación");
		model.put("listaRecursos", recursoService.findActivosOnlyIdNombre());
		return "layout/plantilla-evaluaciones :: nuevaEvaluacion";
	} 
	
	@RequestMapping(value = "/borrarEvaluacion")
	@ResponseBody
	public String borrarEvaluacion(@RequestParam("e") Long codEvaluacion, Map<String, Object> model) {
		evaluacionDesempenoService.delete(codEvaluacion);
		return "1";
	} 
	
	
}
