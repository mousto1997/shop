package net.sid.eboutique.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.sid.eboutique.entities.Activity;

public interface ActivityDAO extends JpaRepository<Activity, Integer>{
	
}
