/**
 * 
 */
package org.logesco.dao;

import java.util.Date;
import java.util.List;

import org.logesco.entities.Personnels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface PersonnelsRepository extends JpaRepository<Personnels, Long> {
	
	public Personnels findByNumcniPers(String numcniPers);
	
	public Personnels findByMatriculePers(String matriculePers);
	
	public Personnels findByNomsPersAndPrenomsPersAndDatenaissPers(
			String nomPers, String prenomsPers, Date datenaisspers);
	
	@Query("SELECT p FROM Personnels p WHERE p.statutPers=:statutPers ORDER BY p.nomsPers ASC, "
			+ " p.prenomsPers ASC,  p.datenaissPers ASC")
	public List<Personnels> findAllPersonnelsDeStatut(@Param("statutPers") String statutPers);
	
	@Query("SELECT p FROM Personnels p  ORDER BY p.nomsPers ASC, "
			+ " p.prenomsPers ASC,  p.datenaissPers ASC")
	public List<Personnels> findAll();
	
}
