/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.ConditionSanctionDisciplinaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface ConditionSanctionDisciplinaireRepository extends JpaRepository<ConditionSanctionDisciplinaire, Long> {
	
	@Query("SELECT csd FROM ConditionSanctionDisciplinaire csd WHERE "
			+ " csd.periodeAssocie.idPeriodes=:idPeriodes  "
			+ " AND "
			+ " csd.classeAssocie.idClasses=:idClasses"
			+ " ORDER BY csd.absenceMin")
	public List<ConditionSanctionDisciplinaire> findListofConditionSanctionDisciplinaireDeClassePourSeq(
			@Param("idClasses") Long idClasses, @Param("idPeriodes") Long idPeriodes);
	
}
