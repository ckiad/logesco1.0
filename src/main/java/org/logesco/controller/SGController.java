/**
 * 
 */
package org.logesco.controller;

import java.io.File;
import java.text.SimpleDateFormat;
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
import org.logesco.entities.SanctionDisciplinaire;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.modeles.ErrorBean;
import org.logesco.modeles.FicheAbsenceEleveBean;
import org.logesco.services.ISGService;
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
@RequestMapping(path="/logesco/users/sg")
public class SGController {
	
	@Value("${dir.emblemes.logo}")
	private String logoetabDir;
	
	@Autowired
	private IUsersService usersService;
	
	@Autowired
	private ISGService sgService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 
	 */
////////////////////////////////////DEBUT DES REQUETES DE TYPE GET ////////////////////////////////////////////
	
	public void constructModelgetdonneesSaisieRAbsences(Model model,	HttpServletRequest request){
		
		/*
		 * On place la liste des niveaux dans le modele sachant qu'on retrouvera les classes
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		
		Annee anneeActive = usersService.findAnneeActive();
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			/*
			 * On place la liste des séquences de l'année active
			 */
			List<Sequences> listofSequence = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
			model.addAttribute("listofSequenceActive", listofSequence);
		}
		
		
	}
	
	
	@GetMapping(path="/getdonneesSaisieRAbsences")
	public String getdonneesSaisieRAbsences(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesSaisieRAbsences(model,	request);
		
		return "users/donneesSaisieRAbsences";
	}
	
	public void constructModelgetdonneesSaisieRDiscipline(Model model,	HttpServletRequest request){
		
		/*
		 * On place la liste des niveaux dans le modele sachant qu'on retrouvera les classes
		 */
		List<Niveaux> listofNiveaux = usersService.findAllNiveaux();
		
		model.addAttribute("listofNiveaux", listofNiveaux);
		
		
		Annee anneeActive = usersService.findAnneeActive();
		if(anneeActive != null) {
			model.addAttribute("anneeActive", anneeActive);
			/*
			 * On place la liste des séquences de l'année active
			 */
			List<Sequences> listofSequence = usersService.findAllSequenceActive(anneeActive.getIdPeriodes());
			model.addAttribute("listofSequenceActive", listofSequence);
		}
		
		
	}
	
	@GetMapping(path="/getdonneesSaisieRDiscipline")
	public String getdonneesSaisieRDiscipline(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesSaisieRDiscipline(model,	request);
		
		return "users/donneesSaisieRDiscipline";
	}
	
	
	
	
	public void constructModelgetformSaisieRAbsences(Model model,	HttpServletRequest request, Long idSequenceConcerne,  Long idClassesConcerne,  
			Long idEleves,  int numPageEleves, int taillePage ){
		
		//System.out.println("constructModelgetformSaisieRAbsences");
		/*
		 * Il faut donc la liste de tous les élèves de la classe dans le modele dans le même ordre qu'ils vont apparaitre dans les pages
		 */
		List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClassesConcerne);
		
		if(listofAllEleve != null){
			model.addAttribute("effectifTotal", listofAllEleve.size());
			
			if((listofAllEleve.size() > 0)){
				model.addAttribute("listofAllEleve", listofAllEleve);
				//System.out.println("La liste des élèves n'est pas vide");
				Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
				Classes classeConcerne = usersService.findClasses(idClassesConcerne);
				
				if(sequenceConcerne!=null && classeConcerne!=null){
					model.addAttribute("sequenceConcerneRabs", sequenceConcerne);
					//System.out.println("sequenceConcerneRabs "+sequenceConcerne.getNumeroSeq());
					model.addAttribute("classeConcerne", classeConcerne);
					
					model.addAttribute("affichechoixclasse", "oui");
					//System.out.println("affichechoixclasse= oui");
				}
				else{
					model.addAttribute("affichechoixclasse", "non");
				}
				
			}
			
		}
		
	}
	
	
	
	@GetMapping(path="/getformSaisieRAbsences")
	public String getformSaisieRAbsences(
			Model model, HttpServletRequest request,
			@RequestParam(name="idSequenceConcerne", defaultValue="-1") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne,
			@RequestParam(name="idEleves", defaultValue="0") Long idEleves,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
	
		this.constructModelgetformSaisieRAbsences(model,	request, idSequenceConcerne, idClassesConcerne,  
				idEleves, numPageEleves, taillePage);
		
		return "users/formSaisieRAbsences";
	}
	
	public void constructModelgetdonneesAbsClasse(Model model,	HttpServletRequest request){
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
	
	@GetMapping(path="/getdonneesAbsClasse")
	public String getdonneesAbsClasse(Model model, HttpServletRequest request){
		
		this.constructModelgetdonneesAbsClasse(model,	request);
		
		return "users/donneesAbsClasses";
	}
	
	@GetMapping(path="/exportAbsencesClasses")
	public ModelAndView exportAbsencesClasses(Model model, HttpServletRequest request,
			@RequestParam(name="idClasseConcerne", defaultValue="0") Long idClasseConcerne,
			@RequestParam(name="idPeriode", defaultValue="0") Long idPeriode,
			@RequestParam(name="datedebut", defaultValue="2019-01-01") String datedebut,
			@RequestParam(name="datefin", defaultValue="2035-01-01") String datefin){
		
		HttpSession session = request.getSession();
		
		Etablissement etablissementConcerne = usersService.getEtablissement();
		Annee anneeScolaire = usersService.findAnneeActive();
		Classes classeConcerne = usersService.findClasses(idClasseConcerne);
		
		if(etablissementConcerne == null ||  anneeScolaire == null ||  classeConcerne == null){
			String erreur = "LISTE DES ERREURS RENCONTREES: CONTACTER L'ADMINISTRATEUR.";
			Map<String, Object> parameters = new HashMap<String, Object>();
			String error=" ";
			if(etablissementConcerne == null ){
				error+="\n ETABLISSEMENT NON RETROUVE ";
			}
			if(anneeScolaire == null){
				error+="\n ANNEE SCOLAIRE NON RETROUVE ";
			}
			if(classeConcerne == null){
				error+="\n CLASSE NON RETROUVE ";
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
		
		parameters.put("classe", classeConcerne.getClasseString());
		
		if(idPeriode.longValue()==0){


			SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
			try{
				Date date_min = spd.parse(datedebut);
				Date date_max = spd.parse(datefin);
				parameters.put("date1", datedebut);
				parameters.put("date2", datefin);
				parameters.put("periode", "");
				
				Collection<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = 
						sgService.generateListFicheAbsenceEleveBean(classeConcerne, date_min, date_max);
				
				//System.out.println("taille de la liste des fiches = "+listofFicheRecapAbsenceCycleBean.size());
				
				
				parameters.put("datasource", listofFicheAbsenceEleveBean);
				JasperReportsPdfView view = new JasperReportsPdfView();
				view.setUrl("classpath:/reports/compiled/fiches/FicheAbsenceEleve.jasper");
				view.setApplicationContext(applicationContext);
				
				return new ModelAndView(view, parameters);
				
			}
			catch(Exception e){
				System.out.println("Erreur de conversion des dates "+datedebut+" et "+datefin+" et on doit "
						+ " lancer un pdf d'erreur "+e.getMessage());
				//e.printStackTrace();
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
			
			Collection<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = null;
			
			if(periodSequence !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Séquence "+periodSequence.getNumeroSeq(): 
					"Sequence "+periodSequence.getNumeroSeq();

				listofFicheAbsenceEleveBean = 
						sgService.generateListFicheAbsenceEleveSeqBean(classeConcerne, periodSequence);
				
			}
			
			if(periodTrimestre !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?"Trimestre "+periodTrimestre.getNumeroTrim(): 
					"Term "+periodTrimestre.getNumeroTrim();
				
				listofFicheAbsenceEleveBean = 
						sgService.generateListFicheAbsenceEleveTrimBean(classeConcerne, periodTrimestre);
				
			}
			
			if(periodAnnee !=null) {
				periode = lang.equalsIgnoreCase("fr")==true?" "+periodAnnee.getIntituleAnnee(): 
					" "+periodAnnee.getIntituleAnnee();
				
				listofFicheAbsenceEleveBean = 
						sgService.generateListFicheAbsenceEleveAnBean(classeConcerne, periodAnnee);
				
			}
			
			parameters.put("date1", "");
			parameters.put("date2", "");
			parameters.put("periode", periode);
			
			parameters.put("datasource", listofFicheAbsenceEleveBean);
			JasperReportsPdfView view = new JasperReportsPdfView();
			view.setUrl("classpath:/reports/compiled/fiches/FicheAbsenceEleve.jasper");
			view.setApplicationContext(applicationContext);
			
			return new ModelAndView(view, parameters);
			
		
		}
		
		return null;
	}
	
	public void constructModelgetformSaisieRDiscipline(Model model,	HttpServletRequest request, Long idSequenceConcerne,  
			Long idClassesConcerne,   int numPageEleves, int taillePage ){
		
		HttpSession session = request.getSession();
		/*Long idEleves, 
		 * Il faut donc la liste de tous les élèves de la classe dans le modele dans le même ordre qu'ils vont apparaitre dans les pages
		 */
		List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClassesConcerne);
		
		if(listofAllEleve != null){
			model.addAttribute("effectifTotal", listofAllEleve.size());
			
			if((listofAllEleve.size() > 0)){
				model.addAttribute("listofAllEleve", listofAllEleve);
				//System.out.println("La liste des élèves n'est pas vide");
				Sequences sequenceConcerne = usersService.findSequences(idSequenceConcerne);
				Classes classeConcerne = usersService.findClasses(idClassesConcerne);
				
				model.addAttribute("idSequenceConcerne", idSequenceConcerne);
				model.addAttribute("idClassesConcerne", idClassesConcerne);
				
				//Il faut faire la liste des sanctionDisciplinaire enregistrées dans le système car le choix doit fait parmi ces sanctions
				List<SanctionDisciplinaire> listofSanctionDisc = usersService.findListAllSanctionDisciplinaire();
				
				if(sequenceConcerne!=null && classeConcerne!=null && listofSanctionDisc!=null){
					
					//////////////////////////////////////////////////////////////////
					Page<Eleves> pageofEleves=usersService.findPageElevesClasse(idClassesConcerne,	
							numPageEleves, 10);
					if(pageofEleves.getContent().size()!=0){
						model.addAttribute("listofEleves", pageofEleves.getContent());
						int[] listofPagesEleves=new int[pageofEleves.getTotalPages()];
							
						model.addAttribute("listofPagesEleves", listofPagesEleves);
							
						model.addAttribute("pageCouranteEleves", numPageEleves);
						//System.out.println("la liste des élève contient "+pageofEleves.getContent().size());
					}
					//////////////////////////////////////////////////////////////////
					
					model.addAttribute("sequenceConcerneRabs", sequenceConcerne);
					
					model.addAttribute("classeConcerne", classeConcerne);
					
					model.addAttribute("affichechoixclasse", "oui");
					
					model.addAttribute("listofSanctionDisc", listofSanctionDisc);
					session.setAttribute("listofSanctionDisc", listofSanctionDisc);
					
					if(listofSanctionDisc.size()<=0){
						model.addAttribute("sanctionDiscVide", listofSanctionDisc);
					}
					else{
						model.addAttribute("sanctionDisc", listofSanctionDisc);
					}
					
				}
				else{
					model.addAttribute("affichechoixclasse", "non");
				}
				
			}
			
		}
		
		
	}
	
	@GetMapping(path="/getformSaisieRDiscipline")
	public String getformSaisieRDiscipline(
			Model model, HttpServletRequest request,
			@RequestParam(name="idSequenceConcerne", defaultValue="-1") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne,
			@RequestParam(name="numPageEleves", defaultValue="0") int numPageEleves,
			@RequestParam(name="taillePage", defaultValue="5") int taillePage){
		
	
		this.constructModelgetformSaisieRDiscipline(model,	request, idSequenceConcerne, idClassesConcerne,  
				 numPageEleves, taillePage);
		
		return "users/formSaisieRDiscipline";
	}
	
	
	@GetMapping(path="/getUpdateRAbsence")
	public String getUpdateRAbsence(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEleves[]", defaultValue="0") Long[] idEleves,
			@RequestParam(name="nbreheureJustifie[]", defaultValue="0") String[] nbreheureJustifie,
			@RequestParam(name="nbreheureNJustifie[]", defaultValue="0") String[] nbreheureNJustifie,
			@RequestParam(name="idSequenceConcerne", defaultValue="0") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne,
			@RequestParam(name="dateenreg") String dateenreg){
		

		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		
		//List<Eleves> listofAllEleve = usersService.findListElevesClasse(idClassesConcerne);
		
		int[] nbreHJustifie = new int[nbreheureJustifie.length];
		int[] nbreHNJustifie = new int[nbreheureNJustifie.length];
		
		try{
			Date date_enreg = spd.parse(dateenreg);
			Date date_defaut = spd.parse("2018-01-01");
			//Boucle de conversion afin de lancer tout lorsqu'on est sur ou d'en annuler tout
			for(int i=0; i<idEleves.length; i++){
				
				nbreHJustifie[i] = Integer.parseInt(nbreheureJustifie[i]);
				nbreHNJustifie[i] = Integer.parseInt(nbreheureNJustifie[i]);
				
				
			}
			//Boucle d'enregistrement puisque tout etant converti on peut tout enregistrer a la fois
			int ret = 0;
			for(int i=0; i<idEleves.length; i++){
				
				/*
				 * On n'enregistre que si pour un eleve donne le coupe (heure justifie, heure non justifie)#(0,0)
				 * ie au moins l'une des 2 valeurs non nul
				 */
				if(nbreHJustifie[i]>0 || nbreHNJustifie[i]>0){
					
						if(date_enreg.compareTo(date_defaut)!=0 ){
							ret = sgService.saveRAbsenceSeqEleve(idEleves[i], idSequenceConcerne, nbreHJustifie[i], 
									nbreHNJustifie[i],date_enreg);
						}
						else{
							ret = sgService.saveRAbsenceSeqEleve(idEleves[i], idSequenceConcerne, nbreHJustifie[i], 
									nbreHNJustifie[i],new Date());
						}
				}
				ret=2;
			}
			
			if(ret == -3) return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceError"
													+ "&&idSequenceConcerne="+idSequenceConcerne
													+ "&&idClassesConcerne="+idClassesConcerne
													+ "&&dateenreg="+dateenreg;
			
			if(ret == -2) return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceDateError"
													+ "&&idSequenceConcerne="+idSequenceConcerne
													+ "&&idClassesConcerne="+idClassesConcerne
													+ "&&dateenreg="+dateenreg;
			
			if(ret == -1) return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceNegError"
													+ "&&idSequenceConcerne="+idSequenceConcerne
													+ "&&idClassesConcerne="+idClassesConcerne
													+ "&&dateenreg="+dateenreg;
			
			if(ret == 0) return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceErroneError"
													+ "&&idSequenceConcerne="+idSequenceConcerne
													+ "&&idClassesConcerne="+idClassesConcerne
													+ "&&dateenreg="+dateenreg;
			
		}
		catch(Exception e){
			//System.err.println("Exception généré "+e.getMessage());
			//e.printStackTrace();
			return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceErrorConvert"
					+ "&&idSequenceConcerne="+idSequenceConcerne
					+ "&&idClassesConcerne="+idClassesConcerne
					+ "&&dateenreg="+dateenreg;
		}
		
		
		return "redirect:/logesco/users/sg/getformSaisieRAbsences?updateAbsenceSuccess"
				+ "&&idSequenceConcerne="+idSequenceConcerne
				+ "&&idClassesConcerne="+idClassesConcerne
				+ "&&dateenreg="+dateenreg;
		
	}
	
	@GetMapping(path="/getupdateRDisciplinaire")
	public String getupdateRDisciplinaire(
			Model model, HttpServletRequest request,
			@RequestParam(name="idEleves", defaultValue="0") Long idEleves,
			@RequestParam(name="dateenreg", defaultValue="0") String dateenreg,
			@RequestParam(name="nbreperiode", defaultValue="0") String nbreperiode,
			@RequestParam(name="motif", defaultValue="0") String motif,
			@RequestParam(name="idSequenceConcerne", defaultValue="0") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne,
			@RequestParam(name="unite") String unite,
			@RequestParam(name="idsanctionAssocie", defaultValue="0") Long idsanctionAssocie){
		
		SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");
		int ret =0;
		try{
			Date date_enreg = spd.parse(dateenreg);
			int nbre_periode = Integer.parseInt(nbreperiode);
			ret = sgService.saveRDisciplineSeqEleve(idEleves, idSequenceConcerne, date_enreg, 
					nbre_periode, unite, motif, idsanctionAssocie);
			
			if(ret==0) return "redirect:/logesco/users/sg/getformSaisieRDiscipline?updateDisciplineErrornbreperiode"
												+ "&&idSequenceConcerne="+idSequenceConcerne
												+ "&&idClassesConcerne="+idClassesConcerne;
		}
		catch(Exception e){
			e.printStackTrace();
			return "redirect:/logesco/users/sg/getformSaisieRDiscipline?updateDisciplineErrorConvert"
					+ "&&idSequenceConcerne="+idSequenceConcerne
					+ "&&idClassesConcerne="+idClassesConcerne;
		}
		
		return "redirect:/logesco/users/sg/getformSaisieRDiscipline?updateDisciplineSuccess"
				+ "&&idSequenceConcerne="+idSequenceConcerne
				+ "&&idClassesConcerne="+idClassesConcerne;
		
	}
	
	
	@GetMapping(path="/getsupprimRDisciplinaire")
	public String getsupprimRDisciplinaire(
			Model model, HttpServletRequest request,
			@RequestParam(name="idRdisc", defaultValue="0") Long idRdisc,
			@RequestParam(name="idSequenceConcerne", defaultValue="0") Long idSequenceConcerne,
			@RequestParam(name="idClassesConcerne", defaultValue="0") Long idClassesConcerne){
		
		sgService.deleteRapportDisciplinaire(idRdisc);
		
		return "redirect:/logesco/users/sg/getformSaisieRDiscipline?supprimDisciplineSuccess"
				+ "&&idSequenceConcerne="+idSequenceConcerne
				+ "&&idClassesConcerne="+idClassesConcerne;
	}

	

	
	
////////////////////////////////////FIN DES REQUETES DE TYPE GET ////////////////////////////////////////////

}
