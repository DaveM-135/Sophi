package com.sophi.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.DetalleRecursoHoras;
import com.sophi.app.models.entity.LiderProyectoEvaluacion;

public interface ICapHoraDao extends CrudRepository<CapHora, Long>{
	
	@Query("FROM CapHora C WHERE C.fecInicioActividad = ?1 AND C.codRecurso = ?2")
	List<CapHora> findListCapHoraByFechaRecurso(Date fecha, Long codRecurso);
	
	@Query("FROM CapHora C WHERE C.codRecurso = ?1 AND C.fecInicioActividad BETWEEN ?2 AND ?3")
	List<CapHora> findListCapHoraByPeriodoFechaRecurso(Long codRecurso, Date fecInicial, Date fecFinal);
	
	@Query("SELECT  SUM(valDuracionReportada) FROM CapHora C WHERE C.codRecurso = ?1 AND C.fecInicioActividad  BETWEEN ?2 AND ?3")
	Float findSumHorasReportadasSemana(Long codRecurso, Date fechaInicio, Date fechaFin);
	
	@Query("SELECT  "
			+ "new com.sophi.app.models.entity.DetalleRecursoHoras(SUM(ch.valDuracionReportada), SUM(ch.valDuracionValidad), SUM(ch.valDuracionRechazada))" +
	           "FROM  CapHora ch " +
	           "WHERE  ch.codRecurso = ?1 AND ch.codProyecto= ?2 and ch.fecInicioActividad BETWEEN ?3 AND ?4 ")
	DetalleRecursoHoras findDetalleRecursoHoras(Long codRecurso, Long codProyecto, Date fecInicial, Date fecFinal);
	
	@Query("SELECT  "
			+ "new com.sophi.app.models.entity.DetalleRecursoHoras(SUM(ch.valDuracionReportada), SUM(ch.valDuracionValidad), SUM(ch.valDuracionRechazada))" +
	           "FROM  CapHora ch " +
	           "WHERE  ch.codRecurso = ?1 and ch.fecInicioActividad BETWEEN ?2 AND ?3 ")
	DetalleRecursoHoras findDetalleRecursoHorasTodos(Long codRecurso, Date fecInicial, Date fecFinal);

	@Query(value="SELECT  "
			+ "new com.sophi.app.models.entity.DetalleRecursoHoras(r.descRecurso, ch.codRecurso, SUM(ch.valDuracionRechazada)) " +
		       "FROM  CapHora AS ch INNER JOIN Recurso AS r ON ch.codRecurso = r.codRecurso " +
		       "WHERE ch.fecInicioActividad BETWEEN :fecInicial AND :fecFinal AND ch.valRechazo = 1 AND r.valActivo = 1 "+
		       "GROUP BY r.descRecurso, ch.codRecurso")
			List<DetalleRecursoHoras> findRecursoHorasRechazo(@Param("fecInicial") Date fecInicial,@Param("fecFinal") Date fecFinal);
	
	@Query(value="FROM  CapHora AS ch INNER JOIN Recurso AS r ON ch.codRecurso = r.codRecurso " +
		       "WHERE ch.fecInicioActividad BETWEEN :fecInicial AND :fecFinal AND ch.valRechazo = 1 AND r.valActivo = 1 " )
			List<CapHora> findRecursoHorasRechazoCustom(@Param("fecInicial") Date fecInicial,@Param("fecFinal") Date fecFinal);
	
	@Query("SELECT DISTINCT(codProyecto) FROM CapHora C WHERE C.fecInicioActividad  BETWEEN ?1 AND ?2")
	List<Long> findProyectosCapturadosSemana(Date fechaInicio, Date fechaFin);
	
	@Query("SELECT DISTINCT(codRecurso) FROM CapHora C WHERE C.fecInicioActividad  BETWEEN ?1 AND ?2")
	List<Long> findRecursoCapturadosSemana(Date fechaInicio, Date fechaFin);
	
	@Query(value="SELECT  "
			+ "new com.sophi.app.models.entity.DetalleRecursoHoras(r.descRecurso, ch.codRecurso, SUM(ch.valDuracionReportada), SUM(ch.valDuracionValidad), SUM(ch.valDuracionRechazada)) " +
		       "FROM  CapHora AS ch INNER JOIN Recurso AS r ON ch.codRecurso = r.codRecurso " +
		       "WHERE ch.fecInicioActividad BETWEEN :fecInicial AND :fecFinal AND ch.codProyecto = :codProyecto  AND r.valActivo = 1 "+
		       "GROUP BY r.descRecurso, ch.codRecurso")
			List<DetalleRecursoHoras> findProyectoRecursosResumenSemanal(@Param("codProyecto") Long codProyecto, @Param("fecInicial") Date fecInicial,@Param("fecFinal") Date fecFinal);
	
	@Query("SELECT new com.sophi.app.models.entity.LiderProyectoEvaluacion(p.codRecursoAprobador)"
			+ " FROM Proyecto p WHERE p.codRecursoAprobador <> ?1 AND p.codProyecto IN ( SELECT DISTINCT(codProyecto) FROM CapHora c"
			+ " WHERE c.codRecurso = ?1 AND c.fecInicioActividad BETWEEN ?2 AND ?3) group by p.codRecursoAprobador")
	List<LiderProyectoEvaluacion> findCodAprobadoresByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin);
	
	@Query("SELECT DISTINCT(p.descProyecto)"
			+ " FROM Proyecto p WHERE p.codRecursoAprobador <> ?1 AND p.codProyecto IN ( SELECT DISTINCT(codProyecto) FROM CapHora c"
			+ " WHERE c.codRecurso = ?1 AND c.fecInicioActividad BETWEEN ?2 AND ?3)")
	List<String> findProyectosByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin);
	
	@Modifying
	@Query(value = "INSERT INTO RECURSOS_CAP_HORAS (cod_actividad, cod_recurso, desc_comentario_detalle, fec_inicio_actividad, fec_registro, cod_proyecto, val_duracion_reportada, val_duracion_validada, val_duracion_rechazada, val_rechazo, cod_cliente, val_nueva_actividad, cod_estatus_proyecto) VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13)", nativeQuery = true)
	void copiarRegistroCapHora(Long codActividad, Long codRecurso, String descComentarioDetalle, Date fecInicioActividad, Date fecRegistro, Long codProyecto, float valDuracionReportada, float valDuracionValidada, float valDuracionRechazada, Long valRechazo, Long codCliente, Long valNuevaActividad, Long codEstatusProyecto);
}
