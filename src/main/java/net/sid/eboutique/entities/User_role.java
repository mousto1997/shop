package net.sid.eboutique.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User_role {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="role")
	private Role role;
	@ManyToOne
	@JoinColumn(name="login")
	private User login;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}	
	public User getLogin() {
		return login;
	}
	public void setLogin(User login) {
		this.login = login;
	}
	public User_role(Role role, User login) {
		super();
		this.role = role;
		this.login = login;
	}
	public User_role() {
		super();
	}
	
	
}
