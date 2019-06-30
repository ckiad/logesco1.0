/**
 * 
 */
package org.logesco.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.logesco.entities.*;
import org.logesco.modeles.EleveBean;
import org.logesco.modeles.EleveBean2;
import org.logesco.modeles.EleveInsolvableBean;
import org.logesco.modeles.ErrorBean;
import org.logesco.modeles.FicheRecapAbsenceClasseBean;
import org.logesco.modeles.FicheRecapAbsenceCycleBean;
import org.logesco.modeles.FicheRecapAbsenceNiveauBean;
import org.logesco.modeles.OperationBean;
import org.logesco.modeles.PV_NoteBean;
import org.logesco.modeles.PV_SequenceBean;
import org.logesco.modeles.PV_TrimestreBean;
import org.logesco.modeles.PersonnelBean;
import org.springframework.data.domain.Page;

/**
 * @author cedrickiadjeu
 *
 */
public  interface IUsersService {

	/**************
	 * Liste des methodes qui correspondent aux besoins fonctionnels des utilisateurs de l'application
	 * vis à vis de l'application
	 *******************/

	public Etablissement getEtablissement();

	/*****************************************************************************
	 * Cette méthode retourne l'entite Personnels associe au username passe en paramètre
	 * ou alors nul si ce username n'existe pas
	 * @param username
	 * @return
	 */
	public Personnels findPersonnelsByUsername(String username);

	/***************************************************
	 * Recherche l'entité utilisateur associe a un username
	 */
	public Utilisateurs findByUsername(String username);
	
	/*********************************************
	 * retourne 1 si l'utilisateur passe en paramètre a le rôle 
	 * specifier par la chainne de caractère passe en second parametre
	 * @param users
	 * @param role
	 * @return
	 */
	public int possedeRole(Utilisateurs users, String role);

	/******************
	 * Retourne l'utilisateur associé à un idUsers passe en paramètre ou null s'il n'existe pas
	 */
	public Utilisateurs findUtilisateurs(Long idUsers);

	/***
	 * Retourne le Personnels dont le numcni est passe en paramètre null s'il n'existe pas
	 * @param numcniPers
	 * @return
	 */
	public Personnels findPersonnel(String numcniPers);
	
	/***
	 * Retourne le Personnels dont le matricule est passe en paramètre null s'il n'existe pas
	 * @param numcniPers
	 * @return
	 */
	public Personnels findPersonnelAvecMatricule(String matriculePers);

	/***
	 * Retourne le Personnels dont le idUsers est passe en paramètre null s'il n'existe pas
	 * @param idUsers
	 * @return
	 */
	public Personnels findPersonnel(Long idUsers);


	/****
	 * Retourne le Personnels ayant le même triplet noms, prenoms, datenaissance et null s'il n'existe pas
	 * @param nomPers
	 * @param prenomsPers
	 * @param datenaisspers
	 * @return
	 */
	public Personnels findPersonnel(String nomPers, String prenomsPers, Date datenaisspers);

	/***
	 * Retourne l'utilisateur dont le username est passe en paramètre et null s'il n'existe pas
	 * @param username
	 * @return
	 */
	public Utilisateurs getUsers(String username);

	/****
	 * Retourne le censeur dont le numero est passe en paramètre null s'il n'existe aucun censeur avec ce numéro
	 */
	public Censeurs findCenseur(int numeroCenseur);

	/****
	 * Retourne le sg dont le numero est passe en paramètre null s'il n'existe aucun censeur avec ce numéro
	 */
	public SG findSG(int numeroSG);

	/****
	 * Retourne le intendant dont le numero est passe en paramètre null s'il n'existe aucun censeur avec ce numéro
	 */
	public Intendant findIntendant(int numeroIntendant);

	/****************************
	 * Methode permettant de rechercher un élève en fonction d'un motif contenu dans son nom ou son prenom
	 * et rrourne le resultat Page par page
	 * @param motif
	 * @param numPage
	 * @param taillePage
	 * @return
	 */
	public Page<Eleves> findListElevesSelonMotif(String motif, int numPage, int taillePage);

	/**
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
	 */
	//public Long saveCenseurs(Censeurs censeur, int roleCode);
	
	/*********************************************************************************
	 * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 */
	//public List<String> saveListCenseurs(List<Censeurs> listofcenseur);

	/**
	 * Met à jour le numero d'un Censeurs à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de le Censeurs a bien été mis à jour
	 *					 0  si la contrainte sur le numero Censeurs est violé
	 */
	//public int updateNumeroCenseurs(Long idUsers, int newNumero);

	/**
	 * Enregistrer un sg dans la base de donnée sachant que ce sg peut en plus être un enseignant
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
	 */
	//public Long saveSG(SG sg, int roleCode);
	
	/*********************************************************************************
	 * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 */
	//public List<String> saveListSG(List<SG> listofsg);

	/**
	 * Met à jour le numero d'un SG à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de le SG a bien été mis à jour
	 *					 0  si la contrainte sur le numero SG est violé
	 */
	//public int updateNumeroSG(Long idUsers, int newNumero);

	/**
	 * Enregistrer un intendant dans la base de donnée sachant que ce censeur peut en plus être un enseignant
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
	 */
	//public Long saveIntendant(Intendant intendant, int roleCode);

	/**
	 * Met à jour le numero d'un intendant à jour dans la base de données
	 * 
	 *Cette méthode retourne
	 *					 1  si le numero de l'intendant a bien été mis à jour
	 *					 0  si la contrainte sur le numero intendant est violé
	 */
	//public int updateNumeroIntendant(Long idUsers, int newNumero);

	/**
	 * Enregistrer un enseignant dans la base de donnée 
	 * Retourne l'id du enseignant enregistré ou mis a jour
	 * 
	 * 
	 *Cette méthode retourne
	 *					l'id du enseignant enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 ******************************************************************************************************/
	//public Long saveEnseignants(Enseignants enseignants);
	
	/**
	 * Enregistrer un personnel d'appui dans la base de donnée 
	 * Retourne l'id du enseignant enregistré ou mis a jour
	 * 
	 * 
	 *Cette méthode retourne
	 *					l'id du personnel d'appui enregistrer si tout s'est bien passe
	 *					-1 si pour  l'enregistrement la contrainte sur le numerocni est violé
	 *					-2 si pour  l'enregistrement la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour  l'enregistrement la contrainte sur le username est violé
	 ******************************************************************************************************/
	//public Long savePersonnelsDAppui(PersonnelsDAppui personnels);
	
	/*********************************************************************************
	 * Cette methode permettra d'enregistrer une liste d'enseignant construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les enseignants de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss), le username ou le numcni
	 *  qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que ces contraintes pour empecher un enregistrement
	 * d'un personnel.
	 */
	//public List<String> saveListEnseignants(List<Enseignants> listofenseignant);


	/**************************************
	 * Cette méthode met à jour un proffesseurs dont l'idUsers est passé en paramètre
	 * 
	 * Cette méthode retourne
	 *					l'id du Proffesseurs mis à jour si tout s'est bien passe
	 *					-1 si pour la mise a jour la contrainte sur le numerocni est violé
	 *					-2 si pour la mise a jour  la contrainte sur le triplet noms prenoms datenaiss est violé
	 *					-3 si pour la mise a jour la contrainte sur le username est violé
	 */
	//public Long updateProffesseurs(Long idUsers, Proffesseurs proffesseurs);

	/**************************************************************
	 *  Cette methode attribue un role à un utilisateur dont l'idUsers est passé en paramètres
	 * donc enregistre les entites de UtilisateursRoles. 
	 * 		Retourne 1 lorsque tout s'est bien passé 
	 * 						 0 sinon
	 * @param idUsers
	 * @param roleString
	 * @return
	 */
	public int saveUsersRoles(Long idUsers, String roleString);

	/******************************************
	 * Cette methode retourne le role associe a une chaine de caractère et null s'il n'existe pas
	 */
	public Roles findRoles(String role);

	/**************************************************************
	 * Retourne le Proffesseurs dont le numcni est passé en paramètre
	 * retourne null si ce numerocni ne correspond à aucun Personnels
	 ***************************************************************/
	public Proffesseurs findProffesseurs(String numcniPers);

	/*******
	 * Retourne la liste des rôles associe à un utilisateur
	 */
	public List<Roles> findListofRoles(Utilisateurs users);

	/***
	 * Retourne le code correspondant aux roles joue par un utilisateur vis a vis du système
	 * Elle prend en paramètre l'utilisateur dont on veut le code associé à ses roles
	 * codeRole				=1 si c'est un Censeurs et il peut donc aussi avoir le role ENSEIGNANT
	 * 					=2 si SG et il peut donc aussi avoir le role ENSEIGNANT
	 * 					=3 si c'est ENSEIGNANT
	 * 					=4 si c'est INTENDANT et il peut donc aussi avoir le role ENSEIGNANT
	 * 					=5 si c'est SECRETAIRE 
	 * 					=6 si c'est SURVEILLANT
	 * 					=7 si c'est VEILLEUR
	 * 					=7 si c'est AUTRES
	 */
	public int getcodeUsersRole(Utilisateurs users);
	
	/********************************************************************
	 * Cette methode retourne true lorsque l'utilisateur passe en 
	 * paramètre a le role donc l'alias est passe en paramètre. 
	 * @param users
	 * @param role
	 * @return
	 */
	public boolean hasRole(Utilisateurs users, String role);
	
	/***********************************************************
	 * Cette methode retourne la liste des rôles d'un utilisateur
	 * vis à vis du système
	 * @param users
	 * @return
	 */
	public List<String> getListRolesUser(Utilisateurs users);

	/********************************************
	 * Supprimer tous les rôles qui sont attribués à un utilisateur
	 * Retourne 1 si tous est bien fait et 0 sinon
	 */
	//public int supprimerAllRoleUsers(Utilisateurs users);

	/*********************
	 * Retourne le Proffesseurs dont le idUsers est passe en paramètre
	 * 		retourne null si ce Proffesseurs n'existe pas
	 */
	public Proffesseurs findProffesseurs(Long idUsers);

