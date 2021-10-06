package com.sophi.app.controllers;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Actividad;
import com.sophi.app.models.entity.ActividadDto;
import com.sophi.app.models.entity.ActividadPlan;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.ProyectoRecursoId;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

@Controller
public class PlaneacionController {
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@GetMapping("/planeacionProyecto/{codProyecto}")
	public String planeacionProyecto(@PathVariable Long codProyecto, Model model) {
		//Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
		model.addAttribute("codProyecto", codProyecto);
		return "formActividadesPlan";
		//return "preventaProyectoConsulta/"+codProyecto+"/"+proyecto.getCodEstatusProyecto()+"/"+proyecto.getCodCliente();
	}
	
	@PostMapping("/guardarPlan")
	public String guardarPlanCSV(@ModelAttribute ActividadDto actividades, Model model , RedirectAttributes flash) {
		if(!(actividades.getActividades().isEmpty() && (actividades.getActividades() == null))) {
			Long codProyecto=actividades.getActividades().get(0).getCodProyecto();
			actividadService.saveAll(actividades.getActividades());
			List<ProyectoRecurso> listaRecursosAsignados = new ArrayList<ProyectoRecurso>();
			for (Actividad actividad : actividades.getActividades()) {
				ProyectoRecursoId prId = new ProyectoRecursoId(actividad.getCodProyecto(), actividad.getCodRecurso(), actividad.getCodCliente(), actividad.getCodEstatusProyecto());
				ProyectoRecurso pr = new ProyectoRecurso();
				pr.setProyectoRecursoId(prId);
				if(!listaRecursosAsignados.contains(pr)) {
				listaRecursosAsignados.add(pr);
				}
			}
			
			for(ProyectoRecurso proyectoRecurso: listaRecursosAsignados) {
				Long codRecurso = proyectoRecurso.getProyectoRecursoId().getCodRecurso();
				float totalHoras = 0;
				for (Actividad actividad : actividades.getActividades()) {
					if(codRecurso.equals(actividad.getCodRecurso())) {
						totalHoras +=  Float.parseFloat(actividad.getValDuracionActividad());
					}
				}
				proyectoRecurso.setValHorasRecurso(totalHoras);
				proyectoRecurso.setImpCostoRecurso(recursoService.findOne(codRecurso).getValCostoMinimo());
			}
			
			
			proyectoRecursoService.saveAll(listaRecursosAsignados);
			
			//Envio de noificacion al recurso de nueva asignacion a proyecto
			for (ProyectoRecurso proyectoRecurso : listaRecursosAsignados) {
				Recurso recurso = recursoService.findOne(proyectoRecurso.getProyectoRecursoId().getCodRecurso());
				Proyecto proyecto = proyectoService.findOne(proyectoRecurso.getProyectoRecursoId().getCodProyecto());
			}
			
			Proyecto setValPlanProy = proyectoService.findOne(codProyecto);
			setValPlanProy.setValPlan(1L);
			proyectoService.save(setValPlanProy);
			
			System.out.println("Proyecto: "+ setValPlanProy.getDescProyecto()+ " val plan: "+ setValPlanProy.getValPlan());
			
			
			flash.addFlashAttribute("success", "Plan cargado con éxito");
			//return "redirect:/preventaProyectoContactoInfraestructura/"+codProyecto;
			return "redirect:/preventaProyectoConsulta/"+codProyecto+"/"+setValPlanProy .getCodEstatusProyecto()+"/"+setValPlanProy.getCodCliente();
		} else {
			flash.addFlashAttribute("error", "Error al cargar plan");
			System.out.println("No cargo nada");
			return "redirect:/";
		}
	}
	
