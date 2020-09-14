package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Role;

public interface RoleDAO extends JpaRepository<Role, String>{
	@Query("select r from Role r where r.role = :x")
	public Role roleById(@Param("x")String role);
	
	@Query("select r from Role r where r.role like :x")
	public List<Role> roles(@Param("x")String kw);

}
