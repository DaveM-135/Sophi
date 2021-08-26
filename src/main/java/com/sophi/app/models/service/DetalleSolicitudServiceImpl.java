package com.sophi.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IDetalleSolicitudDao;
import com.sophi.app.models.entity.DetalleSolicitud;

@Service
public class DetalleSolicitudServiceImpl  implements IDetalleSolicitudService{
	
	@Autowired
	private IDetalleSolicitudDao detalleSolicitudDao;


	@Override
	@Transactional(readOnly = true)
	public DetalleSolicitud findById(Long codDetalleSolicitud) {
		return detalleSolicitudDao.findById(codDetalleSolicitud).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DetalleSolicitud> findByCodSolicitud(Long codSolicitud) {
		return detalleSolicitudDao.findByCodSolicitud(codSolicitud);
	}

	@Override
	public List<DetalleSolicitud> findAll() {
		return detalleSolicitudDao.findAll();
	}

}
