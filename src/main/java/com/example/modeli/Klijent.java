package com.example.modeli;

import javax.persistence.Entity;
import javax.validation.constraints.Size;


@Entity
public class Klijent extends Korisnik {

	@Size(max = 1)
	public String vrsta;

	public Klijent(String ime, String prezime, String korisnickoIme, String lozinka, String vrsta) {
		super(ime, prezime, korisnickoIme, lozinka);
		this.vrsta = vrsta;
	}

	public Klijent() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	
}
