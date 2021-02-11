package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.models.entity.Actividad;
import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Subtarea;
import com.sophi.app.models.entity.Tarea;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.ICapHoraService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.ISubtareaService;
import com.sophi.app.models.service.ITareaService;

@Controller
@SessionAttributes("capHora")
public class CapHorasController {
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private ITareaService tareaService;
	
	@Autowired
	private ISubtareaService subtareaService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private ICapHoraService capHoraService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	final String PREVENTA = "> Preventa (default)";
	final String OTRA = "> Catálogo de actividades";
	
	Long codproyecto, codproyecto_u;
	Long codActividad, codActividad_u;
	String descComentarioDetalle, descComentarioDetalle_u;
	float valDuracionReportada, valDuracionReportada_u;
	
	@GetMapping("/capHoras/{email}")
	public String capHoras(@PathVariable(value="email") String email, Model model) {
		Long codRecurso = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
		model.addAttribute("titulo", "Captura de horas");
		List<Long> proyectoListId = new ArrayList<Long>();
		HashMap<Long, String> proyectoList = new HashMap<Long, String>(); 
		proyectoListId = actividadService.findListaProyectoByRecurso(codRecurso);
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
		proyectosRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(codRecurso);
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
		model.addAttribute("proyectoList", proyectoList);
		model.addAttribute("codRecurso", codRecurso);
		return "formCapHoras";
	}
	
	@GetMapping(value="/cargarActividadPrimaria/{codRecurso}/{codProyecto}")
	public String cargarActividadPrimaria(@PathVariable Long codRecurso, @PathVariable Long codProyecto, Model model){
		if(codProyecto.equals(1L)) {
			Tarea tarea = tareaService.findOne(1L);
			HashMap<Long, String> actividadesPrimariasList = new HashMap<Long, String>(); 
			actividadesPrimariasList.put(tarea.getCodTarea(), tarea.getDescTarea());
			model.addAttribute("actividadesPrimariasNoPlan", actividadesPrimariasList);
			return "layout/capHora :: listActividadesPrimariasNoPlan";
		}else {
			List<String> listaActividadesPrimarias = new ArrayList<String>();
			Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
			if(!proyecto.getCodEstatusProyecto().equals(2L)) {
				listaActividadesPrimarias.add(PREVENTA);
			}
			listaActividadesPrimarias.addAll(actividadService.findListaActividadesPrimariasByRecursoProyecto(codRecurso, codProyecto));
			listaActividadesPrimarias.add(OTRA);
			model.addAttribute("actividadesPrimariasList", listaActividadesPrimarias);
			return "layout/capHora :: listActividadesPrimarias";
		}
	}
	
	@GetMapping(value="/cargarActividadPrimariaEdit/{codRecurso}/{codProyecto}")
	public String cargarActividadPrimariaEdit(@PathVariable Long codRecurso, @PathVariable Long codProyecto, Model model){
		if(codProyecto.equals(1L)) {
			Tarea tarea = tareaService.findOne(1L);
			HashMap<Long, String> actividadesPrimariasList = new HashMap<Long, String>(); 
			actividadesPrimariasList.put(tarea.getCodTarea(), tarea.getDescTarea());
			model.addAttribute("actividadesPrimariasNoPlan", actividadesPrimariasList);
			return "layout/capHora :: listActividadesPrimariasNoPlanEdit";
		}else {
			List<String> listaActividadesPrimarias = new ArrayList<String>();
			Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
			if(!proyecto.getCodEstatusProyecto().equals(2L)) {
				listaActividadesPrimarias.add(PREVENTA);
			}
			listaActividadesPrimarias.addAll(actividadService.findListaActividadesPrimariasByRecursoProyecto(codRecurso, codProyecto));
			listaActividadesPrimarias.add(OTRA);
			model.addAttribute("actividadesPrimariasList", listaActividadesPrimarias);
			return "layout/capHora :: listActividadesPrimariasEdit";
		}
	}
	
	
	
