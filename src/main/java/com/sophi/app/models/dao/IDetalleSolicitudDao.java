package com.sophi.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.DetalleSolicitud;

public interface IDetalleSolicitudDao extends CrudRepository<DetalleSolicitud, Long>{
	
	@Query(value = "FROM DetalleSolicitud d WHERE d.solicitudVacaciones.codSolicitud = ?1 ")
	public List<DetalleSolicitud> findByCodSolicitud(Long codSolicitud);
	
	@Query(value = "FROM DetalleSolicitud d")
	public List<DetalleSolicitud> findAll();

}
