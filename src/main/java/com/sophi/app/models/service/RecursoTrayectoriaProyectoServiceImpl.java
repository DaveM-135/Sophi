package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IRecursoTrayectoriaProyectoDao;
import com.sophi.app.models.entity.RecursoTrayectoriaProyecto;

@Service
public class RecursoTrayectoriaProyectoServiceImpl implements IRecursoTrayectoriaProyectoService {
	
	@Autowired
	private IRecursoTrayectoriaProyectoDao recursoTrayectoriaProyectoDao;

	@Override
	@Transactional(readOnly = true)
	public List<RecursoTrayectoriaProyecto> findByCodRecurso(Long codRecurso) {
		return recursoTrayectoriaProyectoDao.findByCodRecurso(codRecurso);
	}

	@Override
	@Transactional(readOnly = true)
	public RecursoTrayectoriaProyecto findById(Long codRecursoTrayectoriaProyecto) {
		return recursoTrayectoriaProyectoDao.findById(codRecursoTrayectoriaProyecto).orElse(null);
	}

	@Override
	@Transactional
	public void save(RecursoTrayectoriaProyecto recursoTrayectoriaProyecto) {
		recursoTrayectoriaProyectoDao.save(recursoTrayectoriaProyecto);
	}

	@Override
	@Transactional
	public void delete(Long codRecursoTrayectoriaProyecto) {
		recursoTrayectoriaProyectoDao.deleteById(codRecursoTrayectoriaProyecto);
	}
	
	@Override
	@Transactional
	public void insertOne(Long codRecurso, String descProyecto, String descActividades, Date fecInicioParticipacion, Date fecFinParticipacion, String descCliente) {
		recursoTrayectoriaProyectoDao.insertOne(codRecurso, descProyecto, descActividades, fecInicioParticipacion, fecFinParticipacion, descCliente);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Long> findCodTrayectoriaProyectoByDescProyecto(String descProyecto) {
		return recursoTrayectoriaProyectoDao.findCodTrayectoriaProyectoByDescProyecto(descProyecto);
	}

}
