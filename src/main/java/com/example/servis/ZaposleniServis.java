package com.example.servis;

import com.example.modeli.Zaposleni;

public interface ZaposleniServis {

	
	Zaposleni registracijaSalteruse(Zaposleni z);

	Zaposleni preuzmiZaposlenog(Long id);
	
}
