/**
 * 
 */
package org.logesco.dao;

import org.logesco.entities.RapportDAbsence;
import org.logesco.entities.RapportDisciplinaire;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cedrickiadjeu
 *
 */
public interface RapportDisciplinaireRepository extends JpaRepository<RapportDisciplinaire, Long> {
	public RapportDAbsence findByEleveIdElevesAndSequenceIdPeriodes(Long idEleves, Long idPeriodes);
}
