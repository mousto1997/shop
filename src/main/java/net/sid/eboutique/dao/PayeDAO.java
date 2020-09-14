package net.sid.eboutique.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import net.sid.eboutique.entities.Pay;

public interface PayeDAO extends JpaRepository<Pay, Integer>{

}
