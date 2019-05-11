/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.SanctionTravail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cedrickiadjeu
 *
 */
public interface SanctionTravailRepository extends JpaRepository<SanctionTravail, Long> {
	public Page<SanctionTravail> findAllByOrderByCodeSancTravAscIntituleSancTravAsc(Pageable pageable);
	
	public List<SanctionTravail> findAllByOrderByCodeSancTravAscIntituleSancTravAsc();
	
	public SanctionTravail findByCodeSancTrav(String codeSancTrav);
	
	public SanctionTravail findByCodeSancTravEn(String codeSancTravEn);
}
