package com.sophi.app.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.DetalleProyectoInfraestructura;

public interface IDetalleProyectoInfraestructuraDao extends CrudRepository<DetalleProyectoInfraestructura, Long>{
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_INFRAESTRUCTURAS WHERE cod_proyecto=?1", nativeQuery = true)
	void borrarByCodProyecto(Long codProyecto);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_INFRAESTRUCTURAS WHERE cod_proyecto=?1 and cod_estatus_proyecto=?2", nativeQuery = true)
	void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_INFRAESTRUCTURAS WHERE cod_proyecto=?1 and cod_estatus_proyecto=?2 and cod_cliente=?3", nativeQuery = true)
	void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente);
	
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyecto(Long codProyecto);
	
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DETALLE_PROYECTOS_INFRAESTRUCTURAS SET cod_estatus_proyecto=?2 WHERE cod_proyecto=?1", nativeQuery = true)
	void actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(Long codProyecto, Long codEstatusProyecto);
}