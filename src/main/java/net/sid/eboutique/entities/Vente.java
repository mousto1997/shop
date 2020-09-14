package net.sid.eboutique.entities;

import java.io.Serializable;
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
@SuppressWarnings("serial")
@Entity
public class Vente implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idSell;
	private String codeSell;
	private String client;
	private Date dateSell;
	private String sujet;
	private double amount;
	@ManyToOne
	@JoinColumn
	private User user;
	@OneToMany(mappedBy="vente", cascade={CascadeType.REMOVE})
	private Collection<DetailSell> detailSell;
	@OneToMany(mappedBy="vente", cascade={CascadeType.REMOVE})
	private Collection<FactureSell> facture;	
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}	
	public Collection<DetailSell> getDetailSell() {
		return detailSell;
	}
	public void setDetailSell(Collection<DetailSell> detailSell) {
		this.detailSell = detailSell;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	public Collection<FactureSell> getFacture() {
		return facture;
	}
	public void setFacture(Collection<FactureSell> facture) {
		this.facture = facture;
	}	
	public int getIdSell() {
		return idSell;
	}
	public void setIdSell(int idSell) {
		this.idSell = idSell;
	}
	public String getCodeSell() {
		return codeSell;
	}
	public void setCodeSell(String codeSell) {
		this.codeSell = codeSell;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Date getDateSell() {
		return dateSell;
	}
	public void setDateSell(Date dateSell) {
		this.dateSell = dateSell;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Vente() {
		super();
	}
	
	
}
