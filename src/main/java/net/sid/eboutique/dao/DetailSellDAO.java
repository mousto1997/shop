package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.DetailSell;

public interface DetailSellDAO extends JpaRepository<DetailSell, Integer>{
	
	@Query("select c from DetailSell c where c.vente.idSell = :y")
	public List<DetailSell> detailSellBySell(@Param("y")int idSell);
	
	@Query("select c from DetailSell c where c.vente.idSell = :y and c.product.idProd = :x")
	public DetailSell detailSellBySellProd(@Param("y")int idSell, @Param("x") int idProd);
	
	@Query("select d from DetailSell d where d.product.idProd = :x")
	public DetailSell detailSellByProd(@Param("x")int idProd);
	
}
