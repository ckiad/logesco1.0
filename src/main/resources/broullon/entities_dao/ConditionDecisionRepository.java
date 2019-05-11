/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.ConditionDecision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface ConditionDecisionRepository extends JpaRepository<ConditionDecision, Long> {
	@Query("SELECT cd FROM ConditionDecision cd WHERE "
			+ " cd.periodeAssocie.idPeriodes=:idPeriodes  "
			+ " AND "
			+ " cd.classeAssocie.idClasses=:idClasses"
			+ " ORDER BY cd.moyenneMin")
	public List<ConditionDecision> findListofConditionDecisionDeClassePourSeq(
			@Param("idClasses") Long idClasses, @Param("idPeriodes") Long idPeriodes);
}