	/*********************
	 * Retourne le Censeurs dont le idUsers est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Censeurs findCenseurs(Long idUsers);

	/*********************
	 * Retourne le Censeurs dont le numeroCNI est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Censeurs findCenseurs(String numeroCniPers);

	/*********************
	 * Retourne le SG dont le idUsers est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public SG findSG(Long idUsers);

	/*********************
	 * Retourne le SG dont le numeroCNI est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public SG findSG(String numeroCniPers);

	/*********************
	 * Retourne le Intendant dont le idUsers est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Intendant findIntendant(Long idUsers);

	/*********************
	 * Retourne le Intendant dont le numeroCNI est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Intendant findIntendant(String numeroCniPers);

	/*********************
	 * Retourne le Enseignant dont le idUsers est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Enseignants findEnseignants(Long idUsers);

	/*********************
	 * Retourne le Enseignant dont le numeroCNI est passe en paramètre
	 * 		retourne null si ce censeur n'existe pas
	 */
	public Enseignants findEnseignants(String numeroCniPers);

	/*********************
	 * Retourne la liste de tous les proviseurs (meme si pour le cas particulier des proviseurs on est sur qu'il n'y a qu'un seul) 
	 * page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse et par ordre croissant des numero dans la fonction
	 * 		retourne null si aucun n'existe
	 */
	public Page<Proviseur> findAllProviseur(int numPage, int taillePage);

	/*********************
	 * Retourne la liste de tous les censeurs page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse et par ordre croissant des numero dans la fonction
	 * 		retourne null si aucun n'existe
	 */
	public Page<Censeurs> findAllCenseurs(int numPage, int taillePage);

	/************************
	 * Retourne la liste de tous les censeurs enregistrés et null si aucun n'est encore enregistrés dans la base de donnée
	 * @return
	 */
	public List<Censeurs> findAllCenseursOrderByNumero();

	/*********************
	 * Retourne la liste de tous les SG page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse et par ordre croissant des numero dans la fonction
	 * 		retourne null si aucun n'existe
	 */
	public Page<SG> findAllSG(int numPage, int taillePage);

	/************************
	 * Retourne la liste de tous les sg enregistrés et null si aucun n'est encore enregistrés dans la base de donnée
	 * @return
	 */
	public List<SG> findAllSgOrderByNumero();

	/*********************
	 * Retourne la liste de tous les Intendants page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse et par ordre croissant des numero dans la fonction
	 * 		retourne null si aucun n'existe
	 */
	public Page<Intendant> findAllIntendant(int numPage, int taillePage);

	/************************
	 * Retourne la liste de tous les intendant enregistrés et null si aucun n'est encore enregistrés dans la base de donnée
	 * @return
	 */
	public List<Intendant> findAllIntendantOrderByNumero();

	/*********************
	 * Retourne la liste de tous les enseignants page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse et par ordre croissant des numero dans la fonction
	 * 		retourne null si aucun n'existe
	 */
	public Page<Enseignants> findAllEnseignants(int numPage, int taillePage);

	/************************
	 * Retourne la liste de tous les enseignants enregistrés et null si aucun n'est encore enregistrés dans la base de donnée
	 * @return
	 */
	public List<Enseignants> findAllEnseignants();
	
	/*********************
	 * Retourne la liste de tous les personnels d'appui page par page classe par ordre alphabetique des noms et prenoms
	 * et par ordre descendant des datenaisse 
	 * 		retourne null si aucun n'existe
	 */
	public Page<PersonnelsDAppui> findAllPersonnelsDAppui(int numPage, int taillePage);

	/************************
	 * Retourne la liste de tous les personnels d'appui enregistrés et null si aucun n'est encore enregistrés dans la base de donnée
	 * Il s'agit des personnels qui ont comme statut tout sauf proviseur, censeur, sg et intendant
	 * @return
	 */
	public List<PersonnelsDAppui> findAllPersonnelsDAppui();

	/*********************
	 * Retourne la liste de tous les utilisateurs enregistré dans la base de données et qui n'ont pas le rôle admin
	 * 		retourne null si aucun n'existe
	 */
	public List<Utilisateurs> findAllUtilisateurs();


	/*********************************************
	 * Supprimer un utilisateur de la base de données
	 * Elle retourne 1 si la suppression s'est bien passé et 0 sinon
	 * Pour supprimer un users, il faut supprimer le censeur, SG, intendant ou enseignant correspondant,
	 * puis supprimer l'entité Proffesseur associe, puis l'entité Personnel Puis tous les entités UtilisateursRoles associe
	 * et enfin l'entité utilisateur
	 * 
	 * Elle prend en paramètre idUsers à supprimer
	 */
	//public int deleteUsers(Long idUsers);

	/************************
	 * Cette fonction retourne le rang inocupée que peut prendre un proffesseurs censeurs, sg ou intendant
	 * En fait si roleCode correspond à censeur alors elle parcourre la liste des censeurs à la recherche du numero non occupe 
	 * et retourne le numero non occupée disponible. 
	 * Elle retourne 0 si une erreur s'est produite
	 * 
	 * Donc en fonction de la valeur du paramètre roleCode, on sait dans quel catégorie chercher le numero disponible
	 * 
	 * /*****
	 * DIFFERENTES COMBINAISONS DE ROLES POSSIBLES ET LEUR NUMERO ASSOCIE
	 * 1==Censeur et enseignant
	 * 2==Censeur uniquement
	 * 3==Surveillant général et enseignant
	 * 4==Surveillant général uniquement
	 * 5==Intendant et enseignant
	 * 6==Intendant uniquement
	 * 7==Enseignant uniquement
	 *******
	 * 
	 * @param proffesseurs
	 * @return
	 */
	public int chercherNumeroPers( int roleCode);


	/*************************************************************
	 * Fonction permettant de retourner la liste des classes enregistrées
	 *************************************************************/

	/********************************************************************
	 * Retourne la classe dont l'id est envoye en parametre et null si cette classe 
	 * n'existe pas
	 ********************************************************************/
	public Classes findClasses(Long idClasse);

	/**********************************************************************************
	 * Retourne la classe dont le code et le numéro puis la specialité sont envoyées en parametre 
	 * et null si cette classe n'existe pas
	 **********************************************************************************/
	public Classes findClasses(String codeClasses, String numeroClasses, Specialites specialite);

	/******************************************************************************
	 * Cette méthode retourne l'effectif des élèves enregistrés dans une classe
	 * Il s'agit de l'effectif provisoire donc ici on n'a pas à vérifier si la scolarité 
	 * est payée ou pas
	 * 
	 * Elle prend en paramètre l'identifiant de la classe dont on veut l'effectif provisoire
	 * 
	 * Si la classe indiqué est inexistante alors elle retourne l'effectif total des eleves 
	 * deja enregistre dans l'etablissement
	 * 
	 * Elle retourne 0 si aucun élève n'est enregistré dans la classe ou dans l'établissement
	 ******************************************************************************/
	public int getEffectifProvisoireClasse(Long idClasse);
	
	/*******************************************************************
	 * Cette methode retourne l'effectif des insolvables dans une classe dont l'identifiant est passé
	 * en paramètre. Lorsqu'aucun élève n'est insolvable dans la classe elle retourne 0. 
	 * Si la classe est inexistante, elle retourne -1;
	 * @param idClasse
	 * @return
	 */
	public int getEffectifInsolvableDansClasse(Long idClasse);
	
	/***************************************************************
	 * Cette fonction retourne la liste des élèves insolvable d'une classe. 
	 * Elle retourne une liste vide au cas où aucun élève de la classe n'est insolvable
	 * et null si la classe dont l'identifiant est envoyé n'est pas trouvé
	 * @param idClasse
	 * @return
	 */
	public List<Eleves> getListElevesInsolvable(Long idClasse);
	
	/*****************************************
	 * Cette methode retourne la liste des élèves insolvables qui doit doit etre 
	 * imprime en PDF
	 * @param idClasse
	 * @return
	 */
	public Collection<EleveInsolvableBean> generateListEleveInsolvable(Long idClasse);
	
	/*************************************************************************************************
	 * Cette fonction retourne la liste des élèves insolvables dans une classe page par page. 
	 * @param idClasses
	 * @param numPage
	 * @param taillePage
	 * @return
	 */
	public Page<Eleves> findPageElevesInsolvable(Long idClasses,	int numPage,	int taillePage);

	/*****************************************************************************
	 * Cette methode retourne le nombre d'eleve considere comme etant définitivement inscrit
	 * dans la classe dont l'identifiant est passé en paramètre. Les eleves ayant paye le montant
	 * de la scolarite correspondant au pourcentage représenté par le paramètre critere. 
	 * @param idClasseSelect
	 * @param critere
	 * @return
	 */
	public int getEffectifDefinitifClasse(Long idClasseSelect, int critere);

	/*******************************************************************************************
	 * Cette méthode retourne l'effectif des élèves enregistrés dans une classe et qui sont d'un sexe particulier
	 * 0 = masculin et 1 = feminin
	 * Il s'agit de l'effectif provisoire donc ici on n'a pas à vérifier si la scolarité 
	 * est payée ou pas
	 * 
	 * Elle prend en paramètre l'identifiant de la classe dont on veut l'effectif provisoire par sexe et le sexe
	 * 
	 * Si la classe indiqué est inexistante alors elle retourne l'effectif total des eleves du sexe indiqué
	 * deja enregistre dans l'etablissement
	 * 
	 * Elle retourne 0 si aucun élève du sexe indiqué n'est enregistré dans la classe ou dans l'établissement
	 * 					-1 si la classe n'existe pas donc erreur programme
	 ******************************************************************************************/
	public int getEffectifProvisoireClasseParSexe(Long idClasse, int sexe);

	/************************************************************
	 * Retourne la liste de toutes les classes page par page
	 * Elle retourne une liste null si aucune classe n'est enregistré en bd
	 ************************************************************/
	public Page<Classes> findPageClasse(int numPage, int taillePage);

	/************************************************************
	 * Retourne la liste de toutes les classes page par page
	 * Elle retourne une liste null si aucune classe n'est enregistré en bd
	 ************************************************************/
	public List<Classes> findListClasse();

	
	/**************************************************************************************
	 * Cette methode retourne le numero utilise dans la generation du matricule du dernier 
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
	 */
	//public String getNextMatricule_(String codeEtab, String annee);
	

	/******************************************
	 * Methode permettant de retourner un Eleves
	 * à partir de son identifiant
	 * Elle retourne null si aucun eleves n'est trouve
	 ******************************************/
	public Eleves findEleves(Long idEleves);

	/******************************************
	 * Methode permettant de retourner un Eleves
	 * à partir de son matricule
	 * Elle retourne null si aucun eleves n'est trouve
	 *******************************************/
	public Eleves findEleves(String matriculeEleves);

	/*************************************************
	 * Methode permettant de retourner un Eleves
	 * à partir de ses noms, ses prénoms et sa datenaissance
	 * Elle retourne null si aucun eleves n'est trouve
	 **************************************************/
	public Eleves findEleves(String nomsEleves, String prenomsEleves, Date datenaissEleves);
	
