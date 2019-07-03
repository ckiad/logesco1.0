/**
 * 
 */
package org.logesco.services;

import java.util.Collection;
import java.util.Date;

import org.logesco.entities.Annee;
import org.logesco.entities.Classes;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.modeles.FicheAbsenceEleveBean;

/**
 * @author cedrickiadjeu
 *
 */
public interface ISGService {

	/*************************************************************************************
	 *  Methode qui enregistre le rapport des heures d'absence d'un éleve pour une séquence donnée
	 * l'identifiant de l'élève, de la sequence sont pris en paramètre. Les heures d'absences justifiés et non justifiés
	 * constituant le rapport des absences et aussi pris en paramètre.
	 
	 * 
	 * Elle retourne 1 si tout s'est bien passé
	 * 							 0 si les heures justifies sont superieur au heure non justifie car on ne peut justifie ce qu'on
	 * a pas eu
	 * 						    -1 si les heures d'absence saisies sont négatif
	 * 						    -2 si la date_enreg est ultérieure à la date du jour
	 * 						    -3 en cas de toutes autres erreurs
	 * @param idEleves
	 * @param idSequence
	 * @param nbreHJ
	 * @param nbreHNJ
	 * @param date_enreg
	 * @return
	 */
	public int saveRAbsenceSeqEleve(Long idEleves, Long idSequence, int nbreHJ, int nbreHNJ, Date date_enreg);
	
	/******************************************************************************************************
	 * 	 * Methode permettant d'enregistrer une rapport disciplinaire pour un élève dans une séquence. 
	 * Un rapport disciplinaire est lie a une sanction disciplinaire et cette sanction est infligée pour un 
	 * motif.  Elle peut s'exprimer en un nombre de periode avec une unite en heure ou en jour.
	 * 
	 *  Elle retourne 1 si tout s'est bien passe
	 * 							0 si nbreperiode est positif et unite vide
	 * 							-1 si le nbreperiode est négatif
	 * 							-2 si la date_enreg est ultérieure à la date du jour
	 * 							-3 dans tous les autres cas
	 * @param idEleves
	 * @param idSequence
	 * @param date_enreg
	 * @param nbreperiode
	 * @param unite
	 * @param motif
	 * @param idSanctionDisc
	 * @return
	 */
	public int saveRDisciplineSeqEleve(Long idEleves, Long idSequence, Date date_enreg, int nbreperiode, 
			String unite, String motif, Long idSanctionDisc);
	
	
	/********************************************************************************************
	 * * Cette methode supprime un rapport disciplinaire enregistrée
	 * @param idRdisc
	 * @return
	 */
	public int deleteRapportDisciplinaire(Long idRdisc);
	
	/***************************
	 * Cette methode retourne une collection d'objet representant le rapport d'absence de chaque eleve
	 * d'une classe dans une periode donnee
	 * @param classe
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveBean(Classes classe, 
			Date datemin, Date datemax);
	
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveSeqBean(Classes classe, 
			Sequences periode);
	
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveTrimBean(Classes classe, 
			Trimestres periode);
	
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveAnBean(Classes classe, 
			Annee periode);
	
}
