package net.sid.eboutique.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.User_role;

public interface UserRoleDAO extends JpaRepository<User_role, Integer>{
	@Query("select u from User_role u where u.login.login like :x or u.role.role like :x")
	public List<User_role> userRoles(@Param("x")String kw);
	
	@Query("select u from User_role u where u.login.login = :x")
	public User_role uroleByUser(@Param("x")String login);

}
