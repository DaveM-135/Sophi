package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import com.sophi.app.models.entity.Proyecto;

public interface IProyectoService {
	
	public List<Proyecto> findAll();
	
	public void save(Proyecto proyecto);
	
	public Proyecto findOne(Long codProyecto);
	
//	public Proyecto findByDescProyectoAndCodClienteAndCodEstatusProyectoAndFecRegistro(String descProyecto, Long codCliente, Long codEstatusProyecto, Date fecRegistro);
	public Proyecto findByDescProyectoAndCodClienteAndCodEstatusProyecto(String descProyecto, Long codCliente, Long codEstatusProyecto);
	
	public List<Proyecto> findByCodCliente(Long codCliente);
	
	public Proyecto findByCodProyecto(Long codProyecto);
	
	public Proyecto findByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto);
	
	public List<Proyecto> findByCodRecursoLider(Long codRecursoLider);
	
	public Proyecto findByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente);
	
	public List<Proyecto> findProyectosActivos();
	
	public List<Long> findListaClientesRecursoAprobador(Long codRecursoAprobador);
	
	public List<Long> findListaClientesRecursoLider(Long codRecursoLider);
	
	public List<Proyecto> findListaProyectosRecursoAprobador(Long codRecursoAprobador, Long codCliente);
	
	public List<Proyecto> findListaProyectosRecursoLider(Long codRecursoLider, Long codCliente);
	
	public List<Proyecto> findListaProyectosRecursoAprobadorTodos(Long codRecursoAprobador);
	
	public List<Proyecto> findListaProyectosRecursoAprobadorBKPTodos(Long codRecursoAprobadorBKP);
	
	public List<Proyecto> findListaProyectosRecursoLiderTodos(Long codRecursoLider);
	
	public List<Proyecto> findListaProyectosPorCerrar(Date fecFinProyecto);

}
