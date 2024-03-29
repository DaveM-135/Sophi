package com.sophi.app.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sophi.app.models.service.IDetalleSolicitudService;
import com.sophi.app.models.service.IDiaFestivoService;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.IRecursoVacacionesService;
import com.sophi.app.models.service.IRolService;
import com.sophi.app.models.service.ISolicitudVacacionesService;

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
	
	@Autowired
	private IDetalleSolicitudService detalleSolicitudService;

	Date fecha = null;

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
		
		//model.addAttribute("codRecurso", recurso.getCodRecurso());
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
										@RequestParam Long recursoBKP,
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
		System.out.println("Recurso BKP: "+recursoBKP);
		
		if(recursoBKP != 0) {
			List<Proyecto> listaProyectoRecursoBKP = new ArrayList<Proyecto>();
			listaProyectoRecursoBKP = proyectoService.findListaProyectosRecursoAprobadorTodos(codRecurso);
			
			for(Proyecto proyecto: listaProyectoRecursoBKP) {
				proyecto.setCodRecursoAprobadorBKP(recursoBKP);
				proyectoService.save(proyecto);
			}
			
			//Mail Notificacion INICIO recurso BKP
			DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
			Recurso recursobackup = recursoService.findOne(recursoBKP);
			Recurso recurso = recursoService.findOne(codRecurso);
			MailRequest requestbkp = new MailRequest();
			System.out.println(recursobackup.getDescRecurso());
			requestbkp.setName(recursobackup.getDescRecurso());
			requestbkp.setSubject("Asignación de Recurso Backup");
			requestbkp.setTo(recursobackup.getDescCorreoElectronico());
			
			Map<String, Object> modelBKP = new HashMap<String, Object>();
			modelBKP.put("nombreRecurso", requestbkp.getName());
			if(detallesSolicitud.size() > 1) {
				String dias_vacaciones = "";
				for(DetalleSolicitud ds: detallesSolicitud) {
					dias_vacaciones += dt.format(ds.getFecDiaSolicitado()).toString().concat(", ");
				}
				modelBKP.put("mensaje", "<h3>\""+ recurso.getDescRecurso() + " te ha asignado como recurso backup los días "+dias_vacaciones+"\"</h3>.");
			} else {
				modelBKP.put("mensaje", "<h3>\""+ recurso.getDescRecurso() + " te ha asignado como recurso backup para el día "+dt.format(detallesSolicitud.get(0).getFecDiaSolicitado())+"\"</h3>.");
			}
			modelBKP.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-banca.png\">");
			modelBKP.put("pie", "");
			MailResponse response = service.sendEmailBKP(requestbkp, modelBKP);
			System.out.println(response.getMessage());
			//Mail Notificacion FIN recurso BKP
		}
		
		for (String mailAprobador : aprobadores.split(",")) {
		//Mail Notificacion INICIO 
		Recurso recurso = recursoService.findOne(codRecurso);
		Recurso recursoAprobador = recursoService.findByDescCorreoElectronico(mailAprobador);
		if(recursoAprobador.getCodRecurso() != recurso.getCodRecurso()) {
			MailRequest request = new MailRequest();
			System.out.println(recursoAprobador.getDescRecurso());
			request.setName(recursoAprobador.getDescRecurso());
			request.setSubject("Nueva solicitud de vacaciones");
			request.setTo(recursoAprobador.getDescCorreoElectronico());
			
			Map<String, Object> modelM = new HashMap<String, Object>();
			modelM.put("nombreRecurso", request.getName());
			modelM.put("mensaje", "<h3>Nueva solicitud de vacaciones por responder de \""+ recurso.getDescRecurso() + " " + recurso.getDescApellidoPaterno() + "\"</h3>.");
			modelM.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-banca.png\">");
			modelM.put("btnLink", "<a href=\"http://"+new Utiles().getHostName()+".com/aprobacionVacaciones/" +mailAprobador+" \" style=\"text-align: center; border-radius: 5px; font-weight: bold; background-color: #C02C57; color: white; padding: 14px 25px; text-decoration: none; display: inline-block; \">Ver detalle</a>");
			modelM.put("pie", "");
			
			MailResponse response = service.sendEmailEvaluador(request, modelM);
			System.out.println(response.getMessage());
		}
		//Mail Notificacion FIN 
		}
		
		return "/misVacaciones/"+usr;
	}
	
	
	@GetMapping({"/validarDiaLaboralVacacionesAprobador"})
	@ResponseBody
	public String validarDiaLaboralVacacionesAprobador(@RequestParam Long codDia, @RequestParam Long codRecurso, @RequestParam String aprobadores, Model model) {
		List<DiaFestivo> diasFestivos = null;
		diasFestivos = diaFestivoService.findEsNoLaboral(codDia);
		
		Long codRecursoAprob;
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String[] arrayAprob = aprobadores.split(",");
		String fecSolicitadoStr = codDia.toString();
		
		String fecSolicitadoAnio = fecSolicitadoStr.substring(0, 4);
		String fecSolicitadoMes = fecSolicitadoStr.substring(4, 6);
		String fecSolicitadoDia = fecSolicitadoStr.substring(6, 8);
		
		String fecSolicitadoStrFmt = fecSolicitadoAnio + "-" + fecSolicitadoMes + "-" + fecSolicitadoDia;
		
		System.out.println(fecSolicitadoStrFmt);
		try {
			fecha = dateFormatter.parse(fecSolicitadoStrFmt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(new Utiles().getFechaActualCal().get(Calendar.MONTH)+1 != 12) {
			for(String a: arrayAprob) {
				codRecursoAprob = recursoService.findByDescCorreoElectronico(a).getCodRecurso();
				if(codRecursoAprob != codRecurso && codRecursoAprob != 11 && codRecurso !=3) {
					
					List<SolicitudVacaciones> listsv = new ArrayList<SolicitudVacaciones>();
					listsv = solicitudVacacionesService.findByCodRecurso(codRecursoAprob);
					
					for(SolicitudVacaciones sv: listsv) {
						List<DetalleSolicitud> listds = new ArrayList<DetalleSolicitud>();
						listds = detalleSolicitudService.findByCodSolicitud(sv.getCodSolicitud());
						
						for(DetalleSolicitud ds: listds) {
							if(ds.getFecDiaSolicitado().toString().equals(fecSolicitadoStrFmt)) {
								return "El recurso aprobador de proyecto pidió este día de vacaciones";
							}
						}
					}
				}
			}
		}
		
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
	
	@Scheduled(cron="0 0 6 ? * MON,TUE,WED,THU,FRI", zone="America/Mexico_City")
	public void cambioRecursoBKP() {
		System.out.println("Entrando a cambiar rol aprob por BKP y/o viceversa");
		Long codSolicitud, codRecurso, codRecursoAprob, codRecursoAprobBKP;
		
		List<DetalleSolicitud> listaDetalleSolicitud = new ArrayList<DetalleSolicitud>();
		listaDetalleSolicitud = detalleSolicitudService.findAll();
		
		List<DetalleSolicitud> listaDetalleSolicitudAux = new ArrayList<DetalleSolicitud>();
		SolicitudVacaciones solicitudVacaciones;
		
		List<DetalleSolicitud> listaSolicitudAprob = new ArrayList<DetalleSolicitud>();
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
		
		for(DetalleSolicitud detalleSolicitud: listaDetalleSolicitud) {
			if(detalleSolicitud.getFecDiaSolicitado().equals(new Utiles().getFechaActual()) || (int)((new Utiles().getFechaActual().getTime() - detalleSolicitud.getFecDiaSolicitado().getTime())/(1000 * 60 * 60 * 24)) == 1) {
				codSolicitud = detalleSolicitud.getSolicitudVacaciones().getCodSolicitud();
				System.out.println("Solicitud No.: "+codSolicitud);
				listaDetalleSolicitudAux = detalleSolicitudService.findByCodSolicitud(codSolicitud);
				
				for(DetalleSolicitud detalleSolicitudAux: listaDetalleSolicitudAux) {
					solicitudVacaciones = solicitudVacacionesService.findById(codSolicitud);
					codRecurso = solicitudVacaciones.getCodRecurso();
					System.out.println("Recurso: "+codRecurso);
					
					if(solicitudVacaciones.getFecAprobacion() != null && solicitudVacaciones.getFecRechazo() == null && solicitudVacaciones.getFecCancelacion() == null) {
						System.out.println(detalleSolicitudAux.getSolicitudVacaciones().getCodSolicitud()+": "+detalleSolicitudAux.getFecDiaSolicitado());
						
						listaSolicitudAprob = detalleSolicitudService.findByCodSolicitud(codSolicitud);
						System.out.println("Primer elemento: "+listaSolicitudAprob.get(0).getCodDetalleSolicitud()+": "+listaSolicitudAprob.get(0).getFecDiaSolicitado());
						System.out.println("Último elemento: "+listaSolicitudAprob.get(listaSolicitudAprob.size()-1).getCodDetalleSolicitud()+": "+listaSolicitudAprob.get(listaSolicitudAprob.size()-1).getFecDiaSolicitado());						
						
						if(listaSolicitudAprob.get(0).getFecDiaSolicitado().equals(new Utiles().getFechaActual())) {
							listaProyecto = proyectoService.findListaProyectosRecursoAprobadorTodos(codRecurso);
							for(Proyecto listaProyectos: listaProyecto) {
								codRecursoAprob = listaProyectos.getCodRecursoAprobador();
								codRecursoAprobBKP = listaProyectos.getCodRecursoAprobadorBKP();
								
								listaProyectos.setCodRecursoAprobadorBKP(codRecursoAprob);
								listaProyectos.setCodRecursoAprobador(codRecursoAprobBKP);
								proyectoService.save(listaProyectos);
								System.out.println("Terminó de hacer switch inicio");
							}
							System.out.println("Salió for cuando inicia periodo vacacional");
						}
						//(int)((new Utiles().getFechaActual().getTime() - listaSolicitudAprob.get(listaSolicitudAprob.size()-1).getFecDiaSolicitado().getTime())/(1000 * 60 * 60 * 24)) == 1
						else {
							listaProyecto = proyectoService.findListaProyectosRecursoAprobadorBKPTodos(codRecurso);
							for(Proyecto listaProyectos: listaProyecto) {
								codRecursoAprobBKP = listaProyectos.getCodRecursoAprobadorBKP();
								
								listaProyectos.setCodRecursoAprobadorBKP(null);
								listaProyectos.setCodRecursoAprobador(codRecursoAprobBKP);
								proyectoService.save(listaProyectos);
								System.out.println("Terminó de hacer switch fin");
							}
							System.out.println("Salió for cuando termina periodo vacacional");
						}
					}
				}
			}
		}
		
	}
	
}