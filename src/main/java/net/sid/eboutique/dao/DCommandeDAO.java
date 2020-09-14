package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.DetailCommande;

public interface DCommandeDAO extends JpaRepository<DetailCommande, Integer>{
	@Query("select c from DetailCommande c where c.product.idProd = :x and c.commande.idCom = :y")
	public DetailCommande existCommande(@Param("x")int idProd, @Param("y")int idCom);
	
	@Query("select c from DetailCommande c where c.commande.idCom = :y")
	public DetailCommande detailComByCom(@Param("y")int idCom);
	
	@Query("select c from DetailCommande c where c.commande.idCom = :y")
	public List<DetailCommande> detailComsByCom(@Param("y")int idCom);
	
	@Query("select c from DetailCommande c where c.commande.idCom = :y and c.product.idProd = :z")
	public DetailCommande detailComsByComProd(@Param("y")int idCom, @Param("z")int idProd);

	@Query("select d from DetailCommande d where d.product.idProd = :x")
	public DetailCommande detailByProd(@Param("x")int idProd);
}
