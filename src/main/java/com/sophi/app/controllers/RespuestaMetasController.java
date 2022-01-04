package com.sophi.app.controllers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.models.dao.RespuestaMeta;
import com.sophi.app.models.entity.EvaluacionDesempeno;
import com.sophi.app.models.service.IEvaluacionDesempenoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRespuestaMetasService;

@Controller
public class RespuestaMetasController {

	@Autowired
	private IRespuestaMetasService respuestaMetaService;
	
	@Autowired IEvaluacionDesempenoService evaluacionDesempenoService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@RequestMapping(value = "/getRespuestasMetas")
	public String getRespuestasMetas(@RequestParam("re") Long codRecuro,
			@RequestParam("ev") Long codEvaluacion,
			Map<String, Object> model) {
		List<RespuestaMeta> listaRespuestasMetas =  respuestaMetaService.findByCodRecursoAndCodEvaluacionDesempeno(codRecuro, codEvaluacion);
		EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
		String msj ="";
		if(ed.getValEstatus().equals(1L)) {
			msj="en ejecución";
		} else if (ed.getValEstatus().equals(2L)) {
			msj="cerrada";
		}
		
		int porcentajeTotal = 0;
		Float resultadoTotal = (float) 0.0;
		if(listaRespuestasMetas.size() > 0) {
			for (RespuestaMeta respuestaMeta : listaRespuestasMetas) {
				porcentajeTotal += respuestaMeta.getValPorcentaje();
				if(respuestaMeta.getValResultadoPorcentaje()!= null) {
					resultadoTotal += respuestaMeta.getValResultadoPorcentaje();
				}
			}
		} else {
			msj = "no iniciada";
		}
		String recursoDescripcion = recursoService.getNombreApellidoPuestoById(codRecuro);
		
		DecimalFormat df = new DecimalFormat("#.#");
		model.put("msj", msj);
		model.put("recursoDescripcion", recursoDescripcion);
		model.put("porcentajeTotal", porcentajeTotal);
		model.put("resultadoTotal", Float.valueOf(df.format(resultadoTotal)));
		model.put("listaRespuestasMetas", listaRespuestasMetas);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalDetalleMetas";
	}
	
	@RequestMapping(value = "/getRespuestasMetasEdicion")
	public String getRespuestasMetasEdicion(@RequestParam("re") Long codRecuro,
			@RequestParam("ev") Long codEvaluacion,
			Map<String, Object> model) {
		List<RespuestaMeta> listaRespuestasMetas =  respuestaMetaService.findByCodRecursoAndCodEvaluacionDesempeno(codRecuro, codEvaluacion);
		EvaluacionDesempeno ed = evaluacionDesempenoService.findById(codEvaluacion);
		String msj ="";
		if(ed.getValEstatus().equals(1L)) {
			msj="en ejecución";
		} else if (ed.getValEstatus().equals(2L)) {
			msj="cerrada";
		}
		
		int porcentajeTotal = 0;
		Float resultadoTotal = (float) 0.0;
		if(listaRespuestasMetas.size() > 0) {
			for (RespuestaMeta respuestaMeta : listaRespuestasMetas) {
				porcentajeTotal += respuestaMeta.getValPorcentaje();
				if(respuestaMeta.getValResultadoPorcentaje()!= null) {
					resultadoTotal += respuestaMeta.getValResultadoPorcentaje();
				}
			}
		} else {
			msj = "no iniciada";
		}
		
		String recursoDescripcion = recursoService.getNombreApellidoPuestoById(codRecuro);
		
		DecimalFormat df = new DecimalFormat("#.#");
		model.put("msj", msj);
		model.put("recursoDescripcion", recursoDescripcion);
		model.put("porcentajeTotal", porcentajeTotal);
		model.put("resultadoTotal", Float.valueOf(df.format(resultadoTotal)));
		model.put("listaRespuestasMetas", listaRespuestasMetas);
		model.put("re",codRecuro);
		model.put("ev",codEvaluacion);
		return "layout/plantilla-mis-evaluaciones :: fragmentModalDetalleMetasEdicion";
	}
	
	@RequestMapping(value = "/guardarRespuestaMeta")
	@ResponseBody
	public String guardarRespuestaMeta(@RequestParam("erm") Long codRespuestaMeta,
			@RequestParam("resultado") Long resultado,
			Map<String, Object> model) {
		String result="1";
		RespuestaMeta rm = respuestaMetaService.findById(codRespuestaMeta);
		rm.setValResultado(resultado);
		DecimalFormat df = new DecimalFormat("#.#");
		rm.setValResultadoPorcentaje(Float.valueOf(df.format((resultado * rm.getValPorcentaje()) / 100)));
		try {
			respuestaMetaService.guardar(rm);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	
	
	@RequestMapping(value = "/getPorcentajeMetasRecurso")
	@ResponseBody
	public Float getPorcentajeMetasRecurso(@RequestParam("r") Long codRecurso, @RequestParam("ce") Long codEvaluacion,
			Map<String, Object> model) {
		DecimalFormat df = new DecimalFormat("#.#");
		Float metas = respuestaMetaService.getPromedioMetasByCodRecursoAndCodEvaluacion(codRecurso, codEvaluacion);
	Float promedioMetas =  metas == null ? 0 :  Float.valueOf(df.format(metas));
	return promedioMetas;
	}
	
}
