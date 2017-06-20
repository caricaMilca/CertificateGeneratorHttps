package com.example.servis.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.modeli.Role;
import com.example.repository.RoleRepozitorijum;
import com.example.servis.RoleServis;



@Service
@Transactional
public class RoleServisImpl implements RoleServis{

	@Autowired
	private RoleRepozitorijum roleRep;
	
	@Override
	public Role findOne(Long id) {
		// TODO Auto-generated method stub
		return roleRep.findById(id);
	}

}
