/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class FicheAbsenceEleveBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int numero;
	String noms_prenoms;
	String absNJ;
	String absJ;
	

	/**
	 * 
	 */
	public FicheAbsenceEleveBean() {
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
	 * @return the absNJ
	 */
	public String getAbsNJ() {
		return absNJ;
	}


	/**
	 * @param absNJ the absNJ to set
	 */
	public void setAbsNJ(String absNJ) {
		this.absNJ = absNJ;
	}


	/**
	 * @return the absJ
	 */
	public String getAbsJ() {
		return absJ;
	}


	/**
	 * @param absJ the absJ to set
	 */
	public void setAbsJ(String absJ) {
		this.absJ = absJ;
	}
	
	

}
