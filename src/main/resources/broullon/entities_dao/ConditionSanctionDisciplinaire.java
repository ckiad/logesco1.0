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
@Table(name="conditionSanctionDisciplinaire")
public class ConditionSanctionDisciplinaire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idCSD;
	@NotNull
	private int absenceMin;
	@NotNull
	private int absenceMax;
	
	private int nbreperiode;
	private String unite;
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table SanctionDisciplinaire
	 ******************************************/
	/**********************
	 * Le couple (absenceMin, absenceMax) implique une sanctionDisciplinaire qui va donc influencer 
	 * la decision et la sanction du travail. 
	 * Par exemple, entre absenceMin=3h et absenceMax=8h  non justifie alors on a une consigne de 2h mais
	 * la decision et la sanction de travail reste non influencer par cette sanction. Car la consigne. Or si on a 
	 * une blame conduite, on peut se voir refuse le tableau d'honneur meme a ce moment la decision aussi ne 
	 * sera pas influence. 
	 */
	@ManyToOne
	private SanctionDisciplinaire sanctionDiscAssocie;
	
	/*
	 * Association avec la table Periode
	 ******************************************/
	@ManyToOne
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
	public ConditionSanctionDisciplinaire() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return the idCSD
	 */
	public Long getIdCSD() {
		return idCSD;
	}


	/**
	 * @param idCSD the idCSD to set
	 */
	public void setIdCSD(Long idCSD) {
		this.idCSD = idCSD;
	}


	/**
	 * @return the absenceMin
	 */
	public int getAbsenceMin() {
		return absenceMin;
	}
	/**
	 * @param absenceMin the absenceMin to set
	 */
	public void setAbsenceMin(int absenceMin) {
		this.absenceMin = absenceMin;
	}
	/**
	 * @return the absenceMax
	 */
	public int getAbsenceMax() {
		return absenceMax;
	}
	/**
	 * @param absenceMax the absenceMax to set
	 */
	public void setAbsenceMax(int absenceMax) {
		this.absenceMax = absenceMax;
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
	
	
	
	

}
