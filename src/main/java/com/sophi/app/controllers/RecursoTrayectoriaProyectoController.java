package com.sophi.app.controllers;

import java.util.ArrayList;
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

import com.sophi.app.models.entity.DetalleConocimientosProyecto;
import com.sophi.app.models.entity.RecursoTrayectoriaProyecto;
import com.sophi.app.models.service.IConocimientoService;
import com.sophi.app.models.service.IDetalleConocimientoProyectoService;
import com.sophi.app.models.service.IRecursoTrayectoriaProyectoService;

@Controller
public class RecursoTrayectoriaProyectoController {
	
	@Autowired
	private IRecursoTrayectoriaProyectoService recursoTrayectoriaProyectoService;
	
	@Autowired
	private IConocimientoService conocimientoService;
	
	@Autowired
	private IDetalleConocimientoProyectoService detalleConocimientoProyectoService;
	
	@RequestMapping(value = "/verTrayectoriaProyecto")
	public String verTrayectoriaProyecto(@RequestParam Long codRecurso, Map<String, Object> model) {
		List<RecursoTrayectoriaProyecto> listaTrayectoriaProyectos = recursoTrayectoriaProyectoService.findByCodRecurso(codRecurso);
		model.put("listaTrayectoriaProyectos", listaTrayectoriaProyectos);
		return "layout/trayectoria:: fragmentTrayectoriaProyecto";
	}
	
	@RequestMapping(value="/formTrayectoriaProyecto", method = RequestMethod.POST)
	@ResponseBody
	public String guardarTrayectoriaProyecto(@Valid RecursoTrayectoriaProyecto recursoTrayectoriaProyecto, @RequestParam("conocimientos") List<Long> conocimientos, Model model) {
		recursoTrayectoriaProyectoService.save(recursoTrayectoriaProyecto);
		detalleConocimientoProyectoService.borrarByCodTrayectoriaProyecto(recursoTrayectoriaProyecto.getCodTrayectoriaProyecto());
		List<DetalleConocimientosProyecto> nuevaListaDetalleConocimientoProyecto = new ArrayList<DetalleConocimientosProyecto>();
		
		for (Long c : conocimientos) {
			DetalleConocimientosProyecto dcp = new DetalleConocimientosProyecto();
			dcp.setRecursoTrayectoriaProyecto(recursoTrayectoriaProyecto);
			dcp.setConocimiento(conocimientoService.findById(c));
			nuevaListaDetalleConocimientoProyecto.add(dcp);
		}
		detalleConocimientoProyectoService.saveAll(nuevaListaDetalleConocimientoProyecto);
		return "ok";
	}
	
	@RequestMapping(value = "/formTrayectoriaProyecto")
	public String crearTrayectoriaProyecto(@RequestParam Long codRecurso, Map<String, Object> model) {
		RecursoTrayectoriaProyecto recursoTrayectoriaProyecto = new RecursoTrayectoriaProyecto();
		recursoTrayectoriaProyecto.setCodRecurso(codRecurso);
		model.put("trayectoriaProyecto", recursoTrayectoriaProyecto);
		model.put("listConocimientos", conocimientoService.findAll());
		
//		List<DetalleConocimientosProyecto> conocimientosProyecto = detalleConocimientoProyectoService.findByCodTrayectoriaProyecto(rtp);

		List<Long> conocimientos = new ArrayList<>();
		
//		for (DetalleConocimientosProyecto detalleConocimientosProyecto : conocimientosProyecto) {
//			conocimientos.add(detalleConocimientosProyecto.getConocimiento().getCodConocimiento());
//		}
//		
		
		model.put("listActualConocimientos", conocimientos);
		
		return "layout/trayectoria:: fragmentModalTrayectoria";
	}
	
	
	
	@RequestMapping(value = "/editTrayectoriaProyecto")
	public String editarTrayectoriaProyecto(@RequestParam Long rtp, Map<String, Object> model) {
		RecursoTrayectoriaProyecto recursoTrayectoriaProyecto = recursoTrayectoriaProyectoService.findById(rtp);
		model.put("trayectoriaProyecto", recursoTrayectoriaProyecto);
		model.put("listConocimientos", conocimientoService.findAll());
		
		List<DetalleConocimientosProyecto> conocimientosProyecto = detalleConocimientoProyectoService.findByCodTrayectoriaProyecto(rtp);

		List<Long> conocimientos = new ArrayList<>();
		
		for (DetalleConocimientosProyecto detalleConocimientosProyecto : conocimientosProyecto) {
			conocimientos.add(detalleConocimientosProyecto.getConocimiento().getCodConocimiento());
		}
		
		
		model.put("listActualConocimientos", conocimientos);
		
		return "layout/trayectoria:: fragmentModalTrayectoria";
	}
	
	@RequestMapping(value = "/borrarTrayectoriaProyecto")
	@ResponseBody
	public String borrarTrayectoriaProyecto(@RequestParam Long rtp, Map<String, Object> model) {
		recursoTrayectoriaProyectoService.delete(rtp);
		detalleConocimientoProyectoService.borrarByCodTrayectoriaProyecto(rtp);
		return "ok";
	}

	
}
