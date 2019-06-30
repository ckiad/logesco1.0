/**
 * 
 */
package org.logesco.services;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logesco.entities.Annee;
import org.logesco.entities.Classes;
import org.logesco.entities.Cours;
import org.logesco.entities.DecisionConseil;
import org.logesco.entities.Eleves;
import org.logesco.entities.Etablissement;
import org.logesco.entities.RapportDisciplinaire;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.modeles.BulletinAnnuelBean;
import org.logesco.modeles.BulletinSequenceBean;
import org.logesco.modeles.BulletinTrimAnnuelBean;
import org.logesco.modeles.BulletinTrimestreBean;
import org.logesco.modeles.FicheConseilClasseBean;
import org.logesco.modeles.LigneAnnuelGroupeCours;
import org.logesco.modeles.LigneSequentielGroupeCours;
import org.logesco.modeles.LigneTrimestrielGroupeCours;
import org.logesco.modeles.MatiereGroupe1AnnuelBean;
import org.logesco.modeles.MatiereGroupe1SequenceBean;
import org.logesco.modeles.MatiereGroupe1TrimAnnuelBean;
import org.logesco.modeles.MatiereGroupe1TrimestreBean;
import org.logesco.modeles.MatiereGroupe2AnnuelBean;
import org.logesco.modeles.MatiereGroupe2SequenceBean;
import org.logesco.modeles.MatiereGroupe2TrimAnnuelBean;
import org.logesco.modeles.MatiereGroupe2TrimestreBean;
import org.logesco.modeles.MatiereGroupe3AnnuelBean;
import org.logesco.modeles.MatiereGroupe3SequenceBean;
import org.logesco.modeles.MatiereGroupe3TrimAnnuelBean;
import org.logesco.modeles.MatiereGroupe3TrimestreBean;
import org.logesco.modeles.RapportAnnuelClasse;
import org.logesco.modeles.RapportAnnuelCours;
import org.logesco.modeles.RapportSequentielClasse;
import org.logesco.modeles.RapportSequentielCours;
import org.logesco.modeles.RapportTrimestrielClasse;
import org.logesco.modeles.RapportTrimestrielCours;
import org.logesco.modeles.SousRapport1ConseilBean;
import org.logesco.modeles.SousRapport2ConseilBean;
import org.logesco.modeles.SousRapport3ConseilBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cedrickiadjeu
 *
 */
@Service
@Transactional
public class BulletinServiceImplementation implements IBulletinService {

	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	

	
	@Autowired
	private IUsersService usersService;
	
	private UtilitairesBulletins ub;
	
