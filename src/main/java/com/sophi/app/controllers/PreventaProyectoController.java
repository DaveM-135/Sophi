package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Agenda;
import com.sophi.app.models.entity.AreaComercial;
import com.sophi.app.models.entity.Cliente;
import com.sophi.app.models.entity.DetalleClienteAreaComercialId;
import com.sophi.app.models.entity.DetalleClienteAreaComercial;
import com.sophi.app.models.entity.DetalleClienteInfraestructura;
import com.sophi.app.models.entity.DetalleClienteInfraestructuraId;
import com.sophi.app.models.entity.DetalleInfraestructura;
import com.sophi.app.models.entity.DetalleProyectoContacto;
import com.sophi.app.models.entity.DetalleProyectoContactoId;
import com.sophi.app.models.entity.DetalleProyectoInfraestructura;
import com.sophi.app.models.entity.DetalleProyectoInfraestructuraId;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoTrayectoriaProyecto;
import com.sophi.app.models.entity.Rol;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.IAgendaService;
import com.sophi.app.models.service.IAreaComercialService;
import com.sophi.app.models.service.IClasificacionProyectoService;
import com.sophi.app.models.service.IClienteService;
import com.sophi.app.models.service.IConocimientoService;
import com.sophi.app.models.service.IDetalleClienteAreaComercialService;
import com.sophi.app.models.service.IDetalleClienteInfraestructuraService;
import com.sophi.app.models.service.IDetalleConocimientoProyectoService;
import com.sophi.app.models.service.IDetalleInfraestructuraService;
import com.sophi.app.models.service.IDetalleProyectoContactoService;
import com.sophi.app.models.service.IDetalleProyectoInfraestructuraService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRecursoTrayectoriaProyectoService;
import com.sophi.app.models.service.IRolService;
import com.sophi.app.models.service.ITipoFacturacionService;
import com.sophi.app.models.service.ITipoProyectoService;

@Controller
@SessionAttributes("recurso")
public class PreventaProyectoController {
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@Autowired
	private IAreaComercialService areaComercialService;
	
	@Autowired
	private ITipoProyectoService tipoProyectoService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IClasificacionProyectoService clasificacionproyectoService;
	
	@Autowired
	private IAgendaService agendaService;
	
	@Autowired
	private IDetalleInfraestructuraService detalleInfraestructuraService;
	
	@Autowired
	private IDetalleProyectoInfraestructuraService detalleProyectoInfraestructuraService;
	
	@Autowired
	private IDetalleClienteInfraestructuraService detalleClienteInfraestructuraService;
	
	@Autowired
	private IDetalleProyectoContactoService detalleProyectoContactoService;
	
	@Autowired
	private IDetalleClienteAreaComercialService detalleClienteAreaComercialService;
	
	@Autowired
	private ITipoFacturacionService tipoFacturacionService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IRolService rolService;
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private IRecursoTrayectoriaProyectoService recursoTrayectoriaProyectoService;
	
	@Autowired
	private IConocimientoService conocimientoService;
	
	@Autowired
	private IDetalleConocimientoProyectoService detalleConocimientoProyectoService;
	
	@RequestMapping(value = "/preventaProyecto", method = RequestMethod.GET)
	public String listarRecursos(Map<String, Object> modelP, Model model) {
		model.addAttribute("titulo", "Proyecto");
		Proyecto proyecto = new Proyecto();
		modelP.put("proyecto", proyecto);
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		model.addAttribute("recursos", recursoService.findAll());
		return "preventaProyecto";
	}
	
	@RequestMapping(value="/preventaProyectoComplemento", method = RequestMethod.POST)
	public String guardarRecurso(Map<String, Object> modelM,@Valid Proyecto proyecto, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		model.addAttribute("titulo", "Proyecto");
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		
		if(result.hasErrors()) {
			return "preventaProyecto";
		}
		Date fechaHoy = new Utiles().getFechaActual(); 
		proyecto.setFecRegistro(fechaHoy);
		
		//agrego el arrea comercial al cliente
		DetalleClienteAreaComercialId dcacId = new DetalleClienteAreaComercialId();
		DetalleClienteAreaComercial dcac = new DetalleClienteAreaComercial();
		dcacId.setCodAreaComercia(proyecto.getCodAreaComercial());
		dcacId.setCodCliente(proyecto.getCodCliente());
		
		dcac.setDetalleClienteAreaComercialId(dcacId);
		dcac.setFecRegistro(fechaHoy);
		
		detalleClienteAreaComercialService.save(dcac);
		proyectoService.save(proyecto);
		
		//obtengo el proyecto guardado
//		Proyecto proyectoN = proyectoService.findByDescProyectoAndCodClienteAndCodEstatusProyectoAndFecRegistro(proyecto.getDescProyecto(), proyecto.getCodCliente(), proyecto.getCodEstatusProyecto(), proyecto.getFecRegistro());
		Proyecto proyectoN = proyectoService.findByDescProyectoAndCodClienteAndCodEstatusProyecto(proyecto.getDescProyecto(), proyecto.getCodCliente(), proyecto.getCodEstatusProyecto());
		model.addAttribute("contactos", agendaService.findContactosBycodCliente(proyecto.getCodCliente()));
		model.addAttribute("tecnologias", detalleInfraestructuraService.findAll());
//		DetalleInfraestructura di= detalleInfraestructuraService.findAll().get(0);
		modelM.put("proyecto", proyectoN);
		flash.addFlashAttribute("success", "Recurso guardado con éxito");
		return "preventaProyectoComplemento";
	}
	
