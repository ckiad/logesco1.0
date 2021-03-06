/**
 * 
 */
package org.logesco.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.logesco.entities.Annee;
import org.logesco.entities.Censeurs;
import org.logesco.entities.Classes;
import org.logesco.entities.Cours;
import org.logesco.entities.Enseignants;
import org.logesco.entities.Etablissement;
import org.logesco.entities.Evaluations;
import org.logesco.entities.Intendant;
import org.logesco.entities.Matieres;
import org.logesco.entities.Niveaux;
import org.logesco.entities.Proffesseurs;
import org.logesco.entities.Proviseur;
import org.logesco.entities.SG;
import org.logesco.entities.Sequences;
import org.logesco.form.GetrapportEvalSeqForm;
import org.logesco.form.UpdateCoursForm;
import org.logesco.form.UpdateMatieresForm;
import org.logesco.form.UpdateTitulaireForm;
import org.logesco.modeles.ErrorBean;
import org.logesco.modeles.FicheTitulaireparClasseBean;
import org.logesco.services.ICenseurService;
import org.logesco.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

/**
 * @author cedrickiadjeu
 *
 */
@Controller
@RequestMapping(path="/logesco/users/censeur")
public class CenseurController {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IUsersService usersService;
	
	@Autowired
	private ICenseurService censeurService;
	
	@Value("${dir.emblemes.logo}")
	private String logoetabDir;
	
////////////////////////////////////DEBUT DES REQUETES DE TYPE GET ////////////////////////////////////////////
	
