package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sophi.app.Utiles;
import com.sophi.app.models.entity.HistRespuestaAux;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IHistoricoRespuestaClimaService;
import com.sophi.app.models.service.IPreguntaClimaService;
import com.sophi.app.models.service.IPreguntaRespuestaClimaService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRespuestaFlashService;

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
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@Autowired
	private IActividadService actividadService;

	Date fechaActual = new Utiles().getFechaActual();
	
	@GetMapping({"/index","/","","/home"})
	public String index(Map<String, Object> map) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
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
		
		Long codRecurso = recursoService.findByDescCorreoElectronico(auth.getName()).getCodRecurso();
		List<Long> proyectoListId = new ArrayList<Long>();
		HashMap<Long, String> proyectoList = new HashMap<Long, String>(); 
		proyectoListId = actividadService.findListaProyectoByRecurso(codRecurso);
		if (proyectoListId.size() > 0) {
			for (Long id : proyectoListId) {
				Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 2L);
				if(proyecto == null) {
					proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 1L);
					if(proyecto != null){
						if(proyecto.getFecIncioProyecto() == null || proyecto.getFecFinProyecto() == null){
							proyectoList.put(id, proyecto.getDescProyecto());
						} else if(!fechaActual.before(proyecto.getFecIncioProyecto()) || !fechaActual.after(proyecto.getFecFinProyecto())){
							proyectoList.put(id, proyecto.getDescProyecto());
						}
					}
				} else if(proyecto != null){
					if(proyecto.getFecIncioProyecto() == null || proyecto.getFecFinProyecto() == null){
						proyectoList.put(id, proyecto.getDescProyecto());
					} else if(!fechaActual.before(proyecto.getFecIncioProyecto()) || !fechaActual.after(proyecto.getFecFinProyecto())){
						proyectoList.put(id, proyecto.getDescProyecto());
					}
				}
			}
		} 
		
		List<ProyectoRecurso> proyectosRecurso = new ArrayList<ProyectoRecurso>();
		proyectosRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(codRecurso);
		if (proyectosRecurso.size() > 0) {
			for (ProyectoRecurso proyectoRecurso : proyectosRecurso) {
				Long idProyect = proyectoRecurso.getProyectoRecursoId().getCodProyecto();
				Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 2L);
				if (proyecto == null) {
					proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 1L);
					if (proyecto != null) {
						if(proyecto.getFecIncioProyecto() == null || proyecto.getFecFinProyecto() == null){
							proyectoList.put(idProyect, proyecto.getDescProyecto());
						} else if(!fechaActual.before(proyecto.getFecIncioProyecto()) || !fechaActual.after(proyecto.getFecFinProyecto())){
							proyectoList.put(idProyect, proyecto.getDescProyecto());
						}
					}
				} else if (proyecto != null) {
					if(proyecto.getFecIncioProyecto() == null || proyecto.getFecFinProyecto() == null){
						proyectoList.put(idProyect, proyecto.getDescProyecto());
					} else if(!fechaActual.before(proyecto.getFecIncioProyecto()) || !fechaActual.after(proyecto.getFecFinProyecto())){
						proyectoList.put(idProyect, proyecto.getDescProyecto());
					}
				}
				
			}
		}
		
		
		Proyecto proyecto = proyectoService.findByCodProyecto(1L);
		proyectoList.put(proyecto.getCodProyecto(),proyecto.getDescProyecto());
		map.put("proyectoList", proyectoList);
		
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
