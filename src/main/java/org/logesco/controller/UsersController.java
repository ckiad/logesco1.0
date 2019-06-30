/**
 * 
 */
package org.logesco.controller;


import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.logesco.entities.*;
import org.logesco.form.*;
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
import org.logesco.modeles.Recu_versement;
import org.logesco.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

/**
 * @author cedrickiadjeu
 *
 */
@Controller
@RequestMapping(path="/logesco/users")
public class UsersController {
	
	@Value("${dir.emblemes.logo}")
	private String logoetabDir;
	/**
	 * Objet metier qui permettra au controleur d'avoir les méthodes metiers
	 */

	@Autowired
	private IAdminService adminService;
	
	@Autowired
	private IUsersService usersService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	DateFormat dfNameJour=new SimpleDateFormat("EEEE");
	DateFormat dfJour=new SimpleDateFormat("dd");
	DateFormat dfmois=new SimpleDateFormat("MMMM");
	DateFormat dfyear=new SimpleDateFormat("yyyy");
	DateFormat dfheure=new SimpleDateFormat("H:m:s");
	Date dateJour=new Date();
	
	//////////////////////////////////// DEBUT DES REQUETES DE TYPE GET ////////////////////////////////////////////
	
	
	@GetMapping(path="/index")
	public String indexUsers(@ModelAttribute("modifpasswordForm") 
		ModifpasswordForm modifpasswordForm, 
		Model model, HttpServletRequest request) throws ParseException{
		
		
		
		HttpSession session=request.getSession();
		String lang = (String)session.getAttribute("lang");
		
		//System.out.println("le langage choisi estestest "+lang);
		
		if(lang.equals("fr")==true){
    		Locale localeFr = new Locale("fr","FR");
    		dfNameJour = DateFormat.getDateInstance(DateFormat.FULL, localeFr);
    		dfmois = DateFormat.getDateInstance(DateFormat.FULL, localeFr);
    	}
    	else if(lang.equals("en")==true){
    		Locale localeEn = new Locale("en","EN");
    		dfNameJour = DateFormat.getDateInstance(DateFormat.FULL, localeEn);
    		dfmois = DateFormat.getDateInstance(DateFormat.FULL, localeEn);
    	}
		
		session.setAttribute("dfNameJour", dfNameJour.format(dateJour).toUpperCase());
		/*session.setAttribute("dfJour", dfJour.format(dateJour));
		session.setAttribute("dfmois", dfmois.format(dateJour));
		session.setAttribute("dfyear", dfyear.format(dateJour));*/
		session.setAttribute("dfheure", dfheure.format(dateJour));
		
		/*
		 * Il faut recuperer le username du user connecte et placer dans la session
		 * avant toutes redirection vers la page demandée 
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		/*//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxxxxxxxxxx"+auth.getName());*/
		model.addAttribute("username", auth.getName());

		/*//System.out.println(String.format("Modèle=%s, Session[username]=%s", model, 
				session.getAttribute("username")));*/

		//Utiliser pour retrouver la photos de l'utilisateur connecté
		session.setAttribute("username", auth.getName());

		/*//System.out.println(String.format("Modèle=%s, Session[usernamess]=%s", model, 
				session.getAttribute("username")));*/
		
		/*
		 * On doit charger la liste des niveaux car dans le menu un users censeur doit pouvoir
		 * selectionner la classe qu'il désire configurer les cours. Et cette liste doit être dans la session
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		if(listofNiveaux.size() > 0) session.setAttribute("listofNiveaux", listofNiveaux);
		
		return "users/indexUsers";
	}
	
	
	
	/**************************************************************
	 * Traitement de la requete de recherche de la photo d'un utilisateur
	 *************************************************************/
	@RequestMapping(path="/getphotoPers", produces=MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getphotoPers(Long idPers){
		//System.out.println("DEPART DE LA RECHERCHE DE LA PHOTO  du personnel connecté  "+idPers);
		File f=new File(photoPersonnelsDir+idPers);
		try{
			//System.out.println("nous voici ici et chemin fichier est = "+photoPersonnelsDir+idPers);
			return IOUtils.toByteArray(new FileInputStream(f));
		}
		catch(Exception e){
			//System.out.println("erreur de recherche de la photos "+e.getMessage());
			return null;
		}
	}
	
	/******************************************************
	 * Retourne la photos d'un élève qu'on vient d'enregistrer
	 *******************************************************/
	@RequestMapping(path="/getphotoEleve", produces=MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getphotoEleve(Long idEleves){
		//System.out.println("DEPART DE LA RECHERCHE DE LA PHOTO  de l'élève enregistré "+idEleves);
		
		File f=new File(photoElevesDir+idEleves);
		try{
			//System.out.println("nous voici ici et chemin fichier est ="+photoElevesDir+idEleves);
			return IOUtils.toByteArray(new FileInputStream(f));
		}
		catch(Exception e){
			//System.out.println("erreur de recherche de la photos "+e.getMessage());
			return null;
		}
		
	}
	
	/*****************************************************************************
	 * Retourne la photos de la personne connecté. Lorsqu'une personne est connecte
	 * son nom d'utilisateur est dans la requete dans le champ httpServletRequest.remoteUser
	 ******************************************************************************/
	@RequestMapping(path="/getphotoPersonnelConnecte", produces=MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] getphotoPersonnelConnecte(HttpServletRequest request){
		HttpSession session=request.getSession();
		//System.out.println("DEPART DE LA RECHERCHE DE LA PHOTO  "+session.getAttribute("username"));
		/*
		 * Il faut rechercher le personnel associe a ce nom d'utilisateur car c'est avec son nom d'utilisateur
		 * qu'on va retrouver sa photos
		 */
		String username=(String)session.getAttribute("username");
		
		File f=new File(photoPersonnelsDir+usersService.findByUsername(username).getIdUsers());
		try{
			/*//System.out.println("nous voici ici et chemin fichier est ="+photoPersonnelsDir+
					usersService.findByUsername(username).getIdUsers());*/
			return IOUtils.toByteArray(new FileInputStream(f));
		}
		catch(Exception e){
			//System.out.println("erreur sur idusers "+e.getMessage());
			return null;
		}
		
	}
	
	/********************************************************************
	 * Recherche d'un élève que peut être sa classe est inconnue. Cette 
	 * fonctionnalité est importante dans les sessions du proviseur et de l'intendant. 
	 * @param motifrechercheEleve
	 * @param numPageEleves
	 * @param model
	 * @param request
	 * @return
	 **************************************************************/
	
	@GetMapping(path="/getrechercheEleve")
	public String getrechercheEleve(
			@RequestParam(name="motifrechercheEleve", defaultValue=" ") String motifrechercheEleve,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			  Model model, HttpServletRequest request){
		
		model.addAttribute("motifrechercheEleve", motifrechercheEleve);
		
		String motifrechEleve = "%"+motifrechercheEleve+"%";
		
		Page<Eleves> pageofEleves=usersService.findListElevesSelonMotif(motifrechEleve,	numPageEleves, 10);
		if(pageofEleves.getContent().size()!=0){
			model.addAttribute("listdefofEleves", pageofEleves.getContent());
			int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
				
			model.addAttribute("listdefofPagesEleves", listofPagesEleves);
				
			model.addAttribute("pagedefCouranteEleves", numPageEleves);
			//System.out.println("la liste des élève contient "+pageofEleves.getContent().size());
		}
		
		/*for(Eleves elv : pageofEleves.getContent()){
			//System.out.println("-------"+elv.getNomsEleves()+"------"+elv.getPrenomsEleves());
		}*/
		
		return "users/resultatRechEleves";
	}
	
	
	
	public void constructModelListeEleves(Model model, HttpServletRequest request,
			long idClasseSelect,  int numPageprovEleves){
		
		HttpSession session=request.getSession();
		
		/*
		 * Il faut faire la liste des classes et placer dans le model puisqu'on 
		 * va s'en servir pour les etiquetes des classes
		 */
		List<Classes> listofClasses=usersService.findListClasse();
		
		model.addAttribute("listofClasses", listofClasses);
		
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		Long idClasseAEnvoyer=null;
		if(idClasseSelect!=-1){
			idClasseAEnvoyer=new Long(idClasseSelect);
			/*
			 * On place idClasseSelect dans le modele puisqu'au niveau des liens de numerotation des pages il doit être
			 * utilisé pour savoir dans quel classe on se trouvait 
			 */
			model.addAttribute("idClasseSelect", idClasseSelect);
			
			/*
			 * On va rechercher la classe selectionné car on doit la placer aussi dans le model
			 */
			Classes classeSelect = usersService.findClasses(idClasseSelect);
			model.addAttribute("classeSelect", classeSelect);
		
		
			/*
			 * On veut mettre aussi l'effectif total provisoire des élèves de la classe selectionné 
			 * ou des eleves deja enregistre dans l'etablissement
			 */
			int effectifprovClasse=usersService.getEffectifProvisoireClasse(idClasseSelect);
			model.addAttribute("effectifprovClasse", effectifprovClasse);
			
			List<Eleves> listprovofEleves = usersService.findListElevesClasse(idClasseSelect);
			
			session.setAttribute("listprovofEleves", listprovofEleves);
			session.setAttribute("classeSelect", classeSelect);
			
			Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseAEnvoyer,	
					numPageprovEleves, 10);
			if(pageofEleves.getContent().size()!=0){
				model.addAttribute("listprovofEleves", pageofEleves.getContent());
				int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
					
				model.addAttribute("listprovofPagesEleves", listofPagesEleves);
					
				model.addAttribute("pageprovCouranteEleves", numPageprovEleves);
				//System.out.println("la liste des élève contient "+pageofEleves.getContent().size());
			}
		}
	}
	
	@GetMapping(path="/getlisteProvEleves")
	public String getlisteProvEleves(@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect,
			@RequestParam(name="numPageprovEleves", defaultValue="0") int numPageprovEleves,
			  Model model, HttpServletRequest request){
		
		this.constructModelListeEleves(model,	request,  idClasseSelect,  numPageprovEleves);
		
		return "users/listeProvEleves";
	}
	
	@GetMapping(path="/exportlistprovEleves")
	public ModelAndView exportlistprovEleves(Model model, HttpServletRequest request,
			@RequestParam(name="idClasseSelect", defaultValue="0") long idClasseSelect){
	
		//System.out.println("----------------------- IMPRESSION DES LISTES PROVISOIRES -------------------");
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Classes classeSelect = usersService.findClasses(idClasseSelect);
		
		Annee anneeScolaire = usersService.findAnneeActive();
		String error ="";
		if(etablissementConcerne == null || classeSelect == null || anneeScolaire == null) {
			//System.out.println("la classe selectionner est null dans le code donc mauvaise recuperation");
			

			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeSelect == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
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
		
		
		
		Collection<EleveBean> collectionofEleveprovClasse = 
				usersService.generateCollectionofEleveprovClasse(classeSelect.getIdClasses());
		
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
		
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		
		String nomClasse= classeSelect.getCodeClasses()+classeSelect.getSpecialite().getCodeSpecialite()+
				classeSelect.getNumeroClasses();
		
		JasperReportsPdfView view = new JasperReportsPdfView();
		
		if(classeSelect.getLangueClasses().equalsIgnoreCase("fr")==true){
			parameters.put("titre_liste", "LISTE PROVISOIRE DES ELEVES DE  "+nomClasse);
			view.setUrl("classpath:/reports/compiled/fiches/ListeEleveParClasse.jasper");
		}
		else if(classeSelect.getLangueClasses().equalsIgnoreCase("en")==true){
			parameters.put("titre_liste", "PROVISIONAL STUDENT'S LIST OF  "+nomClasse);
			view.setUrl("classpath:/reports/compiled/fiches/ListeEleveParClasse.jasper");
		}
		
		view.setApplicationContext(applicationContext);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		
		params.put("parameters", parameters); 
		params.put("datasource", collectionofEleveprovClasse);
		
		parameters.put("datasource", collectionofEleveprovClasse);
		
		return new ModelAndView(view, parameters);
	}
	
	public void constructModelListOperationEleves(Model model, HttpServletRequest request,
			long idClasseSelect,  int numPageOperationEleves){
		HttpSession session=request.getSession();
		/*
		 * Il faut faire la liste des classes et placer dans le model puisqu'on 
		 * va s'en servir pour les etiquetes des classes
		 */
		List<Classes> listofClasses=usersService.findListClasse();
		
		model.addAttribute("listofClasses", listofClasses);
		
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		if(idClasseSelect!=-1){
			/*
			 * On place idClasseSelect dans le modele puisqu'au niveau des liens de numerotation des pages il doit être
			 * utilisé pour savoir dans quel classe on se trouvait 
			 */
			model.addAttribute("idClasseSelect", idClasseSelect);
			
			/*
			 * On va rechercher la classe selectionné car on doit la placer aussi dans le model
			 */
			Classes classeSelect = usersService.findClasses(idClasseSelect);
			model.addAttribute("classeSelect", classeSelect);
			
			/*
			 * On veut mettre aussi l'effectif total des insolvables d'une classe dans le modèle
			 */
			int effectiftotalClasse = usersService.getEffectifProvisoireClasse(idClasseSelect);
			model.addAttribute("effectiftotalClasse", effectiftotalClasse);
			
			List<Eleves> listprovofEleves = usersService.findListElevesClasse(idClasseSelect);
			
			session.setAttribute("listprovofEleves", listprovofEleves);
			session.setAttribute("classeSelect", classeSelect);
			
			Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseSelect,	
					numPageOperationEleves, 10);
			if(pageofEleves.getContent().size()!=0){
				model.addAttribute("listeleveDansClasses", pageofEleves.getContent());
				int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
					
				model.addAttribute("listofPagesEleves", listofPagesEleves);
					
				model.addAttribute("pageCouranteEleves", numPageOperationEleves);
				//System.out.println("la liste des élève contient "+pageofEleves.getContent().size());
			}
		}
		
	}
	
	@GetMapping(path="/getlistOperationsEleves")
	public String getlistOperationsEleves(@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect,
			@RequestParam(name="numPageOperationEleves", defaultValue="0") int numPageOperationEleves,
			  Model model, HttpServletRequest request){
		
		this.constructModelListOperationEleves(model,	request,  idClasseSelect,  numPageOperationEleves);
		
		return "users/listeOperationEleves";
	}
	
	
	public void constructModelListeElevesInsolvable(Model model, HttpServletRequest request,
			long idClasseSelect,  int numPageElevesInsolvable){
		
		HttpSession session=request.getSession();
		
		/*
		 * Il faut faire la liste des classes et placer dans le model puisqu'on 
		 * va s'en servir pour les etiquetes des classes
		 */
		List<Classes> listofClasses=usersService.findListClasse();
		
		model.addAttribute("listofClasses", listofClasses);
		
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		//Long idClasseAEnvoyer=null;
		if(idClasseSelect!=-1){
			//idClasseAEnvoyer=new Long(idClasseSelect);
			/*
			 * On place idClasseSelect dans le modele puisqu'au niveau des liens de numerotation des pages il doit être
			 * utilisé pour savoir dans quel classe on se trouvait 
			 */
			model.addAttribute("idClasseSelect", idClasseSelect);
			
			/*
			 * On va rechercher la classe selectionné car on doit la placer aussi dans le model
			 */
			Classes classeSelect = usersService.findClasses(idClasseSelect);
			model.addAttribute("classeSelect", classeSelect);
		
		
			/*
			 * On veut mettre aussi l'effectif total des insolvables d'une classe dans le modèle
			 */
			int effectifinsolvableDansClasse=usersService.getEffectifInsolvableDansClasse(idClasseSelect);
			model.addAttribute("effectifinsolvableDansClasse", effectifinsolvableDansClasse);
			
			int effectiftotalClasse = usersService.getEffectifProvisoireClasse(idClasseSelect);
			model.addAttribute("effectiftotalClasse", effectiftotalClasse);
			
			List<Eleves> listeleveinsolvableDansClasses = usersService.getListElevesInsolvable(idClasseSelect);
			
			session.setAttribute("listeleveinsolvableDansClasses", listeleveinsolvableDansClasses);
			session.setAttribute("classeSelect", classeSelect);
			
			Page<Eleves> pageofElevesInsolvable = usersService.findPageElevesInsolvable(idClasseSelect, 
					numPageElevesInsolvable, 10);
			if(pageofElevesInsolvable.getContent().size()!=0){
				model.addAttribute("listeleveinsolvableDansClasses", pageofElevesInsolvable.getContent());
				int[] listofPagesElevesInsolvable=new int[pageofElevesInsolvable.getTotalPages()];
					
				model.addAttribute("listofPagesElevesInsolvable", listofPagesElevesInsolvable);
					
				model.addAttribute("pageCouranteElevesInsolvable", numPageElevesInsolvable);
				//System.out.println("la liste des élève contient "+pageofEleves.getContent().size());
			}
			
		}
		
		
		
	}
	
	@GetMapping(path="/getlisteElevesInsolvable")
	public String getlisteElevesInsolvable(@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect,
			@RequestParam(name="numPageElevesInsolvable", defaultValue="0") int numPageElevesInsolvable,
			  Model model, HttpServletRequest request){
		
		this.constructModelListeElevesInsolvable(model,	request,  idClasseSelect,  numPageElevesInsolvable);
		
		return "users/listeElevesInsolvable";
	}
	
	
	
	public void constructModelListeDefEleves(Model model, HttpServletRequest request,
			long idClasseSelect,  int critere,	int numPagedefEleves){
		
		HttpSession session=request.getSession();
		
		/*
		 * Il faut faire la liste des classes et placer dans le model puisqu'on 
		 * va s'en servir pour les etiquetes des classes
		 */
		List<Classes> listofClasses=usersService.findListClasse();
		
		model.addAttribute("listofClasses", listofClasses);
		
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		Long idClasseAEnvoyer=null;
		if(idClasseSelect!=-1){
			idClasseAEnvoyer=new Long(idClasseSelect);
			/*
			 * On place idClasseSelect dans le modele puisqu'au niveau des liens de numerotation des pages il doit être
			 * utilisé pour savoir dans quel classe on se trouvait 
			 */
			model.addAttribute("idClasseSelect", idClasseSelect);
			
			/*
			 * On va rechercher la classe selectionné car on doit la placer aussi dans le model
			 */
			Classes classeSelect = usersService.findClasses(idClasseSelect);
			model.addAttribute("classeSelect", classeSelect);
		
			model.addAttribute("critere", critere);
			
			session.setAttribute("classeSelect", classeSelect);
		
			/*
			 * On veut mettre aussi l'effectif total provisoire des élèves de la classe selectionné 
			 * ou des eleves deja enregistre dans l'etablissement
			 */
			int effectifprovClasse=usersService.getEffectifProvisoireClasse(idClasseSelect);
			model.addAttribute("effectifprovClasse", effectifprovClasse);
			session.setAttribute("effectifprovClasse", effectifprovClasse);
			
			/*
			 * On veut mettre aussi l'effectif total définitif des élèves de la classe selectionné 
			 * selon le critère choisi
			 */
			int effectifdefClasse=usersService.getEffectifDefinitifClasse(idClasseSelect, critere);
			model.addAttribute("effectifdefClasse", effectifdefClasse);
			session.setAttribute("effectifdefClasse", effectifdefClasse);
			//System.out.println("effectifdefClasse "+effectifdefClasse);
			
			
			/*
			 * Calcul du montant selon le critere en pourcentage saisi
			 */
			double coefMontant = critere * 0.01;
			double montantMin = classeSelect.getMontantScolarite() * coefMontant;
			
			model.addAttribute("montantMin", montantMin);
			
			/*
			 * On place aussi toute la liste des eleves définitif selon le critere dans la session
			 */
			List<Eleves> listdefofEleves = usersService.findListElevesDefClasse(idClasseSelect, montantMin);
			//System.out.println("listdefofEleves "+listdefofEleves.size());
			
			session.setAttribute("listdefofEleves", listdefofEleves);
			
			Page<Eleves> pageDefofEleves=usersService.findPageDefElevesClasse(idClasseAEnvoyer,	montantMin,
					numPagedefEleves, 10);
			if(pageDefofEleves.getContent().size()!=0){
				model.addAttribute("listdefofEleves", pageDefofEleves.getContent());
				int[] listofPagesEleves=new int[pageDefofEleves.getTotalPages()];
					
				model.addAttribute("listdefofPagesEleves", listofPagesEleves);
					
				model.addAttribute("pagedefCouranteEleves", numPagedefEleves);
				//System.out.println("la liste des élève contient "+pageDefofEleves.getContent().size());
			}
		}
		
	}
	
	@GetMapping(path="/getlisteDefEleves")
	public String getlisteDefEleves(
			@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect,
			@RequestParam(name="critere", defaultValue="100") int critere,
			@RequestParam(name="numPagedefEleves", defaultValue="0") int numPagedefEleves,
			  Model model, HttpServletRequest request){
		
		this.constructModelListeDefEleves(model,	request,  idClasseSelect,  critere,	numPagedefEleves);
		
		return "users/listeDefEleves";
	}
	
	
	@GetMapping(path="/exportlistdefEleves")
	public ModelAndView exportlistdefEleves(Model model, HttpServletRequest request,
			@RequestParam(name="idClasseSelect", defaultValue="0") long idClasseSelect,
			@RequestParam(name="critere", defaultValue="100") int critere){
		
		//System.out.println("----------------------- IMPRESSION DES LISTES DEFINITIVES -------------------");
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Classes classeSelect = usersService.findClasses(idClasseSelect);
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		if(etablissementConcerne == null || classeSelect == null || anneeScolaire == null) {
			//System.out.println("la classe selectionner est null dans le code donc mauvaise recuperation");
			
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeSelect == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
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
		
		double coefMontant = critere * 0.01;
		double montantMin = classeSelect.getMontantScolarite() * coefMontant;
		
		//List<Eleves> listdefofEleves = usersService.findListElevesDefClasse(idClasseSelect, montantMin);
		
		Collection<EleveBean> collectionofElevedefClasse = 
				usersService.generateCollectionofElevedefClasse(classeSelect.getIdClasses(), montantMin);
		
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
		String nomClasse= classeSelect.getCodeClasses()+classeSelect.getSpecialite().getCodeSpecialite()+
				classeSelect.getNumeroClasses();
		String titre = "Liste des eleves ayant paye au moins "+montantMin+" en classe de "+nomClasse;
		if(montantMin == classeSelect.getMontantScolarite()) 
			titre = "Liste definitive des eleves de la classe "+nomClasse;
		
		parameters.put("titre_liste", titre);
		parameters.put("ville", etablissementConcerne.getVilleEtab());
		
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/ListeEleveParClasse.jasper");
		view.setApplicationContext(applicationContext);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		
		params.put("parameters", parameters); 
		params.put("datasource", collectionofElevedefClasse);
		
		parameters.put("datasource", collectionofElevedefClasse);
		
		return new ModelAndView(view, parameters);
		
	}
	
	
	public void constructModelgetdonneesSaisieNotes(Model model,	HttpServletRequest request,  
			Long idClassesConcerne,  Long idAnneeActive, int numPageTrimAn, int taillePage, 
			int numPageCoursClasse, int taillePageCoursClasse){
		
		String profConnect = request.getUserPrincipal().getName();
		
		Utilisateurs usersProf = usersService.findByUsername(profConnect);
		
		model.addAttribute("profConnecte", usersProf);
		
		List<Niveaux> listofNiveauxEns = usersService.findAllNiveauxEns(usersProf.getIdUsers());
		//List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveauxEns);
		
		
		/*
		 * On doit placer l'année active dans le model
		 */
		Annee anneeActive = usersService.findAnneeActive();
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
			/*
			 * On garde du meme coup dans la modele l'id de  la classe concerne
			 */
			model.addAttribute("idClassesConcerne", idClassesConcerne);
			/*
			 * Il faut maintenant la liste des trimestres de cette année active
			 */
			Page<Trimestres> pageofTrimestresAnnee = 
					usersService.findAllTrimestresActiveAnnee(numPageTrimAn, taillePage, true,
							anneeActive.getIdPeriodes());
			if(pageofTrimestresAnnee != null){
				if(pageofTrimestresAnnee.getContent().size()!=0){
					model.addAttribute("listofTrimestresAnnee", pageofTrimestresAnnee.getContent());
					int[] listofPagesTrimestresAnnee=new int[pageofTrimestresAnnee.getTotalPages()];
					
					model.addAttribute("listofPagesTrimestresAnnee", listofPagesTrimestresAnnee);
					
					model.addAttribute("pageCouranteTrimestresAnnee", numPageTrimAn);
					//System.out.println("numPageTrimAn  "+numPageTrimAn);
				}
			}
			/*
			 * On n'a maintenant la liste des trimestres de l'année dans le model
			 * il faut la liste des cours d'une classe qui sera affichée pour chaque séquence
			 */
			if(idClassesConcerne.longValue()>0){
				/*
				 * Ceci signifie qu'on a déjà fait le choix d'une classe et
				 * en plus d'apres le if englobant on a déjà l'année concerné
				 * On va donc chercher la liste des cours  enseigné par le prof connecté dans cette classe d'une classe page par page
				 */
				
				Classes classeConcerne = usersService.findClasses(idClassesConcerne);
				model.addAttribute("classeConcerne", classeConcerne);
				
				//Long idProf = request.getUserPrincipal().getName();
				
				//System.out.println("request.getUserPrincipal().getName() "+request.getUserPrincipal().getName());
				
				String usernameConnect = request.getUserPrincipal().getName();
				
				Utilisateurs usersConnect = usersService.findByUsername(usernameConnect);
				
				Page<Cours> pageofCoursClasse = usersService.findAllCoursProfClasse(numPageCoursClasse, taillePageCoursClasse, 
						idClassesConcerne, usersConnect.getIdUsers());
				
				if(pageofCoursClasse != null){
					if(pageofCoursClasse.getContent().size()!=0){
						model.addAttribute("listofCoursClasse", pageofCoursClasse.getContent());
						int[] listofPagesCoursClasse=new int[pageofCoursClasse.getTotalPages()];
						
						model.addAttribute("listofPagesCoursClasse", listofPagesCoursClasse);
						
						model.addAttribute("pageCouranteCoursClasse", numPageCoursClasse);
						//System.out.println("numPageCoursClasse  "+numPageCoursClasse);
					}
				}
			}
			
		}
		
		
	}
	
	@GetMapping(path="/getdonneesSaisieNotes")
	public String getdonneesSaisieNotes(Model model, HttpServletRequest request,
			@RequestParam(name="idClassesConcerne", defaultValue="-1") Long idClassesConcerne,
			@RequestParam(name="idAnneeActive", defaultValue="0") Long idAnneeActive,
			@RequestParam(name="numPageTrimAn", defaultValue="0") int numPageTrimAn, 
			@RequestParam(name="numPageCoursClasse", defaultValue="0") int numPageCoursClasse,
			@RequestParam(name="taillePageCoursClasse", defaultValue="5") int taillePageCoursClasse,
			@RequestParam(name="taillePage", defaultValue="1") int taillePage){
		
		this.constructModelgetdonneesSaisieNotes(model,	request,  idClassesConcerne,  idAnneeActive,
				numPageTrimAn, taillePage, numPageCoursClasse, taillePageCoursClasse);
		
		return "users/donneesSaisieNotes";
	}
	
	public void constructModelgetdonneesSaisieNotesV1(Model model,	HttpServletRequest request,  
			Long idClassesConcerne,  Long idAnneeActive, int numPageTrimAn, int taillePage, 
			int numPageCoursClasse, int taillePageCoursClasse){
		
		/*
		 * On a le idClasseConcerne mais on met plutôt la liste des niveaux dans le model pour l'amélioration du rendu
		 * dans la page html
		 */
		String profConnect = request.getUserPrincipal().getName();
		
		Utilisateurs usersProf = usersService.findByUsername(profConnect);
		
		model.addAttribute("profConnecte", usersProf);
		
		List<Niveaux> listofNiveauxEns = usersService.findAllNiveauxEns(usersProf.getIdUsers());
		//List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		model.addAttribute("listofNiveaux", listofNiveauxEns);
		
		
		/*
		 * On doit placer l'année active dans le model
		 */
		Annee anneeActive = usersService.findAnneeActive();
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
			/*
			 * On garde du meme coup dans la modele l'id de  la classe concerne
			 */
			model.addAttribute("idClassesConcerne", idClassesConcerne);
			/*
			 * Il faut maintenant la liste des trimestres de cette année active
			 */
			Page<Trimestres> pageofTrimestresAnnee = usersService.findAllTrimestresActiveAnnee(
					numPageTrimAn, taillePage,	true, anneeActive.getIdPeriodes());
			if(pageofTrimestresAnnee != null){
				if(pageofTrimestresAnnee.getContent().size()!=0){
					model.addAttribute("listofTrimestresAnnee", pageofTrimestresAnnee.getContent());
					int[] listofPagesTrimestresAnnee=new int[pageofTrimestresAnnee.getTotalPages()];
					
					model.addAttribute("listofPagesTrimestresAnnee", listofPagesTrimestresAnnee);
					
					model.addAttribute("pageCouranteTrimestresAnnee", numPageTrimAn);
					//System.out.println("numPageTrimAn  "+numPageTrimAn);
				}
			}
			
			/*
			 * On n'a maintenant la liste des trimestres de l'année dans le model
			 * il faut la liste des cours d'une classe qui sera affichée pour chaque séquence
			 */
			if(idClassesConcerne.longValue()>0){
				/*
				 * Ceci signifie qu'on a déjà fait le choix d'une classe et
				 * en plus d'apres le if englobant on a déjà l'année concerné
				 * On va donc chercher la liste des cours  enseigné par le prof connecté dans cette classe d'une classe page par page
				 */
				
				Classes classeConcerne = usersService.findClasses(idClassesConcerne);
				model.addAttribute("classeConcerne", classeConcerne);
				
				//Long idProf = request.getUserPrincipal().getName();
				
				//System.out.println("request.getUserPrincipal().getName() "+request.getUserPrincipal().getName());
				
				String usernameConnect = request.getUserPrincipal().getName();
				
				Utilisateurs usersConnect = usersService.findByUsername(usernameConnect);
				
				Page<Cours> pageofCoursClasse = usersService.findAllCoursProfClasse(numPageCoursClasse, taillePageCoursClasse, 
						idClassesConcerne, usersConnect.getIdUsers());
				
				if(pageofCoursClasse != null){
					if(pageofCoursClasse.getContent().size()!=0){
						model.addAttribute("listofCoursClasse", pageofCoursClasse.getContent());
						int[] listofPagesCoursClasse=new int[pageofCoursClasse.getTotalPages()];
						
						model.addAttribute("listofPagesCoursClasse", listofPagesCoursClasse);
						
						model.addAttribute("pageCouranteCoursClasse", numPageCoursClasse);
						//System.out.println("numPageCoursClasse  "+numPageCoursClasse);
					}
				}
				model.addAttribute("affichechoixmatiereetseq", "oui");
			}
			else{
				model.addAttribute("affichechoixmatiereetseq", "non");
			}
			
		}
		
		
	}
	
	@GetMapping(path="/getdonneesSaisieNotesV1")
	public String getdonneesSaisieNotesV1(Model model, HttpServletRequest request,
			@RequestParam(name="idClassesConcerne", defaultValue="-1") Long idClassesConcerne,
			@RequestParam(name="idAnneeActive", defaultValue="0") Long idAnneeActive,
			@RequestParam(name="numPageTrimAn", defaultValue="0") int numPageTrimAn, 
			@RequestParam(name="numPageCoursClasse", defaultValue="0") int numPageCoursClasse,
			@RequestParam(name="taillePageCoursClasse", defaultValue="5") int taillePageCoursClasse,
			@RequestParam(name="taillePage", defaultValue="1") int taillePage){
		
		//System.err.println("les parametres sont classe: "+idClassesConcerne+"  annee: "+idAnneeActive);
		
		this.constructModelgetdonneesSaisieNotesV1(model,	request,  idClassesConcerne,  idAnneeActive,
				numPageTrimAn, taillePage, numPageCoursClasse, taillePageCoursClasse);
		
		return "users/donneesSaisieNotesV1";
		//return "users/donneesSaisieNotesV2";
	}
	
	
	
	public void constructModelgetformSaisieNotesV1(Model model,	HttpServletRequest request, Long idSequenceConcerne,  
			Long idCoursConcerne, String typeEval, Long idClassesConcerne){
		
		//System.err.println("depart de la fonction 1 ");
		/*
		 * Il faut donc la liste de tous les élèves de la classe dans le modele dans le même ordre qu'ils vont apparaitre dans les pages
		 */
		List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClassesConcerne);
		
		/*for(Eleves e : listofAllEleve){
			//System.out.println("AFFx  Noms "+e.getNomsEleves()+" ID: "+e.getIdEleves().longValue());
		}*/
		//System.err.println("depart de la fonction 2");
		if(listofAllEleve != null){
			model.addAttribute("effectifTotal", listofAllEleve.size());
			//System.err.println("depart de la fonction 3 la taille "+listofAllEleve.size());
			if((listofAllEleve.size() > 0)){
				model.addAttribute("listofAllEleve", listofAllEleve);
				//System.err.println("depart de la fonction 4");
				Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
				Cours coursConcerne = usersService.findCours(idCoursConcerne);
				//System.err.println("chargement du modele initie "+sequenceConcerne.getNumeroSeq());
				model.addAttribute("sequenceConcerne", sequenceConcerne);
				model.addAttribute("coursConcerne", coursConcerne);
				model.addAttribute("typeEval", typeEval);
				model.addAttribute("effectifclasse",listofAllEleve.size());
				//System.err.println("chargement du modele terminee");
			}
			
		}
		/*
		 * Il faut placer dans le modele l'évaluation pour laquelle on veut enregistrer les notes
		 */
		//System.err.println("depart de la fonction 5");
		Evaluations eval = usersService.findEvaluations(idCoursConcerne, idSequenceConcerne, typeEval);
		model.addAttribute("evaluationConcerne", eval);
		//System.err.println("depart de la fonction 6");
	}
	
	
	