	public void constructModelUpdateMatiere(Model model,	HttpServletRequest request,	
			UpdateMatieresForm	updateMatieresForm,	Long idMatiere, 
			int numPageMatiere,	int taillePage){
		
		HttpSession session = request.getSession();
		/*
		 * On charge la liste des matieres dans le model
		 */
		Page<Matieres> pageofMatieres = usersService.findAllMatieres(numPageMatiere, taillePage);
		if(pageofMatieres != null){
			if(pageofMatieres.getContent().size()!=0){
				model.addAttribute("listofMatieres", pageofMatieres.getContent());
				int[] listofPagesMatieres=new int[pageofMatieres.getTotalPages()];
				
				model.addAttribute("listofPagesMatieres", listofPagesMatieres);
				
				model.addAttribute("pageCouranteMatiere", numPageMatiere);
				//System.err.println("numPageMatiere  "+numPageMatiere);
				session.setAttribute("numPageMatiere", numPageMatiere);
			}
		}
		/*
		 * On doit rechercher la matière dont le id est passe en paramètre au cas où ca existe
		 */
		
		Matieres matiereConcerne = usersService.findMatieres(idMatiere);
		if(matiereConcerne!=null){
			updateMatieresForm.setCodeMatiere(matiereConcerne.getCodeMatiere());
			updateMatieresForm.setIdMatiere(matiereConcerne.getIdMatiere());
			updateMatieresForm.setIntituleMatiere(matiereConcerne.getIntituleMatiere());
			
			model.addAttribute("matiereConcerne", matiereConcerne);
			session.setAttribute("matiereConcerne", matiereConcerne);
		}
		else{
			//Car l'idMatiere ne doit pas être null lors de l'appel à la methode updateMatiere au niveau du metier 
			updateMatieresForm.setIdMatiere(new Long(0));
			/*
			 * Lors de l'affichage des numero de page on passe aussi dans la requete idmatiere de l'objet matiereConcerne
			 * qui est d'ailleurs le même que celui de updateMatieresForm
			 */
		}
		
	}
	
	
	@GetMapping(path="/getupdateMatieres")
	public String getupdateMatieres(@ModelAttribute("updateMatieresForm") 
	UpdateMatieresForm updateMatieresForm,  
	@RequestParam(name="idMatiere", defaultValue="0") Long idMatiere, 
	@RequestParam(name="numPageMatiere", defaultValue="0") int numPageMatiere,
	@RequestParam(name="taillePage", defaultValue="5") int taillePage,
	Model model, HttpServletRequest request){
		
		this.constructModelUpdateMatiere(model,	request,	updateMatieresForm,	idMatiere, 
				numPageMatiere,	taillePage);
		
		return "users/updateMatieres";
	}
	
	
	public void constructModelUpdateCours(Model model,	HttpServletRequest request,	UpdateCoursForm	updateCoursForm,		
			Long idCours,	Long idMatiereAssocie,	long idUsersAssocie, Long idClasseAssocie,	int numPageCours,	int taillePage){
		
		HttpSession session = request.getSession();
		
		
		
		/*
		 * On doit chargé la classe associe dans le model
		 */
		Classes classeSelect = usersService.findClasses(idClasseAssocie);
		if(classeSelect != null) {
			//System.err.println("idclasseSelect *** "+classeSelect.getCodeClasses());
			
			updateCoursForm.setIdClasseSelect(classeSelect.getIdClasses());
			model.addAttribute("classeSelect", classeSelect);
		}
		
		
		
		Page<Cours> pageofCours = usersService.findAllCoursClasse(numPageCours, taillePage, idClasseAssocie);
		
		if(pageofCours != null){
			if(pageofCours.getContent().size()!=0){
				model.addAttribute("listofCours", pageofCours.getContent());
				int[] listofPagesCours=new int[pageofCours.getTotalPages()];
				
				model.addAttribute("listofPagesCours", listofPagesCours);
				
				model.addAttribute("pageCouranteCours", numPageCours);
				//System.err.println("numPageCours  "+numPageCours);
				session.setAttribute("numPageCours", numPageCours);
			}
		}
		
		
		//System.err.println("liste cours page par page charge ");
		/*
		 * On charge la liste des matieres dans le model pour le formulaire
		 */
		List<Matieres> listofMatiere = usersService.findAllMatieres();
		model.addAttribute("listofMatiere", listofMatiere);
		//System.err.println("liste matiere charge  === ");
		
		
		/*
		 * On fait la liste des niveaux puisque les classes seront plutot chargé par niveaux
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveaux);
		//System.err.println("liste niveau chargé  === ");
		/*
		 * On charge la liste des classes dans le model pour le formulaire
		 */
		List<Classes> listofClasses = usersService.findAllClasse();
		model.addAttribute("listofClasses", listofClasses);
		//System.err.println("liste classe charge === ");
		/*
		 * On charge la liste des Proffesseurs dans le model pour le formulaire
		 */
		List<Proffesseurs> listofProf = usersService.findAllProffesseurs();
		model.addAttribute("listofProf", listofProf);
		
		/*
		 * On charge la liste des Proviseur dans le model pour le formulaire
		 */
		List<Proviseur> listofproviseur = usersService.findAllProviseur();
		if(listofproviseur.size() != 0)	model.addAttribute("listofproviseur", listofproviseur);
		
		/*
		 * On charge la liste des Censeurs dans le model pour le formulaire
		 */
		List<Censeurs> listofcenseurs = usersService.findAllCenseurs();
		if(listofcenseurs.size() != 0)	model.addAttribute("listofcenseurs", listofcenseurs);
		
		/*
		 * On charge la liste des SG dans le model pour le formulaire
		 */
		List<SG> listofsg = usersService.findAllSG();
		if(listofsg.size() != 0)	model.addAttribute("listofsg", listofsg);
		
		/*
		 * On charge la liste des Intendants dans le model pour le formulaire
		 */
		List<Intendant> listofintendant = usersService.findAllIntendant();
		if(listofintendant.size() != 0)	model.addAttribute("listofintendant", listofintendant);
		
		/*
		 * On charge la liste des Enseignants dans le model pour le formulaire
		 */
		List<Enseignants> listofenseignants = usersService.findAllEnseignants();
		if(listofenseignants.size()	!=	0)	model.addAttribute("listofenseignants", listofenseignants);
		
		//System.err.println("liste users chargé  === ");
		
		/*
		 * On doit rechercher la cours dont le id est passe en paramètre au cas où ca existe
		 */
		
		Cours coursConcerne = usersService.findCours(idCours);
		if(coursConcerne!=null){
			//System.err.println(" coursConcerne  === trouve");
			updateCoursForm.setCodeCours(coursConcerne.getCodeCours());
			updateCoursForm.setIntituleCours(coursConcerne.getIntituleCours());
			updateCoursForm.setCoefCours(coursConcerne.getCoefCours());
			updateCoursForm.setIdCours(coursConcerne.getIdCours());
			updateCoursForm.setIdClasseSelect(coursConcerne.getClasse().getIdClasses());
			updateCoursForm.setIdMatiereAssocie(coursConcerne.getMatiere().getIdMatiere());
			updateCoursForm.setIdUsersAssocie(coursConcerne.getProffesseur().getIdUsers());
			
			model.addAttribute("coursConcerne", coursConcerne);
			session.setAttribute("coursConcerne", coursConcerne);
		}
		else{
			//System.err.println(" coursConcerne  === NONtrouve");
			//Car l'idMatiere ne doit pas être null lors de l'appel à la methode updateMatiere au niveau du metier 
			updateCoursForm.setIdCours(new Long(0));
			/*updateCoursForm.setIdClasseAssocie(new Long(0));
			updateCoursForm.setIdMatiereAssocie(new Long(0));
			updateCoursForm.setIdUsersAssocie(new Long(0));*/
			/*
			 * Lors de l'affichage des numero de page on passe aussi dans la requete idmatiere de l'objet matiereConcerne
			 * qui est d'ailleurs le même que celui de updateMatieresForm
			 */
		}
		
		//System.err.println(" TOUS EST TERMINE");
		
	}
	

