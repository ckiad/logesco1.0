/**
 * 
 */
package org.logesco.services;

import java.util.Date;

import org.logesco.dao.CompteInscriptionRepository;
import org.logesco.dao.ElevesRepository;
import org.logesco.dao.OperationsRepository;
import org.logesco.entities.Eleves;
import org.logesco.entities.Operations;
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
public class IntendantServiceImplementation implements IIntendantService {

	@Value("${dir.images.eleves}")
	private String photoElevesDir;
	
	@Value("${dir.images.personnels}")
	private String photoPersonnelsDir;
	
	
	@Autowired
	private ElevesRepository elevesRepository;

	@Autowired
	private CompteInscriptionRepository compteinscriptionRepository;

	@Autowired
	private OperationsRepository operationsRepository;

	@Autowired
	private IUsersService usersService;
	
	/**
	 * 
	 */
	public IntendantServiceImplementation() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Long enregVersementSco(Long idEleveConcerne, double montantAVerser) {
		// TODO Auto-generated method stub

		////System.err.println("debut de enregVersementSco ");
		/****************
		 * Si le montant est negatif c'est qu'on effectue un retrait
		 */
		//if(montantAVerser < 0) montantAVerser *= -1;
		/*
		 * Rechercher l'élève dont on a l'id
		 * Recuperer son compte associe
		 * Ajouter le montantAVerser au montant de son compte
		 * Si le montant total atteint le montantScolarite alors on change son  etat en le plaçant à inscrit
		 * Si ce montant est supérieur à 0 alors on change son etat en le plaçant à en cours
		 * Donc par défaut cet etat reste à non inscrit
		 */
		////System.err.println("recherche de l'élève");
		Eleves eleveConcerne = usersService.findEleves(idEleveConcerne);

		if(eleveConcerne==null){
			return new Long(-1);
		}

		double montantdejaverse = eleveConcerne.getCompteInscription().getMontant();
		double nouveauMontant=montantdejaverse+montantAVerser;
		/*
		 * Il faut se rassurer que le nouveau montant ne sera pas supérieur au montant de la scolarité de la classe concerne
		 */
		if(nouveauMontant <= eleveConcerne.getClasse().getMontantScolarite() && nouveauMontant>=0){
			eleveConcerne.getCompteInscription().setMontant(nouveauMontant);

			//System.out.println("enregistrement du nouveau montant");
			compteinscriptionRepository.save(eleveConcerne.getCompteInscription());

			/*
			 * Il faut maintenant enregistrer cette opération de versement
			 */
			Operations operation = new Operations();
			operation.setCompteinscription(eleveConcerne.getCompteInscription());
			operation.setDateOperation(new Date());
			operation.setIdentifiantOperation(usersService.getNumeroOperation());
			operation.setMontantOperation(montantAVerser);
			operation.setTypeOperation("versement");
			
			//System.out.println("enregistrement de l'opération");

			Operations op_realise = operationsRepository.save(operation);
			//System.out.println("enregistrement de l'opération effectivement effectue");

			if(nouveauMontant >= eleveConcerne.getClasse().getMontantScolarite()) {
				eleveConcerne.setEtatInscEleves("inscrit");
				elevesRepository.save(eleveConcerne);
				return op_realise.getIdOperation();
			}
			if(nouveauMontant >= 0) {
				eleveConcerne.setEtatInscEleves("en cours");
				elevesRepository.save(eleveConcerne);
				return op_realise.getIdOperation();
			}
			//System.out.println("Mise à jour de l'élève effectivement effectue");
		}
		else{
			return new Long(-2);
		}
		return new Long(0);
	
	}

	@Override
	public String getIdentifiantOperation(Long idOperation) {
		// TODO Auto-generated method stub
		Operations op = usersService.findOperation(idOperation);
		if(op==null) return "";
		return op.getIdentifiantOperation();
	
	}

	@Override
	public double getMontantOperation(Long idOperation) {
		// TODO Auto-generated method stub
				Operations op = usersService.findOperation(idOperation);
				if(op==null) return 0;
				return op.getMontantOperation();
	}

}
