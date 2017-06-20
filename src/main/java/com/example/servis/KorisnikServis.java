package com.example.servis;

import com.example.modeli.Korisnik;

public interface KorisnikServis {

	Korisnik logovanje(String korisnickoIme, String lozinka);

	Korisnik save(Korisnik k);
	
	void promenaLozinke(String l);
	
	Korisnik preuzmiKorisnika(Long id);
}
