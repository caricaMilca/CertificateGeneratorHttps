package com.example.servis;

import com.example.data.WithdrawCert;

public interface WithdrawCertServis {

	WithdrawCert save(WithdrawCert w);
	
	WithdrawCert findbySerial(String serialNumber);
}
