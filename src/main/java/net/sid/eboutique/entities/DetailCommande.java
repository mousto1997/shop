package net.sid.eboutique.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class DetailCommande implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idDCom;
	@ManyToOne
	@JoinColumn
	private Commande commande;
	@ManyToOne
	@JoinColumn
	private Product product;
	private int qteCom;
	private double prixUnit;	
	public Commande getCommande() {
		return commande;
	}
	public void setCommande(Commande commande) {
		this.commande = commande;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQteCom() {
		return qteCom;
	}
	public void setQteCom(int qteCom) {
		this.qteCom = qteCom;
	}
	public double getPrixUnit() {
		return prixUnit;
	}
	public void setPrixUnit(double prixUnit) {
		this.prixUnit = prixUnit;
	}	
	public int getIdDCom() {
		return idDCom;
	}
	public void setIdDCom(int idDCom) {
		this.idDCom = idDCom;
	}
	public DetailCommande(Commande commande, Product product, int qteCom, double prixUnit) {
		super();
		this.commande = commande;
		this.product = product;
		this.qteCom = qteCom;
		this.prixUnit = prixUnit;
	}
	public DetailCommande() {
		super();
	}	
}
