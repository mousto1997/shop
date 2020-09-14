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
public class Activity implements Serializable {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idAct;
	private String action;
	private Date dateAct;
	@ManyToOne
	@JoinColumn
	private User user;
	
	public int getIdAct() {
		return idAct;
	}	
	public Date getDateAct() {
		return dateAct;
	}
	public void setDateAct(Date dateAct) {
		this.dateAct = dateAct;
	}
	public void setIdAct(int idAct) {
		this.idAct = idAct;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Activity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Activity(String action, User user) {
		super();
		this.action = action;
		this.user = user;
	}			
}
