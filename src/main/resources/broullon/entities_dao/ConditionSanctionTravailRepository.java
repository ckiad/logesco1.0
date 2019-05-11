/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.ConditionSanctionTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface ConditionSanctionTravailRepository extends JpaRepository<ConditionSanctionTravail, Long> {

	@Query("SELECT cst FROM ConditionSanctionTravail cst WHERE "
			+ " cst.periodeAssocie.idPeriodes=:idPeriodes  "
			+ " AND "
			+ " cst.classeAssocie.idClasses=:idClasses"
			+ " ORDER BY cst.moyenneMin")
	public List<ConditionSanctionTravail> findListofConditionSanctionTravailDeClassePourSeq(
			@Param("idClasses") Long idClasses, @Param("idPeriodes") Long idPeriodes);
	
}
