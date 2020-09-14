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
public class Categorie implements Serializable{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCat;
	@NotEmpty
	private String libelleCat;
	@OneToMany(mappedBy="category", cascade={CascadeType.REMOVE})
	private Collection<Product> category;	

	public int getIdCat() {
		return idCat;
	}
	public void setIdCat(int idCat) {
		this.idCat = idCat;
	}
	public String getLibelleCat() {
		return libelleCat;
	}
	public void setLibelleCat(String libelleCat) {
		this.libelleCat = libelleCat;
	}
	public Collection<Product> getCategory() {
		return category;
	}
	public void setCategory(Collection<Product> category) {
		this.category = category;
	}
	public Categorie(@NotEmpty String libelleCat) {
		super();
		this.libelleCat = libelleCat;
	}
	public Categorie() {
		super();
	}	

}
