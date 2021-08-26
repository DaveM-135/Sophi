package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.entity.DetalleSolicitud;

public interface IDetalleSolicitudService {
	
	public DetalleSolicitud findById(Long codDetalleSolicitud);
	
	public List<DetalleSolicitud> findByCodSolicitud(Long codSolicitud);
	
	public List<DetalleSolicitud> findAll();

}
