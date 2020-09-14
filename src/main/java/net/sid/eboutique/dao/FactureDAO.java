package net.sid.eboutique.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Facture;

public interface FactureDAO extends JpaRepository<Facture, Integer> {
	@Query("select b from Facture b where b.codeFac like :x or b.commande.codeCom like :x or b.commande.client.nom like :x order by idFacture desc")
	Page<Facture> factures(@Param("x")String kw, Pageable page);

	@Query("select b from Facture b where b.commande.idCom = :x and b.status = false")
	Facture billnopayed(@Param("x")int idCom);
	
	@Query("select b from Facture b where b.commande.idCom = :x")
	Facture billByCom(@Param("x")int idCom);
}
