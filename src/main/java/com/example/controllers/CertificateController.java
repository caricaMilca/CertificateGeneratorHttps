package com.example.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.actions.CertificateGenerator;
import com.example.actions.CertificateInfoGenerator;
import com.example.data.CertificateCSR;
import com.example.data.CertificateInfo;
import com.example.data.CsrRequest;
import com.example.data.IssuerData;
import com.example.data.SubjectData;
import com.example.data.WithdrawCert;
import com.example.keyStore.KeyStoreReader;
import com.example.keyStore.KeyStoreWriter;
import com.example.servis.CertificateCSRServis;
import com.example.servis.WithdrawCertServis;

@Controller
@RequestMapping("/certificate")
public class CertificateController {

	private KeyStore keyStore;

	@Autowired
	public WithdrawCertServis ws;
	@Autowired
    public CertificateCSRServis ccsrs;
	
	private static String UPLOADED_FOLDER = "C:/Users/Stefan/Desktop/proba";
	public CertificateInfoGenerator cig = new CertificateInfoGenerator();
	public CertificateGenerator cg = new CertificateGenerator();
	public KeyStoreWriter ksw = new KeyStoreWriter();
	public KeyStoreReader ksr = new KeyStoreReader();

	@RequestMapping(value = "/getValidCertificates", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<String> getValidCertificates() throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, IOException {

		return ksr.getValidCertificates("keystore.jks", "password");

	}

	@RequestMapping(value = "/statusCertificate/{serial}", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<String> statusCertificate(@PathVariable("serial") String serialNumber) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		String sta = ksr.getCertificateStatus(serialNumber, "keystore.jks", "password");
		if (sta == null) {
			WithdrawCert w = ws.findbySerial(serialNumber);
			if (w == null) {
				sta = "Certifikat ne postoji!";
			} else {
				sta = "povucen";
			}
		}
		ArrayList<String> sp = new ArrayList<String>();
		sp.add(sta);
		return sp;

	}

	@RequestMapping(value = "/getCertificate/{serial}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Certificate> getCertificate(@PathVariable("serial") String serialNumber) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		Certificate c = ksr.getOrDeleteCertificateBySerialNumber(serialNumber, false, "keystore.jks", "password");

		String alias = ksr.getAlias(c);
		
		if(c == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		X509Certificate  xcert = (X509Certificate)c;
		
		
		 
		 ksw.save(xcert, "./exported/"+alias);
		  File file = new File("./exported/"+alias+".cer");
		  
		  
		  Path path = Paths.get("./exported/"+alias+".cer");
		  byte[] data = Files.readAllBytes(path);
		  
		   
		         Path putanja = Paths.get("C:/Users/Stefan/Desktop/proba" +alias+".cer");
		         Files.write(putanja, data);
		
		
		/*
		byte[] bytes = c.getEncoded();
		Path path = Paths.get(UPLOADED_FOLDER + c.getType());
		Files.write(path, bytes);
*/
		return new ResponseEntity<>(HttpStatus.OK);
		

	}

	@RequestMapping(value = "/deleteCertificate/{serial}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Certificate> deleteCertificate(@PathVariable("serial") String serialNumber) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

		ArrayList<Certificate> ce = ksr.deleteCert(serialNumber, true, "keystore.jks", "password");

		if(ce == null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		for (int i = 0; i < ce.size(); i++) {
			X509Certificate c = (X509Certificate) ce.get(i);
			WithdrawCert w = new WithdrawCert(c.getSerialNumber().toString());
			ws.save(w);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	
	}

	@RequestMapping(value = "/generateCertificate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> generateCertificate(@RequestBody CertificateInfo info) throws NoSuchAlgorithmException,
			NoSuchProviderException, OperatorCreationException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		ksw.loadKeyStore("keystore.jks", "password".toCharArray());
		System.out.println(info.certificationAuthority);
		
		KeyPair keyPairIssuer = cig.generateKeyPair(info.keyAlgorithm, info.keySize);

		SubjectData subjectData = cig.generateSubjectData(info);
		X509Certificate cer;
		if (info.root) {
			info.issuerName = info.commonName;
			IssuerData issuerData = cig.generateIssuerData(keyPairIssuer.getPrivate(), info);
			cer = cg.generateCertificate(subjectData, issuerData, info.root, info.issuerName, "keystore.jks",
					info.certificationAuthority);
		} else {
			IssuerData id = ksr.readIssuerFromStore("keystore.jks", info.issuerName, "password".toCharArray(),
					"password".toCharArray());

			cer = cg.generateCertificate(subjectData, id, info.root, info.issuerName, "keystore.jks",
					info.certificationAuthority);
		}

		System.out.println("Prilikom upisa: " + cer.getSerialNumber());

		ksw.write(info.commonName, keyPairIssuer.getPrivate(), "password".toCharArray(), cer);
		ksw.saveKeyStore("keystore.jks", "password".toCharArray());
		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@RequestMapping(value= "/generateCRSCertificate",method = RequestMethod.POST)
	public ResponseEntity<?> certificateSigningRequest(@RequestBody CsrRequest info) throws OperatorCreationException, InvalidKeyException, NoSuchAlgorithmException, IOException, CertificateEncodingException, KeyStoreException{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		System.out.println(info.csr);
		
		String signer = info.issuerName;
		String csrString = info.csr;
		//Long days = Long.parseLong(requestInfo.getDays());
	
		 PKCS10CertificationRequest csr = ksr.convertPemToPKCS10CertificationRequest(csrString);
		 //rand
		 System.out.println(csr);
		 X500Name x500NameSubject = csr.getSubject();
		 
		 JcaPKCS10CertificationRequest jcaCertRequest =
			       new JcaPKCS10CertificationRequest(csr.getEncoded()).setProvider("BC");
		// System.out.println("x500Name is: " + x500Name + "\n");
		// KeyStoreReader ksr = new KeyStoreReader();
		 
			//vadimo iz keystora certifikat koji ce biti issuer i potpisivac end certifikatu
			//TODO: Sifra keystora treba se unositi rucno
			Certificate issuer = ksr.readCertificate("keystore.jks","password", info.issuerName);
			
			X509Certificate xcert = (X509Certificate)issuer;
			
			//javni kljuc iz pem-a
		    PublicKey subjectPublicKey = jcaCertRequest.getPublicKey();

			X500Name x500Issuer = new JcaX509CertificateHolder(xcert).getSubject();
			
			//Javni kljuc izdavaoca
			PublicKey authorityPublicKey = xcert.getPublicKey();
			
		
			KeyPair keyPairIssuer = ksr.generateKeyPair();
			//TODO: DA LI SE GENERISE NOVI ILI SE KORISTI STARI 
			IssuerData issuerData=new IssuerData(keyPairIssuer.getPrivate(), x500Issuer);
		     System.out.println("IDID: "+ issuerData);
			IssuerData issuerDatad = ksr.readIssuerFromStore("keystore.jks", info.issuerName, "password".toCharArray(),
					"password".toCharArray());
			System.out.println("ID: " + issuerDatad);
			//Serijski broj sertifikata
			String sn= Long.toString(System.currentTimeMillis());
			SubjectData sdata = new SubjectData();
			sdata.setPublicKey(subjectPublicKey);
			sdata.setX500name(x500NameSubject);
			sdata.setSerialNumber(sn);
			sdata.setStartDate(new Date(System.currentTimeMillis()));
		
			
			int days = 20;
			sdata.setEndDate(new Date(System.currentTimeMillis()+days  *86400000));
			
			CertificateGenerator cg = new CertificateGenerator();
			//za kreiranje izgenerisani novi kljucevi i prosledjen privatni kljuc izdavaoca radi potpisa
			
			
		
			//PrivateKey issuerPrivate = ksr.readPrivateKey("keystore.jks", "password", signer, "private");
            PrivateKey issuerPrivate = issuerData.getPrivateKey(); 
			X509Certificate cert = cg.generateEndCertificate(sdata, issuerData, issuerPrivate, subjectPublicKey);
		
			
			
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			//SIFRU UNOSITI
			keyStoreWriter.loadKeyStore("keystore.jks", "password".toCharArray());
		
			String certpassword  = "password";
			
			keyStoreWriter.write(info.keyAlias, issuerPrivate, certpassword.toCharArray(), cert);
			String keystorepassword = "password";
			keyStoreWriter.saveKeyStore("keystore.jks","password".toCharArray() );
			
			CertificateCSR cer =null;
			
			
		
			 cer = new CertificateCSR(Long.parseLong(sn), info.keyAlias,true);
			System.out.println(cer);
			ccsrs.save(cer);
			
		    return  new ResponseEntity<> (HttpStatus.OK);
		    
           
	}
}