	/******************************************
	 * Methode permettant de retourner l'Eleves qui suit
	 * celui dont l'id est passe en parametre
	 *  dans la liste des élèves reguliers 
	 * pour la séquence
	 * à partir de son numero
	 * Elle retourne null si aucun eleves n'est trouve
	 ******************************************/
	public Eleves findElevesSuivant(Long idEleve, Long idSequence);

	/***************************************************************
	 *Methode permettant d'enregistrer un élève nouvellement recruté dans la 
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
	 */
	//public Long saveEleves(Eleves eleve, Long idClasse);
	
	/*********************************************************************************
	 * Cette methode permettra d'enregistrer une liste d'élève construite à partir d'un fichier
	 * excel. Cette méthode retourne
	 * 	Une liste vide si tout s'est bien passé et que tous les élèves de la liste ont été enregistré
	 * 	Une liste contenant le triplet (noms+prenoms+datenaiss) qui a causé problème lors de 
	 * l'enregistrement puisqu'il n'y a que cette contrainte pour empecher un enregistrement
	 * d'un élève du moment ou le matricule est généré par l'application.
	 */
	//public List<String> saveListEleves(List<Eleves> listofeleve, Long idClasse);

	/************************************************************
	 * Retourne la liste de tous les eleves page par page
	 * Elle retourne une liste null si aucun eleve n'est enregistré en bd
	 ************************************************************/
	public Page<Eleves> findPageEleves(int numPage, int taillePage);

	/************************************************************
	 * Retourne la liste de tous les eleves page par page d'une classe dont
	 * l'idClasse est passé en paramètre. 
	 * Elle retourne une liste null si aucun eleve n'est enregistré dans cette
	 * classe dans la BD
	 ************************************************************/
	public Page<Eleves> findPageElevesClasse(Long idClasses,	int numPage,	int taillePage);

	/**************************************************************
	 * Retourne la liste de tous les eleves page par page dont l'idClasse est 
	 * passe en parametre et qui ont paye au moins le montantMin qui sera 
	 * aussi passe en paramètre. 
	 * @param idClasses
	 * @param montantMin
	 * @param numPage
	 * @param taillePage
	 * @return
	 */
	public Page<Eleves> findPageDefElevesClasse(Long idClasses, double montantMin,	int numPage,	int taillePage);

	/************************************************************
	 * Retourne la liste de tous les eleves d'une classe dont
	 * l'idClasse est passé en paramètre. 
	 * Elle retourne une liste null si aucun eleve n'est enregistré dans cette
	 * classe dans la BD
	 ************************************************************/
	public List<Eleves> findListElevesClasse(Long idClasses);

	/*********************************************************************
	 *Retourne la liste de tous les eleves dont l'idClasse est 
	 * passe en parametre et qui ont paye au moins le montantMin qui sera 
	 * aussi passe en paramètre. 
	 * @param idClasses
	 * @return
	 */
	public List<Eleves> findListElevesDefClasse(Long idClasses, double montantMin);

	/***************************************************************************
	 *Methode permettant de mettre à jour un élève enregistré dans la base de donnée
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
	 */
	//public Long updateEleves(Eleves eleveAModif, Long newidClasse);

	/***************************************************************************
	 * Methode permettant de supprimer un élève de la base de données complètement
	 * 
	 * Retourne 1 si la supppression se passe bien et 0 sinon
	 */
	//public int supprimerEleves(Long idElevesASupprim);


	/****************************************************************************************
	 * Methode qui enregistre le versement de la scolarite d'un élève
	 * Cette méthode retourne 
	 * Long: c'est l'identifiant de l'operation qui vient de se réaliser sur le compte de l'élève
	 * -1 si il ya une erreur quelconque
	 ******************************************************************************************/
	//public Long enregVersementSco(Long idEleveConcerne, double montantAVerser);
	
	/***********************************************************************************************
	 * Cette methode retourne l'identifiant de la dernière opération sur un compteInscription
	 * Cette opération est forcément celle qui a le plus grand numero. On va donc convertir 
	 * la chaine qui correspond à cet identifiant en long puis faire les comparaisons et prendre 
	 * celle qui a le plus grand identifiant
	 * Elle prend en paramètre l'idEleve donc le compteInscription est concerne par l'operation
	 * @param idEleveConcerne
	 * @return
	 */
	public Long getLastOperationOnCompte(Long idEleveConcerne);
	
	/*****************************************************************************************************
	 * Cette fonction retourne le montant d'une operation donc l'identifiant est passe en parametre
	 * @param idOperation
	 * @return
	 */
	//public double getMontantOperation(Long idOperation);
	
	/***********************************************************
	 * Cette fonction retourne l'identifiant de l'operation qui doit apparaitre sur le 
	 * recu de versement
	 * @param idOperation
	 * @return
	 */
	//public String getIdentifiantOperation(Long idOperation);

	/********************************
	 * Cette fonction retourne le nombre de transaction financière déjà effectué.
	 * Ce nombre correspondra au numéro de la transaction qui doit apparaitre 
	 * sur la prochaine facture qu'éditera le système. 
	 * Cette methode retourne une chaine de caractère qui correspondra à ce nombre 
	 * Comme on peut supprimer des lignes dans la table operation, on aura une table 
	 * qui aura la charge de compter le nombre d'enregistrement déjà effectué. Ainsi, a chaque fois 
	 * qu'on recherchera un numero pour une operation, on lit le champ numero de l'enregistrement 
	 * unique qui se trouvera dans cette table. On va l'incrémenter de 1.
	 * @return
	 */
	public String getNumeroOperation();
	
	/*******************************************************************************************
	 * Methode qui retourne la liste de toutes les matières enregistrés dans la base de données page par page
	 * Elle retourne une liste vide au cas ou aucune matière n'est encore enregistré
	 */
	public Page<Matieres> findAllMatieres(int numPage, int taillePage); 

	/*******************************************************************
	 * Methode qui retourne la matiere dont l'identifiant est passe en parametre
	 */
	public Matieres findMatieres(Long idMatiere);

	/******************************************************************************
	 * Methode qui enregistre une nouvelle matiere ou met à jour la matiere deja existante
	 * Elle retourne 
	 * 	2 lorsque la matière est nouvelle et a été enregistré avec succès
	 * 	1 lorsque la matière n'est pas nouvelle et par conséquent a été mis à jour avec succès
	 * 	0 lorsque le code qu'on veut donner à la matière violera la contrainte d'unicite du code 
	 * 		de la matiere
	 ******************************************************************************/
	//public int updateMatiere(Matieres matiere);

	/**************************************************************************
	 * Methode qui supprime une matière(département) de la base de donnée
	 * Elle retourne 
	 * 	1 lorsque la matière est supprimée avec succès
	 * 	0 lorsque la matière contient déjà des cours 
	 * 	-1 si la matière indiquée n'existe pas dans la base de donnée
	 */
	//public int deleteMatiere(Long idMatiere);

	/*******************************************************************************************
	 * Methode qui retourne la liste de tous les cours enregistrés dans la base de données page par page
	 * Elle retourne une liste vide au cas ou aucun cours n'est encore enregistré
	 */
	public Page<Cours> findAllCours(int numPage, int taillePage); 

	/*************************************************************
	 * Methode qui retourne la liste de toutes les matières enregistrées
	 * classe par ordre alphabétique des intitulés des matières
	 * Elle retourne une liste vide si aucune matière n'est enregistrée
	 */
	public List<Matieres> findAllMatieres();

	/***********************************************************
	 * Methode qui retourne la liste de tous les proviseurs enregistrés 
	 * meme si on sait qu'il ne peut en avoir qu'un seul
	 * Elle retourne une liste vide si aucun n'existe
	 */
	public List<Proviseur> findAllProviseur();

	/********************************************************
	 * Methode qui retourne la liste de tous les censeurs enregistrés 
	 * meme si on sait qu'il ne peut en avoir qu'un seul
	 * Elle retourne une liste vide si aucun n'existe
	 */
	public List<Censeurs> findAllCenseurs();

	/********************************************************
	 * Methode qui retourne la liste de tous les SG enregistrés 
	 * meme si on sait qu'il ne peut en avoir qu'un seul
	 * Elle retourne une liste vide si aucun n'existe
	 */
	public List<SG> findAllSG();

	/********************************************************
	 * Methode qui retourne la liste de tous les Intendant enregistrés 
	 * meme si on sait qu'il ne peut en avoir qu'un seul
	 * Elle retourne une liste vide si aucun n'existe
	 */
	public List<Intendant> findAllIntendant();

	/***********************************************************
	 * Methode qui retourne la liste de tous les Proffesseurs enregistrés 
	 * meme si on sait qu'il ne peut en avoir qu'un seul
	 * Elle retourne une liste vide si aucun n'existe
	 */
	public List<Proffesseurs> findAllProffesseurs();

	/************************************************************
	 * Retourne la liste de toutes les classes ENRGISTREES
	 * Elle retourne une liste null si aucune classe n'est enregistré en bd
	 ************************************************************/
	public List<Classes> findAllClasse();
	
	/********************************************************************************************************
	 * Retourne la liste de toutes les classes ENRGISTREES et qui ne sont pas vide ie qui ont des eleves
	 * Elle retourne une liste null si aucune classe n'est enregistré en bd. Elle va retourner les classes dans 
	 * l'ordre croissant des niveau. Car
	 ********************************************************************************************************/
	public List<Classes> findAllClasseNonVide();
	
	/************************************************************
	 * Retourne la liste de tous les cycles ENRGISTREES
	 * Elle retourne une liste null si aucun cycle n'est enregistré en bd
	 ************************************************************/
	public List<Cycles> findAllCycle();
	
	/****************************************************************
	 * Retourne le cycle dont l'identifiant est passé en parametre
	 ****************************************************************/
	public Cycles findCycle(Long idCycle);

	/*******************************************************************
	 * Methode qui retourne le cours dont l'identifiant est passe en parametre
	 */
	public Cours findCours(Long idCours);

	/************************************
	 * Methode qui retourne la liste de tous les niveaux enregistrés dans l'établissement par l'admin
	 */
	public Page<Niveaux> findPageNiveaux(int numPage, int taillePage);

	/************************************
	 * Methode qui retourne la liste de tous les niveaux enregistrés dans l'établissement par l'admin
	 */
	public List<Niveaux> findAllNiveaux();
	
	/****************************************************************
	 * Retourne le niveau dont l'identifiant est passé en parametre
	 ****************************************************************/
	public Niveaux findNiveaux(Long idNiveaux);