	@GetMapping(path="/getformSaisieNotesV1")
	public String getformSaisieNotesV1(
			Model model, HttpServletRequest request,
			@RequestParam(name="idSequenceConcerne", defaultValue="-1") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne,
			@RequestParam(name="idCoursConcerne", defaultValue="0") Long idCoursConcerne,
			@RequestParam(name="typeEval", defaultValue="DS") String typeEval){
		
		/*
		 * Il faut enregistrer l'évaluation s'il n'existe pas encore
		 * pour cela il faut idCours et idSequence et les paramètres de eval
		 */
		Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
		Cours coursConcerne = usersService.findCours(idCoursConcerne);
		
		/*Evaluations eval = new Evaluations();
		eval.setContenuEval("");
		eval.setCours(coursConcerne);
		eval.setDateenregEval(new Date());
		eval.setSequence(sequenceConcerne);
		eval.setTypeEval(typeEval);*/
		
		String contenuEval="";
		String typeEvalAssocie="";
		int proportionEval = 0;
		if(typeEval.equals("DS")) {
			proportionEval = 80;
			typeEvalAssocie = "CC";
		}
		if(typeEval.equals("CC")) {
			proportionEval = 20;
			typeEvalAssocie = "DS";
		}
		
		//System.err.println("la proportion eval est d'abord "+proportionEval);
		Evaluations evalDeTypeExist = usersService.findEvaluations(coursConcerne.getIdCours(), 
				sequenceConcerne.getIdPeriodes(), typeEval);
		
		Evaluations evalDeTypeExistAssocie = usersService.findEvaluations(coursConcerne.getIdCours(), 
				sequenceConcerne.getIdPeriodes(), typeEvalAssocie);
		
		if(evalDeTypeExist == null) {
			if(evalDeTypeExistAssocie !=null){
				proportionEval = 100 - evalDeTypeExistAssocie.getProportionEval();
			}
			int repServeur = usersService.saveEvaluation(contenuEval, coursConcerne, new Date(), 
					proportionEval, sequenceConcerne, typeEval);
			/*
			 * L'evaluation n'existait pas. On l'enregistre et la recupere car elle va aider par la suite
			 */
			if(repServeur != 0){
				evalDeTypeExist = usersService.findEvaluations(coursConcerne.getIdCours(), sequenceConcerne.getIdPeriodes(), typeEval);
				//System.err.println("proportion evalDeTypeExist "+evalDeTypeExist.getProportionEval());
			}
			
			if(repServeur == 0) return "redirect:/logesco/users/500?enregEvaluationerror";
		}
		
		/*
		 * Ici on est sur que l'evaluation concerne elle même existe deja en BD donc peut l'enseignant
		 * veut juste modifier les notes. En fait même lorsqu'il n'existait pas (le if qui precede) on 
		 * l'enregistre puis on le récupère
		 */
		proportionEval = evalDeTypeExist.getProportionEval();
		//System.err.println("la proportion devient de evalDeTypeExist"+proportionEval);
		
		/*
		 * si L'évaluation associe existe on la charge dans la session sinon on affichera un pourcentage null.
		 */
		if(evalDeTypeExistAssocie != null) {
			model.addAttribute("evaluationAssocie", evalDeTypeExistAssocie);
		}
		else{
			//System.err.println("eval associe n'existe pas "+proportionEval);
			int p = 100 - proportionEval;
			model.addAttribute("default_proportion", p);
		}
		
		/*
		 * Ensuite il faut charger la liste des élèves dans le model avec pour chacun le moyen d'avoir 
		 * sa note pour un type d'évaluation précis, dans un cours et une séquence donnée
		 */
		//System.err.println("deja avant la contruction du modele");
		this.constructModelgetformSaisieNotesV1(model,	request, idSequenceConcerne,  idCoursConcerne,  typeEval,
				idClassesConcerne);
		
		return "users/formSaisieNotesV1";
	}
	
	
	
