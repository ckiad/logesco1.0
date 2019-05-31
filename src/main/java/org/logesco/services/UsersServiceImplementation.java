/**
 * 
 */
package org.logesco.services;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logesco.dao.*;
import org.logesco.entities.*;
import org.logesco.modeles.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author cedrickiadjeu
 *
 */
@Service
@Transactional
public class UsersServiceImplementation implements IUsersService {

	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Autowired
	private EtablissementRepository etabRepository;

	@Autowired
	private UtilisateursRepository usersRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UtilisateursRolesRepository usersrolesRepository;
	
	@Autowired
	private PersonnelsRepository persRepository;
	
	@Autowired
	private ProffesseursRepository proffRepository;
	
	@Autowired
	private ProviseurRepository provRepository;
	
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
	private CyclesRepository cycleRepository;

	@Autowired
	private ElevesRepository elevesRepository;

	@Autowired
	private CompteInscriptionRepository compteinscriptionRepository;

	@Autowired
	private OperationsRepository operationsRepository;

	@Autowired
	private MatieresRepository	matieresRepository;

	@Autowired
	private CoursRepository	coursRepository;

	@Autowired
	private NiveauxRepository	niveauxRepository;

	@Autowired
	private SequencesRepository	 sequenceRepository;

	@Autowired
	private TrimestresRepository	 trimestreRepository;

	@Autowired
	private AnneeRepository	 anneeRepository;

	@Autowired
	private EvaluationsRepository	 evalRepository;

	@Autowired
	private NotesEvalRepository	 notesEvalRepository;

	@Autowired
	private RapportDAbsenceRepository	 rapportDAbsenceRepository;
	
	@Autowired
	private MatriculeRepository	 matriculeRepository;
	
	@Autowired
	private RapportDisciplinaireRepository	 rapportdisciplinaireRepository;
	
	@Autowired
	private SanctionDisciplinaireRepository	 sanctionDiscRepository;
	
	@Autowired
	private DecisionRepository	 decisionRepository;
	
	@Autowired
	private DecisionConseilRepository	 decisionConseilRepository;
	
	@Autowired
	private SanctionTravailRepository	 sanctionTravRepository;
	
	@Autowired
	private IdentOperationRepository	 identOpRepository;
	
	private UtilitairesBulletins ub;
	
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

	/*
	 * Début de l'implémentation des méthodes de l'interface
	 */
	
	/**
	 * @param utilitaireBulletin
	 */
	public UsersServiceImplementation() {
		super();
		this.ub = new UtilitairesBulletins();
	}
	

	@Override
	public Etablissement getEtablissement() {
		/*log.log(Level.DEBUG, "getEtablissement(): chargement de l'etablissement a gérer par un User");*/
		ArrayList<Etablissement> listEtab=(ArrayList<Etablissement>) etabRepository.findAll();
		if(!(listEtab.isEmpty())) return listEtab.get(0);
		/*log.log(Level.WARN, "Erreur de recuperation de l'établissement dans getEtablissement()");*/
		return null;
	}


	


	@Override
	public Personnels findPersonnelsByUsername(String username) {
		/*
		 * Il faut faire la liste de tous le personnels et dans cette liste retourner celui qui a pour username celui passe en 
		 * paramètre
		 */
		List<Personnels> listofPersonnels=persRepository.findAll();
		for(Personnels pers : listofPersonnels){
			if(pers.getUsername().equals(username)) return pers;
		}
		//Ici signifie qu'on a rien trouvé dans le for donc on doit retourner null
		return null;
	}


	@Override
	public Utilisateurs findByUsername(String username) {
		Utilisateurs usersAssocie=usersRepository.findByUsername(username);
		return usersAssocie;
	}
	
	@Override
	public int possedeRole(Utilisateurs users, String role){
		int r=0;
		List<UtilisateursRoles> listofUsersRoles = (List<UtilisateursRoles>) users.getListofusersRoles();
		for(UtilisateursRoles userRole : listofUsersRoles){
			String roleAssocie = userRole.getRoleAssocie().getRole();
			if(roleAssocie.toLowerCase().equals(role.toLowerCase())==true){
				r=1;
			}
		}
		return r;
	}

	public Utilisateurs findUtilisateurs(Long idUsers){
		Utilisateurs usersAssocie=usersRepository.findOne(idUsers);
		return usersAssocie;
	}

	@Override
	public Personnels findPersonnel(String numcniPers){
		return persRepository.findByNumcniPers(numcniPers);
	}

	@Override
	public Personnels findPersonnel(Long idUsers){
		return persRepository.findOne(idUsers);
	}

	@Override
	public Personnels findPersonnel(String nomPers, String prenomsPers, Date datenaisspers){
		return persRepository.findByNomsPersAndPrenomsPersAndDatenaissPers(nomPers, prenomsPers, datenaisspers);
	}

	@Override
	public Utilisateurs getUsers(String username) {
		Utilisateurs users=usersRepository.getUtilisateursByUsername(username);
		return users;
	}

	@Override
	public Censeurs findCenseur(int numeroCenseur) {
		Censeurs censeur=censeurRepository.findByNumeroCens(numeroCenseur);
		return censeur;
	}


	@Override
	public SG findSG(int numeroSG) {
		SG sg=sgRepository.findByNumeroSg(numeroSG);
		return sg;
	}


	@Override
	public Intendant findIntendant(int numeroIntendant) {
		Intendant intendant=intendantRepository.findByNumeroInt(numeroIntendant);
		return intendant;
	}

	@Override
	public Page<Eleves> findListElevesSelonMotif(String motif, int numPage, int taillePage){
		return elevesRepository.findPageElevesSelonMotif(motif, new PageRequest(numPage, taillePage));
	}

	@Override
	public Long updateProffesseurs(Long idUsers, Proffesseurs proffesseurs){
		/*log.log(Level.DEBUG, "Lancement de updateProffesseurs: ");*/
		Long idProfModif=new Long(-10);

		Proffesseurs profAModif=this.findProffesseurs(idUsers);

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
				Personnels persExistantAvectriplet=this.findPersonnel(proffesseurs.getNomsPers(), proffesseurs.getPrenomsPers(), 
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
				Personnels persExistantAvecNumcni=this.findPersonnel(proffesseurs.getNumcniPers());
				if(persExistantAvecNumcni!=null) return new Long(-1);
			}

			/*
			 * Pour le username
			 */
			if(!(proffesseurs.getUsername().equals(profAModif.getUsername()))){
				Utilisateurs usersExistAvecUsername=this.getUsers(proffesseurs.getUsername());
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
			 * On peut maintenant faire la mise à jour
			 */
			Proffesseurs profUpdate=proffRepository.save(profAModif);
			////System.err.println("Nous voici a l'execution   777");
			idProfModif=profUpdate.getIdUsers();
		}
		/*log.log(Level.DEBUG, "fin de l'exécution de updateProffesseurs");*/
		return idProfModif;
	}

