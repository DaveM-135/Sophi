package com.sophi.app.models.service;

import com.sophi.app.models.entity.Capacitaciones;

public interface ICapacitacionesService {
	
	public Capacitaciones findById(Long codCapacitacion);
	
	public void save(Capacitaciones capacitacion);
	
	public void delete(Long codCapacitacion);

}
