package com.sophi.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.Proyecto;

public interface IProyectoDao extends CrudRepository<Proyecto, Long> {

//	Proyecto findByDescProyectoAndCodClienteAndCodEstatusProyectoAndFecRegistro(String descProyecto, Long codCliente, Long codEstatusProyecto, Date fecRegistro);
	Proyecto findByDescProyectoAndCodClienteAndCodEstatusProyecto(String descProyecto, Long codCliente, Long codEstatusProyecto);
	
	List<Proyecto> findByCodCliente(Long codCliente);
	
	Proyecto findByCodProyecto(Long codProyecto);
	
	Proyecto findByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto);
	
	Proyecto findByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente);
	
	List<Proyecto> findByCodRecursoLider(Long codRecursoLider);
	
//	@Query("FROM Proyecto Pr WHERE Pr.codEstatusProyecto = 2 and Pr.CodRecursoLider = ?1 ")
//	List<Proyecto> findByCodRecursoLiderProyectosActivos(Long codRecursoLider);
//	
//	@Query("FROM Proyecto Pr WHERE Pr.codEstatusProyecto = 2 and Pr.CodRecursoLider = ?1 and Pr.codCliente = ?2 ")
//	List<Proyecto> findByCodRecursoLiderClientes(Long codRecursoLider, Long codCliente);	
	
	@Query("FROM Proyecto Pr WHERE Pr.codEstatusProyecto = 2 ")
	List<Proyecto> findProyectosActivos();
	
	@Query("SELECT DISTINCT(Pr.codCliente) FROM Proyecto Pr WHERE Pr.codRecursoAprobador = ?1  OR Pr.codRecursoLider = ?1")
	List<Long> findListaClientesRecursoAprobador(Long codRecursoAprobador);
	
	@Query("SELECT DISTINCT(Pr.codCliente) FROM Proyecto Pr WHERE Pr.codRecursoLider = ?1 ")
	List<Long> findListaClientesRecursoLider(Long codRecursoLider);
	
	@Query("FROM Proyecto Pr WHERE (Pr.codRecursoAprobador = ?1  OR Pr.codRecursoLider = ?1) AND Pr.codCliente = ?2")
	List<Proyecto> findListaProyectosRecursoAprobador(Long codRecursoAprobador, Long codCliente);
	
	@Query("FROM Proyecto Pr WHERE Pr.codRecursoLider = ?1  AND Pr.codCliente = ?2")
	List<Proyecto> findListaProyectosRecursoLider(Long codRecursoLider, Long codCliente);
	
	@Query("FROM Proyecto Pr WHERE Pr.codRecursoAprobador = ?1  OR Pr.codRecursoLider = ?1")
	List<Proyecto> findListaProyectosRecursoAprobadorTodos(Long codRecursoAprobador);
	
	@Query("FROM Proyecto Pr WHERE Pr.codRecursoAprobadorBKP = ?1")
	List<Proyecto> findListaProyectosRecursoAprobadorBKPTodos(Long codRecursoAprobadorBKP);
	
	@Query("FROM Proyecto Pr WHERE Pr.codRecursoLider = ?1")
	List<Proyecto> findListaProyectosRecursoLiderTodos(Long codRecursoLider);
	
	@Query("FROM Proyecto Pr WHERE Pr.fecFinProyecto < ?1 AND Pr.codEstatusProyecto NOT IN (3)")
	List<Proyecto> findListaProyectosPorCerrar(Date fecFinProyecto);

}
