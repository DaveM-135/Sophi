package com.sophi.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.models.entity.RecursoTrayectoriaNivel;
import com.sophi.app.models.service.IConocimientoService;
import com.sophi.app.models.service.IDetalleConocimientoProyectoService;
import com.sophi.app.models.service.IExperienciaService;
import com.sophi.app.models.service.IRecursoTrayectoriaNivelService;

@Controller
public class RecursoTrayectoriaNivelController {
	
	@Autowired
	private IRecursoTrayectoriaNivelService recursoTrayectoriaNivelService;
	
	@Autowired
	private IExperienciaService experienciaService;
	
	@Autowired
	private IDetalleConocimientoProyectoService detalleConocimientoProyectoService;
	
	@Autowired
	private IConocimientoService conocimientoService;
	
	@RequestMapping(value = "/verTrayectoriaNivel")
	public String verTrayectoriaNivel(@RequestParam Long codRecurso, Map<String, Object> model) {
		List<RecursoTrayectoriaNivel> listaTrayectoriaNivel = recursoTrayectoriaNivelService.findByCodRecurso(codRecurso);
		model.put("listaTrayectoriaNivel", listaTrayectoriaNivel);
		return "layout/trayectoria:: fragmentTrayectoriaNivel";
	}
	
	@RequestMapping(value="/formTrayectoriaNivel", method = RequestMethod.POST)
	@ResponseBody
	public String guardarTrayectoriaNivel(@Valid RecursoTrayectoriaNivel recursoTrayectoriaNivel, Model model) {
		recursoTrayectoriaNivelService.save(recursoTrayectoriaNivel);
		return "ok";
	}
//	
//	@RequestMapping(value = "/formTrayectoriaProyecto")
//	public String crearTrayectoriaProyecto(@RequestParam Long codRecurso, Map<String, Object> model) {
//		RecursoTrayectoriaProyecto recursoTrayectoriaProyecto = new RecursoTrayectoriaProyecto();
//		recursoTrayectoriaProyecto.setCodRecurso(codRecurso);
//		model.put("trayectoriaProyecto", recursoTrayectoriaProyecto);
//		model.put("listConocimientos", conocimientoService.findAll());
//		return "layout/trayectoria:: fragmentModalTrayectoria";
//	}
//	
//	
//	
	@RequestMapping(value = "/evaluarTrayectoriaNivel")
	public String evaluarTrayectoriaNivel(@RequestParam Long rtn, Map<String, Object> model) {
		RecursoTrayectoriaNivel recursoTrayectoriaNivel = recursoTrayectoriaNivelService.findById(rtn);
		model.put("trayectoriaNivel", recursoTrayectoriaNivel);
		model.put("descripcionConocimiento", recursoTrayectoriaNivel.getConocimiento().getDescConocimiento());
		model.put("listNivelesExperiencia", experienciaService.findAll());
		return "layout/trayectoria:: fragmentModalEvaluarNivel";
	}
//	
//	@RequestMapping(value = "/borrarTrayectoriaProyecto")
//	@ResponseBody
//	public String borrarTrayectoriaProyecto(@RequestParam Long rtp, Map<String, Object> model) {
//		recursoTrayectoriaProyectoService.delete(rtp);
//		return "ok";
//	}
	
	
	@RequestMapping(value = "/actualizaConocimientoPorRecurso")
	public String actualizaConocimientoPorRecurso(@RequestParam("r") Long codRecurso, Map<String, Object> model) {
		
		//Unicos
		List<Long> listaConocimientosUnicos = detalleConocimientoProyectoService.conocimientosDistintosPorRecurso(codRecurso);
		
		//actuales
		List<RecursoTrayectoriaNivel> listaTrayectoriaNivel = recursoTrayectoriaNivelService.findByCodRecurso(codRecurso);
		for (RecursoTrayectoriaNivel recursoTrayectoriaNivel : listaTrayectoriaNivel) {
			recursoTrayectoriaNivel.setBanderaBorrar(1L);
		}
		
		//iteracion con bandera 1
		for (RecursoTrayectoriaNivel recursoTrayectoriaNivel : listaTrayectoriaNivel) {
			Long codConocimientoActual = recursoTrayectoriaNivel.getCodConocimiento();
			for (Long codConocimiento : listaConocimientosUnicos) {
				if (codConocimientoActual.equals(codConocimiento)) {
					recursoTrayectoriaNivel.setBanderaBorrar(0L);
					listaConocimientosUnicos.remove(codConocimiento);
					break;
				}
			}
		}
		
		//borra los que tiene bandera 1
		for (RecursoTrayectoriaNivel recursoTrayectoriaNivel : listaTrayectoriaNivel) {
			if(recursoTrayectoriaNivel.getBanderaBorrar().equals(1L)) {
				recursoTrayectoriaNivelService.delete(recursoTrayectoriaNivel.getCodNivelTrayectoria());
			}
		}
		
		//crea nuevos y guarda
		for (Long codConocimiento : listaConocimientosUnicos) {
			RecursoTrayectoriaNivel rtn = new RecursoTrayectoriaNivel();
			rtn.setCodConocimiento(codConocimiento);
			rtn.setConocimiento(conocimientoService.findById(codConocimiento));
			rtn.setCodRecurso(codRecurso);
			rtn.setCodNivelExperiencia(1L);
			rtn.setPorcentajeExperiencia(0L);
			recursoTrayectoriaNivelService.save(rtn);
		}
		
		
		List<RecursoTrayectoriaNivel> listaTrayectoriaNivelFinal = recursoTrayectoriaNivelService.findByCodRecurso(codRecurso);
		model.put("listaTrayectoriaNivel", listaTrayectoriaNivelFinal);
		return "layout/trayectoria:: fragmentTrayectoriaNivel";
	}
	
	

	
}
