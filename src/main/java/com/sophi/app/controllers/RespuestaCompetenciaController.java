package com.sophi.app.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.models.entity.RecursoEvaluacion;
import com.sophi.app.models.entity.RecursoEvaluador;
import com.sophi.app.models.entity.RespuestaCompetencia;
import com.sophi.app.models.service.ICapHoraService;
import com.sophi.app.models.service.ICompetenciaService;
import com.sophi.app.models.service.IEvaluacionDesempenoService;
import com.sophi.app.models.service.IRecursoEvaluacionService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRecursosEvaluadoresService;
import com.sophi.app.models.service.IRespuestaCompetenciaService;
import com.sophi.app.models.service.RecursosEvaluadoresServiceImpl;

@Controller
public class RespuestaCompetenciaController {
	
	@Autowired
	private IRespuestaCompetenciaService respuestaCompetenciaService;
	
	@Autowired
	private IRecursoEvaluacionService recursoEvaluacionService;
	
	@Autowired
	private IRecursosEvaluadoresService recursoEvaluadorService;
	
	@Autowired
	private ICapHoraService capHoraService;
	
	@Autowired
	private RecursosEvaluadoresServiceImpl recursosEvaluadoresService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private ICompetenciaService competenciaService;
	
	@Autowired
	private IEvaluacionDesempenoService evaluacionDesempenoService;
	
