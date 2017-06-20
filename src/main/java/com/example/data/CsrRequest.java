package com.example.data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CsrRequest implements Serializable {

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String csr;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date validFrom;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date validTo;
	public String keyAlias;
	public String issuerName;
	public String email;
	
	public CsrRequest(){}



	public Date getValidFrom() {
		return validFrom;
	}



	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}



	public Date getValidTo() {
		return validTo;
	}



	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}



	public String getKeyAlias() {
		return keyAlias;
	}



	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}



	public String getIssuerName() {
		return issuerName;
	}



	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getCsr() {
		return csr;
	}

	public void setCsr(String csr) {
		this.csr = csr;
	}

	
}
