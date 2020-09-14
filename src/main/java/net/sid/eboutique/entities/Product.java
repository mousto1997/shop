package net.sid.eboutique.entities;

import java.io.Serializable;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
@SuppressWarnings("serial")
@Entity
public class Product implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProd;
	private String codeProduct;
	@NotEmpty
	private String designation;
	private String siz;
	private String description;
	private double price;
	private int qteStock;
	private int qteMin;
	@ManyToOne
	@JoinColumn
	private Unite unite;
	@ManyToOne
	@JoinColumn
	private Categorie category;
	@OneToMany(mappedBy="product", cascade={CascadeType.REMOVE})
	private Collection<DetailCommande> dCommande;
	@OneToMany(mappedBy="product", cascade={CascadeType.REMOVE})
	private Collection<DetailSell> dSell;
	public int getIdProd() {
		return idProd;
	}
	public void setIdProd(int idProd) {
		this.idProd = idProd;
	}
	public String getCodeProduct() {		
		return codeProduct;
	}
	public void setCodeProduct(String codeProduct) {
		this.codeProduct = codeProduct;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getSiz() {
		return siz;
	}
	public void setSiz(String siz) {
		this.siz = siz;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQteStock() {
		return qteStock;
	}
	public void setQteStock(int qteStock) {
		this.qteStock = qteStock;
	}
	public int getQteMin() {
		return qteMin;
	}
	public void setQteMin(int qteMin) {
		this.qteMin = qteMin;
	}
	public Unite getUnite() {
		return unite;
	}
	public void setUnite(Unite unite) {
		this.unite = unite;
	}
	public Categorie getCategory() {
		return category;
	}
	public void setCategory(Categorie category) {
		this.category = category;
	}
	public Collection<DetailCommande> getdCommande() {
		return dCommande;
	}
	public void setdCommande(Collection<DetailCommande> dCommande) {
		this.dCommande = dCommande;
	}
	public Product(String codeProduct, @NotEmpty String designation, String siz, String description, double price,
			int qteStock, int qteMin, Unite unite, Categorie category) {
		super();
		this.codeProduct = codeProduct;
		this.designation = designation;
		this.siz = siz;
		this.description = description;
		this.price = price;
		this.qteStock = qteStock;
		this.qteMin = qteMin;
		this.unite = unite;
		this.category = category;
	}
	public Product() {
		super();
	}
	
}
