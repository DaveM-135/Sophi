package com.sophi.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoGasto;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoService;

public class EmailNotificaciones {
	
	@Autowired
	private EmailService service;
	
	@Autowired
	private IRecursoService recursoService;
	
	@Autowired
	private IProyectoService proyectoService;
	
	//Mensaje	Por evento/acción	Notificar para aprobación de gastos (enviar a aprobador)
	public void enviaNotificacionAprobacionGasto(RecursoGasto recursoGastos) {
		System.out.println(recursoGastos.getCodProyecto());
		System.out.println(recursoGastos.getCodRecurso());
		
		Proyecto proyecto = new Proyecto(); 
		proyecto = proyectoService.findOne(27L);
		Recurso aprobador = new Recurso(); 
		aprobador = recursoService.findOne(proyecto.getCodRecursoAprobador());
		
		Recurso recurso = recursoService.findOne(recursoGastos.getCodRecurso());
		
		//Aprobador INICIO 
		MailRequest request = new MailRequest();
		request.setName(aprobador.getDescRecurso());
		request.setSubject("Nuevo gasto para aprobar");
		request.setTo(aprobador.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Tienes un nuevo gasto que aprobar del proyecto de "+ recurso.getDescRecurso() + "en el proyecto \""+ proyecto.getDescProyecto() + "\"</h3>");
		model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-status.png\">");
		model.put("pie", "");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
		//Aprobador FIN
	}
	
	
	
}
