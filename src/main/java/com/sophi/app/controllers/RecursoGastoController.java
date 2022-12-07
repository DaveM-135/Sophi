package com.sophi.app.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sophi.app.Utiles;
import com.sophi.app.mail.dto.MailRequest;
import com.sophi.app.mail.dto.MailResponse;
import com.sophi.app.mail.service.EmailService;
import com.sophi.app.models.entity.Proyecto;
import com.sophi.app.models.entity.ProyectoRecurso;
import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.entity.RecursoGasto;
import com.sophi.app.models.entity.TipoGasto;
import com.sophi.app.models.service.IProyectoRecursoService;
import com.sophi.app.models.service.IProyectoService;
import com.sophi.app.models.service.IRecursoGastoService;
import com.sophi.app.models.service.IRecursoService;
import com.sophi.app.models.service.ITipoGastoService;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("recursoGasto")
public class RecursoGastoController {
	
	@Autowired
	private EmailService service;

	@Autowired
	private ITipoGastoService tipoGastoService;

	@Autowired
	private IProyectoRecursoService proyectoRecursoService;

	@Autowired
	private IProyectoService proyectoService;

	@Autowired
	private IRecursoGastoService recursoGastoService;

	@Autowired
	private IRecursoService recursoService;

	// Carga de pagina
	@RequestMapping(value = "/recursoGastoAlta/{email}", method = RequestMethod.GET)
	public String recursosGastosA(Model model, @PathVariable(value = "email") String email) {
		model.addAttribute("titulo", "Gastos");
		Long codRecurso = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
		List<ProyectoRecurso> listaProRec = proyectoRecursoService.findProyectoRecursoActivo(codRecurso);
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
		RecursoGasto recursoGasto = new RecursoGasto();

		for (ProyectoRecurso proRec : listaProRec) {
//			listaProyecto.add(proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(
//					proRec.getProyectoRecursoId().getCodProyecto(),
//					proRec.getProyectoRecursoId().getCodEstatusProyecto(),
//					proRec.getProyectoRecursoId().getCodCliente()));
			if(!proRec.getProyectoRecursoId().getCodEstatusProyecto().equals(3L) || !proRec.getProyectoRecursoId().getCodEstatusProyecto().equals(4L)) {
				listaProyecto.add(proyectoService.findByCodProyecto(proRec.getProyectoRecursoId().getCodProyecto()));
			}
		}

		System.out.println("CodRecurso " + codRecurso);
		model.addAttribute("tiposGastos", tipoGastoService.findAll());
		model.addAttribute("proyectosAsignados", listaProyecto);
		model.addAttribute("recursoGasto", recursoGasto);
		model.addAttribute("r", codRecurso);

		return "recursoGastoAlta";

	}

