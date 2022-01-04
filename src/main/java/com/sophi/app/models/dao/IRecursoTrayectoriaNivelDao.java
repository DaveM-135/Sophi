package com.sophi.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.RecursoTrayectoriaNivel;

public interface IRecursoTrayectoriaNivelDao extends CrudRepository<RecursoTrayectoriaNivel, Long> {
	
	public List<RecursoTrayectoriaNivel> findByCodRecurso(Long codRecurso);
	
	public RecursoTrayectoriaNivel findByCodRecursoAndCodConocimiento(Long codRecurso, Long codConocimiento);

}
