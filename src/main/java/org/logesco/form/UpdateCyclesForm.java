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
public class UpdateCyclesForm {

	@NotNull
	@NotEmpty
	private String codeCycles;
	
	@NotNull
	private int numeroOrdreCycles;

	/**
	 * @return the codeCycles
	 */
	public String getCodeCycles() {
		return codeCycles;
	}

	/**
	 * @param codeCycles the codeCycles to set
	 */
	public void setCodeCycles(String codeCycles) {
		this.codeCycles = codeCycles;
	}

	/**
	 * @return the numeroOrdreCycles
	 */
	public int getNumeroOrdreCycles() {
		return numeroOrdreCycles;
	}

	/**
	 * @param numeroOrdreCycles the numeroOrdreCycles to set
	 */
	public void setNumeroOrdreCycles(int numeroOrdreCycles) {
		this.numeroOrdreCycles = numeroOrdreCycles;
	}
	
	
	
}
