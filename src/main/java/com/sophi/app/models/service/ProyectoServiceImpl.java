package com.sophi.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IProyectoDao;
import com.sophi.app.models.entity.Proyecto;

@Service
public class ProyectoServiceImpl implements IProyectoService {

	@Autowired
	private IProyectoDao proyectoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findAll() {
		return (List<Proyecto>) proyectoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Proyecto proyecto) {
		proyectoDao.save(proyecto);
	}

	@Override
	@Transactional(readOnly = true)
	public Proyecto findOne(Long codProyecto) {
		return proyectoDao.findById(codProyecto).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Proyecto findByDescProyectoAndCodClienteAndCodEstatusProyecto(String descProyecto, Long codCliente, Long codEstatusProyecto) {//, Date fecRegistro) {
		return (Proyecto) proyectoDao.findByDescProyectoAndCodClienteAndCodEstatusProyecto(descProyecto, codCliente, codEstatusProyecto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findByCodCliente(Long codCliente) {
		return (List<Proyecto>) proyectoDao.findByCodCliente(codCliente);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Proyecto findByCodProyecto(Long codProyecto) {
		return proyectoDao.findByCodProyecto(codProyecto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Proyecto findByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto) {
		return proyectoDao.findByCodProyectoAndCodEstatusProyecto(codProyecto, codEstatusProyecto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Proyecto findByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente) {
		return proyectoDao.findByCodProyectoAndCodEstatusProyectoAndCodCliente(codProyecto, codEstatusProyecto, codCliente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findByCodRecursoLider(Long codRecursoLider) {
		return proyectoDao.findByCodRecursoLider(codRecursoLider);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findProyectosActivos() {
		return proyectoDao.findProyectosActivos();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findListaClientesRecursoAprobador(Long codRecursoAprobador) {
		return proyectoDao.findListaClientesRecursoAprobador(codRecursoAprobador);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> findListaClientesRecursoLider(Long codRecursoLider) {
		return proyectoDao.findListaClientesRecursoLider(codRecursoLider);
	}

	@Override
	public List<Proyecto> findListaProyectosRecursoAprobador(Long codRecursoAprobador, Long codCliente) {
		return proyectoDao.findListaProyectosRecursoAprobador(codRecursoAprobador, codCliente);
	}

	@Override
	public List<Proyecto> findListaProyectosRecursoLider(Long codRecursoLider, Long codCliente) {
		return proyectoDao.findListaProyectosRecursoLider(codRecursoLider, codCliente);
	}

	@Override
	public List<Proyecto> findListaProyectosRecursoAprobadorTodos(Long codRecursoAprobador) {
		return proyectoDao.findListaProyectosRecursoAprobadorTodos(codRecursoAprobador);
	}
	
	@Override
	public List<Proyecto> findListaProyectosRecursoAprobadorBKPTodos(Long codRecursoAprobadorBKP) {
		return proyectoDao.findListaProyectosRecursoAprobadorBKPTodos(codRecursoAprobadorBKP);
	}

	@Override
	public List<Proyecto> findListaProyectosRecursoLiderTodos(Long codRecursoLider) {
		return proyectoDao.findListaProyectosRecursoLiderTodos(codRecursoLider);
	}

	@Override
	public List<Proyecto> findListaProyectosPorCerrar(Date fecFinProyecto) {
		return proyectoDao.findListaProyectosPorCerrar(fecFinProyecto);
	}

}