	@PostMapping("/subirPlan")
	public String subirPlanCSV(@RequestParam("archivoCsvPlan") MultipartFile archivoCsvPlan,@RequestParam("codProyecto") Long codProyecto, Model model) {
		if (archivoCsvPlan.isEmpty()) {
            model.addAttribute("message", "Selecciona un archivo .CSV para subir");
            model.addAttribute("status", false);
        } else {
        	
            try (Reader reader = new BufferedReader(new InputStreamReader(archivoCsvPlan.getInputStream()))) {
            	
            	Proyecto proy =  proyectoService.findByCodProyectoAndCodEstatusProyecto(codProyecto, 2L);

				CsvToBean<ActividadPlan> csvToBean = new CsvToBeanBuilder<ActividadPlan>(reader)
						.withType(ActividadPlan.class)
						.withIgnoreLeadingWhiteSpace(true)
						.build();

                
                List<ActividadPlan> actividadesPlanStg = csvToBean.parse();
                List<ActividadPlan> actividadesPlan = new ArrayList<ActividadPlan>();
                
                List<Actividad> actividadesCargar = new ArrayList<Actividad>();
                
                String actividadPrimaria ="";
                String actividadPrimariaAux = "";
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                
                List<Long> listCodRecursos = new ArrayList<Long>(); 
                
                String fechaInicioPlan = "";
                String fechaFinPlan = "";
                
                for (int i = 0; i < actividadesPlanStg.size(); i++) {
                	
                	String actividadSecundaria ="";
                	
                	// Se identifica al primer renglon para obtener nombre y duracion del proyecto
                	if (i == 1) {
						fechaInicioPlan = actividadesPlanStg.get(i).getInicio().substring(0, 10);
						fechaFinPlan = actividadesPlanStg.get(i).getFin().substring(0, 10);
					} else if (i > 1) { // Se itera el listado de CSV posterior al nombre y duracion general del proyecto.
	                	ActividadPlan ap = new ActividadPlan();
	                	ap =actividadesPlanStg.get(i);
	                	ap.setSerie(i);
	                	actividadesPlan.add(ap);
	                	//Identificar si la columna RECURSO esta vacia, lo cual indica que es una actividad PADRE
	                	if(actividadesPlanStg.get(i).getRecursos().isEmpty()) {
	                		actividadPrimariaAux = (actividadPrimariaAux + " > " + actividadesPlanStg.get(i).getNombre()).trim();
	                		actividadPrimaria = actividadPrimariaAux;
	                		continue;
	                	} else {
	                		actividadPrimariaAux="";
	                		actividadSecundaria = actividadesPlanStg.get(i).getNombre();
	                	                	
	                	
	                	
	                	String[] listaRecursos = actividadesPlanStg.get(i).getRecursos().split(",");
	                	if (listaRecursos.length>0) {
	                		for (int j = 0; j < listaRecursos.length; j++) {
	                			String descRecurso = listaRecursos[j].split("\\s+")[0];
	    	                	String descApellidoPaterno = listaRecursos[j].split("\\s+")[1];
	    	                	
	    	                	Actividad actividad = new Actividad();
	    	                	actividad.setDescActividadPrimaria(actividadPrimaria);
	    	                	actividad.setDescActividadSecundaria(actividadSecundaria);
	    	                	actividad.setValNumActividad((long) i);
	    	                	actividad.setCodCliente(proy.getCodCliente());
	    	                	actividad.setCodEstatusProyecto(proy.getCodEstatusProyecto());
	    	                	actividad.setCodProyecto(proy.getCodProyecto());
	    	                	actividad.setValDuracionActividad(String.format("%.2f", Float.parseFloat(actividadesPlanStg.get(i).getEsfuerzo().replaceAll("hora","").replaceAll("s",""))/listaRecursos.length));
	    	                	actividad.setValNuevaActividad(0L);
	    	                	actividad.setFecInicioActividad(format.parse(actividadesPlanStg.get(i).getInicio().substring(0, 10)));
	    	                	actividad.setFecFinActividad(format.parse(actividadesPlanStg.get(i).getFin().substring(0, 10)));
	    	                	actividad.setCodActividadDependiente(actividadesPlanStg.get(i).getPredecesora());
	    	                	
	    	                	List<Recurso> listRecursoActividad = new ArrayList<Recurso>(); 
	    	                	listRecursoActividad=recursoService.findByNombreApellido(descRecurso, descApellidoPaterno);
	    	                	
	    	                	
	    	                	
	    	                	if (!listRecursoActividad.isEmpty()) {
	    	                		System.out.println(listRecursoActividad.get(0).getCodRecurso());
	    	                		actividad.setCodRecurso(listRecursoActividad.get(0).getCodRecurso());
	    	                		listCodRecursos.add(listRecursoActividad.get(0).getCodRecurso());
	    	                		actividad.setNombreRecurso(descRecurso + " " + descApellidoPaterno );
		    	                	actividadesCargar.add(actividad);
		    	                	
	    	                	} else {
	    	                		System.out.println("No existe Recurso");
	    	                	}
	    	                	
	    	                	
							}
	                	}
	                	
	                	
	                	}
	                	
	                	
                	}
                	
                	
				}
                
                List<Long> listCodRecursosUnicos = new ArrayList<>(new HashSet<>(listCodRecursos));
                Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
                
                
                model.addAttribute("actividadesPlan", actividadesPlan);
                ActividadDto actividadesCargarDto = new ActividadDto();
                actividadesCargarDto.setActividades(actividadesCargar);
                model.addAttribute("totalRecursosIdentificados", listCodRecursosUnicos.size());
                model.addAttribute("fechaInicioPlan",fechaInicioPlan);
                model.addAttribute("fechaFinPlan",fechaFinPlan);
                model.addAttribute("actividadesCargarDto", actividadesCargarDto);
                model.addAttribute("proyecto", proyecto);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "Ocurrio un problema mientras se procesa el archivo.");
                model.addAttribute("status", false);
            }
        	
        }
		return "listaActividadesPlan";
	}
	
	
	@GetMapping("/verPlanActividades/{codProyecto}")
	public String verPlanActividades(@PathVariable Long codProyecto, Model model) {
		List<Actividad> listaActividades =  actividadService.findByCodProyecto(codProyecto);
		Proyecto proyecto =  proyectoService.findOne(codProyecto);
		String nombreProyecto =proyecto.getDescProyecto();
		model.addAttribute("listaActividades", listaActividades);
		model.addAttribute("nombreProyecto", nombreProyecto);
		model.addAttribute("proyecto", proyecto);
		return "listaActividadesProyecto";
	}
	
	
	//Asignación de proyecto (aprobador y lider)
	public void enviaNotificacionAsignacionRecurso(Proyecto proy, Recurso recurso) {
		
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(recurso.getDescRecurso());
		request.setSubject("Nueva asignación - Recurso");
		request.setTo(recurso.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Has sido asignado al proyecto \""+ proy.getDescProyecto() + "\", puedes revisar tus actividades en la plataforma en la seccion \"Mis actividades\".</h3>");
		model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-status.png\">");
		model.put("pie", "");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
		//Aprobador FIN
	}


}
