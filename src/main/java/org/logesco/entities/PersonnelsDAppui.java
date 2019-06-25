/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="personnelsDAppui")
@DiscriminatorValue("personnelsDAppui")
public class PersonnelsDAppui extends Personnels implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PersonnelsDAppui() {
		// TODO Auto-generated constructor stub
	}

}
