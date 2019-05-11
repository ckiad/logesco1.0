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
@Table(name="decision")
public class Decision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idDecision;
	@NotNull
	private String intituleDecision;
	@NotNull
	@Column(unique=true)
	private String codeDecision;
	
	@NotNull
	private String intituleDecisionEn;
	@NotNull
	@Column(unique=true)
	private String codeDecisionEn;

	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table DecisionConseil
	 ******************************************/
	@OneToMany(mappedBy="decisionAssocie")
	Collection<DecisionConseil> listofDecisionConseil;
	
	/**
	 * 
	 */
	public Decision() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idDecision
	 */
	public Long getIdDecision() {
		return idDecision;
	}

	/**
	 * @param idDecision the idDecision to set
	 */
	public void setIdDecision(Long idDecision) {
		this.idDecision = idDecision;
	}

	/**
	 * @return the intituleDecision
	 */
	public String getIntituleDecision() {
		return intituleDecision;
	}

	/**
	 * @param intituleDecision the intituleDecision to set
	 */
	public void setIntituleDecision(String intituleDecision) {
		this.intituleDecision = intituleDecision;
	}

	/**
	 * @return the codeDecision
	 */
	public String getCodeDecision() {
		return codeDecision;
	}

	/**
	 * @param codeDecision the codeDecision to set
	 */
	public void setCodeDecision(String codeDecision) {
		this.codeDecision = codeDecision;
	}

	

	/**
	 * @return the intituleDecisionEn
	 */
	public String getIntituleDecisionEn() {
		return intituleDecisionEn;
	}

	/**
	 * @param intituleDecisionEn the intituleDecisionEn to set
	 */
	public void setIntituleDecisionEn(String intituleDecisionEn) {
		this.intituleDecisionEn = intituleDecisionEn;
	}

	/**
	 * @return the codeDecisionEn
	 */
	public String getCodeDecisionEn() {
		return codeDecisionEn;
	}

	/**
	 * @param codeDecisionEn the codeDecisionEn to set
	 */
	public void setCodeDecisionEn(String codeDecisionEn) {
		this.codeDecisionEn = codeDecisionEn;
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

	
	

	
	
}
