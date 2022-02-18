package com.sophi.app.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sophi.app.models.dao.IRegistroWebinarDao;
import com.sophi.app.models.entity.RegistroWebinar;

@Service
public class RegistroWebinarServiceImpl implements IRegistroWebinarService{
	
	@Autowired
	private IRegistroWebinarDao registroWebinarDao;

	@Override
	@Transactional
	public void guardar(RegistroWebinar registroWebinar) {
		registroWebinarDao.save(registroWebinar);
	}
 
}
