/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class OperationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int numero;
	private String numero_ordre;
	private String date_operation;
	private String eleve_concerne;
	private double montant;

	/**
	 * 
	 */
	public OperationBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the numero_ordre
	 */
	public String getNumero_ordre() {
		return numero_ordre;
	}

	/**
	 * @param numero_ordre the numero_ordre to set
	 */
	public void setNumero_ordre(String numero_ordre) {
		this.numero_ordre = numero_ordre;
	}

	

	/**
	 * @return the date_operation
	 */
	public String getDate_operation() {
		return date_operation;
	}

	/**
	 * @param date_operation the date_operation to set
	 */
	public void setDate_operation(String date_operation) {
		this.date_operation = date_operation;
	}

	

	/**
	 * @return the eleve_concerne
	 */
	public String getEleve_concerne() {
		return eleve_concerne;
	}

	/**
	 * @param eleve_concerne the eleve_concerne to set
	 */
	public void setEleve_concerne(String eleve_concerne) {
		this.eleve_concerne = eleve_concerne;
	}

	/**
	 * @return the montant
	 */
	public double getMontant() {
		return montant;
	}

	/**
	 * @param montant the montant to set
	 */
	public void setMontant(double montant) {
		this.montant = montant;
	}
	
	

}
