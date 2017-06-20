package com.example.actions;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import com.example.data.CertificateInfo;
import com.example.data.IssuerData;
import com.example.data.SubjectData;

public class CertificateInfoGenerator {

	public IssuerData generateIssuerData(PrivateKey issuerKey, CertificateInfo info) {
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, info.commonName);
		builder.addRDN(BCStyle.SURNAME, info.surname);
		builder.addRDN(BCStyle.GIVENNAME, info.givenName);
		builder.addRDN(BCStyle.O, info.organization);
		builder.addRDN(BCStyle.OU, info.organizationUnit);
		builder.addRDN(BCStyle.C, info.country);
		builder.addRDN(BCStyle.E, info.email);

		// Kreiraju se podaci za issuer-a, sto u ovom slucaju ukljucuje:
		// - privatni kljuc koji ce se koristiti da potpise sertifikat koji se
		// izdaje
		// - podatke o vlasniku sertifikata koji izdaje nov sertifikat
		return new IssuerData(issuerKey, builder.build());
	}

	public SubjectData generateSubjectData(CertificateInfo info) {
		KeyPair keyPairSubject = generateKeyPair(info.keyAlgorithm, info.keySize);

		// Serijski broj sertifikata
		String sn= Long.toString(System.currentTimeMillis());

	
		
		
		// klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke
		// o vlasniku
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, info.commonName);
		builder.addRDN(BCStyle.SURNAME, info.surname);
		builder.addRDN(BCStyle.GIVENNAME, info.givenName);
		builder.addRDN(BCStyle.O, info.organization);
		builder.addRDN(BCStyle.OU, info.organizationUnit);
		builder.addRDN(BCStyle.C, info.country);
		builder.addRDN(BCStyle.E, info.email);

		// Kreiraju se podaci za sertifikat, sto ukljucuje:
		// - javni kljuc koji se vezuje za sertifikat
		// - podatke o vlasniku
		// - serijski broj sertifikata
		// - od kada do kada vazi sertifikat
		return new SubjectData(keyPairSubject.getPublic(), builder.build(), sn, info.validFrom, info.validTo);
	}

	public KeyPair generateKeyPair(String keyAlgorithm, int keySize) {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(keySize, random);
			return keyGen.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
		return null;
	}
}
