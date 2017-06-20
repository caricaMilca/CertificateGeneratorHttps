package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.data.WithdrawCert;



public interface WithdrawCertRepository extends JpaRepository<WithdrawCert, Long> {

	
	WithdrawCert findBySerialNumber(String serial);
}
