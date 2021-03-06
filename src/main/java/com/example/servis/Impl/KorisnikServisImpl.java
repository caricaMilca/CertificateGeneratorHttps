package com.example.servis.Impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.modeli.Korisnik;
import com.example.repository.KorisnikRepozitorijum;
import com.example.servis.KorisnikServis;



@Service
@Transactional
public class KorisnikServisImpl implements KorisnikServis {

	@Autowired
	KorisnikRepozitorijum korisnikRepozitorijum;
	
	@Autowired
	HttpSession sesija;
	
	@Override
	public Korisnik logovanje(String korisnickoIme, String lozinka) {
		return korisnikRepozitorijum.findByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
	}

	@Override
	public void promenaLozinke(String l) {
		// TODO Auto-generated method stub
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		k.lozinka = l;
		korisnikRepozitorijum.save(k);
	}

	@Override
	public Korisnik save(Korisnik k) {
		// TODO Auto-generated method stub
		return korisnikRepozitorijum.save(k);
	}

	@Override
	public Korisnik preuzmiKorisnika(Long id) {
		// TODO Auto-generated method stub
		return korisnikRepozitorijum.findOne(id);
	}


	

}
