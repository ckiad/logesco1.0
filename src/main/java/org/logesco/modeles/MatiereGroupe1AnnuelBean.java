/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class MatiereGroupe1AnnuelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Informations sur le groupe de matieres
    private String matiere_g1;
    private String matiere_g1_2emelang;
    private String prof_g1;

    // Informations sur une ligne de matière
    private Double note_1_g1;
    private Double note_2_g1;
    private Double note_3_g1;
    private Double note_ann_g1;
    private Double total_ann_g1;
    private Double coef_g1;
    private String extreme_g1;
    private String rang_g1;
    private Double moy_classe_g1;
    private String pourcentage_g1;
    private String appreciation_g1;
    


	/**
	 * 
	 */
	public MatiereGroupe1AnnuelBean() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * @return the matiere_g1
	 */
	public String getMatiere_g1() {
		return matiere_g1;
	}



	/**
	 * @param matiere_g1 the matiere_g1 to set
	 */
	public void setMatiere_g1(String matiere_g1) {
		this.matiere_g1 = matiere_g1;
	}


	
	

	/**
	 * @return the matiere_g1_2emelang
	 */
	public String getMatiere_g1_2emelang() {
		return matiere_g1_2emelang;
	}



	/**
	 * @param matiere_g1_2emelang the matiere_g1_2emelang to set
	 */
	public void setMatiere_g1_2emelang(String matiere_g1_2emelang) {
		this.matiere_g1_2emelang = matiere_g1_2emelang;
	}



	/**
	 * @param pourcentage_g1 the pourcentage_g1 to set
	 */
	public void setPourcentage_g1(String pourcentage_g1) {
		this.pourcentage_g1 = pourcentage_g1;
	}



	/**
	 * @return the prof_g1
	 */
	public String getProf_g1() {
		return prof_g1;
	}



	/**
	 * @param prof_g1 the prof_g1 to set
	 */
	public void setProf_g1(String prof_g1) {
		this.prof_g1 = prof_g1;
	}



	/**
	 * @return the note_1_g1
	 */
	public Double getNote_1_g1() {
		return note_1_g1;
	}



	/**
	 * @param note_1_g1 the note_1_g1 to set
	 */
	public void setNote_1_g1(Double note_1_g1) {
		if(note_1_g1>=0){
        	this.note_1_g1 = note_1_g1;
	     }
	     else{
	        	this.note_1_g1 = null;
	      }
	}



	/**
	 * @return the note_2_g1
	 */
	public Double getNote_2_g1() {
		return note_2_g1;
	}



	/**
	 * @param note_2_g1 the note_2_g1 to set
	 */
	public void setNote_2_g1(Double note_2_g1) {
		if(note_2_g1>=0){
        	this.note_2_g1 = note_2_g1;
	     }
	     else{
	        	this.note_2_g1 = null;
	      }
	}



	/**
	 * @return the note_3_g1
	 */
	public Double getNote_3_g1() {
		return note_3_g1;
	}



	/**
	 * @param note_3_g1 the note_3_g1 to set
	 */
	public void setNote_3_g1(Double note_3_g1) {
		if(note_3_g1>=0){
        	this.note_3_g1 = note_3_g1;
	     }
	     else{
	        	this.note_3_g1 = null;
	      }
	}



	/**
	 * @return the note_ann_g1
	 */
	public Double getNote_ann_g1() {
		return note_ann_g1;
	}



	/**
	 * @param note_ann_g1 the note_ann_g1 to set
	 */
	public void setNote_ann_g1(Double note_ann_g1) {
		if(note_ann_g1>=0){
        	this.note_ann_g1 = note_ann_g1;
	     }
	     else{
	        	this.note_ann_g1 = null;
	      }
	}



	/**
	 * @return the total_ann_g1
	 */
	public Double getTotal_ann_g1() {
		if(this.getNote_ann_g1() == null) return null;
    	if(this.getNote_ann_g1()>=0) total_ann_g1 = getNote_ann_g1()*getCoef_g1();
		return  total_ann_g1;
	}



	/**
	 * @param total_ann_g1 the total_ann_g1 to set
	 */
	public void setTotal_ann_g1(Double total_ann_g1) {
		if(total_ann_g1>=0){
        	this.total_ann_g1 = total_ann_g1;
        }
        else{
        	this.total_ann_g1 = null;
        }
	}



	/**
	 * @return the coef_g1
	 */
	public Double getCoef_g1() {
		return coef_g1;
	}



	/**
	 * @param coef_g1 the coef_g1 to set
	 */
	public void setCoef_g1(Double coef_g1) {
		if(coef_g1>0) {
    		this.coef_g1 = coef_g1;
    	}
    	else{
    		this.coef_g1 = null;
    	}
	}



	/**
	 * @return the extreme_g1
	 */
	public String getExtreme_g1() {
		return extreme_g1;
	}



	/**
	 * @param extreme_g1 the extreme_g1 to set
	 */
	public void setExtreme_g1(String extreme_g1) {
		this.extreme_g1 = extreme_g1;
	}



	/**
	 * @return the rang_g1
	 */
	public String getRang_g1() {
		return rang_g1;
	}



	/**
	 * @param rang_g1 the rang_g1 to set
	 */
	public void setRang_g1(String rang_g1) {
		this.rang_g1 = rang_g1;
	}



	/**
	 * @return the moy_classe_g1
	 */
	public Double getMoy_classe_g1() {
		return moy_classe_g1;
	}



	/**
	 * @param moy_classe_g1 the moy_classe_g1 to set
	 */
	public void setMoy_classe_g1(Double moy_classe_g1) {
		if(moy_classe_g1>=0){
        	this.moy_classe_g1 = moy_classe_g1;
        }
        else{
        	this.moy_classe_g1 = null;
        }
	}



	/**
	 * @return the pourcentage_g1
	 */
	public String getPourcentage_g1() {
		return pourcentage_g1;
	}



	/**
	 * @param pourcentage_g1 the pourcentage_g1 to set
	 */
	public void setPourcentage_g1(double pourcentage_g1) {
		if(pourcentage_g1>=0){
        	this.pourcentage_g1 = pourcentage_g1+" %";
	    }
	    else{
	      	this.pourcentage_g1 = null;
	    }
	}



	/**
	 * @return the appreciation_g1
	 */
	public String getAppreciation_g1() {
		return appreciation_g1;
	}



	/**
	 * @param appreciation_g1 the appreciation_g1 to set
	 */
	public void setAppreciation_g1(String appreciation_g1) {
		this.appreciation_g1 = appreciation_g1;
	}
	
	
	

}