	// Guarda gasto
	@RequestMapping(value = "/recursoGastoAlta", method = RequestMethod.POST)
	public String recursoGastoAlta(Map<String, Object> modelM, @Valid RecursoGasto recursoGasto,
			@RequestParam("compImg") MultipartFile compImg, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status) {
		model.addAttribute("titulo", "Gastos");
		Proyecto p = proyectoService.findByCodProyecto(recursoGasto.getCodProyecto());
		Date fechaHoy = new Date();
		List<ProyectoRecurso> listaProRec = proyectoRecursoService
				.findByProyectoRecursoIdCodRecurso(recursoGasto.getCodRecurso());
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
		TipoGasto tp = tipoGastoService.findOne(recursoGasto.getTipoGasto().getCodTipoGasto());
		recursoGasto.setTipoGasto(tp);

		recursoGasto.setCodCliente(p.getCodCliente());
		recursoGasto.setCodEstatusProyecto(p.getCodEstatusProyecto());
		recursoGasto.setCodProyecto(p.getCodProyecto());

		recursoGasto.setFecRegistro(fechaHoy);

		if (recursoGasto.getComprobante() == null || recursoGasto.getComprobante().length == 0) {
			if (recursoGasto.getCodRecursoGasto() != null) {
				RecursoGasto rg = recursoGastoService.findOne(recursoGasto.getCodRecursoGasto());
				if (rg != null) {
					recursoGasto.setComprobante(rg.getComprobante());
				}
			}
		}

		if (!compImg.isEmpty()) {
			try {
				byte[] bytesImg = compImg.getBytes();
				recursoGasto.setComprobante(bytesImg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		recursoGastoService.save(recursoGasto);
		
		//Se envia notificacion para aprobacion de gastos al aprobador
		try {
			enviaNotificacionAprobacionGasto(recursoGasto);
		} catch (Exception e) {
			System.out.println("NOTIFICACIONES : <Error> No se puede enviar notificacion al aprobador para un nuevo gasto.");
		}
		

		for (ProyectoRecurso proRec : listaProRec) {
			proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(
					proRec.getProyectoRecursoId().getCodProyecto(),
					proRec.getProyectoRecursoId().getCodEstatusProyecto(),
					proRec.getProyectoRecursoId().getCodCliente());
			listaProyecto.add(proyectoService.findByCodProyectoAndCodEstatusProyectoAndCodCliente(
					proRec.getProyectoRecursoId().getCodProyecto(),
					proRec.getProyectoRecursoId().getCodEstatusProyecto(),
					proRec.getProyectoRecursoId().getCodCliente()));
		}

		model.addAttribute("tiposGastos", tipoGastoService.findAll());
		model.addAttribute("proyectosAsignados", listaProyecto);
		model.addAttribute("recursoGasto", recursoGasto);
		model.addAttribute("r", recursoGasto.getCodRecurso());

		List<RecursoGasto> listaRG = recursoGastoService.findByCodRecurso(recursoGasto.getCodRecurso());

		model.addAttribute("listaGastos", listaRG);

		return "redirect:/misGastos/" + recursoService.findOne(recursoGasto.getCodRecurso()).getDescCorreoElectronico();

	}
	
	//Notificacion hacia el aprobador notificando un nuevo gasto de un recurso
	public void enviaNotificacionAprobacionGasto(RecursoGasto recursoGastos) {
		System.out.println(recursoGastos.getCodProyecto());
		System.out.println(recursoGastos.getCodRecurso());
		
		Proyecto proyecto = new Proyecto(); 
		proyecto = proyectoService.findOne(recursoGastos.getCodProyecto());
		Recurso aprobador = new Recurso(); 
		aprobador = recursoService.findOne(proyecto.getCodRecursoAprobador());
		
		Recurso recurso = recursoService.findOne(recursoGastos.getCodRecurso());
		
		MailRequest request = new MailRequest();
		request.setName(aprobador.getDescRecurso());
		request.setSubject("Nuevo gasto para aprobar");
		request.setTo(aprobador.getDescCorreoElectronico());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nombreRecurso", request.getName());
		model.put("mensaje", "<h3>Tienes un nuevo gasto que aprobar de "+ recurso.getDescRecurso() + " en el proyecto \""+ proyecto.getDescProyecto() + "\"</h3>");
		model.put("imagen","<img data-cfsrc=\"images/status.png\" alt=\"\" data-cfstyle=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" style=\"width: 200px; max-width: 400px; height: auto; margin: auto; display: block;\" src=\"https://"+new Utiles().getHostName()+".com/img/img-status.png\">");
		model.put("pie", "");
		
		MailResponse response = service.sendEmail(request, model);
		System.out.println(response.getMessage());
	}
	
	
	

	// lista de gastos recurso
	@RequestMapping(value = "/misGastos/{email}", method = RequestMethod.GET)
	public String listaRecursoGastos(Model model, @PathVariable(value = "email") String email) {
		model.addAttribute("titulo", "Gastos");
		Long codRecurso = recursoService.findByDescCorreoElectronico(email).getCodRecurso();
		List<RecursoGasto> listaRG = recursoGastoService.findByCodRecurso(codRecurso);

		model.addAttribute("listaGastos", listaRG);
		model.addAttribute("r", codRecurso);

		return "listaRecursoGastos";

	}

	// editar de pagina
	@RequestMapping(value = "/editarRecursoGastos/{codRecursoGasto}/{codTipoGasto}/{codProyecto}/{codRecurso}/{codCliente}/{codEstatusProyecto}", method = RequestMethod.GET)
	public String edicionRecursoGasto(Model model, @PathVariable(value = "codRecursoGasto") long codRecursoGasto,
			@PathVariable(value = "codTipoGasto") long codTipoGasto,
			@PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codRecurso") long codRecurso,
			@PathVariable(value = "codCliente") long codCliente,
			@PathVariable(value = "codEstatusProyecto") long codEstatusProyecto) {
		model.addAttribute("titulo", "Gastos");
		List<ProyectoRecurso> listaProRec = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(codRecurso);
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();

		RecursoGasto recursoGasto = recursoGastoService.findOne(codRecursoGasto);

		for (ProyectoRecurso proRec : listaProRec) {
			listaProyecto.add(proyectoService.findByCodProyecto(proRec.getProyectoRecursoId().getCodProyecto()));
		}

		System.out.println("CodRecurso " + codRecurso);
		model.addAttribute("tiposGastos", tipoGastoService.findAll());
		model.addAttribute("proyectosAsignados", listaProyecto);
		model.addAttribute("recursoGasto", recursoGasto);
		model.addAttribute("r", codRecurso);

		List<RecursoGasto> listaRG = recursoGastoService.findByCodRecurso(recursoGasto.getCodRecurso());

		model.addAttribute("listaGastos", listaRG);

		return "recursoGastoAlta";

	}

	// Ver de pagina
	@RequestMapping(value = "/verRecursoGastos/{codRecursoGasto}/{codTipoGasto}/{codProyecto}/{codRecurso}/{codCliente}/{codEstatusProyecto}", method = RequestMethod.GET)
	public String verRecursoGasto(Model model, @PathVariable(value = "codRecursoGasto") long codRecursoGasto,
			@PathVariable(value = "codTipoGasto") long codTipoGasto,
			@PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codRecurso") long codRecurso,
			@PathVariable(value = "codCliente") long codCliente,
			@PathVariable(value = "codEstatusProyecto") long codEstatusProyecto) {
		model.addAttribute("titulo", "Gastos");
		List<ProyectoRecurso> listaProRec = proyectoRecursoService.findByProyectoRecursoIdCodRecurso(codRecurso);
		List<Proyecto> listaProyecto = new ArrayList<Proyecto>();

		RecursoGasto recursoGasto = recursoGastoService.findOne(codRecursoGasto);

		for (ProyectoRecurso proRec : listaProRec) {
			
			if(!proRec.getProyectoRecursoId().getCodEstatusProyecto().equals(3L) || !proRec.getProyectoRecursoId().getCodEstatusProyecto().equals(4L)) {
				listaProyecto.add(proyectoService.findByCodProyecto(proRec.getProyectoRecursoId().getCodProyecto()));
			}
		}

		
		
		System.out.println("CodRecurso " + codRecurso);
		model.addAttribute("tiposGastos", tipoGastoService.findAll());
		model.addAttribute("proyectosAsignados", listaProyecto);
		model.addAttribute("recursoGasto", recursoGasto);
		model.addAttribute("r", codRecurso);
		model.addAttribute("codProyecto", codProyecto); 
		return "recursoGastoVer";

	}

	// imagen de gasto
	@GetMapping(value = "/imagenGasto/{codRecursoGasto}/{codTipoGasto}/{codProyecto}/{codRecurso}/{codCliente}/{codEstatusProyecto}")
	public void verImagenGasto(@PathVariable(value = "codRecursoGasto") long codRecursoGasto,
			@PathVariable(value = "codTipoGasto") long codTipoGasto,
			@PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codRecurso") long codRecurso,
			@PathVariable(value = "codCliente") long codCliente,
			@PathVariable(value = "codEstatusProyecto") long codEstatusProyecto, HttpServletResponse response)
			throws IOException {
		response.setContentType("image/jpeg");
		RecursoGasto recursoGasto = recursoGastoService.findOne(codRecursoGasto);
		InputStream is = new ByteArrayInputStream(recursoGasto.getComprobante());
		IOUtils.copy(is, response.getOutputStream());
	}

	// imagen de comprobante
	@GetMapping(value = "/imagenComprobante/{codRecursoGasto}")
	public void verComprobanteGasto(@PathVariable(value = "codRecursoGasto") long codRecursoGasto,
			HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		RecursoGasto recursoGasto = recursoGastoService.findOne(codRecursoGasto);
		InputStream is = new ByteArrayInputStream(recursoGasto.getComprobante());
		IOUtils.copy(is, response.getOutputStream());
	}

	// eliminar recursoGasto
	// Carga de pagina
	@RequestMapping(value = "/eliminarRecursoGastos/{codRecursoGasto}/{codTipoGasto}/{codProyecto}/{codRecurso}/{codCliente}/{codEstatusProyecto}", method = RequestMethod.GET)
	public String eliminarRecursoGasto(Model model, @PathVariable(value = "codRecursoGasto") long codRecursoGasto,
			@PathVariable(value = "codTipoGasto") long codTipoGasto,
			@PathVariable(value = "codProyecto") long codProyecto, @PathVariable(value = "codRecurso") long codRecurso,
			@PathVariable(value = "codCliente") long codCliente,
			@PathVariable(value = "codEstatusProyecto") long codEstatusProyecto) {
		RecursoGasto rg = recursoGastoService.findOne(codRecursoGasto);
		recursoGastoService.delete(rg);
		model.addAttribute("titulo", "Gastos");
		return "redirect:/misGastos/" + recursoService.findOne(rg.getCodRecurso()).getDescCorreoElectronico();

	}

}