package com.sophi.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sophi.app.models.entity.DiaFestivo;
import com.sophi.app.models.service.IDiaFestivoService;

@Controller
public class CalendarioSophitechController {

	@Autowired
	private IDiaFestivoService diaFestivoService;
	
	@GetMapping({"/obtenerCalendarioSophitech"})
	@ResponseBody
	public List<DiaFestivo> obtenerCalendarioSophitech(@RequestParam Long inicioMes, @RequestParam Long finMes, Model model) {
		
		List<DiaFestivo> listadoFestivosMes = diaFestivoService.findByCodDiaFestivoPorMes(inicioMes, finMes);
		
		return listadoFestivosMes;
	}
}
