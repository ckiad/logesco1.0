/**
 * 
 */
package org.logesco.services;



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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author cedrickiadjeu
 *implements IUsersService
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
	private SanctionDisciplinaireRepository	 sanctionDiscRepository;
	
	@Autowired
	private DecisionRepository	 decisionRepository;
	
	@Autowired
	private DecisionConseilRepository	 decisionConseilRepository;
	
	@Autowired
	private SanctionTravailRepository	 sanctionTravRepository;
	
	@Autowired
	private IdentOperationRepository	 identOpRepository;
	
	@Autowired
	private PersonnelsDAppuiRepository	 personnelsDAppuiRepository;
	
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

	@Override
	public Utilisateurs findUtilisateurs(Long idUsers){
		Utilisateurs usersAssocie=usersRepository.findOne(idUsers);
		return usersAssocie;
	}

	@Override
	public Personnels findPersonnel(String numcniPers){
		return persRepository.findByNumcniPers(numcniPers);
	}
	
	@Override
	public Personnels findPersonnelAvecMatricule(String matriculePers){
		return persRepository.findByMatriculePers(matriculePers);
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
	public Roles findRoles(String role){
		return rolesRepository.findByRole(role);
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
		////System.err.println("debut de getcodeUsersRole ");
		List<UtilisateursRoles> listofUsersRoles=(List<UtilisateursRoles>) users.getListofusersRoles();
		////System.err.println("listofUsersRoles "+listofUsersRoles.size());
		int codeRole=8;

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("CENSEUR"))) codeRole=1;
			
		}

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("SG"))) codeRole=2;
		}

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("ENSEIGNANT"))) codeRole=3;
		}
		
		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("INTENDANT"))) codeRole=4;
		}

		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("SECRETAIRE"))) codeRole=5;
		}
		
		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("SURVEILLANT"))) codeRole=6;
		}
		
		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("VEILLEUR"))) codeRole=7;
		}
	
		for(UtilisateursRoles usersRoles : listofUsersRoles){
			if(usersRoles.getRoleAssocie().getRole().equals(new String("AUTRES"))) codeRole=8;
		}

		return codeRole;
	}
	
	@Override
	public List<String> getListRolesUser(Utilisateurs users){
		List<String> listofRoleUser = new ArrayList<String>();
		List<UtilisateursRoles> listofUsersRoles=(List<UtilisateursRoles>) users.getListofusersRoles();
		for(UtilisateursRoles usersRoles : listofUsersRoles){
			listofRoleUser.add(usersRoles.getRoleAssocie().getRole());
		}
		return listofRoleUser;
	}
	
	@Override
	public boolean hasRole(Utilisateurs users, String role){
		List<String> listofRoleUser = this.getListRolesUser(users);
		for(String rol : listofRoleUser){
			if(rol.equalsIgnoreCase(role)==true){
				return true;
			}
		}
		return false;
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
	public Page<PersonnelsDAppui> findAllPersonnelsDAppui(int numPage, int taillePage){
		return personnelsDAppuiRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc(
				new PageRequest(numPage, taillePage));
	}
	
	@Override
	public List<PersonnelsDAppui> findAllPersonnelsDAppui(){
		return personnelsDAppuiRepository.findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc();
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
			////System.err.println("Ici dans findClasses EXCEPTION "+e.getMessage());
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
			////System.err.println("Ici dans findClasses avec codeClasse EXCEPTION "+e.getMessage());
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
			/*//System.err.println("montantMin "+montantMin+"() classe.getMontantScolarite()"+classe.getMontantScolarite()+""
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
		////System.err.println("numero precedent "+numero);
		int pos = numero+1;
		////System.err.println("position suivant "+pos);
		List<Eleves> listofEleveRegulierInSeq = (List<Eleves>) elv.getClasse().getListofEleves();
		////System.err.println("taille liste  "+listofEleveRegulierInSeq.size());
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
	public Page<Matieres> findAllMatieres(int numPage, int taillePage) {

		return matieresRepository.findAllByOrderByIntituleMatiereAscCodeMatiereAsc(new PageRequest(numPage, taillePage));
	}


	@Override
	public Matieres findMatieres(Long idMatiere) {

		return matieresRepository.findOne(idMatiere);
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
	public List<Classes> findAllClasseNonVide(){
		List<Niveaux> listofNiveaux = this.findAllNiveaux();
		List<Classes> listofClasseNonVide = new ArrayList<Classes>();
		for(Niveaux niv : listofNiveaux){
			for(Classes classe : niv.getListofClasses()){
				if(classe.getListofEleves().size()>0){
					listofClasseNonVide.add(classe);
				}
			}
		}
		return listofClasseNonVide;
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
	public Niveaux findNiveaux(Long idNiveaux){
		return niveauxRepository.findOne(idNiveaux);
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
		////System.err.println("======================================== ");
		List<Niveaux> listofAllNiveauxDirigesEns = new ArrayList<Niveaux>();
		List<Niveaux> listofAllNiveaux = this.findAllNiveaux();
		////System.err.println("listofAllNiveaux.size "+listofAllNiveaux.size());
		for(Niveaux niv : listofAllNiveaux){
			List<Classes> listofClassesNiv = (List<Classes>) niv.getListofClasses();
			////System.err.println("listofClassesNiv.size "+listofClassesNiv.size());
			for(Classes classe : listofClassesNiv){
				////System.err.println("est ce que classessss est null  "+classe.getIdClasses());
				if(classe.getProffesseur() != null){
					////System.err.println("est ce que classessss.getProffesseur() est null?  "+classe.getProffesseur().getDiplomePers());
					////System.err.println("classe.getProffesseur()== "+classe.getProffesseur().getIdUsers()+"   idUsers=="+idUsers);
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
		////System.err.println("listofAllNiveauxDirigesEns==  "+listofAllNiveauxDirigesEns.size());
		return listofAllNiveauxDirigesEns;
	}

	@Override
	public Page<Niveaux> findPageNiveaux(int numPage, int taillePage){

		return niveauxRepository.findAllByOrderByNumeroOrdreNiveauxAsc(new PageRequest(numPage, taillePage));
	}





	@Override
	public Page<Sequences> findAllSequences(int numPage, int taillePage) {

		return sequenceRepository.findAllByOrderByTrimestreAnneeIntituleAnneeDescTrimestreNumeroTrimAscNumeroSeqAsc(
				new PageRequest(numPage, taillePage));
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

				////System.err.println("eval.getProportion cc ds");
				evalRepository.save(eval);
				ret = 1;
			}
			else{
				ret = 0;
			}
		}
		else{

			////System.err.println("eval.getProportion  "+proportionEval+" evalId "+evalDeTypeExist.getIdEval().longValue());
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
			/*
			 * System.System.err.println("yyyyyyyyyyyyyyyyy evalConcerne non trouve au "
					+ " moment du changement de proportion d'évaluation dans updateProportionEvaluation");
			*/
			return -1;
		}
		if(new_proportion<=100){
			newproportionEval_associe = 100 - new_proportion;
			String typeEval_associe = evalConcerne.getTypeEval().equalsIgnoreCase("CC")==true?"DS":"CC";
			//System.err.println("le type d'évaluation associe est "+typeEval_associe);
			
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
		
		/*System.out.println("Lancement de la methode generatePVTrimestre "
				+ "avec idClasse="+idClasse+" idCours="+idCours+"idTrimestre="+idTrimestre);*/
		
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
			String redoublant ="";
			if(eleve.getClasse().getLangueClasses().equalsIgnoreCase("fr")==true){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true) sexe="M"; else sexe="F";
				if(eleve.getStatutEleves().equalsIgnoreCase("nouveau")==true) statut="N"; else statut="A";
				if(eleve.getRedoublant().equalsIgnoreCase("oui")==true) redoublant="O"; else redoublant="N"; 
			}
			else{
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true) sexe="M"; else sexe="F";
				if(eleve.getStatutEleves().equalsIgnoreCase("nouveau")==true) statut="N"; else statut="O";
				if(eleve.getRedoublant().equalsIgnoreCase("oui")==true) redoublant="Y"; else redoublant="N"; 
			}
			elv.setSexe(sexe);
			elv.setStatut(statut);
			elv.setRedoublant(redoublant);
			
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
			String redoublant = "";
			if(eleve.getClasse().getLangueClasses().equalsIgnoreCase("fr")==true){
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true) sexe="M"; else sexe="F";
				if(eleve.getStatutEleves().equalsIgnoreCase("nouveau")==true) statut="N"; else statut="A";
				if(eleve.getRedoublant().equalsIgnoreCase("oui")==true) redoublant="O"; else redoublant="N"; 
			}
			else{
				if(eleve.getSexeEleves().equalsIgnoreCase("masculin")==true) sexe="M"; else sexe="F";
				if(eleve.getStatutEleves().equalsIgnoreCase("nouveau")==true) statut="N"; else statut="O";
				if(eleve.getRedoublant().equalsIgnoreCase("oui")==true) redoublant="Y"; else redoublant="N"; 
			}
			elv.setSexe(sexe);
			elv.setStatut(statut);
			elv.setRedoublant(redoublant);
			
			collectionofElevedefClasse.add(elv);
		}
		
		return collectionofElevedefClasse;
	}
	
	public Collection<PersonnelBean> generateCollectionofPersonnelBean_(){
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
		List<Censeurs> listofCenseur = this.findAllCenseurs();
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
				List<SG> listofSg = this.findAllSG();
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
				List<Enseignants> listofEns = this.findAllEnseignants();
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
				List<Intendant> listofInt = this.findAllIntendant();
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
	
	
	

	
	
	

	

	

	
	
	@Override
	public Collection<PersonnelBean> generateCollectionofPersonnelDAppuiBean(){
		List<PersonnelsDAppui> listofPersonnelsDAppui = this.findAllPersonnelsDAppui();
		List<PersonnelBean> listofPersonnelBean = new ArrayList<PersonnelBean>();
		if(listofPersonnelsDAppui != null){
			int i = 1;
			for(PersonnelsDAppui pers : listofPersonnelsDAppui){

				String noms_prenoms = (pers.getNomsPers()+"  "+
						pers.getPrenomsPers()).toUpperCase();
				SimpleDateFormat spd = new SimpleDateFormat("dd/MM/yyyy");
				String date = spd.format(pers.getDatenaissPers());
				String date_naiss = date;
				String lieu_naiss = "à "+pers.getLieunaissPers();
				String numcni = pers.getNumcniPers();
				String sexe = pers.getSexePers().toLowerCase();
				String nationalite = pers.getNationalitePers();
				String grade = pers.getGradePers();
				String statut = pers.getStatutPers();
				String diplome = pers.getDiplomePers();
				String numtel1 = pers.getNumtel1Pers();
				String numtel2 = pers.getNumtel2Pers();
				if(numtel2.isEmpty()==false){
					numtel1 +="/";
					numtel1 +=numtel2;
				}
				String adresse = pers.getEmailPers();
				String numero = ""+i;
				i++;
				String matricule = pers.getMatriculePers();
				String quotah = pers.getQuotaHorairePers()+"";
				String sitmatri = pers.getSitmatriPers();
				String region = pers.getRegionoriginePers();
				String observation = pers.getObservations();
				String fonction = pers.getFonctionPers();
				
				PersonnelBean pb = new PersonnelBean();
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
	
	@Override
	public Collection<ErrorBean> generateCollectionofErrorBean(String error_msg){
		List<ErrorBean> listofErrorBean = new ArrayList<ErrorBean>();
		ErrorBean eb = new ErrorBean();
		eb.setError(error_msg);
		listofErrorBean.add(eb);
		return listofErrorBean;
	}

	
	public Collection<PersonnelBean> generateCollectionofPersonnelDeStatutBean_(String statutPers){
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
	
	public Collection<PersonnelBean> generateCollectionofProffesseursDeStatutBean_(String statutPers){

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
		if(listofOperationOnCompte == null) {
			System.out.println("Aucune operation ne concerne le compte de cet eleve jusqu'a present ");
			return null;
		}
		System.out.println("Il y a au moins une operation dans le  compte  de cet eleve ==== "+listofOperationOnCompte.size());
		if(listofOperationOnCompte.size()>0){
			Operations opOnCompte = listofOperationOnCompte.get(0);
			System.out.println("l'operation recuperer est ===  "+opOnCompte.getIdOperation()+
					" et son identifiant est  "+opOnCompte.getIdentifiantOperation());
			return opOnCompte.getIdOperation();
		}
		return null;
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
	public List<Niveaux> findListNiveauSup(Classes classe){
		Niveaux niveauClasse = classe.getNiveau();
		Niveaux niveauSupClasse = classe.getNiveau().getNiveau();
		
		List<Niveaux> listofNiveau = new ArrayList<Niveaux>();
		listofNiveau.add(niveauClasse);
		if(niveauSupClasse != null) listofNiveau.add(niveauSupClasse);
		
		return listofNiveau;
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
			Long idsanctionTravAssocie, Long idClasseFuturAssocie,  Long idDecisionAssocie){
		
		Eleves eleve = this.findEleves(idEleves);
		Annee annee = this.findAnnee(idAnneeActive);
		SanctionTravail sanctionTrav = this.findSanctionTravail(idsanctionTravAssocie);
		Decision decision = this.findDecision(idDecisionAssocie);
		Classes futurClasse = this.findClasses(idClasseFuturAssocie);
		
		if(eleve == null || annee == null || sanctionTrav == null || decision == null || futurClasse == null) return 0;
		
		DecisionConseil decConseil = this.findDecisionConseilPeriode(idEleves, idAnneeActive);
		
		if(decConseil == null){
			DecisionConseil dc = new DecisionConseil();
			dc.setDecisionAssocie(decision);
			dc.setEleveConcerne(eleve);
			dc.setPeriodeConcerne(annee);
			dc.setSanctionDiscAssocie(null);
			dc.setSanctionTravAssocie(sanctionTrav);
			dc.setFuturClasse(futurClasse);
			
			decisionConseilRepository.save(dc);
		}
		else{
			decConseil.setDecisionAssocie(decision);
			decConseil.setEleveConcerne(eleve);
			decConseil.setPeriodeConcerne(annee);
			decConseil.setSanctionDiscAssocie(null);
			decConseil.setSanctionTravAssocie(sanctionTrav);
			decConseil.setFuturClasse(futurClasse);
			
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
			
			/*
			 * Dans la boucle for on va donc faire le rapport par cycle
			 */
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
				
				String cycle_string_cy = (cy == null)?"Tous les cycles \n All cycles":(cy.getCodeCycles()+" \n "+cy.getCodeCycles_en());
				fracb.setCycle(cycle_string_cy);
				fracb.setNbreabsfeminin(nbreabsfeminin_cy);
				fracb.setNbreabsmasculin(nbreabsmasculin_cy);
				fracb.setNbreabstotal(nbreabstotal_cy);
				
				
				listofFicheRecapAbsenceCycleBean.add(fracb);
				
			}
			
			/*
			 * Hors de la boucle for, le cycle etant toujours null on va maintenant faire le total 
			 * toujours pour tous les cycles puisque cycle est null
			 */
			
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
				
				String cycle_string_cy = (cy == null)?"Tous les cycles \n All cycles":(cy.getCodeCycles()+" \n "+cy.getCodeCycles_en());
				fracb.setCycle(cycle_string_cy);
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
				
				String cycle_string_cy = (cy == null)?"Tous les cycles \n All cycles":(cy.getCodeCycles()+" \n "+cy.getCodeCycles_en());
				fracb.setCycle(cycle_string_cy);
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
				
				String cycle_string_cy = (cy == null)?"Tous les cycles \n All cycles":(cy.getCodeCycles()+" \n "+cy.getCodeCycles_en());
				fracb.setCycle(cycle_string_cy);
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
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauBean(Niveaux niveau, 
			Date datemin, Date datemax){
	
		List<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = 
				new ArrayList<FicheRecapAbsenceNiveauBean>();
		
		String niveau_string = (niveau == null)?"Tous les niveaux \n All levels":(niveau.getCodeNiveaux()+" \n "+niveau.getCodeNiveaux_en());
		
		if(niveau == null){
			//Alors c'est le rapport complet de tous les cycles
			
			/*
			 * Dans la boucle for on va donc faire le rapport par niveau
			 */
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Niveaux niv : this.findAllNiveaux()){

				FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
				int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveau(niv, datemin, datemax, 0);
				nbreabsJfeminin+=nbreabsJfeminin_niv;
				int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveau(niv, datemin, datemax, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_niv;
				
				int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveau(niv, datemin, datemax, 1);
				nbreabsJmasculin+=nbreabsJmasculin_niv;
				int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveau(niv, datemin, datemax, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_niv;
				
				String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
				String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
				
				int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
				totalabsNJ+=totalabsNJ_niv;
				int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
				totalabsJ+=totalabsJ_niv;
				String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
				
				String niveau_string_niv = (niv == null)?"Tous les niveaux \n All levels":(niv.getCodeNiveaux()+" \n "+niv.getCodeNiveaux_en());
				franb.setNiveau(niveau_string_niv);
				franb.setNbreabsfeminin(nbreabsfeminin_niv);
				franb.setNbreabsmasculin(nbreabsmasculin_niv);
				franb.setNbreabstotal(nbreabstotal_niv);
				
				
				listofFicheRecapAbsenceNiveauBean.add(franb);
				
			
			}
			
			/*
			 * Hors de la boucle for, le niveau etant toujours null on va maintenant faire le total 
			 * toujours pour tous les niveaux puisque niveau est null
			 */
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin);
			franb.setNbreabsmasculin(nbreabsmasculin);
			franb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
		
			
		}
		else{
			/*
			 * Ici on est sur qu'on a demandé le rapport pour un niveau  bien precis
			 */
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			
			int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveau(niveau, datemin, datemax, 0);
			int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveau(niveau, datemin, datemax, 0);
			
			int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveau(niveau, datemin, datemax, 1);
			int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveau(niveau, datemin, datemax, 1);
			
			String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
			String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
			
			int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
			int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
			String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
			
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin_niv);
			franb.setNbreabsmasculin(nbreabsmasculin_niv);
			franb.setNbreabstotal(nbreabstotal_niv);
			
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
		
			
		}
		
		return listofFicheRecapAbsenceNiveauBean;
	}
	
	
	@Override
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauSeqBean(Niveaux niveau, 
			Sequences periode){

		
		if(periode == null) return null;
		
		List<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = 
				new ArrayList<FicheRecapAbsenceNiveauBean>();
		
		String niveau_string = (niveau == null)?"Tous les niveaux \n All levels":(niveau.getCodeNiveaux()+" \n "+niveau.getCodeNiveaux_en());
		
		if(niveau == null){

			//Alors c'est le rapport complet de tous les niveaux
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Niveaux niv : this.findAllNiveaux()){
				FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
				int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauSeq(niv, periode, 0);
				nbreabsJfeminin+=nbreabsJfeminin_niv;
				int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauSeq(niv, periode, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_niv;
				
				int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauSeq(niv, periode, 1);
				nbreabsJmasculin+=nbreabsJmasculin_niv;
				int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauSeq(niv, periode, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_niv;
				
				String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
				String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
				
				int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
				totalabsNJ+=totalabsNJ_niv;
				int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
				totalabsJ+=totalabsJ_niv;
				String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
				
				String niveau_string_niv = (niv == null)?"Tous les niveaux \n All levels":(niv.getCodeNiveaux()+" \n "+niv.getCodeNiveaux_en());
				franb.setNiveau(niveau_string_niv);
				franb.setNbreabsfeminin(nbreabsfeminin_niv);
				franb.setNbreabsmasculin(nbreabsmasculin_niv);
				franb.setNbreabstotal(nbreabstotal_niv);
				
				
				listofFicheRecapAbsenceNiveauBean.add(franb);
				
			}
			
		
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin);
			franb.setNbreabsmasculin(nbreabsmasculin);
			franb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un niveau bien precis
			 */
			
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			
			int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauSeq(niveau, periode, 0);
			int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauSeq(niveau, periode, 0);
			
			int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauSeq(niveau, periode, 1);
			int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauSeq(niveau,periode, 1);
			
			String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
			String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
			
			int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
			int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
			String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
			
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin_niv);
			franb.setNbreabsmasculin(nbreabsmasculin_niv);
			franb.setNbreabstotal(nbreabstotal_niv);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_niv+" nbreabsmasculin_niv=="+nbreabsmasculin_niv+" nbreabstotal_niv== "+nbreabstotal_niv);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
		
			
		
		}
		
		return listofFicheRecapAbsenceNiveauBean;
	
	}
	
	
	@Override
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauTrimBean(Niveaux niveau, 
			Trimestres periode){


		if(periode == null) return null;
		
		List<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = 
				new ArrayList<FicheRecapAbsenceNiveauBean>();
		
		String niveau_string = (niveau == null)?"Tous les niveaux \n All levels":(niveau.getCodeNiveaux()+" \n "+niveau.getCodeNiveaux_en());
		
		if(niveau == null){

			//Alors c'est le rapport complet de tous les niveaux
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Niveaux niv : this.findAllNiveaux()){
				FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
				int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauTrim(niv, periode, 0);
				nbreabsJfeminin+=nbreabsJfeminin_niv;
				int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauTrim(niv, periode, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_niv;
				
				int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauTrim(niv, periode, 1);
				nbreabsJmasculin+=nbreabsJmasculin_niv;
				int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauTrim(niv, periode, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_niv;
				
				String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
				String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
				
				int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
				totalabsNJ+=totalabsNJ_niv;
				int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
				totalabsJ+=totalabsJ_niv;
				String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
				
				String niveau_string_niv = (niv == null)?"Tous les niveaux \n All levels":(niv.getCodeNiveaux()+" \n "+niv.getCodeNiveaux_en());
				franb.setNiveau(niveau_string_niv);
				franb.setNbreabsfeminin(nbreabsfeminin_niv);
				franb.setNbreabsmasculin(nbreabsmasculin_niv);
				franb.setNbreabstotal(nbreabstotal_niv);
				
				
				listofFicheRecapAbsenceNiveauBean.add(franb);
				
			}
			
		
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin);
			franb.setNbreabsmasculin(nbreabsmasculin);
			franb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un niveau bien precis
			 */
			
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			
			int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauTrim(niveau, periode, 0);
			int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauTrim(niveau, periode, 0);
			
			int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauTrim(niveau, periode, 1);
			int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauTrim(niveau,periode, 1);
			
			String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
			String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
			
			int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
			int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
			String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
			
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin_niv);
			franb.setNbreabsmasculin(nbreabsmasculin_niv);
			franb.setNbreabstotal(nbreabstotal_niv);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_niv+" nbreabsmasculin_cy=="+nbreabsmasculin_niv+" nbreabstotal_cy== "+nbreabstotal_niv);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
		
			
		
		}
		
		return listofFicheRecapAbsenceNiveauBean;
	
	
	}
	
	
	@Override
	public Collection<FicheRecapAbsenceNiveauBean> generateListFicheRecapAbsenceNiveauAnBean(Niveaux niveau, 
			Annee periode){

		if(periode == null) return null;
		
		List<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = 
				new ArrayList<FicheRecapAbsenceNiveauBean>();
		
		String niveau_string = (niveau == null)?"Tous les niveaux \n All levels":(niveau.getCodeNiveaux()+" \n "+niveau.getCodeNiveaux_en());
		
		if(niveau == null){

			//Alors c'est le rapport complet de tous les niveaux
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Niveaux niv : this.findAllNiveaux()){
				FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
				int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauAn(niv, periode, 0);
				nbreabsJfeminin+=nbreabsJfeminin_niv;
				int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauAn(niv, periode, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_niv;
				
				int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauAn(niv, periode, 1);
				nbreabsJmasculin+=nbreabsJmasculin_niv;
				int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauAn(niv, periode, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_niv;
				
				String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
				String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
				
				int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
				totalabsNJ+=totalabsNJ_niv;
				int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
				totalabsJ+=totalabsJ_niv;
				String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
				
				String niveau_string_niv = (niv == null)?"Tous les niveaux \n All levels":(niv.getCodeNiveaux()+" \n "+niv.getCodeNiveaux_en());
				franb.setNiveau(niveau_string_niv);
				franb.setNbreabsfeminin(nbreabsfeminin_niv);
				franb.setNbreabsmasculin(nbreabsmasculin_niv);
				franb.setNbreabstotal(nbreabstotal_niv);
				
				
				listofFicheRecapAbsenceNiveauBean.add(franb);
				
			}
			
		
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin);
			franb.setNbreabsmasculin(nbreabsmasculin);
			franb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			
			FicheRecapAbsenceNiveauBean franb = new FicheRecapAbsenceNiveauBean();
			
			int nbreabsJfeminin_niv = this.getNbreAbsJSexeNiveauAn(niveau, periode, 0);
			int nbreabsNJfeminin_niv = this.getNbreAbsNJSexeNiveauAn(niveau, periode, 0);
			
			int nbreabsJmasculin_niv = this.getNbreAbsJSexeNiveauAn(niveau, periode, 1);
			int nbreabsNJmasculin_niv = this.getNbreAbsNJSexeNiveauAn(niveau,periode, 1);
			
			String nbreabsfeminin_niv = ""+nbreabsJfeminin_niv+" / "+nbreabsNJfeminin_niv;
			String nbreabsmasculin_niv = ""+nbreabsJmasculin_niv+" / "+nbreabsNJmasculin_niv;
			
			int totalabsNJ_niv = nbreabsNJfeminin_niv+nbreabsNJmasculin_niv;
			int totalabsJ_niv = nbreabsJfeminin_niv+nbreabsJmasculin_niv;
			String nbreabstotal_niv = ""+totalabsJ_niv+" / "+totalabsNJ_niv;
			
			franb.setNiveau(niveau_string);
			franb.setNbreabsfeminin(nbreabsfeminin_niv);
			franb.setNbreabsmasculin(nbreabsmasculin_niv);
			franb.setNbreabstotal(nbreabstotal_niv);
			
			System.out.println("On ajoute dans liste franb = "+niveau_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_niv+" nbreabsmasculin_cy=="+nbreabsmasculin_niv+" nbreabstotal_cy== "+nbreabstotal_niv);
			
			listofFicheRecapAbsenceNiveauBean.add(franb);
		
			
		
		}
		
		return listofFicheRecapAbsenceNiveauBean;
	
	
	}

	
	
	@Override
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseBean(Classes classe, 
			Date datemin, Date datemax){

		
		List<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheRecapAbsenceClasseBean>();
		
		String classe_string = (classe == null)?"Toutes les classes \n All classess":
			(classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses());
		
		if(classe == null){
			//Alors c'est le rapport complet de tous les cycles
			
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			/*
			 * Dans la boucle for on va donc faire le rapport par classe
			 */
			for(Classes c : this.findAllClasseNonVide()){

				FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
				int nbreabsJfeminin_c = this.getNbreAbsJSexeClasse(c, datemin, datemax, 0);
				nbreabsJfeminin+=nbreabsJfeminin_c;
				int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasse(c, datemin, datemax, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_c;
				
				int nbreabsJmasculin_c = this.getNbreAbsJSexeClasse(c, datemin, datemax, 1);
				nbreabsJmasculin+=nbreabsJmasculin_c;
				int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasse(c, datemin, datemax, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_c;
				
				String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
				String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
				
				int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
				totalabsNJ+=totalabsNJ_c;
				int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
				totalabsJ+=totalabsJ_c;
				String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
				
				String classe_string_c = (c == null)?"Toutes les classes \n All classess":
					(c.getCodeClasses()+c.getSpecialite().getCodeSpecialite()+c.getNumeroClasses());
				fracb.setClasse(classe_string_c);
				fracb.setNbreabsfeminin(nbreabsfeminin_c);
				fracb.setNbreabsmasculin(nbreabsmasculin_c);
				fracb.setNbreabstotal(nbreabstotal_c);
				
				
				listofFicheRecapAbsenceClasseBean.add(fracb);
				
			
			}
			
			/*
			 * Hors de la boucle for, la classe etant toujours null on va maintenant faire le total 
			 * toujours pour toutes les classes puisque niveau est null
			 */
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
		
			
		}
		else{
			/*
			 * Ici on est sur qu'on a demandé le rapport pour une classe  bien precise
			 */
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			
			int nbreabsJfeminin_c = this.getNbreAbsJSexeClasse(classe, datemin, datemax, 0);
			int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasse(classe, datemin, datemax, 0);
			
			int nbreabsJmasculin_c = this.getNbreAbsJSexeClasse(classe, datemin, datemax, 1);
			int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasse(classe, datemin, datemax, 1);
			
			String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
			String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
			
			int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
			int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
			String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
			
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_c);
			fracb.setNbreabsmasculin(nbreabsmasculin_c);
			fracb.setNbreabstotal(nbreabstotal_c);
			
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
		
			
		}
		
		return listofFicheRecapAbsenceClasseBean;
	
	}
	
	@Override
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseSeqBean(Classes classe, 
			Sequences periode){


		
		if(periode == null) return null;
		
		List<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheRecapAbsenceClasseBean>();
		
		String classe_string = (classe == null)?"Toutes les classes \n All classes":
			(classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses());
		
		if(classe == null){

			//Alors c'est le rapport complet de tous les niveaux
			int nbreabsJfeminin=0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin=0;
			int nbreabsNJmasculin =0;
			int totalabsJ =0;
			int totalabsNJ =0;
			
			for(Classes c : this.findAllClasseNonVide()){
				FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
				int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseSeq(c, periode, 0);
				nbreabsJfeminin +=nbreabsJfeminin_c;
				int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseSeq(c, periode, 0);
				nbreabsNJfeminin += nbreabsNJfeminin_c;
				
				int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseSeq(c, periode, 1);
				nbreabsJmasculin +=nbreabsJmasculin_c;
				int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseSeq(c, periode, 1);
				nbreabsNJmasculin += nbreabsNJmasculin_c;
				
				String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
				String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
				
				int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
				totalabsNJ += totalabsNJ_c;
				int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
				totalabsJ +=totalabsNJ_c;
				String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
				
				String classe_string_c = (c == null)?"Toutes les classes \n All classess":
					(c.getCodeClasses()+c.getSpecialite().getCodeSpecialite()+c.getNumeroClasses());
				fracb.setClasse(classe_string_c);
				fracb.setNbreabsfeminin(nbreabsfeminin_c);
				fracb.setNbreabsmasculin(nbreabsmasculin_c);
				fracb.setNbreabstotal(nbreabstotal_c);
				
				
				listofFicheRecapAbsenceClasseBean.add(fracb);
				
			}
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un niveau bien precis
			 */
			
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			
			int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseSeq(classe, periode, 0);
			int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseSeq(classe, periode, 0);
			
			int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseSeq(classe, periode, 1);
			int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseSeq(classe,periode, 1);
			
			String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
			String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
			
			int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
			int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
			String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
			
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_c);
			fracb.setNbreabsmasculin(nbreabsmasculin_c);
			fracb.setNbreabstotal(nbreabstotal_c);
			
			System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin_c=="
					+ " "+nbreabsfeminin_c+" nbreabsmasculin_niv=="+nbreabsmasculin_c+" nbreabstotal_c== "+nbreabstotal_c);
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceClasseBean;
	
	
	}

	
	@Override
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseTrimBean(Classes classe, 
			Trimestres periode){


		if(periode == null) return null;
		
		List<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheRecapAbsenceClasseBean>();
		
		String classe_string = (classe == null)?"Toutes les classes \n All classes":
			(classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses());
		
		if(classe == null){

			//Alors c'est le rapport complet de toutes les classes
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Classes c : this.findAllClasseNonVide()){
				FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
				int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseTrim(c, periode, 0);
				nbreabsJfeminin+=nbreabsJfeminin_c;
				int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseTrim(c, periode, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_c;
				
				int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseTrim(c, periode, 1);
				nbreabsJmasculin+=nbreabsJmasculin_c;
				int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseTrim(c, periode, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_c;
				
				String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
				String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
				
				int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
				totalabsNJ+=totalabsNJ_c;
				int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
				totalabsJ+=totalabsJ_c;
				String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
				
				String classe_string_c = (c == null)?"Toutes les classes \n All classess":
					(c.getCodeClasses()+c.getSpecialite().getCodeSpecialite()+c.getNumeroClasses());
				fracb.setClasse(classe_string_c);
				fracb.setNbreabsfeminin(nbreabsfeminin_c);
				fracb.setNbreabsmasculin(nbreabsmasculin_c);
				fracb.setNbreabstotal(nbreabstotal_c);
				
				
				listofFicheRecapAbsenceClasseBean.add(fracb);
				
			}
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un niveau bien precis
			 */
			
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			
			int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseTrim(classe, periode, 0);
			int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseTrim(classe, periode, 0);
			
			int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseTrim(classe, periode, 1);
			int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseTrim(classe,periode, 1);
			
			String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
			String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
			
			int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
			int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
			String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
			
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_c);
			fracb.setNbreabsmasculin(nbreabsmasculin_c);
			fracb.setNbreabstotal(nbreabstotal_c);
			
			System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin_cy=="
					+ " "+nbreabsfeminin_c+" nbreabsmasculin_cy=="+nbreabsmasculin_c+" nbreabstotal_cy== "+nbreabstotal_c);
			
			listofFicheRecapAbsenceClasseBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceClasseBean;
	
	
	
	}
	
	@Override
	public Collection<FicheRecapAbsenceClasseBean> generateListFicheRecapAbsenceClasseAnBean(Classes classe, 
			Annee periode){


		if(periode == null) return null;
		
		List<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheRecapAbsenceClasseBean>();
		
		String classe_string = (classe == null)?"Toutes les classes \n All classes":
			(classe.getCodeClasses()+classe.getSpecialite().getCodeSpecialite()+classe.getNumeroClasses());
		
		
		if(classe == null){

			//Alors c'est le rapport complet de tous les niveaux
			int nbreabsJfeminin =0;
			int nbreabsNJfeminin =0;
			int nbreabsJmasculin = 0;
			int nbreabsNJmasculin =0;
			int totalabsNJ = 0;
			int totalabsJ = 0;
			
			for(Classes c : this.findAllClasseNonVide()){
				FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
				int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseAn(c, periode, 0);
				nbreabsJfeminin+=nbreabsJfeminin_c;
				int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseAn(c, periode, 0);
				nbreabsNJfeminin+=nbreabsNJfeminin_c;
				
				int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseAn(c, periode, 1);
				nbreabsJmasculin+=nbreabsJmasculin_c;
				int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseAn(c, periode, 1);
				nbreabsNJmasculin+=nbreabsNJmasculin_c;
				
				String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
				String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
				
				int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
				totalabsNJ+=totalabsNJ_c;
				int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
				totalabsJ+=totalabsJ;
				String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
				
				String classe_string_c = (c == null)?"Toutes les classes \n All classess":
					(c.getCodeClasses()+c.getSpecialite().getCodeSpecialite()+c.getNumeroClasses());
				fracb.setClasse(classe_string_c);
				fracb.setNbreabsfeminin(nbreabsfeminin_c);
				fracb.setNbreabsmasculin(nbreabsmasculin_c);
				fracb.setNbreabstotal(nbreabstotal_c);
				
				
				listofFicheRecapAbsenceClasseBean.add(fracb);
				
			}
			
			String nbreabsfeminin = ""+nbreabsJfeminin+" / "+nbreabsNJfeminin;
			String nbreabsmasculin = ""+nbreabsJmasculin+" / "+nbreabsNJmasculin;
			
			String nbreabstotal= ""+totalabsJ+" / "+totalabsNJ;
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin);
			fracb.setNbreabsmasculin(nbreabsmasculin);
			fracb.setNbreabstotal(nbreabstotal);
			
			/*System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin=="
					+ " "+nbreabsfeminin+" nbreabsmasculin=="+nbreabsmasculin+" nbreabstotal== "+nbreabstotal);
			*/
			listofFicheRecapAbsenceClasseBean.add(fracb);
			
		
		}
		else{

			/*
			 * Ici on est sur qu'on a demandé le rapport pour un cycle bien precis
			 */
			
			
			FicheRecapAbsenceClasseBean fracb = new FicheRecapAbsenceClasseBean();
			
			int nbreabsJfeminin_c = this.getNbreAbsJSexeClasseAn(classe, periode, 0);
			int nbreabsNJfeminin_c = this.getNbreAbsNJSexeClasseAn(classe, periode, 0);
			
			int nbreabsJmasculin_c = this.getNbreAbsJSexeClasseAn(classe, periode, 1);
			int nbreabsNJmasculin_c = this.getNbreAbsNJSexeClasseAn(classe,periode, 1);
			
			String nbreabsfeminin_c = ""+nbreabsJfeminin_c+" / "+nbreabsNJfeminin_c;
			String nbreabsmasculin_c = ""+nbreabsJmasculin_c+" / "+nbreabsNJmasculin_c;
			
			int totalabsNJ_c = nbreabsNJfeminin_c+nbreabsNJmasculin_c;
			int totalabsJ_c = nbreabsJfeminin_c+nbreabsJmasculin_c;
			String nbreabstotal_c = ""+totalabsJ_c+" / "+totalabsNJ_c;
			
			fracb.setClasse(classe_string);
			fracb.setNbreabsfeminin(nbreabsfeminin_c);
			fracb.setNbreabsmasculin(nbreabsmasculin_c);
			fracb.setNbreabstotal(nbreabstotal_c);
			
			/*System.out.println("On ajoute dans liste fracb = "+classe_string+" nbreabsfeminin_c=="
					+ " "+nbreabsfeminin_c+" nbreabsmasculin_c=="+nbreabsmasculin_c+" nbreabstotal_c== "+nbreabstotal_c);
			*/
			listofFicheRecapAbsenceClasseBean.add(fracb);
		
			
		
		}
		
		return listofFicheRecapAbsenceClasseBean;
	
	}
	
	public Collection<FicheScolariteparClasseBean> generateListFicheScolariteparClasseBean_(){
		List<FicheScolariteparClasseBean> listofFicheRecapAbsenceClasseBean = 
				new ArrayList<FicheScolariteparClasseBean>();
		
		List<Classes> listofClasse = this.findAllClasse();
		
		for(Classes cl : listofClasse){
			FicheScolariteparClasseBean fspcb = new FicheScolariteparClasseBean();
			
			fspcb.setClasse(cl.getClasseString());
			if(cl.getMontantScolarite()>0) fspcb.setMontantscolarite(cl.getMontantScolarite()+" F cfa");
			fspcb.setNiveau(cl.getNiveau().getNiveauString());
			
			listofFicheRecapAbsenceClasseBean.add(fspcb);
		}
		
		return listofFicheRecapAbsenceClasseBean;
	}
	
	
	@Override
	public UtilitairesBulletins getUtilitairesBulletins() {
		return this.ub;
	}
	



}
