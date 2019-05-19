/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="decisionConseil")
public class DecisionConseil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idDC;
	int nbreperiode;
	String unite;
	
	/****************************************************
	 * Mapping des associations avec les autres tables *
	 *****************************************************/
	/*
	 * Association avec la table Eleves
	 ******************************************/
	@ManyToOne
	Eleves eleveConcerne;
	
	/*
	 * Association avec la table Periodes
	 ******************************************/
	@ManyToOne
	Periodes periodeConcerne;
	
	/*
	 * Association avec la table SanctionDisciplinaire
	 ******************************************/
	@ManyToOne
	SanctionDisciplinaire sanctionDiscAssocie;
	
	/*
	 * Association avec la table SanctionTravail
	 *********************************************/
	@ManyToOne
	SanctionTravail sanctionTravAssocie;
	
	/*
	 * Association avec la table Decision
	 ******************************************/
	@ManyToOne
	Decision decisionAssocie;
	
	
	
	/**
	 * 
	 */
	public DecisionConseil() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the idDC
	 */
	public Long getIdDC() {
		return idDC;
	}



	/**
	 * @param idDC the idDC to set
	 */
	public void setIdDC(Long idDC) {
		this.idDC = idDC;
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
	 * @return the eleveConcerne
	 */
	public Eleves getEleveConcerne() {
		return eleveConcerne;
	}



	/**
	 * @param eleveConcerne the eleveConcerne to set
	 */
	public void setEleveConcerne(Eleves eleveConcerne) {
		this.eleveConcerne = eleveConcerne;
	}



	/**
	 * @return the periodeConcerne
	 */
	public Periodes getPeriodeConcerne() {
		return periodeConcerne;
	}



	/**
	 * @param periodeConcerne the periodeConcerne to set
	 */
	public void setPeriodeConcerne(Periodes periodeConcerne) {
		this.periodeConcerne = periodeConcerne;
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
	
	public String getSanctionDiscDecisionConseilString(String lang){
		if(this.sanctionDiscAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch= this.sanctionDiscAssocie.getIntituleSancDisc()+" ("+
					this.sanctionDiscAssocie.getCodeSancDisc()+")";
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
		else{
			String ch=this.sanctionDiscAssocie.getIntituleSancDiscEn()+" ("+
					this.sanctionDiscAssocie.getCodeSancDiscEn()+")";
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
	}
		
	public String getSanctionDiscDecisionConseilStringCode(String lang){

		if(this.sanctionDiscAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch= this.sanctionDiscAssocie.getCodeSancDisc();
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
		else{
			String ch=this.sanctionDiscAssocie.getCodeSancDiscEn();
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
	
	}
	
	public String getSanctionDiscDecisionConseilStringIntitule(String lang){

		if(this.sanctionDiscAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch= this.sanctionDiscAssocie.getIntituleSancDisc();
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
		else{
			String ch=this.sanctionDiscAssocie.getIntituleSancDiscEn();
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			return ch;
		}
	
	}
		
	public String getSanctionTravDecisionConseilString(String lang){
			if(this.sanctionTravAssocie == null) return " ";
			if(lang.equalsIgnoreCase("fr")==true){
				String ch = this.sanctionTravAssocie.getIntituleSancTrav()+" ("+
						this.sanctionTravAssocie.getCodeSancTrav()+")";
				
				return ch;
			}
			else{
				String ch=this.sanctionTravAssocie.getIntituleSancTravEn()+" ("+
						this.sanctionTravAssocie.getCodeSancTravEn()+")";
				return ch;
			}
	}
	
	public String getSanctionTravDecisionConseilStringCode(String lang){

		if(this.sanctionTravAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch = this.sanctionTravAssocie.getCodeSancTrav();
			
			return ch;
		}
		else{
			String ch=this.sanctionTravAssocie.getCodeSancTravEn();
			return ch;
		}

	}
	
	public String getSanctionTravDecisionConseilStringIntitule(String lang){

		if(this.sanctionTravAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch = this.sanctionTravAssocie.getIntituleSancTrav();
			
			return ch;
		}
		else{
			String ch=this.sanctionTravAssocie.getIntituleSancTravEn();
			return ch;
		}

	}
	
	public String getDecisionDecisionConseilString(String lang){
		if(this.decisionAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			return this.decisionAssocie.getIntituleDecision()+" ("+
					this.decisionAssocie.getCodeDecision()+")";
		}
		else{
			return this.decisionAssocie.getIntituleDecisionEn()+" ("+
					this.decisionAssocie.getCodeDecisionEn()+")";
		}
	
	}
	
	public String getDecisionDecisionConseilStringCode(String lang){

		if(this.decisionAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			return this.decisionAssocie.getCodeDecision();
		}
		else{
			return this.decisionAssocie.getCodeDecisionEn();
		}
	
	}
	
	public String getDecisionDecisionConseilStringIntitule(String lang){

		if(this.decisionAssocie == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			return this.decisionAssocie.getIntituleDecision();
		}
		else{
			return this.decisionAssocie.getIntituleDecisionEn();
		}
	
	}
	
	


}
