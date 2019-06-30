package org.logesco;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.logesco.dao.EtablissementRepository;
import org.logesco.dao.IdentOperationRepository;
import org.logesco.dao.MatriculeRepository;
import org.logesco.dao.RolesRepository;
import org.logesco.dao.UtilisateursRepository;
import org.logesco.dao.UtilisateursRolesRepository;
import org.logesco.entities.Administrateurs;
import org.logesco.entities.Etablissement;
import org.logesco.entities.IdentOperation;
import org.logesco.entities.Matricule;
import org.logesco.entities.Roles;
import org.logesco.entities.Utilisateurs;
import org.logesco.entities.UtilisateursRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/*import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;*/

//import org.logesco.dao.*;
//import org.logesco.entities.*;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@SpringBootApplication
public class LogescoApplication implements CommandLineRunner{
	
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UtilisateursRepository usersRepository;
	/*
	@Autowired
	private ProffesseursRepository profRepository;
	
	@Autowired
	private PersonnelsRepository persRepository;
	
	@Autowired
	private ProviseursRepository provRepository;
	
	@Autowired
	private CenseursRepository censeurRepository;
	*/
	@Autowired
	private UtilisateursRolesRepository usersRoleRepository;
	
	@Autowired
	private EtablissementRepository etabRepository;
	
	@Autowired
	private MatriculeRepository matriculeRepository;
	
	@Autowired
	private IdentOperationRepository identopRepository;
	
	/*
	@Autowired
	private ElevesRepository elevesRepository;
	
	@Autowired
	private CompteInscriptionRepository compteinscriptionRepository;*/
	
	@Value("${dir.racine}")
	private String racineDir;
	
	@Value("${dir.racineEmblemes}")
	private String racineEmblemesDir;
	
	@Value("${dir.racineImage}")
	private String racineImageDir;
	
	@Value("${dir.documents}")
	private String documentsDir;
	
	@Value("${dir.documents.modeles}")
	private String modelesDir;
	
	@Value("${dir.emblemes.banniere}")
	private String banniereDir;
	
