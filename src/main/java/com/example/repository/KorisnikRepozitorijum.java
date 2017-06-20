package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.modeli.Korisnik;



public interface KorisnikRepozitorijum extends JpaRepository<Korisnik, Long> {

	
	Korisnik findByKorisnickoImeAndLozinka(String korisnickoIme, String lozinka);

}
