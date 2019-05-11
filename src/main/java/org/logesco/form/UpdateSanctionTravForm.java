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
public class UpdateSanctionTravForm {

	@NotNull
	@NotEmpty
	private String codeSancTrav;
	@NotNull
	@NotEmpty
	private String intituleSancTrav;
	@NotNull
	@NotEmpty
	private String intituleSancTravEn;
	@NotNull
	@NotEmpty
	private String codeSancTravEn;
	
	private Long idSanctionTrav;
	
	/**
	 * 
	 */
	public UpdateSanctionTravForm() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the codeSancTrav
	 */
	public String getCodeSancTrav() {
		return codeSancTrav;
	}

	/**
	 * @param codeSancTrav the codeSancTrav to set
	 */
	public void setCodeSancTrav(String codeSancTrav) {
		this.codeSancTrav = codeSancTrav;
	}

	/**
	 * @return the intituleSancTrav
	 */
	public String getIntituleSancTrav() {
		return intituleSancTrav;
	}

	/**
	 * @param intituleSancTrav the intituleSancTrav to set
	 */
	public void setIntituleSancTrav(String intituleSancTrav) {
		this.intituleSancTrav = intituleSancTrav;
	}

	/**
	 * @return the intituleSancTravEn
	 */
	public String getIntituleSancTravEn() {
		return intituleSancTravEn;
	}

	/**
	 * @param intituleSancTravEn the intituleSancTravEn to set
	 */
	public void setIntituleSancTravEn(String intituleSancTravEn) {
		this.intituleSancTravEn = intituleSancTravEn;
	}

	/**
	 * @return the codeSancTravEn
	 */
	public String getCodeSancTravEn() {
		return codeSancTravEn;
	}

	/**
	 * @param codeSancTravEn the codeSancTravEn to set
	 */
	public void setCodeSancTravEn(String codeSancTravEn) {
		this.codeSancTravEn = codeSancTravEn;
	}

	/**
	 * @return the idSanctionTrav
	 */
	public Long getIdSanctionTrav() {
		return idSanctionTrav;
	}

	/**
	 * @param idSanctionTrav the idSanctionTrav to set
	 */
	public void setIdSanctionTrav(Long idSanctionTrav) {
		this.idSanctionTrav = idSanctionTrav;
	}
	
	

}
