package com.example.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class WithdrawCert implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6390650937383741213L;

	@Id		
    @GeneratedValue
    public Long id;
	
	@Column
	String serialNumber;
	
	@Column
	String status;
	
	
	
	public WithdrawCert(String serialNumber) {
		super();
		this.serialNumber = serialNumber;
		this.status = "povucen";
	}

	public WithdrawCert() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	
	
}
