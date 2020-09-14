package net.sid.eboutique.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
public class Client implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCli;
	@NotEmpty
	private String nom;
	private String adress;
	private String phone;
	private String email;
	private String company;
	private String city;
	@OneToMany(mappedBy="client", cascade = {CascadeType.REMOVE})
	private Collection<Commande> commande;
		
	public int getIdCli() {
		return idCli;
	}
	public void setIdCli(int idCli) {
		this.idCli = idCli;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Collection<Commande> getCommande() {
		return commande;
	}
	public void setCommande(Collection<Commande> commande) {
		this.commande = commande;
	}
	
	public Client(@NotEmpty String nom, String adress, String phone, String email, String company, String city) {
		super();
		this.nom = nom;
		this.adress = adress;
		this.phone = phone;
		this.email = email;
		this.company = company;
		this.city = city;
	}
	public Client() {
		super();
	}
	
	
}
