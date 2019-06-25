/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class FicheScolariteparClasseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String niveau;
	private String classe;
	private String montantscolarite;

	/**
	 * 
	 */
	public FicheScolariteparClasseBean() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the niveau
	 */
	public String getNiveau() {
		return niveau;
	}

	/**
	 * @param niveau the niveau to set
	 */
	public void setNiveau(String niveau) {
		this.niveau = niveau;
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
	 * @return the montantscolarite
	 */
	public String getMontantscolarite() {
		return montantscolarite;
	}

	/**
	 * @param montantscolarite the montantscolarite to set
	 */
	public void setMontantscolarite(String montantscolarite) {
		this.montantscolarite = montantscolarite;
	}
	
	

}
