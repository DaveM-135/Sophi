package com.sophi.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.sophi.app.models.entity.ContactoEmergencia;
import com.sophi.app.models.entity.Equipo;
import com.sophi.app.models.entity.Herramienta;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoEscolaridad;
import com.sophi.app.models.service.IEstadoCivilService;
import com.sophi.app.models.service.IEtapaEscolarService;
import com.sophi.app.models.service.IGradoEscolarService;
import com.sophi.app.models.service.IAreaRecursoService;
import com.sophi.app.models.service.IContactoEmergenciaService;
import com.sophi.app.models.service.IDocumentacionGeneralService;
import com.sophi.app.models.service.IEquipoService;
import com.sophi.app.models.service.IHerramientaService;
import com.sophi.app.models.service.IJornadaService;
import com.sophi.app.models.service.IParentescoService;
import com.sophi.app.models.service.IPerfilRecursoService;
import com.sophi.app.models.service.IProveedorService;
import com.sophi.app.models.service.IPuestoService;
import com.sophi.app.models.service.IRecursoEscolaridadService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRecursoTrayectoriaProyectoService;
import com.sophi.app.models.service.IRolService;
import com.sophi.app.models.service.ITipoHerramientaService;
import com.sophi.app.models.service.ITipoRecursoService;
import com.sophi.app.models.service.ITipoSangreService;


@Controller
@SessionAttributes("recurso")
public class RecursoController {
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IProveedorService proveedorService;
	
	@Autowired
	private IPuestoService puestoService;
	
	@Autowired
	private ITipoRecursoService tipoRecursoService;
	
	@Autowired
	private IJornadaService jornadaService;
	
	@Autowired
	private IEstadoCivilService estadoCivilService;
	
	@Autowired
	private ITipoSangreService tipoSangreService;
  
	@Autowired
	private IAreaRecursoService areaRecursoService;
	
	@Autowired
	private IHerramientaService herramientaService;
	
	@Autowired
	private IRecursoEscolaridadService recursoEscolaridadService;
	
	@Autowired
	private IGradoEscolarService gradoEscolarService;
	
	@Autowired
	private IEtapaEscolarService estapaEscolarService;
	
	@Autowired
	private IParentescoService parentescoService;
	
	@Autowired
	private IContactoEmergenciaService contactoEmergenciaService;
	
	@Autowired
	private ITipoHerramientaService tipoHerramientaService;
	
	@Autowired
	private IDocumentacionGeneralService documentacionGeneralService;
	
	@Autowired
	private IPerfilRecursoService perfilRecursoService;
	
	@Autowired
	private IEquipoService equipoService;
	
	@Autowired
	private IRecursoTrayectoriaProyectoService recursoTrayectoriaProyectoService;
	
	@Autowired
	private IRolService rolService;
	
