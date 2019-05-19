/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.SanctionDisciplinaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cedrickiadjeu
 *
 */
public interface SanctionDisciplinaireRepository extends JpaRepository<SanctionDisciplinaire, Long> {
	public Page<SanctionDisciplinaire> findAllByOrderByNiveauSeveriteAscCodeSancDiscAscIntituleSancDiscAsc(Pageable pageable);
	
	public List<SanctionDisciplinaire> findAllByOrderByNiveauSeveriteAscCodeSancDiscAscIntituleSancDiscAsc();
	
	
	public SanctionDisciplinaire findByCodeSancDisc(String codeSancDisc);
	
	public SanctionDisciplinaire findByCodeSancDiscEn(String codeSancDiscEn);
	
	public SanctionDisciplinaire findByNiveauSeverite(int niveau);
}
