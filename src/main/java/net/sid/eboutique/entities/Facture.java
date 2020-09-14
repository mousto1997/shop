package net.sid.eboutique.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
@Entity
public class Facture {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idFacture;
	private String codeFac;
	private Date dateFac;
	private Boolean status;
	@OneToMany(mappedBy="facture", cascade={CascadeType.REMOVE})
	private Collection<Pay> pays;
	@ManyToOne
	@JoinColumn
	private Commande commande;
	public int getIdFacture() {
		return idFacture;
	}
	public void setIdFacture(int idFacture) {
		this.idFacture = idFacture;
	}
	public String getCodeFac() {
		return codeFac;
	}
	public void setCodeFac(String codeFac) {
		this.codeFac = codeFac;
	}
	public Date getDateFac() {
		return dateFac;
	}
	public void setDateFac(Date dateFac) {
		this.dateFac = dateFac;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Commande getCommande() {
		return commande;
	}
	public void setCommande(Commande commande) {
		this.commande = commande;
	}
	public Facture(String codeFac, Date dateFac, Boolean status, Commande commande) {
		super();
		this.codeFac = codeFac;
		this.dateFac = dateFac;
		this.status = status;
		this.commande = commande;
	}
	public Facture() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
}
