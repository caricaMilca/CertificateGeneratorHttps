package com.example.keyStore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;



import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import com.example.data.IssuerData;
import com.example.data.SubjectData;
import com.example.data.WithdrawCert;

public class KeyStoreReader {
	// KeyStore je Java klasa za citanje specijalizovanih datoteka koje se
	// koriste za cuvanje kljuceva
	// Tri tipa entiteta koji se obicno nalaze u ovakvim datotekama su:
	// - Sertifikati koji ukljucuju javni kljuc
	// - Privatni kljucevi
	// - Tajni kljucevi, koji se koriste u simetricnima siframa
	private KeyStore keyStore;

	public KeyStoreReader() {
		try {
			keyStore = KeyStore.getInstance("JKS", "SUN");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Zadatak ove funkcije jeste da ucita podatke o izdavaocu i odgovarajuci
	 * privatni kljuc. Ovi podaci se mogu iskoristiti da se novi sertifikati
	 * izdaju.
	 * 
	 * @param keyStoreFile
	 *            - datoteka odakle se citaju podaci
	 * @param alias
	 *            - alias putem kog se identifikuje sertifikat izdavaoca
	 * @param password
	 *            - lozinka koja je neophodna da se otvori key store
	 * @param keyPass
	 *            - lozinka koja je neophodna da se izvuce privatni kljuc
	 * @return - podatke o izdavaocu i odgovarajuci privatni kljuc
	 */
	public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {

		try {

			// Datoteka se ucitava
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			keyStore.load(in, password);
			// Iscitava se sertifikat koji ima dati alias
			Certificate cert = keyStore.getCertificate(alias);
			// Iscitava se privatni kljuc vezan za javni kljuc koji se nalazi na
			// sertifikatu sa datim aliasom
			PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass);

			X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();
			return new IssuerData(privKey, issuerName);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ucitava sertifikat is KS fajla
	 */
	public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if (ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Ucitava privatni kljuc is KS fajla
	 */
	public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(keyStoreFile));
			ks.load(in, keyStorePass.toCharArray());

			if (ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, pass.toCharArray());
				return pk;
			}
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> getValidCertificates(String in, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		ArrayList<String> alijasi = new ArrayList<>();
		keyStore.load(new FileInputStream(in), password.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
		while (aliases.hasMoreElements()) {
			String s = aliases.nextElement();

			X509Certificate temp = (X509Certificate) keyStore.getCertificate(s);
			if (!temp.getNotAfter().before(new Date())) {

				if (temp.getBasicConstraints() != -1) {

					alijasi.add(s);

				}
			}
		}

		return alijasi;
	}

	
		

	
	
	public PKCS10CertificationRequest convertPemToPKCS10CertificationRequest(String pem) {
	       // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        PKCS10CertificationRequest csr = null;
	        ByteArrayInputStream pemStream = null;
	        try {
	            pemStream = new ByteArrayInputStream(pem.getBytes("UTF-8"));
	            System.out.println("pemStream: "+pemStream);
	        } catch (UnsupportedEncodingException ex) {
	          //  LOG.error("UnsupportedEncodingException, convertPemToPublicKey", ex);
	        }

	        Reader pemReader = new BufferedReader(new InputStreamReader(pemStream));
	        System.out.println("PemReader: " + pemReader);
	        PEMParser pemParser = new PEMParser(pemReader);
            System.out.println("pemParser: "+pemParser);
	        try {
	            Object parsedObj = pemParser.readObject();
               
	            System.out.println("PemParser returned: " + parsedObj);

	            if (parsedObj instanceof PKCS10CertificationRequest) {
	                csr = (PKCS10CertificationRequest) parsedObj;

	            }
	        } catch (IOException ex) {
	           // LOG.error("IOException, convertPemToPublicKey", ex);
	        }

	        return csr;
	    }
	
	public KeyPair generateKeyPair() {
        try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA"); 
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(2048, random);
			return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	public ArrayList<Certificate> deleteCert(String serialNumber, boolean delete, String in, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
			IOException {
		ArrayList<Certificate> cert = new ArrayList<Certificate>();
		keyStore.load(new FileInputStream(in), password.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
		Enumeration<String> aliasesed = keyStore.aliases();

		while (aliases.hasMoreElements()) {
			String s = aliases.nextElement();
			Certificate certi = keyStore.getCertificate(s);

			X509Certificate x = (X509Certificate) certi;

			if (((X509Certificate) certi).getSerialNumber().equals(new BigInteger(serialNumber))) {
				if (delete) {

					if (x.getBasicConstraints() == -1) {

						keyStore.deleteEntry(s);
						keyStore.store(new FileOutputStream(in), password.toCharArray());
						cert.add(certi);

					} else {

						ArrayList<Certificate> chain = new ArrayList<>(Arrays.asList(keyStore.getCertificateChain(s)));
						Enumeration<String> aliaseses = keyStore.aliases();
						while (aliaseses.hasMoreElements()) {
							String tempA = aliaseses.nextElement();
							ArrayList<Certificate> tempChain = new ArrayList<>(
									Arrays.asList(keyStore.getCertificateChain(tempA)));
							if (tempChain.containsAll(chain)) {

								keyStore.deleteEntry(tempA);

								for (int n = 0; n < tempChain.size(); n++) {
									cert.add(tempChain.get(n));
								}

								while (aliasesed.hasMoreElements()) {

									String d = aliasesed.nextElement();
									Certificate certid = keyStore.getCertificate(d);
									X509Certificate xd = (X509Certificate) certid;
									X509Certificate xs = (X509Certificate) tempChain.get(0);

									if (xd.getIssuerDN().equals(xs.getSubjectDN())) {
										cert.add(certid);
										keyStore.deleteEntry(d);
									}
								}

							}

						}
						keyStore.store(new FileOutputStream(in), password.toCharArray());
					}
					return cert;
				} else
					return cert;
			}
		}

		return null;
	}

	public String getCertificateStatus(String serialNumber, String in, String password) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		keyStore.load(new FileInputStream(in), password.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
		while (aliases.hasMoreElements()) {
			String s = aliases.nextElement();
			Certificate certi = keyStore.getCertificate(s);

			X509Certificate x;

			if (((X509Certificate) certi).getSerialNumber().equals(new BigInteger(serialNumber))) {

				x = (X509Certificate) certi;
				if (!x.getNotAfter().before(new Date())) {
					String status = "Validan";
					return status;
				} else {
					String status = "Nije Validan";
					return status;
				}

			}

		}
		return null;
	}

	public String getAlias(Certificate c) throws KeyStoreException{
		return keyStore.getCertificateAlias(c);
	}
	
	public Certificate getOrDeleteCertificateBySerialNumber(String serialNumber, boolean delete, String in,
			String password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException {

		keyStore.load(new FileInputStream(in), password.toCharArray());
		Enumeration<String> aliases = keyStore.aliases();
        
		while (aliases.hasMoreElements()) {
			String s = aliases.nextElement();
			Certificate certi = keyStore.getCertificate(s);

			X509Certificate x = (X509Certificate) certi;

			if (((X509Certificate) certi).getSerialNumber().equals(new BigInteger(serialNumber))) {
				if (delete) {

					if (x.getBasicConstraints() == -1) {

						keyStore.deleteEntry(s);
						keyStore.store(new FileOutputStream(in), password.toCharArray());

					} else {

						ArrayList<Certificate> chain = new ArrayList<>(Arrays.asList(keyStore.getCertificateChain(s)));
						Enumeration<String> aliaseses = keyStore.aliases();
						while (aliaseses.hasMoreElements()) {
							String tempA = aliaseses.nextElement();
							ArrayList<Certificate> tempChain = new ArrayList<>(
									Arrays.asList(keyStore.getCertificateChain(tempA)));
							if (tempChain.containsAll(chain)) {

								keyStore.deleteEntry(tempA);

								X509Certificate c = (X509Certificate) tempChain.get(0);
								WithdrawCert w = new WithdrawCert(c.getSerialNumber().toString());

								X509Certificate n = (X509Certificate) tempChain.get(1);
								WithdrawCert we = new WithdrawCert(n.getSerialNumber().toString());

							}

						}
						keyStore.store(new FileOutputStream(in), password.toCharArray());
					}
				} else
					return certi;
			}
		}

		return null;
	}
}
