/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.Decision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cedrickiadjeu
 *
 */
public interface DecisionRepository extends JpaRepository<Decision, Long> {
	public Page<Decision> findAllByOrderByCodeDecisionAscIntituleDecisionAsc(Pageable pageable);
	
	public List<Decision> findAllByOrderByCodeDecisionAscIntituleDecisionAsc();
	
	public Decision findByCodeDecision(String codeDecision);
	
	public Decision findByCodeDecisionEn(String codeDecisionEn);
}
