package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Product;

public interface ProductDAO extends JpaRepository<Product, Integer>{
	@Query("select p from Product p where p.designation like :x or p.codeProduct like :x or p.description like :x or p.unite.libelleUnit like :x or p.category.libelleCat like :x order by idProd desc")
	public Page<Product> reacherch(@Param("x")String kw, Pageable page);
	
	@Query("select p from Product p order by idProd desc")
	public List<Product> products();
	
	@Query("select p from Product p where p.qteStock > 0 order by idProd desc")
	public List<Product> disponibleProd();
		
	@Query("select p.idProd from Product p")
	public int count(List<Integer> idProd);
	
	@Query("select p from Product p where p.designation = :x and p.description = :y and idProd != :z")
	public Product prodByDesignDescrip(@Param("x")String design, @Param("y")String desc, @Param("z")int idProd);
}
