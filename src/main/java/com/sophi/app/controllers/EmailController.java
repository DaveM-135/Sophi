package com.sophi.app.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.mail.service.InviteEmailService;
import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.DetalleForecast;
import com.sophi.app.models.entity.DetalleRecursoHoras;
import com.sophi.app.models.entity.InviteContacto;
import com.sophi.app.models.entity.MesHabil;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.ResumenForecastGeneral;
import com.sophi.app.models.service.IActividadService;
import com.sophi.app.models.service.ICapHoraService;
import com.sophi.app.models.service.IDetalleForecastService;
import com.sophi.app.models.service.IInviteContactoService;
import com.sophi.app.models.service.IMesHabilService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.ISubtareaService;

@Controller
public class EmailController {

	@Autowired
	private EmailService service;
	
	@Autowired
	private InviteEmailService inviteService;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private ICapHoraService capHoraService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	@Autowired
	private ISubtareaService subtareaService;
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private IMesHabilService mesHabilService;
	
	@Autowired
	private IDetalleForecastService detalleForecastService;
	
	@Autowired
	private IInviteContactoService inviteContactoService;
	
//	@Scheduled(cron="0 30 11 * * *", zone="America/Mexico_City")
	public void enviaInvitacionWebinar() {
		List<InviteContacto> listaContactosInvite = new ArrayList<InviteContacto>();
		listaContactosInvite = inviteContactoService.findAll();
		for (InviteContacto inviteContacto : listaContactosInvite) {
			MailRequest request = new MailRequest();
			request.setName(inviteContacto.getNombre());
			request.setSubject("Revolución Analítica para retail");
			request.setTo(inviteContacto.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nombre_invite", request.getName());
			
			MailResponse response = inviteService.sendEmail(request, model);
			System.out.println(response.getMessage());
		}
	}
	
//	@Scheduled(cron="0 53 18 * * *", zone="America/Mexico_City")
	public void enviaLinkWebinar() {
		List<InviteContacto> listaContactosInvite = new ArrayList<InviteContacto>();
		listaContactosInvite = inviteContactoService.findAll();
		for (InviteContacto inviteContacto : listaContactosInvite) {
			MailRequest request = new MailRequest();
			request.setName(inviteContacto.getNombre());
			request.setSubject("Revolución Analítica para retail");
			request.setTo(inviteContacto.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("mensaje", "<h3>Te invitamos a nuestro webinar Revoluci&oacute;n anal&iacute;tica para retail el dia de ma&ntilde;ana 20 de Abril de 12:00 a 13:00 hrs.<br>Esperamos tu apoyo y tu asistencia.</h3><br><h5>Da clic en siguiente bot&oacute;n para entrar al webinar</h5>");
			model.put("nombreRecurso", request.getName());
			model.put("pie", "");
			model.put("imagen","<img data-cfsrc=\"images/webinar.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-webinar.png\">");
			MailResponse response = inviteService.sendEmail(request, model);
			System.out.println(response.getMessage());
		}
	}
	
	
//	@Scheduled(cron="0 16 10 * * *", zone="America/Mexico_City")  
	public void enviaInvitacionWebinarFarmacias() {
		List<InviteContacto> listaContactosInvite = new ArrayList<InviteContacto>();
		listaContactosInvite = inviteContactoService.findAll();
		for (InviteContacto inviteContacto : listaContactosInvite) {
			MailRequest request = new MailRequest();
			request.setName(inviteContacto.getNombre());
			request.setSubject("Revolución Analítica");
			request.setTo(inviteContacto.getEmail());
			
			Map<String, Object> model = new HashMap<String, Object>();
//			model.put("mensaje", "<h3>Te invitamos a nuestro webinar Revoluci&oacute;n anal&iacute;tica para retail el dia de ma&ntilde;ana 20 de Abril de 12:00 a 13:00 hrs.<br>Esperamos tu apoyo y tu asistencia.</h3><br><h5>Da clic en siguiente bot&oacute;n para entrar al webinar</h5>");
//			model.put("nombreRecurso", request.getName());
//			model.put("pie", "");
//			model.put("imagen","<img data-cfsrc=\"images/webinar.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-webinar.png\">");
			MailResponse response = inviteService.sendEmailFarmacias(request, model);
			System.out.println(response.getMessage());
		}
	}
	
	
	//CRON Viernes 10:00 hora mexico
	@Scheduled(cron="0 0 10 * * MON,TUE,WED,THU,FRI", zone="America/Mexico_City")
	public void enviaRecordatorios() {
		List<Recurso> listRecursos = new ArrayList<Recurso>();
		listRecursos = recursoService.findRecursosActivos();
		if (listRecursos.size()>0) {
			for (Recurso recurso : listRecursos) {
				
				MailRequest request = new MailRequest();
				request.setName(recurso.getDescRecurso());
				request.setSubject("Recordatorio de captura");
				request.setTo(recurso.getDescCorreoElectronico());
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreRecurso", request.getName());
				model.put("mensaje", "<h3>Recuerda que hoy debes capturar tus horas en la plataforma</h3>");
				model.put("pie", "Evita que tu esfuerzo se vaya a la banca");
				model.put("imagen","<img data-cfsrc=\"images/time.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-time.png\">");
				
				MailResponse response = service.sendEmail(request, model);
				System.out.println(response.getMessage());
			}
		}
	}
	
	//CRON Viernes 10:00 hora mexico
		@Scheduled(cron="0 0 10 * * FRI", zone="America/Mexico_City")
		public void enviaRecordatoriosFlashClima() {
			List<Recurso> listRecursos = new ArrayList<Recurso>();
			listRecursos = recursoService.findRecursosActivos();
			if (listRecursos.size()>0) {
				for (Recurso recurso : listRecursos) {
					
					MailRequest request = new MailRequest();
					request.setName(recurso.getDescRecurso());
					request.setSubject("Recordatorio de Flash Clima");
					request.setTo(recurso.getDescCorreoElectronico());
					
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("nombreRecurso", request.getName());
					model.put("mensaje", "<h3>Recuerda que hoy debes contestar la encuesta Flash Clima</h3>");
					model.put("pie", "");
					model.put("imagen","<img data-cfsrc=\"images/time.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-time.png\">");
					
					MailResponse response = service.sendEmail(request, model);
					System.out.println(response.getMessage());
				}
			}
		}
	
	//CRON Miercoles 13:15 hora mexico
	@Scheduled(cron="0 0 11 * * MON", zone="America/Mexico_City")
	public void enviaAvisoHrsRechazdas() {
		List<DetalleRecursoHoras> listDetalle = new ArrayList<DetalleRecursoHoras>();

		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1);
		Date fecFinal = calendar.getTime();
		
		calendar.add(Calendar.DAY_OF_YEAR, -6);
		Date fecInicial  = calendar.getTime();
		System.out.println(fecInicial);
		System.out.println(fecFinal);
		
		listDetalle = capHoraService.findRecursoHorasRechazo(fecInicial, fecFinal);
		if (listDetalle.size() > 0) {
			for (DetalleRecursoHoras detalleRecursoHoras : listDetalle) {
				Recurso recurso = recursoService.findOne(detalleRecursoHoras.getLink());
				MailRequest request = new MailRequest();
				System.out.println(recurso.getDescRecurso());
				request.setName(recurso.getDescRecurso());
				request.setSubject("Aviso - horas rechazadas");
				request.setTo(recurso.getDescCorreoElectronico());
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreRecurso", request.getName());
				model.put("mensaje","<h3>" + detalleRecursoHoras.getRechazadas() + " horas fueron rechazadas la semana pasada, revisa tu captura en la plataforma</h3>");
				model.put("pie", "");
				model.put("imagen","<img data-cfsrc=\"images/rechazo.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-rechazo.png\">");
				
				MailResponse response = service.sendEmail(request, model);
				System.out.println(response.getMessage());
			}
		}
	}
	
	
	
	@Scheduled(cron="0 0 10 * * MON", zone="America/Mexico_City")
	public void enviaRecordatoriosParaAprobacion() {
		List<Long> proyectos = new ArrayList<Long>();
		List<Recurso> aprobadores = new ArrayList<Recurso>();
		
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1);
		Date fecInicial = calendar.getTime();
		System.out.println(fecInicial);
		calendar.add(Calendar.DAY_OF_YEAR, 6);
		Date fecFinal = calendar.getTime();
		System.out.println(fecFinal);
		
		proyectos = capHoraService.findProyectosCapturadosSemana(fecInicial, fecFinal);
		if (proyectos.size() > 0) {
			
//			for (Long codProyecto : proyectos) {
//				Proyecto proyecto = proyectoService.findOne(codProyecto);
//				Recurso recurso = recursoService.findOne(proyecto.getCodRecursoAprobador());
			aprobadores = recursoService.findListRecursosAprobadores();	
				
			for (Recurso recurso: aprobadores) {
				MailRequest request = new MailRequest();
				request.setName(recurso.getDescRecurso());
				request.setSubject("Recordatorio de aprobación");
				request.setTo(recurso.getDescCorreoElectronico());
				
				System.out.println(recurso.getDescRecurso());
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreRecurso", request.getName());
				model.put("mensaje", "<h3>Recuerda que hoy debes aprobar las horas de la semana</h3>");
				model.put("pie", "");
				model.put("imagen","<img data-cfsrc=\"images/aprob.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-aprobacion.png\">");
				
				MailResponse response = service.sendEmail(request, model);
				System.out.println(response.getMessage());
				
//				}
			}
		}
	}
	
	
	@Scheduled(cron="0 0 13 * * MON", zone="America/Mexico_City")
	public void enviaEstatusDeAprobacion() {
		List<Long> recursos = new ArrayList<Long>();

		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1);
		Date fecFinal = calendar.getTime();
		
