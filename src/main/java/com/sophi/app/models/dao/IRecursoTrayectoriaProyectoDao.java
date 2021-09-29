package com.sophi.app.models.dao;

import java.util.Date;
//import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sophi.app.models.entity.RecursoTrayectoriaProyecto;

public interface IRecursoTrayectoriaProyectoDao  extends CrudRepository<RecursoTrayectoriaProyecto, Long>{
	
	@Query(value="SELECT * FROM RECURSOS_TRAYECTORIA_PROYECTOS WHERE cod_recurso = ?1 ORDER BY fec_fin_participacion DESC", nativeQuery = true)
	List<RecursoTrayectoriaProyecto> findByCodRecurso(Long codRecurso);
	
	@Modifying
	@Query(value="INSERT INTO RECURSOS_TRAYECTORIA_PROYECTOS (cod_recurso, desc_proyecto, desc_actividades, fec_inicio_participacion, fec_fin_participacion, desc_cliente) VALUES (?1, ?2, ?3,?4, ?5, ?6)", nativeQuery = true)
	void insertOne(Long codRecurso, String descProyecto, String descActividades, Date fecInicioParticipacion, Date fecFinParticipacion, String descCliente);
	
	@Query(value="SELECT DISTINCT codTrayectoriaProyecto FROM RecursoTrayectoriaProyecto WHERE descProyecto = ?1")
	List<Long> findCodTrayectoriaProyectoByDescProyecto(String descProyecto);

}
