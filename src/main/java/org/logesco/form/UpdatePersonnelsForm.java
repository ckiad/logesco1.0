/**
 * 
 */
package org.logesco.form;

import java.util.Date;

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
public class UpdatePersonnelsForm {
	/*
	 * Identifiant du personnel en cas de modification de ses données
	 */
	private Long idPersonnels;//
	private String numcniOuUsernamePersAModif;//
	
	/*
	 * Données personnelles
	 */
	@NotNull
	@NotEmpty
	@Size(min = 9, max = 17)
	private String numcniPers;//
	@NotNull
	@Past
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date datenaissPers; //
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String diplomePers;//
	@Email
	private String emailPers;//
	private String gradePers;//
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 20)
	private String lieunaissPers;//
	@NotNull
	@NotEmpty
	@Size(min = 3, max = 20)
	private String nationalitePers;//
	@NotNull
	@NotEmpty
	@Size(min = 2, max = 50)
	private String nomsPers;//
	@NotNull
	@NotEmpty
	@Size(min = 9, max = 13)
	private String numtel1Pers;//
	private String numtel2Pers;//
	private String photoPers;
	private String prenomsPers;//
	private String quartierPers;//
	@NotNull
	@NotEmpty
	@Size(min = 1, max = 8)
	private String sexePers;//
	@NotNull
	@NotEmpty
	@Size(min = 3, max = 13)
	private String statutPers;//ECI ou Vacataire ou alors Fonctionnaire//
	private String villePers;//
	
	/*
	 * Champ ajouter a l'entite Personnel
	 */
	private String matriculePers;
	private String etabDAttache;
	/*****
	 * les différentes fonctions sont: proviseur, censeur, sg, enseignant, intendant, secretaire, surveillant,
	 * Veilleur(Gardien), Nettoyeur, autres(dont la valeur sera le vide)
	 */
	@NotEmpty
	private String fonctionPers; 
	private int quotaHorairePers;
	@NotEmpty
	private String deptoriginePers;
	@NotEmpty
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
	
	
	/*
	 * Données utilisateurs
	 */
	@NotNull
	@NotEmpty
	@Size(min = 3, max = 20)
	private String username;//
	@NotNull
	@NotEmpty
	private String password;//
	
	/*
	 * Données specifique à la fonction
	 */
	private String specialiteProf;//
	
	/*
	 * Numero au sein de la fonction
	 */
	
	private int numeroPers;//
	
	/*
	 * Roles vis à vis du système
	 */
	/*****
	 * DIFFERENTES COMBINAISONS DE ROLES POSSIBLES ET LEUR NUMERO ASSOCIE
	 * 1==Censeur 
	 * 2==Surveillant Général
	 * 3==Enseignant
	 * 4==Intendant
	 * 5==Secretaire
	 * 6==Surveillant
	 * 7==Veilleur
	 * 8==Autres
	 *******/
	@NotNull
	private int roleCode;//
	
	private int roleCodeAModif;//
	
	/*paramètre qui seront utilisé pour réaffichage de getConsulterPersonnel afin que l'user reste sur les pages qu'il
	consultait*/
	
	 int numPageCenseur;
	 int numPageSg;
	 int numPageIntendant;
	 int numPageEns;
	 int numPagePersAppui;
	/**
	 * @return the idPersonnels
	 */
	public Long getIdPersonnels() {
		return idPersonnels;
	}

	/**
	 * @param idPersonnels the idPersonnels to set
	 */
	public void setIdPersonnels(Long idPersonnels) {
		this.idPersonnels = idPersonnels;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the specialiteProf
	 */
	public String getSpecialiteProf() {
		return specialiteProf;
	}

	/**
	 * @param specialiteProf the specialiteProf to set
	 */
	public void setSpecialiteProf(String specialiteProf) {
		this.specialiteProf = specialiteProf;
	}

	/**
	 * @return the numeroInt
	 */
	public int getNumeroPers() {
		return numeroPers;
	}

	/**
	 * @param numeroInt the numeroInt to set
	 */
	public void setNumeroPers(int numeroPers) {
		this.numeroPers = numeroPers;
	}

	/**
	 * @return the roleCode
	 */
	public int getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(int roleCode) {
		this.roleCode = roleCode;
	}

	
	

	/**
	 * @return the numcniOuUsernamePersAModif
	 */
	public String getNumcniOuUsernamePersAModif() {
		return numcniOuUsernamePersAModif;
	}

	/**
	 * @param numcniOuUsernamePersAModif the numcniOuUsernamePersAModif to set
	 */
	public void setNumcniOuUsernamePersAModif(String numcniOuUsernamePersAModif) {
		this.numcniOuUsernamePersAModif = numcniOuUsernamePersAModif;
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
	 * @return the roleCodeAModif
	 */
	public int getRoleCodeAModif() {
		return roleCodeAModif;
	}

	/**
	 * @param roleCodeAModif the roleCodeAModif to set
	 */
	public void setRoleCodeAModif(int roleCodeAModif) {
		this.roleCodeAModif = roleCodeAModif;
	}

	/**
	 * @return the numPageCenseur
	 */
	public int getNumPageCenseur() {
		return numPageCenseur;
	}

	/**
	 * @param numPageCenseur the numPageCenseur to set
	 */
	public void setNumPageCenseur(int numPageCenseur) {
		this.numPageCenseur = numPageCenseur;
	}

	/**
	 * @return the numPageSg
	 */
	public int getNumPageSg() {
		return numPageSg;
	}

	/**
	 * @param numPageSg the numPageSg to set
	 */
	public void setNumPageSg(int numPageSg) {
		this.numPageSg = numPageSg;
	}

	/**
	 * @return the numPageIntendant
	 */
	public int getNumPageIntendant() {
		return numPageIntendant;
	}

	/**
	 * @param numPageIntendant the numPageIntendant to set
	 */
	public void setNumPageIntendant(int numPageIntendant) {
		this.numPageIntendant = numPageIntendant;
	}

	/**
	 * @return the numPageEns
	 */
	public int getNumPageEns() {
		return numPageEns;
	}

	/**
	 * @param numPageEns the numPageEns to set
	 */
	public void setNumPageEns(int numPageEns) {
		this.numPageEns = numPageEns;
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

	/**
	 * @return the numPagePersAppui
	 */
	public int getNumPagePersAppui() {
		return numPagePersAppui;
	}

	/**
	 * @param numPagePersAppui the numPagePersAppui to set
	 */
	public void setNumPagePersAppui(int numPagePersAppui) {
		this.numPagePersAppui = numPagePersAppui;
	}

	
	
	
	
	
	
	
}
