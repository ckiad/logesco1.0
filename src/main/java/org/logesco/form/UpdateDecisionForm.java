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
public class UpdateDecisionForm {

	private Long idDecision;
	
	@NotNull
	@NotEmpty
	private String codeDecision;
	@NotNull
	@NotEmpty
	private String intituleDecision;
	@NotNull
	@NotEmpty
	private String intituleDecisionEn;
	@NotNull
	@NotEmpty
	private String codeDecisionEn;
	/* 
	 * En ce qui concerne le champ directiondecision il va permettre de savoir quand est ce que le code
	 * de la decision signifie admis en classe supérieur et quand est ce qu'il signifie redouble ou exclu
	 * Pour toutes les décisions désignant une admission en classe supérieur il vaudra 1
	 * Pour toutes les décisions désignant un redoublement, il vaudra 0
	 * Pour toutes autres type de décision il vaudra -1
	 * */
	@NotNull
	private int directionDecision;
	/**
	 * 
	 */
	public UpdateDecisionForm() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the idDecision
	 */
	public Long getIdDecision() {
		return idDecision;
	}
	/**
	 * @param idDecision the idDecision to set
	 */
	public void setIdDecision(Long idDecision) {
		this.idDecision = idDecision;
	}
	/**
	 * @return the codeDecision
	 */
	public String getCodeDecision() {
		return codeDecision;
	}
	/**
	 * @param codeDecision the codeDecision to set
	 */
	public void setCodeDecision(String codeDecision) {
		this.codeDecision = codeDecision;
	}
	/**
	 * @return the intituleDecision
	 */
	public String getIntituleDecision() {
		return intituleDecision;
	}
	/**
	 * @param intituleDecision the intituleDecision to set
	 */
	public void setIntituleDecision(String intituleDecision) {
		this.intituleDecision = intituleDecision;
	}
	/**
	 * @return the intituleDecisionEn
	 */
	public String getIntituleDecisionEn() {
		return intituleDecisionEn;
	}
	/**
	 * @param intituleDecisionEn the intituleDecisionEn to set
	 */
	public void setIntituleDecisionEn(String intituleDecisionEn) {
		this.intituleDecisionEn = intituleDecisionEn;
	}
	/**
	 * @return the codeDecisionEn
	 */
	public String getCodeDecisionEn() {
		return codeDecisionEn;
	}
	/**
	 * @param codeDecisionEn the codeDecisionEn to set
	 */
	public void setCodeDecisionEn(String codeDecisionEn) {
		this.codeDecisionEn = codeDecisionEn;
	}
	/**
	 * @return the directionDecision
	 */
	public int getDirectionDecision() {
		return directionDecision;
	}
	/**
	 * @param directionDecision the directionDecision to set
	 */
	public void setDirectionDecision(int directionDecision) {
		this.directionDecision = directionDecision;
	}
	
	
	

}
