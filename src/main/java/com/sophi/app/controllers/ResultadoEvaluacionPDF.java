package com.sophi.app.controllers;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

@Controller
public class ResultadoEvaluacionPDF {

	@Autowired
	ServletContext servletContext;

	private final TemplateEngine templateEngine;

	public ResultadoEvaluacionPDF(TemplateEngine templateEngine) {
	        this.templateEngine = templateEngine;
	    }

	@RequestMapping("/resultadosEvaluacion")
	public String getResultadosDesempenoPage(Model model) {
//		List<Recurso> recursos = recursoService.findListRecursosAprobadores();
		
//		model.addAttribute("recursos", recursos);
		return "resultadosEvaluacion";
	}

	@RequestMapping(path = "/resultadosEvaluacion/pdf")
	public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

		/* Do Business Logic */

//		List<Recurso> recursos = recursoService.findListRecursosAprobadores();

		/* Create HTML using Thymeleaf template Engine */

		WebContext context = new WebContext(request, response, servletContext);
//		context.setVariable("recursos", recursos);
		String resultadosHtml = templateEngine.process("resultadosEvaluacion", context);

		/* Setup Source and target I/O streams */

		ByteArrayOutputStream target = new ByteArrayOutputStream();
		ConverterProperties converterProperties = new ConverterProperties();
//		converterProperties.setBaseUri("http://localhost:8080");
		converterProperties.setBaseUri("http://localhost:8080");
		/* Call convert method */
		HtmlConverter.convertToPdf(resultadosHtml, target, converterProperties);

		/* extract output as bytes */
		byte[] bytes = target.toByteArray();

		/* Send the response as downloadable PDF */

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resultados.pdf")
				.contentType(MediaType.APPLICATION_PDF).body(bytes);

	}

}
