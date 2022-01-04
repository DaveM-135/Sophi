package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.List;

//import javax.validation.Valid;

import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.AprobacionHoras;
import com.sophi.app.models.entity.AprobacionHorasDto;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IAprobacionHorasService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.ISubtareaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@SessionAttributes("aprobacionhoraslista")
@Controller
public class AprobacionHorasController {

    @Autowired
    private IAprobacionHorasService aprobacionHorasService;

    @Autowired
    private IRecursoService recursoService;
    
    @Autowired
    private IProyectoService proyectoService;
    
    @Autowired
    private ISubtareaService subtareaService;
    
    @Autowired
    private IActividadService actividadService;
    
    public Long CodRecurso;

    @RequestMapping(value = "/aprobacionhoras/{email}", method = RequestMethod.GET)
    public String AprobacionHoras(Model model, @PathVariable(value = "email") String email){
    	Long codRecurso = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
    	
    	//Obtiene listado de proyectos donde el recurso es lider.
    	List<Proyecto> listaProyectosLider = new ArrayList<Proyecto>();
    	listaProyectosLider = proyectoService.findByCodRecursoLider(codRecurso);
    	
    	
    	List<AprobacionHoras> aprobacioneshoras = new ArrayList<>();
        List<Recurso> listaRecursos = new ArrayList<Recurso>();
//        List<Proyecto> listaProyecto = proyectoService.findProyectosActivos();
//		List<Proyecto> listaProyectoTodo = proyectoService.findProyectosActivos();
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
		List<Proyecto> listaProyectoTodo = new ArrayList<Proyecto>();
		
		List<AprobacionHoras> tmpListAprobacion = new ArrayList<AprobacionHoras>();
        
		
		for (Proyecto proyecto : listaProyectosLider) {
			if(proyecto.getCodEstatusProyecto().equals(1L) || proyecto.getCodEstatusProyecto().equals(2L)) {
				listaProyecto.add(proyecto);
				listaProyectoTodo.add(proyecto);
				tmpListAprobacion.addAll(aprobacionHorasService.findAprobacionHorasGeneral(proyecto.getCodProyecto()));
			}
		}
		
//		Proyecto proyectoAux;
//        
//        for(Proyecto p : listaProyecto) {
//			if(p.getCodEstatusProyecto().equals(1L) || p.getCodEstatusProyecto().equals(2L)) {
//				proyectoAux=proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(p.getCodProyecto(), 1L, p.getCodCliente());
//				if(proyectoAux!=null) {
//					listaProyectoTodo.remove(proyectoAux);
//				}
//			}
//		}
        
//        List<AprobacionHoras> tmpListAprobacion = new ArrayList<AprobacionHoras>();
//        tmpListAprobacion = aprobacionHorasService.findAprobacionHorasGeneral();
        
        
        
        for (AprobacionHoras aprobacionHoras : tmpListAprobacion) {
        	if(aprobacionHoras.getValNuevaActividad().equals(1L) || aprobacionHoras.getValNuevaActividad().equals(2L)) {
        		aprobacionHoras.setDescProyecto(proyectoService.findByCodProyecto(aprobacionHoras.getCodProyecto()).getDescProyecto());
        		aprobacionHoras.setDescActividadSecundaria(subtareaService.findOne(aprobacionHoras.getCodActividad()).getDescSubtarea());
        		aprobacionHoras.setHorasPlaneadas((float) 0);
			} else {
				aprobacionHoras.setDescProyecto(proyectoService.findByCodProyecto(aprobacionHoras.getCodProyecto()).getDescProyecto());
				aprobacionHoras.setDescActividadSecundaria(actividadService.findOne(aprobacionHoras.getCodActividad()).getDescActividadSecundaria());
				aprobacionHoras.setHorasPlaneadas(Float.parseFloat(actividadService.findOne(aprobacionHoras.getCodActividad()).getValDuracionActividad()));
			}
		}
        
        tmpListAprobacion.iterator().forEachRemaining(aprobacioneshoras::add);
        listaRecursos = recursoService.findAll();
        AprobacionHorasDto aphdto = new AprobacionHorasDto();
        aphdto.setAprobacionhoras(aprobacioneshoras);
        model.addAttribute("titulo", "Listado de horas capturadas");
        model.addAttribute("aprobacionhoraslista", aphdto);
        model.addAttribute("recursos", listaRecursos);
        model.addAttribute("proyectos", listaProyectoTodo);
        model.addAttribute("r", codRecurso);
        CodRecurso = codRecurso;
        return "aprobacionhoras";
    }
    

    @RequestMapping(value="/guardar", method = RequestMethod.POST)
	public String validarHoras(@ModelAttribute("aprobacionhoraslista") AprobacionHorasDto aprobacionhoraslista, BindingResult result, Model model,RedirectAttributes flash) {// ,SessionStatus status) {

		if(result.hasErrors()) {
			model.addAttribute("titulo", "Listado Horas Capturadas");
			List<Recurso> listaRecursos = new ArrayList<Recurso>();
	        listaRecursos = recursoService.findAll();
	        model.addAttribute("recursos", listaRecursos);
	        model.addAttribute("r", CodRecurso);
			return "aprobacionhoras";
		}

		aprobacionHorasService.saveAll(aprobacionhoraslista.getAprobacionhoras());
//		status.setComplete();
		flash.addFlashAttribute("success", "Horas aprobadas");
		return "redirect:/aprobacionhoras/"+recursoService.findOne(CodRecurso).getDescCorreoElectronico();
	}
}
