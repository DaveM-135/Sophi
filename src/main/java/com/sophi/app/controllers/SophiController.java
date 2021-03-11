package com.sophi.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sophi.app.Utiles;
import com.sophi.app.models.entity.HistRespuestaAux;
import com.sophi.app.models.entity.HistoricoRespuestaClima;
import com.sophi.app.models.entity.PreguntaRespuestaClima;
import com.sophi.app.models.service.IHistoricoRespuestaClimaService;
import com.sophi.app.models.service.IPreguntaClimaService;
import com.sophi.app.models.service.IPreguntaRespuestaClimaService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRespuestaFlashService;

import javassist.bytecode.analysis.Util;

@Controller
public class SophiController {
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IPreguntaRespuestaClimaService preguntaRespuestaClimaService;
	
	@Autowired
	private IPreguntaClimaService preguntaClimaService;
	
	@Autowired
	private IRespuestaFlashService respuestaFlashService;
	
	@Autowired
	private IHistoricoRespuestaClimaService historicoRespuestaClimaService;
	
	@GetMapping({"/index","/","","/home"})
	public String index(Map<String, Object> map) {
		
		int fechaParametro = new Utiles().getFechaViernes();
		long total = 0;
		
		List<HistRespuestaAux> resultados = new ArrayList<HistRespuestaAux>();
		resultados = historicoRespuestaClimaService.resultados(fechaParametro);
		
		for (HistRespuestaAux histRespuestaAux : resultados) {
			total += histRespuestaAux.getTotal();
			histRespuestaAux.setValImagenRespuesta(respuestaFlashService.findByCodRespuesta(histRespuestaAux.getIdPreguntaRespuesta()).getRutaImagen());
//			histRespuestaAux.setValImagenRespuesta(preguntaRespuestaClimaService.findOne(histRespuestaAux.getIdPreguntaRespuesta()).getValImagenRespuesta());
			
		}
		
		for (HistRespuestaAux histRespuestaAux : resultados) {
			histRespuestaAux.setPorcentaje(histRespuestaAux.getTotal() * 100 / total);
		}
		
		int participacion = 0;
		participacion = historicoRespuestaClimaService.totalParticipacion(fechaParametro);

		int totalActivos = 0;
		totalActivos = recursoService.findRecursosActivos().size();
		
		
		map.put("respuestasFlash",resultados);
		map.put("participacion", participacion);
		map.put("totalActivos", totalActivos);
		
		return "index";
	}
	
	@GetMapping({"/accessDenied"})
	public String accessDenied(Map<String, Object> map) {
		return "accessDenied";
	}
	
	
	
	@GetMapping(value="/datosRecursoLogin/{login}")
	public String cargarDatosRecursoLogin(@PathVariable String login, Model model){
		recursoService.findByDescCorreoElectronico(login);
		model.addAttribute("recursoSesion", recursoService.findByDescCorreoElectronico(login));
		return "layout/layout :: dataSesion";
	}
	
	
	@GetMapping(value="/datosOpcionesRecursoLogin/{login}")
	public String cargarDatosOpcionesRecursoLogin(@PathVariable String login, Model model){
		recursoService.findByDescCorreoElectronico(login);
		model.addAttribute("recursoSesion", recursoService.findByDescCorreoElectronico(login));
		return "layout/layout :: dataSesionOption";
	}
	
	@GetMapping(value="/mstrForecast")
	public String mstrForecast(Model model) {
		return "mstr/mstrForecast";
	}
	
	

}
