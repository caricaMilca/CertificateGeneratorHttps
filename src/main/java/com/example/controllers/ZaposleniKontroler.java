package com.example.controllers;

import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.modeli.Klijent;
import com.example.modeli.Korisnik;
import com.example.modeli.Privilege;
import com.example.modeli.Role;
import com.example.modeli.Zaposleni;
import com.example.servis.KlijentServis;
import com.example.servis.KorisnikServis;
import com.example.servis.ZaposleniServis;



@RestController
@RequestMapping("/zaposleni")
public class ZaposleniKontroler {

	@Autowired
	private KlijentServis klijentServis;
	@Autowired
	private ZaposleniServis zaposleniServis;
	@Autowired
	KorisnikServis korisnikServis;
	@Autowired
	HttpSession sesija;

	@PostMapping(path = "/registracijaKlijentaPravno")
	public ResponseEntity<?> registracijaKlijentaPravno(@Valid @RequestBody Klijent klijent) {
		if (authorize("registracijaKlijentaPravno") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		klijentServis.registracijaKlijenta(klijent);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping(path = "/registracijaKlijentaFizicko")
	public ResponseEntity<?> registracijaKlijentaFizicko(@Valid @RequestBody Klijent klijent) {
		if (authorize("registracijaKlijentaFizicko") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		klijentServis.registracijaKlijenta(klijent);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping(path = "/registracijaSalteruse")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> registracijaSalteruse(@Valid @RequestBody Zaposleni z) {
		if (authorize("registracijaSalteruse") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		
		zaposleniServis.registracijaSalteruse(z);
		return new ResponseEntity<>(HttpStatus.OK);

	}
	
	@GetMapping(path = "/preuzmiZaposlenog")
	public ResponseEntity<Zaposleni> preuzmiZaposlenog() {
		if(authorize("preuzmiZaposlenog") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		return new ResponseEntity<Zaposleni>(zaposleniServis.preuzmiZaposlenog(k.id), HttpStatus.OK);
	}

	public String authorize(String imeOperacije) {

		if ((Korisnik) sesija.getAttribute("korisnik") == null)
			return "Ne";
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		Iterator<Role> iterator = k.roles.iterator();

		while (iterator.hasNext()) {
			Iterator<Privilege> it = iterator.next().privileges.iterator();
			while (it.hasNext())
				if (it.next().name.equals(imeOperacije))
					return "Da";
		}
		return "Ne";
	}
}
