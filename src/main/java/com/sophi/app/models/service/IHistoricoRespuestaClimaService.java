package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.entity.HistRespuestaAux;

public interface IHistoricoRespuestaClimaService {
	
	public List<HistRespuestaAux> resultados(long idDia);
	
	public Integer totalParticipacion(long idDia);

}
