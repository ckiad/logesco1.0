/**
 * 
 */
package org.logesco.dao;

import java.util.List;

import org.logesco.entities.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author cedrickiadjeu
 *
 */
public interface RolesRepository extends JpaRepository<Roles, String> {
	@Query("SELECT r FROM Roles r ORDER BY r.role ASC")
	public Page<Roles> findAll(Pageable pageable);
	
	@Query("SELECT r FROM Roles r  ORDER BY r.role ASC")
	public List<Roles> findAll();
	
	@Query("SELECT r FROM Roles r WHERE r.role!=:roles ORDER BY r.role ASC")
	public List<Roles> findAll(@Param("roles") String roles);
	
	public Roles findByRole(String role);
}
