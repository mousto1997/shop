package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Categorie;

public interface CategoryDAO extends JpaRepository<Categorie, Integer>{
	@Query("select c from Categorie c where c.libelleCat like :x")
	public List<Categorie> reacherch(@Param("x")String kw);
	
	@Query("select c from Categorie c where c.libelleCat like :x")
	public Categorie existCat(@Param("x")String kw);
}
