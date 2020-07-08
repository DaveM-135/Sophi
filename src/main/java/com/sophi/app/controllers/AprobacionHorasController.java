package com.sophi.app.controllers;

import java.util.ArrayList;
import java.util.List;

import com.sophi.app.models.entity.Recurso;
import com.sophi.app.models.service.IAprobacionHorasService;
import com.sophi.app.models.service.IRecursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("aprobacionhoras")
public class AprobacionHorasController {

    @Autowired
    private IAprobacionHorasService aprobacionHorasService;

    @Autowired
    private IRecursoService recursoService;

    @RequestMapping(value = "aprobaciónhoras", method = RequestMethod.GET)
    public String AprobacionHoras(Model model){
        List<Recurso> listaRecursos = new ArrayList<Recurso>();
        listaRecursos = recursoService.findAll();
        model.addAttribute("titulo", "Listado de horas capturadas");
        model.addAttribute("aprobacionhoras", aprobacionHorasService.findAll());
        model.addAttribute("recursos", listaRecursos);
        return "aprobaciónhoras";
    }

}
