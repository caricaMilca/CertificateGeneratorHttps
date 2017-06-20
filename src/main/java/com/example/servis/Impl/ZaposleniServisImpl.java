package com.example.servis.Impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.modeli.UlogaKorisnika;
import com.example.modeli.UlogaZaposlenog;
import com.example.modeli.Zaposleni;
import com.example.repository.ZaposleniRepozitorijum;
import com.example.servis.ZaposleniServis;


@Service
@Transactional
public class ZaposleniServisImpl implements ZaposleniServis {

	@Autowired
	ZaposleniRepozitorijum zaposleniRepozitorijum;
	
	@Autowired
	HttpSession sesija;
	
	@Override
	public Zaposleni registracijaSalteruse(Zaposleni z) {
		z.uloga = UlogaKorisnika.Zaposleni;
		//System.out.println(z.ulogaZ);
		z.ulogaZ = UlogaZaposlenog.Salterusa;
		return  zaposleniRepozitorijum.save(z);
	}

	@Override
	public Zaposleni preuzmiZaposlenog(Long id) {
		// TODO Auto-generated method stub
		return zaposleniRepozitorijum.findOne(id);
	}

}