	@RequestMapping(value = "/agregarInfra/{codCliente}/{codProyecto}/{codDetalleInfra}/{codEstatusProyecto}")
	@ResponseBody
	public String agregarInfraestructura(Map<String, Object> modelM, Model model, @PathVariable(value = "codCliente") long codCliente,@PathVariable(value = "codProyecto") long codProyecto,@PathVariable(value = "codDetalleInfra") long codDetalleInfra, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto) {
		DetalleClienteInfraestructura dci = new DetalleClienteInfraestructura();
		DetalleClienteInfraestructuraId dciid = new DetalleClienteInfraestructuraId();
		DetalleProyectoInfraestructura dpi = new DetalleProyectoInfraestructura();
		DetalleProyectoInfraestructuraId dpiid = new DetalleProyectoInfraestructuraId();
		Date fechaHoy = new Date();
		
		
		dciid.setCodCliente(codCliente);
		dciid.setCodDetalleInfraestructura(codDetalleInfra);
		
		
		//dci.setDetalleClienteInfraestructuraId(dciid);
		DetalleInfraestructura di = detalleInfraestructuraService.findOne(codDetalleInfra);
		Cliente client= clienteService.findOne(codCliente);
		dci.setId(dciid);
		dci.setDetalleInfraestructura(di);
		dci.setCliente(client);
		dci.setDescVersion("");
		dci.setFecRegistro(fechaHoy);
		detalleClienteInfraestructuraService.save(dci);
		
		dpiid.setCodDetalleInfraestructura(codDetalleInfra);
		dpiid.setCodEstatusProyecto(codEstatusProyecto);
		dpiid.setCodProyecto(codProyecto);
		dpiid.setCodCliente(codCliente);
		
		//detalleProyectoInfraestructuraService.borrarByCodProyecto(codProyecto);
		dpi.setDetalleProyectoInfraestructuraId(dpiid);
		dpi.setDescVersion(dci.getDescVersion());
		dpi.setFecRegistro(fechaHoy);
		detalleProyectoInfraestructuraService.save(dpi);
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		modelM.put("proyecto", proyecto);
		
		return "preventaProyectoContactoInfraestructura";
    }
	
	@RequestMapping(value = "/eliminarInfra/{codPRoyecto}/{codEstatusProyecto}/{codCliente}")
    @ResponseBody
	public String eliminarInfra(Map<String, Object> modelM, Model model, @PathVariable(value = "codPRoyecto") long codProyecto, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, @PathVariable(value = "codEstatusProyecto") long codCliente) {
		
		detalleProyectoInfraestructuraService.borrarByCodProyecto(codProyecto);
		
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		modelM.put("proyecto", proyecto);
		return "preventaProyectoContactoInfraestructura";
    }
	
	@RequestMapping(value = "/guardarComplemento/{codProyecto}/{codContacto}/{codClasificacionProyecto}/{codEstatusProyecto}/{codCliente}")
	@ResponseBody
	public String guardarComplemento(Map<String, Object> modelM, Model model, @PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codContacto") long cod_contacto, @PathVariable(value = "codClasificacionProyecto") long codClasificacionProyecto, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, @PathVariable(value = "codCliente") long codCliente,RedirectAttributes flash) {
		DetalleProyectoContactoId dpcid = new DetalleProyectoContactoId();
		DetalleProyectoContacto dpc = new DetalleProyectoContacto();
		Date fechaHoy = new Date();
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		
		detalleProyectoContactoService.borrarByCodProyecto(codProyecto);
		//Guarda contacto
		dpcid.setCodCliente(proyecto.getCodCliente());
		dpcid.setCodContacto(cod_contacto);
		dpcid.setCodEstatusProyecto(proyecto.getCodEstatusProyecto());
		dpcid.setCodProyecto(proyecto.getCodProyecto());
		
		dpc.setValResponsableCliente(cod_contacto);
		dpc.setDetalleProyectoContactoId(dpcid);
		dpc.setFecRegistro(fechaHoy);
		
		if(cod_contacto==-1) {
			//flash.addFlashAttribute("success", "Complemento guardado con éxito sin contacto");
		}else {
			//flash.addFlashAttribute("success", "Complemento guardado con éxito con contacto");
			detalleProyectoContactoService.save(dpc);
			proyectoService.save(proyecto);
		}
		
		if(proyecto.getValPlan() == null) {
			proyecto.setValPlan(0L);
			proyectoService.save(proyecto);
		}
		
		System.out.println("ValPlan con 0");
		
		flash.addFlashAttribute("success", "Complemento guardado con éxito");
		return "redirect:/preventaProyectoConsulta/"+codProyecto+"/"+proyecto.getCodEstatusProyecto()+"/"+proyecto.getCodCliente();
    }
	
	@RequestMapping(value = "/eliminarContacto/{codProyecto}/{codEstatusProyecto}/{codCliente}")
    public String eliminarContacto(Map<String, Object> modelM, Model model, @PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, @PathVariable(value = "codCliente") long codCliente) {
		model.addAttribute("titulo", "Proyecto");
		detalleProyectoContactoService.borrarByCodProyecto(codProyecto);
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		modelM.put("proyecto", proyecto);
		return "preventaProyectoConsulta";
    }
	
	@RequestMapping(value = "/guardarContactoProyecto/{codProyecto}/{codContacto}/{codEstatusProyecto}/{codCliente}")
    public String guardarContactoProyecto(Map<String, Object> modelM, Model model, @PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codContacto") long cod_contacto, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, @PathVariable(value = "codCliente") long codCliente) {
		model.addAttribute("titulo", "Proyecto");
		DetalleProyectoContactoId dpcid = new DetalleProyectoContactoId();
		DetalleProyectoContacto dpc = new DetalleProyectoContacto();
		Date fechaHoy = new Date();
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		//Guarda contacto
		dpcid.setCodCliente(codCliente);
		dpcid.setCodContacto(cod_contacto);
		dpcid.setCodEstatusProyecto(proyecto.getCodEstatusProyecto());
		dpcid.setCodProyecto(proyecto.getCodProyecto());
		dpc.setValResponsableCliente(cod_contacto);
		dpc.setDetalleProyectoContactoId(dpcid);
		dpc.setFecRegistro(fechaHoy);
		
		modelM.put("proyecto", proyecto);
		detalleProyectoContactoService.save(dpc);	
		return "preventaProyectoConsulta";
    }
	
