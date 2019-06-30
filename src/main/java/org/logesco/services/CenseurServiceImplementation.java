/**
 * 
 */
package org.logesco.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.logesco.dao.ClassesRepository;
import org.logesco.dao.CoursRepository;
import org.logesco.dao.MatieresRepository;
import org.logesco.dao.ProffesseursRepository;
import org.logesco.dao.UtilisateursRolesRepository;
import org.logesco.entities.Classes;
import org.logesco.entities.Cours;
import org.logesco.entities.Matieres;
import org.logesco.entities.Proffesseurs;
import org.logesco.entities.Roles;
import org.logesco.entities.UtilisateursRoles;
import org.logesco.modeles.FicheTitulaireparClasseBean;
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
public class CenseurServiceImplementation implements ICenseurService {

	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Autowired
	private UtilisateursRolesRepository usersrolesRepository;
	
	@Autowired
	private ProffesseursRepository proffRepository;
	
	@Autowired
	private ClassesRepository classesRepository;
	
	@Autowired
	private MatieresRepository	matieresRepository;

	@Autowired
	private CoursRepository	coursRepository;

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

	
	
	/**
	 * 
	 */
	public CenseurServiceImplementation() {
		// TODO Auto-generated constructor stub
		super();
		//this.ub = new UtilitairesBulletins();
	}



	@Override
	public int deleteMatiere(Long idMatiere) {
		/*log.log(Level.DEBUG, "Lancement de deleteMatiere "+idMatiere);*/
		Matieres matiereASupprim = usersService.findMatieres(idMatiere);
		if(matiereASupprim == null)	return -1;
		if(matiereASupprim.getListofCours().size() != 0) return 0;

		matieresRepository.delete(idMatiere);
		/*log.log(Level.DEBUG, "suppression d'une matiere "+idMatiere);*/
		return 1;
	}



	@Override
	public int deleteCours(Long idCours) {
		/*log.log(Level.DEBUG, "Lancement de deleteCours "+idCours);*/
		Cours coursASupprim = usersService.findCours(idCours);
		if(coursASupprim == null) return -1;
		if(coursASupprim.getListofEval().size() != 0) return 0;

		coursRepository.delete(idCours);
		/*log.log(Level.DEBUG, "suppression d'un cours "+idCours);*/
		return 1;
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
	public int setTitulaireClasse(Long idClasseConcerne, Long idProfTitulaire) {

		
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		Proffesseurs profTitulaire = usersService.findProffesseurs(idProfTitulaire);

		/*log.log(Level.DEBUG, "Lancement de la methode  "
				+ "setTitulaireClasse "+idClasseConcerne+" idProf "+idProfTitulaire);*/

		if((classeConcerne == null) || (profTitulaire == null)) return -1;
		////System.err.println("la classe "+classeConcerne.getIdClasses() + " sera dirige par "+profTitulaire.getNomsPers());
		/*
		 * Il faut vérifier l'existance du rôle TITULAIRE avant de faire les mise à jour
		 */

		Roles role=usersService.findRoles("TITULAIRE");

		if(role == null) return 0;
		////System.err.println(" l'enseignant  "+profTitulaire.getNomsPers()+" aura donc un role de plus qui est "+role.getRole());

		/*
		 * Il faut recuperer s'il existe l'ancien titulaire de la classe. 
		 * Par la suite apres avoir set le nouveau titulaire, on regarde si l'ancien est encore titulaire
		 * si non on le supprime le role.
		 * S'il existe et qu'il n'est desormais dirigeant d'aucune classe, alors le retirer le role titulaire
		 */
		Proffesseurs ancienprofTitulaire = classeConcerne.getProffesseur();
		
		int rep = usersService.saveUsersRoles(profTitulaire.getIdUsers(), role.getRole());

		////System.err.println(" on enregistre donc ce role comme etant un role du user");
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
	public Collection<FicheTitulaireparClasseBean> generateListFicheTitulaireparClasseBean() {
		// TODO Auto-generated method stub
		
		List<FicheTitulaireparClasseBean> collectionofTitulaireparClasse = new ArrayList<FicheTitulaireparClasseBean>();
		
		List<Classes> listofClasse = usersService.findAllClasse();
		
		for(Classes clas:listofClasse){
			FicheTitulaireparClasseBean ftpcb = new FicheTitulaireparClasseBean();
			ftpcb.setClasse(clas.getClasseString());
			if(clas.getProffesseur() == null){
				ftpcb.setTitulaire("Pas encore défini / Not yet defined");	
				ftpcb.setAdresse("");
			}
			else{
				String titulaire = clas.getProffesseur().getNomsPers()+" "+clas.getProffesseur().getPrenomsPers();
				ftpcb.setTitulaire(titulaire);	
				String adresse=clas.getProffesseur().getNumtel1Pers()+" \n "+clas.getProffesseur().getNumtel2Pers();
				ftpcb.setAdresse(adresse);
			}
			collectionofTitulaireparClasse.add(ftpcb);
		}
		
		return collectionofTitulaireparClasse;
	}
	
	
	
	
	
	

}
