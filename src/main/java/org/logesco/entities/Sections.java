/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="sections")
public class Sections implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idSections;
	@NotNull
	@NotEmpty
	@Column(unique=true)
	private String codeSections;
	@NotNull
	@NotEmpty
	private String intituleSections;
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table Classes
	 ******************************************/
	@OneToMany(mappedBy="section")
	Collection<Classes> listofClasses;

	/**
	 * 
	 */
	public Sections() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param codeSections
	 * @param intituleSections
	 */
	public Sections(String codeSections, String intituleSections) {
		super();
		this.codeSections = codeSections;
		this.intituleSections = intituleSections;
	}

	/**
	 * @return the idSections
	 */
	public Long getIdSections() {
		return idSections;
	}

	/**
	 * @param idSections the idSections to set
	 */
	public void setIdSections(Long idSections) {
		this.idSections = idSections;
	}

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

	/**
	 * @return the listofClasses
	 */
	public Collection<Classes> getListofClasses() {
		return listofClasses;
	}

	/**
	 * @param listofClasses the listofClasses to set
	 */
	public void setListofClasses(Collection<Classes> listofClasses) {
		this.listofClasses = listofClasses;
	}
	
	

}
