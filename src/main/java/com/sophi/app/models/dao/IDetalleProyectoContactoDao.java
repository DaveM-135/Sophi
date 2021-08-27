package com.sophi.app.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.DetalleProyectoContacto;
import com.sophi.app.models.entity.DetalleProyectoContactoId;

public interface IDetalleProyectoContactoDao extends CrudRepository<DetalleProyectoContacto, Long>{
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_CONTACTOS WHERE cod_proyecto=?1", nativeQuery = true)
	void borrarByCodProyecto(Long codProyecto);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_CONTACTOS WHERE cod_proyecto=?1 AND cod_estatus_proyecto=?2", nativeQuery = true)
	void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto,Long codEstatusProyecto);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_PROYECTOS_CONTACTOS WHERE cod_proyecto=?1 AND cod_estatus_proyecto=?2 and cod_cliente=?3", nativeQuery = true)
	void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto,Long codEstatusProyecto, Long codCliente);
	
	@Query(value = "select count(cod_contacto) from DETALLE_PROYECTOS_CONTACTOS where cod_contacto = ?1", nativeQuery = true)
	Long findTotalProyectosResponsable(Long codContacto);
	
	DetalleProyectoContacto findByDetalleProyectoContactoId(DetalleProyectoContactoId detalleProyectoContactoId);
	
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyecto(Long codProyecto);
	
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE DETALLE_PROYECTOS_CONTACTOS SET cod_estatus_proyecto = ?2 WHERE cod_proyecto = ?1", nativeQuery = true)
	void actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(Long codProyecto, Long codEstatusProyecto);
}