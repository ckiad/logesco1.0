/**
 * 
 */
package org.logesco.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author cedrickiadjeu
 *
 */
@Entity
@Table(name="rapportDisciplinaire")
public class RapportDisciplinaire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idRdisc;
	@NotNull
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dateenreg;
	private int nbreperiode;
	private String unite;//jours ou heures days or hours RAS si nbreperiode =0
	private String motif;
	
	
	/******************************************
	 * Mapping des associations avec les autres tables
	 ******************************************/
	/*
	 * Association avec la table Sequences
	 ******************************************/
	@ManyToOne
	Sequences sequence;
	
	/*
	 * Association avec la table Eleves
	 ******************************************/
	@ManyToOne
	Eleves eleve;
	
	/*
	 * Association avec la table SanctionDisciplinaire
	 ******************************************/
	@ManyToOne
	SanctionDisciplinaire sanctionDisc;

	/**
	 * 
	 */
	public RapportDisciplinaire() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idRdisc
	 */
	public Long getIdRdisc() {
		return idRdisc;
	}

	/**
	 * @param idRdisc the idRdisc to set
	 */
	public void setIdRdisc(Long idRdisc) {
		this.idRdisc = idRdisc;
	}

	

	/**
	 * @return the nbreperiode
	 */
	public int getNbreperiode() {
		return nbreperiode;
	}

	/**
	 * @param nbreperiode the nbreperiode to set
	 */
	public void setNbreperiode(int nbreperiode) {
		this.nbreperiode = nbreperiode;
	}

	/**
	 * @return the unite
	 */
	public String getUnite() {
		return unite;
	}

	/**
	 * @param unite the unite to set
	 */
	public void setUnite(String unite) {
		this.unite = unite;
	}

	/**
	 * @return the motif
	 */
	public String getMotif() {
		return motif;
	}

	/**
	 * @param motif the motif to set
	 */
	public void setMotif(String motif) {
		this.motif = motif;
	}

	/**
	 * @return the sanctionDisc
	 */
	public SanctionDisciplinaire getSanctionDisc() {
		return sanctionDisc;
	}

	/**
	 * @param sanctionDisc the sanctionDisc to set
	 */
	public void setSanctionDisc(SanctionDisciplinaire sanctionDisc) {
		this.sanctionDisc = sanctionDisc;
	}

	/**
	 * @return the dateenreg
	 */
	public Date getDateenreg() {
		return dateenreg;
	}

	/**
	 * @param dateenreg the dateenreg to set
	 */
	public void setDateenreg(Date dateenreg) {
		this.dateenreg = dateenreg;
	}

	/**
	 * @return the sequence
	 */
	public Sequences getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Sequences sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the eleve
	 */
	public Eleves getEleve() {
		return eleve;
	}

	/**
	 * @param eleve the eleve to set
	 */
	public void setEleve(Eleves eleve) {
		this.eleve = eleve;
	}
	
	public String getRapportDisciplinaireString(String lang){
		if(this.sanctionDisc == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch= this.sanctionDisc.getIntituleSancDisc()+" ("+
					this.sanctionDisc.getCodeSancDisc()+")";
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			SimpleDateFormat spd = new SimpleDateFormat("dd-MM-yyyy");//"dd-MM-yyyy"
			String dateString = spd.format(this.dateenreg);
			ch+=dateString;
			
			return ch;
		}
		else{
			String ch=this.sanctionDisc.getIntituleSancDiscEn()+" ("+
					this.sanctionDisc.getCodeSancDiscEn()+")";
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			SimpleDateFormat spd = new SimpleDateFormat("yyyy-MM-dd");//"dd-MM-yyyy"
			String dateString = spd.format(this.dateenreg);
			ch+=dateString;
			
			return ch;
		}
	}
	
	public String getRapportDisciplinaireStringReduit(String lang){
		if(this.sanctionDisc == null) return " ";
		if(lang.equalsIgnoreCase("fr")==true){
			String ch = "";
			
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			ch+= this.sanctionDisc.getCodeSancDisc();
			
			return ch;
		}
		else{
			String ch = "";
			
			if(this.nbreperiode>0){
				ch+=" "+this.nbreperiode+" "+this.unite;
			}
			ch+= this.sanctionDisc.getCodeSancDiscEn();
			return ch;
		}
	}
	
	
	

}
