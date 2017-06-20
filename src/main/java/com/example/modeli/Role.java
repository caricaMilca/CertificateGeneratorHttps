package com.example.modeli;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Role {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
 
    public String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<Korisnik> users;
 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    public Collection<Privilege> privileges;

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
	}   
    
    
    
}