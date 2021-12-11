package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<TMsRole, Long> {

    TMsRole findByRoleName(String roleName);
}