	@GetMapping(path="/getupdateCours")
	public String getupdateCours(@ModelAttribute("updateCoursForm") 
	UpdateCoursForm updateCoursForm,  
	@RequestParam(name="idCours", defaultValue="0") Long idCours,
	@RequestParam(name="idMatiereAssocie", defaultValue="0") Long idMatiereAssocie, 
	@RequestParam(name="idUsersAssocie", defaultValue="0") Long idUsersAssocie, 
	@RequestParam(name="idClasseAssocie", defaultValue="0") Long idClasseAssocie, 
	@RequestParam(name="numPageCours", defaultValue="0") int numPageCours,
	@RequestParam(name="taillePage", defaultValue="15") int taillePage,
	Model model, HttpServletRequest request){
		
		//System.err.println("idClasseAssocie *** "+idClasseAssocie);
		
		this.constructModelUpdateCours(model,	request,	updateCoursForm,		idCours,	idMatiereAssocie,	idUsersAssocie, 
				idClasseAssocie,	numPageCours,	taillePage);
		
		return "users/updateCours";
	}

	
	@GetMapping(path="/getsupprimerMatieres")
	public String getsupprimerMatieres( 
			@RequestParam(name="idMatiere", defaultValue="0") Long idMatiere, 
			Model model, HttpServletRequest request){
		
		int repServeur = censeurService.deleteMatiere(idMatiere);
		
		if(repServeur == -1) return "redirect:/logesco/users/censeur/getupdateMatieres?supprimmatiereserrorMat";
		
		if(repServeur == 0) return "redirect:/logesco/users/censeur/getupdateMatieres?supprimmatiereserrorCours";
		
		return "redirect:/logesco/users/censeur/getupdateMatieres?supprimmatieressuccess";
	}
	
	
	@GetMapping(path="/getsupprimerCours")
	public String getsupprimerCours( 
			@RequestParam(name="idCours", defaultValue="0") Long idCours, 
			@RequestParam(name="idClasseAssocie", defaultValue="0") Long idClasseAssocie,
			Model model, HttpServletRequest request){
		
		int repServeur = censeurService.deleteCours(idCours);
		
		if(repServeur == -1) return "redirect:/logesco/users/censeur/getupdateCours?supprimcourserrorMat"
				+ "&&idClasseAssocie="+idClasseAssocie;
		
		if(repServeur == 0) return "redirect:/logesco/users/censeur/getupdateCours?supprimcourserrorEval"
				+ "&&idClasseAssocie="+idClasseAssocie;
		
		return "redirect:/logesco/users/censeur/getupdateCours?supprimcourssuccess"
				+ "&&idClasseAssocie="+idClasseAssocie;
		
	}
	
	
	public void constructModelUpdateTitulaire(Model model,	HttpServletRequest request,	UpdateTitulaireForm updateTitulaireForm,
			int numPageNiveaux,	int taillePage,	Long idClasseAModif,	Long idUsersTitulaire){
		
		/*
		 * On charge la liste des Proffesseurs dans le model pour le formulaire
		 */
		List<Proffesseurs> listofProf = usersService.findAllProffesseurs();
		model.addAttribute("listofProf", listofProf);
		
		/*
		 * On charge la liste des Proviseur dans le model pour le formulaire
		 */
		List<Proviseur> listofproviseur = usersService.findAllProviseur();
		if(listofproviseur.size() != 0)	model.addAttribute("listofproviseur", listofproviseur);
		
		/*
		 * On charge la liste des Censeurs dans le model pour le formulaire
		 */
		List<Censeurs> listofcenseurs = usersService.findAllCenseurs();
		if(listofcenseurs.size() != 0)	model.addAttribute("listofcenseurs", listofcenseurs);
		
		/*
		 * On charge la liste des SG dans le model pour le formulaire
		 */
		List<SG> listofsg = usersService.findAllSG();
		if(listofsg.size() != 0)	model.addAttribute("listofsg", listofsg);
		
		/*
		 * On charge la liste des Intendants dans le model pour le formulaire
		 */
		List<Intendant> listofintendant = usersService.findAllIntendant();
		if(listofintendant.size() != 0)	model.addAttribute("listofintendant", listofintendant);
		
		/*
		 * On charge la liste des Enseignants dans le model pour le formulaire
		 */
		List<Enseignants> listofenseignants = usersService.findAllEnseignants();
		if(listofenseignants.size()	!=	0)	model.addAttribute("listofenseignants", listofenseignants);
		
		
		
		
		/*  List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveaux);*/
		
		Page<Niveaux> pageofNiveaux=
				usersService.findPageNiveaux(numPageNiveaux, taillePage); 
		
		if(pageofNiveaux != null){
			if(pageofNiveaux.getContent().size()!=0){
				model.addAttribute("listofNiveaux", pageofNiveaux.getContent());
				int[] listofPagesNiveaux=new int[pageofNiveaux.getTotalPages()];
				
				model.addAttribute("listofPagesNiveaux", listofPagesNiveaux);
				
				model.addAttribute("pageCouranteNiveaux", numPageNiveaux);
				//System.err.println("numPageNiveaux  "+numPageNiveaux);
			}
		}
		updateTitulaireForm.setNumPageNiveaux(numPageNiveaux);
		
		if(idClasseAModif.longValue() != 0){
			updateTitulaireForm.setIdClassesConcerne(idClasseAModif);
		}
		
		if(idUsersTitulaire.longValue() != 0){
			updateTitulaireForm.setIdUsersTitulaire(idUsersTitulaire);
		}
		
	}
	