	@GetMapping(value = "/verRecurso/{id}")
	public String verRecurso(@PathVariable(value="id") Long codRecurso, Map<String, Object> model, RedirectAttributes flash, HttpServletResponse response) {
		response.setContentType("image/jpeg");
		Recurso recurso = recursoService.findOne(codRecurso);
		Herramienta herramienta = new Herramienta();
		if (codRecurso > 0) {
			recurso = recursoService.findOne(codRecurso);
			if(recurso == null) {
				flash.addFlashAttribute("error", "Ups!");
				return "redirect:/listarRecursos";
			}
		} else {
			flash.addFlashAttribute("error", "Ups!");
			return "redirect:/listarRecursos";
		}
		
		
		List<RecursoEscolaridad> listaEscolaridad = recursoEscolaridadService.findByCodRecurso(codRecurso);
		
		model.put("recurso",recurso);
		model.put("recursoEdit",recurso);
		model.put("areaRecursoList", areaRecursoService.findAll());
		model.put("puestoList", puestoService.findAll());
		model.put("jornadaList", jornadaService.findAll());
		model.put("tipoRecursoList", tipoRecursoService.findAll());
		model.put("proveedorList",proveedorService.findAll());
		model.put("perfilRecursoList", perfilRecursoService.findAll());
		model.put("titulo", "Información de " + recurso.getDescRecurso());
		model.put("listaEstadoCivil", estadoCivilService.findAll());
		model.put("listaTipoSangre", tipoSangreService.findAll());
		model.put("herramienta", herramienta);
		model.put("listaEscolaridad", listaEscolaridad);
		model.put("listaGradoEscolar", gradoEscolarService.findAll());
		model.put("listaEtapaEscolar", estapaEscolarService.findAll());
		model.put("listaContactoEmergencia", contactoEmergenciaService.findByCodRecurso(codRecurso));
		model.put("listaParentesco", parentescoService.findAll());
		model.put("listaHerramientas", herramientaService.findByCodRecurso(codRecurso));
		
		// Herramientas - tipo y cat de equipos
		model.put("tipoHerramientaList", tipoHerramientaService.findAll());
		model.put("listaEquipos", equipoService.findListEquiposDisponibles());
		model.put("listaEquiposTodo", equipoService.findAll());
		
		model.put("listaDocumentacion", documentacionGeneralService.findAll());
		
		model.put("listaTrayectoriaProyectos", recursoTrayectoriaProyectoService.findByCodRecurso(codRecurso));
		model.put("perfil", perfilRecursoService.findByCodPerfil(recurso.getCodPerfil()));
//		System.out.println(recurso.getPerfilRecurso().getCodPerfil());
		
		model.put("listAdminRh",rolService.getListaAdminRh());
		
		
		return "verRecurso";
	}
	
	
	@GetMapping(value = "/fotoRecursoPerfil/{id}")
	public void verFotoRecursoPerfil(@PathVariable(value="id") Long codRecurso,  HttpServletResponse response) throws IOException{
		response.setContentType("image/jpeg");
		Recurso recurso = recursoService.findOne(codRecurso);
		InputStream is = new ByteArrayInputStream(recurso.getFoto());
		IOUtils.copy(is, response.getOutputStream());
	}
	
	@GetMapping(value = "/fotoRecursoCv/{id}")
	public void verFotoRecursoCv(@PathVariable(value="id") Long codRecurso,  HttpServletResponse response) throws IOException{
		response.setContentType("image/jpeg");
		Recurso recurso = recursoService.findOne(codRecurso);
		InputStream is = new ByteArrayInputStream(recurso.getFotoCv());
		IOUtils.copy(is, response.getOutputStream());
	}
	
	@RequestMapping(value = "/listarRecursos", method = RequestMethod.GET)
	public String listarRecursos(Model model) {
		model.addAttribute("titulo", "Listado de recursos");
		model.addAttribute("recursos", recursoService.findAll());
		model.addAttribute("listAdminRh",rolService.getListaAdminRh());
		return "listaRecursos";
	}
	
	@RequestMapping(value = "/formRecurso")
	public String crearRecurso(Map<String, Object> model) {
		Recurso recurso = new Recurso();
//		Utiles util = new Utiles();
		model.put("recurso", recurso);
		model.put("areaRecursoList", areaRecursoService.findAll());
		model.put("puestoList", puestoService.findAll());
		model.put("jornadaList", jornadaService.findAll());
		model.put("tipoRecursoList", tipoRecursoService.findAll());
		model.put("proveedorList",proveedorService.findAll());
		model.put("perfilRecursoList", perfilRecursoService.findAll());
		model.put("listaEstadoCivil", estadoCivilService.findAll());
		model.put("listaTipoSangre", tipoSangreService.findAll());
		return "formRecurso";
	}
	
	@RequestMapping(value="/guardaActualizaRecurso", method = RequestMethod.POST)
	@ResponseBody
	public String guardarActualizaRecurso(@Valid Recurso recurso, Model model) {
		System.out.println("entra a guardar");
		
		if(recurso.getValActivo() == 1) {
			recurso.setFecSalidaEmpresa(null);
			recurso.setDescMotivoSalida(null);
		}
		
		recursoService.save(recurso);
		return "OK";
	}
	
