/**
 * 
 */
package org.logesco.services;

import java.util.Collection;
import java.util.List;

import org.logesco.entities.Censeurs;
import org.logesco.entities.Eleves;
import org.logesco.entities.Enseignants;
import org.logesco.entities.Intendant;
import org.logesco.entities.PersonnelsDAppui;
import org.logesco.entities.Proffesseurs;
import org.logesco.entities.SG;
import org.logesco.entities.Utilisateurs;
import org.logesco.modeles.FicheScolariteparClasseBean;
import org.logesco.modeles.PersonnelBean;

/**
 * @author cedrickiadjeu
 *
 */
public interface IProviseurService {
	
	/**************
	 * Liste des methodes qui correspondent aux besoins fonctionnels du role Proviseur uniquement
	 * vis à vis de l'application 
	 *******************/

	/****************************************************
	 *  * Cette méthode met à jour un proffesseurs dont l'idUsers est passé en paramètre
	 * 
	 * Cette méthode retourne
	 *					l'id du Proffesseurs mis à jour si tout s'est bien passe
	 *					-1 si pour la mise a jour la contrainte sur le numerocni est violé
	 *					-2 si pour la mise a jour  la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour la mise a jour la contrainte sur le username est violé
	 *
	 * @param idUsers
	 * @param proffesseurs
	 * @return
	 */
	public Long updateProffesseurs(Long idUsers, Proffesseurs proffesseurs);
	
	/*******************************************************************************
	 * /**
	 * Enregistrer un censeur dans la base de donnée sachant que ce censeur peut en plus être un enseignant
	 * Retourne l'id du censeur enregistré ou mis a jour
	 * 
	 * Elle prend en paramètre un entier roleCode qui est =0 si le censeur est uniquement censeur 
	 * 																					et =1 si le censeur est aussi enseignant
	 * 
	 *Cette méthode retourne
	 *					l'id du censeur enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 *					-4 si la contrainte sur le numeroCenseur est violé
	 *					-5 si le rôle intendant indiqué n'est pas encore enregistré par l'administrateur
	 *					-6 si le rôle enseignant indiqué n'est pas encore enregistré par l'administrateur
	 *
	 * @param censeur
	 * @param roleCode
	 * @return
	 */
	public Long saveCenseurs(Censeurs censeur, int roleCode);
	
	/**********************************************************************************
	 * /*********************************************************************************
	 * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 *
	 * @param listofcenseur
	 * @return
	 */
	public List<String> saveListCenseurs(List<Censeurs> listofcenseur);
	
	/**************************************************************************************
	 * * Met à jour le numero d'un Censeurs à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de le Censeurs a bien été mis à jour
	 *					 0  si la contrainte sur le numero Censeurs est violé
	 *
	 * @param idUsers
	 * @param newNumero
	 * @return
	 */
	public int updateNumeroCenseurs(Long idUsers, int newNumero);
	
	/***********************************************************************************
	 *  * Enregistrer un sg dans la base de donnée sachant que ce sg peut en plus être un enseignant
	 * Retourne l'id du sg enregistré ou mis a jour
	 * 
	 * Elle prend en paramètre un entier roleCode qui est =0 si le sg est uniquement sg 
	 * 																					et =1 si le sg est aussi enseignant
	 * 
	 *Cette méthode retourne
	 *					l'id du sg enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 *					-4 si la contrainte sur le numeroSG est violé
	 *					-5 si le rôle intendant indiqué n'est pas encore enregistré par l'administrateur
	 *					-6 si le rôle enseignant indiqué n'est pas encore enregistré par l'administrateur
	 *
	 * @param sg
	 * @param roleCode
	 * @return
	 */
	public Long saveSG(SG sg, int roleCode);
	
	/******************************************************************************************
	 *  * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 *
	 * @param listofsg
	 * @return
	 */
	public List<String> saveListSG(List<SG> listofsg);
	
	/*************************************************************************************
	 *  * Met à jour le numero d'un SG à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de le SG a bien été mis à jour
	 *					 0  si la contrainte sur le numero SG est violé
	 *
	 * @param idUsers
	 * @param newNumero
	 * @return
	 */
	public int updateNumeroSG(Long idUsers, int newNumero);
	
	/********************************************************************************
	 * * Enregistrer un intendant dans la base de donnée sachant que ce censeur peut en plus être un enseignant
	 * Retourne l'id du intendant enregistré ou mis a jour
	 * 
	 * Elle prend en paramètre un entier roleCode qui est =0 si le intendant est uniquement intendant 
	 * 																					et =1 si le intendant est aussi enseignant
	 * 
	 *Cette méthode retourne
	 *					l'id du intendant enregistrer si tout s'est bien passe
	 *					-1 si pourl'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour l'enregistrement la contrainte sur le username est violé
	 *					-4 si la contrainte sur le numeroIntendant est violé
	 *					-5 si le rôle intendant indiqué n'est pas encore enregistré par l'administrateur
	 *					-6 si le rôle enseignant indiqué n'est pas encore enregistré par l'administrateur
	 *
	 * @param intendant
	 * @param roleCode
	 * @return
	 */
	public Long saveIntendant(Intendant intendant, int roleCode);
	
