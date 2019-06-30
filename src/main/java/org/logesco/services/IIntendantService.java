/**
 * 
 */
package org.logesco.services;

/**
 * @author cedrickiadjeu
 *
 */
public interface IIntendantService {

	/*****************************************************************************
	 *  * Methode qui enregistre le versement de la scolarite d'un élève
	 * Cette méthode retourne 
	 * Long: c'est l'identifiant de l'operation qui vient de se réaliser sur le compte de l'élève
	 * -1 si il ya une erreur quelconque
	 * @param idEleveConcerne
	 * @param montantAVerser
	 * @return
	 */
	public Long enregVersementSco(Long idEleveConcerne, double montantAVerser);
	
	/*******************************************************************
	 *  * Cette fonction retourne l'identifiant de l'operation qui doit apparaitre sur le 
	 * recu de versement
	 * @param idOperation
	 * @return
	 */
	public String getIdentifiantOperation(Long idOperation);
	
	/************************************************************************
	 * Cette fonction retourne le montant d'une operation donc l'identifiant est passe en parametre
	 * @param idOperation
	 * @return
	 */
	public double getMontantOperation(Long idOperation);
	
}