	@GetMapping(value="/cargarActividadSecundaria/{codRecurso}/{codProyecto}/{descPrimaria}")
	public String cargarActividadSecundaria(@PathVariable Long codRecurso, @PathVariable Long codProyecto,@PathVariable String descPrimaria,  Model model){
		if(codProyecto.equals(1L)) {
			List<Subtarea> actividadesSecundariasList = subtareaService.findByCodTarea(1L);
			model.addAttribute("actividadesSecundariasNoPlan", actividadesSecundariasList);
			return "layout/capHora :: listActividadesSecundariasNoPlan";
		} else if (descPrimaria.equalsIgnoreCase(PREVENTA)) {
			System.out.println("entra en preventa");
			model.addAttribute("actividadesSecundariasListFuera", subtareaService.findByCodTarea(2L));
			return "layout/capHora :: listActividadesSecundariasFuera";
		} else if (descPrimaria.equalsIgnoreCase(OTRA)){
			System.out.println("entra en otra fuera de plan");
//			model.addAttribute("actividadesSecundariasListFuera", subtareaService.findFueraDePlan());
			model.addAttribute("actividadesSecundariasListFuera", tareaService.findTareaFueraPlan());
			return "layout/capHora :: listActividadesSecundariasFueraOtra";
		} else {
			model.addAttribute("actividadesSecundariasList", actividadService.findListaActividadesByRecursoProyectoPrimaria(codRecurso, codProyecto, descPrimaria));
			return "layout/capHora :: listActividadesSecundarias";
		}
	}
	
	@GetMapping(value="/cargarActividadSecundariaEdit/{codRecurso}/{codProyecto}/{descPrimaria}")
	public String cargarActividadSecundariaEdit(@PathVariable Long codRecurso, @PathVariable Long codProyecto,@PathVariable String descPrimaria,  Model model){
		if(codProyecto.equals(1L)) {
			List<Subtarea> actividadesSecundariasList = subtareaService.findByCodTarea(1L);
			model.addAttribute("actividadesSecundariasNoPlan", actividadesSecundariasList);
			return "layout/capHora :: listActividadesSecundariasNoPlanEdit";
		} else if (descPrimaria.equalsIgnoreCase(PREVENTA)) {
			System.out.println("entra en preventa");
			model.addAttribute("actividadesSecundariasListFuera", subtareaService.findByCodTarea(2L));
			return "layout/capHora :: listActividadesSecundariasFueraEdit";
		} else if (descPrimaria.equalsIgnoreCase(OTRA)){
			System.out.println("entra en otra fuera de plan");
//			model.addAttribute("actividadesSecundariasListFuera", subtareaService.findFueraDePlan());
			model.addAttribute("actividadesSecundariasListFuera", tareaService.findTareaFueraPlan());
			return "layout/capHora :: listActividadesSecundariasFueraOtraEdit";
		} else {
			model.addAttribute("actividadesSecundariasList", actividadService.findListaActividadesByRecursoProyectoPrimaria(codRecurso, codProyecto, descPrimaria));
			return "layout/capHora :: listActividadesSecundariasEdit";
		}
	}
	
	
	@GetMapping(value="/cargarDetActividad/{codActividad}/{fecCaptura}")
	public String cargarDetActividad(@PathVariable Long codActividad, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date fecCaptura, Model model){
		Actividad actividad = new Actividad();
		actividad = actividadService.findOne(codActividad);
//		CapHoraId capHoraId = new CapHoraId(actividad.getCodActividad(), actividad.getCodRecurso(), actividad.getCodProyecto(), actividad.getCodCliente(), actividad.getCodEstatusProyecto());
		CapHora capHora = new CapHora();
//		capHora.setId(capHoraId);
		capHora.setCodActividad(actividad.getCodActividad());
		capHora.setCodRecurso(actividad.getCodRecurso());
		capHora.setCodProyecto(actividad.getCodProyecto());
		capHora.setCodCliente(actividad.getCodCliente());
		capHora.setCodEstatusProyecto(actividad.getCodEstatusProyecto());
		capHora.setValNuevaActividad(0L);
		capHora.setFecInicioActividad(fecCaptura);
		model.addAttribute("capHora", capHora);
		return "layout/capHora :: detActividades";
	}
	
