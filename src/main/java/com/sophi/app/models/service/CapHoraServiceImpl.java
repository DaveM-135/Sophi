package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.ICapHoraDao;
import com.sophi.app.models.entity.CapHora;
import com.sophi.app.models.entity.DetalleRecursoHoras;
import com.sophi.app.models.entity.LiderProyectoEvaluacion;

@Service
public class CapHoraServiceImpl implements ICapHoraService {
	
	@Autowired
	private ICapHoraDao capHoraDao;

	@Override
	@Transactional(readOnly = true)
	public List<CapHora> findAll() {
		return (List<CapHora>) capHoraDao.findAll();
	}

	@Override
	@Transactional
	public void save(CapHora capHora) {
		capHoraDao.save(capHora);
	}

	@Override
	@Transactional(readOnly = true)
	public CapHora findOne(Long codCapHora) {
		return capHoraDao.findById(codCapHora).orElse(null);
	}

	@Override
	@Transactional
	public void delete(CapHora capHora) {
		capHoraDao.delete(capHora);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CapHora> findListCapHoraByFechaRecurso(Date fecha, Long codRecurso) {
		return capHoraDao.findListCapHoraByFechaRecurso(fecha, codRecurso);
	}

	@Override
	@Transactional(readOnly = true)
	public Float findSumHorasReportadasSemana(Long codRecurso, Date fechaInicio, Date fechaFin) {
		return capHoraDao.findSumHorasReportadasSemana(codRecurso, fechaInicio, fechaFin);
	}

	@Override
	@Transactional(readOnly = true)
	public DetalleRecursoHoras findDetalleRecursoHoras(Long codRecurso, Long codProyecto, Date fecInicial, Date fecFinal) {
		return capHoraDao.findDetalleRecursoHoras(codRecurso, codProyecto, fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public DetalleRecursoHoras findDetalleRecursoHorasTodos(Long codRecurso, Date fecInicial, Date fecFinal) {
		return  capHoraDao.findDetalleRecursoHorasTodos(codRecurso, fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DetalleRecursoHoras> findRecursoHorasRechazo(Date fecInicial, Date fecFinal) {
		return capHoraDao.findRecursoHorasRechazo(fecInicial, fecFinal);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CapHora> findRecursoHorasRechazoCustom(Date fecInicial, Date fecFinal) {
		return capHoraDao.findRecursoHorasRechazoCustom(fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findProyectosCapturadosSemana(Date fecInicial, Date fecFinal) {
		return capHoraDao.findProyectosCapturadosSemana(fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findRecursoCapturadosSemana(Date fecInicial, Date fecFinal) {
		return capHoraDao.findRecursoCapturadosSemana(fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CapHora> findListCapHoraByPeriodoFechaRecurso(Long codRecurso, Date fecInicial, Date fecFinal) {
		return capHoraDao.findListCapHoraByPeriodoFechaRecurso(codRecurso, fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DetalleRecursoHoras> findProyectoRecursosResumenSemanal(Long codProyecto, Date fecInicial, Date fecFinal) {
		return capHoraDao.findProyectoRecursosResumenSemanal(codProyecto, fecInicial, fecFinal);
	}

	@Override
	@Transactional(readOnly = true)
	public List<LiderProyectoEvaluacion> findCodAprobadoresByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin) {
		return capHoraDao.findCodAprobadoresByCodRecursoAndFechaInicioAndFechaFin(codRecurso, fechaInicio, fechaFin);
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> findProyectosByCodRecursoAndFechaInicioAndFechaFin(Long codRecurso, Date fechaInicio, Date fechaFin) {
		return capHoraDao.findProyectosByCodRecursoAndFechaInicioAndFechaFin(codRecurso, fechaInicio, fechaFin);
	}
	
	@Override
	@Transactional
	public void copiarRegistroCapHora(Long codActividad, Long codRecurso, String descComentarioDetalle, Date fecInicioActividad, Date fecRegistro, Long codProyecto, float valDuracionReportada, float valDuracionValidada, float valDuracionRechazada, Long valRechazo, Long codCliente, Long valNuevaActividad, Long codEstatusProyecto) {
		capHoraDao.copiarRegistroCapHora(codActividad, codRecurso, descComentarioDetalle, fecInicioActividad, fecRegistro, codProyecto, valDuracionReportada, valDuracionValidada, valDuracionRechazada, valRechazo, codCliente, valNuevaActividad, codEstatusProyecto);
	}

}
