package net.sid.eboutique.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.sid.eboutique.entities.User;

@Repository
public interface UserDAO extends JpaRepository<User, String>{
	@Query("select u from User u where u.login = :x")
	public User userByUsername(@Param("x")String username);
	@Query("select u from User u where u.login like :x or u.nom like :x or u.email like :x or u.phone like :x")
	public Page<User> users(@Param("x")String kw, Pageable page);
}