	@GetMapping(value="/cargarDetActividadNoPlan/{codActividad}/{fecCaptura}/{codRecurso}")
	public String cargarDetActividadNoPlan(@PathVariable Long codActividad, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date fecCaptura,@PathVariable Long codRecurso, Model model){
		System.out.println(codActividad +" "+codRecurso +" "+fecCaptura);
//		CapHoraId capHoraId = new CapHoraId(codActividad, codRecurso, 1L, 1L, 2L);
		CapHora capHora = new CapHora();
		capHora.setCodActividad(codActividad);
		capHora.setCodRecurso(codRecurso);
		capHora.setCodProyecto(1L);
		capHora.setCodCliente(1L);
		capHora.setCodEstatusProyecto(2L);
//		capHora.setId(capHoraId);
		capHora.setValNuevaActividad(1L);
		capHora.setFecInicioActividad(fecCaptura);
		model.addAttribute("capHora", capHora);
		return "layout/capHora :: detActividades";
	}
	
	
	@GetMapping(value="/cargarDetActividadFuera/{codActividad}/{fecCaptura}/{codRecurso}/{codProyecto}")
	public String cargarDetActividadFuera(@PathVariable Long codActividad, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date fecCaptura,@PathVariable Long codRecurso,@PathVariable Long codProyecto, Model model){
		System.out.println(codActividad +" "+codRecurso +" "+fecCaptura);
//		CapHoraId capHoraId = new CapHoraId(codActividad, codRecurso, 1L, 1L, 2L);
		Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
		CapHora capHora = new CapHora();
		capHora.setCodActividad(codActividad);
		capHora.setCodRecurso(codRecurso);
		capHora.setCodProyecto(proyecto.getCodProyecto());
		capHora.setCodCliente(proyecto.getCodCliente());
		capHora.setCodEstatusProyecto(proyecto.getCodEstatusProyecto());
//		capHora.setId(capHoraId);
		capHora.setValNuevaActividad(2L);
		capHora.setFecInicioActividad(fecCaptura);
		model.addAttribute("capHora", capHora);
		return "layout/capHora :: detActividades";
	}
	
	
	@GetMapping(value="/cargarActividadCapturadas/{codRecurso}/{fecCaptura}")
	public String cargarActividadesCapturadas(@PathVariable Long codRecurso, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date fecCaptura, Model model){
		List<CapHora> listActHoraCapturadas = new ArrayList<CapHora>();
		listActHoraCapturadas = capHoraService.findListCapHoraByFechaRecurso(fecCaptura, codRecurso);
		for (CapHora capHora : listActHoraCapturadas) {
			// 1 o 2 para actividad no plan CAT_SUBTAREAS  - 0 para actividades de plan RECURSOS_ACTIVIDADES
			if(capHora.getValNuevaActividad().equals(1L) || capHora.getValNuevaActividad().equals(2L)) {
				capHora.setDescProyecto(proyectoService.findByCodProyecto(capHora.getCodProyecto()).getDescProyecto());
				capHora.setDescActividadSecundaria(subtareaService.findOne(capHora.getCodActividad()).getDescSubtarea());
			} else {
				capHora.setDescProyecto(proyectoService.findByCodProyecto(capHora.getCodProyecto()).getDescProyecto());
				capHora.setDescActividadSecundaria(actividadService.findOne(capHora.getCodActividad()).getDescActividadSecundaria());
			}
		}
		model.addAttribute("listActHoraCapturadas", listActHoraCapturadas);
		return "layout/capHora :: detActividadesCapturadas";
	}
	
	

