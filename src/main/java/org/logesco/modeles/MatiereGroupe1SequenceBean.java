package org.logesco.modeles;

public class MatiereGroupe1SequenceBean {

    // Informations sur une ligne de cours
    private String matiere_g1;
    private String matiere_g1_2emelang;
    private String prof_g1;
 
    // Informations sur une ligne de matière
    private Double note_seq_g1;
    private Double total_seq_g1;
    private Double coef_g1;
    private String extreme_g1;
    private String rang_g1;
    private Double moy_classe_g1;
    private String pourcentage_g1;
    //private Double pourcentage_g1;
    private String appreciation_g1;
    

    public MatiereGroupe1SequenceBean() {
    }

   
    public String getMatiere_g1() {
        return matiere_g1;
    }

    public void setMatiere_g1(String matiere_g1) {
        this.matiere_g1 = matiere_g1;
    }

    public String getProf_g1() {
        return prof_g1;
    }

    public void setProf_g1(String prof_g1) {
        this.prof_g1 = prof_g1;
    }
    
   
    public Double getCoef_g1() {
        return coef_g1;
    }

    public void setCoef_g1(Double coef_g1) {
    	if(coef_g1>0) {
    		this.coef_g1 = coef_g1;
    	}
    	else{
    		this.coef_g1 = null;
    	}
    }

    public String getExtreme_g1() {
        return extreme_g1;
    }

    public void setExtreme_g1(String extreme_g1) {
        this.extreme_g1 = extreme_g1;
    }

    public String getRang_g1() {
        return rang_g1;
    }

    public void setRang_g1(String rang_g1) {
        this.rang_g1 = rang_g1;
    }

    public Double getMoy_classe_g1() {
        return moy_classe_g1;
    }

    public void setMoy_classe_g1(Double moy_classe_g1) {
        
        if(moy_classe_g1>0){
        	this.moy_classe_g1 = moy_classe_g1;
        }
        else{
        	this.moy_classe_g1 = null;
        }
    }

    public String getPourcentage_g1() {
        return pourcentage_g1;
    }

    public void setPourcentage_g1(Double pourcentage_g1) {
        
        if(pourcentage_g1>=0){
        	this.pourcentage_g1 = pourcentage_g1+" %";
        }
        else{
        	this.pourcentage_g1 = null;
        }
    }

    public String getAppreciation_g1() {
        return appreciation_g1;
    }

    public void setAppreciation_g1(String appreciation_g1) {
        this.appreciation_g1 = appreciation_g1;
    }

    public Double getNote_seq_g1() {
        return note_seq_g1;
    }

    public void setNote_seq_g1(Double note_seq_g1) {
        
        if(note_seq_g1>=0){
        	this.note_seq_g1 = note_seq_g1;
        }
        else{
        	this.note_seq_g1 = null;
        }
    }

    public Double getTotal_seq_g1() {
    	if(this.getNote_seq_g1() == null) return null;
    	if(this.getNote_seq_g1()>=0) total_seq_g1 = getNote_seq_g1()*getCoef_g1();
		return  total_seq_g1;
    }

    public void setTotal_seq_g1(Double total_seq_g1) {
        
        if(total_seq_g1>=0){
        	this.total_seq_g1 = total_seq_g1;
        }
        else{
        	this.total_seq_g1 = null;
        }
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

   
    
    
    
    

}
