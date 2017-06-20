package com.example.servis;

import com.example.modeli.Klijent;

public interface KlijentServis {

	Klijent registracijaKlijenta(Klijent k);
	
	Klijent registracijaKlijentaF(Klijent k);

	Klijent preuzmiKlijenta(Long id);

}