	/************************************
	 * Methode qui retourne la liste de toutes les séquences 
	 */
	public List<Sequences> findAllSequence();

	/*****************************************************
	 * Methode qui retourne la liste de tous les trimestres 
	 */
	public List<Trimestres> findAllTrimestre();

	/************************************
	 * Methode qui retourne la liste de toutes les séquences de l'année en cours ie l'annee active
	 */
	public List<Sequences> findAllSequence(Long idAnneeActive);
	
	/************************************
	 * Methode qui retourne la liste de toutes les séquences Actives de l'année en cours 
	 * ie l'annee active
	 */
	public List<Sequences> findAllSequenceActive(Long idAnneeActive);
	
	
	/************************************
	 * Methode qui retourne la liste de toutes les séquences de l'année en cours ie l'annee active
	 */
	//public List<Sequences> findAllActiveSequence(Long idAnneeActive);
	

	/*****************************************************
	 * Methode qui retourne la liste de tous les trimestres  de l'année en cours ie de l'année active
	 */
	public List<Trimestres> findAllActiveTrimestre(Long idAnneeActive);

	/************************************
	 * Methode qui retourne la liste de tous les niveaux où l'enseignant dont 
	 * l'identifiant est passé en paramètre enseigne. Ainsi cet enseignant n'aura à faire le choix que
	 * dans les niveaux où il enseigne et pour les cours qu'il enseigne dans ces différents niveaux
	 */
	public List<Niveaux> findAllNiveauxEns(Long idUsers);

	/************************************
	 * Methode qui retourne la liste de tous les niveaux où l'enseignant dont 
	 * l'identifiant est passé en paramètre dirige une classe. Ainsi cet enseignant n'aura à faire le choix que
	 * dans les niveaux où il dirige les classes. Et dans ces niveaux on affichera que les classes qu'il dirige effectivemment
	 */
	public List<Niveaux> findAllNiveauxDirigesEns(Long idUsers);

	/******************************************************************************
	 * Methode qui enregistre un nouveau cours ou met à jour le cours deja existant
	 * Elle retourne 
	 * 	2 lorsque le cours est nouveau et a été enregistré avec succès
	 * 	1 lorsque le cours n'est pas nouveau et par conséquent a été mis à jour avec succès
	 * 	0 lorsque le code qu'on veut donner au cours violera la contrainte d'unicite du code 
	 * 		du code
	 * 	-1 lorsque la classe, la matiere ou le prof associe au cours n'existe pas encore
	 ******************************************************************************/
	//public int updateCours(Cours cours, Long idMatiereAssocie, Long idProfAssocie, Long idClasseAssocie);

	/**************************************************************************
	 * Methode qui supprime un cours de la base de donnée
	 * Elle retourne 
	 * 	1 lorsque le cours est supprimé avec succès
	 * 	0 lorsque le cours contient déjà des évaluations 
	 * 	-1 si le cours n'existe pas dans la base de donnée
	 */
	//public int deleteCours(Long idCours);

	/**********************************************************************************************
	 * Méthode qui retourne la liste des séquences enregistrés dans le système page par page
	 * On doit afficher ces séquences 6 à 6 ainsi on va afficher chaque fois les 6 séquences d'une année à la fois
	 ***********************************************************************************************/
	public Page<Sequences> findAllSequences(int numPage, int taillePage);

	/**************************************************************************
	 * Methode qui change l'état d'une Sequence. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si la sequence passe à l'état true
	 * 	1 si la sequence passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 */
	//public int swicthEtatPeriodesSeq(Long idPeriode);

	/**********************************************************************************************
	 * Méthode qui retourne la liste des trimestres enregistrés dans le système page par page
	 ***********************************************************************************************/
	public Page<Trimestres> findAllTrimestres(int numPage, int taillePage);

	/**********************************************************************************************
	 * Méthode qui retourne la liste des annee enregistrés dans le système page par page
	 * On doit afficher ces séquences 1 à 1 
	 ***********************************************************************************************/
	public Page<Annee> findAllAnnee(int numPage, int taillePage);

	/**************************************************************************
	 * Methode qui change l'état d'un Trimestre. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si le trimestre passe à l'état true
	 * 	1 si le trimestre passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 */
	//public int swicthEtatPeriodesTrim_(Long idPeriode);

	/**************************************************************************
	 * Methode qui change l'état d'une AnneeScolaire. les états possible sont true ou false
	 * Elle retourne 
	 * 	2 si l'année scolaire passe à l'état true
	 * 	1 si l'année scolaire passe à l'état false
	 * 	0 si une erreur se produit lors du changement d'état
	 */
	//public int swicthEtatPeriodesAnnee_(Long idPeriode);

	/***************************************************************************
	 * Methode permettant d'attribuer un titulaire à une classe. cette méthode octroit le 
	 * role titulaire à l'enseignant désigné comme titulaire de classe le donnant ainsi la 
	 * possibilité d'imprimer les bulletins des classes pour laquelle il est titulaire. 
	 * Elle prend en paramètre l'identifiant de la classe dont on veut attribuer le titulaire et 
	 * l'identifiant du prof qui doit être titulaire
	 * Elle retourne 
	 * 		1 si la méthode s'est bien exécutée
	 * 		0 si le rôle TITUALIRE que doit désormais avoir le prof indiqué n'existe pas
	 * 	   -1 sinon
	 */
	//public int setTitulaireClasse(Long idClasseConcerne, Long idProfTitulaire);

	/*********************************************************************
	 * Methode qui retourne l'année active enregistré dans la base de donnée
	 * puisqu'on sait qu'au plus une année ne peut être activé dans la BD à la fois
	 */
	public Annee findAnneeActive();

	/**********************************************************************************************
	 * Méthode qui retourne la liste des trimestres d'une année specifié enregistrés dans le système page par page
	 ***********************************************************************************************/
	public Page<Trimestres> findAllTrimestresAnnee(int numPage, int taillePage, Long idAnnee);

	/**********************************************************************************************
	 * Méthode qui retourne la liste des trimestres active d'une année specifié enregistrés 
	 * dans le système page par page
	 ***********************************************************************************************/
	public Page<Trimestres> findAllTrimestresActiveAnnee(int numPage, int taillePage, 
			boolean etatPeriode,	Long idAnnee);

	
	/**********************************************************************************************
	 * Méthode qui retourne la liste de tous les trimestres d'une année specifié enregistrés dans le système
	 ***********************************************************************************************/
	public List<Trimestres> findAllTrimestresAnnee(Long idAnnee);

	/**********************************************************************************************
	 * Méthode qui retourne la liste de tous les trimestres actives d'une année specifié enregistrés 
	 * dans le système
	 ***********************************************************************************************/
	public List<Trimestres> findAllTrimestresActiveAnnee(boolean etatPeriode,	Long idAnnee);

	
	/**********************************************************************************************
	 * Méthode qui retourne la liste des cours d'une classe specifié enregistrés  page par page
	 ***********************************************************************************************/
	public Page<Cours> findAllCoursClasse(int numPage, int taillePage, Long idClasses);

	/**********************************************************************************************
	 * Méthode qui retourne la liste des cours qu'un prof enseigne dans une classe specifié  page par page.
	 * l'identifiant de la classe et du prof sont passés en paramètre
	 ***********************************************************************************************/
	public Page<Cours> findAllCoursProfClasse(int numPage, int taillePage, Long idClasses, Long idProf);

	/**********************************************************************************
	 * Methode qui retourne la liste des évaluations pour un cours dans une séquence donnée
	 * On aura un maximun de 2 évaluations pour chaque séquence avec pour chacune sa proportion 
	 * dans la note finale.
	 */
	public List<Evaluations> findAllEvalCoursSeq(Long idCours, Long idSeq);

	/**********************************************************************
	 * Methode qui retourne une séquence dont l'identifiant est passé en paramètre
	 */
	public Sequences findSequences(Long idPeriodes);

	/**********************************************************************
	 * Methode qui retourne un trimestre dont l'identifiant est passé en paramètre
	 */
	public Trimestres findTrimestres(Long idPeriodes);
	
	/**********************************************************************
	 * Methode qui retourne une annee dont l'identifiant est passé en paramètre
	 */
	public Annee findAnnee(Long idPeriodes);

	/*****************************************************************************************************
	 * Methode permettant d'enregistrer ou de mettre à jour une évaluations pour un cours dans une séquence donnee
	 * 
	 * En effet, dans une séquence on peut faire au plus 2 évaluations (CC ou DS) chacune avec son pourcentage. On doit 
	 * maintenant s'arranger pour que les deux évaluations d'un cours dans une séquence fasse 100%. Pour cela lorsqu'on
	 * modifie ou enregistre une évaluation, on doit rechercher son associé(l'evaluation de l'autre type dans la sequence) 
	 * et l'affecter la proportion 100-celle de l'évaluation
	 * qu'on vient d'enregistrer. 
	 * Si les deux existe déjà la methode retournera 1 sans souci pour indiquer que tous est bien.
	 * 
	 * retourne 1 si tout s'est bien passé
	 * 				0 sinon
	 */
	public int saveEvaluation (String contenuEval, Cours coursEval, Date dateEval, int proportionEval, Sequences seqEval, String typeEval);

	/******************************
	 * Cette fonction met à jour la proportion d'une évaluation et retourne un entier positif lorsque tout 
	 * s'est bien passé. Entre temps, une évaluation peut être associe a une autre dans la meme séquence
	 * par exple dans le cas ou deux evaluations ont été enregistré pour le même cours dans la même 
	 * séquence. Donc cette fonction va du même coup se rassurer si une evaluation associe existe et la
	 * mettre aussi a jour directement afin que la somme des proportions des deux evaluations ne 
	 * soit pas superieur a 100
	 * 
	 * elle retourne 1 si tout s'est bien passé et qu'aucune evaluation associe n'existe
	 * 							2 si tout s'est bien passé et que l'évaluation associe a été mis a jour du meme coup
	 * 							-1 si une erreur s'est produite pendant la mise à jour
	 */
	public int updateProportionEvaluation(Long idEval, int new_proportion);
	
	/*************************************************************************************************
	 * Cette methode retourne l'évaluation qui correspond à un cours, une séquence et un type d'évaluation donné
	 * Sachant qu'un cours passe dans une classe et une seule. 
	 * elle retourne null si aucune évaluation de ce type n'est encore enregistré pour ce cours dans la séquence
	 */
	public Evaluations findEvaluations(Long idCours, Long idSequence, String typeEval);