	@RequestMapping(value = "/listaProyectos", method = RequestMethod.GET)
	public String listarRecursos(Model model) {
		model.addAttribute("titulo", "Proyecto");
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("proyectos", proyectoService.findAll());
		//proyectoService.
		return "preventaProyectoLista";
	}
	
	@RequestMapping(value = "/cargaProyectos/{codCliente}", method = RequestMethod.GET)
	@ResponseBody
	public String cargaProyectos(Model model, @PathVariable(value = "codCliente") long codCliente) {
		model.addAttribute("clientes", clienteService.findAll());
		String contenido="";
		List<Proyecto> listaProyecto;
		
		if(codCliente==-1) {
			listaProyecto=proyectoService.findAll();
		}else{
			listaProyecto=proyectoService.findByCodCliente(codCliente);
		}
		
		contenido=contenido+"<div class=\"table-responsive\">"+
        "<table class=\"table table-bordered\" id=\"dataTable\" width=\"100%\" cellspacing=\"0\">"+
        "<thead>"+
        "<tr>"+
        "<th>Código de proyecto</th>"+
        "<th>Proyecto</th>"+
        "<th>Fecha de inicio</th>"+
        "<th>Preventa o proyecto</th>"+
        "<th>Presupuesto</th>"+
        "<th>Acciones</th>"+
        "</tr>"+
        "</thead>"+
        "<tbody>";
		String estatusP="";
		String fechaI="";
        
		for(Proyecto p : listaProyecto) {
			if(p.getCodEstatusProyecto()==1) {
				estatusP="Preventa";
				fechaI="N/A";
			}else {
				estatusP="Proyecto";
				fechaI=p.getFecIncioProyecto().toString();
			}
			contenido=contenido+"<tr>"+
			        "<td>"+p.getDescCodigoProyecto()+"</td>"+
			        "<td>"+p.getDescProyecto()+"</td>"+
			        "<td>"+fechaI+"</td>"+
			        "<td>"+estatusP+"</td>"+
			        "<td>"+p.getImpPresupuesto()+"</td>"+
			        "<td><a href=\"/preventaProyectoConsulta/"+p.getCodProyecto()+"/"+p.getCodEstatusProyecto()+"/"+p.getCodCliente()+"\">Ver detalle</a></td>"+
					"</tr>";
		}
		
		contenido=contenido+"</tbody>"+
        "</table>"+
        "</div>";
		return contenido;
	}
	
	@RequestMapping(value="/preventaProyectoUpdate", method = RequestMethod.POST)
	public String preventaProyectoUpdate(Map<String, Object> modelM,@Valid Proyecto proyecto, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		model.addAttribute("titulo", "Proyecto");
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		model.addAttribute("proyectos", proyectoService.findAll());
		if(result.hasErrors()) {
			return "preventaProyectoConsulta";
		}
		
		proyectoService.save(proyecto);
		flash.addFlashAttribute("success", "Proyecto guardado con éxito");
		return "preventaProyectoLista";
	}
	
	@RequestMapping(value = "/areaComercialCliente/{codCliente}", method = RequestMethod.GET)
	@ResponseBody
	public String areaComercialCliente(Model model, @PathVariable(value = "codCliente") long codCliente) {
		
		String contenido="";
		List<DetalleClienteAreaComercial> listaAreasComercialesCliente = detalleClienteAreaComercialService.findByDetalleClienteAreaComercialIdCodCliente(codCliente);

		for(DetalleClienteAreaComercial cac : listaAreasComercialesCliente) {
			AreaComercial ac = areaComercialService.findOne(cac.getDetalleClienteAreaComercialId().getCodAreaComercia());
			contenido=contenido+"<option value=\""+ac.getCodAreaComercial()+"\">"+ac.getDescAreaComercial()+"</option>";
		}
		
		return contenido;
	}
	
	@RequestMapping(value = "/contactoCliente/{codCliente}", method = RequestMethod.GET)
	@ResponseBody
	public String contactoCliente(Model model, @PathVariable(value = "codCliente") long codCliente) {
		List<Agenda> listaA=agendaService.findContactosBycodCliente(codCliente);
		String contenido="";

		for(Agenda a : listaA) {
			contenido=contenido+"<option value=\""+a.getCodContacto()+"\">"+a.getDescContacto()+"</option>";
		}
		
		return contenido;
	}
	
	//Comienza de nuevo
	@RequestMapping(value = "/preventaProyectoAlta", method = RequestMethod.GET)
	public String preventaProyectoAlta(Map<String, Object> modelP, Model model) {
		model.addAttribute("titulo", "Proyecto");
		Proyecto proyecto = new Proyecto();
		modelP.put("proyecto", proyecto);
//		List<Rol> listaRolesLider = rolService.findListaRoles();
		List<Recurso> listaRecursosLider = new ArrayList<Recurso>();
		List<Recurso> listaRecursosAprobador = new ArrayList<Recurso>();
		
		listaRecursosLider = recursoService.findListRecursosResponsables();
		listaRecursosAprobador = recursoService.findListRecursosAprobadores();
//		for (Rol rol : listaRolesLider) {
//			Recurso recursoLider = recursoService.findOne(rol.getCod_recurso());
//			if(recursoLider != null) {
//				listaRecursosLider.add(recursoLider);
//			}
//		}
		
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		model.addAttribute("recursosLider", listaRecursosLider);
		model.addAttribute("recursosAprobador", listaRecursosAprobador);
		return "preventaProyectoAlta";
	}
	
	@RequestMapping(value = "/listaProyectosTodo", method = RequestMethod.GET)
	public String listaProyectos(Model model) {
		model.addAttribute("titulo", "Proyecto");
		return "preventaProyectoListaTodo";
	}
	