	@PostMapping(value="/formCapHoraActividad", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String guardarCapHora(@Valid CapHora capHora, BindingResult result, Model model, RedirectAttributes flash ,SessionStatus status) {
		
		codproyecto_u = capHora.getCodProyecto();
		codActividad_u = capHora.getCodActividad();
		descComentarioDetalle_u = capHora.getDescComentarioDetalle();
		valDuracionReportada_u = capHora.getValDuracionReportada();
		
		System.out.println(codproyecto_u + " " + codActividad_u + " " + descComentarioDetalle_u + " " + valDuracionReportada_u);
		
		if(result.hasErrors()) {

			return "formCapHoras";
		}
		
		if(codproyecto != codproyecto_u || codActividad != codActividad_u || descComentarioDetalle != descComentarioDetalle_u || valDuracionReportada != valDuracionReportada_u) {
			capHora.setValRechazo(0L);
			capHora.setValDuracionRechazada(0L);
			capHora.setDescRechazo(null);
		}
		
		String mensajeFlash = "Registro guardado con éxito";
		
		Actividad actividad = actividadService.findOne(capHora.getCodActividad());
		Subtarea subtarea = subtareaService.findOne(capHora.getCodActividad());
		
		if (subtarea != null) {
			capHora.setValNuevaActividad(1L);
		}
		
		if (actividad != null) {
			capHora.setValNuevaActividad(0L);
		}
		
		capHoraService.save(capHora);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "Registro guardado con éxito";
	}
	
	
	@RequestMapping(value="/horasTotalSemana",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Float horasTotalSemana(@RequestParam Long codRecurso, @RequestParam Date fechaInicioSemana, @RequestParam Date fechaFinSemana, Model model) {
		System.out.println(codRecurso + " " + fechaInicioSemana + " "+ fechaFinSemana);
		return capHoraService.findSumHorasReportadasSemana(codRecurso, fechaInicioSemana, fechaFinSemana);
	}
	
	@RequestMapping(value="/borrarCapHora",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String borrarCapHora(@RequestParam Long codCaptura, Model model) {
		CapHora capHora = capHoraService.findOne(codCaptura);
		capHoraService.delete(capHora);
		return "Borrado correcto";
	}
	
	@GetMapping(value="/editCaptura/{codCaptura}")
	public String editCaptura(@PathVariable Long codCaptura, Model model) {
		
		CapHora capHora = capHoraService.findOne(codCaptura);
		
		Long codProyecto = capHora.getCodProyecto();
		Long codRecurso = capHora.getCodRecurso();
		
		//Carga proyectos del recurso
		List<Long> proyectoListId = new ArrayList<Long>();
		HashMap<Long, String> proyectoList = new HashMap<Long, String>(); 
		proyectoListId = actividadService.findListaProyectoByRecurso(capHora.getCodRecurso());
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
		proyectosRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(capHora.getCodRecurso());
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

		
		//carga fase o actividad primaria
		if(codProyecto.equals(1L)) {
			Tarea tarea = tareaService.findOne(1L);
			List<String> actividadesPrimariasList = new ArrayList<String>();
			actividadesPrimariasList.add(tarea.getDescTarea());
			model.addAttribute("actividadesPrimarias", actividadesPrimariasList);
		}else {
			List<String> actividadesPrimariasList = new ArrayList<String>();
			Proyecto proy = proyectoService.findByCodProyecto(codProyecto);
			if(proy.getCodEstatusProyecto().equals(1L)) {
				actividadesPrimariasList.add(PREVENTA);
			}
			actividadesPrimariasList.addAll(actividadService.findListaActividadesPrimariasByRecursoProyecto(codRecurso, codProyecto));
			actividadesPrimariasList.add(OTRA);
			model.addAttribute("actividadesPrimarias", actividadesPrimariasList);
		}
		
		
		//Carga actividad secundaria apartir de codActividad y valNuevaActividad
		if(capHora.getValNuevaActividad().equals(0L)) {
			List<Actividad> listaActividadesSecundarias = new ArrayList<>();
			String actividadPrimaria = actividadService.findOne(capHora.getCodActividad()).getDescActividadPrimaria();
			listaActividadesSecundarias = actividadService.findListaActividadesByRecursoProyectoPrimaria(capHora.getCodRecurso(), capHora.getCodProyecto(), actividadPrimaria);
			HashMap<Long, String> listadoActividadesSecundarias = new HashMap<>();
			for (Actividad actividad : listaActividadesSecundarias) {
				listadoActividadesSecundarias.put(actividad.getCodActividad(), actividad.getDescActividadSecundaria());
			}
			model.addAttribute("actividadesSecundarias", listadoActividadesSecundarias);
			model.addAttribute("actividadPrimariaSelec", actividadPrimaria);
			model.addAttribute("otra","0");
			System.out.println(actividadPrimaria);
		} else {
			if(codProyecto.equals(1L)) {
				
				Tarea tarea = tareaService.findOne(1L);
				
				model.addAttribute("actividadPrimariaSelec", tarea.getDescTarea());
				
				List<Tarea> listadoActividadesSecundarias = new ArrayList<>();
				listadoActividadesSecundarias.add(tarea);
				model.addAttribute("actividadesSecundarias", listadoActividadesSecundarias);
				model.addAttribute("otra","1");
				System.out.println(PREVENTA);
			} else {
				model.addAttribute("actividadPrimariaSelec", OTRA);
				model.addAttribute("otra","1");
//				List<Subtarea> listaActividadesSecundarias = new ArrayList<>();
//				listaActividadesSecundarias = subtareaService.findFueraDePlan();
//				HashMap<Long, String> listadoActividadesSecundarias = new HashMap<>();
//				for (Subtarea subtarea : listaActividadesSecundarias) {
//					listadoActividadesSecundarias.put(subtarea.getCodSubtarea(), subtarea.getDescSubtarea());
//				}
				List<Tarea> listadoActividadesSecundarias = new ArrayList<>();
				listadoActividadesSecundarias = tareaService.findTareaFueraPlan();
				model.addAttribute("actividadesSecundarias", listadoActividadesSecundarias);
				
				System.out.println(OTRA);
			}
	
		}
		
		
		model.addAttribute("proyectoList", proyectoList);
		model.addAttribute("capHora", capHora);
		
		return "layout/capHora :: editDetActividades";
	}

}