	/*************************************************************************************************
	 * Cette methode retourne l'évaluation associe à un idEval qui est passe en paramètre
	 * Elle retourne nul si aucune évaluation ne correspond à cet idEval passe en paramètre
	 */
	public Evaluations findEvaluations(Long idEval);

	/************************************************************
	 * Retourne la liste de tous les eleves page par page d'une classe dont
	 * l'idClasse est passé en paramètre et ont déjà commencé leur inscription 
	 * ie que leur état d'inscription est à "en cours" ou à "inscrit". 
	 * Elle retourne une liste null si aucun eleve n'est enregistré dans cette
	 * classe dans la BD
	 ************************************************************/
	public Page<Eleves> findPageElevesClasse(Long idClasses,	String etatInscription, int numPage,	int taillePage);

	/************************************************************
	 * Retourne la liste de tous les eleves d'une classe dont
	 * l'idClasse est passé en paramètre. 
	 * Elle retourne une liste null si aucun eleve n'est enregistré dans cette
	 * classe dans la BD
	 ************************************************************/
	public List<Eleves> findListElevesClasse(Long idClasses, String etatInscription);

	/***************************************************************
	 * Methode qui retourne le numero d'un élève parmi une liste d'élève déjà trié
	 * par ordre alphabétique des noms, prénoms et date de naissance.
	 * Elle prend en paramètre l'identifiant de l'élève dont on veut le numéro
	 * La methode retourne 0 si l'élève en question n'est pas dans la liste
	 */
	public int getNumeroEleve(List<Eleves> listofEleve, Long idEleve);

	/***************************************************************
	 * Methode qui retourne le numero d'un élève parmi les élèves de sa classe.
	 * Elle prend en paramètre l'identifiant de l'élève dont on veut le numéro
	 * La methode retourne 0 si l'élève en question n'est pas dans la liste
	 */
	public int getNumeroEleve(Long idEleve);

	/***************************************************************
	 * Methode qui retourne l'éffectif des élèves enregistré dans la classe de
	 * l'élève dont l'id est passé en paramètre. tous les élèves sont compte meme 
	 * s'il n'ont pas payé de scolarite
	 */
	public int getEffectifClasse(Long idEleve);

	/***********************************************************************
	 * Methode qui retourne l'élève qui suit ou qui précède directement celui dont 
	 * l'id sera passé en paramètre.
	 * elle retourne null si aucun élève ne suit ou aucun ne précède
	 * 
	 * mode ==0 si on veut l'élève qui précède celui dont l'id est passé en paramètre
	 *  mode ==1 si on veut l'élève qui suit celui dont l'id est passé en paramètre
	 */
	public Eleves getElevesATraiter(Long idEleve,  int mode);

	/***************************************************************
	 * Methode qui retourne l'élève qui est à une certaine position de la liste. 
	 * la position sera aussi passé en paramètre de la méthode. 
	 * La méthode retourne null si aucun élève ne peut être trouve à la position indiquée
	 * 
	 * La position passé en paramètre doit être comprise entre 1 et la taille exacte de la liste. 
	 * Donc puisque dans la liste les elts commencent à partir de 0 on va donc concrètement 
	 * retourner l'item à la position pos - 1; Donc au départ pos doit etre > 0  et <= liste.size
	 */
	public Eleves findEleveDansListe(List<Eleves> listofEleve, int pos);

	/***************************************************************************************
	 * Methode qui enregistre la note d'un élève à une évaluation
	 * Elle prend en paramètre la valeur de la note, l'identifiant de l'évaluation et l'identifiant de l'élève
	 * 
	 * Elle retourne 1 si tout s'est bien passé 
	 * 						0 si la note envoyé n'est pas entre 0 et 20
	 * 						-1 en cas de toutes autres erreurs: convertion des chaines en double ou int et autre
	 */
	public int saveNoteEvalEleve(Long idEval, Long idEleves, double valNote);

	/**********************************************************************************************
	 * Methode qui enregistre le rapport des heures d'absence d'un éleve pour une séquence donnée
	 * l'identifiant de l'élève, de la sequence sont pris en paramètre. Les heures d'absences justifiés et non justifiés
	 * constituant le rapport des absences et aussi pris en paramètre.
	 * @param idEleves
	 * @param idSequence
	 * @param nbreHJ
	 * @param nbreHNJ
	 * @return
	 * 
	 * Elle retourne 1 si tout s'est bien passé
	 * 							 0 si les heures justifies sont superieur au heure non justifie car on ne peut justifie ce qu'on
	 * a pas eu
	 * 						    -1 si les heures d'absence saisies sont négatif
	 * 						    -2 si la date_enreg est ultérieure à la date du jour
	 * 						    -3 en cas de toutes autres erreurs
	 */
	//public int saveRAbsenceSeqEleve(Long idEleves, Long idSequence, int nbreHJ, int nbreHNJ, Date date_enreg);


	/**********************************************************************************************************
	 * Methode permettant d'enregistrer une rapport disciplinaire pour un élève dans une séquence. 
	 * Un rapport disciplinaire est lie a une sanction disciplinaire et cette sanction est infligée pour un 
	 * motif.  Elle peut s'exprimer en un nombre de periode avec une unite en heure ou en jour.
	 * @param idEleves
	 * @param idSequence
	 * @param date_enreg
	 * @param nbreperiode
	 * @param unite
	 * @param motif
	 * @param idSanctionDisc
	 * @return
	 * 
	 * Elle retourne 1 si tout s'est bien passe
	 * 							0 si nbreperiode est positif et unite vide
	 * 							-1 si le nbreperiode est négatif
	 * 							-2 si la date_enreg est ultérieure à la date du jour
	 * 							-3 dans tous les autres cas
	 */
	/*public int saveRDisciplineSeqEleve(Long idEleves, Long idSequence, Date date_enreg, int nbreperiode, 
			String unite, String motif, Long idSanctionDisc);*/
	
	/*************************************************************
	 * Cette methode supprime un rapport disciplinaire enregistrée
	 * @param idRdisc
	 * @return
	 */
	//public int deleteRapportDisciplinaire(Long idRdisc);
	
	
	/*************************************************************
	 * Methode qui retourne la NotesEval d'un élève à une évaluations.
	 * Elle prend donc en paramètre l'idEleves et l'idEval
	 * Elle retourne null si aucune noteEval n'est encore enregistré pour cet élève à cette évaluation
	 */
	public NotesEval findNotesEvalElevesEval(Long idEleves, Long idEval);

	/******************************************************************
	 * Methode qui retourne le RapportDabsence d'un élève pour une séquence
	 * Elle prend en paramètre l'idEleve et l'idSequence et 
	 * retourne null si aucun rapport n'est encore enregistré pour cet élève à cette 
	 * séquence. 
	 * @param idEleves
	 * @param idSequence
	 * @return
	 */
	public RapportDAbsence findRapportDAbsenceSeqEleves(Long idEleves, Long idSequence);

	/*****************************************************************************************
	 * Methode qui retourne le numero de la page ou se trouve l'élève dont l'id est passé en paramètre 
	 * parmi les élèves de sa classe. 
	 * Elle prend donc en paramètre l'id de l'élève et la taille d'affichage des pages
	 * elle retourne -1 si l'élève ne se trouve pas dans la liste
	 */
	public int getNumeroPageEleve(Long idEleves, int taillePage);

	/********************************************************************************************
	 * Methode qui va fixer le montant de la scolarité de chaque classe. Elle prend en paramètre l'id de la classe
	 * à modifier et le montant de la scolarité qu'on désire fixer
	 * 	Elle retourne 1 si tout s'est bien passé
	 * 							0 si la classe n'existe pas
	 * 							-1 si le montant n'est pas un double
	 * 							-2 en cas d'autres erreurs
	 */
	//public int setMontantScoClasse_(Long idClasseAConfig, double montantScolarite);

	/************************************************************************************************
	 * Methode qui retourne la liste des cours qui passe dans une classe dont l'identifiant sera passé en 
	 * paramètre. 
	 * Cette methode retourne null si aucun cours n'est encore enregistré pour la classe
	 */
	public List<Cours> getListCoursClasse(long idClasseConcerne);

	/********************************************************************************************************
	 * Methode qui retourne une map(un dictionnaire) qui pour chaque cours (idCours) associe sa liste d'evaluation dans la 
	 *séquence prise en paramètre
	 */
	public Map<Long, List<Evaluations>> getMapEvalAllCoursClasseSeq(long idClasseConcerne, long idSequenceConcerne);

	/********************************************************************************************************
	 * Methode qui retourne une liste contenant toutes les évaluations de tous les cours d'une séquence donnée
	 */
	public List<Evaluations> getListEvalAllCoursClasseSeq(long idClasseConcerne, long idSequenceConcerne);



	/************************************************************************************************
	 * Methode qui retourne la liste des classes dirigées par l'utilisateur dont l'identifiant  est passe en paramètre
	 * Cette methode retourne null si aucune classe n'est dirigée par l'utilisateur dont l'identifiant est passe en paramètre
	 */
	public List<Classes> getListClassesDirige(long idUsers);

	

	/*************************************************************************************************
	 * Cette methode retourne la liste de tous les cours scientifique qui passe dans une classe. 
	 * Elle prend en parametre l'id la classe concerne
	 * 
	 */
	public List<Cours> findListofCoursGrp1(Long idClasse);

	/*************************************************************************************************
	 * Cette methode retourne la liste de tous les cours littéraire qui passe dans une classe. 
	 * Elle prend en parametre l'id la classe concerne
	 * 
	 */
	public List<Cours> findListofCoursGrp2(Long idClasse);

	/*************************************************************************************************
	 * Cette methode retourne la liste de tous les cours divers qui passe dans une classe. 
	 * Elle prend en parametre l'id la classe concerne
	 * 
	 */
	public List<Cours> findListofCoursGrp3(Long idClasse);

	public Collection<PV_SequenceBean> generatePVSequence(Long idClasse,	Long idCours, Long idSequence);
	
	public Collection<PV_NoteBean> generatePVEvalAvecListeNote(List<NotesEval> listofNotesEvalSeq);

	public Collection<PV_TrimestreBean> generatePVTrimestre(Long idClasse,	Long idCours, Long idTrimestre);
	
	public Collection<EleveBean2> generateReleveNote(Long idClasse);
	
	public List<NotesEval> getListofnotesEvalDeSeq(Long idEleve, Long idSequence);
	
