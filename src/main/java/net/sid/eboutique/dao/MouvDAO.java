package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Mouvement;

public interface MouvDAO extends JpaRepository<Mouvement, Integer> {
	@Query("select m from Mouvement m where m.commande = :x")
	public List<Mouvement> mouvByComm(@Param("x")String order);
	
	@Query("select m from Mouvement m where m.product = :x")
	public Mouvement mouvByProd(@Param("x")String prod);
}
