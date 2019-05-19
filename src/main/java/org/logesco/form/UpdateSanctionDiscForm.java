/**
 * 
 */
package org.logesco.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author cedrickiadjeu
 *
 */
public class UpdateSanctionDiscForm  {

	@NotNull
	@NotEmpty
	private String codeSancDisc;
	@NotNull
	@NotEmpty
	private String intituleSancDisc;
	@NotNull
	@NotEmpty
	private String intituleSancDiscEn;
	@NotNull
	@NotEmpty
	private String codeSancDiscEn;
	@NotNull
	@NotEmpty
	private String niveauSeverite;
	
	private Long idSanctionDisc;
	
	/**
	 * 
	 */
	public UpdateSanctionDiscForm() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the intituleSancDisc
	 */
	public String getIntituleSancDisc() {
		return intituleSancDisc;
	}

	/**
	 * @param intituleSancDisc the intituleSancDisc to set
	 */
	public void setIntituleSancDisc(String intituleSancDisc) {
		this.intituleSancDisc = intituleSancDisc;
	}


	/**
	 * @return the intituleSancDiscEn
	 */
	public String getIntituleSancDiscEn() {
		return intituleSancDiscEn;
	}

	/**
	 * @param intituleSancDiscEn the intituleSancDiscEn to set
	 */
	public void setIntituleSancDiscEn(String intituleSancDiscEn) {
		this.intituleSancDiscEn = intituleSancDiscEn;
	}

	/**
	 * @return the codeSancDiscEn
	 */
	public String getCodeSancDiscEn() {
		return codeSancDiscEn;
	}

	/**
	 * @param codeSancDiscEn the codeSancDiscEn to set
	 */
	public void setCodeSancDiscEn(String codeSancDiscEn) {
		this.codeSancDiscEn = codeSancDiscEn;
	}



	

	/**
	 * @return the idSanctionDisc
	 */
	public Long getIdSanctionDisc() {
		return idSanctionDisc;
	}

	/**
	 * @param idSanctionDisc the idSanctionDisc to set
	 */
	public void setIdSanctionDisc(Long idSanctionDisc) {
		this.idSanctionDisc = idSanctionDisc;
	}

	/**
	 * @return the codeSancDisc
	 */
	public String getCodeSancDisc() {
		return codeSancDisc;
	}

	/**
	 * @param codeSancDisc the codeSancDisc to set
	 */
	public void setCodeSancDisc(String codeSancDisc) {
		this.codeSancDisc = codeSancDisc;
	}

	/**
	 * @return the niveauSeverite
	 */
	public String getNiveauSeverite() {
		return niveauSeverite;
	}

	/**
	 * @param niveauSeverite the niveauSeverite to set
	 */
	public void setNiveauSeverite(String niveauSeverite) {
		this.niveauSeverite = niveauSeverite;
	}
	
	
	

}
