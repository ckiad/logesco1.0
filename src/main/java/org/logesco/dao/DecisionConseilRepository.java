/**
 * 
 */
package org.logesco.dao;



import org.logesco.entities.DecisionConseil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface DecisionConseilRepository extends JpaRepository<DecisionConseil, Long> {
	
	@Query("SELECT dc FROM DecisionConseil dc WHERE dc.eleveConcerne.idEleves=:idEleves "
			+ " AND dc.periodeConcerne.idPeriodes=:idPeriode")
	public DecisionConseil findDecisionConseilPeriode(@Param("idEleves") Long idEleves, 
			@Param("idPeriode") Long idPeriode);
	
}