	@RequestMapping(value = "/proyectoFiltro/{sesion}", method = RequestMethod.GET)
	public String proyectoFiltro(@PathVariable(value = "sesion") String mail, Model model) {
		Recurso recurso = new Recurso();
		recurso = recursoService.findByDescCorreoElectronico(mail);
		List<Rol> roles = new ArrayList<Rol>();
		List<Cliente> clientes = new ArrayList<Cliente>();
		if (recurso != null) {
			roles = rolService.findByCodRecurso(recurso.getCodRecurso());
			if(roles.size() > 0) {
				for (Rol rol : roles) {
					if(rol.getDescRol().equalsIgnoreCase("ROLE_ADMIN")){
						clientes = clienteService.findAll();
					} else if(rol.getDescRol().equalsIgnoreCase("ROLE_APROB")) {
						List<Long> listaClientes = new ArrayList<Long>();
						listaClientes = proyectoService.findListaClientesRecursoAprobador(recurso.getCodRecurso());
						if (listaClientes.size()>0) {
							for (Long codCliente : listaClientes) {
								clientes.add(clienteService.findOne(codCliente));
							}
						}
					} else if(rol.getDescRol().equalsIgnoreCase("ROLE_LIDER")) {
						List<Long> listaClientes = new ArrayList<Long>();
						listaClientes = proyectoService.findListaClientesRecursoLider(recurso.getCodRecurso());
						if (listaClientes.size()>0) {
							for (Long codCliente : listaClientes) {
								clientes.add(clienteService.findOne(codCliente));
							}
						}
					}
				}
			}
		}
		
		model.addAttribute("clientes", clientes);
		
		return "layout/plantilla-filtros :: proyectos-filtro";
	}
	
	@RequestMapping(value = "/proyecto/{codCliente}/{session}", method = RequestMethod.GET)
	public String listado(@PathVariable(value = "codCliente") Long codCliente, @PathVariable(value = "session") String mail,  Model model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
		List<Proyecto> listaProyectoTodo = new ArrayList<Proyecto>();
		
		List<Rol> roles = new ArrayList<Rol>();
		if (recurso != null) {
			roles = rolService.findByCodRecurso(recurso.getCodRecurso());
			if(roles.size() > 0) {
				for (Rol rol : roles) {
					if(rol.getDescRol().equalsIgnoreCase("ROLE_ADMIN")){
						if(codCliente.equals(-1L)) {
							listaProyectoTodo = proyectoService.findAll();
						} else {
							listaProyectoTodo = proyectoService.findByCodCliente(codCliente);
						}
					} else if(rol.getDescRol().equalsIgnoreCase("ROLE_APROB")) {
						if(codCliente.equals(-1L)) {
							listaProyectoTodo = proyectoService.findListaProyectosRecursoAprobadorTodos(recurso.getCodRecurso());
						} else {
							listaProyectoTodo = proyectoService.findListaProyectosRecursoAprobador(recurso.getCodRecurso(), codCliente);
						}
					} else if(rol.getDescRol().equalsIgnoreCase("ROLE_LIDER")) {
						if(codCliente.equals(-1L)) {
							listaProyectoTodo = proyectoService.findListaProyectosRecursoLiderTodos(recurso.getCodRecurso());
						} else {
							listaProyectoTodo = proyectoService.findListaProyectosRecursoLider(recurso.getCodRecurso(), codCliente);
						}
					}
				}
			}
		}
		
		Proyecto proyectoSophi= proyectoService.findByCodProyecto(1L);
		listaProyectoTodo.remove(proyectoSophi);
		
		for (Proyecto proyecto : listaProyectoTodo) {
			Recurso recursoLider = new Recurso();
			recursoLider = recursoService.findOne(proyecto.getCodRecursoLider());
			Recurso recursoAprobador = new Recurso();
			recursoAprobador = recursoService.findOne(proyecto.getCodRecursoAprobador());
			if (recursoLider != null) {
				proyecto.setNombreRecursoLider(recursoLider.getDescRecurso() + " " + recursoLider.getDescApellidoPaterno());
			}
			if (recursoAprobador != null) {
				proyecto.setNombreRecursoAprobador(recursoAprobador.getDescRecurso() + " " + recursoAprobador.getDescApellidoPaterno());
			}
		}
		
		for (Proyecto proyecto2 : listaProyectoTodo) {
			proyecto2.setNumAct(actividadService.countByCodProyecto(proyecto2.getCodProyecto()));
		}
		
		model.addAttribute("proyectos", listaProyectoTodo);
		return "layout/plantilla-filtros :: proyectos-listado";
	}
	
	
	@RequestMapping(value = "/listaMisProyectos/{codCliente}/{codLider}", method = RequestMethod.GET)
	public String listaPorLider(@PathVariable(value = "codCliente") Long codCliente, @PathVariable(value = "codLider") Long codLider, Model model) {
		
		List<Proyecto> listaProyectoTodo = proyectoService.findByCodRecursoLider(codLider);
		
		Proyecto proyectoSophi = proyectoService.findOne(1L);
		listaProyectoTodo.remove(proyectoSophi);
		
		List<Cliente> listaClientesLider = new ArrayList<Cliente>();
		for (Proyecto proyecto2 : listaProyectoTodo) {
			proyecto2.setNumAct(actividadService.countByCodProyecto(proyecto2.getCodProyecto()));
			Cliente clienteLider = clienteService.findOne(proyecto2.getCodCliente());
			listaClientesLider.remove(clienteLider);
			listaClientesLider.add(clienteLider);
		}

		model.addAttribute("proyectos", listaProyectoTodo);
		return "preventaProyectoListaTodo";
	}
	
	@RequestMapping(value = "/listaMisProyectos/{email}", method = RequestMethod.GET)
	public String listaProyectosPorLider(@PathVariable(value = "email") String email, Model model) {
		Long codLider = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
		List<Proyecto> listaProyectoTodo = proyectoService.findByCodRecursoLider(codLider);
		Proyecto proyectoSophi = proyectoService.findOne(1L);
		listaProyectoTodo.remove(proyectoSophi);
		
		List<Cliente> listaClientesLider = new ArrayList<Cliente>();
		for (Proyecto proyecto2 : listaProyectoTodo) {
			proyecto2.setNumAct(actividadService.countByCodProyecto(proyecto2.getCodProyecto()));
			Cliente clienteLider = clienteService.findOne(proyecto2.getCodCliente());
			listaClientesLider.remove(clienteLider);
			listaClientesLider.add(clienteLider);
		}
		model.addAttribute("titulo", "Proyecto");
		model.addAttribute("clientes", listaClientesLider);
//		model.addAttribute("proyectos", listaProyectoTodo);
		model.addAttribute("codLider",codLider);
		return "preventaProyectoListaTodo";
	}
	

	
	
