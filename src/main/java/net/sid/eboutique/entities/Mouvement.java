package net.sid.eboutique.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mouvement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1251534590799378119L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idMouv;
	private String typeMouv;
	private String commande;
	private String vente; 
	private Date dateMouv;
	private int qte;
	private Double montant;
	private String product;	
	public int getIdMouv() {
		return idMouv;
	}
	public void setIdMouv(int idMouv) {
		this.idMouv = idMouv;
	}
	public String getTypeMouv() {
		return typeMouv;
	}
	public void setTypeMouv(String typeMouv) {
		this.typeMouv = typeMouv;
	}
	public Date getDateMouv() {
		return dateMouv;
	}
	public void setDateMouv(Date dateMouv) {
		this.dateMouv = dateMouv;
	}
	public int getQte() {
		return qte;
	}
	public void setQte(int qte) {
		this.qte = qte;
	}		
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}
	public Mouvement(String typeMouv, String commande, String vente, Date dateMouv, int qte, Double montant,
			String product) {
		super();
		this.typeMouv = typeMouv;
		this.commande = commande;
		this.vente = vente;
		this.dateMouv = dateMouv;
		this.qte = qte;
		this.montant = montant;
		this.product = product;
	}
	public String getCommande() {
		return commande;
	}
	public void setCommande(String commande) {
		this.commande = commande;
	}
	public String getVente() {
		return vente;
	}
	public void setVente(String vente) {
		this.vente = vente;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Mouvement() {
		super();
	}
	
}
