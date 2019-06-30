/**
 * 
 */
package org.logesco.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.logesco.dao.AnneeRepository;
import org.logesco.dao.CenseursRepository;
import org.logesco.dao.ClassesRepository;
import org.logesco.dao.CompteInscriptionRepository;
import org.logesco.dao.ElevesRepository;
import org.logesco.dao.EnseignantsRepository;
import org.logesco.dao.IntendantRepository;
import org.logesco.dao.MatriculeRepository;
import org.logesco.dao.PersonnelsDAppuiRepository;
import org.logesco.dao.PersonnelsRepository;
import org.logesco.dao.ProffesseursRepository;
import org.logesco.dao.RolesRepository;
import org.logesco.dao.SGRepository;
import org.logesco.dao.SequencesRepository;
import org.logesco.dao.TrimestresRepository;
import org.logesco.dao.UtilisateursRolesRepository;
import org.logesco.entities.Annee;
import org.logesco.entities.Censeurs;
import org.logesco.entities.Classes;
import org.logesco.entities.CompteInscription;
import org.logesco.entities.Eleves;
import org.logesco.entities.Enseignants;
import org.logesco.entities.Intendant;
import org.logesco.entities.Matricule;
import org.logesco.entities.Personnels;
import org.logesco.entities.PersonnelsDAppui;
import org.logesco.entities.Proffesseurs;
import org.logesco.entities.Proviseur;
import org.logesco.entities.Roles;
import org.logesco.entities.SG;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.entities.Utilisateurs;
import org.logesco.entities.UtilisateursRoles;
import org.logesco.modeles.FicheScolariteparClasseBean;
import org.logesco.modeles.PersonnelBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cedrickiadjeu
 *
 */
@Service
@Transactional
public class ProviseurServiceImplementation implements IProviseurService {
	
	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UtilisateursRolesRepository usersrolesRepository;
	
	@Autowired
	private PersonnelsRepository persRepository;
	
	@Autowired
	private ProffesseursRepository proffRepository;
	
	@Autowired
	private CenseursRepository censeurRepository;
	
	@Autowired
	private SGRepository sgRepository;
	
	@Autowired
	private IntendantRepository intendantRepository;
	
	@Autowired
	private EnseignantsRepository enseignantRepository;
	
	@Autowired
	private ClassesRepository classesRepository;
	
	@Autowired
	private ElevesRepository elevesRepository;

	@Autowired
	private CompteInscriptionRepository compteinscriptionRepository;

	@Autowired
	private SequencesRepository	 sequenceRepository;

	@Autowired
	private TrimestresRepository	 trimestreRepository;

	@Autowired
	private AnneeRepository	 anneeRepository;

	@Autowired
	private MatriculeRepository	 matriculeRepository;
	
	@Autowired
	private PersonnelsDAppuiRepository	 personnelsDAppuiRepository;
	
	@Autowired
	private IUsersService usersService;
	
	//private UtilitairesBulletins ub;
	
	
	
	//private static Logger log = LogescoApplication.log;

	public String[] ch_unites = {"zéro", "un", "deux", "trois", "quatre", "cinq", "six", "sept",
            "huit","neuf", "dix", "onze", "douze", "treize", "quatorze",
              "quinze", "seize", "dix-sept", "dix-huit", "dix-neuf"};

	public String[] ch_dizaines = {"", "", "vingt", "trente", "quarante", "cinquante", "soixante",
		"", "quatre-vingt"};

	public String[] ch_units = {"zero", "one", "two", "three", "four", "five", "six", "seven",
	  "eight","nine", "ten", "eleven", "twelve", "thirteen", "fourteen",
	    "fiveteen", "sixteen", "seventeen", "eighteen", "nineteen"};

	public String[] ch_tens = {"", "", "twenty", "thirty", "forty", "fifty", "sixty",
		"seventy", "eighty", "ninety"};
	
	
	public ProviseurServiceImplementation() {
		super();
		//this.ub = new UtilitairesBulletins();
	}
	
	
	
	
	/********************************************************************************
	 * LES METHODES DE L'INTERFACE IProviseurImplementation QUI SONT LES 
	 * FONCTIONS QUI SERVENT PLUS DANS UNE SESSION PROVISEUR
	 */
	
	
	

	
	public Long updateProffesseurs(Long idUsers, Proffesseurs proffesseurs){

		/*log.log(Level.DEBUG, "Lancement de updateProffesseurs: ");*/
		Long idProfModif=new Long(-10);

		Proffesseurs profAModif=usersService.findProffesseurs(idUsers);

		if(profAModif!=null){
			/*
			 * Maintenant on doit mettre à jour ce personnel avec les nouvelle donnée reçu
			 * en vérifiant toujours que les contrainte resteront inviolé
			 */

			//la contrainte noms prenoms datenaiss restera t'elle inviolé
			if(!(proffesseurs.getNomsPers().equals(profAModif.getNomsPers())) ||
					!(proffesseurs.getPrenomsPers().equals(profAModif.getPrenomsPers()))||
					(proffesseurs.getDatenaissPers().getTime()!=profAModif.getDatenaissPers().getTime())){
				/*
				 * On doit vérifier l'unicite du nouveau triplet
				 */
				Personnels persExistantAvectriplet=usersService.findPersonnel(proffesseurs.getNomsPers(), proffesseurs.getPrenomsPers(), 
						proffesseurs.getDatenaissPers());
				if(persExistantAvectriplet!=null) return new Long(-2);

			}

			/*
			 * Pour le numero de cni
			 */
			if(!(proffesseurs.getNumcniPers().equals(profAModif.getNumcniPers()))){
				/*
				 * Il faut vérifier unicité du nouveau numero de cni
				 */
				Personnels persExistantAvecNumcni=usersService.findPersonnel(proffesseurs.getNumcniPers());
				if(persExistantAvecNumcni!=null) return new Long(-1);
			}

			/*
			 * Pour le username
			 */
			if(!(proffesseurs.getUsername().equals(profAModif.getUsername()))){
				Utilisateurs usersExistAvecUsername=usersService.getUsers(proffesseurs.getUsername());
				if(usersExistAvecUsername!=null) return new Long(-3);
			}

			/*
			 * Ici a ce stade du code on est sur que tout a été vérifier donc aucune contrainte ne peut être violé
			 * donc on peut faire les mises à jour sans souci
			 */
			profAModif.setDatenaissPers(proffesseurs.getDatenaissPers());
			profAModif.setDiplomePers(proffesseurs.getDiplomePers());
			profAModif.setEmailPers(proffesseurs.getEmailPers());
			profAModif.setGradePers(proffesseurs.getGradePers());
			profAModif.setLieunaissPers(proffesseurs.getLieunaissPers());
			profAModif.setNationalitePers(proffesseurs.getNationalitePers());
			profAModif.setNomsPers(proffesseurs.getNomsPers());
			profAModif.setNumcniPers(proffesseurs.getNumcniPers());
			profAModif.setNumtel1Pers(proffesseurs.getNumtel1Pers());
			profAModif.setNumtel2Pers(proffesseurs.getNumtel2Pers());
			if(proffesseurs.getPhotoPers()!=null){
				profAModif.setPhotoPers(proffesseurs.getPhotoPers());
			}
			profAModif.setPrenomsPers(proffesseurs.getPrenomsPers());
			profAModif.setQuartierPers(proffesseurs.getQuartierPers());
			profAModif.setSexePers(proffesseurs.getSexePers());
			profAModif.setSpecialiteProf(proffesseurs.getSpecialiteProf());
			profAModif.setStatutPers(proffesseurs.getStatutPers());
			profAModif.setUsername(proffesseurs.getUsername());
			profAModif.setVillePers(proffesseurs.getVillePers());
			
			/*
			 * Traitement des nouveaux champs
			 */
			profAModif.setSitmatriPers(proffesseurs.getSitmatriPers());
			profAModif.setMatriculePers(proffesseurs.getMatriculePers());
			profAModif.setDeptoriginePers(proffesseurs.getDeptoriginePers());
			profAModif.setRegionoriginePers(proffesseurs.getRegionoriginePers());
			profAModif.setFonctionPers(proffesseurs.getFonctionPers());
			profAModif.setQuotaHorairePers(proffesseurs.getQuotaHorairePers());
			profAModif.setDateentreeFPPers(proffesseurs.getDateentreeFPPers());
			profAModif.setDatePSPers(proffesseurs.getDatePSPers());
			profAModif.setObservations(proffesseurs.getObservations());
			profAModif.setEtabDAttache(proffesseurs.getEtabDAttache());



			/*
			 * On peut maintenant faire la mise à jour
			 */
			Proffesseurs profUpdate=proffRepository.save(profAModif);
			////System.err.println("Nous voici a l'execution   777");
			idProfModif=profUpdate.getIdUsers();
		}
		/*log.log(Level.DEBUG, "fin de l'exécution de updateProffesseurs");*/
		return idProfModif;
	
	}

