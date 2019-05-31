/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class MatiereGroupe3TrimestreBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 // Informations sur le groupe de matieres
    private String matiere_g3;
    private String prof_g3;

    // Informations sur une ligne de matière
    private Double note_1_g3;
    private Double note_2_g3;
    private Double note_trim_g3;
    private Double total_trim_g3;
    private Double coef_g3;
    private String extreme_g3;
    private String rang_g3;
    private Double moy_classe_g3;
    private String pourcentage_g3;
    private String appreciation_g3;
	

	/**
	 * 
	 */
	public MatiereGroupe3TrimestreBean() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the matiere_g3
	 */
	public String getMatiere_g3() {
		return matiere_g3;
	}


	/**
	 * @param matiere_g3 the matiere_g3 to set
	 */
	public void setMatiere_g3(String matiere_g3) {
		this.matiere_g3 = matiere_g3;
	}


	/**
	 * @return the prof_g3
	 */
	public String getProf_g3() {
		return prof_g3;
	}


	/**
	 * @param prof_g3 the prof_g3 to set
	 */
	public void setProf_g3(String prof_g3) {
		this.prof_g3 = prof_g3;
	}


	/**
	 * @return the note_1_g3
	 */
	public Double getNote_1_g3() {
		return note_1_g3;
	}


	/**
	 * @param note_1_g3 the note_1_g3 to set
	 */
	public void setNote_1_g3(Double note_1_g3) {
		if(note_1_g3>=0){
        	this.note_1_g3 = note_1_g3;
	     }
	     else{
	        	this.note_1_g3 = null;
	      }
	}


	/**
	 * @return the note_2_g3
	 */
	public Double getNote_2_g3() {
		return note_2_g3;
	}


	/**
	 * @param note_2_g3 the note_2_g3 to set
	 */
	public void setNote_2_g3(Double note_2_g3) {
		if(note_2_g3>=0){
        	this.note_2_g3 = note_2_g3;
	     }
	     else{
	        	this.note_2_g3 = null;
	      }
	}


	/**
	 * @return the note_trim_g3
	 */
	public Double getNote_trim_g3() {
		return note_trim_g3;
	}


	/**
	 * @param note_trim_g3 the note_trim_g3 to set
	 */
	public void setNote_trim_g3(Double note_trim_g3) {
		if(note_trim_g3>=0){
        	this.note_trim_g3 = note_trim_g3;
	     }
	     else{
	        	this.note_trim_g3 = null;
	      }
	}


	/**
	 * @return the total_trim_g3
	 */
	public Double getTotal_trim_g3() {
		if(this.getNote_trim_g3() == null) return null;
    	if(this.getNote_trim_g3()>=0) total_trim_g3 = getNote_trim_g3()*getCoef_g3();
		return  total_trim_g3;
	}


	/**
	 * @param total_trim_g3 the total_trim_g3 to set
	 */
	public void setTotal_trim_g3(Double total_trim_g3) {
		if(total_trim_g3>=0){
        	this.total_trim_g3 = total_trim_g3;
        }
        else{
        	this.total_trim_g3 = null;
        }
	}


	/**
	 * @return the coef_g3
	 */
	public Double getCoef_g3() {
		return coef_g3;
	}


	/**
	 * @param coef_g3 the coef_g3 to set
	 */
	public void setCoef_g3(Double coef_g3) {
		if(coef_g3>0) {
    		this.coef_g3 = coef_g3;
    	}
    	else{
    		this.coef_g3 = null;
    	}
	}


	/**
	 * @return the extreme_g3
	 */
	public String getExtreme_g3() {
		return extreme_g3;
	}


	/**
	 * @param extreme_g3 the extreme_g3 to set
	 */
	public void setExtreme_g3(String extreme_g3) {
		this.extreme_g3 = extreme_g3;
	}


	/**
	 * @return the rang_g3
	 */
	public String getRang_g3() {
		return rang_g3;
	}


	/**
	 * @param rang_g3 the rang_g3 to set
	 */
	public void setRang_g3(String rang_g3) {
		this.rang_g3 = rang_g3;
	}


	/**
	 * @return the moy_classe_g3
	 */
	public Double getMoy_classe_g3() {
		return moy_classe_g3;
	}


	/**
	 * @param moy_classe_g3 the moy_classe_g3 to set
	 */
	public void setMoy_classe_g3(Double moy_classe_g3) {
		if(moy_classe_g3>0){
        	this.moy_classe_g3 = moy_classe_g3;
        }
        else{
        	this.moy_classe_g3 = null;
        }
	}


	/**
	 * @return the pourcentage_g3
	 */
	public String getPourcentage_g3() {
		return pourcentage_g3;
	}


	/**
	 * @param pourcentage_g3 the pourcentage_g3 to set
	 */
	public void setPourcentage_g3(double pourcentage_g3) {
		if(pourcentage_g3>=0){
        	this.pourcentage_g3 = pourcentage_g3+" %";
        }
        else{
        	this.pourcentage_g3 = null;
        }
	}


	/**
	 * @return the appreciation_g3
	 */
	public String getAppreciation_g3() {
		return appreciation_g3;
	}


	/**
	 * @param appreciation_g3 the appreciation_g3 to set
	 */
	public void setAppreciation_g3(String appreciation_g3) {
		this.appreciation_g3 = appreciation_g3;
	}
	
	
	
	

}
