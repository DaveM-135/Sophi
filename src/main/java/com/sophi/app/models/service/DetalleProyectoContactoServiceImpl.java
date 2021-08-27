package com.sophi.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IDetalleProyectoContactoDao;
import com.sophi.app.models.entity.DetalleProyectoContacto;
import com.sophi.app.models.entity.DetalleProyectoContactoId;

@Service
public class DetalleProyectoContactoServiceImpl implements IDetalleProyectoContactoService {
	
	@Autowired
	private IDetalleProyectoContactoDao detalleProyectoContactoDao;

	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoContacto> findAll() {
		return (List<DetalleProyectoContacto>) detalleProyectoContactoDao.findAll();
	}

	@Override
	public void save(DetalleProyectoContacto detalleProyectoInfraestructura) {
		detalleProyectoContactoDao.save(detalleProyectoInfraestructura);
	}
	
	@Override
	public void borrarByCodProyecto(Long codProyecto) {
		detalleProyectoContactoDao.borrarByCodProyecto(codProyecto);
	}
	
	@Override
	public DetalleProyectoContacto findByDetalleProyectoContactoId(DetalleProyectoContactoId detalleProyectoContactoId) {
		return (DetalleProyectoContacto)detalleProyectoContactoDao.findByDetalleProyectoContactoId(detalleProyectoContactoId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyecto(Long codProyecto) {
		return (List<DetalleProyectoContacto>) detalleProyectoContactoDao.findByDetalleProyectoContactoIdCodProyecto(codProyecto);
	}
	
	@Override
	public void borrarByCodProyectoAndCodEstatusProyecto(Long codProyecto,Long codEstatusProyecto) {
		detalleProyectoContactoDao.borrarByCodProyectoAndCodEstatusProyecto(codProyecto,codEstatusProyecto);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalleProyectoContacto> findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(Long codProyecto,Long codEstatusProyecto,Long codCliente) {
		return (List<DetalleProyectoContacto>) detalleProyectoContactoDao.findByDetalleProyectoContactoIdCodProyectoAndDetalleProyectoContactoIdCodEstatusProyectoAndDetalleProyectoContactoIdCodCliente(codProyecto, codEstatusProyecto, codCliente);
	}
	
	@Override
	public void borrarByCodProyectoAndCodEstatusProyectoAndCodCliente(Long codProyecto,Long codEstatusProyecto, Long codCliente) {
		detalleProyectoContactoDao.borrarByCodProyectoAndCodEstatusProyectoAndCodCliente( codProyecto, codEstatusProyecto, codCliente);
	}

	@Override
	@Transactional(readOnly = true)
	public Long findTotalProyectosResponsable(Long codContacto) {
		return detalleProyectoContactoDao.findTotalProyectosResponsable(codContacto);
	}
	
	@Override
	public void actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(Long codProyecto, Long codEstatusProyecto) {
		detalleProyectoContactoDao.actualizaEstatusProyectoDetalleProyectoContactoByCodProyecto(codProyecto, codEstatusProyecto);;
	}

}
