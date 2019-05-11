/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="conditionDecision")
public class ConditionDecision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idCD;

	@NotNull
	private double moyenneMin;
	@NotNull
	private double moyenneMax;
	
	private int nbreperiode;
	private String unite;//jours ou heures days or hours
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table SanctionDisciplinaire
	 ******************************************/
	@OneToOne
	private SanctionDisciplinaire sanctionDisciplinaire;
	/*
	 * Association avec la table Periode
	 ******************************************/
	@OneToOne
	private Periodes periodeAssocie;
	/*
	 * Association avec la table Classe
	 ******************************************/
	@ManyToOne
	@JoinColumn(name="idClasseAssocie")
	private Classes classeAssocie;
	
	/********************
	 * Chaque triplet (moyenneMin, moyenneMax, sanctionDisciplinaire) implique une Decision
	 * Cette relation entre les triplets et la decision doit être etabli par le proviseur ou ses censeurs 
	 * pendant les conseils de classe
	 */
	/*
	 * Association avec la table Decision
	 ******************************************/
	@ManyToOne
	private Decision decisionAssocie;
	
	/**
	 * 
	 */
	public ConditionDecision() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idCD
	 */
	public Long getIdCD() {
		return idCD;
	}

	/**
	 * @param idCD the idCD to set
	 */
	public void setIdCD(Long idCD) {
		this.idCD = idCD;
	}

	/**
	 * @return the moyenneMin
	 */
	public double getMoyenneMin() {
		return moyenneMin;
	}

	/**
	 * @param moyenneMin the moyenneMin to set
	 */
	public void setMoyenneMin(double moyenneMin) {
		this.moyenneMin = moyenneMin;
	}

	/**
	 * @return the moyenneMax
	 */
	public double getMoyenneMax() {
		return moyenneMax;
	}

	/**
	 * @param moyenneMax the moyenneMax to set
	 */
	public void setMoyenneMax(double moyenneMax) {
		this.moyenneMax = moyenneMax;
	}

	/**
	 * @return the sanctionDisciplinaire
	 */
	public SanctionDisciplinaire getSanctionDisciplinaire() {
		return sanctionDisciplinaire;
	}

	/**
	 * @param sanctionDisciplinaire the sanctionDisciplinaire to set
	 */
	public void setSanctionDisciplinaire(SanctionDisciplinaire sanctionDisciplinaire) {
		this.sanctionDisciplinaire = sanctionDisciplinaire;
	}

	/**
	 * @return the periodeAssocie
	 */
	public Periodes getPeriodeAssocie() {
		return periodeAssocie;
	}

	/**
	 * @param periodeAssocie the periodeAssocie to set
	 */
	public void setPeriodeAssocie(Periodes periodeAssocie) {
		this.periodeAssocie = periodeAssocie;
	}

	/**
	 * @return the classeAssocie
	 */
	public Classes getClasseAssocie() {
		return classeAssocie;
	}

	/**
	 * @param classeAssocie the classeAssocie to set
	 */
	public void setClasseAssocie(Classes classeAssocie) {
		this.classeAssocie = classeAssocie;
	}

	/**
	 * @return the decisionAssocie
	 */
	public Decision getDecisionAssocie() {
		return decisionAssocie;
	}

	/**
	 * @param decisionAssocie the decisionAssocie to set
	 */
	public void setDecisionAssocie(Decision decisionAssocie) {
		this.decisionAssocie = decisionAssocie;
	}

	/**
	 * @return the nbreperiode
	 */
	public int getNbreperiode() {
		return nbreperiode;
	}

	/**
	 * @param nbreperiode the nbreperiode to set
	 */
	public void setNbreperiode(int nbreperiode) {
		this.nbreperiode = nbreperiode;
	}

	/**
	 * @return the unite
	 */
	public String getUnite() {
		return unite;
	}

	/**
	 * @param unite the unite to set
	 */
	public void setUnite(String unite) {
		this.unite = unite;
	}

	
	
	
}
