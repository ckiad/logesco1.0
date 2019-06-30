/**
 * 
 */
package org.logesco.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.logesco.entities.Annee;
import org.logesco.entities.Classes;
import org.logesco.entities.Eleves;
import org.logesco.entities.Etablissement;
import org.logesco.entities.Niveaux;
import org.logesco.modeles.Recu_versement;
import org.logesco.services.IIntendantService;
import org.logesco.services.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

/**
 * @author cedrickiadjeu
 *
 */
@Controller
@RequestMapping(path="/logesco/users/intendant")
public class IntendantController {
	@Value("${dir.emblemes.logo}")
	private String logoetabDir;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private IUsersService usersService;
	
	@Autowired
	private IIntendantService intendantService;
	
	
////////////////////////////////////DEBUT DES REQUETES DE TYPE GET ////////////////////////////////////////////
	public void constructModelInscriptionEleves(Model model, HttpServletRequest request, int numPageEleves,
			 long idClasseSelect){
		
		HttpSession session=request.getSession();
		/*
		 * Il faut faire la liste des classes et placer dans le model puisqu'on 
		 * va s'en servir pour les etiquetes des classes et cette liste se fait lorsque la page se charge
		 * c'est à dire lorsque idClasseSelect==-1 ou montantScolarite==0
		 */
			List<Classes> listofClasses=usersService.findListClasse();
			
			model.addAttribute("listofClasses", listofClasses);
			
			List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
			model.addAttribute("listofNiveaux", listofNiveaux);
			
			/*
			 * Ici on est sur que la page est déjà chargé et qu'on veut en fait la liste des élèves d'une classe particulière 
			 * page par page
			 */
			Long idClasseAEnvoyer=new Long(idClasseSelect);
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
			
			//System.err.println("Voici la classe choisi "+classeSelect);
			
			/*
			 * Il faut vérifier si le montant de la scolarité pour les élèves de la classe selectionnée
			 * est déjà spécifié. Sinon on doit plutôt signaler une erreur pour exiger la configuration
			 * Il est configuré lorsqu'il est strictement supérieur à 0
			 */
			if(classeSelect != null){
				//System.err.println("Voici la classe choisi "+classeSelect.getMontantScolarite());
				if(classeSelect.getMontantScolarite() > 0){
					
				
				/*
				 * Il faut maintenant la liste des élèves de cette classe
				 */
				int effectifprovClasse=usersService.getEffectifProvisoireClasse(idClasseSelect);
				model.addAttribute("effectifprovClasse", effectifprovClasse);
				
				//On garde le numpageeleve dans la session puisqu'apres modification il va falloir revenir sur la page qui etait affiche
				session.setAttribute("numPageEleves", numPageEleves);
				
				Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClasseAEnvoyer,	
						numPageEleves, 10);
				
				List<Eleves> listofAllEleves = usersService.findListElevesClasse(idClasseAEnvoyer);
				model.addAttribute("listofAllEleves", listofAllEleves);
				
				//System.err.println("liste des élèves dans la liste "+pageofEleves.getContent().size());
				
				if(pageofEleves != null){
					if(pageofEleves.getContent().size()!=0){
						model.addAttribute("listofEleves", pageofEleves.getContent());
						int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
							
						model.addAttribute("listofPagesEleves", listofPagesEleves);
							
						model.addAttribute("pageCouranteEleves", numPageEleves);
						//System.err.println("la liste des élève contient "+pageofEleves.getContent().size());
					}
					else{
						//System.err.println("cette classe ne contient encore aucun élève");
						model.addAttribute("erreurClasseVide", "erreurClasseVide");
					}
			    }
			}
			else{
				model.addAttribute("erreurClasseNonConfig", "erreurClasseNonConfig");
			}
		}
	}
	
	@GetMapping(path="/getinscriptionEleves")
	public String getinscriptionEleves(Model model, HttpServletRequest request,	
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect) throws ParseException{
		
		/*
		 * Il faut la liste des classes dans le modèle et puis lors du clic sur le bouton recherche on doit 
		 * afficher la liste des élèves de la classe choisi avec un bouton permettant d'inscrire un élève ou d'annuler une inscription
		 */
		this.constructModelInscriptionEleves(model, request, numPageEleves, idClasseSelect);
		
		return "users/inscrireEleves";
	}
	
	@GetMapping(path="/versementSco")
	public String versementSco(Model model, HttpServletRequest request,	
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="montantAVerse", defaultValue="0") double montantAVerse,
			@RequestParam(name="idEleveAModif", defaultValue="0") long idEleveAModif,
			@RequestParam(name="idClasseSelect", defaultValue="-1") long idClasseSelect) throws ParseException{
		
		HttpSession session=request.getSession();
		/*
		 * Il faut rechercher l'élève dont on va modifier l'état d'inscription au cas où le montant 
		 * deja versé de sa scolarité atteint  le montant de la scolarité. 
		 */
		//System.err.println("Appel à la fonction d'enregistrement de versement ");
		
		Long repServeur = intendantService.enregVersementSco(idEleveAModif, montantAVerse);
		
		
		 System.out.println("Le montant versé est enregistré et il faut à présent sortir  un reçu qui approuve "
		 		+ "ce versement. Ce reçu ne sort que si l'enregistrement s'est bien passé c'est  pourquoi dans le code"
		 		+ " on va d'abord interprété les cas ou l'opération s'est mal passé avant d'appelerla fonction qui"
		 		+ " impremera le reçu de versement donc apres repServeur == -1 et repServeur == -2  "
		 		+ " Mais la fonction enregVersementSco retourne "+repServeur);
		 
		
		System.out.println("idEleveAModif=="+idEleveAModif+"  montantAVerse=="+montantAVerse
				+" repServeur=="+repServeur);
		
		if(repServeur == -1) return "redirect:/logesco/users/intendant/getinscriptionEleves?updateetatInsceleveserror"
				+ "&&idClasseSelect="+idClasseSelect
				+ "&&numPageEleves="+numPageEleves;
		
		if(repServeur == -2) return "redirect:/logesco/users/intendant/getinscriptionEleves?updateetatInsceleveserrorMt"
				+ "&&idClasseSelect="+idClasseSelect
				+ "&&numPageEleves="+numPageEleves;
		
		/*
		 * System.err.println("Comme indiqué plus haut, on va donc maintenant appeler la fonction qui 
		 * va imprimé le reçu de versement car ici on est sur que le versement s'est bien effectué");
		 */
		
		/*
		 * on place en session l'operation qui vient d'etre realiser et l'eleve concerne par cette operation
		 * On placera aussi dans la session une indication d'une opération pas encore imprime
		 * Ainsi, au rechargement de la page, le bouton d'impression va apparaitre pour que cette 
		 * dernière opération puisse être imprimé. Si il effectue plus d'une opération sans impression, 
		 * c'est toujours la toute derniere operation qui sera imprimé. mais il peut retrouver une opération
		 * effectué sur un élève quelconque du moment ou il connait son nom et sa classe. 
		 * On va donc appeler la fonction qui retourne la dernière opération sur un compteInscription donne
		 */
		session.setAttribute("idEleveConcerne", idEleveAModif);
		
		//System.out.println(" L'operation realise a pour identifiant "+repServeur);
		session.setAttribute("idOperation_a_imprimer", repServeur);
		session.setAttribute("operation_presente", "1");
		
		
		
		return "redirect:/logesco/users/intendant/getinscriptionEleves?updateetatInscelevessuccess"
				+ "&&idClasseSelect="+idClasseSelect
				+ "&&numPageEleves="+numPageEleves;
		
		
		
	}
	
	/*
	 * On va ici construire le recu de versement en pdf qui sera imprimé avec une imprimante matricielle
	 * pour eviter un gaspillage de papier car ce recu ne tient pas sur un format entier
	 */
	@GetMapping(path="/lancerEditionsRecuVersement")
	public ModelAndView lancerEditionsRecuVersement(Model model, HttpServletRequest request,
			@RequestParam(name="idEleveConcerne", defaultValue="0") long idEleveConcerne,
			@RequestParam(name="idOperation_a_imprimer", defaultValue="0") long idOperation_a_imprimer){
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		if(etablissementConcerne == null ||  anneeScolaire == null ){
			return null;
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
		
		Date dateJour = new Date();
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");//"dd-MM-yyyy"
		String dateString = spd.format(dateJour);
		parameters.put("date_jour", dateString);
		
		String numero_recu=intendantService.getIdentifiantOperation(idOperation_a_imprimer);
		parameters.put("numero_recu", numero_recu);
		
		double montantTransaction = intendantService.getMontantOperation(idOperation_a_imprimer);
		parameters.put("montant", montantTransaction+" F cfa");
		
		Eleves eleveConcerne = usersService.findEleves(idEleveConcerne);
		if(eleveConcerne==null){
			return null;
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
	
	
	
	
	@GetMapping(path="/getrapportVersement")
	public String getrapportVersement(Model model, HttpServletRequest request,
			@RequestParam(name="idClassesConcerne", defaultValue="0") long idClassesConcerne) throws ParseException{
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		/*
		 * On va rechercher la classe selectionné car on doit la placer aussi dans le model
		 */
		Classes classeConcerne = usersService.findClasses(idClassesConcerne);
		
		List<Eleves> listofElevesDeClasse =usersService.findListElevesClasse(classeConcerne.getIdClasses());
		
		HttpSession session=request.getSession();
		
		
		session.setAttribute("etablissementConcerne", etablissementConcerne);
		session.setAttribute("classeConcerne", classeConcerne);
		session.setAttribute("listofElevesDeClasse", listofElevesDeClasse);
		
		return "users/rapportversementSco";
	}
	
////////////////////////////////////FIN DES REQUETES DE TYPE GET ////////////////////////////////////////////	

////////////////////////////////////DEBUT DES REQUETES DE TYPE POST ////////////////////////////////////////////
	
////////////////////////////////////FIN DES REQUETES DE TYPE POST ////////////////////////////////////////////		
	
}
