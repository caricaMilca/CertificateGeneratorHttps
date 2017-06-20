package com.example.servis.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.data.CertificateCSR;
import com.example.repository.CertificateCSRepozitorijum;
import com.example.servis.CertificateCSRServis;

@Service
@Transactional
public class CertificateCSRServisImpl implements CertificateCSRServis {

	
	@Autowired
	CertificateCSRepozitorijum ccrsr;
	
	@Override
	public CertificateCSR save(CertificateCSR c) {
		// TODO Auto-generated method stub
		return ccrsr.save(c);
	}

}
