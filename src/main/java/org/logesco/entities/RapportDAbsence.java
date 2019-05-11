/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="rapportDAbsence")
public class RapportDAbsence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idRabs;
	@NotNull
	private int nbreheureJustifie;
	@NotNull
	private int nbreheureNJustifie;
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateenreg;
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table Sequences
	 ******************************************/
	@ManyToOne
	Sequences sequence;
	
	/*
	 * Association avec la table Eleves
	 ******************************************/
	@ManyToOne
	Eleves eleve;
	
	

	/**
	 * 
	 */
	public RapportDAbsence() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nbreheureJustifie
	 * @param nbreheureNJustifie
	 */
	public RapportDAbsence(int nbreheureJustifie, int nbreheureNJustifie) {
		super();
		this.nbreheureJustifie = nbreheureJustifie;
		this.nbreheureNJustifie = nbreheureNJustifie;
	}

	/**
	 * @return the idRabs
	 */
	public Long getIdRabs() {
		return idRabs;
	}

	/**
	 * @param idRabs the idRabs to set
	 */
	public void setIdRabs(Long idRabs) {
		this.idRabs = idRabs;
	}

	/**
	 * @return the nbreheureJustifie
	 */
	public int getNbreheureJustifie() {
		return nbreheureJustifie;
	}

	/**
	 * @param nbreheureJustifie the nbreheureJustifie to set
	 */
	public void setNbreheureJustifie(int nbreheureJustifie) {
		this.nbreheureJustifie = nbreheureJustifie;
	}

	/**
	 * @return the nbreheureNJustifie
	 */
	public int getNbreheureNJustifie() {
		return nbreheureNJustifie;
	}

	/**
	 * @param nbreheureNJustifie the nbreheureNJustifie to set
	 */
	public void setNbreheureNJustifie(int nbreheureNJustifie) {
		this.nbreheureNJustifie = nbreheureNJustifie;
	}

	/**
	 * @return the dateenreg
	 */
	public Date getDateenreg() {
		return dateenreg;
	}

	/**
	 * @param dateenreg the dateenreg to set
	 */
	public void setDateenreg(Date dateenreg) {
		this.dateenreg = dateenreg;
	}

	/**
	 * @return the sequence
	 */
	public Sequences getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Sequences sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the eleve
	 */
	public Eleves getEleve() {
		return eleve;
	}

	/**
	 * @param eleve the eleve to set
	 */
	public void setEleve(Eleves eleve) {
		this.eleve = eleve;
	}

	

}
