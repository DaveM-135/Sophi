package com.sophi.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

//import javax.validation.Valid;

import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.Rol;
import com.sophi.app.models.entity.AuxActividadHorasRecurso;
import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.DetalleRecursoHoras;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.ICapHoraService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRolService;
import com.sophi.app.models.service.ISubtareaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class AprobacionHorasProyectoController {

    @Autowired
    private IRecursoService recursoService;
    
    @Autowired
    private IProyectoService proyectoService;
    
    @Autowired
    private IProyectoRecursoService proyectoRecursoService;
    
    @Autowired
    private ISubtareaService subtareaService;
    
    @Autowired
    private IActividadService actividadService;
    
    @Autowired
    private ICapHoraService capHoraService;
    
    @Autowired
    private IRolService rolService;
    
    Long codRecursoSesion;
	Date inicio = new Date();
    Date fin = new Date();
    
    @RequestMapping(value = "/aprobacionHorasProyecto/{email}", method = RequestMethod.GET)
    public String aprobacionHorasProyecto(Model model, @PathVariable(value = "email") String email){
    	//Obtiene el codigo del recurso solicitante
    	Long codRecurso = null;
    	codRecurso = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
    	codRecursoSesion = codRecurso;
    	
    	List<Rol> roles = new ArrayList<Rol>();
    	List<String> cadenaRol = new ArrayList<String>();
    	
    	//creacion e incializacion de lista temporales
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
		
    	
    	//si existe el recurso solicitante...
    	if (codRecurso != null) {
    		
    		roles = rolService.findByCodRecurso(codRecurso);
    		for (Rol rol : roles) {
				cadenaRol.add(rol.getDescRol());
			}
    		
    		//Obtiene listado de proyectos donde el recurso es lider.
        	List<Proyecto> listaProyectosLider = new ArrayList<Proyecto>();
        	String r = "ROLE_ADMIN";
        	if (cadenaRol.contains(r)) {
        		listaProyectosLider = proyectoService.findAll();
        	} else {
        		listaProyectosLider = proyectoService.findListaProyectosRecursoAprobadorTodos(codRecurso);
        	}
        	
        	//si el recurso es lider y tiene proyectos a cargo
        	if (listaProyectosLider.size() > 0) {
        		
        		//recorre la lista de proyectos de recurso lider solicitante
        		for (Proyecto proyecto : listaProyectosLider) {
        			//si el proyecto se encuentra en estatus 1 o 2  se incluye como proyecto activo
        			//y se obtienen las horas a aprobar 
        			if(proyecto.getCodEstatusProyecto().equals(1L) || proyecto.getCodEstatusProyecto().equals(2L)) {
        				listaProyecto.add(proyecto);
        			}
        		}
        		
        	}
    	
    	}
    	
//    	Agregar proyecto admin
//    	listaProyecto.add(proyectoService.findOne(1L));

        
        model.addAttribute("proyectos", listaProyecto);
        return "aprobacionHorasProyecto";
    }
    
    
    @RequestMapping(value = "/capturaHorasPeriodo/{from}/{to}/{codProyecto}", method = RequestMethod.GET)
    public String listadoRecursosCaptura(Model model, @PathVariable(value = "from") String desde, @PathVariable(value = "to") String hasta, @PathVariable(value = "codProyecto") String codProyecto){
    	Date inicio = new Date();
    	Date fin = new Date();
    	String todos = "no";
    	try {
			inicio =new SimpleDateFormat("MM-dd-yyyy").parse(desde);
			fin =new SimpleDateFormat("MM-dd-yyyy").parse(hasta);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
    	//Listas temporales para almacenar info
    	List<Long> proyectos = new ArrayList<Long>();
    	List<ProyectoRecurso> listProyectoRecurso = new ArrayList<ProyectoRecurso>();
    	List<DetalleRecursoHoras> listaDetalle = new ArrayList<DetalleRecursoHoras>();
    
    	//Se obtienen los codigos de proyectos del recurso solicitante (-1 es todos)
    	for(String codPro : codProyecto.split(",")) {
    		if (codPro.equals("-1")) {
    			todos="si";
    			continue;
    		} else {
    			proyectos.add(Long.parseLong(codPro));
    		}
    	}
    	
    	//Se obtiene los recurso de los proyectos solicitados
    	for (Long clvPro : proyectos) {
    			listProyectoRecurso.addAll(proyectoRecursoService.findByProyectoRecursoIdCodProyecto(clvPro));
		}
    	
    	//si existen recursos en los proyectos
		if (listProyectoRecurso.size() > 0){
			if (todos.equals("si")) {
				HashSet<Long> recursosUnicos = new HashSet<Long>();
				for (ProyectoRecurso proyectoRecurso : listProyectoRecurso) {
					if(!proyectoRecurso.getProyectoRecursoId().getCodRecurso().equals(codRecursoSesion)) {
						recursosUnicos.add(proyectoRecurso.getProyectoRecursoId().getCodRecurso());
					}
				}
				if(recursosUnicos.size()>0) {
					for (Long codR : recursosUnicos) {
					Recurso recursoTmp = recursoService.findOne(codR);
						if(recursoTmp.getCodAreaRecurso().equals(1L) && recursoTmp.getValActivo().equals(1L) && !codR.equals(codRecursoSesion)) {
							DetalleRecursoHoras detalle = capHoraService.findDetalleRecursoHorasTodos(codR, inicio, fin);
							detalle.setNombreRecurso(recursoTmp.getDescRecurso() + ' ' + recursoTmp.getDescApellidoPaterno());
							detalle.setLink(recursoTmp.getCodRecurso());
							detalle.setAprobadas(detalle.getAprobadas()== null ? 0 : detalle.getAprobadas());
							detalle.setCapturadas(detalle.getCapturadas()== null ? 0 : detalle.getCapturadas());
							detalle.setRechazadas(detalle.getRechazadas()== null ? 0 : detalle.getRechazadas());
							listaDetalle.add(detalle);
						}
					}
				}
			} else {
				for (ProyectoRecurso proyectoRecurso : listProyectoRecurso) {
					Recurso recursoTmp = recursoService.findOne(proyectoRecurso.getProyectoRecursoId().getCodRecurso());
					if(recursoTmp.getCodAreaRecurso().equals(1L) && recursoTmp.getValActivo().equals(1L) && !recursoTmp.getCodRecurso().equals(codRecursoSesion)) {
						DetalleRecursoHoras detalle = capHoraService.findDetalleRecursoHoras(proyectoRecurso.getProyectoRecursoId().getCodRecurso(), proyectoRecurso.getProyectoRecursoId().getCodProyecto(), inicio, fin);
						detalle.setNombreRecurso(recursoTmp.getDescRecurso() + ' ' + recursoTmp.getDescApellidoPaterno());
						detalle.setLink(recursoTmp.getCodRecurso());
						detalle.setAprobadas(detalle.getAprobadas()== null ? 0 : detalle.getAprobadas());
						detalle.setCapturadas(detalle.getCapturadas()== null ? 0 : detalle.getCapturadas());
						detalle.setRechazadas(detalle.getRechazadas()== null ? 0 : detalle.getRechazadas());
						listaDetalle.add(detalle);
					}
				}
			}
			
			
		}
    	
		model.addAttribute("listRecursos", listaDetalle);
    	
    	return "layout/plantilla-caphora :: recursos-caphoras";
    }
    
    
    @RequestMapping(value = "/aprobacionHorasProyecto/detalle", method = RequestMethod.GET)
    public String detalleAprobacionHorasProyecto(@RequestParam(value = "r") Long codRecurso, 
    											@RequestParam(value = "p") String proyectos,  
    											@RequestParam(value = "s") String semana, 
    											@RequestParam(value = "f") String desde, 
    											@RequestParam(value = "t") String hasta, 
    											Model model){
    	
    	try {
			inicio =new SimpleDateFormat("MM-dd-yyyy").parse(desde);
			fin =new SimpleDateFormat("MM-dd-yyyy").parse(hasta);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	List<Date> fechasPeriodo = new ArrayList<Date>();
    	List<AuxActividadHorasRecurso> listaActividades = new ArrayList<AuxActividadHorasRecurso>();
    	
    	for (int i = 0; i < 7; i++) {
    		Date tmp = sumarDia(inicio,i);
    		fechasPeriodo.add(tmp);
    		//solicitar info de horas capturadas
    		List<CapHora> detalleCapturadas = new ArrayList<CapHora>();
    		detalleCapturadas.addAll(actividadesCapturadas(codRecurso, tmp, proyectos));
    		
    		if (detalleCapturadas.size() > 0) {
    			for (CapHora capHora : detalleCapturadas) {
    				AuxActividadHorasRecurso aux = new AuxActividadHorasRecurso();
        			aux.setIdCapHora(capHora.getCodCapHora());
        			aux.setIdProyecto(capHora.getCodProyecto());
        			aux.setDescProyecto(capHora.getDescProyecto());
        			aux.setIdActividad(capHora.getCodActividad());
        			aux.setDescActividad(capHora.getDescActividadSecundaria());
        			aux.setDescComentario(capHora.getDescComentarioDetalle());
        			aux.setHorasCapturadas(capHora.getValDuracionReportada());
        			aux.setFechaActividad(capHora.getFecInicioActividad());
        			aux.setFechaValidada(capHora.getFecValidacion());
        			aux.setHorasPlaneadas(capHora.getHorasPlaneadas());
        			aux.setHorasAprobadas(capHora.getValDuracionValidad());
        			aux.setValRechazo(capHora.getValRechazo());
        			aux.setDia(i+1);
        			listaActividades.add(aux);
				}
    			
    		}

    		
		}
    	
    	Recurso recurso = recursoService.findOne(codRecurso);
    	String nombre = recurso.getDescRecurso() + " " + recurso.getDescApellidoPaterno();
    	model.addAttribute("listActHoraCapturadas",listaActividades);
    	model.addAttribute("fechas",fechasPeriodo);
    	model.addAttribute("nombre", nombre);
    	return "layout/plantilla-caphora :: detalle-recursos-caphoras";
    }
    
    
//    @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") 
	public List<CapHora> actividadesCapturadas(Long codRecurso,Date fecCaptura, String proyectos){
		
		String[] listaProyectos = proyectos.split(",");
		List<Long> codProyectos = new ArrayList<Long>();
		codProyectos.add(1L);
		for (String proy : listaProyectos) {
			codProyectos.add(Long.parseLong(proy));
		}
		
		List<CapHora> listActHoraCapturadas = new ArrayList<CapHora>();
		List<CapHora> listActHoraCapturadasF = new ArrayList<CapHora>();
		listActHoraCapturadas = capHoraService.findListCapHoraByFechaRecurso(fecCaptura, codRecurso);
		
		for (CapHora capHora : listActHoraCapturadas) {
			if (codProyectos.contains(capHora.getCodProyecto())) {
				if(capHora.getValNuevaActividad().equals(1L) || capHora.getValNuevaActividad().equals(2L)) {
					capHora.setDescProyecto(proyectoService.findByCodProyecto(capHora.getCodProyecto()).getDescProyecto());
					capHora.setDescActividadSecundaria(subtareaService.findOne(capHora.getCodActividad()).getDescSubtarea());
					capHora.setHorasPlaneadas((float) 0);
					listActHoraCapturadasF.add(capHora);
				} else {
					capHora.setDescProyecto(proyectoService.findByCodProyecto(capHora.getCodProyecto()).getDescProyecto());
					capHora.setDescActividadSecundaria(actividadService.findOne(capHora.getCodActividad()).getDescActividadSecundaria());
					capHora.setHorasPlaneadas(Float.parseFloat(actividadService.findOne(capHora.getCodActividad()).getValDuracionActividad()));
					listActHoraCapturadasF.add(capHora);
				}
			} 
		}
		 return listActHoraCapturadasF;
	}
    
    
    public Date sumarDia(Date fecha,int dia) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(fecha);
    	calendar.add(Calendar.DAY_OF_YEAR, dia);
    	return calendar.getTime();
    }
    
    @RequestMapping(value = "/updAprobacion", method = RequestMethod.GET)
    @ResponseBody
    public String updAprobacion(@RequestParam(value = "codCapHora") Long codCapHora, 
			@RequestParam(value = "hrAprobada") Float hora,
    	@RequestParam(value = "aprobador") String mail ) {
    	Long codRecurso = recursoService.findByDescCorreoElectronico(mail).getCodRecurso();
    	CapHora capHora = capHoraService.findOne(codCapHora);
    	if (capHora != null) {
    		capHora.setValDuracionValidad(hora);
    		capHora.setCodRecursoValidador(codRecurso);
    		capHora.setFecValidacion(new Date());
    		capHora.setValRechazo(0L);
    		capHora.setValDuracionRechazada(0L);
    		capHora.setDescRechazo(null);
    		capHoraService.save(capHora);
    		return "ok";
    	} else {
    		return "noOk";
    	}
    	
    }
    
    @RequestMapping(value = "/updRechazar", method = RequestMethod.GET)
    @ResponseBody
    public String updRechazar(@RequestParam(value = "codCapHora") Long codCapHora, 
    	@RequestParam(value = "aprobador") String mail,
    	@RequestParam(value = "comentario") String comentario) {
    	Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
    	CapHora capHora = capHoraService.findOne(codCapHora);
    	if (capHora != null) {
    		capHora.setValRechazo(1L);
    		capHora.setCodRecursoValidador(recurso.getCodRecurso());
    		capHora.setValDuracionRechazada(capHora.getValDuracionReportada());
    		capHora.setDescRechazo(recurso.getDescRecurso() + " dijo: "+ comentario);
    		capHora.setValDuracionValidad((float) 0);
    		capHora.setFecValidacion(null);
    		capHoraService.save(capHora);
    		return "ok";
    	} else {
    		return "noOk";
    	}
    	
    }

}
