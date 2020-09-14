package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Unite;

public interface UniteDAO extends JpaRepository<Unite, Integer>{

	@Query("select u from Unite u where u.libelleUnit like :x")
	public List<Unite> reacherchU(@Param("x")String kw);
	
	@Query("select u from Unite u where u.libelleUnit like :x")
	public Unite existUnite(@Param("x")String kw);
}