	/*********************************************************************************
	 *  * Met à jour le numero d'un intendant à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de l'intendant a bien été mis à jour
	 *					 0  si la contrainte sur le numero intendant est violé
	 *
	 * @param idUsers
	 * @param newNumero
	 * @return
	 */
	public int updateNumeroIntendant(Long idUsers, int newNumero);
	
	/****************************************************************************
	 * * Enregistrer un enseignant dans la base de donnée 
	 * Retourne l'id du enseignant enregistré ou mis a jour
	 * 
	 * 
	 *Cette méthode retourne
	 *					l'id du enseignant enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 * @param enseignants
	 * @return
	 */
	public Long saveEnseignants(Enseignants enseignants);
	
	/*****************************************************************************************
	 *  * Enregistrer un personnel d'appui dans la base de donnée 
	 * Retourne l'id du enseignant enregistré ou mis a jour
	 * 
	 * 
	 *Cette méthode retourne
	 *					l'id du personnel d'appui enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 * @param personnels
	 * @return
	 */
	public Long savePersonnelsDAppui(PersonnelsDAppui personnels);
	
	/************************************************************************************************
	 * * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 *
	 * @param listofenseignants
	 * @return
	 */
	public List<String> saveListEnseignants(List<Enseignants> listofenseignants);
	
	/************************************************************************
	 *  * Cette methode attribue un role à un utilisateur dont l'idUsers est passé en paramètres
	 * donc enregistre les entites de UtilisateursRoles. 
	 * 		Retourne 1 lorsque tout s'est bien passé 
	 * 						 0 sinon
	 *
	 * @param idUsers
	 * @param roleString
	 * @return
	 */
	public int saveUsersRoles(Long idUsers, String roleString);
	
	/***************************************************************
	 * * Supprimer tous les rôles qui sont attribués à un utilisateur
	 * Retourne 1 si tous est bien fait et 0 sinon
	 *
	 * @param users
	 * @return
	 */
	public int supprimerAllRoleUsers(Utilisateurs users);
	
	/***************************************************************
	 * * Supprimer un utilisateur de la base de données
	 * Elle retourne 1 si la suppression s'est bien passé et 0 sinon
	 * Pour supprimer un users, il faut supprimer le censeur, SG, intendant ou enseignant correspondant,
	 * puis supprimer l'entité Proffesseur associe, puis l'entité Personnel Puis tous les entités UtilisateursRoles associe
	 * et enfin l'entité utilisateur
	 * 
	 * Elle prend en paramètre idUsers à supprimer
	 *
	 * @param idUsers
	 * @return
	 */
	public int deleteUsers(Long idUsers);
	
	/****************************************************************************************
	 * *Methode permettant d'enregistrer un élève nouvellement recruté dans la 
	 *base de données. 
	 *Elle prend en paramètre l'élève à enregistrer, l'identifiant de la classe dans
	 *laquelle enregistré (car sur l'interface on affiche certe l'étiquete mais 
	 *au moment de l'envoi au serveur c'est l'identifiant qui est envoyé)
	 *
	 *Elle retourne un Long qui correspond à l'identifiant de l'élève enregistré
	 *ou alors
	 *			0 le triplet noms+prenoms+datenaissance n'est pas unique
	 *			-1 si le matricule n'est pas unique
	 *			-2 si la classe dont l'identifiant est indiqué n'est pas existante
	 *
	 * @param eleve
	 * @param idClasse
	 * @return
	 */
	public Long saveEleves(Eleves eleve, Long idClasse);
	
	/*****************************************************************************************
	 * * Cette methode permettra d'enregistrer une liste d'élève construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les élèves de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss) qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que cette contrainte pour empecher un enregistrement
	 * d'un élève du moment ou le matricule est généré par l'application.
	 *
	 * @param listofeleve
	 * @param idClasse
	 * @return
	 */
	public List<String> saveListEleves(List<Eleves> listofeleve, Long idClasse);
	