	@RequestMapping(value="/formRecurso", method = RequestMethod.POST)
	public String guardarRecurso(@ModelAttribute Recurso recurso, MultipartFile fotoPerfil, Model model ,SessionStatus status) {
		InputStream imgPath = null;
		byte[] bytesFotoPerfil = null;
		try {
			if(fotoPerfil.getSize() == 0){
				URL url = new URL("https://"+new Utiles().getHostName()+".com/img/defaultUser.png");
				//URL url = new URL("https://sophitech.herokuapp.com/img/fotos_nuevas/7_300x288.jpg");
				imgPath = url.openStream();
				bytesFotoPerfil = IOUtils.toByteArray(imgPath);
				recurso.setFoto(bytesFotoPerfil);
			} else {
				bytesFotoPerfil = fotoPerfil.getBytes();
				recurso.setFoto(bytesFotoPerfil);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		recursoService.save(recurso);
		status.setComplete();
		return "redirect:listarRecursos";
	}
	
	@RequestMapping(value = "/formRecurso/{id}")
	public String editarRecurso(@PathVariable(value = "id") Long codRecurso, Map<String, Object> model, RedirectAttributes flash) {
		Recurso recurso = null;
		if (codRecurso > 0) {
			recurso = recursoService.findOne(codRecurso);
			if(recurso == null) {
				flash.addFlashAttribute("error", "El codigo del recurso no existe en base de datos!");
				return "redirect:/listarRecursos";
			}
		} else {
			flash.addFlashAttribute("error", "El codigo del recurso no debe ser cero!");
			return "redirect:/listarRecursos";
		}
		model.put("recurso", recurso);
		model.put("titulo", "Formulario recurso");
		Utiles util = new Utiles();
		model.put("etapaList", util.recursosEtapaList());
		model.put("puestoList", puestoService.findAll());
		model.put("jornadaList", jornadaService.findAll());
		model.put("tipoRecursoList", tipoRecursoService.findAll());
		model.put("proveedorList",proveedorService.findAll());
		model.put("estadoCivilList", estadoCivilService.findAll());
		return "formRecurso";
	}
	
	
	@RequestMapping(value="/guardaEscolaridad",method = RequestMethod.GET)
	@ResponseBody
	public String guardaEscolaridad(@RequestParam Long cre, @RequestParam Long cr, 
			@RequestParam String ia, 
			@RequestParam Long ge,
			@RequestParam Long ee,  
			@RequestParam String cp,
			@RequestParam String ca,
			@RequestParam String fi,
			@RequestParam String ff, Model model) {
		
		
		System.out.println("cre"  + cre);
		if (cre == null) {
			RecursoEscolaridad re = new RecursoEscolaridad();
			re.setCodEtapaEscolar(ee);
			re.setDescInstitucionAcademica(ia);
			re.setDescCedulaProfesional(cp);
			re.setCodGradoEscolar(ge);
			re.setCodRecurso(cr);
			re.setDescCarrera(ca);
			re.setFecInicio(fi);
			re.setFecFin(ff);
			recursoEscolaridadService.save(re);
		} else {
		RecursoEscolaridad re =	recursoEscolaridadService.findById(cre);
		re.setCodEtapaEscolar(ee);
		re.setDescInstitucionAcademica(ia);
		re.setDescCedulaProfesional(cp);
		re.setCodGradoEscolar(ge);
		re.setCodRecurso(cr);
		re.setDescCarrera(ca);
		re.setFecInicio(fi);
		re.setFecFin(ff);
		recursoEscolaridadService.save(re);
		}
		
		return "ok";

	}
	
	@RequestMapping(value="/obtieneEscolaridad",method = RequestMethod.GET)
	public String obtieneEscolaridad(@RequestParam Long codRecurso, Model model) {
		
		model.addAttribute("listaGradoEscolar", gradoEscolarService.findAll());
		model.addAttribute("listaEtapaEscolar", estapaEscolarService.findAll());
		
		List<RecursoEscolaridad> listaEscolaridad = recursoEscolaridadService.findByCodRecurso(codRecurso);
		model.addAttribute("listaEscolaridad", listaEscolaridad);
		return "verRecurso :: fragmentEscolaridad";
	}
	
	@RequestMapping(value="/obtieneEscolaridadUnica",method = RequestMethod.GET)
	@ResponseBody
	public List<String> obtieneEscolaridadUnica(@RequestParam Long ce, Model model) {
		RecursoEscolaridad recursoEscolaridad = recursoEscolaridadService.findById(ce);
		List<String> listaDetalle = new ArrayList<String>();
		listaDetalle.add(recursoEscolaridad.getCodRecursoEscolaridad().toString());
		listaDetalle.add(recursoEscolaridad.getDescInstitucionAcademica());
		listaDetalle.add(recursoEscolaridad.getDescCarrera());
		listaDetalle.add(recursoEscolaridad.getGradoEscolar().getCodGradoEscolaridad().toString());
		listaDetalle.add(recursoEscolaridad.getEtapaEscolar().getCodEtapaEscolar().toString());
		listaDetalle.add(recursoEscolaridad.getFecInicio());
		listaDetalle.add(recursoEscolaridad.getFecFin());
		listaDetalle.add(recursoEscolaridad.getDescCedulaProfesional());
		return listaDetalle;
	}
	
	@RequestMapping(value="/borrarEscolaridad",method = RequestMethod.GET)
	@ResponseBody
	public String borrarEscolaridad(@RequestParam Long cre, Model model) {
		recursoEscolaridadService.delete(cre);
		return "ok";

	}
	
	@RequestMapping(value="/guardaContactoEmergencia",method = RequestMethod.GET)
	@ResponseBody
	public String guardaContactoEmergencia(@RequestParam Long crc, @RequestParam String nc, 
			@RequestParam String tc, 
			@RequestParam Long pc,
			@RequestParam Long ed,  
			@RequestParam Long cr, Model model) {
		
		
		if (crc == null) {
			ContactoEmergencia ce = new ContactoEmergencia();
			ce.setDescNombreContacto(nc);
			ce.setCodParentesco(pc);
			ce.setValDependienteEconomico(ed);
			ce.setCodRecurso(cr);
			ce.setDescTelContactoEmergencia(tc);
			contactoEmergenciaService.save(ce);
		}else {
			ContactoEmergencia ce = contactoEmergenciaService.findOne(crc);
			ce.setDescNombreContacto(nc);
			ce.setCodParentesco(pc);
			ce.setValDependienteEconomico(ed);
			ce.setCodRecurso(cr);
			ce.setDescTelContactoEmergencia(tc);
			contactoEmergenciaService.save(ce);
		}
    
		return "ok";

	}
	
	@RequestMapping(value="/obtieneContactosEmergencia",method = RequestMethod.GET)
	public String obtieneContactosEmergencia(@RequestParam Long codRecurso, Model model) {
		
		model.addAttribute("listaContactoEmergencia", contactoEmergenciaService.findByCodRecurso(codRecurso));
		model.addAttribute("listaParentesco", parentescoService.findAll());
		
		return "verRecurso :: fragmentContactoEmergencia";
	}
	
	@RequestMapping(value="/obtieneContactosEmergenciaUnico",method = RequestMethod.GET)
	@ResponseBody
	public List<String> obtieneContactosEmergenciaUnico(@RequestParam Long ce, Model model) {
		
		ContactoEmergencia contactoEmergencia = contactoEmergenciaService.findOne(ce);
		List<String> listaDetalle = new ArrayList<String>();
		listaDetalle.add(contactoEmergencia.getCodContactoEmergencia().toString());
		listaDetalle.add(contactoEmergencia.getDescNombreContacto());
		listaDetalle.add(contactoEmergencia.getDescTelContactoEmergencia());
		listaDetalle.add(contactoEmergencia.getValDependienteEconomico().toString());
		listaDetalle.add(contactoEmergencia.getParentesco().getCodParentesco().toString());

		return listaDetalle;
	}
	
	@RequestMapping(value="/borrarContactoEmergencia",method = RequestMethod.GET)
	@ResponseBody
	public String borrarContactoEmergencia(@RequestParam Long cre, Model model) {
		ContactoEmergencia ce = contactoEmergenciaService.findOne(cre);
		contactoEmergenciaService.delete(ce);
		return "ok";
	}
	
// Herramientas
	@RequestMapping(value="/guardaHerramienta",method = RequestMethod.GET)
	@ResponseBody
	public String guardaHerramienta(@RequestParam Long codHerramientaRecurso,
			@RequestParam Long codHerramienta, @RequestParam Long codRecurso, 
			@RequestParam String observaciones, 
			@RequestParam("responsiva") MultipartFile responsiva, 
			@RequestParam String fecPrestamoString,
			@RequestParam String fecDevolucionString, Model model) {
		
		System.out.println(codHerramienta + " "+ codRecurso + " " +observaciones + " " +fecPrestamoString+" "+fecDevolucionString);
		
		Equipo equipo = equipoService.findByCodHerramienta(codHerramienta);
		
		equipo.setCodEstadoHerramienta(2L);
		
		if(observaciones == null) {
			observaciones = "";
		}
		
		if(fecDevolucionString == "") {
			fecDevolucionString = "1990-01-01";
		}
		
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date fecPrestamo = null;
		Date fecDevolucion = null;
		byte[] bytesResponsiva = null;
		
		try {
			fecPrestamo = formato.parse(fecPrestamoString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			fecDevolucion = formato.parse(fecDevolucionString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(!responsiva.isEmpty()) {
			try {
				bytesResponsiva = responsiva.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (codHerramientaRecurso == null){
			Herramienta h = new Herramienta();
			h.setCodHerramienta(codHerramienta);
			h.setDescObservaciones(observaciones);
			h.setResponsiva(bytesResponsiva);
			h.setFecPrestamo(fecPrestamo);
			h.setFecDevolucion(fecDevolucion);
			h.setCodRecurso(codRecurso);
			herramientaService.save(h);
		} else {
			Herramienta h = herramientaService.findOne(codHerramientaRecurso);
			h.setCodHerramienta(codHerramienta);
			h.setDescObservaciones(observaciones);
			h.setResponsiva(bytesResponsiva);
			h.setFecPrestamo(fecPrestamo);
			h.setFecDevolucion(fecDevolucion);
			h.setCodRecurso(codRecurso);
			herramientaService.save(h);
		}
		
		return "ok";

	}
	
	
	@RequestMapping(value="/obtieneHerramienta",method = RequestMethod.GET)
	public String obtieneHerramienta(@RequestParam Long codRecurso, Model model) {
		
		model.addAttribute("listaHerramientas", herramientaService.findByCodRecurso(codRecurso));
		model.addAttribute("listaEquiposTodo", equipoService.findAll());
		
		return "verRecurso :: fragmentHerramientas";
	}
	
	@RequestMapping(value="/obtieneHerramientaUnico",method = RequestMethod.GET)
	@ResponseBody
	public List<String> obtieneHerramientaUnico(@RequestParam Long chr, Model model) {
		
		Herramienta herramienta = herramientaService.findOne(chr);
		
		List<String> listaDetalle = new ArrayList<String>();
		listaDetalle.add(herramienta.getCodHerramientaRecurso().toString());
		listaDetalle.add(herramienta.getCodHerramienta().toString());
		listaDetalle.add(herramienta.getDescObservaciones());
		listaDetalle.add(herramienta.getFecPrestamo().toString());
		listaDetalle.add(herramienta.getFecDevolucion().toString());
		return listaDetalle;
	}
	
	@RequestMapping(value="/borrarHerramienta",method = RequestMethod.GET)
	@ResponseBody
	public String borrarHerramienta(@RequestParam Long ch, Model model) {
		Herramienta h = herramientaService.findOne(ch);
		Equipo equipo = equipoService.findByCodHerramienta(h.getCodHerramienta());
		equipo.setCodEstadoHerramienta(1L);
		herramientaService.delete(h);
		
		return "ok";
	}
}
