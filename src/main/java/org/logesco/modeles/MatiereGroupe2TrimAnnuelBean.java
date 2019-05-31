/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class MatiereGroupe2TrimAnnuelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	// Informations sur le groupe de matieres
    private String matiere_g2;
    private String prof_g2;

    // Informations sur une ligne de matière
    private Double note_1_g2;
    private Double note_2_g2;
    private Double note_trim_g2;
    private Double total_trim_g2;
    private Double coef_g2;
    private String extreme_g2;
    private String rang_g2;
    private Double moy_classe_g2;
    private String pourcentage_g2;
    private String appreciation_g2;
	


	/**
	 * 
	 */
	public MatiereGroupe2TrimAnnuelBean() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the matiere_g2
	 */
	public String getMatiere_g2() {
		return matiere_g2;
	}



	/**
	 * @param matiere_g2 the matiere_g2 to set
	 */
	public void setMatiere_g2(String matiere_g2) {
		this.matiere_g2 = matiere_g2;
	}



	/**
	 * @return the prof_g2
	 */
	public String getProf_g2() {
		return prof_g2;
	}



	/**
	 * @param prof_g2 the prof_g2 to set
	 */
	public void setProf_g2(String prof_g2) {
		this.prof_g2 = prof_g2;
	}



	/**
	 * @return the note_1_g2
	 */
	public Double getNote_1_g2() {
		return note_1_g2;
	}



	/**
	 * @param note_1_g2 the note_1_g2 to set
	 */
	public void setNote_1_g2(Double note_1_g2) {
		if(note_1_g2>=0){
        	this.note_1_g2 = note_1_g2;
	     }
	     else{
	        	this.note_1_g2 = null;
	      }
	}



	/**
	 * @return the note_2_g2
	 */
	public Double getNote_2_g2() {
		return note_2_g2;
	}



	/**
	 * @param note_2_g2 the note_2_g2 to set
	 */
	public void setNote_2_g2(Double note_2_g2) {
		if(note_2_g2>=0){
        	this.note_2_g2 = note_2_g2;
	     }
	     else{
	        	this.note_2_g2 = null;
	      }
	}



	/**
	 * @return the note_trim_g2
	 */
	public Double getNote_trim_g2() {
		return note_trim_g2;
	}



	/**
	 * @param note_trim_g2 the note_trim_g2 to set
	 */
	public void setNote_trim_g2(Double note_trim_g2) {
		if(note_trim_g2>=0){
        	this.note_trim_g2 = note_trim_g2;
	     }
	     else{
	        	this.note_trim_g2 = null;
	      }
	}



	/**
	 * @return the total_trim_g2
	 */
	public Double getTotal_trim_g2() {
		if(this.getNote_trim_g2() == null) return null;
    	if(this.getNote_trim_g2()>=0) total_trim_g2 = getNote_trim_g2()*getCoef_g2();
		return  total_trim_g2;
	}



	/**
	 * @param total_trim_g2 the total_trim_g2 to set
	 */
	public void setTotal_trim_g2(Double total_trim_g2) {
		if(total_trim_g2>=0){
        	this.total_trim_g2 = total_trim_g2;
        }
        else{
        	this.total_trim_g2 = null;
        }
	}



	/**
	 * @return the coef_g2
	 */
	public Double getCoef_g2() {
		return coef_g2;
	}



	/**
	 * @param coef_g2 the coef_g2 to set
	 */
	public void setCoef_g2(Double coef_g2) {
		if(coef_g2>0) {
    		this.coef_g2 = coef_g2;
    	}
    	else{
    		this.coef_g2 = null;
    	}
	}



	/**
	 * @return the extreme_g2
	 */
	public String getExtreme_g2() {
		return extreme_g2;
	}



	/**
	 * @param extreme_g2 the extreme_g2 to set
	 */
	public void setExtreme_g2(String extreme_g2) {
		this.extreme_g2 = extreme_g2;
	}



	/**
	 * @return the rang_g2
	 */
	public String getRang_g2() {
		return rang_g2;
	}



	/**
	 * @param rang_g2 the rang_g2 to set
	 */
	public void setRang_g2(String rang_g2) {
		this.rang_g2 = rang_g2;
	}



	/**
	 * @return the moy_classe_g2
	 */
	public Double getMoy_classe_g2() {
		return moy_classe_g2;
	}



	/**
	 * @param moy_classe_g2 the moy_classe_g2 to set
	 */
	public void setMoy_classe_g2(Double moy_classe_g2) {
		if(moy_classe_g2>=0){
        	this.moy_classe_g2 = moy_classe_g2;
        }
        else{
        	this.moy_classe_g2 = null;
        }
	}



	/**
	 * @return the pourcentage_g2
	 */
	public String getPourcentage_g2() {
		return pourcentage_g2;
	}



	/**
	 * @param pourcentage_g2 the pourcentage_g2 to set
	 */
	public void setPourcentage_g2(double pourcentage_g2) {
		if(pourcentage_g2>=0){
        	this.pourcentage_g2 = pourcentage_g2+" %";
	    }
	    else{
	      	this.pourcentage_g2 = null;
	    }
	}



	/**
	 * @return the appreciation_g2
	 */
	public String getAppreciation_g2() {
		return appreciation_g2;
	}



	/**
	 * @param appreciation_g2 the appreciation_g2 to set
	 */
	public void setAppreciation_g2(String appreciation_g2) {
		this.appreciation_g2 = appreciation_g2;
	}

	
	
}
