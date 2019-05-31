package org.logesco.modeles;

public class MatiereGroupe3SequenceBean {

    // Informations sur le groupe de matieres
    private String matiere_g3;
    private String prof_g3;

    // Informations sur une ligne de matière
    private Double note_seq_g3;
    private Double total_seq_g3;
    private Double coef_g3;
    private String extreme_g3;
    private String rang_g3;
    private Double moy_classe_g3;
    private String pourcentage_g3;
    private String appreciation_g3;
    

    // Constructeur de classe
    public MatiereGroupe3SequenceBean() {
    }

    // Accesseurs et modificateurs
    public String getMatiere_g3() {
        return matiere_g3;
    }

    public void setMatiere_g3(String matiere_g3) {
        this.matiere_g3 = matiere_g3;
    }

    public String getProf_g3() {
        return prof_g3;
    }

    public void setProf_g3(String prof_g3) {
        this.prof_g3 = prof_g3;
    }

    public Double getCoef_g3() {
        return coef_g3;
    }

    public void setCoef_g3(Double coef_g3) {
        if(coef_g3>0) {
    		this.coef_g3 = coef_g3;
    	}
    	else{
    		this.coef_g3 = null;
    	}
    }

    public String getExtreme_g3() {
        return extreme_g3;
    }

    public void setExtreme_g3(String extreme_g3) {
        this.extreme_g3 = extreme_g3;
    }

    public String getRang_g3() {
        return rang_g3;
    }

    public void setRang_g3(String rang_g3) {
        this.rang_g3 = rang_g3;
    }

    public Double getMoy_classe_g3() {
        return moy_classe_g3;
    }

    public void setMoy_classe_g3(Double moy_classe_g3) {
        if(moy_classe_g3>0){
        	this.moy_classe_g3 = moy_classe_g3;
        }
        else{
        	this.moy_classe_g3 = null;
        }
    }

    public String getPourcentage_g3() {
        return pourcentage_g3;
    }

    public void setPourcentage_g3(Double pourcentage_g3) {
        if(pourcentage_g3>=0){
        	this.pourcentage_g3 = pourcentage_g3+" %";
        }
        else{
        	this.pourcentage_g3 = null;
        }
    }

    public String getAppreciation_g3() {
        return appreciation_g3;
    }

    public void setAppreciation_g3(String appreciation_g3) {
        this.appreciation_g3 = appreciation_g3;
    }

    public Double getNote_seq_g3() {
        return note_seq_g3;
    }

    public void setNote_seq_g3(Double note_seq_g3) {
       if(note_seq_g3>0){
        	this.note_seq_g3 = note_seq_g3;
        }
        else{
        	this.note_seq_g3 = null;
        }
    }

    public Double getTotal_seq_g3() {
        if(this.getNote_seq_g3() == null) return null;
    	if(this.getNote_seq_g3()>=0) total_seq_g3 = getNote_seq_g3()*getCoef_g3();
		return  total_seq_g3;
    }

    public void setTotal_seq_g3(Double total_seq_g3) {
       if(total_seq_g3>0){
        	this.total_seq_g3 = total_seq_g3;
        }
        else{
        	this.total_seq_g3 = null;
        }
    }
    

}