	/********************************************************************************
	 * *Methode permettant de mettre à jour un élève enregistré dans la base de donnée
	 *Elle prend en paramètre l'élève à modifier, l'identifiant de sa nouvelle classe 
	 * (car sur l'interface on affiche certe l'étiquete mais 
	 *au moment de l'envoi au serveur c'est l'identifiant qui est envoyé)
	 *
	 *Elle retourne un Long qui correspond à l'identifiant de l'élève modifie
	 *ou alors
	 *			0 le triplet noms+prenoms+datenaissance n'est pas unique donc les nouvelles valeurs ne respecte pas la contrainte
	 *			-1 si le matricule n'est pas unique donc le nouveau ne respecte pas la contrainte
	 *			-2 si la classe dont l'identifiant est indiqué n'est pas existante(evenement impossible)
	 *			-3 si l'eleve à modifier n'est pas retrouver dans la base de donnée (evenement impossible)
	 *
	 * @param eleveModif
	 * @param newidClasse
	 * @return
	 */
	public Long updateEleves(Eleves eleveModif, Long newidClasse);
	
	/**************************************************************************************
	 * * Methode permettant de supprimer un élève de la base de données complètement
	 * 
	 * Retourne 1 si la supppression se passe bien et 0 sinon
	 *
	 * @param idElevesASupprim
	 * @return
	 */
	public int supprimerEleves(Long idElevesASupprim);
	
	/**************************************************************************
	 * * Methode qui change l'état d'une Sequence. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si la sequence passe à l'état true
	 * 	1 si la sequence passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 *
	 * @param idPeriode
	 * @return
	 */
	public int swicthEtatPeriodesSeq(Long idPeriode);
	
	/*********************************************************
	 * * Methode qui change l'état d'un Trimestre. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si le trimestre passe à l'état true
	 * 	1 si le trimestre passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 *
	 * @param idPeriode
	 * @return
	 */
	public int swicthEtatPeriodesTrim(Long idPeriode);
	
	/*********************************************************************************
	 * * Methode qui change l'état d'une AnneeScolaire. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si l'année scolaire passe à l'état true
	 * 	1 si l'année scolaire passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 *
	 * @param idPeriode
	 * @return
	 */
	public int swicthEtatPeriodesAnnee(Long idPeriode);
	
	/********************************************************************************************
	 *  * Methode qui va fixer le montant de la scolarité de chaque classe. Elle prend en paramètre l'id de la classe
	 * à modifier et le montant de la scolarité qu'on désire fixer
	 * 	Elle retourne 1 si tout s'est bien passé
	 * 							0 si la classe n'existe pas
	 * 							-1 si le montant n'est pas un double
	 * 							-2 en cas d'autres erreurs
	 *
	 * @param idClasseAConfig
	 * @param montantScolarite
	 * @return
	 */
	public int setMontantScoClasse(Long idClasseAConfig, double montantScolarite);
	
	/*****************************************************************************
	 *  * Cette methode retourne le numero utilise dans la generation du matricule du dernier 
	 * eleve à enregistrer dans la base de donnée.
	 * Ce matricule est concu en concatenant le code de l'établissement au quatre chiffres de l'année
	 * suivi d'un tiret et du numéro d'enregistrement de l'élève. Ce numéro d'enregistrement tiendra sur 
	 * 4 chiffres.
	 * Donc on a toujours un et un seul enregistrement dans la table matricule dont le champ valeur 
	 * indique le numero d'enregistrement du dernier élève enregistré. Ainsi, le prochain eleve aura ce numero
	 * incrémente de 1
	 * @param annee
	 * @param codeEtab
	 * @return
	 *
	 * @param codeEtab
	 * @param annee
	 * @return
	 */
	public String getNextMatricule(String codeEtab, String annee);
	
	/***********************************************************
	 * * Cette methode retourne la liste de tous les personnels  enregistré dans le système avec le statut
	 * passe en parametre
	 * @return
	 *
	 * @param statutPers
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofPersonnelDeStatutBean(String statutPers);
	
	/*********************************************************************************
	 * * Cette methode retourne la liste de tous les proffesseurs  enregistré dans le système avec le statut
	 * passe en parametre
	 * @return
	 *
	 * @param statutPers
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofProffesseursDeStatutBean(String statutPers);
	

	
	
	/**********************************************************************************
	 *  * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction INTENDANT dans l'établissement. 
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofIntendantBean();
	
	/********************************************************************************
	 *  * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction ENSEIGNANT dans l'établissement. 
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofEnseignantBean();
	
	/***************************************************************************
	 *  Cette methode retourne la liste des membres du personnel
	 * ayant la fonction SG dans l'établissement. 
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofSgBean();
	
	/*************************************************************
	 * * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction CENSEUR dans l'établissement. 
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofCenseurBean();
	
	/****************************************************************************************
	 * * Cette methode retourne la liste des membres du personnel
	 * de l'établissement sans distinction de fonction
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofPersonnelBean();
	
	/*********************************************************************
	 * * Cette methode permet de retourner la liste des classes enregistrer avec les montant de scolarte s'il
	 * sont deja defini
	 * @return
	 */
	public Collection<FicheScolariteparClasseBean> generateListFicheScolariteparClasseBean();
	
}
