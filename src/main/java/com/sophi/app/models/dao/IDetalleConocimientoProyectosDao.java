package com.sophi.app.models.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.DetalleConocimientosProyecto;

public interface IDetalleConocimientoProyectosDao extends CrudRepository<DetalleConocimientosProyecto, Long>{

	public List<DetalleConocimientosProyecto> findByRecursoTrayectoriaProyectoCodTrayectoriaProyecto(Long codTrayectoriaProyecto);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DETALLE_CONOCIMIENTOS_PROYECTOS WHERE cod_trayectoria_proyecto=?1", nativeQuery = true)
	public void borrarByCodTrayectoriaProyecto(Long codTrayectoriaProyecto);
	
	
	@Query(value = "select distinct(cod_conocimiento) from DETALLE_CONOCIMIENTOS_PROYECTOS where cod_trayectoria_proyecto in (select rtp.cod_trayectoria_proyecto from RECURSOS_TRAYECTORIA_PROYECTOS rtp where rtp.cod_recurso = ?1)", nativeQuery = true)
	public List<Long> findConocimientosDistintosPorRecurso(Long codRecurso);
	
	@Modifying
	@Query(value = "INSERT INTO DETALLE_CONOCIMIENTOS_PROYECTOS (cod_trayectoria_proyecto, cod_conocimiento) VALUES (?1, ?2)", nativeQuery = true)
	public void insertOneDCP(Long codTrayectoriaProyecto, Long codConocimiento);
	
}
