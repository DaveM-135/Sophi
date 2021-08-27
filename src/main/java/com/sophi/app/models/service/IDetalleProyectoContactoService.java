package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.entity.DetalleProyectoContacto;
import com.sophi.app.models.entity.DetalleProyectoContactoId;

public interface IDetalleProyectoContactoService {
	
	public List<DetalleProyectoContacto> findAll();
	
	public void save(DetalleProyectoContacto detalleProyectoContacto);
	
	public void borrarByCodProyecto(Long codProyecto);
	
	public DetalleProyectoContacto findByDetalleProyectoContactoId(DetalleProyectoContactoId detalleProyectoContactoId);
	
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyecto(Long codProyecto);
	
	public void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto,Long codEstatusProyecto);
	
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente);
	
	public void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto,Long codEstatusProyecto, Long codCliente);
	
	public Long findTotalProyectosResponsable(Long codContacto);

	public void actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(Long codProyecto, Long codEstatusProyecto);
	
}