	/*************************************************************************************************
	 * Cette methode retourne une collection qui contiendra les bulletins d'une séquence des élèves d'une 
	 * classe dont l'identifiant de la classe et de la séquence sont passé en paramètre. 
	 * Celle semble plus optimisé et met moins de temps pour l'exécution
	 * @param idClasse
	 * @param idSequence
	 * @return
	 */
	//public Collection<BulletinSequenceBean> generateCollectionofBulletinSequence_opt(Long idClasse, Long idSequence);
	//public Map<String, Object> generateCollectionofBulletinSequence_opt(Long idClasse, Long idSequence);
	
	/*************************************************************************************
	 * Cette methode retourne une collection qui va contenir un seul bulletin séquentiel. 
	 * Il s'agit du bulletin de l'élève dont l'id est passe en parametre. La classe qui passe en
	 * paramètre nous permettra de se rassurer simplement que l'élève se trouve dans la classe
	 * indiquée.
	 * @param idEleve
	 * @param idClasse
	 * @param idSequence
	 * @return
	 */
	//public Collection<BulletinSequenceBean> generate1BulletinSequence(Long idEleve, Long idClasse, Long idSequence);
	
	/*********************************************************************************************************
	 * Cette methode retourne un dictionnaire qui aura n+1 clé où les n premières cles est associe chacune 
	 * à la liste des élèves ayant eu la sanction i. En effet, si on a 5 niveau de severite dans les sanctions alors
	 * sanction_i est associe a la liste des eleves ayant eu la sanction de niveau de severite i aucours de la 
	 * sequence. 
	 * la dernière cles (absence) sera associe à la liste des élèves rangés dans l'ordre décroissant des nombres
	 * d'heure d'absence non justifié. En effet, au cas où les précédentes listes sont vide, les plus indiscipline
	 * correspondront a ceux qui ont le plus grand nombre d'heure d'absence non justifié. 
	 * @param classe
	 * @param sequence
	 * @return
	 */
	public Map<String, Object> getListeEleveParSanctionDiscSeq(Classes classe, Sequences sequence);
	
	
	/*********************************************************************************************************
	 * Cette methode retourne un dictionnaire qui aura n+1 clé où les n premières cles est associe chacune 
	 * à la liste des élèves ayant eu la sanction i. En effet, si on a 5 niveau de severite dans les sanctions alors
	 * sanction_i est associe a la liste des eleves ayant eu la sanction de niveau de severite i aucours du 
	 * trimestre. 
	 * la dernière cles (absence) sera associe à la liste des élèves rangés dans l'ordre décroissant des nombres
	 * d'heure d'absence non justifié. En effet, au cas où les précédentes listes sont vide, les plus indiscipline
	 * correspondront a ceux qui ont le plus grand nombre d'heure d'absence non justifié. 
	 * @param classe
	 * @param trimestre
	 * @return
	 */
	public Map<String, Object> getListeEleveParSanctionDiscTrim(Classes classe, Trimestres trimestre);
	
	/*********************************************************************************************************
	 * Cette methode retourne un dictionnaire qui aura n+1 clé où les n premières cles est associe chacune 
	 * à la liste des élèves ayant eu la sanction i. En effet, si on a 5 niveau de severite dans les sanctions alors
	 * sanction_i est associe a la liste des eleves ayant eu la sanction de niveau de severite i aucours de 
	 * l'annee. 
	 * la dernière cles (absence) sera associe à la liste des élèves rangés dans l'ordre décroissant des nombres
	 * d'heure d'absence non justifié. En effet, au cas où les précédentes listes sont vide, les plus indiscipline
	 * correspondront a ceux qui ont le plus grand nombre d'heure d'absence non justifié. 
	 * @param classe
	 * @param trimestre
	 * @return
	 */
	public Map<String, Object> getListeEleveParSanctionDiscAnnee(Classes classe, Annee an);
	
	
	/***********************************************************************************************************
	 * Cette methode se charge d'appeler la fonction getListeEleveParSanctionDiscSeq qui retourne une map
	 * avec autant de cle que de sanction pour fabriquer une liste de n personnes les plus indiscipline d'une
	 * classe Pendant une séquence
	 * @param classe
	 * @param sequence
	 * @return
	 */
	public List<Eleves> getListeElevePlusIndisciplineSeq(Classes classe, Sequences sequence, int n);
	
	/***********************************************************************************************************
	 * Cette methode se charge d'appeler la fonction getListeEleveParSanctionDiscSeq qui retourne une map
	 * avec autant de cle que de sanction pour fabriquer une liste de n personnes les plus indiscipline d'une
	 * classe pendant un trimestre
	 * @param classe
	 * @param sequence
	 * @return
	 */
	public List<Eleves> getListeElevePlusIndisciplineTrim(Classes classe, Trimestres trimestre, int n);
	
	/***********************************************************************************************************
	 * Cette methode se charge d'appeler la fonction getListeEleveParSanctionDiscSeq qui retourne une map
	 * avec autant de cle que de sanction pour fabriquer une liste de n personnes les plus indiscipline d'une
	 * classe pendant une annee
	 * @param classe
	 * @param sequence
	 * @return
	 */
	public List<Eleves> getListeElevePlusIndisciplineAnnee(Classes classe, Annee an, int n);
	
	
	/*********************************************************************************************************
	 * Cette methode retourne la liste des eleves d'une classe ayant eu la sanction disciplinaire passe en
	 * parametre pendant une séquence. Cette liste est dans l'ordre alphabetique des noms puis des prenoms
	 * des eleves de la dite classe.
	 * @param classe
	 * @param sanctionDisc
	 * @param sequence
	 * @return
	 */
	public List<Eleves> getListEleveSanctionDiscSeq(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Sequences sequence);
	
	/*********************************************************************************************************
	 * Cette methode retourne la liste des eleves d'une classe ayant eu la sanction disciplinaire passe en
	 * parametre pendant un trimestre. Cette liste est dans l'ordre alphabetique des noms puis des prenoms
	 * des eleves de la dite classe.
	 * @param classe
	 * @param sanctionDisc
	 * @param trimestre
	 * @return
	 */
	public List<Eleves> getListEleveSanctionDiscTrim(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Trimestres trimestre);
	
	/*********************************************************************************************************
	 * Cette methode retourne la liste des eleves d'une classe ayant eu la sanction disciplinaire passe en
	 * parametre pendant une annee. Cette liste est dans l'ordre alphabetique des noms puis des prenoms
	 * des eleves de la dite classe.
	 * @param classe
	 * @param sanctionDisc
	 * @param an
	 * @return
	 */
	public List<Eleves> getListEleveSanctionDiscAnnee(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Annee an);
	
	
	/***************************************************************************************
	 * Calcule et génère la fiche simplifier de conseil de classe séquentiel. Cette fiche est généré 
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
	 * @param profPrincipal
	 * @return
	 */
	/*public FicheConseilClasseBean getRapportConseilClasseSequentiel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			Sequences sequence, List<Eleves> listofElevesOrdreDecroissantMoyenneSequentiel);*/
	
	/*************************************************************************************
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
	/*public FicheConseilClasseBean getRapportConseilClasseTrimestriel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			Trimestres trimestre, List<Eleves> listofElevesOrdreDecroissantMoyenneTrimestriel);*/
	
	/********************************************************************************
	 * 
	 * @param etab
	 * @param annee
	 * @param classe
	 * @param tauxReussite
	 * @param moyenne_general
	 * @param trimestre
	 * @param listofElevesOrdreDecroissantMoyenneAnnuel
	 * @return
	 */
	/*public FicheConseilClasseBean getRapportConseilClasseAnnuel(Etablissement etab, 
			Annee annee, Classes classe , double tauxReussite, double moyenne_general,	
			List<Eleves> listofElevesOrdreDecroissantMoyenneAnnuel);*/
	
	/********************************************************************
	 * Cette methode genere la liste des EleveBean qui represente la liste des élèves 
	 * par classe.
	 * @param idClasse
	 * @return
	 */
	public Collection<EleveBean> generateCollectionofEleveprovClasse(Long idClasse);
	
	/****************************************************************************
	 * Cette methode retourne la liste des élèves qui ont deja paye au moins le montant min
	 * @param idClasse
	 * @param critere
	 * @return
	 */
	public Collection<EleveBean> generateCollectionofElevedefClasse(Long idClasse, double montantMin);
	
	/***************************************************************
	 * Cette methode retourne la liste des membres du personnel
	 * de l'établissement sans distinction de fonction
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofPersonnelBean_();
	
	/**********************************************************************
	 * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction CENSEUR dans l'établissement. 
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofCenseurBean();
	
	public Collection<ErrorBean> generateCollectionofErrorBean(String error_msg);
	
	/**********************************************************************
	 * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction SG dans l'établissement. 
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofSgBean();
	
	/**********************************************************************
	 * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction ENSEIGNANT dans l'établissement. 
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofEnseignantBean();
	
	/**********************************************************************
	 * Cette methode retourne la liste des membres du personnel
	 * ayant la fonction INTENDANT dans l'établissement. 
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofIntendantBean();
	
	/**********************************************************************
	 * Cette methode retourne la liste des personnel d'appui dans l'établissement. 
	 * @return
	 */
	public Collection<PersonnelBean> generateCollectionofPersonnelDAppuiBean();
	
	/*********************************************************************************************
	 * Cette methode retourne la liste de tous les personnels  enregistré dans le système avec le statut
	 * passe en parametre
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofPersonnelDeStatutBean_(String statutPers);
	
	/*********************************************************************************************
	 * Cette methode retourne la liste de tous les proffesseurs  enregistré dans le système avec le statut
	 * passe en parametre
	 * @return
	 */
	//public Collection<PersonnelBean> generateCollectionofProffesseursDeStatutBean_(String statutPers);
	
	
	/*******************************************************************************************
	 * Cette methode calcule et génère tous les bulletins trimestriel d'une classe pour affichage. Il ne les 
	 * enregistre pas en base de données. Donc en fait toutes les classes prévues pour enregistrer les 
	 * bulletins doivent être supprimé puisqu'un bulletin doit toujours etre généré à la volée. A chaque 
	 * fois qu'on demande un bulletin il faut le regénéré puisque les notes peuvent avoir changé.
	 * Cette fonction ci est quel peut plus optimisée
	 * @param idClasse
	 * @param idTrimestre
	 * @return
	 */
	/*public Collection<BulletinTrimestreBean> generateCollectionofBulletinTrimestre_opt(Long idClasse, 
			Long idTrimestre);*/
	/* public Map<String, Object> generateCollectionofBulletinTrimestre_opt(Long idClasse, 
				Long idTrimestre);*/
	
