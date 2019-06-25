/**
 * 
 */
package org.logesco.modeles;

import java.io.Serializable;

/**
 * @author cedrickiadjeu
 *
 */
public class FicheRecapAbsenceNiveauBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String niveau;
	String nbreabsfeminin;
	String nbreabsmasculin;
	String nbreabstotal;
	
	/**
	 * 
	 */
	public FicheRecapAbsenceNiveauBean() {
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
	 * @return the nbreabsfeminin
	 */
	public String getNbreabsfeminin() {
		return nbreabsfeminin;
	}

	/**
	 * @param nbreabsfeminin the nbreabsfeminin to set
	 */
	public void setNbreabsfeminin(String nbreabsfeminin) {
		this.nbreabsfeminin = nbreabsfeminin;
	}

	/**
	 * @return the nbreabsmasculin
	 */
	public String getNbreabsmasculin() {
		return nbreabsmasculin;
	}

	/**
	 * @param nbreabsmasculin the nbreabsmasculin to set
	 */
	public void setNbreabsmasculin(String nbreabsmasculin) {
		this.nbreabsmasculin = nbreabsmasculin;
	}

	/**
	 * @return the nbreabstotal
	 */
	public String getNbreabstotal() {
		return nbreabstotal;
	}

	/**
	 * @param nbreabstotal the nbreabstotal to set
	 */
	public void setNbreabstotal(String nbreabstotal) {
		this.nbreabstotal = nbreabstotal;
	}

	
	
}
