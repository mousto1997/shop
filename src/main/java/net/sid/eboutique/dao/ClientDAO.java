package net.sid.eboutique.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.sid.eboutique.entities.Client;

public interface ClientDAO extends JpaRepository<Client, Integer>{
	@Query("select c from Client c where c.nom like :x or c.phone like :x or c.adress like :x or c.email like :x or c.company like :x order by c.idCli desc")
	public Page<Client> clients(@Param("x")String kw, Pageable page);
	
	@Query("select c from Client c where c.email = :x and c.idCli != :y and c.email != ''")
	public Client clientByEmail(@Param("x")String email, @Param("y")int idCli);
	
	@Query("select c from Client c where c.phone = :x and c.idCli != :y")
	public Client clientByPhone(@Param("x")String phone, @Param("y")int idCli);
	
	@Query("select c from Client c where c.phone = :x and c.nom = :z and c.idCli != :y")
	public Client clientByPhoneName(@Param("x")String phone, @Param("z")String name, @Param("y")int idCli);
	
	@Query("select c from Client c where c.nom = :x and c.company = :z and c.idCli != :y")
	public Client clientByNameCompany(@Param("x")String name, @Param("z")String company, @Param("y")int idCli);

}
