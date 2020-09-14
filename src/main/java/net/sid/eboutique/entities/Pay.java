package net.sid.eboutique.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Pay implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idPaye;
	private Date datePaye;
	private double montantP;
	@ManyToOne
	@JoinColumn
	private Facture facture;
	@ManyToOne
	@JoinColumn
	private User user;
	public int getIdPaye() {
		return idPaye;
	}
	public void setIdPaye(int idPaye) {
		this.idPaye = idPaye;
	}
	public Date getDatePaye() {
		return datePaye;
	}
	public void setDatePaye(Date datePaye) {
		this.datePaye = datePaye;
	}
	public double getMontantP() {
		return montantP;
	}
	public void setMontantP(double montantP) {
		this.montantP = montantP;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Facture getFacture() {
		return facture;
	}
	public void setFacture(Facture facture) {
		this.facture = facture;
	}
	public Pay(Date datePaye, double montantP, Facture facture, User user) {
		super();
		this.datePaye = datePaye;
		this.montantP = montantP;
		this.facture = facture;
		this.user = user;
	}
	public Pay() {
		super();
	}	
}
