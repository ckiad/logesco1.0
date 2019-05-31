/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;
import java.util.List;

/**
 * @author cedrickiadjeu
 *
 */
public class BulletinTrimAnnuelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Informations d'entete du bulletin
    private String ministere_fr;
    private String ministere_en;
    private String delegation_fr;
    private String delegation_en;
    private String etablissement_fr;
    private String etablissement_en;
    private String adresse;
    private String annee_scolaire_fr;
    private String annee_scolaire_en;
    private String devise_fr;
    private String devise_en;
    private String titre_bulletin;

    // Informations sur les labels d'entete des notes
    private String label_note_1;
    private String label_note_2;
    private String label_trimestre;
    private String label_trim_x_coef;

    // Informations personnelles de l'eleve
    private String nom_eleve;
    private String prenom_eleve;
    private String date_naissance_eleve;
    private String lieu_naissance_eleve;
    private String sexe;
    private String classe_eleve;
    private String prof_principal;
    private String matricule_eleve;
    private String numero;
    private int effectif_classe;
    private int effectif_presente;
    private String redoublant;
    private String photo;

    //  Listes alimentant les sous-rapports
    private List<MatiereGroupe1TrimAnnuelBean> matieresGroupe1TrimAnnuel;
    private List<MatiereGroupe2TrimAnnuelBean> matieresGroupe2TrimAnnuel;
    private List<MatiereGroupe3TrimAnnuelBean> matieresGroupe3TrimAnnuel;

    // Informations liées au sommaire de chaque groupe de matière
    // Premier groupe de matière
    private String nom_g1;
    private double total_coef_g1;
    private double total_g1;
    private String total_extreme_g1;
    private String total_rang_g1;
    private double mg_classe_g1;
    private String total_pourcentage_g1;
    private double moyenne_g1;

    // Deuxième groupe de matière
    private String nom_g2;
    private double total_coef_g2;
    private double total_g2;
    private String total_extreme_g2;
    private String total_rang_g2;
    private double mg_classe_g2;
    private String total_pourcentage_g2;
    private double moyenne_g2;
    
    // Troisième groupe de matière
    private String nom_g3;
    private double total_coef_g3;
    private double total_g3;
    private String total_extreme_g3;
    private String total_rang_g3;
    private double mg_classe_g3;
    private String total_pourcentage_g3;
    private double moyenne_g3;
    
    // Informations sur les totaux
    private double total_coef;
    private double total_points;

    // Informations sur le résultat trimestriel de l'élève
    private double result_tt_points;
    private double result_tt_coef;
    private String result_rang_trim;
    
    private double moy_an;
    private String rang_an;

    // Informations sur le profil de la classe
    private double moy_premier;
    private double moy_dernier;
    private int nbre_moyennes;
    private double taux_reussite;
    private double t_reussite_an;
    private double moy_gen_classe;
    private double moy_gen_classe_an;

    // Informations sur la conduite trimestrielle de l'eleve
    private int absence_NJ;
    private int absence_J;
    private String consigne;
    private String exclusion;
    private String avertissement;
    private String blame_conduite;
    private String rapport_disc1;
    private String rapport_disc2;
    private String rapport_disc3;

    // Informations sur les rappels des moyennes des sequences antérieures
    private String rappel_1;
    private String rappel_2;
    private String rappel_3;
    private String rappel_4;
    private String rappel_5;
    private double r_moy_1;
    private double r_moy_2;
    private double r_moy_3;
    private double r_moy_4;
    private double r_moy_5;
    private String r_rang_1;
    private String r_rang_2;
    private String r_rang_3;
    private String r_rang_4;
    private String r_rang_5;
   
    private String rappel_an;
    private String r_rang_an;
    private double r_moy_an;

    // Informations sur l'appreciation du travail de l'eleve
    private String appreciation;
    private String avertissement_travail;
    private String blame_travail;
    private String tableau_hon;
    private String tableau_enc;
    private String tableau_fel;
    private String distinction;

    // Décision du conseil de classe
    private String decision_conseil;
    private String effort_matiere1;
    private String effort_matiere2;
    private String effort_matiere3;

    // Informations de l'espace visa
    private String ville;




	/**
	 * 
	 */
	public BulletinTrimAnnuelBean() {
		// TODO Auto-generated constructor stub
	}




	/**
	 * @return the ministere_fr
	 */
	public String getMinistere_fr() {
		return ministere_fr;
	}




	/**
	 * @param ministere_fr the ministere_fr to set
	 */
	public void setMinistere_fr(String ministere_fr) {
		this.ministere_fr = ministere_fr;
	}




	/**
	 * @return the ministere_en
	 */
	public String getMinistere_en() {
		return ministere_en;
	}




	/**
	 * @param ministere_en the ministere_en to set
	 */
	public void setMinistere_en(String ministere_en) {
		this.ministere_en = ministere_en;
	}




	/**
	 * @return the delegation_fr
	 */
	public String getDelegation_fr() {
		return delegation_fr;
	}




	/**
	 * @param delegation_fr the delegation_fr to set
	 */
	public void setDelegation_fr(String delegation_fr) {
		this.delegation_fr = delegation_fr;
	}




	/**
	 * @return the delegation_en
	 */
	public String getDelegation_en() {
		return delegation_en;
	}




	/**
	 * @param delegation_en the delegation_en to set
	 */
	public void setDelegation_en(String delegation_en) {
		this.delegation_en = delegation_en;
	}




	/**
	 * @return the etablissement_fr
	 */
	public String getEtablissement_fr() {
		return etablissement_fr;
	}




	/**
	 * @param etablissement_fr the etablissement_fr to set
	 */
	public void setEtablissement_fr(String etablissement_fr) {
		this.etablissement_fr = etablissement_fr;
	}




	/**
	 * @return the etablissement_en
	 */
	public String getEtablissement_en() {
		return etablissement_en;
	}




	/**
	 * @param etablissement_en the etablissement_en to set
	 */
	public void setEtablissement_en(String etablissement_en) {
		this.etablissement_en = etablissement_en;
	}




	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}




	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}




	/**
	 * @return the annee_scolaire_fr
	 */
	public String getAnnee_scolaire_fr() {
		return annee_scolaire_fr;
	}




	/**
	 * @param annee_scolaire_fr the annee_scolaire_fr to set
	 */
	public void setAnnee_scolaire_fr(String annee_scolaire_fr) {
		this.annee_scolaire_fr = annee_scolaire_fr;
	}




	/**
	 * @return the annee_scolaire_en
	 */
	public String getAnnee_scolaire_en() {
		return annee_scolaire_en;
	}




	/**
	 * @param annee_scolaire_en the annee_scolaire_en to set
	 */
	public void setAnnee_scolaire_en(String annee_scolaire_en) {
		this.annee_scolaire_en = annee_scolaire_en;
	}




	/**
	 * @return the devise_fr
	 */
	public String getDevise_fr() {
		return devise_fr;
	}




	/**
	 * @param devise_fr the devise_fr to set
	 */
	public void setDevise_fr(String devise_fr) {
		this.devise_fr = devise_fr;
	}




	/**
	 * @return the devise_en
	 */
	public String getDevise_en() {
		return devise_en;
	}




	/**
	 * @param devise_en the devise_en to set
	 */
	public void setDevise_en(String devise_en) {
		this.devise_en = devise_en;
	}




	/**
	 * @return the titre_bulletin
	 */
	public String getTitre_bulletin() {
		return titre_bulletin;
	}




	/**
	 * @param titre_bulletin the titre_bulletin to set
	 */
	public void setTitre_bulletin(String titre_bulletin) {
		this.titre_bulletin = titre_bulletin;
	}




	/**
	 * @return the label_note_1
	 */
	public String getLabel_note_1() {
		return label_note_1;
	}




	/**
	 * @param label_note_1 the label_note_1 to set
	 */
	public void setLabel_note_1(String label_note_1) {
		this.label_note_1 = label_note_1;
	}




	/**
	 * @return the label_note_2
	 */
	public String getLabel_note_2() {
		return label_note_2;
	}




	/**
	 * @param label_note_2 the label_note_2 to set
	 */
	public void setLabel_note_2(String label_note_2) {
		this.label_note_2 = label_note_2;
	}




	/**
	 * @return the label_trimestre
	 */
	public String getLabel_trimestre() {
		return label_trimestre;
	}




	/**
	 * @param label_trimestre the label_trimestre to set
	 */
	public void setLabel_trimestre(String label_trimestre) {
		this.label_trimestre = label_trimestre;
	}




	/**
	 * @return the label_trim_x_coef
	 */
	public String getLabel_trim_x_coef() {
		return label_trim_x_coef;
	}




	/**
	 * @param label_trim_x_coef the label_trim_x_coef to set
	 */
	public void setLabel_trim_x_coef(String label_trim_x_coef) {
		this.label_trim_x_coef = label_trim_x_coef;
	}




	/**
	 * @return the nom_eleve
	 */
	public String getNom_eleve() {
		return nom_eleve;
	}




	/**
	 * @param nom_eleve the nom_eleve to set
	 */
	public void setNom_eleve(String nom_eleve) {
		this.nom_eleve = nom_eleve;
	}




	/**
	 * @return the prenom_eleve
	 */
	public String getPrenom_eleve() {
		return prenom_eleve;
	}




	/**
	 * @param prenom_eleve the prenom_eleve to set
	 */
	public void setPrenom_eleve(String prenom_eleve) {
		this.prenom_eleve = prenom_eleve;
	}




	/**
	 * @return the date_naissance_eleve
	 */
	public String getDate_naissance_eleve() {
		return date_naissance_eleve;
	}




	/**
	 * @param date_naissance_eleve the date_naissance_eleve to set
	 */
	public void setDate_naissance_eleve(String date_naissance_eleve) {
		this.date_naissance_eleve = date_naissance_eleve;
	}




	/**
	 * @return the lieu_naissance_eleve
	 */
	public String getLieu_naissance_eleve() {
		return lieu_naissance_eleve;
	}




	/**
	 * @param lieu_naissance_eleve the lieu_naissance_eleve to set
	 */
	public void setLieu_naissance_eleve(String lieu_naissance_eleve) {
		this.lieu_naissance_eleve = lieu_naissance_eleve;
	}




	/**
	 * @return the sexe
	 */
	public String getSexe() {
		return sexe;
	}




	/**
	 * @param sexe the sexe to set
	 */
	public void setSexe(String sexe) {
		this.sexe = sexe;
	}




	/**
	 * @return the classe_eleve
	 */
	public String getClasse_eleve() {
		return classe_eleve;
	}




	/**
	 * @param classe_eleve the classe_eleve to set
	 */
	public void setClasse_eleve(String classe_eleve) {
		this.classe_eleve = classe_eleve;
	}




	/**
	 * @return the prof_principal
	 */
	public String getProf_principal() {
		return prof_principal;
	}




	/**
	 * @param prof_principal the prof_principal to set
	 */
	public void setProf_principal(String prof_principal) {
		this.prof_principal = prof_principal;
	}




	/**
	 * @return the matricule_eleve
	 */
	public String getMatricule_eleve() {
		return matricule_eleve;
	}




	/**
	 * @param matricule_eleve the matricule_eleve to set
	 */
	public void setMatricule_eleve(String matricule_eleve) {
		this.matricule_eleve = matricule_eleve;
	}




	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}




	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}




	/**
	 * @return the effectif_classe
	 */
	public int getEffectif_classe() {
		return effectif_classe;
	}




	/**
	 * @param effectif_classe the effectif_classe to set
	 */
	public void setEffectif_classe(int effectif_classe) {
		this.effectif_classe = effectif_classe;
	}




	/**
	 * @return the effectif_presente
	 */
	public int getEffectif_presente() {
		return effectif_presente;
	}




	/**
	 * @param effectif_presente the effectif_presente to set
	 */
	public void setEffectif_presente(int effectif_presente) {
		this.effectif_presente = effectif_presente;
	}




	/**
	 * @return the redoublant
	 */
	public String getRedoublant() {
		return redoublant;
	}




	/**
	 * @param redoublant the redoublant to set
	 */
	public void setRedoublant(String redoublant) {
		this.redoublant = redoublant;
	}




	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}




	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}


	

	/**
	 * @return the matieresGroupe1TrimAnnuel
	 */
	public List<MatiereGroupe1TrimAnnuelBean> getMatieresGroupe1TrimAnnuel() {
		return matieresGroupe1TrimAnnuel;
	}




	/**
	 * @param matieresGroupe1TrimAnnuel the matieresGroupe1TrimAnnuel to set
	 */
	public void setMatieresGroupe1TrimAnnuel(List<MatiereGroupe1TrimAnnuelBean> matieresGroupe1TrimAnnuel) {
		this.matieresGroupe1TrimAnnuel = matieresGroupe1TrimAnnuel;
	}




	/**
	 * @return the matieresGroupe2TrimAnnuel
	 */
	public List<MatiereGroupe2TrimAnnuelBean> getMatieresGroupe2TrimAnnuel() {
		return matieresGroupe2TrimAnnuel;
	}




	/**
	 * @param matieresGroupe2TrimAnnuel the matieresGroupe2TrimAnnuel to set
	 */
	public void setMatieresGroupe2TrimAnnuel(List<MatiereGroupe2TrimAnnuelBean> matieresGroupe2TrimAnnuel) {
		this.matieresGroupe2TrimAnnuel = matieresGroupe2TrimAnnuel;
	}




	/**
	 * @return the matieresGroupe3TrimAnnuel
	 */
	public List<MatiereGroupe3TrimAnnuelBean> getMatieresGroupe3TrimAnnuel() {
		return matieresGroupe3TrimAnnuel;
	}




	/**
	 * @param matieresGroupe3TrimAnnuel the matieresGroupe3TrimAnnuel to set
	 */
	public void setMatieresGroupe3TrimAnnuel(List<MatiereGroupe3TrimAnnuelBean> matieresGroupe3TrimAnnuel) {
		this.matieresGroupe3TrimAnnuel = matieresGroupe3TrimAnnuel;
	}




	/**
	 * @return the nom_g1
	 */
	public String getNom_g1() {
		return nom_g1;
	}




	/**
	 * @param nom_g1 the nom_g1 to set
	 */
	public void setNom_g1(String nom_g1) {
		this.nom_g1 = nom_g1;
	}




	/**
	 * @return the total_coef_g1
	 */
	public double getTotal_coef_g1() {
		return total_coef_g1;
	}




	/**
	 * @param total_coef_g1 the total_coef_g1 to set
	 */
	public void setTotal_coef_g1(double total_coef_g1) {
		this.total_coef_g1 = total_coef_g1;
	}




	/**
	 * @return the total_g1
	 */
	public double getTotal_g1() {
		return total_g1;
	}




	/**
	 * @param total_g1 the total_g1 to set
	 */
	public void setTotal_g1(double total_g1) {
		this.total_g1 = total_g1;
	}




	/**
	 * @return the total_extreme_g1
	 */
	public String getTotal_extreme_g1() {
		return total_extreme_g1;
	}




	/**
	 * @param total_extreme_g1 the total_extreme_g1 to set
	 */
	public void setTotal_extreme_g1(String total_extreme_g1) {
		this.total_extreme_g1 = total_extreme_g1;
	}




	/**
	 * @return the total_rang_g1
	 */
	public String getTotal_rang_g1() {
		return total_rang_g1;
	}




	/**
	 * @param total_rang_g1 the total_rang_g1 to set
	 */
	public void setTotal_rang_g1(String total_rang_g1) {
		this.total_rang_g1 = total_rang_g1;
	}




	/**
	 * @return the mg_classe_g1
	 */
	public double getMg_classe_g1() {
		return mg_classe_g1;
	}




	/**
	 * @param mg_classe_g1 the mg_classe_g1 to set
	 */
	public void setMg_classe_g1(double mg_classe_g1) {
		this.mg_classe_g1 = mg_classe_g1;
	}




	/**
	 * @return the total_pourcentage_g1
	 */
	public String getTotal_pourcentage_g1() {
		return total_pourcentage_g1;
	}




	/**
	 * @param total_pourcentage_g1 the total_pourcentage_g1 to set
	 */
	public void setTotal_pourcentage_g1(double total_pourcentage_g1) {
		this.total_pourcentage_g1 = total_pourcentage_g1+" %";
	}




	/**
	 * @return the moyenne_g1
	 */
	public double getMoyenne_g1() {
		return moyenne_g1;
	}




	/**
	 * @param moyenne_g1 the moyenne_g1 to set
	 */
	public void setMoyenne_g1(double moyenne_g1) {
		this.moyenne_g1 = moyenne_g1;
	}




	/**
	 * @return the nom_g2
	 */
	public String getNom_g2() {
		return nom_g2;
	}




	/**
	 * @param nom_g2 the nom_g2 to set
	 */
	public void setNom_g2(String nom_g2) {
		this.nom_g2 = nom_g2;
	}




	/**
	 * @return the total_coef_g2
	 */
	public double getTotal_coef_g2() {
		return total_coef_g2;
	}




	/**
	 * @param total_coef_g2 the total_coef_g2 to set
	 */
	public void setTotal_coef_g2(double total_coef_g2) {
		this.total_coef_g2 = total_coef_g2;
	}




	/**
	 * @return the total_g2
	 */
	public double getTotal_g2() {
		return total_g2;
	}




	/**
	 * @param total_g2 the total_g2 to set
	 */
	public void setTotal_g2(double total_g2) {
		this.total_g2 = total_g2;
	}




	/**
	 * @return the total_extreme_g2
	 */
	public String getTotal_extreme_g2() {
		return total_extreme_g2;
	}




	/**
	 * @param total_extreme_g2 the total_extreme_g2 to set
	 */
	public void setTotal_extreme_g2(String total_extreme_g2) {
		this.total_extreme_g2 = total_extreme_g2;
	}




	/**
	 * @return the total_rang_g2
	 */
	public String getTotal_rang_g2() {
		return total_rang_g2;
	}




	/**
	 * @param total_rang_g2 the total_rang_g2 to set
	 */
	public void setTotal_rang_g2(String total_rang_g2) {
		this.total_rang_g2 = total_rang_g2;
	}




	/**
	 * @return the mg_classe_g2
	 */
	public double getMg_classe_g2() {
		return mg_classe_g2;
	}




	/**
	 * @param mg_classe_g2 the mg_classe_g2 to set
	 */
	public void setMg_classe_g2(double mg_classe_g2) {
		this.mg_classe_g2 = mg_classe_g2;
	}




	/**
	 * @return the total_pourcentage_g2
	 */
	public String getTotal_pourcentage_g2() {
		return total_pourcentage_g2;
	}




	/**
	 * @param total_pourcentage_g2 the total_pourcentage_g2 to set
	 */
	public void setTotal_pourcentage_g2(double total_pourcentage_g2) {
		this.total_pourcentage_g2 = total_pourcentage_g2+" %";
	}




	/**
	 * @return the moyenne_g2
	 */
	public double getMoyenne_g2() {
		return moyenne_g2;
	}




	/**
	 * @param moyenne_g2 the moyenne_g2 to set
	 */
	public void setMoyenne_g2(double moyenne_g2) {
		this.moyenne_g2 = moyenne_g2;
	}




	/**
	 * @return the nom_g3
	 */
	public String getNom_g3() {
		return nom_g3;
	}




	/**
	 * @param nom_g3 the nom_g3 to set
	 */
	public void setNom_g3(String nom_g3) {
		this.nom_g3 = nom_g3;
	}




	/**
	 * @return the total_coef_g3
	 */
	public double getTotal_coef_g3() {
		return total_coef_g3;
	}




	/**
	 * @param total_coef_g3 the total_coef_g3 to set
	 */
	public void setTotal_coef_g3(double total_coef_g3) {
		this.total_coef_g3 = total_coef_g3;
	}




	/**
	 * @return the total_g3
	 */
	public double getTotal_g3() {
		return total_g3;
	}




	/**
	 * @param total_g3 the total_g3 to set
	 */
	public void setTotal_g3(double total_g3) {
		this.total_g3 = total_g3;
	}




	/**
	 * @return the total_extreme_g3
	 */
	public String getTotal_extreme_g3() {
		return total_extreme_g3;
	}




	/**
	 * @param total_extreme_g3 the total_extreme_g3 to set
	 */
	public void setTotal_extreme_g3(String total_extreme_g3) {
		this.total_extreme_g3 = total_extreme_g3;
	}




	/**
	 * @return the total_rang_g3
	 */
	public String getTotal_rang_g3() {
		return total_rang_g3;
	}




	/**
	 * @param total_rang_g3 the total_rang_g3 to set
	 */
	public void setTotal_rang_g3(String total_rang_g3) {
		this.total_rang_g3 = total_rang_g3;
	}




	/**
	 * @return the mg_classe_g3
	 */
	public double getMg_classe_g3() {
		return mg_classe_g3;
	}




	/**
	 * @param mg_classe_g3 the mg_classe_g3 to set
	 */
	public void setMg_classe_g3(double mg_classe_g3) {
		this.mg_classe_g3 = mg_classe_g3;
	}




	/**
	 * @return the total_pourcentage_g3
	 */
	public String getTotal_pourcentage_g3() {
		return total_pourcentage_g3;
	}




	/**
	 * @param total_pourcentage_g3 the total_pourcentage_g3 to set
	 */
	public void setTotal_pourcentage_g3(double total_pourcentage_g3) {
		this.total_pourcentage_g3 = total_pourcentage_g3+" %";
	}




	/**
	 * @return the moyenne_g3
	 */
	public double getMoyenne_g3() {
		return moyenne_g3;
	}




	/**
	 * @param moyenne_g3 the moyenne_g3 to set
	 */
	public void setMoyenne_g3(double moyenne_g3) {
		this.moyenne_g3 = moyenne_g3;
	}




	/**
	 * @return the total_coef
	 */
	public double getTotal_coef() {
		return total_coef;
	}




	/**
	 * @param total_coef the total_coef to set
	 */
	public void setTotal_coef(double total_coef) {
		this.total_coef = total_coef;
	}




	/**
	 * @return the total_points
	 */
	public double getTotal_points() {
		return total_points;
	}




	/**
	 * @param total_points the total_points to set
	 */
	public void setTotal_points(double total_points) {
		this.total_points = total_points;
	}




	/**
	 * @return the result_tt_points
	 */
	public double getResult_tt_points() {
		return result_tt_points;
	}




	/**
	 * @param result_tt_points the result_tt_points to set
	 */
	public void setResult_tt_points(double result_tt_points) {
		this.result_tt_points = result_tt_points;
	}




	/**
	 * @return the result_tt_coef
	 */
	public double getResult_tt_coef() {
		return result_tt_coef;
	}




	/**
	 * @param result_tt_coef the result_tt_coef to set
	 */
	public void setResult_tt_coef(double result_tt_coef) {
		this.result_tt_coef = result_tt_coef;
	}




	/**
	 * @return the result_rang_trim
	 */
	public String getResult_rang_trim() {
		return result_rang_trim;
	}




	/**
	 * @param result_rang_trim the result_rang_trim to set
	 */
	public void setResult_rang_trim(String result_rang_trim) {
		this.result_rang_trim = result_rang_trim;
	}




	/**
	 * @return the moy_an
	 */
	public double getMoy_an() {
		return moy_an;
	}




	/**
	 * @param moy_an the moy_an to set
	 */
	public void setMoy_an(double moy_an) {
		this.moy_an = moy_an;
	}




	/**
	 * @return the rang_an
	 */
	public String getRang_an() {
		return rang_an;
	}




	/**
	 * @param rang_an the rang_an to set
	 */
	public void setRang_an(String rang_an) {
		this.rang_an = rang_an;
	}




	/**
	 * @return the moy_premier
	 */
	public double getMoy_premier() {
		return moy_premier;
	}




	/**
	 * @param moy_premier the moy_premier to set
	 */
	public void setMoy_premier(double moy_premier) {
		this.moy_premier = moy_premier;
	}




	/**
	 * @return the moy_dernier
	 */
	public double getMoy_dernier() {
		return moy_dernier;
	}




	/**
	 * @param moy_dernier the moy_dernier to set
	 */
	public void setMoy_dernier(double moy_dernier) {
		this.moy_dernier = moy_dernier;
	}




	/**
	 * @return the nbre_moyennes
	 */
	public int getNbre_moyennes() {
		return nbre_moyennes;
	}




	/**
	 * @param nbre_moyennes the nbre_moyennes to set
	 */
	public void setNbre_moyennes(int nbre_moyennes) {
		this.nbre_moyennes = nbre_moyennes;
	}




	/**
	 * @return the taux_reussite
	 */
	public double getTaux_reussite() {
		return taux_reussite;
	}




	/**
	 * @param taux_reussite the taux_reussite to set
	 */
	public void setTaux_reussite(double taux_reussite) {
		this.taux_reussite = taux_reussite;
	}




	/**
	 * @return the t_reussite_an
	 */
	public double getT_reussite_an() {
		return t_reussite_an;
	}




	/**
	 * @param t_reussite_an the t_reussite_an to set
	 */
	public void setT_reussite_an(double t_reussite_an) {
		this.t_reussite_an = t_reussite_an;
	}




	/**
	 * @return the moy_gen_classe
	 */
	public double getMoy_gen_classe() {
		return moy_gen_classe;
	}




	/**
	 * @param moy_gen_classe the moy_gen_classe to set
	 */
	public void setMoy_gen_classe(double moy_gen_classe) {
		this.moy_gen_classe = moy_gen_classe;
	}




	/**
	 * @return the moy_gen_classe_an
	 */
	public double getMoy_gen_classe_an() {
		return moy_gen_classe_an;
	}




	/**
	 * @param moy_gen_classe_an the moy_gen_classe_an to set
	 */
	public void setMoy_gen_classe_an(double moy_gen_classe_an) {
		this.moy_gen_classe_an = moy_gen_classe_an;
	}




	/**
	 * @return the absence_NJ
	 */
	public int getAbsence_NJ() {
		return absence_NJ;
	}




	/**
	 * @param absence_NJ the absence_NJ to set
	 */
	public void setAbsence_NJ(int absence_NJ) {
		this.absence_NJ = absence_NJ;
	}




	/**
	 * @return the absence_J
	 */
	public int getAbsence_J() {
		return absence_J;
	}




	/**
	 * @param absence_J the absence_J to set
	 */
	public void setAbsence_J(int absence_J) {
		this.absence_J = absence_J;
	}




	/**
	 * @return the consigne
	 */
	public String getConsigne() {
		return consigne;
	}




	/**
	 * @param consigne the consigne to set
	 */
	public void setConsigne(String consigne) {
		this.consigne = consigne;
	}




	/**
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}




	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}




	/**
	 * @return the avertissement
	 */
	public String getAvertissement() {
		return avertissement;
	}




	/**
	 * @param avertissement the avertissement to set
	 */
	public void setAvertissement(String avertissement) {
		this.avertissement = avertissement;
	}




	/**
	 * @return the blame_conduite
	 */
	public String getBlame_conduite() {
		return blame_conduite;
	}




	/**
	 * @param blame_conduite the blame_conduite to set
	 */
	public void setBlame_conduite(String blame_conduite) {
		this.blame_conduite = blame_conduite;
	}




	/**
	 * @return the rappel_1
	 */
	public String getRappel_1() {
		return rappel_1;
	}




	/**
	 * @param rappel_1 the rappel_1 to set
	 */
	public void setRappel_1(String rappel_1) {
		this.rappel_1 = rappel_1;
	}




	/**
	 * @return the rappel_2
	 */
	public String getRappel_2() {
		return rappel_2;
	}




	/**
	 * @param rappel_2 the rappel_2 to set
	 */
	public void setRappel_2(String rappel_2) {
		this.rappel_2 = rappel_2;
	}




	/**
	 * @return the rappel_3
	 */
	public String getRappel_3() {
		return rappel_3;
	}




	/**
	 * @param rappel_3 the rappel_3 to set
	 */
	public void setRappel_3(String rappel_3) {
		this.rappel_3 = rappel_3;
	}




	/**
	 * @return the rappel_4
	 */
	public String getRappel_4() {
		return rappel_4;
	}




	/**
	 * @param rappel_4 the rappel_4 to set
	 */
	public void setRappel_4(String rappel_4) {
		this.rappel_4 = rappel_4;
	}




	/**
	 * @return the rappel_5
	 */
	public String getRappel_5() {
		return rappel_5;
	}




	/**
	 * @param rappel_5 the rappel_5 to set
	 */
	public void setRappel_5(String rappel_5) {
		this.rappel_5 = rappel_5;
	}




	/**
	 * @return the r_moy_1
	 */
	public double getR_moy_1() {
		return r_moy_1;
	}




	/**
	 * @param r_moy_1 the r_moy_1 to set
	 */
	public void setR_moy_1(double r_moy_1) {
		this.r_moy_1 = r_moy_1;
	}




	/**
	 * @return the r_moy_2
	 */
	public double getR_moy_2() {
		return r_moy_2;
	}




	/**
	 * @param r_moy_2 the r_moy_2 to set
	 */
	public void setR_moy_2(double r_moy_2) {
		this.r_moy_2 = r_moy_2;
	}




	/**
	 * @return the r_moy_3
	 */
	public double getR_moy_3() {
		return r_moy_3;
	}




	/**
	 * @param r_moy_3 the r_moy_3 to set
	 */
	public void setR_moy_3(double r_moy_3) {
		this.r_moy_3 = r_moy_3;
	}




	/**
	 * @return the r_moy_4
	 */
	public double getR_moy_4() {
		return r_moy_4;
	}




	/**
	 * @param r_moy_4 the r_moy_4 to set
	 */
	public void setR_moy_4(double r_moy_4) {
		this.r_moy_4 = r_moy_4;
	}




	/**
	 * @return the r_moy_5
	 */
	public double getR_moy_5() {
		return r_moy_5;
	}




	/**
	 * @param r_moy_5 the r_moy_5 to set
	 */
	public void setR_moy_5(double r_moy_5) {
		this.r_moy_5 = r_moy_5;
	}




	/**
	 * @return the r_rang_1
	 */
	public String getR_rang_1() {
		return r_rang_1;
	}




	/**
	 * @param r_rang_1 the r_rang_1 to set
	 */
	public void setR_rang_1(String r_rang_1) {
		this.r_rang_1 = r_rang_1;
	}




	/**
	 * @return the r_rang_2
	 */
	public String getR_rang_2() {
		return r_rang_2;
	}




	/**
	 * @param r_rang_2 the r_rang_2 to set
	 */
	public void setR_rang_2(String r_rang_2) {
		this.r_rang_2 = r_rang_2;
	}




	/**
	 * @return the r_rang_3
	 */
	public String getR_rang_3() {
		return r_rang_3;
	}




	/**
	 * @param r_rang_3 the r_rang_3 to set
	 */
	public void setR_rang_3(String r_rang_3) {
		this.r_rang_3 = r_rang_3;
	}




	/**
	 * @return the r_rang_4
	 */
	public String getR_rang_4() {
		return r_rang_4;
	}




	/**
	 * @param r_rang_4 the r_rang_4 to set
	 */
	public void setR_rang_4(String r_rang_4) {
		this.r_rang_4 = r_rang_4;
	}




	/**
	 * @return the r_rang_5
	 */
	public String getR_rang_5() {
		return r_rang_5;
	}




	/**
	 * @param r_rang_5 the r_rang_5 to set
	 */
	public void setR_rang_5(String r_rang_5) {
		this.r_rang_5 = r_rang_5;
	}




	/**
	 * @return the rappel_an
	 */
	public String getRappel_an() {
		return rappel_an;
	}




	/**
	 * @param rappel_an the rappel_an to set
	 */
	public void setRappel_an(String rappel_an) {
		this.rappel_an = rappel_an;
	}




	/**
	 * @return the r_rang_an
	 */
	public String getR_rang_an() {
		return r_rang_an;
	}




	/**
	 * @param r_rang_an the r_rang_an to set
	 */
	public void setR_rang_an(String r_rang_an) {
		this.r_rang_an = r_rang_an;
	}




	/**
	 * @return the r_moy_an
	 */
	public double getR_moy_an() {
		return r_moy_an;
	}




	/**
	 * @param r_moy_an the r_moy_an to set
	 */
	public void setR_moy_an(double r_moy_an) {
		this.r_moy_an = r_moy_an;
	}




	/**
	 * @return the appreciation
	 */
	public String getAppreciation() {
		return appreciation;
	}




	/**
	 * @param appreciation the appreciation to set
	 */
	public void setAppreciation(String appreciation) {
		this.appreciation = appreciation;
	}




	/**
	 * @return the tableau_hon
	 */
	public String getTableau_hon() {
		return tableau_hon;
	}




	/**
	 * @param tableau_hon the tableau_hon to set
	 */
	public void setTableau_hon(String tableau_hon) {
		this.tableau_hon = tableau_hon;
	}




	/**
	 * @return the tableau_enc
	 */
	public String getTableau_enc() {
		return tableau_enc;
	}




	/**
	 * @param tableau_enc the tableau_enc to set
	 */
	public void setTableau_enc(String tableau_enc) {
		this.tableau_enc = tableau_enc;
	}




	/**
	 * @return the tableau_fel
	 */
	public String getTableau_fel() {
		return tableau_fel;
	}




	/**
	 * @param tableau_fel the tableau_fel to set
	 */
	public void setTableau_fel(String tableau_fel) {
		this.tableau_fel = tableau_fel;
	}




	/**
	 * @return the decision_conseil
	 */
	public String getDecision_conseil() {
		return decision_conseil;
	}




	/**
	 * @param decision_conseil the decision_conseil to set
	 */
	public void setDecision_conseil(String decision_conseil) {
		this.decision_conseil = decision_conseil;
	}




	/**
	 * @return the effort_matiere1
	 */
	public String getEffort_matiere1() {
		return effort_matiere1;
	}




	/**
	 * @param effort_matiere1 the effort_matiere1 to set
	 */
	public void setEffort_matiere1(String effort_matiere1) {
		this.effort_matiere1 = effort_matiere1;
	}




	/**
	 * @return the effort_matiere2
	 */
	public String getEffort_matiere2() {
		return effort_matiere2;
	}




	/**
	 * @param effort_matiere2 the effort_matiere2 to set
	 */
	public void setEffort_matiere2(String effort_matiere2) {
		this.effort_matiere2 = effort_matiere2;
	}




	/**
	 * @return the effort_matiere3
	 */
	public String getEffort_matiere3() {
		return effort_matiere3;
	}




	/**
	 * @param effort_matiere3 the effort_matiere3 to set
	 */
	public void setEffort_matiere3(String effort_matiere3) {
		this.effort_matiere3 = effort_matiere3;
	}




	/**
	 * @return the ville
	 */
	public String getVille() {
		return ville;
	}




	/**
	 * @param ville the ville to set
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}




	/**
	 * @return the avertissement_travail
	 */
	public String getAvertissement_travail() {
		return avertissement_travail;
	}




	/**
	 * @param avertissement_travail the avertissement_travail to set
	 */
	public void setAvertissement_travail(String avertissement_travail) {
		this.avertissement_travail = avertissement_travail;
	}




	/**
	 * @return the blame_travail
	 */
	public String getBlame_travail() {
		return blame_travail;
	}




	/**
	 * @param blame_travail the blame_travail to set
	 */
	public void setBlame_travail(String blame_travail) {
		this.blame_travail = blame_travail;
	}




	/**
	 * @return the rapport_disc1
	 */
	public String getRapport_disc1() {
		return rapport_disc1;
	}




	/**
	 * @param rapport_disc1 the rapport_disc1 to set
	 */
	public void setRapport_disc1(String rapport_disc1) {
		this.rapport_disc1 = rapport_disc1;
	}




	/**
	 * @return the rapport_disc2
	 */
	public String getRapport_disc2() {
		return rapport_disc2;
	}




	/**
	 * @param rapport_disc2 the rapport_disc2 to set
	 */
	public void setRapport_disc2(String rapport_disc2) {
		this.rapport_disc2 = rapport_disc2;
	}




	/**
	 * @return the rapport_disc3
	 */
	public String getRapport_disc3() {
		return rapport_disc3;
	}




	/**
	 * @param rapport_disc3 the rapport_disc3 to set
	 */
	public void setRapport_disc3(String rapport_disc3) {
		this.rapport_disc3 = rapport_disc3;
	}




	/**
	 * @return the distinction
	 */
	public String getDistinction() {
		return distinction;
	}




	/**
	 * @param distinction the distinction to set
	 */
	public void setDistinction(String distinction) {
		this.distinction = distinction;
	}
	
	
	
	

}
