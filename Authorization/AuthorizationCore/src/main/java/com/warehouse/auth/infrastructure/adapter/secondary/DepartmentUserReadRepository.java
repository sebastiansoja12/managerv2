package com.warehouse.auth.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.DepartmentEntity;

public interface DepartmentUserReadRepository extends JpaRepository<DepartmentEntity, String> {
	@Query("""
			    SELECT CASE WHEN COUNT(d) > 0 THEN TRUE ELSE FALSE END
			    FROM user.DepartmentEntity d
			    WHERE d.departmentCode = :departmentCode AND d.status = 'ACTIVE'
			""")
	boolean existsByDepartmentCode(final String departmentCode);
}
