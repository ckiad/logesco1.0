/**
 * 
 */
package org.logesco.dao;

import java.util.Date;
import java.util.List;

import org.logesco.entities.Operations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface OperationsRepository extends JpaRepository<Operations, Long> {
	@Query("SELECT op FROM Operations op WHERE op.dateOperation>=:datemin AND op.dateOperation<=:datemax "
			+ " ORDER BY op.dateOperation ASC")
	public Page<Operations> findPageOperations(@Param("datemin") Date datemin,	
			@Param("datemax") Date datemax, Pageable pageable);
	
	public List<Operations> findAllByOrderByDateOperationAsc();
	
	public List<Operations> findAllByOrderByDateOperationDesc();
	
	@Query("SELECT op FROM Operations op WHERE op.dateOperation>=:datemin "
			+ " ORDER BY op.dateOperation ASC")
	public List<Operations> findAllOperationAPartirDeDate(@Param("datemin") Date datemin);
	
	@Query("SELECT op FROM Operations op WHERE op.dateOperation>=:datemin AND op.dateOperation<=:datemax "
			+ " ORDER BY op.dateOperation ASC")
	public List<Operations> findAllOperationEntreDate(@Param("datemin") Date datemin, @Param("datemax") Date datemax);
	
	@Query("SELECT op FROM Operations op WHERE op.compteinscription.eleveProprietaire.idEleves=:idEleves "
			+ " ORDER BY op.dateOperation ASC")
	public List<Operations> findAllOperationEleve(@Param("idEleves") long idEleves);
	
	@Query("SELECT op FROM Operations op WHERE op.compteinscription.eleveProprietaire.idEleves=:idEleves "
			+ " ORDER BY op.dateOperation DESC")
	public List<Operations> findAllOperationEleveDesc(@Param("idEleves") long idEleves);
	
}
