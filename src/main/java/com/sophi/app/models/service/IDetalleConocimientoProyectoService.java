package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.entity.DetalleConocimientosProyecto;

public interface IDetalleConocimientoProyectoService {
	
	public List<DetalleConocimientosProyecto> findByCodTrayectoriaProyecto(Long codTrayectoriaProyecto);
	
	public void save(DetalleConocimientosProyecto detalleConocimientosProyecto);
	
	public void saveAll(List<DetalleConocimientosProyecto> ListDetalleConocimientosProyecto);
	
	public void delete(Long codDetalleConocimiento);
	
	public DetalleConocimientosProyecto findById(Long codDetalleConocimiento);
	
	public void borrarByCodTrayectoriaProyecto(Long codTrayectoriaProyecto);
	
	public List<Long> conocimientosDistintosPorRecurso(Long codRecurso);
	
	public void insertOneDCP(Long codTrayectoriaProyecto, Long codConocimiento);

}