		calendar.add(Calendar.DAY_OF_YEAR, -6);
		Date fecInicial = calendar.getTime();
		
		System.out.println(fecInicial);
		System.out.println(fecFinal);
		
		String strDateFormat = "dd/MMM";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		System.out.println(objSDF.format(fecInicial)); 
		System.out.println(objSDF.format(fecFinal)); 
		
		recursos = capHoraService.findRecursoCapturadosSemana(fecInicial, fecFinal);
		
		if (recursos.size() > 0) {
			
			for (Long codRecurso : recursos) {
				
				List<CapHora> listCapHora = new ArrayList<CapHora>();
				listCapHora = capHoraService.findListCapHoraByPeriodoFechaRecurso(codRecurso, fecInicial, fecFinal);
				
				StringBuilder tablaDetalle = new StringBuilder();
				tablaDetalle.append("<div style=\"overflow-x:auto;\">");
//				tablaDetalle.append("<table align=\"left\" role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"#C3C3C3\" border=\"1\" width=\"100%\" style=\"margin: auto; font-size:9px; font-weight: 100;\">");
				tablaDetalle.append("<table style=\"margin: auto; font-size:9px; font-weight: 100; border-collapse: collapse; width: 100%;  border: 1px solid #ddd;\">");
				tablaDetalle.append("<thead>");
				tablaDetalle.append("<tr>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Fecha</th>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Proyecto</th>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Tarea</th>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.Total</th>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.Ok</th>");
				tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.NOk</th>");
				tablaDetalle.append("</tr>");
				tablaDetalle.append("</thead>");
				tablaDetalle.append("<tbody>");
				
				for (CapHora captura : listCapHora) {
					if(captura.getValNuevaActividad().equals(1L) || captura.getValNuevaActividad().equals(2L)) {
						captura.setDescProyecto(proyectoService.findByCodProyecto(captura.getCodProyecto()).getDescProyecto());
						captura.setDescActividadSecundaria(subtareaService.findOne(captura.getCodActividad()).getDescSubtarea());
					} else {
						captura.setDescProyecto(proyectoService.findByCodProyecto(captura.getCodProyecto()).getDescProyecto());
						captura.setDescActividadSecundaria(actividadService.findOne(captura.getCodActividad()).getDescActividadSecundaria());
					}
					
					tablaDetalle.append("<tr>");
					tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
					tablaDetalle.append(captura.getFecInicioActividad());
					tablaDetalle.append("</td>");
					tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
					tablaDetalle.append(captura.getDescProyecto());
					tablaDetalle.append("</td>");
					tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
					tablaDetalle.append(captura.getDescActividadSecundaria());
					tablaDetalle.append("</td>");
					tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
					tablaDetalle.append(captura.getValDuracionReportada());
					tablaDetalle.append("</td>");
					tablaDetalle.append(captura.getValDuracionValidad()> 0 ? "<td style=\"color : green; border-bottom: 1px solid #ddd;\">" + captura.getValDuracionValidad() :  "<td style=\"border-bottom: 1px solid #ddd; color: #000;\">" + captura.getValDuracionValidad());
					tablaDetalle.append("</td>");
					tablaDetalle.append(captura.getValDuracionRechazada() > 0 ? "<td style=\"color : red; border-bottom: 1px solid #ddd;\">" + captura.getValDuracionRechazada() :  "<td style=\"border-bottom: 1px solid #ddd; color: #000;\">" + captura.getValDuracionRechazada());
					tablaDetalle.append("</td>");
					tablaDetalle.append("</tr>");
				}
				
				tablaDetalle.append("</tbody>");
				tablaDetalle.append("</table>");
				tablaDetalle.append("</div><br>");
				
				Recurso recurso = recursoService.findOne(codRecurso);
				
				MailRequest request = new MailRequest();
				request.setName(recurso.getDescRecurso());
				request.setSubject("Resumen de captura de horas");
				request.setTo(recurso.getDescCorreoElectronico());
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreRecurso", request.getName());
				model.put("mensaje", "<h3>Resumen de horas del " + objSDF.format(fecInicial) + " al " + objSDF.format(fecFinal) +"</h3>" + tablaDetalle.toString());
				model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-status.png\">");
				model.put("pie", "");
				
				MailResponse response = service.sendEmail(request, model);
				System.out.println(response.getMessage());
			}
		}
	}
	
	
	//Enviar resumen de horas aprobadas (por proyecto y enviado al aprobador)
	@Scheduled(cron="0 0 19 * * MON", zone="America/Mexico_City")
	public void enviaEstatusDeAprobacionPorProyecto() {
		List<Long> proyectos = new ArrayList<Long>();
		
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_WEEK) - 1) * -1);
		Date fecFinal = calendar.getTime();
		
		calendar.add(Calendar.DAY_OF_YEAR, -6);
		Date fecInicial = calendar.getTime();
		
		System.out.println(fecInicial);
		System.out.println(fecFinal);
		
		String strDateFormat = "dd/MMM";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		System.out.println(objSDF.format(fecInicial)); 
		System.out.println(objSDF.format(fecFinal)); 
		
		proyectos = capHoraService.findProyectosCapturadosSemana(fecInicial, fecFinal);
		
		if (proyectos.size() > 0 ) {
			for (Long codProyecto : proyectos) {
				Proyecto proyecto = new Proyecto();
				proyecto = proyectoService.findOne(codProyecto);
				if(proyecto != null) {
					List<DetalleRecursoHoras> detalle = new ArrayList<DetalleRecursoHoras>();
					detalle = capHoraService.findProyectoRecursosResumenSemanal(proyecto.getCodProyecto(), fecInicial, fecFinal);
					if (detalle.size() > 0) {
						
						StringBuilder tablaDetalle = new StringBuilder();
						tablaDetalle.append("<div style=\"overflow-x:auto;\">");
						tablaDetalle.append("<table style=\"margin: 20px; font-size:9px; font-weight: 100; border-collapse: collapse; width: 100%;  border: 1px solid #ddd;\">");
						tablaDetalle.append("<thead>");
						tablaDetalle.append("<tr>");
						tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Recurso</th>");
						tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.Total</th>");
						tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.Ok</th>");
						tablaDetalle.append("<th style=\"background-color: #4CAF50; color: white;\">Hrs.NOk</th>");
						tablaDetalle.append("</tr>");
						tablaDetalle.append("</thead>");
						tablaDetalle.append("<tbody>");
						
						for (DetalleRecursoHoras det : detalle) {
							tablaDetalle.append("<tr>");
							tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
							tablaDetalle.append(det.getNombreRecurso());
							tablaDetalle.append("</td>");
							tablaDetalle.append("<td style=\"border-bottom: 1px solid #ddd; color: #000;\">");
							tablaDetalle.append(det.getCapturadas());
							tablaDetalle.append("</td>");
							tablaDetalle.append(det.getAprobadas() > 0 ? "<td style=\"color : green; border-bottom: 1px solid #ddd;\">" + det.getAprobadas() :  "<td style=\"border-bottom: 1px solid #ddd; color: #000;\">" + det.getAprobadas());
							tablaDetalle.append("</td>");
							tablaDetalle.append(det.getRechazadas() > 0 ? "<td style=\"color : red; border-bottom: 1px solid #ddd;\">" + det.getRechazadas() :  "<td style=\"border-bottom: 1px solid #ddd; color: #000;\">" + det.getRechazadas());
							tablaDetalle.append("</td>");
							tablaDetalle.append("</tr>");
						}
						
						tablaDetalle.append("</tbody>");
						tablaDetalle.append("</table>");
						tablaDetalle.append("</div><br>");
						
						Recurso recursoAprobador = recursoService.findOne(proyecto.getCodRecursoAprobador());
						
						MailRequest request = new MailRequest();
						request.setName(recursoAprobador.getDescRecurso());
						request.setSubject(proyecto.getDescProyecto() + " - Resumen de captura de horas ");
						request.setTo(recursoAprobador.getDescCorreoElectronico());
						
						Map<String, Object> model = new HashMap<String, Object>();
						model.put("nombreRecurso", request.getName());
						model.put("mensaje", "<h3>Resumen de horas del proyecto \""+ proyecto.getDescProyecto() + "\" del " + objSDF.format(fecInicial) + " al " + objSDF.format(fecFinal) +"</h3>" + tablaDetalle.toString());
						model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-status.png\">");
						model.put("pie", "");
						
						MailResponse response = service.sendEmail(request, model);
						System.out.println(response.getMessage());
						
					}
				}
				
			}
			
		}
	}
	
	
	//CRON Viernes 10:00 hora mexico
	@Scheduled(cron="0 0 10 * * THU", zone="America/Mexico_City")
	public void enviaRecordatoriosForecast() {
		List<Recurso> listRecursos = new ArrayList<Recurso>();
		//solo recursos del area Servicios
		listRecursos = recursoService.findByCodAreaRecurso(1L);
		if (listRecursos.size()>0) {
			for (Recurso recurso : listRecursos) {
				//solo recurso activos
				if(recurso.getValActivo().equals(1L)) {
					Utiles utiles = new Utiles();
					
					Calendar fecha = Calendar.getInstance();
					fecha.setTime(utiles.getFechaActual());
					
					String[] listaMeses = new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
					
					fecha.add(Calendar.MONTH, 1);
		        	
		        	String anio = String.valueOf(fecha.get(Calendar.YEAR));
		            String mes = String.valueOf(fecha.get(Calendar.MONTH) + 1);
		            if (mes.length() == 1) {
		            	mes = "0" + mes;
		            }
		            MesHabil mesHabil = mesHabilService.findById(Long.parseLong(anio +  mes));
		            String nombreMes = listaMeses[fecha.get(Calendar.MONTH)];
		            
		            float totalHorasForecast = 0;
	    			List<DetalleForecast> listaForecast = detalleForecastService.findByRecursoAndMesHabil(recurso,mesHabil);
	    			for (DetalleForecast detalleForecast : listaForecast) {
	    				totalHorasForecast += detalleForecast.getValHoras();
	    			}
					
	    			float alcanceHoras = mesHabil.getValHorasFestivos() + mesHabil.getValHorasHabiles();
	    			
	    			if (totalHorasForecast < alcanceHoras){ 
	    				
	    				MailRequest request = new MailRequest();
	    				request.setName(recurso.getDescRecurso());
	    				request.setSubject("Recordatorio de captura de forecast");
	    				request.setTo(recurso.getDescCorreoElectronico());
	    				
	    				Map<String, Object> model = new HashMap<String, Object>();
	    				model.put("nombreRecurso", request.getName());
	    				model.put("mensaje", "<h3>Recuerda que debes capturar tu forecast del mes de "+ nombreMes +" en la plataforma</h3>");
	    				model.put("pie", "Evita que tu esfuerzo se vaya a la banca");
	    				model.put("imagen","<img data-cfsrc=\"images/forecast.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-forecast.png\">");
	    				
	    				MailResponse response = service.sendEmail(request, model);
	    				System.out.println(response.getMessage());
	    			}
					
				}
			}
		}
	}
	
	//CRON Diario 12:00 hora mexico
	@Scheduled(cron="0 0 12 * * *", zone="America/Mexico_City")
	public void enviaRecordatorioCierreProyecto() {
		List<Proyecto> listaProyectos = new ArrayList<>();
		listaProyectos = proyectoService.findListaProyectosPorCerrar(new Utiles().getFechaActual());
		
		if(listaProyectos.size() > 0) {
			for (Proyecto proyecto : listaProyectos) {
				Recurso recurso = recursoService.findOne(proyecto.getCodRecursoAprobador());
				MailRequest request = new MailRequest();
				request.setName(recurso.getDescRecurso());
				request.setSubject("Recordatorio de cierre de proyecto");
				request.setTo(recurso.getDescCorreoElectronico());
				
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("nombreRecurso", request.getName());
				model.put("mensaje", "<h3>La fecha de t&eacute;rmino del proyecto " + proyecto.getDescProyecto()+" ha vencido, es necesario cerrar el proyecto lo antes posible en la plataforma</h3>");
				model.put("pie", "");
				model.put("imagen","<img data-cfsrc=\"images/dashboard.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-dashboard.png\">");
				
				MailResponse response = service.sendEmail(request, model);
				System.out.println(response.getMessage());
			}
		}
	}
	
	
	//CRON Lunes 11:00 hora mexico
	@Scheduled(cron="0 0 11 * * MON", zone="America/Mexico_City")
	public void enviaResumenForecastAprobadores() {
		List<Recurso> listaAprobadores = new ArrayList<>();
		
		listaAprobadores = recursoService.findListRecursosAprobadores();
		for (Recurso recurso : listaAprobadores) {
			MailRequest request = new MailRequest();
			request.setName(recurso.getDescRecurso());
			request.setSubject("Resumen forecast");
			request.setTo(recurso.getDescCorreoElectronico());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nombreRecurso", request.getName());
			model.put("mensaje", "<h3>Resumen forecast de los próximos meses</h3><br>" + tablaMailForecast());
			model.put("pie", "");
			model.put("imagen","<img data-cfsrc=\"images/forecast.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-forecast.png\">");
			
			MailResponse response = service.sendEmail(request, model);
			System.out.println(response.getMessage());
		}

	}
	
	//Metodo que genera tabla html con resumen de forecast para enviar por mail a los aprobadores
		//Llamado desde Notificaciones controller
		public String tablaMailForecast() {
			List<Recurso> listaRecursosConsultoresTMP = new ArrayList<>();
			List<Recurso> listaRecursosConsultores = new ArrayList<>();
			
			//Recursos consultores es cod = 1
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
			
			 
			 StringBuilder retorno = new StringBuilder();
			 retorno.append("<table class=\"table\" width=\"100%\" cellspacing=\"0\" style=\"padding:0 50px;\">");
			 
			 
			 retorno.append("<tr>");
			 
			 retorno.append("<th style=\"text-align:left;\">Recurso</th>");
			 retorno.append("<th>"+listaPeriodo.get(0).getNombreMes() +"</th>");
			 retorno.append("<th>"+listaPeriodo.get(1).getNombreMes() +"</th>");
			 retorno.append("<th>"+listaPeriodo.get(2).getNombreMes() +"</th>");
			 
			 retorno.append("</tr>");
			 
			 
			 
			 for (Recurso recurso : listaRecursosConsultores) {
			 			retorno.append("<tr>"); 
			 			retorno.append("<td width=25% style=\"text-align:left;\">" + recurso.getDescRecurso() + "</td>"); 
			 			
			 			//INICIO MES 1
			 			retorno.append("<td width=25%>"); 
			 			
			 			float cal0 = recurso.getTotalHorasForecast().get(0) * 100 / listaPeriodo.get(0).getTotalHorasHabiles();
			 			if (cal0 < 85) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-danger\" role=\"progressbar\" style=\"width:" + cal0 + "%; display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #dc3545 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(0) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal0 >= 85 && cal0 < 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-warning\" role=\"progressbar\" style=\"width:" + cal0 + "%;    display: -ms-flexbox; " + 
				 					"  display: flex; " + 
				 					"  -ms-flex-direction: column; " + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center; " + 
				 					"  justify-content: center; " + 
				 					"  overflow: hidden; " + 
				 					"  color: #fff; " + 
				 					"  text-align: center; " + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff; " + 
				 					"  transition: width 0.6s ease; background-color: #ffc107 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(0) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal0 >= 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-success\" role=\"progressbar\" style=\"width:" + cal0 + "%;   display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #28a745 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(0) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			
			 			retorno.append("</td>"); 
			 			//FIN MES 1
			 			
			 			//INICIO MES 2
			 			retorno.append("<td width=25%>"); 
			 			
			 			float cal1 = recurso.getTotalHorasForecast().get(1) * 100 / listaPeriodo.get(1).getTotalHorasHabiles();
			 			if (cal1 < 85) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-danger\" role=\"progressbar\" style=\"width:" + cal1 + "%; display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #dc3545 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(1) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal1 >= 85 && cal1 < 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-warning\" role=\"progressbar\" style=\"width:" + cal1 + "%;    display: -ms-flexbox; " + 
				 					"  display: flex; " + 
				 					"  -ms-flex-direction: column; " + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center; " + 
				 					"  justify-content: center; " + 
				 					"  overflow: hidden; " + 
				 					"  color: #fff; " + 
				 					"  text-align: center; " + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff; " + 
				 					"  transition: width 0.6s ease; background-color: #ffc107 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(1) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal1 >= 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-success\" role=\"progressbar\" style=\"width:" + cal1 + "%;   display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #28a745 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(1) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			
			 			retorno.append("</td>"); 
			 			//FIN MES 2
			 			
			 			//INICIO MES 3
			 			retorno.append("<td width=30%>"); 
			 			
			 			float cal2 = recurso.getTotalHorasForecast().get(2) * 100 / listaPeriodo.get(2).getTotalHorasHabiles();
			 			if (cal2 < 85) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-danger\" role=\"progressbar\" style=\"width:" + cal2 + "%; display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #dc3545 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(2) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal2 >= 85 && cal2 < 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-warning\" role=\"progressbar\" style=\"width:" + cal2 + "%;    display: -ms-flexbox; " + 
				 					"  display: flex; " + 
				 					"  -ms-flex-direction: column; " + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center; " + 
				 					"  justify-content: center; " + 
				 					"  overflow: hidden; " + 
				 					"  color: #fff; " + 
				 					"  text-align: center; " + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff; " + 
				 					"  transition: width 0.6s ease; background-color: #ffc107 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(2) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			if (cal2 >= 100) {
			 				retorno.append("<div class=\"progress\" style=\" display: -ms-flexbox;" + 
			 						"  display: flex;" + 
			 						"  height: 1rem;" + 
			 						"  overflow: hidden;" + 
			 						"  line-height: 0;" + 
			 						"  font-size: 0.75rem;" + 
			 						"  background-color: #e9ecef;" + 
			 						"  border-radius: 0.25rem;\">");
				 			retorno.append("<div class=\"progress-bar bg-success\" role=\"progressbar\" style=\"width:" + cal2 + "%;   display: -ms-flexbox;" + 
				 					"  display: flex;" + 
				 					"  -ms-flex-direction: column;" + 
				 					"  flex-direction: column;" + 
				 					"  -ms-flex-pack: center;" + 
				 					"  justify-content: center;" + 
				 					"  overflow: hidden;" + 
				 					"  color: #fff;" + 
				 					"  text-align: center;" + 
				 					"  white-space: nowrap;" + 
				 					"  background-color: #007bff;" + 
				 					"  transition: width 0.6s ease; background-color: #28a745 !important; font-size: 10px; vertical-align: middle;\"><span style=\"margin-top:8px;\">" + recurso.getTotalHorasForecast().get(2) + "</span></div>"); 
				 			retorno.append("</div>"); 
			 			}
			 			
			 			retorno.append("</td>"); 
			 			//FIN MES 3
			 			
			 			retorno.append("</tr>"); 
			 			
			 		}
			 
			 		retorno.append("</table>");
			 
			 		
			 		System.out.println(retorno.toString());
			 
			 return retorno.toString();
			
		}
	
	//CRON Viernes 10:00 hora mexico
		@Scheduled(cron="0 0 10 * * MON,TUE,WED,THU", zone="America/Mexico_City")
		public void enviaRecordatorioFlashClimaLunesJueves() {
			Recurso recurso = recursoService.findOne(15L);
			MailRequest request = new MailRequest();
			request.setName(recurso.getDescRecurso());
			request.setSubject("Recordatorio de flash clima");
			request.setTo(recurso.getDescCorreoElectronico());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nombreRecurso", request.getName());
			model.put("mensaje", "<h3>Recuerda que debes actualizar la encuesta flash clima de los días Lunes a Jueves de la siguiente semana</h3>");
			model.put("pie", "");
			model.put("imagen","<img data-cfsrc=\"images/time.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-time.png\">");
			
			MailResponse response = service.sendEmail(request, model);
			System.out.println(response.getMessage());
		}
		
		//CRON Lunes 10:00 hora mexico
		@Scheduled(cron="0 0 10 * * MON", zone="America/Mexico_City")
		public void enviaRecordatorioFlashClimaViernes() {
			Recurso recurso = recursoService.findOne(15L);
			MailRequest request = new MailRequest();
			request.setName(recurso.getDescRecurso());
			request.setSubject("Recordatorio de flash clima");
			request.setTo(recurso.getDescCorreoElectronico());
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("nombreRecurso", request.getName());
			model.put("mensaje", "<h3>Recuerda que debes actualizar la encuesta flash clima del día Viernes</h3>");
			model.put("pie", "");
			model.put("imagen","<img data-cfsrc=\"images/time.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://sophitech.herokuapp.com/img/img-time.png\">");
			
			MailResponse response = service.sendEmail(request, model);
			System.out.println(response.getMessage());
		}
	
}
