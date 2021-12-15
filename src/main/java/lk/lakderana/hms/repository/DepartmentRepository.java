package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<TMsDepartment, String> {

    TMsDepartment findByDpmtCodeAndDpmtStatus(String dpmtCode, Short dpmtStatus);
}