	 /*********************************************************************************************************
	  * Cette methode retourne le bulletin d'un eleve dans un trimestre. l'identifiant de l'élève et du trimestre
	  * sont passe en paramètre. 
	  * @param idEleve
	  * @param idClasse
	  * @param idTrimestre
	  * @return
	  */
	 /*public Collection<BulletinTrimestreBean> generate1BulletinTrimestre(Long idEleve, Long idClasse, 
			 Long idTrimestre);*/
	 

	/*******************************************************************************************
	 * Cette methode calcule et génère tous les bulletins annuels d'une classe pour affichage. Il ne les 
	 * enregistre pas en base de données. Donc en fait toutes les classes prévues pour enregistrer les 
	 * bulletins doivent être supprimé puisqu'un bulletin doit toujours etre généré à la volée. A chaque 
	 * fois qu'on demande un bulletin il faut le regénéré puisque les notes peuvent avoir changé, les 
	 * heures d'absences, les noms des enseignants, le titulaire de la classe et autres.
	 * Cette fonction ci est quel peut plus optimisée
	 * @param idClasse
	 * @param idAnnee
	 * @return
	 */
	/*public Collection<BulletinAnnuelBean> generateCollectionofBulletinAnnee(Long idClasse,
			Long idAnnee);*/
	/*public  Map<String, Object> generateCollectionofBulletinAnnee(Long idClasse,
			Long idAnnee);*/
	
	/***********************************************************************************************************
	 * Cette methode retourne le bulletin Annuel de l'élève dont l'id est passe en parametre pour le compte
	 * de l'année dont l'id  est passe en paramètre
	 * @param idEleve
	 * @param idClasse
	 * @param idAnnee
	 * @return
	 */
	 /*public Collection<BulletinAnnuelBean> generate1BulletinAnnuel(Long idEleve, Long idClasse, 
			 Long idAnnee);*/
	 
	
	/******************************************************************************************************
	 * Cette methode retourne la liste des bulletins trimestriels de tous les eleves contenus dans la classe dont 
	 * l'identifiant est passé en paramètre. Pour chaque bulletin trimestriel, on aura dessus un rapport annuel
	 * montrant l'incidence de la moyenne trimestriel sur la moyenne annuel. 
	 * @param idClasse
	 * @param idTrimestre
	 * @return
	 */
	/*public Collection<BulletinTrimAnnuelBean> generateCollectionofBulletinTrimAnnee(Long idClasse,
	Long idAnnee);*/
	/*public  Map<String, Object> generateCollectionofBulletinTrimAnnee(Long idClasse,
		Long idTrimestre);*/
	
	/**********************************************************************************************************
	 * Cette methode retourne le bulletin trimestriel d'un eleve avec son  rapport annuel. 
	 * @param idEleve
	 * @param idClasse
	 * @param idTrimestre
	 * @return
	 */
	/*public Collection<BulletinTrimAnnuelBean> generate1BulletinTrimAnnuel(Long idEleve, Long idClasse, 
			Long idTrimestre);*/
	 
	
	public double getValeurNotesFinaleEleve(Long idEleve, Long idCours, Long idSequence);
	
	public int getNbreNoteDansCourspourSeq(Long idClasse, Long idCours, 
			Long idSequence);
	
	public int getNbreNoteDansCourspourEvalDansListe(List<NotesEval> listofNotesEvalSeq);
	
	public int getNbreSousNoteDansCourspourEvalDansListe(List<NotesEval> listofNotesEvalSeq);
	
	public int getNbreSousNoteDansCourspourSeq(Long idClasse, Long idCours, 
			Long idSequence);
	
	public int getNbreNoteDansCourspourTrim(Long idClasse, Long idCours, 
			Long idTrimestre);
	
	public int getNbreSousNoteDansCourspourTrim(Long idClasse, Long idCours, 
			Long idTrimestre);
	
	
	/*******
	 * Cette fonction converti les nombres qui ont au plus 2 chiffres en toutes lettres
	 * dans la langue française.
	 * @param nbre
	 * @param accord
	 * @return
	 */
	public String ecritEnLettreNombreDeux9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreTrois9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreQuatre9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreCinq9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreSix9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreNeuf9(long nbre, boolean accord);
	
	public String ecritEnLettreNombreDouze9(long nbre, boolean accord);
	
	/*****************************
	 * Cette fonction recupère n'importe quel nombre ecrit en chiffre et le converti en 
	 * toutes lettres dans la langue française en se servant des fonctions auxilliaires 
	 * ci-dessus.
	 * @param nbre
	 * @param accord
	 * @return
	 */
	public String ecritEnLettreNombrePlusDeDouze9(long nbre, boolean accord);
	
	/**********************************
	 * Cette fonction converti les nombres qui ont au plus 2 chiffres en toutes lettres
	 * dans la langue anglaise
	 * @param nbre
	 * @return
	 */
	public String writeInLetterNumberTwo9(long nbre);
	
	public String writeInLetterNumberThree9(long nbre);
	
	public String writeInLetterNumberFour9(long nbre);
	
	public String writeInLetterNumberFive9(long nbre);
	
	public String writeInLetterNumberSix9(long nbre);
	
	public String writeInLetterNumberNine9(long nbre);
	
	public String writeInLetterNumberTwelve9(long nbre);
	
	/********************************************************
	 *  Cette fonction recupère n'importe quel nombre ecrit en chiffre et le converti en 
	 *  toutes lettres en langue anglaise en se servant des fonctions auxilliaires ci-dessus.
	 * @param nbre
	 * @return
	 */
	public String writeInLetterNumberOverTwelve9(long nbre);
	
	/*******************************************************************
	 * Cette fonction retourne les opérations financière operer entre 
	 * la datemin et la datemax passé en paramètre. Il les retourne 
	 * page par page selon la taille d'une page passé en paramètre 
	 * @param datemin
	 * @param datemax
	 * @param taillePage
	 * @return
	 */
	public Page<Operations> findPageOperations(Date datemin, Date datemax,	int numPage, int taillePage);
	
	/**********************************************************
	 * Cette fonction retourne la liste de toutes les opérations
	 * effectue entre les deux date indiquée
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public List<Operations> findListAllOperations(Date datemin, Date datemax);
	
	/*************************
	 * Cette fonction retourne une opération dont l'id est passe en paramètre
	 * @param idOperation
	 * @return
	 */
	public Operations findOperation(long idOperation);
	
	/******************************************
	 * cette fonction calcule la somme de tous les montants encaissé pour chacune des 
	 * opérations contenues dans la liste des opérations passé en paramètre
	 * @param listOperation
	 * @return
	 */
	public double calculMontantTotalListOperation (List<Operations> listOperation);
	
	/*************************************************************
	 * Cette methode retourne une collection contenant les opérations de versement réalisées entre 
	 * la datemin et la datemax
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public Collection<OperationBean> generateListOperation(Date datemin, Date datemax);
	
	
	public List<Operations> findListAllOperationsEleve(Long idEleve);
	
	/***************************************************************************
	 * Cette methode retourne la collection contenant les opérations de versement effectués
	 * dans le système pour le compte de l'élève dont l'identifiant est passé en paramètre. 
	 * @param idEleve
	 * @return
	 */
	public Collection<OperationBean> generateListOperationEleve(Long idEleve);
	
	/**************************************************
	 * Cette methode retourne la liste des sanctions disciplinaire existant dans le système dans l'ordre
	 * croissant des niveau de severite
	 * @return
	 */
	public List<SanctionDisciplinaire> findListAllSanctionDisciplinaire();
	
	/**************************************************
	 * Cette methode retourne la liste des sanctions disciplinaire existant dans le système dans l'ordre
	 * decroissant des niveau de severite
	 * @return
	 */
	public List<SanctionDisciplinaire> findListAllSanctionDisciplinaire_DESC();
	
	public SanctionDisciplinaire findSanctionDisciplinaire(Long idSanctionDisc);
	
	/**************************************************
	 * Cette methode retourne la liste des sanctions travail existant dans le système
	 * @return
	 */
	public List<SanctionTravail> findListAllSanctionTravail();
	
	public SanctionTravail findSanctionTravail(Long idSanctionTrav);
	
	/**************************************************
	 * Cette methode retourne la liste des decision existant dans le système
	 * @return
	 */
	public List<Decision> findListAllDecision();
	
	/*************************************************************************************************
	 * Cette methode retourne la liste des niveaux contenant les classes dans lesquelles on peut 
	 * placer un élève après un conseil de classe annuel. Il s'agit d'abord du niveau contenant la 
	 * classe prise en parametre mais aussi du niveau imediatement supérieur à celui dans lequel se 
	 * trouve la classe prise en paramètre. 
	 * @param classe
	 * @return
	 */
	public List<Niveaux> findListNiveauSup(Classes classe);
	
	/*************************************************************************************
	 * Cette methode retourne la decision du conseil de classe d'un élève pendant une 
	 * periode donnee (sequence, trimestre ou annee). Si aucune n'est encore enregistrée, 
	 * elle retourne null
	 * @param idEleves
	 * @param idPeriode
	 * @return
	 */
	public DecisionConseil findDecisionConseilPeriode(Long idEleves, Long idPeriode);
	
	/*********************************************************************************
	 * Cette methode enregistre en BD la decision prise pendant un conseil de classe 
	 * séquentiel pour un élève. 
	 * @param idEleves
	 * @param idSequence
	 * @param idSanctionDisc
	 * @param nbreperiode
	 * @param unite
	 * @param idSanctionTravAssocie
	 * @return
	 * 
	 * 		Cette methode retourne -1 si l'eleve ou la séquence ou alors des sanction associe n'ont pas été trouve
	 * 													  0 si les paramètres nbreperiode et unite ne sont pas coherent 
	 * (nbreperiode>0 et unite=RAS ou alors nbreperiode<0)
	 * 													  1 si tout s'est bien passé et la décision du conseil bien enregistré
	 * 														
	 */
	public int saveDecisionConseilSeq(Long idEleves, Long idSequence, Long idSanctionDisc, int nbreperiode,
			String unite, Long idSanctionTravAssocie);
	
	/*************************************************************************************************************
	 * Cette methode enregistre en BD la decision prise pendant le conseil de classe trimestriel pour un eleve.
	 * @param idEleves
	 * @param idTrimestre
	 * @param idSanctionDisc
	 * @param nbreperiode
	 * @param unite
	 * @param idSanctionTravAssocie
	 * @return
	 * 
	 * 
	 * 		cette methode retourne -1 si l'élève ou le trimestre ou alors des sanction associe n'ont pas été trouve		
	 * 													  0 si les paramètres nbreperiode et unite ne sont pas coherent 
	 * (nbreperiode>0 et unite=RAS ou alors nbreperiode<0)
	 * 													  1 si tout s'est bien passé et la décision du conseil bien enregistré
	 * 
	 */
	public int saveDecisionConseilTrim(Long idEleves, Long idTrimestre, Long idSanctionDisc, int nbreperiode,
			String unite, Long idSanctionTravAssocie);
	