	public Long saveCenseurs(Censeurs censeur, int roleCode) {

		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		//System.err.println("Nous voici a l'execution   TOUT DEBUTE 1111");
		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=usersService.findPersonnel(censeur.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}
		
		/*
		 * Est ce que quelqun a le meme matricule
		 */
		Personnels persExistantAvecMatricule = usersService.findPersonnelAvecMatricule(censeur.getMatriculePers());
		if(persExistantAvecMatricule!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}
		
		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=usersService.findPersonnel(censeur.getNomsPers(), censeur.getPrenomsPers(), 
				censeur.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}
		//System.err.println("Nous voici a l'execution   3333");

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=usersService.getUsers(censeur.getUsername());
		if(usersExistAvecUsername!=null) {
			//System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}
		//System.err.println("Nous voici a l'execution   4444");

		/*
		 * Est ce qu'un censeur existe avec le même numéro ?
		 */
		Censeurs censeurExistAvecNumero=usersService.findCenseur(censeur.getNumeroCens());
		if(censeurExistAvecNumero!=null) {
			//System.err.println("Voici censeurExistAvecNumero  "+censeurExistAvecNumero.toString());
			return new Long(-4);
		}
		//System.err.println("Nous voici a l'execution   5555");
		
		if(censeur.getStatutPers().equalsIgnoreCase("FONCTIONNAIRE")==true){
			if(censeur.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				censeur.getFonctionPers().equalsIgnoreCase("SG")==true||
				censeur.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
				censeur.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(censeur.getMatriculePers().length()<=0){
					return new Long(-6);
				}
			}
		}
		
		if(censeur.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				censeur.getFonctionPers().equalsIgnoreCase("SG")==true||
						censeur.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
			if(censeur.getSpecialiteProf().length()<=0){
				return new Long(-5);
			}
		}
		
		if(censeur.getStatutPers().equalsIgnoreCase("VACATAIRE")==false){
			if(censeur.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				censeur.getFonctionPers().equalsIgnoreCase("SG")==true||
				censeur.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
				censeur.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(censeur.getGradePers().length()<=0){
					return new Long(-7);
				}
			}
		}

		/*
		 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifié
		 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
		 * afin de créer les UtilisateursRoles
		 */
		//On encode donc le mot de passe
		censeur.setPassword(p.encode(censeur.getPassword()));

		//en enregistre le censeur dans la bd
		Censeurs censeurEnreg=censeurRepository.save(censeur);
		//System.err.println("Nous voici a l'execution   6666");
		/*
		 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
		 */
		Roles roles1=rolesRepository.findByRole("CENSEUR");
		if(roles1==null) return new Long(-8);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)censeurEnreg);
		usersroles.setRoleAssocie(roles1);
		System.err.println("Nous voici a l'execution   7777");

		usersrolesRepository.save(usersroles);

		/*if(roleCode==1){
			//Alors il a le role censeur et enseignant
			Roles roles2=rolesRepository.findByRole("ENSEIGNANT");
			if(roles2==null) return new Long(-6);
			//On enregistre ce role pour cet utilisateur
			UtilisateursRoles usersroles1=new UtilisateursRoles();
			usersroles1.setUsers((Utilisateurs)censeurEnreg);
			usersroles1.setRoleAssocie(roles2);
			System.err.println("Nous voici a l'execution   8888");

			usersrolesRepository.save(usersroles1);
		}
		System.err.println("Nous voici a l'execution   9999");*/

