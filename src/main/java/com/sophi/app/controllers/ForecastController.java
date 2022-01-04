package com.sophi.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.Utiles;
import com.sophi.app.models.entity.ClasificacionForecast;
import com.sophi.app.models.entity.DetalleForecast;
import com.sophi.app.models.entity.MesHabil;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.ResumenForecastGeneral;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IClasificacionForecastService;
import com.sophi.app.models.service.IDetalleForecastService;
import com.sophi.app.models.service.IMesHabilService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

@Controller
public class ForecastController {
	
	@Autowired
	private IDetalleForecastService detalleForecastService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@Autowired
	private IMesHabilService mesHabilService;
	
	@Autowired
	private IClasificacionForecastService clasificacionForecastService;
	
	@RequestMapping(value = "/miForecast/{mail}", method = RequestMethod.GET)
	public String miForecast(@PathVariable(value = "mail") String mail, Model model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
		
		List<Long> proyectoListId = new ArrayList<Long>();
		HashMap<Long, String> proyectoList = new HashMap<Long, String>(); 
		proyectoListId = actividadService.findListaProyectoByRecurso(recurso.getCodRecurso());
		if (proyectoListId.size() > 0) {
			for (Long id : proyectoListId) {
				Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 2L);
				if(proyecto == null) {
					proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 1L);
					if(proyecto != null){
						proyectoList.put(id, proyecto.getDescProyecto());
					}
				} else if(proyecto != null){
					proyectoList.put(id, proyecto.getDescProyecto());
				}
			}
		} 
		
		List<ProyectoRecurso> proyectosRecurso = new ArrayList<ProyectoRecurso>();
		proyectosRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(recurso.getCodRecurso());
		if (proyectosRecurso.size() > 0) {
			for (ProyectoRecurso proyectoRecurso : proyectosRecurso) {
				Long idProyect = proyectoRecurso.getProyectoRecursoId().getCodProyecto();
				Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 2L);
				if (proyecto == null) {
					proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 1L);
					if (proyecto != null) {
						proyectoList.put(idProyect,proyecto.getDescProyecto());
					}
				} else if (proyecto != null) {
					proyectoList.put(idProyect,proyecto.getDescProyecto());
				}
				
			}
		}
		
		
		Proyecto proyecto = proyectoService.findByCodProyecto(1L);
		proyectoList.put(proyecto.getCodProyecto(),proyecto.getDescProyecto());
		model.addAttribute("proyectos", proyectoList);
		
		Utiles utiles = new Utiles();
		
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(utiles.getFechaActual());
		fecha.add(Calendar.MONTH, -3);
        String actual = "";
		List<String> listaPeriodos = new ArrayList<>(); 
        //Aqui va valor dinamico para calculo de meses
        for (int i = 0; i < 6; i++) {
        	fecha.add(Calendar.MONTH, 1);
        	
        	String anio = String.valueOf(fecha.get(Calendar.YEAR));
            String mes = String.valueOf(fecha.get(Calendar.MONTH) + 1);
            if (mes.length() == 1) {
            	mes = "0" + mes;
            }
            if(i==3) {
            	actual = anio +  mes;
            }
            listaPeriodos.add(anio +  mes);
		}
        
        MesHabil mesHabil = mesHabilService.findById(Long.parseLong(actual));
        model.addAttribute("horasHabiles",mesHabil.getValHorasHabiles());
        model.addAttribute("horasInhabiles", mesHabil.getValHorasFestivos());
        model.addAttribute("listaPeriodos", listaPeriodos);
        model.addAttribute("codRecurso", recurso.getCodRecurso());
        model.addAttribute("actual", actual);
        
        
		
		
		List<String> listaClasificacionPresupuesto = clasificacionForecastService.findUniqueDescClasificacionPresupuesto();
		model.addAttribute("listaClasificacionPresupuesto", listaClasificacionPresupuesto);
		
		List<ClasificacionForecast> listaClasificacion = clasificacionForecastService.findByDescClasificacionPresupuesto(listaClasificacionPresupuesto.get(0)) ;
		model.addAttribute("listaClasificacion", listaClasificacion);
		
		List<DetalleForecast> listaForecast = detalleForecastService.findByRecursoAndMesHabil(recurso, mesHabil);
		model.addAttribute("listaForecast", listaForecast);
		
		return "forecastResumen";
	}
	
	
	@RequestMapping(value="/horasPeriodo", method = RequestMethod.GET)
	@ResponseBody
	public MesHabil horasTotalSemana(@RequestParam(value = "periodo") Long periodo, Model model) {
        	MesHabil mesHabil = mesHabilService.findById(periodo);
		return mesHabil;
	}
	
	@RequestMapping(value="/getNombreRecurso", method = RequestMethod.GET)
	@ResponseBody
	public String getNombreRecurso(@RequestParam(value = "codRecurso") Long codRecurso, Model model) {
        	Recurso recurso = recursoService.findOne(codRecurso);
		return recurso.getDescRecurso();
	}
	
	@RequestMapping(value = "/guardaForecast", method = RequestMethod.GET)
	@ResponseBody
	public String guardaForecast(@RequestParam(value = "codRecurso") Long codRecurso,
			@RequestParam(value = "proyecto") Long codProyecto,
			@RequestParam(value = "codForecast") String codForecast,
			@RequestParam(value = "clasificacion") Long codClasificacion,
			@RequestParam(value = "inicioActividad") String inicioActividad,
			@RequestParam(value = "finActividad") String finActividad,
			@RequestParam(value = "horasPlaneadas") float horasPlaneadas,
			@RequestParam(value = "mesHabil") Long mesHabil
			, Model model) {
		
		DetalleForecast forecast;
		if (codForecast.length() == 0) {
			forecast = new DetalleForecast();
		}else {
			forecast = detalleForecastService.findById(Long.parseLong(codForecast));
		}
		
		Recurso recurso = recursoService.findOne(codRecurso);
		ClasificacionForecast clasificacion = clasificacionForecastService.findByCodClasificacion(codClasificacion);
		Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
		MesHabil periodo =  mesHabilService.findById(mesHabil);
		forecast.setRecurso(recurso);
		forecast.setProyecto(proyecto);
		forecast.setClasificacionForecast(clasificacion);
		forecast.setMesHabil(periodo);
		forecast.setValHoras(horasPlaneadas);
		try {
			forecast.setFecInicioActividad(new SimpleDateFormat("yyyy-MM-dd").parse(inicioActividad));
			forecast.setFecFinActividad(new SimpleDateFormat("yyyy-MM-dd").parse(finActividad));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		try {
			detalleForecastService.save(forecast);
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "1";
		}
		
	}
	
	@RequestMapping(value = "/actualizaListadoForecast/{codRecurso}/{codPeriodo}", method = RequestMethod.GET)
	public String actualizaListadoForecast(@PathVariable(value="codRecurso") Long codRecurso, @PathVariable(value="codPeriodo") Long codPeriodo, Model model) {
		Recurso recurso = recursoService.findOne(codRecurso);
		MesHabil mesHabil = mesHabilService.findById(codPeriodo);
		List<DetalleForecast> listaForecast = detalleForecastService.findByRecursoAndMesHabil(recurso, mesHabil);
		model.addAttribute("listaForecast", listaForecast);
		return "layout/forecast :: listaForecast";
	}
	
	@RequestMapping(value = "/sumaHorasForecast", method = RequestMethod.GET)
	@ResponseBody
	public float sumaHorasForecast(@RequestParam(value = "codRecurso") Long codRecurso,
			@RequestParam(value = "mesHabil") Long mesHabil, Model model) {
		float totalHorasForecast = 0;
		Recurso recurso = recursoService.findOne(codRecurso);
		MesHabil mes = mesHabilService.findById(mesHabil);
		List<DetalleForecast> listaForecast = detalleForecastService.findByRecursoAndMesHabil(recurso, mes);
		for (DetalleForecast detalleForecast : listaForecast) {
			totalHorasForecast += detalleForecast.getValHoras();
		}
		return totalHorasForecast;
	}
	
	
	@RequestMapping(value = "/borrarForecast", method = RequestMethod.GET)
	@ResponseBody
	public String borrarForecast(@RequestParam(value = "codForecast") Long codForecast, Model model) {
		try {
			detalleForecastService.delete(codForecast);
			return "0";
		} catch (Exception e) {
			return "1";
		}
	}
	
	
	@RequestMapping(value = "/editarForecast", method = RequestMethod.GET)
	@ResponseBody
	public List<String> editarForecast(@RequestParam(value = "codForecast") Long codForecast, Model model) {
		DetalleForecast detalleForecast = detalleForecastService.findById(codForecast);
		List<String> listaDetalle = new ArrayList<String>();
		listaDetalle.add(detalleForecast.getCodForecast().toString());
		listaDetalle.add(detalleForecast.getValHoras().toString());
		listaDetalle.add(detalleForecast.getFecInicioActividad().toString());
		listaDetalle.add(detalleForecast.getFecFinActividad().toString());
		listaDetalle.add(detalleForecast.getProyecto().getCodProyecto().toString());
		listaDetalle.add(detalleForecast.getClasificacionForecast().getDescClasificacion());
		listaDetalle.add(detalleForecast.getClasificacionForecast().getCodClasificacionForecast().toString());
		listaDetalle.add(detalleForecast.getMesHabil().getCodMes().toString());
		return listaDetalle;
	}
	
	
	@RequestMapping(value = "/resumenForecast", method = RequestMethod.GET)
	public String resumenForecast(Model model) {
		List<Recurso> listaRecursosConsultores = new ArrayList<>();
		List<Recurso> listaRecursosConsultoresTMP = new ArrayList<>();
		
		//Recursos consultores cod = 1
		listaRecursosConsultoresTMP = recursoService.findByCodAreaRecurso(1L);
		
		for (Recurso recurso : listaRecursosConsultoresTMP) {
			if(recurso.getValActivo().equals(1L)) {
				listaRecursosConsultores.add(recurso);
			}
		}
		
		Utiles utiles = new Utiles();
		
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(utiles.getFechaActual());
		
		String[] listaMeses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		
		List<ResumenForecastGeneral> listaPeriodo = new ArrayList<>();
		
        
		//Aqui va valor dinamico para calculo de meses
        for (int i = 0; i < 3; i++) {
        	fecha.add(Calendar.MONTH, 1);
        	
        	String anio = String.valueOf(fecha.get(Calendar.YEAR));
            String mes = String.valueOf(fecha.get(Calendar.MONTH) + 1);
            if (mes.length() == 1) {
            	mes = "0" + mes;
            }
            MesHabil mesHabil = mesHabilService.findById(Long.parseLong(anio +  mes));
            String nombreMes = listaMeses[fecha.get(Calendar.MONTH)];
            ResumenForecastGeneral rfg = new ResumenForecastGeneral();
            rfg.setNombreMes(nombreMes);
            rfg.setMesHabil(mesHabil);
            rfg.setTotalHorasHabiles(mesHabil.getValHorasFestivos() + mesHabil.getValHorasHabiles());
            listaPeriodo.add(rfg);
		}
        
        for (Recurso recurso : listaRecursosConsultores) {
        	List<Float> totales = new ArrayList<>();
        	for (ResumenForecastGeneral rfg : listaPeriodo) {
        	    float totalHorasForecast = 0;
    			List<DetalleForecast> listaForecast = detalleForecastService.findByRecursoAndMesHabil(recurso,rfg.getMesHabil());
    			for (DetalleForecast detalleForecast : listaForecast) {
    				totalHorasForecast += detalleForecast.getValHoras();
    			}
    			totales.add(totalHorasForecast);
        	}
        	
			recurso.setTotalHorasForecast(totales);
		}		
		
		
		 model.addAttribute("listaPeriodos", listaPeriodo);
		 model.addAttribute("listaRecursosConsultores", listaRecursosConsultores);
		 
		 return "forecastResumentGeneral";
		
	}
	
	
	@RequestMapping(value = "detalleForecastRecurso/{codRecurso}/{mes1}/{mes2}/{mes3}", method = RequestMethod.GET)
	public String resumenForecast(@PathVariable(value="codRecurso") Long codRecurso, 
			@PathVariable(value="mes1") Long mes1,
			@PathVariable(value="mes2") Long mes2,
			@PathVariable(value="mes3") Long mes3,
			Model model) {
			
			Recurso recurso = recursoService.findOne(codRecurso);
			
			List<MesHabil> listaMesesHabiles = new ArrayList<>();
			
			MesHabil mesHabil1 = mesHabilService.findById(mes1);
			List<DetalleForecast> listaForecast1 = detalleForecastService.findByRecursoAndMesHabil(recurso, mesHabil1);
			MesHabil mesHabil2 = mesHabilService.findById(mes2);
			List<DetalleForecast> listaForecast2 = detalleForecastService.findByRecursoAndMesHabil(recurso, mesHabil2);
			MesHabil mesHabil3 = mesHabilService.findById(mes3);
			List<DetalleForecast> listaForecast3 = detalleForecastService.findByRecursoAndMesHabil(recurso, mesHabil3);
			
			listaMesesHabiles.add(mesHabil1);
			listaMesesHabiles.add(mesHabil2);
			listaMesesHabiles.add(mesHabil3);
			model.addAttribute("listaMesesHabiles", listaMesesHabiles);
			
			List<DetalleForecast> listaCompletaForecast = new ArrayList<>();
			
			listaCompletaForecast.addAll(listaForecast1);
			listaCompletaForecast.addAll(listaForecast2);
			listaCompletaForecast.addAll(listaForecast3);
			
			
			model.addAttribute("listaForecast", listaCompletaForecast);
			
			
			List<Long> proyectoListId = new ArrayList<Long>();
			HashMap<Long, String> proyectoList = new HashMap<Long, String>(); 
			proyectoListId = actividadService.findListaProyectoByRecurso(recurso.getCodRecurso());
			if (proyectoListId.size() > 0) {
				for (Long id : proyectoListId) {
					Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 2L);
					if(proyecto == null) {
						proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(id, 1L);
						if(proyecto != null){
							proyectoList.put(id, proyecto.getDescProyecto());
						}
					} else if(proyecto != null){
						proyectoList.put(id, proyecto.getDescProyecto());
					}
				}
			} 
			
			List<ProyectoRecurso> proyectosRecurso = new ArrayList<ProyectoRecurso>();
			proyectosRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(recurso.getCodRecurso());
			if (proyectosRecurso.size() > 0) {
				for (ProyectoRecurso proyectoRecurso : proyectosRecurso) {
					Long idProyect = proyectoRecurso.getProyectoRecursoId().getCodProyecto();
					Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 2L);
					if (proyecto == null) {
						proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(idProyect, 1L);
						if (proyecto != null) {
							proyectoList.put(idProyect,proyecto.getDescProyecto());
						}
					} else if (proyecto != null) {
						proyectoList.put(idProyect,proyecto.getDescProyecto());
					}
					
				}
			}
			
			
			Proyecto proyecto = proyectoService.findByCodProyecto(1L);
			proyectoList.put(proyecto.getCodProyecto(),proyecto.getDescProyecto());
			model.addAttribute("proyectos", proyectoList);
			
			List<String> listaClasificacionPresupuesto = clasificacionForecastService.findUniqueDescClasificacionPresupuesto();
			model.addAttribute("listaClasificacionPresupuesto", listaClasificacionPresupuesto);
			
			List<ClasificacionForecast> listaClasificacion = clasificacionForecastService.findByDescClasificacionPresupuesto(listaClasificacionPresupuesto.get(0)) ;
			model.addAttribute("listaClasificacion", listaClasificacion);
			
			
			return "layout/forecast :: listaForecastRecurso";
		}
	
	
	@RequestMapping(value = "/actualizaClasificacion/{clasificacion}", method = RequestMethod.GET)
	public String refreshItem(@PathVariable(value = "clasificacion") String clasificacion, Model model) {
		
		List<ClasificacionForecast> listaClasificacion = clasificacionForecastService.findByDescClasificacionPresupuesto(clasificacion) ;
		model.addAttribute("listaClasificacion", listaClasificacion);

	    return "layout/forecast :: clasifpresupuesto";
	}
	
	
	

	
	

}
