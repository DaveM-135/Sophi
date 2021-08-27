package com.sophi.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IDetalleProyectoInfraestructuraDao;
import com.sophi.app.models.entity.DetalleProyectoInfraestructura;

@Service
public class DetalleProyectoInfraestructuraServiceImpl implements IDetalleProyectoInfraestructuraService {
	
	@Autowired
	private IDetalleProyectoInfraestructuraDao detalleProyectoInfraestructuraDao;

	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoInfraestructura> findAll() {
		return (List<DetalleProyectoInfraestructura>) detalleProyectoInfraestructuraDao.findAll();
	}

	@Override
	public void save(DetalleProyectoInfraestructura detalleProyectoInfraestructura) {
		detalleProyectoInfraestructuraDao.save(detalleProyectoInfraestructura);
	}
	
	@Override
	public void borrarByCodProyecto(Long codProyecto) {
		detalleProyectoInfraestructuraDao.borrarByCodProyecto(codProyecto);
	}
	
	@Override
	public void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto, Long codEstatusProyecto) {
		detalleProyectoInfraestructuraDao.borrarByCodProyectoAndCodEstatusProyecto(codProyecto, codEstatusProyecto);
	}
	
	@Override
	public void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto, Long codEstatusProyecto, Long codCliente) {
		detalleProyectoInfraestructuraDao.borrarByCodProyectoAndCodEstatusProyectoAndCodCliente( codProyecto, codEstatusProyecto, codCliente);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyecto(Long codProyecto) {
		return (List<DetalleProyectoInfraestructura>) detalleProyectoInfraestructuraDao.findByDetalleProyectoInfraestructuraIdCodProyecto(codProyecto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoInfraestructura> findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente) {
		return (List<DetalleProyectoInfraestructura>) detalleProyectoInfraestructuraDao.findByDetalleProyectoInfraestructuraIdCodProyectoAndDetalleProyectoInfraestructuraIdCodEstatusProyectoAndDetalleProyectoInfraestructuraIdCodCliente(codProyecto, codEstatusProyecto, codCliente);
	}
	
	@Override
	public void actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(Long codProyecto, Long codEstatusProyecto) {
		detalleProyectoInfraestructuraDao.actualizaEstatusProyectoDetalleProyectoInfraestructuraByCodProyecto(codProyecto, codEstatusProyecto);
	}

}
