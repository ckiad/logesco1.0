/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class FicheTitulaireparClasseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String titulaire;
	private String classe;
	private String adresse;

	/**
	 * 
	 */
	public FicheTitulaireparClasseBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the titulaire
	 */
	public String getTitulaire() {
		return titulaire;
	}

	/**
	 * @param titulaire the titulaire to set
	 */
	public void setTitulaire(String titulaire) {
		this.titulaire = titulaire;
	}

	/**
	 * @return the classe
	 */
	public String getClasse() {
		return classe;
	}

	/**
	 * @param classe the classe to set
	 */
	public void setClasse(String classe) {
		this.classe = classe;
	}

	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	

}