	/**
	 * 
	 */
	public BulletinServiceImplementation() {
		// TODO Auto-generated constructor stub
		this.ub = new UtilitairesBulletins();
	}
	
	
	public FicheConseilClasseBean getRapportConseilClasseSequentiel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			Sequences sequence, List<Eleves> listofElevesOrdreDecroissantMoyenneSequentiel){
		

		
		/*log.log(Level.DEBUG, "Lancement de la methode getRapportConseilClasseSequentiel ");*/
		FicheConseilClasseBean ficheCC = new FicheConseilClasseBean();
		
		String lang="";
		if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
			lang="fr";
		}
		else{
			lang="en";
		}
		
		/****
		 * Information d'entete du rapport de conseil de classe
		 */
		
		ficheCC.setDelegation_fr(etab.getDeleguationdeptuteleEtab());
		ficheCC.setDelegation_en(etab.getDeleguationdeptuteleanglaisEtab());
		ficheCC.setEtablissement_fr(etab.getNomsEtab());
		ficheCC.setEtablissement_en(etab.getNomsanglaisEtab());
		ficheCC.setAdresse("BP "+etab.getBpEtab()+"/"+
				etab.getNumtel1Etab()+"/"+etab.getEmailEtab());
		ficheCC.setAnnee_scolaire_en("School year "+annee.getIntituleAnnee());
		String nomClasse = classe.getCodeClasses()+""+
				classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses();
		ficheCC.setClasse(nomClasse);
		ficheCC.setAnnee_scolaire_fr("Année scolaire "+annee.getIntituleAnnee());
		ficheCC.setMinistere_fr(etab.getMinisteretuteleEtab());
		ficheCC.setMinistere_en(etab.getMinisteretuteleanglaisEtab());
		ficheCC.setDevise_en(etab.getDeviseanglaisEtab());
		ficheCC.setDevise_fr(etab.getDeviseEtab());
		
		ficheCC.setAnnee(annee.getIntituleAnnee());
		if(lang.equalsIgnoreCase("fr")==true){
			ficheCC.setPeriode("Séquence "+sequence.getNumeroSeq());
			String titre_fiche = "CONSEIL DE CLASSE DE LA SEQUENCE: "+sequence.getNumeroSeq();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		else{
			ficheCC.setPeriode("Sequence "+sequence.getNumeroSeq());
			String titre_fiche = "CLASS COUNCIL OF SEQUENCE: "+sequence.getNumeroSeq();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		
		String profPrincipal = "";
		if(classe.getProffesseur()!=null){
			profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
			profPrincipal=profPrincipal.toUpperCase();
		}
		
		ficheCC.setEnseignant(profPrincipal);
		String nonClasse = classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+
				classe.getNumeroClasses();		
		ficheCC.setClasse(nonClasse);
		
		int g_ins =  0;
		int f_ins = 0;
		int t_ins = 0;
		List<Integer> listofEffectif = new ArrayList<Integer>();
		listofEffectif = ub.geteffectifSexeDansClasse(classe);
		t_ins = listofEffectif.get(0) + listofEffectif.get(1);
		if(listofEffectif.size() == 2){
			g_ins = listofEffectif.get(0);
			f_ins = listofEffectif.get(1);
		}
		ficheCC.setG_ins(g_ins);
		ficheCC.setF_ins(f_ins);
		ficheCC.setT_ins(t_ins);
		
		
		int g_pre =  0;
		int f_pre = 0;
		int t_pre = 0;
		List<Integer> listofEffectifRegulier = new ArrayList<Integer>();
		listofEffectifRegulier = ub.geteffectifRegulierSexeDansClasse(classe, sequence);
		t_pre = listofEffectifRegulier.get(0) + listofEffectifRegulier.get(1);
		if(listofEffectif.size() == 2){
			g_pre = listofEffectifRegulier.get(0);
			f_pre = listofEffectifRegulier.get(1);
		}
		ficheCC.setG_pre(g_pre);
		ficheCC.setF_pre(f_pre);
		ficheCC.setT_pre(t_pre);
		
		//Preparation des données du sous_rapport1_conseil
		List<SousRapport1ConseilBean> listofSousRapport1ConseilBean = 
				ub.getListofSousRapport1Conseil(classe, sequence);
		ficheCC.setSous_rapport1_conseil(listofSousRapport1ConseilBean);
		//System.err.println("listofSousRapport1ConseilBean  "+listofSousRapport1ConseilBean.size());
		//resultat d'ensemble
		int g_classe = 0;
		int f_classe = 0;
		int t_classe = 0;
		int g_nonclasse = 0;
		int f_nonclasse = 0;
		int t_nonclasse = 0;
		Map<String, Object> mapofEff  = ub.getEffectifMoyenneSexeClasse(
				listofElevesOrdreDecroissantMoyenneSequentiel, sequence);
		
		int nbreMoyG5 = (Integer)mapofEff.get("nbreMoyG5"); //nombre de garcon avec une moyenne < à 5
		int nbreMoyG7 = (Integer)mapofEff.get("nbreMoyG7"); //nombre de garcon avec une moyenne comprise entre >=5 et <7
		int nbreMoyG8 = (Integer)mapofEff.get("nbreMoyG8");//>=7 et <8
		int nbreMoyG9 = (Integer)mapofEff.get("nbreMoyG9");//>=7 et <8
		int nbreMoyG10 = (Integer)mapofEff.get("nbreMoyG10");//>=9 et <10
		int nbreMoyG12 = (Integer)mapofEff.get("nbreMoyG12");
		//int nbreMoyG13 = 0;
		int nbreMoyG14 = (Integer)mapofEff.get("nbreMoyG14");
		int nbreMoyG20 = (Integer)mapofEff.get("nbreMoyG20");
		
		g_classe = nbreMoyG5+nbreMoyG7+nbreMoyG8+nbreMoyG9+nbreMoyG10+nbreMoyG12+
				nbreMoyG14+nbreMoyG20;
		g_nonclasse = g_ins - g_classe;
		int g_nbremoy = nbreMoyG12+nbreMoyG14+nbreMoyG20;
		
		int nbreMoyF5 = (Integer)mapofEff.get("nbreMoyF5"); //nombre de fille avec une moyenne < à 5
		int nbreMoyF7 = (Integer)mapofEff.get("nbreMoyF7"); //nombre de fille avec une moyenne comprise entre >=5 et <7
		int nbreMoyF8 = (Integer)mapofEff.get("nbreMoyF8");//>=7 et <8
		int nbreMoyF9 = (Integer)mapofEff.get("nbreMoyF9");//>=8 et <9
		int nbreMoyF10 = (Integer)mapofEff.get("nbreMoyF10");//>=9 et <10
		int nbreMoyF12 = (Integer)mapofEff.get("nbreMoyF12");
		//int nbreMoyF13 = 0;
		int nbreMoyF14 = (Integer)mapofEff.get("nbreMoyF14");
		int nbreMoyF20 = (Integer)mapofEff.get("nbreMoyF20");
		
		f_classe = nbreMoyF5+nbreMoyF7+nbreMoyF8+nbreMoyF9+nbreMoyF10+nbreMoyF12+
				nbreMoyF14+nbreMoyF20;
		f_nonclasse = f_ins - f_classe;
		int f_nbremoy = nbreMoyF12+nbreMoyF14+nbreMoyF20;
		
		t_nonclasse = g_nonclasse + f_nonclasse;
		int t_nbremoy = f_nbremoy + g_nbremoy;
		
		double pourCG = (Double)mapofEff.get("pourCG");
		
		double pourCF = (Double)mapofEff.get("pourCF");
		
		ficheCC.setG_classe(g_classe);
		ficheCC.setF_classe(f_classe);
		t_classe = g_classe+f_classe;
		ficheCC.setT_classe(t_classe);
		ficheCC.setG_nonclasse(g_nonclasse);
		ficheCC.setF_nonclasse(f_nonclasse);
		ficheCC.setT_nonclasse(t_nonclasse);
		ficheCC.setG_nbremoy(g_nbremoy);
		ficheCC.setF_nbremoy(f_nbremoy);
		ficheCC.setT_nbremoy(t_nbremoy);
		ficheCC.setG_pourcentage(pourCG);
		ficheCC.setF_pourcentage(pourCF);
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try{
			tauxReussite=df.parse(df.format(tauxReussite)).doubleValue();
			moyenne_general=df.parse(df.format(moyenne_general)).doubleValue();
		}
		catch(Exception e){
			
		}*/
		int nb_decimale = 3;
		tauxReussite = this.ub.tronqueDouble(tauxReussite, nb_decimale);
		moyenne_general = this.ub.tronqueDouble(moyenne_general, nb_decimale);
		ficheCC.setT_pourcentage(tauxReussite);
		ficheCC.setMg_classe(moyenne_general);
		
		//Classement
		String nom_1ere ="";
		String nom_2eme ="";
		String nom_3eme ="";
		String nom_4eme ="";
		String nom_5eme ="";
		
		double moy_1ere =0.0;
		double moy_2eme =0.0;
		double moy_3eme =0.0;
		double moy_4eme =0.0;
		double moy_5eme =0.0;
		
		
		int nbreMoyCalcule = listofElevesOrdreDecroissantMoyenneSequentiel.size();
		if(nbreMoyCalcule>=5){
			Eleves eleve1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(0);
			nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
			moy_1ere = ub.getMoyenneSequentiel(eleve1, sequence);
			
			Eleves eleve2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(1);
			nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
			moy_2eme = ub.getMoyenneSequentiel(eleve2, sequence);
			
			Eleves eleve3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(2);
			nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
			moy_3eme = ub.getMoyenneSequentiel(eleve3, sequence);
			
			Eleves eleve4 = listofElevesOrdreDecroissantMoyenneSequentiel.get(3);
			nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
			moy_4eme = ub.getMoyenneSequentiel(eleve4, sequence);
			
			Eleves eleve5 = listofElevesOrdreDecroissantMoyenneSequentiel.get(4);
			nom_5eme = eleve5.getNomsEleves()+" "+eleve5.getPrenomsEleves();
			moy_5eme = ub.getMoyenneSequentiel(eleve5, sequence);
		}
		else if(nbreMoyCalcule>=4){
			Eleves eleve1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(0);
			nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
			moy_1ere = ub.getMoyenneSequentiel(eleve1, sequence);
			
			Eleves eleve2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(1);
			nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
			moy_2eme = ub.getMoyenneSequentiel(eleve2, sequence);
			
			Eleves eleve3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(2);
			nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
			moy_3eme = ub.getMoyenneSequentiel(eleve3, sequence);
			
			Eleves eleve4 = listofElevesOrdreDecroissantMoyenneSequentiel.get(3);
			nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
			moy_4eme = ub.getMoyenneSequentiel(eleve4, sequence);
		}
		else if(nbreMoyCalcule>=3){
			Eleves eleve1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(0);
			nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
			moy_1ere = ub.getMoyenneSequentiel(eleve1, sequence);
			
			Eleves eleve2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(1);
			nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
			moy_2eme = ub.getMoyenneSequentiel(eleve2, sequence);
			
			Eleves eleve3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(2);
			nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
			moy_3eme = ub.getMoyenneSequentiel(eleve3, sequence);
		}
		else if(nbreMoyCalcule>=2){
			Eleves eleve1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(0);
			nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
			moy_1ere = ub.getMoyenneSequentiel(eleve1, sequence);
			
			Eleves eleve2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(1);
			nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
			moy_2eme = ub.getMoyenneSequentiel(eleve2, sequence);
		}
		else if(nbreMoyCalcule>=1){
			Eleves eleve1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(0);
			nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
			moy_1ere = ub.getMoyenneSequentiel(eleve1, sequence);
		}
		
		ficheCC.setNom_1ere(nom_1ere);
		if(moy_1ere>0) ficheCC.setMoy_1ere(moy_1ere);
		ficheCC.setNom_2eme(nom_2eme);
		if(moy_2eme>0) ficheCC.setMoy_2eme(moy_2eme);
		ficheCC.setNom_3eme(nom_3eme);
		if(moy_3eme>0) ficheCC.setMoy_3eme(moy_3eme);
		ficheCC.setNom_4eme(nom_4eme);
		if(moy_4eme>0) ficheCC.setMoy_4eme(moy_4eme);
		ficheCC.setNom_5eme(nom_5eme);
		if(moy_5eme>0) ficheCC.setMoy_5eme(moy_5eme);
		
		String nom_dernier1 = "";
		String nom_dernier2 = "";
		String nom_dernier3 = "";
		String nom_dernier4 = "";
		String nom_dernier5 = "";
		
		String rang_dernier1 ="";
		String rang_dernier2 ="";
		String rang_dernier3 ="";
		String rang_dernier4 ="";
		String rang_dernier5 ="";
		
		double moy_dernier1 =0.0;
		double moy_dernier2 =0.0;
		double moy_dernier3 =0.0;
		double moy_dernier4 =0.0;
		double moy_dernier5 =0.0;
		
		if(nbreMoyCalcule>5){
			int nbredeDernier = nbreMoyCalcule-5;
			if(nbredeDernier>=5){
				Eleves eleved5 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-1);
				nom_dernier5 = eleved5.getNomsEleves()+" "+eleved5.getPrenomsEleves();
				rang_dernier5 = ""+(nbreMoyCalcule);
				moy_dernier5 = ub.getMoyenneSequentiel(eleved5, sequence);
				
				Eleves eleved4 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-2);
				nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
				rang_dernier4 = ""+(nbreMoyCalcule-1);
				moy_dernier4 = ub.getMoyenneSequentiel(eleved4, sequence);
				
				Eleves eleved3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-3);
				nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
				rang_dernier3 = ""+(nbreMoyCalcule-2);
				moy_dernier3 = ub.getMoyenneSequentiel(eleved3, sequence);
				
				Eleves eleved2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-4);
				nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
				rang_dernier2 = ""+(nbreMoyCalcule-3);
				moy_dernier2 = ub.getMoyenneSequentiel(eleved2, sequence);
				
				Eleves eleved1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-5);
				nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
				rang_dernier1 = ""+(nbreMoyCalcule-4);
				moy_dernier1 = ub.getMoyenneSequentiel(eleved1, sequence);
			}
			else if(nbredeDernier>=4){
				Eleves eleved4 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-1);
				nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
				rang_dernier4 = ""+(nbreMoyCalcule);
				moy_dernier4 = ub.getMoyenneSequentiel(eleved4, sequence);
				
				Eleves eleved3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-2);
				nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
				rang_dernier3 = ""+(nbreMoyCalcule-1);
				moy_dernier3 = ub.getMoyenneSequentiel(eleved3, sequence);
				
				Eleves eleved2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-3);
				nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
				rang_dernier2 = ""+(nbreMoyCalcule-2);
				moy_dernier2 = ub.getMoyenneSequentiel(eleved2, sequence);
				
				Eleves eleved1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-4);
				nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
				rang_dernier1 = ""+(nbreMoyCalcule-3);
				moy_dernier1 = ub.getMoyenneSequentiel(eleved1, sequence);
			}
			else if(nbredeDernier>=3){
				Eleves eleved3 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-1);
				nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
				rang_dernier3 = ""+(nbreMoyCalcule);
				moy_dernier3 = ub.getMoyenneSequentiel(eleved3, sequence);
				
				Eleves eleved2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-2);
				nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
				rang_dernier2 = ""+(nbreMoyCalcule-1);
				moy_dernier2 = ub.getMoyenneSequentiel(eleved2, sequence);
				
				Eleves eleved1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-3);
				nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
				rang_dernier1 = ""+(nbreMoyCalcule-2);
				moy_dernier1 = ub.getMoyenneSequentiel(eleved1, sequence);
			}
			else if(nbredeDernier>=2){
				Eleves eleved2 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-1);
				nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
				rang_dernier2 = ""+(nbreMoyCalcule);
				moy_dernier2 = ub.getMoyenneSequentiel(eleved2, sequence);
				
				Eleves eleved1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-2);
				nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
				rang_dernier1 = ""+(nbreMoyCalcule-1);
				moy_dernier1 = ub.getMoyenneSequentiel(eleved1, sequence);
			}
			else if(nbredeDernier>=1){
				Eleves eleved1 = listofElevesOrdreDecroissantMoyenneSequentiel.get(nbreMoyCalcule-1);
				nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
				rang_dernier1 = ""+(nbreMoyCalcule);
				moy_dernier1 = ub.getMoyenneSequentiel(eleved1, sequence);
			}
			
		}
		
		ficheCC.setRang_dernier1(rang_dernier1);
		ficheCC.setNom_dernier1(nom_dernier1);
		if(moy_dernier1>0)	ficheCC.setMoy_dernier1(moy_dernier1);
		
		ficheCC.setRang_dernier2(rang_dernier2);
		ficheCC.setNom_dernier2(nom_dernier2);
		if(moy_dernier2>0)	ficheCC.setMoy_dernier2(moy_dernier2);
		
		ficheCC.setRang_dernier3(rang_dernier3);
		ficheCC.setNom_dernier3(nom_dernier3);
		if(moy_dernier3>0)	ficheCC.setMoy_dernier3(moy_dernier3);
		
		ficheCC.setRang_dernier4(rang_dernier4);
		ficheCC.setNom_dernier4(nom_dernier4);
		if(moy_dernier4>0)	ficheCC.setMoy_dernier4(moy_dernier4);
		
		ficheCC.setRang_dernier5(rang_dernier5);
		ficheCC.setNom_dernier5(nom_dernier5);
		if(moy_dernier5>0)	ficheCC.setMoy_dernier5(moy_dernier5);
		
		ficheCC.setVille(etab.getVilleEtab());
		ficheCC.setEnseignant(profPrincipal);
		
		//Statistiques globales

		
		int nbre_moy5 = nbreMoyG5+nbreMoyF5;
		int nbre_moy7 = nbreMoyG7+nbreMoyF7;
		int nbre_moy8 = nbreMoyG8+nbreMoyF8;
		int nbre_moy9 = nbreMoyG9+nbreMoyF9;
		int nbre_moy10 = nbreMoyG10+nbreMoyF10;
		int nbre_moy12 = nbreMoyG12+nbreMoyF12;
		int nbre_moy14 = nbreMoyG14+nbreMoyF14;
		int nbre_moy15 = nbreMoyG20+nbreMoyF20;

		ficheCC.setNbre_moy5(nbre_moy5);
		ficheCC.setNbre_moy7(nbre_moy7);
		ficheCC.setNbre_moy8(nbre_moy8);
		ficheCC.setNbre_moy9(nbre_moy9);
		ficheCC.setNbre_moy10(nbre_moy10);
		ficheCC.setNbre_moy12(nbre_moy12);
		ficheCC.setNbre_moy14(nbre_moy14);
		ficheCC.setNbre_moy15(nbre_moy15);
		
		//Taux de reussite par discipline
		List<SousRapport2ConseilBean> sous_rapport2_conseil = ub.getListofSousRapport2Conseil(classe, sequence);
		
		List<SousRapport3ConseilBean> sous_rapport3_conseil_inter = ub.getListofSousRapport3Conseil(classe, sequence);
		
		List<SousRapport3ConseilBean> sous_rapport3_conseil = new ArrayList<SousRapport3ConseilBean>();
		
		for(SousRapport2ConseilBean sr2cb: sous_rapport2_conseil){
			int r=0;
			for(SousRapport3ConseilBean sr3cb: sous_rapport3_conseil_inter){
				if(sr2cb.getNom_matiere().equalsIgnoreCase(sr3cb.getNom_matiere()) == true){
					r=1;
					break;
				}
			}
			if(r == 0){
				SousRapport3ConseilBean sr3cb = new SousRapport3ConseilBean();
				sr3cb.setNbre_moy(sr2cb.getNbre_moy());
				sr3cb.setNbrepresent_mat(sr2cb.getNbrepresent_mat());
				sr3cb.setNom_matiere(sr2cb.getNom_matiere());
				sr3cb.setPourcentage(sr2cb.getPourcentage());
				
				sous_rapport3_conseil.add(sr3cb);
			}
		}
		
		ficheCC.setSous_rapport2_conseil(sous_rapport2_conseil);
		ficheCC.setSous_rapport3_conseil(sous_rapport3_conseil);
		
		/*
		 * Il faut placer dans ce bean le total des absences par sexe pour le total entier
		 */
		int totalAbsF = usersService.getNbreAbsNJSexeClasseSeq(classe, sequence, 0);
		int totalAbsM = usersService.getNbreAbsNJSexeClasseSeq(classe, sequence, 1);
		int totalAbs = totalAbsF+totalAbsM;
		
		ficheCC.setTotalAbs(totalAbs);
		ficheCC.setTotalAbsF(totalAbsF);
		ficheCC.setTotalAbsM(totalAbsM);
		
		/*
		 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
		 */
		int n=10;
		List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = usersService.getListeElevePlusIndisciplineSeq(classe, sequence, n);
		
		//premier eleve le plus indiscipline
		if(listofEleveLesPlusIndisciDansClasseSeq.size()>0){
			String indiscnom1 = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNomsEleves();
			indiscnom1+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(0).getPrenomsEleves();
			String sanction1 = "aucune";
			List<RapportDisciplinaire> listofRappDiscEleve = 
					(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(0).getListofRDisc_DESC();
			
			if(listofRappDiscEleve!=null){
				if(listofRappDiscEleve.size()>0){
					if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
						sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
					}
					else{
						sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
					}
				}
				else{
					//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
					int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
					if(nbreHANJ>0){
						sanction1=nbreHANJ+" H";
					}
				}
			}
			
			
			if(sanction1.equalsIgnoreCase("aucune")==false){
				ficheCC.setIndiscnom1(indiscnom1);
				ficheCC.setSanction1(sanction1);
			}
			
		}
		
		//deuxieme eleve le plus indiscipline
		if(listofEleveLesPlusIndisciDansClasseSeq.size()>1){
			String indiscnom2 = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNomsEleves();
			indiscnom2+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(1).getPrenomsEleves();
			String sanction2 = "aucune";
			List<RapportDisciplinaire> listofRappDiscEleve = 
					(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(1).getListofRDisc_DESC();
			
			if(listofRappDiscEleve!=null){
				if(listofRappDiscEleve.size()>0){
					if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
						sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
					}
					else{
						sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
					}
				}
				else{
					//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
					int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
					if(nbreHANJ>0){
						sanction2=nbreHANJ+" H";
					}
				}
			}
			
			
			if(sanction2.equalsIgnoreCase("aucune")==false){
				ficheCC.setIndiscnom2(indiscnom2);
				ficheCC.setSanction2(sanction2);
			}
			
		}
		
		//3eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>2){
					String indiscnom3 = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNomsEleves();
					indiscnom3+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(2).getPrenomsEleves();
					String sanction3 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(2).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction3=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction3.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom3(indiscnom3);
						ficheCC.setSanction3(sanction3);
					}
					
				}
		
				//4eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>3){
					String indiscnom4 = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNomsEleves();
					indiscnom4+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(3).getPrenomsEleves();
					String sanction4 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(3).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction4=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction4.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom4(indiscnom4);
						ficheCC.setSanction4(sanction4);
					}
					
				}
				
				//5eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>4){
					String indiscnom5 = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNomsEleves();
					indiscnom5+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(4).getPrenomsEleves();
					String sanction5 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(4).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction5=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction5.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom5(indiscnom5);
						ficheCC.setSanction5(sanction5);
					}
					
				}
				
				//6eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>5){
					String indiscnom6 = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNomsEleves();
					indiscnom6+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(5).getPrenomsEleves();
					String sanction6 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(5).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction6=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction6.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom6(indiscnom6);
						ficheCC.setSanction6(sanction6);
					}
					
				}
				
				//7eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>6){
					String indiscnom7 = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNomsEleves();
					indiscnom7+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(6).getPrenomsEleves();
					String sanction7 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(6).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction7=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction7.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom7(indiscnom7);
						ficheCC.setSanction7(sanction7);
					}
					
				}
				
				//8eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>7){
					String indiscnom8 = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNomsEleves();
					indiscnom8+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(7).getPrenomsEleves();
					String sanction8 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(7).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction8=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction8.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom8(indiscnom8);
						ficheCC.setSanction8(sanction8);
					}
					
				}
				
				//9eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>8){
					String indiscnom9 = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNomsEleves();
					indiscnom9+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(8).getPrenomsEleves();
					String sanction9 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(8).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction9=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction9.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom9(indiscnom9);
						ficheCC.setSanction9(sanction9);
					}
					
				}
				
				//10eme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>9){
					String indiscnom10 = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNomsEleves();
					indiscnom10+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(9).getPrenomsEleves();
					String sanction10 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(9).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes());
							if(nbreHANJ>0){
								sanction10=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction10.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom10(indiscnom10);
						ficheCC.setSanction10(sanction10);
					}
					
				}
		
		
		return ficheCC;
	
		
	}
	
	

	@Override
	public Map<String, Object> generateCollectionofBulletinSequence_opt(Long idClasse, Long idSequence) {

		
		
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		//java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		
		 long startTime = System.currentTimeMillis();
		 
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Sequences sequenceConcerne = usersService.findSequences(idSequence);
			
		 List<BulletinSequenceBean> collectionofBulletionSequence_opt = new ArrayList<BulletinSequenceBean>();
		
		 if((classeConcerne==null) || (sequenceConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves 
			 * de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalue = ub.getListOfCoursEvalueDansSequence(classeConcerne, 
					sequenceConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas d'un seul élève et son identique pour tous les bulletins d'une classe 
			 * dans une séquence
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulier(classeConcerne, sequenceConcerne);
			
			RapportSequentielClasse rapportSequentielClasse = ub.getRapportSequentielClasse(classeConcerne, 
					listofEleveRegulier, sequenceConcerne);
			double moyenne_premier_classe = 0.0;
			double moyenne_dernier_classe = 0.0;
			int nbre_moyenne_classeSeq = 0;
			double tauxReussite = 0.0;
			double moyenne_general = 0.0;
			
			moyenne_premier_classe = rapportSequentielClasse.getValeurMoyennePremierDansSeq();
				
			moyenne_dernier_classe = rapportSequentielClasse.getValeurMoyenneDernierDansSeq();
				
			nbre_moyenne_classeSeq = rapportSequentielClasse.getNbreMoyennePourSeq();
				
			tauxReussite = rapportSequentielClasse.getTauxReussiteSequentiel();
				
			moyenne_general = rapportSequentielClasse.getMoyenneGeneralSequence();
			
			
			
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			/*****************************************************
			 * Il faut verifier si le proffesseur principal n'a pas encore ete specifie pour la classe
			 * Si c'est le cas alors il faut placer le vide devant car aucun prof principal n'existe
			 */
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseSeq = ub.geteffectifEleveRegulier(classeConcerne, sequenceConcerne);
			
			
			int numBull = 1;
			
			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans la séquence considérée. Pour que le trie des élèves ne soit pas fait pour chaque 
			 * élève dans le but de trouver son rang.
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantSequence(classeConcerne, sequenceConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Séquentiellement. Pour que le trie ne soit pas fait sur chaque élève 
			 * traité dans le but de trouver son rang.
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneSequentiel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneSequentielOrdreDecroissant_static(classeConcerne, sequenceConcerne);
			
			
			for(Eleves eleve : listofEleveClasse){
				long startTimeFor = System.currentTimeMillis();
				
				BulletinSequenceBean bulletinSeq = new BulletinSequenceBean();
				/*
				 * Initialisons les premieres donnees du bulletin sequentiel
				 */
				/****
				 * Information d'entete du bulletin
				 */
				bulletinSeq.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
				bulletinSeq.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
				bulletinSeq.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
				bulletinSeq.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
				bulletinSeq.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
				bulletinSeq.setEtablissement_fr(etablissementConcerne.getNomsEtab());
				bulletinSeq.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
						etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
				bulletinSeq.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
				bulletinSeq.setDevise_fr(etablissementConcerne.getDeviseEtab());
				
				if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
					bulletinSeq.setTitre_bulletin("Bulletin de note de la séquence "+sequenceConcerne.getNumeroSeq());
				}
				else{
					bulletinSeq.setTitre_bulletin("Report card of sequence "+sequenceConcerne.getNumeroSeq());
				}
				bulletinSeq.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
				bulletinSeq.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
				
				/*****
				 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
				 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
				 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
				 * on va essayer de charger le fichier correspondant avec la classe File de java.io
				 */
				File f=new File(photoElevesDir+eleve.getIdEleves());
				//System.err.println("est ce que le fichier existe "+f.exists());
				
				if(f.exists()==true){
					bulletinSeq.setPhoto(photoElevesDir+eleve.getIdEleves()); 
				}
				
				
				/***************
				 * Information personnel de l'élève
				 */
				bulletinSeq.setNumero(" "+eleve.getNumero(listofElevesClasse));
				bulletinSeq.setSexe(eleve.getSexeEleves());
				bulletinSeq.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
				bulletinSeq.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				bulletinSeq.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
				bulletinSeq.setLieu_naissance_eleve(eleve.getLieunaissEleves());
				bulletinSeq.setMatricule_eleve(eleve.getMatriculeEleves());
				bulletinSeq.setRedoublant(eleve.getRedoublant());
				bulletinSeq.setClasse_eleve(classeString);
				bulletinSeq.setProf_principal(profPrincipal);
				bulletinSeq.setEffectif_classe(effectifTotalClasse);
				bulletinSeq.setEffectif_presente(effectifRegulierClasseSeq);
				
				/********
				 * Informations sur les labels d'entete des notes séquentiels
				 */
				bulletinSeq.setLabel_seq_x_coef("Note*Coef");
				bulletinSeq.setLabel_sequence("Note Seq"+sequenceConcerne.getNumeroSeq());
				
				/***********
				 * Information sur les totaux séquentiels
				 */
				
				double total_coef = ub.getSommeCoefCoursComposeD(eleve, sequenceConcerne);
				double t_coef = 1.0*total_coef;
				bulletinSeq.setTotal_coef(t_coef);
				
				double total_points = ub.getTotalPointsSequentiel(eleve, sequenceConcerne);
				
				if(total_points>0){
					bulletinSeq.setTotal_points(total_points);
				}
				
				/***********
				 * Informations sur les resultats sequentiels de l'eleve
				 */
				bulletinSeq.setResult_tt_coef(total_coef);
				
				if(total_points>0){
					bulletinSeq.setResult_tt_points(total_points);
				}
				
				
				
				//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
				
				 int rang = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
						 listofElevesOrdreDecroissantMoyenneSequentiel);
				
				if(rang>0){
					bulletinSeq.setResult_rang_seq(rang+"e");
				}
				
				
				/***************************************************
				 * Informations sur le profil  de la classe dans la séquence
				 */
				if(moyenne_premier_classe>0){
					bulletinSeq.setMoy_premier(moyenne_premier_classe);
				}
				if(moyenne_dernier_classe>0){
					bulletinSeq.setMoy_dernier(moyenne_dernier_classe);
				}
				bulletinSeq.setNbre_moyennes(nbre_moyenne_classeSeq);
				if(tauxReussite>0){
					bulletinSeq.setTaux_reussite(tauxReussite);
				}
				if(moyenne_general>0){
					bulletinSeq.setMoy_gen_classe(moyenne_general);
				}
				
				
				/***********************
				 * Informations sur la conduite sequentiel de l'élève
				 */
				//List<RapportDAbsence> listofRabs = eleve.getListRapportDAbsenceSeq(idSequence);
				
				bulletinSeq.setAbsence_J(eleve.getNbreHeureAbsenceJustifie(idSequence));
				bulletinSeq.setAbsence_NJ(eleve.getNbreHeureAbsenceNonJustifie(idSequence));
				
				/*
				 * On doit prendre si elle existe les 03 sanctions ayant le niveau de sévérité
				 * le plus élevée parmi toutes les sanctions obtenus par l'élève pendant la période.
				 * 			 */
				bulletinSeq.setRapport_disc1("");
				bulletinSeq.setRapport_disc2("");
				bulletinSeq.setRapport_disc3("");
				List<RapportDisciplinaire> listofRDiscEleve = eleve.getListRapportDisciplinaireSeq_DESC(idSequence);
				
				if(listofRDiscEleve != null){
					if(listofRDiscEleve.size()>0) {
						RapportDisciplinaire rdisc = listofRDiscEleve.get(0);
						String rdisc_chaine = "";
						rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
						//On peut donc fixer rapport_disc1
						bulletinSeq.setRapport_disc1(rdisc_chaine);
					}
					
					/*
					 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
					 * on va set rapport_disc2
					 */
					if(listofRDiscEleve.size()>1) {

						RapportDisciplinaire rdisc = listofRDiscEleve.get(1);
						String rdisc_chaine = "";
						rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
						//On peut donc fixer rapport_disc1
						bulletinSeq.setRapport_disc2(rdisc_chaine);
					
					}
					
					if(listofRDiscEleve.size()>2) {

						RapportDisciplinaire rdisc = listofRDiscEleve.get(2);
						String rdisc_chaine = "";
						rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
						
						//On peut donc fixer rapport_disc1
						bulletinSeq.setRapport_disc3(rdisc_chaine);
										
					}
					
				}
				
				/**************************
				 * Informations sur le rappel de la moyenne et du rang sequentiel
				 */
				if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
					bulletinSeq.setRappel_1("Séquence "+sequenceConcerne.getNumeroSeq());
				}
				else{
					bulletinSeq.setRappel_1("Sequence "+sequenceConcerne.getNumeroSeq());
				}
				
				double moy_seq = ub.getMoyenneSequentiel(eleve, sequenceConcerne);
				
				if(moy_seq>0){
					bulletinSeq.setR_moy_1(moy_seq);
				}
				
				if(rang>0){
					bulletinSeq.setR_rang_1(rang+"e");
				}
				else{
					bulletinSeq.setR_rang_1("");
				}
				
				
				/****************************
				 * Informations sur l'appreciation du travail de l'élève
				 */
				/*
				 * A traduire en fonction de la langue de la classe
				 */
				bulletinSeq.setTableau_hon("");
				bulletinSeq.setTableau_enc("");
				bulletinSeq.setTableau_fel("");
				String appreciation = ub.calculAppreciation(moy_seq,lang);
				bulletinSeq.setAppreciation(appreciation);
				
				/*
				 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
				 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
				 */
				DecisionConseil decConseil = eleve.getDecisionConseilPeriode(sequenceConcerne.getIdPeriodes());
				bulletinSeq.setDistinction("");
				bulletinSeq.setDecision_conseil("");
				if(decConseil !=null){
					/*******************************
					 * Informations sur les distinctions octroyées  dans la séquence
					 */
					String distinction="";
					distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
					bulletinSeq.setDistinction(distinction);
					
					/*******************************
					 * Informations sur les decision du conseil de classe dans la séquence
					 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
					 * de classe.
					 */
					String decision="";
					decision += decConseil.getSanctionDiscDecisionConseilString(lang);
					/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
					decision+=distinction;*/
					bulletinSeq.setDecision_conseil(decision);
				}
				
				
				
				List<Cours> listofCoursEffortAFournir = ub.getListofCoursDansOrdreEffortAFournir(eleve, listofCoursEvalue, 
						sequenceConcerne);
				bulletinSeq.setEffort_matiere1("");
				bulletinSeq.setEffort_matiere2("");
				bulletinSeq.setEffort_matiere3("");
				if(listofCoursEffortAFournir.size()>0) {
					String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
					bulletinSeq.setEffort_matiere1(codeCours);
				}
				
				
				if(listofCoursEffortAFournir.size()>1) {
					String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
					bulletinSeq.setEffort_matiere2(codeCours);
				}
				
				if(listofCoursEffortAFournir.size()>2) {
					String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
					bulletinSeq.setEffort_matiere3(codeCours);
				}
				
				
				
				/*****************************
				 * Information sur l'espace VISA du bulletin
				 */
				bulletinSeq.setVille(villeEtab);
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres scientifique (Groupe1) dans la séquence
				 * cccccccccccccccccccccccccc
				 */
				
				LigneSequentielGroupeCours ligneSequentielGroupeCoursScientifique = 
						ub.getLigneSequentielGroupeCours(eleve, listofCoursScientifique, sequenceConcerne);
				
				/*
				 * A traduire en fonction de la langue de la classe
				 */
				if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
					bulletinSeq.setNom_g1("SCIENTIFIQUE");
				}
				else{
					bulletinSeq.setNom_g1("SCIENCES");
				}
				
				double total_coef_g1 = ligneSequentielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
				
				
				bulletinSeq.setTotal_coef_g1(total_coef_g1);
				
				double total_g1 = ligneSequentielGroupeCoursScientifique.getTotalNoteSeqElevePourGroupeCours();
				
				if(total_g1>0){
					bulletinSeq.setTotal_g1(total_g1);
				}
				
				String totalextreme_g1 = "";
				
				double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansSeq(
						listofElevesClasse, listofCoursScientifique, sequenceConcerne);
				
				double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansSeq(
						listofElevesClasse, listofCoursScientifique, sequenceConcerne);
				
				if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
					totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
							valeurMoyPremierGrpCours1+"]";
				}
				bulletinSeq.setTotal_extreme_g1(totalextreme_g1);
				
				int r1 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursScientifique, 
						sequenceConcerne, eleve);
				
				if(r1>0){
					bulletinSeq.setTotal_rang_g1(r1+"e");
				}
				
				
				double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
						listofCoursScientifique, sequenceConcerne);
				
				if(moy_gen_grp1>0){
					bulletinSeq.setMg_classe_g1(moy_gen_grp1);
				}
				
				double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
						listofCoursScientifique, sequenceConcerne);
				
				if(total_pourcentage_g1>=0){
					bulletinSeq.setTotal_pourcentage_g1(total_pourcentage_g1);
				}
				
				double moyenne_g1 = ligneSequentielGroupeCoursScientifique.
						getMoyenneSeqElevePourGroupeCours();
				if(moyenne_g1>0){
					bulletinSeq.setMoyenne_g1(moyenne_g1);
				}
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres litteraire (Groupe2) dans la séquence
				 */
				
				
				
				LigneSequentielGroupeCours ligneSequentielGroupeCoursLitteraire = 
						ub.getLigneSequentielGroupeCours(eleve, listofCoursLitteraire, sequenceConcerne);
				
				if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
					bulletinSeq.setNom_g2("LITTERAIRES");
				}
				else{
					bulletinSeq.setNom_g2("ARTS");
				}
				
				double total_coef_g2 = ligneSequentielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
				
				bulletinSeq.setTotal_coef_g2(total_coef_g2);
				
				double total_g2 = ligneSequentielGroupeCoursLitteraire.getTotalNoteSeqElevePourGroupeCours();
				
				if(total_g2>0){
					bulletinSeq.setTotal_g2(total_g2);
				}
				
				String totalextreme_g2 = "";
				
				double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansSeq(
						listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
				
				double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansSeq(
						listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
				
				if(valeurMoyDernierGrpCours2>=0 && valeurMoyPremierGrpCours2>0){
					totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
							valeurMoyPremierGrpCours2+"]";
				}
				bulletinSeq.setTotal_extreme_g2(totalextreme_g2);
				
				
				int r2 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursLitteraire, 
						sequenceConcerne, eleve);
				if(r2>0){
					bulletinSeq.setTotal_rang_g2(r2+"e");
				}	
				
				double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
						listofCoursLitteraire, sequenceConcerne);
				
				if(moy_gen_grp2>0){
					bulletinSeq.setMg_classe_g2(moy_gen_grp2);
				}
				
				
				double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
						listofCoursLitteraire, sequenceConcerne);
				
				
				
				if(total_pourcentage_g2>=0){
					bulletinSeq.setTotal_pourcentage_g2(total_pourcentage_g2);
				}
				
				double moyenne_g2 = ligneSequentielGroupeCoursLitteraire.
						getMoyenneSeqElevePourGroupeCours();
				
				
				if(moyenne_g2>0){
					bulletinSeq.setMoyenne_g2(moyenne_g2);
				}
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Divers (Groupe3) dans la séquence
				 */
								
				LigneSequentielGroupeCours ligneSequentielGroupeCoursDivers = 
						ub.getLigneSequentielGroupeCours(eleve, listofCoursDivers, sequenceConcerne);
				
				if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
					bulletinSeq.setNom_g3("DIVERS");
				}
				else{
					bulletinSeq.setNom_g3("OTHERS");
				}
				
				double total_coef_g3 = ligneSequentielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
				
				bulletinSeq.setTotal_coef_g3(total_coef_g3);
				
				double total_g3 = ligneSequentielGroupeCoursDivers.getTotalNoteSeqElevePourGroupeCours();
				
				if(total_g3>0){
					bulletinSeq.setTotal_g3(total_g3);
				}
				
				String totalextreme_g3 = "";
				
				double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansSeq(
						listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
				
				double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansSeq(
						listofElevesClasse, listofCoursDivers, sequenceConcerne);
				
				if(valeurMoyDernierGrpCours3>=0 && valeurMoyPremierGrpCours3>0){
					totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
							valeurMoyPremierGrpCours3+"]";
				}
				bulletinSeq.setTotal_extreme_g3(totalextreme_g3);
				
				
				int r3 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursDivers, 
						sequenceConcerne, eleve);
				if(r3>0){
					bulletinSeq.setTotal_rang_g3(r3+"e");
				}
				
				
				double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
						listofCoursDivers, sequenceConcerne);
				
				if(moy_gen_grp3>0){
					bulletinSeq.setMg_classe_g3(moy_gen_grp3);
				}
				
				
				double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
						listofCoursDivers, sequenceConcerne);
				
				if(total_pourcentage_g3>=0){
					bulletinSeq.setTotal_pourcentage_g3(total_pourcentage_g3);
				}
				
				double moyenne_g3 = ligneSequentielGroupeCoursDivers.
						getMoyenneSeqElevePourGroupeCours();
				
				if(moyenne_g3>0){
					bulletinSeq.setMoyenne_g3(moyenne_g3);
				}
				
				t_coef = total_coef_g1+total_coef_g2+total_coef_g3;
				
				bulletinSeq.setTotal_coef(t_coef);
				
				/************************************
				 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
				 **********/
				
				
				List<MatiereGroupe1SequenceBean> listofCoursScientifiqueSequenceBean 
							= new ArrayList<MatiereGroupe1SequenceBean>(); 
				
				int rc1 = 0;
				//Gestion des cours scientifique
				for(Cours cours : listofCoursScientifique){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe1SequenceBean mGrp1SeqBean = new MatiereGroupe1SequenceBean();
						
					RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
							classeConcerne, cours, sequenceConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 25);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 20);
					
					mGrp1SeqBean.setMatiere_g1(matiere);
					mGrp1SeqBean.setMatiere_g1_2emelang(matiere_2emelang);
					mGrp1SeqBean.setProf_g1(nomProf);
					
					double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
					
					if(note_seq_g1>0){
						mGrp1SeqBean.setNote_seq_g1(note_seq_g1);
					}
					double total_seq_g1 = note_seq_g1*cours.getCoefCours();
					if(total_seq_g1>0){
						mGrp1SeqBean.setTotal_seq_g1(total_seq_g1);
					}
					mGrp1SeqBean.setCoef_g1(cours.getCoefCours());
					String extreme_g1 = "";
					double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
					double notePremierCours = rapportSequentielCours.getValeurNotePremier();
					
					if(noteDernierCours>=0 && notePremierCours>0){
						extreme_g1 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
						mGrp1SeqBean.setExtreme_g1(extreme_g1);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc1 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					if(rc1>0){
						mGrp1SeqBean.setRang_g1(rc1+"e");
					}
					
					
					double moy_classe_g1 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
							sequenceConcerne);
					
					if(moy_classe_g1>0){
						mGrp1SeqBean.setMoy_classe_g1(moy_classe_g1);
					}
					
					
					double pourcentage_g1 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
					
					if(pourcentage_g1>=0){
						mGrp1SeqBean.setPourcentage_g1(pourcentage_g1+" %");
					}
					
					String appreciationNote = ub.calculAppreciation(note_seq_g1,lang);
					mGrp1SeqBean.setAppreciation_g1(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursScientifiqueSequenceBean.add(mGrp1SeqBean);
					
				}//fin du for sur les cours scientifique qui passe dans la classe
				
				//On place la liste des matieres scientifique construit
				bulletinSeq.setMatieresGroupe1Sequence(listofCoursScientifiqueSequenceBean);
				
			
				List<MatiereGroupe2SequenceBean> listofCoursLitteraireSequenceBean 
							= new ArrayList<MatiereGroupe2SequenceBean>();
				
				
				//Gestion des cours litteraire
				
				
				int rc2 = 0;
				//Gestion des cours litteraire
				for(Cours cours : listofCoursLitteraire){
		
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe2SequenceBean mGrp2SeqBean = new MatiereGroupe2SequenceBean();
		
		
					
					RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
							classeConcerne, cours, sequenceConcerne);
		
					
					String matiere = ub.subString(cours.getIntituleCours(), 25);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 20);
					
					mGrp2SeqBean.setMatiere_g2(matiere);
					mGrp2SeqBean.setMatiere_g2_2emelang(matiere_2emelang);
					mGrp2SeqBean.setProf_g2(nomProf);
					
		
					
					double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
					
					if(note_seq_g2>0){
						mGrp2SeqBean.setNote_seq_g2(note_seq_g2);
					}
					double total_seq_g2 = note_seq_g2*cours.getCoefCours();
					if(total_seq_g2>0){
						mGrp2SeqBean.setTotal_seq_g2(total_seq_g2);
					}
					
					mGrp2SeqBean.setCoef_g2(cours.getCoefCours());
					String extreme_g2 = "";
					double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
					double notePremierCours = rapportSequentielCours.getValeurNotePremier();
					
					if(noteDernierCours>=0 && notePremierCours>0){
						extreme_g2 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
						mGrp2SeqBean.setExtreme_g2(extreme_g2);
					}
		
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc2 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc2>0){
						mGrp2SeqBean.setRang_g2(rc2+"e");
					}
		
					
					double moy_classe_g2 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
							sequenceConcerne);
					
					if(moy_classe_g2>0){
						mGrp2SeqBean.setMoy_classe_g2(moy_classe_g2);
					}
		
					
					double pourcentage_g2 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
					
					if(pourcentage_g2>=0){
						mGrp2SeqBean.setPourcentage_g2(pourcentage_g2+" %");
					}
		
					String appreciationNote = ub.calculAppreciation(note_seq_g2,lang);
					mGrp2SeqBean.setAppreciation_g2(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursLitteraireSequenceBean.add(mGrp2SeqBean);
		
				}//fin du for sur les cours litteraire qui passe dans la classe			
				
				
				//On place la liste des matieres litteraire construit
				bulletinSeq.setMatieresGroupe2Sequence(listofCoursLitteraireSequenceBean);
				
			
				List<MatiereGroupe3SequenceBean> listofCoursDiversSequenceBean 
							= new ArrayList<MatiereGroupe3SequenceBean>();//Construire a partir de listofCoursDivers
		
				
				//Gestion des cours Divers
				
				int rc3 = 0;
				for(Cours cours : listofCoursDivers){
		
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe3SequenceBean mGrp3SeqBean = new MatiereGroupe3SequenceBean();
		
		
					RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
							classeConcerne, cours, sequenceConcerne);
		
					String matiere = ub.subString(cours.getIntituleCours(), 25);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 20);
					
					mGrp3SeqBean.setMatiere_g3(matiere);
					mGrp3SeqBean.setMatiere_g3_2emelang(matiere_2emelang);
					mGrp3SeqBean.setProf_g3(nomProf);
		
					
					
					double note_seq_g3 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
					
					if(note_seq_g3>0){
						mGrp3SeqBean.setNote_seq_g3(note_seq_g3);
					}
					double total_seq_g3 = note_seq_g3*cours.getCoefCours();
					if(total_seq_g3>0){
						mGrp3SeqBean.setTotal_seq_g3(total_seq_g3);
					}
					
					mGrp3SeqBean.setCoef_g3(cours.getCoefCours());
					String extreme_g3 = "";
					double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
					double notePremierCours = rapportSequentielCours.getValeurNotePremier();
					
					if(noteDernierCours>=0 && notePremierCours>0){
						extreme_g3 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
						mGrp3SeqBean.setExtreme_g3(extreme_g3);
					}
		
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc3 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc3>0){
						mGrp3SeqBean.setRang_g3(rc3+"e");
					}
		
					
					double moy_classe_g3 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
							sequenceConcerne);
					
					if(moy_classe_g3>0){
						mGrp3SeqBean.setMoy_classe_g3(moy_classe_g3);
					}
		
					
					double pourcentage_g3 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
					
					if(pourcentage_g3>=0){
						mGrp3SeqBean.setPourcentage_g3(pourcentage_g3+" %");
					}
		
					String appreciationNote = ub.calculAppreciation(note_seq_g3,lang);
					mGrp3SeqBean.setAppreciation_g3(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursDiversSequenceBean.add(mGrp3SeqBean);
		
				}//fin du for sur les cours Divers qui passe dans la classe	
				
				
				
				//On place la liste des matieres divers construit
				bulletinSeq.setMatieresGroupe3Sequence(listofCoursDiversSequenceBean);
				
				
				long finforTime = System.currentTimeMillis();
				collectionofBulletionSequence_opt.add(bulletinSeq);
				
				System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
						" de la sequence "+sequenceConcerne.getNumeroSeq()+
						"  ajouter avec succes en "+(finforTime-startTimeFor));
				
				numBull++;
				
				
				
				
				
			}//fin du for sur la liste des élèves de la classe
			
			
			long finforTime = System.currentTimeMillis();
			System.out.println("Version opt A ce stade on a deja passe :"+ (finforTime-startTime));
			
		donnee.put("collectionofBulletionSequence", collectionofBulletionSequence_opt);
		
		/*******
		 * Conception du rapport de conseil de classe séquentiel
		 */
		 
		FicheConseilClasseBean ficheCC = this.getRapportConseilClasseSequentiel(etablissementConcerne, 
				anneeScolaire, classeConcerne, tauxReussite, moyenne_general, sequenceConcerne, 
				listofElevesOrdreDecroissantMoyenneSequentiel );
		
		donnee.put("ficheconseilclassesequentiel", ficheCC);

		//return collectionofBulletionSequence_opt;
		return donnee;
	
	
	}

	
	public FicheConseilClasseBean getRapportConseilClasseTrimestriel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			Trimestres trimestre, List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel){
		

		
		/*log.log(Level.DEBUG, "Lancement de la methode getRapportConseilClasseTrimestriel ");*/
		FicheConseilClasseBean ficheCC = new FicheConseilClasseBean();
		
		String lang="";
		if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
			lang="fr";
		}
		else{
			lang="en";
		}
		/****
		 * Information d'entete du rapport de conseil de classe
		 */
		
		ficheCC.setDelegation_fr(etab.getDeleguationdeptuteleEtab());
		ficheCC.setDelegation_en(etab.getDeleguationdeptuteleanglaisEtab());
		ficheCC.setEtablissement_fr(etab.getNomsEtab());
		ficheCC.setEtablissement_en(etab.getNomsanglaisEtab());
		ficheCC.setAdresse("BP "+etab.getBpEtab()+"/"+
				etab.getNumtel1Etab()+"/"+etab.getEmailEtab());
		ficheCC.setAnnee_scolaire_en("School year "+annee.getIntituleAnnee());
		String nomClasse = classe.getCodeClasses()+""+
				classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses();
		ficheCC.setClasse(nomClasse);
		ficheCC.setAnnee_scolaire_fr("Année scolaire "+annee.getIntituleAnnee());
		ficheCC.setMinistere_fr(etab.getMinisteretuteleEtab());
		ficheCC.setMinistere_en(etab.getMinisteretuteleanglaisEtab());
		ficheCC.setDevise_en(etab.getDeviseanglaisEtab());
		ficheCC.setDevise_fr(etab.getDeviseEtab());
		
		ficheCC.setAnnee(annee.getIntituleAnnee());
		if(lang.equalsIgnoreCase("fr")==true){
			ficheCC.setPeriode("Trimestre "+trimestre.getNumeroTrim());
			String titre_fiche = "CONSEIL DE CLASSE DU TRIMESTRE: "+trimestre.getNumeroTrim();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		else{
			ficheCC.setPeriode("Term "+trimestre.getNumeroTrim());
			String titre_fiche = "CLASS COUNCIL OF SEQUENCE: "+trimestre.getNumeroTrim();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		
		String profPrincipal = "";
		if(classe.getProffesseur()!=null){
			profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
			profPrincipal=profPrincipal.toUpperCase();
		}
		
		ficheCC.setEnseignant(profPrincipal);
		String nonClasse = classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+
				classe.getNumeroClasses();		
		ficheCC.setClasse(nonClasse);
		
		int g_ins =  0;
		int f_ins = 0;
		int t_ins = 0;
		List<Integer> listofEffectif = new ArrayList<Integer>();
		listofEffectif = ub.geteffectifSexeDansClasse(classe);
		t_ins = listofEffectif.get(0) + listofEffectif.get(1);
		if(listofEffectif.size() == 2){
			g_ins = listofEffectif.get(0);
			f_ins = listofEffectif.get(1);
		}
		ficheCC.setG_ins(g_ins);
		ficheCC.setF_ins(f_ins);
		ficheCC.setT_ins(t_ins);
		
		
		int g_pre =  0;
		int f_pre = 0;
		int t_pre = 0;
		List<Integer> listofEffectifRegulier = new ArrayList<Integer>();
		listofEffectifRegulier = ub.geteffectifRegulierSexeDansClasse(classe, trimestre);
		t_pre = listofEffectifRegulier.get(0) + listofEffectifRegulier.get(1);
		if(listofEffectif.size() == 2){
			g_pre = listofEffectifRegulier.get(0);
			f_pre = listofEffectifRegulier.get(1);
		}
		ficheCC.setG_pre(g_pre);
		ficheCC.setF_pre(f_pre);
		ficheCC.setT_pre(t_pre);
		
		//Preparation des données du sous_rapport1_conseil
				List<SousRapport1ConseilBean> listofSousRapport1ConseilBean = 
						ub.getListofSousRapport1Conseil(classe, trimestre);
				ficheCC.setSous_rapport1_conseil(listofSousRapport1ConseilBean);
				//System.err.println("listofSousRapport1ConseilBean  "+listofSousRapport1ConseilBean.size());
				//resultat d'ensemble
				int g_classe = 0;
				int f_classe = 0;
				int t_classe = 0;
				int g_nonclasse = 0;
				int f_nonclasse = 0;
				int t_nonclasse = 0;
				Map<String, Object> mapofEff  = ub.getEffectifMoyenneSexeClasse(
						listofElevesOrdreDecroissantMoyenneTrimestriel, trimestre);
				
				int nbreMoyG5 = (Integer)mapofEff.get("nbreMoyG5"); //nombre de garcon avec une moyenne < à 5
				int nbreMoyG7 = (Integer)mapofEff.get("nbreMoyG7"); //nombre de garcon avec une moyenne comprise entre >=5 et <7
				int nbreMoyG8 = (Integer)mapofEff.get("nbreMoyG8");//>=7 et <8
				int nbreMoyG9 = (Integer)mapofEff.get("nbreMoyG9");//>=7 et <8
				int nbreMoyG10 = (Integer)mapofEff.get("nbreMoyG10");//>=9 et <10
				int nbreMoyG12 = (Integer)mapofEff.get("nbreMoyG12");
				//int nbreMoyG13 = 0;
				int nbreMoyG14 = (Integer)mapofEff.get("nbreMoyG14");
				int nbreMoyG20 = (Integer)mapofEff.get("nbreMoyG20");
				
				g_classe = nbreMoyG5+nbreMoyG7+nbreMoyG8+nbreMoyG9+nbreMoyG10+nbreMoyG12+
						nbreMoyG14+nbreMoyG20;
				g_nonclasse = g_ins - g_classe;
				int g_nbremoy = nbreMoyG12+nbreMoyG14+nbreMoyG20;
				
				int nbreMoyF5 = (Integer)mapofEff.get("nbreMoyF5"); //nombre de fille avec une moyenne < à 5
				int nbreMoyF7 = (Integer)mapofEff.get("nbreMoyF7"); //nombre de fille avec une moyenne comprise entre >=5 et <7
				int nbreMoyF8 = (Integer)mapofEff.get("nbreMoyF8");//>=7 et <8
				int nbreMoyF9 = (Integer)mapofEff.get("nbreMoyF9");//>=8 et <9
				int nbreMoyF10 = (Integer)mapofEff.get("nbreMoyF10");//>=9 et <10
				int nbreMoyF12 = (Integer)mapofEff.get("nbreMoyF12");
				//int nbreMoyF13 = 0;
				int nbreMoyF14 = (Integer)mapofEff.get("nbreMoyF14");
				int nbreMoyF20 = (Integer)mapofEff.get("nbreMoyF20");
				
				f_classe = nbreMoyF5+nbreMoyF7+nbreMoyF8+nbreMoyF9+nbreMoyF10+nbreMoyF12+
						nbreMoyF14+nbreMoyF20;
				f_nonclasse = f_ins - f_classe;
				int f_nbremoy = nbreMoyF12+nbreMoyF14+nbreMoyF20;
				
				t_nonclasse = g_nonclasse + f_nonclasse;
				int t_nbremoy = f_nbremoy + g_nbremoy;
				
				double pourCG = (Double)mapofEff.get("pourCG");
				
				double pourCF = (Double)mapofEff.get("pourCF");
				
				ficheCC.setG_classe(g_classe);
				ficheCC.setF_classe(f_classe);
				t_classe = g_classe + f_classe;
				ficheCC.setT_classe(t_classe);
				ficheCC.setG_nonclasse(g_nonclasse);
				ficheCC.setF_nonclasse(f_nonclasse);
				ficheCC.setT_nonclasse(t_nonclasse);
				ficheCC.setG_nbremoy(g_nbremoy);
				ficheCC.setF_nbremoy(f_nbremoy);
				ficheCC.setT_nbremoy(t_nbremoy);
				ficheCC.setG_pourcentage(pourCG);
				ficheCC.setF_pourcentage(pourCF);
				/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
				try{
					tauxReussite=df.parse(df.format(tauxReussite)).doubleValue();
					moyenne_general=df.parse(df.format(moyenne_general)).doubleValue();
				}
				catch(Exception e){
					
				}*/
				int nb_decimale = 3;
				tauxReussite =  this.ub.tronqueDouble(tauxReussite, nb_decimale);
				moyenne_general = this.ub.tronqueDouble(moyenne_general, nb_decimale);
				ficheCC.setT_pourcentage(tauxReussite);
				ficheCC.setMg_classe(moyenne_general);

				//Classement
				String nom_1ere ="";
				String nom_2eme ="";
				String nom_3eme ="";
				String nom_4eme ="";
				String nom_5eme ="";
				
				double moy_1ere =0.0;
				double moy_2eme =0.0;
				double moy_3eme =0.0;
				double moy_4eme =0.0;
				double moy_5eme =0.0;
				
				
				int nbreMoyCalcule = listofElevesOrdreDecroissantMoyenneTrimestriel.size();
				if(nbreMoyCalcule>=5){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneTrimestriel(eleve1, trimestre);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneTrimestriel(eleve2, trimestre);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneTrimestriel(eleve3, trimestre);
					
					Eleves eleve4 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(3);
					nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
					moy_4eme = ub.getMoyenneTrimestriel(eleve4, trimestre);
					
					Eleves eleve5 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(4);
					nom_5eme = eleve5.getNomsEleves()+" "+eleve5.getPrenomsEleves();
					moy_5eme = ub.getMoyenneTrimestriel(eleve5, trimestre);
				}
				else if(nbreMoyCalcule>=4){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneTrimestriel(eleve1, trimestre);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneTrimestriel(eleve2, trimestre);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneTrimestriel(eleve3, trimestre);
					
					Eleves eleve4 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(3);
					nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
					moy_4eme = ub.getMoyenneTrimestriel(eleve4, trimestre);
				}
				else if(nbreMoyCalcule>=3){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneTrimestriel(eleve1, trimestre);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneTrimestriel(eleve2, trimestre);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneTrimestriel(eleve3, trimestre);
				}
				else if(nbreMoyCalcule>=2){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneTrimestriel(eleve1, trimestre);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneTrimestriel(eleve2, trimestre);
				}
				else if(nbreMoyCalcule>=1){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneTrimestriel(eleve1, trimestre);
				}
				
				ficheCC.setNom_1ere(nom_1ere);
				if(moy_1ere>0) ficheCC.setMoy_1ere(moy_1ere);
				ficheCC.setNom_2eme(nom_2eme);
				if(moy_2eme>0) ficheCC.setMoy_2eme(moy_2eme);
				ficheCC.setNom_3eme(nom_3eme);
				if(moy_3eme>0) ficheCC.setMoy_3eme(moy_3eme);
				ficheCC.setNom_4eme(nom_4eme);
				if(moy_4eme>0) ficheCC.setMoy_4eme(moy_4eme);
				ficheCC.setNom_5eme(nom_5eme);
				if(moy_5eme>0) ficheCC.setMoy_5eme(moy_5eme);
				
				String nom_dernier1 = "";
				String nom_dernier2 = "";
				String nom_dernier3 = "";
				String nom_dernier4 = "";
				String nom_dernier5 = "";
				
				String rang_dernier1 ="";
				String rang_dernier2 ="";
				String rang_dernier3 ="";
				String rang_dernier4 ="";
				String rang_dernier5 ="";
				
				double moy_dernier1 =0.0;
				double moy_dernier2 =0.0;
				double moy_dernier3 =0.0;
				double moy_dernier4 =0.0;
				double moy_dernier5 =0.0;
				
				if(nbreMoyCalcule>5){
					int nbredeDernier = nbreMoyCalcule-5;
					if(nbredeDernier>=5){
						Eleves eleved5 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-1);
						nom_dernier5 = eleved5.getNomsEleves()+" "+eleved5.getPrenomsEleves();
						rang_dernier5 = ""+(nbreMoyCalcule);
						moy_dernier5 = ub.getMoyenneTrimestriel(eleved5, trimestre);
						
						Eleves eleved4 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-2);
						nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
						rang_dernier4 = ""+(nbreMoyCalcule-1);
						moy_dernier4 = ub.getMoyenneTrimestriel(eleved4, trimestre);
						
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-3);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule-2);
						moy_dernier3 = ub.getMoyenneTrimestriel(eleved3, trimestre);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-4);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-3);
						moy_dernier2 = ub.getMoyenneTrimestriel(eleved2, trimestre);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-5);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-4);
						moy_dernier1 = ub.getMoyenneTrimestriel(eleved1, trimestre);
					}
					else if(nbredeDernier>=4){
						Eleves eleved4 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-1);
						nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
						rang_dernier4 = ""+(nbreMoyCalcule);
						moy_dernier4 = ub.getMoyenneTrimestriel(eleved4, trimestre);
						
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-2);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule-1);
						moy_dernier3 = ub.getMoyenneTrimestriel(eleved3, trimestre);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-3);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-2);
						moy_dernier2 = ub.getMoyenneTrimestriel(eleved2, trimestre);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-4);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-3);
						moy_dernier1 = ub.getMoyenneTrimestriel(eleved1, trimestre);
					}
					else if(nbredeDernier>=3){
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-1);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule);
						moy_dernier3 = ub.getMoyenneTrimestriel(eleved3, trimestre);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-2);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-1);
						moy_dernier2 = ub.getMoyenneTrimestriel(eleved2, trimestre);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-3);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-2);
						moy_dernier1 = ub.getMoyenneTrimestriel(eleved1, trimestre);
					}
					else if(nbredeDernier>=2){
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-1);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule);
						moy_dernier2 = ub.getMoyenneTrimestriel(eleved2, trimestre);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-2);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-1);
						moy_dernier1 = ub.getMoyenneTrimestriel(eleved1, trimestre);
					}
					else if(nbredeDernier>=1){
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneTrimestriel.get(nbreMoyCalcule-1);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule);
						moy_dernier1 = ub.getMoyenneTrimestriel(eleved1, trimestre);
					}
					
				}
				
				ficheCC.setRang_dernier1(rang_dernier1);
				ficheCC.setNom_dernier1(nom_dernier1);
				if(moy_dernier1>0)	ficheCC.setMoy_dernier1(moy_dernier1);
				
				ficheCC.setRang_dernier2(rang_dernier2);
				ficheCC.setNom_dernier2(nom_dernier2);
				if(moy_dernier2>0)	ficheCC.setMoy_dernier2(moy_dernier2);
				
				ficheCC.setRang_dernier3(rang_dernier3);
				ficheCC.setNom_dernier3(nom_dernier3);
				if(moy_dernier3>0)	ficheCC.setMoy_dernier3(moy_dernier3);
				
				ficheCC.setRang_dernier4(rang_dernier4);
				ficheCC.setNom_dernier4(nom_dernier4);
				if(moy_dernier4>0)	ficheCC.setMoy_dernier4(moy_dernier4);
				
				ficheCC.setRang_dernier5(rang_dernier5);
				ficheCC.setNom_dernier5(nom_dernier5);
				if(moy_dernier5>0)	ficheCC.setMoy_dernier5(moy_dernier5);
				
				ficheCC.setVille(etab.getVilleEtab());
				ficheCC.setEnseignant(profPrincipal);

				//Statistiques globales
				
				int nbre_moy5 = nbreMoyG5+nbreMoyF5;
				int nbre_moy7 = nbreMoyG7+nbreMoyF7;
				int nbre_moy8 = nbreMoyG8+nbreMoyF8;
				int nbre_moy9 = nbreMoyG9+nbreMoyF9;
				int nbre_moy10 = nbreMoyG10+nbreMoyF10;
				int nbre_moy12 = nbreMoyG12+nbreMoyF12;
				int nbre_moy14 = nbreMoyG14+nbreMoyF14;
				int nbre_moy15 = nbreMoyG20+nbreMoyF20;

				ficheCC.setNbre_moy5(nbre_moy5);
				ficheCC.setNbre_moy7(nbre_moy7);
				ficheCC.setNbre_moy8(nbre_moy8);
				ficheCC.setNbre_moy9(nbre_moy9);
				ficheCC.setNbre_moy10(nbre_moy10);
				ficheCC.setNbre_moy12(nbre_moy12);
				ficheCC.setNbre_moy14(nbre_moy14);
				ficheCC.setNbre_moy15(nbre_moy15);
				
				//Taux de reussite par discipline
				List<SousRapport2ConseilBean> sous_rapport2_conseil = ub.getListofSousRapport2Conseil(classe, trimestre);
				List<SousRapport3ConseilBean> sous_rapport3_conseil_inter = ub.getListofSousRapport3Conseil(classe, trimestre);
				
				List<SousRapport3ConseilBean> sous_rapport3_conseil = new ArrayList<SousRapport3ConseilBean>();
				
				for(SousRapport2ConseilBean sr2cb: sous_rapport2_conseil){
					int r=0;
					for(SousRapport3ConseilBean sr3cb: sous_rapport3_conseil_inter){
						if(sr2cb.getNom_matiere().equalsIgnoreCase(sr3cb.getNom_matiere()) == true){
							r=1;
							break;
						}
					}
					if(r == 0){
						SousRapport3ConseilBean sr3cb = new SousRapport3ConseilBean();
						sr3cb.setNbre_moy(sr2cb.getNbre_moy());
						sr3cb.setNbrepresent_mat(sr2cb.getNbrepresent_mat());
						sr3cb.setNom_matiere(sr2cb.getNom_matiere());
						sr3cb.setPourcentage(sr2cb.getPourcentage());
						
						sous_rapport3_conseil.add(sr3cb);
					}
				}
				
				ficheCC.setSous_rapport2_conseil(sous_rapport2_conseil);
				ficheCC.setSous_rapport3_conseil(sous_rapport3_conseil);
				
		
				
				
				/*
				 * Il faut placer dans ce bean le total des absences par sexe pour le total entier
				 */
				int totalAbsF = usersService.getNbreAbsNJSexeClasseTrim(classe, trimestre, 0);
				int totalAbsM = usersService.getNbreAbsNJSexeClasseTrim(classe, trimestre, 1);
				int totalAbs = totalAbsF+totalAbsM;
				
				ficheCC.setTotalAbs(totalAbs);
				ficheCC.setTotalAbsF(totalAbsF);
				ficheCC.setTotalAbsM(totalAbsM);
				
				/*
				 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
				 */
				int n=10;
				List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = usersService.getListeElevePlusIndisciplineTrim(classe, trimestre, n);
				
				//premier eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>0){
					String indiscnom1 = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNomsEleves();
					indiscnom1+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(0).getPrenomsEleves();
					String sanction1 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(0).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNbreHeureAbsenceNonJustifieTrim(trimestre);
							if(nbreHANJ>0){
								sanction1=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction1.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom1(indiscnom1);
						ficheCC.setSanction1(sanction1);
					}
					
				}
				
				//deuxieme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>1){
					String indiscnom2 = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNomsEleves();
					indiscnom2+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(1).getPrenomsEleves();
					String sanction2 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(1).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNbreHeureAbsenceNonJustifieTrim(trimestre);
							if(nbreHANJ>0){
								sanction2=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction2.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom2(indiscnom2);
						ficheCC.setSanction2(sanction2);
					}
					
				}
				
				//3eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>2){
							String indiscnom3 = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNomsEleves();
							indiscnom3+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(2).getPrenomsEleves();
							String sanction3 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(2).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction3=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction3.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom3(indiscnom3);
								ficheCC.setSanction3(sanction3);
							}
							
						}
				
						//4eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>3){
							String indiscnom4 = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNomsEleves();
							indiscnom4+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(3).getPrenomsEleves();
							String sanction4 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(3).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction4=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction4.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom4(indiscnom4);
								ficheCC.setSanction4(sanction4);
							}
							
						}
						
						//5eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>4){
							String indiscnom5 = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNomsEleves();
							indiscnom5+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(4).getPrenomsEleves();
							String sanction5 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(4).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction5=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction5.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom5(indiscnom5);
								ficheCC.setSanction5(sanction5);
							}
							
						}
						
						//6eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>5){
							String indiscnom6 = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNomsEleves();
							indiscnom6+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(5).getPrenomsEleves();
							String sanction6 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(5).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction6=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction6.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom6(indiscnom6);
								ficheCC.setSanction6(sanction6);
							}
							
						}
						
						//7eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>6){
							String indiscnom7 = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNomsEleves();
							indiscnom7+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(6).getPrenomsEleves();
							String sanction7 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(6).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction7=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction7.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom7(indiscnom7);
								ficheCC.setSanction7(sanction7);
							}
							
						}
						
						//8eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>7){
							String indiscnom8 = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNomsEleves();
							indiscnom8+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(7).getPrenomsEleves();
							String sanction8 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(7).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction8=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction8.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom8(indiscnom8);
								ficheCC.setSanction8(sanction8);
							}
							
						}
						
						//9eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>8){
							String indiscnom9 = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNomsEleves();
							indiscnom9+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(8).getPrenomsEleves();
							String sanction9 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(8).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction9=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction9.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom9(indiscnom9);
								ficheCC.setSanction9(sanction9);
							}
							
						}
						
						//10eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>9){
							String indiscnom10 = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNomsEleves();
							indiscnom10+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(9).getPrenomsEleves();
							String sanction10 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(9).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNbreHeureAbsenceNonJustifieTrim(trimestre);
									if(nbreHANJ>0){
										sanction10=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction10.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom10(indiscnom10);
								ficheCC.setSanction10(sanction10);
							}
							
						}
			
		
		return ficheCC;
	
		
	}
	
	@Override
	public Map<String, Object> generateCollectionofBulletinTrimestre_opt(Long idClasse, Long idTrimestre) {

		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinTrimestre_opt avec "
				+ "idClasse= "+idClasse+" idTrimestre="+idTrimestre);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		 long startTime = System.currentTimeMillis();
		 
		 //java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		 
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestre);
			
		 List<BulletinTrimestreBean> collectionofBulletionTrimestre = new ArrayList<BulletinTrimestreBean>();
		
		 if((classeConcerne==null) || (trimestreConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueTrim = ub.getListOfCoursEvalueDansTrimestre(classeConcerne, 
					trimestreConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans un trimestre
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			
			RapportTrimestrielClasse rapportTrimestrielClasse = ub.getRapportTrimestrielClasse(classeConcerne, 
					listofEleveRegulier, trimestreConcerne);
			
			double moyenne_premier_classe=0;
			double moyenne_dernier_classe =0;
			double tauxReussite=0;
			double moyenne_general = 0;
			int nbre_moyenne_classeSeq = 0;
		
			moyenne_premier_classe = rapportTrimestrielClasse.getValeurMoyennePremierDansTrim();
			moyenne_dernier_classe = rapportTrimestrielClasse.getValeurMoyenneDernierDansTrim();
			nbre_moyenne_classeSeq = rapportTrimestrielClasse.getNbreMoyennePourTrim();
			tauxReussite = rapportTrimestrielClasse.getTauxReussiteTrimestriel();
			moyenne_general = rapportTrimestrielClasse.getMoyenneGeneralTrimestre();
			
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseTrim = ub.geteffectifEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans le trimestre considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantTrimestre(classeConcerne, trimestreConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Trimestriellement
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trimestreConcerne);
			
			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque séquence dans le trimestre
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneSequentiel = new 
					HashMap<Long, List<Eleves>>();
			
			for(Sequences seq : trimestreConcerne.getListofsequence()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = (List<Eleves>) 
						ub.getMoyenneSequentielOrdreDecroissant1(classeConcerne, seq);
				
				mapofElevesOrdreDecroissantMoyenneSequentiel.put(seq.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneSeq);
				
			}
			
			for(Eleves eleve : listofEleveClasse){
				long startTimeFor = System.currentTimeMillis();
				
				BulletinTrimestreBean bulletinTrim = new BulletinTrimestreBean();
				/*
				 * Initialisons les premieres donnees du bulletin trimestriel
				 */
				/****
				 * Information d'entete du bulletin
				 */
				bulletinTrim.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
				bulletinTrim.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
				bulletinTrim.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
				bulletinTrim.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
				bulletinTrim.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
				bulletinTrim.setEtablissement_fr(etablissementConcerne.getNomsEtab());
				bulletinTrim.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
						etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
				bulletinTrim.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
				bulletinTrim.setDevise_fr(etablissementConcerne.getDeviseEtab());
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrim.setTitre_bulletin("Bulletin de note du trimestre "+trimestreConcerne.getNumeroTrim());
				}
				else{
					bulletinTrim.setTitre_bulletin("Report card of term "+trimestreConcerne.getNumeroTrim());
				}
				bulletinTrim.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
				bulletinTrim.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
				
				
				/***************
				 * Information personnel de l'élève
				 */
				bulletinTrim.setNumero(" "+eleve.getNumero(listofElevesClasse));
				bulletinTrim.setSexe(eleve.getSexeEleves());
				bulletinTrim.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
				bulletinTrim.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				bulletinTrim.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
				bulletinTrim.setLieu_naissance_eleve(eleve.getLieunaissEleves());
				bulletinTrim.setMatricule_eleve(eleve.getMatriculeEleves());
				bulletinTrim.setRedoublant(eleve.getRedoublant());
				bulletinTrim.setClasse_eleve(classeString);
				bulletinTrim.setProf_principal(profPrincipal);
				bulletinTrim.setEffectif_classe(effectifTotalClasse);
				bulletinTrim.setEffectif_presente(effectifRegulierClasseTrim);
				
				/*****
				 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
				 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
				 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
				 * on va essayer de charger le fichier correspondant avec la classe File de java.io
				 */
				File f=new File(photoElevesDir+eleve.getIdEleves());
				//System.err.println("est ce que le fichier existe "+f.exists());
				
				if(f.exists()==true){
					bulletinTrim.setPhoto(photoElevesDir+eleve.getIdEleves()); 
				}
				
				/********
				 * Informations sur les labels d'entete des notes du bulletin trimestriel
				 */
				for(Sequences seq : trimestreConcerne.getListofsequence()){
					if(seq.getNumeroSeq()%2==1){
						bulletinTrim.setLabel_note_1("N S"+seq.getNumeroSeq());
					}
					else{
						bulletinTrim.setLabel_note_2("N S"+seq.getNumeroSeq());
					}
				}
				
				bulletinTrim.setLabel_trimestre("N T"+trimestreConcerne.getNumeroTrim());
				bulletinTrim.setLabel_trim_x_coef("N T"+trimestreConcerne.getNumeroTrim()+"*Coef");
		
				/***********
				 * Information sur les totaux trimestriels
				 */
				
				double total_coef = ub.getSommeCoefCoursComposeTrimestre(eleve, trimestreConcerne);
				bulletinTrim.setTotal_coef(total_coef);
				
				double total_points = ub.getTotalPointsTrimestriel(eleve, trimestreConcerne);
				
				if(total_points>0){
					bulletinTrim.setTotal_points(total_points);
				}
				
				if(total_coef>0){
					double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
					if(moy_trim>=0)	bulletinTrim.setResult_moy_trim(moy_trim);
				}
				
				/***********
				 * Informations sur les resultats trimestriels de l'eleve
				 */
				bulletinTrim.setResult_tt_coef(total_coef);
				
				if(total_points>0){
					bulletinTrim.setResult_tt_points(total_points);
				}
				
				//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
				
				/*int rang = this.getRangTrimestrielEleveAuMoinsUneNote(classeConcerne, trimestreConcerne, 
						eleve);*/
				 
				 int rang = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
						 listofElevesOrdreDecroissantMoyenneTrimestriel);
				 
				if(rang>0){
					bulletinTrim.setResult_rang_trim(rang+"e");
					bulletinTrim.setR_rang_trim(rang+"e");
				}
				else{
					bulletinTrim.setResult_rang_trim("");
				}
				
				
				/*************************************************
				 * Informations sur le profil de la classe dans le trimestre
				 */
				if(moyenne_premier_classe>0){
					bulletinTrim.setMoy_premier(moyenne_premier_classe);
				}
				if(moyenne_dernier_classe>0){
					bulletinTrim.setMoy_dernier(moyenne_dernier_classe);
				}
				bulletinTrim.setNbre_moyennes(nbre_moyenne_classeSeq);
				if(tauxReussite>0){
					bulletinTrim.setTaux_reussite(tauxReussite);
				}
				if(moyenne_general>0){
					bulletinTrim.setMoy_gen_classe(moyenne_general);
				}
				
				
				/***********************
				 * Informations sur la conduite trimestriel de l'élève
				 */
				int nhaj = 0;
				int nhanj = 0;
				
				/*for(Sequences seq : trimestreConcerne.getListofsequence()){
					RapportDAbsence rabs = eleve.getRapportDAbsenceSeq(seq.getIdPeriodes());
					if(rabs!=null){
						nhaj = nhaj + rabs.getNbreheureJustifie();
						nhanj = nhanj + rabs.getNbreheureNJustifie();
						nhc = nhc + rabs.getConsigne();
						nje = nje + rabs.getJourExclusion();
					}
				}*/
				
				nhanj = eleve.getNbreHeureAbsenceNonJustifieTrim(trimestreConcerne);
				nhaj = eleve.getNbreHeureAbsenceJustifieTrim(trimestreConcerne);
				
				bulletinTrim.setAbsence_NJ(nhanj);
				bulletinTrim.setAbsence_J(nhaj);
				bulletinTrim.setConsigne("");
				bulletinTrim.setExclusion("");
				bulletinTrim.setAvertissement("");
				bulletinTrim.setBlame_conduite("");
				
				/************************
				 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(trimestre)
				 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
				 * ete infligées. On va commencer de la séquence paire vers la séquence impair à chercher
				 * Il est important de noter qu'il s'agit des sanctions déjà exécutées pendant la période. 
				 */
				bulletinTrim.setRapport_disc1("");
				bulletinTrim.setRapport_disc2("");
				bulletinTrim.setRapport_disc3("");
				for(Sequences seq : trimestreConcerne.getListofsequence_DESC()){
					List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
					
					if(listofRDiscEleveSeq != null){
						if(listofRDiscEleveSeq.size()>0) {
							RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							//On peut donc fixer rapport_disc1
							bulletinTrim.setRapport_disc1(rdisc_chaine);
						}
						
						/*
						 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
						 * on va set rapport_disc2
						 */
						if(listofRDiscEleveSeq.size()>1) {

							RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							//On peut donc fixer rapport_disc1
							bulletinTrim.setRapport_disc2(rdisc_chaine);
						
						}
						
						if(listofRDiscEleveSeq.size()>2) {

							RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							
							//On peut donc fixer rapport_disc1
							bulletinTrim.setRapport_disc3(rdisc_chaine);
											
						}
						
					}
					
				}
				
				/**************************
				 * Informations sur le rappel de la moyenne et du rang trimestriel
				 */
				
				for(Sequences seq : trimestreConcerne.getListofsequence()){
					
					List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = 
							mapofElevesOrdreDecroissantMoyenneSequentiel.get(seq.getIdPeriodes());
					
					if(seq.getNumeroSeq()%2==1){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrim.setRappel_1("Séquence "+seq.getNumeroSeq());
						}
						else{
							bulletinTrim.setRappel_1("Sequence "+seq.getNumeroSeq());
						}
						
						double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
						
						if(moy_seq>0){
							bulletinTrim.setR_moy_1(moy_seq);
						}
						
						int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneSeq);
						
						if(rangseq>0){
							bulletinTrim.setR_rang_1(rangseq+"e");
						}
						else{
							bulletinTrim.setR_rang_1("");
						}
					}
					else{
						
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrim.setRappel_2("Séquence "+seq.getNumeroSeq());
						}
						else{
							bulletinTrim.setRappel_2("Sequence "+seq.getNumeroSeq());
						}
						
						double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
						
						if(moy_seq>0){
							bulletinTrim.setR_moy_2(moy_seq);
						}
						int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneSeq);
						
						if(rangseq>0){
							bulletinTrim.setR_rang_2(rangseq+"e");
						}
						else{
							bulletinTrim.setR_rang_2("");
						}
					}
					
				}//fin du for sur les sequences
				
				
				
				
				/****************************
				 * Informations sur l'appreciation du travail de l'élève
				 */
				double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
				
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrim.setRappel_3("Trimestre "+trimestreConcerne.getNumeroTrim());
				}
				else{
					bulletinTrim.setRappel_3("Term "+trimestreConcerne.getNumeroTrim());
				}
				
				if(moy_trim>=0) bulletinTrim.setR_moy_trim(moy_trim);
				
				bulletinTrim.setTableau_hon("");
				bulletinTrim.setTableau_enc("");
				bulletinTrim.setTableau_fel("");
				String appreciation = ub.calculAppreciation(moy_trim,lang);
				bulletinTrim.setAppreciation(appreciation);

				
				/*
				 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
				 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
				 */
				DecisionConseil decConseil = eleve.getDecisionConseilPeriode(trimestreConcerne.getIdPeriodes());
				bulletinTrim.setDistinction("");
				bulletinTrim.setDecision_conseil("");
				if(decConseil !=null){
					/*******************************
					 * Informations sur les distinctions octroyées  dans la séquence
					 */
					String distinction="";
					distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
					bulletinTrim.setDistinction(distinction);
					
					/*******************************
					 * Informations sur les decision du conseil de classe dans la séquence
					 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
					 * de classe.
					 */
					String decision="";
					decision += decConseil.getSanctionDiscDecisionConseilString(lang);
					/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
					decision+=distinction;*/
					bulletinTrim.setDecision_conseil(decision);
				}
				
				
				
				List<Cours> listofCoursEffortAFournir = 
						ub.getListofCoursDansOrdreEffortAFournirTrimestre(eleve, listofCoursEvalueTrim, 
						trimestreConcerne);
				bulletinTrim.setEffort_matiere1("");
				bulletinTrim.setEffort_matiere2("");
				bulletinTrim.setEffort_matiere3("");
				if(listofCoursEffortAFournir.size()>0) {
					String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
					bulletinTrim.setEffort_matiere1(codeCours);
				}
				
				
				if(listofCoursEffortAFournir.size()>1) {
					String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
					bulletinTrim.setEffort_matiere2(codeCours);
				}
				
				if(listofCoursEffortAFournir.size()>2) {
					String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
					bulletinTrim.setEffort_matiere3(codeCours);
				}
				
				
				
				/*****************************
				 * Information sur l'espace VISA du bulletin
				 */
				bulletinTrim.setVille(villeEtab);
				
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres scientifique (Groupe1) dans le trimestre
				 * cccccccccccccccccccccccccc
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursScientifique = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursScientifique, trimestreConcerne);
				
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrim.setNom_g1("Scientifique");
				}
				else{
					bulletinTrim.setNom_g1("Scientific");
				}
				
				double total_coef_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
				
				bulletinTrim.setTotal_coef_g1(total_coef_g1);
				//System.err.println("total_coef_g1 == "+total_coef_g1);
				
				double total_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalNoteTrimElevePourGroupeCours();
				
				if(total_g1>0){
					bulletinTrim.setTotal_g1(total_g1);
				}
				
				String totalextreme_g1 = "";
				
				double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursScientifique, trimestreConcerne);
				
				double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursScientifique, trimestreConcerne);
				
				if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
					totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
							valeurMoyPremierGrpCours1+"]";
				}
				bulletinTrim.setTotal_extreme_g1(totalextreme_g1);
				
				int r1 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursScientifique, 
						trimestreConcerne, eleve);
				
				if(r1>0){
					bulletinTrim.setTotal_rang_g1(r1+"e");
				}
				
				
				double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursScientifique, trimestreConcerne);
				if(moy_gen_grp1>0){
					bulletinTrim.setMg_classe_g1(moy_gen_grp1);
				}
				
				double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursScientifique, trimestreConcerne);
				
				if(total_pourcentage_g1>=0){
					bulletinTrim.setTotal_pourcentage_g1(total_pourcentage_g1);
				}
				
				
				
				double moyenne_g1 = ligneTrimestrielGroupeCoursScientifique.
						getMoyenneTrimElevePourGroupeCours();
				
				if(moyenne_g1>0){
					bulletinTrim.setMoyenne_g1(moyenne_g1);
				}
		
				
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Litteraire (Groupe2) dans le trimestre
				 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursLitteraire = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursLitteraire, trimestreConcerne);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrim.setNom_g2("Litteraire");
				}
				else{
					bulletinTrim.setNom_g2("Arts");
				}
				
				double total_coef_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
				
				bulletinTrim.setTotal_coef_g2(total_coef_g2);
				
				double total_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalNoteTrimElevePourGroupeCours();
				
				if(total_g2>0){
					bulletinTrim.setTotal_g2(total_g2);
				}
				
				String totalextreme_g2 = "";
				
				double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
				
				double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
				
				if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
					totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
							valeurMoyPremierGrpCours2+"]";
				}
				bulletinTrim.setTotal_extreme_g2(totalextreme_g2);
				
				int r2 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursLitteraire, 
						trimestreConcerne, eleve);
				
				if(r2>0){
					bulletinTrim.setTotal_rang_g2(r2+"e");
				}
				
				
				double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursLitteraire, trimestreConcerne);
				
				if(moy_gen_grp2>0){
					bulletinTrim.setMg_classe_g2(moy_gen_grp2);
				}
				
				double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursLitteraire, trimestreConcerne);
				
				if(total_pourcentage_g2>=0){
					bulletinTrim.setTotal_pourcentage_g2(total_pourcentage_g2);
				}
				
				double moyenne_g2 = ligneTrimestrielGroupeCoursLitteraire.
						getMoyenneTrimElevePourGroupeCours();
				
				if(moyenne_g2>0){
					bulletinTrim.setMoyenne_g2(moyenne_g2);
				}
		
				

				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Divers (Groupe3) dans le trimestre
				 * ddddddddddddddddddddddddddddddddddd
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursDivers = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursDivers, trimestreConcerne);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrim.setNom_g3("Divers");
				}
				else{
					bulletinTrim.setNom_g3("Orthers");
				}
				
				double total_coef_g3 = ligneTrimestrielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
				
				bulletinTrim.setTotal_coef_g3(total_coef_g3);
				
				double total_g3 = ligneTrimestrielGroupeCoursDivers.getTotalNoteTrimElevePourGroupeCours();
				
				if(total_g3>0){
					bulletinTrim.setTotal_g3(total_g3);
				}
				
				String totalextreme_g3 = "";
				
				double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursDivers, trimestreConcerne);
				
				double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursDivers, trimestreConcerne);
				
				if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
					totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
							valeurMoyPremierGrpCours3+"]";
				}
				bulletinTrim.setTotal_extreme_g3(totalextreme_g3);
				
				int r3 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursDivers, 
						trimestreConcerne, eleve);
				
				if(r3>0){
					bulletinTrim.setTotal_rang_g3(r3+"e");
				}
				
				
				double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursDivers, trimestreConcerne);
				
				if(moy_gen_grp3>0){
					bulletinTrim.setMg_classe_g3(moy_gen_grp3);
				}
				
				double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursDivers, trimestreConcerne);
				if(total_pourcentage_g3>=0){
					bulletinTrim.setTotal_pourcentage_g3(total_pourcentage_g3);
				}
				
				
				
				double moyenne_g3 = ligneTrimestrielGroupeCoursDivers.
						getMoyenneTrimElevePourGroupeCours();
				
				if(moyenne_g3>0){
					bulletinTrim.setMoyenne_g3(moyenne_g3);
				}
				

				/************************************
				 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
				 **********/
				
				
				List<MatiereGroupe1TrimestreBean> listofCoursScientifiqueTrimestreBean 
							= new ArrayList<MatiereGroupe1TrimestreBean>(); 
				
				int rc1 = 0;
				/***
				 * debut du for sur les cours scientifique
				 * Gestion des cours scientifique
				 */
				for(Cours cours : listofCoursScientifique){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe1TrimestreBean mGrp1TrimBean = new MatiereGroupe1TrimestreBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 15);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
					
					mGrp1TrimBean.setMatiere_g1(matiere);
					mGrp1TrimBean.setMatiere_g1_2emelang(matiere_2emelang);
					mGrp1TrimBean.setProf_g1(nomProf);
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g1>0){
								mGrp1TrimBean.setNote_1_g1(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g2>0){
								mGrp1TrimBean.setNote_2_g1(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						
						mGrp1TrimBean.setNote_trim_g1(noteCoursTrim);
						double total_trim_g1 = noteCoursTrim*cours.getCoefCours();
						mGrp1TrimBean.setTotal_trim_g1(total_trim_g1);
						//System.out.println("Calculss "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g1);
					}
					
					mGrp1TrimBean.setCoef_g1(cours.getCoefCours());
					
					String extreme_g1 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g1 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp1TrimBean.setExtreme_g1(extreme_g1);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc1 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc1>0){
						mGrp1TrimBean.setRang_g1(rc1+"e");
					}
					
					
					double moy_classe_g1 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					
					if(moy_classe_g1>0){
						mGrp1TrimBean.setMoy_classe_g1(moy_classe_g1);
					}
					
					
					double pourcentage_g1 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(pourcentage_g1>=0){
						mGrp1TrimBean.setPourcentage_g1(pourcentage_g1);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp1TrimBean.setAppreciation_g1(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursScientifiqueTrimestreBean.add(mGrp1TrimBean);
					
				}
				/****
					fin du for sur les cours scientifique qui passe dans la classe
				*****/
				
				//On place la liste des matieres scientifique construit
				bulletinTrim.setMatieresGroupe1Trimestre(listofCoursScientifiqueTrimestreBean);
				
				
				List<MatiereGroupe2TrimestreBean> listofCoursLitteraireTrimestreBean 
				= new ArrayList<MatiereGroupe2TrimestreBean>(); 
	
				int rc2 = 0;
				/***
				 * debut du for sur les cours Litteraire
				 * Gestion des cours Litteraire
				 */
				for(Cours cours : listofCoursLitteraire){
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe2TrimestreBean mGrp2TrimBean = new MatiereGroupe2TrimestreBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 14);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);

					
					mGrp2TrimBean.setMatiere_g2(matiere);
					mGrp2TrimBean.setMatiere_g2_2emelang(matiere_2emelang);
					mGrp2TrimBean.setProf_g2(nomProf);
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					

					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g1>0){
								mGrp2TrimBean.setNote_1_g2(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								/*System.out.println(cours.getCodeCours()+" seq "+seq.getNumeroSeq()+" "+"note_seq_g1 = "+note_seq_g1+" "
										+ "et somme = "+soenoteTrim);*/
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g2>0){
								mGrp2TrimBean.setNote_2_g2(note_seq_g2);
								
								soenoteTrim = soenoteTrim + note_seq_g2;
								/*System.out.println(cours.getCodeCours()+" seq "+seq.getNumeroSeq()+" note_seq_g2 = "+note_seq_g2+" "
										+ "et somme = "+soenoteTrim);*/
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						
						mGrp2TrimBean.setNote_trim_g2(noteCoursTrim);
						double total_trim_g2 = noteCoursTrim*cours.getCoefCours();
						mGrp2TrimBean.setTotal_trim_g2(total_trim_g2);
						//System.out.println("Calculss "+cours.getCodeCours()+" "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g2);
					}
					
					mGrp2TrimBean.setCoef_g2(cours.getCoefCours());
					String extreme_g2 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g2 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp2TrimBean.setExtreme_g2(extreme_g2);
					}
					
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc2 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc2>0){
						mGrp2TrimBean.setRang_g2(rc2+"e");
					}
					
					
					double moy_classe_g2 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					
					if(moy_classe_g2>0){
						mGrp2TrimBean.setMoy_classe_g2(moy_classe_g2);
					}
					
					
					double pourcentage_g2 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					
					if(pourcentage_g2>=0){
						mGrp2TrimBean.setPourcentage_g2(pourcentage_g2);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp2TrimBean.setAppreciation_g2(appreciationNote);
					

					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursLitteraireTrimestreBean.add(mGrp2TrimBean);
					
					
				}
				/****
					fin du for sur les cours litteraire qui passe dans la classe
				 *****/
			
			//On place la liste des matieres scientifique construit
			bulletinTrim.setMatieresGroupe2Trimestre(listofCoursLitteraireTrimestreBean);
			
				
		
			

			List<MatiereGroupe3TrimestreBean> listofCoursDiversTrimestreBean 
			= new ArrayList<MatiereGroupe3TrimestreBean>(); 

			int rc3 = 0;
			/***
			 * debut du for sur les cours Divers
			 * Gestion des cours Divers
			 */
			for(Cours cours : listofCoursDivers){
				//long debutforCoursTime = System.currentTimeMillis();
				
				MatiereGroupe3TrimestreBean mGrp3TrimBean = new MatiereGroupe3TrimestreBean();
				
				
				
				RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
						classeConcerne, cours, trimestreConcerne);
				
				String matiere = ub.subString(cours.getIntituleCours(), 14);
				matiere = matiere + ":";
				String codeMat = ub.subString(cours.getCodeCours(), 5);
				matiere = matiere + codeMat;
				
				String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
				
				String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
				nomProf = ub.subString(nomProf, 15);

				
				mGrp3TrimBean.setMatiere_g3(matiere);
				mGrp3TrimBean.setMatiere_g3_2emelang(matiere_2emelang);
				mGrp3TrimBean.setProf_g3(nomProf);
				
				
				double soenoteTrim = 0;
				int nbreNoteDansTrimPourCours = 0;
				

				for(Sequences seq : trimestreConcerne.getListofsequence()){
					if(seq.getNumeroSeq()%2==1){
						double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
						
						if(note_seq_g1>0){
							mGrp3TrimBean.setNote_1_g3(note_seq_g1);
							soenoteTrim = soenoteTrim + note_seq_g1;
							nbreNoteDansTrimPourCours +=1; 
						}
					}
					else{
						double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
						
						if(note_seq_g2>0){
							mGrp3TrimBean.setNote_2_g3(note_seq_g2);
							soenoteTrim = soenoteTrim + note_seq_g2;
							nbreNoteDansTrimPourCours +=1; 
						}
					}
				}
				
				double noteCoursTrim = 0;
				if(nbreNoteDansTrimPourCours>0){
					noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
					
					mGrp3TrimBean.setNote_trim_g3(noteCoursTrim);
					double total_trim_g3 = noteCoursTrim*cours.getCoefCours();
					mGrp3TrimBean.setTotal_trim_g3(total_trim_g3);
					//System.out.println("Calculss "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g3);
				}
				
				mGrp3TrimBean.setCoef_g3(cours.getCoefCours());
				String extreme_g3 = "";
				double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
				double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
				
				if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
					extreme_g3 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
					mGrp3TrimBean.setExtreme_g3(extreme_g3);
				}
				
				
				List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
				
				rc3 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
						listofEleveOrdreDecroissantPourCours);
				
				if(rc3>0){
					mGrp3TrimBean.setRang_g3(rc3+"e");
				}
				
				
				double moy_classe_g3 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
						cours, trimestreConcerne);
				
				if(moy_classe_g3>0){
					mGrp3TrimBean.setMoy_classe_g3(moy_classe_g3);
				}
				
				
				double pourcentage_g3 = ub.getTauxReussiteCoursTrim(classeConcerne, 
						cours, trimestreConcerne);
				
				if(pourcentage_g3>=0){
					mGrp3TrimBean.setPourcentage_g3(pourcentage_g3);
				}
				
				String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
				mGrp3TrimBean.setAppreciation_g3(appreciationNote);
				

				//On ajoute la ligne de cours dans le groupe correspondant
				listofCoursDiversTrimestreBean.add(mGrp3TrimBean);
				
				
			}
			/****
				fin du for sur les cours Divers qui passe dans la classe
			 *****/
		
		//On place la liste des matieres scientifique construit
		bulletinTrim.setMatieresGroupe3Trimestre(listofCoursDiversTrimestreBean);
		
		/*double moy_trimm = this.getMoyenneTrimestriel(eleve, trimestreConcerne);
		System.err.println("moy_trimm de "+eleve.getNomsEleves()+" est de "+moy_trimm);*/
				
				
				
				long finforTime = System.currentTimeMillis();
				collectionofBulletionTrimestre.add(bulletinTrim);
				System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
						" dans le trimestre "+trimestreConcerne.getNumeroTrim()+
						"  ajouter avec succes en "+(finforTime-startTimeFor));
				numBull++;
				
				
			}//fin du for sur les eleves pour les Bulletins sequentiels
			
			
			long finforTime = System.currentTimeMillis();
			System.out.println("Version opt A ce stade on a deja passe :"+ (finforTime-startTime));
			
			
			donnee.put("collectionofBulletionTrimestre", collectionofBulletionTrimestre);
			
			/*******
			 * Conception du rapport de conseil de classe trimestriel
			 */
			FicheConseilClasseBean ficheCC = this.getRapportConseilClasseTrimestriel(etablissementConcerne, 
					anneeScolaire, classeConcerne, tauxReussite, moyenne_general, trimestreConcerne, 
					listofElevesOrdreDecroissantMoyenneTrimestriel);
			
			donnee.put("ficheconseilclassetrimestriel", ficheCC);

		return donnee;
		
	}

	
	public FicheConseilClasseBean getRapportConseilClasseAnnuel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			List<Eleves> listofElevesOrdreDecroissantMoyenneAnnuel){

		
		/*log.log(Level.DEBUG, "Lancement de la methode getRapportConseilClasseAnnuel ");*/
		FicheConseilClasseBean ficheCC = new FicheConseilClasseBean();
		
		String lang="";
		if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
			lang="fr";
		}
		else{
			lang="en";
		}
		/****
		 * Information d'entete du rapport de conseil de classe
		 */
		
		ficheCC.setDelegation_fr(etab.getDeleguationdeptuteleEtab());
		ficheCC.setDelegation_en(etab.getDeleguationdeptuteleanglaisEtab());
		ficheCC.setEtablissement_fr(etab.getNomsEtab());
		ficheCC.setEtablissement_en(etab.getNomsanglaisEtab());
		ficheCC.setAdresse("BP "+etab.getBpEtab()+"/"+
				etab.getNumtel1Etab()+"/"+etab.getEmailEtab());
		ficheCC.setAnnee_scolaire_en("School year "+annee.getIntituleAnnee());
		String nomClasse = classe.getCodeClasses()+""+
				classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses();
		ficheCC.setClasse(nomClasse);
		ficheCC.setAnnee_scolaire_fr("Année scolaire "+annee.getIntituleAnnee());
		ficheCC.setMinistere_fr(etab.getMinisteretuteleEtab());
		ficheCC.setMinistere_en(etab.getMinisteretuteleanglaisEtab());
		ficheCC.setDevise_en(etab.getDeviseanglaisEtab());
		ficheCC.setDevise_fr(etab.getDeviseEtab());
		
		ficheCC.setAnnee(annee.getIntituleAnnee());
		if(lang.equalsIgnoreCase("fr")==true){
			ficheCC.setPeriode("Année scolaire "+annee.getIntituleAnnee());
			String titre_fiche = "CONSEIL DE CLASSE DE L'ANNEE: "+annee.getIntituleAnnee();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		else{
			ficheCC.setPeriode("School year "+annee.getIntituleAnnee());
			String titre_fiche = "CLASS COUNCIL OF YEAR: "+annee.getIntituleAnnee();
			ficheCC.setTitre_fiche(titre_fiche);
		}
		
		String profPrincipal = "";
		if(classe.getProffesseur()!=null){
			profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
			profPrincipal=profPrincipal.toUpperCase();
		}
		
		ficheCC.setEnseignant(profPrincipal);
		String nonClasse = classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+
				classe.getNumeroClasses();		
		ficheCC.setClasse(nonClasse);

		int g_ins =  0;
		int f_ins = 0;
		int t_ins = 0;
		List<Integer> listofEffectif = new ArrayList<Integer>();
		listofEffectif = ub.geteffectifSexeDansClasse(classe);
		t_ins = listofEffectif.get(0) + listofEffectif.get(1);
		if(listofEffectif.size() == 2){
			g_ins = listofEffectif.get(0);
			f_ins = listofEffectif.get(1);
		}
		ficheCC.setG_ins(g_ins);
		ficheCC.setF_ins(f_ins);
		ficheCC.setT_ins(t_ins);
		
		
		int g_pre =  0;
		int f_pre = 0;
		int t_pre = 0;
		List<Integer> listofEffectifRegulier = new ArrayList<Integer>();
		listofEffectifRegulier = ub.geteffectifRegulierSexeDansClasse(classe, annee);
		t_pre = listofEffectifRegulier.get(0) + listofEffectifRegulier.get(1);
		if(listofEffectif.size() == 2){
			g_pre = listofEffectifRegulier.get(0);
			f_pre = listofEffectifRegulier.get(1);
		}
		ficheCC.setG_pre(g_pre);
		ficheCC.setF_pre(f_pre);
		ficheCC.setT_pre(t_pre);


		//Preparation des données du sous_rapport1_conseil
		List<SousRapport1ConseilBean> listofSousRapport1ConseilBean = 
				ub.getListofSousRapport1Conseil(classe, annee);
		ficheCC.setSous_rapport1_conseil(listofSousRapport1ConseilBean);
		//System.err.println("listofSousRapport1ConseilBean  "+listofSousRapport1ConseilBean.size());
		//resultat d'ensemble
		int g_classe = 0;
		int f_classe = 0;
		int t_classe = 0;
		int g_nonclasse = 0;
		int f_nonclasse = 0;
		int t_nonclasse = 0;
		Map<String, Object> mapofEff  = ub.getEffectifMoyenneSexeClasse(
				listofElevesOrdreDecroissantMoyenneAnnuel, annee);
		
		int nbreMoyG5 = (Integer)mapofEff.get("nbreMoyG5"); //nombre de garcon avec une moyenne < à 5
		int nbreMoyG7 = (Integer)mapofEff.get("nbreMoyG7"); //nombre de garcon avec une moyenne comprise entre >=5 et <7
		int nbreMoyG8 = (Integer)mapofEff.get("nbreMoyG8");//>=7 et <8
		int nbreMoyG9 = (Integer)mapofEff.get("nbreMoyG9");//>=7 et <8
		int nbreMoyG10 = (Integer)mapofEff.get("nbreMoyG10");//>=9 et <10
		int nbreMoyG12 = (Integer)mapofEff.get("nbreMoyG12");
		//int nbreMoyG13 = 0;
		int nbreMoyG14 = (Integer)mapofEff.get("nbreMoyG14");
		int nbreMoyG20 = (Integer)mapofEff.get("nbreMoyG20");
		
		g_classe = nbreMoyG5+nbreMoyG7+nbreMoyG8+nbreMoyG9+nbreMoyG10+nbreMoyG12+
				nbreMoyG14+nbreMoyG20;
		g_nonclasse = g_ins - g_classe;
		int g_nbremoy = nbreMoyG12+nbreMoyG14+nbreMoyG20;
		
		int nbreMoyF5 = (Integer)mapofEff.get("nbreMoyF5"); //nombre de fille avec une moyenne < à 5
		int nbreMoyF7 = (Integer)mapofEff.get("nbreMoyF7"); //nombre de fille avec une moyenne comprise entre >=5 et <7
		int nbreMoyF8 = (Integer)mapofEff.get("nbreMoyF8");//>=7 et <8
		int nbreMoyF9 = (Integer)mapofEff.get("nbreMoyF9");//>=8 et <9
		int nbreMoyF10 = (Integer)mapofEff.get("nbreMoyF10");//>=9 et <10
		int nbreMoyF12 = (Integer)mapofEff.get("nbreMoyF12");
		//int nbreMoyF13 = 0;
		int nbreMoyF14 = (Integer)mapofEff.get("nbreMoyF14");
		int nbreMoyF20 = (Integer)mapofEff.get("nbreMoyF20");
		
		f_classe = nbreMoyF5+nbreMoyF7+nbreMoyF8+nbreMoyF9+nbreMoyF10+nbreMoyF12+
				nbreMoyF14+nbreMoyF20;
		f_nonclasse = f_ins - f_classe;
		int f_nbremoy = nbreMoyF12+nbreMoyF14+nbreMoyF20;
		
		t_nonclasse = g_nonclasse + f_nonclasse;
		int t_nbremoy = f_nbremoy + g_nbremoy;
		
		double pourCG = (Double)mapofEff.get("pourCG");
		
		double pourCF = (Double)mapofEff.get("pourCF");
		
		ficheCC.setG_classe(g_classe);
		ficheCC.setF_classe(f_classe);
		t_classe = g_classe + f_classe;
		ficheCC.setT_classe(t_classe);
		ficheCC.setG_nonclasse(g_nonclasse);
		ficheCC.setF_nonclasse(f_nonclasse);
		ficheCC.setT_nonclasse(t_nonclasse);
		ficheCC.setG_nbremoy(g_nbremoy);
		ficheCC.setF_nbremoy(f_nbremoy);
		ficheCC.setT_nbremoy(t_nbremoy);
		ficheCC.setG_pourcentage(pourCG);
		ficheCC.setF_pourcentage(pourCF);
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try{
			tauxReussite=df.parse(df.format(tauxReussite)).doubleValue();
			moyenne_general=df.parse(df.format(moyenne_general)).doubleValue();
		}
		catch(Exception e){
			
		}*/
		int nb_decimale = 3;
		tauxReussite = this.ub.tronqueDouble(tauxReussite, nb_decimale);
		moyenne_general = this.ub.tronqueDouble(moyenne_general, nb_decimale);
		ficheCC.setT_pourcentage(tauxReussite);
		ficheCC.setMg_classe(moyenne_general);
		
		//Classement
				String nom_1ere ="";
				String nom_2eme ="";
				String nom_3eme ="";
				String nom_4eme ="";
				String nom_5eme ="";
				
				double moy_1ere =0.0;
				double moy_2eme =0.0;
				double moy_3eme =0.0;
				double moy_4eme =0.0;
				double moy_5eme =0.0;
				
				
				int nbreMoyCalcule = listofElevesOrdreDecroissantMoyenneAnnuel.size();
				if(nbreMoyCalcule>=5){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneAnnuel(eleve1, annee);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneAnnuel(eleve2, annee);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneAnnuel(eleve3, annee);
					
					Eleves eleve4 = listofElevesOrdreDecroissantMoyenneAnnuel.get(3);
					nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
					moy_4eme = ub.getMoyenneAnnuel(eleve4, annee);
					
					Eleves eleve5 = listofElevesOrdreDecroissantMoyenneAnnuel.get(4);
					nom_5eme = eleve5.getNomsEleves()+" "+eleve5.getPrenomsEleves();
					moy_5eme = ub.getMoyenneAnnuel(eleve5, annee);
				}
				else if(nbreMoyCalcule>=4){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneAnnuel(eleve1, annee);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneAnnuel(eleve2, annee);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneAnnuel(eleve3, annee);
					
					Eleves eleve4 = listofElevesOrdreDecroissantMoyenneAnnuel.get(3);
					nom_4eme = eleve4.getNomsEleves()+" "+eleve4.getPrenomsEleves();
					moy_4eme = ub.getMoyenneAnnuel(eleve4, annee);
				}
				else if(nbreMoyCalcule>=3){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneAnnuel(eleve1, annee);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneAnnuel(eleve2, annee);
					
					Eleves eleve3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(2);
					nom_3eme = eleve3.getNomsEleves()+" "+eleve3.getPrenomsEleves();
					moy_3eme = ub.getMoyenneAnnuel(eleve3, annee);
				}
				else if(nbreMoyCalcule>=2){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneAnnuel(eleve1, annee);
					
					Eleves eleve2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(1);
					nom_2eme = eleve2.getNomsEleves()+" "+eleve2.getPrenomsEleves();
					moy_2eme = ub.getMoyenneAnnuel(eleve2, annee);
				}
				else if(nbreMoyCalcule>=1){
					Eleves eleve1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(0);
					nom_1ere = eleve1.getNomsEleves()+" "+eleve1.getPrenomsEleves();
					moy_1ere = ub.getMoyenneAnnuel(eleve1, annee);
				}
				
				ficheCC.setNom_1ere(nom_1ere);
				if(moy_1ere>0) ficheCC.setMoy_1ere(moy_1ere);
				ficheCC.setNom_2eme(nom_2eme);
				if(moy_2eme>0) ficheCC.setMoy_2eme(moy_2eme);
				ficheCC.setNom_3eme(nom_3eme);
				if(moy_3eme>0) ficheCC.setMoy_3eme(moy_3eme);
				ficheCC.setNom_4eme(nom_4eme);
				if(moy_4eme>0) ficheCC.setMoy_4eme(moy_4eme);
				ficheCC.setNom_5eme(nom_5eme);
				if(moy_5eme>0) ficheCC.setMoy_5eme(moy_5eme);
				
				String nom_dernier1 = "";
				String nom_dernier2 = "";
				String nom_dernier3 = "";
				String nom_dernier4 = "";
				String nom_dernier5 = "";
				
				String rang_dernier1 ="";
				String rang_dernier2 ="";
				String rang_dernier3 ="";
				String rang_dernier4 ="";
				String rang_dernier5 ="";
				
				double moy_dernier1 =0.0;
				double moy_dernier2 =0.0;
				double moy_dernier3 =0.0;
				double moy_dernier4 =0.0;
				double moy_dernier5 =0.0;
				
				if(nbreMoyCalcule>5){
					int nbredeDernier = nbreMoyCalcule-5;
					if(nbredeDernier>=5){
						Eleves eleved5 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-1);
						nom_dernier5 = eleved5.getNomsEleves()+" "+eleved5.getPrenomsEleves();
						rang_dernier5 = ""+(nbreMoyCalcule);
						moy_dernier5 = ub.getMoyenneAnnuel(eleved5, annee);
						
						Eleves eleved4 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-2);
						nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
						rang_dernier4 = ""+(nbreMoyCalcule-1);
						moy_dernier4 = ub.getMoyenneAnnuel(eleved4, annee);
						
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-3);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule-2);
						moy_dernier3 = ub.getMoyenneAnnuel(eleved3, annee);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-4);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-3);
						moy_dernier2 = ub.getMoyenneAnnuel(eleved2, annee);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-5);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-4);
						moy_dernier1 = ub.getMoyenneAnnuel(eleved1, annee);
					}
					else if(nbredeDernier>=4){
						Eleves eleved4 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-1);
						nom_dernier4 = eleved4.getNomsEleves()+" "+eleved4.getPrenomsEleves();
						rang_dernier4 = ""+(nbreMoyCalcule);
						moy_dernier4 = ub.getMoyenneAnnuel(eleved4, annee);
						
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-2);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule-1);
						moy_dernier3 = ub.getMoyenneAnnuel(eleved3, annee);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-3);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-2);
						moy_dernier2 = ub.getMoyenneAnnuel(eleved2, annee);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-4);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-3);
						moy_dernier1 = ub.getMoyenneAnnuel(eleved1, annee);
					}
					else if(nbredeDernier>=3){
						Eleves eleved3 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-1);
						nom_dernier3 = eleved3.getNomsEleves()+" "+eleved3.getPrenomsEleves();
						rang_dernier3 = ""+(nbreMoyCalcule);
						moy_dernier3 = ub.getMoyenneAnnuel(eleved3, annee);
						
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-2);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule-1);
						moy_dernier2 = ub.getMoyenneAnnuel(eleved2, annee);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-3);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-2);
						moy_dernier1 = ub.getMoyenneAnnuel(eleved1, annee);
					}
					else if(nbredeDernier>=2){
						Eleves eleved2 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-1);
						nom_dernier2 = eleved2.getNomsEleves()+" "+eleved2.getPrenomsEleves();
						rang_dernier2 = ""+(nbreMoyCalcule);
						moy_dernier2 = ub.getMoyenneAnnuel(eleved2, annee);
						
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-2);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule-1);
						moy_dernier1 = ub.getMoyenneAnnuel(eleved1, annee);
					}
					else if(nbredeDernier>=1){
						Eleves eleved1 = listofElevesOrdreDecroissantMoyenneAnnuel.get(nbreMoyCalcule-1);
						nom_dernier1 = eleved1.getNomsEleves()+" "+eleved1.getPrenomsEleves();
						rang_dernier1 = ""+(nbreMoyCalcule);
						moy_dernier1 = ub.getMoyenneAnnuel(eleved1, annee);
					}
					
				}
				
				ficheCC.setRang_dernier1(rang_dernier1);
				ficheCC.setNom_dernier1(nom_dernier1);
				if(moy_dernier1>0)	ficheCC.setMoy_dernier1(moy_dernier1);
				
				ficheCC.setRang_dernier2(rang_dernier2);
				ficheCC.setNom_dernier2(nom_dernier2);
				if(moy_dernier2>0)	ficheCC.setMoy_dernier2(moy_dernier2);
				
				ficheCC.setRang_dernier3(rang_dernier3);
				ficheCC.setNom_dernier3(nom_dernier3);
				if(moy_dernier3>0)	ficheCC.setMoy_dernier3(moy_dernier3);
				
				ficheCC.setRang_dernier4(rang_dernier4);
				ficheCC.setNom_dernier4(nom_dernier4);
				if(moy_dernier4>0)	ficheCC.setMoy_dernier4(moy_dernier4);
				
				ficheCC.setRang_dernier5(rang_dernier5);
				ficheCC.setNom_dernier5(nom_dernier5);
				if(moy_dernier5>0)	ficheCC.setMoy_dernier5(moy_dernier5);
				
				ficheCC.setVille(etab.getVilleEtab());
				ficheCC.setEnseignant(profPrincipal);

				//Statistiques globales
				
				int nbre_moy5 = nbreMoyG5+nbreMoyF5;
				int nbre_moy7 = nbreMoyG7+nbreMoyF7;
				int nbre_moy8 = nbreMoyG8+nbreMoyF8;
				int nbre_moy9 = nbreMoyG9+nbreMoyF9;
				int nbre_moy10 = nbreMoyG10+nbreMoyF10;
				int nbre_moy12 = nbreMoyG12+nbreMoyF12;
				int nbre_moy14 = nbreMoyG14+nbreMoyF14;
				int nbre_moy15 = nbreMoyG20+nbreMoyF20;

				ficheCC.setNbre_moy5(nbre_moy5);
				ficheCC.setNbre_moy7(nbre_moy7);
				ficheCC.setNbre_moy8(nbre_moy8);
				ficheCC.setNbre_moy9(nbre_moy9);
				ficheCC.setNbre_moy10(nbre_moy10);
				ficheCC.setNbre_moy12(nbre_moy12);
				ficheCC.setNbre_moy14(nbre_moy14);
				ficheCC.setNbre_moy15(nbre_moy15);
				
				//Taux de reussite par discipline
				List<SousRapport2ConseilBean> sous_rapport2_conseil = ub.getListofSousRapport2Conseil(classe, annee);
				List<SousRapport3ConseilBean> sous_rapport3_conseil = ub.getListofSousRapport3Conseil(classe, annee);
				
				ficheCC.setSous_rapport2_conseil(sous_rapport2_conseil);
				ficheCC.setSous_rapport3_conseil(sous_rapport3_conseil);


				/*
				 * Il faut placer dans ce bean le total des absences par sexe pour le total entier
				 */
				int totalAbsF = usersService.getNbreAbsNJSexeClasseAn(classe, annee, 0);
				int totalAbsM = usersService.getNbreAbsNJSexeClasseAn(classe, annee, 1);
				int totalAbs = totalAbsF+totalAbsM;
				
				ficheCC.setTotalAbs(totalAbs);
				ficheCC.setTotalAbsF(totalAbsF);
				ficheCC.setTotalAbsM(totalAbsM);
				
				/*
				 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
				 */
				int n=10;
				List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = usersService.getListeElevePlusIndisciplineAnnee(classe, annee, n);
				
				//premier eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>0){
					String indiscnom1 = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNomsEleves();
					indiscnom1+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(0).getPrenomsEleves();
					String sanction1 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(0).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction1 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(0).getNbreHeureAbsenceNonJustifieAnnee(annee);
							if(nbreHANJ>0){
								sanction1=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction1.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom1(indiscnom1);
						ficheCC.setSanction1(sanction1);
					}
					
				}
				
				//deuxieme eleve le plus indiscipline
				if(listofEleveLesPlusIndisciDansClasseSeq.size()>1){
					String indiscnom2 = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNomsEleves();
					indiscnom2+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(1).getPrenomsEleves();
					String sanction2 = "aucune";
					List<RapportDisciplinaire> listofRappDiscEleve = 
							(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(1).getListofRDisc_DESC();
					
					if(listofRappDiscEleve!=null){
						if(listofRappDiscEleve.size()>0){
							if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
								sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
							}
							else{
								sanction2 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
							}
						}
						else{
							//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
							int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(1).getNbreHeureAbsenceNonJustifieAnnee(annee);
							if(nbreHANJ>0){
								sanction2=nbreHANJ+" H";
							}
						}
					}
					
					
					if(sanction2.equalsIgnoreCase("aucune")==false){
						ficheCC.setIndiscnom2(indiscnom2);
						ficheCC.setSanction2(sanction2);
					}
					
				}
				
				//3eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>2){
							String indiscnom3 = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNomsEleves();
							indiscnom3+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(2).getPrenomsEleves();
							String sanction3 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(2).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction3 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(2).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction3=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction3.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom3(indiscnom3);
								ficheCC.setSanction3(sanction3);
							}
							
						}
				
						//4eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>3){
							String indiscnom4 = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNomsEleves();
							indiscnom4+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(3).getPrenomsEleves();
							String sanction4 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(3).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction4 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(3).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction4=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction4.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom4(indiscnom4);
								ficheCC.setSanction4(sanction4);
							}
							
						}
						
						//5eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>4){
							String indiscnom5 = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNomsEleves();
							indiscnom5+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(4).getPrenomsEleves();
							String sanction5 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(4).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction5 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(4).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction5=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction5.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom5(indiscnom5);
								ficheCC.setSanction5(sanction5);
							}
							
						}
						
						//6eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>5){
							String indiscnom6 = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNomsEleves();
							indiscnom6+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(5).getPrenomsEleves();
							String sanction6 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(5).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction6 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(5).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction6=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction6.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom6(indiscnom6);
								ficheCC.setSanction6(sanction6);
							}
							
						}
						
						//7eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>6){
							String indiscnom7 = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNomsEleves();
							indiscnom7+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(6).getPrenomsEleves();
							String sanction7 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(6).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction7 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(6).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction7=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction7.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom7(indiscnom7);
								ficheCC.setSanction7(sanction7);
							}
							
						}
						
						//8eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>7){
							String indiscnom8 = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNomsEleves();
							indiscnom8+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(7).getPrenomsEleves();
							String sanction8 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(7).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction8 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(7).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction8=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction8.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom8(indiscnom8);
								ficheCC.setSanction8(sanction8);
							}
							
						}
						
						//9eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>8){
							String indiscnom9 = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNomsEleves();
							indiscnom9+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(8).getPrenomsEleves();
							String sanction9 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(8).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction9 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(8).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction9=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction9.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom9(indiscnom9);
								ficheCC.setSanction9(sanction9);
							}
							
						}
						
						//10eme eleve le plus indiscipline
						if(listofEleveLesPlusIndisciDansClasseSeq.size()>9){
							String indiscnom10 = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNomsEleves();
							indiscnom10+=" "+ listofEleveLesPlusIndisciDansClasseSeq.get(9).getPrenomsEleves();
							String sanction10 = "aucune";
							List<RapportDisciplinaire> listofRappDiscEleve = 
									(List<RapportDisciplinaire>) listofEleveLesPlusIndisciDansClasseSeq.get(9).getListofRDisc_DESC();
							
							if(listofRappDiscEleve!=null){
								if(listofRappDiscEleve.size()>0){
									if(classe.getLangueClasses().equalsIgnoreCase("fr")==true){
										sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("fr");
									}
									else{
										sanction10 = listofRappDiscEleve.get(0).getRapportDisciplinaireStringReduit("en");
									}
								}
								else{
									//Le gar n'a pas de rapport disciplinaire donc on va regarder si il a des absences
									int nbreHANJ = listofEleveLesPlusIndisciDansClasseSeq.get(9).getNbreHeureAbsenceNonJustifieAnnee(annee);
									if(nbreHANJ>0){
										sanction10=nbreHANJ+" H";
									}
								}
							}
							
							
							if(sanction10.equalsIgnoreCase("aucune")==false){
								ficheCC.setIndiscnom10(indiscnom10);
								ficheCC.setSanction10(sanction10);
							}
							
						}
			
			
		
		return ficheCC;
	
	}
	
	
	@Override
	public Map<String, Object> generateCollectionofBulletinAnnee(Long idClasse, Long idAnnee) {
		// TODO Auto-generated method stub

		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinAnnee avec "
				+ "idClasse= "+idClasse+" idAnnee="+idAnnee);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		long startTime = System.currentTimeMillis();
		 
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
			
		 List<BulletinAnnuelBean> collectionofBulletionAnnuel = new ArrayList<BulletinAnnuelBean>();
		
		 if((classeConcerne==null) || (anneeScolaire==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang ="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueAn = ub.getListOfCoursEvalueDansAnnee(classeConcerne, 
					anneeScolaire);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la section général)
			 * Cette liste de cours doit etre extraite des cours evalue
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans une année
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			

			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierAnnee(classeConcerne, anneeScolaire);
			

			RapportAnnuelClasse rapportAnnuelClasse = ub.getRapportAnnuelClasse(classeConcerne, 
					listofEleveRegulier, anneeScolaire);
			
			
			double moyenne_premier_classe = rapportAnnuelClasse.getValeurMoyennePremierDansAn();
			
			double moyenne_dernier_classe = rapportAnnuelClasse.getValeurMoyenneDernierDansAn();
			
			int nbre_moyenne_classeSeq = rapportAnnuelClasse.getNbreMoyennePourAn();
			
			double tauxReussite = rapportAnnuelClasse.getTauxReussiteAnnuel();
			
			double moyenne_general = rapportAnnuelClasse.getMoyenneGeneralAnnuel();
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =	ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseAn = ub.geteffectifEleveRegulierAnnee(classeConcerne, anneeScolaire);
			
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans l'année considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantAnnee(classeConcerne, anneeScolaire);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu annuellement
			 */
			List<Eleves> listofElevesOrdreDecroissantAnnee = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneAnnuelOrdreDecroissant1(classeConcerne, anneeScolaire);
			

			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque trimestre de l'année
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneTrimestriel = new 
					HashMap<Long, List<Eleves>>();
			
			for(Trimestres trim : anneeScolaire.getListoftrimestre()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneTrim = (List<Eleves>) 
						UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
				
				mapofElevesOrdreDecroissantMoyenneTrimestriel.put(trim.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneTrim);
				
			}
			
			
			
			for(Eleves eleve : listofEleveClasse){
				long startTimeFor = System.currentTimeMillis();
				
				BulletinAnnuelBean bulletinAn = new BulletinAnnuelBean();
				/*
				 * Initialisons les premieres donnees du bulletin annuel
				 */
				/****
				 * Information d'entete du bulletin
				 */
				
				bulletinAn.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
				bulletinAn.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
				bulletinAn.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
				bulletinAn.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
				bulletinAn.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
				bulletinAn.setEtablissement_fr(etablissementConcerne.getNomsEtab());
				bulletinAn.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
						etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
				bulletinAn.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
				bulletinAn.setDevise_fr(etablissementConcerne.getDeviseEtab());
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinAn.setTitre_bulletin("Bulletin de note de l'année scolaire "+
							anneeScolaire.getIntituleAnnee());
				}
				else{
					bulletinAn.setTitre_bulletin("Report card of school year "+anneeScolaire.getIntituleAnnee());
				}
				bulletinAn.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
				bulletinAn.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
				
				
				/***************
				 * Information personnel de l'élève
				 */
				bulletinAn.setNumero(" "+eleve.getNumero(listofElevesClasse));
				bulletinAn.setSexe(eleve.getSexeEleves());
				bulletinAn.setNom_eleve(eleve.getNomsEleves());
				bulletinAn.setPrenom_eleve(eleve.getPrenomsEleves());
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				bulletinAn.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
				bulletinAn.setLieu_naissance_eleve(eleve.getLieunaissEleves());
				bulletinAn.setMatricule_eleve(eleve.getMatriculeEleves());
				bulletinAn.setRedoublant(eleve.getRedoublant());
				bulletinAn.setClasse_eleve(classeString);
				bulletinAn.setProf_principal(profPrincipal);
				bulletinAn.setEffectif_classe(effectifTotalClasse);
				bulletinAn.setEffectif_presente(effectifRegulierClasseAn);
				
				/*****
				 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
				 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
				 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
				 * on va essayer de charger le fichier correspondant avec la classe File de java.io
				 */
				File f=new File(photoElevesDir+eleve.getIdEleves());
				//System.err.println("est ce que le fichier existe "+f.exists());
				
				if(f.exists()==true){
					bulletinAn.setPhoto(photoElevesDir+eleve.getIdEleves()); 
				}
				
				
				/********
				 * Informations sur les labels d'entete des notes du bulletin annuel
				 */
				for(Trimestres trim : anneeScolaire.getListoftrimestre()){
					if(trim.getNumeroTrim()==1){
						bulletinAn.setLabel_note_1("T"+trim.getNumeroTrim());
					}
					else if(trim.getNumeroTrim()==2){
						bulletinAn.setLabel_note_2("T"+trim.getNumeroTrim());
					}
					else if(trim.getNumeroTrim()==3){
						bulletinAn.setLabel_note_3("T"+trim.getNumeroTrim());
					}
				}
				bulletinAn.setLabel_annuel("N An");
				bulletinAn.setLabel_ann_x_coef("N An"+"*Coef");

				/***********
				 * Information sur les totaux annuels
				 */
				
				double total_coef = ub.getSommeCoefCoursComposeAnnee(eleve, anneeScolaire);
				bulletinAn.setTotal_coef(total_coef);
				
				double total_points = ub.getTotalPointsAnnuel(eleve, anneeScolaire);
				if(total_points>0){
					bulletinAn.setTotal_points(total_points);
				}
				
				/***********
				 * Informations sur les resultats annuels de l'eleve
				 */
				bulletinAn.setResult_tt_coef(total_coef);
				
				if(total_points>0){
					bulletinAn.setResult_tt_points(total_points);
				}
				
				
			
				int rang = ub.getRangAnnuelEleveAuMoinsUneNote(eleve,listofElevesOrdreDecroissantAnnee);
				if(rang>0){
					bulletinAn.setResult_rang_ann(rang+"e");
					bulletinAn.setR_rang_an(rang+"e");
				}
				else{
					bulletinAn.setResult_rang_ann("");
					bulletinAn.setR_rang_an("");
				}


				
				/**********************
				 * Informations sur le profil de la classe dans l'année
				 */
				if(moyenne_premier_classe>0){
					bulletinAn.setMoy_premier(moyenne_premier_classe);
				}
				if(moyenne_dernier_classe>0){
					bulletinAn.setMoy_dernier(moyenne_dernier_classe);
				}
				bulletinAn.setNbre_moyennes(nbre_moyenne_classeSeq);
				if(tauxReussite>0){
					bulletinAn.setTaux_reussite(tauxReussite);
				}
				if(moyenne_general>0){
					bulletinAn.setMoy_gen_classe(moyenne_general);
				}

				/***********************
				 * Informations sur la conduite annuel de l'élève
				 */
				int nhaj = 0;
				int nhanj = 0;
				int nhc = 0;
				int nje = 0;
				
				
				nhanj = eleve.getNbreHeureAbsenceNonJustifieAnnee(anneeScolaire);
				nhaj = eleve.getNbreHeureAbsenceJustifieAnnee(anneeScolaire);
				
				bulletinAn.setAbsence_NJ(nhanj);
				bulletinAn.setAbsence_J(nhaj);
				bulletinAn.setConsigne(nhc+"h");
				bulletinAn.setExclusion(nje+" J");
				bulletinAn.setAvertissement("");
				bulletinAn.setBlame_conduite("");
				
				/************************
				 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(annee)
				 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
				 * ete infligées. On va commencer du trimestre de plus grand numero vers celui de plus petit et
				 *  de la séquence paire vers la séquence impair de chaque trimestre à chercher
				 */
				bulletinAn.setRapport_disc1("");
				bulletinAn.setRapport_disc2("");
				bulletinAn.setRapport_disc3("");
				for(Trimestres trim : anneeScolaire.getListoftrimestre_DESC()){
						for(Sequences seq : trim.getListofsequence_DESC()){
							List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
							
							if(listofRDiscEleveSeq != null){
								if(listofRDiscEleveSeq.size()>0) {
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									//On peut donc fixer rapport_disc1
									bulletinAn.setRapport_disc1(rdisc_chaine);
								}
								
								/*
								 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
								 * on va set rapport_disc2
								 */
								if(listofRDiscEleveSeq.size()>1) {
		
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									//On peut donc fixer rapport_disc1
									bulletinAn.setRapport_disc2(rdisc_chaine);
								
								}
								
								if(listofRDiscEleveSeq.size()>2) {
		
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									
									//On peut donc fixer rapport_disc1
									bulletinAn.setRapport_disc3(rdisc_chaine);
													
								}
								
						}
					}
				}
				
				
				/**************************
				 * Informations sur le rappel de la moyenne et du rang des autres trimestres
				 */
				
				for(Trimestres trim : anneeScolaire.getListoftrimestre()){
					
					List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = 
							mapofElevesOrdreDecroissantMoyenneTrimestriel.get(trim.getIdPeriodes());
					
					if(trim.getNumeroTrim() == 1){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinAn.setRappel_1("Trimestre "+trim.getNumeroTrim());
						}
						else{
							bulletinAn.setRappel_1("Term "+trim.getNumeroTrim());
						}
						
						double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
						if(moy_trim>0){
							bulletinAn.setR_moy_1(moy_trim);
						}
						
						int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneTrimestriel);
						
						if(rangtrim>0){
							bulletinAn.setR_rang_1(rangtrim+"e");
						}
						else{
							bulletinAn.setR_rang_1("");
						}
					}//trim1
					else if(trim.getNumeroTrim() == 2){
						if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setRappel_2("Trimestre"+trim.getNumeroTrim());
						}
						else{
							bulletinAn.setRappel_2("Term "+trim.getNumeroTrim());
						}
						double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
						if(moy_trim>0){
							bulletinAn.setR_moy_2(moy_trim);
						}
						
						int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneTrimestriel);
						
						if(rangtrim>0){
							bulletinAn.setR_rang_2(rangtrim+"e");
						}
						else{
							bulletinAn.setR_rang_2("");
						}
					}//fin trim2
					else if(trim.getNumeroTrim() == 3){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinAn.setRappel_3("Trimestre"+trim.getNumeroTrim());
						}
						else{
							bulletinAn.setRappel_3("Term "+trim.getNumeroTrim());
						}
						double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
						if(moy_trim>0){
							bulletinAn.setR_moy_3(moy_trim);
						}
						
						int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneTrimestriel);
						
						if(rangtrim>0){
							bulletinAn.setR_rang_3(rangtrim+"e");
						}
						else{
							bulletinAn.setR_rang_3("");
						}
					}//fin trim3
					
				}//fin du for sur les trim
				
				/****************************
				 * Informations sur l'appreciation du travail de l'élève
				 */
				double moy_an = ub.getMoyenneAnnuel(eleve, anneeScolaire);
				
				if(moy_an>=0) bulletinAn.setR_moy_an(moy_an);
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinAn.setRappel_4("Année");
				}
				else{
					bulletinAn.setRappel_4("Year ");
				}
				
				bulletinAn.setTableau_hon("");
				bulletinAn.setTableau_enc("");
				bulletinAn.setTableau_fel("");
				String appreciation = ub.calculAppreciation(moy_an,lang);
				bulletinAn.setAppreciation(appreciation);
				
				/*
				 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
				 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
				 */
				DecisionConseil decConseil = eleve.getDecisionConseilPeriode(anneeScolaire.getIdPeriodes());
				bulletinAn.setDistinction("");
				bulletinAn.setDecision_conseil("");
				if(decConseil !=null){
					/*******************************
					 * Informations sur les distinctions octroyées  dans la séquence
					 */
					String distinction="";
					distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
					bulletinAn.setDistinction(distinction);
					
					/*******************************
					 * Informations sur les decision du conseil de classe dans la séquence
					 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
					 * de classe. Et ici c'est le conseil de classe annuel donc la decision finale
					 */
					String decision="";
					decision += decConseil.getDecisionDecisionConseilString(lang);
					/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
					decision+=distinction;*/
					bulletinAn.setDecision_conseil(decision);
				}
				
				
				
				
				List<Cours> listofCoursEffortAFournir = 
						ub.getListofCoursDansOrdreEffortAFournirAnnee(eleve, listofCoursEvalueAn, 
						anneeScolaire);
				bulletinAn.setEffort_matiere1("");
				bulletinAn.setEffort_matiere2("");
				bulletinAn.setEffort_matiere3("");
				if(listofCoursEffortAFournir.size()>0) {
					String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
					bulletinAn.setEffort_matiere1(codeCours);
				}
				
				
				if(listofCoursEffortAFournir.size()>1) {
					String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
					bulletinAn.setEffort_matiere2(codeCours);
				}
				
				if(listofCoursEffortAFournir.size()>2) {
					String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
					bulletinAn.setEffort_matiere3(codeCours);
				}
				

				/*****************************
				 * Information sur l'espace VISA du bulletin
				 */
				bulletinAn.setVille(villeEtab);
				
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres scientifique (Groupe1) dans l'année
				 * cccccccccccccccccccccccccc
				 */
				
				LigneAnnuelGroupeCours ligneAnnuelGroupeCoursScientifique = 
						ub.getLigneAnnuelGroupeCours(eleve, listofCoursScientifique, anneeScolaire);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinAn.setNom_g1("Scientifique");
				}
				else{
					bulletinAn.setNom_g1("Scientific");
				}
				
				double total_coef_g1 = ligneAnnuelGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
				
				bulletinAn.setTotal_coef_g1(total_coef_g1);
				//System.err.println("total_coef_g1 == "+total_coef_g1);
				
				double total_g1 = ligneAnnuelGroupeCoursScientifique.getTotalNoteAnElevePourGroupeCours();
				if(total_g1>0){
					bulletinAn.setTotal_g1(total_g1);
				}
				
				String totalextreme_g1 = "";
				
				double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansAn(
						listofElevesClasse, listofCoursScientifique, anneeScolaire);
				
				double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansAn(
						listofElevesClasse, listofCoursScientifique, anneeScolaire);
				if(valeurMoyDernierGrpCours1>0 && valeurMoyPremierGrpCours1>0){
					totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
							valeurMoyPremierGrpCours1+"]";
				}
				bulletinAn.setTotal_extreme_g1(totalextreme_g1);
				
				int r1 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursScientifique, 
						anneeScolaire, eleve);
				
				if(r1>0){
					bulletinAn.setTotal_rang_g1(r1+"e");
				}
				else{
					bulletinAn.setTotal_rang_g1("");
				}
				
				
				double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
						listofCoursScientifique, anneeScolaire);
				if(moy_gen_grp1>0){
					bulletinAn.setMg_classe_g1(moy_gen_grp1);
				}
				
				double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
						listofCoursScientifique, anneeScolaire);
				if(total_pourcentage_g1>=0){
					bulletinAn.setTotal_pourcentage_g1(total_pourcentage_g1);
				}
				
				double moyenne_g1 = ligneAnnuelGroupeCoursScientifique.
						getMoyenneAnElevePourGroupeCours();
				if(moyenne_g1>0){
					bulletinAn.setMoyenne_g1(moyenne_g1);
				}
		

				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Litteraire (Groupe2) dans l'année
				 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
				 */
				
				LigneAnnuelGroupeCours ligneAnnuelGroupeCoursLitteraire = 
						ub.getLigneAnnuelGroupeCours(eleve, listofCoursLitteraire, anneeScolaire);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinAn.setNom_g2("Litteraire");
				}
				else{
					bulletinAn.setNom_g2("Arts");
				}
				
				double total_coef_g2 = ligneAnnuelGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
				
				bulletinAn.setTotal_coef_g2(total_coef_g2);
				
				double total_g2 = ligneAnnuelGroupeCoursLitteraire.getTotalNoteAnElevePourGroupeCours();
				if(total_g2>0){
					bulletinAn.setTotal_g2(total_g2);
				}
				
				String totalextreme_g2 = "";
				
				double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansAn(
						listofElevesClasse, listofCoursLitteraire, anneeScolaire);
				
				double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansAn(
						listofElevesClasse, listofCoursLitteraire, anneeScolaire);
				if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
					totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
							valeurMoyPremierGrpCours2+"]";
				}
				bulletinAn.setTotal_extreme_g2(totalextreme_g2);
				
				int r2 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursLitteraire, 
						anneeScolaire, eleve);
				
				if(r2>0){
					bulletinAn.setTotal_rang_g2(r2+"e");
				}
				else{
					bulletinAn.setTotal_rang_g2("");
				}
				
				
				double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
						listofCoursLitteraire, anneeScolaire);
				if(moy_gen_grp2>0){
					bulletinAn.setMg_classe_g2(moy_gen_grp2);
				}
				
				double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
						listofCoursLitteraire, anneeScolaire);
				if(total_pourcentage_g2>=0){
					bulletinAn.setTotal_pourcentage_g2(total_pourcentage_g2);
				}
				
				double moyenne_g2 = ligneAnnuelGroupeCoursLitteraire.
						getMoyenneAnElevePourGroupeCours();
				if(moyenne_g2>0){
					bulletinAn.setMoyenne_g2(moyenne_g2);
				}

				

				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Divers (Groupe3) dans l'année
				 * ddddddddddddddddddddddddddddddddddd
				 */
				
				LigneAnnuelGroupeCours ligneAnnuelGroupeCoursDivers = 
						ub.getLigneAnnuelGroupeCours(eleve, listofCoursDivers, anneeScolaire);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinAn.setNom_g3("Divers");
				}
				else{
					bulletinAn.setNom_g3("Others");
				}
				
				double total_coef_g3 = ligneAnnuelGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
				
				bulletinAn.setTotal_coef_g3(total_coef_g3);
				
				double total_g3 = ligneAnnuelGroupeCoursDivers.getTotalNoteAnElevePourGroupeCours();
				if(total_g3>0){
					bulletinAn.setTotal_g3(total_g3);
				}
				
				String totalextreme_g3 = "";
				
				double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansAn(
						listofElevesClasse, listofCoursDivers, anneeScolaire);
				
				double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansAn(
						listofElevesClasse, listofCoursDivers, anneeScolaire);
				if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
					totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
							valeurMoyPremierGrpCours3+"]";
				}
				bulletinAn.setTotal_extreme_g3(totalextreme_g3);
				
				int r3 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursDivers, 
						anneeScolaire, eleve);
				
				if(r3>0){
					bulletinAn.setTotal_rang_g3(r3+"e");
				}
				else{
					bulletinAn.setTotal_rang_g3("");
				}
				
				
				double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
						listofCoursDivers, anneeScolaire);
				if(moy_gen_grp3>0){
					bulletinAn.setMg_classe_g3(moy_gen_grp3);
				}
				
				double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
						listofCoursDivers, anneeScolaire);
				if(total_pourcentage_g3>=0){
					bulletinAn.setTotal_pourcentage_g3(total_pourcentage_g3);
				}
				
				double moyenne_g3 = ligneAnnuelGroupeCoursDivers.
						getMoyenneAnElevePourGroupeCours();
				if(moyenne_g3>0){
					bulletinAn.setMoyenne_g3(moyenne_g3);
				}
				
				/************************************
				 * Listes alimentant les sous rapports: les rapports sur les groupes des matières 
				 **********/
				
				
				List<MatiereGroupe1AnnuelBean> listofCoursScientifiqueAnnuelBean 
							= new ArrayList<MatiereGroupe1AnnuelBean>(); 
				
				int rc1 = 0;
				/***
				 * debut du for sur les cours scientifique
				 * Gestion des cours scientifique
				 */
				for(Cours cours : listofCoursScientifique){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe1AnnuelBean mGrp1AnBean = new MatiereGroupe1AnnuelBean();
					
					
					
					RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
							classeConcerne, cours, anneeScolaire);
					
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
					
					mGrp1AnBean.setMatiere_g1(matiere);
					mGrp1AnBean.setMatiere_g1_2emelang(matiere_2emelang);
					mGrp1AnBean.setProf_g1(nomProf);
					
					double soenoteAn = 0;
					int nbreNoteDansAnPourCours = 0;
					
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						if(trim.getNumeroTrim() == 1){
							double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g1>0){
								mGrp1AnBean.setNote_1_g1(note_trim_g1);
								soenoteAn = soenoteAn + note_trim_g1;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 2){
							double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g2>0){
								mGrp1AnBean.setNote_2_g1(note_trim_g2);
								soenoteAn = soenoteAn + note_trim_g2;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 3){
							double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g3>0){
								mGrp1AnBean.setNote_3_g1(note_trim_g3);
								soenoteAn = soenoteAn + note_trim_g3;
								nbreNoteDansAnPourCours +=1; 
							}
						}
					}
					
					double noteCoursAn = 0;
					if(nbreNoteDansAnPourCours>0){
						noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
						mGrp1AnBean.setNote_ann_g1(noteCoursAn);
						double total_ann_g1 = noteCoursAn*cours.getCoefCours();
						mGrp1AnBean.setTotal_ann_g1(total_ann_g1);
					}
					
					mGrp1AnBean.setCoef_g1(cours.getCoefCours());
					
					String extreme_g1 = "";
					double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
					double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
					if(noteAnDernierCours>0 && noteAnPremierCours>0){
						extreme_g1 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
						mGrp1AnBean.setExtreme_g1(extreme_g1);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc1 = ub.getRangNoteAnnuelElevePourCours(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc1>0){
						mGrp1AnBean.setRang_g1(rc1+"e");
					}
					else{
						mGrp1AnBean.setRang_g1("");
					}
					
					
					double moy_classe_g1 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(moy_classe_g1>0){
						mGrp1AnBean.setMoy_classe_g1(moy_classe_g1);
					}
					
					
					double pourcentage_g1 = ub.getTauxReussiteCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(pourcentage_g1>=0){
						mGrp1AnBean.setPourcentage_g1(pourcentage_g1);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
					mGrp1AnBean.setAppreciation_g1(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursScientifiqueAnnuelBean.add(mGrp1AnBean);
				
				}//fin du for sur les cours scientifique
				/****
					fin du for sur les cours scientifique qui passe dans la classe
				 *****/

				//On place la liste des matieres scientifique construit
				bulletinAn.setMatieresGroupe1Annuel(listofCoursScientifiqueAnnuelBean);
				

				List<MatiereGroupe2AnnuelBean> listofCoursLitteraireAnnuelBean 
							= new ArrayList<MatiereGroupe2AnnuelBean>(); 
				
				int rc2 = 0;
				/***
				 * debut du for sur les cours Litteraire
				 * Gestion des cours Litteraire
				 */
				for(Cours cours : listofCoursLitteraire){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe2AnnuelBean mGrp2AnBean = new MatiereGroupe2AnnuelBean();
					
					
					
					RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
							classeConcerne, cours, anneeScolaire);
					
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
					
					mGrp2AnBean.setMatiere_g2(cours.getCodeCours());
					
					mGrp2AnBean.setMatiere_g2(matiere);
					mGrp2AnBean.setMatiere_g2_2emelang(matiere_2emelang);
					mGrp2AnBean.setProf_g2(nomProf);
					
					double soenoteAn = 0;
					int nbreNoteDansAnPourCours = 0;
					
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						if(trim.getNumeroTrim() == 1){
							double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g1>0){
								mGrp2AnBean.setNote_1_g2(note_trim_g1);
								soenoteAn = soenoteAn + note_trim_g1;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 2){
							double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g2>0){
								mGrp2AnBean.setNote_2_g2(note_trim_g2);
								soenoteAn = soenoteAn + note_trim_g2;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 3){
							double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g3>0){
								mGrp2AnBean.setNote_3_g2(note_trim_g3);
								soenoteAn = soenoteAn + note_trim_g3;
								nbreNoteDansAnPourCours +=1; 
							}
						}
					}
					
					double noteCoursAn = 0;
					if(nbreNoteDansAnPourCours>0){
						noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
						mGrp2AnBean.setNote_ann_g2(noteCoursAn);
						double total_ann_g2 = noteCoursAn*cours.getCoefCours();
						mGrp2AnBean.setTotal_ann_g2(total_ann_g2);
					}
					
					mGrp2AnBean.setCoef_g2(cours.getCoefCours());
					
					String extreme_g2 = "";
					double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
					double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
					if(noteAnDernierCours>0 && noteAnPremierCours>0){
						extreme_g2 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
						mGrp2AnBean.setExtreme_g2(extreme_g2);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc2 = ub.getRangNoteAnnuelElevePourCours(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc2>0){
						mGrp2AnBean.setRang_g2(rc2+"e");
					}
					else{
						mGrp2AnBean.setRang_g2("");
					}
					
					
					double moy_classe_g2 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(moy_classe_g2>0){
						mGrp2AnBean.setMoy_classe_g2(moy_classe_g2);
					}
					
					
					double pourcentage_g2 = ub.getTauxReussiteCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(pourcentage_g2>=0){
						mGrp2AnBean.setPourcentage_g2(pourcentage_g2);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
					mGrp2AnBean.setAppreciation_g2(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursLitteraireAnnuelBean.add(mGrp2AnBean);
				
				}//fin du for sur les cours Litteraire
				/****
					fin du for sur les cours Litteraire qui passe dans la classe
				 *****/
				
				//On place la liste des matieres scientifique construit
				bulletinAn.setMatieresGroupe2Annuel(listofCoursLitteraireAnnuelBean);
				

				List<MatiereGroupe3AnnuelBean> listofCoursDiversAnnuelBean 
							= new ArrayList<MatiereGroupe3AnnuelBean>(); 
				
				int rc3 = 0;
				/***
				 * debut du for sur les cours Divers
				 * Gestion des cours Divers
				 */
				for(Cours cours : listofCoursDivers){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe3AnnuelBean mGrp3AnBean = new MatiereGroupe3AnnuelBean();
					
					
					
					RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
							classeConcerne, cours, anneeScolaire);
					
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
					
					mGrp3AnBean.setMatiere_g3(matiere);
					mGrp3AnBean.setMatiere_g3_2emelang(matiere_2emelang);
					mGrp3AnBean.setProf_g3(nomProf);
					
					double soenoteAn = 0;
					int nbreNoteDansAnPourCours = 0;
					
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						if(trim.getNumeroTrim() == 1){
							double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g1>0){
								mGrp3AnBean.setNote_1_g3(note_trim_g1);
								soenoteAn = soenoteAn + note_trim_g1;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 2){
							double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g2>0){
								mGrp3AnBean.setNote_2_g3(note_trim_g2);
								soenoteAn = soenoteAn + note_trim_g2;
								nbreNoteDansAnPourCours +=1; 
							}
						}
						else if(trim.getNumeroTrim() == 3){
							double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
							if(note_trim_g3>0){
								mGrp3AnBean.setNote_3_g3(note_trim_g3);
								soenoteAn = soenoteAn + note_trim_g3;
								nbreNoteDansAnPourCours +=1; 
							}
						}
					}
					
					double noteCoursAn = 0;
					if(nbreNoteDansAnPourCours>0){
						noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
						mGrp3AnBean.setNote_ann_g3(noteCoursAn);
						double total_ann_g3 = noteCoursAn*cours.getCoefCours();
						mGrp3AnBean.setTotal_ann_g3(total_ann_g3);
					}
					
					mGrp3AnBean.setCoef_g3(cours.getCoefCours());
					
					String extreme_g3 = "";
					double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
					double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
					if(noteAnDernierCours>0 && noteAnPremierCours>0){
						extreme_g3 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
						mGrp3AnBean.setExtreme_g3(extreme_g3);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc3 = ub.getRangNoteAnnuelElevePourCours(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc3>0){
						mGrp3AnBean.setRang_g3(rc3+"e");
					}
					else{
						mGrp3AnBean.setRang_g3(" ");
					}
					
					
					double moy_classe_g3 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(moy_classe_g3>0){
						mGrp3AnBean.setMoy_classe_g3(moy_classe_g3);
					}
					
					
					double pourcentage_g3 = ub.getTauxReussiteCoursAn(classeConcerne, 
							cours, anneeScolaire);
					if(pourcentage_g3>=0){
						mGrp3AnBean.setPourcentage_g3(pourcentage_g3);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
					mGrp3AnBean.setAppreciation_g3(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursDiversAnnuelBean.add(mGrp3AnBean);
				
				}//fin du for sur les cours Divers
				/****
					fin du for sur les cours Divers qui passe dans la classe
				 *****/
				
				//On place la liste des matieres scientifique construit
				bulletinAn.setMatieresGroupe3Annuel(listofCoursDiversAnnuelBean);
				
				
				
				long finforTime = System.currentTimeMillis();
				collectionofBulletionAnnuel.add(bulletinAn);
				System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
						" dans l'année "+anneeScolaire.getIntituleAnnee()+
						"  ajouter avec succes en "+(finforTime-startTimeFor));
				numBull++;				
				
			}//fin du for sur les élèves
			
			long finforTime = System.currentTimeMillis();
			System.out.println("A ce stade on a deja passe dans bulletinAn :"+ (finforTime-startTime));
			
			donnee.put("collectionofBulletionAnnuel", collectionofBulletionAnnuel);
			
			/*******
			 * Conception du rapport de conseil de classe Annuel
			 */
			FicheConseilClasseBean ficheCC = this.getRapportConseilClasseAnnuel(etablissementConcerne, 
					anneeScolaire, classeConcerne, tauxReussite, moyenne_general,
					listofElevesOrdreDecroissantAnnee);
			
			donnee.put("ficheconseilclasseannuel", ficheCC);

		return donnee;
		//return collectionofBulletionAnnuel;
	
	}

	@Override
	public Map<String, Object> generateCollectionofBulletinTrimAnnee(Long idClasse, Long idTrimestre) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinTrimAnnee avec "
				+ "idClasse= "+idClasse+" idTrimestre="+idTrimestre);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		long startTime = System.currentTimeMillis();
		
		
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestre);
			
		 List<BulletinTrimAnnuelBean> collectionofBulletionTrimAnnuel = 
				 new ArrayList<BulletinTrimAnnuelBean>();
		
		 if((classeConcerne==null) || (trimestreConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueTrim = ub.getListOfCoursEvalueDansTrimestre(classeConcerne, 
					trimestreConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);


			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans un trimestre
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			
			RapportTrimestrielClasse rapportTrimestrielClasse = ub.getRapportTrimestrielClasse(classeConcerne, 
					listofEleveRegulier, trimestreConcerne);
			
			RapportAnnuelClasse rapportAnnuelClasse = ub.getRapportAnnuelClasse(classeConcerne, 
					listofEleveRegulier, anneeScolaire);
			
			double moyenne_premier_classe = rapportTrimestrielClasse.getValeurMoyennePremierDansTrim();
			
			double moyenne_dernier_classe = rapportTrimestrielClasse.getValeurMoyenneDernierDansTrim();
			
			int nbre_moyenne_classeSeq = rapportTrimestrielClasse.getNbreMoyennePourTrim();
			
			double tauxReussite = rapportTrimestrielClasse.getTauxReussiteTrimestriel();
			
			double moyenne_general = rapportTrimestrielClasse.getMoyenneGeneralTrimestre();
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =	ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseTrim = ub.geteffectifEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			double t_reussite_an=0.0;
			double moy_gen_classe_an=0.0;
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans le trimestre considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantTrimestre(classeConcerne, trimestreConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Trimestriellement
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trimestreConcerne);
			
			
			
			List<Eleves> listofElevesOrdreDecroissantAnnee = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneAnnuelOrdreDecroissant1(classeConcerne, anneeScolaire);
			
			
			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque séquence dans le trimestre
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneSequentiel = new 
					HashMap<Long, List<Eleves>>();

			for(Sequences seq : trimestreConcerne.getListofsequence()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = (List<Eleves>) 
						ub.getMoyenneSequentielOrdreDecroissant1(classeConcerne, seq);
				
				mapofElevesOrdreDecroissantMoyenneSequentiel.put(seq.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneSeq);
				
			}

			for(Eleves eleve : listofEleveClasse){
				long startTimeFor = System.currentTimeMillis();
				
				BulletinTrimAnnuelBean bulletinTrimAn = new BulletinTrimAnnuelBean();
				/*
				 * Initialisons les premieres donnees du bulletin trimestriel
				 */
				/****
				 * Information d'entete du bulletin
				 */
				bulletinTrimAn.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
				bulletinTrimAn.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
				bulletinTrimAn.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
				bulletinTrimAn.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
				bulletinTrimAn.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
				bulletinTrimAn.setEtablissement_fr(etablissementConcerne.getNomsEtab());
				bulletinTrimAn.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
						etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
				bulletinTrimAn.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
				bulletinTrimAn.setDevise_fr(etablissementConcerne.getDeviseEtab());
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setTitre_bulletin("Bulletin de note du trimestre "+trimestreConcerne.getNumeroTrim());
				}
				else{
					bulletinTrimAn.setTitre_bulletin("Report card of term "+trimestreConcerne.getNumeroTrim());
				}
				bulletinTrimAn.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
				bulletinTrimAn.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
				
				
				/***************
				 * Information personnel de l'élève
				 */
				bulletinTrimAn.setNumero(" "+eleve.getNumero(listofElevesClasse));
				bulletinTrimAn.setSexe(eleve.getSexeEleves());
				bulletinTrimAn.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
				bulletinTrimAn.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				bulletinTrimAn.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
				bulletinTrimAn.setLieu_naissance_eleve(eleve.getLieunaissEleves());
				bulletinTrimAn.setMatricule_eleve(eleve.getMatriculeEleves());
				bulletinTrimAn.setRedoublant(eleve.getRedoublant());
				bulletinTrimAn.setClasse_eleve(classeString);
				bulletinTrimAn.setProf_principal(profPrincipal);
				bulletinTrimAn.setEffectif_classe(effectifTotalClasse);
				bulletinTrimAn.setEffectif_presente(effectifRegulierClasseTrim);
				
				/*****
				 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
				 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
				 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
				 * on va essayer de charger le fichier correspondant avec la classe File de java.io
				 */
				File f=new File(photoElevesDir+eleve.getIdEleves());
				//System.err.println("est ce que le fichier existe "+f.exists());
				
				if(f.exists()==true){
					bulletinTrimAn.setPhoto(photoElevesDir+eleve.getIdEleves()); 
				}


				/********
				 * Informations sur les labels d'entete des notes du bulletin trimestriel
				 */
				for(Sequences seq : trimestreConcerne.getListofsequence()){
					if(seq.getNumeroSeq()%2==1){
						bulletinTrimAn.setLabel_note_1("N S"+seq.getNumeroSeq());
					}
					else{
						bulletinTrimAn.setLabel_note_2("N S"+seq.getNumeroSeq());
					}
				}
				

				bulletinTrimAn.setLabel_trimestre("N T"+trimestreConcerne.getNumeroTrim());
				bulletinTrimAn.setLabel_trim_x_coef("N T"+trimestreConcerne.getNumeroTrim()+"*Coef");
		
				/***********
				 * Information sur les totaux trimestriels
				 */
				
				double total_coef = ub.getSommeCoefCoursComposeTrimestre(eleve, trimestreConcerne);
				bulletinTrimAn.setTotal_coef(total_coef);
				
				double total_points = ub.getTotalPointsTrimestriel(eleve, trimestreConcerne);
				if(total_points>0){
					bulletinTrimAn.setTotal_points(total_points);
				}
				
				/***********
				 * Informations sur les resultats trimestriels de l'eleve
				 */
				bulletinTrimAn.setResult_tt_coef(total_coef);
				
				if(total_points>0){
					bulletinTrimAn.setResult_tt_points(total_points);
				}
				
				
			
				//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
				
				/*int rang = this.getRangTrimestrielEleveAuMoinsUneNote(classeConcerne, trimestreConcerne, 
						eleve);*/
				 
				 int rang = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
						 listofElevesOrdreDecroissantMoyenneTrimestriel);
				 
				if(rang>0){
					bulletinTrimAn.setResult_rang_trim(rang+"e");
				}
				else{
					bulletinTrimAn.setResult_rang_trim("");
				}
				
				/********************************
				 * Info sur le resultat annuel a ce stade de l'élève
				 */
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setRappel_an("Résultat annuel");
				}
				else{
					bulletinTrimAn.setRappel_an("Annual result");
				}
				double moy_an = ub.getMoyenneAnnuel(eleve, anneeScolaire);
				
				if(moy_an>0)	bulletinTrimAn.setMoy_an(moy_an);
				
				double r_moy_an = moy_an;
				
				if(r_moy_an>=0) bulletinTrimAn.setR_moy_an(r_moy_an);
				
				int rang_an = ub.getRangAnnuelEleveAuMoinsUneNote(eleve, listofElevesOrdreDecroissantAnnee);
				if(rang_an>0) bulletinTrimAn.setRang_an(rang_an+"e");
				
				int r_rang_an = rang_an;
				if(r_rang_an>0) bulletinTrimAn.setR_rang_an(r_rang_an+"e");
				
				
				t_reussite_an = rapportAnnuelClasse.getTauxReussiteAnnuel();
				bulletinTrimAn.setT_reussite_an(t_reussite_an);
				
				moy_gen_classe_an = rapportAnnuelClasse.getMoyenneGeneralAnnuel();
				bulletinTrimAn.setMoy_gen_classe_an(moy_gen_classe_an);
			
				
				/*
				 * On doit faire cette liste des élèves pour tous les autres trimestres
				 * en fonction du numero de trimestre demande
				 */
				List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_1=null;
				List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_2=null;
				List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_3=null;
				
				Trimestres trimestre1=null;
				Trimestres trimestre2=null;
				Trimestres trimestre3=null;
				
				int rang1=0;
				int rang2=0;
				int rang3=0;
				
				for(Trimestres trim : anneeScolaire.getListoftrimestre()){
					if(trim.getNumeroTrim()==1){
						listofElevesOrdreDecroissantMoyenneTrimestriel_1 = (List<Eleves>) 
								UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
						 
						rang1 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								 listofElevesOrdreDecroissantMoyenneTrimestriel_1);
						
						trimestre1 = trim;
					}
					else if(trim.getNumeroTrim()==2){
						listofElevesOrdreDecroissantMoyenneTrimestriel_2 = (List<Eleves>) 
								UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
						
						rang2 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								 listofElevesOrdreDecroissantMoyenneTrimestriel_2);
						
						trimestre2 = trim;
					}
					else if(trim.getNumeroTrim()==3){
						listofElevesOrdreDecroissantMoyenneTrimestriel_3 = (List<Eleves>) 
								UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
						
						rang3 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
								 listofElevesOrdreDecroissantMoyenneTrimestriel_3);
						
						trimestre3 = trim;
					}
				}
				
				
				
				if(trimestreConcerne.getNumeroTrim()==1){
					if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setRappel_3("Trimestre1");
					}
					else{
						bulletinTrimAn.setRappel_3("Term 1");
					}
					bulletinTrimAn.setRappel_4("");
					bulletinTrimAn.setRappel_5("");
					double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
					if(r_moy_3>=0)	bulletinTrimAn.setR_moy_3(r_moy_3);
					
					if(rang1>0){
						bulletinTrimAn.setR_rang_3(rang1+"e");
					}
					else{
						bulletinTrimAn.setR_rang_3("");
					}
				}
				else if(trimestreConcerne.getNumeroTrim()==2){
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_3("Trimestre1");
					}
					else{
						bulletinTrimAn.setRappel_3("Term 1");
					}
					double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
					if(r_moy_3>=0)	bulletinTrimAn.setR_moy_3(r_moy_3);
					
					if(rang1>0){
						bulletinTrimAn.setR_rang_3(rang1+"e");
					}
					else{
						bulletinTrimAn.setR_rang_3("");
					}
					
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_4("Trimestre2");
					}
					else{
						bulletinTrimAn.setRappel_4("Term 2");
					}
					double r_moy_4 = ub.getMoyenneTrimestriel(eleve, trimestre2);
					if(r_moy_4>=0) bulletinTrimAn.setR_moy_4(r_moy_4);
					
					if(rang2>0){
						bulletinTrimAn.setR_rang_4(rang2+"e");
					}
					else{
						bulletinTrimAn.setR_rang_4("");
					}
					bulletinTrimAn.setRappel_5("");
					
				}
				else if(trimestreConcerne.getNumeroTrim()==3){
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_3("Trimestre1");
					}
					else{
						bulletinTrimAn.setRappel_3("Term 1");
					}
					double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
					if(r_moy_3>=0)	bulletinTrimAn.setR_moy_3(r_moy_3);
					
					if(rang1>0){
						bulletinTrimAn.setR_rang_3(rang1+"e");
					}
					else{
						bulletinTrimAn.setR_rang_3("");
					}
					
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_4("Trimestre2");
					}
					else{
						bulletinTrimAn.setRappel_4("Term 2");
					}
					double r_moy_4 = ub.getMoyenneTrimestriel(eleve, trimestre2);
					if(r_moy_4>=0) bulletinTrimAn.setR_moy_4(r_moy_4);
					
					if(rang2>0){
						bulletinTrimAn.setR_rang_4(rang2+"e");
					}
					else{
						bulletinTrimAn.setR_rang_4("");
					}
					
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_5("Trimestre3");
					}
					else{
						bulletinTrimAn.setRappel_5("term 3");
					}
					double r_moy_5 = ub.getMoyenneTrimestriel(eleve, trimestre3);
					if(r_moy_5>=0) bulletinTrimAn.setR_moy_5(r_moy_5);
					
					if(rang3>0){
						bulletinTrimAn.setR_rang_5(rang3+"e");
					}
					else{
						bulletinTrimAn.setR_rang_5("");
					}
					
				}
				
				
				
				/*************************************************
				 * Informations sur le profil de la classe dans le trimestre
				 */
				if(moyenne_premier_classe>0){
					bulletinTrimAn.setMoy_premier(moyenne_premier_classe);
				}
				if(moyenne_dernier_classe>0){
					bulletinTrimAn.setMoy_dernier(moyenne_dernier_classe);
				}
				
				bulletinTrimAn.setNbre_moyennes(nbre_moyenne_classeSeq);
				if(tauxReussite>0){
					bulletinTrimAn.setTaux_reussite(tauxReussite);
				}
				if(moyenne_general>0){
					bulletinTrimAn.setMoy_gen_classe(moyenne_general);
				}
				

				/***********************
				 * Informations sur la conduite trimestriel de l'élève
				 */
				int nhaj = 0;
				int nhanj = 0;
				int nhc = 0;
				int nje = 0;
				
			
				
				nhanj = eleve.getNbreHeureAbsenceNonJustifieTrim(trimestreConcerne);
				nhaj = eleve.getNbreHeureAbsenceJustifieTrim(trimestreConcerne);
				
				bulletinTrimAn.setAbsence_NJ(nhanj);
				bulletinTrimAn.setAbsence_J(nhaj);
				bulletinTrimAn.setConsigne(nhc+"h");
				bulletinTrimAn.setExclusion(nje+" J");
				bulletinTrimAn.setAvertissement("");
				bulletinTrimAn.setBlame_conduite("");
				
				/************************
				 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(annee)
				 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
				 * ete infligées. On va commencer du trimestre de plus grand numero vers celui de plus petit et
				 *  de la séquence paire vers la séquence impair de chaque trimestre à chercher
				 */
				bulletinTrimAn.setRapport_disc1("");
				bulletinTrimAn.setRapport_disc2("");
				bulletinTrimAn.setRapport_disc3("");
				for(Trimestres trim : anneeScolaire.getListoftrimestre_DESC()){
						for(Sequences seq : trim.getListofsequence_DESC()){
							List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
							
							if(listofRDiscEleveSeq != null){
								if(listofRDiscEleveSeq.size()>0) {
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									//On peut donc fixer rapport_disc1
									bulletinTrimAn.setRapport_disc1(rdisc_chaine);
								}
								
								/*
								 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
								 * on va set rapport_disc2
								 */
								if(listofRDiscEleveSeq.size()>1) {
		
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									//On peut donc fixer rapport_disc1
									bulletinTrimAn.setRapport_disc2(rdisc_chaine);
								
								}
								
								if(listofRDiscEleveSeq.size()>2) {
		
									RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
									String rdisc_chaine = "";
									rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
									
									//On peut donc fixer rapport_disc1
									bulletinTrimAn.setRapport_disc3(rdisc_chaine);
													
								}
								
						}
					}
				}
				
				
				/**************************
				 * Informations sur le rappel de la moyenne et du rang trimestriel
				 */

				for(Sequences seq : trimestreConcerne.getListofsequence()){
					
					List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = 
							mapofElevesOrdreDecroissantMoyenneSequentiel.get(seq.getIdPeriodes());
					
					if(seq.getNumeroSeq()%2==1){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_1("Séquence "+seq.getNumeroSeq());
						}
						else{
							bulletinTrimAn.setRappel_1("Sequence "+seq.getNumeroSeq());
						}
						
						double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
						if(moy_seq>0){
							bulletinTrimAn.setR_moy_1(moy_seq);
						}
						
						int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneSeq);
						
						if(rangseq>0){
							bulletinTrimAn.setR_rang_1(rangseq+"e");
						}
						else{
							bulletinTrimAn.setR_rang_1("");
						}
					}
					else{
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_2("Séquence "+seq.getNumeroSeq());
						}
						else{
							bulletinTrimAn.setRappel_2("Sequence "+seq.getNumeroSeq());
						}
						double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
						if(moy_seq>0){
							bulletinTrimAn.setR_moy_2(moy_seq);
						}
						int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
								listofElevesOrdreDecroissantMoyenneSeq);
						
						if(rangseq>0){
							bulletinTrimAn.setR_rang_2(rangseq+"e");
						}
						else{
							bulletinTrimAn.setR_rang_2("");
						}
					}
				}//fin du for sur les sequences

				/****************************
				 * Informations sur l'appreciation du travail de l'élève
				 */
				double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
				bulletinTrimAn.setTableau_hon("");
				bulletinTrimAn.setTableau_enc("");
				bulletinTrimAn.setTableau_fel("");
				String appreciation = ub.calculAppreciation(moy_trim,lang);
				bulletinTrimAn.setAppreciation(appreciation);
				/*
				 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
				 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
				 */
				DecisionConseil decConseil = eleve.getDecisionConseilPeriode(anneeScolaire.getIdPeriodes());
				bulletinTrimAn.setDistinction("");
				bulletinTrimAn.setDecision_conseil("");
				if(decConseil !=null){
					/*******************************
					 * Informations sur les distinctions octroyées  dans la séquence
					 */
					String distinction="";
					distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
					bulletinTrimAn.setDistinction(distinction);
					
					/*******************************
					 * Informations sur les decision du conseil de classe dans la séquence
					 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
					 * de classe. Et ici c'est le conseil de classe annuel donc la decision finale
					 */
					String decision="";
					decision += decConseil.getDecisionDecisionConseilString(lang);
					/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
					decision+=distinction;*/
					bulletinTrimAn.setDecision_conseil(decision);
				}
				
				
				
				List<Cours> listofCoursEffortAFournir = 
						ub.getListofCoursDansOrdreEffortAFournirTrimestre(eleve, listofCoursEvalueTrim, 
						trimestreConcerne);
				bulletinTrimAn.setEffort_matiere1("");
				bulletinTrimAn.setEffort_matiere2("");
				bulletinTrimAn.setEffort_matiere3("");
				if(listofCoursEffortAFournir.size()>0) {
					String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
					bulletinTrimAn.setEffort_matiere1(codeCours);
				}
				
				
				if(listofCoursEffortAFournir.size()>1) {
					String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
					bulletinTrimAn.setEffort_matiere2(codeCours);
				}
				
				if(listofCoursEffortAFournir.size()>2) {
					String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
					bulletinTrimAn.setEffort_matiere3(codeCours);
				}
				
				
				
				/*****************************
				 * Information sur l'espace VISA du bulletin
				 */
				bulletinTrimAn.setVille(villeEtab);
				
				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres scientifique (Groupe1) dans le trimestre
				 * cccccccccccccccccccccccccc
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursScientifique = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursScientifique, trimestreConcerne);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setNom_g1("Scientifique");
				}
				else{
					bulletinTrimAn.setNom_g1("Scientific");
				}
				
				double total_coef_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
				
				bulletinTrimAn.setTotal_coef_g1(total_coef_g1);
				//System.err.println("total_coef_g1 == "+total_coef_g1);
				
				double total_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalNoteTrimElevePourGroupeCours();
				if(total_g1>0){
					bulletinTrimAn.setTotal_g1(total_g1);
				}
				
				String totalextreme_g1 = "";
				
				double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursScientifique, trimestreConcerne);
				
				double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursScientifique, trimestreConcerne);
				if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
					totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
							valeurMoyPremierGrpCours1+"]";
				}
				bulletinTrimAn.setTotal_extreme_g1(totalextreme_g1);
				
				int r1 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursScientifique, 
						trimestreConcerne, eleve);
				
				if(r1>0){
					bulletinTrimAn.setTotal_rang_g1(r1+"e");
				}
				
				
				double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursScientifique, trimestreConcerne);
				if(moy_gen_grp1>0){
					bulletinTrimAn.setMg_classe_g1(moy_gen_grp1);
				}
				
				double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursScientifique, trimestreConcerne);
				if(total_pourcentage_g1>=0){
					bulletinTrimAn.setTotal_pourcentage_g1(total_pourcentage_g1);
				}
				
				double moyenne_g1 = ligneTrimestrielGroupeCoursScientifique.
						getMoyenneTrimElevePourGroupeCours();
				if(moyenne_g1>0){
					bulletinTrimAn.setMoyenne_g1(moyenne_g1);
				}


				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Litteraire (Groupe2) dans le trimestre
				 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursLitteraire = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursLitteraire, trimestreConcerne);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setNom_g2("Litteraire");
				}
				else{
					bulletinTrimAn.setNom_g2("Arts");
				}
				
				double total_coef_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
				
				bulletinTrimAn.setTotal_coef_g2(total_coef_g2);
				
				double total_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalNoteTrimElevePourGroupeCours();
				if(total_g2>0){
					bulletinTrimAn.setTotal_g2(total_g2);
				}
				
				String totalextreme_g2 = "";
				
				double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
				
				double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
				if(valeurMoyDernierGrpCours2>=0 && valeurMoyPremierGrpCours2>0){
					totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
							valeurMoyPremierGrpCours2+"]";
				}
				bulletinTrimAn.setTotal_extreme_g2(totalextreme_g2);
				
				int r2 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursLitteraire, 
						trimestreConcerne, eleve);
				
				if(r2>0){
					bulletinTrimAn.setTotal_rang_g2(r2+"e");
				}
				
				
				double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursLitteraire, trimestreConcerne);
				if(moy_gen_grp2>0){
					bulletinTrimAn.setMg_classe_g2(moy_gen_grp2);
				}
				
				double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursLitteraire, trimestreConcerne);
				if(total_pourcentage_g2>=0){
					bulletinTrimAn.setTotal_pourcentage_g2(total_pourcentage_g2);
				}
				
				double moyenne_g2 = ligneTrimestrielGroupeCoursLitteraire.
						getMoyenneTrimElevePourGroupeCours();
				if(moyenne_g2>0){
					bulletinTrimAn.setMoyenne_g2(moyenne_g2);
				}
		
				

				/****************
				 * Informations lie au sommaire de chaque groupe
				 * Groupe des matieres Divers (Groupe3) dans le trimestre
				 * ddddddddddddddddddddddddddddddddddd
				 */
				
				LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursDivers = 
						ub.getLigneTrimestrielGroupeCours(eleve, listofCoursDivers, trimestreConcerne);
		
				if(lang.equalsIgnoreCase("fr")==true){
					bulletinTrimAn.setNom_g3("Divers");
				}
				else{
					bulletinTrimAn.setNom_g3("Others");
				}
				double total_coef_g3 = ligneTrimestrielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
				
				bulletinTrimAn.setTotal_coef_g3(total_coef_g3);
				
				double total_g3 = ligneTrimestrielGroupeCoursDivers.getTotalNoteTrimElevePourGroupeCours();
				if(total_g3>0){
					bulletinTrimAn.setTotal_g3(total_g3);
				}
				
				String totalextreme_g3 = "";
				
				double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansTrim(
						listofElevesClasse, listofCoursDivers, trimestreConcerne);
				
				double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansTrim(
						listofElevesClasse, listofCoursDivers, trimestreConcerne);
				if(valeurMoyDernierGrpCours3>=0 && valeurMoyPremierGrpCours3>0){
					totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
							valeurMoyPremierGrpCours3+"]";
				}
				bulletinTrimAn.setTotal_extreme_g3(totalextreme_g3);
				
				int r3 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursDivers, 
						trimestreConcerne, eleve);
				
				if(r3>0){
					bulletinTrimAn.setTotal_rang_g3(r3+"e");
				}
				
				
				double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
						listofCoursDivers, trimestreConcerne);
				if(moy_gen_grp3>0){
					bulletinTrimAn.setMg_classe_g3(moy_gen_grp3);
				}
				
				double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
						listofCoursDivers, trimestreConcerne);
				if(total_pourcentage_g3>=0){
					bulletinTrimAn.setTotal_pourcentage_g3(total_pourcentage_g3);
				}
				
				double moyenne_g3 = ligneTrimestrielGroupeCoursDivers.
						getMoyenneTrimElevePourGroupeCours();
				if(moyenne_g3>0){
					bulletinTrimAn.setMoyenne_g3(moyenne_g3);
				}
				

				/************************************
				 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
				 **********/

				List<MatiereGroupe1TrimAnnuelBean> listofCoursScientifiqueTrimAnnuelBean 
						= new ArrayList<MatiereGroupe1TrimAnnuelBean>(); 
	
				int rc1 = 0;
				/***
				 * debut du for sur les cours scientifique
				 * Gestion des cours scientifique
				 */
				for(Cours cours : listofCoursScientifique){
					
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe1TrimAnnuelBean mGrp1TrimAnBean = new MatiereGroupe1TrimAnnuelBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
					
					mGrp1TrimAnBean.setMatiere_g1(matiere);
					mGrp1TrimAnBean.setMatiere_g1_2emelang(matiere_2emelang);
					mGrp1TrimAnBean.setProf_g1(cours.getProffesseur().getNomsPers());
					
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g1>0){
								mGrp1TrimAnBean.setNote_1_g1(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g2>0){
								mGrp1TrimAnBean.setNote_2_g1(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						mGrp1TrimAnBean.setNote_trim_g1(noteCoursTrim);
						double total_trim_g1 = noteCoursTrim*cours.getCoefCours();
						mGrp1TrimAnBean.setTotal_trim_g1(total_trim_g1);
					}
					
					mGrp1TrimAnBean.setCoef_g1(cours.getCoefCours());
					
					String extreme_g1 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g1 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp1TrimAnBean.setExtreme_g1(extreme_g1);
					}
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc1 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc1>0){
						mGrp1TrimAnBean.setRang_g1(rc1+"e");
					}
					
					
					double moy_classe_g1 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(moy_classe_g1>0){
						mGrp1TrimAnBean.setMoy_classe_g1(moy_classe_g1);
					}
					
					
					double pourcentage_g1 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(pourcentage_g1>=0){
						mGrp1TrimAnBean.setPourcentage_g1(pourcentage_g1);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp1TrimAnBean.setAppreciation_g1(appreciationNote);
					
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursScientifiqueTrimAnnuelBean.add(mGrp1TrimAnBean);
					
				}
				/****
					fin du for sur les cours scientifique qui passe dans la classe
				*****/
				
				//On place la liste des matieres scientifique construit
				bulletinTrimAn.setMatieresGroupe1TrimAnnuel(listofCoursScientifiqueTrimAnnuelBean);
				
				
				List<MatiereGroupe2TrimAnnuelBean> listofCoursLitteraireTrimAnnuelBean 
						= new ArrayList<MatiereGroupe2TrimAnnuelBean>(); 
			
				int rc2 = 0;
				/***
				 * debut du for sur les cours Litteraire
				 * Gestion des cours Litteraire
				 */

				for(Cours cours : listofCoursLitteraire){
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe2TrimAnnuelBean mGrp2TrimAnBean = new MatiereGroupe2TrimAnnuelBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
				
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);

					mGrp2TrimAnBean.setMatiere_g2(matiere);
					mGrp2TrimAnBean.setMatiere_g2_2emelang(matiere_2emelang);
					mGrp2TrimAnBean.setProf_g2(nomProf);
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					

					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g1>0){
								mGrp2TrimAnBean.setNote_1_g2(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g2>0){
								mGrp2TrimAnBean.setNote_2_g2(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						mGrp2TrimAnBean.setNote_trim_g2(noteCoursTrim);
						double total_trim_g2 = noteCoursTrim*cours.getCoefCours();
						mGrp2TrimAnBean.setTotal_trim_g2(total_trim_g2);
					}
					
					mGrp2TrimAnBean.setCoef_g2(cours.getCoefCours());
					String extreme_g2 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g2 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp2TrimAnBean.setExtreme_g2(extreme_g2);
					}
					
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc2 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc2>0){
						mGrp2TrimAnBean.setRang_g2(rc2+"e");
					}
					
					
					double moy_classe_g2 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(moy_classe_g2>0){
						mGrp2TrimAnBean.setMoy_classe_g2(moy_classe_g2);
					}
					
					
					double pourcentage_g2 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(pourcentage_g2>=0){
						mGrp2TrimAnBean.setPourcentage_g2(pourcentage_g2);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp2TrimAnBean.setAppreciation_g2(appreciationNote);
					

					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursLitteraireTrimAnnuelBean.add(mGrp2TrimAnBean);
					
					
				}
				/****
					fin du for sur les cours litteraire qui passe dans la classe
				 *****/
			
			//On place la liste des matieres scientifique construit
				bulletinTrimAn.setMatieresGroupe2TrimAnnuel(listofCoursLitteraireTrimAnnuelBean);
			
				
		
			

			List<MatiereGroupe3TrimAnnuelBean> listofCoursDiversTrimAnnuelBean 
					= new ArrayList<MatiereGroupe3TrimAnnuelBean>(); 

			int rc3 = 0;
			/***
			 * debut du for sur les cours Divers
			 * Gestion des cours Divers
			 */
			for(Cours cours : listofCoursDivers){
				//long debutforCoursTime = System.currentTimeMillis();
				
				MatiereGroupe3TrimAnnuelBean mGrp3TrimAnBean = new MatiereGroupe3TrimAnnuelBean();
				
				
				
				RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
						classeConcerne, cours, trimestreConcerne);
				
				String matiere = ub.subString(cours.getIntituleCours(), 17);
				matiere = matiere + ":";
				String codeMat = ub.subString(cours.getCodeCours(), 8);
				matiere = matiere + codeMat;
				
				String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
				
				String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
				nomProf = ub.subString(nomProf, 15);

				mGrp3TrimAnBean.setMatiere_g3(matiere);
				mGrp3TrimAnBean.setMatiere_g3_2emelang(matiere_2emelang);
				mGrp3TrimAnBean.setProf_g3(nomProf);
				
				double soenoteTrim = 0;
				int nbreNoteDansTrimPourCours = 0;
				

				for(Sequences seq : trimestreConcerne.getListofsequence()){
					if(seq.getNumeroSeq()%2==1){
						double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
						if(note_seq_g1>0){
							mGrp3TrimAnBean.setNote_1_g3(note_seq_g1);
							soenoteTrim = soenoteTrim + note_seq_g1;
							nbreNoteDansTrimPourCours +=1; 
						}
					}
					else{
						double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
						if(note_seq_g2>0){
							mGrp3TrimAnBean.setNote_2_g3(note_seq_g2);
							soenoteTrim = soenoteTrim + note_seq_g2;
							nbreNoteDansTrimPourCours +=1; 
						}
					}
				}
				
				double noteCoursTrim = 0;
				if(nbreNoteDansTrimPourCours>0){
					noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
					mGrp3TrimAnBean.setNote_trim_g3(noteCoursTrim);
					double total_trim_g3 = noteCoursTrim*cours.getCoefCours();
					mGrp3TrimAnBean.setTotal_trim_g3(total_trim_g3);
				}
				
				mGrp3TrimAnBean.setCoef_g3(cours.getCoefCours());
				String extreme_g3 = "";
				double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
				double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
				if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
					extreme_g3 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
					mGrp3TrimAnBean.setExtreme_g3(extreme_g3);
				}
				
				
				List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
				
				rc3 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
						listofEleveOrdreDecroissantPourCours);
				
				if(rc3>0){
					mGrp3TrimAnBean.setRang_g3(rc3+"e");
				}
				
				
				double moy_classe_g3 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
						cours, trimestreConcerne);
				if(moy_classe_g3>0){
					mGrp3TrimAnBean.setMoy_classe_g3(moy_classe_g3);
				}
				
				
				double pourcentage_g3 = ub.getTauxReussiteCoursTrim(classeConcerne, 
						cours, trimestreConcerne);
				if(pourcentage_g3>=0){
					mGrp3TrimAnBean.setPourcentage_g3(pourcentage_g3);
				}
				
				String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
				mGrp3TrimAnBean.setAppreciation_g3(appreciationNote);
				

				//On ajoute la ligne de cours dans le groupe correspondant
				listofCoursDiversTrimAnnuelBean.add(mGrp3TrimAnBean);
				
				
			}
			/****
				fin du for sur les cours Divers qui passe dans la classe
			 *****/
		
		//On place la liste des matieres scientifique construit
			bulletinTrimAn.setMatieresGroupe3TrimAnnuel(listofCoursDiversTrimAnnuelBean);
		

				
				
				
				long finforTime = System.currentTimeMillis();
				collectionofBulletionTrimAnnuel.add(bulletinTrimAn);
				System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
						" dans le trimestre "+trimestreConcerne.getNumeroTrim()+
						"  ajouter avec succes en "+(finforTime-startTimeFor));
				numBull++;
			
			}//fin du for sur les eleves pour les Bulletins sequentiels


			
			long finforTime = System.currentTimeMillis();
			System.out.println("Version opt A ce stade on a deja passe :"+ (finforTime-startTime));
			
			
			donnee.put("collectionofBulletionTrimAnnuel", collectionofBulletionTrimAnnuel);
			
			/****************************************************
			 * Conception du rapport de conseil de classe trimestriel
			 */
			FicheConseilClasseBean ficheCC = this.getRapportConseilClasseTrimestriel(etablissementConcerne, 
					anneeScolaire, classeConcerne, tauxReussite, moyenne_general, trimestreConcerne, 
					listofElevesOrdreDecroissantMoyenneTrimestriel);
			
			donnee.put("ficheconseilclassetriman", ficheCC);


			/****************************************************
			 * Conception du rapport de conseil de classe annuel
			 */
			FicheConseilClasseBean ficheCA = this.getRapportConseilClasseAnnuel(etablissementConcerne, 
					anneeScolaire, classeConcerne, t_reussite_an, moy_gen_classe_an,
					listofElevesOrdreDecroissantAnnee);
			
			donnee.put("ficheconseilclasseantrim", ficheCA);
			
			
		 
		return donnee;
	
	}

	@Override
	public Collection<BulletinTrimAnnuelBean> generate1BulletinTrimAnnuel(Long idEleve, Long idClasse,
			Long idTrimestre) {

		

		// TODO Auto-generated method stub
		
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestre);
		 Eleves eleveConcerne = usersService.findEleves(idEleve);
			
		 List<BulletinTrimAnnuelBean> collectionofBulletionTrimAnnuel = 
				 new ArrayList<BulletinTrimAnnuelBean>();
		
		 if((classeConcerne==null) || (trimestreConcerne==null) || (eleveConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueTrim = ub.getListOfCoursEvalueDansTrimestre(classeConcerne, 
					trimestreConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);


			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans un trimestre
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			
			RapportTrimestrielClasse rapportTrimestrielClasse = ub.getRapportTrimestrielClasse(classeConcerne, 
					listofEleveRegulier, trimestreConcerne);
			
			RapportAnnuelClasse rapportAnnuelClasse = ub.getRapportAnnuelClasse(classeConcerne, 
					listofEleveRegulier, anneeScolaire);
			
			double moyenne_premier_classe = rapportTrimestrielClasse.getValeurMoyennePremierDansTrim();
			
			double moyenne_dernier_classe = rapportTrimestrielClasse.getValeurMoyenneDernierDansTrim();
			
			int nbre_moyenne_classeSeq = rapportTrimestrielClasse.getNbreMoyennePourTrim();
			
			double tauxReussite = rapportTrimestrielClasse.getTauxReussiteTrimestriel();
			
			double moyenne_general = rapportTrimestrielClasse.getMoyenneGeneralTrimestre();
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =	ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseTrim = ub.geteffectifEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			double t_reussite_an=0.0;
			double moy_gen_classe_an=0.0;
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans le trimestre considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantTrimestre(classeConcerne, trimestreConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Trimestriellement
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trimestreConcerne);
			
			
			
			List<Eleves> listofElevesOrdreDecroissantAnnee = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneAnnuelOrdreDecroissant1(classeConcerne, anneeScolaire);
			
			
			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque séquence dans le trimestre
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneSequentiel = new 
					HashMap<Long, List<Eleves>>();

			for(Sequences seq : trimestreConcerne.getListofsequence()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = (List<Eleves>) 
						ub.getMoyenneSequentielOrdreDecroissant1(classeConcerne, seq);
				
				mapofElevesOrdreDecroissantMoyenneSequentiel.put(seq.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneSeq);
				
			}

			for(Eleves eleve : listofEleveClasse){
				if(eleve.getIdEleves() == eleveConcerne.getIdEleves()){
					long startTimeFor = System.currentTimeMillis();
					
					BulletinTrimAnnuelBean bulletinTrimAn = new BulletinTrimAnnuelBean();
					/*
					 * Initialisons les premieres donnees du bulletin trimestriel
					 */
					/****
					 * Information d'entete du bulletin
					 */
					bulletinTrimAn.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
					bulletinTrimAn.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
					bulletinTrimAn.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
					bulletinTrimAn.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
					bulletinTrimAn.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
					bulletinTrimAn.setEtablissement_fr(etablissementConcerne.getNomsEtab());
					bulletinTrimAn.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
							etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
					bulletinTrimAn.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
					bulletinTrimAn.setDevise_fr(etablissementConcerne.getDeviseEtab());
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setTitre_bulletin("Bulletin de note du trimestre "+trimestreConcerne.getNumeroTrim());
					}
					else{
						bulletinTrimAn.setTitre_bulletin("Report card of term "+trimestreConcerne.getNumeroTrim());
					}
					bulletinTrimAn.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
					bulletinTrimAn.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
					
					
					/***************
					 * Information personnel de l'élève
					 */
					bulletinTrimAn.setNumero(" "+eleve.getNumero(listofElevesClasse));
					bulletinTrimAn.setSexe(eleve.getSexeEleves());
					bulletinTrimAn.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
					bulletinTrimAn.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
					SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
					bulletinTrimAn.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
					bulletinTrimAn.setLieu_naissance_eleve(eleve.getLieunaissEleves());
					bulletinTrimAn.setMatricule_eleve(eleve.getMatriculeEleves());
					bulletinTrimAn.setRedoublant(eleve.getRedoublant());
					bulletinTrimAn.setClasse_eleve(classeString);
					bulletinTrimAn.setProf_principal(profPrincipal);
					bulletinTrimAn.setEffectif_classe(effectifTotalClasse);
					bulletinTrimAn.setEffectif_presente(effectifRegulierClasseTrim);
					
					/*****
					 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
					 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
					 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
					 * on va essayer de charger le fichier correspondant avec la classe File de java.io
					 */
					File f=new File(photoElevesDir+eleve.getIdEleves());
					//System.err.println("est ce que le fichier existe "+f.exists());
					
					if(f.exists()==true){
						bulletinTrimAn.setPhoto(photoElevesDir+eleve.getIdEleves()); 
					}
	
	
					/********
					 * Informations sur les labels d'entete des notes du bulletin trimestriel
					 */
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							bulletinTrimAn.setLabel_note_1("N S"+seq.getNumeroSeq());
						}
						else{
							bulletinTrimAn.setLabel_note_2("N S"+seq.getNumeroSeq());
						}
					}
					
	
					bulletinTrimAn.setLabel_trimestre("N T"+trimestreConcerne.getNumeroTrim());
					bulletinTrimAn.setLabel_trim_x_coef("N T"+trimestreConcerne.getNumeroTrim()+"*Coef");
			
					/***********
					 * Information sur les totaux trimestriels
					 */
					
					double total_coef = ub.getSommeCoefCoursComposeTrimestre(eleve, trimestreConcerne);
					bulletinTrimAn.setTotal_coef(total_coef);
					
					double total_points = ub.getTotalPointsTrimestriel(eleve, trimestreConcerne);
					if(total_points>0){
						bulletinTrimAn.setTotal_points(total_points);
					}
					
					/***********
					 * Informations sur les resultats trimestriels de l'eleve
					 */
					bulletinTrimAn.setResult_tt_coef(total_coef);
					
					if(total_points>0){
						bulletinTrimAn.setResult_tt_points(total_points);
					}
					
					
				
					//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
					
					/*int rang = this.getRangTrimestrielEleveAuMoinsUneNote(classeConcerne, trimestreConcerne, 
							eleve);*/
					 
					 int rang = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
							 listofElevesOrdreDecroissantMoyenneTrimestriel);
					 
					if(rang>0){
						bulletinTrimAn.setResult_rang_trim(rang+"e");
					}
					else{
						bulletinTrimAn.setResult_rang_trim("");
					}
					
					/********************************
					 * Info sur le resultat annuel a ce stade de l'élève
					 */
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_an("Résultat annuel");
					}
					else{
						bulletinTrimAn.setRappel_an("Annual result");
					}
					double moy_an = ub.getMoyenneAnnuel(eleve, anneeScolaire);
					
					if(moy_an>0)	bulletinTrimAn.setMoy_an(moy_an);
					
					double r_moy_an = moy_an;
					
					if(r_moy_an>=0) bulletinTrimAn.setR_moy_an(r_moy_an);
					
					int rang_an = ub.getRangAnnuelEleveAuMoinsUneNote(eleve, listofElevesOrdreDecroissantAnnee);
					if(rang_an>0) bulletinTrimAn.setRang_an(rang_an+"e");
					
					int r_rang_an = rang_an;
					if(r_rang_an>0) bulletinTrimAn.setR_rang_an(r_rang_an+"e");
					
					
					t_reussite_an = rapportAnnuelClasse.getTauxReussiteAnnuel();
					bulletinTrimAn.setT_reussite_an(t_reussite_an);
					
					moy_gen_classe_an = rapportAnnuelClasse.getMoyenneGeneralAnnuel();
					bulletinTrimAn.setMoy_gen_classe_an(moy_gen_classe_an);
				
					
					/*
					 * On doit faire cette liste des élèves pour tous les autres trimestres
					 * en fonction du numero de trimestre demande
					 */
					List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_1=null;
					List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_2=null;
					List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel_3=null;
					
					Trimestres trimestre1=null;
					Trimestres trimestre2=null;
					Trimestres trimestre3=null;
					
					int rang1=0;
					int rang2=0;
					int rang3=0;
					
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						if(trim.getNumeroTrim()==1){
							listofElevesOrdreDecroissantMoyenneTrimestriel_1 = (List<Eleves>) 
									UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
							 
							rang1 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									 listofElevesOrdreDecroissantMoyenneTrimestriel_1);
							
							trimestre1 = trim;
						}
						else if(trim.getNumeroTrim()==2){
							listofElevesOrdreDecroissantMoyenneTrimestriel_2 = (List<Eleves>) 
									UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
							
							rang2 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									 listofElevesOrdreDecroissantMoyenneTrimestriel_2);
							
							trimestre2 = trim;
						}
						else if(trim.getNumeroTrim()==3){
							listofElevesOrdreDecroissantMoyenneTrimestriel_3 = (List<Eleves>) 
									UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
							
							rang3 = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									 listofElevesOrdreDecroissantMoyenneTrimestriel_3);
							
							trimestre3 = trim;
						}
					}
					
					
					
					if(trimestreConcerne.getNumeroTrim()==1){
						if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setRappel_3("Trimestre1");
						}
						else{
							bulletinTrimAn.setRappel_3("Term 1");
						}
						bulletinTrimAn.setRappel_4("");
						bulletinTrimAn.setRappel_5("");
						double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
						if(r_moy_3>=0) bulletinTrimAn.setR_moy_3(r_moy_3);
						
						if(rang1>0){
							bulletinTrimAn.setR_rang_3(rang1+"e");
						}
						else{
							bulletinTrimAn.setR_rang_3("");
						}
					}
					else if(trimestreConcerne.getNumeroTrim()==2){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_3("Trimestre1");
						}
						else{
							bulletinTrimAn.setRappel_3("Term 1");
						}
						double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
						if(r_moy_3>=0) bulletinTrimAn.setR_moy_3(r_moy_3);
						
						if(rang1>0){
							bulletinTrimAn.setR_rang_3(rang1+"e");
						}
						else{
							bulletinTrimAn.setR_rang_3("");
						}
						
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_4("Trimestre2");
						}
						else{
							bulletinTrimAn.setRappel_4("Term 2");
						}
						double r_moy_4 = ub.getMoyenneTrimestriel(eleve, trimestre2);
						if(r_moy_4>=0) bulletinTrimAn.setR_moy_4(r_moy_4);
						
						if(rang2>0){
							bulletinTrimAn.setR_rang_4(rang2+"e");
						}
						else{
							bulletinTrimAn.setR_rang_4("");
						}
						bulletinTrimAn.setRappel_5("");
						
					}
					else if(trimestreConcerne.getNumeroTrim()==3){
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_3("Trimestre1");
						}
						else{
							bulletinTrimAn.setRappel_3("Term 1");
						}
						double r_moy_3 = ub.getMoyenneTrimestriel(eleve, trimestre1);
						if(r_moy_3>=0) bulletinTrimAn.setR_moy_3(r_moy_3);
						
						if(rang1>0){
							bulletinTrimAn.setR_rang_3(rang1+"e");
						}
						else{
							bulletinTrimAn.setR_rang_3("");
						}
						
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_4("Trimestre2");
						}
						else{
							bulletinTrimAn.setRappel_4("Term 2");
						}
						double r_moy_4 = ub.getMoyenneTrimestriel(eleve, trimestre2);
						if(r_moy_4>=0) bulletinTrimAn.setR_moy_4(r_moy_4);
						
						if(rang2>0){
							bulletinTrimAn.setR_rang_4(rang2+"e");
						}
						else{
							bulletinTrimAn.setR_rang_4("");
						}
						
						if(lang.equalsIgnoreCase("fr")==true){
							bulletinTrimAn.setRappel_5("Trimestre3");
						}
						else{
							bulletinTrimAn.setRappel_5("term 3");
						}
						double r_moy_5 = ub.getMoyenneTrimestriel(eleve, trimestre3);
						if(r_moy_5>=0) bulletinTrimAn.setR_moy_5(r_moy_5);
						
						if(rang3>0){
							bulletinTrimAn.setR_rang_5(rang3+"e");
						}
						else{
							bulletinTrimAn.setR_rang_5("");
						}
						
					}
					
					
					
					/*************************************************
					 * Informations sur le profil de la classe dans le trimestre
					 */
					if(moyenne_premier_classe>0){
						bulletinTrimAn.setMoy_premier(moyenne_premier_classe);
					}
					if(moyenne_dernier_classe>0){
						bulletinTrimAn.setMoy_dernier(moyenne_dernier_classe);
					}
					
					bulletinTrimAn.setNbre_moyennes(nbre_moyenne_classeSeq);
					if(tauxReussite>0){
						bulletinTrimAn.setTaux_reussite(tauxReussite);
					}
					if(moyenne_general>0){
						bulletinTrimAn.setMoy_gen_classe(moyenne_general);
					}
					
	
					/***********************
					 * Informations sur la conduite trimestriel de l'élève
					 */
					int nhaj = 0;
					int nhanj = 0;
					int nhc = 0;
					int nje = 0;
					
				
					
					nhanj = eleve.getNbreHeureAbsenceNonJustifieTrim(trimestreConcerne);
					nhaj = eleve.getNbreHeureAbsenceJustifieTrim(trimestreConcerne);
					
					bulletinTrimAn.setAbsence_NJ(nhanj);
					bulletinTrimAn.setAbsence_J(nhaj);
					bulletinTrimAn.setConsigne(nhc+"h");
					bulletinTrimAn.setExclusion(nje+" J");
					bulletinTrimAn.setAvertissement("");
					bulletinTrimAn.setBlame_conduite("");
					
					/************************
					 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(annee)
					 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
					 * ete infligées. On va commencer du trimestre de plus grand numero vers celui de plus petit et
					 *  de la séquence paire vers la séquence impair de chaque trimestre à chercher
					 */
					bulletinTrimAn.setRapport_disc1("");
					bulletinTrimAn.setRapport_disc2("");
					bulletinTrimAn.setRapport_disc3("");
					for(Trimestres trim : anneeScolaire.getListoftrimestre_DESC()){
							for(Sequences seq : trim.getListofsequence_DESC()){
								List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
								
								if(listofRDiscEleveSeq != null){
									if(listofRDiscEleveSeq.size()>0) {
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										//On peut donc fixer rapport_disc1
										bulletinTrimAn.setRapport_disc1(rdisc_chaine);
									}
									
									/*
									 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
									 * on va set rapport_disc2
									 */
									if(listofRDiscEleveSeq.size()>1) {
			
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										//On peut donc fixer rapport_disc1
										bulletinTrimAn.setRapport_disc2(rdisc_chaine);
									
									}
									
									if(listofRDiscEleveSeq.size()>2) {
			
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										
										//On peut donc fixer rapport_disc1
										bulletinTrimAn.setRapport_disc3(rdisc_chaine);
														
									}
									
							}
						}
					}
					
					
					/**************************
					 * Informations sur le rappel de la moyenne et du rang trimestriel
					 */
	
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						
						List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = 
								mapofElevesOrdreDecroissantMoyenneSequentiel.get(seq.getIdPeriodes());
						
						if(seq.getNumeroSeq()%2==1){
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinTrimAn.setRappel_1("Séquence "+seq.getNumeroSeq());
							}
							else{
								bulletinTrimAn.setRappel_1("Sequence "+seq.getNumeroSeq());
							}
							
							double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
							if(moy_seq>0){
								bulletinTrimAn.setR_moy_1(moy_seq);
							}
							
							int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneSeq);
							
							if(rangseq>0){
								bulletinTrimAn.setR_rang_1(rangseq+"e");
							}
							else{
								bulletinTrimAn.setR_rang_1("");
							}
						}
						else{
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinTrimAn.setRappel_2("Séquence "+seq.getNumeroSeq());
							}
							else{
								bulletinTrimAn.setRappel_2("Sequence "+seq.getNumeroSeq());
							}
							double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
							if(moy_seq>0){
								bulletinTrimAn.setR_moy_2(moy_seq);
							}
							int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneSeq);
							
							if(rangseq>0){
								bulletinTrimAn.setR_rang_2(rangseq+"e");
							}
							else{
								bulletinTrimAn.setR_rang_2("");
							}
						}
					}//fin du for sur les sequences
	
					/****************************
					 * Informations sur l'appreciation du travail de l'élève
					 */
					double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
					bulletinTrimAn.setTableau_hon("");
					bulletinTrimAn.setTableau_enc("");
					bulletinTrimAn.setTableau_fel("");
					String appreciation = ub.calculAppreciation(moy_trim,lang);
					bulletinTrimAn.setAppreciation(appreciation);
					/*
					 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
					 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
					 */
					DecisionConseil decConseil = eleve.getDecisionConseilPeriode(anneeScolaire.getIdPeriodes());
					bulletinTrimAn.setDistinction("");
					bulletinTrimAn.setDecision_conseil("");
					if(decConseil !=null){
						/*******************************
						 * Informations sur les distinctions octroyées  dans la séquence
						 */
						String distinction="";
						distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
						bulletinTrimAn.setDistinction(distinction);
						
						/*******************************
						 * Informations sur les decision du conseil de classe dans la séquence
						 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
						 * de classe. Et ici c'est le conseil de classe annuel donc la decision finale
						 */
						String decision="";
						decision += decConseil.getDecisionDecisionConseilString(lang);
						/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
						decision+=distinction;*/
						bulletinTrimAn.setDecision_conseil(decision);
					}
					
					
					
					List<Cours> listofCoursEffortAFournir = 
							ub.getListofCoursDansOrdreEffortAFournirTrimestre(eleve, listofCoursEvalueTrim, 
							trimestreConcerne);
					bulletinTrimAn.setEffort_matiere1("");
					bulletinTrimAn.setEffort_matiere2("");
					bulletinTrimAn.setEffort_matiere3("");
					if(listofCoursEffortAFournir.size()>0) {
						String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
						bulletinTrimAn.setEffort_matiere1(codeCours);
					}
					
					
					if(listofCoursEffortAFournir.size()>1) {
						String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
						bulletinTrimAn.setEffort_matiere2(codeCours);
					}
					
					if(listofCoursEffortAFournir.size()>2) {
						String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
						bulletinTrimAn.setEffort_matiere3(codeCours);
					}
					
					
					
					/*****************************
					 * Information sur l'espace VISA du bulletin
					 */
					bulletinTrimAn.setVille(villeEtab);
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres scientifique (Groupe1) dans le trimestre
					 * cccccccccccccccccccccccccc
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursScientifique = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursScientifique, trimestreConcerne);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setNom_g1("Scientifique");
					}
					else{
						bulletinTrimAn.setNom_g1("Scientific");
					}
					
					double total_coef_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
					
					bulletinTrimAn.setTotal_coef_g1(total_coef_g1);
					//System.err.println("total_coef_g1 == "+total_coef_g1);
					
					double total_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalNoteTrimElevePourGroupeCours();
					if(total_g1>0){
						bulletinTrimAn.setTotal_g1(total_g1);
					}
					
					String totalextreme_g1 = "";
					
					double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursScientifique, trimestreConcerne);
					
					double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursScientifique, trimestreConcerne);
					if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
						totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
								valeurMoyPremierGrpCours1+"]";
					}
					bulletinTrimAn.setTotal_extreme_g1(totalextreme_g1);
					
					int r1 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursScientifique, 
							trimestreConcerne, eleve);
					
					if(r1>0){
						bulletinTrimAn.setTotal_rang_g1(r1+"e");
					}
					
					
					double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursScientifique, trimestreConcerne);
					if(moy_gen_grp1>0){
						bulletinTrimAn.setMg_classe_g1(moy_gen_grp1);
					}
					
					double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursScientifique, trimestreConcerne);
					if(total_pourcentage_g1>=0){
						bulletinTrimAn.setTotal_pourcentage_g1(total_pourcentage_g1);
					}
					
					double moyenne_g1 = ligneTrimestrielGroupeCoursScientifique.
							getMoyenneTrimElevePourGroupeCours();
					if(moyenne_g1>0){
						bulletinTrimAn.setMoyenne_g1(moyenne_g1);
					}
	
	
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Litteraire (Groupe2) dans le trimestre
					 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursLitteraire = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursLitteraire, trimestreConcerne);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setNom_g2("Litteraire");
					}
					else{
						bulletinTrimAn.setNom_g2("Arts");
					}
					
					double total_coef_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
					
					bulletinTrimAn.setTotal_coef_g2(total_coef_g2);
					
					double total_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalNoteTrimElevePourGroupeCours();
					if(total_g2>0){
						bulletinTrimAn.setTotal_g2(total_g2);
					}
					
					String totalextreme_g2 = "";
					
					double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
					
					double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
					if(valeurMoyDernierGrpCours2>=0 && valeurMoyPremierGrpCours2>0){
						totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
								valeurMoyPremierGrpCours2+"]";
					}
					bulletinTrimAn.setTotal_extreme_g2(totalextreme_g2);
					
					int r2 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursLitteraire, 
							trimestreConcerne, eleve);
					
					if(r2>0){
						bulletinTrimAn.setTotal_rang_g2(r2+"e");
					}
					
					
					double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursLitteraire, trimestreConcerne);
					if(moy_gen_grp2>0){
						bulletinTrimAn.setMg_classe_g2(moy_gen_grp2);
					}
					
					double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursLitteraire, trimestreConcerne);
					if(total_pourcentage_g2>=0){
						bulletinTrimAn.setTotal_pourcentage_g2(total_pourcentage_g2);
					}
					
					double moyenne_g2 = ligneTrimestrielGroupeCoursLitteraire.
							getMoyenneTrimElevePourGroupeCours();
					if(moyenne_g2>0){
						bulletinTrimAn.setMoyenne_g2(moyenne_g2);
					}
			
					
	
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Divers (Groupe3) dans le trimestre
					 * ddddddddddddddddddddddddddddddddddd
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursDivers = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursDivers, trimestreConcerne);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrimAn.setNom_g3("Divers");
					}
					else{
						bulletinTrimAn.setNom_g3("Others");
					}
					double total_coef_g3 = ligneTrimestrielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
					
					bulletinTrimAn.setTotal_coef_g3(total_coef_g3);
					
					double total_g3 = ligneTrimestrielGroupeCoursDivers.getTotalNoteTrimElevePourGroupeCours();
					if(total_g3>0){
						bulletinTrimAn.setTotal_g3(total_g3);
					}
					
					String totalextreme_g3 = "";
					
					double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursDivers, trimestreConcerne);
					
					double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursDivers, trimestreConcerne);
					if(valeurMoyDernierGrpCours3>=0 && valeurMoyPremierGrpCours3>0){
						totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
								valeurMoyPremierGrpCours3+"]";
					}
					bulletinTrimAn.setTotal_extreme_g3(totalextreme_g3);
					
					int r3 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursDivers, 
							trimestreConcerne, eleve);
					
					if(r3>0){
						bulletinTrimAn.setTotal_rang_g3(r3+"e");
					}
					
					
					double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursDivers, trimestreConcerne);
					if(moy_gen_grp3>0){
						bulletinTrimAn.setMg_classe_g3(moy_gen_grp3);
					}
					
					double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursDivers, trimestreConcerne);
					if(total_pourcentage_g3>=0){
						bulletinTrimAn.setTotal_pourcentage_g3(total_pourcentage_g3);
					}
					
					double moyenne_g3 = ligneTrimestrielGroupeCoursDivers.
							getMoyenneTrimElevePourGroupeCours();
					if(moyenne_g3>0){
						bulletinTrimAn.setMoyenne_g3(moyenne_g3);
					}
					
	
					/************************************
					 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
					 **********/
	
					List<MatiereGroupe1TrimAnnuelBean> listofCoursScientifiqueTrimAnnuelBean 
							= new ArrayList<MatiereGroupe1TrimAnnuelBean>(); 
		
					int rc1 = 0;
					/***
					 * debut du for sur les cours scientifique
					 * Gestion des cours scientifique
					 */
					for(Cours cours : listofCoursScientifique){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe1TrimAnnuelBean mGrp1TrimAnBean = new MatiereGroupe1TrimAnnuelBean();
						
						
						
						RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
								classeConcerne, cours, trimestreConcerne);
						
						String matiere = ub.subString(cours.getIntituleCours(), 17);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 8);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
						
						mGrp1TrimAnBean.setMatiere_g1(matiere);
						mGrp1TrimAnBean.setMatiere_g1_2emelang(matiere_2emelang);
						mGrp1TrimAnBean.setProf_g1(cours.getProffesseur().getNomsPers());
						
						
						double soenoteTrim = 0;
						int nbreNoteDansTrimPourCours = 0;
						
						for(Sequences seq : trimestreConcerne.getListofsequence()){
							if(seq.getNumeroSeq()%2==1){
								double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								if(note_seq_g1>0){
									mGrp1TrimAnBean.setNote_1_g1(note_seq_g1);
									soenoteTrim = soenoteTrim + note_seq_g1;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
							else{
								double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								if(note_seq_g2>0){
									mGrp1TrimAnBean.setNote_2_g1(note_seq_g2);
									soenoteTrim = soenoteTrim + note_seq_g2;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
						}
						
						
						double noteCoursTrim = 0;
						if(nbreNoteDansTrimPourCours>0){
							noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
							mGrp1TrimAnBean.setNote_trim_g1(noteCoursTrim);
							double total_trim_g1 = noteCoursTrim*cours.getCoefCours();
							mGrp1TrimAnBean.setTotal_trim_g1(total_trim_g1);
						}
						
						mGrp1TrimAnBean.setCoef_g1(cours.getCoefCours());
						
						String extreme_g1 = "";
						double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
						double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
						if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
							extreme_g1 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
							mGrp1TrimAnBean.setExtreme_g1(extreme_g1);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc1 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc1>0){
							mGrp1TrimAnBean.setRang_g1(rc1+"e");
						}
						
						
						double moy_classe_g1 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						if(moy_classe_g1>0){
							mGrp1TrimAnBean.setMoy_classe_g1(moy_classe_g1);
						}
						
						
						double pourcentage_g1 = ub.getTauxReussiteCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						if(pourcentage_g1>=0){
							mGrp1TrimAnBean.setPourcentage_g1(pourcentage_g1);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
						mGrp1TrimAnBean.setAppreciation_g1(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursScientifiqueTrimAnnuelBean.add(mGrp1TrimAnBean);
						
					}
					/****
						fin du for sur les cours scientifique qui passe dans la classe
					*****/
					
					//On place la liste des matieres scientifique construit
					bulletinTrimAn.setMatieresGroupe1TrimAnnuel(listofCoursScientifiqueTrimAnnuelBean);
					
					
					List<MatiereGroupe2TrimAnnuelBean> listofCoursLitteraireTrimAnnuelBean 
							= new ArrayList<MatiereGroupe2TrimAnnuelBean>(); 
				
					int rc2 = 0;
					/***
					 * debut du for sur les cours Litteraire
					 * Gestion des cours Litteraire
					 */
	
					for(Cours cours : listofCoursLitteraire){
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe2TrimAnnuelBean mGrp2TrimAnBean = new MatiereGroupe2TrimAnnuelBean();
						
						
						
						RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
								classeConcerne, cours, trimestreConcerne);
					
						String matiere = ub.subString(cours.getIntituleCours(), 17);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 8);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
	
						mGrp2TrimAnBean.setMatiere_g2(matiere);
						mGrp2TrimAnBean.setMatiere_g2_2emelang(matiere_2emelang);
						mGrp2TrimAnBean.setProf_g2(nomProf);
						
						double soenoteTrim = 0;
						int nbreNoteDansTrimPourCours = 0;
						
	
						for(Sequences seq : trimestreConcerne.getListofsequence()){
							if(seq.getNumeroSeq()%2==1){
								double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								if(note_seq_g1>0){
									mGrp2TrimAnBean.setNote_1_g2(note_seq_g1);
									soenoteTrim = soenoteTrim + note_seq_g1;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
							else{
								double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								if(note_seq_g2>0){
									mGrp2TrimAnBean.setNote_2_g2(note_seq_g2);
									soenoteTrim = soenoteTrim + note_seq_g2;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
						}
						
						double noteCoursTrim = 0;
						if(nbreNoteDansTrimPourCours>0){
							noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
							mGrp2TrimAnBean.setNote_trim_g2(noteCoursTrim);
							double total_trim_g2 = noteCoursTrim*cours.getCoefCours();
							mGrp2TrimAnBean.setTotal_trim_g2(total_trim_g2);
						}
						
						mGrp2TrimAnBean.setCoef_g2(cours.getCoefCours());
						String extreme_g2 = "";
						double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
						double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
						if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
							extreme_g2 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
							mGrp2TrimAnBean.setExtreme_g2(extreme_g2);
						}
						
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc2 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc2>0){
							mGrp2TrimAnBean.setRang_g2(rc2+"e");
						}
						
						
						double moy_classe_g2 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						if(moy_classe_g2>0){
							mGrp2TrimAnBean.setMoy_classe_g2(moy_classe_g2);
						}
						
						
						double pourcentage_g2 = ub.getTauxReussiteCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						if(pourcentage_g2>=0){
							mGrp2TrimAnBean.setPourcentage_g2(pourcentage_g2);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
						mGrp2TrimAnBean.setAppreciation_g2(appreciationNote);
						
	
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursLitteraireTrimAnnuelBean.add(mGrp2TrimAnBean);
						
						
					}
					/****
						fin du for sur les cours litteraire qui passe dans la classe
					 *****/
				
				//On place la liste des matieres scientifique construit
					bulletinTrimAn.setMatieresGroupe2TrimAnnuel(listofCoursLitteraireTrimAnnuelBean);
				
					
			
				
	
				List<MatiereGroupe3TrimAnnuelBean> listofCoursDiversTrimAnnuelBean 
						= new ArrayList<MatiereGroupe3TrimAnnuelBean>(); 
	
				int rc3 = 0;
				/***
				 * debut du for sur les cours Divers
				 * Gestion des cours Divers
				 */
				for(Cours cours : listofCoursDivers){
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe3TrimAnnuelBean mGrp3TrimAnBean = new MatiereGroupe3TrimAnnuelBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 17);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 8);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
	
					mGrp3TrimAnBean.setMatiere_g3(matiere);
					mGrp3TrimAnBean.setMatiere_g3_2emelang(matiere_2emelang);
					mGrp3TrimAnBean.setProf_g3(nomProf);
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					
	
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g1>0){
								mGrp3TrimAnBean.setNote_1_g3(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g2>0){
								mGrp3TrimAnBean.setNote_2_g3(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						mGrp3TrimAnBean.setNote_trim_g3(noteCoursTrim);
						double total_trim_g3 = noteCoursTrim*cours.getCoefCours();
						mGrp3TrimAnBean.setTotal_trim_g3(total_trim_g3);
					}
					
					mGrp3TrimAnBean.setCoef_g3(cours.getCoefCours());
					String extreme_g3 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g3 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp3TrimAnBean.setExtreme_g3(extreme_g3);
					}
					
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc3 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc3>0){
						mGrp3TrimAnBean.setRang_g3(rc3+"e");
					}
					
					
					double moy_classe_g3 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(moy_classe_g3>0){
						mGrp3TrimAnBean.setMoy_classe_g3(moy_classe_g3);
					}
					
					
					double pourcentage_g3 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					if(pourcentage_g3>=0){
						mGrp3TrimAnBean.setPourcentage_g3(pourcentage_g3);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp3TrimAnBean.setAppreciation_g3(appreciationNote);
					
	
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursDiversTrimAnnuelBean.add(mGrp3TrimAnBean);
					
					
				}
				/****
					fin du for sur les cours Divers qui passe dans la classe
				 *****/
			
			//On place la liste des matieres scientifique construit
				bulletinTrimAn.setMatieresGroupe3TrimAnnuel(listofCoursDiversTrimAnnuelBean);
			
	
					
					
					
					long finforTime = System.currentTimeMillis();
					collectionofBulletionTrimAnnuel.add(bulletinTrimAn);
					System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
							" dans le trimestre "+trimestreConcerne.getNumeroTrim()+
							"  ajouter avec succes en "+(finforTime-startTimeFor));
					numBull++;
				}
			}//fin du for sur les eleves pour les Bulletins sequentiels


			
			
		 
		return collectionofBulletionTrimAnnuel;
	
		
	
	}

	@Override
	public Collection<BulletinTrimestreBean> generate1BulletinTrimestre(Long idEleve, Long idClasse, Long idTrimestre) {

		List<BulletinTrimestreBean> collectionofBulletinTrim = new ArrayList<BulletinTrimestreBean>();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestre);
		 Eleves eleveConcerne = usersService.findEleves(idEleve);
		
		 if((classeConcerne==null) || (trimestreConcerne==null) || (eleveConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueTrim = ub.getListOfCoursEvalueDansTrimestre(classeConcerne, 
					trimestreConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans un trimestre
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			
			RapportTrimestrielClasse rapportTrimestrielClasse = ub.getRapportTrimestrielClasse(classeConcerne, 
					listofEleveRegulier, trimestreConcerne);
			
			double moyenne_premier_classe=0;
			double moyenne_dernier_classe =0;
			double tauxReussite=0;
			double moyenne_general = 0;
			int nbre_moyenne_classeSeq = 0;
		
			moyenne_premier_classe = rapportTrimestrielClasse.getValeurMoyennePremierDansTrim();
			moyenne_dernier_classe = rapportTrimestrielClasse.getValeurMoyenneDernierDansTrim();
			nbre_moyenne_classeSeq = rapportTrimestrielClasse.getNbreMoyennePourTrim();
			tauxReussite = rapportTrimestrielClasse.getTauxReussiteTrimestriel();
			moyenne_general = rapportTrimestrielClasse.getMoyenneGeneralTrimestre();
			
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseTrim = ub.geteffectifEleveRegulierTrimestre(classeConcerne, trimestreConcerne);
			
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans le trimestre considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantTrimestre(classeConcerne, trimestreConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Trimestriellement
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trimestreConcerne);
			
			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque séquence dans le trimestre
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneSequentiel = new 
					HashMap<Long, List<Eleves>>();
			
			for(Sequences seq : trimestreConcerne.getListofsequence()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = (List<Eleves>) 
						ub.getMoyenneSequentielOrdreDecroissant1(classeConcerne, seq);
				
				mapofElevesOrdreDecroissantMoyenneSequentiel.put(seq.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneSeq);
				
			}
			
			for(Eleves eleve : listofEleveClasse){
				if(eleve.getIdEleves() == eleveConcerne.getIdEleves()){
					long startTimeFor = System.currentTimeMillis();
					
					BulletinTrimestreBean bulletinTrim = new BulletinTrimestreBean();
					/*
					 * Initialisons les premieres donnees du bulletin trimestriel
					 */
					/****
					 * Information d'entete du bulletin
					 */
					bulletinTrim.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
					bulletinTrim.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
					bulletinTrim.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
					bulletinTrim.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
					bulletinTrim.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
					bulletinTrim.setEtablissement_fr(etablissementConcerne.getNomsEtab());
					bulletinTrim.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
							etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
					bulletinTrim.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
					bulletinTrim.setDevise_fr(etablissementConcerne.getDeviseEtab());
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrim.setTitre_bulletin("Bulletin de note du trimestre "+trimestreConcerne.getNumeroTrim());
					}
					else{
						bulletinTrim.setTitre_bulletin("Report card of term "+trimestreConcerne.getNumeroTrim());
					}
					bulletinTrim.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
					bulletinTrim.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
					
					
					/***************
					 * Information personnel de l'élève
					 */
					bulletinTrim.setNumero(" "+eleve.getNumero(listofElevesClasse));
					bulletinTrim.setSexe(eleve.getSexeEleves());
					bulletinTrim.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
					bulletinTrim.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
					SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
					bulletinTrim.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
					bulletinTrim.setLieu_naissance_eleve(eleve.getLieunaissEleves());
					bulletinTrim.setMatricule_eleve(eleve.getMatriculeEleves());
					bulletinTrim.setRedoublant(eleve.getRedoublant());
					bulletinTrim.setClasse_eleve(classeString);
					bulletinTrim.setProf_principal(profPrincipal);
					bulletinTrim.setEffectif_classe(effectifTotalClasse);
					bulletinTrim.setEffectif_presente(effectifRegulierClasseTrim);
					
					/*****
					 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
					 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
					 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
					 * on va essayer de charger le fichier correspondant avec la classe File de java.io
					 */
					File f=new File(photoElevesDir+eleve.getIdEleves());
					//System.err.println("est ce que le fichier existe "+f.exists());
					
					if(f.exists()==true){
						bulletinTrim.setPhoto(photoElevesDir+eleve.getIdEleves()); 
					}
					
					/********
					 * Informations sur les labels d'entete des notes du bulletin trimestriel
					 */
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							bulletinTrim.setLabel_note_1("N S"+seq.getNumeroSeq());
						}
						else{
							bulletinTrim.setLabel_note_2("N S"+seq.getNumeroSeq());
						}
					}
					
					bulletinTrim.setLabel_trimestre("N T"+trimestreConcerne.getNumeroTrim());
					bulletinTrim.setLabel_trim_x_coef("N T"+trimestreConcerne.getNumeroTrim()+"*Coef");
			
					/***********
					 * Information sur les totaux trimestriels
					 */
					
					double total_coef = ub.getSommeCoefCoursComposeTrimestre(eleve, trimestreConcerne);
					bulletinTrim.setTotal_coef(total_coef);
					
					double total_points = ub.getTotalPointsTrimestriel(eleve, trimestreConcerne);
					
					if(total_points>0){
						bulletinTrim.setTotal_points(total_points);
					}
					
					if(total_coef>0){
						double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
						if(moy_trim>=0)	bulletinTrim.setResult_moy_trim(moy_trim);
					}
					
					/***********
					 * Informations sur les resultats trimestriels de l'eleve
					 */
					bulletinTrim.setResult_tt_coef(total_coef);
					
					if(total_points>0){
						bulletinTrim.setResult_tt_points(total_points);
					}
					
					//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
					
					/*int rang = this.getRangTrimestrielEleveAuMoinsUneNote(classeConcerne, trimestreConcerne, 
							eleve);*/
					 
					 int rang = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
							 listofElevesOrdreDecroissantMoyenneTrimestriel);
					 
					if(rang>0){
						bulletinTrim.setResult_rang_trim(rang+"e");
						bulletinTrim.setR_rang_trim(rang+"e");
					}
					else{
						bulletinTrim.setResult_rang_trim("");
					}
					
					
					/*************************************************
					 * Informations sur le profil de la classe dans le trimestre
					 */
					if(moyenne_premier_classe>0){
						bulletinTrim.setMoy_premier(moyenne_premier_classe);
					}
					if(moyenne_dernier_classe>0){
						bulletinTrim.setMoy_dernier(moyenne_dernier_classe);
					}
					bulletinTrim.setNbre_moyennes(nbre_moyenne_classeSeq);
					if(tauxReussite>0){
						bulletinTrim.setTaux_reussite(tauxReussite);
					}
					if(moyenne_general>0){
						bulletinTrim.setMoy_gen_classe(moyenne_general);
					}
					
					
					/***********************
					 * Informations sur la conduite trimestriel de l'élève
					 */
					int nhaj = 0;
					int nhanj = 0;
					
					/*for(Sequences seq : trimestreConcerne.getListofsequence()){
						RapportDAbsence rabs = eleve.getRapportDAbsenceSeq(seq.getIdPeriodes());
						if(rabs!=null){
							nhaj = nhaj + rabs.getNbreheureJustifie();
							nhanj = nhanj + rabs.getNbreheureNJustifie();
							nhc = nhc + rabs.getConsigne();
							nje = nje + rabs.getJourExclusion();
						}
					}*/
					
					nhanj = eleve.getNbreHeureAbsenceNonJustifieTrim(trimestreConcerne);
					nhaj = eleve.getNbreHeureAbsenceJustifieTrim(trimestreConcerne);
					
					bulletinTrim.setAbsence_NJ(nhanj);
					bulletinTrim.setAbsence_J(nhaj);
					bulletinTrim.setConsigne("");
					bulletinTrim.setExclusion("");
					bulletinTrim.setAvertissement("");
					bulletinTrim.setBlame_conduite("");
					
					/************************
					 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(trimestre)
					 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
					 * ete infligées. On va commencer de la séquence paire vers la séquence impair à chercher
					 * Il est important de noter qu'il s'agit des sanctions déjà exécutées pendant la période. 
					 */
					bulletinTrim.setRapport_disc1("");
					bulletinTrim.setRapport_disc2("");
					bulletinTrim.setRapport_disc3("");
					for(Sequences seq : trimestreConcerne.getListofsequence_DESC()){
						List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
						
						if(listofRDiscEleveSeq != null){
							if(listofRDiscEleveSeq.size()>0) {
								RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
								String rdisc_chaine = "";
								rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
								//On peut donc fixer rapport_disc1
								bulletinTrim.setRapport_disc1(rdisc_chaine);
							}
							
							/*
							 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
							 * on va set rapport_disc2
							 */
							if(listofRDiscEleveSeq.size()>1) {
	
								RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
								String rdisc_chaine = "";
								rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
								//On peut donc fixer rapport_disc1
								bulletinTrim.setRapport_disc2(rdisc_chaine);
							
							}
							
							if(listofRDiscEleveSeq.size()>2) {
	
								RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
								String rdisc_chaine = "";
								rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
								
								//On peut donc fixer rapport_disc1
								bulletinTrim.setRapport_disc3(rdisc_chaine);
												
							}
							
						}
						
					}
					
					/**************************
					 * Informations sur le rappel de la moyenne et du rang trimestriel
					 */
					
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						
						List<Eleves> listofElevesOrdreDecroissantMoyenneSeq = 
								mapofElevesOrdreDecroissantMoyenneSequentiel.get(seq.getIdPeriodes());
						
						if(seq.getNumeroSeq()%2==1){
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinTrim.setRappel_1("Séquence "+seq.getNumeroSeq());
							}
							else{
								bulletinTrim.setRappel_1("Sequence "+seq.getNumeroSeq());
							}
							
							double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
							
							if(moy_seq>0){
								bulletinTrim.setR_moy_1(moy_seq);
							}
							
							int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneSeq);
							
							if(rangseq>0){
								bulletinTrim.setR_rang_1(rangseq+"e");
							}
							else{
								bulletinTrim.setR_rang_1("");
							}
						}
						else{
							
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinTrim.setRappel_2("Séquence "+seq.getNumeroSeq());
							}
							else{
								bulletinTrim.setRappel_2("Sequence "+seq.getNumeroSeq());
							}
							
							double moy_seq = ub.getMoyenneSequentiel(eleve, seq);
							
							if(moy_seq>0){
								bulletinTrim.setR_moy_2(moy_seq);
							}
							int rangseq = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneSeq);
							
							if(rangseq>0){
								bulletinTrim.setR_rang_2(rangseq+"e");
							}
							else{
								bulletinTrim.setR_rang_2("");
							}
						}
						
					}//fin du for sur les sequences
					
					
					
					
					/****************************
					 * Informations sur l'appreciation du travail de l'élève
					 */
					double moy_trim = ub.getMoyenneTrimestriel(eleve, trimestreConcerne);
					
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrim.setRappel_3("Trimestre "+trimestreConcerne.getNumeroTrim());
					}
					else{
						bulletinTrim.setRappel_3("Term "+trimestreConcerne.getNumeroTrim());
					}
					
					if(moy_trim>=0)	bulletinTrim.setR_moy_trim(moy_trim);
					
					bulletinTrim.setTableau_hon("");
					bulletinTrim.setTableau_enc("");
					bulletinTrim.setTableau_fel("");
					String appreciation = ub.calculAppreciation(moy_trim,lang);
					bulletinTrim.setAppreciation(appreciation);
	
					
					/*
					 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
					 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
					 */
					DecisionConseil decConseil = eleve.getDecisionConseilPeriode(trimestreConcerne.getIdPeriodes());
					bulletinTrim.setDistinction("");
					bulletinTrim.setDecision_conseil("");
					if(decConseil !=null){
						/*******************************
						 * Informations sur les distinctions octroyées  dans la séquence
						 */
						String distinction="";
						distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
						bulletinTrim.setDistinction(distinction);
						
						/*******************************
						 * Informations sur les decision du conseil de classe dans la séquence
						 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
						 * de classe.
						 */
						String decision="";
						decision += decConseil.getSanctionDiscDecisionConseilString(lang);
						/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
						decision+=distinction;*/
						bulletinTrim.setDecision_conseil(decision);
					}
					
					
					
					List<Cours> listofCoursEffortAFournir = 
							ub.getListofCoursDansOrdreEffortAFournirTrimestre(eleve, listofCoursEvalueTrim, 
							trimestreConcerne);
					bulletinTrim.setEffort_matiere1("");
					bulletinTrim.setEffort_matiere2("");
					bulletinTrim.setEffort_matiere3("");
					if(listofCoursEffortAFournir.size()>0) {
						String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
						bulletinTrim.setEffort_matiere1(codeCours);
					}
					
					
					if(listofCoursEffortAFournir.size()>1) {
						String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
						bulletinTrim.setEffort_matiere2(codeCours);
					}
					
					if(listofCoursEffortAFournir.size()>2) {
						String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
						bulletinTrim.setEffort_matiere3(codeCours);
					}
					
					
					
					/*****************************
					 * Information sur l'espace VISA du bulletin
					 */
					bulletinTrim.setVille(villeEtab);
					
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres scientifique (Groupe1) dans le trimestre
					 * cccccccccccccccccccccccccc
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursScientifique = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursScientifique, trimestreConcerne);
					
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrim.setNom_g1("Scientifique");
					}
					else{
						bulletinTrim.setNom_g1("Scientific");
					}
					
					double total_coef_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
					
					bulletinTrim.setTotal_coef_g1(total_coef_g1);
					//System.err.println("total_coef_g1 == "+total_coef_g1);
					
					double total_g1 = ligneTrimestrielGroupeCoursScientifique.getTotalNoteTrimElevePourGroupeCours();
					
					if(total_g1>0){
						bulletinTrim.setTotal_g1(total_g1);
					}
					
					String totalextreme_g1 = "";
					
					double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursScientifique, trimestreConcerne);
					
					double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursScientifique, trimestreConcerne);
					
					if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
						totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
								valeurMoyPremierGrpCours1+"]";
					}
					bulletinTrim.setTotal_extreme_g1(totalextreme_g1);
					
					int r1 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursScientifique, 
							trimestreConcerne, eleve);
					
					if(r1>0){
						bulletinTrim.setTotal_rang_g1(r1+"e");
					}
					
					
					double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursScientifique, trimestreConcerne);
					if(moy_gen_grp1>0){
						bulletinTrim.setMg_classe_g1(moy_gen_grp1);
					}
					
					double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursScientifique, trimestreConcerne);
					
					if(total_pourcentage_g1>=0){
						bulletinTrim.setTotal_pourcentage_g1(total_pourcentage_g1);
					}
					
					
					
					double moyenne_g1 = ligneTrimestrielGroupeCoursScientifique.
							getMoyenneTrimElevePourGroupeCours();
					
					if(moyenne_g1>0){
						bulletinTrim.setMoyenne_g1(moyenne_g1);
					}
			
					
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Litteraire (Groupe2) dans le trimestre
					 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursLitteraire = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursLitteraire, trimestreConcerne);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrim.setNom_g2("Litteraire");
					}
					else{
						bulletinTrim.setNom_g2("Arts");
					}
					
					double total_coef_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
					
					bulletinTrim.setTotal_coef_g2(total_coef_g2);
					
					double total_g2 = ligneTrimestrielGroupeCoursLitteraire.getTotalNoteTrimElevePourGroupeCours();
					
					if(total_g2>0){
						bulletinTrim.setTotal_g2(total_g2);
					}
					
					String totalextreme_g2 = "";
					
					double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
					
					double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursLitteraire, trimestreConcerne);
					
					if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
						totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
								valeurMoyPremierGrpCours2+"]";
					}
					bulletinTrim.setTotal_extreme_g2(totalextreme_g2);
					
					int r2 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursLitteraire, 
							trimestreConcerne, eleve);
					
					if(r2>0){
						bulletinTrim.setTotal_rang_g2(r2+"e");
					}
					
					
					double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursLitteraire, trimestreConcerne);
					
					if(moy_gen_grp2>0){
						bulletinTrim.setMg_classe_g2(moy_gen_grp2);
					}
					
					double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursLitteraire, trimestreConcerne);
					
					if(total_pourcentage_g2>=0){
						bulletinTrim.setTotal_pourcentage_g2(total_pourcentage_g2);
					}
					
					double moyenne_g2 = ligneTrimestrielGroupeCoursLitteraire.
							getMoyenneTrimElevePourGroupeCours();
					
					if(moyenne_g2>0){
						bulletinTrim.setMoyenne_g2(moyenne_g2);
					}
			
					
	
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Divers (Groupe3) dans le trimestre
					 * ddddddddddddddddddddddddddddddddddd
					 */
					
					LigneTrimestrielGroupeCours ligneTrimestrielGroupeCoursDivers = 
							ub.getLigneTrimestrielGroupeCours(eleve, listofCoursDivers, trimestreConcerne);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinTrim.setNom_g3("Divers");
					}
					else{
						bulletinTrim.setNom_g3("Orthers");
					}
					
					double total_coef_g3 = ligneTrimestrielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
					
					bulletinTrim.setTotal_coef_g3(total_coef_g3);
					
					double total_g3 = ligneTrimestrielGroupeCoursDivers.getTotalNoteTrimElevePourGroupeCours();
					
					if(total_g3>0){
						bulletinTrim.setTotal_g3(total_g3);
					}
					
					String totalextreme_g3 = "";
					
					double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansTrim(
							listofElevesClasse, listofCoursDivers, trimestreConcerne);
					
					double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansTrim(
							listofElevesClasse, listofCoursDivers, trimestreConcerne);
					
					if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
						totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
								valeurMoyPremierGrpCours3+"]";
					}
					bulletinTrim.setTotal_extreme_g3(totalextreme_g3);
					
					int r3 = ub.getRangMoyenneTrimElevePourGroupe(classeConcerne, listofCoursDivers, 
							trimestreConcerne, eleve);
					
					if(r3>0){
						bulletinTrim.setTotal_rang_g3(r3+"e");
					}
					
					
					double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursTrim(classeConcerne, 
							listofCoursDivers, trimestreConcerne);
					
					if(moy_gen_grp3>0){
						bulletinTrim.setMg_classe_g3(moy_gen_grp3);
					}
					
					double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursTrim(classeConcerne, 
							listofCoursDivers, trimestreConcerne);
					if(total_pourcentage_g3>=0){
						bulletinTrim.setTotal_pourcentage_g3(total_pourcentage_g3);
					}
					
					
					
					double moyenne_g3 = ligneTrimestrielGroupeCoursDivers.
							getMoyenneTrimElevePourGroupeCours();
					
					if(moyenne_g3>0){
						bulletinTrim.setMoyenne_g3(moyenne_g3);
					}
					
	
					/************************************
					 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
					 **********/
					
					
					List<MatiereGroupe1TrimestreBean> listofCoursScientifiqueTrimestreBean 
								= new ArrayList<MatiereGroupe1TrimestreBean>(); 
					
					int rc1 = 0;
					/***
					 * debut du for sur les cours scientifique
					 * Gestion des cours scientifique
					 */
					for(Cours cours : listofCoursScientifique){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe1TrimestreBean mGrp1TrimBean = new MatiereGroupe1TrimestreBean();
						
						
						
						RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
								classeConcerne, cours, trimestreConcerne);
						
						String matiere = ub.subString(cours.getIntituleCours(), 15);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 5);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
						
						mGrp1TrimBean.setMatiere_g1(matiere);
						mGrp1TrimBean.setMatiere_g1_2emelang(matiere_2emelang);
						mGrp1TrimBean.setProf_g1(nomProf);
						
						double soenoteTrim = 0;
						int nbreNoteDansTrimPourCours = 0;
						
						for(Sequences seq : trimestreConcerne.getListofsequence()){
							if(seq.getNumeroSeq()%2==1){
								double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								
								if(note_seq_g1>0){
									mGrp1TrimBean.setNote_1_g1(note_seq_g1);
									soenoteTrim = soenoteTrim + note_seq_g1;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
							else{
								double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								
								if(note_seq_g2>0){
									mGrp1TrimBean.setNote_2_g1(note_seq_g2);
									soenoteTrim = soenoteTrim + note_seq_g2;
									nbreNoteDansTrimPourCours +=1; 
								}
							}
						}
						
						
						double noteCoursTrim = 0;
						if(nbreNoteDansTrimPourCours>0){
							noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
							
							mGrp1TrimBean.setNote_trim_g1(noteCoursTrim);
							double total_trim_g1 = noteCoursTrim*cours.getCoefCours();
							mGrp1TrimBean.setTotal_trim_g1(total_trim_g1);
							//System.out.println("Calculss "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g1);
						}
						
						mGrp1TrimBean.setCoef_g1(cours.getCoefCours());
						
						String extreme_g1 = "";
						double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
						double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
						
						if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
							extreme_g1 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
							mGrp1TrimBean.setExtreme_g1(extreme_g1);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc1 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc1>0){
							mGrp1TrimBean.setRang_g1(rc1+"e");
						}
						
						
						double moy_classe_g1 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						
						if(moy_classe_g1>0){
							mGrp1TrimBean.setMoy_classe_g1(moy_classe_g1);
						}
						
						
						double pourcentage_g1 = ub.getTauxReussiteCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						if(pourcentage_g1>=0){
							mGrp1TrimBean.setPourcentage_g1(pourcentage_g1);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
						mGrp1TrimBean.setAppreciation_g1(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursScientifiqueTrimestreBean.add(mGrp1TrimBean);
						
					}
					/****
						fin du for sur les cours scientifique qui passe dans la classe
					*****/
					
					//On place la liste des matieres scientifique construit
					bulletinTrim.setMatieresGroupe1Trimestre(listofCoursScientifiqueTrimestreBean);
					
					
					List<MatiereGroupe2TrimestreBean> listofCoursLitteraireTrimestreBean 
					= new ArrayList<MatiereGroupe2TrimestreBean>(); 
		
					int rc2 = 0;
					/***
					 * debut du for sur les cours Litteraire
					 * Gestion des cours Litteraire
					 */
					for(Cours cours : listofCoursLitteraire){
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe2TrimestreBean mGrp2TrimBean = new MatiereGroupe2TrimestreBean();
						
						
						
						RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
								classeConcerne, cours, trimestreConcerne);
						
						String matiere = ub.subString(cours.getIntituleCours(), 14);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 5);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
	
						
						mGrp2TrimBean.setMatiere_g2(matiere);
						mGrp2TrimBean.setMatiere_g2_2emelang(matiere_2emelang);
						mGrp2TrimBean.setProf_g2(nomProf);
						
						double soenoteTrim = 0;
						int nbreNoteDansTrimPourCours = 0;
						
	
						for(Sequences seq : trimestreConcerne.getListofsequence()){
							if(seq.getNumeroSeq()%2==1){
								double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								
								if(note_seq_g1>0){
									mGrp2TrimBean.setNote_1_g2(note_seq_g1);
									soenoteTrim = soenoteTrim + note_seq_g1;
									/*System.out.println(cours.getCodeCours()+" seq "+seq.getNumeroSeq()+" "+"note_seq_g1 = "+note_seq_g1+" "
											+ "et somme = "+soenoteTrim);*/
									nbreNoteDansTrimPourCours +=1; 
								}
							}
							else{
								double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
								
								if(note_seq_g2>0){
									mGrp2TrimBean.setNote_2_g2(note_seq_g2);
									
									soenoteTrim = soenoteTrim + note_seq_g2;
									/*System.out.println(cours.getCodeCours()+" seq "+seq.getNumeroSeq()+" note_seq_g2 = "+note_seq_g2+" "
											+ "et somme = "+soenoteTrim);*/
									nbreNoteDansTrimPourCours +=1; 
								}
							}
						}
						
						double noteCoursTrim = 0;
						if(nbreNoteDansTrimPourCours>0){
							noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
							
							mGrp2TrimBean.setNote_trim_g2(noteCoursTrim);
							double total_trim_g2 = noteCoursTrim*cours.getCoefCours();
							mGrp2TrimBean.setTotal_trim_g2(total_trim_g2);
							//System.out.println("Calculss "+cours.getCodeCours()+" "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g2);
						}
						
						mGrp2TrimBean.setCoef_g2(cours.getCoefCours());
						String extreme_g2 = "";
						double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
						double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
						
						if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
							extreme_g2 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
							mGrp2TrimBean.setExtreme_g2(extreme_g2);
						}
						
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc2 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc2>0){
							mGrp2TrimBean.setRang_g2(rc2+"e");
						}
						
						
						double moy_classe_g2 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						
						if(moy_classe_g2>0){
							mGrp2TrimBean.setMoy_classe_g2(moy_classe_g2);
						}
						
						
						double pourcentage_g2 = ub.getTauxReussiteCoursTrim(classeConcerne, 
								cours, trimestreConcerne);
						
						if(pourcentage_g2>=0){
							mGrp2TrimBean.setPourcentage_g2(pourcentage_g2);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
						mGrp2TrimBean.setAppreciation_g2(appreciationNote);
						
	
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursLitteraireTrimestreBean.add(mGrp2TrimBean);
						
						
					}
					/****
						fin du for sur les cours litteraire qui passe dans la classe
					 *****/
				
				//On place la liste des matieres scientifique construit
				bulletinTrim.setMatieresGroupe2Trimestre(listofCoursLitteraireTrimestreBean);
				
					
			
				
	
				List<MatiereGroupe3TrimestreBean> listofCoursDiversTrimestreBean 
				= new ArrayList<MatiereGroupe3TrimestreBean>(); 
	
				int rc3 = 0;
				/***
				 * debut du for sur les cours Divers
				 * Gestion des cours Divers
				 */
				for(Cours cours : listofCoursDivers){
					//long debutforCoursTime = System.currentTimeMillis();
					
					MatiereGroupe3TrimestreBean mGrp3TrimBean = new MatiereGroupe3TrimestreBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					String matiere = ub.subString(cours.getIntituleCours(), 14);
					matiere = matiere + ":";
					String codeMat = ub.subString(cours.getCodeCours(), 5);
					matiere = matiere + codeMat;
					
					String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 14);
					
					String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
					nomProf = ub.subString(nomProf, 15);
	
					
					mGrp3TrimBean.setMatiere_g3(matiere);
					mGrp3TrimBean.setMatiere_g3_2emelang(matiere_2emelang);
					mGrp3TrimBean.setProf_g3(nomProf);
					
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					
	
					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g1>0){
								mGrp3TrimBean.setNote_1_g3(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							
							if(note_seq_g2>0){
								mGrp3TrimBean.setNote_2_g3(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
					}
					
					double noteCoursTrim = 0;
					if(nbreNoteDansTrimPourCours>0){
						noteCoursTrim = soenoteTrim/nbreNoteDansTrimPourCours;
						
						mGrp3TrimBean.setNote_trim_g3(noteCoursTrim);
						double total_trim_g3 = noteCoursTrim*cours.getCoefCours();
						mGrp3TrimBean.setTotal_trim_g3(total_trim_g3);
						//System.out.println("Calculss "+noteCoursTrim+" * "+cours.getCoefCours()+" = "+total_trim_g3);
					}
					
					mGrp3TrimBean.setCoef_g3(cours.getCoefCours());
					String extreme_g3 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					
					if(noteTrimDernierCours>=0 && noteTrimPremierCours>0){
						extreme_g3 = "["+noteTrimDernierCours+" ; "+ noteTrimPremierCours+"]";
						mGrp3TrimBean.setExtreme_g3(extreme_g3);
					}
					
					
					List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
					
					rc3 = ub.getRangNoteTrimestrielElevePourCours_opt(eleve, 
							listofEleveOrdreDecroissantPourCours);
					
					if(rc3>0){
						mGrp3TrimBean.setRang_g3(rc3+"e");
					}
					
					
					double moy_classe_g3 = ub.getMoyenneGeneralCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					
					if(moy_classe_g3>0){
						mGrp3TrimBean.setMoy_classe_g3(moy_classe_g3);
					}
					
					
					double pourcentage_g3 = ub.getTauxReussiteCoursTrim(classeConcerne, 
							cours, trimestreConcerne);
					
					if(pourcentage_g3>=0){
						mGrp3TrimBean.setPourcentage_g3(pourcentage_g3);
					}
					
					String appreciationNote = ub.calculAppreciation(noteCoursTrim,lang);
					mGrp3TrimBean.setAppreciation_g3(appreciationNote);
					
	
					//On ajoute la ligne de cours dans le groupe correspondant
					listofCoursDiversTrimestreBean.add(mGrp3TrimBean);
					
					
				}
				/****
					fin du for sur les cours Divers qui passe dans la classe
				 *****/
			
			//On place la liste des matieres scientifique construit
			bulletinTrim.setMatieresGroupe3Trimestre(listofCoursDiversTrimestreBean);
			
			/*double moy_trimm = this.getMoyenneTrimestriel(eleve, trimestreConcerne);
			System.err.println("moy_trimm de "+eleve.getNomsEleves()+" est de "+moy_trimm);*/
					
					
					
					long finforTime = System.currentTimeMillis();
					collectionofBulletinTrim.add(bulletinTrim);
					System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
							" dans le trimestre "+trimestreConcerne.getNumeroTrim()+
							"  ajouter avec succes en "+(finforTime-startTimeFor));
					numBull++;
					
				}
			
			}

		return collectionofBulletinTrim;
	
	}

	@Override
	public Collection<BulletinSequenceBean> generate1BulletinSequence(Long idEleve, Long idClasse, Long idSequence) {
		// TODO Auto-generated method stub

		List<BulletinSequenceBean> collectionofBulletinSeq = new ArrayList<BulletinSequenceBean>();


		// long startTime = System.currentTimeMillis();
		 
		 Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Sequences sequenceConcerne = usersService.findSequences(idSequence);
		 Eleves eleveConcerne = usersService.findEleves(idEleve);
		 
		
		 if((classeConcerne==null) || (sequenceConcerne==null) || (eleveConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang="fr";
		 }
		 else{
			 lang="en";
		 }

	
			
			List<Cours> listofCoursEvalue = ub.getListOfCoursEvalueDansSequence(classeConcerne, 
					sequenceConcerne);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la 
			 * section général)
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas d'un seul élève et son identique pour tous les bulletins d'une classe 
			 * dans une séquence
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulier(classeConcerne, sequenceConcerne);
			
			RapportSequentielClasse rapportSequentielClasse = ub.getRapportSequentielClasse(classeConcerne, 
					listofEleveRegulier, sequenceConcerne);
			double moyenne_premier_classe = 0.0;
			double moyenne_dernier_classe = 0.0;
			int nbre_moyenne_classeSeq = 0;
			double tauxReussite = 0.0;
			double moyenne_general = 0.0;
			
			moyenne_premier_classe = rapportSequentielClasse.getValeurMoyennePremierDansSeq();
				
			moyenne_dernier_classe = rapportSequentielClasse.getValeurMoyenneDernierDansSeq();
				
			nbre_moyenne_classeSeq = rapportSequentielClasse.getNbreMoyennePourSeq();
				
			tauxReussite = rapportSequentielClasse.getTauxReussiteSequentiel();
				
			moyenne_general = rapportSequentielClasse.getMoyenneGeneralSequence();
			
			
			
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			/*****************************************************
			 * Il faut verifier si le proffesseur principal n'a pas encore ete specifie pour la classe
			 * Si c'est le cas alors il faut placer le vide devant car aucun prof principal n'existe
			 */
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseSeq = ub.geteffectifEleveRegulier(classeConcerne, sequenceConcerne);
			
			
			int numBull = 1;
			
			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans la séquence considérée. Pour que le trie des élèves ne soit pas fait pour chaque 
			 * élève dans le but de trouver son rang.
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantSequence(classeConcerne, sequenceConcerne);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu Séquentiellement. Pour que le trie ne soit pas fait sur chaque élève 
			 * traité dans le but de trouver son rang.
			 */
			List<Eleves> listofElevesOrdreDecroissantMoyenneSequentiel = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneSequentielOrdreDecroissant_static(classeConcerne, sequenceConcerne);

			for(Eleves eleve : listofElevesClasse){
				if(eleve.getIdEleves()==eleveConcerne.getIdEleves()){
					long startTimeFor = System.currentTimeMillis();
					
					BulletinSequenceBean bulletinSeq = new BulletinSequenceBean();
					/*
					 * Initialisons les premieres donnees du bulletin sequentiel
					 */
					/****
					 * Information d'entete du bulletin
					 */
					bulletinSeq.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
					bulletinSeq.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
					bulletinSeq.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
					bulletinSeq.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
					bulletinSeq.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
					bulletinSeq.setEtablissement_fr(etablissementConcerne.getNomsEtab());
					bulletinSeq.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
							etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
					bulletinSeq.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
					bulletinSeq.setDevise_fr(etablissementConcerne.getDeviseEtab());
					
					if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
						bulletinSeq.setTitre_bulletin("Bulletin de note de la séquence "+sequenceConcerne.getNumeroSeq());
					}
					else{
						bulletinSeq.setTitre_bulletin("Report card of sequence "+sequenceConcerne.getNumeroSeq());
					}
					bulletinSeq.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
					bulletinSeq.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
					
					/*****
					 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
					 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
					 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
					 * on va essayer de charger le fichier correspondant avec la classe File de java.io
					 */
					File f=new File(photoElevesDir+eleve.getIdEleves());
					//System.err.println("est ce que le fichier existe "+f.exists());
					
					if(f.exists()==true){
						bulletinSeq.setPhoto(photoElevesDir+eleve.getIdEleves()); 
					}
					
					
					/***************
					 * Information personnel de l'élève
					 */
					bulletinSeq.setNumero(" "+eleve.getNumero(listofElevesClasse));
					bulletinSeq.setSexe(eleve.getSexeEleves());
					bulletinSeq.setNom_eleve(" "+eleve.getNomsEleves().toUpperCase());
					bulletinSeq.setPrenom_eleve(eleve.getPrenomsEleves().toUpperCase());
					SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
					bulletinSeq.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
					bulletinSeq.setLieu_naissance_eleve(eleve.getLieunaissEleves());
					bulletinSeq.setMatricule_eleve(eleve.getMatriculeEleves());
					bulletinSeq.setRedoublant(eleve.getRedoublant());
					bulletinSeq.setClasse_eleve(classeString);
					bulletinSeq.setProf_principal(profPrincipal);
					bulletinSeq.setEffectif_classe(effectifTotalClasse);
					bulletinSeq.setEffectif_presente(effectifRegulierClasseSeq);
					
					/********
					 * Informations sur les labels d'entete des notes séquentiels
					 */
					bulletinSeq.setLabel_seq_x_coef("Note*Coef");
					bulletinSeq.setLabel_sequence("Note Seq"+sequenceConcerne.getNumeroSeq());
					
					/***********
					 * Information sur les totaux séquentiels
					 */
					
					double total_coef = ub.getSommeCoefCoursComposeD(eleve, sequenceConcerne);
					double t_coef = 1.0*total_coef;
					bulletinSeq.setTotal_coef(t_coef);
					
					double total_points = ub.getTotalPointsSequentiel(eleve, sequenceConcerne);
					
					if(total_points>0){
						bulletinSeq.setTotal_points(total_points);
					}
					
					/***********
					 * Informations sur les resultats sequentiels de l'eleve
					 */
					bulletinSeq.setResult_tt_coef(total_coef);
					
					if(total_points>0){
						bulletinSeq.setResult_tt_points(total_points);
					}
					
					
					
					//Cette methode donne un rang a tout le monde meme ceux qui n'ont compose qu'un seul cours
					
					 int rang = ub.getRangSequentielEleveAuMoinsUneNote(eleve, 
							 listofElevesOrdreDecroissantMoyenneSequentiel);
					
					if(rang>0){
						bulletinSeq.setResult_rang_seq(rang+"e");
					}
					
					
					/***************************************************
					 * Informations sur le profil  de la classe dans la séquence
					 */
					if(moyenne_premier_classe>0){
						bulletinSeq.setMoy_premier(moyenne_premier_classe);
					}
					if(moyenne_dernier_classe>0){
						bulletinSeq.setMoy_dernier(moyenne_dernier_classe);
					}
					bulletinSeq.setNbre_moyennes(nbre_moyenne_classeSeq);
					if(tauxReussite>0){
						bulletinSeq.setTaux_reussite(tauxReussite);
					}
					if(moyenne_general>0){
						bulletinSeq.setMoy_gen_classe(moyenne_general);
					}
					
					
					/***********************
					 * Informations sur la conduite sequentiel de l'élève
					 */
					//List<RapportDAbsence> listofRabs = eleve.getListRapportDAbsenceSeq(idSequence);
					
					bulletinSeq.setAbsence_J(eleve.getNbreHeureAbsenceJustifie(idSequence));
					bulletinSeq.setAbsence_NJ(eleve.getNbreHeureAbsenceNonJustifie(idSequence));
					
					/*
					 * On doit prendre si elle existe les 03 sanctions ayant le niveau de sévérité
					 * le plus élevée parmi toutes les sanctions obtenus par l'élève pendant la période.
					 * 			 */
					bulletinSeq.setRapport_disc1("");
					bulletinSeq.setRapport_disc2("");
					bulletinSeq.setRapport_disc3("");
					List<RapportDisciplinaire> listofRDiscEleve = eleve.getListRapportDisciplinaireSeq_DESC(idSequence);
					
					if(listofRDiscEleve != null){
						if(listofRDiscEleve.size()>0) {
							RapportDisciplinaire rdisc = listofRDiscEleve.get(0);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							//On peut donc fixer rapport_disc1
							bulletinSeq.setRapport_disc1(rdisc_chaine);
						}
						
						/*
						 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
						 * on va set rapport_disc2
						 */
						if(listofRDiscEleve.size()>1) {
	
							RapportDisciplinaire rdisc = listofRDiscEleve.get(1);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							//On peut donc fixer rapport_disc1
							bulletinSeq.setRapport_disc2(rdisc_chaine);
						
						}
						
						if(listofRDiscEleve.size()>2) {
	
							RapportDisciplinaire rdisc = listofRDiscEleve.get(2);
							String rdisc_chaine = "";
							rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
							
							//On peut donc fixer rapport_disc1
							bulletinSeq.setRapport_disc3(rdisc_chaine);
											
						}
						
					}
					
					/**************************
					 * Informations sur le rappel de la moyenne et du rang sequentiel
					 */
					if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
						bulletinSeq.setRappel_1("Séquence "+sequenceConcerne.getNumeroSeq());
					}
					else{
						bulletinSeq.setRappel_1("Sequence "+sequenceConcerne.getNumeroSeq());
					}
					
					double moy_seq = ub.getMoyenneSequentiel(eleve, sequenceConcerne);
					
					if(moy_seq>0){
						bulletinSeq.setR_moy_1(moy_seq);
					}
					
					if(rang>0){
						bulletinSeq.setR_rang_1(rang+"e");
					}
					else{
						bulletinSeq.setR_rang_1("");
					}
					
					
					/****************************
					 * Informations sur l'appreciation du travail de l'élève
					 */
					/*
					 * A traduire en fonction de la langue de la classe
					 */
					bulletinSeq.setTableau_hon("");
					bulletinSeq.setTableau_enc("");
					bulletinSeq.setTableau_fel("");
					String appreciation = ub.calculAppreciation(moy_seq,lang);
					bulletinSeq.setAppreciation(appreciation);
					
					/*
					 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
					 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
					 */
					DecisionConseil decConseil = eleve.getDecisionConseilPeriode(sequenceConcerne.getIdPeriodes());
					bulletinSeq.setDistinction("");
					bulletinSeq.setDecision_conseil("");
					if(decConseil !=null){
						/*******************************
						 * Informations sur les distinctions octroyées  dans la séquence
						 */
						String distinction="";
						distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
						bulletinSeq.setDistinction(distinction);
						
						/*******************************
						 * Informations sur les decision du conseil de classe dans la séquence
						 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
						 * de classe.
						 */
						String decision="";
						decision += decConseil.getSanctionDiscDecisionConseilString(lang);
						/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
						decision+=distinction;*/
						bulletinSeq.setDecision_conseil(decision);
					}
					
					
					
					List<Cours> listofCoursEffortAFournir = ub.getListofCoursDansOrdreEffortAFournir(eleve, listofCoursEvalue, 
							sequenceConcerne);
					bulletinSeq.setEffort_matiere1("");
					bulletinSeq.setEffort_matiere2("");
					bulletinSeq.setEffort_matiere3("");
					if(listofCoursEffortAFournir.size()>0) {
						String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
						bulletinSeq.setEffort_matiere1(codeCours);
					}
					
					
					if(listofCoursEffortAFournir.size()>1) {
						String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
						bulletinSeq.setEffort_matiere2(codeCours);
					}
					
					if(listofCoursEffortAFournir.size()>2) {
						String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
						bulletinSeq.setEffort_matiere3(codeCours);
					}
					
					
					
					/*****************************
					 * Information sur l'espace VISA du bulletin
					 */
					bulletinSeq.setVille(villeEtab);
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres scientifique (Groupe1) dans la séquence
					 * cccccccccccccccccccccccccc
					 */
					
					LigneSequentielGroupeCours ligneSequentielGroupeCoursScientifique = 
							ub.getLigneSequentielGroupeCours(eleve, listofCoursScientifique, sequenceConcerne);
					
					/*
					 * A traduire en fonction de la langue de la classe
					 */
					if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
						bulletinSeq.setNom_g1("SCIENTIFIQUE");
					}
					else{
						bulletinSeq.setNom_g1("SCIENCES");
					}
					
					double total_coef_g1 = ligneSequentielGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
					
					
					bulletinSeq.setTotal_coef_g1(total_coef_g1);
					
					double total_g1 = ligneSequentielGroupeCoursScientifique.getTotalNoteSeqElevePourGroupeCours();
					
					if(total_g1>0){
						bulletinSeq.setTotal_g1(total_g1);
					}
					
					String totalextreme_g1 = "";
					
					double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansSeq(
							listofElevesClasse, listofCoursScientifique, sequenceConcerne);
					
					double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansSeq(
							listofElevesClasse, listofCoursScientifique, sequenceConcerne);
					
					if(valeurMoyDernierGrpCours1>=0 && valeurMoyPremierGrpCours1>0){
						totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
								valeurMoyPremierGrpCours1+"]";
					}
					bulletinSeq.setTotal_extreme_g1(totalextreme_g1);
					
					int r1 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursScientifique, 
							sequenceConcerne, eleve);
					
					if(r1>0){
						bulletinSeq.setTotal_rang_g1(r1+"e");
					}
					
					
					double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
							listofCoursScientifique, sequenceConcerne);
					
					if(moy_gen_grp1>0){
						bulletinSeq.setMg_classe_g1(moy_gen_grp1);
					}
					
					double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
							listofCoursScientifique, sequenceConcerne);
					
					if(total_pourcentage_g1>=0){
						bulletinSeq.setTotal_pourcentage_g1(total_pourcentage_g1);
					}
					
					double moyenne_g1 = ligneSequentielGroupeCoursScientifique.
							getMoyenneSeqElevePourGroupeCours();
					if(moyenne_g1>0){
						bulletinSeq.setMoyenne_g1(moyenne_g1);
					}
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres litteraire (Groupe2) dans la séquence
					 */
					
					
					
					LigneSequentielGroupeCours ligneSequentielGroupeCoursLitteraire = 
							ub.getLigneSequentielGroupeCours(eleve, listofCoursLitteraire, sequenceConcerne);
					
					if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
						bulletinSeq.setNom_g2("LITTERAIRES");
					}
					else{
						bulletinSeq.setNom_g2("ARTS");
					}
					
					double total_coef_g2 = ligneSequentielGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
					
					bulletinSeq.setTotal_coef_g2(total_coef_g2);
					
					double total_g2 = ligneSequentielGroupeCoursLitteraire.getTotalNoteSeqElevePourGroupeCours();
					
					if(total_g2>0){
						bulletinSeq.setTotal_g2(total_g2);
					}
					
					String totalextreme_g2 = "";
					
					double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansSeq(
							listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
					
					double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansSeq(
							listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
					
					if(valeurMoyDernierGrpCours2>=0 && valeurMoyPremierGrpCours2>0){
						totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
								valeurMoyPremierGrpCours2+"]";
					}
					bulletinSeq.setTotal_extreme_g2(totalextreme_g2);
					
					
					int r2 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursLitteraire, 
							sequenceConcerne, eleve);
					if(r2>0){
						bulletinSeq.setTotal_rang_g2(r2+"e");
					}	
					
					double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
							listofCoursLitteraire, sequenceConcerne);
					
					if(moy_gen_grp2>0){
						bulletinSeq.setMg_classe_g2(moy_gen_grp2);
					}
					
					
					double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
							listofCoursLitteraire, sequenceConcerne);
					
					
					
					if(total_pourcentage_g2>=0){
						bulletinSeq.setTotal_pourcentage_g2(total_pourcentage_g2);
					}
					
					double moyenne_g2 = ligneSequentielGroupeCoursLitteraire.
							getMoyenneSeqElevePourGroupeCours();
					
					
					if(moyenne_g2>0){
						bulletinSeq.setMoyenne_g2(moyenne_g2);
					}
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Divers (Groupe3) dans la séquence
					 */
									
					LigneSequentielGroupeCours ligneSequentielGroupeCoursDivers = 
							ub.getLigneSequentielGroupeCours(eleve, listofCoursDivers, sequenceConcerne);
					
					if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
						bulletinSeq.setNom_g3("DIVERS");
					}
					else{
						bulletinSeq.setNom_g3("OTHERS");
					}
					
					double total_coef_g3 = ligneSequentielGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
					
					bulletinSeq.setTotal_coef_g3(total_coef_g3);
					
					double total_g3 = ligneSequentielGroupeCoursDivers.getTotalNoteSeqElevePourGroupeCours();
					
					if(total_g3>0){
						bulletinSeq.setTotal_g3(total_g3);
					}
					
					String totalextreme_g3 = "";
					
					double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansSeq(
							listofElevesClasse, listofCoursLitteraire, sequenceConcerne);
					
					double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansSeq(
							listofElevesClasse, listofCoursDivers, sequenceConcerne);
					
					if(valeurMoyDernierGrpCours3>=0 && valeurMoyPremierGrpCours3>0){
						totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
								valeurMoyPremierGrpCours3+"]";
					}
					bulletinSeq.setTotal_extreme_g3(totalextreme_g3);
					
					
					int r3 = ub.getRangMoyenneSeqElevePourGroupe(classeConcerne, listofCoursDivers, 
							sequenceConcerne, eleve);
					if(r3>0){
						bulletinSeq.setTotal_rang_g3(r3+"e");
					}
					
					
					double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCours(classeConcerne, 
							listofCoursDivers, sequenceConcerne);
					
					if(moy_gen_grp3>0){
						bulletinSeq.setMg_classe_g3(moy_gen_grp3);
					}
					
					
					double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCours(classeConcerne, 
							listofCoursDivers, sequenceConcerne);
					
					if(total_pourcentage_g3>=0){
						bulletinSeq.setTotal_pourcentage_g3(total_pourcentage_g3);
					}
					
					double moyenne_g3 = ligneSequentielGroupeCoursDivers.
							getMoyenneSeqElevePourGroupeCours();
					
					if(moyenne_g3>0){
						bulletinSeq.setMoyenne_g3(moyenne_g3);
					}
					
					t_coef = total_coef_g1+total_coef_g2+total_coef_g3;
					
					bulletinSeq.setTotal_coef(t_coef);
					
					/************************************
					 * Listes alimentant les sous rapport: les rapports sur les groupes des matières 
					 **********/
					
					
					List<MatiereGroupe1SequenceBean> listofCoursScientifiqueSequenceBean 
								= new ArrayList<MatiereGroupe1SequenceBean>(); 
					
					int rc1 = 0;
					//Gestion des cours scientifique
					for(Cours cours : listofCoursScientifique){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe1SequenceBean mGrp1SeqBean = new MatiereGroupe1SequenceBean();
							
						RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
								classeConcerne, cours, sequenceConcerne);
						
						String matiere = ub.subString(cours.getIntituleCours(), 25);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 8);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 20);
						
						mGrp1SeqBean.setMatiere_g1(matiere);
						mGrp1SeqBean.setMatiere_g1_2emelang(matiere_2emelang);
						mGrp1SeqBean.setProf_g1(nomProf);
						
						double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
						
						if(note_seq_g1>0){
							mGrp1SeqBean.setNote_seq_g1(note_seq_g1);
						}
						double total_seq_g1 = note_seq_g1*cours.getCoefCours();
						if(total_seq_g1>0){
							mGrp1SeqBean.setTotal_seq_g1(total_seq_g1);
						}
						mGrp1SeqBean.setCoef_g1(cours.getCoefCours());
						String extreme_g1 = "";
						double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
						double notePremierCours = rapportSequentielCours.getValeurNotePremier();
						
						if(noteDernierCours>=0 && notePremierCours>0){
							extreme_g1 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
							mGrp1SeqBean.setExtreme_g1(extreme_g1);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc1 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						if(rc1>0){
							mGrp1SeqBean.setRang_g1(rc1+"e");
						}
						
						
						double moy_classe_g1 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
								sequenceConcerne);
						
						if(moy_classe_g1>0){
							mGrp1SeqBean.setMoy_classe_g1(moy_classe_g1);
						}
						
						
						double pourcentage_g1 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
						
						if(pourcentage_g1>=0){
							mGrp1SeqBean.setPourcentage_g1(pourcentage_g1+" %");
						}
						
						String appreciationNote = ub.calculAppreciation(note_seq_g1,lang);
						mGrp1SeqBean.setAppreciation_g1(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursScientifiqueSequenceBean.add(mGrp1SeqBean);
						
					}//fin du for sur les cours scientifique qui passe dans la classe
					
					//On place la liste des matieres scientifique construit
					bulletinSeq.setMatieresGroupe1Sequence(listofCoursScientifiqueSequenceBean);
					
				
					List<MatiereGroupe2SequenceBean> listofCoursLitteraireSequenceBean 
								= new ArrayList<MatiereGroupe2SequenceBean>();
					
					
					//Gestion des cours litteraire
					
					
					int rc2 = 0;
					//Gestion des cours litteraire
					for(Cours cours : listofCoursLitteraire){
			
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe2SequenceBean mGrp2SeqBean = new MatiereGroupe2SequenceBean();
			
			
						
						RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
								classeConcerne, cours, sequenceConcerne);
			
						
						String matiere = ub.subString(cours.getIntituleCours(), 25);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 8);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 20);
						
						mGrp2SeqBean.setMatiere_g2(matiere);
						mGrp2SeqBean.setMatiere_g2_2emelang(matiere_2emelang);
						mGrp2SeqBean.setProf_g2(nomProf);
						
			
						
						double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
						
						if(note_seq_g2>0){
							mGrp2SeqBean.setNote_seq_g2(note_seq_g2);
						}
						double total_seq_g2 = note_seq_g2*cours.getCoefCours();
						if(total_seq_g2>0){
							mGrp2SeqBean.setTotal_seq_g2(total_seq_g2);
						}
						
						mGrp2SeqBean.setCoef_g2(cours.getCoefCours());
						String extreme_g2 = "";
						double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
						double notePremierCours = rapportSequentielCours.getValeurNotePremier();
						
						if(noteDernierCours>=0 && notePremierCours>0){
							extreme_g2 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
							mGrp2SeqBean.setExtreme_g2(extreme_g2);
						}
			
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc2 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc2>0){
							mGrp2SeqBean.setRang_g2(rc2+"e");
						}
			
						
						double moy_classe_g2 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
								sequenceConcerne);
						
						if(moy_classe_g2>0){
							mGrp2SeqBean.setMoy_classe_g2(moy_classe_g2);
						}
			
						
						double pourcentage_g2 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
						
						if(pourcentage_g2>=0){
							mGrp2SeqBean.setPourcentage_g2(pourcentage_g2+" %");
						}
			
						String appreciationNote = ub.calculAppreciation(note_seq_g2,lang);
						mGrp2SeqBean.setAppreciation_g2(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursLitteraireSequenceBean.add(mGrp2SeqBean);
			
					}//fin du for sur les cours litteraire qui passe dans la classe			
					
					
					//On place la liste des matieres litteraire construit
					bulletinSeq.setMatieresGroupe2Sequence(listofCoursLitteraireSequenceBean);
					
				
					List<MatiereGroupe3SequenceBean> listofCoursDiversSequenceBean 
								= new ArrayList<MatiereGroupe3SequenceBean>();//Construire a partir de listofCoursDivers
			
					
					//Gestion des cours Divers
					
					int rc3 = 0;
					for(Cours cours : listofCoursDivers){
			
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe3SequenceBean mGrp3SeqBean = new MatiereGroupe3SequenceBean();
			
			
						RapportSequentielCours rapportSequentielCours = ub.getRapportSequentielCours(
								classeConcerne, cours, sequenceConcerne);
			
						String matiere = ub.subString(cours.getIntituleCours(), 25);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 8);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 25);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 20);
						
						mGrp3SeqBean.setMatiere_g3(matiere);
						mGrp3SeqBean.setMatiere_g3_2emelang(matiere_2emelang);
						mGrp3SeqBean.setProf_g3(nomProf);
			
						
						
						double note_seq_g3 = ub.getValeurNotesFinaleEleve(eleve, cours, sequenceConcerne);
						
						if(note_seq_g3>0){
							mGrp3SeqBean.setNote_seq_g3(note_seq_g3);
						}
						double total_seq_g3 = note_seq_g3*cours.getCoefCours();
						if(total_seq_g3>0){
							mGrp3SeqBean.setTotal_seq_g3(total_seq_g3);
						}
						
						mGrp3SeqBean.setCoef_g3(cours.getCoefCours());
						String extreme_g3 = "";
						double noteDernierCours = rapportSequentielCours.getValeurNoteDernier();
						double notePremierCours = rapportSequentielCours.getValeurNotePremier();
						
						if(noteDernierCours>=0 && notePremierCours>0){
							extreme_g3 = "["+noteDernierCours+" ; "+ notePremierCours+"]";
							mGrp3SeqBean.setExtreme_g3(extreme_g3);
						}
			
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc3 = ub.getRangNoteSequentielElevePourCours_opt(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc3>0){
							mGrp3SeqBean.setRang_g3(rc3+"e");
						}
			
						
						double moy_classe_g3 = ub.getMoyenneGeneralCoursSeq(classeConcerne, cours, 
								sequenceConcerne);
						
						if(moy_classe_g3>0){
							mGrp3SeqBean.setMoy_classe_g3(moy_classe_g3);
						}
			
						
						double pourcentage_g3 = ub.getTauxReussiteCoursSeq(classeConcerne, cours, sequenceConcerne);
						
						if(pourcentage_g3>=0){
							mGrp3SeqBean.setPourcentage_g3(pourcentage_g3+" %");
						}
			
						String appreciationNote = ub.calculAppreciation(note_seq_g3,lang);
						mGrp3SeqBean.setAppreciation_g3(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursDiversSequenceBean.add(mGrp3SeqBean);
			
					}//fin du for sur les cours Divers qui passe dans la classe	
					
					
					
					//On place la liste des matieres divers construit
					bulletinSeq.setMatieresGroupe3Sequence(listofCoursDiversSequenceBean);
					
					
					long finforTime = System.currentTimeMillis();
					
					collectionofBulletinSeq.add(bulletinSeq);
					
					System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
							" de la sequence "+sequenceConcerne.getNumeroSeq()+
							"  ajouter avec succes en "+(finforTime-startTimeFor));
					
					numBull++;
				}
			}
		
		 
		 
		
		return collectionofBulletinSeq;
	
	}

	@Override
	public Collection<BulletinAnnuelBean> generate1BulletinAnnuel(Long idEleve, Long idClasse, Long idAnnee) {


		Etablissement etablissementConcerne = usersService.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 
		 Classes classeConcerne = usersService.findClasses(idClasse);
		 Annee anneeScolaire = usersService.findAnneeActive();
		 Eleves eleveConcerne = usersService.findEleves(idEleve);
			
		 List<BulletinAnnuelBean> collectionofBulletionAnnuel = new ArrayList<BulletinAnnuelBean>();
		
		 if((classeConcerne==null) || (anneeScolaire==null) || (eleveConcerne==null)) {
			//System.err.println("les données de calcul du bean bulletin sont errone donc rien n'est possible ");
			return null;
		 }
		 
		 String lang="";
		 if(classeConcerne.getLangueClasses().equalsIgnoreCase("fr")==true){
			 lang ="fr";
		 }
		 else{
			 lang="en";
		 }
		 
		 /*
			 * Ici on est sur que la classe est bel et bien retrouver. On doit faire le bulletin de tous les élèves de la classe.
			 * mais si un élève n'est pas régulier son bulletin devient particulier
			 */
			List<Eleves>  listofEleveClasse = (List<Eleves>) classeConcerne.getListofEleves();
			
			List<Cours> listofCoursEvalueAn = ub.getListOfCoursEvalueDansAnnee(classeConcerne, 
					anneeScolaire);
			
			/*
			 * Etablissons ensuite la liste des 3 groupes de cours(scientifique, littéraire et divers dans la section général)
			 * Cette liste de cours doit etre extraite des cours evalue
			 */
			
			List<Cours> listofCoursScientifique = ub.getListofCoursScientifiqueDansClasse(classeConcerne);
			
			List<Cours> listofCoursLitteraire = ub.getListofCoursLitteraireDansClasse(classeConcerne);
			
			List<Cours> listofCoursDivers = ub.getListofCoursDiversDansClasse(classeConcerne);
			
			/*
			 * Donnée du bulletin qui ne doivent pas être recalcule à chaque tour de boucle sur les élèves
			 * car elles ne dépendent pas de l'élève et son identique pour tous les bulletins d'une classe 
			 * dans une année
			 */
			List<Eleves> listofElevesClasse = (List<Eleves>) classeConcerne.getListofEleves();
			

			List<Eleves> listofEleveRegulier = ub.getListofEleveRegulierAnnee(classeConcerne, anneeScolaire);
			

			RapportAnnuelClasse rapportAnnuelClasse = ub.getRapportAnnuelClasse(classeConcerne, 
					listofEleveRegulier, anneeScolaire);
			
			
			double moyenne_premier_classe = rapportAnnuelClasse.getValeurMoyennePremierDansAn();
			
			double moyenne_dernier_classe = rapportAnnuelClasse.getValeurMoyenneDernierDansAn();
			
			int nbre_moyenne_classeSeq = rapportAnnuelClasse.getNbreMoyennePourAn();
			
			double tauxReussite = rapportAnnuelClasse.getTauxReussiteAnnuel();
			
			double moyenne_general = rapportAnnuelClasse.getMoyenneGeneralAnnuel();
			String classeString = classeConcerne.getCodeClasses()+
					classeConcerne.getSpecialite().getCodeSpecialite()+classeConcerne.getNumeroClasses();
			
			String profPrincipal ="";
			if(classeConcerne.getProffesseur()!=null){
				profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			}
			
			
			int effectifTotalClasse =	ub.geteffectifEleve(classeConcerne);
			
			int effectifRegulierClasseAn = ub.geteffectifEleveRegulierAnnee(classeConcerne, anneeScolaire);
			
			
			int numBull = 1;

			/*
			 * On va appeler une méthode qui retourne une Map<idCours, List<Eleves>>
			 * Chaque entrée de la Map a comme cle l'id d'un cours passant dans la classe et 
			 * comme valeur la liste des élèves rangés dans l'ordre décroissant des notes obtenues 
			 * dans l'année considéré
			 */
			Map<Long, List<Eleves>> mapCoursEleves = 
					ub.getMapCoursElevesOrdreDecroissantAnnee(classeConcerne, anneeScolaire);
			
			/*
			 * On va appeler une méthode qui retourne la liste des élèves classés dans l'ordre décroissant 
			 * des moyenne obtenu annuellement
			 */
			List<Eleves> listofElevesOrdreDecroissantAnnee = (List<Eleves>) 
					UtilitairesBulletins.getMoyenneAnnuelOrdreDecroissant1(classeConcerne, anneeScolaire);
			

			/*
			 * On va mettre dans cette Map la liste des élèves classés dans l'ordre décroissant des 
			 * moyennes obtenu pour chaque trimestre de l'année
			 */
			Map<Long,List<Eleves>> mapofElevesOrdreDecroissantMoyenneTrimestriel = new 
					HashMap<Long, List<Eleves>>();
			
			for(Trimestres trim : anneeScolaire.getListoftrimestre()){
				
				List<Eleves> listofElevesOrdreDecroissantMoyenneTrim = (List<Eleves>) 
						UtilitairesBulletins.getMoyenneTrimestrielOrdreDecroissant1(classeConcerne, trim);
				
				mapofElevesOrdreDecroissantMoyenneTrimestriel.put(trim.getIdPeriodes(),
						listofElevesOrdreDecroissantMoyenneTrim);
				
			}
			
			
			
			for(Eleves eleve : listofEleveClasse){
				if(eleve.getIdEleves() == eleveConcerne.getIdEleves()){
					long startTimeFor = System.currentTimeMillis();
					
					BulletinAnnuelBean bulletinAn = new BulletinAnnuelBean();
					/*
					 * Initialisons les premieres donnees du bulletin annuel
					 */
					/****
					 * Information d'entete du bulletin
					 */
					
					bulletinAn.setMinistere_fr(etablissementConcerne.getMinisteretuteleEtab());
					bulletinAn.setMinistere_en(etablissementConcerne.getMinisteretuteleanglaisEtab());
					bulletinAn.setDelegation_en(etablissementConcerne.getDeleguationdeptuteleanglaisEtab());
					bulletinAn.setDelegation_fr(etablissementConcerne.getDeleguationdeptuteleEtab());
					bulletinAn.setEtablissement_en(etablissementConcerne.getNomsanglaisEtab());
					bulletinAn.setEtablissement_fr(etablissementConcerne.getNomsEtab());
					bulletinAn.setAdresse("BP "+etablissementConcerne.getBpEtab()+"/"+
							etablissementConcerne.getNumtel1Etab()+"/"+etablissementConcerne.getEmailEtab());
					bulletinAn.setDevise_en(etablissementConcerne.getDeviseanglaisEtab());
					bulletinAn.setDevise_fr(etablissementConcerne.getDeviseEtab());
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setTitre_bulletin("Bulletin de note de l'année scolaire "+
								anneeScolaire.getIntituleAnnee());
					}
					else{
						bulletinAn.setTitre_bulletin("Report card of school year "+anneeScolaire.getIntituleAnnee());
					}
					bulletinAn.setAnnee_scolaire_en("School year "+anneeScolaire.getIntituleAnnee());
					bulletinAn.setAnnee_scolaire_fr("Année scolaire "+anneeScolaire.getIntituleAnnee());
					
					
					/***************
					 * Information personnel de l'élève
					 */
					bulletinAn.setNumero(" "+eleve.getNumero(listofElevesClasse));
					bulletinAn.setSexe(eleve.getSexeEleves());
					bulletinAn.setNom_eleve(eleve.getNomsEleves());
					bulletinAn.setPrenom_eleve(eleve.getPrenomsEleves());
					SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
					bulletinAn.setDate_naissance_eleve(spd.format(eleve.getDatenaissEleves()));
					bulletinAn.setLieu_naissance_eleve(eleve.getLieunaissEleves());
					bulletinAn.setMatricule_eleve(eleve.getMatriculeEleves());
					bulletinAn.setRedoublant(eleve.getRedoublant());
					bulletinAn.setClasse_eleve(classeString);
					bulletinAn.setProf_principal(profPrincipal);
					bulletinAn.setEffectif_classe(effectifTotalClasse);
					bulletinAn.setEffectif_presente(effectifRegulierClasseAn);
					
					/*****
					 * Pour le chargement des photos: Si une photos n'est pas dispo il y aura une exception 
					 * puisque Jasper ne pourra pas trouver l'image. Donc il faut un moyen de ne rien fixé 
					 * à setPhoto lorsque l'image n'est pas dispo. Pour vérifier que l'image n'est pas dispo
					 * on va essayer de charger le fichier correspondant avec la classe File de java.io
					 */
					File f=new File(photoElevesDir+eleve.getIdEleves());
					//System.err.println("est ce que le fichier existe "+f.exists());
					
					if(f.exists()==true){
						bulletinAn.setPhoto(photoElevesDir+eleve.getIdEleves()); 
					}
					
					
					/********
					 * Informations sur les labels d'entete des notes du bulletin annuel
					 */
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						if(trim.getNumeroTrim()==1){
							bulletinAn.setLabel_note_1("T"+trim.getNumeroTrim());
						}
						else if(trim.getNumeroTrim()==2){
							bulletinAn.setLabel_note_2("T"+trim.getNumeroTrim());
						}
						else if(trim.getNumeroTrim()==3){
							bulletinAn.setLabel_note_3("T"+trim.getNumeroTrim());
						}
					}
					bulletinAn.setLabel_annuel("N An");
					bulletinAn.setLabel_ann_x_coef("N An"+"*Coef");
	
					/***********
					 * Information sur les totaux annuels
					 */
					
					double total_coef = ub.getSommeCoefCoursComposeAnnee(eleve, anneeScolaire);
					bulletinAn.setTotal_coef(total_coef);
					
					double total_points = ub.getTotalPointsAnnuel(eleve, anneeScolaire);
					if(total_points>0){
						bulletinAn.setTotal_points(total_points);
					}
					
					/***********
					 * Informations sur les resultats annuels de l'eleve
					 */
					bulletinAn.setResult_tt_coef(total_coef);
					
					if(total_points>0){
						bulletinAn.setResult_tt_points(total_points);
					}
					
					
				
					int rang = ub.getRangAnnuelEleveAuMoinsUneNote(eleve,listofElevesOrdreDecroissantAnnee);
					if(rang>0){
						bulletinAn.setResult_rang_ann(rang+"e");
						bulletinAn.setR_rang_an(rang+"e");
					}
					else{
						bulletinAn.setResult_rang_ann("");
						bulletinAn.setR_rang_an("");
					}
	
	
					
					/**********************
					 * Informations sur le profil de la classe dans l'année
					 */
					if(moyenne_premier_classe>0){
						bulletinAn.setMoy_premier(moyenne_premier_classe);
					}
					if(moyenne_dernier_classe>0){
						bulletinAn.setMoy_dernier(moyenne_dernier_classe);
					}
					bulletinAn.setNbre_moyennes(nbre_moyenne_classeSeq);
					if(tauxReussite>0){
						bulletinAn.setTaux_reussite(tauxReussite);
					}
					if(moyenne_general>0){
						bulletinAn.setMoy_gen_classe(moyenne_general);
					}
	
					/***********************
					 * Informations sur la conduite annuel de l'élève
					 */
					int nhaj = 0;
					int nhanj = 0;
					int nhc = 0;
					int nje = 0;
					
					
					nhanj = eleve.getNbreHeureAbsenceNonJustifieAnnee(anneeScolaire);
					nhaj = eleve.getNbreHeureAbsenceJustifieAnnee(anneeScolaire);
					
					bulletinAn.setAbsence_NJ(nhanj);
					bulletinAn.setAbsence_J(nhaj);
					bulletinAn.setConsigne(nhc+"h");
					bulletinAn.setExclusion(nje+" J");
					bulletinAn.setAvertissement("");
					bulletinAn.setBlame_conduite("");
					
					/************************
					 * On doit rechercher les sanctions disciplinaire obtenu dans la periode(annee)
					 * dans leur ordre decroissant de sévérité et dans l'ordre décroissant des dates ou elles ont 
					 * ete infligées. On va commencer du trimestre de plus grand numero vers celui de plus petit et
					 *  de la séquence paire vers la séquence impair de chaque trimestre à chercher
					 */
					bulletinAn.setRapport_disc1("");
					bulletinAn.setRapport_disc2("");
					bulletinAn.setRapport_disc3("");
					for(Trimestres trim : anneeScolaire.getListoftrimestre_DESC()){
							for(Sequences seq : trim.getListofsequence_DESC()){
								List<RapportDisciplinaire> listofRDiscEleveSeq = eleve.getListRapportDisciplinaireSeq_DESC(seq.getIdPeriodes());
								
								if(listofRDiscEleveSeq != null){
									if(listofRDiscEleveSeq.size()>0) {
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(0);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										//On peut donc fixer rapport_disc1
										bulletinAn.setRapport_disc1(rdisc_chaine);
									}
									
									/*
									 * On ne fait pas de else car il faut encore reprendre le test et au cas ou ca marche 
									 * on va set rapport_disc2
									 */
									if(listofRDiscEleveSeq.size()>1) {
			
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(1);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										//On peut donc fixer rapport_disc1
										bulletinAn.setRapport_disc2(rdisc_chaine);
									
									}
									
									if(listofRDiscEleveSeq.size()>2) {
			
										RapportDisciplinaire rdisc = listofRDiscEleveSeq.get(2);
										String rdisc_chaine = "";
										rdisc_chaine = rdisc.getRapportDisciplinaireString(lang);
										
										//On peut donc fixer rapport_disc1
										bulletinAn.setRapport_disc3(rdisc_chaine);
														
									}
									
							}
						}
					}
					
					
					/**************************
					 * Informations sur le rappel de la moyenne et du rang des autres trimestres
					 */
					
					for(Trimestres trim : anneeScolaire.getListoftrimestre()){
						
						List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel = 
								mapofElevesOrdreDecroissantMoyenneTrimestriel.get(trim.getIdPeriodes());
						
						if(trim.getNumeroTrim() == 1){
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinAn.setRappel_1("Trimestre "+trim.getNumeroTrim());
							}
							else{
								bulletinAn.setRappel_1("Term "+trim.getNumeroTrim());
							}
							
							double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
							if(moy_trim>0){
								bulletinAn.setR_moy_1(moy_trim);
							}
							
							int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneTrimestriel);
							
							if(rangtrim>0){
								bulletinAn.setR_rang_1(rangtrim+"e");
							}
							else{
								bulletinAn.setR_rang_1("");
							}
						}//trim1
						else if(trim.getNumeroTrim() == 2){
							if(lang.equalsIgnoreCase("fr")==true){
							bulletinAn.setRappel_2("Trimestre"+trim.getNumeroTrim());
							}
							else{
								bulletinAn.setRappel_2("Term "+trim.getNumeroTrim());
							}
							double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
							if(moy_trim>0){
								bulletinAn.setR_moy_2(moy_trim);
							}
							
							int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneTrimestriel);
							
							if(rangtrim>0){
								bulletinAn.setR_rang_2(rangtrim+"e");
							}
							else{
								bulletinAn.setR_rang_2("");
							}
						}//fin trim2
						else if(trim.getNumeroTrim() == 3){
							if(lang.equalsIgnoreCase("fr")==true){
								bulletinAn.setRappel_3("Trimestre"+trim.getNumeroTrim());
							}
							else{
								bulletinAn.setRappel_3("Term "+trim.getNumeroTrim());
							}
							double moy_trim = ub.getMoyenneTrimestriel(eleve, trim);
							if(moy_trim>0){
								bulletinAn.setR_moy_3(moy_trim);
							}
							
							int rangtrim = ub.getRangTrimestrielEleveAuMoinsUneNote(eleve, 
									listofElevesOrdreDecroissantMoyenneTrimestriel);
							
							if(rangtrim>0){
								bulletinAn.setR_rang_3(rangtrim+"e");
							}
							else{
								bulletinAn.setR_rang_3("");
							}
						}//fin trim3
						
					}//fin du for sur les trim
					
					/****************************
					 * Informations sur l'appreciation du travail de l'élève
					 */
					double moy_an = ub.getMoyenneAnnuel(eleve, anneeScolaire);
					
					if(moy_an>=0) bulletinAn.setR_moy_an(moy_an);
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setRappel_4("Année");
					}
					else{
						bulletinAn.setRappel_4("Year ");
					}
					
					bulletinAn.setTableau_hon("");
					bulletinAn.setTableau_enc("");
					bulletinAn.setTableau_fel("");
					String appreciation = ub.calculAppreciation(moy_an,lang);
					bulletinAn.setAppreciation(appreciation);
					
					/*
					 * On doit chercher la decision de conseil dans la periode sachant qu'on a une seule decision de 
					 * conseil dans une période donnée (que ce soit séquence, trimestre ou année)
					 */
					DecisionConseil decConseil = eleve.getDecisionConseilPeriode(anneeScolaire.getIdPeriodes());
					bulletinAn.setDistinction("");
					bulletinAn.setDecision_conseil("");
					if(decConseil !=null){
						/*******************************
						 * Informations sur les distinctions octroyées  dans la séquence
						 */
						String distinction="";
						distinction = decConseil.getSanctionTravDecisionConseilStringIntitule(lang);
						bulletinAn.setDistinction(distinction);
						
						/*******************************
						 * Informations sur les decision du conseil de classe dans la séquence
						 * en fait il s'agit de préciser les sanctions disciplinaire infligées à un élève lors du conseil
						 * de classe. Et ici c'est le conseil de classe annuel donc la decision finale
						 */
						String decision="";
						decision += decConseil.getDecisionDecisionConseilString(lang);
						/*distinction = decConseil.getSanctionTravDecisionConseilString(lang);
						decision+=distinction;*/
						bulletinAn.setDecision_conseil(decision);
					}
					
					
					
					
					List<Cours> listofCoursEffortAFournir = 
							ub.getListofCoursDansOrdreEffortAFournirAnnee(eleve, listofCoursEvalueAn, 
							anneeScolaire);
					bulletinAn.setEffort_matiere1("");
					bulletinAn.setEffort_matiere2("");
					bulletinAn.setEffort_matiere3("");
					if(listofCoursEffortAFournir.size()>0) {
						String codeCours = listofCoursEffortAFournir.get(0).getCodeCours();
						bulletinAn.setEffort_matiere1(codeCours);
					}
					
					
					if(listofCoursEffortAFournir.size()>1) {
						String codeCours = listofCoursEffortAFournir.get(1).getCodeCours();
						bulletinAn.setEffort_matiere2(codeCours);
					}
					
					if(listofCoursEffortAFournir.size()>2) {
						String codeCours = listofCoursEffortAFournir.get(2).getCodeCours();
						bulletinAn.setEffort_matiere3(codeCours);
					}
					
	
					/*****************************
					 * Information sur l'espace VISA du bulletin
					 */
					bulletinAn.setVille(villeEtab);
					
					
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres scientifique (Groupe1) dans l'année
					 * cccccccccccccccccccccccccc
					 */
					
					LigneAnnuelGroupeCours ligneAnnuelGroupeCoursScientifique = 
							ub.getLigneAnnuelGroupeCours(eleve, listofCoursScientifique, anneeScolaire);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setNom_g1("Scientifique");
					}
					else{
						bulletinAn.setNom_g1("Scientific");
					}
					
					double total_coef_g1 = ligneAnnuelGroupeCoursScientifique.getTotalCoefElevePourGroupeCours();
					
					bulletinAn.setTotal_coef_g1(total_coef_g1);
					//System.err.println("total_coef_g1 == "+total_coef_g1);
					
					double total_g1 = ligneAnnuelGroupeCoursScientifique.getTotalNoteAnElevePourGroupeCours();
					if(total_g1>0){
						bulletinAn.setTotal_g1(total_g1);
					}
					
					String totalextreme_g1 = "";
					
					double valeurMoyDernierGrpCours1 = ub.getValeurMoyenneDernierPourGrpDansAn(
							listofElevesClasse, listofCoursScientifique, anneeScolaire);
					
					double valeurMoyPremierGrpCours1 = ub.getValeurMoyennePremierPourGrpDansAn(
							listofElevesClasse, listofCoursScientifique, anneeScolaire);
					if(valeurMoyDernierGrpCours1>0 && valeurMoyPremierGrpCours1>0){
						totalextreme_g1 = "["+valeurMoyDernierGrpCours1+" ; "+
								valeurMoyPremierGrpCours1+"]";
					}
					bulletinAn.setTotal_extreme_g1(totalextreme_g1);
					
					int r1 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursScientifique, 
							anneeScolaire, eleve);
					
					if(r1>0){
						bulletinAn.setTotal_rang_g1(r1+"e");
					}
					else{
						bulletinAn.setTotal_rang_g1("");
					}
					
					
					double moy_gen_grp1 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
							listofCoursScientifique, anneeScolaire);
					if(moy_gen_grp1>0){
						bulletinAn.setMg_classe_g1(moy_gen_grp1);
					}
					
					double total_pourcentage_g1 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
							listofCoursScientifique, anneeScolaire);
					if(total_pourcentage_g1>=0){
						bulletinAn.setTotal_pourcentage_g1(total_pourcentage_g1);
					}
					
					double moyenne_g1 = ligneAnnuelGroupeCoursScientifique.
							getMoyenneAnElevePourGroupeCours();
					if(moyenne_g1>0){
						bulletinAn.setMoyenne_g1(moyenne_g1);
					}
			
	
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Litteraire (Groupe2) dans l'année
					 * lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll
					 */
					
					LigneAnnuelGroupeCours ligneAnnuelGroupeCoursLitteraire = 
							ub.getLigneAnnuelGroupeCours(eleve, listofCoursLitteraire, anneeScolaire);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setNom_g2("Litteraire");
					}
					else{
						bulletinAn.setNom_g2("Arts");
					}
					
					double total_coef_g2 = ligneAnnuelGroupeCoursLitteraire.getTotalCoefElevePourGroupeCours();
					
					bulletinAn.setTotal_coef_g2(total_coef_g2);
					
					double total_g2 = ligneAnnuelGroupeCoursLitteraire.getTotalNoteAnElevePourGroupeCours();
					if(total_g2>0){
						bulletinAn.setTotal_g2(total_g2);
					}
					
					String totalextreme_g2 = "";
					
					double valeurMoyDernierGrpCours2 = ub.getValeurMoyenneDernierPourGrpDansAn(
							listofElevesClasse, listofCoursLitteraire, anneeScolaire);
					
					double valeurMoyPremierGrpCours2 = ub.getValeurMoyennePremierPourGrpDansAn(
							listofElevesClasse, listofCoursLitteraire, anneeScolaire);
					if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
						totalextreme_g2 = "["+valeurMoyDernierGrpCours2+" ; "+
								valeurMoyPremierGrpCours2+"]";
					}
					bulletinAn.setTotal_extreme_g2(totalextreme_g2);
					
					int r2 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursLitteraire, 
							anneeScolaire, eleve);
					
					if(r2>0){
						bulletinAn.setTotal_rang_g2(r2+"e");
					}
					else{
						bulletinAn.setTotal_rang_g2("");
					}
					
					
					double moy_gen_grp2 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
							listofCoursLitteraire, anneeScolaire);
					if(moy_gen_grp2>0){
						bulletinAn.setMg_classe_g2(moy_gen_grp2);
					}
					
					double total_pourcentage_g2 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
							listofCoursLitteraire, anneeScolaire);
					if(total_pourcentage_g2>=0){
						bulletinAn.setTotal_pourcentage_g2(total_pourcentage_g2);
					}
					
					double moyenne_g2 = ligneAnnuelGroupeCoursLitteraire.
							getMoyenneAnElevePourGroupeCours();
					if(moyenne_g2>0){
						bulletinAn.setMoyenne_g2(moyenne_g2);
					}
	
					
	
					/****************
					 * Informations lie au sommaire de chaque groupe
					 * Groupe des matieres Divers (Groupe3) dans l'année
					 * ddddddddddddddddddddddddddddddddddd
					 */
					
					LigneAnnuelGroupeCours ligneAnnuelGroupeCoursDivers = 
							ub.getLigneAnnuelGroupeCours(eleve, listofCoursDivers, anneeScolaire);
			
					if(lang.equalsIgnoreCase("fr")==true){
						bulletinAn.setNom_g3("Divers");
					}
					else{
						bulletinAn.setNom_g3("Others");
					}
					
					double total_coef_g3 = ligneAnnuelGroupeCoursDivers.getTotalCoefElevePourGroupeCours();
					
					bulletinAn.setTotal_coef_g3(total_coef_g3);
					
					double total_g3 = ligneAnnuelGroupeCoursDivers.getTotalNoteAnElevePourGroupeCours();
					if(total_g3>0){
						bulletinAn.setTotal_g3(total_g3);
					}
					
					String totalextreme_g3 = "";
					
					double valeurMoyDernierGrpCours3 = ub.getValeurMoyenneDernierPourGrpDansAn(
							listofElevesClasse, listofCoursDivers, anneeScolaire);
					
					double valeurMoyPremierGrpCours3 = ub.getValeurMoyennePremierPourGrpDansAn(
							listofElevesClasse, listofCoursDivers, anneeScolaire);
					if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
						totalextreme_g3 = "["+valeurMoyDernierGrpCours3+" ; "+
								valeurMoyPremierGrpCours3+"]";
					}
					bulletinAn.setTotal_extreme_g3(totalextreme_g3);
					
					int r3 = ub.getRangMoyenneAnElevePourGroupe(classeConcerne, listofCoursDivers, 
							anneeScolaire, eleve);
					
					if(r3>0){
						bulletinAn.setTotal_rang_g3(r3+"e");
					}
					else{
						bulletinAn.setTotal_rang_g3("");
					}
					
					
					double moy_gen_grp3 = ub.getMoyenneGeneralPourGroupeCoursAn(classeConcerne, 
							listofCoursDivers, anneeScolaire);
					if(moy_gen_grp3>0){
						bulletinAn.setMg_classe_g3(moy_gen_grp3);
					}
					
					double total_pourcentage_g3 = ub.getTauxReussitePourGroupeCoursAn(classeConcerne, 
							listofCoursDivers, anneeScolaire);
					if(total_pourcentage_g3>=0){
						bulletinAn.setTotal_pourcentage_g3(total_pourcentage_g3);
					}
					
					double moyenne_g3 = ligneAnnuelGroupeCoursDivers.
							getMoyenneAnElevePourGroupeCours();
					if(moyenne_g3>0){
						bulletinAn.setMoyenne_g3(moyenne_g3);
					}
					
					/************************************
					 * Listes alimentant les sous rapports: les rapports sur les groupes des matières 
					 **********/
					
					
					List<MatiereGroupe1AnnuelBean> listofCoursScientifiqueAnnuelBean 
								= new ArrayList<MatiereGroupe1AnnuelBean>(); 
					
					int rc1 = 0;
					/***
					 * debut du for sur les cours scientifique
					 * Gestion des cours scientifique
					 */
					for(Cours cours : listofCoursScientifique){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe1AnnuelBean mGrp1AnBean = new MatiereGroupe1AnnuelBean();
						
						
						
						RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
								classeConcerne, cours, anneeScolaire);
						
						String matiere = ub.subString(cours.getIntituleCours(), 17);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 5);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
						
						mGrp1AnBean.setMatiere_g1(matiere);
						mGrp1AnBean.setMatiere_g1_2emelang(matiere_2emelang);
						mGrp1AnBean.setProf_g1(nomProf);
						
						double soenoteAn = 0;
						int nbreNoteDansAnPourCours = 0;
						
						for(Trimestres trim : anneeScolaire.getListoftrimestre()){
							if(trim.getNumeroTrim() == 1){
								double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g1>0){
									mGrp1AnBean.setNote_1_g1(note_trim_g1);
									soenoteAn = soenoteAn + note_trim_g1;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 2){
								double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g2>0){
									mGrp1AnBean.setNote_2_g1(note_trim_g2);
									soenoteAn = soenoteAn + note_trim_g2;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 3){
								double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g3>0){
									mGrp1AnBean.setNote_3_g1(note_trim_g3);
									soenoteAn = soenoteAn + note_trim_g3;
									nbreNoteDansAnPourCours +=1; 
								}
							}
						}
						
						double noteCoursAn = 0;
						if(nbreNoteDansAnPourCours>0){
							noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
							mGrp1AnBean.setNote_ann_g1(noteCoursAn);
							double total_ann_g1 = noteCoursAn*cours.getCoefCours();
							mGrp1AnBean.setTotal_ann_g1(total_ann_g1);
						}
						
						mGrp1AnBean.setCoef_g1(cours.getCoefCours());
						
						String extreme_g1 = "";
						double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
						double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
						if(noteAnDernierCours>0 && noteAnPremierCours>0){
							extreme_g1 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
							mGrp1AnBean.setExtreme_g1(extreme_g1);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc1 = ub.getRangNoteAnnuelElevePourCours(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc1>0){
							mGrp1AnBean.setRang_g1(rc1+"e");
						}
						else{
							mGrp1AnBean.setRang_g1("");
						}
						
						
						double moy_classe_g1 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(moy_classe_g1>0){
							mGrp1AnBean.setMoy_classe_g1(moy_classe_g1);
						}
						
						
						double pourcentage_g1 = ub.getTauxReussiteCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(pourcentage_g1>=0){
							mGrp1AnBean.setPourcentage_g1(pourcentage_g1);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
						mGrp1AnBean.setAppreciation_g1(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursScientifiqueAnnuelBean.add(mGrp1AnBean);
					
					}//fin du for sur les cours scientifique
					/****
						fin du for sur les cours scientifique qui passe dans la classe
					 *****/
	
					//On place la liste des matieres scientifique construit
					bulletinAn.setMatieresGroupe1Annuel(listofCoursScientifiqueAnnuelBean);
					
	
					List<MatiereGroupe2AnnuelBean> listofCoursLitteraireAnnuelBean 
								= new ArrayList<MatiereGroupe2AnnuelBean>(); 
					
					int rc2 = 0;
					/***
					 * debut du for sur les cours Litteraire
					 * Gestion des cours Litteraire
					 */
					for(Cours cours : listofCoursLitteraire){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe2AnnuelBean mGrp2AnBean = new MatiereGroupe2AnnuelBean();
						
						
						
						RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
								classeConcerne, cours, anneeScolaire);
						
						String matiere = ub.subString(cours.getIntituleCours(), 17);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 5);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
						
						mGrp2AnBean.setMatiere_g2(cours.getCodeCours());
						
						mGrp2AnBean.setMatiere_g2(matiere);
						mGrp2AnBean.setMatiere_g2_2emelang(matiere_2emelang);
						mGrp2AnBean.setProf_g2(nomProf);
						
						double soenoteAn = 0;
						int nbreNoteDansAnPourCours = 0;
						
						for(Trimestres trim : anneeScolaire.getListoftrimestre()){
							if(trim.getNumeroTrim() == 1){
								double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g1>0){
									mGrp2AnBean.setNote_1_g2(note_trim_g1);
									soenoteAn = soenoteAn + note_trim_g1;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 2){
								double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g2>0){
									mGrp2AnBean.setNote_2_g2(note_trim_g2);
									soenoteAn = soenoteAn + note_trim_g2;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 3){
								double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g3>0){
									mGrp2AnBean.setNote_3_g2(note_trim_g3);
									soenoteAn = soenoteAn + note_trim_g3;
									nbreNoteDansAnPourCours +=1; 
								}
							}
						}
						
						double noteCoursAn = 0;
						if(nbreNoteDansAnPourCours>0){
							noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
							mGrp2AnBean.setNote_ann_g2(noteCoursAn);
							double total_ann_g2 = noteCoursAn*cours.getCoefCours();
							mGrp2AnBean.setTotal_ann_g2(total_ann_g2);
						}
						
						mGrp2AnBean.setCoef_g2(cours.getCoefCours());
						
						String extreme_g2 = "";
						double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
						double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
						if(noteAnDernierCours>0 && noteAnPremierCours>0){
							extreme_g2 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
							mGrp2AnBean.setExtreme_g2(extreme_g2);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc2 = ub.getRangNoteAnnuelElevePourCours(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc2>0){
							mGrp2AnBean.setRang_g2(rc2+"e");
						}
						else{
							mGrp2AnBean.setRang_g2("");
						}
						
						
						double moy_classe_g2 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(moy_classe_g2>0){
							mGrp2AnBean.setMoy_classe_g2(moy_classe_g2);
						}
						
						
						double pourcentage_g2 = ub.getTauxReussiteCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(pourcentage_g2>=0){
							mGrp2AnBean.setPourcentage_g2(pourcentage_g2);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
						mGrp2AnBean.setAppreciation_g2(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursLitteraireAnnuelBean.add(mGrp2AnBean);
					
					}//fin du for sur les cours Litteraire
					/****
						fin du for sur les cours Litteraire qui passe dans la classe
					 *****/
					
					//On place la liste des matieres scientifique construit
					bulletinAn.setMatieresGroupe2Annuel(listofCoursLitteraireAnnuelBean);
					
	
					List<MatiereGroupe3AnnuelBean> listofCoursDiversAnnuelBean 
								= new ArrayList<MatiereGroupe3AnnuelBean>(); 
					
					int rc3 = 0;
					/***
					 * debut du for sur les cours Divers
					 * Gestion des cours Divers
					 */
					for(Cours cours : listofCoursDivers){
						
						//long debutforCoursTime = System.currentTimeMillis();
						
						MatiereGroupe3AnnuelBean mGrp3AnBean = new MatiereGroupe3AnnuelBean();
						
						
						
						RapportAnnuelCours rapportAnnuelCours = ub.getRapportAnnuelCours(
								classeConcerne, cours, anneeScolaire);
						
						String matiere = ub.subString(cours.getIntituleCours(), 17);
						matiere = matiere + ":";
						String codeMat = ub.subString(cours.getCodeCours(), 5);
						matiere = matiere + codeMat;
						
						String matiere_2emelang = ub.subString(cours.getIntitule2langueCours(), 17);
						
						String nomProf = cours.getProffesseur().getNomsPers()+" "+cours.getProffesseur().getPrenomsPers();
						nomProf = ub.subString(nomProf, 15);
						
						mGrp3AnBean.setMatiere_g3(matiere);
						mGrp3AnBean.setMatiere_g3_2emelang(matiere_2emelang);
						mGrp3AnBean.setProf_g3(nomProf);
						
						double soenoteAn = 0;
						int nbreNoteDansAnPourCours = 0;
						
						for(Trimestres trim : anneeScolaire.getListoftrimestre()){
							if(trim.getNumeroTrim() == 1){
								double note_trim_g1 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g1>0){
									mGrp3AnBean.setNote_1_g3(note_trim_g1);
									soenoteAn = soenoteAn + note_trim_g1;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 2){
								double note_trim_g2 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g2>0){
									mGrp3AnBean.setNote_2_g3(note_trim_g2);
									soenoteAn = soenoteAn + note_trim_g2;
									nbreNoteDansAnPourCours +=1; 
								}
							}
							else if(trim.getNumeroTrim() == 3){
								double note_trim_g3 = ub.getValeurNotesFinaleEleveTrimestre(eleve, cours, trim);
								if(note_trim_g3>0){
									mGrp3AnBean.setNote_3_g3(note_trim_g3);
									soenoteAn = soenoteAn + note_trim_g3;
									nbreNoteDansAnPourCours +=1; 
								}
							}
						}
						
						double noteCoursAn = 0;
						if(nbreNoteDansAnPourCours>0){
							noteCoursAn = soenoteAn/nbreNoteDansAnPourCours;
							mGrp3AnBean.setNote_ann_g3(noteCoursAn);
							double total_ann_g3 = noteCoursAn*cours.getCoefCours();
							mGrp3AnBean.setTotal_ann_g3(total_ann_g3);
						}
						
						mGrp3AnBean.setCoef_g3(cours.getCoefCours());
						
						String extreme_g3 = "";
						double noteAnDernierCours = rapportAnnuelCours.getValeurNoteDernier();
						double noteAnPremierCours = rapportAnnuelCours.getValeurNotePremier();
						if(noteAnDernierCours>0 && noteAnPremierCours>0){
							extreme_g3 = "["+noteAnDernierCours+" ; "+ noteAnPremierCours+"]";
							mGrp3AnBean.setExtreme_g3(extreme_g3);
						}
						
						List<Eleves> listofEleveOrdreDecroissantPourCours = mapCoursEleves.get(cours.getIdCours());
						
						rc3 = ub.getRangNoteAnnuelElevePourCours(eleve, 
								listofEleveOrdreDecroissantPourCours);
						
						if(rc3>0){
							mGrp3AnBean.setRang_g3(rc3+"e");
						}
						else{
							mGrp3AnBean.setRang_g3(" ");
						}
						
						
						double moy_classe_g3 = ub.getMoyenneGeneralCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(moy_classe_g3>0){
							mGrp3AnBean.setMoy_classe_g3(moy_classe_g3);
						}
						
						
						double pourcentage_g3 = ub.getTauxReussiteCoursAn(classeConcerne, 
								cours, anneeScolaire);
						if(pourcentage_g3>=0){
							mGrp3AnBean.setPourcentage_g3(pourcentage_g3);
						}
						
						String appreciationNote = ub.calculAppreciation(noteCoursAn,lang);
						mGrp3AnBean.setAppreciation_g3(appreciationNote);
						
						//On ajoute la ligne de cours dans le groupe correspondant
						listofCoursDiversAnnuelBean.add(mGrp3AnBean);
					
					}//fin du for sur les cours Divers
					/****
						fin du for sur les cours Divers qui passe dans la classe
					 *****/
					
					//On place la liste des matieres scientifique construit
					bulletinAn.setMatieresGroupe3Annuel(listofCoursDiversAnnuelBean);
					
					
					
					long finforTime = System.currentTimeMillis();
					collectionofBulletionAnnuel.add(bulletinAn);
					System.err.println("bulletin "+numBull+" de  "+ eleve.getNomsEleves()+
							" dans l'année "+anneeScolaire.getIntituleAnnee()+
							"  ajouter avec succes en "+(finforTime-startTimeFor));
					numBull++;				
				}
			}//fin du for sur les élèves
			

		return collectionofBulletionAnnuel;
	
	 
	}

}
