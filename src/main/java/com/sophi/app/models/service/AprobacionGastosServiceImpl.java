package com.sophi.app.models.service;

import java.util.List;

import com.sophi.app.models.dao.IAprobacionGastosDao;
import com.sophi.app.models.entity.AprobacionGastos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AprobacionGastosServiceImpl implements IAprobacionGastosService {

    @Autowired
    private IAprobacionGastosDao aprobaciongastosDao;

    @Override
	@Transactional(readOnly = true)
	public List<AprobacionGastos> findAll() {
		return (List<AprobacionGastos>) aprobaciongastosDao.findAll();
	}
    
}