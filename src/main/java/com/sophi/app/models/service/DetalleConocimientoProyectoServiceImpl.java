package com.sophi.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IDetalleConocimientoProyectosDao;
import com.sophi.app.models.entity.DetalleConocimientosProyecto;

@Service
public class DetalleConocimientoProyectoServiceImpl implements IDetalleConocimientoProyectoService{
	
	@Autowired
	private IDetalleConocimientoProyectosDao detalleConocimientoProyectoDao;

	@Override
	@Transactional(readOnly = true)
	public List<DetalleConocimientosProyecto> findByCodTrayectoriaProyecto(Long codTrayectoriaProyecto) {
		return detalleConocimientoProyectoDao.findByRecursoTrayectoriaProyectoCodTrayectoriaProyecto(codTrayectoriaProyecto);
	}

	@Override
	@Transactional
	public void save(DetalleConocimientosProyecto detalleConocimientosProyecto) {
		detalleConocimientoProyectoDao.save(detalleConocimientosProyecto);
	}

	@Override
	@Transactional
	public void delete(Long codDetalleConocimiento) {
		detalleConocimientoProyectoDao.deleteById(codDetalleConocimiento);
	}

	@Override
	@Transactional(readOnly = true)
	public DetalleConocimientosProyecto findById(Long codDetalleConocimiento) {
		return detalleConocimientoProyectoDao.findById(codDetalleConocimiento).orElse(null);
	}

	@Override
	@Transactional
	public void saveAll(List<DetalleConocimientosProyecto> ListDetalleConocimientosProyecto) {
		detalleConocimientoProyectoDao.saveAll(ListDetalleConocimientosProyecto);
	}

	@Override
	@Transactional
	public void borrarByCodTrayectoriaProyecto(Long codTrayectoriaProyecto) {
		detalleConocimientoProyectoDao.borrarByCodTrayectoriaProyecto(codTrayectoriaProyecto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Long> conocimientosDistintosPorRecurso(Long codRecurso) {
		return detalleConocimientoProyectoDao.findConocimientosDistintosPorRecurso(codRecurso);
	}
	
	@Override
	@Transactional
	public void insertOneDCP(Long codTrayectoriaProyecto, Long codConocimiento) {
		detalleConocimientoProyectoDao.insertOneDCP(codTrayectoriaProyecto, codConocimiento);
	}

}