	@RequestMapping(value = "/cargaProyectosTodo/{codCliente}", method = RequestMethod.GET)
	@ResponseBody
	public String cargaProyectosTodo(Model model, @PathVariable(value = "codCliente") long codCliente) {
		model.addAttribute("clientes", clienteService.findAll());
		String contenido="";
		List<Proyecto> listaProyectoTodo;
		Proyecto proyectoSophi = proyectoService.findByCodProyecto(1L);
		
		if(codCliente==-1) {
			listaProyectoTodo=proyectoService.findAll();
			listaProyectoTodo.remove(proyectoSophi);
			
		}else{
			listaProyectoTodo=proyectoService.findByCodCliente(codCliente);
			listaProyectoTodo.remove(proyectoSophi);
		}
		
		contenido=contenido+"<div class=\"table-responsive\">"+
        "<table class=\"table\" id=\"dataTable\" width=\"100%\" cellspacing=\"0\">"+
        "<thead>"+
        "<tr>"+
        "<th>Código de proyecto</th>"+
        "<th>Proyecto</th>"+
        "<th>Fecha de inicio</th>"+
        "<th>Preventa o proyecto</th>"+
        "<th>Presupuesto</th>"+
        "<th>Acciones</th>"+
        "</tr>"+
        "</thead>"+
        "<tbody>";
		String estatusP="";
		String fecha="";
		String presupuesto="";
        
		for(Proyecto p : listaProyectoTodo) {
			if(p.getCodEstatusProyecto()==1) {
				estatusP="Preventa";
			}else {
				estatusP="Proyecto";
			}
			
			if(p.getFecIncioProyecto()==null) {
				fecha="NA";
			}else {
				fecha=p.getFecIncioProyecto()+"";
			}
			
			if(p.getImpPresupuesto()==null) {
				presupuesto="NA";
			}else {
				presupuesto=p.getImpPresupuesto()+"";
			}
			contenido=contenido+"<tr>"+
			        "<td>"+p.getDescCodigoProyecto()+"</td>"+
			        "<td>"+p.getDescProyecto()+"</td>"+
			        "<td>"+fecha+"</td>"+
			        "<td>"+estatusP+"</td>"+
			        "<td>"+presupuesto+"</td>"+
			        "<td>" +
//			        "<a href=\"/preventaProyectoConsulta/"+p.getCodProyecto()+"/"+p.getCodEstatusProyecto()+"/"+p.getCodCliente()+"\">Ver detalle</a>" +
			        "<a href=\"/preventaProyectoConsulta/"+p.getCodProyecto()+"/"+p.getCodEstatusProyecto()+"/"+p.getCodCliente()+"\" class=\"badge badge-success\">Detalle  <i class=\"far fa-edit\"></i></a>"+
			        "</td>"+
					"</tr>";
		}
		
		contenido=contenido+"</tbody>"+
        "</table>"+
        "</div>";
		return contenido;
	}
	
	@RequestMapping(value = "/preventaProyectoConsulta/{codProyecto}/{codEstatusProyecto}/{codCliente}", method = RequestMethod.GET)
    public String preventaProyectoConsulta (Map<String, Object> modelM, Model model,@PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, @PathVariable(value = "codCliente") long codCliente) {
		model.addAttribute("titulo", "Proyecto");
		Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
		
		List<DetalleInfraestructura> listaDI= detalleInfraestructuraService.findAll();
		List<DetalleProyectoInfraestructura> listaDPI=detalleProyectoInfraestructuraService.findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(codProyecto, codEstatusProyecto, codCliente);
		List<DetalleInfraestructura> listaDISel= new ArrayList<DetalleInfraestructura>();
		List<DetalleProyectoContacto> listaDPC=detalleProyectoContactoService.findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(codProyecto, codEstatusProyecto, codCliente);
		
		for(int indice = 0;indice<listaDI.size();indice++){
		    for(int indice2 = 0;indice2<listaDPI.size();indice2++){
		    	if(listaDI.get(indice).getCodDetalleInfraestructura()==listaDPI.get(indice2).getDetalleProyectoInfraestructuraId().getCodDetalleInfraestructura()) {
		    		listaDISel.add(listaDI.get(indice));
		    		listaDI.remove(listaDI.get(indice));
		    	}
		    }
		}
		
		String texto="";
		for(int indice = 0;indice<listaDISel.size();indice++){
			texto=texto+listaDISel.get(indice).getDescDetalle()+", ";
		}
		
		
		Long codCon=0L;
		for(int indice = 0;indice<listaDPC.size();indice++){
			codCon=listaDPC.get(indice).getDetalleProyectoContactoId().getCodContacto();
		}
		
		//lista real de areas comerciales
		List<DetalleClienteAreaComercial> listaDCAC=detalleClienteAreaComercialService.findByDetalleClienteAreaComercialIdCodCliente(codCliente);
		List<AreaComercial> listaAC= areaComercialService.findAll();
		List<AreaComercial> listaAux= new ArrayList<AreaComercial>();
		for(int indice = 0;indice<listaAC.size();indice++){
			for(int indice2 = 0;indice2<listaDCAC.size();indice2++){
				if(listaAC.get(indice).getCodAreaComercial()==listaDCAC.get(indice2).getDetalleClienteAreaComercialId().getCodAreaComercia()) {
					listaAux.add(listaAC.get(indice));
				}
			}
		}
		
		List<Cliente> listaCliente= clienteService.findAll();
		List<Cliente> listaClienteAux= new ArrayList<Cliente>();
		
		for(int indice = 0;indice<listaCliente.size();indice++){
			if(listaCliente.get(indice).getCodCliente()==codCliente) {
				listaClienteAux.add(listaCliente.get(indice));
			}
		}
		
//		List<Rol> listaRolesLider = rolService.findListaRoles();
		List<Recurso> listaRecursosLider = new ArrayList<Recurso>();
		List<Recurso> listaRecursosAprobador = new ArrayList<Recurso>();
		listaRecursosLider = recursoService.findListRecursosResponsables();
		listaRecursosAprobador = recursoService.findListRecursosAprobadores();
		
//		for (Rol rol : listaRolesLider) {
//			Recurso recursoLider = recursoService.findOne(rol.getCod_recurso());
//			if(recursoLider != null) {
//				listaRecursosLider.add(recursoLider);
//			}
//		}
		
		Cliente cp = clienteService.findOne(proyecto.getCodCliente());
		modelM.put("proyecto", proyecto);
		model.addAttribute("clienteProyecto", cp);
		model.addAttribute("listaClienteProyecto", clienteService.findAll());
		model.addAttribute("clientes", listaClienteAux);
		model.addAttribute("contactos", agendaService.findContactosBycodCliente(proyecto.getCodCliente()));
		
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("tecnologias", listaDI);
		model.addAttribute("tecAcept", listaDISel);
		model.addAttribute("textoTec", texto);
		model.addAttribute("codCon", codCon);
		model.addAttribute("contactoVista", agendaService.findOne(codCon));
		model.addAttribute("recursosLider", listaRecursosLider);
		model.addAttribute("recursosAprobador", listaRecursosAprobador);
		model.addAttribute("tecTam", listaDISel.size());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		return "preventaProyectoConsulta";
    }
	
