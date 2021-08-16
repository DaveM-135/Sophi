package com.sophi.app.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.DetalleSolicitud;
import com.sophi.app.models.entity.DiaFestivo;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoVacaciones;
import com.sophi.app.models.entity.Rol;
import com.sophi.app.models.entity.SolicitudVacaciones;
import com.sophi.app.models.service.IDiaFestivoService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRecursoVacacionesService;
import com.sophi.app.models.service.IRolService;
import com.sophi.app.models.service.ISolicitudVacacionesService;
import com.sophi.app.models.service.RecursoVacacionesServiceImpl;
import com.sophi.app.models.service.RolServiceImpl;

@Controller
public class VacacionesController {
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IRecursoVacacionesService recursoVacacionesService;
	
	@Autowired
	private ISolicitudVacacionesService solicitudVacacionesService;
	
	@Autowired
	private IProyectoRecursoService proyectoRecursoService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private IDiaFestivoService diaFestivoService;
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IRolService rolService;

	@GetMapping({"/misVacaciones/{mail}"})
	public String listadoVacaciones(@PathVariable String mail, Model model) {
		
		Recurso recurso = null;
		recurso = recursoService.findByDescCorreoElectronico(mail);
		
		List<SolicitudVacaciones> listaSolicitudes = new ArrayList<SolicitudVacaciones>();
		
		Long contador = 0L;
		
		RecursoVacaciones recursoVacaciones = null;
		
		if (recurso != null) {
			recursoVacaciones = recursoVacacionesService.findById(recurso.getCodRecurso());
			listaSolicitudes = solicitudVacacionesService.findByCodRecurso(recurso.getCodRecurso());
			for (SolicitudVacaciones solicitudVacaciones : listaSolicitudes) {
				contador++;
				solicitudVacaciones.setContador(contador);
			}
		}
		
		model.addAttribute("codRecurso", recurso.getCodRecurso());
		model.addAttribute("recursoVacaciones", recursoVacaciones);
		model.addAttribute("listaSolicitudes", listaSolicitudes);
		return "listaVacaciones";
	}
	
	@GetMapping({"/verDetalleSolicitud"})
	@ResponseBody
	public String verDetalleSolicitud(@RequestParam Long idSolicitud, Model model) {
		
		SolicitudVacaciones solicitud = null;
		solicitud = solicitudVacacionesService.findById(idSolicitud);
		
		StringBuilder result = new StringBuilder();
		result.append("");
		
		if (solicitud != null) {
			result.append("<br><h5>días solicitados:</h5>");
			result.append("<p>");
			for (DetalleSolicitud detalleSolicitud : solicitud.getDetallesSolicitud()) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				result.append(format.format(detalleSolicitud.getFecDiaSolicitado()));
				result.append("<br>");
			}
		}
		
