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
public class UpdateSectionsForm {
	@NotNull
	@NotEmpty
	private String codeSections;
	@NotNull
	@NotEmpty
	private String intituleSections;
	
	/**
	 * @return the codeSections
	 */
	public String getCodeSections() {
		return codeSections;
	}
	/**
	 * @param codeSections the codeSections to set
	 */
	public void setCodeSections(String codeSections) {
		this.codeSections = codeSections;
	}
	/**
	 * @return the intituleSections
	 */
	public String getIntituleSections() {
		return intituleSections;
	}
	/**
	 * @param intituleSections the intituleSections to set
	 */
	public void setIntituleSections(String intituleSections) {
		this.intituleSections = intituleSections;
	}
	
	
	
}
