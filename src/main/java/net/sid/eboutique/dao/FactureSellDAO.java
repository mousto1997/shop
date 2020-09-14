package net.sid.eboutique.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.FactureSell;

public interface FactureSellDAO extends JpaRepository<FactureSell, Integer> {
	
	@Query("select b from FactureSell b where b.vente.idSell = :x")
	FactureSell billByVente(@Param("x")int idSell);
	
}
