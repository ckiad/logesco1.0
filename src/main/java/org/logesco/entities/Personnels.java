/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="personnels", 
	uniqueConstraints = {@UniqueConstraint(
		columnNames = {"nomsPers", "prenomsPers", "datenaissPers"})})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Personnels extends Utilisateurs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column(unique=true)
	@NotEmpty
	@Size(min = 9, max = 17)
	private String numcniPers;
	@NotNull
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date datenaissPers;
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String diplomePers;
	@Email
	private String emailPers;
	private String gradePers;
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String lieunaissPers;
	@NotNull
	@NotEmpty
	@Size(min = 3, max = 20)
	private String nationalitePers;
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 50)
	private String nomsPers;
	@NotNull
	@NotEmpty
	@Size(min = 9, max = 13)
	private String numtel1Pers;
	private String numtel2Pers;
	private String photoPers;
	private String prenomsPers;
	private String quartierPers;
	@NotNull
	@NotEmpty
	@Size(min = 1, max = 8)
	private String sexePers;
	@NotNull
	@NotEmpty
	@Size(min = 3, max = 13)
	private String statutPers;//ECI ou Vacataire ou Permanent ou alors Fonctionnaire
	private String villePers;
	
	/*
	 * Champ ajouter pour la conformite avec la realite sur le terrain
	 */
	@Column(unique=true)
	private String matriculePers;
	private String etabDAttache;
	/*****
	 * les différentes fonctions sont: proviseur, censeur, sg, enseignant, intendant, secretaire, surveillant,
	 * Veilleur(Gardien), Nettoyeur, autres(dont la valeur sera le vide)
	 */
	@NotEmpty
	private String fonctionPers; 
	private int quotaHorairePers;
	private String deptoriginePers;
	private String regionoriginePers;
	@NotEmpty
	private String sitmatriPers;
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateentreeFPPers; 
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date datePSPers; 
	private String observations;
	
	/**
	 * 
	 */
	public Personnels() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param numcniPers
	 * @param datenaissPers
	 * @param diplomePers
	 * @param emailPers
	 * @param gradePers
	 * @param lieunaissPers
	 * @param nationalitePers
	 * @param nomsPers
	 * @param numtel1Pers
	 * @param numtel2Pers
	 * @param photoPers
	 * @param prenomsPers
	 * @param quartierPers
	 * @param sexePers
	 * @param statutPers
	 * @param villePers
	 */
	public Personnels(String numcniPers, Date datenaissPers, String diplomePers, String emailPers, String gradePers,
			String lieunaissPers, String nationalitePers, String nomsPers, String numtel1Pers, String numtel2Pers,
			String photoPers, String prenomsPers, String quartierPers, String sexePers, String statutPers,
			String villePers, String username, String password, boolean etatCompteUsers) {
		
		super(username, password, etatCompteUsers);
		this.numcniPers = numcniPers;
		this.datenaissPers = datenaissPers;
		this.diplomePers = diplomePers;
		this.emailPers = emailPers;
		this.gradePers = gradePers;
		this.lieunaissPers = lieunaissPers;
		this.nationalitePers = nationalitePers;
		this.nomsPers = nomsPers;
		this.numtel1Pers = numtel1Pers;
		this.numtel2Pers = numtel2Pers;
		this.photoPers = photoPers;
		this.prenomsPers = prenomsPers;
		this.quartierPers = quartierPers;
		this.sexePers = sexePers;
		this.statutPers = statutPers;
		this.villePers = villePers;
	}

	/**
	 * @param username
	 * @param password
	 * @param etatCompteUsers
	 */
	public Personnels(String username, String password, boolean etatCompteUsers) {
		super(username, password, etatCompteUsers);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @return the numcniPers
	 */
	public String getNumcniPers() {
		return numcniPers;
	}

	/**
	 * @param numcniPers the numcniPers to set
	 */
	public void setNumcniPers(String numcniPers) {
		this.numcniPers = numcniPers;
	}

	/**
	 * @return the datenaissPers
	 */
	public Date getDatenaissPers() {
		return datenaissPers;
	}

	/**
	 * @param datenaissPers the datenaissPers to set
	 */
	public void setDatenaissPers(Date datenaissPers) {
		this.datenaissPers = datenaissPers;
	}

	/**
	 * @return the diplomePers
	 */
	public String getDiplomePers() {
		return diplomePers;
	}

	/**
	 * @param diplomePers the diplomePers to set
	 */
	public void setDiplomePers(String diplomePers) {
		this.diplomePers = diplomePers;
	}

	/**
	 * @return the emailPers
	 */
	public String getEmailPers() {
		return emailPers;
	}

	/**
	 * @param emailPers the emailPers to set
	 */
	public void setEmailPers(String emailPers) {
		this.emailPers = emailPers;
	}

	/**
	 * @return the gradePers
	 */
	public String getGradePers() {
		return gradePers;
	}

	/**
	 * @param gradePers the gradePers to set
	 */
	public void setGradePers(String gradePers) {
		this.gradePers = gradePers;
	}

	/**
	 * @return the lieunaissPers
	 */
	public String getLieunaissPers() {
		return lieunaissPers;
	}

	/**
	 * @param lieunaissPers the lieunaissPers to set
	 */
	public void setLieunaissPers(String lieunaissPers) {
		this.lieunaissPers = lieunaissPers;
	}

	/**
	 * @return the nationalitePers
	 */
	public String getNationalitePers() {
		return nationalitePers;
	}

	/**
	 * @param nationalitePers the nationalitePers to set
	 */
	public void setNationalitePers(String nationalitePers) {
		this.nationalitePers = nationalitePers;
	}

	/**
	 * @return the nomsPers
	 */
	public String getNomsPers() {
		return nomsPers;
	}

	/**
	 * @param nomsPers the nomsPers to set
	 */
	public void setNomsPers(String nomsPers) {
		this.nomsPers = nomsPers;
	}

	/**
	 * @return the numtel1Pers
	 */
	public String getNumtel1Pers() {
		return numtel1Pers;
	}

	/**
	 * @param numtel1Pers the numtel1Pers to set
	 */
	public void setNumtel1Pers(String numtel1Pers) {
		this.numtel1Pers = numtel1Pers;
	}

	/**
	 * @return the numtel2Pers
	 */
	public String getNumtel2Pers() {
		return numtel2Pers;
	}

	/**
	 * @param numtel2Pers the numtel2Pers to set
	 */
	public void setNumtel2Pers(String numtel2Pers) {
		this.numtel2Pers = numtel2Pers;
	}

	/**
	 * @return the photoPers
	 */
	public String getPhotoPers() {
		return photoPers;
	}

	/**
	 * @param photoPers the photoPers to set
	 */
	public void setPhotoPers(String photoPers) {
		this.photoPers = photoPers;
	}

	/**
	 * @return the prenomsPers
	 */
	public String getPrenomsPers() {
		return prenomsPers;
	}

	/**
	 * @param prenomsPers the prenomsPers to set
	 */
	public void setPrenomsPers(String prenomsPers) {
		this.prenomsPers = prenomsPers;
	}

	/**
	 * @return the quartierPers
	 */
	public String getQuartierPers() {
		return quartierPers;
	}

	/**
	 * @param quartierPers the quartierPers to set
	 */
	public void setQuartierPers(String quartierPers) {
		this.quartierPers = quartierPers;
	}

	/**
	 * @return the sexePers
	 */
	public String getSexePers() {
		return sexePers;
	}

	/**
	 * @param sexePers the sexePers to set
	 */
	public void setSexePers(String sexePers) {
		this.sexePers = sexePers;
	}

	/**
	 * @return the statutPers
	 */
	public String getStatutPers() {
		return statutPers;
	}

	/**
	 * @param statutPers the statutPers to set
	 */
	public void setStatutPers(String statutPers) {
		this.statutPers = statutPers;
	}

	/**
	 * @return the villePers
	 */
	public String getVillePers() {
		return villePers;
	}

	/**
	 * @param villePers the villePers to set
	 */
	public void setVillePers(String villePers) {
		this.villePers = villePers;
	}

	/**
	 * @return the matriculePers
	 */
	public String getMatriculePers() {
		return matriculePers;
	}

	/**
	 * @param matriculePers the matriculePers to set
	 */
	public void setMatriculePers(String matriculePers) {
		this.matriculePers = matriculePers;
	}

	/**
	 * @return the fonctionPers
	 */
	public String getFonctionPers() {
		return fonctionPers;
	}

	/**
	 * @param fonctionPers the fonctionPers to set
	 */
	public void setFonctionPers(String fonctionPers) {
		this.fonctionPers = fonctionPers;
	}

	

	/**
	 * @return the quotaHorairePers
	 */
	public int getQuotaHorairePers() {
		return quotaHorairePers;
	}

	/**
	 * @param quotaHorairePers the quotaHorairePers to set
	 */
	public void setQuotaHorairePers(int quotaHorairePers) {
		this.quotaHorairePers = quotaHorairePers;
	}

	/**
	 * @return the deptoriginePers
	 */
	public String getDeptoriginePers() {
		return deptoriginePers;
	}

	/**
	 * @param deptoriginePers the deptoriginePers to set
	 */
	public void setDeptoriginePers(String deptoriginePers) {
		this.deptoriginePers = deptoriginePers;
	}

	/**
	 * @return the regionoriginePers
	 */
	public String getRegionoriginePers() {
		return regionoriginePers;
	}

	/**
	 * @param regionoriginePers the regionoriginePers to set
	 */
	public void setRegionoriginePers(String regionoriginePers) {
		this.regionoriginePers = regionoriginePers;
	}

	/**
	 * @return the sitmatriPers
	 */
	public String getSitmatriPers() {
		return sitmatriPers;
	}

	/**
	 * @param sitmatriPers the sitmatriPers to set
	 */
	public void setSitmatriPers(String sitmatriPers) {
		this.sitmatriPers = sitmatriPers;
	}

	/**
	 * @return the dateentreeFPPers
	 */
	public Date getDateentreeFPPers() {
		return dateentreeFPPers;
	}

	/**
	 * @param dateentreeFPPers the dateentreeFPPers to set
	 */
	public void setDateentreeFPPers(Date dateentreeFPPers) {
		this.dateentreeFPPers = dateentreeFPPers;
	}

	/**
	 * @return the datePSPers
	 */
	public Date getDatePSPers() {
		return datePSPers;
	}

	/**
	 * @param datePSPers the datePSPers to set
	 */
	public void setDatePSPers(Date datePSPers) {
		this.datePSPers = datePSPers;
	}

	/**
	 * @return the observations
	 */
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations the observations to set
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return the etabDAttache
	 */
	public String getEtabDAttache() {
		return etabDAttache;
	}

	/**
	 * @param etabDAttache the etabDAttache to set
	 */
	public void setEtabDAttache(String etabDAttache) {
		this.etabDAttache = etabDAttache;
	}

	
	
	
	
	

}
