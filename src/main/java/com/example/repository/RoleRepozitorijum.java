package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.modeli.Role;



public interface RoleRepozitorijum extends JpaRepository<Role, Long> {

	public Role findById(Long id);
}
