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
@Table(name="sanctionTravail")
public class SanctionTravail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idSancTrav;
	@NotNull
	private String intituleSancTrav;
	@NotNull
	@Column(unique=true)
	private String codeSancTrav;
	
	@NotNull
	private String intituleSancTravEn;
	@NotNull
	@Column(unique=true)
	private String codeSancTravEn;
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table DecisionConseil
	 ******************************************/
	@OneToMany(mappedBy="sanctionTravAssocie")
	Collection<DecisionConseil> listofDecisionConseil;
	
	
	/**
	 * 
	 */
	public SanctionTravail() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idSancTrav
	 */
	public Long getIdSancTrav() {
		return idSancTrav;
	}

	/**
	 * @param idSancTrav the idSancTrav to set
	 */
	public void setIdSancTrav(Long idSancTrav) {
		this.idSancTrav = idSancTrav;
	}

	/**
	 * @return the intituleSancTrav
	 */
	public String getIntituleSancTrav() {
		return intituleSancTrav;
	}

	/**
	 * @param intituleSancTrav the intituleSancTrav to set
	 */
	public void setIntituleSancTrav(String intituleSancTrav) {
		this.intituleSancTrav = intituleSancTrav;
	}

	/**
	 * @return the codeSancTrav
	 */
	public String getCodeSancTrav() {
		return codeSancTrav;
	}

	/**
	 * @param codeSancTrav the codeSancTrav to set
	 */
	public void setCodeSancTrav(String codeSancTrav) {
		this.codeSancTrav = codeSancTrav;
	}

	

	/**
	 * @return the intituleSancTravEn
	 */
	public String getIntituleSancTravEn() {
		return intituleSancTravEn;
	}

	/**
	 * @param intituleSancTravEn the intituleSancTravEn to set
	 */
	public void setIntituleSancTravEn(String intituleSancTravEn) {
		this.intituleSancTravEn = intituleSancTravEn;
	}

	/**
	 * @return the codeSancTravEn
	 */
	public String getCodeSancTravEn() {
		return codeSancTravEn;
	}

	/**
	 * @param codeSancTravEn the codeSancTravEn to set
	 */
	public void setCodeSancTravEn(String codeSancTravEn) {
		this.codeSancTravEn = codeSancTravEn;
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

	public String getSanctionTravString(String lang){
		if(lang.equalsIgnoreCase("fr")==true){
			String ch = this.intituleSancTrav+" ("+this.codeSancTrav+")";
			
			return ch;
		}
		else{
			String ch=this.intituleSancTravEn+" ("+this.codeSancTravEn+")";
			return ch;
		}
}
	
	

}