	/***********************************************************
	 * Cette methode retourne la decision dont l'identifiant est passé en paramètre. 
	 * Si aucune décision n'est trouve, elle retourne null
	 * @param idDecision
	 * @return
	 */
	public Decision findDecision(Long idDecision);
	
	/***********************************************************************************************************
	 * Cette methode enregistre en BD la décision prise pendant le conseil de classe annuel pour un élève
	 * Elle enregistre du même coup la future classe dans laquelle l'élève sera enregistrer lors des migrations
	 * 
	 * @param idElevesConcerne
	 * @param idAnneeActive
	 * @param idsanctionTravAssocie
	 * @param idClasseFuturAssocie
	 * @param idDecisionAssocie
	 * @return
	 * 
	 * 		Cette methode retourne 0 si l'élève ou l'annee ou la sanctionTrav et ou la decision n'existe pas
	 * 													 1 si tout se passe bien
	 * 
	 */
	public int saveDecisionConseilAn(Long idElevesConcerne, Long idAnneeActive, 
			Long idsanctionTravAssocie, Long idClasseFuturAssocie, Long idDecisionAssocie);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans une classe entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si la classe est null alors il s'agit de toutes les classes qui sont dans la base de donnée
	 * @param classe
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeClasse(Classes classe, Date datemin, Date datemax, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans une classe dans une periodes pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si la classe est null alors il s'agit de toutes les classes qui sont dans la base de donnée
	 * @param classe
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeClasseSeq(Classes classe, Sequences periode, int sexe);
	public int getNbreAbsJSexeClasseTrim(Classes classe, Trimestres periode, int sexe);
	public int getNbreAbsJSexeClasseAn(Classes classe, Annee periode, int sexe);
	
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans une classe entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si la classe est null alors il s'agit de toutes les classes qui sont dans la base de donnée
	 * @param classe
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeClasse(Classes classe, Date datemin, Date datemax, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans une classe dans une periode pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si la classe est null alors il s'agit de toutes les classes qui sont dans la base de donnée
	 * @param classe
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeClasseSeq(Classes classe, Sequences periode, int sexe);
	public int getNbreAbsNJSexeClasseTrim(Classes classe, Trimestres periode, int sexe);
	public int getNbreAbsNJSexeClasseAn(Classes classe, Annee periode, int sexe);
	
	
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans un niveau entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si le niveau est null alors il s'agit de tous les niveaux qui sont dans la base de donnée
	 * @param niveau
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeNiveau(Niveaux niveau, Date datemin, Date datemax, int sexe);
	
	
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans un niveau dans une periode pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si le niveau est null alors il s'agit de tous les niveaux qui sont dans la base de donnée
	 * @param niveau
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeNiveauSeq(Niveaux niveau, Sequences periode, int sexe);
	public int getNbreAbsJSexeNiveauTrim(Niveaux niveau, Trimestres periode, int sexe);
	public int getNbreAbsJSexeNiveauAn(Niveaux niveau, Annee periode, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans un niveau entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si le niveau est null alors il s'agit de tous les niveaux qui sont dans la base de donnée
	 * @param niveau
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeNiveau(Niveaux niveau, Date datemin, Date datemax, int sexe);
	
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans un niveau dans une periode pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si le niveau est null alors il s'agit de tous les niveaux qui sont dans la base de donnée
	 * @param niveau
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeNiveauSeq(Niveaux niveau, Sequences periode, int sexe);
	public int getNbreAbsNJSexeNiveauTrim(Niveaux niveau, Trimestres periode, int sexe);
	public int getNbreAbsNJSexeNiveauAn(Niveaux niveau, Annee periode, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans un cycle entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si le cycle est null alors il s'agit de tous les cycles qui sont dans la base de donnée
	 * @param cycle
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeCycle(Cycles cycle, Date datemin, Date datemax, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence justifie dans un cycle dans une periode  pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 * Si le cycle est null alors il s'agit de tous les cycles qui sont dans la base de donnée
	 * @param cycle
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsJSexeCycleSeq(Cycles cycle, Sequences periode, int sexe);
	public int getNbreAbsJSexeCycleTrim(Cycles cycle, Trimestres periode, int sexe);
	public int getNbreAbsJSexeCycleAn(Cycles cycle, Annee periode, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans un cycle entre deux dates pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si le cycle est null alors il s'agit de tous les cycles qui sont dans la base de donnée
	 * @param cycle
	 * @param datemin
	 * @param datemax
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeCycle(Cycles cycle, Date datemin, Date datemax, int sexe);
	
	/********************************************************************************************
	 * Retourne le nombre d'heure d'absence non justifie dans un cycle dans une periode pour un 
	 * sexe donnee. 1 signifie masculin et 0 féminin
	 * 
	 *  Si le cycle est null alors il s'agit de tous les cycles qui sont dans la base de donnée
	 * @param cycle
	 * @param periode
	 *  @param sexe
	 * @return
	 */
	public int getNbreAbsNJSexeCycleSeq(Cycles cycle, Sequences periode, int sexe);
	public int getNbreAbsNJSexeCycleTrim(Cycles cycle, Trimestres periode, int sexe);
	public int getNbreAbsNJSexeCycleAn(Cycles cycle, Annee periode, int sexe);
	
	
	/*********************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour un cycle passe en paramètre.
	 * Si le cycle est null alors on va retourner celui de tous les cycles enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleve de sexe masculin d'un cycle et ceux du sexe féminn du meme cycle. 
	 * 
	 * Donc pour chaque élève du cycle, on va rechercher tous les rapports d'absence enregistré dans l'intervalle
	 * de date précisé
	 * 
	 * Et pour avoir tous les élèves du cycle, il faut d'abord récuperer tous les niveaux du cycle puis 
	 * pour chaque niveau toutes les classes et enfin pour chaque classe tous les élèves.
	 * 
	 * @param cycle
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleBean(Cycles cycle, 
			Date datemin, Date datemax);
	
	/*********************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour un cycle passe en paramètre.
	 * Si le cycle est null alors on va retourner celui de tous les cycles enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleve de sexe masculin d'un cycle et ceux du sexe féminn du meme cycle. 
	 * 
	 * Donc pour chaque élève du cycle, on va rechercher tous les rapports d'absence enregistré dans la 
	 * periode specifie en paramètre
	 * 
	 * Et pour avoir tous les élèves du cycle, il faut d'abord récuperer tous les niveaux du cycle puis 
	 * pour chaque niveau toutes les classes et enfin pour chaque classe tous les élèves.
	 * 
	 * @param cycle
	 * @param sequence
	 * @return
	 */
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleSeqBean(Cycles cycle, 
			Sequences periode);
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleTrimBean(Cycles cycle, 
			Trimestres periode);
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleAnBean(Cycles cycle, 
			Annee periode);
	
	
	/*********************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour un niveau passe en paramètre.
	 * Si le niveau est null alors on va retourner celui de tous les niveaux enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleves de sexe masculin d'un niveau et ceux du sexe féminn du meme niveau. 
	 * 
	 * Donc pour chaque élève du niveau, on va rechercher tous les rapports d'absence enregistré dans l'intervalle
	 * de date précisé
	 * 
	 * Et pour avoir tous les élèves du niveau, il faut d'abord récuperer toutes les classes du niveau puis 
	 * pour chaque classe tous les élèves.
	 * 
	 * @param niveau
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauBean(Niveaux niveau, 
			Date datemin, Date datemax);
	
	/***********************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour un niveau passe en paramètre.
	 * Si le niveau est null alors on va retourner celui de tous les niveaux enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleve de sexe masculin d'un niveau et ceux du sexe féminn du meme niveau. 
	 * 
	 * Donc pour chaque élève du niveau, on va rechercher tous les rapports d'absence enregistré dans la 
	 * periode specifie en paramètre
	 * 
	 * Et pour avoir tous les élèves du niveau, il faut d'abord récuperer toutes les classes et 
	 * enfin pour chaque classe tous les élèves.
	 * @param niveau
	 * @param periode
	 * @return
	 */
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauSeqBean(Niveaux niveau, 
			Sequences periode);
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauTrimBean(Niveaux niveau, 
			Trimestres periode);
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauAnBean(Niveaux niveau, 
			Annee periode);
	
	
	/************************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour une classe passe en paramètre.
	 * Si la classe est null alors on va retourner celui de toutes les classes enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleves de sexe masculin d'une classe et ceux du sexe féminin du meme niveau. 
	 * 
	 * Donc pour chaque élève de la classe, on va rechercher tous les rapports d'absence enregistré dans l'intervalle
	 * de date précisé
	 * 
	 * @param classe
	 * @param datemin
	 * @param datemax
	 * @return
	 */
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseBean(Classes classe, 
			Date datemin, Date datemax);
	
	/************************************************************************************************************
	 * cette methode retourne le rapport des absences par sexe et le total pour une classe passe en paramètre.
	 * Si la classe est null alors on va retourner celui de toutes les classes enregistre en BD. 
	 * 
	 * Par rapport des absences, on entend le nombre d'heure d'absence Non justfié / Total des heures d'absence
	 * Pour les eleve de sexe masculin d'une classe et ceux du sexe féminn de la même classe. 
	 * 
	 * Donc pour chaque élève de la classe, on va rechercher tous les rapports d'absence enregistré dans la 
	 * periode specifie en paramètre
	 * 
	 * @param classe
	 * @param periode
	 * @return
	 */
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseSeqBean(Classes classe, 
			Sequences periode);
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseTrimBean(Classes classe, 
			Trimestres periode);
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseAnBean(Classes classe, 
			Annee periode);
	
	
	/*********************************************************************************************
	 * Cette methode permet de retourner la liste des classes enregistrer avec les montant de scolarte s'il
	 * sont deja defini
	 * @return
	 */
	//public Collection<FicheScolariteparClasseBean> generateListFicheScolariteparClasseBean_();
	
	
	
	
	/******************************************************
	 * Cette methode retourne la classe utilitaire qui a permis
	 * de calculer toutes les données présentées sur un bulletin
	 * @return
	 */
	public UtilitairesBulletins getUtilitairesBulletins();
	
	
}
