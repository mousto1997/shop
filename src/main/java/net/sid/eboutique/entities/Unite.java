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
public class Unite implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idUnit;
	private String libelleUnit;
	@OneToMany(mappedBy="unite", cascade={CascadeType.REMOVE})
	private Collection<Product> products;
	public int getIdUnit() {
		return idUnit;
	}
	public void setIdUnit(int idUnit) {
		this.idUnit = idUnit;
	}
	public String getLibelleUnit() {
		return libelleUnit;
	}
	public void setLibelleUnit(String libelleUnit) {
		this.libelleUnit = libelleUnit;
	}
	public Collection<Product> getProducts() {
		return products;
	}
	public void setProducts(Collection<Product> products) {
		this.products = products;
	}
	public Unite(String libelleUnit) {
		super();
		this.libelleUnit = libelleUnit;
	}
	public Unite() {
		super();
	}
	
}
