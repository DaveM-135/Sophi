package com.sophi.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sophi.app.Utiles;
import com.sophi.app.models.entity.ExpedienteDigital;
import com.sophi.app.models.service.IDocumentacionGeneralService;
import com.sophi.app.models.service.IExpedienteDigitalService;

@Controller
public class ExpedienteDigitalController {
	
	@Autowired
	private IDocumentacionGeneralService documentacionGeneralService;
	
	@Autowired
	private IExpedienteDigitalService expedienteDigitalService; 
	
	@RequestMapping(value = "/verExpedientesRecurso")
	public String verExpedienteDigital(@RequestParam("r") Long codRecurso, Map<String, Object> model) {
		List<Long> listaExpedientesCargados = new ArrayList<Long>();
		List<Long> listaExpedientesAprobados = new ArrayList<Long>();
		List<Long> listaExpedienteRechazados = new ArrayList<Long>();
		
		List<ExpedienteDigital> expedientesActuales =  expedienteDigitalService.findByCodRecurso(codRecurso);
		for (ExpedienteDigital expedienteDigital : expedientesActuales) {
			if(expedienteDigital.getValAprobado().equals(1L)) {
				listaExpedientesCargados.add(expedienteDigital.getCodDocumento());
			} else if(expedienteDigital.getValAprobado().equals(2L)) {
				listaExpedientesAprobados.add(expedienteDigital.getCodDocumento());
			} else if (expedienteDigital.getValAprobado().equals(3L)) {
				listaExpedienteRechazados.add(expedienteDigital.getCodDocumento());
			}
		}
		
		model.put("listaDocumentacion", documentacionGeneralService.findAll());
		model.put("listaExpedientesCargados", listaExpedientesCargados);
		model.put("listaExpedientesAprobados", listaExpedientesAprobados);
		model.put("listaExpedientesRechazados", listaExpedienteRechazados);
		model.put("rec", codRecurso);
		return "layout/expedienteDigital:: fragmentExpedienteDigital";
	}
	
	@RequestMapping(value = "/formDocumento")
	public String registrarDocumento(@RequestParam("r") Long codRecurso, @RequestParam("d") Long codDocumento,  Map<String, Object> model) {
		ExpedienteDigital ed = new ExpedienteDigital();
		ed.setCodRecurso(codRecurso);
		ed.setCodDocumento(codDocumento);
		model.put("expedienteDigital", ed);
		model.put("descripcionDocumento", documentacionGeneralService.findById(codDocumento).getDescDocumento());
		
		return "layout/expedienteDigital:: fragmentModalExpedienteDigital";
	}
	
	
	@RequestMapping(value = "/formEditDocumento")
	public String editarDocumento(@RequestParam("r") Long codRecurso, @RequestParam("d") Long codDocumento,  Map<String, Object> model) {
		ExpedienteDigital ed = expedienteDigitalService.findByCodRecursoAndCodDocumento(codRecurso, codDocumento);
		model.put("expedienteDigital", ed);
		model.put("descripcionDocumento", documentacionGeneralService.findById(codDocumento).getDescDocumento());
		return "layout/expedienteDigital:: fragmentModalExpedienteDigital";
	}
	
	
	@RequestMapping(value="/formDocumento", method = RequestMethod.POST)
	@ResponseBody
	public String registrarDocumento(@ModelAttribute ExpedienteDigital expedienteDigital, @RequestParam("documentoExpediente") MultipartFile documentoExpediente, Model model) {
		if(!documentoExpediente.isEmpty()) {
			try {
				byte[] bytesDocumentoExpediente = documentoExpediente.getBytes();
				expedienteDigital.setDocumentoEvidencia(bytesDocumentoExpediente);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		expedienteDigital.setValAprobado(1L);
		expedienteDigital.setFecRegistro(new Utiles().getFechaActual());
		
		expedienteDigitalService.save(expedienteDigital);
		return "ok";
	}
	
	@RequestMapping(value = "/expediente/ev")
	public void verExpedienteCargado(@RequestParam("r") Long codRecurso, @RequestParam("e") Long codDocumento, HttpServletResponse response) throws IOException{
		response.setContentType("image/jpeg");
		ExpedienteDigital ed = expedienteDigitalService.findByCodRecursoAndCodDocumento(codRecurso, codDocumento);
		InputStream is = new ByteArrayInputStream(ed.getDocumentoEvidencia());
		IOUtils.copy(is, response.getOutputStream());
	}
	
	@RequestMapping(value = "/aprobarDocumento")
	@ResponseBody
	public String aprobarDocumento(@RequestParam("r") Long codRecurso, @RequestParam("d") Long codDocumento,  Map<String, Object> model) {
		ExpedienteDigital ed = expedienteDigitalService.findByCodRecursoAndCodDocumento(codRecurso, codDocumento);
		ed.setValAprobado(2L);
		expedienteDigitalService.save(ed);
		return "ok";
	}
	
	
	@RequestMapping(value = "/rechazarDocumento")
	@ResponseBody
	public String rechazarDocumento(@RequestParam("r") Long codRecurso, @RequestParam("d") Long codDocumento,  Map<String, Object> model) {
		ExpedienteDigital ed = expedienteDigitalService.findByCodRecursoAndCodDocumento(codRecurso, codDocumento);
		ed.setValAprobado(3L);
		expedienteDigitalService.save(ed);
		return "ok";
	}
	

}
