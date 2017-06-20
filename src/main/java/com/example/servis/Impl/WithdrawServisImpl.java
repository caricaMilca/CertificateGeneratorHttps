package com.example.servis.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.data.WithdrawCert;
import com.example.repository.WithdrawCertRepository;
import com.example.servis.WithdrawCertServis;

@Service
@Transactional
public class WithdrawServisImpl implements WithdrawCertServis{

	@Autowired
	WithdrawCertRepository wdcr;
	
	@Override
	public WithdrawCert save(WithdrawCert w) {
		// TODO Auto-generated method stub
		return wdcr.save(w);
	}

	@Override
	public WithdrawCert findbySerial(String serialNumber) {
		// TODO Auto-generated method stub
		return wdcr.findBySerialNumber(serialNumber);
	}

}