		return censeurEnreg.getIdUsers();
	
	}

	public List<String> saveListCenseurs(List<Censeurs> listofcenseur){

		/*
		 * On enregistre les censeurs qui viennent d'une liste donc on va les attribuer le rôle 
		 */
		List<String> listofError= new ArrayList<String>();
		for(Censeurs cens : listofcenseur){
			System.out.println("usersService enreg cens :"+cens.getNomsPers());
			int roleCode = 2;
			Long val = this.saveCenseurs(cens, roleCode);
			//System.out.println("valretour usersService enreg cens :"+val);
			if(val.longValue() == -1){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Numéro CNI déjà utilisé :"+cens.getNomsPers()+" "+cens.getPrenomsPers()
					+" né le "+spd1.format(cens.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -2){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Ces noms sont déjà utilisés :"+cens.getNomsPers()+" "+cens.getPrenomsPers()
					+" né le "+spd1.format(cens.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -3){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Login déjà utilisé :"+cens.getNomsPers()+" "+cens.getPrenomsPers()
					+" né le "+spd1.format(cens.getDatenaissPers());
				listofError.add(erreur);
			}
		}
		return listofError;
	
	}

	public int updateNumeroCenseurs(Long idUsers, int newNumero){

		Censeurs censeursAModif=usersService.findCenseurs(idUsers);
		if(censeursAModif==null) return 0;
		/*
		 *On verifie la contrainte  Pour le numeroCens
		 */

		Censeurs censeursAvecNumero=usersService.findCenseur(newNumero);
		if(censeursAvecNumero!=null)  return 0;

		censeursAModif.setNumeroCens(newNumero);
		censeurRepository.save(censeursAModif);

		return 1;
	
	}

	public Long saveSG(SG sg, int roleCode) {

		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=usersService.findPersonnel(sg.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			//////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}
		
		/*
		 * Est ce que quelqun a le meme matricule
		 */
		Personnels persExistantAvecMatricule = usersService.findPersonnelAvecMatricule(sg.getMatriculePers());
		if(persExistantAvecMatricule!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=usersService.findPersonnel(sg.getNomsPers(), sg.getPrenomsPers(), 
				sg.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			//////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=usersService.getUsers(sg.getUsername());
		if(usersExistAvecUsername!=null) {
			//////System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}

		/*
		 * Est ce qu'un sg existe avec le même numéro ?
		 */
		SG sgExistAvecNumero=usersService.findSG(sg.getNumeroSG());
		if(sgExistAvecNumero!=null) {
			////System.err.println("Voici censeurExistAvecNumero  "+sgExistAvecNumero.toString());
			return new Long(-4);
		}

		if(sg.getStatutPers().equalsIgnoreCase("FONCTIONNAIRE")==true){
			if(sg.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
					sg.getFonctionPers().equalsIgnoreCase("SG")==true||
					sg.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
					sg.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(sg.getMatriculePers().length()<=0){
					return new Long(-6);
				}
			}
		}
		
		if(sg.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				sg.getFonctionPers().equalsIgnoreCase("SG")==true||
				sg.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
			if(sg.getSpecialiteProf().length()<=0){
				return new Long(-5);
			}
		}
		
		
		if(sg.getStatutPers().equalsIgnoreCase("VACATAIRE")==false){
			if(sg.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				sg.getFonctionPers().equalsIgnoreCase("SG")==true||
				sg.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
				sg.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(sg.getGradePers().length()<=0){
					return new Long(-7);
				}
			}
		}
		
		/*
		 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifiées
		 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
		 * afin de créer les UtilisateursRoles
		 */
		//On encode donc le mot de passe
		sg.setPassword(p.encode(sg.getPassword()));

		//en enregistre le censeur dans la bd
		SG sgEnreg=sgRepository.save(sg);
		/*
		 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
		 */
		Roles roles1=rolesRepository.findByRole("SG");
		if(roles1==null) return new Long(-8);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)sgEnreg);
		usersroles.setRoleAssocie(roles1);

		usersrolesRepository.save(usersroles);

		/*if(roleCode==3){
			//Alors il a le role censeur et enseignant
			Roles roles2=rolesRepository.findByRole("ENSEIGNANT");
			if(roles2==null) return new Long(-6);
			//On enregistre ce role pour cet utilisateur
			UtilisateursRoles usersroles1=new UtilisateursRoles();
			usersroles1.setUsers((Utilisateurs)sgEnreg);
			usersroles1.setRoleAssocie(roles2);

			usersrolesRepository.save(usersroles1);
		}*/

		return sgEnreg.getIdUsers();
	
	}

	public List<String> saveListSG(List<SG> listofsg){

		/*
		 * On enregistre les sg qui viennent d'une liste donc on va les attribuer le rôle 
		 */
		List<String> listofError= new ArrayList<String>();
		for(SG sg : listofsg){
			//System.out.println("usersService enreg SG:"+sg.getNomsPers());
			int roleCode = 4;
			Long val = this.saveSG(sg, roleCode);
			if(val.longValue() == -1){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Numéro CNI déjà utilisé :"+sg.getNomsPers()+" "+sg.getPrenomsPers()
					+" né le "+spd1.format(sg.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -2){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Ces noms sont déjà utilisés :"+sg.getNomsPers()+" "+sg.getPrenomsPers()
					+" né le "+spd1.format(sg.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -3){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Login déjà utilisé :"+sg.getNomsPers()+" "+sg.getPrenomsPers()
					+" né le "+spd1.format(sg.getDatenaissPers());
				listofError.add(erreur);
			}
		}
		
		return listofError;
	
	}

	public int updateNumeroSG(Long idUsers, int newNumero){

		SG sgAModif=usersService.findSG(idUsers);
		if(sgAModif==null) return 0;
		/*
		 * On verifie la contrainte sur Pour le numeroSG
		 */

		SG sgAvecNumero=usersService.findSG(newNumero);
		if(sgAvecNumero!=null) return 0;

		sgAModif.setNumeroSG(newNumero);

		return 1;
	
	}

	public Long saveIntendant(Intendant intendant, int roleCode) {

		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=usersService.findPersonnel(intendant.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Est ce que quelqun a le meme matricule
		 */
		Personnels persExistantAvecMatricule = usersService.findPersonnelAvecMatricule(intendant.getMatriculePers());
		if(persExistantAvecMatricule!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}
		
		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=usersService.findPersonnel(intendant.getNomsPers(), intendant.getPrenomsPers(), 
				intendant.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=usersService.getUsers(intendant.getUsername());
		if(usersExistAvecUsername!=null) {
			////System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}

		/*
		 * Est ce qu'un sg existe avec le même numéro ?
		 */
		Intendant intendantExistAvecNumero=usersService.findIntendant(intendant.getNumeroInt());
		if(intendantExistAvecNumero!=null) {
			////System.err.println("Voici censeurExistAvecNumero  "+intendantExistAvecNumero.toString());
			return new Long(-4);
		}
		
		if(intendant.getStatutPers().equalsIgnoreCase("FONCTIONNAIRE")==true){
			if(intendant.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
					intendant.getFonctionPers().equalsIgnoreCase("SG")==true||
					intendant.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
					intendant.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(intendant.getMatriculePers().length()<=0){
					return new Long(-6);
				}
			}
		}

		if(intendant.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				intendant.getFonctionPers().equalsIgnoreCase("SG")==true||
				intendant.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
			if(intendant.getSpecialiteProf().length()<=0){
				return new Long(-5);
			}
		}
		
		if(intendant.getStatutPers().equalsIgnoreCase("VACATAIRE")==false){
			if(intendant.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				intendant.getFonctionPers().equalsIgnoreCase("SG")==true||
				intendant.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
				intendant.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(intendant.getGradePers().length()<=0){
					return new Long(-7);
				}
			}
		}
		
		
		/*
		 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifiées
		 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
		 * afin de créer les UtilisateursRoles
		 */
		//On encode donc le mot de passe
		intendant.setPassword(p.encode(intendant.getPassword()));

		//en enregistre le censeur dans la bd
		Intendant intendantEnreg=intendantRepository.save(intendant);

		/*
		 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
		 */
		Roles roles1=rolesRepository.findByRole("INTENDANT");
		if(roles1==null) return new Long(-8);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)intendantEnreg);
		usersroles.setRoleAssocie(roles1);

		usersrolesRepository.save(usersroles);

		/*if(roleCode==5){
			//Alors il a le role censeur et enseignant
			Roles roles2=rolesRepository.findByRole("ENSEIGNANT");
			if(roles2==null) return new Long(-6);
			//On enregistre ce role pour cet utilisateur
			UtilisateursRoles usersroles1=new UtilisateursRoles();
			usersroles1.setUsers((Utilisateurs)intendantEnreg);
			usersroles1.setRoleAssocie(roles2);

			usersrolesRepository.save(usersroles1);
		}*/

		return intendantEnreg.getIdUsers();
	
	}
	
	public int updateNumeroIntendant(Long idUsers, int newNumero){

		Intendant intendantAModif=usersService.findIntendant(idUsers);
		if(intendantAModif==null) return 0;
		/*
		 *On verifie la contrainte  Pour le numeroInt
		 */

		Intendant intendantAvecNumero=usersService.findIntendant(newNumero);
		if(intendantAvecNumero!=null) return 0;

		intendantAModif.setNumeroInt(newNumero);

		return 1;
	
	}
	
	public Long saveEnseignants(Enseignants enseignants) {

		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=usersService.findPersonnel(enseignants.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Pour le matricule
		 */
		Personnels persExistantAvecMatricule=usersService.findPersonnel(enseignants.getMatriculePers());
		if(persExistantAvecMatricule!=null) {
			return new Long(-1);
		}

		
		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=usersService.findPersonnel(enseignants.getNomsPers(), enseignants.getPrenomsPers(), 
				enseignants.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=usersService.getUsers(enseignants.getUsername());
		if(usersExistAvecUsername!=null) {
			////System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}

		if(enseignants.getStatutPers().equalsIgnoreCase("FONCTIONNAIRE")==true){
			if(enseignants.getFonctionPers().equalsIgnoreCase("PROVISEUR")==true ||
					enseignants.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
					enseignants.getFonctionPers().equalsIgnoreCase("SG")==true||
					enseignants.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
					enseignants.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(enseignants.getMatriculePers().length()<=0){
					return new Long(-5);
				}
			}
		}
		
		if(enseignants.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				enseignants.getFonctionPers().equalsIgnoreCase("SG")==true||
				enseignants.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
			if(enseignants.getSpecialiteProf().length()<=0){
				return new Long(-4);
			}
		}
		
		if(enseignants.getStatutPers().equalsIgnoreCase("VACATAIRE")==false){
			if(enseignants.getFonctionPers().equalsIgnoreCase("PROVISEUR")==true ||
				enseignants.getFonctionPers().equalsIgnoreCase("CENSEUR")==true||
				enseignants.getFonctionPers().equalsIgnoreCase("SG")==true||
				enseignants.getFonctionPers().equalsIgnoreCase("INTENDANT")==true||
				enseignants.getFonctionPers().equalsIgnoreCase("ENSEIGNANT")==true){
				if(enseignants.getGradePers().length()<=0){
					return new Long(-6);
				}
			}
		}
		
		


		/*
		 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifiées
		 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
		 * afin de créer les UtilisateursRoles
		 */
		//On encode donc le mot de passe
		enseignants.setPassword(p.encode(enseignants.getPassword()));

		//en enregistre le censeur dans la bd
		Enseignants enseignantsEnreg=enseignantRepository.save(enseignants);
		/*
		 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
		 */
		Roles roles1=rolesRepository.findByRole("ENSEIGNANT");
		if(roles1==null) return new Long(-6);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)enseignantsEnreg);
		usersroles.setRoleAssocie(roles1);

		usersrolesRepository.save(usersroles);

		return enseignantsEnreg.getIdUsers();
	
	}
	
	public Long savePersonnelsDAppui(PersonnelsDAppui personnels){

		//Pour encoder les mots de passe
				Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

				/*
				 * Est ce que quelqu'un existe en bd avec le meme numerocni?
				 */
				Personnels persExistantAvecNumcni=usersService.findPersonnel(personnels.getNumcniPers());
				if(persExistantAvecNumcni!=null) {
					////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
					return new Long(-1);
				}
				
				
				/*
				 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
				 */
				Personnels persExistantAvectriplet=usersService.findPersonnel(personnels.getNomsPers(), personnels.getPrenomsPers(), 
						personnels.getDatenaissPers());
				if(persExistantAvectriplet!=null) {
					////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
					return new Long(-2);
				}

				/*
				 * Est ce qu'un utilisateur existe donc avec le même username
				 */
				Utilisateurs usersExistAvecUsername=usersService.getUsers(personnels.getUsername());
				if(usersExistAvecUsername!=null) {
					////System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
					return new Long(-3);
				}



				/*
				 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifiées
				 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
				 * afin de créer les UtilisateursRoles
				 */
				//On encode donc le mot de passe
				personnels.setPassword(p.encode(personnels.getPassword()));

				//en enregistre le censeur dans la bd
				PersonnelsDAppui personnelsEnreg=personnelsDAppuiRepository.save(personnels);
				/*
				 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
				 */
				Roles roles1=rolesRepository.findByRole("AUTRES");
				if(roles1==null) return new Long(-6);
				UtilisateursRoles usersroles=new UtilisateursRoles();
				usersroles.setUsers((Utilisateurs)personnelsEnreg);
				usersroles.setRoleAssocie(roles1);

				usersrolesRepository.save(usersroles);

				return personnelsEnreg.getIdUsers();
	
	}
	
	public List<String> saveListEnseignants(List<Enseignants> listofenseignants){

		/*
		 * On enregistre les sg qui viennent d'une liste donc on va les attribuer le rôle 
		 */
		List<String> listofError= new ArrayList<String>();
		for(Enseignants ens : listofenseignants){
			//System.out.println("usersService enreg :"+ens.getNomsPers());
			Long val = this.saveEnseignants(ens);
			if(val.longValue() == -1){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Numéro CNI déjà utilisé :"+ens.getNomsPers()+" "+ens.getPrenomsPers()
					+" né le "+spd1.format(ens.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -2){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Ces noms sont déjà utilisés :"+ens.getNomsPers()+" "+ens.getPrenomsPers()
					+" né le "+spd1.format(ens.getDatenaissPers());
				listofError.add(erreur);
			}
			else if(val.longValue() == -3){
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String erreur = "Login déjà utilisé :"+ens.getNomsPers()+" "+ens.getPrenomsPers()
					+" né le "+spd1.format(ens.getDatenaissPers());
				listofError.add(erreur);
			}
		}
		return listofError;
	
	}
	
	public int saveUsersRoles(Long idUsers, String roleString){


		Utilisateurs users=usersService.findUtilisateurs(idUsers);
		Roles role=usersService.findRoles(roleString);

		UtilisateursRoles usersroles=new UtilisateursRoles();

		usersroles.setUsers(users);
		usersroles.setRoleAssocie(role);

		UtilisateursRoles userRoleExist = usersrolesRepository.getUtilisateursRoles(users.getIdUsers(), role.getRole());

		if(userRoleExist != null) return 1;

		UtilisateursRoles usersrolesenreg= usersrolesRepository.save(usersroles);

		if(usersrolesenreg!=null) return 1;

		return 0;
	
	}
	
	public int supprimerAllRoleUsers(Utilisateurs users){
		List<UtilisateursRoles> listofRolesUsers=(List<UtilisateursRoles>) users.getListofusersRoles();
		if(listofRolesUsers.size()!=0){
			for(UtilisateursRoles usersRoles : listofRolesUsers){
				usersrolesRepository.delete(usersRoles.getIdUsersRoles());
			}
			return 1;
		}
		return 0;
	}
	
	
	public int deleteUsers(Long idUsers) {

		/*log.log(Level.DEBUG, "lancement de deleteUsers avec ses rôles"+idUsers);*/
		Utilisateurs usersAssocie=usersService.findUtilisateurs(idUsers);
		List<UtilisateursRoles> listofRoles=(List<UtilisateursRoles>)usersAssocie.getListofusersRoles();
		for(UtilisateursRoles usersRoles : listofRoles){
			usersrolesRepository.delete(usersRoles);
		}
		////System.err.println("******************** On a supprimer les usersRoles associe censeurs**********************");

		Censeurs censeurAssocie=usersService.findCenseurs(idUsers);
		SG sgAssocie=usersService.findSG(idUsers);
		Intendant intendantAssocie=usersService.findIntendant(idUsers);
		Enseignants enseignantAssocie=usersService.findEnseignants(idUsers);
		if(censeurAssocie!=null){
			//alors c'est un censeur qu'on veut supprimer
			/*log.log(Level.DEBUG, "suppression du censeur "+idUsers);*/
			censeurRepository.delete(censeurAssocie);
			////System.err.println("******************** On a supprimer le censeur associe **********************");

			return 1;
		}
		else if(sgAssocie!=null){
			//alors c'est un sg qu'on veut supprimer
			/*log.log(Level.DEBUG, "suppression du SG "+idUsers);*/
			sgRepository.delete(sgAssocie);
			////System.err.println("******************** On a supprimer le sg associe **********************");
			return 1;
		}
		else if(intendantAssocie!=null){
			intendantRepository.delete(intendantAssocie);
			/*log.log(Level.DEBUG, "suppression de l'intendant "+idUsers);*/
			////System.err.println("******************** On a supprimer le intendant associe **********************");
			return 1;
		}
		else if(enseignantAssocie!=null){
			enseignantRepository.delete(enseignantAssocie);
			/*log.log(Level.DEBUG, "suppression de l'enseignant "+idUsers);*/
			////System.err.println("******************** On a supprimer le enseignant associe **********************");
			return 1;
		}
		/*log.log(Level.DEBUG, "fin de l'exécution de deleteUsers "+idUsers);*/
		return 0;
	
	}
	
	public Long saveEleves(Eleves eleve, Long idClasse){


		////System.err.println("le nom de la photos est "+eleve.getPhotoEleves());

		/*
		 * Est ce que le triplet noms prenoms datenaissance sera unique
		 */
		Eleves elevesExistName=usersService.findEleves(eleve.getNomsEleves(), eleve.getPrenomsEleves(), eleve.getDatenaissEleves());
		if(elevesExistName!=null) return new Long(0);

		/*
		 * Est ce que le matricule sera unique apres enregistrement
		 */
		Eleves elevesExistMatricule=usersService.findEleves(eleve.getMatriculeEleves());
		if(elevesExistMatricule!=null) {
			
			/*System.err.println("l'eleve "+elevesExistMatricule.getNomsEleves()+" a deja le matricule "
					+ "qui est entrain d'être utilisé pour enregistré un autre. son identifiant est "+elevesExistMatricule.getIdEleves()
					+" ce matricule est d'ailleurs "+elevesExistMatricule.getMatriculeEleves());*/
			return new Long(-1);
		}

		/*
		 * Maintenant on  recherche la classe dans laquelle enregistré l'élève
		 */
		Classes classeDenreg=usersService.findClasses(idClasse);
		if(classeDenreg==null) return new Long(-2);

		/*
		 * On est sur ici qu'on a trouve la classe et qu'il n'ya pas de conflit au niveau des contraintes
		 */
		/*
		 * On l'introduit donc dans sa classe
		 */
		eleve.setClasse(classeDenreg);
		/*
		 * Une fois la classe trouvée on doit enregistrée un compte pour cet élève en bd et l'indique
		 */
		CompteInscription compteEleve = new CompteInscription(new Double(0));
		CompteInscription cptEleveCree = compteinscriptionRepository.save(compteEleve);
		eleve.setCompteInscription(compteEleve);
		/*
		 * Puis on enregistre l'élève lui même
		 */
		Eleves eleveEnreg=elevesRepository.save(eleve);
		
		/*****************
		 * Enfin il faut mettre à jour le proprietaire du compte pour que la relation soient bidirectionnelle
		 * entre eleve et compteInscription
		 */
		cptEleveCree.setEleveProprietaire(eleveEnreg);
		compteinscriptionRepository.save(cptEleveCree);
		
		

		return eleveEnreg.getIdEleves();
	
	}
	
	public List<String> saveListEleves(List<Eleves> listofeleve, Long idClasse){

		List<String> listofError= new ArrayList<String>();
		for(Eleves eleve : listofeleve){
			Long val = this.saveEleves(eleve, idClasse);
			////System.out.println("enreg de eleve "+eleve.getNomsEleves()+" matricule "+eleve.getMatriculeEleves()+" == "+val);
			if(val.longValue()==0){
				//Le triplet noms prenoms datenaiss n'est pas unique donc erreur a signaler
				SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
				String triplet = eleve.getNomsEleves()+" "+
						eleve.getPrenomsEleves()+" né le "+
						spd1.format(eleve.getDatenaissEleves());
				listofError.add(triplet);
			}
		}
		return listofError;
	
	}
	
	
	public Long updateEleves(Eleves eleveModif, Long newidClasse){


		/*
		 * Recuperer l'eleve a modifier de la base de données
		 */
		Eleves eleveAModif=elevesRepository.findOne(eleveModif.getIdEleves());
		if(eleveAModif==null) return new Long(-3);

		/*
		 * Ici on a retrouve l'eleve a modifier
		 */
		if(!(eleveModif.getNomsEleves().equals(eleveAModif.getNomsEleves())) ||
				!(eleveModif.getPrenomsEleves().equals(eleveAModif.getPrenomsEleves()))||
				(eleveModif.getDatenaissEleves().getTime()!=eleveAModif.getDatenaissEleves().getTime())){

			Eleves elevesExistName=usersService.findEleves(eleveModif.getNomsEleves(), eleveModif.getPrenomsEleves(), 
					eleveModif.getDatenaissEleves());
			if(elevesExistName!=null) return new Long(0);

		}

		/*
		 * Est ce que le matricule sera unique apres enregistrement
		 */
		
		if(!(eleveModif.getMatriculeEleves().equals(eleveAModif.getMatriculeEleves()))){
			Eleves elevesExistMatricule=usersService.findEleves(eleveModif.getMatriculeEleves());
			if(elevesExistMatricule!=null) return new Long(-1);
		}

		/*
		 * Ici on est sur que le nouveau matricule et le nouveau triplet ne violeront aucune contrainte
		 * Maintenant on  recherche la classe dans laquelle enregistré l'élève
		 */

		if(newidClasse.longValue()!=eleveAModif.getClasse().getIdClasses().longValue()){
			Classes newclasseDenreg=usersService.findClasses(newidClasse);
			if(newclasseDenreg==null) return new Long(-2);
			//Ici on a retrouver la nouvelle classe et on peut donc l'introduire dans sa nouvelle classe
			eleveAModif.setClasse(newclasseDenreg);
		}

		//On peut donc modifier le reste des valeurs de l'objet avant de save 
		eleveAModif.setDatenaissEleves(eleveModif.getDatenaissEleves());
		eleveAModif.setEmailParent(eleveModif.getEmailParent());
		eleveAModif.setLieunaissEleves(eleveModif.getLieunaissEleves());
		eleveAModif.setMatriculeEleves(eleveModif.getMatriculeEleves());
		eleveAModif.setNationaliteEleves(eleveModif.getNationaliteEleves());
		eleveAModif.setNomsEleves(eleveModif.getNomsEleves());
		eleveAModif.setNumtel1Parent(eleveModif.getNumtel1Parent());
		if(eleveModif.getPhotoEleves()!=null){
			eleveAModif.setPhotoEleves(eleveModif.getPhotoEleves());
		}
		eleveAModif.setPrenomsEleves(eleveModif.getPrenomsEleves());
		eleveAModif.setQuartierEleves(eleveModif.getQuartierEleves());
		eleveAModif.setSexeEleves(eleveModif.getSexeEleves());
		eleveAModif.setStatutEleves(eleveModif.getStatutEleves());
		eleveAModif.setRedoublant(eleveModif.getRedoublant());

		Eleves eleveUpdate=elevesRepository.save(eleveAModif);


		return eleveUpdate.getIdEleves();
	
	}
	
	public int supprimerEleves(Long idElevesASupprim) {
		Eleves eleveASupprim=usersService.findEleves(idElevesASupprim);
		if(eleveASupprim!=null){
			elevesRepository.delete(eleveASupprim);
			return 1;
		}
		return 0;
	}
	
	public int swicthEtatPeriodesSeq(Long idPeriode) {

		Sequences seq = sequenceRepository.findOne(idPeriode);
		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "swicthEtatPeriodesSeq(actif, bloqué) "+idPeriode);*/
		if(seq == null) return 0;
		if(seq.isEtatPeriodes() == false) {
			if(seq.getTrimestre().isEtatPeriodes() == true){
				/*
				 * Avant de mettre à true il faut se rassurer que ca va être la seule la seule séquence active
				 * puisqu'on ne veut avoir qu'une seule période activer à la fois.
				 */
				List<Sequences> listofSeq = sequenceRepository.findAll();
				int compt = 0;
				for(Sequences seq1 : listofSeq){
					if(seq1.isEtatPeriodes() == true){
						compt+=1;
					}
				}
				if(compt == 0){
				seq.setEtatPeriodes(true);
				sequenceRepository.save(seq);
				return 2;
				}
				else{
					return 0;
				}
			}
			else{
				return -1;
			}
		}
		else{
			seq.setEtatPeriodes(false);
			return 1;
		}
		
	}
	
	
	public int swicthEtatPeriodesTrim(Long idPeriode) {

		Trimestres trim = trimestreRepository.findOne(idPeriode);
		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "swicthEtatPeriodesTrim(actif, bloqué) "+idPeriode);*/
		if(trim == null) return 0;
		for(Sequences seq : trim.getListofsequence()){
			if(seq.isEtatPeriodes()==true){
				return -1;
			}
		}
		
		if(trim.isEtatPeriodes() == false) {
			/*
			 * On active un trimestre lorsque les autres trimestres de l'annee en cours sont tous 
			 * desactivee
			 */
			int compt = 0;
			for(Trimestres trim1 : trim.getAnnee().getListoftrimestre()){
				if(trim1.isEtatPeriodes() == true){
					compt +=1;
				}
			}
			if(compt == 0){
				trim.setEtatPeriodes(true);
				trimestreRepository.save(trim);
				return 2;
			}
			else{
				return 0;
			}
		}
		else{
			trim.setEtatPeriodes(false);
			return 1;
		}
	
	}
	
	public int swicthEtatPeriodesAnnee(Long idPeriode) {

		Annee annee = anneeRepository.findOne(idPeriode);
		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "swicthEtatPeriodesAnnee(actif, bloqué) "+idPeriode);*/
		if(annee == null) return 0;
		if(annee.isEtatPeriodes() == false) {
			annee.setEtatPeriodes(true);
			anneeRepository.save(annee);
			return 2;
		}
		else{
			annee.setEtatPeriodes(false);
			return 1;
		}
	
	}
	
	
	public int setMontantScoClasse(Long idClasseAConfig, double montantScolarite){


		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "setMontantScoClasse ");*/
		Classes classeAConfig = usersService.findClasses(idClasseAConfig);

		if(classeAConfig == null) return 0;

		if(montantScolarite < 0) return -1;

		classeAConfig.setMontantScolarite(montantScolarite);

		classesRepository.save(classeAConfig);

		return 1;
	
	}
	
	public String getNextMatricule(String codeEtab, String annee) {

		String matricule = "";
		//System.err.println("anneeJour userService "+annee);
		String prefixe = codeEtab+annee+"-";
		
		if(matriculeRepository.findAll().size()>0){
			Matricule mat = matriculeRepository.findAll().get(0);
			int numero = mat.getValeur();
			int next_numero = numero+1;
			//On modifie et on enregistre pour qu'a chaque fois il n'y ait qu'un seul numero en BD
			mat.setValeur(next_numero);
			matriculeRepository.save(mat);//ca va modifier celui qu'on a pris en BD
			
			String suffixe = "";
			if((next_numero >= 0)&&(next_numero < 10)){
				suffixe = "000"+next_numero;
			}
			else if((next_numero >= 10)&&(next_numero < 100)){
				suffixe = "00"+next_numero;
			}
			if((next_numero >= 100)&&(next_numero < 1000)){
				suffixe = "0"+next_numero;
			}
			if((next_numero >= 1000)&&(next_numero < 10000)){
				suffixe = ""+next_numero;
			}
			matricule = prefixe+suffixe;
			//System.err.println("prefixe "+prefixe+" suffixe "+suffixe);
			return matricule;
			
			
		}
		else{
			/*
			 * Ceci signifie qu'il n'y a pas d'enregistrement dans la table matricule permettant 
			 * de fabriquer les matricules des élèves
			 * On doit donc faire un premier enregistrement
			 */
			int numero = 1;
			Matricule mat = new Matricule();
			mat.setValeur(numero);
			matriculeRepository.save(mat);
			
			String suffixe = "";
			if((numero >= 0)&&(numero < 10)){
				suffixe = "000"+numero;
			}
			else if((numero >= 10)&&(numero < 100)){
				suffixe = "00"+numero;
			}
			if((numero >= 100)&&(numero < 1000)){
				suffixe = "0"+numero;
			}
			if((numero >= 1000)&&(numero < 10000)){
				suffixe = ""+numero;
			}
			matricule = prefixe+suffixe;
			//System.err.println("prefixe "+prefixe+" suffixe "+suffixe);
			return matricule;
			
		}
			
	
	}
	
	public Collection<PersonnelBean> generateCollectionofPersonnelDeStatutBean(String statutPers){

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		
		List<Personnels> listofPersonnels = persRepository.findAll();
		int i = 1;
		for(Personnels p : listofPersonnels){
			if(p.getStatutPers().equalsIgnoreCase(statutPers)==true){
				PersonnelBean pb = new PersonnelBean();
				
				String noms_prenoms = (p.getNomsPers()+"  "+
						p.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(p.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+p.getLieunaissPers();
				String numcni = p.getNumcniPers();
				String sexe = p.getSexePers().toLowerCase();
				String nationalite = p.getNationalitePers();
				String grade = p.getGradePers();
				String statut = p.getStatutPers();
				String diplome = p.getDiplomePers();
				String numtel1 = p.getNumtel1Pers();
				String numtel2 = p.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = p.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = p.getMatriculePers();
				String quotah = p.getQuotaHorairePers()+"";
				String sitmatri = p.getSitmatriPers();
				String region = p.getRegionoriginePers();
				String observation = p.getObservations();
				String fonction = p.getFonctionPers();
				
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);		
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
				
			}
		}
		return listofPersonnelBean;
	
	}
	
	
	public Collection<PersonnelBean> generateCollectionofProffesseursDeStatutBean(String statutPers){


		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		
		List<Proffesseurs> listofPersonnels = proffRepository.findAll();
		int i = 1;
		for(Proffesseurs p : listofPersonnels){
			if(p.getStatutPers().equalsIgnoreCase(statutPers)==true){
				PersonnelBean pb = new PersonnelBean();
				
				String noms_prenoms = (p.getNomsPers()+"  "+
						p.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(p.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+p.getLieunaissPers();
				String numcni = p.getNumcniPers();
				String sexe = p.getSexePers().toLowerCase();
				String nationalite = p.getNationalitePers();
				String grade = p.getGradePers();
				String statut = p.getStatutPers();
				String diplome = p.getDiplomePers();
				String numtel1 = p.getNumtel1Pers();
				String numtel2 = p.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = p.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = p.getMatriculePers();
				String quotah = p.getQuotaHorairePers()+"";
				String sitmatri = p.getSitmatriPers();
				String region = p.getRegionoriginePers();
				String observation = p.getObservations();
				String fonction = p.getFonctionPers();
				String specialite = p.getSpecialiteProf();
				
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);
				pb.setSpecialite(specialite);
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
				
			}
		}
		
		return listofPersonnelBean;
	
	}
	
	
	
	public Collection<PersonnelBean> generateCollectionofIntendantBean() {

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<Intendant> listofInt = usersService.findAllIntendant();
		if(listofInt!=null){
			int i = 1;
			for(Intendant intendant : listofInt){
				String noms_prenoms = (intendant.getNomsPers()+"  "+
						intendant.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(intendant.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+intendant.getLieunaissPers();
				String numcni = intendant.getNumcniPers();
				String sexe = intendant.getSexePers().toLowerCase();
				String nationalite = intendant.getNationalitePers();
				String grade = intendant.getGradePers();
				String statut = intendant.getStatutPers();
				String diplome = intendant.getDiplomePers();
				String specialite = intendant.getSpecialiteProf();
				String fonction  = "INTENDANT";
				String numtel1 = intendant.getNumtel1Pers();
				String numtel2 = intendant.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = intendant.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = intendant.getMatriculePers();
				String quotah = intendant.getQuotaHorairePers()+"";
				String sitmatri = intendant.getSitmatriPers();
				String region = intendant.getRegionoriginePers();
				String observation = intendant.getObservations();
				
				PersonnelBean pb = new PersonnelBean();
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setSpecialite(specialite);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);		
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	
	}
	
	public Collection<PersonnelBean> generateCollectionofEnseignantBean() {

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<Enseignants> listofEns = usersService.findAllEnseignants();
		if(listofEns!=null){
			int i = 1;
			for(Enseignants ens : listofEns){
				String noms_prenoms = (ens.getNomsPers()+"  "+
						ens.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(ens.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+ens.getLieunaissPers();
				String numcni = ens.getNumcniPers();
				String sexe = ens.getSexePers().toLowerCase();
				String nationalite = ens.getNationalitePers();
				String grade = ens.getGradePers();
				String statut = ens.getStatutPers();
				String diplome = ens.getDiplomePers();
				String specialite = ens.getSpecialiteProf();
				String fonction  = "ENSEIGNANT";
				String numtel1 = ens.getNumtel1Pers();
				String numtel2 = ens.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = ens.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = ens.getMatriculePers();
				String quotah = ens.getQuotaHorairePers()+"";
				String sitmatri = ens.getSitmatriPers();
				String region = ens.getRegionoriginePers();
				String observation = ens.getObservations();
				
				PersonnelBean pb = new PersonnelBean();
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setSpecialite(specialite);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	
	}
	
	public Collection<PersonnelBean> generateCollectionofSgBean() {

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<SG> listofSg = usersService.findAllSG();
		if(listofSg!=null){
			int i = 1;
			for(SG sg : listofSg){
				String noms_prenoms = (sg.getNomsPers()+"  "+
						sg.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(sg.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+sg.getLieunaissPers();
				String numcni = sg.getNumcniPers();
				String sexe = sg.getSexePers().toLowerCase();
				String nationalite = sg.getNationalitePers();
				String grade = sg.getGradePers();
				String statut = sg.getStatutPers();
				String diplome = sg.getDiplomePers();
				String specialite = sg.getSpecialiteProf();
				String fonction  = "SG";
				String numtel1 = sg.getNumtel1Pers();
				String numtel2 = sg.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = sg.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = sg.getMatriculePers();
				String quotah = sg.getQuotaHorairePers()+"";
				String sitmatri = sg.getSitmatriPers();
				String region = sg.getRegionoriginePers();
				String observation = sg.getObservations();
				
				PersonnelBean pb = new PersonnelBean();
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setSpecialite(specialite);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);		
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	
	}
	
	public Collection<PersonnelBean> generateCollectionofCenseurBean() {

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
				List<Censeurs> listofCenseur = usersService.findAllCenseurs();
				if(listofCenseur!=null){
					int i=1;
					for(Censeurs c : listofCenseur){
						String noms_prenoms = (c.getNomsPers()+"  "+
								c.getPrenomsPers()).toUpperCase();
						SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
						String date = spd.format(c.getDatenaissPers());
						String date_naiss = date;
						String lieu_naiss = "à "+c.getLieunaissPers();
						String numcni = c.getNumcniPers();
						String sexe = c.getSexePers().toLowerCase();
						String nationalite = c.getNationalitePers();
						String grade = c.getGradePers();
						String statut = c.getStatutPers();
						String diplome = c.getDiplomePers();
						String specialite = c.getSpecialiteProf();
						String fonction  = "CENSEUR";
						String numtel1 = c.getNumtel1Pers();
						String numtel2 = c.getNumtel2Pers();
						if(numtel2.isEmpty()==false){
							numtel1 +="/";
							numtel1 +=numtel2;
						}
						String adresse = c.getEmailPers();
						String numero = ""+i;
						i++;
						String matricule = c.getMatriculePers();
						String quotah = c.getQuotaHorairePers()+"";
						String sitmatri = c.getSitmatriPers();
						String region = c.getRegionoriginePers();
						String observation = c.getObservations();
						
						PersonnelBean pb = new PersonnelBean();
						pb.setAdresse(adresse);
						pb.setDate_naiss(date_naiss);
						pb.setLieu_naiss(lieu_naiss);
						pb.setDiplome(diplome);
						pb.setSpecialite(specialite);
						pb.setFonction(fonction);
						pb.setGrade(grade);
						pb.setStatut(statut);
						pb.setNoms_prenoms(noms_prenoms);
						pb.setNumcni(numcni);
						pb.setSexe(sexe);
						pb.setNationalite(nationalite);
						pb.setNumtel1(numtel1);
						pb.setNumtel2(numtel2);
						
						pb.setNumero(numero);
						pb.setMatricule(matricule);
						pb.setQuotah(quotah);
						pb.setSitmatri(sitmatri);
						pb.setRegion(region);
						pb.setObservation(observation);
						
						listofPersonnelBean.add(pb);
					}
				}
		return listofPersonnelBean;
	
	}
	
	public Collection<PersonnelBean> generateCollectionofPersonnelBean(){

		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofPersonnelBean ");*/
		//On commence par le proviseur ou le chef d'etablissement
		List<Proviseur> listofProviseur = usersService.findAllProviseur();
		if(listofProviseur!=null){
			if(listofProviseur.size()>=1){
				Proviseur proviseur = listofProviseur.get(0);
				
				String noms_prenoms = (proviseur.getNomsPers()+"  "+
						proviseur.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(proviseur.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+proviseur.getLieunaissPers();
				String numcni = proviseur.getNumcniPers();
				String sexe = proviseur.getSexePers().toLowerCase();
				String nationalite = proviseur.getNationalitePers();
				String grade = proviseur.getGradePers();
				String statut = proviseur.getStatutPers();
				String diplome = proviseur.getDiplomePers();
				String specialite = proviseur.getSpecialiteProf();
				String fonction  = "CHEF D'ETABLISSEMENT";
				String numtel1 = proviseur.getNumtel1Pers();
				String numtel2 = proviseur.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = proviseur.getEmailPers();
				String numero = ""+1;
				String matricule = proviseur.getMatriculePers();
				String quotah = proviseur.getQuotaHorairePers()+"";
				String sitmatri = proviseur.getSitmatriPers();
				String region = proviseur.getRegionoriginePers();
				String observation = proviseur.getObservations();
				
				PersonnelBean pb = new PersonnelBean();
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setSpecialite(specialite);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
			}
		}
		
		//On continue avec la liste des censeurs
		List<Censeurs> listofCenseur = usersService.findAllCenseurs();
		if(listofCenseur!=null){
			int i = 1;
			for(Censeurs c : listofCenseur){
				String noms_prenoms = (c.getNomsPers()+"  "+
						c.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(c.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+c.getLieunaissPers();
				String numcni = c.getNumcniPers();
				String sexe = c.getSexePers().toLowerCase();
				String nationalite = c.getNationalitePers();
				String grade = c.getGradePers();
				String statut = c.getStatutPers();
				String diplome = c.getDiplomePers();
				String specialite = c.getSpecialiteProf();
				String fonction  = "CENSEUR";
				String numtel1 = c.getNumtel1Pers();
				String numtel2 = c.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = c.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = c.getMatriculePers();
				String quotah = c.getQuotaHorairePers()+"";
				String sitmatri = c.getSitmatriPers();
				String region = c.getRegionoriginePers();
				String observation = c.getObservations();
				
				PersonnelBean pb = new PersonnelBean();
				pb.setAdresse(adresse);
				pb.setDate_naiss(date_naiss);
				pb.setLieu_naiss(lieu_naiss);
				pb.setDiplome(diplome);
				pb.setSpecialite(specialite);
				pb.setFonction(fonction);
				pb.setGrade(grade);
				pb.setStatut(statut);
				pb.setNoms_prenoms(noms_prenoms);
				pb.setNumcni(numcni);
				pb.setSexe(sexe);
				pb.setNationalite(nationalite);
				pb.setNumtel1(numtel1);
				pb.setNumtel2(numtel2);
				
				pb.setNumero(numero);
				pb.setMatricule(matricule);
				pb.setQuotah(quotah);
				pb.setSitmatri(sitmatri);
				pb.setRegion(region);
				pb.setObservation(observation);
				
				listofPersonnelBean.add(pb);
			}
		}
		
		//On continue avec la liste des SG
				List<SG> listofSg = usersService.findAllSG();
				if(listofSg!=null){
					int i = 1;
					for(SG sg : listofSg){
						String noms_prenoms = (sg.getNomsPers()+"  "+
								sg.getPrenomsPers()).toUpperCase();
						SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
						String date = spd.format(sg.getDatenaissPers());
						String date_naiss = date;
						String lieu_naiss = "à "+sg.getLieunaissPers();
						String numcni = sg.getNumcniPers();
						String sexe = sg.getSexePers().toLowerCase();
						String nationalite = sg.getNationalitePers();
						String grade = sg.getGradePers();
						String statut = sg.getStatutPers();
						String diplome = sg.getDiplomePers();
						String specialite = sg.getSpecialiteProf();
						String fonction  = "SG";
						String numtel1 = sg.getNumtel1Pers();
						String numtel2 = sg.getNumtel2Pers();
						if(numtel2.isEmpty()==false){
							numtel1 +="/";
							numtel1 +=numtel2;
						}
						String adresse = sg.getEmailPers();
						String numero = ""+i;
						i++;
						String matricule = sg.getMatriculePers();
						String quotah = sg.getQuotaHorairePers()+"";
						String sitmatri = sg.getSitmatriPers();
						String region = sg.getRegionoriginePers();
						String observation = sg.getObservations();
						
						PersonnelBean pb = new PersonnelBean();
						pb.setAdresse(adresse);
						pb.setDate_naiss(date_naiss);
						pb.setLieu_naiss(lieu_naiss);
						pb.setDiplome(diplome);
						pb.setSpecialite(specialite);
						pb.setFonction(fonction);
						pb.setGrade(grade);
						pb.setStatut(statut);
						pb.setNoms_prenoms(noms_prenoms);
						pb.setNumcni(numcni);
						pb.setSexe(sexe);
						pb.setNationalite(nationalite);
						pb.setNumtel1(numtel1);
						pb.setNumtel2(numtel2);		
						
						pb.setNumero(numero);
						pb.setMatricule(matricule);
						pb.setQuotah(quotah);
						pb.setSitmatri(sitmatri);
						pb.setRegion(region);
						pb.setObservation(observation);
						
						listofPersonnelBean.add(pb);
					}
				}
				
				//On continue avec la liste des Enseignants
				List<Enseignants> listofEns = usersService.findAllEnseignants();
				if(listofEns!=null){
					int i = 1;
					for(Enseignants ens : listofEns){
						String noms_prenoms = (ens.getNomsPers()+"  "+
								ens.getPrenomsPers()).toUpperCase();
						SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
						String date = spd.format(ens.getDatenaissPers());
						String date_naiss = date;
						String lieu_naiss = "à "+ens.getLieunaissPers();
						String numcni = ens.getNumcniPers();
						String sexe = ens.getSexePers().toLowerCase();
						String nationalite = ens.getNationalitePers();
						String grade = ens.getGradePers();
						String statut = ens.getStatutPers();
						String diplome = ens.getDiplomePers();
						String specialite = ens.getSpecialiteProf();
						String fonction  = "ENSEIGNANT";
						String numtel1 = ens.getNumtel1Pers();
						String numtel2 = ens.getNumtel2Pers();
						if(numtel2.isEmpty()==false){
							numtel1 +="/";
							numtel1 +=numtel2;
						}
						String adresse = ens.getEmailPers();
						String numero = ""+i;
						i++;
						String matricule = ens.getMatriculePers();
						String quotah = ens.getQuotaHorairePers()+"";
						String sitmatri = ens.getSitmatriPers();
						String region = ens.getRegionoriginePers();
						String observation = ens.getObservations();
						
						PersonnelBean pb = new PersonnelBean();
						pb.setAdresse(adresse);
						pb.setDate_naiss(date_naiss);
						pb.setLieu_naiss(lieu_naiss);
						pb.setDiplome(diplome);
						pb.setSpecialite(specialite);
						pb.setFonction(fonction);
						pb.setGrade(grade);
						pb.setStatut(statut);
						pb.setNoms_prenoms(noms_prenoms);
						pb.setNumcni(numcni);
						pb.setSexe(sexe);
						pb.setNationalite(nationalite);
						pb.setNumtel1(numtel1);
						pb.setNumtel2(numtel2);
						
						pb.setNumero(numero);
						pb.setMatricule(matricule);
						pb.setQuotah(quotah);
						pb.setSitmatri(sitmatri);
						pb.setRegion(region);
						pb.setObservation(observation);
						
						listofPersonnelBean.add(pb);
					}
				}
				
				//On continue avec la liste des Enseignants
				List<Intendant> listofInt = usersService.findAllIntendant();
				if(listofInt!=null){
					int i = 1;
					for(Intendant intendant : listofInt){
						String noms_prenoms = (intendant.getNomsPers()+"  "+
								intendant.getPrenomsPers()).toUpperCase();
						SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
						String date = spd.format(intendant.getDatenaissPers());
						String date_naiss = date;
						String lieu_naiss = "à "+intendant.getLieunaissPers();
						String numcni = intendant.getNumcniPers();
						String sexe = intendant.getSexePers().toLowerCase();
						String nationalite = intendant.getNationalitePers();
						String grade = intendant.getGradePers();
						String statut = intendant.getStatutPers();
						String diplome = intendant.getDiplomePers();
						String specialite = intendant.getSpecialiteProf();
						String fonction  = "INTENDANT";
						String numtel1 = intendant.getNumtel1Pers();
						String numtel2 = intendant.getNumtel2Pers();
						
						if(numtel2.isEmpty()==false){
							numtel1 +="/";
							numtel1 +=numtel2;
						}
						String adresse = intendant.getEmailPers();
						String numero = ""+i;
						i++;
						String matricule = intendant.getMatriculePers();
						String quotah = intendant.getQuotaHorairePers()+"";
						String sitmatri = intendant.getSitmatriPers();
						String region = intendant.getRegionoriginePers();
						String observation = intendant.getObservations();
						
						PersonnelBean pb = new PersonnelBean();
						pb.setAdresse(adresse);
						pb.setDate_naiss(date_naiss);
						pb.setLieu_naiss(lieu_naiss);
						pb.setDiplome(diplome);
						pb.setSpecialite(specialite);
						pb.setFonction(fonction);
						pb.setGrade(grade);
						pb.setStatut(statut);
						pb.setNoms_prenoms(noms_prenoms);
						pb.setNumcni(numcni);
						pb.setSexe(sexe);
						pb.setNationalite(nationalite);
						pb.setNumtel1(numtel1);
						pb.setNumtel2(numtel2);		
						
						pb.setNumero(numero);
						pb.setMatricule(matricule);
						pb.setQuotah(quotah);
						pb.setSitmatri(sitmatri);
						pb.setRegion(region);
						pb.setObservation(observation);
						
						listofPersonnelBean.add(pb);
					}
				}
		
		return listofPersonnelBean;
	
	}
	
	public Collection<FicheScolariteparClasseBean> generateListFicheScolariteparClasseBean(){

		List<FicheScolariteparClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheScolariteparClasseBean>();
		
		List<Classes> listofClasse = usersService.findAllClasse();
		
		for(Classes cl : listofClasse){
			FicheScolariteparClasseBean fspcb = new FicheScolariteparClasseBean();
			
			fspcb.setClasse(cl.getClasseString());
			if(cl.getMontantScolarite()>0) fspcb.setMontantscolarite(cl.getMontantScolarite()+" F cfa");
			fspcb.setNiveau(cl.getNiveau().getNiveauString());
			
			listofFicheRecapAbsenceClasseBean.add(fspcb);
		}
		
		return listofFicheRecapAbsenceClasseBean;
	
	}
	
	
	
	
	
}
