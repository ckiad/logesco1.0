/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="identoperation")
public class IdentOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idIdentOperation;
	@NotNull
	private int numero;

	/**
	 * 
	 */
	public IdentOperation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idIdentOperation
	 */
	public Long getIdIdentOperation() {
		return idIdentOperation;
	}

	/**
	 * @param idIdentOperation the idIdentOperation to set
	 */
	public void setIdIdentOperation(Long idIdentOperation) {
		this.idIdentOperation = idIdentOperation;
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
	
	

}
