/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.Proffesseurs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author cedrickiadjeu
 *
 */
public interface ProffesseursRepository extends JpaRepository<Proffesseurs	, Long> {
	public Proffesseurs findByNumcniPers(String numcniPers);
	
	public Proffesseurs findByIdUsers(Long idUsers);
	
	@Query("SELECT p FROM Proffesseurs p  ORDER BY p.nomsPers ASC, "
			+ " p.prenomsPers ASC,  p.datenaissPers ASC")
	public List<Proffesseurs> findAll();
	
}
