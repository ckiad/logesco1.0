/**
 * 
 */
package org.logesco.services;

import java.util.Collection;

import org.logesco.entities.Cours;
import org.logesco.entities.Matieres;
import org.logesco.modeles.FicheTitulaireparClasseBean;

/**
 * @author cedrickiadjeu
 *
 */
public interface ICenseurService {

	/********************************************************
	 *  * Methode qui supprime une matière(département) de la base de donnée
	 * Elle retourne 
	 * 	1 lorsque la matière est supprimée avec succès
	 * 	0 lorsque la matière contient déjà des cours 
	 * 	-1 si la matière indiquée n'existe pas dans la base de donnée
	 * @param idMatiere
	 * @return
	 */
	public int deleteMatiere(Long idMatiere);
	
	/******************************************************
	 * * Methode qui supprime un cours de la base de donnée
	 * Elle retourne 
	 * 	1 lorsque le cours est supprimé avec succès
	 * 	0 lorsque le cours contient déjà des évaluations 
	 * 	-1 si le cours n'existe pas dans la base de donnée
	 * @param idCours
	 * @return
	 */
	public int deleteCours(Long idCours);
	
	/*********************************************************
	 * * Methode qui enregistre une nouvelle matiere ou met à jour la matiere deja existante
	 * Elle retourne 
	 * 	2 lorsque la matière est nouvelle et a été enregistré avec succès
	 * 	1 lorsque la matière n'est pas nouvelle et par conséquent a été mis à jour avec succès
	 * 	0 lorsque le code qu'on veut donner à la matière violera la contrainte d'unicite du code 
	 * 		de la matiere
	 * @param matiere
	 * @return
	 */
	public int updateMatiere(Matieres matiere);
	
	/***********************************************************************************************
	 * * Methode qui enregistre un nouveau cours ou met à jour le cours deja existant
	 * Elle retourne 
	 * 	2 lorsque le cours est nouveau et a été enregistré avec succès
	 * 	1 lorsque le cours n'est pas nouveau et par conséquent a été mis à jour avec succès
	 * 	0 lorsque le code qu'on veut donner au cours violera la contrainte d'unicite du code 
	 * 		du code
	 * 	-1 lorsque la classe, la matiere ou le prof associe au cours n'existe pas encore
	 * @param cours
	 * @param idMatiereAssocie
	 * @param idProfAssocie
	 * @param idClasseAssocie
	 * @return
	 */
	public int updateCours(Cours cours, Long idMatiereAssocie, Long idProfAssocie, Long idClasseAssocie);
	
	/***************************************************************************************
	 * * Methode permettant d'attribuer un titulaire à une classe. cette méthode octroit le 
	 * role titulaire à l'enseignant désigné comme titulaire de classe le donnant ainsi la 
	 * possibilité d'imprimer les bulletins des classes pour laquelle il est titulaire. 
	 * Elle prend en paramètre l'identifiant de la classe dont on veut attribuer le titulaire et 
	 * l'identifiant du prof qui doit être titulaire
	 * Elle retourne 
	 * 		1 si la méthode s'est bien exécutée
	 * 		0 si le rôle TITUALIRE que doit désormais avoir le prof indiqué n'existe pas
	 * 	   -1 sinon
	 * @param idClasseConcerne
	 * @param idProfTitulaire
	 * @return
	 */
	public int setTitulaireClasse(Long idClasseConcerne, Long idProfTitulaire);
	
	/*********************************************************************
	 * * Cette methode permet de retourner la liste des classes enregistrer avec les titulaire s'il
	 * sont deja defini
	 * @return
	 */
	public Collection<FicheTitulaireparClasseBean> generateListFicheTitulaireparClasseBean();
	
	
}
