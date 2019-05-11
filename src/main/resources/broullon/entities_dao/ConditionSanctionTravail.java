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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="conditionSanctionTravail")
public class ConditionSanctionTravail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idCST;
	@NotNull
	private int moyenneMin;
	@NotNull
	private int moyenneMax;
	
	private int nbreperiode;
	private String unite;//jours ou heures days or hours
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table SanctionDisciplinaire
	 ******************************************/
	@ManyToOne
	private SanctionDisciplinaire sanctionDiscAssocie;
	
	/********************
	 * Chaque triplet (moyenneMin, moyenneMax, sanctionDisciplinaire) implique une SanctionTravail
	 * Cette relation entre les triplets et la SanctionTravail doit être etabli par le proviseur ou ses censeurs 
	 * pendant les conseils de classe
	 */
	/*
	 * Association avec la table SanctionTravail
	 ******************************************/
	@ManyToOne
	private SanctionTravail sanctionTravAssocie;
	/*
	 * Association avec la table Periode
	 ******************************************/
	@ManyToOne
	@JoinColumn(name="idPeriodeAssocie")
	private Periodes periodeAssocie;
	/*
	 * Association avec la table Classe
	 ******************************************/
	@ManyToOne
	@JoinColumn(name="idClasseAssocie")
	private Classes classeAssocie;
	/**
	 * 
	 */
	public ConditionSanctionTravail() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the idCST
	 */
	public Long getIdCST() {
		return idCST;
	}
	/**
	 * @param idCST the idCST to set
	 */
	public void setIdCST(Long idCST) {
		this.idCST = idCST;
	}
	/**
	 * @return the moyenneMin
	 */
	public int getMoyenneMin() {
		return moyenneMin;
	}
	/**
	 * @param moyenneMin the moyenneMin to set
	 */
	public void setMoyenneMin(int moyenneMin) {
		this.moyenneMin = moyenneMin;
	}
	/**
	 * @return the moyenneMax
	 */
	public int getMoyenneMax() {
		return moyenneMax;
	}
	/**
	 * @param moyenneMax the moyenneMax to set
	 */
	public void setMoyenneMax(int moyenneMax) {
		this.moyenneMax = moyenneMax;
	}
	/**
	 * @return the sanctionDiscAssocie
	 */
	public SanctionDisciplinaire getSanctionDiscAssocie() {
		return sanctionDiscAssocie;
	}
	/**
	 * @param sanctionDiscAssocie the sanctionDiscAssocie to set
	 */
	public void setSanctionDiscAssocie(SanctionDisciplinaire sanctionDiscAssocie) {
		this.sanctionDiscAssocie = sanctionDiscAssocie;
	}
	/**
	 * @return the sanctionTravAssocie
	 */
	public SanctionTravail getSanctionTravAssocie() {
		return sanctionTravAssocie;
	}
	/**
	 * @param sanctionTravAssocie the sanctionTravAssocie to set
	 */
	public void setSanctionTravAssocie(SanctionTravail sanctionTravAssocie) {
		this.sanctionTravAssocie = sanctionTravAssocie;
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
