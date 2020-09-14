package net.sid.eboutique.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.sid.eboutique.entities.Vente;

public interface VenteDAO extends JpaRepository<Vente, Integer>{
	
	@Query("select v from Vente v where v.codeSell = null")
	public Vente incompleteSell();
}