	@GetMapping(path="/getUpdateNoteSaisie")
	public String getUpdateNoteSaisie(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEleves", defaultValue="0") Long idEleves,
			@RequestParam(name="idEval", defaultValue="0") Long idEval,
			@RequestParam(name="proportionEval", defaultValue="0") String proportionEval,
			@RequestParam(name="noteSaisi", defaultValue="0") String noteSaisi,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves){
		
		//System.out.println("proportionEvalproportionEvalproportionEval == "+proportionEval);
		
		Eleves elv = usersService.findEleves(idEleves);
		
		/*
		 * Recuperer l'évaluation
		 */
		Evaluations evalConcerne = usersService.findEvaluations(idEval);
		/*if(evalConcerne == null) //System.out.println("yyyyyyyyyyyyyyyyy evalConcerne non trouve");*/
		
		/*
		 * On doit calculer le numero de la page à partir de l'id de l'élève sachant que 
		 * les page d'élève sont de taille 5.
		 */
		
		
		/*
		 * Il faut enregistrer la note de l'élève mais avant il faut convertir la note en double
		 * et retourner une erreur au cas ou ce n'est pas possible
		 */
		try{
			int newproportionEval = Integer.parseInt(proportionEval);
			double valNoteSaisi = Double.parseDouble(noteSaisi);
			
			if((newproportionEval>100) || (valNoteSaisi>20)){
				//on rejete car il y a une erreur inaceptable
				
				return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiError"
				+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
				+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
				+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
				+ "&&typeEval="+evalConcerne.getTypeEval()
				+ "&&idEleves="+elv.getIdEleves()
				+ "&&numPageEleves="+numPageEleves;
				
			}
			
			/*
			 * On met à jour la nouvelle proportion de l'évaluation
			 */
			evalConcerne.setProportionEval(newproportionEval);
			
			int r = usersService.saveEvaluation(evalConcerne.getContenuEval(), evalConcerne.getCours(), evalConcerne.getDateenregEval(), 
					evalConcerne.getProportionEval(), evalConcerne.getSequence(), evalConcerne.getTypeEval());
			
			//System.out.println("le r de saveEval == "+r);
			
			if(r == 0) return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiErrorsaveEval"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval()
					+ "&&idEleves="+elv.getIdEleves()
					+ "&&numPageEleves="+numPageEleves;
			
			/*
			 * On peut donc enregistrer la note de l'élève
			 */
			int ret = usersService.saveNoteEvalEleve(idEval, idEleves, valNoteSaisi);
			
			if(ret == 0) return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisierrorNote"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval()
					+ "&&idEleves="+elv.getIdEleves()
					+ "&&numPageEleves="+numPageEleves;
			
			/*
			 * On regarde si on n'a pas atteint la fin de la liste des élèves
			 */
			
			int numEleve = usersService.getNumeroEleve(idEleves);
			
			//System.out.println("usersService.getNumeroEleve(idEleves) "+numEleve+"  "+usersService.getEffectifClasse(idEleves));
			
			if(numEleve == usersService.getEffectifClasse(idEleves)){
				/*
				 * Alors on doit chargé le même élève et précisé que la liste est terminé
				 */
				return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiwarningElv"
						+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
						+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
						+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
						+ "&&typeEval="+evalConcerne.getTypeEval()
						+ "&&idEleves="+elv.getIdEleves()
						+ "&&numPageEleves="+numPageEleves;
				
			}
			else if(numEleve < usersService.getEffectifClasse(idEleves)){
				/*
				 * Alors on doit chargé l'élève qui suit. 
				 * Il faut donc la liste trié pour retirer l'élève qui suit celui qui est entreint d'etre traité reférencé par idEleves
				 */
				//System.out.println("Cherchons l'élève suivant ");
				int mode = 1;
				Eleves elvSvt = usersService.getElevesATraiter(idEleves, mode);
				
				/*if(elvSvt == null) //System.out.println("l'élève suivant n'est pas trouvable donc problème dans le code ou la requete");*/
				
				//System.out.println("Cherchons l'élève suivant  de "+elv.getNomsEleves()+" id == "+elv.getIdEleves().longValue());
				//System.out.println("et c'est l'élève suivant  "+elvSvt.getNomsEleves()+" id == "+elvSvt.getIdEleves().longValue());
				
			
				
				/*
				 * On doit recalculer le numero de la page
				 */
				int taillePage = 5;
				int newnumPageEleves = usersService.getNumeroPageEleve(elvSvt.getIdEleves(),taillePage);
				
				//System.out.println("le numero de page du nouvel eleve est "+newnumPageEleves);
				
				return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiSucces"
						+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
						+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
						+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
						+ "&&typeEval="+evalConcerne.getTypeEval()
						+ "&&idEleves="+elvSvt.getIdEleves()
						+ "&&numPageEleves="+newnumPageEleves;
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiError"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval()
					+ "&&idEleves="+elv.getIdEleves()
					+ "&&numPageEleves="+numPageEleves;
		}
		
		return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiError"
		+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
		+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
		+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
		+ "&&typeEval="+evalConcerne.getTypeEval()
		+ "&&idEleves="+elv.getIdEleves()
		+ "&&numPageEleves="+numPageEleves;
		
		
	}
	
	
	@GetMapping(path="/getUpdateNoteSaisieV1")
	public String getUpdateNoteSaisieV1(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEval", defaultValue="0") Long idEval,
			@RequestParam(name="proportionEval", defaultValue="0") String proportionEval,
			@RequestParam(name="tabnotesaisi[]") String tabnotesaisi[],
			@RequestParam(name="tabideleve[]") String tabideleve[]){
		
		//System.out.println("proportionEvalproportionEvalproportionEval == "+tabnotesaisi.length);
		
		
		Evaluations evalConcerne = usersService.findEvaluations(idEval);
		/*if(evalConcerne == null) //System.out.println("yyyyyyyyyyyyyyyyy evalConcerne non trouve");*/
		
	
		int numero = 1;
		try{
			//System.err.println("proportion "+proportionEval);
			int newproportionEval = Integer.parseInt(proportionEval);
			//System.err.println("proportion converti "+newproportionEval);
			evalConcerne.setProportionEval(newproportionEval);
			int r = usersService.saveEvaluation(evalConcerne.getContenuEval(), evalConcerne.getCours(), evalConcerne.getDateenregEval(), 
					evalConcerne.getProportionEval(), evalConcerne.getSequence(), evalConcerne.getTypeEval());
			int reponse=1;
			//System.err.println("evaluation enregistree "+r);
			if(r>0){
				for(String noteS : tabnotesaisi){
					//System.err.println("note en string "+noteS);
					double valNoteSaisi = Double.parseDouble(noteS);
					//System.err.println("valNoteSaisi en int "+valNoteSaisi);
					int i=numero-1;
					String idEleveString = tabideleve[i];
					long idEleve = Long.parseLong(idEleveString);
					int ret = usersService.saveNoteEvalEleve(idEval, idEleve, valNoteSaisi);
					//System.err.println("enreg note eval "+valNoteSaisi+" pour eleve "+numero);
					if(ret == 0) reponse = 0;
					numero+=1;
				}
				//System.err.println("Nous voici dans la suite ");
				if(reponse == 0){
					model.addAttribute("numero", numero);
					return "redirect:/logesco/users/getformSaisieNotesV1?updatenotesaisiError1"
							+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
							+ "&&idClassesConcerne="+evalConcerne.getCours().getClasse().getIdClasses()
							+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
							+ "&&typeEval="+evalConcerne.getTypeEval();
				}
		return "redirect:/logesco/users/getformSaisieNotesV1?updatenotesaisiSucces"
				+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
				+ "&&idClassesConcerne="+evalConcerne.getCours().getClasse().getIdClasses()
				+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
				+ "&&typeEval="+evalConcerne.getTypeEval();
			}
		}
		catch(Exception e){
			//System.err.println("exception gggg "+e.getMessage());
			model.addAttribute("numero", numero);
			return "redirect:/logesco/users/getformSaisieNotesV1?updatenotesaisiError1"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+evalConcerne.getCours().getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval();
		}
		
		return "redirect:/logesco/users/getformSaisieNotesV1?updatenotesaisiError"
				+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
				+ "&&idClassesConcerne="+evalConcerne.getCours().getClasse().getIdClasses()
				+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
				+ "&&typeEval="+evalConcerne.getTypeEval();
		
	}
	
	
	@GetMapping(path="/getPrecedenteNoteSaisie")
	public String getPrecedenteNoteSaisie(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEleves", defaultValue="0") Long idEleves,
			@RequestParam(name="idEval", defaultValue="0") Long idEval,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves){
		
		Eleves elv = usersService.findEleves(idEleves);
		Evaluations evalConcerne = usersService.findEvaluations(idEval);
		
	/*	if((elv == null) || (evalConcerne == null)) //System.out.println("Eleve ou evaluation non trouve donc problème de passage de paramètre "
				+ "dans la requete initié par le clic sur le lien");*/
		
		int numEleve = usersService.getNumeroEleve(idEleves);
		
		/*if(numEleve == 0) //System.out.println("L'élève n'existe pas dans la liste des élève de la classe donc il y a encore erreur lors du passage des "
				+ " paramètres dans la requete initié par le clic sur le lien");*/
		
		if(numEleve == 1){
			/*
			 * On a atteint le premier élève de la liste donc on ne peut pas aller plus bas
			 */
			return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiwarningElvPre"
				+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
				+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
				+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
				+ "&&typeEval="+evalConcerne.getTypeEval()
				+ "&&idEleves="+elv.getIdEleves()
				+ "&&numPageEleves="+numPageEleves;
			
		}
		else if((numEleve > 1) && (numEleve <= usersService.getEffectifClasse(idEleves))){
			
			//System.out.println("Cherchons l'élève précédent ");
			int mode = 0;
			Eleves elvSvt = usersService.getElevesATraiter(idEleves, mode);
			
			/*if(elvSvt == null) //System.out.println("l'élève suivant n'est pas trouvable donc problème dans le code ou la requete");*/
			
			/*//System.out.println("Cherchons l'élève suivant  de "+elv.getNomsEleves()+" id == "+elv.getIdEleves().longValue());
			//System.out.println("et c'est l'élève suivant  "+elvSvt.getNomsEleves()+" id == "+elvSvt.getIdEleves().longValue());
			*/
			
			/*
			 * On doit recalculer le numero de la page
			 */
			int taillePage = 5;
			int newnumPageEleves = usersService.getNumeroPageEleve(elvSvt.getIdEleves(),taillePage);
			
			////System.out.println("le numero de page du nouvel eleve est "+newnumPageEleves);
			
			return "redirect:/logesco/users/getformSaisieNotes?"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval()
					+ "&&idEleves="+elvSvt.getIdEleves()
					+ "&&numPageEleves="+newnumPageEleves;
			
		}
		
		
		return "redirect:/logesco/users/getformSaisieNotes?updatenotesaisiRien"
				+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
				+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
				+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
				+ "&&typeEval="+evalConcerne.getTypeEval()
				+ "&&idEleves="+elv.getIdEleves()
				+ "&&numPageEleves="+numPageEleves;
	}
	
	
	@GetMapping(path="/getRechercheEleveParNumero")
	public String getRechercheEleveParNumero(
			Model model, HttpServletRequest request,
			@RequestParam(name="numeroElv", defaultValue="0") String numeroElv,
			@RequestParam(name="idEleves", defaultValue="0") Long idEleves,
			@RequestParam(name="idEvalRech", defaultValue="0") Long idEvalRech,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves){
		
		Eleves elv = usersService.findEleves(idEleves);
		Evaluations evalConcerne = usersService.findEvaluations(idEvalRech);
		
		try{
			int numeroEleve = Integer.parseInt(numeroElv);
			int effectifClasse = usersService.getEffectifClasse(idEleves);
			if((numeroEleve > 0) && (numeroEleve <= effectifClasse)){
				List<Eleves> listofAllEleve = usersService.findListElevesClasse(elv.getClasse().getIdClasses());
				Eleves eleveChercher = usersService.findEleveDansListe(listofAllEleve, numeroEleve);
				
				/*
				 * Il faut donc charger l'élève retrouver
				 */
				if(eleveChercher == null) return "redirect:/logesco/users/getformSaisieNotes?rechEleveNull"
						+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
						+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
						+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
						+ "&&typeEval="+evalConcerne.getTypeEval()
						+ "&&idEleves="+elv.getIdEleves()
						+ "&&numPageEleves="+numPageEleves;
				
				/*
				 * Ici on est sur l'élève chercher n'est pas null
				 * on doit donc rechercher son numpage
				 */
				int taillePage = 5;
				int newnumPageEleves = usersService.getNumeroPageEleve(eleveChercher.getIdEleves(),taillePage);
				
				return "redirect:/logesco/users/getformSaisieNotes?"
						+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
						+ "&&idClassesConcerne="+eleveChercher.getClasse().getIdClasses()
						+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
						+ "&&typeEval="+evalConcerne.getTypeEval()
						+ "&&idEleves="+eleveChercher.getIdEleves()
						+ "&&numPageEleves="+newnumPageEleves;
				
			}
		}
		catch(Exception e){
			return "redirect:/logesco/users/getformSaisieNotes?numeroElvErrone"
					+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
					+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
					+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
					+ "&&typeEval="+evalConcerne.getTypeEval()
					+ "&&idEleves="+elv.getIdEleves()
					+ "&&numPageEleves="+numPageEleves;
		}
		
		
		return "redirect:/logesco/users/getformSaisieNotes?"
			+ "&&idSequenceConcerne="+evalConcerne.getSequence().getIdPeriodes()
			+ "&&idClassesConcerne="+elv.getClasse().getIdClasses()
			+ "&&idCoursConcerne="+evalConcerne.getCours().getIdCours()
			+ "&&typeEval="+evalConcerne.getTypeEval()
			+ "&&idEleves="+elv.getIdEleves()
			+ "&&numPageEleves="+numPageEleves;
		
	}
	
	
	public void constructModelListprocesverbalEvalSeq(Model model,	HttpServletRequest request,	
			GetrapportEvalSeqForm getrapportEvalSeqForm){
		
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		
		////System.out.println("l'user connecte est  "+username +" mais quel fonction occupe t'il dans le système?");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
		model.addAttribute("profConnecte", profConnecte);
		
		if(userconnecte != null){
			//int roleUser = usersService.getcodeUsersRole(userconnecte);
			//System.out.println("le role joue vis a vis du système a pour code "+roleUser);

			/*
			 * Il faut la liste des séquences de l'année en cours
			 */
			Annee anneeActive = usersService.findAnneeActive();
			
			String roleCen = "CENSEUR";
			List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
			if(usersService.hasRole(userconnecte, roleCen)==true){
				/*
				 *Si il a le role censeur alors il faut chargé la liste de toutes les classes puisque lui il peut voir 
				 *tous les proces verbaux recapitulatifs
				 */
				//session.setAttribute("listofNiveauxForPV", listofNiveaux);
				model.addAttribute("listofNiveauxForPV", listofNiveaux);
				
				List<Sequences> listofSequence = usersService.findAllSequence(anneeActive.getIdPeriodes());
				model.addAttribute("listofSequence", listofSequence);
				
			}
			else {
				List<Sequences> listofSequence = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
				
				model.addAttribute("listofSequence", listofSequence);
				/*
				 * ici il s'agit de quelqu'un qui peut avoir tout autre role que le role Censeur et dans ce cas on ne doit chargé que les
				 * classes qu'il dirige et par conséquent uniquement les niveaux dans lesquelle se trouve ces classes
				 */
				//List<Classes> listofClasseDirige = usersService.getListClassesDirige(userconnecte.getIdUsers());
				List<Niveaux> listofNiveauxDiriges = usersService.findAllNiveauxDirigesEns(userconnecte.getIdUsers());
				if(listofNiveauxDiriges != null){
					/*
					 *On va donc plutot placer dans la session la liste des classes dirigés
					 */
					//session.setAttribute("listofClassesForPV", listofClasseDirige);
					model.addAttribute("listofNiveauxDirigesForPV", listofNiveauxDiriges);
				}
			}
		}
		
	}
	
	@GetMapping(path="/getlistprocesverbalEvalSeq")
	public String getlistprocesverbalEvalSeq(
			@ModelAttribute("getrapportEvalSeqForm") 
			GetrapportEvalSeqForm getrapportEvalSeqForm,
			Model model, HttpServletRequest request){
		
		this.constructModelListprocesverbalEvalSeq(model,	request,	getrapportEvalSeqForm);
		
		return "users/listprocesverbalEvalSeq";
	}
	
	
	@GetMapping(path="/getdonneesReleveNotes")
	public ModelAndView getdonneesReleveNotes(
			@RequestParam(name="idClasseSelect", defaultValue="0") long idClasseSelect,
			Model model, HttpServletRequest request){
		
		
		HttpSession session=request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Classes classeConcerne = usersService.findClasses(idClasseSelect);
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		String username=(String)session.getAttribute("username");
		
		////System.out.println("l'user connecte est  "+username +" mais quel fonction occupe t'il dans le système?");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		//Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());

		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				 userconnecte==null) {
			//System.out.println("un de ces truc est null vraiment");
			
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
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
		
		String titre_liste = "RELEVE DE NOTE A REMPLIR ET A DEPOSER AU PRES DE L'ADMINISTRATION";
		parameters.put("titre_liste", titre_liste);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);

		List<EleveBean2> listofEleve = (List<EleveBean2>) usersService.generateReleveNote(idClasseSelect);
		parameters.put("SUBREPORT_DIR", "classpath:/reports/compiled/fiches/");
		
		String titulaire="";
		if(classeConcerne.getProffesseur() != null){
			titulaire = classeConcerne.getProffesseur().getNomsPers()+" "+
					classeConcerne.getProffesseur().getPrenomsPers();
		}
		
		parameters.put("titulaire", titulaire.toUpperCase());
		
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		parameters.put("datasource", listofEleve);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofEleve.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/relevedenoteVideParClasse.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
		
	}
	
	
	@GetMapping(path="/getprocesverbalfinalNotesSeq")
	public ModelAndView getprocesverbalfinalNotesSeq(
			@RequestParam(name="idClasse", defaultValue="0") long idClasseConcerne,
			@RequestParam(name="idCours", defaultValue="0") long idCoursConcerne,
			@RequestParam(name="idSequence", defaultValue="0") long idSequenceConcerne,
			Model model, HttpServletRequest request){
		
		HttpSession session=request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
		
		Cours cours = usersService.findCours(idCoursConcerne);
		
		String username=(String)session.getAttribute("username");
		
		////System.out.println("l'user connecte est  "+username +" mais quel fonction occupe t'il dans le système?");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());

		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				sequenceConcerne==null || cours==null || userconnecte==null) {
			//System.out.println("un de ces truc est null vraiment pour l'impression des pv de sequence");

			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
			}
			if(sequenceConcerne == null){
				error+="\n LA SEQUENCE INDIQUEE N'A PAS ETE RETROUVE";
			}
			if(cours == null){
				error+="\n LE COURS CONCERNE N'EST PAS RETROUVE";
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
		
		parameters.put("deleguation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("deleguation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
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
		
		String titre_pv = "PROCES VERBAL DES NOTES : Séquence "+sequenceConcerne.getNumeroSeq();
		parameters.put("titre_pv", titre_pv);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);
		
		String matiere = cours.getIntituleCours()+" ("+cours.getMatiere().getIntituleMatiere()+")";
		parameters.put("matiere", matiere);
		String enseignant = profConnecte.getNomsPers()+" "+profConnecte.getPrenomsPers();
		parameters.put("enseignant", enseignant);
		int nbre_moyennes = usersService.getNbreNoteDansCourspourSeq(idClasseConcerne, 
				idCoursConcerne, idSequenceConcerne);
		int nbre_sous_moyennes = usersService.getNbreSousNoteDansCourspourSeq(idClasseConcerne, 
				idCoursConcerne, idSequenceConcerne);
		//System.err.println("nbre_moyennes "+nbre_moyennes);
		
		if(nbre_moyennes<0) nbre_moyennes = 0;
		parameters.put("nbre_moyennes", nbre_moyennes);
		
		if(nbre_sous_moyennes<0) nbre_sous_moyennes = 0;
		parameters.put("nbre_sous_moyennes", nbre_sous_moyennes);
		
		double nbM=new Double(nbre_moyennes).doubleValue();
		double nbSM=new Double(nbre_sous_moyennes).doubleValue();
		double pourRr = (nbM/(nbM+nbSM))*100;
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try {
			pourRr =df.parse(df.format(pourRr)).doubleValue();
			////System.out.println("pourcentagessss "+pourcentage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int nb_decimale = 3;
		pourRr = usersService.getUtilitairesBulletins().tronqueDouble(pourRr, nb_decimale);
		String pourR = ""+pourRr+" %";
		String pourcc = "";
		String pourds = "";
		//On cherche le pourcentage du CC et du DS fixé
		
		List<Evaluations> listofEvalDeCoursDansSeq = cours.getListofEvalDeCoursDansSeq(idSequenceConcerne);
		if(listofEvalDeCoursDansSeq != null){
			if(listofEvalDeCoursDansSeq.size()>=1){
				for(Evaluations eval : listofEvalDeCoursDansSeq){
					if(eval.getTypeEval().equalsIgnoreCase("CC")==true){
						pourcc = eval.getProportionEval()+" %";
					}
					if(eval.getTypeEval().equalsIgnoreCase("DS")==true){
						pourds = eval.getProportionEval()+" %";
					}
				}
			}
		}
		
		/*
		 * Liste des élèves avec pour chacun sa note finale pour le cours dans la sequence
		 */
		List<PV_SequenceBean> listofPV = (List<PV_SequenceBean>) usersService.generatePVSequence(idClasseConcerne, 
				idCoursConcerne, idSequenceConcerne);
		
		parameters.put("pourcc", pourcc);
		parameters.put("pourds", pourds);
		parameters.put("pourR", pourR);
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		//parameters.put("IMAGE_FOND", "src/main/resources/static/images/logobekoko.png");
		parameters.put("datasource", listofPV);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofPV.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/PVSequence.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
		
	}
	
	@GetMapping(path="/getprocesverbalresumeNotesTrim")
	public ModelAndView getprocesverbalresumeNotesTrim(
			@RequestParam(name="idCours", defaultValue="0") long idCoursConcerne,
			@RequestParam(name="idTrimestre", defaultValue="0") long idTrimestreConcerne,
			Model model, HttpServletRequest request){
		
		HttpSession session=request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestreConcerne);
		
		Cours cours = usersService.findCours(idCoursConcerne);
		
		Classes classeConcerne = cours.getClasse();
		Long idClasseConcerne = classeConcerne.getIdClasses();
		
		String username=(String)session.getAttribute("username");
		
		////System.out.println("l'user connecte est  "+username +" mais quel fonction occupe t'il dans le système?");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());

		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				trimestreConcerne==null || cours==null || userconnecte==null) {
			/*//System.out.println("un de ces truc est null vraiment dans l'impression du pv du trimestre "
					+ "etablissementConcerne "+etablissementConcerne+
					"classeConcerne "+classeConcerne+" anneeScolaire "+anneeScolaire+
					"trimestreConcerne"+trimestreConcerne+" cours"+cours+" userconnecte"+userconnecte);*/
			
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
			}
			if(trimestreConcerne == null){
				error+="\n LA SEQUENCE INDIQUEE N'A PAS ETE RETROUVE";
			}
			if(cours == null){
				error+="\n LE COURS CONCERNE N'EST PAS RETROUVE";
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
		
		parameters.put("deleguation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("deleguation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
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
		
		String titre_pv = "PROCES VERBAL DES NOTES : Trimestre "+trimestreConcerne.getNumeroTrim();
		parameters.put("titre_pv", titre_pv);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);
		
		String matiere = cours.getIntituleCours()+" ("+cours.getMatiere().getIntituleMatiere()+")";
		parameters.put("matiere", matiere);
		String enseignant = profConnecte.getNomsPers()+" "+profConnecte.getPrenomsPers();
		parameters.put("enseignant", enseignant);
		
		String labelSeq1="";
		String labelSeq2="";
		if(trimestreConcerne.getNumeroTrim() == 1){
			labelSeq1 = "NOTE SEQ1";
			labelSeq2 = "NOTE SEQ2";
		}
		else if(trimestreConcerne.getNumeroTrim() == 2){
			labelSeq1 = "NOTE SEQ3";
			labelSeq2 = "NOTE SEQ4";
		}
		else if(trimestreConcerne.getNumeroTrim() == 3){
			labelSeq1 = "NOTE SEQ5";
			labelSeq2 = "NOTE SEQ6";
		}
		
		parameters.put("labelSeq1", labelSeq1);
		parameters.put("labelSeq2", labelSeq2);
		
		int nbre_moyennes = usersService.getNbreNoteDansCourspourTrim(idClasseConcerne, 
				idCoursConcerne, idTrimestreConcerne);
		int nbre_sous_moyennes = usersService.getNbreSousNoteDansCourspourTrim(idClasseConcerne, 
				idCoursConcerne, idTrimestreConcerne);
		//System.err.println("nbre_moyennes trim "+nbre_moyennes);
		
		if(nbre_moyennes<0) nbre_moyennes = 0;
		parameters.put("nbre_moyennes", nbre_moyennes);
		
		if(nbre_sous_moyennes<0) nbre_sous_moyennes = 0;
		parameters.put("nbre_sous_moyennes", nbre_sous_moyennes);
		
		double nbM=new Double(nbre_moyennes).doubleValue();
		double nbSM=new Double(nbre_sous_moyennes).doubleValue();
		double pourRr = (nbM/(nbM+nbSM))*100;
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try {
			pourRr =df.parse(df.format(pourRr)).doubleValue();
			////System.out.println("pourcentagessss "+pourcentage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int nb_decimale = 3;
		pourRr = usersService.getUtilitairesBulletins().tronqueDouble(pourRr, nb_decimale);
		String pourR = ""+pourRr+" %";
		
		/*
		 * Liste des élèves avec pour chacun sa note finale pour le cours dans la sequence
		 */
		List<PV_TrimestreBean> listofPV = (List<PV_TrimestreBean>) usersService.generatePVTrimestre(idClasseConcerne, 
				idCoursConcerne, idTrimestreConcerne);
		
		parameters.put("pourR", pourR);
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		//parameters.put("IMAGE_FOND", "src/main/resources/static/images/logobekoko.png");
		parameters.put("datasource", listofPV);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofPV.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/PVTrimestre.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
		
		
	}
	
	public void constructModelListprocesverbalEvalTrim(Model model,	HttpServletRequest request,	
			GetrapportEvalTrimForm getrapportEvalTrimForm){
		
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		
		//System.out.println("l'user connecte est  "+username +" mais quel fonction occupe t'il dans le système?");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
		model.addAttribute("profConnecte", profConnecte);
		
		if(userconnecte != null){
			//int roleUser = usersService.getcodeUsersRole(userconnecte);
			//System.out.println("le role joue vis a vis du système a pour code "+roleUser);

			/*
			 * Il faut la liste des trimestres de l'année en cours
			 */
			Annee anneeActive = usersService.findAnneeActive();
			List<Trimestres> listofTrimestre = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
			model.addAttribute("listofTrimestre", listofTrimestre);
			
			List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
			String roleCen = "CENSEUR";
			if(usersService.hasRole(userconnecte, roleCen)==true){
				/*
				 *Si il a le role censeur alors il faut chargé la liste de toutes les classes puisque lui il peut voir 
				 *tous les proces verbaux recapitulatifs
				 */
				//session.setAttribute("listofNiveauxForPV", listofNiveaux);
				model.addAttribute("listofNiveauxForPV", listofNiveaux);
			}
			else {
				/*
				 * ici il s'agit de quelqu'un qui peut avoir tout autre role que le role Censeur et dans ce cas on ne doit chargé que les
				 * classes qu'il dirige et par conséquent uniquement les niveaux dans lesquelle se trouve ces classes
				 */
				List<Classes> listofClasseDirige = usersService.getListClassesDirige(userconnecte.getIdUsers());
				if(listofClasseDirige != null){
					/*
					 *On va donc plutot placer dans la session la liste des classes dirigés
					 */
					//session.setAttribute("listofClassesForPV", listofClasseDirige);
					model.addAttribute("listofClassesForPV", listofClasseDirige);
				}
				
				List<Niveaux> listofNiveauxDiriges = usersService.findAllNiveauxDirigesEns(userconnecte.getIdUsers());
				if(listofNiveauxDiriges != null){
					/*
					 *On va donc plutot placer dans la session la liste des classes dirigés
					 */
					//session.setAttribute("listofClassesForPV", listofClasseDirige);
					model.addAttribute("listofNiveauxDirigesForPV", listofNiveauxDiriges);
				}
				
				
			}
		}
		
	}
	
	@GetMapping(path="/getlistprocesverbalEvalTrim")
	public String getlistprocesverbalEvalTrim(
			@ModelAttribute("getrapportEvalTrimForm") 
			GetrapportEvalTrimForm getrapportEvalTrimForm,
			Model model, HttpServletRequest request){
		
		this.constructModelListprocesverbalEvalTrim(model,	request,	getrapportEvalTrimForm);
		
		return "users/listprocesverbalEvalTrim";
	}
	
	
	/*******************************************
	 * Preparation des listes de toutes sortes à afficher
	 */
	@GetMapping(path="/getNotesEvalClasse")
	public ModelAndView getNotesEvalClasse(
			Model model, HttpServletRequest request,
			@RequestParam(name="idClasseConcerne", defaultValue="0") long idClasseConcerne,
			@RequestParam(name="idEvalConcerne", defaultValue="0") long idEvalConcerne){
		
		HttpSession session=request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		//System.out.println("classeConcernelist "+classeConcerne.getCodeClasses());
		
		Evaluations evalConcerne =  usersService.findEvaluations(idEvalConcerne);
		
		Cours cours = evalConcerne.getCours();
		
		String username=(String)session.getAttribute("username");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
		
		/*//System.out.println("classeConcernelist "+classeConcerne.getCodeClasses()+
				"  evalConcernelist "+evalConcerne.getProportionEval()+"");*/
		
		List<NotesEval> listofNotesEvalSeq = (List<NotesEval>) evalConcerne.getListofnotesEval();
		
	/*	//System.out.println("classeConcernelist "+classeConcerne.getCodeClasses()+
				"  evalConcernelist "+evalConcerne.getProportionEval()+""
						+ " listofNotesEvalSeq "+listofNotesEvalSeq.size());*/
		
		//liste des élèves classé par ordre alphabetique
		List<Eleves> listofAllEleveDeClasseConcerne = usersService.findListElevesClasse(classeConcerne.getIdClasses());
		
		
		session.setAttribute("etablissementConcerne", etablissementConcerne);
		session.setAttribute("listofNotesEvalSeq", listofNotesEvalSeq);
		session.setAttribute("classeConcerneListofNotesEvalSeq", classeConcerne);
		session.setAttribute("effectifclasseConcerneListofNotesEvalSeq", classeConcerne.getListofEleves().size());
		//liste des élèves classé par ordre alphabetique
		session.setAttribute("listofAllEleveDeClasseConcerne", listofAllEleveDeClasseConcerne);
		session.setAttribute("nbrenoteEvalEnregistre", listofNotesEvalSeq.size());
		session.setAttribute("evalConcerneListofNotesEvalSeq", evalConcerne);
		
		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				evalConcerne==null || cours==null || userconnecte==null) {
			/*//System.out.println("un de ces truc est null vraiment dans l'impression du pv du trimestre "
					+ "etablissementConcerne "+etablissementConcerne+
					"classeConcerne "+classeConcerne+" anneeScolaire "+anneeScolaire+
					"evalConcerne "+evalConcerne+" cours"+cours+" userconnecte "+userconnecte);*/
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
			}
			if(evalConcerne == null){
				error+="\n L'EVALUATION CONCERNE N'A PAS ETE RETROUVE";
			}
			if(cours == null){
				error+="\n LE COURS CONCERNE N'EST PAS RETROUVE";
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
		
		parameters.put("deleguation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("deleguation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
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
		
		String titre_pv = "PROCES VERBAL DES NOTES ";
		parameters.put("titre_pv", titre_pv);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);
		
		String matiere = cours.getIntituleCours()+" ("+cours.getMatiere().getIntituleMatiere()+")";
		parameters.put("matiere", matiere);
		String enseignant = profConnecte.getNomsPers()+" "+profConnecte.getPrenomsPers();
		parameters.put("enseignant", enseignant);
		
		int nbre_moyennes = usersService.getNbreNoteDansCourspourEvalDansListe(listofNotesEvalSeq);
		int nbre_sous_moyennes = usersService.getNbreSousNoteDansCourspourEvalDansListe(listofNotesEvalSeq);
		//System.err.println("nbre_moyennes dans evaluation "+nbre_moyennes);
		
		if(nbre_moyennes<0) nbre_moyennes = 0;
		parameters.put("nbre_moyennes", nbre_moyennes);
		
		if(nbre_sous_moyennes<0) nbre_sous_moyennes = 0;
		parameters.put("nbre_sous_moyennes", nbre_sous_moyennes);
		
		double nbM=new Double(nbre_moyennes).doubleValue();
		double nbSM=new Double(nbre_sous_moyennes).doubleValue();
		double pourRr = (nbM/(nbM+nbSM))*100;
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try {
			pourRr =df.parse(df.format(pourRr)).doubleValue();
			////System.out.println("pourcentagessss "+pourcentage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int nb_decimale = 3;
		pourRr = usersService.getUtilitairesBulletins().tronqueDouble(pourRr, nb_decimale);
		String pourR = ""+pourRr+" %";
		
		List<PV_NoteBean> listofPV = (List<PV_NoteBean>) usersService.generatePVEvalAvecListeNote(listofNotesEvalSeq);
		
		String label_devoir=" EVALUATION COMPTANT POUR ";
		
		parameters.put("pourd", evalConcerne.getProportionEval()+"%");
		parameters.put("pourR", pourR);
		parameters.put("label_devoir", label_devoir);
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		//parameters.put("IMAGE_FOND", "src/main/resources/static/images/logobekoko.png");
		parameters.put("datasource", listofPV);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofPV.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/PVNoteEval.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
	}
	
	
	@GetMapping(path="/getprocesverbalNotes")
	public ModelAndView getprocesverbalNotes(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEvalConcerne", defaultValue="0") long idEvalConcerne){
		
		HttpSession session=request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		
		Evaluations evalConcerne =  usersService.findEvaluations(idEvalConcerne);
		Classes classeConcerne = null;
		if(evalConcerne!=null){
			classeConcerne = evalConcerne.getCours().getClasse();
		}
		
		Cours cours = evalConcerne.getCours();
		
		String username=(String)session.getAttribute("username");
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
		
		
		List<NotesEval> listofNotesEvalSeq = (List<NotesEval>) evalConcerne.getListofnotesEval();
	
		
		//liste des élèves classé par ordre alphabetique
		//List<Eleves> listofAllEleveDeClasseConcerne = usersService.findListElevesClasse(classeConcerne.getIdClasses());
		
		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				evalConcerne==null || cours==null || userconnecte==null) {
			/*//System.out.println("un de ces truc est null vraiment dans l'impression du pv du trimestre "
					+ "etablissementConcerne "+etablissementConcerne+
					"classeConcerne "+classeConcerne+" anneeScolaire "+anneeScolaire+
					"evalConcerne "+evalConcerne+" cours"+cours+" userconnecte "+userconnecte);*/
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
			}
			if(evalConcerne == null){
				error+="\n L'EVALUATION CONCERNE N'A PAS ETE RETROUVE";
			}
			if(cours == null){
				error+="\n LE COURS CONCERNE N'EST PAS RETROUVE";
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
		
		parameters.put("deleguation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("deleguation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
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
		
		String titre_pv = "PROCES VERBAL DES NOTES ";
		parameters.put("titre_pv", titre_pv);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);
		
		String matiere = cours.getIntituleCours()+" ("+cours.getMatiere().getIntituleMatiere()+")";
		parameters.put("matiere", matiere);
		String enseignant = profConnecte.getNomsPers()+" "+profConnecte.getPrenomsPers();
		parameters.put("enseignant", enseignant);
		
		int nbre_moyennes = usersService.getNbreNoteDansCourspourEvalDansListe(listofNotesEvalSeq);
		int nbre_sous_moyennes = usersService.getNbreSousNoteDansCourspourEvalDansListe(listofNotesEvalSeq);
		//System.err.println("nbre_moyennes dans evaluation "+nbre_moyennes);
		
		if(nbre_moyennes<0) nbre_moyennes = 0;
		parameters.put("nbre_moyennes", nbre_moyennes);
		
		if(nbre_sous_moyennes<0) nbre_sous_moyennes = 0;
		parameters.put("nbre_sous_moyennes", nbre_sous_moyennes);
		
		double nbM=new Double(nbre_moyennes).doubleValue();
		double nbSM=new Double(nbre_sous_moyennes).doubleValue();
		double pourRr = (nbM/(nbM+nbSM))*100;

		int nb_decimale = 3;
		pourRr = usersService.getUtilitairesBulletins().tronqueDouble(pourRr, nb_decimale);
		String pourR = ""+pourRr+" %";
		
		List<PV_NoteBean> listofPV = (List<PV_NoteBean>) usersService.generatePVEvalAvecListeNote(listofNotesEvalSeq);
		
		String label_devoir=" EVALUATION COMPTANT POUR ";
		
		parameters.put("pourd", evalConcerne.getProportionEval()+"%");
		parameters.put("pourR", pourR);
		parameters.put("label_devoir", label_devoir);
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		//parameters.put("IMAGE_FOND", "src/main/resources/static/images/logobekoko.png");
		parameters.put("datasource", listofPV);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofPV.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/PVNoteEval.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
		
	}
	
	
	@GetMapping(path="/getNotesFinaleClasse")
	public ModelAndView getNotesFinaleClasse(
			Model model, HttpServletRequest request,
			@RequestParam(name="idClasse", defaultValue="0") long idClasseConcerne,
			@RequestParam(name="idCours", defaultValue="0") long idCoursConcerne,
			@RequestParam(name="idSequence", defaultValue="0") long idSequenceConcerne){
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		//System.out.println("classeConcernelist "+classeConcerne.getCodeClasses());
		
		Sequences seqConcerne = usersService.findSequences(idSequenceConcerne);
		
		Cours coursConcerne = usersService.findCours(idCoursConcerne);
		
		List<Evaluations> listofEvalSeq = seqConcerne.getListofEvalSeqDeCours(idCoursConcerne);
		
		HttpSession session=request.getSession();
		
		session.setAttribute("etablissementConcerne", etablissementConcerne);
		session.setAttribute("listofEvalSeq", listofEvalSeq);
		//Initialisation des évaluations séquentiel dans la session
		session.setAttribute("evalSeqpair", null);
		session.setAttribute("evalSeqimpair", null);
		if(listofEvalSeq.size() >= 1) {
			session.setAttribute("evalSeqimpair", listofEvalSeq.get(0));
		}
		if(listofEvalSeq.size() >= 2) session.setAttribute("evalSeqpair", listofEvalSeq.get(1));
		//liste des élèves classé par ordre alphabetique
		session.setAttribute("listofAllEleveDeClasseConcerne", usersService.findListElevesClasse(classeConcerne.getIdClasses()));
		session.setAttribute("coursConcerne", coursConcerne);
		session.setAttribute("seqConcerne", seqConcerne);
		session.setAttribute("effectifclasseConcerneListofNotesEvalSeq", classeConcerne.getListofEleves().size());
		session.setAttribute("classeConcerneListofNotesEvalSeq", classeConcerne);
		
		Annee anneeScolaire = usersService.findAnneeActive();
		
		String username=(String)session.getAttribute("username");
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
		
		if(etablissementConcerne==null || classeConcerne==null || anneeScolaire==null || 
				coursConcerne==null || userconnecte==null) {
			/*//System.out.println("un de ces truc est null vraiment dans l'impression du pv du trimestre "
					+ "etablissementConcerne "+etablissementConcerne+
					"classeConcerne "+classeConcerne+" anneeScolaire "+anneeScolaire
					+" cours  "+coursConcerne+" userconnecte "+userconnecte);*/
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT EST INEXISTANT";
			}
			if(classeConcerne == null){
				error+="\n LA CLASSE N'A PAS ETE RETROUVE AU NIVEAU DE LA BASE DE DONNEE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE N'A PAS ENCORE ETE ENREGISTRE";
			}
			if(userconnecte == null){
				error+="\n ERREUR D'UTILISATEUR CONNECTE";
			}
			
			if(coursConcerne == null){
				error+="\n LE COURS CONCERNE N'EST PAS RETROUVE";
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
		
		parameters.put("deleguation_fr", etablissementConcerne.getDeleguationdeptuteleEtab().toUpperCase());
		parameters.put("deleguation_en", etablissementConcerne.getDeleguationdeptuteleanglaisEtab().toUpperCase());
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
		
		String titre_pv = "PROCES VERBAL SEQUENTIEL DES NOTES ";
		parameters.put("titre_pv", titre_pv);
		String nomClasse= classeConcerne.getCodeClasses()+classeConcerne.getSpecialite().getCodeSpecialite()+
				classeConcerne.getNumeroClasses();
		parameters.put("classe", nomClasse);
		
		String matiere = coursConcerne.getIntituleCours()+" ("+coursConcerne.getMatiere().getIntituleMatiere()+")";
		parameters.put("matiere", matiere);
		String enseignant = profConnecte.getNomsPers()+" "+profConnecte.getPrenomsPers();
		parameters.put("enseignant", enseignant);
		
		int nbre_moyennes = usersService.getNbreNoteDansCourspourSeq(classeConcerne.getIdClasses(), 
				coursConcerne.getIdCours(),seqConcerne.getIdPeriodes());
		int nbre_sous_moyennes = usersService.getNbreSousNoteDansCourspourSeq(classeConcerne.getIdClasses(), 
				coursConcerne.getIdCours(),seqConcerne.getIdPeriodes());
		//System.err.println("nbre_moyennes dans Sequence "+nbre_moyennes);
		
		if(nbre_moyennes<0) nbre_moyennes = 0;
		parameters.put("nbre_moyennes", nbre_moyennes);
		
		if(nbre_sous_moyennes<0) nbre_sous_moyennes = 0;
		parameters.put("nbre_sous_moyennes", nbre_sous_moyennes);
		
		double nbM=new Double(nbre_moyennes).doubleValue();
		double nbSM=new Double(nbre_sous_moyennes).doubleValue();
		double pourRr = (nbM/(nbM+nbSM))*100;
		/*java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");
		try {
			pourRr =df.parse(df.format(pourRr)).doubleValue();
			////System.out.println("pourcentagessss "+pourcentage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int nb_decimale = 3;
		pourRr = usersService.getUtilitairesBulletins().tronqueDouble(pourRr, nb_decimale);
		String pourR = ""+pourRr+" %";
		
		List<PV_SequenceBean> listofPVSeq = (List<PV_SequenceBean>) usersService.generatePVSequence(classeConcerne.getIdClasses(), 
				coursConcerne.getIdCours(),seqConcerne.getIdPeriodes());
		
		int pourds=0;
		int pourcc=0;
		for(Evaluations eval: listofEvalSeq){
			if(eval.getTypeEval().equalsIgnoreCase("CC")){
				pourcc = eval.getProportionEval();
				pourds = 100 - pourcc;
			}
			if(eval.getTypeEval().equalsIgnoreCase("DS")){
				pourds = eval.getProportionEval();
				pourcc = 100 - pourds;
			}
		}
	
		parameters.put("pourds", pourds+"%");
		parameters.put("pourcc", pourcc+"%");
		parameters.put("pourR", pourR);
		parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		//parameters.put("IMAGE_FOND", "src/main/resources/static/images/logobekoko.png");
		parameters.put("datasource", listofPVSeq);
		//System.out.println("Aucun de ces truc n'est null vraiment  "+listofPVSeq.size());
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/PVSequence.jasper");
		view.setApplicationContext(applicationContext);
		
		
		return new ModelAndView(view, parameters);
	}
	
	
	@GetMapping(path="/getlistcoursofEnseignant")
	public String getlistcoursofEnseignant(
			Model model, HttpServletRequest request){
		
		/*
		 * Il faut recuperer l'user connecte et placer la liste des cours qu'il enseigne dans la session pour les besoins d'affichage
		 */
		HttpSession session=request.getSession();
		String username=(String)session.getAttribute("username");
		
		////System.out.println("l'user connecte est  "+username );
		
		Utilisateurs userconnecte = usersService.findByUsername(username);
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(userconnecte != null){
			
			Proffesseurs profConnecte = usersService.findProffesseurs(userconnecte.getIdUsers());
			session.setAttribute("profConnecte", profConnecte);
			session.setAttribute("anneeActive", anneeActive);
			
			List<Cours> listofcoursEnseigne = (List<Cours>) profConnecte.getListofCours();
			
			if(listofcoursEnseigne != null){
				/*
				 * On peut donc garder cette liste dans la session et effectuer l'affichage de son contenu par la suite
				 */
				session.setAttribute("listofcoursEnseigne", listofcoursEnseigne);
				
			}
			
		}
		
		return "users/listcoursofEnseignant";
	}
	
	public void constructModelListOperation(String datemin, String datemax,	 Model model, 
			HttpServletRequest request, int numPageOperation){
		
		
		model.addAttribute("datemin", datemin);
		model.addAttribute("datemax", datemax);
		
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date date_min = spd.parse(datemin);
			Date date_max = spd.parse(datemax);
			//System.out.println("datemin ="+date_min+" datemax="+date_max+" bien converti donc ca va aller");
			
			/*
			 * Il faut recuperer la liste des opérations comprise entre les 2 date et les placer dans le model
			 * puis on va charger celle de la page demande. La taille d'une page est de 10 operations max
			 */
			
			
			Page<Operations> pageofOperations=
					usersService.findPageOperations(date_min, date_max, numPageOperation, 10);
			
			if(pageofOperations.getContent().size()!=0){
				model.addAttribute("listpageofOperations", pageofOperations.getContent());
				int[] tabofPagesOperations=new int[pageofOperations.getTotalPages()];
				
				model.addAttribute("tabofpageOperations", tabofPagesOperations);
				
				model.addAttribute("pageCourante", numPageOperation);
				System.out.println("nbre de page du resultat  "+pageofOperations.getTotalPages());
			}
			else{
				/*
				 * Donc il n'ya aucune page donc aucune operation trouve
				 */
				model.addAttribute("aucune", "aucune operation dans l'intervalle de date donnee");
			}
			
		}
		catch(Exception e){
			System.out.println("erreur de conversion de pendant le dressage de la liste des operations"
					+ " "+datemin+" "+e.getMessage());
			System.out.println("erreur de conversion de pendant le dressage de la liste des operations"
					+ " "+datemax+" "+e.getMessage());
		}
		
		
		//System.out.println("datemin ="+datemin+" datemax="+datemax);
		
		
		
	}
	
	@GetMapping(path="/getlistOperations")
	public String getlistOperations(
			Model model, HttpServletRequest request,
			@RequestParam(name="datemin", defaultValue="2019-01-01") String datemin,
			@RequestParam(name="datemax", defaultValue="2035-01-01") String datemax,
			@RequestParam(name="numPageOperation", defaultValue="0") int numPageOperation){
		
		//System.out.println("voici l'intervalle de date "+datemin+" datemax = "+datemax);
		/*
			 * Il faut recuperer toutes les opérations et les charger dans le modèle afin de les afficher 
			 * page par page pour l'année scolaire active
			 */
		this.constructModelListOperation(datemin, datemax,	model, request, numPageOperation);
		
		return "users/listeOperations";
	}
	
	@GetMapping(path="/imprimerRecuOperation")
	public ModelAndView imprimerRecuOperation(Model model, HttpServletRequest request,
			@RequestParam(name="idOperation_a_imprimer", defaultValue="0") long idOperation_a_imprimer){
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		if(etablissementConcerne == null ||  anneeScolaire == null ){
			
			String error="";
			
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

		File f=new File(logoetabDir+etablissementConcerne.getLogoEtab());

		if(f.exists()==true){
			parameters.put("logo", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("logo", "classpath:/static/images/logobekoko.png");
		}
		
		Operations operation_concerne = usersService.findOperation(idOperation_a_imprimer);
		if(operation_concerne == null) {
			
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(operation_concerne == null){
				error+="\n L'OPERATION EFFECTUE N'A PAS ETE RETROUVE";
			}
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
		}
		
		Date date_op = operation_concerne.getDateOperation();
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");//"dd-MM-yyyy"
		String dateString = spd.format(date_op);
		parameters.put("date_jour", dateString);
		
		String numero_recu=operation_concerne.getIdentifiantOperation();
		parameters.put("numero_recu", numero_recu);
		
		double montantTransaction = operation_concerne.getMontantOperation();
		parameters.put("montant", montantTransaction+" F cfa");
		
		Eleves eleveConcerne = operation_concerne.getCompteinscription().getEleveProprietaire();
		if(eleveConcerne==null){
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(eleveConcerne == null){
				error+="\n L'ELEVE CONCERNE N'A PAS ETE RETROUVE";
			}
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
		}
		
		
		String classeString=eleveConcerne.getClasse().getCodeClasses()+
				eleveConcerne.getClasse().getSpecialite().getCodeSpecialite()+
				eleveConcerne.getClasse().getNumeroClasses();
		
		String recu_de=(eleveConcerne.getNomsEleves()+" "+eleveConcerne.getPrenomsEleves()).toUpperCase();
		recu_de+=" pour la classe de ";
		recu_de+=classeString;
		parameters.put("recu_de", recu_de);
		
		String nature_versement="Frais de scolarité / School fees ";
		parameters.put("nature_versement", nature_versement);
		
		long montant_long = Math.round(montantTransaction);
		String montant_en_lettre_fr = usersService.ecritEnLettreNombrePlusDeDouze9(montant_long, true);
		parameters.put("montant_verse_lettre_fr", montant_en_lettre_fr+" Francs cfa");
		
		String montant_en_lettre_en = usersService.writeInLetterNumberOverTwelve9(montant_long);
		parameters.put("montant_verse_lettre_en", montant_en_lettre_en+" Francs cfa");
		
		Collection<Recu_versement> collectionofEleveprovClasse = new ArrayList<Recu_versement>();
		Recu_versement rv=new Recu_versement();
		collectionofEleveprovClasse.add(rv);
		parameters.put("datasource", collectionofEleveprovClasse);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/recus/recu.jasper");
		view.setApplicationContext(applicationContext);
		
		return new ModelAndView(view, parameters);
		
	}
	
	@GetMapping(path="/exportlistOperations")
	public ModelAndView exportlistOperations(Model model, HttpServletRequest request,
			@RequestParam(name="datemin", defaultValue="2019-01-01") String datemin,
			@RequestParam(name="datemax", defaultValue="2035-01-01") String datemax){
		
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Date date_min = spd.parse(datemin);
			Date date_max = spd.parse(datemax);
			
			List<Operations> listOperation = usersService.findListAllOperations(date_min, date_max);
			double montantTotal=usersService.calculMontantTotalListOperation(listOperation);
			Collection<OperationBean> listofOperationBean = usersService.generateListOperation(date_min, date_max);
			
			Etablissement etablissementConcerne = usersService.getEtablissement();
			Annee anneeScolaire = usersService.findAnneeActive();
			if(etablissementConcerne == null ||  anneeScolaire == null ){
				String error="";
				
				String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
				Map<String, Object> parameters1 = new HashMap<String, Object>();
				if(etablissementConcerne == null){
					error+="\n L'ETABLISSEMENT CONCERNE N'A PAS ETE RETROUVE";
				}
				if(anneeScolaire == null){
					error+="\n L'ANNEE SCOLAIRE CONCERNE N'A PAS ETE RETROUVE";
				}
				Collection<ErrorBean> collectionofErreurBean = 
						usersService.generateCollectionofErrorBean(error);
				
				parameters1.put("erreur", erreur);
				parameters1.put("datasource", collectionofErreurBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/errors/error.jasper");
				view.setApplicationContext(applicationContext);

				return new ModelAndView(view, parameters1);
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

			File f=new File(logoetabDir+etablissementConcerne.getLogoEtab());

			if(f.exists()==true){
				parameters.put("logo", logoetabDir+etablissementConcerne.getLogoEtab());
			}
			else{
				parameters.put("logo", "classpath:/static/images/logobekoko.png");
			}

			parameters.put("datemin", datemin);
			parameters.put("datemax", datemax);
			parameters.put("total_encaisse", montantTotal+"");
			
			long montant_long = Math.round(montantTotal);
			String montantLettre_fr = usersService.ecritEnLettreNombrePlusDeDouze9(montant_long, true);
			String montantLettre_en = usersService.writeInLetterNumberOverTwelve9(montant_long);
			parameters.put("total_enlettre", montantLettre_fr+" Francs cfa /" +montantLettre_en+" Francs cfa");
			
			parameters.put("datasource", listofOperationBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/recus/listOperation.jasper");
			view.setApplicationContext(applicationContext);
			
			return new ModelAndView(view, parameters);
			
		}
		catch(Exception e){
			System.out.println("erreur de conversion de date pendant le dressage de la liste des operations"
					+ " "+datemin+" "+e.getMessage());
			System.out.println("erreur de conversion de date pendant le dressage de la liste des operations"
					+ " "+datemax+" "+e.getMessage());
			
		}
		return null;
	}
	
	
	
	@GetMapping(path="/imprimerlistingVersement")
	public ModelAndView imprimerlistingVersement(Model model, HttpServletRequest request,
			@RequestParam(name="idEleve", defaultValue="0") Long idEleve){
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		Eleves eleveConcerne = usersService.findEleves(idEleve);
		if(etablissementConcerne == null ||  anneeScolaire == null || eleveConcerne == null){
			System.out.println("Erreur dans les paramètre car etab, annee ou eleve sont null");
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT CONCERNE N'A PAS ETE RETROUVE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE CONCERNE N'A PAS ETE RETROUVE";
			}
			if(eleveConcerne == null){
				error+="\n L'ELEVE CONCERNE N'A PAS ETE RETROUVE";
			}
			
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
			
		}
		
		
		List<Operations> listOperationEleve = usersService.findListAllOperationsEleve(idEleve);
		double montantTotal=usersService.calculMontantTotalListOperation(listOperationEleve);
		Collection<OperationBean> listofOperationBean = usersService.generateListOperationEleve(idEleve);
		
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

		File f=new File(logoetabDir+etablissementConcerne.getLogoEtab());

		if(f.exists()==true){
			parameters.put("logo", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("logo", "classpath:/static/images/logobekoko.png");
		}
		
		String nomeleveconcerne = eleveConcerne.getNomsEleves();
		nomeleveconcerne+="  "+eleveConcerne.getPrenomsEleves();
		String classeeleve =  eleveConcerne.getClasse().getCodeClasses();
		classeeleve +=  eleveConcerne.getClasse().getSpecialite().getCodeSpecialite();
		classeeleve +=  eleveConcerne.getClasse().getNumeroClasses();
		
		parameters.put("eleve_concerne", nomeleveconcerne);
		parameters.put("classe_eleve", classeeleve);
		parameters.put("total_encaisse", montantTotal+"");
		
		long montant_long = Math.round(montantTotal);
		String montantLettre_fr = usersService.ecritEnLettreNombrePlusDeDouze9(montant_long, true);
		String montantLettre_en = usersService.writeInLetterNumberOverTwelve9(montant_long);
		parameters.put("total_enlettre", montantLettre_fr+" Francs cfa /" +montantLettre_en+" Francs cfa");
		
		parameters.put("datasource", listofOperationBean);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/recus/listOperationEleve.jasper");
		view.setApplicationContext(applicationContext);
		
		return new ModelAndView(view, parameters);
	}
	
	@GetMapping(path="/exportlistElevesInsolvable")
	public ModelAndView exportlistElevesInsolvable(Model model, HttpServletRequest request,
			@RequestParam(name="idClasseSelect", defaultValue="0") Long idClasseSelect){
		
		HttpSession session=request.getSession();
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		Classes classe = usersService.findClasses(idClasseSelect);
		if(etablissementConcerne == null ||  anneeScolaire == null || classe == null){
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT CONCERNE N'A PAS ETE RETROUVE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE CONCERNE N'A PAS ETE RETROUVE";
			}
			if(classe == null){
				error+="\n LA CLASSE CONCERNE N'A PAS ETE RETROUVE";
			}
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
		}
		
		List<Eleves> listeleveinsolvableDansClasses = usersService.getListElevesInsolvable(idClasseSelect);
		Collection<EleveInsolvableBean> collectionofInsolvable = usersService.generateListEleveInsolvable(idClasseSelect);
		
		session.setAttribute("listeleveinsolvableDansClasses", listeleveinsolvableDansClasses);
		
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
		
		String classe_concerne =  classe.getCodeClasses();
		classe_concerne +=  classe.getSpecialite().getCodeSpecialite();
		classe_concerne +=  classe.getNumeroClasses();
		parameters.put("classe", classe_concerne);
		
		parameters.put("montant_exige", classe.getMontantScolarite()+"");
		
		parameters.put("datasource", collectionofInsolvable);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/fiches/ListeEleveInsolvable.jasper");
		view.setApplicationContext(applicationContext);
		
		return new ModelAndView(view, parameters);
	}
	
	public void constructModelgetdonneesConseilSequentiel(Model model,	HttpServletRequest request){
		
		List<Classes> listofClasses = usersService.findAllClasse();
		if(listofClasses.size()>0){
			model.addAttribute("affichechoixclasse", "oui");
		}
		else{
			model.addAttribute("affichechoixclasse", "non");
		}
		/*
		 * On place la liste des niveaux dans le modele sachant qu'on retrouvera les classes
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			/*
			 * On place la liste des séquences actives de l'année active
			 */
			List<Sequences> listofSequenceActive = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
			model.addAttribute("listofSequenceActive", listofSequenceActive);
			List<Sequences> listofSequence = usersService.findAllSequence(anneeActive.getIdPeriodes());
			model.addAttribute("listofSequence", listofSequence);
		}
		
	}
	
	@GetMapping(path="/getdonneesConseilSequentiel")
	public String getdonneesConseilSequentiel(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesConseilSequentiel(model,	request);
		
		return "users/donneesConseilSequentiel";
	}
	
	public void constructModelgetformSaisieConseilClasseSeq(Model model,	HttpServletRequest request,
			Long idSequenceConcerne,	Long idClasseConcerne, int numPageEleves, int taillePage){
		
		//HttpSession session = request.getSession();
		model.addAttribute("idClasseConcerne", idClasseConcerne);
		model.addAttribute("idSequenceConcerne", idSequenceConcerne);
		model.addAttribute("numPageEleves", numPageEleves);
		
		/*
		 * Il faut faire la liste des sanctions diciplinaire et placer dans le modele car on devra en choisir 
		 * une pour chaque élève. 
		 * Si cette liste est vide alors le conseil ne saurait se passé
		 * On doit faire pareille avec la liste des sanctions travail (distinction)
		 */
		List<SanctionDisciplinaire> listofSanctionDisc = usersService.findListAllSanctionDisciplinaire();
		List<SanctionTravail> listofSanctionTrav = usersService.findListAllSanctionTravail();
		/*
		 * Pour signifie que les parametres du conseil ne sont pas encore fixé par l'administrateur ie les 
		 * possible sanction de travail et sanction disciplinaire
		 */
		model.addAttribute("paramConseil","0");
		
		if(listofSanctionDisc.size()>0 && listofSanctionTrav.size()>0){
			model.addAttribute("listofSanctionDisc", listofSanctionDisc);
			model.addAttribute("listofSanctionTrav", listofSanctionTrav);
			
			/*
			 * Pour signifie que les parametres du conseil sont deja fixé par l'administrateur ie les 
			 * possible sanction de travail et sanction disciplinaire
			 */
			model.addAttribute("paramConseil","1");
			
			List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClasseConcerne);
			System.out.println("listofAllEleve=="+listofAllEleve);
			if(listofAllEleve != null){
				model.addAttribute("effectifTotal", listofAllEleve.size());
				System.out.println("liste superieur a 0 "+listofAllEleve.size());
				if((listofAllEleve.size() > 0)){
					model.addAttribute("listofAllEleve", listofAllEleve);
					Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
					Classes classeConcerne = usersService.findClasses(idClasseConcerne);
					model.addAttribute("sequenceConcerne", sequenceConcerne);
					model.addAttribute("classeConcerne", classeConcerne);
					
					Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseConcerne,	
							numPageEleves, taillePage);
					
					model.addAttribute("ub", usersService.getUtilitairesBulletins());
					
					if(pageofEleves.getContent().size()!=0){
						model.addAttribute("listofEleves", pageofEleves.getContent());
						int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
							
						model.addAttribute("listofPagesEleves", listofPagesEleves);
							
						model.addAttribute("pageCouranteEleves", numPageEleves);
						
					}
					
				}
			}
		}
		
	}
	
	@GetMapping(path="/getformSaisieConseilClasseSeq")
	public String getformSaisieConseilClasseSeq(Model model, HttpServletRequest request,
			@RequestParam(name="idSequenceConcerne", defaultValue="-1") Long idSequenceConcerne,
			@RequestParam(name="idClasseConcerne", defaultValue="0") Long idClasseConcerne,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
		this.constructModelgetformSaisieConseilClasseSeq(model,	request,	idSequenceConcerne,
				idClasseConcerne, numPageEleves, taillePage);
		
		return "users/formSaisieConseilClasseSeq";
	}

	@GetMapping(path="/getUpdateDecisionConseilSeq")
	public String getUpdateDecisionConseilSeq(Model model, HttpServletRequest request,
			@RequestParam(name="idElevesConcerne", defaultValue="-1") Long idElevesConcerne,
			@RequestParam(name="idSequenceConcerne", defaultValue="-1") Long idSequenceConcerne,
			@RequestParam(name="idClasseConcerne", defaultValue="-1") Long idClasseConcerne,
			@RequestParam(name="idsanctionDiscAssocie", defaultValue="-1") Long idsanctionDiscAssocie,
			@RequestParam(name="idsanctionTravAssocie", defaultValue="-1") Long idsanctionTravAssocie,
			@RequestParam(name="nbreperiode", defaultValue="0") String nbreperiode,
			@RequestParam(name="unite", defaultValue="0") String unite,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		HttpSession session = request.getSession();
		session.setAttribute("idElevesConcerne", idElevesConcerne);
		try{
			int nbre_periode = Integer.parseInt(nbreperiode);
			if((nbre_periode>0 && unite.equalsIgnoreCase("RAS")==true)||(nbre_periode<0)){
				return "redirect:/logesco/users/getformSaisieConseilClasseSeq?updateDecisionConseilSeqErrorUnite"
						+ "&&idSequenceConcerne="+idSequenceConcerne
						+ "&&idClasseConcerne="+idClasseConcerne
						+ "&&numPageEleves="+numPageEleves
						+ "&&taillePage="+taillePage;
			}
			
			/*
			 * Le nombre de periode et l'unite sont deja bien ie soit 0 soit >0 et unite!=RAS
			 */
			//On recupere l'eleve associe
			int ret = usersService.saveDecisionConseilSeq(idElevesConcerne, idSequenceConcerne, 
					idsanctionDiscAssocie, nbre_periode, unite, idsanctionTravAssocie);
			if (ret == -1) return "redirect:/logesco/users/getformSaisieConseilClasseSeq?getformSaisieConseilClasseSeq"
										+ "&&idSequenceConcerne="+idSequenceConcerne
										+ "&&idClasseConcerne="+idClasseConcerne
										+ "&&numPageEleves="+numPageEleves
										+ "&&taillePage="+taillePage;
			
			if(ret == 0) return "redirect:/logesco/users/getformSaisieConseilClasseSeq?updateDecisionConseilSeqErrorUnite"
										+ "&&idSequenceConcerne="+idSequenceConcerne
										+ "&&idClasseConcerne="+idClasseConcerne
										+ "&&numPageEleves="+numPageEleves
										+ "&&taillePage="+taillePage;
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/logesco/users/getformSaisieConseilClasseSeq?updateDecisionConseilSeqErrorConvert"
					+ "&&idSequenceConcerne="+idSequenceConcerne
					+ "&&idClasseConcerne="+idClasseConcerne
					+ "&&numPageEleves="+numPageEleves
					+ "&&taillePage="+taillePage;
		}
		
		return "redirect:/logesco/users/getformSaisieConseilClasseSeq?updateDecisionConseilSeqSuccess"
					+ "&&idSequenceConcerne="+idSequenceConcerne
					+ "&&idClasseConcerne="+idClasseConcerne
					+ "&&numPageEleves="+numPageEleves
					+ "&&taillePage="+taillePage;
	}
	
	public void constructModelgetdonneesConseilTrimestriel(Model model,	HttpServletRequest request){
		
		List<Classes> listofClasses = usersService.findAllClasse();
		if(listofClasses.size()>0){
			model.addAttribute("affichechoixclasse", "oui");
		}
		else{
			model.addAttribute("affichechoixclasse", "non");
		}
		/*
		 * On place la liste des niveaux dans le modele sachant qu'on retrouvera les classes
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			/*
			 * On place la liste des trimestres actifs de l'année active
			 */
			List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
			model.addAttribute("listofTrimestreActif", listofTrimestreActif);
			List<Trimestres> listofTrimestre = usersService.findAllTrimestresAnnee(anneeActive.getIdPeriodes());
			model.addAttribute("listofTrimestre", listofTrimestre);
		}
		
	}
	
	@GetMapping(path="/getdonneesConseilTrimestriel")
	public String getdonneesConseilTrimestriel(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesConseilTrimestriel(model,	request);
		
		return "users/donneesConseilTrimestriel";
	}
	
	public void constructModelgetformSaisieConseilClasseTrim(Model model,	HttpServletRequest request,
			Long idTrimestreConcerne,	Long idClasseConcerne, int numPageEleves, int taillePage){
		
		model.addAttribute("idClasseConcerne", idClasseConcerne);
		model.addAttribute("idTrimestreConcerne", idTrimestreConcerne);
		
		/*
		 * Il faut faire la liste des sanctions diciplinaire et placer dans le modele car on devra en choisir 
		 * une pour chaque élève. 
		 * Si cette liste est vide alors le conseil ne saurait se passé
		 * On doit faire pareille avec la liste des sanctions travail (distinctions)
		 */
		List<SanctionDisciplinaire> listofSanctionDisc = usersService.findListAllSanctionDisciplinaire();
		List<SanctionTravail> listofSanctionTrav = usersService.findListAllSanctionTravail();
		model.addAttribute("paramConseil","0");
		
		if(listofSanctionDisc.size()>0 && listofSanctionTrav.size()>0){
			model.addAttribute("listofSanctionDisc", listofSanctionDisc);
			model.addAttribute("listofSanctionTrav", listofSanctionTrav);
			model.addAttribute("paramConseil","1");
			
			List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClasseConcerne);
			if(listofAllEleve != null){
				model.addAttribute("effectifTotal", listofAllEleve.size());
				if((listofAllEleve.size() > 0)){
					model.addAttribute("listofAllEleve", listofAllEleve);
					Trimestres trimestreConcerne = usersService.findTrimestres(idTrimestreConcerne);
					
					Classes classeConcerne = usersService.findClasses(idClasseConcerne);
					model.addAttribute("trimestreConcerne", trimestreConcerne);
					model.addAttribute("classeConcerne", classeConcerne);
					
					Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseConcerne,	
							numPageEleves, taillePage);
					
					model.addAttribute("ub", usersService.getUtilitairesBulletins());
					
					if(pageofEleves.getContent().size()!=0){
						model.addAttribute("listofEleves", pageofEleves.getContent());
						int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];  
							
						model.addAttribute("listofPagesEleves", listofPagesEleves);
							
						model.addAttribute("pageCouranteEleves", numPageEleves);
						
					}
					
				}
			}
		}
		
	}
	
	@GetMapping(path="/getformSaisieConseilClasseTrim")
	public String getformSaisieConseilClasseTrim(Model model, HttpServletRequest request,
			@RequestParam(name="idTrimestreConcerne", defaultValue="-1") Long idTrimestreConcerne,
			@RequestParam(name="idClasseConcerne", defaultValue="0") Long idClasseConcerne,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
		this.constructModelgetformSaisieConseilClasseTrim(model,	request,	idTrimestreConcerne,
				idClasseConcerne, numPageEleves, taillePage);
		
		return "users/formSaisieConseilClasseTrim";
	}
	
	
	@GetMapping(path="/getUpdateDecisionConseilTrim")
	public String getUpdateDecisionConseilTrim(Model model, HttpServletRequest request,
			@RequestParam(name="idElevesConcerne", defaultValue="-1") Long idElevesConcerne,
			@RequestParam(name="idTrimestreConcerne", defaultValue="-1") Long idTrimestreConcerne,
			@RequestParam(name="idClasseConcerne", defaultValue="-1") Long idClasseConcerne,
			@RequestParam(name="idsanctionDiscAssocie", defaultValue="-1") Long idsanctionDiscAssocie,
			@RequestParam(name="idsanctionTravAssocie", defaultValue="-1") Long idsanctionTravAssocie,
			@RequestParam(name="nbreperiode", defaultValue="0") String nbreperiode,
			@RequestParam(name="unite", defaultValue="0") String unite,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
		HttpSession session = request.getSession();
		session.setAttribute("idElevesConcerne", idElevesConcerne);
		
		try{
			int nbre_periode = Integer.parseInt(nbreperiode);
			if((nbre_periode>0 && unite.equalsIgnoreCase("RAS")==true)||(nbre_periode<0)){
				return "redirect:/logesco/users/getformSaisieConseilClasseTrim?updateDecisionConseilTrimErrorUnite"
						+ "&&idTrimestreConcerne="+idTrimestreConcerne
						+ "&&idClasseConcerne="+idClasseConcerne
						+ "&&numPageEleves="+numPageEleves
						+ "&&taillePage="+taillePage;
			}
			
			
			/*
			 * Le nombre de  periode et l'unite sont deja bien ie soit 0 soit >0 et unite!=RAS
			 */
			//On recupere l'eleve associe
			int ret = usersService.saveDecisionConseilTrim(idElevesConcerne, idTrimestreConcerne, 
					idsanctionDiscAssocie, nbre_periode, unite, idsanctionTravAssocie);
			if (ret == -1) return "redirect:/logesco/users/getformSaisieConseilClasseTrim?getformSaisieConseilClasseTrim"
										+ "&&idTrimestreConcerne="+idTrimestreConcerne
										+ "&&idClasseConcerne="+idClasseConcerne
										+ "&&numPageEleves="+numPageEleves
										+ "&&taillePage="+taillePage;
			
			if(ret == 0) return "redirect:/logesco/users/getformSaisieConseilClasseTrim?updateDecisionConseilTrimErrorUnite"
										+ "&&idTrimestreConcerne="+idTrimestreConcerne
										+ "&&idClasseConcerne="+idClasseConcerne
										+ "&&numPageEleves="+numPageEleves
										+ "&&taillePage="+taillePage;
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/logesco/users/getformSaisieConseilClasseTrim?updateDecisionConseilTrimErrorConvert"
					+ "&&idTrimestreConcerne="+idTrimestreConcerne
					+ "&&idClasseConcerne="+idClasseConcerne
					+ "&&numPageEleves="+numPageEleves
					+ "&&taillePage="+taillePage;
		}
		
		
		
		return "redirect:/logesco/users/getformSaisieConseilClasseTrim?updateDecisionConseilTrimSuccess"
					+ "&&idTrimestreConcerne="+idTrimestreConcerne
					+ "&&idClasseConcerne="+idClasseConcerne
					+ "&&numPageEleves="+numPageEleves
					+ "&&taillePage="+taillePage;
		
	}
	
	
	public void constructModelgetdonneesConseilAnnuel(Model model,	HttpServletRequest request){
		
		List<Classes> listofClasses = usersService.findAllClasse();
		if(listofClasses.size()>0){
			model.addAttribute("affichechoixclasse", "oui");
		}
		else{
			model.addAttribute("affichechoixclasse", "non");
		}
		
		/*
		 * On place la liste des niveaux dans le modele sachant qu'on retrouvera les classes
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
			/*List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
			model.addAttribute("listofTrimestreActif", listofTrimestreActif);
			List<Trimestres> listofTrimestre = usersService.findAllTrimestresAnnee(anneeActive.getIdPeriodes());
			model.addAttribute("listofTrimestre", listofTrimestre);*/
		}
		
	}
	
	
	@GetMapping(path="/getdonneesConseilAnnuel")
	public String getdonneesConseilAnnuel(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesConseilAnnuel(model,	request);
		
		return "users/donneesConseilAnnuel";
	}
	
	public void constructModelgetformSaisieConseilClasseAn(Model model,	HttpServletRequest request,
			Long idAnneeActive,	Long idClasseConcerne, int numPageEleves, int taillePage){
		
		model.addAttribute("idClasseConcerne", idClasseConcerne);
		model.addAttribute("idAnneeActive", idAnneeActive);
		
		/*
		 * Si cette liste est vide alors le conseil ne saurait se passé
		 * On doit faire pareille avec la liste des sanctions travail (distinctions)
		 * et pour la liste des décisions car etant donnée que c'est un conseil de classe annuel, ce sont les 
		 * decisions qui sont plus importante. 
		 */
		//List<SanctionDisciplinaire> listofSanctionDisc = usersService.findListAllSanctionDisciplinaire();
		List<SanctionTravail> listofSanctionTrav = usersService.findListAllSanctionTravail();
		List<Decision> listofDecision = usersService.findListAllDecision();
		
		model.addAttribute("paramConseil","0");
		
		if(listofDecision.size()>0 && listofSanctionTrav.size()>0){
			model.addAttribute("listofDecision", listofDecision);
			model.addAttribute("listofSanctionTrav", listofSanctionTrav);
			model.addAttribute("paramConseil","1");
			List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClasseConcerne);
			if(listofAllEleve != null){
				model.addAttribute("listofAllEleve", listofAllEleve);
				Annee anneeConcerne = usersService.findAnnee(idAnneeActive);
				
				Classes classeConcerne = usersService.findClasses(idClasseConcerne);
				model.addAttribute("anneeConcerne", anneeConcerne);
				model.addAttribute("classeConcerne", classeConcerne);
				
				/*
				 * Il faut charger aussi dans le modele de la page du formulaire les probables futur classe qu'un 
				 * élève peut avoir après un conseil de classe annuelle
				 */
				List<Niveaux> listofNiveauSup = usersService.findListNiveauSup(classeConcerne);
				model.addAttribute("listniveauClasseSup", listofNiveauSup);
				
				Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseConcerne,	
						numPageEleves, taillePage);
				
				model.addAttribute("ub", usersService.getUtilitairesBulletins());
				
				if(pageofEleves.getContent().size()!=0){
					model.addAttribute("listofEleves", pageofEleves.getContent());
					int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
						
					model.addAttribute("listofPagesEleves", listofPagesEleves);
						
					model.addAttribute("pageCouranteEleves", numPageEleves);
					
				}
				
			}
		}
		
	}
	
	@GetMapping(path="/getformSaisieConseilClasseAn")
	public String getformSaisieConseilClasseAn(Model model, HttpServletRequest request,
			@RequestParam(name="idAnneeActive", defaultValue="-1") Long idAnneeActive,
			@RequestParam(name="idClasseConcerne", defaultValue="0") Long idClasseConcerne,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
		this.constructModelgetformSaisieConseilClasseAn(model,	request,	idAnneeActive,
				idClasseConcerne, numPageEleves, taillePage);
		
		return "users/formSaisieConseilClasseAn";
	}
	
	@GetMapping(path="/getUpdateDecisionConseilAn")
	public String getUpdateDecisionConseilAn(Model model, HttpServletRequest request,
			@RequestParam(name="idElevesConcerne", defaultValue="-1") Long idElevesConcerne,
			@RequestParam(name="idAnneeActive", defaultValue="-1") Long idAnneeActive,
			@RequestParam(name="idClasseConcerne", defaultValue="-1") Long idClasseConcerne,
			@RequestParam(name="idDecisionAssocie", defaultValue="-1") Long idDecisionAssocie,
			@RequestParam(name="idsanctionTravAssocie", defaultValue="-1") Long idsanctionTravAssocie,
			@RequestParam(name="idClasseFuturAssocie", defaultValue="-1") Long idClasseFuturAssocie,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
		HttpSession session = request.getSession();
		session.setAttribute("idElevesConcerne", idElevesConcerne);
		
		int ret = usersService.saveDecisionConseilAn(idElevesConcerne, idAnneeActive, 
				idsanctionTravAssocie, idClasseFuturAssocie, idDecisionAssocie);
		
		if(ret == 0){
			return "redirect:/logesco/users/getformSaisieConseilClasseAn?updateDecisionConseilAnError"
					+ "&&idAnneeActive="+idAnneeActive
					+ "&&idClasseConcerne="+idClasseConcerne
					+ "&&numPageEleves="+numPageEleves
					+ "&&taillePage="+taillePage;
		}
		
		return "redirect:/logesco/users/getformSaisieConseilClasseAn?updateDecisionConseilAnSuccess"
				+ "&&idAnneeActive="+idAnneeActive
				+ "&&idClasseConcerne="+idClasseConcerne
				+ "&&numPageEleves="+numPageEleves
				+ "&&taillePage="+taillePage;
		
	}
	
	public void constructModelgetdonneesRAbsCycle(Model model,	HttpServletRequest request){
		
		List<Cycles> listofCycles = usersService.findAllCycle();
		if(listofCycles.size()>0){
			model.addAttribute("affichechoixcycle", "oui");
			model.addAttribute("listofCycles", listofCycles);
		}
		else{
			model.addAttribute("affichechoixcycle", "non");
		}
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
		}
		
		List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
		model.addAttribute("listofTrimestreActif", listofTrimestreActif);
		
		List<Sequences> listofSequenceActif = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequenceActif", listofSequenceActif);
		
	}
	
	
	@GetMapping(path="/getdonneesRAbsCycle")
	public String getdonneesRAbsCycle(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesRAbsCycle(model,	request);
		
		return "users/donneesRAbsCycle";
	}
	
	@GetMapping(path="/exportRAbsencesCycle")
	public ModelAndView exportRAbsencesCycle(Model model, HttpServletRequest request,
			@RequestParam(name="idCycleConcerne", defaultValue="0") Long idCycleConcerne,
			@RequestParam(name="idPeriode", defaultValue="0") Long idPeriode,
			@RequestParam(name="datedebut", defaultValue="2019-01-01") String datedebut,
			@RequestParam(name="datefin", defaultValue="2035-01-01") String datefin){
		
		HttpSession session = request.getSession();
		/*
		 * Il faut chercher le cycle et retourner une erreur s'il n'existe pas
		 */
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		if(etablissementConcerne == null ||  anneeScolaire == null){
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT CONCERNE N'A PAS ETE RETROUVE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE CONCERNE N'A PAS ETE RETROUVE";
			}
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
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
			parameters.put("LOGO", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		}

		
		
		/*
		 * On fixe les autres paramètres du PDF et on va aussi aller chercher les rapports par cycle
		 * Donc il faut le cycle et si le cycle rechercher est null alors on veut pour tous les cycles
		 */
		Cycles cycleConcerne = usersService.findCycle(idCycleConcerne);
		if(idPeriode.longValue()==0){
			SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
			try{
				Date date_min = spd.parse(datedebut);
				Date date_max = spd.parse(datefin);
				parameters.put("date1", datedebut);
				parameters.put("date2", datefin);
				parameters.put("periode", "");
				
				Collection<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = 
						usersService.generateListFicheRecapAbsenceCycleBean(cycleConcerne, date_min, date_max);
				
				//System.out.println("taille de la liste des fiches = "+listofFicheRecapAbsenceCycleBean.size());
				
				
				parameters.put("datasource", listofFicheRecapAbsenceCycleBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceCycle.jasper");
				view.setApplicationContext(applicationContext);
				
				return new ModelAndView(view, parameters);
				
			}
			catch(Exception e){
				System.out.println("Erreur de conversion des dates "+datedebut+" et "+datefin+" et on doit "
						+ " lancer un pdf d'erreur "+e.getMessage());
				e.printStackTrace();
			}
		}
		else{
			/*
			 * A ce niveau cela signifie qu'il n'a pas choisi un intervalle de date mais juste une période bien 
			 * déterminer et enregistrer dans le système comme une séquence, un trimestre ou une 
			 * annéescolaire
			 */
			String periode = "";
			String lang = (String)session.getAttribute("lang");
			Sequences periodSequence = usersService.findSequences(idPeriode);
			Trimestres periodTrimestre = usersService.findTrimestres(idPeriode);
			Annee periodAnnee = usersService.findAnnee(idPeriode);
			
			if(periodSequence == null && periodTrimestre == null && periodAnnee == null) {
				String error=" LA PERIODE SPECIFIEE N'A PAS ETE RETROUVEE";
				
				String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
				Map<String, Object> parameters1 = new HashMap<String, Object>();
				
				Collection<ErrorBean> collectionofErreurBean = 
						usersService.generateCollectionofErrorBean(error);
				
				parameters1.put("erreur", erreur);
				parameters1.put("datasource", collectionofErreurBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/errors/error.jasper");
				view.setApplicationContext(applicationContext);

				return new ModelAndView(view, parameters1);
			}
			
			Collection<FicheRecapAbsenceCycleBean> listofFicheRecapAbsenceCycleBean = null;
			
			if(periodSequence !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Séquence "+periodSequence.getNumeroSeq(): 
					"Sequence "+periodSequence.getNumeroSeq();

				listofFicheRecapAbsenceCycleBean = 
						usersService.generateListFicheRecapAbsenceCycleSeqBean(cycleConcerne, periodSequence);
				
			}
			
			if(periodTrimestre !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Trimestre "+periodTrimestre.getNumeroTrim(): 
					"Term "+periodTrimestre.getNumeroTrim();
				
				listofFicheRecapAbsenceCycleBean = 
						usersService.generateListFicheRecapAbsenceCycleTrimBean(cycleConcerne, periodTrimestre);
				
			}
			
			if(periodAnnee !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?" "+periodAnnee.getIntituleAnnee(): 
					" "+periodAnnee.getIntituleAnnee();
				
				listofFicheRecapAbsenceCycleBean = 
						usersService.generateListFicheRecapAbsenceCycleAnBean(cycleConcerne, periodAnnee);
				
			}
			
			parameters.put("date1", "");
			parameters.put("date2", "");
			parameters.put("periode", periode);
			
			parameters.put("datasource", listofFicheRecapAbsenceCycleBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceCycle.jasper");
			view.setApplicationContext(applicationContext);
			
			return new ModelAndView(view, parameters);
			
			
			
		}
		
		return null;
	}
	
	
	
	public void constructModelgetdonneesRabsNiveau(Model model,	HttpServletRequest request){
		
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		if(listofNiveaux.size()>0){
			model.addAttribute("affichechoixcycle", "oui");
			model.addAttribute("listofNiveaux", listofNiveaux);
		}
		else{
			model.addAttribute("affichechoixcycle", "non");
		}
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
		}
		
		List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
		model.addAttribute("listofTrimestreActif", listofTrimestreActif);
		
		List<Sequences> listofSequenceActif = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequenceActif", listofSequenceActif);
		
	}
	
	@GetMapping(path="/getdonneesRabsNiveau")
	public String getdonneesRabsNiveau(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesRabsNiveau(model,	request);
		
		return "users/donneesRabsNiveaux";
	}
	
	@GetMapping(path="/exportRAbsencesNiveaux")
	public ModelAndView exportRAbsencesNiveaux(Model model, HttpServletRequest request,
			@RequestParam(name="idNiveauxConcerne", defaultValue="0") Long idNiveauxConcerne,
			@RequestParam(name="idPeriode", defaultValue="0") Long idPeriode,
			@RequestParam(name="datedebut", defaultValue="2019-01-01") String datedebut,
			@RequestParam(name="datefin", defaultValue="2035-01-01") String datefin){
		
		HttpSession session = request.getSession();
		/*
		 * Il faut chercher le cycle et retourner une erreur s'il n'existe pas
		 */
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		if(etablissementConcerne == null ||  anneeScolaire == null){
			String error="";
			
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters1 = new HashMap<String, Object>();
			if(etablissementConcerne == null){
				error+="\n L'ETABLISSEMENT CONCERNE N'A PAS ETE RETROUVE";
			}
			if(anneeScolaire == null){
				error+="\n L'ANNEE SCOLAIRE CONCERNE N'A PAS ETE RETROUVE";
			}
			Collection<ErrorBean> collectionofErreurBean = 
					usersService.generateCollectionofErrorBean(error);
			
			parameters1.put("erreur", erreur);
			parameters1.put("datasource", collectionofErreurBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/errors/error.jasper");
			view.setApplicationContext(applicationContext);

			return new ModelAndView(view, parameters1);
		}
		
		System.out.println("on cherche les vraies donnees");
		
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
			parameters.put("LOGO", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		}

		
		
		/*
		 * On fixe les autres paramètres du PDF et on va aussi aller chercher les rapports par niveau
		 * Donc il faut le niveau et si le niveau rechercher est null alors on veut pour tous les niveaux
		 */
		Niveaux niveauConcerne = usersService.findNiveaux(idNiveauxConcerne);
		if(idPeriode.longValue()==0){
			SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
			try{
				Date date_min = spd.parse(datedebut);
				Date date_max = spd.parse(datefin);
				parameters.put("date1", datedebut);
				parameters.put("date2", datefin);
				parameters.put("periode", "");
				
				Collection<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = 
						usersService.generateListFicheRecapAbsenceNiveauBean(niveauConcerne, date_min, date_max);
				
				//System.out.println("taille de la liste des fiches = "+listofFicheRecapAbsenceCycleBean.size());
				
				
				parameters.put("datasource", listofFicheRecapAbsenceNiveauBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceNiveau.jasper");
				view.setApplicationContext(applicationContext);
				
				return new ModelAndView(view, parameters);
				
			}
			catch(Exception e){
				System.out.println("Erreur de conversion des dates "+datedebut+" et "+datefin+" et on doit "
						+ " lancer un pdf d'erreur "+e.getMessage());
				e.printStackTrace();
			}
		}
		else{
			/*
			 * A ce niveau cela signifie qu'il n'a pas choisi un intervalle de date mais juste une période bien 
			 * déterminer et enregistrer dans le système comme une séquence, un trimestre ou une 
			 * année scolaire
			 */
			System.out.println("on cherche les vraies donnees mais avec la periode");
			String periode = "";
			String lang = (String)session.getAttribute("lang");
			Sequences periodSequence = usersService.findSequences(idPeriode);
			Trimestres periodTrimestre = usersService.findTrimestres(idPeriode);
			Annee periodAnnee = usersService.findAnnee(idPeriode);
			
			if(periodSequence == null && periodTrimestre == null && periodAnnee == null) {
				
				String error="LA PERIODE SPECIFIE N'A PAS ETE RETROUVEE";
				
				String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
				Map<String, Object> parameters1 = new HashMap<String, Object>();
				
				Collection<ErrorBean> collectionofErreurBean = 
						usersService.generateCollectionofErrorBean(error);
				
				parameters1.put("erreur", erreur);
				parameters1.put("datasource", collectionofErreurBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/errors/error.jasper");
				view.setApplicationContext(applicationContext);

				return new ModelAndView(view, parameters1);
			}
			
			Collection<FicheRecapAbsenceNiveauBean> listofFicheRecapAbsenceNiveauBean = null;
			
			if(periodSequence !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Séquence "+periodSequence.getNumeroSeq(): 
					"Sequence "+periodSequence.getNumeroSeq();

				listofFicheRecapAbsenceNiveauBean = 
						usersService.generateListFicheRecapAbsenceNiveauSeqBean(niveauConcerne, periodSequence);
				
			}
			
			if(periodTrimestre !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Trimestre "+periodTrimestre.getNumeroTrim(): 
					"Term "+periodTrimestre.getNumeroTrim();
				
				listofFicheRecapAbsenceNiveauBean = 
						usersService.generateListFicheRecapAbsenceNiveauTrimBean(niveauConcerne, periodTrimestre);
				
			}
			
			if(periodAnnee !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?" "+periodAnnee.getIntituleAnnee(): 
					" "+periodAnnee.getIntituleAnnee();
				
				listofFicheRecapAbsenceNiveauBean = 
						usersService.generateListFicheRecapAbsenceNiveauAnBean(niveauConcerne, periodAnnee);
				
			}
			
			parameters.put("date1", "");
			parameters.put("date2", "");
			parameters.put("periode", periode);
			
			parameters.put("datasource", listofFicheRecapAbsenceNiveauBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceNiveau.jasper");
			view.setApplicationContext(applicationContext);
			
			return new ModelAndView(view, parameters);
			
			
		}
		
		System.out.println("aucun traitement");
		
		Map<String, Object> parameters_erreur = new HashMap<String, Object>();
		String message = "AUCUN TRAITEMENT EFFECTUE \n NO TREATMENT EFFECTS";
		parameters_erreur.put("erreur", message);
		parameters_erreur.put("datasource", null);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/compiled/errors/error.jasper");
		view.setApplicationContext(applicationContext);
		
		return new ModelAndView(view, parameters_erreur);
	}
	
	
	public void constructModelgetdonneesRabsClasse(Model model,	HttpServletRequest request){
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		if(listofNiveaux.size()>0){
			model.addAttribute("affichechoixcycle", "oui");
			model.addAttribute("listofNiveaux", listofNiveaux);
		}
		else{
			model.addAttribute("affichechoixcycle", "non");
		}
		
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			
		}
		
		List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
		model.addAttribute("listofTrimestreActif", listofTrimestreActif);
		
		List<Sequences> listofSequenceActif = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequenceActif", listofSequenceActif);
		
	}
	
	@GetMapping(path="/getdonneesRabsClasse")
	public String getdonneesRabsClasse(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesRabsClasse(model,	request);
		
		return "users/donneesRabsClasses";
	}
	
	@GetMapping(path="/exportRAbsencesClasses")
	public ModelAndView exportRAbsencesClasses(Model model, HttpServletRequest request,
			@RequestParam(name="idClasseConcerne", defaultValue="0") Long idClasseConcerne,
			@RequestParam(name="idPeriode", defaultValue="0") Long idPeriode,
			@RequestParam(name="datedebut", defaultValue="2019-01-01") String datedebut,
			@RequestParam(name="datefin", defaultValue="2035-01-01") String datefin){
		
		HttpSession session = request.getSession();
		/*
		 * Il faut chercher le cycle et retourner une erreur s'il n'existe pas
		 */
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		if(etablissementConcerne == null ||  anneeScolaire == null){
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			String error=" ";
			if(etablissementConcerne == null ){
				error+="\n ETABLISSEMENT NON RETROUVE ";
			}
			if(anneeScolaire == null){
				error+="\n ANNEE SCOLAIRE NON RETROUVE ";
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
			parameters.put("LOGO", logoetabDir+etablissementConcerne.getLogoEtab());
		}
		else{
			parameters.put("LOGO", "classpath:/static/images/logobekoko.png");
		}

		/*
		 * On fixe les autres paramètres du PDF et on va aussi aller chercher les rapports par classe
		 * Donc il faut la classe et si la classe rechercher est null alors on veut pour toutes les classes
		 */
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		
		if(idPeriode.longValue()==0){

			SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
			try{
				Date date_min = spd.parse(datedebut);
				Date date_max = spd.parse(datefin);
				parameters.put("date1", datedebut);
				parameters.put("date2", datefin);
				parameters.put("periode", "");
				
				Collection<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = 
						usersService.generateListFicheRecapAbsenceClasseBean(classeConcerne, date_min, date_max);
				
				//System.out.println("taille de la liste des fiches = "+listofFicheRecapAbsenceCycleBean.size());
				
				
				parameters.put("datasource", listofFicheRecapAbsenceClasseBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceClasse.jasper");
				view.setApplicationContext(applicationContext);
				
				return new ModelAndView(view, parameters);
				
			}
			catch(Exception e){
				System.out.println("Erreur de conversion des dates "+datedebut+" et "+datefin+" et on doit "
						+ " lancer un pdf d'erreur "+e.getMessage());
				e.printStackTrace();
			}
		
		}
		else{

			/*
			 * A ce niveau cela signifie qu'il n'a pas choisi un intervalle de date mais juste une période bien 
			 * déterminer et enregistrer dans le système comme une séquence, un trimestre ou une 
			 * année scolaire
			 */
			
			String periode = "";
			String lang = (String)session.getAttribute("lang");
			Sequences periodSequence = usersService.findSequences(idPeriode);
			Trimestres periodTrimestre = usersService.findTrimestres(idPeriode);
			Annee periodAnnee = usersService.findAnnee(idPeriode);
			
			if(periodSequence == null && periodTrimestre == null && periodAnnee == null) {
				

				String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
				Map<String, Object> parameters1 = new HashMap<String, Object>();
				String error=" LA PERIODE INDIQUE N'EST PAS VALIDE";
				
				Collection<ErrorBean> collectionofErreurBean = 
						usersService.generateCollectionofErrorBean(error);
				
				parameters1.put("erreur", erreur);
				parameters1.put("datasource", collectionofErreurBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/errors/error.jasper");
				view.setApplicationContext(applicationContext);

				return new ModelAndView(view, parameters1);
			
			}
			
			Collection<FicheRecapAbsenceClasseBean> listofFicheRecapAbsenceClasseBean = null;
			
			if(periodSequence !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Séquence "+periodSequence.getNumeroSeq(): 
					"Sequence "+periodSequence.getNumeroSeq();

				listofFicheRecapAbsenceClasseBean = 
						usersService.generateListFicheRecapAbsenceClasseSeqBean(classeConcerne, periodSequence);
				
			}
			
			if(periodTrimestre !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Trimestre "+periodTrimestre.getNumeroTrim(): 
					"Term "+periodTrimestre.getNumeroTrim();
				
				listofFicheRecapAbsenceClasseBean = 
						usersService.generateListFicheRecapAbsenceClasseTrimBean(classeConcerne, periodTrimestre);
				
			}
			
			if(periodAnnee !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?" "+periodAnnee.getIntituleAnnee(): 
					" "+periodAnnee.getIntituleAnnee();
				
				listofFicheRecapAbsenceClasseBean = 
						usersService.generateListFicheRecapAbsenceClasseAnBean(classeConcerne, periodAnnee);
				
			}
			
			parameters.put("date1", "");
			parameters.put("date2", "");
			parameters.put("periode", periode);
			
			parameters.put("datasource", listofFicheRecapAbsenceClasseBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/fiches/FicheRecapAbsenceClasse.jasper");
			view.setApplicationContext(applicationContext);
			
			return new ModelAndView(view, parameters);
			
		}
		
		
		return null;
	}
	
	public void constructModelgetdonneesStatMoyClasse(Model model,	HttpServletRequest request){
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		String usernameConnect = request.getUserPrincipal().getName();
		
		Utilisateurs usersConnect = usersService.findByUsername(usernameConnect);
		
		model.addAttribute("usersConnect", usersConnect);
		
		if(usersService.hasRole(usersConnect, "CENSEUR")==false){
			if(usersService.hasRole(usersConnect, "TITULAIRE")==true){
				listofNiveaux = usersService.findAllNiveauxDirigesEns(usersConnect.getIdUsers());
			}
		}
		else{
			//Donc il a le role censeur donc c'est un patron
			model.addAttribute("patron", usersConnect);
		}
		
		if(listofNiveaux.size()>0){
			model.addAttribute("affichechoixcycle", "oui");
			model.addAttribute("listofNiveaux", listofNiveaux);
		}
		else{
			model.addAttribute("affichechoixcycle", "non");
		}
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
		}
		
		List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
		model.addAttribute("listofTrimestreActif", listofTrimestreActif);
		
		List<Sequences> listofSequenceActif = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequenceActif", listofSequenceActif);
		
	}
	
	@GetMapping(path="/getdonneesStatMoyClasse")
	public String getdonneesStatMoyClasse(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesStatMoyClasse(model,	request);
		
		return "users/donneesStatMoyClasse";
	}
	
	public void constructModelgetdonneesStatNoteCoursPeriode(Model model,	HttpServletRequest request){

		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		String usernameConnect = request.getUserPrincipal().getName();
		
		Utilisateurs usersConnect = usersService.findByUsername(usernameConnect);
		
		model.addAttribute("usersConnect", usersConnect);
		
		if(listofNiveaux.size()>0){
			model.addAttribute("affichechoixcycle", "oui");
			model.addAttribute("listofNiveaux", listofNiveaux);
		}
		else{
			model.addAttribute("affichechoixcycle", "non");
		}
		
		Annee anneeActive = usersService.findAnneeActive();
		
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
		}
		
		List<Trimestres> listofTrimestreActif = usersService.findAllActiveTrimestre(anneeActive.getIdPeriodes());
		model.addAttribute("listofTrimestreActif", listofTrimestreActif);
		
		List<Sequences> listofSequenceActif = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
		model.addAttribute("listofSequenceActif", listofSequenceActif);
		
	
	}
	
	@GetMapping(path="/getdonneesStatNoteCoursPeriode")
	public String getdonneesStatNoteCoursPeriode(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesStatNoteCoursPeriode(model,	request);
		
		return "users/donneesStatNoteCoursPeriode";
	}
	
	
	
	
	
/////////////////////////// FIN DES REQUETES DE TYPES GET ///////////////////////////
	
/////////////////////////////////DEBUT DES REQUETES DE TYPES POST ///////////////////////////////////////
	
	
	@PostMapping(path="/postupdatePassword")
	public String updatePassword(@Valid ModifpasswordForm modifpasswordForm, 
			BindingResult bindingResult, Model model, 
			HttpServletRequest request, HttpServletResponse response) {


		

		if (bindingResult.hasErrors()) {
			return "users/indexUsers";
		}

		/*
		 * Suite d'instruction permettant de modifier le mot de passe
		 */
		int reponse=adminService.updatePassword(modifpasswordForm.getPasswordCourant(), 
				modifpasswordForm.getNewPassword(), 
				modifpasswordForm.getNewPasswordConfirm(), 
				modifpasswordForm.getUsername());

		if(reponse == -2) {
			//username invalide

			return "redirect:/logesco/users/index?usernameerreur";
		}
		if(reponse == -1) {
			return "redirect:/logesco/users/index?passwordnotconfirm";
		}
		if(reponse == 0) {
			return "redirect:/logesco/users/index?activepassworderror";
		}

		/*
		 * On deconnecte proprement l'utilisateur pour qu'il se reconnecte avec son 
		 * nouveau mot de passe. Pour cela on recupere son authentification qui 
		 * normalement ne doit pas être null et là on peut grace a logout le 
		 * deconnecter proprement
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		return "redirect:/login?updatepassword";

	}
	
	@PostMapping(path="/postlistprocesverbalEvalSeq")
	public String postlistprocesverbalEvalSeq( @Valid @ModelAttribute("getrapportEvalSeqForm") 
		GetrapportEvalSeqForm getrapportEvalSeqForm,	BindingResult bindingResult,	Model model, 
		HttpServletRequest request, HttpServletResponse response) 
				throws ParseException, Exception{
		
		/*
		 * Il faut effectuer la liste des cours qui passe dans la classe
		 */
		/*//System.out.println("On veut les cours de la classe "+getrapportEvalSeqForm.getIdclasseRapport());
		//System.out.println("Avec les evaluations de la séquence "+getrapportEvalSeqForm.getIdsequenceRapport());*/
		
		List<Cours> listcoursofClasses = usersService.getListCoursClasse(getrapportEvalSeqForm.getIdclasseRapport());
		
		/*
		 * Il faut pour chaque cours la liste de ses évaluations pour la séquence indiquée
		 * Cette liste ne peut être automatiquement chargé puisque les cours ne se charge pas directement 
		 * avec la liste des évaluations qui lui sont liées
		 */
		List<Evaluations> listofEvalSeq = usersService.getListEvalAllCoursClasseSeq(getrapportEvalSeqForm.getIdclasseRapport(), 
				getrapportEvalSeqForm.getIdsequenceRapport());
		
		Classes classeRapport = usersService.findClasses(getrapportEvalSeqForm.getIdclasseRapport());
		Sequences seqRapport = usersService.findSequences(getrapportEvalSeqForm.getIdsequenceRapport());
		HttpSession session=request.getSession();
		
		if(classeRapport == null || seqRapport == null) {
			return "redirect:/logesco/users/getlistprocesverbalEvalSeq?listprocesverbalEvalSeqerror"; 
		}
		
		String classesname = ""+classeRapport.getCodeClasses()+" "+classeRapport.getSpecialite().getCodeSpecialite()+classeRapport.getNumeroClasses();
		session.setAttribute("classeRapportSeq", classesname);
		session.setAttribute("classeRapport", classeRapport);
		session.setAttribute("sequenceRapportSeq", seqRapport.getNumeroSeq());
		if(listcoursofClasses != null) {
			session.setAttribute("listcoursofClassesRapportSeq", listcoursofClasses);
			session.setAttribute("listofEvalSeqRapportSeq", listofEvalSeq);
			session.setAttribute("numeroseqdeRapportSeq", getrapportEvalSeqForm.getIdsequenceRapport());
			//On retire l'attribut erreurcoursRapportSeq de la session pour qu'elle ne s'affiche pas. 
			session.removeAttribute("erreurcoursRapportSeq");
		}
		else{
			/*
			 * Il faut retirer les éléments listcoursofClassesRapportSeq, listofEvalSeqRapportSeq et numeroseqdeRapportSeq
			 * de la session ainsi si les données d'une autre classe était déjà chargé alors le tableau ne s'affichera pas puisque 
			 * les données de la classe sélectionnée n'existe pas encore dans la BD.
			 */
			session.removeAttribute("listcoursofClassesRapportSeq");
			session.removeAttribute("listofEvalSeqRapportSeq");
			session.removeAttribute("numeroseqdeRapportSeq");
			session.setAttribute("erreurcoursRapportSeq", "Aucun cours n'est enregistré dans cette classe");
		}
		
		return "redirect:/logesco/users/getlistprocesverbalEvalSeq?listprocesverbalEvalSeqsuccess";
	}
	
	
	@PostMapping(path="/postlistprocesverbalEvalTrim")
	public String postlistprocesverbalEvalTrim( @Valid @ModelAttribute("getrapportEvalTrimForm") 
		GetrapportEvalTrimForm getrapportEvalTrimForm,	BindingResult bindingResult,	Model model, 
		HttpServletRequest request, HttpServletResponse response) 
				throws ParseException, Exception{
		
		HttpSession session=request.getSession();
		/*
		 * Il faut effectuer la liste des cours qui passe dans la classe
		 */
		/*//System.out.println("On veut les cours de la classe "+getrapportEvalSeqForm.getIdclasseRapport());
		//System.out.println("Avec les evaluations de la séquence "+getrapportEvalSeqForm.getIdsequenceRapport());*/
		List<Cours> listcoursofClasses = usersService.getListCoursClasse(getrapportEvalTrimForm.getIdclasseRapport());
		
		Trimestres trimestreConcerne = usersService.findTrimestres(getrapportEvalTrimForm.getIdtrimestreRapport());
		
		if(trimestreConcerne == null) {
			return "redirect:/logesco/users/getlistprocesverbalEvalTrim?listprocesverbalEvalTrimerror"; 
		}
		
		List<Sequences> listofSeqTrim = (List<Sequences>) trimestreConcerne.getListofsequence();
		List<Evaluations> listofEvaltrim = new ArrayList<Evaluations>();
		
		for(Sequences seq : listofSeqTrim){
			List<Evaluations> listofEvalSeq = usersService.getListEvalAllCoursClasseSeq(getrapportEvalTrimForm.getIdclasseRapport(), 
					seq.getIdPeriodes());
			//System.out.println("taillesss taillesss "+listofEvalSeq.size());
			listofEvaltrim.addAll(listofEvalSeq);
		}
		
		//System.out.println("taille taille "+listofEvaltrim.size());
		
		
		Classes classeRapport = usersService.findClasses(getrapportEvalTrimForm.getIdclasseRapport());
		Trimestres trimRapport = usersService.findTrimestres(getrapportEvalTrimForm.getIdtrimestreRapport());
		
		if(classeRapport == null || trimRapport == null) {
			return "redirect:/logesco/users/getlistprocesverbalEvalTrim?listprocesverbalEvalTrimerror"; 
		}
		
		String classesname = ""+classeRapport.getCodeClasses()+" "+classeRapport.getSpecialite().getCodeSpecialite()+classeRapport.getNumeroClasses();
		session.setAttribute("classeRapportTrim", classesname);
		session.setAttribute("trimestreRapportTrim", trimRapport.getNumeroTrim());
		
		if(listcoursofClasses != null) {
			session.setAttribute("listcoursofClassesRapportTrim", listcoursofClasses);
			session.setAttribute("numerotrimdeRapportTrim", getrapportEvalTrimForm.getIdtrimestreRapport());
			session.setAttribute("listofEvaltrim", listofEvaltrim);
			//On retire l'attribut erreurcoursRapportTrim de la session pour qu'elle ne s'affiche pas. 
			session.removeAttribute("erreurcoursRapportTrim");
		}
		else{
			/*
			 * Il faut retirer les éléments listcoursofClassesRapportSeq, listofEvalSeqRapportSeq et numeroseqdeRapportSeq
			 * de la session ainsi si les données d'une autre classe était déjà chargé alors le tableau ne s'affichera pas puisque 
			 * les données de la classe sélectionnée n'existe pas encore dans la BD.
			 */
			session.removeAttribute("listcoursofClassesRapportTrim");
			session.removeAttribute("numerotrimdeRapportTrim");
			session.removeAttribute("listofEvaltrim");
			session.setAttribute("erreurcoursRapportTrim", "Aucun cours n'est enregistré dans cette classe");
		}
		
		return "redirect:/logesco/users/getlistprocesverbalEvalTrim?listprocesverbalEvalTrimsuccess";
	}
	
	
	
///////////////////////////////// FIN DES REQUETES DE TYPE POST /////////////////////////////////////////////
	
	
	
}
