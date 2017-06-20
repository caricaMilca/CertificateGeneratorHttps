package com.example.servis.Impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.modeli.Klijent;
import com.example.modeli.Role;
import com.example.modeli.UlogaKorisnika;
import com.example.repository.KlijentRepozitorijumi;
import com.example.servis.KlijentServis;
import com.example.servis.RoleServis;



@Service
@Transactional
public class KlijentServisImpl implements KlijentServis {

	@Autowired
	KlijentRepozitorijumi klijentRepozitorijum;
	
	@Autowired
	RoleServis roleServis;

	@Override
	public Klijent registracijaKlijenta(Klijent k) {
		// TODO Auto-generated method stub
	    k.vrsta = "P";
		k.uloga = UlogaKorisnika.Klijent;
		k.roles = new ArrayList<Role>();
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		return klijentRepozitorijum.save(k);
	}

	@Override
	public Klijent registracijaKlijentaF(Klijent k) {
		// TODO Auto-generated method stub
		k.vrsta = "F";
		k.uloga = UlogaKorisnika.Klijent;
		k.roles = new ArrayList<Role>();
		k.roles.add(roleServis.findOne(Long.valueOf(1)));
		return klijentRepozitorijum.save(k);
	}

	@Override
	public Klijent preuzmiKlijenta(Long id) {
		return klijentRepozitorijum.findOne(id);
	}

}
