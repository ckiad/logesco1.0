/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.PersonnelsDAppui;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cedrickiadjeu
 *
 */
public interface PersonnelsDAppuiRepository extends JpaRepository<PersonnelsDAppui, Long> {

	public PersonnelsDAppui findByNumcniPers(String numcniPers);
	
	public PersonnelsDAppui findByIdUsers(Long idUsers);
	
	public Page<PersonnelsDAppui> findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc(Pageable pageable);
	
	public List<PersonnelsDAppui> findAllByOrderByNomsPersAscPrenomsPersAscDatenaissPersDesc();
}
