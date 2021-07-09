package net.sid.eboutique.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
@SuppressWarnings("serial")
@Entity
public class User implements Serializable {	
	@Id
	private String login;
	private String nom;	
	private String email;
	private String phone;
	private String password;
	private String photo;
	private boolean active;
	@OneToMany(mappedBy="user", cascade={CascadeType.REMOVE})
	private Collection<Commande> commande;
	@OneToMany(mappedBy="user", cascade={CascadeType.REMOVE})
	private Collection<Vente> vente;
	@OneToMany(mappedBy="user", cascade={CascadeType.REMOVE})
	private Collection<Activity> activity;
	@OneToMany(mappedBy="login", cascade={CascadeType.REMOVE})
	private Collection<User_role> user;
			
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Collection<Commande> getCommande() {
		return commande;
	}
	public void setCommande(Collection<Commande> commande) {
		this.commande = commande;
	}
	public Collection<User_role> getUser() {
		return user;
	}
	public void setUser(Collection<User_role> user) {
		this.user = user;
	}	
	public Collection<Vente> getVente() {
		return vente;
	}
	public void setVente(Collection<Vente> vente) {
		this.vente = vente;
	}
	public Collection<Activity> getActivity() {
		return activity;
	}
	public void setActivity(Collection<Activity> activity) {
		this.activity = activity;
	}
	public User(String nom, String login, String email, String phone, String password, String photo,
			Collection<Commande> commande, Collection<User_role> user) {
		super();
		this.nom = nom;
		this.login = login;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.photo = photo;
		this.commande = commande;
		this.user = user;
	}
	
	
	
	public User(String login, String nom, String email, String phone, String password, boolean active) {
		super();
		this.login = login;
		this.nom = nom;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.active = active;
	}
	
	
	public User() {
		super();
	}
	
}