		return result.toString();
	}
	
	@GetMapping({"/solicitudVacaciones/{codRecurso}"})
	public String solicitudVacaciones(@PathVariable Long codRecurso, Model model) {
		System.out.println(codRecurso);
		List<ProyectoRecurso> listaPR = new ArrayList<>();
		StringBuilder strb = new StringBuilder();
		strb.append("<ul style=\"margin:0px;\">");
		
		
		RecursoVacaciones recursoVacaciones = null;
		recursoVacaciones = recursoVacacionesService.findById(codRecurso);
		
		List<Recurso> listaRecursosAprobadores = new ArrayList<Recurso>();
		listaRecursosAprobadores = recursoService.findListRecursosAprobadoresBKP(codRecurso);
		
		Recurso recurso = recursoService.findOne(codRecurso);
		Long valAprobador  = recurso.getValAprobador();
		
		String pattern = "yyyyMMdd";
		DateFormat df = new SimpleDateFormat(pattern);
		String hoyVal = df.format(new Date(new Utiles().getFechaActual().getTime() + (1000 * 60 * 60 * 24 * 1) ));
//		String hoyVal = df.format(new Date(new Utiles().getFechaActual().getTime()));
		System.out.println(hoyVal);
		List<String> aprobadores = new ArrayList<String>();
		
		listaPR = proyectoRecursoService.findProyectoRecursoActivo(codRecurso);
		for (ProyectoRecurso proyectoRecurso : listaPR) {
			Proyecto proyecto = proyectoService.findByCodProyectoAndCodEstatusProyecto(proyectoRecurso.getProyectoRecursoId().getCodProyecto(), 2L);
			if(proyecto != null  && proyecto.getCodEstatusProyecto() == 2) {				
				strb.append("<li>" + proyecto.getDescProyecto());
				strb.append(" del ");
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				if(proyecto.getFecIncioProyecto() != null) {
					strb.append(format.format(proyecto.getFecIncioProyecto()));
				}
				else {
					strb.append("N/A");
				}
				strb.append(" al ");
				if(proyecto.getFecFinProyecto() != null) {
					strb.append(format.format(proyecto.getFecFinProyecto()));
				}
				else {
					strb.append("N/A");
				}
				strb.append(".</li>");
				aprobadores.add(recursoService.findOne(proyecto.getCodRecursoAprobador()).getDescCorreoElectronico());
			}
			
		}
		
		strb.append("</ul>");
		
		 Set<String> hashSet = new HashSet<String>(aprobadores);
		 aprobadores.clear();
		 aprobadores.addAll(hashSet);
		 
		 StringBuilder aprob = new StringBuilder();
		 
		 for (String string : hashSet) {
			 aprob.append(string + ",");
		}
		 

		model.addAttribute("recursoVacaciones", recursoVacaciones);
		model.addAttribute("hoyVal",hoyVal);
		model.addAttribute("informacion",strb.toString());
		model.addAttribute("aprobadores",aprob.toString());
		model.addAttribute("listaAprobadores", listaRecursosAprobadores);
		model.addAttribute("valAprobador", valAprobador);
		return "formSolicitudVacaciones";		
	}
	
	@GetMapping({"/confirmarSolicitud"})
	@ResponseBody
	public String confirmarSolicitud(@RequestParam Long codRecurso,
										@RequestParam List<String> diasSelec,
										@RequestParam String usr,
										@RequestParam Long totalSolicitud,
										@RequestParam String aprobadores,
										//@RequestParam Long recursoBKP,
										Model model) {
		
		List<DetalleSolicitud> detallesSolicitud = new ArrayList<>();
		
		if(diasSelec.size()>0){
			if(!diasSelec.get(0).equals("[]")) {
				for (String idDia : diasSelec) {
					String codDia = null;
//						codDia = Long.parseLong(codDia.replace("\"", "").replace("[", "").replace("]", ""));
					codDia = idDia.replace("\"", "").replace("[", "").replace("]", "");
					DetalleSolicitud detalle = new DetalleSolicitud();
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					try {
						detalle.setFecDiaSolicitado(format.parse(codDia));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					detallesSolicitud.add(detalle);
				}
			}
		}
		
		RecursoVacaciones recursoVacaciones = recursoVacacionesService.findById(codRecurso);
		recursoVacaciones.setValDisponibles(recursoVacaciones.getValDisponibles() - totalSolicitud);
		
		SolicitudVacaciones solicitud = new SolicitudVacaciones();
		solicitud.setCodRecurso(codRecurso);
		solicitud.setValDiasSolicitados(totalSolicitud);
		solicitud.setFecSolicitud(new Utiles().getFechaActual());
		solicitud.setValPeriodo(recursoVacaciones.getValPeriodo());
		solicitud.setDetallesSolicitud(detallesSolicitud);
		
		solicitudVacacionesService.save(solicitud);
		
		recursoVacacionesService.save(recursoVacaciones);
		
		System.out.println(aprobadores);
		//System.out.println("Recurso BKP: "+recursoBKP);
		
		List<ProyectoRecurso> listaProyectoRecurso = new ArrayList<ProyectoRecurso>();
		listaProyectoRecurso = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(codRecurso);
		
		for(ProyectoRecurso proyectorecurso: listaProyectoRecurso) {
			Proyecto proyecto = proyectoService.findByCodProyecto(proyectorecurso.getProyectoRecursoId().getCodProyecto());
			proyecto.setCodRecursoAprobadorBKP(0L);
			proyectoService.save(proyecto);
		}
		
		for (String mailAprobador : aprobadores.split(",")) {
		//Mail Notificacion INICIO 
		Recurso recurso = recursoService.findOne(codRecurso);
		Recurso recursoAprobador = recursoService.findByDescCorreoElectronico(mailAprobador);
		MailRequest request = new MailRequest();
		System.out.println(recursoAprobador.getDescRecurso());
		request.setName(recursoAprobador.getDescRecurso());
		request.setSubject("Nueva solicitud de vacaciones");
		request.setTo(recursoAprobador.getDescCorreoElectronico());
		
		Map<String, Object> modelM = new HashMap<String, Object>();
		modelM.put("nombreRecurso", request.getName());
		modelM.put("mensaje", "<h3>Nueva solicitud de vacaciones por responder de \""+ recurso.getDescRecurso() + " " + recurso.getDescApellidoPaterno() + "\"</h3>.");
		modelM.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-banca.png\">");
		modelM.put("btnLink", "<a href=\"https://sophitech.herokuapp.com/aprobacionVacaciones/" +mailAprobador+" \" style=\"text-align: center; border-radius: 5px; font-weight: bold; background-color: #C02C57; color: white; padding: 14px 25px; text-decoration: none; display: inline-block; \">Ver detalle</a>");
		modelM.put("pie", "");
		
		MailResponse response = service.sendEmailEvaluador(request, modelM);
		System.out.println(response.getMessage());
		//Mail Notificacion FIN 
		}
		
		//Mail Notificacion INICIO recurso BKP
		/*Recurso recursobackup = recursoService.findOne(recursoBKP);
		MailRequest requestbkp = new MailRequest();
		System.out.println(recursobackup.getDescRecurso());
		requestbkp.setName(recursobackup.getDescRecurso());
		requestbkp.setSubject("Recurso Backup");
		requestbkp.setTo(recursobackup.getDescCorreoElectronico());
		Map<String, Object> modelBKP = new HashMap<String, Object>();
		modelBKP.put("nombreRecurso", requestbkp.getName());
		modelBKP.put("mensaje", "<h3>\""+ recursoVacaciones.getNombreRecurso() + " te ha asignado como recurso backup\"</h3>.");
		modelBKP.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-banca.png\">");
		modelBKP.put("pie", "");
		MailResponse response = service.sendEmailEvaluador(requestbkp, modelBKP);
		System.out.println(response.getMessage());*/
		//Mail Notificacion FIN recurso BKP
		
		return "/misVacaciones/"+usr;
	}
	
	
	@GetMapping({"/validarDiaLaboral"})
	@ResponseBody
	public String validarDiaLaboral(@RequestParam Long codDia, Model model) {
		List<DiaFestivo> diasFestivos = null;
		diasFestivos = diaFestivoService.findEsNoLaboral(codDia);
		if (diasFestivos.size() > 0 ) {
			String desc = "";
			for (DiaFestivo df : diasFestivos) {
				desc += df.getDescDiaFestivo() + " ";
			}
			return desc;
		} else {
			return "0";
		}
		
	}
	
	
	
	@GetMapping("/controlVacaciones")
	public String controlVacaciones(Model model) {
		return "vacacionesResumen";
	}
	
	
	@GetMapping("/vacacionesResumen")
	public String vacacionesResumen(@RequestParam String mail, Model model) {
		Recurso recurso = recursoService.findByDescCorreoElectronico(mail);
		if(recurso != null) {
			List<Rol> listaRolesRecurso =  rolService.findByCodRecurso(recurso.getCodRecurso());
			if(listaRolesRecurso != null && listaRolesRecurso.size() > 0) {
				for (Rol rol : listaRolesRecurso) {
					if(rol.getDescRol().equalsIgnoreCase("ROLE_RH") || rol.getDescRol().equalsIgnoreCase("ROLE_ADMIN")) {
						
						List<RecursoVacaciones> listadoRecursosTmp = recursoVacacionesService.findAll();
						List<RecursoVacaciones> listadoRecursos = new ArrayList<>();
						
						for (RecursoVacaciones rv : listadoRecursosTmp) {
							Recurso recursoTmp = recursoService.findOne(rv.getCodRecurso());
							if(recursoTmp.getValActivo().equals(1L)) {
								rv.setNombreRecurso(recursoTmp.getDescRecurso() + " " + recursoTmp.getDescApellidoPaterno());
								listadoRecursos.add(rv);
							} 
						}
						model.addAttribute("listadoRecursos", listadoRecursos);
						return  "layout/vacaciones :: listaRecursosVacaciones";
					}
				}
			}
		}
		return "layout/vacaciones :: accesoDenegado";
	}
	
	//Servicio para la actualizacion de dias acumulados, recuperados y por contrato de los recursos en sophitech. por cada periodo
	@GetMapping({"/actualizarDiasVacaciones"})
	@ResponseBody
	public String actualizarDiasVacaciones(@RequestParam Long codRecurso,
										@RequestParam Long acumulado,
										@RequestParam Long recuperado,
										@RequestParam Long contrato,
										Model model) {
		RecursoVacaciones recursoVacaciones = recursoVacacionesService.findById(codRecurso);
		if(recursoVacaciones != null ) {
			//Set de nuevos valores
			recursoVacaciones.setValAcumulado(acumulado);
			recursoVacaciones.setValRecuperado(recuperado);
			recursoVacaciones.setValContrato(contrato);
			//Calculados
			Long total = acumulado+recuperado+contrato;
			recursoVacaciones.setValTotal(total);
			Long aprobados = recursoVacaciones.getValAprobado();
			recursoVacaciones.setValDisponibles(total-aprobados);
			//Guardar actualizacion
			recursoVacacionesService.save(recursoVacaciones);
			return "ok";
		} else {
			return "nok";
		}
		
	}

	@GetMapping({"/detalleVacacionesRecurso/{codRecurso}"})
	public String detalleVacacionesRecurso(@PathVariable Long codRecurso, Model model) {
		
		
		
		List<SolicitudVacaciones> listaSolicitudes = new ArrayList<SolicitudVacaciones>();
		
		Long contador = 0L;
		
		RecursoVacaciones recursoVacaciones = null;
		
//		if (recurso != null) {
			recursoVacaciones = recursoVacacionesService.findById(codRecurso);
			listaSolicitudes = solicitudVacacionesService.findByCodRecurso(codRecurso);
			for (SolicitudVacaciones solicitudVacaciones : listaSolicitudes) {
				contador++;
				solicitudVacaciones.setContador(contador);
			}
//		}
		
		model.addAttribute("codRecurso", codRecurso);
		model.addAttribute("recursoVacaciones", recursoVacaciones);
		model.addAttribute("listaSolicitudes", listaSolicitudes);
		return "listaVacaciones :: listaDetalleVacaciones";
	}
	
	/*@Scheduled(cron="0 0 6 * * *", zone="America/Mexico_City")
	public void cambioRecursoBKP() {
		System.out.println(new Utiles().getFechaActual());
		DetalleSolicitud detalleSolicitud = new DetalleSolicitud();
		detalleSolicitud.getFe
	}*/
	
}