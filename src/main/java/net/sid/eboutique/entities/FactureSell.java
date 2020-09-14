package net.sid.eboutique.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class FactureSell {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idFacSell;
	private String codeFac;
	private Date dateFac;
	private Boolean status;
	@ManyToOne
	@JoinColumn
	private Vente vente;	
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
	public int getIdFacSell() {
		return idFacSell;
	}
	public void setIdFacSell(int idFacSell) {
		this.idFacSell = idFacSell;
	}
	public Vente getVente() {
		return vente;
	}
	public void setVente(Vente vente) {
		this.vente = vente;
	}
	public FactureSell(String codeFac, Date dateFac, Boolean status, Vente vente) {
		super();
		this.codeFac = codeFac;
		this.dateFac = dateFac;
		this.status = status;
		this.vente = vente;
	}
	public FactureSell() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
}
