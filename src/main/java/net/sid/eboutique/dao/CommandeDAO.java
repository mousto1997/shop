package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Commande;

public interface CommandeDAO extends JpaRepository<Commande, Integer>{
	@Query("select c from Commande c where c.client.idCli = :x and c.codeCom = null")
	public Commande incompleteOrder(@Param("x") int idCli);
	
	@Query("select c from Commande c where c.codeCom = null")
	public List<Commande> incompleteOrders();
	
	@Query("select c from Commande c where c.codeCom != null")
	public List<Commande> orders();
	
	@Query("select c from Commande c where c.codeCom = null")
	public List<Commande> incompletOrder();
}