	@RequestMapping(value="/preventaProyectoCI", method = RequestMethod.POST)
	public String preventaProyectoCI(Map<String, Object> modelM,@Valid Proyecto proyecto, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		model.addAttribute("titulo", "Proyecto");
		
		if(result.hasErrors()) {
			flash.addFlashAttribute("error", "Error al guardar proyecto");
			return "redirect:/preventaProyectoAlta";
		}
		
		Date fechaHoy = new Utiles().getFechaActual(); 
		
		
		//agrego el arrea comercial al cliente
		DetalleClienteAreaComercialId dcacId = new DetalleClienteAreaComercialId();
		DetalleClienteAreaComercial dcac = new DetalleClienteAreaComercial();
		dcacId.setCodAreaComercia(proyecto.getCodAreaComercial());
		dcacId.setCodCliente(proyecto.getCodCliente());
		
		dcac.setDetalleClienteAreaComercialId(dcacId);
		dcac.setFecRegistro(fechaHoy);
				
		detalleClienteAreaComercialService.save(dcac);
		proyectoService.save(proyecto);
		
		//Se envía notificacion de asignacion al responsable y aprobador
		try {
			enviaNotificacionAsignacionAprobadorResponsable(proyecto);
		} catch (Exception e) {
			System.out.println("NOTIFICACIONES : <Error> al enviar notificacion a responsable y aprobador de nuevo proyecto");
			System.out.println(e);
		}
		
		//obtengo el proyecto guardado
		Proyecto proyectoN = proyectoService.findByDescProyectoAndCodClienteAndCodEstatusProyecto(proyecto.getDescProyecto(), proyecto.getCodCliente(), proyecto.getCodEstatusProyecto());
		model.addAttribute("contactos", agendaService.findContactosBycodCliente(proyecto.getCodCliente()));
		model.addAttribute("tecnologias", detalleInfraestructuraService.findAll());

		modelM.put("proyecto", proyectoN);
		
		if(proyectoN.getCodEstatusProyecto()==1) {
			flash.addFlashAttribute("success", "Información guardada con éxito");
			model.addAttribute("proyectos", proyectoService.findAll());
			return "redirect:/listaProyectosTodo";
		}else {
			return "preventaProyectoContactoInfraestructura";
		}
		
	}
	
