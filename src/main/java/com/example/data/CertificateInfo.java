package com.example.data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CertificateInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -199374326633790157L;
	public String keyAlgorithm;
	public int keySize;
	public String commonName;
	public String givenName;
	public String surname;
	public String organization;
	public String organizationUnit;
	public String country;
	public String email;
	public boolean root;
	public boolean certificationAuthority;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date validFrom;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	public Date validTo;
	public String keyAlias;
	public String issuerName;

	public CertificateInfo() {

	}
}
