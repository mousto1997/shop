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
public class Commande implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCom;
	private String codeCom;
	@ManyToOne
	@JoinColumn
	private Client client;
	private Date dateCom;
	private String sujet;
	private double montantT;
	private double montantP;
	private double montantR;
	private boolean status;
	@ManyToOne
	@JoinColumn
	private User user;
	@OneToMany(mappedBy="commande", cascade={CascadeType.REMOVE})
	private Collection<DetailCommande> dcomm;
	@OneToMany(mappedBy="commande", cascade={CascadeType.REMOVE})
	private Collection<Facture> facture;
	public int getIdCom() {
		return idCom;
	}
	public void setIdCom(int idCom) {
		this.idCom = idCom;
	}
	public String getCodeCom() {
		return codeCom;
	}
	public void setCodeCom(String codeCom) {
		this.codeCom = codeCom;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Date getDateCom() {
		return dateCom;
	}
	public void setDateCom(Date dateCom) {
		this.dateCom = dateCom;
	}
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}
	public double getMontantT() {
		return montantT;
	}
	public void setMontantT(double montantT) {
		this.montantT = montantT;
	}
	public double getMontantP() {
		return montantP;
	}
	public void setMontantP(double montantP) {
		this.montantP = montantP;
	}
	public double getMontantR() {
		return montantR;
	}
	public void setMontantR(double montantR) {
		this.montantR = montantR;
	}
	public Collection<DetailCommande> getDcomm() {
		return dcomm;
	}
	public void setDcomm(Collection<DetailCommande> dcomm) {
		this.dcomm = dcomm;
	}	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	public Collection<Facture> getFacture() {
		return facture;
	}
	public void setFacture(Collection<Facture> facture) {
		this.facture = facture;
	}
	public Commande(String codeCom, Client client, Date dateCom, String sujet, double montantT, double montantP,
			double montantR) {
		super();
		this.codeCom = codeCom;
		this.client = client;
		this.dateCom = dateCom;
		this.sujet = sujet;
		this.montantT = montantT;
		this.montantP = montantP;
		this.montantR = montantR;
	}
	public Commande() {
		super();
	}
}
