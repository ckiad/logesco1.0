/**
 * 
 */
package org.logesco.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.logesco.entities.Annee;
import org.logesco.entities.Classes;
import org.logesco.entities.Eleves;
import org.logesco.entities.Etablissement;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.modeles.BulletinAnnuelBean;
import org.logesco.modeles.BulletinSequenceBean;
import org.logesco.modeles.BulletinTrimAnnuelBean;
import org.logesco.modeles.BulletinTrimestreBean;
import org.logesco.modeles.FicheConseilClasseBean;

/**
 * @author cedrickiadjeu
 *
 */
public interface IBulletinService {

	/************************************************************
	 *  * Cette methode retourne une collection qui contiendra les bulletins d'une séquence des élèves d'une 
	 * classe dont l'identifiant de la classe et de la séquence sont passé en paramètre. 
	 * Celle semble plus optimisé et met moins de temps pour l'exécution
	 * @param idClasse
	 * @param idSequence
	 * @return
	 */
	public Map<String, Object> generateCollectionofBulletinSequence_opt(Long idClasse, 
			Long idSequence);
	
	/**************************************************************
	 * * Cette methode calcule et génère tous les bulletins trimestriel d'une classe pour affichage. Il ne les 
	 * enregistre pas en base de données. Donc en fait toutes les classes prévues pour enregistrer les 
	 * bulletins doivent être supprimé puisqu'un bulletin doit toujours etre généré à la volée. A chaque 
	 * fois qu'on demande un bulletin il faut le regénéré puisque les notes peuvent avoir changé.
	 * Cette fonction ci est quel peut plus optimisée
	 * @param idClasse
	 * @param idTrimestre
	 * @return
	 */
	 public Map<String, Object> generateCollectionofBulletinTrimestre_opt(Long idClasse, 
				Long idTrimestre);
	 
	 /**********************************************************************
	  *  Cette methode calcule et génère tous les bulletins annuels d'une classe pour affichage. Il ne les 
	 * enregistre pas en base de données. Donc en fait toutes les classes prévues pour enregistrer les 
	 * bulletins doivent être supprimé puisqu'un bulletin doit toujours etre généré à la volée. A chaque 
	 * fois qu'on demande un bulletin il faut le regénéré puisque les notes peuvent avoir changé, les 
	 * heures d'absences, les noms des enseignants, le titulaire de la classe et autres.
	 * Cette fonction ci est quel peut plus optimisée
	  * @param idClasse
	  * @param idAnnee
	  * @return
	  */
	 public  Map<String, Object> generateCollectionofBulletinAnnee(Long idClasse,
				Long idAnnee);
	 
	 /**************************************************************************
	  * * Cette methode retourne la liste des bulletins trimestriels de tous les eleves contenus dans la classe dont 
	 * l'identifiant est passé en paramètre. Pour chaque bulletin trimestriel, on aura dessus un rapport annuel
	 * montrant l'incidence de la moyenne trimestriel sur la moyenne annuel.
	  * @param idClasse
	  * @param idTrimestre
	  * @return
	  */
	 public  Map<String, Object> generateCollectionofBulletinTrimAnnee(Long idClasse,
				Long idTrimestre);
	 
	 /**********************************************************
	  * * Cette methode retourne le bulletin trimestriel d'un eleve avec son  rapport annuel. 
	  * @param idEleve
	  * @param idClasse
	  * @param idTrimestre
	  * @return
	  */
	 public Collection<BulletinTrimAnnuelBean> generate1BulletinTrimAnnuel(Long idEleve, Long idClasse, 
				Long idTrimestre);
	 
	 /*************************************************************
	  * * Cette methode retourne le bulletin d'un eleve dans un trimestre. l'identifiant de l'élève et du trimestre
	  * sont passe en paramètre. 
	  * @param idEleve
	  * @param idClasse
	  * @param idTrimestre
	  * @return
	  */
	 public Collection<BulletinTrimestreBean> generate1BulletinTrimestre(Long idEleve, Long idClasse, 
			 Long idTrimestre);
	
	 /******************************************************************************
	  *  * Cette methode retourne une collection qui va contenir un seul bulletin séquentiel. 
	 * Il s'agit du bulletin de l'élève dont l'id est passe en parametre. La classe qui passe en
	 * paramètre nous permettra de se rassurer simplement que l'élève se trouve dans la classe
	 * indiquée.
	  * @param idEleve
	  * @param idClasse
	  * @param idSequence
	  * @return
	  */
	 public Collection<BulletinSequenceBean> generate1BulletinSequence(Long idEleve, Long idClasse, Long idSequence);
	 
	 /******************************************************************
	  * * Cette methode retourne le bulletin Annuel de l'élève dont l'id est passe en parametre pour le compte
	 * de l'année dont l'id  est passe en paramètre
	  * @param idEleve
	  * @param idClasse
	  * @param idAnnee
	  * @return
	  */
	 public Collection<BulletinAnnuelBean> generate1BulletinAnnuel(Long idEleve, Long idClasse, 
			 Long idAnnee);
	 
	 /*****************************************************************************
	  *  * Calcule et génère la fiche simplifier de conseil de classe séquentiel. Cette fiche est généré 
	 * automatiquement après la génération des bulletins séquentiels. Donc l'utilisateur n'a pas 
	 * besoin de la générer individuellement. Apres génération des bulletins, il ne lui reste plus 
	 * qu'à actualiser la page et de lancer l'ouverture du rapport du conseil généré conformément 
	 * au bulletin qu'il vient de générer.
	  * @param etab
	  * @param annee
	  * @param classe
	  * @param tauxReussite
	  * @param moyenne_general
	  * @param sequence
	  * @param listofElevesOrdreDecroissantMoyenneSequentiel
	  * @return
	  */
	 public FicheConseilClasseBean getRapportConseilClasseSequentiel(Etablissement etab, 
				Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
				Sequences sequence, List<Eleves> listofElevesOrdreDecroissantMoyenneSequentiel);
	 
	 /***********************************************************************
	  * 
	  * @param etab
	  * @param annee
	  * @param classe
	  * @param tauxReussite
	  * @param moyenne_general
	  * @param trimestre
	  * @param listofElevesOrdreDecroissantMoyenneTrimestriel
	  * @return
	  */
	 public FicheConseilClasseBean getRapportConseilClasseTrimestriel(Etablissement etab, 
				Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
				Trimestres trimestre, List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel);
	 
	 /****************************************************************************
	  * 
	  * @param etab
	  * @param annee
	  * @param classe
	  * @param tauxReussite
	  * @param moyenne_general
	  * @param listofElevesOrdreDecroissantMoyenneAnnuel
	  * @return
	  */
	 public FicheConseilClasseBean getRapportConseilClasseAnnuel(Etablissement etab, 
				Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
				List<Eleves> listofElevesOrdreDecroissantMoyenneAnnuel);
	 
	 
	 
}
