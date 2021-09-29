package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.DetalleRecursoHoras;
import com.sophi.app.models.entity.LiderProyectoEvaluacion;

public interface ICapHoraService {

	public List<CapHora> findAll();
	
	public void save(CapHora capHora);
	
	public CapHora findOne(Long codCapHora);
	
	public void delete(CapHora capHora);
	
	public List<CapHora> findListCapHoraByFechaRecurso(Date fecha, Long codRecurso);
	
	public Float findSumHorasReportadasSemana(Long codRecurso, Date fechaInicio, Date fechaFin);
	
//	public List<DetalleRecursoHoras> findDetalleRecursoHoras(List<Long> codRecurso, List<Long> codProyecto, Date fecInicial, Date fecFinal);
	
	public DetalleRecursoHoras findDetalleRecursoHoras(Long codRecurso,Long codProyecto, Date fecInicial, Date fecFinal);
	
	public DetalleRecursoHoras findDetalleRecursoHorasTodos(Long codRecurso, Date fecInicial, Date fecFinal);
	
	public List<DetalleRecursoHoras> findRecursoHorasRechazo(Date fecInicial, Date fecFinal);
	
	List<CapHora> findRecursoHorasRechazoCustom(Date fecInicial, Date fecFinal);
	
	public List<Long> findProyectosCapturadosSemana(Date fecInicial, Date fecFinal);

	public List<Long> findRecursoCapturadosSemana(Date fecInicial, Date fecFinal);
	
	public List<CapHora> findListCapHoraByPeriodoFechaRecurso(Long codRecurso, Date fecInicial, Date fecFinal);
	
	public List<DetalleRecursoHoras> findProyectoRecursosResumenSemanal(Long codProyecto, Date fecInicial, Date fecFinal);
	
	public List<LiderProyectoEvaluacion> findCodAprobadoresByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin);
	
	public List<String> findProyectosByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin);
	
	public void copiarRegistroCapHora(Long codActividad, Long codRecurso, String descComentarioDetalle, Date fecInicioActividad, Date fecRegistro, Long codProyecto, float valDuracionReportada, float valDuracionValidada, float valDuracionRechazada, Long valRechazo, Long codCliente, Long valNuevaActividad, Long codEstatusProyecto);
	
}
