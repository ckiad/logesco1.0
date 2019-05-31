package org.logesco.modeles;

public class MatiereGroupe2SequenceBean {

    // Informations sur le groupe de matieres
    private String matiere_g2;
    private String prof_g2;

    // Informations sur une ligne de matière
    private Double note_seq_g2;
    private Double total_seq_g2;
    private Double coef_g2;
    private String extreme_g2;
    private String rang_g2;
    private Double moy_classe_g2;
    private String pourcentage_g2;
    private String appreciation_g2;

    
    // Constructeur de classe
    public MatiereGroupe2SequenceBean() {}

    // Accesseurs et modificateurs
    public String getMatiere_g2() {
        return matiere_g2;
    }

    public void setMatiere_g2(String matiere_g2) {
        this.matiere_g2 = matiere_g2;
    }

    public String getProf_g2() {
        return prof_g2;
    }

    public void setProf_g2(String prof_g2) {
        this.prof_g2 = prof_g2;
    }

    
    public double getCoef_g2() {
        return coef_g2;
    }

    public void setCoef_g2(Double coef_g2) {
        if(coef_g2>0) {
    		this.coef_g2 = coef_g2;
    	}
    	else{
    		this.coef_g2 = null;
    	}
    }

    public String getExtreme_g2() {
        return extreme_g2;
    }

    public void setExtreme_g2(String extreme_g2) {
        this.extreme_g2 = extreme_g2;
    }

    public String getRang_g2() {
        return rang_g2;
    }

    public void setRang_g2(String rang_g2) {
        this.rang_g2 = rang_g2;
    }

    public Double getMoy_classe_g2() {
        return moy_classe_g2;
    }

    public void setMoy_classe_g2(Double moy_classe_g2) {
        if(moy_classe_g2>0){
        	this.moy_classe_g2 = moy_classe_g2;
        }
        else{
        	this.moy_classe_g2 = null;
        }
    }

    public String getPourcentage_g2() {
        return pourcentage_g2;
    }

    public void setPourcentage_g2(double pourcentage_g2) {
        if(pourcentage_g2>=0){
        	this.pourcentage_g2 = pourcentage_g2+" %";
        }
        else{
        	this.pourcentage_g2 = null;
        }
    }

    public String getAppreciation_g2() {
        return appreciation_g2;
    }

    public void setAppreciation_g2(String appreciation_g2) {
        this.appreciation_g2 = appreciation_g2;
    }

    public Double getNote_seq_g2() {
        return note_seq_g2;
    }

    public void setNote_seq_g2(Double note_seq_g2) {
        if(note_seq_g2>0){
        	this.note_seq_g2 = note_seq_g2;
        }
        else{
        	this.note_seq_g2 = null;
        }
    }

    public Double getTotal_seq_g2() {
       if(this.getNote_seq_g2() == null) return null;
    	if(this.getNote_seq_g2()>=0) total_seq_g2 = getNote_seq_g2()*getCoef_g2();
		return  total_seq_g2;
    }

    public void setTotal_seq_g2(Double total_seq_g2) {
        if(total_seq_g2>0){
        	this.total_seq_g2 = total_seq_g2;
        }
        else{
        	this.total_seq_g2 = null;
        }
    }

    
    
    
    
	
}