	@Override
	public Long saveCenseurs(Censeurs censeur, int roleCode) {
		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		System.err.println("Nous voici a l'execution   TOUT DEBUTE 1111");
		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=this.findPersonnel(censeur.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}
		System.err.println("Nous voici a l'execution   2222");

		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=this.findPersonnel(censeur.getNomsPers(), censeur.getPrenomsPers(), 
				censeur.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}
		System.err.println("Nous voici a l'execution   3333");

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=this.getUsers(censeur.getUsername());
		if(usersExistAvecUsername!=null) {
			System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}
		System.err.println("Nous voici a l'execution   4444");

		/*
		 * Est ce qu'un censeur existe avec le même numéro ?
		 */
		Censeurs censeurExistAvecNumero=this.findCenseur(censeur.getNumeroCens());
		if(censeurExistAvecNumero!=null) {
			System.err.println("Voici censeurExistAvecNumero  "+censeurExistAvecNumero.toString());
			return new Long(-4);
		}
		System.err.println("Nous voici a l'execution   5555");

		/*
		 * Ici on peut donc effectuer l'enregistrement sans souci car toutes les contraintes sont vérifié
		 * Pour cela il faut déjà encoder le mot de passe et recuperer les rôles dans la base de données
		 * afin de créer les UtilisateursRoles
		 */
		//On encode donc le mot de passe
		censeur.setPassword(p.encode(censeur.getPassword()));

		//en enregistre le censeur dans la bd
		Censeurs censeurEnreg=censeurRepository.save(censeur);
		System.err.println("Nous voici a l'execution   6666");
		/*
		 * On recupere les rôle jouer dans la bd sachant qu'il a d'abord le rôle CENSEUR
		 */
		Roles roles1=rolesRepository.findByRole("CENSEUR");
		if(roles1==null) return new Long(-5);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)censeurEnreg);
		usersroles.setRoleAssocie(roles1);
		System.err.println("Nous voici a l'execution   7777");

		usersrolesRepository.save(usersroles);

		if(roleCode==1){
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
		System.err.println("Nous voici a l'execution   9999");

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
			System.out.println("valretour usersService enreg cens :"+val);
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

	@Override
	public int updateNumeroCenseurs(Long idUsers, int newNumero){
		Censeurs censeursAModif=this.findCenseurs(idUsers);
		if(censeursAModif==null) return 0;
		/*
		 *On verifie la contrainte  Pour le numeroCens
		 */

		Censeurs censeursAvecNumero=this.findCenseur(newNumero);
		if(censeursAvecNumero!=null)  return 0;

		censeursAModif.setNumeroCens(newNumero);
		censeurRepository.save(censeursAModif);

		return 1;
	}

	@Override
	public Long saveSG(SG sg, int roleCode) {
		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=this.findPersonnel(sg.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=this.findPersonnel(sg.getNomsPers(), sg.getPrenomsPers(), 
				sg.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			////System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=this.getUsers(sg.getUsername());
		if(usersExistAvecUsername!=null) {
			////System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}

		/*
		 * Est ce qu'un sg existe avec le même numéro ?
		 */
		SG sgExistAvecNumero=this.findSG(sg.getNumeroSG());
		if(sgExistAvecNumero!=null) {
			//System.err.println("Voici censeurExistAvecNumero  "+sgExistAvecNumero.toString());
			return new Long(-4);
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
		if(roles1==null) return new Long(-5);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)sgEnreg);
		usersroles.setRoleAssocie(roles1);

		usersrolesRepository.save(usersroles);

		if(roleCode==3){
			//Alors il a le role censeur et enseignant
			Roles roles2=rolesRepository.findByRole("ENSEIGNANT");
			if(roles2==null) return new Long(-6);
			//On enregistre ce role pour cet utilisateur
			UtilisateursRoles usersroles1=new UtilisateursRoles();
			usersroles1.setUsers((Utilisateurs)sgEnreg);
			usersroles1.setRoleAssocie(roles2);

			usersrolesRepository.save(usersroles1);
		}

		return sgEnreg.getIdUsers();
	}

	public List<String> saveListSG(List<SG> listofsg){
		/*
		 * On enregistre les sg qui viennent d'une liste donc on va les attribuer le rôle 
		 */
		List<String> listofError= new ArrayList<String>();
		for(SG sg : listofsg){
			System.out.println("usersService enreg SG:"+sg.getNomsPers());
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
	

	@Override
	public int updateNumeroSG(Long idUsers, int newNumero){
		SG sgAModif=this.findSG(idUsers);
		if(sgAModif==null) return 0;
		/*
		 * On verifie la contrainte sur Pour le numeroSG
		 */

		SG sgAvecNumero=this.findSG(newNumero);
		if(sgAvecNumero!=null) return 0;

		sgAModif.setNumeroSG(newNumero);

		return 1;
	}

	@Override
	public Long saveIntendant(Intendant intendant, int roleCode) {
		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=this.findPersonnel(intendant.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=this.findPersonnel(intendant.getNomsPers(), intendant.getPrenomsPers(), 
				intendant.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=this.getUsers(intendant.getUsername());
		if(usersExistAvecUsername!=null) {
			//System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
		}

		/*
		 * Est ce qu'un sg existe avec le même numéro ?
		 */
		Intendant intendantExistAvecNumero=this.findIntendant(intendant.getNumeroInt());
		if(intendantExistAvecNumero!=null) {
			//System.err.println("Voici censeurExistAvecNumero  "+intendantExistAvecNumero.toString());
			return new Long(-4);
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
		if(roles1==null) return new Long(-5);
		UtilisateursRoles usersroles=new UtilisateursRoles();
		usersroles.setUsers((Utilisateurs)intendantEnreg);
		usersroles.setRoleAssocie(roles1);

		usersrolesRepository.save(usersroles);

		if(roleCode==5){
			//Alors il a le role censeur et enseignant
			Roles roles2=rolesRepository.findByRole("ENSEIGNANT");
			if(roles2==null) return new Long(-6);
			//On enregistre ce role pour cet utilisateur
			UtilisateursRoles usersroles1=new UtilisateursRoles();
			usersroles1.setUsers((Utilisateurs)intendantEnreg);
			usersroles1.setRoleAssocie(roles2);

			usersrolesRepository.save(usersroles1);
		}

		return intendantEnreg.getIdUsers();
	}


	@Override
	public int updateNumeroIntendant(Long idUsers, int newNumero){
		Intendant intendantAModif=this.findIntendant(idUsers);
		if(intendantAModif==null) return 0;
		/*
		 *On verifie la contrainte  Pour le numeroInt
		 */

		Intendant intendantAvecNumero=this.findIntendant(newNumero);
		if(intendantAvecNumero!=null) return 0;

		intendantAModif.setNumeroInt(newNumero);

		return 1;
	}

	@Override
	public Long saveEnseignants(Enseignants enseignants) {
		//Pour encoder les mots de passe
		Pbkdf2PasswordEncoder p=new Pbkdf2PasswordEncoder();

		/*
		 * Est ce que quelqu'un existe en bd avec le meme numerocni?
		 */
		Personnels persExistantAvecNumcni=this.findPersonnel(enseignants.getNumcniPers());
		if(persExistantAvecNumcni!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvecNumcni.toString());
			return new Long(-1);
		}

		/*
		 * Est ce que quelqu'un existe en bd avec le meme triplet noms, prenoms datenaiss
		 */
		Personnels persExistantAvectriplet=this.findPersonnel(enseignants.getNomsPers(), enseignants.getPrenomsPers(), 
				enseignants.getDatenaissPers());
		if(persExistantAvectriplet!=null) {
			//System.err.println("Voici persExistantAvecNumcni  "+persExistantAvectriplet.toString());
			return new Long(-2);
		}

		/*
		 * Est ce qu'un utilisateur existe donc avec le même username
		 */
		Utilisateurs usersExistAvecUsername=this.getUsers(enseignants.getUsername());
		if(usersExistAvecUsername!=null) {
			//System.err.println("Voici usersExistAvecUsername  "+usersExistAvecUsername.toString());
			return new Long(-3);
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
	
	public List<String> saveListEnseignants(List<Enseignants> listofenseignants){
		/*
		 * On enregistre les sg qui viennent d'une liste donc on va les attribuer le rôle 
		 */
		List<String> listofError= new ArrayList<String>();
		for(Enseignants ens : listofenseignants){
			System.out.println("usersService enreg :"+ens.getNomsPers());
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


	@Override
	public Roles findRoles(String role){
		return rolesRepository.findByRole(role);
	}

	@Override
	public int saveUsersRoles(Long idUsers, String roleString){

		Utilisateurs users=this.findUtilisateurs(idUsers);
		Roles role=this.findRoles(roleString);

		UtilisateursRoles usersroles=new UtilisateursRoles();

		usersroles.setUsers(users);
		usersroles.setRoleAssocie(role);

		UtilisateursRoles userRoleExist = usersrolesRepository.getUtilisateursRoles(users.getIdUsers(), role.getRole());

		if(userRoleExist != null) return 1;

		UtilisateursRoles usersrolesenreg= usersrolesRepository.save(usersroles);

		if(usersrolesenreg!=null) return 1;

		return 0;
	}


	@Override
	public Proffesseurs findProffesseurs(String numcniPers) {
		Proffesseurs proffAssocie=proffRepository.findByNumcniPers(numcniPers);

		return proffAssocie;
	}


	@Override
	public List<Roles> findListofRoles(Utilisateurs users) {
		List<UtilisateursRoles> listofusersRoles=(List<UtilisateursRoles>) users.getListofusersRoles();
		List<Roles> listofRoles=new ArrayList<Roles>();
		for(UtilisateursRoles usersRoles : listofusersRoles){
			listofRoles.add(usersRoles.getRoleAssocie());
		}
		return listofRoles;
	}


	@Override
	public int getcodeUsersRole(Utilisateurs users) {
		//System.err.println("debut de getcodeUsersRole ");
		List<UtilisateursRoles> listofUsersRoles=(List<UtilisateursRoles>) users.getListofusersRoles();
		//System.err.println("listofUsersRoles "+listofUsersRoles.size());
		int codeRole=0;
		int containsRoles=0;

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("CENSEUR"))) containsRoles=1;
			//System.err.println("usersRoles "+usersRoles.getRoleAssocie().getRole()+" containsRoles == "+containsRoles);
		}

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("SG"))) containsRoles=2;
		}

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("INTENDANT"))) containsRoles=3;
		}

		if(containsRoles==1){//donc il est censeur
			int ensrole=0;
			for(UtilisateursRoles usersRoles : listofUsersRoles){
				//lorsque le si est verifie alors il est en plus enseignant; selon les code on doit avoir 1 comme code
				if(usersRoles.getRoleAssocie().getRole().equals(new String("ENSEIGNANT"))) ensrole=1;
				//System.err.println("A t'il enseignant comme role en plus de censeur? "+" ensrole == "+ensrole);
			}
			if(ensrole==0) codeRole=2;//role censeur uniquement
			if(ensrole==1) codeRole=1;//role censeur et ennseignant
		}

		if(containsRoles==2){//donc il est SG
			int ensrole=0;
			for(UtilisateursRoles usersRoles : listofUsersRoles){
				//en plus de SG si le if est verifie alors il est en plus enseignant donc 3 comme code
				if(usersRoles.getRoleAssocie().getRole().equals(new String("ENSEIGNANT"))) ensrole=1;
				//System.err.println("A t'il enseignant comme role en plus de sg? "+" ensrole == "+ensrole);
			}
			if(ensrole==0) codeRole=4;//role SG uniquement
			if(ensrole==1) codeRole=3;//role SG et ennseignant
		}

		if(containsRoles==3){//donc il est d'abord Intendant
			int ensrole=0;
			for(UtilisateursRoles usersRoles : listofUsersRoles){
				if(usersRoles.getRoleAssocie().getRole().equals(new String("ENSEIGNANT"))) ensrole=1;
				//System.err.println("A t'il enseignant comme role en plus de intendant? "+" ensrole == "+ensrole);
			}
			if(ensrole==0) codeRole=6;//role Intendant uniquement
			if(ensrole==1) codeRole=5;//role Intendant et ennseignant
		}

		if((containsRoles!=1)&&(containsRoles!=2)&&(containsRoles!=3)){//ni censeur ni sg ni intendant
			for(UtilisateursRoles usersRoles : listofUsersRoles){
				if(usersRoles.getRoleAssocie().getRole().equals(new String("ENSEIGNANT"))) codeRole=7;//donc juste role Enseignant
			}
		}

		return codeRole;
	}

	@Override
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

	@Override
	public Censeurs findCenseurs(Long idUsers) {

		return censeurRepository.findByIdUsers(idUsers);
	}


	@Override
	public Censeurs findCenseurs(String numeroCniPers) {

		return censeurRepository.findByNumcniPers(numeroCniPers);
	}

	@Override
	public SG findSG(Long idUsers) {

		return sgRepository.findByIdUsers(idUsers);
	}


	@Override
	public SG findSG(String numeroCniPers) {

		return sgRepository.findByNumcniPers(numeroCniPers);
	}


	@Override
	public Intendant findIntendant(Long idUsers) {

		return intendantRepository.findByIdUsers(idUsers);
	}


	@Override
	public Intendant findIntendant(String numeroCniPers) {

		return intendantRepository.findByNumcniPers(numeroCniPers);
	}

	@Override
	public Enseignants findEnseignants(Long idUsers) {

		return enseignantRepository.findByIdUsers(idUsers);
	}

	@Override
	public Enseignants findEnseignants(String numeroCniPers) {

		return enseignantRepository.findByNumcniPers(numeroCniPers);
	}

	@Override
	public Proffesseurs findProffesseurs(Long idUsers) {
		
		return proffRepository.findByIdUsers(idUsers);
	}

	@Override
	public Page<Proviseur> findAllProviseur(int numPage, int taillePage){

		return provRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public Page<Censeurs> findAllCenseurs(int numPage, int taillePage) {

		return censeurRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroCensAsc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public List<Censeurs> findAllCenseursOrderByNumero(){
		return censeurRepository.findAllByOrderByNumeroCensAsc();
	}


	@Override
	public Page<SG> findAllSG(int numPage, int taillePage) {

		return sgRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroSgAsc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public List<SG> findAllSgOrderByNumero(){
		return sgRepository.findAllByOrderByNumeroSgAsc();
	}

	@Override
	public Page<Intendant> findAllIntendant(int numPage, int taillePage) {

		return intendantRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroIntAsc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public List<Intendant> findAllIntendantOrderByNumero(){
		return intendantRepository.findAllByOrderByNumeroIntAsc();
	}

	@Override
	public Page<Enseignants> findAllEnseignants(int numPage, int taillePage) {

		return enseignantRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public List<Enseignants> findAllEnseignants(){
		return enseignantRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc();
	}

	@Override
	public List<Utilisateurs> findAllUtilisateurs(){
		List<Utilisateurs> listofUsers = new ArrayList<Utilisateurs>();
		List<Utilisateurs> listofAllUsers = usersRepository.findAllUtilisateurs();

		for(Utilisateurs users : listofAllUsers){
			int usersAleRoleAdmin=0;
			List<UtilisateursRoles> listofusersRoles = (List<UtilisateursRoles>) users.getListofusersRoles();
			for(UtilisateursRoles usersRoles : listofusersRoles){
				if((usersRoles.getRoleAssocie().getRole().equals(new String("ADMIN")))){
					usersAleRoleAdmin=1;
				}
			}
			if(usersAleRoleAdmin==0){
				listofUsers.add(users);
			}
		}

		return listofUsers;
	}

	@Override
	public int deleteUsers(Long idUsers) {
		/*log.log(Level.DEBUG, "lancement de deleteUsers avec ses rôles"+idUsers);*/
		Utilisateurs usersAssocie=this.findUtilisateurs(idUsers);
		List<UtilisateursRoles> listofRoles=(List<UtilisateursRoles>)usersAssocie.getListofusersRoles();
		for(UtilisateursRoles usersRoles : listofRoles){
			usersrolesRepository.delete(usersRoles);
		}
		//System.err.println("******************** On a supprimer les usersRoles associe censeurs**********************");

		Censeurs censeurAssocie=this.findCenseurs(idUsers);
		SG sgAssocie=this.findSG(idUsers);
		Intendant intendantAssocie=this.findIntendant(idUsers);
		Enseignants enseignantAssocie=this.findEnseignants(idUsers);
		if(censeurAssocie!=null){
			//alors c'est un censeur qu'on veut supprimer
			/*log.log(Level.DEBUG, "suppression du censeur "+idUsers);*/
			censeurRepository.delete(censeurAssocie);
			//System.err.println("******************** On a supprimer le censeur associe **********************");

			return 1;
		}
		else if(sgAssocie!=null){
			//alors c'est un sg qu'on veut supprimer
			/*log.log(Level.DEBUG, "suppression du SG "+idUsers);*/
			sgRepository.delete(sgAssocie);
			//System.err.println("******************** On a supprimer le sg associe **********************");
			return 1;
		}
		else if(intendantAssocie!=null){
			intendantRepository.delete(intendantAssocie);
			/*log.log(Level.DEBUG, "suppression de l'intendant "+idUsers);*/
			//System.err.println("******************** On a supprimer le intendant associe **********************");
			return 1;
		}
		else if(enseignantAssocie!=null){
			enseignantRepository.delete(enseignantAssocie);
			/*log.log(Level.DEBUG, "suppression de l'enseignant "+idUsers);*/
			//System.err.println("******************** On a supprimer le enseignant associe **********************");
			return 1;
		}
		/*log.log(Level.DEBUG, "fin de l'exécution de deleteUsers "+idUsers);*/
		return 0;
	}


	@Override
	public int chercherNumeroPers(int roleCode) {
		int numeroTrouve=0;

		if(roleCode==1 || roleCode==2){
			/*
			 * Alors on veut un numero disponible parmi les censeurs
			 * les numeros sont censé aller de 1 à ...
			 * Donc on va juste retourne la taille de la liste des censeurs +1. 
			 */
			List<Censeurs> listofCenseurs=this.findAllCenseursOrderByNumero();

			numeroTrouve=listofCenseurs.size()+1;
		}
		else if(roleCode==3 || roleCode==4){
			/*
			 * Alors on veut un numero disponible parmi les SG
			 * les numeros sont censé aller de 1 à ...
			 * Donc on va juste retourne la taille de la liste des SG +1. 
			 */
			List<SG> listofSg=this.findAllSgOrderByNumero();

			numeroTrouve=listofSg.size()+1;
		}
		else if(roleCode==5 || roleCode==6){
			/*
			 * Alors on veut un numero disponible parmi les Intendant
			 * les numeros sont censé aller de 1 à ...
			 * Donc on va juste retourne la taille de la liste des Intendant +1. 
			 */
			List<Intendant> listofIntendant=this.findAllIntendantOrderByNumero();

			numeroTrouve=listofIntendant.size()+1;
		}

		return numeroTrouve;
	}


	@Override
	public Classes findClasses(Long idClasse) {
		try{
			Classes classeRechercher=classesRepository.findOne(idClasse);
			return classeRechercher;
		}
		catch(Exception e){
			//System.err.println("Ici dans findClasses EXCEPTION "+e.getMessage());
			return null;
		}
	}


	@Override
	public Classes findClasses(String codeClasses, String numeroClasses, Specialites specialite) {
		try{
			Classes classeRechercher=classesRepository.
					findByCodeClassesAndNumeroClassesAndSpecialite(codeClasses, numeroClasses, specialite);
			return classeRechercher;
		}
		catch(Exception e){
			//System.err.println("Ici dans findClasses avec codeClasse EXCEPTION "+e.getMessage());
			return null;
		}
	}

	@Override
	public Page<Classes> findPageClasse(int numPage, int taillePage) {
		return classesRepository.findAll(new PageRequest(numPage, taillePage));
		/*return classesRepository.findAllByOrderByCodeClassesAscSpecialiteCodeSpecialiteAscNumeroClassesAsc(
				new PageRequest(numPage, taillePage));*/
	}


	@Override
	public List<Classes> findListClasse() {

		return classesRepository.findAll();
	}

	@Override
	public int getEffectifProvisoireClasse(Long idClasse){
		/*
		 * Recherche de la classe dont on veut le nombre d'élève
		 */
		Classes classe=this.findClasses(idClasse);
		if(classe!=null){
			List<Eleves> listofEleveClasse=(List<Eleves>)classe.getListofEleves();

			return listofEleveClasse.size();
		}
		/*
		 * Cela signifie que la classe n'a pas été trouve donc on retourne l'effectif des eleves deja enregistre quelque soit 
		 * la classe
		 */
		List<Eleves> listofAllEleves=elevesRepository.findAll();
		return listofAllEleves.size();
	}

	@Override
	public int getEffectifDefinitifClasse(Long idClasse, int critere){
		List<Eleves> listdefofEleveClasse = new ArrayList<Eleves>();
		/*
		 * Recherche de la classe dont on veut le nombre d'élève ayant paye critere% de la scolarite
		 */
		Classes classe=this.findClasses(idClasse);
		if(classe!=null){
			double pourcentage = critere * 0.01;
			double montantMin = classe.getMontantScolarite() * pourcentage;
			/*System.err.println("montantMin "+montantMin+"() classe.getMontantScolarite()"+classe.getMontantScolarite()+""
					+ " critere "+critere + " pourcentage "+pourcentage);*/
			List<Eleves> listofEleveClasse=(List<Eleves>)classe.getListofEleves();

			for(Eleves elv : listofEleveClasse){
				if(elv.getCompteInscription().getMontant().doubleValue() >= montantMin){
					listdefofEleveClasse.add(elv);
				}
			}

			return listdefofEleveClasse.size();
		}
		return -1;
	}

	@Override
	public int getEffectifProvisoireClasseParSexe(Long idClasse, int sexe){
		Classes classe=this.findClasses(idClasse);
		if(classe!=null){
			int effectifSexe = classe.getEffectifofElevesDeSexe(sexe);

			return effectifSexe;
		}
		return -1;
	}

	@Override
	public Eleves findEleves(Long idEleves) {

		return elevesRepository.findOne(idEleves);
	}
	
	@Override
	public Eleves findElevesSuivant(Long idEleve, Long idSequence) {
		Eleves elv = this.findEleves(idEleve);
		if(elv==null) return null;
		Sequences seq = this.findSequences(idSequence);
		int numero = elv.getNumero(seq);
		//System.err.println("numero precedent "+numero);
		int pos = numero+1;
		//System.err.println("position suivant "+pos);
		List<Eleves> listofEleveRegulierInSeq = (List<Eleves>) elv.getClasse().getListofEleves();
		//System.err.println("taille liste  "+listofEleveRegulierInSeq.size());
		Eleves elvSvt = this.findEleveDansListe(listofEleveRegulierInSeq, pos);
		
		return elvSvt;
	}


	@Override
	public Eleves findEleves(String matriculeEleves) {

		return elevesRepository.findByMatriculeEleves(matriculeEleves);
	}


	@Override
	public Eleves findEleves(String nomsEleves, String prenomsEleves, Date datenaissEleves) {

		return elevesRepository.findByNomsElevesAndPrenomsElevesAndDatenaissEleves(nomsEleves, prenomsEleves,
				datenaissEleves);
	}

	@Override
	public Long saveEleves(Eleves eleve, Long idClasse){

		//System.err.println("le nom de la photos est "+eleve.getPhotoEleves());

		/*
		 * Est ce que le triplet noms prenoms datenaissance sera unique
		 */
		Eleves elevesExistName=this.findEleves(eleve.getNomsEleves(), eleve.getPrenomsEleves(), eleve.getDatenaissEleves());
		if(elevesExistName!=null) return new Long(0);

		/*
		 * Est ce que le matricule sera unique apres enregistrement
		 */
		Eleves elevesExistMatricule=this.findEleves(eleve.getMatriculeEleves());
		if(elevesExistMatricule!=null) {
			System.err.println("l'eleve "+elevesExistMatricule.getNomsEleves()+" a deja le matricule "
					+ "qui est entrain d'être utilisé pour enregistré un autre. son identifiant est "+elevesExistMatricule.getIdEleves()
					+" ce matricule est d'ailleurs "+elevesExistMatricule.getMatriculeEleves());
			return new Long(-1);
		}

		/*
		 * Maintenant on  recherche la classe dans laquelle enregistré l'élève
		 */
		Classes classeDenreg=this.findClasses(idClasse);
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
		compteinscriptionRepository.save(compteEleve);
		eleve.setCompteInscription(compteEleve);
		/*
		 * Puis on enregistre l'élève lui même
		 */
		Eleves eleveEnreg=elevesRepository.save(eleve);

		return eleveEnreg.getIdEleves();
	}
	
	public List<String> saveListEleves(List<Eleves> listofeleve, Long idClasse){
		List<String> listofError= new ArrayList<String>();
		for(Eleves eleve : listofeleve){
			Long val = this.saveEleves(eleve, idClasse);
			//System.out.println("enreg de eleve "+eleve.getNomsEleves()+" matricule "+eleve.getMatriculeEleves()+" == "+val);
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


	@Override
	public Page<Eleves> findPageEleves(int numPage, int taillePage) {

		return elevesRepository.findAll(new PageRequest(numPage, taillePage));
	}


	@Override
	public Page<Eleves> findPageElevesClasse(Long idClasses,	int numPage,	int taillePage) {
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findPageElevesOfClasse(idClasses, new PageRequest(numPage, taillePage));
	}

	@Override
	public Page<Eleves> findPageDefElevesClasse(Long idClasses, double montantMin,	int numPage,	int taillePage) {
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findPageElevesDefOfClasse(idClasses,	montantMin, new PageRequest(numPage, taillePage));
	}

	@Override
	public Page<Eleves> findPageElevesClasse(Long idClasses,	String etatInscription, int numPage,	int taillePage){
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findPageElevesOfClasse(idClasses, etatInscription, new PageRequest(numPage, taillePage));
	}

	@Override
	public List<Eleves> findListElevesClasse(Long idClasses) {
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findListElevesOfClasse(idClasses);
	}

	@Override
	public List<Eleves> findListElevesDefClasse(Long idClasses, double montantMin){
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findListElevesDefOfClasse(idClasses, montantMin);
	}

	@Override
	public List<Eleves> findListElevesClasse(Long idClasses,	String etatInscription) {
		if(idClasses==null){
			return null;//this.findPageEleves(numPage, taillePage);
		}
		return elevesRepository.findListElevesOfClasse(idClasses, etatInscription);
	}

	@Override
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

			Eleves elevesExistName=this.findEleves(eleveModif.getNomsEleves(), eleveModif.getPrenomsEleves(), 
					eleveModif.getDatenaissEleves());
			if(elevesExistName!=null) return new Long(0);

		}

		/*
		 * Est ce que le matricule sera unique apres enregistrement
		 */
		
		if(!(eleveModif.getMatriculeEleves().equals(eleveAModif.getMatriculeEleves()))){
			Eleves elevesExistMatricule=this.findEleves(eleveModif.getMatriculeEleves());
			if(elevesExistMatricule!=null) return new Long(-1);
		}

		/*
		 * Ici on est sur que le nouveau matricule et le nouveau triplet ne violeront aucune contrainte
		 * Maintenant on  recherche la classe dans laquelle enregistré l'élève
		 */

		if(newidClasse.longValue()!=eleveAModif.getClasse().getIdClasses().longValue()){
			Classes newclasseDenreg=this.findClasses(newidClasse);
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

		Eleves eleveUpdate=elevesRepository.save(eleveAModif);


		return eleveUpdate.getIdEleves();
	}


	@Override
	public int supprimerEleves(Long idElevesASupprim) {
		Eleves eleveASupprim=this.findEleves(idElevesASupprim);
		if(eleveASupprim!=null){
			elevesRepository.delete(eleveASupprim);
			return 1;
		}
		return 0;
	}


	@Override
	public int enregVersementSco(Long idEleveConcerne, double montantAVerser) {
		//System.err.println("debut de enregVersementSco ");
		
		if(montantAVerser < 0) montantAVerser *= -1;
		/*
		 * Rechercher l'élève dont on a l'id
		 * Recuperer son compte associe
		 * Ajouter le montantAVerser au montant de son compte
		 * Si le montant total atteint le montantScolarite alors on change son  etat en le plaçant à inscrit
		 * Si ce montant est supérieur à 0 alors on change son etat en le plaçant à en cours
		 * Donc par défaut cet etat reste à non inscrit
		 */
		//System.err.println("recherche de l'élève");
		Eleves eleveConcerne = this.findEleves(idEleveConcerne);

		if(eleveConcerne==null){
			return -1;
		}

		double montantdejaverse = eleveConcerne.getCompteInscription().getMontant();
		double nouveauMontant=montantdejaverse+montantAVerser;
		/*
		 * Il faut se rassurer que le nouveau montant ne sera pas supérieur au montant de la scolarité de la classe concerne
		 */
		if(nouveauMontant <= eleveConcerne.getClasse().getMontantScolarite()){
			eleveConcerne.getCompteInscription().setMontant(nouveauMontant);

			compteinscriptionRepository.save(eleveConcerne.getCompteInscription());

			/*
			 * Il faut maintenant enregistrer cette opération de versement
			 */
			Operations operation = new Operations();
			operation.setCompteinscription(eleveConcerne.getCompteInscription());
			operation.setDateOperation(new Date());
			operation.setIdentifiantOperation(this.getNumeroOperation());
			operation.setMontantOperation(montantAVerser);
			operation.setTypeOperation("versement");
			
			

			operationsRepository.save(operation);

			if(nouveauMontant >= eleveConcerne.getClasse().getMontantScolarite()) {
				eleveConcerne.setEtatInscEleves("inscrit");
				elevesRepository.save(eleveConcerne);
				return 2;
			}
			if(nouveauMontant >= 0) {
				eleveConcerne.setEtatInscEleves("en cours");
				elevesRepository.save(eleveConcerne);
				return 1;
			}
		}
		else{
			return -2;
		}
		return 0;
	}


	@Override
	public Page<Matieres> findAllMatieres(int numPage, int taillePage) {

		return matieresRepository.findAllByOrderByIntituleMatiereAscCodeMatiereAsc(new PageRequest(numPage, taillePage));
	}


	@Override
	public Matieres findMatieres(Long idMatiere) {

		return matieresRepository.findOne(idMatiere);
	}


	@Override
	public int updateMatiere(Matieres matiere) {
		Matieres matiereExistCode = matieresRepository.findByCodeMatiere(matiere.getCodeMatiere());
		Matieres matiereAModif = matieresRepository.findOne(matiere.getIdMatiere());

		if(matiereAModif == null) {
			/*
			 * Alors on veut faire un nouvel enregistrement
			 */
			if(matiereExistCode!=null) return 0;

			Matieres matiereAEnreg = new Matieres();
			matiereAEnreg.setCodeMatiere(matiere.getCodeMatiere());
			matiereAEnreg.setIntituleMatiere(matiere.getIntituleMatiere());

			matieresRepository.save(matiereAEnreg);
			return 1;
		}
		/*
		 * A ce niveau cela signifie qu'on veut faire une mise à jour de la matière trouvé en BD
		 * donc matiereAModif est != null
		 * On va donc vérifier si le nouveau code est différent de l'ancien et le cas échéant vérifier que 
		 * la contrainte ne sera pas violé
		 */

		if(!(matiere.getCodeMatiere().equals(matiereAModif.getCodeMatiere()))){
			/*
			 * il ne sont pas égaux donc on veut aussi modifier le code 
			 * on va donc se rassurer que la contrainte ne sera pas violé
			 */
			if(matiereExistCode!=null) return 0;
			/*
			 * Ici on est sur que la contrainte ne sera pas violé malgré le fait qu'on veut modifier aussi le code
			 */
			matiereAModif.setCodeMatiere(matiere.getCodeMatiere());
			matiereAModif.setIntituleMatiere(matiere.getIntituleMatiere());

			matieresRepository.save(matiereAModif);
			return 2;

		}
		/*
		 * Ici on est sur que les 2 code sont égaux donc c'est pas le code qu'on veut modifier
		 * mais juste l'intitulé peut. 
		 */
		matiereAModif.setIntituleMatiere(matiere.getIntituleMatiere());

		matieresRepository.save(matiereAModif);
		return 2;
	}


	@Override
	public Page<Cours> findAllCours(int numPage, int taillePage) {

		return coursRepository.findAllCoursOrderByClasseMatiere(new PageRequest(numPage, taillePage));
		//return coursRepository.findAllByOrderByMatiereCodeMatiereAscCodeCoursAsc(new PageRequest(numPage, taillePage));
	}


	@Override
	public List<Matieres> findAllMatieres() {

		return matieresRepository.findAll();
	}


	@Override
	public List<Proviseur> findAllProviseur() {

		return provRepository.findAll();
	}


	@Override
	public List<Censeurs> findAllCenseurs() {

		return censeurRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroCensAsc();
	}


	@Override
	public List<SG> findAllSG() {

		return sgRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroSgAsc();
	}


	@Override
	public List<Intendant> findAllIntendant() {

		return intendantRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDescNumeroIntAsc();
	}


	@Override
	public List<Proffesseurs> findAllProffesseurs() {

		return proffRepository.findAll();
	}


	@Override
	public List<Classes> findAllClasse() {
		return classesRepository.findAll();
	}

	@Override
	public List<Cycles> findAllCycle(){
		return cycleRepository.findAllByOrderByNumeroOrdreCyclesAsc();
	}

	@Override
	public Cours findCours(Long idCours) {

		return coursRepository.findOne(idCours);
	}
	
	@Override
	public Cycles findCycle(Long idCycle){
		return cycleRepository.findOne(idCycle);
	}


	@Override
	public List<Niveaux> findAllNiveaux() {

		return niveauxRepository.findAllByOrderByNumeroOrdreNiveauxAsc();
	}

	@Override
	public List<Sequences> findAllSequence(){
		return sequenceRepository.findAllByOrderByTrimestreAnneeIntituleAnneeDescTrimestreNumeroTrimAscNumeroSeqAsc();
	}

	@Override
	public List<Sequences>  findAllSequence(Long idAnneeActive){
		List<Trimestres> listofTrimestre = trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc(idAnneeActive);
		//List<Trimestres> listofTrimestre = trimestreRepository.findAllTrimestreActive(true, idAnneeActive);
		
		List<Sequences> listofSequence = new ArrayList<Sequences>();
		for(Trimestres trim : listofTrimestre){
			for(Sequences seq : trim.getListofsequence()){
				
				listofSequence.add(seq);
			}
		}
		//return sequenceRepository.findAllByOrderByTrimestreAnneeIntituleAnneeDescTrimestreNumeroTrimAscNumeroSeqAsc();
		return listofSequence;
	}
	
	public List<Sequences> findAllSequenceActive(Long idAnneeActive){
		//List<Trimestres> listofTrimestre = trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc(idAnneeActive);
		List<Trimestres> listofTrimestre = trimestreRepository.findAllTrimestreActive(true, idAnneeActive);
		
		List<Sequences> listofSequence = new ArrayList<Sequences>();
		for(Trimestres trim : listofTrimestre){
			for(Sequences seq : trim.getListofsequence()){
				//on ne trie que les sequences active
				if(seq.isEtatPeriodes() == true)	listofSequence.add(seq);
			}
		}
		//return sequenceRepository.findAllByOrderByTrimestreAnneeIntituleAnneeDescTrimestreNumeroTrimAscNumeroSeqAsc();
		return listofSequence;
	}

	@Override
	public List<Trimestres> findAllTrimestre(){
		return trimestreRepository.findAllByOrderByAnneeIntituleAnneeDescNumeroTrimAsc();
	}

	@Override
	public List<Trimestres> findAllActiveTrimestre(Long idAnneeActive){
		List<Trimestres> listofallTrim = 
				trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc(idAnneeActive);
		List<Trimestres> listofTrimActif = new ArrayList<Trimestres>();
		for(Trimestres trim : listofallTrim){
			if(trim.isEtatPeriodes() == true){
				listofTrimActif.add(trim);
			}
		}
		//return trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc(idAnneeActive);
		return listofTrimActif;
	}

	@Override
	public List<Niveaux> findAllNiveauxEns(Long idUsers){
		List<Niveaux> listofAllNiveauxEns = new ArrayList<Niveaux>();
		List<Niveaux> listofAllNiveaux = this.findAllNiveaux();
		for(Niveaux niv : listofAllNiveaux){
			List<Classes> listofClassesNiv = (List<Classes>) niv.getListofClasses();
			for(Classes classe : listofClassesNiv){
				List<Cours> listofCoursClasse = (List<Cours>) classe.getListofCours();
				for(Cours cours : listofCoursClasse){
					if(cours != null){
						if(cours.getProffesseur().getIdUsers().longValue() == idUsers.longValue()){
							/*
							 * Il faut juste verifier que le niveau n'est pas deja dans la liste resultat qu'il faut retourner
							 */
							int t = 0;
							for(Niveaux nivP : listofAllNiveauxEns){
								if(nivP.getIdNiveaux().longValue() == niv.getIdNiveaux().longValue()) {
									t = 1;
									break;
								}
							}
							if(t == 0) listofAllNiveauxEns.add(niv);
						}
					}
				}
			}
		}
		return listofAllNiveauxEns;
	}

	@Override
	public List<Niveaux> findAllNiveauxDirigesEns(Long idUsers){
		//System.err.println("======================================== ");
		List<Niveaux> listofAllNiveauxDirigesEns = new ArrayList<Niveaux>();
		List<Niveaux> listofAllNiveaux = this.findAllNiveaux();
		//System.err.println("listofAllNiveaux.size "+listofAllNiveaux.size());
		for(Niveaux niv : listofAllNiveaux){
			List<Classes> listofClassesNiv = (List<Classes>) niv.getListofClasses();
			//System.err.println("listofClassesNiv.size "+listofClassesNiv.size());
			for(Classes classe : listofClassesNiv){
				//System.err.println("est ce que classessss est null  "+classe.getIdClasses());
				if(classe.getProffesseur() != null){
					//System.err.println("est ce que classessss.getProffesseur() est null?  "+classe.getProffesseur().getDiplomePers());
					//System.err.println("classe.getProffesseur()== "+classe.getProffesseur().getIdUsers()+"   idUsers=="+idUsers);
					if(classe.getProffesseur().getIdUsers().longValue() == idUsers.longValue()){
						/*
						 * Il faut juste verifier que le niveau n'est pas deja dans la liste resultat qu'il faut retourner
						 */
						int t = 0;
						for(Niveaux nivP : listofAllNiveauxDirigesEns){
							if(nivP.getIdNiveaux().longValue() == niv.getIdNiveaux().longValue()) {
								t = 1;
								break;
							}
						}
						if(t == 0) listofAllNiveauxDirigesEns.add(niv);
					}
				}
			}
		}
		//System.err.println("listofAllNiveauxDirigesEns==  "+listofAllNiveauxDirigesEns.size());
		return listofAllNiveauxDirigesEns;
	}

	@Override
	public Page<Niveaux> findPageNiveaux(int numPage, int taillePage){

		return niveauxRepository.findAllByOrderByNumeroOrdreNiveauxAsc(new PageRequest(numPage, taillePage));
	}


	@Override
	public int updateCours(Cours cours, Long idMatiereAssocie, Long idProfAssocie, Long idClasseAssocie) {
		//Cours coursExistCode = coursRepository.findByCodeCours(cours.getCodeCours());
		Cours coursAModif = coursRepository.findOne(cours.getIdCours());

		Matieres matiereAssocie = matieresRepository.findOne(idMatiereAssocie);

		Proffesseurs profAssocie = proffRepository.findOne(idProfAssocie);

		Classes classeAssocie = classesRepository.findOne(idClasseAssocie);

		if((matiereAssocie == null) || (profAssocie == null) || (classeAssocie == null)){
			return -1;
		}

		if(coursAModif == null) {
			/*
			 * Alors on veut faire un nouvel enregistrement de cours
			 */
			//if(coursExistCode != null) return 0;

			Cours coursAEnreg = new Cours();
			coursAEnreg.setCodeCours(cours.getCodeCours());
			coursAEnreg.setIntituleCours(cours.getIntituleCours());
			coursAEnreg.setCoefCours(cours.getCoefCours());
			/*
			 * Recherchons la matiere, la classe et le users associe
			 */

			//Ici on est sur que la classe, la matiere et le prof existe
			coursAEnreg.setMatiere(matiereAssocie);
			coursAEnreg.setProffesseur(profAssocie);
			coursAEnreg.setClasse(classeAssocie);

			coursRepository.save(coursAEnreg);

			return 1;
		}
		/*
		 * A ce niveau cela signifie qu'on veut faire une mise à jour du cours trouvé en BD
		 * donc matiereAModif est != null
		 * On va donc vérifier si le nouveau code est différent de l'ancien et le cas échéant vérifier que 
		 * la contrainte ne sera pas violé
		 */
		if(!(cours.getCodeCours().equals(coursAModif.getCodeCours()))){
			//if(coursExistCode != null) return 0;

			coursAModif.setCodeCours(cours.getCodeCours());
			coursAModif.setIntituleCours(cours.getIntituleCours());
			coursAModif.setCoefCours(cours.getCoefCours());

			if(matiereAssocie.getIdMatiere().longValue() != coursAModif.getMatiere().getIdMatiere().longValue()){
				coursAModif.setMatiere(matiereAssocie);
			}

			if(profAssocie.getIdUsers().longValue() != coursAModif.getProffesseur().getIdUsers().longValue()){
				coursAModif.setProffesseur(profAssocie);
			}

			if(classeAssocie.getIdClasses().longValue() != coursAModif.getClasse().getIdClasses().longValue()){
				coursAModif.setClasse(classeAssocie);
			}

			coursRepository.save(coursAModif);
			return 2;

		}

		/*
		 * Ici on est sur que ce n'est pas le code qu'on veut modifier mais peut être le reste
		 * des champs de l'entite cours
		 */
		coursAModif.setIntituleCours(cours.getIntituleCours());
		coursAModif.setCoefCours(cours.getCoefCours());

		if(matiereAssocie.getIdMatiere().longValue() != coursAModif.getMatiere().getIdMatiere().longValue()){
			coursAModif.setMatiere(matiereAssocie);
		}

		if(profAssocie.getIdUsers().longValue() != coursAModif.getProffesseur().getIdUsers().longValue()){
			coursAModif.setProffesseur(profAssocie);
		}

		if(classeAssocie.getIdClasses().longValue() != coursAModif.getClasse().getIdClasses().longValue()){
			coursAModif.setClasse(classeAssocie);
		}

		coursRepository.save(coursAModif);

		return 2;

	}


	@Override
	public int deleteMatiere(Long idMatiere) {
		/*log.log(Level.DEBUG, "Lancement de deleteMatiere "+idMatiere);*/
		Matieres matiereASupprim = this.findMatieres(idMatiere);
		if(matiereASupprim == null)	return -1;
		if(matiereASupprim.getListofCours().size() != 0) return 0;

		matieresRepository.delete(idMatiere);
		/*log.log(Level.DEBUG, "suppression d'une matiere "+idMatiere);*/
		return 1;
	}


	@Override
	public int deleteCours(Long idCours) {
		/*log.log(Level.DEBUG, "Lancement de deleteCours "+idCours);*/
		Cours coursASupprim = this.findCours(idCours);
		if(coursASupprim == null) return -1;
		if(coursASupprim.getListofEval().size() != 0) return 0;

		coursRepository.delete(idCours);
		/*log.log(Level.DEBUG, "suppression d'un cours "+idCours);*/
		return 1;
	}


	@Override
	public Page<Sequences> findAllSequences(int numPage, int taillePage) {

		return sequenceRepository.findAllByOrderByTrimestreAnneeIntituleAnneeDescTrimestreNumeroTrimAscNumeroSeqAsc(
				new PageRequest(numPage, taillePage));
	}


	@Override
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

	@Override
	public Page<Trimestres> findAllTrimestres(int numPage, int taillePage) {

		return trimestreRepository.findAllByOrderByAnneeIntituleAnneeDescNumeroTrimAsc(
				new PageRequest(numPage, taillePage));
	}

	@Override
	public Page<Annee> findAllAnnee(int numPage, int taillePage) {

		return anneeRepository.findAllByOrderByIntituleAnneeDesc(new PageRequest(numPage, taillePage));
	}


	@Override
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


	@Override
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


	@Override
	public int setTitulaireClasse(Long idClasseConcerne, Long idProfTitulaire) {
		
		Classes classeConcerne = this.findClasses(idClasseConcerne);
		Proffesseurs profTitulaire = this.findProffesseurs(idProfTitulaire);

		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "setTitulaireClasse "+idClasseConcerne+" idProf "+idProfTitulaire);*/

		if((classeConcerne == null) || (profTitulaire == null)) return -1;
		//System.err.println("la classe "+classeConcerne.getIdClasses() + " sera dirige par "+profTitulaire.getNomsPers());
		/*
		 * Il faut vérifier l'existance du rôle TITULAIRE avant de faire les mise à jour
		 */

		Roles role=this.findRoles("TITULAIRE");

		if(role == null) return 0;
		//System.err.println(" l'enseignant  "+profTitulaire.getNomsPers()+" aura donc un role de plus qui est "+role.getRole());

		/*
		 * Il faut recuperer s'il existe l'ancien titulaire de la classe. 
		 * Par la suite apres avoir set le nouveau titulaire, on regarde si l'ancien est encore titulaire
		 * si non on le supprime le role.
		 * S'il existe et qu'il n'est desormais dirigeant d'aucune classe, alors le retirer le role titulaire
		 */
		Proffesseurs ancienprofTitulaire = classeConcerne.getProffesseur();
		
		int rep = this.saveUsersRoles(profTitulaire.getIdUsers(), role.getRole());

		//System.err.println(" on enregistre donc ce role comme etant un role du user");
		if(rep != 1) return -1;

		classeConcerne.setProffesseur(profTitulaire);

		classesRepository.save(classeConcerne);

		/*
		 * On a donc placer le nouveau titulaire. Il faut maintenant se rassurer que l'ancien
		 * est encore titulaire sinon le retirer le role de titulaire
		 */
		if(ancienprofTitulaire!=null){
			if(ancienprofTitulaire.getIdUsers().longValue() != profTitulaire.getIdUsers().longValue()){
				//les deux prof sont différents donc on continue.
				//On etablit la liste des classes
				List<Classes> listofClasses = classesRepository.findAll();
				int estencoreTitulaire = 0;
				for(Classes classe : listofClasses){
					if(classe.getProffesseur()!=null){
						if(classe.getProffesseur().getIdUsers().longValue() == 
								ancienprofTitulaire.getIdUsers().longValue()){
							/*
							 * On vient donc de trouver une autre classe dirigé par l'ancien titulaire qu'on a changer
							 * il doit donc garder le role pour pouvoir travailler avec cette classe qu'il dirige encore
							 */
							estencoreTitulaire = 1;
						}
					}
				}
				if(estencoreTitulaire == 0){
					/*
					 * Alors aprs avoir attribué la classe a un autre l'enseignant en question ne dirige plus 
					 * aucune autre classe.  Ainsi on peut lui supprimer le rôle de titulaire
					 */
					Long idUsers = ancienprofTitulaire.getIdUsers();
					UtilisateursRoles ur = usersrolesRepository.getUtilisateursRoles(idUsers, role.getRole());
					if(ur!=null){
						usersrolesRepository.delete(ur);
					}
				}
			}
	    }
		
		return 1;
	}


	@Override
	public Annee findAnneeActive() {

		return anneeRepository.findAnneeActive(true);
	}


	@Override
	public Page<Trimestres> findAllTrimestresAnnee(int numPage, int taillePage, Long idAnnee) {

		return trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc(
				new PageRequest(numPage, taillePage), idAnnee);
	}
	
	@Override
	public Page<Trimestres> findAllTrimestresActiveAnnee(int numPage, int taillePage, 
			boolean etatPeriodes,	Long idAnnee){
		return trimestreRepository.findAllTrimestreActive(new PageRequest(numPage, taillePage), 
				etatPeriodes, idAnnee);
	}


	@Override
	public List<Trimestres> findAllTrimestresAnnee(Long idAnnee) {

		return trimestreRepository.findAllByAnneeIdPeriodesOrderByNumeroTrimAsc( idAnnee);
	}
	
	@Override
	public List<Trimestres> findAllTrimestresActiveAnnee(boolean etatPeriode,	Long idAnnee) {

		return trimestreRepository.findAllTrimestreActive(etatPeriode, idAnnee);
	}

	@Override
	public Page<Cours> findAllCoursClasse(int numPage, int taillePage, Long idClasses){

		return coursRepository.findAllByClasseIdClassesOrderByMatiereCodeMatiereAscCodeCoursAsc(
				new PageRequest(numPage, taillePage), idClasses);
	}

	@Override
	public Page<Cours> findAllCoursProfClasse(int numPage, int taillePage, Long idClasses, Long idProf){

		return coursRepository.findAllByClasseIdClassesAndProffesseurIdUsersOrderByMatiereCodeMatiereAscCodeCoursAsc(
				new PageRequest(numPage, taillePage), idClasses, idProf);
	}


	@Override
	public List<Evaluations> findAllEvalCoursSeq(Long idCours, Long idSeq) {

		return evalRepository.findAllByCoursIdCoursAndSequenceIdPeriodesOrderByDateenregEvalAsc(idCours, idSeq);
	}

	@Override
	public Sequences findSequences(Long idPeriodes){

		return sequenceRepository.findByIdPeriodes(idPeriodes);
	}

	@Override
	public Trimestres findTrimestres(Long idPeriodes){

		return trimestreRepository.findByIdPeriodes(idPeriodes);
	}
	
	@Override
	public Annee findAnnee(Long idPeriodes){
		return anneeRepository.findByIdPeriodes(idPeriodes);
	}

	@Override
	public Evaluations findEvaluations(Long idCours, Long idSequence, String typeEval){

		return evalRepository.findByCoursSequenceTypeEval(idCours, idSequence, typeEval);
	}

	@Override
	public Evaluations findEvaluations(Long idEval){

		return evalRepository.findOne(idEval);
	}

	@Override
	public int saveEvaluation (String contenuEval, Cours coursEval, Date dateEval, int proportionEval, Sequences seqEval, String typeEval){

		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "saveEvaluation ");*/
		
		int ret = 0;
		/*
		 * Alors on veut enregistrer un DS et on va donc vérifier qu'il n'existe pas encore 
		 * dans cette séquence pour ce cours et si c'est le cas on enregistre plus
		 */
		Evaluations evalDeTypeExist = this.findEvaluations(coursEval.getIdCours(), seqEval.getIdPeriodes(), typeEval);


		if(evalDeTypeExist == null){
			if(proportionEval > 0 && proportionEval < 100){
				Evaluations eval = new Evaluations();
				eval.setContenuEval(contenuEval);
				eval.setCours(coursEval);
				eval.setDateenregEval(dateEval);
				eval.setProportionEval(proportionEval);
				eval.setSequence(seqEval);
				eval.setTypeEval(typeEval);

				//System.err.println("eval.getProportion cc ds");
				evalRepository.save(eval);
				ret = 1;
			}
			else{
				ret = 0;
			}
		}
		else{

			//System.err.println("eval.getProportion  "+proportionEval+" evalId "+evalDeTypeExist.getIdEval().longValue());
			if(proportionEval > 0 && proportionEval < 100){
				evalDeTypeExist.setProportionEval(proportionEval);

				/*
				 * Dans cette partie il s'agit d'une mise à jour de proportion donc 
				 * Il faut recherche l'éval CC ou DS en fonction de typeEval qu'on enregistre afin de s'assurer
				 * que la somme des pourcentages donnent exactement 100.
				 */
				if(typeEval.equals("DS")){
					//C'est un DS donc on doit chercher le CC associe
					String typeEvalAssocie = new String("CC");
					Evaluations evalAssocieDeTypeExist = this.findEvaluations(coursEval.getIdCours(), seqEval.getIdPeriodes(), typeEvalAssocie);
					if(evalAssocieDeTypeExist != null) {
						//On doit mettre à jour sa proportion pour que la somme des proportions des 2 évaluations soit 100
						evalAssocieDeTypeExist.setProportionEval(100 - proportionEval);
						evalRepository.save(evalAssocieDeTypeExist);
					}
				}
				else if(typeEval.equals("CC")){
					//C'est un CC donc on doit chercher le DS associe
					String typeEvalAssocie = new String("DS");
					Evaluations evalAssocieDeTypeExist = this.findEvaluations(coursEval.getIdCours(), seqEval.getIdPeriodes(), typeEvalAssocie);
					if(evalAssocieDeTypeExist != null) {
						//On doit mettre à jour sa proportion pour que la somme des proportions des 2 évaluations soit 100
						evalAssocieDeTypeExist.setProportionEval(100 - proportionEval);
						evalRepository.save(evalAssocieDeTypeExist);
					}
				}

				evalRepository.save(evalDeTypeExist);
				ret = 1;
			}
			else{
				ret = 0;
			}
		}


		return ret;
	}

	@Override
	public int updateProportionEvaluation(Long idEval, int new_proportion){
		int ret = -1;
		int newproportionEval_associe = 0;
		Evaluations evalConcerne = this.findEvaluations(idEval);
		if(evalConcerne == null) {
			System.err.println("yyyyyyyyyyyyyyyyy evalConcerne non trouve au "
					+ " moment du changement de proportion d'évaluation dans updateProportionEvaluation");
			return -1;
		}
		if(new_proportion<=100){
			newproportionEval_associe = 100 - new_proportion;
			String typeEval_associe = evalConcerne.getTypeEval().equalsIgnoreCase("CC")==true?"DS":"CC";
			System.err.println("le type d'évaluation associe est "+typeEval_associe);
			
			Evaluations evalDeTypeAssocieExist = this.findEvaluations(evalConcerne.getCours().getIdCours(), 
					evalConcerne.getSequence().getIdPeriodes(), typeEval_associe);
			/*
			 * On met a jour evalConcerne
			 */
			evalConcerne.setProportionEval(new_proportion);
			evalRepository.save(evalConcerne);
			ret = 1;
			if(evalDeTypeAssocieExist!=null){
				evalDeTypeAssocieExist.setProportionEval(newproportionEval_associe);
				evalRepository.save(evalDeTypeAssocieExist);
				ret = 2;
			}
		}
		return ret;
	}
	
	
	@Override
	public int getNumeroEleve(List<Eleves> listofEleve, Long idEleve){
		int numEleve = 0;
		int pos = 0;
		//System.err.println("départ de getNumero ");
		for(Eleves elv : listofEleve){
			pos = pos+1;
			if(elv.getIdEleves().longValue() == idEleve.longValue()) {

				numEleve = pos;
				//System.err.println("En cours de getNumero "+numEleve);
				break;
			}
		}
		//System.err.println("fin de getNumero "+ numEleve);
		return numEleve;
	}

	@Override
	public int getNumeroEleve(Long idEleve){

		Eleves elv = this.findEleves(idEleve);
		if(elv == null) return -1;
		List<Eleves> listofAllEleve = this.findListElevesClasse(elv.getClasse().getIdClasses());

		return this.getNumeroEleve(listofAllEleve, idEleve);
	}

	@Override
	public int getEffectifClasse(Long idEleve){
		Eleves elv = this.findEleves(idEleve);
		if(elv == null) return -1;
		List<Eleves> listofAllEleve = this.findListElevesClasse(elv.getClasse().getIdClasses());

		return listofAllEleve.size();
	}

	@Override
	public Eleves getElevesATraiter(Long idEleve, int mode){

		//System.err.println("Départ de recherche du prochain");

		Eleves elv = this.findEleves(idEleve);
		if(elv == null) return null;
		List<Eleves> listofAllEleve = this.findListElevesClasse(elv.getClasse().getIdClasses());

		/*int p=0;
		for(Eleves el : listofAllEleve){
			//System.err.println("Suite de recherche du prochain  "+el.getNomsEleves()+" a la position "+p);
			p = p +1;
		}*/
		/*
		 * On recupere la position de l'élève référencé par idEleve dans la liste
		 */

		int pos = listofAllEleve.indexOf(elv);
		//System.err.println("Suite de recherche du prochain  "+listofAllEleve.size());
		//System.err.println("Suite de recherche du prochain  "+pos);

		int posResult = pos;

		if(mode == 0){
			//ceci signifie qu'on cherche l'élève qui précède celui qui est passé en paramètre
			if(pos == 0) return null;

			posResult = pos - 1;
		}
		else if(mode == 1){
			if(pos == listofAllEleve.size() - 1) return null;

			posResult = pos + 1;
		}

		//System.err.println("position == "+ posResult+" Le  prochain  "+listofAllEleve.get(posResult).getNomsEleves());
		return listofAllEleve.get(posResult);

	}

	@Override
	public Eleves findEleveDansListe(List<Eleves> listofEleve, int pos){
		Eleves elvChercher = null;

		if((pos > 0) && (pos <= listofEleve.size())){
			elvChercher = listofEleve.get(pos - 1);
		}

		return elvChercher;
	}

	@Override
	public NotesEval findNotesEvalElevesEval(Long idEleves, Long idEval){

		return notesEvalRepository.findByEleveIdElevesAndEvaluationIdEval(idEleves, idEval);
	}

	@Override
	public RapportDAbsence findRapportDAbsenceSeqEleves(Long idEleves, Long idSequence){

		return rapportDAbsenceRepository.findByEleveIdElevesAndSequenceIdPeriodes(idEleves, idSequence);
	}

	@Override
	public int saveNoteEvalEleve(Long idEval, Long idEleves, double valNote){
		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "saveNoteEvalEleve ");*/
		Evaluations evalConcerne = this.findEvaluations(idEval);
		Eleves eleveConcerne = this.findEleves(idEleves);

		if(evalConcerne == null) return -1;
		if(eleveConcerne == null) return -1;

		if((valNote < 0) || (valNote > 20)) return 0;

		/*
		 * On verifie d'abord si une note pareille n'existe pas encore
		 */
		NotesEval noteExist = this.findNotesEvalElevesEval(idEleves, idEval);

		if(noteExist != null){
			/*
			 * Dans ce cas on ne modifiera que la valeur
			 */
			noteExist.setValeurnoteEval(valNote);

			notesEvalRepository.save(noteExist);
		}
		else{
			NotesEval note = new NotesEval();

			note.setDateenregnoteEval(new Date());
			note.setEleve(eleveConcerne);
			note.setEvaluation(evalConcerne);
			note.setValeurnoteEval(valNote);

			notesEvalRepository.save(note);
		}

		return 1;
	}

	public int saveRAbsenceSeqEleve(Long idEleves, Long idSequence, int nbreHJ, int nbreHNJ,
			int nbreHConsigne,	int nbreJExclusion, boolean avertConduite, boolean blameConduite){
		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "saveRAbsenceSeqEleve ");*/
		Eleves eleveConcerne = this.findEleves(idEleves);
		Sequences sequenceConcerne = this.findSequences(idSequence);

		if(sequenceConcerne == null) return -1;
		if(eleveConcerne == null) return -1;

		if((nbreHJ < 0) || (nbreHNJ < 0)) return 0;

		/*
		 * On verifie d'abord si un rapport d'absence n'existe pas déjà pour cet élève dans la séquence
		 */
		RapportDAbsence rabsExist = this.findRapportDAbsenceSeqEleves(idEleves, idSequence);

		if(rabsExist != null){
			/*
			 * Dans ce cas on ne modifiera que les valeurs
			 */
			rabsExist.setEleve(eleveConcerne);
			rabsExist.setSequence(sequenceConcerne);
			rabsExist.setNbreheureJustifie(nbreHJ);
			rabsExist.setNbreheureNJustifie(nbreHNJ);

			

			rapportDAbsenceRepository.save(rabsExist);
		}
		else{
			RapportDAbsence rabs = new RapportDAbsence();

			rabs.setEleve(eleveConcerne);
			rabs.setSequence(sequenceConcerne);
			rabs.setNbreheureJustifie(nbreHJ);
			rabs.setNbreheureNJustifie(nbreHNJ);

			

			rapportDAbsenceRepository.save(rabs);
		}

		return 1;
	}

	@Override
	public int getNumeroPageEleve(Long idEleves, int taillePage){
		int numPage = -1;

		//System.err.println("On cherche le numero de l'eleve a charge");
		Eleves eleveConcerne = this.findEleves(idEleves);

		if(eleveConcerne != null){
			Page<Eleves> pageofEleves = this.findPageElevesClasse(eleveConcerne.getClasse().getIdClasses(),	
					0, taillePage);
			int nbreTotalPage = pageofEleves.getTotalPages();
			//System.err.println("Suite de On cherche le numero de l'eleve a charge "+nbreTotalPage);
			int i = 0;
			while(i < nbreTotalPage){
				pageofEleves = this.findPageElevesClasse(eleveConcerne.getClasse().getIdClasses(),	
						i, taillePage);
				int j=0;
				for(Eleves elv : pageofEleves){
					if(elv.getIdEleves().longValue() == idEleves.longValue()) {
						//System.err.println("On n'a trouve donc on break");
						j=1;
						break;
					}
				}
				if(j==1) break;//on break la boucle while puisqu'on a deja trouve
				i = i + 1;
				//System.err.println("i passe à "+i);
			}
			numPage = i;
		}
		//System.err.println("Enfin on trouve "+numPage);
		return numPage;
	}

	@Override
	public int setMontantScoClasse(Long idClasseAConfig, double montantScolarite){

		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "setMontantScoClasse ");*/
		Classes classeAConfig = this.findClasses(idClasseAConfig);

		if(classeAConfig == null) return 0;

		if(montantScolarite < 0) return -1;

		classeAConfig.setMontantScolarite(montantScolarite);

		classesRepository.save(classeAConfig);

		return 1;
	}


	@Override
	public List<Cours> getListCoursClasse(long idClasseConcerne) {

		Classes classeConcerne = this.findClasses(idClasseConcerne);
		if(classeConcerne.getListofCours().size() == 0) return null;

		return (List<Cours>) classeConcerne.getListofCours();
	}


	@Override
	public Map<Long, List<Evaluations>> getMapEvalAllCoursClasseSeq(long idClasseConcerne, long idSequenceConcerne) {
		List<Cours> listcoursofClasses = this.getListCoursClasse(idClasseConcerne);
		List<Evaluations> listofEvalSeq = new ArrayList<Evaluations>();
		Map<Long, List<Evaluations>> mapofEvalSeq = new HashMap<Long, List<Evaluations>>();

		for(Cours cours : listcoursofClasses){
			List<Evaluations> listofEvalCours = (List<Evaluations>) cours.getListofEval();
			for(Evaluations eval : listofEvalCours){
				if(eval.getSequence().getIdPeriodes().longValue() == idSequenceConcerne){
					//Alors c'est une évaluation de la séquence concernée
					listofEvalSeq.add(eval);
				}
			}
			mapofEvalSeq.put(cours.getIdCours(), listofEvalCours);
		}
		return mapofEvalSeq;
	}

	@Override
	public List<Evaluations> getListEvalAllCoursClasseSeq(long idClasseConcerne, long idSequenceConcerne) {
		List<Cours> listcoursofClasses = this.getListCoursClasse(idClasseConcerne);
		List<Evaluations> listofEvalSeq = new ArrayList<Evaluations>();
		if(listcoursofClasses != null){
			for(Cours cours : listcoursofClasses){
				List<Evaluations> listofEvalCours = (List<Evaluations>) cours.getListofEval();
				for(Evaluations eval : listofEvalCours){
					if(eval.getSequence().getIdPeriodes().longValue() == idSequenceConcerne){
						//Alors c'est une évaluation de la séquence concernée
						listofEvalSeq.add(eval);
					}
				}
			}
		}
		return listofEvalSeq;
	}

	@Override
	public List<Classes> getListClassesDirige(long idUsers){
		Proffesseurs usersConcerne = this.findProffesseurs(idUsers);
		if(usersConcerne == null) return null;

		if(usersConcerne.getListofClassesTitulaire().size() == 0) return null;

		return (List<Classes>) usersConcerne.getListofClassesTitulaire();
	}

	

	@Override
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
			System.err.println("prefixe "+prefixe+" suffixe "+suffixe);
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
			System.err.println("prefixe "+prefixe+" suffixe "+suffixe);
			return matricule;
			
		}
			
	}

	/*public String getNextMatriculeforIndex(String codeEtab, String annee, int index){
		String matricule = "";
		String prefixe = codeEtab+annee+"-";
		int totalEleve = elevesRepository.findAll().size()+1;
		totalEleve +=index;
		String suffixe = "";
		if((totalEleve >= 0)&&(totalEleve < 10)){
			suffixe = "000"+totalEleve;
		}
		else if((totalEleve >= 10)&&(totalEleve < 100)){
			suffixe = "00"+totalEleve;
		}
		if((totalEleve >= 100)&&(totalEleve < 1000)){
			suffixe = "0"+totalEleve;
		}
		if((totalEleve >= 1000)&&(totalEleve < 10000)){
			suffixe = ""+totalEleve;
		}
		matricule = prefixe+suffixe;
		return matricule;
	}*/
	

	@Override
	public List<Cours> findListofCoursGrp1(Long idClasse) {
		List<Cours> listofCoursGrp1 = new ArrayList<Cours>();
		List<Cours> listofCoursClasse = new ArrayList<Cours>();
		Classes classeConcerne = this.findClasses(idClasse);
		if(classeConcerne != null){
			listofCoursClasse = (List<Cours>) classeConcerne.getListofCours();
			for(Cours cours : listofCoursClasse){
				if(cours.getGroupeCours().equals(new String("Scientifique"))==true){
					listofCoursGrp1.add(cours);
				}
			}
		}
		return listofCoursGrp1;
	}


	@Override
	public List<Cours> findListofCoursGrp2(Long idClasse) {
		List<Cours> listofCoursGrp2 = new ArrayList<Cours>();
		List<Cours> listofCoursClasse = new ArrayList<Cours>();
		Classes classeConcerne = this.findClasses(idClasse);
		if(classeConcerne != null){
			listofCoursClasse = (List<Cours>) classeConcerne.getListofCours();
			for(Cours cours : listofCoursClasse){
				if(cours.getGroupeCours().equals(new String("Litteraire"))==true){
					listofCoursGrp2.add(cours);
				}
			}
		}
		return listofCoursGrp2;
	}


	@Override
	public List<Cours> findListofCoursGrp3(Long idClasse) {
		List<Cours> listofCoursGrp3 = new ArrayList<Cours>();
		List<Cours> listofCoursClasse = new ArrayList<Cours>();
		Classes classeConcerne = this.findClasses(idClasse);
		if(classeConcerne != null){
			listofCoursClasse = (List<Cours>) classeConcerne.getListofCours();
			for(Cours cours : listofCoursClasse){
				if(cours.getGroupeCours().equals(new String("Divers"))==true){
					listofCoursGrp3.add(cours);
				}
			}
		}
		return listofCoursGrp3;
	}

	@Override
	public List<NotesEval> getListofnotesEvalDeSeq(Long idEleve, Long idSequence){
		Eleves eleve = this.findEleves(idEleve);
		Sequences sequence = this.findSequences(idSequence);
		if(eleve == null || sequence == null) return null;
		return ub.getListofnotesEvalDeSeq(eleve, sequence);
	}

	@Override
	public Collection<EleveBean2> generateReleveNote(Long idClasse){
		/*log.log(Level.DEBUG, "Lancement de la methode generateReleveNote "
				+ "avec idClasse="+idClasse);*/
		List<EleveBean2> listofEleve2 = new ArrayList<EleveBean2>();
		List<EleveBean> listofEleveBean = new ArrayList<EleveBean>();
		Classes classe = this.findClasses(idClasse);

		SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Eleves eleve : classe.getListofEleves()){
			EleveBean eb = new EleveBean();
			eb.setNumero(eleve.getNumero((List<Eleves>) classe.getListofEleves()));
			String date = spd.format(eleve.getDatenaissEleves());
			eb.setDate_naissance(date);
			eb.setLieu_naissance(eleve.getLieunaissEleves());
			eb.setMatricule(eleve.getMatriculeEleves());
			eb.setSexe(eleve.getSexeEleves());
			String noms_prenoms=" "+eleve.getNomsEleves()+" "+eleve.getPrenomsEleves();
			eb.setNoms_prenoms(noms_prenoms);
			
			listofEleveBean.add(eb);
		}
		EleveBean2 eb2 = new EleveBean2();
		eb2.setListofEleve(listofEleveBean);
		listofEleve2.add(eb2);
		return listofEleve2;
	}
	
	
	@Override
	public Collection<PV_TrimestreBean> generatePVTrimestre(Long idClasse,	Long idCours, 
			Long idTrimestre){
		
		System.out.println("Lancement de la methode generatePVTrimestre "
				+ "avec idClasse="+idClasse+" idCours="+idCours+"idTrimestre="+idTrimestre);
		
		Classes classe = this.findClasses(idClasse);
		
		Trimestres trimestre = this.findTrimestres(idTrimestre);
		
		List<PV_TrimestreBean> listofPVTrimestreBean = new ArrayList<PV_TrimestreBean>();
		
		List<Eleves> listofElevesregulier = ub. getListofEleveRegulierTrimestre(classe, trimestre);
		SimpleDateFormat spd1 = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Eleves eleve : listofElevesregulier){
			PV_TrimestreBean lignePV = new PV_TrimestreBean();
			lignePV.setNumero(eleve.getNumero(listofElevesregulier));
			lignePV.setMatricule(eleve.getMatriculeEleves());
			String noms_prenoms = new String(" "+eleve.getNomsEleves()+" "+eleve.getPrenomsEleves());
			lignePV.setNoms_prenoms(noms_prenoms.toUpperCase());
			String datenaiss = spd1.format(eleve.getDatenaissEleves());
			lignePV.setDate_naissance(datenaiss);
			double noteF = 0;
			double soenoteF = 0;
			double nbreN = 0;
			for(Sequences seq : trimestre.getListofsequence()){
				double noteFseq =  this.getValeurNotesFinaleEleve(eleve.getIdEleves(), idCours, seq.getIdPeriodes());
				if(noteFseq>=0) {
					soenoteF +=noteFseq;
					nbreN +=1;
				}
				if(seq.getNumeroSeq()%2==0){
					if(noteFseq>=0) lignePV.setNoteSeq2(noteFseq); else lignePV.setNoteSeq2(0);
				}
				else{
					if(noteFseq>=0) lignePV.setNoteSeq1(noteFseq); else lignePV.setNoteSeq1(0);
				}
			}
			if(nbreN == 1) {
				noteF = soenoteF;
			}
			
			if(nbreN == 2) {
				noteF = soenoteF*0.5;
			}
			
			if(noteF>=0) lignePV.setNote_finale(noteF); else lignePV.setNote_finale(0);
			
			listofPVTrimestreBean.add(lignePV);
		}
		return listofPVTrimestreBean;
	}
	
	@Override
	public Collection<PV_NoteBean> generatePVEvalAvecListeNote(List<NotesEval> listofNotesEvalSeq){
		List<PV_NoteBean> listofPVNoteBean = new ArrayList<PV_NoteBean>();
		NotesEval n_eval = null;
		Classes classe = null;
		Sequences sequence = null;
		if (listofNotesEvalSeq.size()>0) {
			n_eval=listofNotesEvalSeq.get(0);
			classe = n_eval.getEvaluation().getCours().getClasse();
			sequence = n_eval.getEvaluation().getSequence();
			
			List<Eleves> listofElevesregulier = ub.getListofEleveRegulier(classe, sequence);
			SimpleDateFormat spd1 = new SimpleDateFormat("dd/MM/yyyy");
			
			for(Eleves eleve : listofElevesregulier){
				PV_NoteBean lignePV = new PV_NoteBean();
				lignePV.setNumero(eleve.getNumero(listofElevesregulier));
				lignePV.setMatricule(eleve.getMatriculeEleves());
				String noms_prenoms = new String(" "+eleve.getNomsEleves()+" "+eleve.getPrenomsEleves());
				lignePV.setNoms_prenoms(noms_prenoms.toUpperCase());
				String datenaiss = spd1.format(eleve.getDatenaissEleves());
				lignePV.setDate_naissance(datenaiss);
				double val_note=0;
				for(NotesEval ne : listofNotesEvalSeq){
					if(ne.getEleve().getMatriculeEleves().equalsIgnoreCase(eleve.getMatriculeEleves())){
						val_note = ne.getValeurnoteEval();
						break;
					}
				}
				lignePV.setNote_eval(val_note);
				
				listofPVNoteBean.add(lignePV);
			}
			
		}
		
		
		
		return listofPVNoteBean;
	}
	
	@Override
	public Collection<PV_SequenceBean> generatePVSequence(Long idClasse,	Long idCours, 
			Long idSequence){
		/*log.log(Level.DEBUG, "Lancement de la methode generatePVSequence "
				+ "avec idClasse="+idClasse+" idCours="+idCours+"idSequence="+idSequence);*/
		Classes classe = this.findClasses(idClasse);
		Sequences sequence = this.findSequences(idSequence);
		
		List<PV_SequenceBean> listofPVSequenceBean = new ArrayList<PV_SequenceBean>();
		
		List<Eleves> listofElevesregulier = ub.getListofEleveRegulier(classe, sequence);
		SimpleDateFormat spd1 = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Eleves eleve : listofElevesregulier){
			PV_SequenceBean lignePV = new PV_SequenceBean();
			lignePV.setNumero(eleve.getNumero(listofElevesregulier));
			lignePV.setMatricule(eleve.getMatriculeEleves());
			String noms_prenoms = new String(" "+eleve.getNomsEleves()+" "+eleve.getPrenomsEleves());
			lignePV.setNoms_prenoms(noms_prenoms.toUpperCase());
			String datenaiss = spd1.format(eleve.getDatenaissEleves());
			lignePV.setDate_naissance(datenaiss);
			
			double noteF =  this.getValeurNotesFinaleEleve(eleve.getIdEleves(), idCours, idSequence);
			List<NotesEval> listofnote = this.getListofnotesEvalDeSeq(eleve.getIdEleves(), idSequence);
			for(NotesEval noteEval : listofnote){
				if(noteEval.getEvaluation().getCours().getIdCours().longValue() == idCours){
					if(noteEval.getEvaluation().getTypeEval().equalsIgnoreCase("CC")==true){
						lignePV.setNote_cc(noteEval.getValeurnoteEval());
					}
					if(noteEval.getEvaluation().getTypeEval().equalsIgnoreCase("DS")==true){
						lignePV.setNote_ds(noteEval.getValeurnoteEval());
					}
				}
			}
			if(noteF>=0) lignePV.setNote_finale(noteF); else lignePV.setNote_finale(0);
			
			listofPVSequenceBean.add(lignePV);
		}
		
		return listofPVSequenceBean;
	}
	
	@Override
	/*public Collection<BulletinSequenceBean> generateCollectionofBulletinSequence_opt(Long idClasse, 
			Long idSequence) {*/
	public Map<String, Object> generateCollectionofBulletinSequence_opt(Long idClasse, 
			Long idSequence) {
		
		
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		//java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		
		 long startTime = System.currentTimeMillis();
		 
		 Etablissement etablissementConcerne = this.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = this.findClasses(idClasse);
		 Annee anneeScolaire = this.findAnneeActive();
		 Sequences sequenceConcerne = this.findSequences(idSequence);
			
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
			String profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			
			
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
					bulletinSeq.setNom_g1("Scientifique");
				}
				else{
					bulletinSeq.setNom_g1("Scientific");
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
				
				if(valeurMoyDernierGrpCours1>0 && valeurMoyPremierGrpCours1>0){
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
					bulletinSeq.setNom_g2("Litteraire");
				}
				else{
					bulletinSeq.setNom_g2("Arts");
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
				
				if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
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
					bulletinSeq.setNom_g3("Divers");
				}
				else{
					bulletinSeq.setNom_g3("Others");
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
				
				if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
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
					
					
					mGrp1SeqBean.setMatiere_g1(cours.getCodeCours());
					mGrp1SeqBean.setProf_g1(cours.getProffesseur().getNomsPers());
					
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
					
					if(noteDernierCours>0 && notePremierCours>0){
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
						mGrp1SeqBean.setPourcentage_g1(pourcentage_g1);
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
		
		
					mGrp2SeqBean.setMatiere_g2(cours.getCodeCours());
					mGrp2SeqBean.setProf_g2(cours.getProffesseur().getNomsPers());
					
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
					
					if(noteDernierCours>0 && notePremierCours>0){
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
						mGrp2SeqBean.setPourcentage_g2(pourcentage_g2);
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
		
		
					mGrp3SeqBean.setMatiere_g3(cours.getCodeCours());
					mGrp3SeqBean.setProf_g3(cours.getProffesseur().getNomsPers());
					
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
					
					if(noteDernierCours>0 && notePremierCours>0){
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
						mGrp3SeqBean.setPourcentage_g3(pourcentage_g3);
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
	
	@Override
	public List<Eleves> getListEleveSanctionDiscSeq(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Sequences sequence){
		List<Eleves> listofEleveSanctionDisc = new ArrayList<Eleves>();
		
		for(Eleves elv : classe.getListofEleves()){
			List<RapportDisciplinaire> listofRapportDisciplinaireDeSeq = 
					elv.getListRapportDisciplinaireSeq_DESC(sequence.getIdPeriodes());
			
			if(listofRapportDisciplinaireDeSeq!=null){
				for(RapportDisciplinaire rappDisc : listofRapportDisciplinaireDeSeq){
					if(rappDisc.getSanctionDisc().getIdSancDisc().longValue() == sanctionDisc.getIdSancDisc().longValue()){
						listofEleveSanctionDisc.add(elv);
						break;
					}
				}
			}
		}
		
		return listofEleveSanctionDisc;
	}
	
	@Override
	public Map<String, Object> getListeEleveParSanctionDiscSeq(Classes classe, Sequences sequence){
		Map<String, Object> donnee = new HashMap<String, Object>();
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for(SanctionDisciplinaire sanctDisc : listofSanctionDisc){
			List<Eleves> listofEleveAyantEuSanctionDansSeq = 
					this.getListEleveSanctionDiscSeq(classe, sanctDisc, sequence);
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			donnee.put(label_cle, listofEleveAyantEuSanctionDansSeq);
		}
		/*
		 * On doit placer la dernière cle qui doit concerner les absences
		 */
		List<Eleves> listofEleveslesPlusAbsentDansClasseSeq = classe.getListofEleveslesPlusAbsentDansClasseSeq(sequence,10);
		
		donnee.put("absence", listofEleveslesPlusAbsentDansClasseSeq);
		return donnee;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Eleves> getListeElevePlusIndisciplineSeq(Classes classe, Sequences sequence, int n){
		List<Eleves> listofElevePlusIndiscSeq = new ArrayList<Eleves>();
		Map<String, Object> donnee = this.getListeEleveParSanctionDiscSeq(classe, sequence);
		
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for (SanctionDisciplinaire sanctDisc : listofSanctionDisc) {
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			//System.err.println(label_cle);
			List<Eleves> listofEleveDeSanctionLabel_cle = (List<Eleves>) donnee.get(label_cle);
			if(listofEleveDeSanctionLabel_cle!=null){
				for(Eleves elv : listofEleveDeSanctionLabel_cle){
					listofElevePlusIndiscSeq.add(elv);
				}
			}
			if(listofElevePlusIndiscSeq.size()==n) break;
		}
		
		if(listofElevePlusIndiscSeq.size()<n){
			List<Eleves> listofEleveDeSanctionAbsence = (List<Eleves>) donnee.get("absence");
			for(Eleves elv : listofEleveDeSanctionAbsence){
				
				if(elv.getNbreHeureAbsenceNonJustifie(sequence.getIdPeriodes())>0){
					listofElevePlusIndiscSeq.add(elv);
					//System.err.println("elv pour absence "+elv.getNomsEleves());
				}
				if(listofElevePlusIndiscSeq.size()==n) break;
			}
		}
		
		return listofElevePlusIndiscSeq;
	}
	
	@Override
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
		String profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
		profPrincipal=profPrincipal.toUpperCase();
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
		int totalAbsF = this.getNbreAbsNJSexeClasseSeq(classe, sequence, 0);
		int totalAbsM = this.getNbreAbsNJSexeClasseSeq(classe, sequence, 1);
		int totalAbs = totalAbsF+totalAbsM;
		
		ficheCC.setTotalAbs(totalAbs);
		ficheCC.setTotalAbsF(totalAbsF);
		ficheCC.setTotalAbsM(totalAbsM);
		
		/*
		 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
		 */
		int n=10;
		List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = this.getListeElevePlusIndisciplineSeq(classe, sequence, n);
		
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
	/*public Collection<BulletinTrimestreBean> Map<String, Object> generateCollectionofBulletinTrimestre_opt(Long idClasse,
			Long idTrimestre) {*/

	public Map<String, Object> generateCollectionofBulletinTrimestre_opt(Long idClasse,
				Long idTrimestre) {
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinTrimestre_opt avec "
				+ "idClasse= "+idClasse+" idTrimestre="+idTrimestre);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		 long startTime = System.currentTimeMillis();
		 
		 //java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		 
		 Etablissement etablissementConcerne = this.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = this.findClasses(idClasse);
		 Annee anneeScolaire = this.findAnneeActive();
		 Trimestres trimestreConcerne = this.findTrimestres(idTrimestre);
			
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
			String profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			
			
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
							bulletinTrim.setRappel_2("Sequence "+seq.getNumeroSeq());
						}
						else{
							bulletinTrim.setRappel_2("Séquence "+seq.getNumeroSeq());
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
				
				if(valeurMoyDernierGrpCours1>0 && valeurMoyPremierGrpCours1>0){
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
				
				System.out.println("total_pourcentage_g1total_pourcentage_g1 "+total_pourcentage_g1);
				
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
				
				System.out.println("total_pourcentage_g3total_pourcentage_g3 "+total_pourcentage_g1);
				
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
					
					
					mGrp1TrimBean.setMatiere_g1(cours.getCodeCours());
					mGrp1TrimBean.setProf_g1(cours.getProffesseur().getNomsPers());
					
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
					
					if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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
					

					mGrp2TrimBean.setMatiere_g2(cours.getCodeCours());
					mGrp2TrimBean.setProf_g2(cours.getProffesseur().getNomsPers());
					
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
					
					if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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
				

				mGrp3TrimBean.setMatiere_g3(cours.getCodeCours());
				mGrp3TrimBean.setProf_g3(cours.getProffesseur().getNomsPers());
				
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
				
				if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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

			
			
		 
		//return collectionofBulletionTrimestre;
		return donnee;
		
		
	}

	

	@Override
	public Map<String, Object> generateCollectionofBulletinTrimAnnee(Long idClasse, Long idTrimestre) {
		// TODO Auto-generated method stub
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinTrimAnnee avec "
				+ "idClasse= "+idClasse+" idTrimestre="+idTrimestre);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		long startTime = System.currentTimeMillis();
		
		
		 Etablissement etablissementConcerne = this.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = this.findClasses(idClasse);
		 Annee anneeScolaire = this.findAnneeActive();
		 Trimestres trimestreConcerne = this.findTrimestres(idTrimestre);
			
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
			String profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			
			
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
				
				if(r_moy_an>0) bulletinTrimAn.setR_moy_an(r_moy_an);
				
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
					bulletinTrimAn.setR_moy_3(r_moy_3);
					
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
					bulletinTrimAn.setR_moy_3(r_moy_3);
					
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
					bulletinTrimAn.setR_moy_4(r_moy_4);
					
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
					bulletinTrimAn.setR_moy_3(r_moy_3);
					
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
					bulletinTrimAn.setR_moy_4(r_moy_4);
					
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
					bulletinTrimAn.setR_moy_5(r_moy_5);
					
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
				if(valeurMoyDernierGrpCours1>0 && valeurMoyPremierGrpCours1>0){
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
				if(valeurMoyDernierGrpCours2>0 && valeurMoyPremierGrpCours2>0){
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
				if(valeurMoyDernierGrpCours3>0 && valeurMoyPremierGrpCours3>0){
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
					
					MatiereGroupe1TrimAnnuelBean mGrp1TrimBean = new MatiereGroupe1TrimAnnuelBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					
					
					mGrp1TrimBean.setMatiere_g1(cours.getCodeCours());
					mGrp1TrimBean.setProf_g1(cours.getProffesseur().getNomsPers());
					
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
					}
					
					mGrp1TrimBean.setCoef_g1(cours.getCoefCours());
					
					String extreme_g1 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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
					listofCoursScientifiqueTrimAnnuelBean.add(mGrp1TrimBean);
					
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
					
					MatiereGroupe2TrimAnnuelBean mGrp2TrimBean = new MatiereGroupe2TrimAnnuelBean();
					
					
					
					RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
							classeConcerne, cours, trimestreConcerne);
					

					mGrp2TrimBean.setMatiere_g2(cours.getCodeCours());
					mGrp2TrimBean.setProf_g2(cours.getProffesseur().getNomsPers());
					
					double soenoteTrim = 0;
					int nbreNoteDansTrimPourCours = 0;
					

					for(Sequences seq : trimestreConcerne.getListofsequence()){
						if(seq.getNumeroSeq()%2==1){
							double note_seq_g1 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g1>0){
								mGrp2TrimBean.setNote_1_g2(note_seq_g1);
								soenoteTrim = soenoteTrim + note_seq_g1;
								nbreNoteDansTrimPourCours +=1; 
							}
						}
						else{
							double note_seq_g2 = ub.getValeurNotesFinaleEleve(eleve, cours, seq);
							if(note_seq_g2>0){
								mGrp2TrimBean.setNote_2_g2(note_seq_g2);
								soenoteTrim = soenoteTrim + note_seq_g2;
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
					}
					
					mGrp2TrimBean.setCoef_g2(cours.getCoefCours());
					String extreme_g2 = "";
					double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
					double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
					if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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
					listofCoursLitteraireTrimAnnuelBean.add(mGrp2TrimBean);
					
					
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
				
				MatiereGroupe3TrimAnnuelBean mGrp3TrimBean = new MatiereGroupe3TrimAnnuelBean();
				
				
				
				RapportTrimestrielCours rapportTrimestrielCours = ub.getRapportTrimestrielCours(
						classeConcerne, cours, trimestreConcerne);
				

				mGrp3TrimBean.setMatiere_g3(cours.getCodeCours());
				mGrp3TrimBean.setProf_g3(cours.getProffesseur().getNomsPers());
				
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
				}
				
				mGrp3TrimBean.setCoef_g3(cours.getCoefCours());
				String extreme_g3 = "";
				double noteTrimDernierCours = rapportTrimestrielCours.getValeurNoteDernier();
				double noteTrimPremierCours = rapportTrimestrielCours.getValeurNotePremier();
				if(noteTrimDernierCours>0 && noteTrimPremierCours>0){
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
				listofCoursDiversTrimAnnuelBean.add(mGrp3TrimBean);
				
				
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
	public List<Eleves> getListEleveSanctionDiscTrim(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Trimestres trimestre){
		List<Eleves> listofEleveSanctionDisc = new ArrayList<Eleves>();
		
		for(Eleves elv : classe.getListofEleves()){
			List<RapportDisciplinaire> listofRapportDisciplinaireDeSeq = 
					elv.getListRapportDisciplinaireTrim_DESC(trimestre);
			
			if(listofRapportDisciplinaireDeSeq!=null){
				for(RapportDisciplinaire rappDisc : listofRapportDisciplinaireDeSeq){
					if(rappDisc.getSanctionDisc().getIdSancDisc().longValue() == sanctionDisc.getIdSancDisc().longValue()){
						listofEleveSanctionDisc.add(elv);
						break;
					}
				}
			}
		}
		
		return listofEleveSanctionDisc;
	}
	
	@Override
	public Map<String, Object> getListeEleveParSanctionDiscTrim(Classes classe, Trimestres trimestre){
		Map<String, Object> donnee = new HashMap<String, Object>();
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for(SanctionDisciplinaire sanctDisc : listofSanctionDisc){
			List<Eleves> listofEleveAyantEuSanctionDansSeq = 
					this.getListEleveSanctionDiscTrim(classe, sanctDisc, trimestre);
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			donnee.put(label_cle, listofEleveAyantEuSanctionDansSeq);
		}
		/*
		 * On doit placer la dernière cle qui doit concerner les absences
		 */
		List<Eleves> listofEleveslesPlusAbsentDansClasseSeq = classe.getListofEleveslesPlusAbsentDansClasseTrim(trimestre,10);
		
		donnee.put("absence", listofEleveslesPlusAbsentDansClasseSeq);
		return donnee;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Eleves> getListeElevePlusIndisciplineTrim(Classes classe, Trimestres trimestre, int n){
		List<Eleves> listofElevePlusIndiscSeq = new ArrayList<Eleves>();
		Map<String, Object> donnee = this.getListeEleveParSanctionDiscTrim(classe, trimestre);
		
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for (SanctionDisciplinaire sanctDisc : listofSanctionDisc) {
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			//System.err.println(label_cle);
			List<Eleves> listofEleveDeSanctionLabel_cle = (List<Eleves>) donnee.get(label_cle);
			if(listofEleveDeSanctionLabel_cle!=null){
				for(Eleves elv : listofEleveDeSanctionLabel_cle){
					listofElevePlusIndiscSeq.add(elv);
				}
			}
			if(listofElevePlusIndiscSeq.size()==n) break;
		}
		
		if(listofElevePlusIndiscSeq.size()<n){
			List<Eleves> listofEleveDeSanctionAbsence = (List<Eleves>) donnee.get("absence");
			for(Eleves elv : listofEleveDeSanctionAbsence){
				
				if(elv.getNbreHeureAbsenceNonJustifieTrim(trimestre)>0){
					listofElevePlusIndiscSeq.add(elv);
					//System.err.println("elv pour absence "+elv.getNomsEleves());
				}
				if(listofElevePlusIndiscSeq.size()==n) break;
			}
		}
		
		return listofElevePlusIndiscSeq;
	}
	
	@Override
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
		
		String profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
		profPrincipal=profPrincipal.toUpperCase();
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
				int totalAbsF = this.getNbreAbsNJSexeClasseTrim(classe, trimestre, 0);
				int totalAbsM = this.getNbreAbsNJSexeClasseTrim(classe, trimestre, 1);
				int totalAbs = totalAbsF+totalAbsM;
				
				ficheCC.setTotalAbs(totalAbs);
				ficheCC.setTotalAbsF(totalAbsF);
				ficheCC.setTotalAbsM(totalAbsM);
				
				/*
				 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
				 */
				int n=10;
				List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = this.getListeElevePlusIndisciplineTrim(classe, trimestre, n);
				
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
	/*public Collection<BulletinAnnuelBean> generateCollectionofBulletinAnnee(Long idClasse,
			Long idAnnee){*/
	public Map<String, Object> generateCollectionofBulletinAnnee(Long idClasse,
			Long idAnnee){
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofBulletinAnnee avec "
				+ "idClasse= "+idClasse+" idAnnee="+idAnnee);*/
		Map<String, Object> donnee = new HashMap<String, Object>();
		
		long startTime = System.currentTimeMillis();
		 
		 Etablissement etablissementConcerne = this.getEtablissement();
		 String villeEtab = "";
		 if(etablissementConcerne != null) villeEtab = etablissementConcerne.getVilleEtab();
		 Classes classeConcerne = this.findClasses(idClasse);
		 Annee anneeScolaire = this.findAnneeActive();
			
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
			String profPrincipal = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
			
			
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
				}
				else{
					bulletinAn.setResult_rang_ann("");
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
					
					
					mGrp1AnBean.setMatiere_g1(cours.getCodeCours());
					mGrp1AnBean.setProf_g1(cours.getProffesseur().getNomsPers());
					
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
					
					
					mGrp2AnBean.setMatiere_g2(cours.getCodeCours());
					mGrp2AnBean.setProf_g2(cours.getProffesseur().getNomsPers());
					
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
					
					
					mGrp3AnBean.setMatiere_g3(cours.getCodeCours());
					mGrp3AnBean.setProf_g3(cours.getProffesseur().getNomsPers());
					
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
	public List<Eleves> getListEleveSanctionDiscAnnee(Classes classe, SanctionDisciplinaire sanctionDisc, 
			Annee an){
		List<Eleves> listofEleveSanctionDisc = new ArrayList<Eleves>();
		
		for(Eleves elv : classe.getListofEleves()){
			List<RapportDisciplinaire> listofRapportDisciplinaireDeSeq = 
					elv.getListRapportDisciplinaireAnnee_DESC(an);
			
			if(listofRapportDisciplinaireDeSeq!=null){
				for(RapportDisciplinaire rappDisc : listofRapportDisciplinaireDeSeq){
					if(rappDisc.getSanctionDisc().getIdSancDisc().longValue() == sanctionDisc.getIdSancDisc().longValue()){
						listofEleveSanctionDisc.add(elv);
						break;
					}
				}
			}
		}
		
		return listofEleveSanctionDisc;
	}

	@Override
	public Map<String, Object> getListeEleveParSanctionDiscAnnee(Classes classe, Annee an){
		Map<String, Object> donnee = new HashMap<String, Object>();
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for(SanctionDisciplinaire sanctDisc : listofSanctionDisc){
			List<Eleves> listofEleveAyantEuSanctionDansSeq = 
					this.getListEleveSanctionDiscAnnee(classe, sanctDisc, an);
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			donnee.put(label_cle, listofEleveAyantEuSanctionDansSeq);
		}
		/*
		 * On doit placer la dernière cle qui doit concerner les absences
		 */
		List<Eleves> listofEleveslesPlusAbsentDansClasseSeq = classe.getListofEleveslesPlusAbsentDansClasseAnnee(an,10);
		
		donnee.put("absence", listofEleveslesPlusAbsentDansClasseSeq);
		return donnee;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Eleves> getListeElevePlusIndisciplineAnnee(Classes classe, Annee an, int n){
		List<Eleves> listofElevePlusIndiscSeq = new ArrayList<Eleves>();
		Map<String, Object> donnee = this.getListeEleveParSanctionDiscAnnee(classe, an);
		
		List<SanctionDisciplinaire> listofSanctionDisc = this.findListAllSanctionDisciplinaire_DESC();
		for (SanctionDisciplinaire sanctDisc : listofSanctionDisc) {
			String label_cle="sanction"+sanctDisc.getNiveauSeverite();
			//System.err.println(label_cle);
			List<Eleves> listofEleveDeSanctionLabel_cle = (List<Eleves>) donnee.get(label_cle);
			if(listofEleveDeSanctionLabel_cle!=null){
				for(Eleves elv : listofEleveDeSanctionLabel_cle){
					listofElevePlusIndiscSeq.add(elv);
				}
			}
			if(listofElevePlusIndiscSeq.size()==n) break;
		}
		
		if(listofElevePlusIndiscSeq.size()<n){
			List<Eleves> listofEleveDeSanctionAbsence = (List<Eleves>) donnee.get("absence");
			for(Eleves elv : listofEleveDeSanctionAbsence){
				
				if(elv.getNbreHeureAbsenceNonJustifieAnnee(an)>0){
					listofElevePlusIndiscSeq.add(elv);
					//System.err.println("elv pour absence "+elv.getNomsEleves());
				}
				if(listofElevePlusIndiscSeq.size()==n) break;
			}
		}
		
		return listofElevePlusIndiscSeq;
	}
	
	@Override
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
		String profPrincipal = " "+classe.getProffesseur().getNomsPers()+" "+classe.getProffesseur().getPrenomsPers();
		profPrincipal=profPrincipal.toUpperCase();
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
				int totalAbsF = this.getNbreAbsNJSexeClasseAn(classe, annee, 0);
				int totalAbsM = this.getNbreAbsNJSexeClasseAn(classe, annee, 1);
				int totalAbs = totalAbsF+totalAbsM;
				
				ficheCC.setTotalAbs(totalAbs);
				ficheCC.setTotalAbsF(totalAbsF);
				ficheCC.setTotalAbsM(totalAbsM);
				
				/*
				 * Il faut faire la liste des 10 élèves les plus indiscipline de la classe
				 */
				int n=10;
				List<Eleves> listofEleveLesPlusIndisciDansClasseSeq = this.getListeElevePlusIndisciplineAnnee(classe, annee, n);
				
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
	public Collection<EleveBean> generateCollectionofEleveprovClasse(Long idClasse) {
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofEleveprovClasse ");*/
		Classes classeSelect = this.findClasses(idClasse);
		if(classeSelect == null) return null;
		List<EleveBean> collectionofEleveprovClasse = new ArrayList<EleveBean>();
		List<Eleves> listofEleveDeClasse = (List<Eleves>) classeSelect.getListofEleves();
		
		SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Eleves eleve : listofEleveDeClasse){
			EleveBean elv = new EleveBean();
			String date = spd.format(eleve.getDatenaissEleves());
			elv.setDate_naissance(date);
			elv.setLieu_naissance(eleve.getLieunaissEleves());
			elv.setMatricule(eleve.getMatriculeEleves());
			String nomComplet = eleve.getNomsEleves().toUpperCase()+"  "+
					eleve.getPrenomsEleves().toUpperCase();
			elv.setNoms_prenoms(nomComplet);
			elv.setNumero(eleve.getNumero(listofEleveDeClasse));
			String sexe ="";
			String statut ="";
			if(eleve.getSexeEleves().equals("masculin")==true) sexe="M"; else sexe="F";
			if(eleve.getStatutEleves().equals("nouveau")==true) statut="N"; else statut="A";
			elv.setSexe(sexe);
			elv.setStatut(statut);
			
			collectionofEleveprovClasse.add(elv);
		}
		
		return collectionofEleveprovClasse;
	}
	
	@Override
	public Collection<EleveBean> generateCollectionofElevedefClasse(Long idClasse, 
			double montantMin){
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofElevedefClasse ");*/
		Classes classeSelect = this.findClasses(idClasse);
		if(classeSelect == null) return null;
		List<EleveBean> collectionofElevedefClasse = new ArrayList<EleveBean>();
		
		List<Eleves> listdefofEleves = this.findListElevesDefClasse(idClasse, montantMin);
		SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
		
		for(Eleves eleve : listdefofEleves){
			EleveBean elv = new EleveBean();
			String date = spd.format(eleve.getDatenaissEleves());
			elv.setDate_naissance(date);
			elv.setLieu_naissance(eleve.getLieunaissEleves());
			elv.setMatricule(eleve.getMatriculeEleves());
			String nomComplet = eleve.getNomsEleves().toUpperCase()+"  "+
					eleve.getPrenomsEleves().toUpperCase();
			elv.setNoms_prenoms(nomComplet);
			elv.setNumero(eleve.getNumero(listdefofEleves));
			String sexe ="";
			String statut ="";
			if(eleve.getSexeEleves().equals("masculin")==true) sexe="M"; else sexe="F";
			if(eleve.getStatutEleves().equals("nouveau")==true) statut="N"; else statut="A";
			elv.setSexe(sexe);
			elv.setStatut(statut);
			
			collectionofElevedefClasse.add(elv);
		}
		
		return collectionofElevedefClasse;
	}
	
	public Collection<PersonnelBean> generateCollectionofPersonnelBean(){
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		/*log.log(Level.DEBUG, "Lancement de la methode generateCollectionofPersonnelBean ");*/
		//On commence par le proviseur ou le chef d'etablissement
		List<Proviseur> listofProviseur = this.findAllProviseur();
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
				String adresse = proviseur.getEmailPers();
				
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
				listofPersonnelBean.add(pb);
			}
		}
		
		//On continue avec la liste des censeurs
		List<Censeurs> listofCenseur = this.findAllCenseurs();
		if(listofCenseur!=null){
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
				String adresse = c.getEmailPers();
				
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
				
				listofPersonnelBean.add(pb);
			}
		}
		
		//On continue avec la liste des SG
				List<SG> listofSg = this.findAllSG();
				if(listofSg!=null){
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
						String adresse = sg.getEmailPers();
						
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
						listofPersonnelBean.add(pb);
					}
				}
				
				//On continue avec la liste des Enseignants
				List<Enseignants> listofEns = this.findAllEnseignants();
				if(listofEns!=null){
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
						String adresse = ens.getEmailPers();
						
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
						
						listofPersonnelBean.add(pb);
					}
				}
				
				//On continue avec la liste des Enseignants
				List<Intendant> listofInt = this.findAllIntendant();
				if(listofInt!=null){
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
						String adresse = intendant.getEmailPers();
						
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
						listofPersonnelBean.add(pb);
					}
				}
		
		return listofPersonnelBean;
	}
	

	@Override
	public Collection<PersonnelBean> generateCollectionofCenseurBean() {
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
				List<Censeurs> listofCenseur = this.findAllCenseurs();
				if(listofCenseur!=null){
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
						String adresse = c.getEmailPers();
						
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
						
						listofPersonnelBean.add(pb);
					}
				}
		return listofPersonnelBean;
	}


	@Override
	public Collection<PersonnelBean> generateCollectionofSgBean() {
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<SG> listofSg = this.findAllSG();
		if(listofSg!=null){
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
				String adresse = sg.getEmailPers();
				
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
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	}


	@Override
	public Collection<PersonnelBean> generateCollectionofEnseignantBean() {
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<Enseignants> listofEns = this.findAllEnseignants();
		if(listofEns!=null){
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
				String adresse = ens.getEmailPers();
				
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
				
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	}


	@Override
	public Collection<PersonnelBean> generateCollectionofIntendantBean() {
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		List<Intendant> listofInt = this.findAllIntendant();
		if(listofInt!=null){
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
				String adresse = intendant.getEmailPers();
				
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
				listofPersonnelBean.add(pb);
			}
		}
		return listofPersonnelBean;
	}
	
	

	/*********************
	 * Fin des codes des différentes fonction qui entre dans la fabrication et l'édition des bulletins
	 */
	
	
	@Override
	public double getValeurNotesFinaleEleve(Long idEleve, Long idCours, Long idSequence){
		double noteFinale = -1;
		Eleves eleve = this.findEleves(idEleve);
		Cours cours = this.findCours(idCours);
		Sequences sequence =  this.findSequences(idSequence);
		
		if(eleve!=null && cours!=null && sequence!=null){
			noteFinale = ub.getValeurNotesFinaleEleve(eleve, cours, sequence);
		}
		return noteFinale;
	}
	
	@Override
	public int getNbreNoteDansCourspourEvalDansListe(List<NotesEval> listofNotesEvalSeq){
		int nbreNote = 0;
		for(NotesEval ne : listofNotesEvalSeq){
			if(ne.getValeurnoteEval()>=10) nbreNote +=1;
		}
		return nbreNote;
	}
	
	@Override
	public int getNbreSousNoteDansCourspourEvalDansListe(List<NotesEval> listofNotesEvalSeq){
		int nbresousNote = 0;
		for(NotesEval ne : listofNotesEvalSeq){
			if(ne.getValeurnoteEval()<10) nbresousNote +=1;
		}
		return nbresousNote;
	}
	
	@Override
	public int getNbreNoteDansCourspourSeq(Long idClasse, Long idCours, 
			Long idSequence){
		Classes classe = this.findClasses(idClasse);
		Cours cours = this.findCours(idCours);
		Sequences sequence =  this.findSequences(idSequence);
		
		if(classe==null || cours==null || sequence==null) return -1;
		int nbre = ub.getNbreNoteDansCourspourSeq(classe, cours, sequence);
		return nbre;
	}
	
	@Override
	public int getNbreNoteDansCourspourTrim(Long idClasse, Long idCours, 
			Long idTrimestre){
		Classes classe = this.findClasses(idClasse);
		Cours cours = this.findCours(idCours);
		Trimestres trimestre =  this.findTrimestres(idTrimestre);
		
		if(classe==null || cours==null || trimestre==null) return -1;
		int nbre = ub.getNbreNoteDansCourspourTrim(classe, cours, trimestre);
		return nbre;
	}
	
	@Override
	public int getNbreSousNoteDansCourspourSeq(Long idClasse, Long idCours, 
			Long idSequence){
		Classes classe = this.findClasses(idClasse);
		Cours cours = this.findCours(idCours);
		Sequences sequence =  this.findSequences(idSequence);
		
		if(classe==null || cours==null || sequence==null) return -1;
		int nbre = ub.getNbreSousNoteDansCourspourSeq(classe, cours, sequence);
		return nbre;
	}
	
	@Override
	public int getNbreSousNoteDansCourspourTrim(Long idClasse, Long idCours, 
			Long idTrimestre){
		Classes classe = this.findClasses(idClasse);
		Cours cours = this.findCours(idCours);
		Trimestres trimestre =  this.findTrimestres(idTrimestre);
		
		if(classe==null || cours==null || trimestre==null) return -1;
		int nbre = ub.getNbreSousNoteDansCourspourTrim(classe, cours, trimestre);
		return nbre;
	}


	@Override
	public int getEffectifInsolvableDansClasse(Long idClasse) {
		// TODO Auto-generated method stub
		Classes classe = this.findClasses(idClasse);
		if(classe == null) return -1;
		double montantScoClasse = classe.getMontantScolarite();
		int nbre_elv_insolvable = 0;
		
		for(Eleves eleve : classe.getListofEleves()){
			if(eleve.getCompteInscription().getMontant()<montantScoClasse){
				nbre_elv_insolvable+=1;
			}
		}
		
		return nbre_elv_insolvable;
	}


	@Override
	public List<Eleves> getListElevesInsolvable(Long idClasse) {
		Classes classe = this.findClasses(idClasse);
		if(classe == null) return null;
		double montantScoClasse = classe.getMontantScolarite();
		List<Eleves> listofEleveInsolvable = new ArrayList<Eleves>();
		for(Eleves eleve : classe.getListofEleves()){
			if(eleve.getCompteInscription().getMontant()<montantScoClasse){
				listofEleveInsolvable.add(eleve);
			}
		}
		return listofEleveInsolvable;
	}


	@Override
	public Collection<EleveInsolvableBean> generateListEleveInsolvable(Long idClasse) {
		// TODO Auto-generated method stub
		List<EleveInsolvableBean> listofInsolvable = new ArrayList<EleveInsolvableBean>();
		SimpleDateFormat spd1 = new SimpleDateFormat("yyyy-MM-dd");
		Classes classe = this.findClasses(idClasse);
		if(classe == null) return listofInsolvable;
		List<Eleves> listofEleve = this.getListElevesInsolvable(idClasse);
		for(Eleves eleve : listofEleve){
			EleveInsolvableBean eleveInsolvable = new EleveInsolvableBean();
			
			 String datelieuString="";
			 datelieuString = spd1.format(eleve.getDatenaissEleves());
			 datelieuString+=" à ";
			 datelieuString+=eleve.getLieunaissEleves();
			 
			 eleveInsolvable.setDate_lieunaissance(datelieuString);
			 
			 double montantVerse = eleve.getCompteInscription().getMontant();
			 
			 eleveInsolvable.setMontant_verse(""+montantVerse);
			 
			eleveInsolvable.setNumero(eleve.getNumero((List<Eleves>) classe.getListofEleves()));
			
			String noms_prenoms = eleve.getNomsEleves().toUpperCase()+" "+eleve.getPrenomsEleves();
			eleveInsolvable.setNoms_prenoms(noms_prenoms);
			
			 listofInsolvable.add(eleveInsolvable);
		}
		return listofInsolvable;
	}


	@Override
	public Page<Eleves> findPageElevesInsolvable(Long idClasses, int numPage, int taillePage) {
		return elevesRepository.findPageElevesInsolvableDansClasse(idClasses, new PageRequest(numPage, taillePage));
	}


	

	@Override
	public Long getLastOperationOnCompte(Long idEleveConcerne) {
		List<Operations> listofOperationOnCompte = this.findListAllOperationsEleve(idEleveConcerne);
		if(listofOperationOnCompte == null) return null;
		if(listofOperationOnCompte.size()>0){
			Operations opOnCompte = listofOperationOnCompte.get(0);
			return opOnCompte.getIdOperation();
		}
		return null;
	}


	@Override
	public double getMontantOperation(Long idOperation) {
		// TODO Auto-generated method stub
		Operations op = this.findOperation(idOperation);
		if(op==null) return 0;
		return op.getMontantOperation();
	}


	@Override
	public String getIdentifiantOperation(Long idOperation) {
		// TODO Auto-generated method stub
		Operations op = this.findOperation(idOperation);
		if(op==null) return "";
		return op.getIdentifiantOperation();
	}


	@Override
	public String getNumeroOperation() {
		// TODO Auto-generated method stub
		int numeroop=0;
		if(identOpRepository.findAll().size()>0){
			IdentOperation identop = identOpRepository.findAll().get(0);
			int numero = identop.getNumero();
			int next_numero = numero +1;
			identop.setNumero(next_numero);
			identOpRepository.save(identop);
			numeroop = next_numero;
		}
		else{
			/*
			 * C'est la toute première fois qu'on initialise l'enregistrement de la table identOperation
			 * pour la generation efficace des numeros des operations (transactions)
			 */
			int numero = 1;
			IdentOperation identop = new IdentOperation();
			identop.setNumero(numero);
			identOpRepository.save(identop);
			numeroop = numero;
		}
		
		return ""+numeroop;
	}


	@Override
	public int saveRAbsenceSeqEleve(Long idEleves, Long idSequence, int nbreHJ, int nbreHNJ, Date date_enreg) {
		// TODO Auto-generated method stub
		Eleves eleveConcerne = this.findEleves(idEleves);
		Sequences sequenceConcerne = this.findSequences(idSequence);
		Date dateJour = new Date();
		if(eleveConcerne == null || sequenceConcerne == null) return -3;
		if(dateJour.compareTo(date_enreg)<0) return -2;
		if(nbreHJ<0 || nbreHNJ<0) return -1;
		//On récupere les heures qui existent le cas écheant
		int total_nbreHJ = eleveConcerne.getNbreHeureAbsenceJustifie(idSequence);
		int total_nbreNHJ = eleveConcerne.getNbreHeureAbsenceNonJustifie(idSequence);
		//On ajoute ceux qui ont ete envoye dans la requete
		total_nbreHJ+=nbreHJ;
		total_nbreNHJ+=nbreHNJ;
		//On vérifie donc qu'apres l'opération on aura pas les heures justifiés > aux heures non justifiées
		if(total_nbreHJ>total_nbreNHJ) return 0;
		/*
		 * Tout est fin pret pour qu'un enregistrement soit effectue tout en gardant la coherence
		 */
		RapportDAbsence rapportDAbsence = new RapportDAbsence();
		rapportDAbsence.setDateenreg(date_enreg);
		rapportDAbsence.setEleve(eleveConcerne);
		rapportDAbsence.setNbreheureJustifie(nbreHJ);
		rapportDAbsence.setNbreheureNJustifie(nbreHNJ);
		rapportDAbsence.setSequence(sequenceConcerne);
		
		rapportDAbsenceRepository.save(rapportDAbsence);
		return 1;
	}


	@Override
	public int saveRDisciplineSeqEleve(Long idEleves, Long idSequence, Date date_enreg, int nbreperiode, String unite,
			String motif, Long idSanctionDisc) {
		// TODO Auto-generated method stub
		Eleves eleve = this.findEleves(idEleves);
		Sequences sequence = this.findSequences(idSequence);
		SanctionDisciplinaire sanctionDisc = this.findSanctionDisciplinaire(idSanctionDisc);
		Date dateJour = new Date();
		if(eleve == null || sequence == null || sanctionDisc == null ) return -3;
		if(dateJour.compareTo(date_enreg)<0) return -2;
		if(nbreperiode<0) return -1;
		if(nbreperiode>0 && unite.equalsIgnoreCase("RAS")==true) return 0;
		
		RapportDisciplinaire rapDisc = new RapportDisciplinaire();
		rapDisc.setDateenreg(date_enreg);
		rapDisc.setEleve(eleve);
		rapDisc.setMotif(motif);
		rapDisc.setNbreperiode(nbreperiode);
		rapDisc.setSanctionDisc(sanctionDisc);
		rapDisc.setSequence(sequence);
		rapDisc.setUnite(unite);
		
		
		rapportdisciplinaireRepository.save(rapDisc);
		
		/*
		 * Si la sanction disciplinaire a un niveau de severite = 5 alors il s'agit d'une exclusion définitive et 
		 * par cconséquent il faut actualiser l'état de l'élève. 
		 */
		if(sanctionDisc.getNiveauSeverite()>=5){
			eleve.setStatutEleves(new String("exclu"));
			
			/*
			 * On va donc effectuer effectivement la mise à jour
			 */
			elevesRepository.save(eleve);
		}
		
		return 1;
	}


	@Override
	public int deleteRapportDisciplinaire(Long idRdisc) {
		// TODO Auto-generated method stub
		rapportdisciplinaireRepository.delete(idRdisc);
		return 1;
	}



	@Override
	public String ecritEnLettreNombreDeux9(long nbre, boolean accord) {

		String chaine_result="";
		int nbre_dizaines;
		int nbre_unites;
		if(nbre>=20){
			//System.out.println("nbre>=20");
			if(nbre>=60){
				//System.out.println("nbre>=60");
				nbre_dizaines=2*((new Long(nbre)).intValue()/20);
				nbre_unites=(new Long(nbre)).intValue()%20;
			}
			else{
				nbre_dizaines=(new Long(nbre)).intValue()/10;
				nbre_unites=(new Long(nbre)).intValue()%10;
				//System.out.println("nbre_dizaines="+nbre_dizaines+" nbre_unites="+nbre_unites);
			}
			
			chaine_result+=this.ch_dizaines[nbre_dizaines];
			//System.out.println("on ajoute "+this.ch_dizaines[nbre_dizaines]+" car nbre_dizaines="+nbre_dizaines);
			
			if(accord==true && nbre_dizaines==8 && nbre_unites==0){
				chaine_result+="s";
				//System.out.println("on ajoute s");
			}
			
			if((nbre_unites%10==1) && nbre_dizaines!=8){
				chaine_result+=" et ";
				//System.out.println("on ajoute et");
			}
			else if(nbre_unites!=0){
				chaine_result+="-";
				//System.out.println("on ajoute -");
			}
		}
		else{
			nbre_unites=(new Long(nbre)).intValue();
		}
		
		if(nbre==0 || nbre_unites!=0){
			chaine_result+=this.ch_unites[nbre_unites];
			//System.out.println("on ajoute "+this.ch_unites[nbre_unites]+" car nbre_unites="+nbre_unites);
		}
		
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreTrois9(long nbre, boolean accord) {

		String chaine_result="";
		int nbre_centaines;
		int nbre99;
		
		nbre_centaines=(new Long(nbre)).intValue()/100;
		nbre99=(new Long(nbre)).intValue()%100;
		
		if(nbre_centaines>=1){
			if(nbre_centaines>=2){
				chaine_result+=this.ch_unites[nbre_centaines];
				chaine_result+=" ";
			}
			
			chaine_result+="cent";
			if(accord==true && nbre_centaines>=2 && nbre99==0){
				chaine_result+="s";
			}
			
			if(nbre99!=0){
				chaine_result+=" ";
			}
		}
		
		if(nbre==0 || nbre99!=0){
			String chaine_result99=ecritEnLettreNombreDeux9(nbre99, accord);
			chaine_result+=chaine_result99;
		}
		
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreQuatre9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_milliers;
		long nbre999;
		
		nbre_milliers=nbre/1000;
		nbre999=nbre%1000;
		
		if(nbre_milliers>=1){
			if(nbre_milliers>=2){
				String chaine_result999=ecritEnLettreNombreTrois9(nbre_milliers, false);
				chaine_result+=chaine_result999;
				chaine_result+=" ";
			}
			
			chaine_result+="mille";
			
			if(nbre999!=0){
				chaine_result+=" ";
			}
		}
		
		if(nbre==0 || nbre999!=0){
			String chaine_result999=ecritEnLettreNombreTrois9(nbre999, true);
			chaine_result+=chaine_result999;
		}
		
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreCinq9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_dizaine_milliers;
		long nbre9999;
		
		nbre_dizaine_milliers=nbre/1000;
		nbre9999=nbre%1000;
		
		if(nbre_dizaine_milliers>=1){
			if(nbre_dizaine_milliers>=2){
				String chaine_result9999=ecritEnLettreNombreQuatre9(nbre_dizaine_milliers, false);
				chaine_result+=chaine_result9999;
				chaine_result+=" ";
			}
			chaine_result+="mille";
			if(nbre9999!=0){
				chaine_result+=" ";
			}
			
		}
		
		if(nbre==0 || nbre9999!=0){
			String chaine_result9999=ecritEnLettreNombreQuatre9(nbre9999, true);
			chaine_result+=chaine_result9999;
		}
		
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreSix9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_centaine_milliers;
		long nbre99999;
		
		nbre_centaine_milliers=nbre/1000;
		nbre99999=nbre%1000;
		
		if(nbre_centaine_milliers>=1){
			if(nbre_centaine_milliers>=2){
				String chaine_result99999=ecritEnLettreNombreCinq9(nbre_centaine_milliers, false);
				chaine_result+=chaine_result99999;
				chaine_result+=" ";
			}
			chaine_result+="mille";
			if(nbre99999!=0){
				chaine_result+=" ";
			}
			
		}
		
		if(nbre==0 || nbre99999!=0){
			String chaine_result99999=ecritEnLettreNombreCinq9(nbre99999, true);
			chaine_result+=chaine_result99999;
		}
		
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreNeuf9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_millions;
		long nbre999999;
		
		nbre_millions=nbre/1000000;
		nbre999999=nbre%1000000;
		
		if(nbre_millions>=1){
			//System.out.println("if1");
			if(nbre_millions>=2){
				//System.out.println("if2");
				String chaine_result999999=ecritEnLettreNombreTrois9(nbre_millions, false);
				chaine_result+=chaine_result999999;
				chaine_result+=" ";
				//System.out.println("1=="+chaine_result);
			}
			else if(nbre_millions==1){
				chaine_result+="un ";
				//System.out.println("A1=="+chaine_result);
			}
			chaine_result+="million";
			//System.out.println("2=="+chaine_result);
			
			if(nbre_millions>1){
				chaine_result+="s";
			}
			
			if(nbre999999!=0){
				//System.out.println("if4");
				chaine_result+=" ";
				//System.out.println("3=="+chaine_result);
			}
			
		}
		
		if(nbre==0 || nbre999999!=0){
			//System.out.println("if5");
			String chaine_result999999=ecritEnLettreNombreSix9(nbre999999, true);
			chaine_result+=chaine_result999999;
			//System.out.println("4=="+chaine_result);
		}
		//System.out.println("5=="+chaine_result);
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombreDouze9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_milliards;
		long nbre999999999;
		
		nbre_milliards=nbre/1000000000;
		nbre999999999=nbre%1000000000;
		
		if(nbre_milliards>=1){
			//System.out.println("if6");
			if(nbre_milliards>=2){
				//System.out.println("if7");
				String chaine_result999999999=ecritEnLettreNombreTrois9(nbre_milliards, false);
				chaine_result+=chaine_result999999999;
				chaine_result+=" ";
				//System.out.println("6=="+chaine_result);
			}
			else if(nbre_milliards==1){
				chaine_result+="un ";
				//System.out.println("B1=="+chaine_result);
			}
			chaine_result+="milliard";
			//System.out.println("7=="+chaine_result);
			
			if(nbre_milliards>1){
				chaine_result+="s";
			}
			
			if(nbre999999999!=0){
				//System.out.println("if8");
				chaine_result+=" ";
				//System.out.println("8=="+chaine_result);
			}
			
		}
		
		if(nbre==0 || nbre999999999!=0){
			//System.out.println("if9");
			String chaine_result999999999=ecritEnLettreNombreNeuf9(nbre999999999, true);
			chaine_result+=chaine_result999999999;
			//System.out.println("9=="+chaine_result);
		}
		//System.out.println("10=="+chaine_result);
		return chaine_result;
	
	}


	@Override
	public String ecritEnLettreNombrePlusDeDouze9(long nbre, boolean accord) {

		String chaine_result="";
		long nbre_milliards;
		long nbrePlusDeNeuf9;
		
		String s_diviseur="1000000000";
		long diviseur=Long.parseLong(s_diviseur);
		
		nbre_milliards=nbre/diviseur;
		nbrePlusDeNeuf9=nbre%diviseur;
		
		if(nbre_milliards<1){
			chaine_result+= ecritEnLettreNombreDouze9(nbre,accord);
		}
		else{
			chaine_result+=ecritEnLettreNombreDouze9(nbre_milliards,accord);
			chaine_result+= " ";
			chaine_result+= "milliard";
			if(nbre_milliards>1){
				chaine_result+= "s";
			}
			chaine_result+= " ";
			chaine_result+= ecritEnLettreNombreDouze9(nbrePlusDeNeuf9,accord);
		}
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberTwo9(long nbre) {

		String chaine_result="";
		int nbre_tens;
		int nbre_units;
		if(nbre>=20){
			nbre_tens=(new Long(nbre)).intValue()/10;
			nbre_units=(new Long(nbre)).intValue()%10;
			chaine_result+=this.ch_tens[nbre_tens];
			if(nbre_units!=0){
				chaine_result+="-";
			}
		}
		else{
			nbre_units=(new Long(nbre)).intValue();
		}
		if(nbre==0 || nbre_units!=0){
			chaine_result+=this.ch_units[nbre_units];
		}
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberThree9(long nbre) {

		String chaine_result="";
		int nbre_hundred;
		int nbre99;
		
		nbre_hundred=(new Long(nbre)).intValue()/100;
		nbre99=(new Long(nbre)).intValue()%100;
		if(nbre_hundred>0){
			chaine_result+=this.ch_units[nbre_hundred];
			chaine_result+=" ";
			chaine_result+="hundred";
		}
		if(nbre_hundred>0 && nbre99>0){
			chaine_result+=" ";
			chaine_result+="and";
			chaine_result+=" ";
			chaine_result+=writeInLetterNumberTwo9(nbre99);
		}
		
		if(nbre_hundred==0 && nbre99>0){
			chaine_result+=writeInLetterNumberTwo9(nbre99);
		}
		
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberFour9(long nbre) {

		String chaine_result="";
		long nbre_thousand;
		long nbre999;
		
		nbre_thousand=nbre/1000;
		nbre999=nbre%1000;
		
		chaine_result+=writeInLetterNumberThree9(nbre_thousand);
		chaine_result+=" ";
		chaine_result+="thousand";
		chaine_result+=" ";
		chaine_result+=writeInLetterNumberThree9(nbre999);
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberFive9(long nbre) {

		String chaine_result="";
		long nbre_thousand;
		long nbre9999;
		
		nbre_thousand=nbre/1000;
		nbre9999=nbre%1000;
		
		chaine_result+=writeInLetterNumberTwo9(nbre_thousand);
		chaine_result+=" ";
		chaine_result+="thousand";
		chaine_result+=" ";
		chaine_result+=writeInLetterNumberThree9(nbre9999);
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberSix9(long nbre) {

		String chaine_result="";
		long nbre_thousand;
		long nbre99999;
		
		nbre_thousand=nbre/1000;
		nbre99999=nbre%1000;
		if(nbre_thousand>0){
			chaine_result+=writeInLetterNumberThree9(nbre_thousand);
			chaine_result+=" ";
			chaine_result+="thousand";
			chaine_result+=" ";
		}
		if(nbre99999>0){
			chaine_result+=writeInLetterNumberThree9(nbre99999);
		}
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberNine9(long nbre) {

		String chaine_result="";
		long nbre_million;
		long nbre999999;
		
		nbre_million=nbre/1000000;
		nbre999999=nbre%1000000;
		if(nbre_million>0){
			chaine_result+=writeInLetterNumberThree9(nbre_million);
			chaine_result+=" ";
			chaine_result+="million";
			chaine_result+=" ";
		}
		if(nbre999999>0){
			chaine_result+=writeInLetterNumberSix9(nbre999999);
		}
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberTwelve9(long nbre) {

		String chaine_result="";
		long nbre_billion;
		long nbre9999999;
		
		nbre_billion=nbre/1000000000;
		nbre9999999=nbre%1000000000;
		if(nbre_billion>0){
			chaine_result+=writeInLetterNumberThree9(nbre_billion);
			chaine_result+=" ";
			chaine_result+="billion";
			chaine_result+=" ";
		}
		if(nbre9999999>0){
			chaine_result+=writeInLetterNumberNine9(nbre9999999);
		}
		
		return chaine_result;
	
	}


	@Override
	public String writeInLetterNumberOverTwelve9(long nbre) {

		String chaine_result="";
		long nbre_billion;
		long nbre99999999;
		
		String s_diviseur="1000000000";
		long diviseur=Long.parseLong(s_diviseur);
		
		nbre_billion=nbre/diviseur;
		nbre99999999=nbre%diviseur;
		
		if(nbre_billion<1){
			chaine_result+= writeInLetterNumberTwelve9(nbre);
		}
		else{
			chaine_result+= writeInLetterNumberTwelve9(nbre_billion);
			chaine_result+= " ";
			chaine_result+= "billion";
			chaine_result+= " ";
			chaine_result+= writeInLetterNumberTwelve9(nbre99999999);
		}
		
		return chaine_result;
	
	}
	
	


	@Override
	public Page<Operations> findPageOperations(Date datemin, Date datemax, int numPage, int taillePage) {
		return operationsRepository.findPageOperations(datemin, datemax, new PageRequest(numPage, taillePage));
	}


	@Override
	public List<Operations> findListAllOperations(Date datemin, Date datemax) {
		return operationsRepository.findAllOperationEntreDate(datemin, datemax);
	}


	@Override
	public Operations findOperation(long idOperation) {
		return operationsRepository.findOne(idOperation);
	}


	@Override
	public double calculMontantTotalListOperation(List<Operations> listOperation) {
		double montant=0.0;
		for(Operations op : listOperation){
			montant+= op.getMontantOperation();
		}
		return montant;
	}


	@Override
	public Collection<OperationBean> generateListOperation(Date datemin, Date datemax) {
		// TODO Auto-generated method stub
		List<OperationBean> listofOperationBean = new ArrayList<OperationBean>();
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		int numero = 1;
		for(Operations op : this.findListAllOperations(datemin, datemax)){
			OperationBean opBean = new OperationBean();
			opBean.setDate_operation(spd.format(op.getDateOperation()));
			String noms = op.getCompteinscription().getEleveProprietaire().getNomsEleves().toUpperCase();
			String prenoms = op.getCompteinscription().getEleveProprietaire().getPrenomsEleves();
			opBean.setEleve_concerne(noms+" "+prenoms);
			opBean.setMontant(op.getMontantOperation());
			opBean.setNumero_ordre(op.getIdentifiantOperation());
			opBean.setNumero(numero);
			
			listofOperationBean.add(opBean);
			
			numero+=1;
		}
		return listofOperationBean;
	}


	@Override
	public List<Operations> findListAllOperationsEleve(Long idEleve) {
		return operationsRepository.findAllOperationEleve(idEleve);
	}


	@Override
	public Collection<OperationBean> generateListOperationEleve(Long idEleve) {
		// TODO Auto-generated method stub

		List<OperationBean> listofOperationBean = new ArrayList<OperationBean>();
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		int numero = 1;
		for(Operations op : this.findListAllOperationsEleve(idEleve)){
			OperationBean opBean = new OperationBean();
			opBean.setDate_operation(spd.format(op.getDateOperation()));
			String noms = op.getCompteinscription().getEleveProprietaire().getNomsEleves().toUpperCase();
			String prenoms = op.getCompteinscription().getEleveProprietaire().getPrenomsEleves();
			opBean.setEleve_concerne(noms+" "+prenoms);
			opBean.setMontant(op.getMontantOperation());
			opBean.setNumero_ordre(op.getIdentifiantOperation());
			opBean.setNumero(numero);
			
			listofOperationBean.add(opBean);
			
			numero+=1;
		}
		return listofOperationBean;
	
	}


	@Override
	public List<SanctionDisciplinaire> findListAllSanctionDisciplinaire() {
		// TODO Auto-generated method stub
		return sanctionDiscRepository.findAllByOrderByNiveauSeveriteAscCodeSancDiscAscIntituleSancDiscAsc();
	}

	@Override
	public List<SanctionDisciplinaire> findListAllSanctionDisciplinaire_DESC(){
		return sanctionDiscRepository.findAllByOrderByNiveauSeveriteDescCodeSancDiscAscIntituleSancDiscAsc();
	}

	@Override
	public SanctionDisciplinaire findSanctionDisciplinaire(Long idSanctionDisc) {
		// TODO Auto-generated method stub
		return sanctionDiscRepository.findOne(idSanctionDisc);
	}


	@Override
	public List<SanctionTravail> findListAllSanctionTravail() {
		// TODO Auto-generated method stub
		return sanctionTravRepository.findAllByOrderByCodeSancTravAscIntituleSancTravAsc();
	}


	@Override
	public SanctionTravail findSanctionTravail(Long idSanctionTrav) {
		// TODO Auto-generated method stub
		return sanctionTravRepository.findOne(idSanctionTrav);
	}


	@Override
	public List<Decision> findListAllDecision() {
		// TODO Auto-generated method stub
		return decisionRepository.findAllByOrderByCodeDecisionAscIntituleDecisionAsc();
	}


	@Override
	public DecisionConseil findDecisionConseilPeriode(Long idEleves, Long idPeriode) {
		// TODO Auto-generated method stub
		return decisionConseilRepository.findDecisionConseilPeriode(idEleves, idPeriode);
	}


	@Override
	public int saveDecisionConseilSeq(Long idEleves, Long idSequence, Long idSanctionDisc, int nbreperiode,
			String unite, Long idSanctionTravAssocie) {
		// TODO Auto-generated method stub
		Eleves eleve = this.findEleves(idEleves);
		Sequences sequence = this.findSequences(idSequence);
		SanctionDisciplinaire sanctionDisc = this.findSanctionDisciplinaire(idSanctionDisc);
		SanctionTravail sanctionTrav = this.findSanctionTravail(idSanctionTravAssocie);
		
		if(sanctionTrav == null || sanctionDisc == null || eleve == null || sequence == null) return -1;
		
		if(nbreperiode<0) return 0;
		if(nbreperiode>0 && unite.equalsIgnoreCase("RAS")==true) return 0;
		
		DecisionConseil decConseil = this.findDecisionConseilPeriode(idEleves, idSequence);
		
		if(decConseil == null){
			DecisionConseil dc = new DecisionConseil();
			dc.setDecisionAssocie(null);
			dc.setEleveConcerne(eleve);
			dc.setNbreperiode(nbreperiode);
			dc.setPeriodeConcerne(sequence);
			dc.setSanctionDiscAssocie(sanctionDisc);
			dc.setSanctionTravAssocie(sanctionTrav);
			dc.setUnite(unite);
			
			decisionConseilRepository.save(dc);
		}
		else{
			decConseil.setDecisionAssocie(null);
			decConseil.setNbreperiode(nbreperiode);
			decConseil.setSanctionDiscAssocie(sanctionDisc);
			decConseil.setSanctionTravAssocie(sanctionTrav);
			decConseil.setUnite(unite);
			
			decisionConseilRepository.save(decConseil);
		}
		
		/*
		 * Si la sanction disciplinaire a un niveau de severite = 5 alors il s'agit d'une exclusion définitive et 
		 * par cconséquent il faut actualiser l'état de l'élève. 
		 */
		if(sanctionDisc.getNiveauSeverite()>=5){
			eleve.setStatutEleves(new String("exclu"));
			
			/*
			 * On va donc effectuer effectivement la mise à jour
			 */
			elevesRepository.save(eleve);
		}
		
		return 1;
	}
	
	
	@Override
	public int saveDecisionConseilTrim(Long idEleves, Long idTrimestre, Long idSanctionDisc, int nbreperiode,
			String unite, Long idSanctionTravAssocie) {
		// TODO Auto-generated method stub
		Eleves eleve = this.findEleves(idEleves);
		Trimestres trimestre = this.findTrimestres(idTrimestre);
		SanctionDisciplinaire sanctionDisc = this.findSanctionDisciplinaire(idSanctionDisc);
		SanctionTravail sanctionTrav = this.findSanctionTravail(idSanctionTravAssocie);
		
		if(sanctionTrav == null || sanctionDisc == null || eleve == null || trimestre == null) return -1;
		
		if(nbreperiode<0) return 0;
		if(nbreperiode>0 && unite.equalsIgnoreCase("RAS")==true) return 0;
		
		DecisionConseil decConseil = this.findDecisionConseilPeriode(idEleves, idTrimestre);
		
		if(decConseil == null){
			DecisionConseil dc = new DecisionConseil();
			dc.setDecisionAssocie(null);
			dc.setEleveConcerne(eleve);
			dc.setNbreperiode(nbreperiode);
			dc.setPeriodeConcerne(trimestre);
			dc.setSanctionDiscAssocie(sanctionDisc);
			dc.setSanctionTravAssocie(sanctionTrav);
			dc.setUnite(unite);
			
			decisionConseilRepository.save(dc);
		}
		else{
			decConseil.setDecisionAssocie(null);
			decConseil.setNbreperiode(nbreperiode);
			decConseil.setSanctionDiscAssocie(sanctionDisc);
			decConseil.setSanctionTravAssocie(sanctionTrav);
			decConseil.setUnite(unite);
			
			decisionConseilRepository.save(decConseil);
		}
		
		/*
		 * Si la sanction disciplinaire a un niveau de severite = 5 alors il s'agit d'une exclusion définitive et 
		 * par cconséquent il faut actualiser l'état de l'élève. 
		 */
		if(sanctionDisc.getNiveauSeverite()>=5){
			eleve.setStatutEleves(new String("exclu"));
			
			/*
			 * On va donc effectuer effectivement la mise à jour
			 */
			elevesRepository.save(eleve);
		}
		
		return 1;
	}
	
	@Override
	public Decision findDecision(Long idDecision){
		return decisionRepository.findOne(idDecision);
	}
	
	@Override
	public int saveDecisionConseilAn(Long idEleves, Long idAnneeActive, 
			Long idsanctionTravAssocie, Long idDecisionAssocie){
		
		Eleves eleve = this.findEleves(idEleves);
		Annee annee = this.findAnnee(idAnneeActive);
		SanctionTravail sanctionTrav = this.findSanctionTravail(idsanctionTravAssocie);
		Decision decision = this.findDecision(idDecisionAssocie);
		
		if(eleve == null || annee == null || sanctionTrav == null || decision == null) return 0;
		
		DecisionConseil decConseil = this.findDecisionConseilPeriode(idEleves, idAnneeActive);
		
		if(decConseil == null){
			DecisionConseil dc = new DecisionConseil();
			dc.setDecisionAssocie(decision);
			dc.setEleveConcerne(eleve);
			dc.setPeriodeConcerne(annee);
			dc.setSanctionDiscAssocie(null);
			dc.setSanctionTravAssocie(sanctionTrav);
			
			decisionConseilRepository.save(dc);
		}
		else{
			decConseil.setDecisionAssocie(decision);
			decConseil.setEleveConcerne(eleve);
			decConseil.setPeriodeConcerne(annee);
			decConseil.setSanctionDiscAssocie(null);
			decConseil.setSanctionTravAssocie(sanctionTrav);
			
			decisionConseilRepository.save(decConseil);
		}
		
		return 1;
	}
	
	@Override
	public int getNbreAbsJSexeClasse(Classes classe, Date datemin, Date datemax, int sexe){
		int nbreHJ = 0;
		if(sexe == 0){//ici il s'agit du sexe feminin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifie(datemin, datemax);
				}
			}
		}
		else{//ici on cherche pour le sexe masculin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifie(datemin, datemax);
				}
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeClasseSeq(Classes classe, Sequences periode, int sexe){
		int nbreHJ=0;
		if(sexe == 0){//ici il s'agit du sexe feminin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifie(periode.getIdPeriodes());
				}
			}
		}
		else{//ici on cherche pour le sexe masculin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifie(periode.getIdPeriodes());
				}
			}
		}
		
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeClasseTrim(Classes classe, Trimestres periode, int sexe){
		int nbreHJ=0;
		if(sexe == 0){//ici il s'agit du sexe feminin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifieTrim(periode);
				}
			}
		}
		else{//ici on cherche pour le sexe masculin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifieTrim(periode);
				}
			}
		}
		
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeClasseAn(Classes classe, Annee periode, int sexe){
		int nbreHJ=0;
		if(sexe == 0){//ici il s'agit du sexe feminin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifieAnnee(periode);
				}
			}
		}
		else{//ici on cherche pour le sexe masculin
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreHJ +=eleve.getNbreHeureAbsenceJustifieAnnee(periode);
				}
			}
		}
		
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeClasse(Classes classe, Date datemin, Date datemax, int sexe){
		int nbreNHJ = 0;
		if(sexe == 0){
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifie(datemin, datemax);
				}
			}
		}
		else{
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifie(datemin, datemax);
				}
			}
		}
		return nbreNHJ;
	}

	@Override
	public int getNbreAbsNJSexeClasseSeq(Classes classe, Sequences periode, int sexe){
		int nbreNHJ = 0;
		if(sexe == 0){
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifie(periode.getIdPeriodes());
				}
			}
		}
		else{
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifie(periode.getIdPeriodes());
				}
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeClasseTrim(Classes classe, Trimestres periode, int sexe){
		int nbreNHJ = 0;
		if(sexe == 0){
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifieTrim(periode);
				}
			}
		}
		else{
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifieTrim(periode);
				}
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeClasseAn(Classes classe, Annee periode, int sexe){
		int nbreNHJ = 0;
		if(sexe == 0){
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("feminin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifieAnnee(periode);
				}
			}
		}
		else{
			for(Eleves eleve : classe.getListofEleves()){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true){
					nbreNHJ +=eleve.getNbreHeureAbsenceNonJustifieAnnee(periode);
				}
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsJSexeNiveau(Niveaux niveau, Date datemin, Date datemax, int sexe){
		int nbreHJ = 0;
		
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreHJ +=this.getNbreAbsJSexeClasse(classe, datemin, datemax, sexe);
				}
			}
			return nbreHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreHJ +=this.getNbreAbsJSexeClasse(classe, datemin, datemax, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeNiveauSeq(Niveaux niveau, Sequences periode, int sexe){
		int nbreHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreHJ +=this.getNbreAbsJSexeClasseSeq(classe, periode, sexe);
				}
			}
			return nbreHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreHJ +=this.getNbreAbsJSexeClasseSeq(classe, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeNiveauTrim(Niveaux niveau, Trimestres periode, int sexe){
		int nbreHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreHJ +=this.getNbreAbsJSexeClasseTrim(classe, periode, sexe);
				}
			}
			return nbreHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreHJ +=this.getNbreAbsJSexeClasseTrim(classe, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeNiveauAn(Niveaux niveau, Annee periode, int sexe){
		int nbreHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreHJ +=this.getNbreAbsJSexeClasseAn(classe, periode, sexe);
				}
			}
			return nbreHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreHJ +=this.getNbreAbsJSexeClasseAn(classe, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeNiveau(Niveaux niveau, Date datemin, Date datemax, int sexe){
		int nbreNHJ = 0;
		
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreNHJ +=this.getNbreAbsNJSexeClasse(classe, datemin, datemax, sexe);
				}
			}
			return nbreNHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreNHJ +=this.getNbreAbsNJSexeClasse(classe, datemin, datemax, sexe);
			}
		}
		return nbreNHJ;
		
	}
	
	@Override
	public int getNbreAbsNJSexeNiveauSeq(Niveaux niveau, Sequences periode, int sexe){
		int nbreNHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreNHJ +=this.getNbreAbsNJSexeClasseSeq(classe, periode, sexe);
				}
			}
			return nbreNHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreNHJ +=this.getNbreAbsNJSexeClasseSeq(classe, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeNiveauTrim(Niveaux niveau, Trimestres periode, int sexe){
		int nbreNHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreNHJ +=this.getNbreAbsNJSexeClasseTrim(classe, periode, sexe);
				}
			}
			return nbreNHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreNHJ +=this.getNbreAbsNJSexeClasseTrim(classe, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeNiveauAn(Niveaux niveau, Annee periode, int sexe){
		int nbreNHJ = 0;
		if(niveau == null){
			for(Niveaux niv : this.findAllNiveaux()){
				for(Classes classe : niv.getListofClasses()){
					nbreNHJ +=this.getNbreAbsNJSexeClasseAn(classe, periode, sexe);
				}
			}
			return nbreNHJ;
		}
		else{
			for(Classes classe : niveau.getListofClasses()){
				nbreNHJ +=this.getNbreAbsNJSexeClasseAn(classe, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsJSexeCycle(Cycles cycle, Date datemin, Date datemax, int sexe){
		int nbreHJ = 0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreHJ += this.getNbreAbsJSexeNiveau(niv, datemin, datemax, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreHJ += this.getNbreAbsJSexeNiveau(niv, datemin, datemax, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeCycleSeq(Cycles cycle, Sequences periode, int sexe){
		int nbreHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreHJ += this.getNbreAbsJSexeNiveauSeq(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreHJ += this.getNbreAbsJSexeNiveauSeq(niv, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeCycleTrim(Cycles cycle, Trimestres periode, int sexe){
		int nbreHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreHJ += this.getNbreAbsJSexeNiveauTrim(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreHJ += this.getNbreAbsJSexeNiveauTrim(niv, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsJSexeCycleAn(Cycles cycle, Annee periode, int sexe){
		int nbreHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreHJ += this.getNbreAbsJSexeNiveauAn(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreHJ += this.getNbreAbsJSexeNiveauAn(niv, periode, sexe);
			}
		}
		return nbreHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeCycle(Cycles cycle, Date datemin, Date datemax, int sexe){
		int nbreNHJ = 0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreNHJ += this.getNbreAbsNJSexeNiveau(niv, datemin, datemax, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreNHJ += this.getNbreAbsNJSexeNiveau(niv, datemin, datemax, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeCycleSeq(Cycles cycle, Sequences periode, int sexe){
		int nbreNHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreNHJ += this.getNbreAbsNJSexeNiveauSeq(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreNHJ += this.getNbreAbsNJSexeNiveauSeq(niv, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeCycleTrim(Cycles cycle, Trimestres periode, int sexe){
		int nbreNHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreNHJ += this.getNbreAbsNJSexeNiveauTrim(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreNHJ += this.getNbreAbsNJSexeNiveauTrim(niv, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	@Override
	public int getNbreAbsNJSexeCycleAn(Cycles cycle, Annee periode, int sexe){
		int nbreNHJ=0;
		if(cycle == null){
			List<Cycles> listofCycle = this.findAllCycle();
			for(Cycles c : listofCycle){
				for(Niveaux niv : c.getListofNiveaux()){
					nbreNHJ += this.getNbreAbsNJSexeNiveauAn(niv, periode, sexe);
				}
			}
		}
		else{
			for(Niveaux niv : cycle.getListofNiveaux()){
				nbreNHJ += this.getNbreAbsNJSexeNiveauAn(niv, periode, sexe);
			}
		}
		return nbreNHJ;
	}
	
	
	
	@Override
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleBean(Cycles cycle, 
			Date datemin, Date datemax){
		
		List<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = 
				new ArrayList<FicheRecapAbsenceCycleBean>();
		
		String cycle_string = (cycle == null)?"Tous les cycles \n All cycles":(cycle.getCodeCycles()+" \n "+cycle.getCodeCycles_en());
		
		if(cycle == null){
			//Alors c'est le rapport complet de tous les cycles
			
			
			for(Cycles cy : this.findAllCycle()){
				FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
				int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycle(cy, datemin, datemax, 0);
				int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycle(cy, datemin, datemax, 0);
				
				int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycle(cy, datemin, datemax, 1);
				int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycle(cy, datemin, datemax, 1);
				
				String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
				String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
				
				int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
				int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
				String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
				
				fracb.setCycle(cycle_string);
				fracb.setNbreabsfeminin(nbreabsfeminin_cy);
				fracb.setNbreabsmasculin(nbreabsmasculin_cy);
				fracb.setNbreabstotal(nbreabstotal_cy);
				
				
				listofFicheRecapAbsenceCycleBean.add(fracb);
				
			}
			
		
			
			int nbreabsJfeminin = this.getNbreAbsJSexeCycle(cycle, datemin, datemax, 0);
			int nbreabsNJfeminin = this.getNbreAbsNJSexeCycle(cycle, datemin, datemax, 0);
			
			int nbreabsJmasculin = this.getNbreAbsJSexeCycle(cycle, datemin, datemax, 1);
			int nbreabsNJmasculin = this.getNbreAbsNJSexeCycle(cycle, datemin, datemax, 1);
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			int totalabsNJ = nbreabsNJfeminin+nbreabsNJmasculin;
			int totalabsJ = nbreabsJfeminin+nbreabsJmasculin;
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
			
		}
		else{
			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			
			int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycle(cycle, datemin, datemax, 0);
			int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycle(cycle, datemin, datemax, 0);
			
			int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycle(cycle, datemin, datemax, 1);
			int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycle(cycle, datemin, datemax, 1);
			
			String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
			String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
			
			int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
			int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
			String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
			
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_cy);
			fracb.setNbreabsmasculin(nbreabsmasculin_cy);
			fracb.setNbreabstotal(nbreabstotal_cy);
			
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
		
			
		}
		
		//System.out.println("Taille de la liste retourner "+listofFicheRecapAbsenceCycleBean.size());
		
		return listofFicheRecapAbsenceCycleBean;
	}
	
	@Override
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleSeqBean(Cycles cycle, 
			Sequences periode){
		
		if(periode == null) return null;
		
		List<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = 
				new ArrayList<FicheRecapAbsenceCycleBean>();
		
		String cycle_string = (cycle == null)?"Tous les cycles \n All cycles":(cycle.getCodeCycles()+" \n "+cycle.getCodeCycles_en());
		
		if(cycle == null){

			//Alors c'est le rapport complet de tous les cycles
			
			
			for(Cycles cy : this.findAllCycle()){
				FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
				int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleSeq(cy, periode, 0);
				int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleSeq(cy, periode, 0);
				
				int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleSeq(cy, periode, 1);
				int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleSeq(cy, periode, 1);
				
				String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
				String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
				
				int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
				int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
				String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
				
				fracb.setCycle(cycle_string);
				fracb.setNbreabsfeminin(nbreabsfeminin_cy);
				fracb.setNbreabsmasculin(nbreabsmasculin_cy);
				fracb.setNbreabstotal(nbreabstotal_cy);
				
				
				listofFicheRecapAbsenceCycleBean.add(fracb);
				
			}
			
		
			
			int nbreabsJfeminin = this.getNbreAbsJSexeCycleSeq(cycle, periode, 0);
			int nbreabsNJfeminin = this.getNbreAbsNJSexeCycleSeq(cycle, periode, 0);
			
			int nbreabsJmasculin = this.getNbreAbsJSexeCycleSeq(cycle, periode, 1);
			int nbreabsNJmasculin = this.getNbreAbsNJSexeCycleSeq(cycle, periode, 1);
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			int totalabsNJ = nbreabsNJfeminin+nbreabsNJmasculin;
			int totalabsJ = nbreabsJfeminin+nbreabsJmasculin;
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			
			int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleSeq(cycle, periode, 0);
			int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleSeq(cycle, periode, 0);
			
			int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleSeq(cycle, periode, 1);
			int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleSeq(cycle,periode, 1);
			
			String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
			String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
			
			int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
			int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
			String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
			
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_cy);
			fracb.setNbreabsmasculin(nbreabsmasculin_cy);
			fracb.setNbreabstotal(nbreabstotal_cy);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_cy+" nbreabsmasculin_cy=="+nbreabsmasculin_cy+" nbreabstotal_cy== "+nbreabstotal_cy);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceCycleBean;
	}
	
	@Override
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleTrimBean(Cycles cycle, 
			Trimestres periode){

		
		if(periode == null) return null;
		
		List<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = 
				new ArrayList<FicheRecapAbsenceCycleBean>();
		
		String cycle_string = (cycle == null)?"Tous les cycles \n All cycles":(cycle.getCodeCycles()+" \n "+cycle.getCodeCycles_en());
		
		if(cycle == null){

			//Alors c'est le rapport complet de tous les cycles
			
			
			for(Cycles cy : this.findAllCycle()){
				FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
				int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleTrim(cy, periode, 0);
				int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleTrim(cy, periode, 0);
				
				int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleTrim(cy, periode, 1);
				int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleTrim(cy, periode, 1);
				
				String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
				String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
				
				int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
				int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
				String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
				
				fracb.setCycle(cycle_string);
				fracb.setNbreabsfeminin(nbreabsfeminin_cy);
				fracb.setNbreabsmasculin(nbreabsmasculin_cy);
				fracb.setNbreabstotal(nbreabstotal_cy);
				
				
				listofFicheRecapAbsenceCycleBean.add(fracb);
				
			}
			
		
			
			int nbreabsJfeminin = this.getNbreAbsJSexeCycleTrim(cycle, periode, 0);
			int nbreabsNJfeminin = this.getNbreAbsNJSexeCycleTrim(cycle, periode, 0);
			
			int nbreabsJmasculin = this.getNbreAbsJSexeCycleTrim(cycle, periode, 1);
			int nbreabsNJmasculin = this.getNbreAbsNJSexeCycleTrim(cycle, periode, 1);
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			int totalabsNJ = nbreabsNJfeminin+nbreabsNJmasculin;
			int totalabsJ = nbreabsJfeminin+nbreabsJmasculin;
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			
			int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleTrim(cycle, periode, 0);
			int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleTrim(cycle, periode, 0);
			
			int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleTrim(cycle, periode, 1);
			int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleTrim(cycle,periode, 1);
			
			String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
			String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
			
			int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
			int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
			String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
			
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_cy);
			fracb.setNbreabsmasculin(nbreabsmasculin_cy);
			fracb.setNbreabstotal(nbreabstotal_cy);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_cy+" nbreabsmasculin_cy=="+nbreabsmasculin_cy+" nbreabstotal_cy== "+nbreabstotal_cy);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceCycleBean;
	
	}

	@Override
	public Collection<FicheRecapAbsenceCycleBean> generateListFicheRecapAbsenceCycleAnBean(Cycles cycle, 
			Annee periode){

		
		if(periode == null) return null;
		
		List<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = 
				new ArrayList<FicheRecapAbsenceCycleBean>();
		
		String cycle_string = (cycle == null)?"Tous les cycles \n All cycles":(cycle.getCodeCycles()+" \n "+cycle.getCodeCycles_en());
		
		if(cycle == null){

			//Alors c'est le rapport complet de tous les cycles
			
			
			for(Cycles cy : this.findAllCycle()){
				FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
				int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleAn(cy, periode, 0);
				int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleAn(cy, periode, 0);
				
				int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleAn(cy, periode, 1);
				int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleAn(cy, periode, 1);
				
				String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
				String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
				
				int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
				int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
				String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
				
				fracb.setCycle(cycle_string);
				fracb.setNbreabsfeminin(nbreabsfeminin_cy);
				fracb.setNbreabsmasculin(nbreabsmasculin_cy);
				fracb.setNbreabstotal(nbreabstotal_cy);
				
				
				listofFicheRecapAbsenceCycleBean.add(fracb);
				
			}
			
		
			
			int nbreabsJfeminin = this.getNbreAbsJSexeCycleAn(cycle, periode, 0);
			int nbreabsNJfeminin = this.getNbreAbsNJSexeCycleAn(cycle, periode, 0);
			
			int nbreabsJmasculin = this.getNbreAbsJSexeCycleAn(cycle, periode, 1);
			int nbreabsNJmasculin = this.getNbreAbsNJSexeCycleAn(cycle, periode, 1);
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			int totalabsNJ = nbreabsNJfeminin+nbreabsNJmasculin;
			int totalabsJ = nbreabsJfeminin+nbreabsJmasculin;
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			
			FicheRecapAbsenceCycleBean fracb = new FicheRecapAbsenceCycleBean();
			
			int nbreabsJfeminin_cy = this.getNbreAbsJSexeCycleAn(cycle, periode, 0);
			int nbreabsNJfeminin_cy = this.getNbreAbsNJSexeCycleAn(cycle, periode, 0);
			
			int nbreabsJmasculin_cy = this.getNbreAbsJSexeCycleAn(cycle, periode, 1);
			int nbreabsNJmasculin_cy = this.getNbreAbsNJSexeCycleAn(cycle,periode, 1);
			
			String nbreabsfeminin_cy = ""+nbreabsJfeminin_cy+" / "+nbreabsNJfeminin_cy;
			String nbreabsmasculin_cy = ""+nbreabsJmasculin_cy+" / "+nbreabsNJmasculin_cy;
			
			int totalabsNJ_cy = nbreabsNJfeminin_cy+nbreabsNJmasculin_cy;
			int totalabsJ_cy = nbreabsJfeminin_cy+nbreabsJmasculin_cy;
			String nbreabstotal_cy = ""+totalabsJ_cy+" / "+totalabsNJ_cy;
			
			fracb.setCycle(cycle_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_cy);
			fracb.setNbreabsmasculin(nbreabsmasculin_cy);
			fracb.setNbreabstotal(nbreabstotal_cy);
			
			System.out.println("On ajoute dans liste fracb = "+cycle_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_cy+" nbreabsmasculin_cy=="+nbreabsmasculin_cy+" nbreabstotal_cy== "+nbreabstotal_cy);
			
			listofFicheRecapAbsenceCycleBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceCycleBean;
	
	}
	
	
	
	
	
	
	
	

	@Override
	public UtilitairesBulletins getUtilitairesBulletins() {
		return this.ub;
	}
	



}