	@Value("${dir.emblemes.logo}")
	private String logoDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.logs}")
	private String logsDir;

	//public static Logger log = Logger.getLogger(LogescoApplication.class);
	//public static Logger log = LogManager.getLogger(LogescoApplication.class);
	Logger logger = LoggerFactory.getLogger(LogescoApplication.class);
	
	public static DateFormat formatdateop=new SimpleDateFormat("yyyy-MM-dd H:m:s");
	public static Date dateJour=new Date();
	
	public static void main(String[] args) {
		SpringApplication.run(LogescoApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		

		/*logger.trace("This is a TRACE message.");
	    logger.debug("This is a DEBUG message.");
	    logger.info("This is an INFO message.");
	    logger.warn("This is a WARN message.");
	    logger.error("You guessed it, an ERROR message.");*/
		
		
		
		
		
		/*System.out.println(formatdateop.format(dateJour)+
				"****TRACE**** DEMARRAGE DE L'APPLICATION  ****TRACE**** ");*/
	   logger.info("====================================================================");
	    logger.info("====================================================================");
	    logger.info("DEBUT DU DEMARRAGE DE L'APPLICATION.");
	    logger.info("====================================================================");
		/**********************************************************
		 * On va ici créer les différents répertoire lorsqu'il n'existe pas
		 * Ce sont les dossiers de sauvegarde des images et des documents
		 * important pour le logiciels
		 */
	try{
		/*System.out.println(formatdateop.format(dateJour)+
				"****TRACE**** LANCEMENT DE LA CREATION DE L'ARBORESECENCE SE  TROUVANT DANS "+racineDir+"****TRACE**** ");*/
		logger.debug("LANCEMENT DE LA CREATION DE L'ARBORESECENCE SE  TROUVANT DANS "+racineDir);
		File f = new File(racineDir);
		f.mkdirs();
		new File(racineEmblemesDir).mkdirs();
		new File(documentsDir).mkdirs();
		new File(modelesDir).mkdirs();
		new File(racineImageDir).mkdirs();
		
		new File(banniereDir).mkdirs();
		new File(logoDir).mkdirs();
		
		new File(photoPersonnelsDir).mkdirs();
		new File(photoElevesDir).mkdirs();
		new File(logsDir).mkdirs();
		//Ici il faut donc cacher ce dossier racine pour qu'il ne soit pas visible par les premiers venus
		String[] cmd = {"attrib","+s","+h",f.getAbsolutePath()};
		Process process = Runtime.getRuntime().exec(cmd);
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		
		logger.debug("LANCEMENT DE LA CREATION DE L'ARBORESECENCE SE  TROUVANT DANS "+racineDir);
	}
	catch(Exception e){
		logger.error("ERREUR LORS DE LA CREATION DES DOSSIERS NECESSAIRES AU BON FONCTIONNEMENT "
				+ " DU LOGICIEL. Exception "+e.getMessage());
	}
		
		
		/****************************
		 * AA) INSTALLER LES POLICES UTILISEES DANS LES RAPPORTS (ARIAL NARROW)
		 * 			Telecharger le zip sur 
		 * 			decompresser et copier les polices dans jdk_home/jre/lib/fonts et dans jre/lib/fonts
		 * 			installer chacune d'elle en faisant un clic droit puis en cliquant sur installer
		 * 
		 * BB) Il est recommendée d'uploader les emblemes de l'établissement avant l'édition de tous rapport
		 * sur PDF. Il s'agit notamment du logo et de la banniere de l'établissement. Tous les rapports porteront
		 * ce logo. Seul l'administrateur est habileté à uploader ces éléments
		 * 
		 * Il faut faire les configurations de base au démarage et de façon automatique
		 * A) Configurer le SGBD en créant le user logesco ou root avec un mot de passe qui est indique
		 * dans le fichier application.properties
		 * B) Créer la base de donnée logescodb au niveau du SGBD
		 * C) Deployer le logiciel pour permettre a JPA de creer les tables dans cette base de donnee
		 * 
		 * 
		 * 
		 * 1) Enregistrer les roles de base (ADMIN, SUPERADMIN, PROVISEUR, CENSEUR, SG, INTENDANT,
		 * ENSEIGNANT: ceci est d'ailleurs fait par défaut à l'installation du logiciel donc faudra juste verifier)
		 * 2) Enregistrer les utilisateurs de base qui sont tous des administrateurs (admin, superadmin)
		 * L'un s'occupe de la configuration du serveur(admin) et l'autre de realiser dans sa session ce 
		 * que  les utilisateurs n'ont pas pu réaliser (superadmin)
		 * 3) Enregistrer les UtilisateurRoles qui permettra effectivement de donner les roles crées aux 
		 * utilisateurs crées
		 * 4) Créer les différents répertoires permettant d'upload (ceci etant realise par le code précédent)
		 * 		a) documentsUpload: pour stocker les documents uploader
		 * 		b) emblemes/baniere et emblemes/logo: pour stocker les emblemes de l'etablissement gérer
		 * 		c)	imagesUpload/eleves et imagesUpload/personnels pour stocker les photos de profil des 
		 * élèves et des membres du personnel.
		 * 5) Apres les répertoires l'administrateur doit créer  les différents rôles que les utilisateurs 
		 * peuvent avoir dans le système car on ne peut pas avoir des utilisateurs sans role. Le rôle admin
		 * et superadmin ont deja ete crée donc il reste proviseur, censeur, sg, intendant et enseignant. 
		 * 6) Il faut modifier l'établissement par défaut enregistré et surtout uploader les emblèmes (logo et banniere)
		 * 7) Il faut enregistrer le proviseur (chose qui se fait grâce à l'administrateur)
		 * 8) Il faut enregistrer les différentes sections (type d'enseignement) de l'établissement: 
		 * Les différents code des sections sont standard donc il ne restera plus qu'à changer 
		 * les libelles sans changer leur code: 
		 * ---Section GENERAl a pour code GENERAL
		 * ---Section INDUSTRIELLE a pour code INDUSTRIELLE
		 * ---Section COMMERCIAL a pour code COMMERCIAL
		 * 9) Il faut enregistrer les différents cycles qu'on a dans l'établissement
		 * 10) Il faut enregistrer les différents niveaux qu'on a dans l'établissement
		 * 11) Il faut enregistrer toutes les spécialités qu'on a dans l'établissement. Les troncs commun peuvent
		 * etre enregistré avec un code vide afin de ne pas allonger le nom des classes puisque le code de la 
		 * spécialité en fait partie. Toutes les classes où les specialités se mélangent doivent donc prendre cette 
		 * spécialité de code vide. On a par exemple les 6eme, les 5eme, les 2nd, etc.
		 * 12) Il faut enregistrer les classes qu'on a dans l'établissement
		 * 13) Il faut enregistrer les départements ie en quelques sortes les matieres dont les cours en feront partie
		 * 14) Il faut maintenant configurer les cours qui passeront dans chacune des classes. 
		 * Pour la section anglophones, il faudra pour une classe enregistrer tous les cours même si tous les élèves
		 * ne les feront pas. En fait dans une classe comme form4 arts, tous les élèves ne font pas les cours programmés
		 * pour la spécialités.
		 * 16) Il faut enregistrer les sanctions disciplinaires (RAS, retards en heure, consignes en heure, retenus,
		 * Avertissement conduite, Blame conduite, Exclusion temporaire, Exclusion définitive ). Il faut noter que 
		 * le niveau de sévérité le plus petit (0) correspond à la sanction la moins lourde. En occurence RAS qui signifie 
		 * Rien à Signaler. 
		 * 17) Il faut enregistrer les distinctions due aux travail (Effort supplémentaire, Avertissement travail, 
		 * Blame travail, Tableau d'honneur, Tableau d'honneur avec encouragement, Tableau d'honneur avec
		 * félicitation, Tableau d'honneur refusé)
		 * 18) Il faut enregistrer les décisions qui peuvent être prise pendant le conseil de classse annuel (Admis 
		 * ou promoted, admis exceptionnellement, exclu pour age, exclu pour indiscipline, exclu si echec, 
		 * exclu pour travail, redouble ou repeat, redouble si echec, redouble exceptionnellement, exclu pour 
		 * insolvabilité, exclu pour démission, exclu pour absenteisme)
		 * 19) ll faut enregistrer l'année scolaire qui sera gérer
		 * 20) Il faut enregistrer les trimestres qui seront gérer dans l'année scolaire
		 * 21) Il faut enregistrer les séquences qui seront gérer dans l'année scolaire
		 * 22) Demander au proviseur de n'activer qu'une séquence et qu'un trimestre à la fois. Celui dans lequel
		 * les enseignants devraient travailler afin d'éviter les erreurs par exemple l'enregistrement des notes 
		 * d'une séquence dans une autre. 
		 * 23) Les secretaires devront s'accomoder au modèle de fichier excel qui leur sont proposés. En effet, 
		 * pour l'enregistrement des élèves pendant les recrutements, elles peuvents simplement remplir un 
		 * fichier par classe selon le modèle proposé. Ainsi, puisque ce sont des élèves nouvellement recrutés, on est 
		 * sur que leurs statut est nouveau, leurs etat est non inscrit et ils ne sont pas redoublant. 
		 * 24) Le superadmin peut à chaque fois modifier les rôles attribués à un utilisateur afin de définir 
		 * ce qu'il pourra ou pas faire dans le système.
		 * 
		 */
			
		try{
			logger.debug("CREATION DES SUPERS ROLES DE L'APPLICATION (admin et superadmin) "
					+ "AU CAS OU IL N'EXISTE PAS.");
			//On enregistre les role de base de l'application
			rolesRepository.save(new Roles("ADMIN"));
			rolesRepository.save(new Roles("SUPERADMIN"));
			rolesRepository.save(new Roles("PROVISEUR"));
			rolesRepository.save(new Roles("CENSEUR"));
			rolesRepository.save(new Roles("SG"));
			rolesRepository.save(new Roles("INTENDANT"));
			rolesRepository.save(new Roles("ENSEIGNANT"));
			rolesRepository.save(new Roles("TITULAIRE"));
			/****
			 * les autres roles qui sont crées mais qui n'ont pas encore de vue sur le système
			 * 
			 */
			rolesRepository.save(new Roles("SECRETAIRE"));
			rolesRepository.save(new Roles("SURVEILLANT"));
			rolesRepository.save(new Roles("VEILLEUR"));
			rolesRepository.save(new Roles("ELEVES"));
			rolesRepository.save(new Roles("PARENTS"));
			rolesRepository.save(new Roles("AUTRES"));
			
		}
		catch(Exception e){
			logger.error("LES ROLES DE BASE POUR LA CONFIGURATION DE L'APPLICATION EXISTE DEJA. "
					+ "Exception "+e.getMessage());
		}
		Utilisateurs admin = null;
		Utilisateurs superadmin = null;
		try{
			logger.debug("CREATION DES UTILISATEURS CHARGES DE LA CONFIGURATION DE LOGESCO"
					+ " (admin et superadmin)");
			
			Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();
			admin = usersRepository.findByUsername("admin");
			
			if(admin == null){
				admin=usersRepository.save(new Administrateurs("admin@gmail.com", 
						"678470262", "695093228", "admin", p.encode("12345"), true));
				UtilisateursRoles usersRole = usersRoleRepository.getUtilisateursRoles(admin.getIdUsers(), "ADMIN");
				
				if(usersRole == null){
					usersRoleRepository.save(new UtilisateursRoles(admin,
							rolesRepository.findByRole("ADMIN")));
				}
			}
			else{
				logger.debug("L'ADMIN A DEJA ETE CREE AVEC LE ROLE ADMIN QUI LUI PERMETTRA DE CONFIGURER"
						+ " L'APPLICATION ");
			}
			
			superadmin = usersRepository.findByUsername("superadmin");
			
			if(superadmin == null){
				superadmin=usersRepository.save(new Administrateurs("superadmin@gmail.com", 
						"678470262", "695093228", "superadmin", p.encode("12345"), true));
				UtilisateursRoles usersRole = usersRoleRepository.getUtilisateursRoles(superadmin.getIdUsers(), "SUPERADMIN");
				
				if(usersRole == null){
					usersRoleRepository.save(new UtilisateursRoles(superadmin,
							rolesRepository.findByRole("SUPERADMIN")));
				}
			}
			else{
				logger.debug("LE SUPERADMIN A DEJA ETE CREE AVEC LE ROLE SUPERADMIN LUI PERMETTANT DE "
						+ " FAIRE CERTAINES ACTIONS IMPORTANTE AU NIVEAU UTILISATEURS ");
			}
			
		}
		catch(Exception e){
			logger.error("LES USERS DE BASE EXISTE DEJA EN BD (admin et superadmin). Exception "+e.getMessage());
		}
		
		/**************************
		 * On prepare l'enregistrement de la table matricule qui va permettre de generer les matricules
		 * avec les numeros d'enregistrement
		 */
		try{
			List<Matricule> listofMatricule = matriculeRepository.findAll();
			if(listofMatricule!=null){
				if(listofMatricule.size()>0){
					logger.debug("LE CHAMP PERMETTANT DE CREER LE NUMERO D'ORDRE DANS LA GENERATION "
							+ "DES MATRICULES DES ELEVES EXISTE DEJA ");
				}
				else if(listofMatricule.size() == 0){
					logger.debug("ENREGISTREMENT DU PPREMIER NUMERO QUI PERMETTRA LA GENERATION DES "
							+ "MATRICULES: DONC LE NUMERO 0");
					Matricule mat = new Matricule();
					mat.setValeur(0);
					matriculeRepository.save(mat);
				}
			}
			else{
				logger.error("LA LISTE DES MATRICULES EST NULL DONC IL Y A EU "
						+ "UN SOUCI PENDANT LE DEMARRAGE ");
			}
		}
		catch(Exception e){
			logger.error("ERREUR D'ENREGISTREMENT DES NUMEROS POUR LA FABRICATION DES MATRICULES"
					+ " "+e.getMessage());
		}
		
		/*************************************************
		 * On prepare l'enregistrement de la table Identoperation qui va permettre de génerer le numero
		 * des transactions financieres qui aura lieu dans les différents comptes d'incription des élèves
		 */
		try{
			List<IdentOperation> listofIdentop = identopRepository.findAll();
			if(listofIdentop!=null){
				if(listofIdentop.size()>0){
					logger.debug("LE CHAMP PERMETTANT DE GENERER LES NUMEROS DE TRANSACTION "
							+ " FINANCIERE EXISTE DEJA ");
				}
				else if(listofIdentop.size() == 0){
					logger.debug("ENREGISTREMENT DU PREMIER NUMERO QUI PERMETTRA LA GENERATION DES "
							+ "NUMEROS DE TRANSACTION ");
					IdentOperation identop = new IdentOperation();
					identop.setNumero(0);
					identopRepository.save(identop);
				}
			}
			else{
				logger.error("LA LISTE DES IDENTIFIANT DES OPERATIONS EST NULL DONC IL Y A EU UN SOUCI DE "
						+ "DEMARRAGE ");
			}
		}
		catch(Exception e){
			logger.error("ERREUR D'ENREGISTREMENT DES NUMEROS POUR LA FABRICATION DES "
					+ "NUMEROS DE TRANSACTION FINANCIERE: "+e.getMessage());
		}
		
		
		
		/*****************************
		 * On enregistre un établissement par defaut car en fait on doit avoir un établissement
		 * dans chaque base de donnée. Tous ceci fait parti des configurations de 
		 * base. la configuration est telle qu'on aura toujours un seul établissement 
		 * dans la base de donnée. Car le logiciel doit gérer un établissement à la fois
		 */
		
			List<Etablissement> listofEtab = etabRepository.findAll();
			if(listofEtab != null){
				if(listofEtab.size()>0){
					logger.debug("L'ETABLISSEMENT A GERER EST DEJA ENREGISTRE DANS LA BD ");
				}
				else if(listofEtab.size() == 0){
					/*
					 * On va enregistrer le tout premier etablissement de tel sorte qu'il soit juste
					 * modifier pendant la configuration
					 */
					logger.debug("PREPARATION DE L'ENREGISTREMENT DE L'ETABLISSEMENT PAR DEFAUT QUI SERA MODIFIER PAR LA SUITE PAR LE CHEF DE L'ETABLISSEMENT CLIENTE");
					
					Etablissement etab = new Etablissement();
					etab.setAliasEtab("Alias");
					etab.setAliasministeretuteleanglaisEtab("MINESEC");
					etab.setAliasministeretuteleEtab("MINESEC");
					etab.setAliasnomanglaisEtab("Noms en anglais");
					etab.setBanniereEtab(" ");
					etab.setBpEtab("bp");
					etab.setCodeMatriculeEtab("code");
					etab.setDeleguationdeptuteleanglaisEtab("DDES");
					etab.setDeleguationdeptuteleEtab("DDES");
					etab.setDeleguationregtuteleanglaisEtab("DRES");
					etab.setDeleguationregtuteleEtab("DRES");
					etab.setDeviseanglaisEtab("Peace-Work-Fatherland");
					etab.setDeviseEtab("Paix-Travail-Patrie");
					etab.setEmailEtab("etablissement@gmail.com");
					etab.setLogoEtab("");
					etab.setMatriculeEtab("IM");
					etab.setMinisteretuteleanglaisEtab("MINESEC");
					etab.setMinisteretuteleEtab("MINESEC");
					etab.setNomsanglaisEtab("Noms etab en anglais");
					etab.setNomsEtab("Noms en français");
					etab.setNumtel1Etab("600000000");
					etab.setNumtel2Etab("");
					etab.setVilleEtab("Ville ");
					try{
					etabRepository.save(etab);
					}
					catch(Exception e){
						logger.error("ERREUR LORS D'ENREGISTREMENT DE L'ETABLISSEMENT . Exception : "+e.getMessage());
						
					}
					
				}
			}
			else{
				logger.error("LA LISTE DES ETABLISSEMENTS EST NULL DONC MAUVAISE RECUPERATION");
			}
		
		
		
		/*
		 * On récupère tous les élèves et on leur attribue un compte
		 */
		/*List<Eleves> listofEleve = elevesRepository.findAll();
		
		for(Eleves eleves : listofEleve){
			CompteInscription compte = new CompteInscription(new Double(0));
			compteinscriptionRepository.save(compte);
			eleves.setCompteInscription(compte);
			elevesRepository.save(eleves);
		}*/
		
		
		/*
		 * Enregistrement du niveau superieure à tous les autres niveaux
		 */
		/*Niveaux niveauAucun=new Niveaux();
		niveauAucun.setCodeNiveaux("aucun");
		niveauAucun.setNumeroOrdreNiveaux(8);
		niveauAucun.setCycle(null);
		niveauAucun.setNiveau(null);
		niveauxRepository.save(niveauAucun);*/
		
			/*Roles roles1=rolesRepository.save(new Roles("ADMIN"));
			Roles roles2=rolesRepository.save(new Roles("PROVISEUR"));
			Roles roles3=rolesRepository.save(new Roles("CENSEUR"));
			Roles roles4=rolesRepository.save(new Roles("SG"));
			Roles roles5=rolesRepository.save(new Roles("ENSEIGNANT"));
			Roles roles6=rolesRepository.save(new Roles("USER"));
			Roles roles7=rolesRepository.save(new Roles("SUPERADMIN"));
			
			
			 // Un administrateur de l'application
			 
			Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();
			Utilisateurs admin1=usersRepository.save(new Administrateurs("admin1@gmail.com", 
					"678470262", "695093228", "admin", p.encode("12345"), true));
			try{
				usersRoleRepository.save(new UtilisateursRoles(admin1,roles1));
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
			
			
			 // Un utilisateur pour l'application qui aura le role PROVISEUR
			 
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			Proviseur pr=provRepository.save(new Proviseur("000008624", df.parse("1989-05-15"), 
					"DIPET2", "ckia@gmail.com", "PLET", "fokoue", "camerounaise", "azobs", "678470262", 
					"695093228", "tof", "cedric", "sadi", "masculin", "fonctionnaire", "douala", "proviseur", 
					p.encode("proviseur"), true, "informatique"));
			  
			try{  
				usersRoleRepository.save(new UtilisateursRoles(pr,roles2));
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
			
			
			 // Un utilisateur pour l'application qui aura le role CENSEUR
			 
			Censeurs ce=censeurRepository.save(new Censeurs("000008625",  df.parse("1989-05-15"), 
					"DIPET2", "ckia@gmail.com", "PLET", "fokoue", "camerounaise", "azobs", "678470262",
					"695093228", "tof",  "cedrico", "sadi", "masculin",
					"fonctionnaire", "douala", "censeur1", p.encode("censeur1"), true,
					"informatique", 1));
			  
			try{  
				usersRoleRepository.save(new UtilisateursRoles(ce,roles3));
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
			
			try{
				usersRoleRepository.save(new UtilisateursRoles(ce,roles5));
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}*/
			/*log.log(Level.DEBUG, "FIN DU DEMARRAGE ET CONFIGURATION DE BASE EFFECTUEE");*/
			
		
		logger.info("FIN DU DEMARRAGE DE L'APPLICATION.");
	    logger.info("====================================================================");
		
		}
	
}
