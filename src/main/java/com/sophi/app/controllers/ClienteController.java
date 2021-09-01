package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sophi.app.models.entity.Cliente;
import com.sophi.app.models.entity.Sector;
import com.sophi.app.models.service.IClienteService;
import com.sophi.app.models.service.ISectorService;

@Controller
@SessionAttributes("clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private ISectorService sectorService;
	
	@RequestMapping(value = "/listaClientes", method = RequestMethod.GET)
	public String clientes(Model model) {
		model.addAttribute("titulo", "Lista de clientes");
		List<Cliente> listaClientes = new ArrayList<Cliente>();
		listaClientes = clienteService.findAll();
		Cliente cliente = clienteService.findOne(1L);
		listaClientes.remove(cliente);
		model.addAttribute("clientes", listaClientes);
		return "listaClientes";
	}
	
	@RequestMapping(value = "/dataCliente/{id}")
	public String verCliente(@PathVariable(value = "id") Long codCliente, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		if (codCliente > 0) {
			cliente = clienteService.findOne(codCliente);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El codigo del cliente no existe en base de datos!");
				return "redirect:/listaClientes";
			}
		} else {
			flash.addFlashAttribute("error", "El codigo del cliente no debe ser cero!");
			return "redirect:/listaClientes";
		}
		model.put("cliente", cliente);
		model.put("textoTitulo","Información de tu cliente");
		model.put("textoDescriptivo","Encuentra aquí la información completa de tu cliente.");
		return "dataCliente";
	}
	
	@RequestMapping(value = "/formCliente")
	public String crearCliente(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		List<Sector> sectores = sectorService.findAll();
		model.put("sectoresList",sectores);
		model.put("titulo", "Formulario de clientes");
		return "formCliente";
	}
	
	@RequestMapping(value="/formCliente", method = RequestMethod.POST)
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model,RedirectAttributes flash,SessionStatus status) {
		if(cliente.getCodCliente() != null) {
			String nombre_anterior = clienteService.findOne(cliente.getCodCliente()).getDescCliente();
			
			if(!nombre_anterior.equals(cliente.getDescCliente())) {
				cliente.setDescClienteAnterior(nombre_anterior);
			}
		}
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario cliente");
			List<Sector> sectores = sectorService.findAll();
			model.addAttribute("sectoresList",sectores);
			return "formCliente";
		}
		cliente.setValActivo(1L);
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", "Cliente guardado con éxito");
		return "redirect:/listaClientes";
	}
	
	@RequestMapping(value = "/formCliente/{id}")
	public String editarCliente(@PathVariable(value = "id") Long codCliente, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		
		if (codCliente > 0) {
			cliente = clienteService.findOne(codCliente);
			if(cliente == null) {
				flash.addFlashAttribute("error", "El codigo del cliente no existe en base de datos!");
				return "redirect:/listaClientes";
			}
		} else {
			flash.addFlashAttribute("error", "El codigo del cliente no debe ser cero!");
			return "redirect:/listaClientes";
		}
		List<Sector> sectores = sectorService.findAll();
		model.put("sectoresList",sectores);
		model.put("cliente", cliente);
		model.put("titulo", "Formulario cliente");
		return "formCliente";
	}
	
	@GetMapping(value = "/desactivarCliente/{id}")
    public String desactivarCliente(Model model, @PathVariable(value = "id") Long codCliente) {
		System.out.println(codCliente);
        //clienteService.delete(codCliente);
		Cliente cliente = clienteService.findOne(codCliente);
		cliente.setValActivo(0L);
		clienteService.save(cliente);
        return "redirect:/listaClientes";
    }
	
	@GetMapping(value = "/activarCliente/{id}")
    public String activarCliente(Model model, @PathVariable(value = "id") Long codCliente) {
		System.out.println(codCliente);
        //clienteService.delete(codCliente);
		Cliente cliente = clienteService.findOne(codCliente);
		cliente.setValActivo(1L);
		clienteService.save(cliente);
        return "redirect:/listaClientes";
    }
	
}