	@RequestMapping(value="/preventaProyectoActualizar", method = RequestMethod.POST)
	public String preventaProyectoContactoInfraestructura(Map<String, Object> modelM,@ModelAttribute("proyecto") Proyecto proyecto,@RequestParam("fecRegistro") String fecRegistro, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		model.addAttribute("proyectos", proyectoService.findAll());
		model.addAttribute("titulo", "Proyecto");
		
		//Long clienteProyecto = proyectoService.findByCodProyecto(proyecto.getCodProyecto()).getCodCliente();
		Long clienteProyecto = proyecto.getCodCliente();
		Long codEstatusProyecto = proyecto.getCodEstatusProyecto();
		Long codProyecto = proyecto.getCodProyecto();
		
		proyecto.setCodCliente(clienteProyecto);
		
		System.out.println("Estatus del proyecto actual: "+codEstatusProyecto);
		System.out.println("Area comercial: "+proyecto.getCodAreaComercial());
		
		if(result.hasErrors()) {
			flash.addFlashAttribute("error", "Error al guardar preventa/proyecto");
			return "preventaProyectoConsulta";
		}
		
		
		//agrego el area comercial al cliente
//		Date fechaHoy = new Date(); 
		//agrego el arrea comercial al cliente
		DetalleClienteAreaComercialId dcacId = new DetalleClienteAreaComercialId();
		DetalleClienteAreaComercial dcac = new DetalleClienteAreaComercial();
		dcacId.setCodAreaComercia(proyecto.getCodAreaComercial());
		dcacId.setCodCliente(proyecto.getCodCliente());
		
		dcac.setDetalleClienteAreaComercialId(dcacId);
//		dcac.setFecRegistro(fechaHoy);
				
		detalleClienteAreaComercialService.save(dcac);
		
//		proyecto.setValTotalHorasProyecto((proyecto.getValTotalHorasProyecto() == "" || proyecto.getValTotalHorasProyecto() == null) ? "0" : proyecto.getValTotalHorasProyecto());
		
		if(codEstatusProyecto == 2) {
			//actualizo los registros que tienen el id del proyecto a estatus 2
			detalleProyectoContactoService.actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(codProyecto, codEstatusProyecto);
			
			//hago lo mismo que en el paso anterior
			detalleProyectoInfraestructuraService.actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(codProyecto, codEstatusProyecto);
			
			System.out.println("Actualizó los estatus");
		}
		
		//Si el proyecto pasa a estatus 3, se llenará automáticamente la trayectoria del recurso
		if(codEstatusProyecto == 3) {
			System.out.println("Entra a cambiar a estatus 3 y llenar las tablas de trayectoria y conocimiento");
			List<ProyectoRecurso> listPR = new ArrayList<ProyectoRecurso>();
			Cliente cliente = new Cliente();
			
			listPR = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
			cliente = clienteService.findOne(clienteProyecto);
			
			System.out.println("Tamaño de la lista: "+listPR.size());
			
			if(listPR.size() != 0) {
				for(ProyectoRecurso PR: listPR) {
					System.out.println("Datos: "+PR.getProyectoRecursoId().getCodRecurso()+", "+proyecto.getDescProyecto()+", "+proyecto.getDescProyectoTexto()+", "+PR.getFecInicioAsignacion()+", "+PR.getFecFinAsignacion()+", "+cliente.getDescCliente());
					if(PR.getFecInicioAsignacion()!=null && PR.getFecFinAsignacion()!=null) {
						recursoTrayectoriaProyectoService.insertOne(PR.getProyectoRecursoId().getCodRecurso(), proyecto.getDescProyecto(), proyecto.getDescProyectoTexto(), PR.getFecInicioAsignacion(), PR.getFecFinAsignacion(), cliente.getDescCliente());
					}
				}
			}
			
			System.out.println("Salió de recurso trayectoria proyectos");
			
			List<DetalleProyectoInfraestructura> listDPI = new ArrayList<DetalleProyectoInfraestructura>();
			listDPI = detalleProyectoInfraestructuraService.findByDetalleProyectoInfraestructuraIdCodProyecto(codProyecto);
			
			List<Long> codTrayectoriaProyecto = recursoTrayectoriaProyectoService.findCodTrayectoriaProyectoByDescProyecto(proyecto.getDescProyecto());
			
			System.out.println("Tamaño de la lista: "+listDPI.size());
			
			if(listDPI.size() != 0 && codTrayectoriaProyecto.size() != 0) {
				for(Long CTP: codTrayectoriaProyecto) {					
					for(DetalleProyectoInfraestructura DPI: listDPI) {
						System.out.println("Datos: "+CTP+", "+DPI.getDetalleProyectoInfraestructuraId().getCodDetalleInfraestructura());
						detalleConocimientoProyectoService.insertOneDCP(CTP, DPI.getDetalleProyectoInfraestructuraId().getCodDetalleInfraestructura());
					}
				}
			}
			
			System.out.println("Terminó de cambiar a estatus 3 y llenar las tablas de trayectoria y conocimiento");
		}
		
		if(codEstatusProyecto == 4) {
			//actualizo los registros que tienen el id del proyecto a estatus 4
			detalleProyectoContactoService.actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(codProyecto, codEstatusProyecto);
			
			//hago lo mismo que en el paso anterior
			detalleProyectoInfraestructuraService.actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(codProyecto, codEstatusProyecto);
			
			System.out.println("Actualizó los estatus");
		}
		
		//guardo proyecto
		
		if(proyecto.getCodEstatusProyecto().equals(2L) && proyecto.getFecCambioEstatus() == null) {
			proyecto.setFecCambioEstatus(new Utiles().getFechaActual());
		}
		
		if(proyecto.getCodEstatusProyecto().equals(3L) && proyecto.getFecCambioEstatus() != null) {
			proyecto.setFecCambioEstatus(new Utiles().getFechaActual());
		}
		
		if(proyecto.getCodEstatusProyecto().equals(4L) && proyecto.getFecCambioEstatus() != null) {
			proyecto.setFecCambioEstatus(new Utiles().getFechaActual());
		}
		
		proyectoService.save(proyecto);
		
		flash.addFlashAttribute("success", "Proyecto guardado con éxito");
		
		//redirijo dependiendo
		if(proyecto.getCodEstatusProyecto() == 3 || proyecto.getCodEstatusProyecto() == 4) {
			List<Proyecto> listaProyectoTodo = proyectoService.findAll();
			flash.addFlashAttribute("success", "Información actualizada con éxito");
			model.addAttribute("proyectos", listaProyectoTodo);
			return "redirect:/listaProyectosTodo";
		}else {
			flash.addFlashAttribute("success", "Información actualizada con éxito");
			return "redirect:/preventaProyectoConsulta/"+proyecto.getCodProyecto()+"/"+proyecto.getCodEstatusProyecto()+"/"+proyecto.getCodCliente();
		}
	}
	