	@RequestMapping(value = "/guardarRespuestaCompetencia")
	@ResponseBody
	public String guardarRespuestaCompetencia(@RequestParam("arc") Long codRespuestaCompetencia,
			@RequestParam("resultado") Long resultado,
			Map<String, Object> model) {
		String result="1";
		RespuestaCompetencia rc = respuestaCompetenciaService.findById(codRespuestaCompetencia);
		RecursoEvaluacion re = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluadorAndValTipoEvaluador(rc.getCodRecurso(), rc.getCodEvaluacionDesempeno(), rc.getCodRecursoEvaluador(), rc.getValTipoEvaluador());
		
		rc.setValResultado(resultado);
		if (re.getValTipoEvaluador().equals(1L)) {
			re.setValEstatus(2L);
			re.setDescEstatus("en autoevaluación");
		} else if(re.getValTipoEvaluador().equals(2L)) {
			re.setValEstatus(2L);
			re.setDescEstatus("en evaluación");
		}
		
		try {
			respuestaCompetenciaService.guardar(rc);
			recursoEvaluacionService.guardar(re);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	
	@RequestMapping(value = "/getLideresEvaluacion")
	public String obtenerLideresEvaluacion(@RequestParam("re") Long codRecursoEvaluacion,
			Map<String, Object> model) {
		RecursoEvaluacion re = recursoEvaluacionService.findById(codRecursoEvaluacion);
		List<String> listaProyectos = capHoraService.findProyectosByCodRecursoAndFechaInicioAndFechaFin(re.getCodRecurso(), re.getEvaluacionDesempeno().getFecPeriodoInicio(), re.getEvaluacionDesempeno().getFecPeriodoFin());
		List<RecursoEvaluador> listaEvaluadores = recursoEvaluadorService.findByCodEvaluacionDesempenoAndCodRecurso(re.getCodEvaluacionDesempeno(), re.getCodRecurso());
		model.put("listaProyectos", listaProyectos);
		model.put("listaEvaluadores", listaEvaluadores);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalCierreAutoevaluacion";
	}

	
	@RequestMapping(value = "/cerrarAutoevaluacion")
	@ResponseBody
	public String cerrarAutoevaluacion(@RequestParam("re") Long codRecursoEvaluacion,
			@RequestParam("pca") Float resultado,
			Map<String, Object> model) {
		String result="1";
		RecursoEvaluacion re = recursoEvaluacionService.findById(codRecursoEvaluacion);
		re.setValResultadoCompetencias(resultado);
		re.setValEstatus(3L);
		re.setDescEstatus("autoevaluación completa");
		
		//Guarda evaluadores y envia notificación para evaluacion
		recursosEvaluadoresService.guardarEvaluadoresRecurso(re);
		
		try {
			recursoEvaluacionService.guardar(re);
		} catch (Exception e) {
			result="0";
		}
		
		return result;
	}
	
	@RequestMapping(value = "/cerrarEvaluacion")
	@ResponseBody
	public String cerrarEvaluacion(@RequestParam("re") Long codRecursoEvaluacion,
			@RequestParam("pca") Float resultado,
			Map<String, Object> model) {
		String result="1";
		RecursoEvaluacion re = recursoEvaluacionService.findById(codRecursoEvaluacion);
		re.setValResultadoCompetencias(resultado);
		re.setValEstatus(3L);
		re.setDescEstatus("evaluación completa");
		
		try {
			recursoEvaluacionService.guardar(re);
		} catch (Exception e) {
			result="0";
		}
		
		return result;
	}
	
	
	@RequestMapping(value = "/getRespuestasEvaluacion")
	public String getRespuestasEvaluacion(@RequestParam("re") Long codRecuro,
			@RequestParam("ev") Long codEvaluacion,
			Map<String, Object> model) {
		List<RecursoEvaluacion> listaEvaluadores = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempeno(codRecuro, codEvaluacion);
		for (RecursoEvaluacion recursoEvaluacion : listaEvaluadores) {
			List<RespuestaCompetencia> respuestas = respuestaCompetenciaService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluador(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno(), recursoEvaluacion.getCodRecursoEvaluador());
			recursoEvaluacion.setRespuestasCompetencias(respuestas);
			recursoEvaluacion.setEvaluadoresConcat(recursoService.getNombreApellidoById(recursoEvaluacion.getCodRecursoEvaluador()));
		}
		model.put("listaEvaluadores", listaEvaluadores);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalDetalleRespuestas";
	}
	
	
	
	@RequestMapping(value = "/getRespuestasEvaluacionEvaluador")
	public String getRespuestasEvaluacionEvaluador(@RequestParam("re") Long codRecuro,
			@RequestParam("ev") Long codEvaluacion, @RequestParam("er") Long codEvaluador,
			Map<String, Object> model) {
		List<RecursoEvaluacion> listaEvaluadores = new ArrayList<>();
			
		//Autoevaluacion
		RecursoEvaluacion recursoEvaluacion = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluadorAndValTipoEvaluador(codRecuro, codEvaluacion, codRecuro, 1L);
		
		List<RespuestaCompetencia> respuestas = respuestaCompetenciaService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluador(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno(), recursoEvaluacion.getCodRecursoEvaluador());
		recursoEvaluacion.setRespuestasCompetencias(respuestas);
		recursoEvaluacion.setEvaluadoresConcat(recursoService.getNombreApellidoById(recursoEvaluacion.getCodRecursoEvaluador()));
	
		listaEvaluadores.add(recursoEvaluacion);
		
		//Evaluacion evaluador
		RecursoEvaluacion recursoEvaluacionEvaluador = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluadorAndValTipoEvaluador(codRecuro, codEvaluacion, codEvaluador, 2L);
		
		List<RespuestaCompetencia> respuestasEvaluador = respuestaCompetenciaService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluador(recursoEvaluacionEvaluador.getCodRecurso(), recursoEvaluacionEvaluador.getCodEvaluacionDesempeno(), recursoEvaluacionEvaluador.getCodRecursoEvaluador());
		recursoEvaluacionEvaluador.setRespuestasCompetencias(respuestasEvaluador);
		recursoEvaluacionEvaluador.setEvaluadoresConcat(recursoService.getNombreApellidoById(recursoEvaluacionEvaluador.getCodRecursoEvaluador()));
		
		listaEvaluadores.add(recursoEvaluacionEvaluador);
			
		model.put("listaEvaluadores", listaEvaluadores);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalDetalleRespuestas";
	}
	
	
	@RequestMapping(value = "/getRespuestasEvaluacionEvaluado")
	public String getRespuestasEvaluacionEvaluado(@RequestParam("re") Long codRecuro,
			@RequestParam("ev") Long codEvaluacion,
			Map<String, Object> model) {
		List<RecursoEvaluacion> listaEvaluadores = new ArrayList<>();
			
		//Autoevaluacion
		RecursoEvaluacion recursoEvaluacion = recursoEvaluacionService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluadorAndValTipoEvaluador(codRecuro, codEvaluacion, codRecuro, 1L);
		
		List<RespuestaCompetencia> respuestas = respuestaCompetenciaService.findByCodRecursoAndCodEvaluacionDesempenoAndCodRecursoEvaluador(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno(), recursoEvaluacion.getCodRecursoEvaluador());
		recursoEvaluacion.setRespuestasCompetencias(respuestas);
		recursoEvaluacion.setEvaluadoresConcat(recursoService.getNombreApellidoById(recursoEvaluacion.getCodRecursoEvaluador()));
	
		listaEvaluadores.add(recursoEvaluacion);
		
		//promedioEvaluadores solo si esta cerrada el periodo de evaluacion;
		if (evaluacionDesempenoService.findById(codEvaluacion).getValEstatus().equals(2L)) {
			
			RecursoEvaluacion recursoEvaluadores = new RecursoEvaluacion();
			
			List<RespuestaCompetencia> respuestasEvaluadoresPromedio = respuestaCompetenciaService.getRespuestasPromedioEvaluadores(recursoEvaluacion.getCodRecurso(), recursoEvaluacion.getCodEvaluacionDesempeno(), 2L);
			
			int sumaResultados =0;
			int totalReactivos = respuestasEvaluadoresPromedio.size();
			
			for (RespuestaCompetencia respuestaCompetencia : respuestasEvaluadoresPromedio) {
				respuestaCompetencia.setCompetencia(competenciaService.findById(respuestaCompetencia.getCodCompetencias()));
				sumaResultados+=respuestaCompetencia.getValResultado();
			}
			
			recursoEvaluadores.setCodRecursoEvaluacion(9999L);
			recursoEvaluadores.setRespuestasCompetencias(respuestasEvaluadoresPromedio);
			recursoEvaluadores.setEvaluadoresConcat("Empresa");
			
			recursoEvaluadores.setDescEstatus("evaluación completa");
			
			DecimalFormat df = new DecimalFormat("#.#");
			if (totalReactivos == 0) {
				recursoEvaluadores.setValResultadoCompetencias(0);
			} else {
				recursoEvaluadores.setValResultadoCompetencias(Float.valueOf(df.format(sumaResultados/totalReactivos)));
			}
			
			
			
			listaEvaluadores.add(recursoEvaluadores);
		
		}
			
		model.put("listaEvaluadores", listaEvaluadores);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalDetalleRespuestas";
	}
	
	
	
	

}
