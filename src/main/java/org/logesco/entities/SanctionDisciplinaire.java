/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="sanctionDisciplinaire")
public class SanctionDisciplinaire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idSancDisc;
	@NotNull
	private String intituleSancDisc;
	@NotNull
	@Column(unique=true)
	private String codeSancDisc;
	
	@NotNull
	private String intituleSancDiscEn;
	@NotNull
	@Column(unique=true)
	private String codeSancDiscEn;
	
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table RapportDisciplinaire
	 ******************************************/
	@OneToMany(mappedBy="sanctionDisc")
	Collection<RapportDisciplinaire> listofRDisc;
	
	/*
	 * Association avec la table DecisionConseil
	 ******************************************/
	@OneToMany(mappedBy="sanctionDiscAssocie")
	Collection<DecisionConseil> listofDecisionConseil;
	
	
	/**
	 * 
	 */
	public SanctionDisciplinaire() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idSancDisc
	 */
	public Long getIdSancDisc() {
		return idSancDisc;
	}

	/**
	 * @param idSancDisc the idSancDisc to set
	 */
	public void setIdSancDisc(Long idSancDisc) {
		this.idSancDisc = idSancDisc;
	}

	/**
	 * @return the intituleSancDisc
	 */
	public String getIntituleSancDisc() {
		return intituleSancDisc;
	}

	/**
	 * @param intituleSancDisc the intituleSancDisc to set
	 */
	public void setIntituleSancDisc(String intituleSancDisc) {
		this.intituleSancDisc = intituleSancDisc;
	}

	/**
	 * @return the codeSancDisc
	 */
	public String getCodeSancDisc() {
		return codeSancDisc;
	}

	/**
	 * @param codeSancDisc the codeSancDisc to set
	 */
	public void setCodeSancDisc(String codeSancDisc) {
		this.codeSancDisc = codeSancDisc;
	}

	

	/**
	 * @return the intituleSancDiscEn
	 */
	public String getIntituleSancDiscEn() {
		return intituleSancDiscEn;
	}

	/**
	 * @param intituleSancDiscEn the intituleSancDiscEn to set
	 */
	public void setIntituleSancDiscEn(String intituleSancDiscEn) {
		this.intituleSancDiscEn = intituleSancDiscEn;
	}

	/**
	 * @return the codeSancDiscEn
	 */
	public String getCodeSancDiscEn() {
		return codeSancDiscEn;
	}

	/**
	 * @param codeSancDiscEn the codeSancDiscEn to set
	 */
	public void setCodeSancDiscEn(String codeSancDiscEn) {
		this.codeSancDiscEn = codeSancDiscEn;
	}

	/**
	 * @return the listofRDisc
	 */
	public Collection<RapportDisciplinaire> getListofRDisc() {
		return listofRDisc;
	}

	/**
	 * @param listofRDisc the listofRDisc to set
	 */
	public void setListofRDisc(Collection<RapportDisciplinaire> listofRDisc) {
		this.listofRDisc = listofRDisc;
	}

	/**
	 * @return the listofDecisionConseil
	 */
	public Collection<DecisionConseil> getListofDecisionConseil() {
		return listofDecisionConseil;
	}

	/**
	 * @param listofDecisionConseil the listofDecisionConseil to set
	 */
	public void setListofDecisionConseil(Collection<DecisionConseil> listofDecisionConseil) {
		this.listofDecisionConseil = listofDecisionConseil;
	}

	
	public String getSanctionDiscString(String lang){
		if(lang.equalsIgnoreCase("fr")==true){
			String ch= this.intituleSancDisc+" ("+this.codeSancDisc+")";
			
			return ch;
		}
		else{
			String ch=this.intituleSancDiscEn+" ("+this.codeSancDiscEn+")";
			
			return ch;
		}
	}
	
	
	
	

}
