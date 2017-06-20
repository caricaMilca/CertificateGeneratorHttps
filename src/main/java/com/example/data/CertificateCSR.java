package com.example.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CertificateCSR {

	
	
	@Id
	@GeneratedValue
	private Long id;
	

	
	@Column(nullable = true)
	private long serial;
		

	public CertificateCSR() {
		super();
	}



	public CertificateCSR( long serial, String alias, boolean ocspValid) {
		super();
		
		this.serial = serial;
		this.alias = alias;
		this.ocspValid = ocspValid;
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}



	public long getSerial() {
		return serial;
	}


	public void setSerial(long serial) {
		this.serial = serial;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public boolean isOcspValid() {
		return ocspValid;
	}


	public void setOcspValid(boolean ocspValid) {
		this.ocspValid = ocspValid;
	}


	@Column(nullable = true)
	private String alias;

	
	@Column(nullable = false)
	private boolean ocspValid;

}
