package com.example.controllers;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.modeli.Klijent;
import com.example.modeli.Korisnik;
import com.example.modeli.Privilege;
import com.example.modeli.Role;
import com.example.servis.KlijentServis;



@RestController
@RequestMapping("/klijent")
public class KlijentKontroler {

	@Autowired
	HttpSession sesija;
	
	@Autowired
	KlijentServis klijentServis;
	
	@GetMapping(path = "/preuzmiKlijenta")
	public ResponseEntity<Klijent> preuzmiKlijenta() {
		if(authorize("preuzmiKlijenta") == "Ne")
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		return new ResponseEntity<Klijent>(klijentServis.preuzmiKlijenta(k.id), HttpStatus.OK);
	}
	
	public String authorize(String imeOperacije){
	    if((Korisnik) sesija.getAttribute("korisnik") == null) 
			return "Ne";
	    Korisnik k = (Korisnik) sesija.getAttribute("korisnik");
		 Iterator<Role> iterator = k.roles.iterator();

	        while (iterator.hasNext()) {
	        Iterator<Privilege> it = iterator.next().privileges.iterator();
	        while(it.hasNext())
	        	if(it.next().name.equals(imeOperacije))
	        		return "Da";
	        }
	        return "Ne";
	} 
	
}
