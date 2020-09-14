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
public class DetailSell implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idDSell;
	@ManyToOne
	@JoinColumn
	private Vente vente;
	@ManyToOne
	@JoinColumn
	private Product product;
	private int qtySell;
	private double prixUnit;	
	public void setPrixUnit(double prixUnit) {
		this.prixUnit = prixUnit;
	}		
	public int getIdDSell() {
		return idDSell;
	}
	public void setIdDSell(int idDSell) {
		this.idDSell = idDSell;
	}
	public Vente getVente() {
		return vente;
	}
	public void setVente(Vente vente) {
		this.vente = vente;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQtySell() {
		return qtySell;
	}
	public void setQtySell(int qtySell) {
		this.qtySell = qtySell;
	}
	public double getPrixUnit() {
		return prixUnit;
	}

	public DetailSell() {
		super();
	}	
}