	@RequestMapping(value = "/preventaProyectoContactoInfraestructura/{codProyecto}", method = RequestMethod.GET)
	public String preventaProyectoContactoInfraEstrcutoraResp(Model model, @PathVariable(value = "codProyecto") long codProyecto) {
		Proyecto proyecto = proyectoService.findByCodProyecto(codProyecto);
		List<DetalleInfraestructura> listaDI= detalleInfraestructuraService.findAll();
		List<DetalleProyectoInfraestructura> listaDPI=detalleProyectoInfraestructuraService.findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(proyecto.getCodProyecto(), proyecto.getCodEstatusProyecto(), proyecto.getCodCliente());
		List<DetalleInfraestructura> listaDISel= new ArrayList<DetalleInfraestructura>();
		for(int indice = 0;indice<listaDI.size();indice++){

		    for(int indice2 = 0;indice2<listaDPI.size();indice2++){

		    	if(listaDI.get(indice).getCodDetalleInfraestructura()==listaDPI.get(indice2).getDetalleProyectoInfraestructuraId().getCodDetalleInfraestructura()) {

		    		listaDISel.add(listaDI.get(indice));
		    		listaDI.remove(listaDI.get(indice));
		    	}
		    }
		}
		
		String texto="";
		for(int indice = 0;indice<listaDISel.size();indice++){
			texto=texto+listaDISel.get(indice).getDescDetalle()+", ";
		}
		List<DetalleProyectoContacto> listaDPC=detalleProyectoContactoService.findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(proyecto.getCodProyecto(), proyecto.getCodEstatusProyecto(), proyecto.getCodCliente());
		Long codCon=0L;
		for(int indice = 0;indice<listaDPC.size();indice++){
			codCon=listaDPC.get(indice).getDetalleProyectoContactoId().getCodContacto();
		}
		
		Long numActividades = actividadService.countByCodProyecto(proyecto.getCodProyecto());
		
		List<ProyectoRecurso> listProyectoRecurso = new ArrayList<ProyectoRecurso>();
		listProyectoRecurso = proyectoRecursoService.findByProyectoRecursoIdCodProyecto(codProyecto);
		Long numRecursosAsignados = 0L;
		numRecursosAsignados = (long) listProyectoRecurso.size();
		
		model.addAttribute("titulo", "Proyecto");
		model.addAttribute("proyecto", proyecto);
		model.addAttribute("tecnologias", listaDI);
		model.addAttribute("tecAcept", listaDISel);
		model.addAttribute("textoTec", texto);
		model.addAttribute("codCon", codCon);
		model.addAttribute("numAct",numActividades);
		model.addAttribute("numAsignados", numRecursosAsignados);
		model.addAttribute("contactos", agendaService.findContactosBycodCliente(proyecto.getCodCliente()));
		return "preventaProyectoContactoInfraestructura";
	}
	
	@RequestMapping(value="/preventaProyectoContactoInfraestructura", method = RequestMethod.POST)
	public String preventaProyectoContactoInfra(Map<String, Object> modelM,@Valid Proyecto proyecto, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		model.addAttribute("clientes", clienteService.findAll());
		model.addAttribute("areasComerciales", areaComercialService.findAll());
		model.addAttribute("tiposProyecto", tipoProyectoService.findAll());
		model.addAttribute("tiposFacturacion", tipoFacturacionService.findAll());
		model.addAttribute("clasificacionesProyecto", clasificacionproyectoService.findAll());
		
		if(result.hasErrors()) {
			return "preventaProyecto";
		}
		Date fechaHoy = new Utiles().getFechaActual(); 
		proyecto.setFecRegistro(fechaHoy);
		
		//agrego el arrea comercial al cliente
		DetalleClienteAreaComercialId dcacId = new DetalleClienteAreaComercialId();
		DetalleClienteAreaComercial dcac = new DetalleClienteAreaComercial();
		dcacId.setCodAreaComercia(proyecto.getCodAreaComercial());
		dcacId.setCodCliente(proyecto.getCodCliente());
		
		dcac.setDetalleClienteAreaComercialId(dcacId);
		dcac.setFecRegistro(fechaHoy);
		
		detalleClienteAreaComercialService.save(dcac);
		proyectoService.save(proyecto);
		
		//obtengo el proyecto guardado
//		Proyecto proyectoN = proyectoService.findByDescProyectoAndCodClienteAndCodEstatusProyectoAndFecRegistro(proyecto.getDescProyecto(), proyecto.getCodCliente(), proyecto.getCodEstatusProyecto(), proyecto.getFecRegistro());
		Proyecto proyectoN = proyectoService.findByDescProyectoAndCodClienteAndCodEstatusProyecto(proyecto.getDescProyecto(), proyecto.getCodCliente(), proyecto.getCodEstatusProyecto());
		model.addAttribute("contactos", agendaService.findContactosBycodCliente(proyecto.getCodCliente()));
		model.addAttribute("tecnologias", detalleInfraestructuraService.findAll());

		List<DetalleProyectoContacto> listaDPC=detalleProyectoContactoService.findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(proyecto.getCodProyecto(), proyecto.getCodEstatusProyecto(), proyecto.getCodCliente());
		Long codCon=0L;
		for(int indice = 0;indice<listaDPC.size();indice++){
			codCon=listaDPC.get(indice).getDetalleProyectoContactoId().getCodContacto();
		}
		
		model.addAttribute("titulo", "Proyecto");
		modelM.put("proyecto", proyectoN);
		model.addAttribute("codCon", codCon);
		flash.addFlashAttribute("success", "Recurso guardado con éxito");
		return "preventaProyectoContactoInfraestructura";
	}
	
	
	//Asignación de proyecto (aprobador y lider)
	public void enviaNotificacionAsignacionAprobadorResponsable(Proyecto proy) {
		
		Recurso aprobador = recursoService.findOne(proy.getCodRecursoAprobador());
		Recurso responsable = recursoService.findOne(proy.getCodRecursoLider());
		
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(aprobador.getDescRecurso());
		request.setSubject("Nueva asignación - Aprobador");
		request.setTo(aprobador.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Has sido asignado como aprobador del proyecto \""+ proy.getDescProyecto() + "\"</h3>.");
		model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-status.png\">");
		model.put("pie", "");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
		//Aprobador FIN
		
		//Responsable INICIO
		MailRequest request2 = new MailRequest();
		request2.setName(responsable.getDescRecurso());
		request2.setSubject("Nueva asignación - Responsable");
		request2.setTo(responsable.getDescCorreoElectronico());
		
		Map<String, Object> model2 = new HashMap<String, Object>();
		model2.put("nombreRecurso", request2.getName());
		model2.put("mensaje", "<h3>Has sido asignado como responsable del proyecto \""+ proy.getDescProyecto() + "\".</h3>");
		model2.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-status.png\">");
		model2.put("pie", "");
		
		MailResponse response2 = service.sendEmail(request2, model2);
		System.out.println(response2.getMessage());
		//Responsable FIN
		
	}

}
