package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.entity.DetalleProyectoInfraestructura;

public interface IDetalleProyectoInfraestructuraService {
	
	public List<DetalleProyectoInfraestructura> findAll();
	
	public void save(DetalleProyectoInfraestructura detalleProyectoInfraestructura);
	
	public void borrarByCodProyecto(Long codProyecto);
	
	public void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto);
	
	public void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente);
	
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyecto(Long codProyecto);
	
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente);

	public void actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(Long codProyecto, Long codEstatusProyecto);
	
}
