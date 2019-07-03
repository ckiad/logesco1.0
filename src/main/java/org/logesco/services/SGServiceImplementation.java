/**
 * 
 */
package org.logesco.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.logesco.dao.ElevesRepository;
import org.logesco.dao.RapportDAbsenceRepository;
import org.logesco.dao.RapportDisciplinaireRepository;
import org.logesco.entities.Annee;
import org.logesco.entities.Classes;
import org.logesco.entities.Eleves;
import org.logesco.entities.RapportDAbsence;
import org.logesco.entities.RapportDisciplinaire;
import org.logesco.entities.SanctionDisciplinaire;
import org.logesco.entities.Sequences;
import org.logesco.entities.Trimestres;
import org.logesco.modeles.FicheAbsenceEleveBean;
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
public class SGServiceImplementation implements ISGService {

	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	@Autowired
	private RapportDAbsenceRepository	 rapportDAbsenceRepository;
	
		
	@Autowired
	private ElevesRepository elevesRepository;

		
	@Autowired
	private IUsersService usersService;
	
	@Autowired
	private RapportDisciplinaireRepository	 rapportdisciplinaireRepository;
	
	/**
	 * 
	 */
	public SGServiceImplementation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int saveRAbsenceSeqEleve(Long idEleves, Long idSequence, int nbreHJ, 
			int nbreHNJ, Date date_enreg) {
		// TODO Auto-generated method stub
		Eleves eleveConcerne = usersService.findEleves(idEleves);
		Sequences sequenceConcerne = usersService.findSequences(idSequence);
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

		// TODO Auto-generated method stub
		Eleves eleve = usersService.findEleves(idEleves);
		Sequences sequence = usersService.findSequences(idSequence);
		SanctionDisciplinaire sanctionDisc = usersService.findSanctionDisciplinaire(idSanctionDisc);
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
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveBean(Classes classe, 
			Date datemin, Date datemax){
		
		List<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = 
				new ArrayList<FicheAbsenceEleveBean>();
		
		if(classe == null){
			return null;
		}
		
		for(Eleves elv : classe.getListofEleves()){
			int absJ = elv.getNbreHeureAbsenceJustifie(datemin, datemax);
			int absNJ = elv.getNbreHeureAbsenceNonJustifie(datemin, datemax);
			String noms_prenoms = elv.getNomsEleves()+" "+elv.getPrenomsEleves();
			int numero = elv.getNumero((List<Eleves>) classe.getListofEleves());
			FicheAbsenceEleveBean faeb = new FicheAbsenceEleveBean();
			faeb.setNumero(numero);
			faeb.setNoms_prenoms(noms_prenoms);
			faeb.setAbsNJ(absNJ+" h");
			faeb.setAbsJ(absJ+" h");
			
			listofFicheAbsenceEleveBean.add(faeb);
		}
		
		
		return listofFicheAbsenceEleveBean;
	}
	
	
	@Override
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveSeqBean(Classes classe, 
			Sequences periode){
		List<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = 
				new ArrayList<FicheAbsenceEleveBean>();
		
		if(classe == null){
			return null;
		}
		
		for(Eleves elv : classe.getListofEleves()){
			int absJ = elv.getNbreHeureAbsenceJustifie(periode.getIdPeriodes());
			int absNJ = elv.getNbreHeureAbsenceNonJustifie(periode.getIdPeriodes());
			String noms_prenoms = elv.getNomsEleves()+" "+elv.getPrenomsEleves();
			int numero = elv.getNumero((List<Eleves>) classe.getListofEleves());
			FicheAbsenceEleveBean faeb = new FicheAbsenceEleveBean();
			faeb.setNumero(numero);
			faeb.setNoms_prenoms(noms_prenoms);
			faeb.setAbsNJ(absNJ+" h");
			faeb.setAbsJ(absJ+" h");
			
			listofFicheAbsenceEleveBean.add(faeb);
		}
		
		return listofFicheAbsenceEleveBean;
	}
	
	
	@Override
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveTrimBean(Classes classe, 
			Trimestres periode){
		List<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = 
				new ArrayList<FicheAbsenceEleveBean>();
		
		if(classe == null){
			return null;
		}
		
		for(Eleves elv : classe.getListofEleves()){
			int absJ = elv.getNbreHeureAbsenceJustifieTrim(periode);
			int absNJ = elv.getNbreHeureAbsenceNonJustifieTrim(periode);
			String noms_prenoms = elv.getNomsEleves()+" "+elv.getPrenomsEleves();
			int numero = elv.getNumero((List<Eleves>) classe.getListofEleves());
			FicheAbsenceEleveBean faeb = new FicheAbsenceEleveBean();
			faeb.setNumero(numero);
			faeb.setNoms_prenoms(noms_prenoms);
			faeb.setAbsNJ(absNJ+" h");
			faeb.setAbsJ(absJ+" h");
			
			listofFicheAbsenceEleveBean.add(faeb);
		}
		
		return listofFicheAbsenceEleveBean;
	}
	
	
	@Override
	public Collection<FicheAbsenceEleveBean> generateListFicheAbsenceEleveAnBean(Classes classe, 
			Annee periode){
		List<FicheAbsenceEleveBean> listofFicheAbsenceEleveBean = 
				new ArrayList<FicheAbsenceEleveBean>();
		
		if(classe == null){
			return null;
		}
		
		for(Eleves elv : classe.getListofEleves()){
			int absJ = elv.getNbreHeureAbsenceJustifieAnnee(periode);
			int absNJ = elv.getNbreHeureAbsenceNonJustifieAnnee(periode);
			String noms_prenoms = elv.getNomsEleves()+" "+elv.getPrenomsEleves();
			int numero = elv.getNumero((List<Eleves>) classe.getListofEleves());
			FicheAbsenceEleveBean faeb = new FicheAbsenceEleveBean();
			faeb.setNumero(numero);
			faeb.setNoms_prenoms(noms_prenoms);
			faeb.setAbsNJ(absNJ+" h");
			faeb.setAbsJ(absJ+" h");
			
			listofFicheAbsenceEleveBean.add(faeb);
		}
		
		return listofFicheAbsenceEleveBean;
	}
	

}
