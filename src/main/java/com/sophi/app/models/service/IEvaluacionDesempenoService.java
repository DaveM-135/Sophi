package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import com.sophi.app.models.entity.EvaluacionDesempeno;

public interface IEvaluacionDesempenoService {

	public List<EvaluacionDesempeno> findAll();
	
	public EvaluacionDesempeno findById(Long codEvaluacionDesempeno);
	
	public void guardar(EvaluacionDesempeno evaluacionDesempeno);
	
	public void delete(Long codEvaluacionDesempeno);
	
	public void updateCierreEvaluacionDesempeno(Long codEvaluacionDesempeno,Long valEstatus,Date fecCierre, Float promedioEmpresa);
	
	public Float getHorasHabilesByMes(Long mesIdInicio, Long mesIdFin);
		
	public	Float getHorasHabilesValidadasByCodRecursoAndMes(Long codRecurso, Date fecInicio, Date fecFin);

	public	Float getAvgCsiByCodRecursoAndMes(Long codRecurso, Date fecInicio, Date fecFin);
	
}
