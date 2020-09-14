package net.sid.eboutique.entities;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	@Id 
	private String role;
	@OneToMany(mappedBy="role")
	private Collection<User_role> roles;
		
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Role(String role) {
		super();
		this.role = role;
	}
	public Role() {
		super();
	}
	
}
