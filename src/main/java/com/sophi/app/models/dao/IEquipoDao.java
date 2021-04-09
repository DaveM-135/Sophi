package com.sophi.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.sophi.app.models.entity.Equipo;

public interface IEquipoDao extends CrudRepository<Equipo, Long>{
	
}
