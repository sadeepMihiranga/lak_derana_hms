package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<TMsDepartment, String> {

    TMsDepartment findByDpmtCodeAndDpmtStatus(String dpmtCode, Short dpmtStatus);

    List<TMsDepartment> findAllByDpmtStatus(Short dpmtStatus);
}
