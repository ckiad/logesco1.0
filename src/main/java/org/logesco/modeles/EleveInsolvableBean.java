/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class EleveInsolvableBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int numero;
	String noms_prenoms;
	String date_lieunaissance;
	String montant_verse;

	/**
	 * 
	 */
	public EleveInsolvableBean() {
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
	 * @return the noms_prenoms
	 */
	public String getNoms_prenoms() {
		return noms_prenoms;
	}

	/**
	 * @param noms_prenoms the noms_prenoms to set
	 */
	public void setNoms_prenoms(String noms_prenoms) {
		this.noms_prenoms = noms_prenoms;
	}

	/**
	 * @return the date_lieunaissance
	 */
	public String getDate_lieunaissance() {
		return date_lieunaissance;
	}

	/**
	 * @param date_lieunaissance the date_lieunaissance to set
	 */
	public void setDate_lieunaissance(String date_lieunaissance) {
		this.date_lieunaissance = date_lieunaissance;
	}

	/**
	 * @return the montant_verse
	 */
	public String getMontant_verse() {
		return montant_verse;
	}

	/**
	 * @param montant_verse the montant_verse to set
	 */
	public void setMontant_verse(String montant_verse) {
		this.montant_verse = montant_verse;
	}
	
	

}