	@GetMapping(path="/getupdateTitulaire")
	public String getupdateTitulaire( @ModelAttribute("updateTitulaireForm") 
			UpdateTitulaireForm updateTitulaireForm,
			@RequestParam(name="numPageNiveaux", defaultValue="0") int numPageNiveaux,
			@RequestParam(name="taillePage", defaultValue="1") int taillePage,
			@RequestParam(name="idClasseAModif", defaultValue="0") Long idClasseAModif,
			@RequestParam(name="idUsersTitulaire", defaultValue="0") Long idUsersTitulaire,
			Model model, HttpServletRequest request){
		
		this.constructModelUpdateTitulaire(model,	request,	updateTitulaireForm,	numPageNiveaux,	taillePage,	idClasseAModif,	idUsersTitulaire);
		
		return "users/updateTitulaire";
	}
	
	public void constructModelGetrapportEvalSeq(Model model,	HttpServletRequest request,	
			GetrapportEvalSeqForm getrapportEvalSeqForm){
		
		/*
		 * Il faut la liste des classes car il faut choisir la classe pour laquelle on veut avoir le rapport
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		/*
		 * Il faut la liste des séquences de l'année en cours
		 */
		Annee anneeActive = usersService.findAnneeActive();
		List<Sequences> listofSequence = usersService.findAllSequence(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequence", listofSequence);
		
	}
	
	@GetMapping(path="/getrapportEvalSeq")
	public String getrapportEvalSeq( @ModelAttribute("getrapportEvalSeqForm") 
			GetrapportEvalSeqForm getrapportEvalSeqForm,
			Model model, HttpServletRequest request){
	
		this.constructModelGetrapportEvalSeq(model,	request,	getrapportEvalSeqForm);
		
		return "users/rapportEvalSeq";
	}
	
	@GetMapping(path="/exportlistTitulaireClasse")
	public ModelAndView exportlistTitulaireClasse( Model model, HttpServletRequest request){
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		String error ="";
		if(etablissementConcerne == null ||  anneeScolaire == null ){

			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters.put("erreur", erreur);
			parameters.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters);
			
		}
		
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("delegation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("delegation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
		parameters.put("etablissement_fr", etablissementConcerne.getNomsEtab().toUpperCase());
		parameters.put("etablissement_en", etablissementConcerne.getNomsanglaisEtab().toUpperCase());
		String adresse = "BP "+etablissementConcerne.getBpEtab()+
				"  TEL: "+etablissementConcerne.getNumtel1Etab();
		parameters.put("adresse", adresse);
		parameters.put("annee_scolaire_fr", "Année Académique "+anneeScolaire.getIntituleAnnee());
		parameters.put("annee_scolaire_en", "Academic year "+anneeScolaire.getIntituleAnnee());
		parameters.put("ministere_fr", etablissementConcerne.getMinisteretuteleEtab());
		parameters.put("ministere_en", etablissementConcerne.getMinisteretuteleanglaisEtab());
		parameters.put("devise_fr", etablissementConcerne.getDeviseEtab());
		parameters.put("devise_en", etablissementConcerne.getDeviseanglaisEtab());
		parameters.put("ville", etablissementConcerne.getVilleEtab());

		File f=new File(logoetabDir+etablissementConcerne.getLogoEtab());

		if(f.exists()==true){
			parameters.put("logo", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("logo", "classpath:/static/images/logobekoko.png");
		}

		parameters.put("periode", anneeScolaire.getIntituleAnnee());
		
		Collection<FicheTitulaireparClasseBean> collectionofTitulaireparClasse = 
				censeurService.generateListFicheTitulaireparClasseBean();
		
		parameters.put("datasource", collectionofTitulaireparClasse);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/FicheTitulaireparClasse.jasper");
		view.setApplicationContext(applicationContext);
		
		return new ModelAndView(view, parameters);
		
		
	}
	
	
	
	
	
	
////////////////////////////////////FIN DES REQUETES DE TYPE GET ////////////////////////////////////////////	
	
////////////////////////////////////DEBUT DES REQUETES DE TYPE POST ////////////////////////////////////////////
	
	@SuppressWarnings("deprecation")
	@PostMapping(path="/postupdateMatieres")
	public String postupdateMatieres(@Valid @ModelAttribute("updateMatieresForm") 
			UpdateMatieresForm updateMatieresForm, 
			BindingResult bindingResult, Model model, 
			HttpServletRequest request, HttpServletResponse response) 
					throws ParseException, Exception, IOException{
		
		//System.err.println("ICI AU DEBUT DE postUPDATEMatieres USERSUSERSUSERS");
		
		int cleExistante=0;
		int numPageMatiere = 0;
		Matieres matiereConcerne = null;
		for(String cle : request.getSession().getValueNames()){
			if(cle.equals("numPageMatiere")) cleExistante=cleExistante+1;
			if(cle.equals("matiereConcerne")) cleExistante=cleExistante+1;
		}
		
		//System.err.println("LES CLES RECHERCHEES EXISTENT BEL ET BIEN DANS "+cleExistante);
		if(cleExistante==2){
			numPageMatiere = (Integer)request.getSession().getAttribute("numPageMatiere");
			matiereConcerne = (Matieres)request.getSession().getAttribute("matiereConcerne");
		}
		if (bindingResult.hasErrors()) {
			//System.err.println("ERREUR DE REMPLISSAGE DU FORMULAIRE updateMatière");
			
			if(matiereConcerne!=null) {
				this.constructModelUpdateMatiere(model, request, updateMatieresForm, 
						matiereConcerne.getIdMatiere(), numPageMatiere, 3);
			}
			else{
				this.constructModelUpdateMatiere(model, request, updateMatieresForm, 
						new Long(0), 0, 3);
			}
			
			return "users/updateMatieres";
			
		}
		
		/*
		 * Ici tout est bon et on peut aller enregistrer la matieres ou le département
		 */
		Matieres matiere = new Matieres();
		
		matiere.setCodeMatiere(updateMatieresForm.getCodeMatiere());
		matiere.setIntituleMatiere(updateMatieresForm.getIntituleMatiere());
		
		matiere.setIdMatiere(updateMatieresForm.getIdMatiere() );
		
		int repServeur = censeurService.updateMatiere(matiere);
		
		if(repServeur == 0) return "redirect:/logesco/users/censeur/getupdateMatieres?updatematiereserrorCode"
				+ "&&idMatiere="+updateMatieresForm.getIdMatiere();
		if(repServeur == 2) return "redirect:/logesco/users/censeur/getupdateMatieres?updatematieressuccess";
		return "redirect:/logesco/users/censeur/getupdateMatieres?enregmatieressuccess";
	}
	
	@SuppressWarnings("deprecation")
	@PostMapping(path="/postupdateCours")
	public String postupdateCours(@Valid @ModelAttribute("updateMatieresForm") 
			UpdateCoursForm updateCoursForm, 
			BindingResult bindingResult, Model model, 
			HttpServletRequest request, HttpServletResponse response) 
					throws ParseException, Exception, IOException{
		
		//System.err.println("ICI AU DEBUT DE postupdateCours CENSEURCENSEURCENSEURCENSEUR");
		
		int cleExistante=0;
		int numPageCours = 0;
		Cours coursConcerne = null;
		for(String cle : request.getSession().getValueNames()){
			if(cle.equals("numPageCours")) cleExistante=cleExistante+1;
			if(cle.equals("coursConcerne")) cleExistante=cleExistante+1;
		}
		
		//System.err.println("LES CLES RECHERCHEES EXISTENT BEL ET BIEN DANS "+cleExistante);
		if(cleExistante==2){
			numPageCours = (Integer)request.getSession().getAttribute("numPageCours");
			coursConcerne = (Cours)request.getSession().getAttribute("coursConcerne");
		}
		
		if (bindingResult.hasErrors()) {
			//System.err.println("ERREUR DE REMPLISSAGE DU FORMULAIRE updateCours");
			
			if(coursConcerne	!=	null) {
				//System.err.println("ERREUR DE REMPLISSAGE coursConcerne  === ");
				/*this.constructModelUpdateCours(model, request, updateCoursForm, 
						coursConcerne.getIdCours(), coursConcerne.getMatiere().getIdMatiere(), 
						coursConcerne.getProffesseur().getIdUsers(), coursConcerne.getClasse().getIdClasses(), numPageCours, 3);*/
				
				return "redirect:/logesco/users/censeur/getupdateCours?updateCourserror"
							+ "&&idCours="+updateCoursForm.getIdCours()
							+ "&&idMatiereAssocie="+updateCoursForm.getIdCours()
							+ "&&idUsersAssocie="+updateCoursForm.getIdCours()
							+ "&&idClasseAssocie="+updateCoursForm.getIdClasseSelect()
							+ "&&numPageCours="+numPageCours;
				
			}
			else{
				//System.err.println("ERREUR DE REMPLISSAGE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!coursConcerne  === ");
				/*this.constructModelUpdateCours(model, request, updateCoursForm, 
						new Long(0), new Long(0), new Long(0), new Long(0), 0, 3);*/
				return "redirect:/logesco/users/censeur/getupdateCours?updateCourserror";
			}
			
			//return "users/updateCours";
			
		}
		
		/*
		 * On va d'abord vérifier que le coefficient entré est bel et bien un entier naturel
		 */
		
			Cours cours = new Cours();
			
			cours.setCodeCours(updateCoursForm.getCodeCours());
			cours.setCoefCours(updateCoursForm.getCoefCours());
			cours.setIdCours(updateCoursForm.getIdCours());
			cours.setIntituleCours(updateCoursForm.getIntituleCours());
			
			int repServeur = censeurService.updateCours(cours, updateCoursForm.getIdMatiereAssocie(), 
					updateCoursForm.getIdUsersAssocie(), updateCoursForm.getIdClasseSelect());
			
			if(repServeur == 0) return "redirect:/logesco/users/censeur/getupdateCours?updatecourserrorCode"
					+ "&&idCours="+updateCoursForm.getIdCours()
					+ "&&idMatiereAssocie="+updateCoursForm.getIdCours()
					+ "&&idUsersAssocie="+updateCoursForm.getIdCours()
					+ "&&idClasseAssocie="+updateCoursForm.getIdClasseSelect()
					+ "&&numPageCours="+numPageCours;
			
			if(repServeur == 2) return "redirect:/logesco/users/censeur/getupdateCours?updatecourssuccess"
					+ "&&idClasseAssocie="+updateCoursForm.getIdClasseSelect()
					+ "&&numPageCours="+numPageCours;
			
			//System.err.println("idClasseAssocieSelectionner *** "+updateCoursForm.getIdClasseSelect());
			
			return "redirect:/logesco/users/censeur/getupdateCours?enregcourssuccess"
					+ "&&idClasseAssocie="+updateCoursForm.getIdClasseSelect()
					+ "&&numPageCours="+numPageCours;
		
		
	}
	
	@PostMapping(path="/postupdateTitulaire")
	public String postupdateTitulaire(@Valid @ModelAttribute("updateTitulaireForm") 
			UpdateTitulaireForm updateTitulaireForm, 
			BindingResult bindingResult, Model model, 
			HttpServletRequest request, HttpServletResponse response) 
					throws ParseException, Exception, IOException{
		
		if (bindingResult.hasErrors()) {
			//System.err.println("ERREUR DE REMPLISSAGE DU FORMULAIRE updateTitulaireClasse");
			
			//System.out.println("valeur de la classe "+updateTitulaireForm.getIdClassesConcerne());
			//System.out.println("valeur du user "+updateTitulaireForm.getIdUsersTitulaire());
			
			if(updateTitulaireForm.getIdClassesConcerne() == null){
				return "redirect:/logesco/users/censeur/getupdateTitulaire?updatetutulaireerror"
						+ "&&numPageNiveaux="+updateTitulaireForm.getNumPageNiveaux();
			}
			
			if(updateTitulaireForm.getIdUsersTitulaire() == null){
				return "redirect:/logesco/users/censeur/getupdateTitulaire?updatetutulaireerror"
						+ "&&numPageNiveaux="+updateTitulaireForm.getNumPageNiveaux();
			}
			
			return "users/updateTitulaire";
			
		}
		
		
		int repServeur = censeurService.setTitulaireClasse(updateTitulaireForm.getIdClassesConcerne(), updateTitulaireForm.getIdUsersTitulaire());
		
		if(repServeur == 0) return "redirect:/logesco/users/censeur/getupdateTitulaire?updatetutulaireerror"
				+ "&&numPageNiveaux="+updateTitulaireForm.getNumPageNiveaux()
				+ "&&idClasseAModif="+updateTitulaireForm.getIdClassesConcerne()
				+ "&&idUsersTitulaire="+updateTitulaireForm.getIdUsersTitulaire();
		
		if(repServeur == -1) return "redirect:/logesco/users/censeur/getupdateTitulaire?updatetutulaireerrorTit"
				+ "&&numPageNiveaux="+updateTitulaireForm.getNumPageNiveaux()
				+ "&&idClasseAModif="+updateTitulaireForm.getIdClassesConcerne()
				+ "&&idUsersTitulaire="+updateTitulaireForm.getIdUsersTitulaire();
		
		return "redirect:/logesco/users/censeur/getupdateTitulaire?updatetutulairesuccess"
				+ "&&numPageNiveaux="+updateTitulaireForm.getNumPageNiveaux()
				+ "&&idClasseAModif="+updateTitulaireForm.getIdClassesConcerne()
				+ "&&idUsersTitulaire="+updateTitulaireForm.getIdUsersTitulaire();
	}
	
	
	@PostMapping(path="/postrapportEvalSeq")
	public String postrapportEvalSeq( @Valid @ModelAttribute("getrapportEvalSeqForm") 
		GetrapportEvalSeqForm getrapportEvalSeqForm,	BindingResult bindingResult,	Model model, 
		HttpServletRequest request, HttpServletResponse response) 
				throws ParseException, Exception{
		
		/*
		 * Il faut effectuer la liste des cours qui passe dans la classe
		 */
		/*//System.err.println("On veut les cours de la classe "+getrapportEvalSeqForm.getIdclasseRapport());
		//System.err.println("Avec les evaluations de la séquence "+getrapportEvalSeqForm.getIdsequenceRapport());*/
		
		List<Cours> listcoursofClasses = usersService.getListCoursClasse(getrapportEvalSeqForm.getIdclasseRapport());
		
		/*
		 * Il faut pour chaque cours la liste de ses évaluations pour la séquence indiqué
		 * Cette liste ne peut être automatiquement chargé puisque les cours ne se charge pas directement 
		 * avec la liste des évaluations qui lui sont liées
		 */
		List<Evaluations> listofEvalSeq = usersService.getListEvalAllCoursClasseSeq(getrapportEvalSeqForm.getIdclasseRapport(), 
				getrapportEvalSeqForm.getIdsequenceRapport());
		
		
		HttpSession session=request.getSession();
		if(listcoursofClasses != null) {
			session.setAttribute("listcoursofClassesRapportEvalSeq", listcoursofClasses);
			session.setAttribute("listofEvalSeqRapportEvalSeq", listofEvalSeq);
			session.setAttribute("numeroseqdeRapportEvalSeq", getrapportEvalSeqForm.getIdsequenceRapport());
			session.removeAttribute("erreurcoursRapportEvalSeq");
		}
		else{
			session.setAttribute("erreurcoursRapportEvalSeq", "Aucun cours n'est enregistré dans cette classe");
			session.removeAttribute("listcoursofClassesRapportEvalSeq");
			session.removeAttribute("listofEvalSeqRapportEvalSeq");
			session.removeAttribute("numeroseqdeRapportEvalSeq");
		}
		
		return "redirect:/logesco/users/censeur/getrapportEvalSeq?rapportEvalSeqsuccess";
	}
	
	
////////////////////////////////////FIN DES REQUETES DE TYPE POST ////////////////////////////////////////////
	
}
